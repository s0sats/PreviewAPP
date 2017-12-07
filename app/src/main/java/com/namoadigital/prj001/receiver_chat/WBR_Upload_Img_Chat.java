package com.namoadigital.prj001.receiver_chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service_chat.WS_Upload_Img_Chat;

/**
 * Created by neomatrix on 01/02/17.
 */

public class WBR_Upload_Img_Chat extends WakefulBroadcastReceiver {

    public static boolean IS_RUNNING = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WS_Upload_Img_Chat.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }

        startWakefulService(context, mService);
    }
}
