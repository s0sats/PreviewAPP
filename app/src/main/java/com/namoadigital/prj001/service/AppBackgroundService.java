package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 27/11/17.
 */

public class AppBackgroundService extends Service {

    public static boolean isRunning;
    private SingletonWebSocket singletonWebSocket;
    //APAGAR APOS TESTE
    //private File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
    private String serviceLastCaller = "";
    //

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.isRunning = true;
        if ( /*ToolBox_Inf.parameterExists(getApplicationContext(),Constant.PARAM_CHAT)
             &&*/ ToolBox_Con.getPreference_Status_Login(getApplicationContext()).equals(Constant.LOGIN_STATUS_OK)
            ) {

            try {
                //ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService OnStart \n", log_file);
                if (intent != null) {
                    serviceLastCaller = intent.getStringExtra(Constant.CHAT_START_SERVICE_CALLER);
                }
                //ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService Caller: "+serviceLastCaller+" \n", log_file);
                Log.d("ChatEvent", ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService Caller: " + serviceLastCaller + " \n");
                //
                singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
                //
                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE);
                //
            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }else{
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
//        try {
//            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService onDestroy \n", log_file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Log.d("ChatEvent"," onDestroy AppBackgroundService \n");
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE);
        //LUCHE - 23/07/2019
        //Adicionado tratativa de null antes de chamardestroySingletonWebSocket
        //A falta da tratativa, gerava crash no app caso o usr perdesse sessão
        //enquanto device apagado ou desligado e a ultima tela carregada fosse a
        //act003
        if(singletonWebSocket != null) {
            singletonWebSocket.destroySingletonWebSocket();
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        try {
//            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService onBind \n", log_file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Log.d("ChatEvent"," onBind AppBackgroundService \n");
        return null;
    }
}

