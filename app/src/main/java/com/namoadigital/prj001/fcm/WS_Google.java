package com.namoadigital.prj001.fcm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.model.TGoogle_Env;
import com.namoadigital.prj001.model.TGoogle_Rec;
import com.namoadigital.prj001.model.TSync_Rec;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_007;
import com.namoadigital.prj001.sql.GE_File_Sql_005;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_001;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_Google extends IntentService {

    public WS_Google() {
        super("WS_Cleanning");
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
