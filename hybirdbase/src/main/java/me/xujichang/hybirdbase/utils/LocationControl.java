package me.xujichang.hybirdbase.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.lang.ref.WeakReference;

import me.xujichang.util.tool.BaiduTransform;
import me.xujichang.util.tool.LogTool;
import me.xujichang.util.tool.Transform;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/12/27 14:17.
 */

public class LocationControl {
    private WeakReference<Context> contextWeakReference;

    private static LocationControl instance;
    private LocationManager locationManager;
    private LocalizationListener localizationListener;
    private SelfLocationListener locationListener;

    public static LocationControl getInstance() {
        if (null == instance) {
            instance = new LocationControl();
        }
        return instance;
    }

    /**
     * 开启定位
     *
     * @param context
     */
    public void startGetLocation(Context context, String provider, @NonNull LocalizationListener localizationListener) {
        contextWeakReference = new WeakReference<>(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.localizationListener = localizationListener;
        getLocationForProvider(provider);
    }

    public void startGetLocation(Context context, @NonNull LocalizationListener localizationListener) {
        contextWeakReference = new WeakReference<>(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.localizationListener = localizationListener;
        getLocationForProvider(LocationManager.GPS_PROVIDER);
        getLocationForProvider(LocationManager.NETWORK_PROVIDER);
    }

    private void getLocationForProvider(String provider) {
        boolean isEnabled = locationManager.isProviderEnabled(provider);
        if (!isEnabled) {
            localizationListener.onProviderDisable();
            return;
        }
        Context context = contextWeakReference.get();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            localizationListener.onLocationPermissionDenied(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        updateToNewLocation(location);
        locationListener = new SelfLocationListener();
        // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
        locationManager.requestLocationUpdates(provider, 2 * 1000, 20,
                locationListener);
    }

    private void updateToNewLocation(Location location) {
        LogTool.d("Last Known Location:" + (null == location ? "location is null" : location.toString()));
        localizationListener.onGotLocationWithProvider(location);
    }

    public boolean compare(Object tempTarget, Object target) {
        return false;
    }

    public static double[] convertBaidu2Gps(double[] target) {
        double[] mars = BaiduTransform.TransBaidu2Mars(target[0], target[1]);
        return Transform.Mars2WGS(mars[1], mars[0]);
    }

    private class SelfLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LogTool.d("LocationListener:" + location.toString());
            localizationListener.onGotLocationWithProvider(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogTool.d("LocationListener:Status Changed:");
            localizationListener.onLocationStatusChanged(status, extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogTool.d("LocationListener: enable");

            localizationListener.onProviderEnable();
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogTool.d("LocationListener: disable");

            localizationListener.onProviderDisable();
        }
    }

    public interface LocalizationListener {
        /**
         * GPS未开启
         */
        void onProviderDisable();

        /**
         * GPS权限 被拒绝
         *
         * @param permissions 权限
         */
        void onLocationPermissionDenied(String[] permissions);

        /**
         * Gps 定位成功
         *
         * @param location
         */
        void onGotLocationWithProvider(Location location);

        /**
         * Gps 可用
         */
        void onProviderEnable();

        /**
         * 状态改变
         *
         * @param status 状态
         * @param extras 数据
         */
        void onLocationStatusChanged(int status, Bundle extras);
    }

    public static class SimpleLocalizationListener implements LocalizationListener {

        @Override
        public void onProviderDisable() {

        }

        @Override
        public void onLocationPermissionDenied(String[] permissions) {

        }

        @Override
        public void onGotLocationWithProvider(Location location) {

        }

        @Override
        public void onProviderEnable() {

        }


        @Override
        public void onLocationStatusChanged(int status, Bundle extras) {

        }
    }
}
