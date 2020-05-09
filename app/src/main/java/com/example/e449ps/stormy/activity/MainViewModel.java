package com.example.e449ps.stormy.activity;

import android.location.Location;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.LocationFacade;
import com.example.e449ps.stormy.WeatherConverter;
import com.example.e449ps.stormy.model.DisplayWeather;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private WeatherConverter weatherConverter;
    private ForecastService forecastService;
    private DisplayWeather displayWeather;
    private LocationFacade locationFacade;
    private Location lastKnownLocation;
    private boolean inInitialState;

    //    @Inject
    public MainViewModel(WeatherConverter weatherConverter, ForecastService forecastService) {
        this.weatherConverter = weatherConverter;
        this.forecastService = forecastService;

        displayWeather = null;
        lastKnownLocation = null;
        inInitialState = true;
//        locationFacade =
//                new LocationFacade(this, this::locationRationalDialog, this::locationCallback, () -> {
//                });
    }
}
