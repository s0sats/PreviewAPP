package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.service.WS_AL_Full;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_AL_Full extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        activateWBR_AL_Full(context);

    }

    private void activateWBR_AL_Full(Context context) {
        Intent mIntent = new Intent(context, WS_AL_Full.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
//        context.startService(mIntent);
    }
}
