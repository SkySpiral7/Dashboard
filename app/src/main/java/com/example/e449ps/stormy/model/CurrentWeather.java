package com.example.e449ps.stormy.model;

import java.io.Serializable;

public class CurrentWeather implements Serializable {
    private static final long serialVersionUID = 0;

    private String locationLabel;
    private int iconId;
    private String formattedTime;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;

    public CurrentWeather() {
    }

    public CurrentWeather(
            String locationLabel,
            int iconId,
            String formattedTime,
            double temperature,
            double humidity,
            double precipChance,
            String summary) {
        this.locationLabel = locationLabel;
        this.iconId = iconId;
        this.formattedTime = formattedTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
    }

    public CurrentWeather(CurrentWeather copy) {
        this.setIconId(copy.getIconId());
        this.setFormattedTime(copy.getFormattedTime());
        this.setTemperature(copy.getTemperature());
        this.setSummary(copy.getSummary());

        this.locationLabel = copy.locationLabel;
        this.humidity = copy.humidity;
        this.precipChance = copy.precipChance;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
