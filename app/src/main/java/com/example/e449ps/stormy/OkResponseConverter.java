package com.example.e449ps.stormy;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import dagger.Reusable;
import okhttp3.Headers;
import okhttp3.Response;

@Reusable
public class OkResponseConverter {
    @Inject
    public OkResponseConverter() {
    }

    @NonNull
    public String httpResponseAsString(Response response, String responseBody) {
        StringBuilder responseMessageBuilder = new StringBuilder();
        responseMessageBuilder.append("HTTP Status: ").append(response.code()).append("\n");

        Headers responseHeaders = response.headers();
        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
            responseMessageBuilder
                    .append(responseHeaders.name(i))
                    .append(": ")
                    .append(responseHeaders.value(i))
                    .append("\n");
        }

        responseMessageBuilder.append(responseBody);
        return responseMessageBuilder.toString();
    }
}
