package com.namoadigital.prj001.fcm;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.namoadigital.prj001.util.ToolBox_Con;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by neomatrix on 29/03/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        ToolBox_Con.setPreference_Google_ID(
                getApplicationContext(),
                refreshedToken);

        ToolBox_Con.setPreference_Google_ID_OK(
                getApplicationContext(),
                ""
        );

        Intent mIntent = new Intent(getApplicationContext(), WS_Google.class);
        startService(mIntent);

        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }
}
