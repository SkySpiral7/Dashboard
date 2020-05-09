package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.ForecastRetrofitCaller;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class StormModule {
    @Provides
    @Reusable
    public static Gson gson() {
        return new Gson();
    }

    @Provides
    @Singleton
    public static OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    public static Retrofit retrofit() {
        return new Retrofit.Builder()
                //TODO: test empty string and http (so says it won't work)
                .baseUrl("http://localhost/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public static ForecastRetrofitCaller forecastRetrofitCaller(Retrofit retrofit) {
        return retrofit.create(ForecastRetrofitCaller.class);
    }
}
