package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.namoadigital.prj001.receiver.WBR_AP_Search;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 23/02/2018.
 */

public class WS_AP_Search extends IntentService {

    public WS_AP_Search() {
        super("WS_AP_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {


        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_AP_Search.completeWakefulIntent(intent);
        }

    }
}
