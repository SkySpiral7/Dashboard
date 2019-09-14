package com.example.e449ps.stormy.dagger;

import android.app.Application;

public class StormApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Dagger.setDagger(
                DaggerStormComponent.builder()
                        .stormModule(new StormModule())
                        .build());
        Dagger.getDagger().inject(this);
    }
}
