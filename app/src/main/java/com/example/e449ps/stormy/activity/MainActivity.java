package com.example.e449ps.stormy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e449ps.stormy.ConnectionFacade;
import com.example.e449ps.stormy.LocationFacade;
import com.example.e449ps.stormy.R;
import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.StormComponent;
import com.example.e449ps.stormy.databinding.ActivityMainBinding;
import com.example.e449ps.stormy.dialog.GeneralErrorDialogFragment;
import com.example.e449ps.stormy.dialog.InternetErrorDialogFragment;
import com.example.e449ps.stormy.model.DisplayWeather;
import com.example.e449ps.stormy.model.HourlyWeather;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private ImageView iconImageView;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    //TODO: make tests for real classes (see example branch)
    private ConnectionFacade connectionFacade;
    private DisplayWeather displayWeather;
    private LocationFacade locationFacade;
    private WeatherCallbacks weatherCallbacks;
    private LocationCallbacks locationCallbacks;
    private Disposable weatherDisposable;

    public MainActivity() {
        StormComponent stormComponent = Dagger.get();
        connectionFacade = stormComponent.connectionFacade();
        weatherCallbacks = new WeatherCallbacks();
        locationCallbacks = new LocationCallbacks();
    }

    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        TextView darkSky = findViewById(R.id.darkSkyAttribution);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());

        iconImageView = findViewById(R.id.iconImageView);

        locationFacade = new LocationFacade(this, locationCallbacks);
        viewModel = Dagger.getViewModel(this, MainViewModel.class);

        Supplier<Boolean> internetConnectionTester = () -> connectionFacade.isConnectedToInternet(this);
        Runnable noInternetCallback = () -> new InternetErrorDialogFragment().show(getFragmentManager(), "network_error_dialogue");
        viewModel.onActivityCreated(locationFacade, internetConnectionTester, noInternetCallback);

        weatherDisposable = viewModel.getDisplayWeatherSubject().subscribe(weatherCallbacks::onNext, weatherCallbacks::onError);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationFacade.connect();
        viewModel.onActivityResume(this, locationFacade);
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
        viewModel.onActivityDestroy();
        //don't need to check if weatherDisposable is null because onCreate shouldn't be interrupted by onDestroy
        weatherDisposable.dispose();
    }
    //endregion lifecycle

    //region UI Callbacks
    public void refreshOnClick(View unused) {
        Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
        //will only need to ask for permission if the initial was denied
        viewModel.requestLocation(locationFacade);
    }

    public void hourlyOnClick(View unused) {
        //TODO: disable button until displayWeather is non-null
        List<HourlyWeather> hourlyWeather = displayWeather.getHourlyWeather();
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putParcelableArrayListExtra(
                HourlyForecastActivity.HOUR_EXTRA, new ArrayList<Parcelable>(hourlyWeather));
        startActivity(intent);
    }
    //endregion UI Callbacks

    private class LocationCallbacks implements LocationFacade.Callbacks {
        @Override
        public void permissionApprovedCallback() {
            viewModel.takeLocation();
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
        private void onNext(DisplayWeather newDisplayWeather) {
            displayWeather = newDisplayWeather;
            binding.setWeather(newDisplayWeather.getCurrentWeather());
            iconImageView.setImageDrawable(getDrawable(newDisplayWeather.getCurrentWeather().getIconId()));
        }

        private void onError(Throwable throwable) {
            Timber.e(throwable, "internet call failed");
            GeneralErrorDialogFragment dialog = new GeneralErrorDialogFragment();
            dialog.show(getFragmentManager(), "error_dialogue");
        }
    }
}
