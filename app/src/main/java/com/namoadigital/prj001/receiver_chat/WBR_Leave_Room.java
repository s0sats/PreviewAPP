package com.namoadigital.prj001.receiver_chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service_chat.WS_Leave_Room;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WBR_Leave_Room extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WS_Leave_Room.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }

        startWakefulService(context, mService);
    }
}
