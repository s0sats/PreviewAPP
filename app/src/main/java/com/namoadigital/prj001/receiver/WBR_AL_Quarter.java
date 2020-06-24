package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.service.WS_AL_Quarter;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_AL_Quarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        activateWBR_AL_Quarter(context);

    }

    private void activateWBR_AL_Quarter(Context context) {
        Intent mIntent = new Intent(context, WS_AL_Quarter.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
       // context.startService(mIntent);
    }
}
