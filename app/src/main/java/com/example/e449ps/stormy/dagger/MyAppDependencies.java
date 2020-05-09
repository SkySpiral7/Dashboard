package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.WeatherConverter;

public interface MyAppDependencies {
    WeatherConverter weatherConverter();

    ForecastService forecastService();

//    InjectionViewModelFactory injectionViewModelFactory();
}
