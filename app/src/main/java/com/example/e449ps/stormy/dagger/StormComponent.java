package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.WeatherConverter;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {StormModule.class})
public interface StormComponent {
  WeatherConverter weatherConverter();

  ForecastService forecastService();
}
