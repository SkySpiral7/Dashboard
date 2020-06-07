package com.example.e449ps.stormy;

import com.example.e449ps.stormy.model.darkSky.Forecast;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ForecastRetrofitCaller {
    //yes there's a comma in the URL instead of being query parameters
    @GET("{latitude},{longitude}")
    @Headers("Accept: application/json")
    Single<Forecast> getForecast(@Path("latitude") double latitude, @Path("longitude") double longitude);
}
