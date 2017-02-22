package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.namoadigital.prj001.service.WS_Cleanning;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_Cleanning extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        activateCleanning(context);

    }

    private void activateCleanning(Context context) {
        Intent mIntent = new Intent(context, WS_Cleanning.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.startService(mIntent);
    }
}
