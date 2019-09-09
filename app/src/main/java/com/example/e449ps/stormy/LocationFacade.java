package com.example.e449ps.stormy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private final OnSuccessListener<Location> locationCallback;
    private final Consumer<DialogInterface.OnClickListener> justificationFactory;

    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Runnable permissionDeniedCallback;

    public LocationFacade(Activity activity, Consumer<DialogInterface.OnClickListener> justificationFactory, OnSuccessListener<Location> locationCallback, Runnable permissionDeniedCallback) {
        this.activity = activity;
        this.justificationFactory = justificationFactory;
        this.locationCallback = locationCallback;
        this.permissionDeniedCallback = permissionDeniedCallback;

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            //the array will be length 0 or 1 since I only asked for FINE_LOCATION
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //I don't think I need to check hasLocationPermission because I already checked the array for "granted"
                lastLocationCallback();
            } else {
                permissionDeniedCallback.run();
            }
        }
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isLocationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This is Deprecated in API 28
            //there's also !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            int mode = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    public boolean isAirplaneModeOn() {
        return Settings.Global.getInt(activity.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    public void askForLocation() {
        if (!hasLocationPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                justificationFactory.accept((dialogInterface, i) -> {
                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION_PERMISSION_CODE);
                });
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION_CODE);
            }
        } else {
            lastLocationCallback();
        }
    }

    private void lastLocationCallback() throws SecurityException {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, locationCallback);
        //onFailure doesn't seem to be called
    }
}
