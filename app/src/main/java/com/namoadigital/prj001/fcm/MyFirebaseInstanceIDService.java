package com.namoadigital.prj001.fcm;

/**
 * Created by neomatrix on 29/03/17.
 */

public class MyFirebaseInstanceIDService{}
/*public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


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
 */
