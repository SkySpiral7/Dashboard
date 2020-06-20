package com.example.e449ps.stormy.dagger;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {StormModule.class, ViewModelModule.class})
public interface StormComponent extends MyAppDependencies {
}
