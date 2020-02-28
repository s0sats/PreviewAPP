package com.namoadigital.prj001.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
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
    public static final int LOCATION_NOTIFICATION_ID = 9999;
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
    private static final int LOCATION_INTERVAL_DEFAULT  = 1 * 60 * 1000;
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
            String dataRecorded =
                    "\nasync_gps = " + async_gps +
                    "\nmLocation_Type = " + location.getProvider().toUpperCase() +
                    "\nmLocation_Latitude = " + location.getLatitude() +
                    "\nmLocation_Longitude = " + location.getLongitude();
            recordProcess("\nonLocationChanged: " + dataRecorded );
            mLastLocation.set(location);

            mLocation_Type = location.getProvider().toUpperCase();
            mLocation_Latitude = String.valueOf(location.getLatitude());
            mLocation_Longitude = String.valueOf(location.getLongitude());
            Log.i("GPS_Service", "location Lat: " + location.getLatitude() +  " location Long: " + location.getLongitude());
            ToolBox.toastMSG(getApplicationContext(), "onLocationChanged: " + ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            boolean hasError;
            switch (async_gps){
                case LOCATION_NFORM_ON:
                    Log.i("GPS_Service", "async_gps: " + async_gps);
                    hasError = setFormLocation();
                    setLocationPreference(location, hasError);
                    recordProcess("onLocationChanged -> async_gps: " + async_gps );
                    break;
                case LOCATION_BACKGROUND:
                    Log.i("GPS_Service", "async_gps: " + async_gps);
                    hasError = setFormLocation();
                    setLocationPreference(location, hasError);
                    recordProcess("onLocationChanged -> async_gps: " + async_gps );
                    stopSelf();
                    break;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("GPS_Service", "onProviderDisabled: " + provider);
            String dataRecorded = "\nonProviderDisabled: " + provider;
            recordProcess(dataRecorded);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("GPS_Service", "onProviderEnabled: " + provider);
            String dataRecorded = "\nonProviderDisabled: " + provider;
            recordProcess(dataRecorded);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("GPS_Service", "onStatusChanged: " + "provider: " + provider +"\nstatus: " + status);
            String dataRecorded = "\nonProviderDisabled: " + provider;
            recordProcess(dataRecorded);
        }
    }

    private boolean setFormLocation() {
        boolean hasError = false;
        long customer_code = ToolBox_Con.getPreference_Customer_Code(getApplicationContext());
        GE_Custom_Form_DataDao ge_custom_form_dataDao = new GE_Custom_Form_DataDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);

        List<GE_Custom_Form_Data> formDataList = ge_custom_form_dataDao.query(
                new GE_Custom_Form_Data_Sql_006(customer_code).toSqlQuery()
        );

        if(formDataList != null && !formDataList.isEmpty()) {
            for (GE_Custom_Form_Data form_data : formDataList) {
                form_data.setLocation_lat(mLocation_Latitude);
                form_data.setLocation_lng(mLocation_Longitude);
                form_data.setLocation_type(mLocation_Type.toUpperCase());
                form_data.setLocation_pendency(0);
                form_data.setDate_gps(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));

                DaoObjReturn daoObjReturn = ge_custom_form_dataDao.addUpdateWithReturn(form_data);
                if(daoObjReturn.hasError()){
                    hasError = true;
                }
//                String dataRecorded =
//                        "\nasync_gps = " + async_gps +
//                                "\nmSerial_id = " + form_data.getSerial_id() +
//                                "\nmLocation_Type = " + form_data.getLocation_type() +
//                                "\nmLocation_Latitude = " + form_data.getLocation_lat() +
//                                "\nmLocation_Longitude = " + form_data.getLocation_lng() +
//                                "\nDate_gps = " + form_data.getDate_gps();
//                recordProcess("\nonLocationChanged: " + dataRecorded);
            }
        }
        return hasError;
    }

    private void recordProcess(String data) {
        try {
            String date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "\n ";
            String filePath = getApplicationContext().getFilesDir().getPath().toString() + "/GPS_Histo.txt";
            ToolBox_Inf.writeIn(data + "  ---  " + date, new File(filePath));
            Log.i("GPS_Service", "recordProcess: " + date + data);
        }catch (NullPointerException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLocationPreference(Location location, boolean hasError) {
        ToolBox_Con.setStringPreference(getApplicationContext(), Constant.LOCATION_LAT, String.valueOf(location.getLatitude()));
        ToolBox_Con.setStringPreference(getApplicationContext(), Constant.LOCATION_LNG, String.valueOf(location.getLongitude()));
        ToolBox_Con.setStringPreference(getApplicationContext(), Constant.LOCATION_TYPE, location.getProvider().toUpperCase());
        ToolBox_Con.setLongPreference(getApplicationContext(), Constant.LOCATION_DATE, Calendar.getInstance().getTime().getTime());
        if(hasError) {
            ToolBox_Con.setBooleanPreference(getApplicationContext(), Constant.HAS_PENDING_LOCATION, true);
        }else {
            ToolBox_Con.setBooleanPreference(getApplicationContext(), Constant.HAS_PENDING_LOCATION, false);
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
        ToolBox.toastMSG(getApplicationContext(), "onStartCommand");
        if (intent != null) {
            async_gps = intent.getIntExtra(ASYNC_GPS, LOCATION_BACKGROUND);
        }else{
            async_gps = LOCATION_BACKGROUND;
        }
        ToolBox_Inf.cancelNotification(getApplicationContext(), LOCATION_NOTIFICATION_ID);
        call_Notification();
        Log.i("GPS_Service", "onStartCommand: " + async_gps);

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
                recordProcess("onStartCommand -> async_gps: " + async_gps );
                setLocationListeners(LOCATION_INTERVAL_NFORM_ON);
                break;
            //Parametrizacao para recuperar localizacao apos finalizacao do N-FORM
            case LOCATION_BACKGROUND:
                Log.i("GPS_Service", "onStartCommand LOCATION_BACKGROUND");
                recordProcess("onStartCommand -> async_gps: " + async_gps );
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
        recordProcess("onCreate");
        loadTranslation();
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
        String dataRecorded = "\nonDestroy: ";
        recordProcess(dataRecorded);
        status = false;
        ToolBox_Inf.cancelNotification(getApplicationContext(), LOCATION_NOTIFICATION_ID);
        removeLocationListeners();
        ToolBox.toastMSG(getApplicationContext(), "onDestroy");
        super.onDestroy();

    }

    private void removeLocationListeners() {
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                    recordProcess("removeLocationListener index: " + i);
                } catch (Exception ex) {
                    recordProcess("removeLocationListener exception: " + ex.toString());
                }
            }
        }else{
            recordProcess("removeLocationListeners: LocationManager eh null");
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            recordProcess("initLocationManager: Era null");
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            recordProcess("initLocationManager: Agora ta instanciado: " + mLocationManager.toString());
        }else{
            recordProcess("initLocationManager: " + mLocationManager.toString());
            Log.i("GPS_Service", mLocationManager.toString());
        }
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("gps_location_aquired");
        translist.add("gps_location_not_aquired");
        translist.add("gps_searching_location");

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

    public void call_Notification() {
        NotificationManager nm = (NotificationManager)
                getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_namoa);
        builder.setAutoCancel(false);
        builder.setContentTitle(getApplicationContext().getString(R.string.title_notification_generic));
        RemoteViews view = new RemoteViews(getApplicationContext().getPackageName(), R.layout.sv_resume_notification);
        String gps_searching_location = hmAux_Trans.get("gps_searching_location");
        String latitude = ToolBox_Con.getStringPreferencesByKey(getApplicationContext(), Constant.LOCATION_LAT,"");
        String longitude = ToolBox_Con.getStringPreferencesByKey(getApplicationContext(), Constant.LOCATION_LNG,"");
        long locationDate = ToolBox_Con.getLongPreferencesByKey(getApplicationContext(), Constant.LOCATION_DATE,0);
        String location_type = ToolBox_Con.getStringPreferencesByKey(getApplicationContext(), Constant.LOCATION_TYPE,"");

        builder.setContentText(gps_searching_location);

        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("latitude: " + latitude +
                        "\nlongitude: " + longitude +
                        "\nlocationDate: " + ToolBox_Inf.millisecondsToString(locationDate, "dd/MM/yyyy HH:mm:ss Z") +
                        "\nlocation_type: " + location_type +
                        "\nasync_gps: " + async_gps));
        //
        int versao = Build.VERSION.SDK_INT;
        //
        if (versao >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            nm.notify(LOCATION_NOTIFICATION_ID, builder.build());
        } else {
            nm.notify(LOCATION_NOTIFICATION_ID, builder.getNotification());
        }
    }

}

