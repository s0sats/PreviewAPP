package com.namoadigital.prj001.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service.WS_DownLoad_Customer_Logo;

/**
 * Created by DANIEL.LUCHE on 07/03/2017.
 */

public class WBR_DownLoad_Customer_Logo extends WakefulBroadcastReceiver {

    public static boolean IS_RUNNING = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WS_DownLoad_Customer_Logo.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(mService);
        } else {
            startWakefulService(context, mService);
        }

    }
}
