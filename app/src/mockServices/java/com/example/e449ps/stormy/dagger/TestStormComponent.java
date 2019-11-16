package com.example.e449ps.stormy.dagger;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TestStormModule.class})
public interface TestStormComponent extends StormComponent {
}
