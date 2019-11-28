package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class WS_Ticket_Download extends IntentService {

    public static final String TICKET_PREFIX = "TICKET_PREFIX";
    public static final String TICKET_CODE = "TICKET_CODE";

    public WS_Ticket_Download() { super("WS_Ticket_Download");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
