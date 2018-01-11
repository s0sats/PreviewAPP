package com.namoadigital.prj001.receiver_chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service_chat.WS_Room_Info;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WBR_Room_Info extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WS_Room_Info.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }

        startWakefulService(context, mService);
    }
}
