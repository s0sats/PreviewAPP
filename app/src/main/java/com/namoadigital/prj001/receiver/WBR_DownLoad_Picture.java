package com.namoadigital.prj001.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service.WS_DownLoad_Picture;

/**
 * Created by neomatrix on 01/02/17.
 */

public class WBR_DownLoad_Picture extends WakefulBroadcastReceiver {

    public static boolean IS_RUNNING = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WS_DownLoad_Picture.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }
        //TODO Comentado para testar download somnete via wokrker
        //startWakefulService(context, mService);
    }
}
