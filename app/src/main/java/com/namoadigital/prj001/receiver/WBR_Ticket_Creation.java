package com.namoadigital.prj001.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.namoadigital.prj001.service.WSTicketCreation;

public class WBR_Ticket_Creation extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        Intent mService = new Intent(context, WSTicketCreation.class);

        if (bundle != null) {
            mService.putExtras(bundle);
        } else {
            mService.putExtras(new Bundle());
        }
        //
        startWakefulService(context, mService);
        //
    }
}