package com.example.e449ps.stormy.dagger;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;

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
}
