package com.example.e449ps.stormy.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.OkResponseConverter;
import com.example.e449ps.stormy.R;
import com.example.e449ps.stormy.WeatherConverter;
import com.example.e449ps.stormy.databinding.ActivityMainBinding;
import com.example.e449ps.stormy.dialog.GeneralErrorDialogFragment;
import com.example.e449ps.stormy.dialog.NetworkErrorDialogFragment;
import com.example.e449ps.stormy.model.DisplayWeather;
import com.example.e449ps.stormy.model.HourlyWeather;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private ImageView iconImageView;
    private WeatherConverter weatherConverter;
    private ForecastService forecastService;
    private DisplayWeather displayWeather;

    public MainActivity() {
        weatherConverter = new WeatherConverter(new Gson());
        OkHttpClient client = new OkHttpClient();
//        client = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        }).build();
        forecastService = new ForecastService(client, new OkResponseConverter());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        TextView darkSky = findViewById(R.id.darkSkyAttribution);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());

        iconImageView = findViewById(R.id.iconImageView);

        //old: 37.8267, -122.4233
        //me: 38.792909, -90.627005
        final double latitude = 38.792909;
        final double longitude = -90.627005;

        ImageView refreshImage = findViewById(R.id.refreshImageView);
        refreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                getForecast(binding, latitude, longitude);
            }
        });
        displayWeather = null;
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
        getForecast(binding, latitude, longitude);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void hourlyOnClick(View view) {
        List<HourlyWeather> hourlyWeather = displayWeather.getHourlyWeather();
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putParcelableArrayListExtra(HourlyForecastActivity.HOUR_EXTRA, new ArrayList<Parcelable>(hourlyWeather));
        startActivity(intent);
    }

    private void getForecast(final ActivityMainBinding binding, double latitude, double longitude) {
        forecastService.getForecast(this, new ForecastService.StringConsumer() {
            @Override
            public void accept(String responseBodyString) {
                displayWeather = weatherConverter.getCurrentDetails(responseBodyString);
                binding.setWeather(displayWeather.getCurrentWeather());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iconImageView.setImageDrawable(getDrawable(displayWeather.getCurrentWeather().getIconId()));
                    }
                });
            }
        }, latitude, longitude);
    }

    public void alertUserAboutError() {
        GeneralErrorDialogFragment dialog = new GeneralErrorDialogFragment();
        dialog.show(getFragmentManager(), "error_dialogue");
    }

    public void showNetworkErrorDialog() {
        new NetworkErrorDialogFragment().show(getFragmentManager(), "network_error_dialogue");
    }
}
