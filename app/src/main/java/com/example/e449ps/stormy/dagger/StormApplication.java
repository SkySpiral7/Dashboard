package com.example.e449ps.stormy.dagger;

import android.app.Application;

public class StormApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Dagger.set(DaggerStormComponent.create());
    }
}
