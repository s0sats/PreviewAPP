package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_Cleanning extends IntentService {

    public WS_Cleanning() {
        super("WS_Cleanning");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

           /* Toast.makeText(
                    getApplicationContext(),
                    "Rodou o Alarm",
                    Toast.LENGTH_SHORT
            ).show();  */

            Log.d("ALARM", "EXECUTOU ALARM");


        } catch (Exception e) {
            String results = e.toString();
        } finally {
        }
    }
}
