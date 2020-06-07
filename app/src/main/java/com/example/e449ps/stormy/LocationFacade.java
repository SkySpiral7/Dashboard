package com.example.e449ps.stormy;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.reactivex.Observable;

/**
 * Boilerplate:
 * <pre>{@code
 *    @literal @Override
 *     protected void onResume() {
 *         super.onResume();
 *         locationFacade.connect();
 *     }
 *
 *    @literal @Override
 *     protected void onPause() {
 *         super.onPause();
 *         locationFacade.disconnect();
 *     }
 *
 *    @literal @Override
 *     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 *         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 *         locationFacade.onRequestPermissionsResult(requestCode, permissions, grantResults);
 *     }}</pre>
 */
public class LocationFacade {

    /**
     * This is a random number. It can be any positive short but should be unique.
     */
    private static final short REQUEST_LOCATION_PERMISSION_CODE = 2878;

    private final Activity activity;
    private final Callbacks callbacks;

    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public LocationFacade(
            Activity activity,
            Callbacks callbacks) {
        this.activity = activity;
        this.callbacks = callbacks;

        googleApiClient = new GoogleApiClient.Builder(activity).addApi(LocationServices.API).build();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public interface Callbacks {
        default void permissionApprovedCallback() {
        }

        default void permissionDeniedCallback() {
        }

        /**
         * to avoid asking twice this needs to generate a single button dialogue.
         * the android native prompt that follows handles declining
         */
        default void justificationFactory(DialogInterface.OnClickListener onClickListener) {
        }
    }

    //region Boilerplate
    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    public void onRequestPermissionsResult(
            final int requestCode, @NonNull String[] requestedPermissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            // the array will be length 0 or 1 since I only asked for FINE_LOCATION
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // I don't think I need to check hasLocationPermission because I already checked the array
                // for "granted"
                callbacks.permissionApprovedCallback();
            } else {
                callbacks.permissionDeniedCallback();
            }
        }
    }
    //endregion Boilerplate

    public boolean hasLocationPermission() {
        //TODO: test: ACCESS_COARSE_LOCATION ("city block") should be enough and saves battery
        //hold off on this. since traffic will probably need fine grain
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Will ask for location permission as needed.
     *
     * @return will never complete or error
     */
    public Observable<Location> askForLocation() {
        if (!hasLocationPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                callbacks.justificationFactory(
                        (dialog, whichButton) -> {
                            // Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                    activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION_PERMISSION_CODE);
                        });
            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION_CODE);
            }
        }

        return Observable.create(emitter -> {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, it -> {
                        // null means that it successfully contacted a disabled service...
                        // ignore this so that your callback only gets successful locations
                        // TODO: is null a single place to check if location is on?
                        // not sure if !airplane && isLocationEnabled is enough: does GPS work with internet off?
                        if (!emitter.isDisposed() && it != null) emitter.onNext(it);
                    }
            );
            // onFailure is only called if it lacks permission
        });
    }
}
