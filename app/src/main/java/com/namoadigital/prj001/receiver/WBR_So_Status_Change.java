package com.namoadigital.prj001.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service.WSSoStatusChange;

public class WBR_So_Status_Change extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WSSoStatusChange.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }
        startWakefulService(context, mService);
    }
}
