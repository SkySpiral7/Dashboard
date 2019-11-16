package com.example.e449ps.stormy;

import android.app.Application;

import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.DaggerStormComponent;

public class StormApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Dagger.set(DaggerStormComponent.create());
    }
}
