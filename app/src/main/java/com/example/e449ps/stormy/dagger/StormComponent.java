package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {StormModule.class})
public interface StormComponent {
    void inject(MainActivity mainActivity);
}
