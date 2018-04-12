package com.namoadigital.prj001.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 1/16/18.
 */

public class ScreenStatusReceiver extends BroadcastReceiver {

    //private File log_file = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        //File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
        try {
            boolean screenOn;
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                screenOn = false;
                //
                Log.d("ChatEvent", "Status da Tela :DESLIGADO");
                //
                //ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "  ===================================>DISPLAY_OFF<===================================\n.", log_file);
                //
                Intent chatService = new Intent(context, AppBackgroundService.class);
                context.stopService(chatService);

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                screenOn = true;
                Log.d("ChatEvent", "Status da Tela : LIGADO");
                //ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "\n___________________________________>DISPLAY_ON<____________________________________\n.", log_file);
                //
                if (
                    ToolBox_Inf.parameterExists(context,Constant.PARAM_CHAT)
                    && ToolBox_Inf.isUsrAppLogged(context)
                    && ToolBox_Con.getPreference_Status_Login(context).equals(Constant.LOGIN_STATUS_OK)
                ){
                    if(!AppBackgroundService.isRunning) {
                        Intent chatService = new Intent(context, AppBackgroundService.class);
                        chatService.putExtra(Constant.CHAT_START_SERVICE_CALLER, getClass().getName());
                        context.startService(chatService);
                    }else{
                        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                        singletonWebSocket.attemptSendLogin();
                    }
                }
                Log.d("STATUS_DISPLAY", screenOn ? "LIGADO" : "Ligada");
            }else{
                String whatElse = intent.getAction().toString();
                Log.d("ChatEvent", "Status da Tela : OUTRA COISA o.O: " + whatElse );
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
