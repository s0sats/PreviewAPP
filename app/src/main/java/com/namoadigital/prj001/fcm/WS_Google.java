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

public class WS_Google extends IntentService {

    public WS_Google() {
        super("WS_Google");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            if (!ToolBox_Con.getPreference_Session_App(getApplicationContext()).equalsIgnoreCase("") &&
                    !ToolBox_Con.getPreference_Google_ID(getApplicationContext()).equalsIgnoreCase("")) {

                TGoogle_Env env = new TGoogle_Env();
                env.setApp_code(Constant.PRJ001_CODE);
                env.setApp_version(Constant.PRJ001_VERSION);

                env.setSession_app(
                        ToolBox_Con.getPreference_Session_App(getApplicationContext())
                );
                env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
                env.setGcm_id(
                        ToolBox_Con.getPreference_Google_ID(getApplicationContext())
                );

                Gson gson = new GsonBuilder().serializeNulls().create();

                String sResults = ToolBox_Con.connWebService(
                        Constant.WS_GOOGLE,
                        gson.toJson(env)
                );

                TGoogle_Rec rec = gson.fromJson(
                        sResults,
                        TGoogle_Rec.class
                );

                if (rec.getRet().equalsIgnoreCase("OK")) {
                    ToolBox_Con.setPreference_Google_ID_OK(
                            getApplicationContext(),
                            "OK"
                    );

                } else {
                    programAlarm(getApplicationContext());
                }

            } else {
                programAlarm(getApplicationContext());
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);

            programAlarm(getApplicationContext());
        }
    }

    private void programAlarm(Context context) {
        Calendar calendarAux = Calendar.getInstance();
        //
        calendarAux.set(
                Calendar.MINUTE,
                calendarAux.get(Calendar.MINUTE) + 5
        );
        //
        Intent mIntent = new Intent(
                context,
                WS_Google.class
        );
        //
        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                30,
                mIntent,
                0
        );
        //
        AlarmManager am = (AlarmManager)
                context.getSystemService(ALARM_SERVICE);
        //
        am.set(
                AlarmManager.RTC_WAKEUP,
                calendarAux.getTimeInMillis(),
                pi
        );
    }

}
