package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.Calendar;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_AL_Quarter extends IntentService {


    public WS_AL_Quarter() {
        super("WS_AL_Quarter");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            long dt_full = ToolBox_Con.getPreference_AL_DT(getApplicationContext());
            long dt_current = Calendar.getInstance().getTimeInMillis();

            long difference = dt_current - dt_full;
            long customer_code =-1L;
            customer_code = ToolBox_Con.getPreference_Customer_Code(getApplicationContext());

            //Se parametro de customer na preferencias estiver igual a -1 não realizar a limpeza.
            if (customer_code == -1L) {
                return;
            }

            //if (difference < (1000 * 60 * 5)){
                ToolBox_Inf.generateNotification(getApplicationContext(), 200);
            //}

        } catch (Exception e) {
            String results = e.toString();
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        Log.d("ALARM", String.valueOf("QUARTER"));
    }

}
