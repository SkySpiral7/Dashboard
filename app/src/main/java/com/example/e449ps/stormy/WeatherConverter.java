package com.example.e449ps.stormy;

import android.util.Log;

import com.example.e449ps.stormy.model.CurrentWeather;
import com.example.e449ps.stormy.model.DisplayWeather;
import com.example.e449ps.stormy.model.HourlyWeather;
import com.example.e449ps.stormy.model.darkSky.DataPoint;
import com.example.e449ps.stormy.model.darkSky.Forecast;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WeatherConverter {
    private static final String TAG = WeatherConverter.class.getSimpleName();
    private Gson gson;

    public WeatherConverter(Gson gson) {
        this.gson = gson;
    }

    public DisplayWeather getCurrentDetails(String json) {
        Forecast forecast = gson.fromJson(json, Forecast.class);
        TimeZone timeZone = TimeZone.getTimeZone(forecast.getTimezone());

        DataPoint currently = forecast.getCurrently();
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getHumidity());
        currentWeather.setPrecipChance(currently.getPrecipProbability());
        currentWeather.setSummary(currently.getSummary());
        currentWeather.setTemperature(currently.getTemperature());
        //above is is 1:1 mapping

        currentWeather.setLocationLabel("Alcatraz Island, CA");
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("h:mm a");
        simpleDateFormatter.setTimeZone(timeZone);
        Date forecastDate = new Date(currently.getTime() * 1000);
        String formattedTime = simpleDateFormatter.format(forecastDate);
        currentWeather.setFormattedTime(formattedTime);

        currentWeather.setIconId(getIconId(currently.getIcon()));

        simpleDateFormatter = new SimpleDateFormat("h a");
        simpleDateFormatter.setTimeZone(timeZone);
        List<HourlyWeather> hourlyWeatherList = new ArrayList<>();
        for (DataPoint dataPoint : forecast.getHourly().getData()) {
            HourlyWeather hourly = new HourlyWeather();
            hourly.setSummary(dataPoint.getSummary());
            hourly.setTemperature(dataPoint.getTemperature());
            //above is is 1:1 mapping

            forecastDate = new Date(dataPoint.getTime() * 1000);
            formattedTime = simpleDateFormatter.format(forecastDate);
            hourly.setFormattedTime(formattedTime);

            hourly.setIconId(getIconId(dataPoint.getIcon()));
            hourlyWeatherList.add(hourly);
        }

        return new DisplayWeather(currentWeather, hourlyWeatherList);
    }

    private int getIconId(String iconString) {
        final int iconId;
        //https://darksky.net/dev/docs#data-point
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        switch (iconString) {
            case "clear-day":
                iconId = R.drawable.clear_day;
                break;
            case "clear-night":
                iconId = R.drawable.clear_night;
                break;
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "fog":
                iconId = R.drawable.fog;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId = R.drawable.cloudy_night;
                break;
            default:
                //"Developers should ensure that a sensible default is defined, as additional values, such as hail, thunderstorm, or tornado, may be defined in the future."
                Log.w(TAG, "Unexpected weather type: " + iconString);
                iconId = R.drawable.clear_day;
        }
        return iconId;
    }
}
