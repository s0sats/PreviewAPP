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
            //
            /*AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent mIntent = new Intent(context, WBR_Stop_Chat_service.class);
            //
            PendingIntent checkPIntnet = PendingIntent.getBroadcast(
                    context,
                    999,
                    mIntent,
                    PendingIntent.FLAG_NO_CREATE
            );
            if (checkPIntnet == null) {
                //
                PendingIntent pIntnet = PendingIntent.getBroadcast(
                        context,
                        999,
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
                try {
                    File log_file = new File(Constant.SUPPORT_PATH, "screen_off_alarm_log.txt");
                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - WBR_Stop_Chat_service - Setado\n", log_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //
                PendingIntent cancelPIntent = PendingIntent.getBroadcast(
                        context,
                        999,
                        mIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
                //
                alarmManager.cancel(cancelPIntent);
                cancelPIntent.cancel();
                Log.d("ChatEvent", "WBR_Stop_Chat_service - Cancelado (ScreenOff)");
                try {
                    File log_file = new File(Constant.SUPPORT_PATH, "screen_off_alarm_log.txt");
                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - WBR_Stop_Chat_service - Setado\n", log_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
            //
            //Intent intent1 = new Intent(context,WBR_Stop_Chat_service.class);
            // context.sendBroadcast(intent1);
            Intent serviceStopChat = new Intent(context,S_StopChatService.class);
            context.startService(serviceStopChat);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOn = true;
            /*Intent mIntent = new Intent(context, WBR_Stop_Chat_service.class);
            //
            PendingIntent pIntent = PendingIntent.getBroadcast(
                    context,
                    999,
                    mIntent,
                    PendingIntent.FLAG_NO_CREATE
            );
            //
            if (pIntent != null) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                //alarmManager.cancel(pIntent);
                //
                //pIntent.cancel();
                //
                Log.d("ChatEvent", "WBR_Stop_Chat_service - Cancelado (ScreenOn)");
                try {
                    File log_file = new File(Constant.SUPPORT_PATH, "screen_off_alarm_log.txt");
                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - WBR_Stop_Chat_service - Cancelado (ScreenOn)\n", log_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //
            if (ToolBox_Inf.isUsrAppLogged(context) && !AppBackgroundService.isRunning) {
                //
                Intent chatService = new Intent(context, AppBackgroundService.class);
                context.startService(chatService);
            }*/
            S_StopChatService.keepOnRunning = false;
        }

        Log.d("STATUS_DISPLAY", screenOn ? "LIGADO" : "DESLIGADO");
    }
}
