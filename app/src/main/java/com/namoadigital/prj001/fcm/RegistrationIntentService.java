package com.namoadigital.prj001.fcm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.Calendar;

/**
 * Created by neomatrix on 29/03/17.
 */

public class RegistrationIntentService extends IntentService {


    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        try {

            if (!ToolBox_Con.getPreference_Google_ID_OK(getApplicationContext()).equalsIgnoreCase("OK")){
                String sToken = FirebaseInstanceId.getInstance().getToken();

                ToolBox_Con.setPreference_Google_ID(
                        getApplicationContext(),
                        sToken);

                Log.d("ID_GOOGLE", sToken);

                Intent mIntent = new Intent(getApplicationContext(), WS_Google.class);
                startService(mIntent);
            }

        } catch (Exception e) {

            programAlarm(getApplicationContext());

            Log.d("ID_GOOGLE", "ID Error: " + e.toString());
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
                RegistrationIntentService.class
        );
        //
        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                20,
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
