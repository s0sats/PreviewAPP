package com.namoadigital.prj001.service;

import android.app.Notification;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_006;
import com.namoadigital.prj001.sql.Sql_SV_Location_Tracker_001;
import com.namoadigital.prj001.sql.Sql_SV_Location_Tracker_002;
import com.namoadigital.prj001.sql.Sql_SV_Location_Tracker_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.namoadigital.prj001.util.NotificationHelper.LOCATION_NOTIFICATION_ID;

/**
 * Created by neomatrix on 11/05/17.
 */

public class SV_LocationTracker extends Service {
    public static final String ASYNC_GPS = "ASYNC_GPS";
    public static final int LOCATION_DEFAULT = 0;
    public static final int LOCATION_NFORM_ON = 1;
    public static final int LOCATION_BACKGROUND = 2;
    public static final int LOCATION_FOREGROUND_ID = 9990;
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
    private static final int LOCATION_INTERVAL_DEFAULT = 1 * 60 * 1000;
    private static final float LOCATION_DISTANCE = 0f;

    private Handler mHandler;
    private Runnable mRunnable;

    private int readings = 0;

    private String mLocation_Latitude;
    private String mLocation_Longitude;
    private String mLocation_Type;
    private int async_gps;
    private boolean isForegroundService = false;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
//            String dataRecorded =
//                    "\nasync_gps = " + async_gps +
//                    "\nmLocation_Type = " + location.getProvider().toUpperCase() +
//                    "\nmLocation_Latitude = " + location.getLatitude() +
//                    "\nmLocation_Longitude = " + location.getLongitude();
//            recordProcess("\nonLocationChanged: " + dataRecorded );
            mLastLocation.set(location);

            mLocation_Type = location.getProvider().toUpperCase();
            mLocation_Latitude = String.valueOf(location.getLatitude());
            mLocation_Longitude = String.valueOf(location.getLongitude());

            boolean hasError;
            switch (async_gps) {
                case LOCATION_NFORM_ON:
                    hasError = setFormLocation();
                    setLocationPreference(location, hasError);
//                    Log.i("GPS_Service", "async_gps: " + async_gps);
//                    recordProcess("onLocationChanged -> async_gps: " + async_gps );
                    break;
                case LOCATION_BACKGROUND:
                    hasError = setFormLocation();
                    setLocationPreference(location, hasError);
//                    Log.i("GPS_Service", "async_gps: " + async_gps);
//                    recordProcess("onLocationChanged -> async_gps: " + async_gps );
                    ToolBox_Inf.callPendencyNotification(getApplicationContext());
                    if (!ToolBox_Con.getBooleanPreferencesByKey(getApplicationContext(), Constant.HAS_PENDING_LOCATION, false)) {
                        if(isForegroundService){
                            stopForeground(true);
                        }else {
                            stopSelf();
                        }
                    }
                    break;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
//            Log.i("GPS_Service", "onProviderDisabled: " + provider);
//            String dataRecorded = "\nonProviderDisabled: " + provider;
//            recordProcess(dataRecorded);
        }

        @Override
        public void onProviderEnabled(String provider) {
//            Log.i("GPS_Service", "onProviderEnabled: " + provider);
//            String dataRecorded = "\nonProviderDisabled: " + provider;
//            recordProcess(dataRecorded);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.i("GPS_Service", "onStatusChanged: " + "provider: " + provider +"\nstatus: " + status);
//            String dataRecorded = "\nonProviderDisabled: " + provider;
//            recordProcess(dataRecorded);
        }
    }

    /**
     *
     * LUCHE - 08/09/2020
     * Modificado metodo para além de setar dados no form, caso seja um form de um ticket,
     * setar o ticket, step e ctrl com update_required
     * @return
     */
    private boolean setFormLocation() {
        boolean hasError = false;
        long customer_code = ToolBox_Con.getPreference_Customer_Code(getApplicationContext());
        GE_Custom_Form_DataDao ge_custom_form_dataDao = new GE_Custom_Form_DataDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        TK_TicketDao ticketDao = new TK_TicketDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);

        List<GE_Custom_Form_Data> formDataList = getFormDataList(customer_code, ge_custom_form_dataDao);

        if(formDataList != null && !formDataList.isEmpty()) {
            for (GE_Custom_Form_Data form_data : formDataList) {
                form_data.setLocation_lat(mLocation_Latitude);
                form_data.setLocation_lng(mLocation_Longitude);
                form_data.setLocation_type(mLocation_Type.toUpperCase());
                form_data.setLocation_pendency(0);
                form_data.setDate_gps(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                //
                DaoObjReturn daoObjReturn = ge_custom_form_dataDao.addUpdateWithReturn(form_data);
                if(daoObjReturn.hasError()){
                    hasError = true;
                } else {
                    //Se não teve erro ao atualizar for, verifica se é de ticket e se for,
                    //atualiza flags.
                    if (isFormCreateByTicket(form_data)) {
                        //Seta update required pra 1 no ticket
                        ticketDao.addUpdate(
                                new Sql_SV_Location_Tracker_001(
                                        form_data.getCustomer_code(),
                                        form_data.getTicket_prefix(),
                                        form_data.getTicket_code()
                                ).toSqlQuery()
                        );
                        //Seta update required pra 1 no step
                        ticketDao.addUpdate(
                                new Sql_SV_Location_Tracker_002(
                                        form_data.getCustomer_code(),
                                        form_data.getTicket_prefix(),
                                        form_data.getTicket_code(),
                                        form_data.getStep_code()
                                ).toSqlQuery()
                        );
                        //Seta update required pra 1 no ctrl
                        ticketDao.addUpdate(
                                new Sql_SV_Location_Tracker_003(
                                        form_data.getCustomer_code(),
                                        form_data.getTicket_prefix(),
                                        form_data.getTicket_code(),
                                        form_data.getStep_code(),
                                        form_data.getTicket_seq(),
                                        form_data.getTicket_seq_tmp()
                                ).toSqlQuery()
                        );
                        //Envia broadcast para tela
                        sendTicketBroadcastStatus();
                    }
                    //
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

    private List<GE_Custom_Form_Data> getFormDataList(long customer_code, GE_Custom_Form_DataDao ge_custom_form_dataDao) {
        return ge_custom_form_dataDao.query(
                new GE_Custom_Form_Data_Sql_006(customer_code).toSqlQuery()
        );
    }

    /**
     * LUCHE - 08/ 09/2020
     * Verifica se fomr foi criado via ticket.
     * @param customFormData
     * @return
     */
    private boolean isFormCreateByTicket(GE_Custom_Form_Data customFormData) {
        return
                customFormData.getTicket_prefix() != null && customFormData.getTicket_prefix() > -1
                        && customFormData.getTicket_code() != null && customFormData.getTicket_code() > -1
                        && customFormData.getTicket_seq() != null && customFormData.getTicket_seq() > -1
                        && customFormData.getTicket_seq_tmp() != null && customFormData.getTicket_seq_tmp() > -1
                        && customFormData.getStep_code() != null && customFormData.getStep_code() > -1;
    }

    /**
     * LUCHE - 09/09/2020
     * <p></p>
     * Metodo que dispara broadcast para avisar tela de ticket que precisa ser atualizada.
     */
    private void sendTicketBroadcastStatus() {
        Intent mIntent = new Intent();
        mIntent.setAction(Constant.WS_FCM);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntent.putExtra(ConstantBaseApp.SW_TYPE, ConstantBaseApp.TK_TICKET_FORM_GPS_LOCATION_UPDATE);
        //
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mIntent);
    }

    private void recordProcess(String data) {
        try {
            String date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "\n ";
            String filePath = getApplicationContext().getFilesDir().getPath().toString() + "/GPS_Histo.txt";
            ToolBox_Inf.writeIn(data + "  ---  " + date, new File(filePath));
            Log.i("GPS_Service", "recordProcess: " + date + data);
        } catch (NullPointerException e) {
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
        if (hasError) {
            ToolBox_Con.setBooleanPreference(getApplicationContext(), Constant.HAS_PENDING_LOCATION, true);
        } else {
            ToolBox_Con.setBooleanPreference(getApplicationContext(), Constant.HAS_PENDING_LOCATION, false);
        }

//        debugNotificacao();

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
//        ToolBox.toastMSG(getApplicationContext(), "onStartCommand");
        if (intent != null) {
            async_gps = intent.getIntExtra(ASYNC_GPS, LOCATION_BACKGROUND);
        } else {
            async_gps = LOCATION_BACKGROUND;
        }

//        Log.i("GPS_Service", "onStartCommand: " + async_gps);

        switch (async_gps) {
            //Parametrizacao para recuperar localizacao durante a exec do N-FORM no intervalo de 5min
            case LOCATION_NFORM_ON:
//                Log.i("GPS_Service", "onStartCommand LOCATION_NFORM_ON");
//                recordProcess("onStartCommand -> async_gps: " + async_gps );
                setLocationListeners(LOCATION_INTERVAL_NFORM_ON);
                break;
            //Parametrizacao para recuperar localizacao apos finalizacao do N-FORM
            case LOCATION_BACKGROUND:
//                Log.i("GPS_Service", "onStartCommand LOCATION_BACKGROUND");
//                recordProcess("onStartCommand -> async_gps: " + async_gps );
                Notification notification = ToolBox_Inf.callPendencyNotification(getApplicationContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        && notification != null) {
                    isForegroundService = true;
//                    Log.d("GPS_Service", "startForeground notification_id: " + LOCATION_NOTIFICATION_ID);
//                    Log.d("GPS_Service", "startForeground isForegroundService: " + isForegroundService);
                    startForeground(LOCATION_NOTIFICATION_ID, notification);
                }
                setLocationListeners(LOCATION_INTERVAL_DEFAULT);
                break;

        }


//        debugNotificacao();

        mLocation_Type = "";
        mLocation_Latitude = "";
        mLocation_Longitude = "";

        status = true;

        return START_STICKY;
    }

    /**
     * BARRIONUEVO - 02-04-2020
     * METODO PARA TESTES
     */
//    private void debugNotificacao() {
//        String latloc, longloc,typeloc, dateloc;
//
//        latloc = ToolBox_Con.getStringPreferencesByKey(getApplicationContext(), Constant.LOCATION_LAT, "Nao ha");
//        longloc = ToolBox_Con.getStringPreferencesByKey(getApplicationContext(), Constant.LOCATION_LNG, "Nao ha");
//        typeloc = ToolBox_Con.getStringPreferencesByKey(getApplicationContext(), Constant.LOCATION_TYPE, "Nao ha");
//        dateloc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(new Date(ToolBox_Con.getLongPreferencesByKey(getApplicationContext(), Constant.LOCATION_DATE, 0)));
//        String freqLoc = "NDa";
//        switch (async_gps){
//            //Parametrizacao para recuperar localizacao durante a exec do N-FORM no intervalo de 5min
//            case LOCATION_NFORM_ON:
//                freqLoc = "5 min";
//                break;
//            //Parametrizacao para recuperar localizacao apos finalizacao do N-FORM
//            case LOCATION_BACKGROUND:
//                freqLoc = "1 min";
//                break;
//
//        }
//
//        String msg =
//                "latloc: " + latloc +
//                "\nlongloc: " + longloc +
//                "\ntypeloc: " + typeloc +
//                "\ndateloc: " + dateloc+
//                "\nFrequancia: " + freqLoc;
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(getApplicationContext())
//                        .setSmallIcon(R.drawable.namoa_logo)
//                        .setContentTitle("Preferencias de Localizacao")
//                        .setContentText("Expanda para ver as infos")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(msg));
//
//        NotificationManager nm = (NotificationManager)
//                getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            nm.notify(123401234, mBuilder.build());
//        } else {
//            nm.notify(123401234, mBuilder.getNotification());
//        }
//    }

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
        loadTranslation();
        isForegroundService = false;
//        setNotificationForForegroundService();
        Notification notification = ToolBox_Inf.callPendencyNotification(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && notification != null) {
            isForegroundService = true;
            Log.d("GPS_Service", "startForeground isForegroundService: " + isForegroundService);
            Log.d("GPS_Service", "startForeground notification_id: " + LOCATION_NOTIFICATION_ID);
            startForeground(LOCATION_NOTIFICATION_ID, notification);
        }
        initializeLocationManager();
        status = true;
    }

    /**
     * BARRIONUEVO 26-06-2020
     * Seta notification responsavel por segurar o servico de busca de posicao GPS.
     */
    private void setNotificationForForegroundService() {
        NotificationManager nm = (NotificationManager)
                getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        //
        Notification notification;
        NotificationCompat.Builder builder;

        builder = ToolBox_Inf.getLowImportanceBuilder(getApplicationContext(), nm);
        builder.setSmallIcon(R.drawable.namoa_logo_white24x24);
        builder.setOngoing(true);
        builder.setContentTitle(getApplicationContext().getString(R.string.title_notification_generic));
        builder.setContentText(hmAux_Trans.get("gps_searching_location"));
        notification = builder.build();
        nm.notify(LOCATION_FOREGROUND_ID, notification);
        startForeground(LOCATION_FOREGROUND_ID, notification);
    }

    private void setLocationListeners(long location_interval) {
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, location_interval, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            status = false;
            Log.d("GPS_Service", "isForegroundService: " + isForegroundService);
            if(isForegroundService){
                stopForeground(true);
            }else {
                stopSelf();
            }
        } catch (IllegalArgumentException ex) {
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, location_interval, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            status = false;
            Log.d("GPS_Service", "isForegroundService: " + isForegroundService);
            if(isForegroundService){
                stopForeground(true);
            }else {
                stopSelf();
            }
        } catch (IllegalArgumentException ex) {
        } catch (Exception e) {
            status = false;
        }
    }

    @Override
    public void onDestroy() {
        status = false;
        ToolBox_Inf.callPendencyNotification(getApplicationContext());

        removeLocationListeners();
//        ToolBox.toastMSG(getApplicationContext(), "onDestroy");
        super.onDestroy();

    }

    private void removeLocationListeners() {
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
//                    recordProcess("removeLocationListener index: " + i);
                } catch (Exception ex) {
//                    recordProcess("removeLocationListener exception: " + ex.toString());
                    ToolBox_Inf.registerException(ex);
                    status = false;
                    Log.d("GPS_Service", "isForegroundService: " + isForegroundService);
                    if(isForegroundService) {
                        stopForeground(true);
                    }else {
                        stopSelf();
                    }
                }
            }
        } else {
//            recordProcess("removeLocationListeners: LocationManager eh null");
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        } else {
//            recordProcess("initLocationManager: " + mLocationManager.toString());
//            Log.i("GPS_Service", mLocationManager.toString());
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



}

