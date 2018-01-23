package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.service.ScreenStatusService;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ToolBox_Inf.reprogramAlarms(context);
        ToolBox_Inf.reprogramAlarms_Full_Quarter(context);

        if(ToolBox_Inf.isUsrAppLogged(context) && !AppBackgroundService.isRunning){
            Intent chatService = new Intent(context, AppBackgroundService.class);
            context.startService(chatService);
        }

        if (ToolBox_Inf.isUsrAppLogged(context) && !ScreenStatusService.isRunning) {
            Intent mIntent = new Intent(context, ScreenStatusService.class);
            context.startService(mIntent);
        }

    }
}
