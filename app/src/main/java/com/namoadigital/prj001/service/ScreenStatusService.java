package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by neomatrix on 1/16/18.
 */

public class ScreenStatusService extends Service {

    public static boolean isRunning = false;

    private ScreenStatusReceiver mScreenStateReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;

        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);

        mScreenStateReceiver = new ScreenStatusReceiver();

        registerReceiver(mScreenStateReceiver, screenStateFilter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(mScreenStateReceiver);

        isRunning = false;

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

