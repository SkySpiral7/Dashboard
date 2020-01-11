package com.example.e449ps.stormy;

import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.DaggerTestStormComponent;
import com.example.e449ps.stormy.dagger.StormComponent;

public class TestApplication extends StormApplication {
    @Override
    public void onCreate() {
        StormComponent originalComponent = Dagger.get();
        if (originalComponent == null || StormComponent.class.equals(originalComponent.getClass())) {
            //default to test version if dagger didn't exist or was prod
            originalComponent = DaggerTestStormComponent.create();
        }
        super.onCreate();
        //super.onCreate sets dagger to prod so set it back
        Dagger.set(originalComponent);
    }
}
