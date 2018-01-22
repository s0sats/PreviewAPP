package com.namoadigital.prj001.receiver_chat;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.namoadigital.prj001.service.AppBackgroundService;

/**
 * Created by d.luche on 22/01/2018.
 */

public class WBR_Stop_Chat_service extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ChatEvent","WBR_Stop_Chat_service - Acionado");
        if(AppBackgroundService.isRunning){
            Intent chatService = new Intent(context, AppBackgroundService.class);
            context.stopService(chatService);
        }
        //
        Intent mIntent = new Intent(context,WBR_Stop_Chat_service.class);
        //
        PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                0,
                mIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        //
        pIntent.cancel();
    }
}
