package com.example.e449ps.stormy;

import android.util.Log;

import com.example.e449ps.stormy.activity.MainActivity;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Reusable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Reusable
public class ForecastService {
    private static final String TAG = ForecastService.class.getSimpleName();
    private static final String darkSkyBaseUrl = "https://api.darksky.net";
    private static final String apiKey = "61d409957e62d46af62a7a99618d5141";

    private OkHttpClient client;
    private OkResponseConverter okResponseConverter;

    @Inject
    public ForecastService(OkHttpClient client, OkResponseConverter okResponseConverter) {
        this.client = client;
        this.okResponseConverter = okResponseConverter;
    }

    public interface StringConsumer {
        void accept(String responseBodyString);
    }

    public void getForecast(final MainActivity caller, final StringConsumer consumer, double latitude, double longitude) {
        if (!caller.isNetworkConnected()) {
            caller.showNetworkErrorDialog();
            return;
        }
        String forecastUrl = darkSkyBaseUrl + "/forecast/" + apiKey + "/" + latitude + "," + longitude;
        Log.i(TAG, "GET " + forecastUrl);
        Request request = new Request.Builder()
                .url(forecastUrl)
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "internet call failed", e);
                caller.alertUserAboutError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    String httpResponseAsString = okResponseConverter.httpResponseAsString(response, responseBodyString);
                    Log.i(TAG, httpResponseAsString);
                    if (response.isSuccessful()) consumer.accept(responseBodyString);
                    else caller.alertUserAboutError();
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Not json?", e);
                    caller.alertUserAboutError();
                }
            }
        });
    }
}
