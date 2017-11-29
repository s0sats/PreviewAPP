package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.namoadigital.prj001.singleton.SingletonWebSocket;

/**
 * Created by neomatrix on 27/11/17.
 */

public class AppBackgroundService extends Service {

    public static boolean isRunning;
    private SingletonWebSocket singletonWebSocket;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.isRunning = true;
        //
        singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
        //
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
        //
        singletonWebSocket.disconnect();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

