package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.service.ScreenStatusService;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ToolBox_Inf.reprogramAlarms(context);
        ToolBox_Inf.reprogramAlarms_Full_Quarter(context);

        if(ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT) && ToolBox_Inf.isUsrAppLogged(context)){
            if(!AppBackgroundService.isRunning) {
//                try {
//                    File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
//                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Subirá o Serviço\n", log_file);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Subirá o Serviço\n");
                Intent chatService = new Intent(context, AppBackgroundService.class);
                chatService.putExtra(Constant.CHAT_START_SERVICE_CALLER, getClass().getName());
                context.startService(chatService);

            }else{
//                try {
//                    File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
//                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Serviço rodando, não faz nada\n", log_file);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Serviço rodando, não faz nada ");

            }
        }

        if (ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT) && ToolBox_Inf.isUsrAppLogged(context) && !ScreenStatusService.isRunning) {
            Intent mIntent = new Intent(context, ScreenStatusService.class);
            context.startService(mIntent);
        }

    }
}
