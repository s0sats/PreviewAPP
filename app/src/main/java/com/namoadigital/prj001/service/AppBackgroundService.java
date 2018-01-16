package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

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
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE);
        //
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
        //
        singletonWebSocket.attemptDisconnect("App Socket.disconnect()");
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

