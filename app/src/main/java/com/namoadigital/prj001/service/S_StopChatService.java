package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 23/01/2018.
 */

public class S_StopChatService extends Service {

    public static boolean isRunning;
    private Thread mThread = null;
    public static boolean keepOnRunning = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.isRunning = true;
        this.keepOnRunning = true;
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
                    PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");

                    wl.acquire();

                    int index = 0;
                    while (keepOnRunning) {
                        Thread.sleep(1000);
                        //
                        if (index == 59) {
                            break;
                        }
                        index++;
                    }
                    //
                    Intent chatService = new Intent(getApplicationContext(),AppBackgroundService.class);
                    getApplicationContext().stopService(chatService);
                    //
                    wl.release();
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
        this.keepOnRunning = true;
        //
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
