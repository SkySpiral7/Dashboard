package com.example.e449ps.stormy.dagger;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StormProvider {
    @Provides
    @Singleton
    public Gson gson() {
        return new Gson();
    }
}
