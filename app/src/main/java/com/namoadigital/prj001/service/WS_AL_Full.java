package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_AL_Full extends IntentService {

    long customer_code =-1L;

    public WS_AL_Full() {
        super("WS_AL_Full");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            customer_code = ToolBox_Con.getPreference_Customer_Code(getApplicationContext());
            /*
                BARRIONUEVO - 05/12/2019
                Tratativa para nao executar servicos quando deslogado
             */
            //Se parametro de customer na preferencias estiver igual a -1 não gera notification.
            if (customer_code == -1L) {
                return;
            }
            ToolBox_Inf.generateNotification(getApplicationContext(),
                //100
                ConstantBaseApp.ALARM_REQUEST_CODE_WS_AL_FULL
            );

        } catch (Exception e) {
            String results = e.toString();
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        Log.d("ALARM", String.valueOf("FULL"));
    }


}
