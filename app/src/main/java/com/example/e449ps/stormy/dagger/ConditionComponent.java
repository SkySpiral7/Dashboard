package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {StormProvider.class})
public interface ConditionComponent {
    void inject(StormApplication stormApplication);

    void inject(MainActivity mainActivity);
}
