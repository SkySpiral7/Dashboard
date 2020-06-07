package com.example.e449ps.stormy;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import javax.inject.Inject;

public class ConnectionFacade {
    @Inject
    public ConnectionFacade() {
    }

    /**
     * @return true if {@code !airplaneMode && (Wi-Fi || cell data)}
     */
    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            LocationManager locationManager =
                    (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isLocationEnabled();
        } else {
            // This is Deprecated in API 28
            // there's also manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            int mode =
                    Settings.Secure.getInt(
                            context.getContentResolver(),
                            Settings.Secure.LOCATION_MODE,
                            Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    public boolean isAirplaneModeOn(Context context) {
        return Settings.Global.getInt(
                context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0)
                != 0;
    }
}
