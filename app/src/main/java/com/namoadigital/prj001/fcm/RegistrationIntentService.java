package com.namoadigital.prj001.fcm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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

            String tt = ToolBox_Con.getPreference_Google_ID_OK(getApplicationContext());
            /**
             * LUCHE - 16/09/2020
             * Solicitaram que a chamada do getInstanceId fosse feito sempre e a comunicação com o
             * WS_Google aconteca apenas se o id retornado foi diferente da preferencia
             */
            //if (!ToolBox_Con.getPreference_Google_ID_OK(getApplicationContext()).equalsIgnoreCase("OK")){
                //String sToken = FirebaseInstanceId.getInstance().getToken();
                /**
                 * LUCHE - 16/04/2020
                 * Nova metodologia para resgatar o token, ja que FirebaseInstanceId.getInstance().getToken(), foi depreciada
                 */
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    ToolBox_Inf.registerException(getClass().getName(),task.getException());
                                    Log.d("ID_GOOGLE", "getInstanceId failed", task.getException());
                                    return;
                                }
                                String sToken = task.getResult().getToken();
                                //LUCHE - 16/09/2020
                                //Com a nova logica de sempre chamar o getInstanceId() na act005, a
                                //atualização da preferencia e chama da do Ws deve ser feita apenas
                                //se o token retornado for diferente da preferencia, indicando que
                                //houve a troca.
                                if(!ToolBox_Con.getPreference_Google_ID(getApplicationContext()).equalsIgnoreCase(sToken)) {
                                    //
                                    ToolBox_Con.setPreference_Google_ID(
                                        getApplicationContext(),
                                        sToken);
                                    //Log.d("ID_GOOGLE", sToken);
                                    Intent mIntent = new Intent(getApplicationContext(), WS_Google.class);
                                    startService(mIntent);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                ToolBox_Inf.registerException(getClass().getName(),e);
                            }
                        })
                        .addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                ToolBox_Inf.registerException(getClass().getName(), new Exception("addOnCanceledListener"));
                            }
                        });
            //}
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);

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
