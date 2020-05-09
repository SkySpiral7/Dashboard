package com.example.e449ps.stormy;

import com.example.e449ps.stormy.model.darkSky.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ForecastRetrofitCaller {
    //TODO: consider a retrofit for each base URL. also API key should be static
    @GET("https://api.darksky.net/forecast/61d409957e62d46af62a7a99618d5141/{latitude},{longitude}")
    @Headers("Accept: application/json")
    Call<Forecast> getForecast(@Path("latitude") double latitude, @Path("longitude") double longitude);
}
