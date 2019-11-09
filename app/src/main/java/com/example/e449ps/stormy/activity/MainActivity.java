package com.example.e449ps.stormy.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.LocationFacade;
import com.example.e449ps.stormy.R;
import com.example.e449ps.stormy.WeatherConverter;
import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.databinding.ActivityMainBinding;
import com.example.e449ps.stormy.dialog.GeneralErrorDialogFragment;
import com.example.e449ps.stormy.dialog.InternetErrorDialogFragment;
import com.example.e449ps.stormy.model.DisplayWeather;
import com.example.e449ps.stormy.model.HourlyWeather;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {
    private ImageView iconImageView;
    // it's calling get() twice but really this whole thing should be in a ViewModel
    private WeatherConverter weatherConverter = Dagger.get().weatherConverter();
    private ForecastService forecastService = Dagger.get().forecastService();
    private DisplayWeather displayWeather;
    private LocationFacade locationFacade;
    private Location lastKnownLocation;
    private ActivityMainBinding binding;
    private boolean inInitialState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        TextView darkSky = findViewById(R.id.darkSkyAttribution);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());

        iconImageView = findViewById(R.id.iconImageView);

        displayWeather = null;
        lastKnownLocation = null;
        inInitialState = true;
        locationFacade =
                new LocationFacade(this, this::locationRationalDialog, this::locationCallback, () -> {
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationFacade.connect();
        if (inInitialState) {
            // use boolean instead of lastKnownLocation==null to avoid this happening more than once in a
            // row
            inInitialState = false;
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
            locationFacade.askForLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationFacade.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationFacade.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void refreshOnClick(View unused) {
        Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
        locationFacade.askForLocation();
    }

    private void locationRationalDialog(DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_location_permission)
                .setMessage(R.string.text_location_permission)
                .setPositiveButton(R.string.ok_text, listener)
                .create()
                .show();
    }

    private void locationCallback(Location newLocation) {
        lastKnownLocation = newLocation;
        getForecast(binding, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
    }

    /**
     * @return true if !airplaneMode && (Wi-Fi || cell data)
     */
    public boolean isConnectedToInternet() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void hourlyOnClick(View unused) {
        List<HourlyWeather> hourlyWeather = displayWeather.getHourlyWeather();
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putParcelableArrayListExtra(
                HourlyForecastActivity.HOUR_EXTRA, new ArrayList<Parcelable>(hourlyWeather));
        startActivity(intent);
    }

    private void getForecast(final ActivityMainBinding binding, double latitude, double longitude) {
        forecastService.getForecast(
                this,
                new ForecastService.StringConsumer() {
                    @Override
                    public void accept(String responseBodyString) {
                        displayWeather = weatherConverter.getCurrentDetails(responseBodyString);
                        binding.setWeather(displayWeather.getCurrentWeather());
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        iconImageView.setImageDrawable(
                                                getDrawable(displayWeather.getCurrentWeather().getIconId()));
                                    }
                                });
                    }
                },
                latitude,
                longitude);
    }

    public void alertUserAboutError() {
        GeneralErrorDialogFragment dialog = new GeneralErrorDialogFragment();
        dialog.show(getFragmentManager(), "error_dialogue");
    }

    public void showInternetErrorDialog() {
        new InternetErrorDialogFragment().show(getFragmentManager(), "network_error_dialogue");
    }
}
