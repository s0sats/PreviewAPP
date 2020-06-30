package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.service.ScreenStatusService;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_Connections_Change extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String status = ToolBox_Con.checkConStatus(context);

        if (!status.equalsIgnoreCase("NO_SERVICE")) {
            if (!ToolBox_Con.getPreference_Service(context).equals("NO_SERVICE")) {
                //TODO DESCOMENTAR E REVISAR APÓS CRIAÇÃO DOS WORKERS
                //TODO Verificar necessidade colocar chamada do Work_Upload_Img aqui.
                //Como antigamente havia o trigger do upload_img aqui, algumas rotinas só chamavam o upload
                //se tivesse conexão. Com o worker, devemos sempre chamar, pois quem verifica a conexão será o
                //workmanager.
                //LUCHE - 30/06/2020
                //Substituido o antigo serviço pelo Worker de Download de Img
                //ToolBox_Inf.scheduleAllDownloadWorkers(context);
                /**
                 * TESTE UPLOAD VIA WORKER
                 * APENAS TESTES , REMOVER APÓS TESTAR 22/06/2020
                activateCleanning(context);
                //
                //activeChatService(context);
                //Add disparo do serviço de UnsentImgs
                activateUnsentUpload(context);
                //
                activateLocationService(context);
                //
                ToolBox_Inf.cleanOldSyncChecklistData(context);
                 */
            }
        }

        if (ToolBox_Inf.isUsrAppLogged(context) && !ScreenStatusService.isRunning) {
            Intent mIntent = new Intent(context, ScreenStatusService.class);
//            context.startService(mIntent);
        }
        ToolBox_Inf.callPendencyNotification(context);
    }

    private void activeChatService(Context context) {
        if(/*ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT) && */ToolBox_Inf.isUsrAppLogged(context) ){
            if(ToolBox_Inf.isScreenOn(context)){
                if(!AppBackgroundService.isRunning){
                    Intent chatService = new Intent(context, AppBackgroundService.class);
                    chatService.putExtra(Constant.CHAT_START_SERVICE_CALLER, Constant.WBR_CONNECTIONS_CHANGE);
                    context.startService(chatService);
                }else{
                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                    singletonWebSocket.attemptSendLogin();
                }
            }
        }

    }
    /**
     * LUCHE - 14/05/2019
     * Metodo que chama serviço de upload das imagens do diretorio UnsentImgs
     * @param context
     */
    private void activateUnsentUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Other_User_Img.class);
        context.sendBroadcast(mIntent);
    }
    /**
     * BARRIONUEVO - 30/04/2020
     * Metodo que chama serviço de localizacao caso usuario esteja logado, o servico parado e pende
     * cias de envio.
     * @param context
     */
    private void activateLocationService(Context context) {
        if (!SV_LocationTracker.status
        && ToolBox_Inf.isUsrAppLogged(context)) {
            int pendencies = ToolBox_Inf.getLocationPendencies(context);
            if(pendencies>0) {
                ToolBox_Inf.call_Location_Tracker_On_Background(context, SV_LocationTracker.LOCATION_BACKGROUND);
            }
        }
    }

}
