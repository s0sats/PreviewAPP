package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/05/17.
 */

public class SV_LocationTracker extends Service {
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "gps_service";

    public static String msg_nok = "";
    public static String msg_ok = "";

    // 1 minutos
    private static int PROGRESS_TIME_OUT = 1 * 60 * 1000;

    public static boolean status;

    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;

    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 0f;

    private Handler mHandler;
    private Runnable mRunnable;

    private int readings = 0;

    private String mLocation_Latitude;
    private String mLocation_Longitude;
    private String mLocation_Type;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);

            mLocation_Type = location.getProvider();
            mLocation_Latitude = String.valueOf(location.getLatitude());
            mLocation_Longitude = String.valueOf(location.getLongitude());

            readings++;

            if (readings >= 5) {
                readings = 0;
                stopSelf();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                stopSelf();
                status = false;
            }
        };

        mHandler.postDelayed(mRunnable, PROGRESS_TIME_OUT);

        mLocation_Type = "";
        mLocation_Latitude = "";
        mLocation_Longitude = "";

        status = true;

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        initializeLocationManager();

        status = true;

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        } catch (Exception e) {
            status = false;
        }
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        status = false;

        loadTranslation();

        if (!mLocation_Type.equals("")) {
            ToolBox_Inf.sendBCStatus(
                    getApplicationContext(),
                    "GPS_OK",
                    hmAux_Trans.get("gps_location_aquired"),
                    mLocation_Type.toUpperCase() + "#" + mLocation_Latitude + "#" + mLocation_Longitude,
                    "0"
            );
        } else {
            ToolBox_Inf.sendBCStatus(
                    getApplicationContext(),
                    "CUSTOM_ERROR",
                    hmAux_Trans.get("gps_location_not_aquired"),
                    "",
                    "0"
            );
        }

        super.onDestroy();

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                }
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("gps_location_aquired");
        translist.add("gps_location_not_aquired");

        mResource_Code = ToolBox_Inf.getResourceCode(
                getApplicationContext(),
                mModule_Code,
                mResource_Name
        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
                getApplicationContext(),
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
                translist);
    }
}

