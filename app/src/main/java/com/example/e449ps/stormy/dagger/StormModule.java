package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.ForecastRetrofitCaller;
import com.example.e449ps.stormy.MyLoggingInterceptor;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class StormModule {
    private static final String DARK_SKY_RETROFIT_NAME = "Dark Sky retrofit";
    private static final String DARK_SKY_API_KEY = "61d409957e62d46af62a7a99618d5141";

    @Provides
    @Reusable
    public static Gson gson() {
        return new Gson();
    }

    @Provides
    @Reusable
    public static GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    public static OkHttpClient okHttpClient(MyLoggingInterceptor interceptor) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(interceptor);
        return httpClientBuilder.build();
    }

    @Provides
    @Reusable
    @Named(DARK_SKY_RETROFIT_NAME)
    public static Retrofit retrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl("https://api.darksky.net/forecast/" + DARK_SKY_API_KEY + "/")
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Reusable
    public static ForecastRetrofitCaller forecastRetrofitCaller(@Named(DARK_SKY_RETROFIT_NAME) Retrofit retrofit) {
        return retrofit.create(ForecastRetrofitCaller.class);
    }
}
