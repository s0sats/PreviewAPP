package com.namoadigital.prj001.receiver_chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service_chat.WS_Add_User_Room_AP;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WBR_Add_User_Room_AP extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WS_Add_User_Room_AP.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }

        startWakefulService(context, mService);
    }
}
