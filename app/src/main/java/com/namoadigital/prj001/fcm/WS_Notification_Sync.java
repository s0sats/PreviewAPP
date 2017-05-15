package com.namoadigital.prj001.fcm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.model.TGoogle_Env;
import com.namoadigital.prj001.model.TGoogle_Rec;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.Calendar;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_Notification_Sync extends IntentService {

    public WS_Notification_Sync() {
        super("WS_Notification_Sync");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            if (!ToolBox_Con.getPreference_SYNC_REQUIRED(getApplicationContext()).equalsIgnoreCase("")) {
                ToolBox_Inf.call_Notification_Sync(getApplicationContext(), 11);
            }

        } catch (Exception e) {
        }
    }
}
