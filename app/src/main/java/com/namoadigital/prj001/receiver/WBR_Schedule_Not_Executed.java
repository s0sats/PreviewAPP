package com.namoadigital.prj001.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service.WsScheduleNotExecuted;

public class WBR_Schedule_Not_Executed  extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WsScheduleNotExecuted.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }

        startWakefulService(context, mService);

    }
}
