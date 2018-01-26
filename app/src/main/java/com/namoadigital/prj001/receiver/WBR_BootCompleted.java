package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
            ToolBox_Inf.defineChatServiceAction(context,Constant.WBR_BOOTCOMPLETED,true);
//            if(!AppBackgroundService.isRunning) {
//                Intent chatService = new Intent(context, AppBackgroundService.class);
//                chatService.putExtra(Constant.CHAT_START_SERVICE_CALLER, getClass().getName());
//                context.startService(chatService);
//            }else{
//                try{
//                    File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
//                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Startou o singletonGetInstance()\n", log_file);
//                    Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Startou o singletonGetInstance()\n");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
//                singletonWebSocket.attemptSendLogin();
//            }
        }

        if (ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT) && ToolBox_Inf.isUsrAppLogged(context) && !ScreenStatusService.isRunning) {
            Intent mIntent = new Intent(context, ScreenStatusService.class);
            context.startService(mIntent);
        }

    }
}
