package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //ToolBox_Inf.reprogramAlarms(context);

    }

    private void activateCleanning(Context context) {
        Intent mIntent = new Intent(context, WBR_Cleanning.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
