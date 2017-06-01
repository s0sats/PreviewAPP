package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_AL_Full extends IntentService {

    public WS_AL_Full() {
        super("WS_AL_Full");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            ToolBox_Inf.generateNotification(getApplicationContext(), 100);

        } catch (Exception e) {
            String results = e.toString();
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        Log.d("ALARM", String.valueOf("FULL"));
    }


}
