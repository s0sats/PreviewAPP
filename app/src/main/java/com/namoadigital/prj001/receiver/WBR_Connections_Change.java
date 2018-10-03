package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.service.AppBackgroundService;
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
                activateUpload(context);
                activateCleanning(context);
                //
                activateDownLoadPDF(context);
                activateDownLoadPicture(context);
                activateLogo(context);
                //activeChatService(context);
                //
                ToolBox_Inf.cleanOldSyncChecklistData(context);
            }
        }

        if (ToolBox_Inf.isUsrAppLogged(context) && !ScreenStatusService.isRunning) {
            Intent mIntent = new Intent(context, ScreenStatusService.class);
            context.startService(mIntent);
        }
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

    private void activateDownLoadPDF(Context context) {
        Intent mIntent = new Intent(context, WBR_DownLoad_PDF.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void activateDownLoadPicture(Context context) {
        Intent mIntent = new Intent(context, WBR_DownLoad_Picture.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void activateCleanning(Context context) {
        Intent mIntent = new Intent(context, WBR_Cleanning.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        bundle.putString(Constant.LOGIN_USER_CODE,ToolBox_Con.getPreference_User_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void activateLogo(Context context){
        Intent mIntentLogo =  new Intent(context,WBR_DownLoad_Customer_Logo.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        bundle.putString(Constant.LOGIN_USER_CODE,ToolBox_Con.getPreference_User_Code(context));
        mIntentLogo.putExtras(bundle);
        //
        context.sendBroadcast(mIntentLogo);
    }


}
