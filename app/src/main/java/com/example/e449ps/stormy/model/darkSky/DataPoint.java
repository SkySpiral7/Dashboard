package com.example.e449ps.stormy.model.darkSky;

import java.io.Serializable;

public class DataPoint implements Serializable {
  private static final long serialVersionUID = 0;

  private long time;
  private String icon;
  private double precipProbability;
  private double temperature;
  private double humidity;
  private String summary;

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public double getPrecipProbability() {
    return precipProbability;
  }

  public void setPrecipProbability(double precipProbability) {
    this.precipProbability = precipProbability;
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

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }
}
