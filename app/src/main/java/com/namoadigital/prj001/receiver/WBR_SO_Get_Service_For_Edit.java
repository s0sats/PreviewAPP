package com.namoadigital.prj001.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service.WS_SO_Get_Service_For_Edit;

public class WBR_SO_Get_Service_For_Edit extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WS_SO_Get_Service_For_Edit.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }

        startWakefulService(context, mService);
    }
}
