package com.example.e449ps.stormy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e449ps.stormy.ConnectionFacade;
import com.example.e449ps.stormy.ForecastRetrofitCaller;
import com.example.e449ps.stormy.LocationFacade;
import com.example.e449ps.stormy.R;
import com.example.e449ps.stormy.SchedulerFacade;
import com.example.e449ps.stormy.WeatherConverter;
import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.databinding.ActivityMainBinding;
import com.example.e449ps.stormy.dialog.GeneralErrorDialogFragment;
import com.example.e449ps.stormy.dialog.InternetErrorDialogFragment;
import com.example.e449ps.stormy.model.DisplayWeather;
import com.example.e449ps.stormy.model.HourlyWeather;
import com.example.e449ps.stormy.model.darkSky.Forecast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private ImageView iconImageView;
    private ActivityMainBinding binding;
//    private ViewModelProvider.Factory vmFactory = Dagger.get().injectionViewModelFactory();

    // it's calling get() twice but really this whole thing should be in a ViewModel
    //TODO: look into ViewModel. find something (mitch didn't help) or maybe I already know it
    //think I need rx to do vm. I know enough vm to listen to mitch about rx
    //? vm inject: https://www.youtube.com/watch?v=6eOyCEkQ5zQ
    //TODO: make tests for real classes (see example branch)
    private WeatherConverter weatherConverter = Dagger.get().weatherConverter();
    private ForecastRetrofitCaller forecastRetrofitCaller = Dagger.get().forecastRetrofitCaller();
    private SchedulerFacade schedulerFacade = Dagger.get().schedulerFacade();
    private ConnectionFacade connectionFacade = Dagger.get().connectionFacade();
    private DisplayWeather displayWeather;
    private LocationFacade locationFacade;
    /**
     * Only used for debugging
     */
    private Location lastKnownLocation;
    private boolean inInitialState;
    private Disposable networkDisposable;
    private Disposable locationDisposable;
    private Observable<Location> locationObservable;
    private WeatherCallbacks weatherCallbacks = new WeatherCallbacks();
    private LocationCallbacks locationCallbacks = new LocationCallbacks();

    //region lifecycle
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
        locationFacade = new LocationFacade(this, locationCallbacks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationFacade.connect();
        if (inInitialState) {
            /*use boolean instead of lastKnownLocation==null to avoid this happening more than once in a
            row. the reason this isn't in onCreate is so that it's after connect*/
            inInitialState = false;
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();

            //this is coded funny to avoid a race condition since askForLocation changes hasLocationPermission
            if (locationFacade.hasLocationPermission()) {
                locationObservable = schedulerFacade.ioToBackground(locationFacade.askForLocation());
                //if already have permission then permission accepted callback won't be called so manually call takeLocation
                takeLocation();
            } else {
                locationObservable = schedulerFacade.ioToBackground(locationFacade.askForLocation());
                /*takeLocation will be called in the permission accepted callback.
                can't do it here because no permission yet*/
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] requestedPermissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, requestedPermissions, grantResults);
        locationFacade.onRequestPermissionsResult(requestCode, requestedPermissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationFacade.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //these need to be disposed if success wasn't yet called
        if (networkDisposable != null) networkDisposable.dispose();
        if (locationDisposable != null) locationDisposable.dispose();
    }
    //endregion lifecycle

    //region UI Callbacks
    public void refreshOnClick(View unused) {
        Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
        /*ask for permission again if the initial was denied. this will then call permission accepted callback
        which is takeLocation. else take a new location.*/
        if (!locationFacade.hasLocationPermission())
            locationObservable = schedulerFacade.ioToBackground(locationFacade.askForLocation());
        else takeLocation();
    }

    public void hourlyOnClick(View unused) {
        List<HourlyWeather> hourlyWeather = displayWeather.getHourlyWeather();
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putParcelableArrayListExtra(
                HourlyForecastActivity.HOUR_EXTRA, new ArrayList<Parcelable>(hourlyWeather));
        startActivity(intent);
    }
    //endregion UI Callbacks

    private void takeLocation() {
        locationDisposable = locationObservable.take(1).subscribe(this::consumeLocation);
    }

    private void consumeLocation(Location newLocation) {
        lastKnownLocation = newLocation;
        locationDisposable.dispose();
        if (!connectionFacade.isConnectedToInternet(this)) {
            //TODO: confirm this works on background thread
            new InternetErrorDialogFragment().show(getFragmentManager(), "network_error_dialogue");
            return;
        }
        //TODO: can these be composed into 1 Observable instead of 2?

        //don't make getForecast observable because it needs a new request every location also take(2) doesn't work
        networkDisposable = schedulerFacade.ioToUi(forecastRetrofitCaller.getForecast(newLocation.getLatitude(), newLocation.getLongitude()))
                .subscribe(weatherCallbacks::onSuccess, weatherCallbacks::onError);
    }

    private class LocationCallbacks implements LocationFacade.Callbacks {
        @Override
        public void permissionApprovedCallback() {
            MainActivity.this.takeLocation();
        }

        @Override
        public void permissionDeniedCallback() {
            Toast.makeText(MainActivity.this, "Denied: Can't load weather", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void justificationFactory(DialogInterface.OnClickListener listener) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.open_permission_button, listener)
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    private class WeatherCallbacks {
        private void onSuccess(Forecast forecast) {
            displayWeather = weatherConverter.getCurrentDetails(forecast);
            binding.setWeather(displayWeather.getCurrentWeather());
            iconImageView.setImageDrawable(getDrawable(displayWeather.getCurrentWeather().getIconId()));
            networkDisposable.dispose();
        }

        private void onError(Throwable throwable) {
            Timber.e(throwable, "internet call failed");
            GeneralErrorDialogFragment dialog = new GeneralErrorDialogFragment();
            dialog.show(getFragmentManager(), "error_dialogue");
            networkDisposable.dispose();
        }
    }
}
