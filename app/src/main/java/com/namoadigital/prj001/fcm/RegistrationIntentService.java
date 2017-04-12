package com.namoadigital.prj001.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

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

            String sToken = FirebaseInstanceId.getInstance().getToken();

            Log.d("ID", sToken);


        } catch (Exception e) {

            Log.d("ID", "ID Error: " + e.toString());
        }


    }
}
