package com.keven.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class GpsHelper {

    private LocationListener locationListener = new LocationListener() {
        public void onProviderDisabled(String str) {
        }
        public void onProviderEnabled(String str) {
        }
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
        public void onLocationChanged(Location location) {
            GpsHelper.this.updateToNewLocation(location);
        }
    };

    private Context mContext;

    public GpsHelper(Context context) {
        this.mContext = context;
        openGPSSettings();
    }

    private void openGPSSettings() {
        if (((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")) {
            Toast.makeText(mContext, "GPS模块正常", Toast.LENGTH_LONG).show();
            getLocation();
            return;
        }
        Toast.makeText(mContext, "请开启GPS！", Toast.LENGTH_LONG).show();
        mContext.startActivity(new Intent("android.settings.SECURITY_SETTINGS"));
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(1);

        updateToNewLocation(locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true)));
        try {
            locationManager.requestLocationUpdates("gps", 2000, 500.0f, locationListener);
            locationManager.requestLocationUpdates("network", 2000, 500.0f, locationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateToNewLocation(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("维度=");
            stringBuilder.append(latitude);
            stringBuilder.append(",经度=");
            stringBuilder.append(longitude);
            Log.i("GPS", stringBuilder.toString());
        }
    }
}