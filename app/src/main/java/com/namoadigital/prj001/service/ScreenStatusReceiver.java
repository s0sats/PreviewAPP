package com.namoadigital.prj001.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 1/16/18.
 */

public class ScreenStatusReceiver extends BroadcastReceiver {

    private boolean screenOn;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOn = false;
            //
           /* AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent mIntent = new Intent(context, WBR_Stop_Chat_service.class);
            //
            PendingIntent checkPIntnet = PendingIntent.getBroadcast(
                    context,
                    0,
                    mIntent,
                    PendingIntent.FLAG_NO_CREATE
            );
            if (checkPIntnet == null) {
                //
                PendingIntent pIntnet = PendingIntent.getBroadcast(
                        context,
                        0,
                        mIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
                //
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (1 * 60 * 1000),
                        pIntnet
                );
                Log.d("ChatEvent", "WBR_Stop_Chat_service - Setado");
            }else{
                //
                PendingIntent cancelPIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        mIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
                //
                alarmManager.cancel(cancelPIntent);
                cancelPIntent.cancel();
                Log.d("ChatEvent", "WBR_Stop_Chat_service - Cancelado (ScreenOff)");
            }*/
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOn = true;
           /* Intent mIntent = new Intent(context, WBR_Stop_Chat_service.class);
            //
            PendingIntent pIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    mIntent,
                    PendingIntent.FLAG_NO_CREATE
            );
            //
            if (pIntent != null) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pIntent);
                //
                pIntent.cancel();
                //
                Log.d("ChatEvent", "WBR_Stop_Chat_service - Cancelado (ScreenOn)");
            }*/

            //
            if (ToolBox_Inf.isUsrAppLogged(context) && !AppBackgroundService.isRunning) {
                //
                Intent chatService = new Intent(context, AppBackgroundService.class);
                context.startService(chatService);
            }
        }

        Log.d("STATUS_DISPLAY", screenOn ? "LIGADO" : "DESLIGADO");
    }
}
