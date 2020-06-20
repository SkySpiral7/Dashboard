package com.example.e449ps.stormy.activity;

import android.location.Location;
import android.widget.Toast;

import com.example.e449ps.stormy.ForecastRetrofitCaller;
import com.example.e449ps.stormy.LocationFacade;
import com.example.e449ps.stormy.SchedulerFacade;
import com.example.e449ps.stormy.WeatherConverter;
import com.example.e449ps.stormy.model.DisplayWeather;
import com.example.e449ps.stormy.model.darkSky.Forecast;

import java.util.function.Supplier;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

public class MainViewModel extends ViewModel {
    private WeatherConverter weatherConverter;
    private ForecastRetrofitCaller forecastRetrofitCaller;
    private SchedulerFacade schedulerFacade;
    private boolean inInitialState;
    private Disposable networkDisposable;
    private Disposable locationDisposable;
    private Observable<Location> locationObservable;
    private Subject<DisplayWeather> displayWeatherSubject;
    private Supplier<Boolean> internetConnectionTester;
    private Runnable noInternetCallback;

    @Inject
    public MainViewModel(WeatherConverter weatherConverter, ForecastRetrofitCaller forecastRetrofitCaller,
                         SchedulerFacade schedulerFacade) {
        this.weatherConverter = weatherConverter;
        this.forecastRetrofitCaller = forecastRetrofitCaller;
        this.schedulerFacade = schedulerFacade;

        inInitialState = true;
        displayWeatherSubject = BehaviorSubject.create();
    }

    //region lifecycle
    @Override
    protected void onCleared() {
        super.onCleared();
        onActivityDestroy();
    }

    /**
     * Must be called in activity's onCreate in order to get a reference to the activity that will be used for call backs.
     */
    public void onActivityCreated(LocationFacade locationFacade, Supplier<Boolean> internetConnectionTester, Runnable noInternetCallback) {
        locationObservable = schedulerFacade.ioToBackground(locationFacade.getLocationObservable());
        this.internetConnectionTester = internetConnectionTester;
        this.noInternetCallback = noInternetCallback;
    }

    public void onActivityResume(MainActivity mainActivity, LocationFacade locationFacade) {
        if (inInitialState) {
            /*use boolean instead of lastKnownLocation==null to avoid this happening more than once in a
            row. the reason this isn't in onCreate is so that it's after locationFacade.connect*/
            inInitialState = false;
            Toast.makeText(mainActivity, "Loading", Toast.LENGTH_SHORT).show();
            requestLocation(locationFacade);
        }
    }

    public void onActivityDestroy() {
        //these need to be disposed if success wasn't yet called
        if (networkDisposable != null) networkDisposable.dispose();
        if (locationDisposable != null) locationDisposable.dispose();

        //these may be references to the activity and thus need to be cleared to avoid leak
        internetConnectionTester = null;
        noInternetCallback = null;
    }
    //endregion lifecycle

    public Subject<DisplayWeather> getDisplayWeatherSubject() {
        return displayWeatherSubject;
    }

    /**
     * Handles permissions and taking the location
     */
    public void requestLocation(LocationFacade locationFacade) {
        if (locationFacade.hasLocationPermission()) takeLocation();
        else locationFacade.requestLocationPermission();
        /*can't takeLocation right after requestLocationPermission because permission is another thread
        permissionApprovedCallback is takeLocation so only call takeLocation if we already have permission*/
    }

    /**
     * Does nothing if doesn't have location permission or location is disabled
     */
    public void takeLocation() {
        //only possible if a previous subscribe was never called (eg lack of permission)
        if (locationDisposable != null) locationDisposable.dispose();
        //this can't become a single Observable because I need to take on demand
        locationDisposable = locationObservable.take(1).subscribe(this::consumeLocation);
    }

    private void consumeLocation(Location newLocation) {
        Timber.i("New location: %f, %f", newLocation.getLatitude(), newLocation.getLongitude());
        locationDisposable.dispose();
        if (!internetConnectionTester.get()) {
            /*yes it is possible to turn off Wi-Fi and cell data without turning off GPS
            and yes this dialogue will show even though consumeLocation is on a background thread.*/
            noInternetCallback.run();
            return;
        }

        Consumer<Forecast> onSuccess = forecast -> {
            DisplayWeather displayWeather = weatherConverter.getCurrentDetails(forecast);
            networkDisposable.dispose();
            displayWeatherSubject.onNext(displayWeather);
        };
        Consumer<Throwable> onError = throwable -> {
            networkDisposable.dispose();
            displayWeatherSubject.onError(throwable);
        };
        //only possible if 2 locations were requested quickly but the internet is super slow
        if (networkDisposable != null) networkDisposable.dispose();
        //don't make getForecast observable because it needs a new request every location also take(2) doesn't work
        networkDisposable = schedulerFacade.ioToUi(forecastRetrofitCaller.getForecast(newLocation.getLatitude(), newLocation.getLongitude()))
                .subscribe(onSuccess, onError);
    }
}
