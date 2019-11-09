package com.example.e449ps.stormy.model;

import java.io.Serializable;
import java.util.List;

public class DisplayWeather implements Serializable {
    private static final long serialVersionUID = 0;

    private CurrentWeather currentWeather;
    private List<HourlyWeather> hourlyWeather;

    public DisplayWeather() {
    }

    public DisplayWeather(CurrentWeather currentWeather, List<HourlyWeather> hourlyWeather) {
        this.currentWeather = currentWeather;
        this.hourlyWeather = hourlyWeather;
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public List<HourlyWeather> getHourlyWeather() {
        return hourlyWeather;
    }

    public void setHourlyWeather(List<HourlyWeather> hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }
}
