package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.ConnectionFacade;
import com.example.e449ps.stormy.ForecastRetrofitCaller;
import com.example.e449ps.stormy.SchedulerFacade;
import com.example.e449ps.stormy.WeatherConverter;

public interface MyAppDependencies {
    WeatherConverter weatherConverter();

    ForecastRetrofitCaller forecastRetrofitCaller();

    SchedulerFacade schedulerFacade();

    ConnectionFacade connectionFacade();

//    InjectionViewModelFactory injectionViewModelFactory();
}
