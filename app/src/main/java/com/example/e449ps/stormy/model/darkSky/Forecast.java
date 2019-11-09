package com.example.e449ps.stormy.model.darkSky;

import java.io.Serializable;

public class Forecast implements Serializable {
    private static final long serialVersionUID = 0;

    private String timezone;
    private DataPoint currently;
    private DataBlock hourly;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public DataPoint getCurrently() {
        return currently;
    }

    public void setCurrently(DataPoint currently) {
        this.currently = currently;
    }

    public DataBlock getHourly() {
        return hourly;
    }

    public void setHourly(DataBlock hourly) {
        this.hourly = hourly;
    }
}
