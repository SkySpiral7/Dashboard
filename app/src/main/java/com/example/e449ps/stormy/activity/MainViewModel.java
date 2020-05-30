package com.example.e449ps.stormy.activity;

import android.location.Location;
import android.widget.Toast;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.LocationFacade;
import com.example.e449ps.stormy.WeatherConverter;
import com.example.e449ps.stormy.model.DisplayWeather;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private WeatherConverter weatherConverter;
    private ForecastService forecastService;
    private DisplayWeather displayWeather;
    private LocationFacade locationFacade;
    /**
     * Only used for debugging
     */
    private Location lastKnownLocation;
    private boolean inInitialState;

    @Inject
    public MainViewModel(WeatherConverter weatherConverter, ForecastService forecastService) {
        this.weatherConverter = weatherConverter;
        this.forecastService = forecastService;

        displayWeather = null;
        lastKnownLocation = null;
        inInitialState = true;
    }

    /**
     * Must be called in activity's onCreate in order to get a reference to the activity that will be used for call backs.
     */
    public void onActivityCreated(MainActivity mainActivity) {
        Runnable permissionDeniedCallback = () -> Toast.makeText(mainActivity, "Denied: Can't load weather", Toast.LENGTH_SHORT).show();
        locationFacade = new LocationFacade(mainActivity, mainActivity::locationRationalDialog, mainActivity::locationCallback, permissionDeniedCallback);
    }
}
