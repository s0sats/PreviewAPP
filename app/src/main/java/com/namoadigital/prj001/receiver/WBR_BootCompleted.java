package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.service.ScreenStatusService;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO APAGAR APÓS SUBSTITUIR PELOS WORKERS
        //ToolBox_Inf.reprogramAlarms(context);
        //TODO APAGAR APÓS SUBSTITUIR PELOS WORKERS
        //ToolBox_Inf.reprogramAlarms_Full_Quarter(context);


//        ToolBox_Inf.reprogramAlarms(context);
//        ToolBox_Inf.reprogramAlarms_Full_Quarter(context);
        /*
            Barrionuevo 26-06-2020
            Start de servico de localizacao ao iniciar device e estando com pendencia de gps.
         */
        if (!SV_LocationTracker.status
                && ToolBox_Inf.isUsrAppLogged(context)) {
            int pendencies = ToolBox_Inf.getLocationPendencies(context);
            if(pendencies > 0) {
                ToolBox_Inf.call_Location_Tracker_On_Background(context, SV_LocationTracker.LOCATION_BACKGROUND);
            }
        }
        //TODO REVER BAIAXO FOI COMENTAOD PARA PARAR DE DAR CRAHS
        //region comentario teste target9 27012021
//        if(/*ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT) && */ToolBox_Inf.isUsrAppLogged(context) && ToolBox_Con.getPreference_Status_Login(context).equals(Constant.LOGIN_STATUS_OK)){
//            if(!AppBackgroundService.isRunning) {
////                try {
////                    File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
////                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Subirá o Serviço\n", log_file);
////                }catch (Exception e){
////                    e.printStackTrace();
////                }
//                Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Subirá o Serviço\n");
//                Intent chatService = new Intent(context, AppBackgroundService.class);
//                chatService.putExtra(Constant.CHAT_START_SERVICE_CALLER, getClass().getName());
//                context.startService(chatService);
//
//            }else{
////                try {
////                    File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
////                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Serviço rodando, não faz nada\n", log_file);
////                }catch (Exception e){
////                    e.printStackTrace();
////                }
//                Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - BootCompleted Serviço rodando, não faz nada ");
//
//            }
//        }
        //endregion

        if (/*ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT) &&*/ ToolBox_Inf.isUsrAppLogged(context) && ToolBox_Con.getPreference_Status_Login(context).equals(Constant.LOGIN_STATUS_OK) && !ScreenStatusService.isRunning) {
            Intent mIntent = new Intent(context, ScreenStatusService.class);
            context.startService(mIntent);
        }

    }
}
