package com.example.e449ps.stormy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.e449ps.stormy.databinding.HourlyListItemBinding;
import com.example.e449ps.stormy.model.HourlyWeather;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {
    private final List<HourlyWeather> hourlyWeatherList;
    private final Context context;

    public HourlyAdapter(List<HourlyWeather> hourlyWeatherList, Context context) {
        this.hourlyWeatherList = hourlyWeatherList;
        this.context = context;
    }

    @NonNull
    @Override
    public HourlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HourlyListItemBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.hourly_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapter.ViewHolder holder, int position) {
        HourlyWeather hourlyWeather = hourlyWeatherList.get(position);
        holder.hourlyListItemBinding.setHour(hourlyWeather);
    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final HourlyListItemBinding hourlyListItemBinding;

        public ViewHolder(HourlyListItemBinding hourlyListItemBinding) {
            super(hourlyListItemBinding.getRoot());
            this.hourlyListItemBinding = hourlyListItemBinding;
        }
    }
}
