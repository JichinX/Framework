package me.xujichang.framework;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import me.xujichang.hybirdbase.utils.LocationControl;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/12/27 14:27.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocationControl.getInstance().startGetLocation(getApplicationContext(), new LocationControl.SimpleLocalizationListener() {
            @Override
            public void onGotLocationWithProvider(Location location) {
                Log.i("LocationListener", "Provider:" + location.getProvider() + " lat :" + location.getLatitude() + " lng:" + location.getLongitude());
                super.onGotLocationWithProvider(location);
            }

            @Override
            public void onLocationPermissionDenied(String[] permissions) {
                super.onLocationPermissionDenied(permissions);
            }

            @Override
            public void onProviderDisable() {
                super.onProviderDisable();
            }
        });
    }
}
