package com.namoadigital.prj001.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_011;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_SERVICE_MODE_ACTIVED;
import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_SERVICE_MODE_LOGIN;
import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_SERVICE_MODE_SCHEDULED;

/**
 * Created by neomatrix on 27/11/17.
 */

public class AppBackgroundService extends Service {

    public static boolean isRunning;
    private SingletonWebSocket singletonWebSocket;
    //APAGAR APOS TESTE
    //private File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
    private String serviceLastCaller = "";
    private HMAux hmAux_Trans;
    /**
     * Variavel de controle utiliza para finalizar o servico.
     */
    public static String serviceChatMode;
    private String notificationContentText = "";
    private Handler mHandler;
    private Runnable mRunnable;
    private static int PROGRESS_TIME_OUT = 1000 * 30 ;

    @Override
    public void onCreate() {
        //
        Context context = getApplicationContext();
        List<String> translist = new ArrayList<>();
        //
        if(mHandler == null && mRunnable == null){
            configServiceHandler();
        }
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                "",
                "0",
                ToolBox_Con.getPreference_Translate_Code(context),
                translist,
                ToolBox_Con.getPreference_Customer_Code(context)
        );
        //
        setNotificationContentText();
        setNotificationForForegroundService(true);
        //
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
                    serviceChatMode = intent.getStringExtra(Constant.CHAT_SERVICE_MODE);
                } else {
                    serviceChatMode = CHAT_SERVICE_MODE_LOGIN;
                }
                //
                setNotificationContentText();
                setNotificationForForegroundService(false);
                //
                startServiceTimeout();

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
        return START_NOT_STICKY;
    }

    private void setNotificationContentText() {
        String chat_service_mode_desc;
        //
        switch (serviceChatMode){
            case CHAT_SERVICE_MODE_ACTIVED:
                chat_service_mode_desc =  hmAux_Trans.get("sys_active_chat_notification_detail");
                break;
            case CHAT_SERVICE_MODE_LOGIN:
            case CHAT_SERVICE_MODE_SCHEDULED:
            default:
                chat_service_mode_desc = hmAux_Trans.get("sys_sync_chat_notification_detail");

        }
        //
        notificationContentText = chat_service_mode_desc;
        //
    }


    private void setNotificationForForegroundService(boolean executeService) {
        NotificationManager nm = (NotificationManager)
            getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        //
        Notification notification;
        NotificationCompat.Builder builder;

        builder = ToolBox_Inf.getLowImportanceBuilder(getApplicationContext(), nm);
        builder.setSmallIcon(R.drawable.ic_n_chat);
        builder.setOngoing(true);
        builder.setContentTitle(getApplicationContext().getString(R.string.title_notification_generic));
        builder.setContentText(notificationContentText);
        notification = builder.build();
        nm.notify(ConstantBaseApp.NOTIFICATION_CHAT_FOREGROUND_SERVICE, notification);
        if(executeService) {
            startForeground(ConstantBaseApp.NOTIFICATION_CHAT_FOREGROUND_SERVICE, notification);
        }
    }

    private void configServiceHandler() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                Log.d("ChatEvent"," mRunnable");
                Log.d("ChatEvent"," serviceChatMode: " + serviceChatMode);
                if(hasLifeSpanMode()) {
                    stopSelf();
                }
            }
        };
    }

    private boolean hasLifeSpanMode() {
        return CHAT_SERVICE_MODE_LOGIN.equals(serviceChatMode)
        || CHAT_SERVICE_MODE_SCHEDULED.equals(serviceChatMode);
    }

    private void startServiceTimeout() {
        if(hasLifeSpanMode()) {
            mHandler.postDelayed(mRunnable, PROGRESS_TIME_OUT);
        }
    }

    private void stopServiceTimeout(){
        Log.d("ChatEvent"," stopServiceTimeout: " + mHandler);
        if(mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
    @Override
    public void onDestroy() {
        this.isRunning = false;
        //
        stopServiceTimeout();
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
        //
        if(hasChatPendencies()){
            //ToolBox_Inf.scheduleWorkQuarterChatRefresh(getApplicationContext());
            ToolBox_Inf.scheduleWorkChatRefresh(getApplicationContext());
       }
        //else{
//            WorkManager.getInstance(getApplicationContext()).cancelUniqueWork(Work_Quarter_Chat_Refresh.WORKER_TAG);
//        }
    }

    private boolean hasChatPendencies() {
        CH_MessageDao messageDao = new CH_MessageDao(getApplicationContext());
        //
        ArrayList<CH_Message> offlineMsgs =
            (ArrayList<CH_Message>) messageDao.query(
                new CH_Message_Sql_011().toSqlQuery()
            );
        //
        Log.d("ChatEvent","AppBackgroundService, chatPendencies = " + (offlineMsgs == null ? "null" : offlineMsgs.size()));
        //
        return offlineMsgs != null && offlineMsgs.size() > 0 ;
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

