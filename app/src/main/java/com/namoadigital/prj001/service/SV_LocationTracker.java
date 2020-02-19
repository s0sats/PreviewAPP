package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_006;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by neomatrix on 11/05/17.
 */

public class SV_LocationTracker extends Service {
    public static final String ASYNC_GPS = "ASYNC_GPS";
    public static final int LOCATION_DEFAULT = 0;
    public static final int LOCATION_NFORM_ON = 1;
    public static final int LOCATION_BACKGROUND = 2;
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "gps_service";

    public static String msg_nok = "";
    public static String msg_ok = "";

    // 1 minutos
    //private static int PROGRESS_TIME_OUT = 1 * 60 * 1000;
    private static int PROGRESS_TIME_OUT = 1 * 30 * 1000;

    public static boolean status;

    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;

    private static final int LOCATION_INTERVAL_NFORM_ON = 5 * 1 * 60 * 1000;
    private static final int LOCATION_INTERVAL_DEFAULT  = 1 * 10 * 1000;
    private static final float LOCATION_DISTANCE = 0f;

    private Handler mHandler;
    private Runnable mRunnable;

    private int readings = 0;

    private String mLocation_Latitude;
    private String mLocation_Longitude;
    private String mLocation_Type;
    private int async_gps;

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
            Log.i("GPS_Service", "location Lat: " + location.getLatitude() +  " location Long: " + location.getLongitude());

            switch (async_gps){
                case LOCATION_DEFAULT:
                    Log.i("GPS_Service", "async_gps: " + async_gps);
                    readings++;
                    if (readings >= 5) {
                        readings = 0;
                        stopSelf();
                    }
                    break;
                case LOCATION_NFORM_ON:
                    Log.i("GPS_Service", "async_gps: " + async_gps);
                    setLocationPreference(location);
                    break;
                case LOCATION_BACKGROUND:
//            setLocationToView("Old: " + lastKnownLocation.getLatitude(), "Old: " + lastKnownLocation.getLongitude());
                    Log.i("GPS_Service", "async_gps: " + async_gps);
                    long customer_code = ToolBox_Con.getPreference_Customer_Code(getApplicationContext());
                    GE_Custom_Form_DataDao ge_custom_form_dataDao = new GE_Custom_Form_DataDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
                    ge_custom_form_dataDao.addUpdate(
                            new GE_Custom_Form_Data_Sql_006(
                                    customer_code,
                                    mLocation_Type,
                                    mLocation_Latitude,
                                    mLocation_Longitude
                            ).toSqlQuery().toLowerCase()
                    );
                    setLocationPreference(location);
                    stopSelf();
                    break;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("GPS_Service", "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("GPS_Service", "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("GPS_Service", "onStatusChanged: " + "provider: " + provider +"\nstatus: " + status);
        }
    }

    private void setLocationPreference(Location location) {
        ToolBox_Con.setStringPreference(getApplicationContext(), Constant.LOCATION_LAT, String.valueOf(location.getLatitude()));
        ToolBox_Con.setStringPreference(getApplicationContext(), Constant.LOCATION_LNG, String.valueOf(location.getLongitude()));
        ToolBox_Con.setStringPreference(getApplicationContext(), Constant.LOCATION_TYPE, location.getProvider().toUpperCase());
        ToolBox_Con.setLongPreference(getApplicationContext(), Constant.LOCATION_DATE, Calendar.getInstance().getTime().getTime());
        ToolBox_Con.setBooleanPreference(getApplicationContext(),Constant.HAS_PENDING_LOCATION,false);
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
        Log.i("GPS_Service", "onStartCommand");
        async_gps = intent.getIntExtra(ASYNC_GPS, 0);

        switch (async_gps){
            //Parametrizacao para recuperar localizacao no termino do N-FORM
//            case LOCATION_DEFAULT:
//                Log.i("GPS_Service", "onStartCommand LOCATION_DEFAULT");
//                setLocationListeners(LOCATION_INTERVAL_DEFAULT);
//                setServiceTimeout();
//                break;
            //Parametrizacao para recuperar localizacao durante a exec do N-FORM no intervalo de 5min
            case LOCATION_NFORM_ON:
                Log.i("GPS_Service", "onStartCommand LOCATION_NFORM_ON");
                setLocationListeners(LOCATION_INTERVAL_NFORM_ON);
                break;
            //Parametrizacao para recuperar localizacao apos finalizacao do N-FORM
            case LOCATION_BACKGROUND:
                Log.i("GPS_Service", "onStartCommand LOCATION_BACKGROUND");
                setLocationListeners(LOCATION_INTERVAL_DEFAULT);
                break;

        }


        mLocation_Type = "";
        mLocation_Latitude = "";
        mLocation_Longitude = "";

        status = true;

        return START_STICKY;
    }

    private void setServiceTimeout() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                stopSelf();
                status = false;
            }
        };

        mHandler.postDelayed(mRunnable, PROGRESS_TIME_OUT);
    }

    @Override
    public void onCreate() {
        Log.i("GPS_Service", "onCreate");
        initializeLocationManager();

        status = true;

    }

    private void setLocationListeners(long location_interval) {
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, location_interval, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, location_interval, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        } catch (Exception e) {
            status = false;
        }
    }

    @Override
    public void onDestroy() {
        Log.i("GPS_Service", "onDestroy");

//        mHandler.removeCallbacks(mRunnable);

        status = false;

        loadTranslation();

//        if (!mLocation_Type.equals("")) {
//            ToolBox_Inf.sendBCStatus(
//                    getApplicationContext(),
//                    "GPS_OK",
//                    hmAux_Trans.get("gps_location_aquired"),
//                    mLocation_Type.toUpperCase() + "#" + mLocation_Latitude + "#" + mLocation_Longitude,
//                    "0"
//            );
//        } else {
//            ToolBox_Inf.sendBCStatus(
//                    getApplicationContext(),
//                    "CUSTOM_ERROR",
//                    hmAux_Trans.get("gps_location_not_aquired"),
//                    "",
//                    "0"
//            );
//        }

        super.onDestroy();

        removeLocationListeners();
    }

    private void removeLocationListeners() {
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

