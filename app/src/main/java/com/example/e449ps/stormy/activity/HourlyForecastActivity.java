package com.example.e449ps.stormy.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.e449ps.stormy.HourlyAdapter;
import com.example.e449ps.stormy.R;
import com.example.e449ps.stormy.databinding.ActivityHourlyForecastBinding;
import com.example.e449ps.stormy.model.HourlyWeather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HourlyForecastActivity extends AppCompatActivity {
    public static final String HOUR_EXTRA = "hourlyWeather";

    private HourlyAdapter adapter;
    private ActivityHourlyForecastBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);

        List<HourlyWeather> hourList = getIntent().getParcelableArrayListExtra(HOUR_EXTRA);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_hourly_forecast);
        adapter = new HourlyAdapter(hourList, this);
        binding.hourlyRecyclerView.setAdapter(adapter);
        binding.hourlyRecyclerView.setHasFixedSize(true);
        binding.hourlyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
