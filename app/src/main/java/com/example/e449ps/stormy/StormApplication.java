package com.example.e449ps.stormy;

import android.app.Application;
import android.util.Log;

import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.DaggerStormComponent;

import timber.log.Timber;

public class StormApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Dagger.set(DaggerStormComponent.create());
        if (!BuildConfig.BUILD_TYPE.equals("release")) {
            Timber.plant(new VerboseTagTree(Log.VERBOSE));
        } else {
            Timber.plant(new VerboseTagTree(Log.WARN));
        }
    }
}
