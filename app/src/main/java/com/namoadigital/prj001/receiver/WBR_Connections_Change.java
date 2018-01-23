package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.service.ScreenStatusService;
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
                //
                ToolBox_Inf.cleanOldSyncChecklistData(context);
            }
        }

        if (ToolBox_Inf.isUsrAppLogged(context) && !ScreenStatusService.isRunning) {
            Intent mIntent = new Intent(context, ScreenStatusService.class);
            context.startService(mIntent);
        }
    }

    private void activateDownLoadPDF(Context context) {
        Intent mIntent = new Intent(context, WBR_DownLoad_PDF.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void activateDownLoadPicture(Context context) {
        Intent mIntent = new Intent(context, WBR_DownLoad_Picture.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void activateCleanning(Context context) {
        Intent mIntent = new Intent(context, WBR_Cleanning.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void activateLogo(Context context){
        Intent mIntentLogo =  new Intent(context,WBR_DownLoad_Customer_Logo.class);
        Bundle bundle = new Bundle();

        mIntentLogo.putExtras(bundle);
        //
        context.sendBroadcast(mIntentLogo);
    }


}
