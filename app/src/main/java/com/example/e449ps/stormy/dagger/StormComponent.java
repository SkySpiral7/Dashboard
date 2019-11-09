package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.WeatherConverter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {StormModule.class})
public interface StormComponent {
    WeatherConverter weatherConverter();

    ForecastService forecastService();
}
