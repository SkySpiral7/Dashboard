package com.example.e449ps.stormy;

import com.example.e449ps.stormy.activity.MainActivity;
import com.example.e449ps.stormy.model.darkSky.Forecast;

import javax.inject.Inject;

import dagger.Reusable;
import timber.log.Timber;

@Reusable
public class ForecastService {
    private ForecastRetrofitCaller forecastRetrofitCaller;

    @Inject
    public ForecastService(ForecastRetrofitCaller forecastRetrofitCaller) {
        this.forecastRetrofitCaller = forecastRetrofitCaller;
    }

    public interface ForecastConsumer {
        void accept(Forecast forecast);
    }

    public void getForecast(
            final MainActivity caller, final ForecastConsumer consumer, double latitude, double longitude) {
        if (!caller.isConnectedToInternet()) {
            caller.showInternetErrorDialog();
            return;
        }
        //TODO: add rx java
        //? https://codingwithmitch.com/blog/android-retrofit2-getting-started/
        //https://codingwithmitch.com/courses/rest-api-mvvm-retrofit2/
        //https://codingwithmitch.com/courses/rxjava-rxandroid-for-beginners/

        forecastRetrofitCaller.getForecast(latitude, longitude).enqueue(new retrofit2.Callback<Forecast>() {
            @Override
            public void onResponse(retrofit2.Call<Forecast> call, retrofit2.Response<Forecast> response) {
                Forecast forecast = response.body();
                if (response.isSuccessful()) consumer.accept(forecast);
                else caller.alertUserAboutError();
            }

            @Override
            public void onFailure(retrofit2.Call<Forecast> call, Throwable t) {
                Timber.e(t, "internet call failed");
                caller.alertUserAboutError();
            }
        });
    }
}
