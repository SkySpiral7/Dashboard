package com.example.e449ps.stormy;

import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.DaggerTestStormComponent;
import com.example.e449ps.stormy.dagger.StormComponent;

public class TestApplication extends StormApplication {
    @Override
    public void onCreate() {
        StormComponent originalComponent = Dagger.get();
        super.onCreate();
        if (originalComponent == null || StormComponent.class.equals(originalComponent.getClass())) {
            Dagger.set(DaggerTestStormComponent.create());
        }
        //TODO: what about running multiple Dagger tests? will it pollute? will need each test to clean up
    }
}
