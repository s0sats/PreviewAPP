package com.namoadigital.prj001.receiver_chat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;

/**
 * Created by d.luche on 22/01/2018.
 */

public class WBR_Stop_Chat_service extends WakefulBroadcastReceiver {
    public static boolean isRunning = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        //region FinalGambi
        Log.d("ChatEvent","WBR_Stop_Chat_service - Acionado");
        //Intent mService = new Intent(context, WS_Stop_Chat_Service.class);
        //startWakefulService(context, mService);
        //endregion
        /*PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PM_SingletonWebSocket");
        wl.acquire();*/



        /*if(AppBackgroundService.isRunning){
            Intent chatService = new Intent(context, AppBackgroundService.class);
            context.stopService(chatService);
        }*/


        /*//
        Intent mIntent = new Intent(context,WBR_Stop_Chat_service.class);
        //
        PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                0,
                mIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        //
        pIntent.cancel();*/
        //wl.release();
        try {
            File log_file = new File(Constant.SUPPORT_PATH, "screen_off_alarm_log.txt");
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - WBR_Stop_Chat_service - Acionado\n", log_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WBR_Stop_Chat_service.completeWakefulIntent(intent);
    }
}
