package com.namoadigital.prj001.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
    //

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.isRunning = true;
        try {
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService OnStart \n", log_file);
        }catch (Exception e){
            e.printStackTrace();
        }
        boolean isFcmCall = intent.getBooleanExtra(Constant.WS_FCM,false);
        //
        singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
        if(isFcmCall){
            singletonWebSocket.turnOnFcmCall();
        }
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE);
        //
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
        try {
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - AppBackgroundService onDestroy \n", log_file);
        }catch (Exception e){
            e.printStackTrace();
        }
        //
        singletonWebSocket.attemptDisconnect("App Socket.disconnect()");
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE);
        //
        singletonWebSocket.destroySingletonWebSocket();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

