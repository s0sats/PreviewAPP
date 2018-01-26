package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.Calendar;

/**
 * Created by d.luche on 23/01/2018.
 */

public class ChatPowerService extends Service {

    public static boolean isRunning;
    private Thread mThread = null;
    public static boolean keepOnRunning = false;
    public static Calendar lastCall = null;
    private PowerManager pm = null;
    private PowerManager.WakeLock wl = null;
    private File log_file = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        if (log_file == null) {
            log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
        }
        this.isRunning = true;
        this.keepOnRunning = true;
        pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        if(lastCall == null){
            lastCall = Calendar.getInstance();
        }
        try{
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "PowerService lastCall: " + lastCall.getTime().toString()+ " \n", log_file);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("ChatEvent","PowerService lastCall: " + lastCall.getTime().toString());
        //
        startHolding();
        //
        return START_NOT_STICKY;
    }

    private void startHolding() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //wl.acquire(5*60*1000L /*10 minutes*/);
                    wl.acquire();
                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "PowerService: Requisitou energia  \n", log_file);
                    Log.d("ChatEvent","PowerService: Requisitou energia");

                    //
                    int index = 0;
                    //
                    while (keepOnRunning) {
                        Thread.sleep(30 *1000);
                        //
                       // if (index == 25) {
//                            Log.d("ChatEvent","Forçou parada da thread");
//                            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "PowerService: Forçou parada da thread  \n", log_file);
//                            break;
//                        }
                        //Log.d("ChatEvent","Counter: " + String.valueOf(index));
                        long diff = Calendar.getInstance().getTimeInMillis() - lastCall.getTimeInMillis();
                        float diffInMinutes = (float) diff / (60*1000);
                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "PowerService: DIFF EM MINUTS:" + String.valueOf(diffInMinutes)+"\n", log_file);
                        Log.d("ChatEvent","PowerService: DIFF EM MINUTS:" + String.valueOf(diffInMinutes));
                        if(diffInMinutes > 10.0f ){
                            keepOnRunning = false;
                        }
                        //
                        index++;
                    }
                    //
                    wl.release();
                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "PowerService: Liberou energia\n", log_file);
                    Log.d("ChatEvent","PowerService: Liberou energia");
                    //
                    stopSelf();
                } catch (Exception e) {
                    ToolBox_Inf.registerException(getClass().getName(),e);
                }
            }
        });
        //
        mThread.start();
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
        this.keepOnRunning = false;
        //
        try{
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "PowerService: onDestroy \n", log_file);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("ChatEvent","PowerService: onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
