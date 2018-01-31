package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;

/**
 * Created by neomatrix on 27/11/17.
 */

public class AppBackgroundService extends Service {

    public static boolean isRunning;
    private SingletonWebSocket singletonWebSocket;
    //APAGAR APOS TESTE
    private File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
    private String serviceLastCaller = "";
    //

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.isRunning = true;
        try {
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService OnStart \n", log_file);
            //boolean isFcmCall = intent.getBooleanExtra(Constant.WS_FCM,false);
            if(intent != null) {
                serviceLastCaller = intent.getStringExtra(Constant.CHAT_START_SERVICE_CALLER);
            }
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService Caller: "+serviceLastCaller+" \n", log_file);
            Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService Caller: "+serviceLastCaller+" \n");
            //
            //boolean mSocketSetted = SingletonWebSocket.isSocketSetted();
//            boolean mSocketForced = false;
//            if(SingletonWebSocket.isSocketSetted()){
//                SingletonWebSocket.mSocket = null;
//                mSocketForced  = true;
//            }
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService chama zeraSingleton. \n", log_file);
            Log.d("ChatEvent"," AppBackgroundService chama zeraSingleton.\n");
            //SingletonWebSocket.zeraSingleton(log_file);
            //
            Log.d("ChatEvent"," AppBackgroundService mSocket. SingletonWebSocket.mSocket value = "+SingletonWebSocket.mSocket+" \n");
            //boolean mSocketSetted = SingletonWebSocket.isSocketSetted();
            singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
            //singletonWebSocket.initConnection();
//            if(mSocketSetted){
//                singletonWebSocket.initConnection();
//            }
//            if(mSocketForced){
//                singletonWebSocket.initConnection();
//            }
//        if(isFcmCall){
//            singletonWebSocket.turnOnFcmCall();
//        }
            //
            ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE);
            //
        } catch (Exception e) {
            e.printStackTrace();
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
        return START_STICKY;
       // return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
        try {
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService onDestroy \n", log_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("ChatEvent"," onDestroy AppBackgroundService \n");
        //
        //singletonWebSocket.attemptDisconnect("App Socket.disconnect()");
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE);
        //
        //singletonWebSocket.destroySingletonWebSocket();
        //singletonWebSocket.destroySingletonWebSocketV2();
        singletonWebSocket.destroySingletonWebSocketV3();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        try {
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService onBind \n", log_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("ChatEvent"," onBind AppBackgroundService \n");
        return null;
    }
}

