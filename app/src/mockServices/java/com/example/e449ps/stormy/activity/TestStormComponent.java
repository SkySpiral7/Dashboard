package com.example.e449ps.stormy.activity;

import com.example.e449ps.stormy.dagger.StormComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TestStormModule.class})
public interface TestStormComponent extends StormComponent {
}
