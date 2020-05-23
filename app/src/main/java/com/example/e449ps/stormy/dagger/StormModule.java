package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.ForecastRetrofitCaller;
import com.example.e449ps.stormy.MyLoggingInterceptor;
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
    public static OkHttpClient okHttpClient(MyLoggingInterceptor interceptor) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(interceptor);
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
    public static Retrofit retrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                //base URL is required (null and "" fail)
                .baseUrl("http://localhost/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public static ForecastRetrofitCaller forecastRetrofitCaller(Retrofit retrofit) {
        return retrofit.create(ForecastRetrofitCaller.class);
    }
}
