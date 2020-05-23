package com.example.e449ps.stormy;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Reusable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import timber.log.Timber;

/**
 * needed for timber. it's nice that the body is lower level and Timber won't cut off the body
 *
 * @see okhttp3.logging.HttpLoggingInterceptor
 */
//TODO: do I need to handle all the things HttpLoggingInterceptor does?
@Reusable
public class MyLoggingInterceptor implements Interceptor {
    @Inject
    public MyLoggingInterceptor() {
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Timber.i("--> %s %s%n%s", request.method(), request.url(), request.headers());
        String requestBody = requestBodyToString(request);
        if (requestBody == null) Timber.d("No request body");
        else Timber.v("Request body (%d):%n%s", requestBody.length(), requestBody);

        long start = System.nanoTime();
        Response response = chain.proceed(request);
        long end = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(end - start);

        ResponseBody responseBody = response.body();
        String responseBodyString = null;
        Response.Builder newResponseBuilder = response.newBuilder();
        if (responseBody != null) {
            responseBodyString = responseBodyToString(responseBody);

            // now we have extracted the response body but in the process
            // we have consumed the original response and can't read it again
            // so we need to build a new one to return from this method
            newResponseBuilder.body(ResponseBody.create(responseBody.source(), responseBody.contentType(), responseBody.contentLength()));
        }

        Timber.i("<-- %d %s (%dms)%n%s", response.code(), response.request().url(), duration, response.headers());
        if (responseBodyString == null) Timber.d("No response body");
        else
            Timber.v("Response body (%d):%n%s", responseBodyString.length(), responseBodyString);

        return newResponseBuilder.build();
    }

    private String requestBodyToString(final Request request) {
        if (request.body() == null) return null;
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return e.toString();
        }
    }

    private String responseBodyToString(final ResponseBody responseBody) throws IOException {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);  //buffer entire body. don't use contentLength which may be -1
        Charset charset = responseBody.contentType() == null ? StandardCharsets.UTF_8 : responseBody.contentType().charset(StandardCharsets.UTF_8);
        return source.getBuffer().clone().readString(charset);
    }
}
