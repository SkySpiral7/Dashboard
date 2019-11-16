package com.example.e449ps.stormy.dagger;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;

import static org.mockito.Mockito.mock;

@Module
public class TestStormModule {
    @Provides
    @Reusable
    public static Gson gson() {
        //Mockito can't mock it because it's a final class and has no interface or super class.
        //Real version shouldn't hurt.
        return new Gson();
    }

    @Provides
    @Singleton
    public static OkHttpClient okHttpClient() {
        return mock(OkHttpClient.class);
    }
}
