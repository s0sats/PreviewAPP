package com.namoadigital.prj001.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by neomatrix on 1/16/18.
 */

public class ScreenStatusReceiver extends BroadcastReceiver {

    private boolean screenOn;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOn = true;
            //
            if(!AppBackgroundService.isRunning){
                Intent chatService = new Intent(context, AppBackgroundService.class);
                context.startService(chatService);
            }
        }

        Log.d("STATUS_DISPLAY", screenOn ? "LIGADO" : "DESLIGADO");
    }
}
