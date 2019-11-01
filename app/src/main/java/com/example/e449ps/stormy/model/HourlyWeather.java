package com.example.e449ps.stormy.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class HourlyWeather implements Serializable, Parcelable {
  private static final long serialVersionUID = 0;

  private String formattedTime;
  private int iconId;
  private String summary;
  private double temperature;

  public HourlyWeather() {}

  public HourlyWeather(String formattedTime, int iconId, String summary, double temperature) {
    this.formattedTime = formattedTime;
    this.iconId = iconId;
    this.summary = summary;
    this.temperature = temperature;
  }

  protected HourlyWeather(Parcel in) {
    formattedTime = in.readString();
    iconId = in.readInt();
    summary = in.readString();
    temperature = in.readDouble();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(formattedTime);
    dest.writeInt(iconId);
    dest.writeString(summary);
    dest.writeDouble(temperature);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<HourlyWeather> CREATOR =
      new Creator<HourlyWeather>() {
        @Override
        public HourlyWeather createFromParcel(Parcel in) {
          return new HourlyWeather(in);
        }

        @Override
        public HourlyWeather[] newArray(int size) {
          return new HourlyWeather[size];
        }
      };

  public String getFormattedTime() {
    return formattedTime;
  }

  public void setFormattedTime(String formattedTime) {
    this.formattedTime = formattedTime;
  }

  public int getIconId() {
    return iconId;
  }

  public void setIconId(int iconId) {
    this.iconId = iconId;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }
}
