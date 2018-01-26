package com.namoadigital.prj001.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 1/16/18.
 */

public class ScreenStatusReceiver extends BroadcastReceiver {

    public static boolean screenOn;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOn = false;
            //
            if(!ChatPowerService.isRunning) {
                Intent powerService = new Intent(context, ChatPowerService.class);
                context.startService(powerService);
            }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOn = true;
            //
            if (ToolBox_Inf.isUsrAppLogged(context) ) {
                ToolBox_Inf.defineChatServiceAction(context, Constant.SCREEN_STATUS_RECEIVER,true);
//                if(!AppBackgroundService.isRunning) {
//                    Intent chatService = new Intent(context, AppBackgroundService.class);
//                    chatService.putExtra(Constant.CHAT_START_SERVICE_CALLER, getClass().getName());
//                    context.startService(chatService);
//                }else{
//                    try{
//                        File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
//                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - DisplayOn Startou o singletonGetInstance()\n", log_file);
//                        Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - DisplayOn Startou o singletonGetInstance()\n");
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
//                    singletonWebSocket.attemptSendLogin();
//                }
            }
        }

        Log.d("STATUS_DISPLAY", screenOn ? "LIGADO" : "DESLIGADO");
    }
}
