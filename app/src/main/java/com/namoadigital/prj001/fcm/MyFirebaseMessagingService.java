package com.namoadigital.prj001.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.namoadigital.prj001.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("msg", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            StringBuilder sb = new StringBuilder();

            sb.append("Resposta\n");
            sb.append("\ncustomer: ");
            sb.append(remoteMessage.getData().get("customer"));
            sb.append("\ntype: ");
            sb.append(remoteMessage.getData().get("type"));
            sb.append("\ntitle: ");
            sb.append(remoteMessage.getData().get("title"));
            sb.append("\nmsg_short: ");
            sb.append(remoteMessage.getData().get("msg_short"));
            sb.append("\nmsg_long: ");
            sb.append(remoteMessage.getData().get("msg_long"));
            sb.append("\nmodule: ");
            sb.append(remoteMessage.getData().get("module"));
            sb.append("\nsender: ");
            sb.append(remoteMessage.getData().get("sender"));
            sb.append("\nsync: ");
            sb.append(remoteMessage.getData().get("sync"));

            Log.d("msg", "Message data payload: " + sb.toString());

            makeNF(
                    getApplicationContext(),
                    remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("type")
            );
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("msg", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private void makeNF(Context context, String title, String message) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_namoa);
        builder.setAutoCancel(true);
        builder.setContentTitle("NAMOA / PRJ0001 " + title);
        builder.setContentText(message);
        //
        builder.setDefaults(
                Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE);
        //
        // Devolve a versao do Android instalado no equipamento
        int versao = Build.VERSION.SDK_INT;
        //
        if (versao >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            nm.notify(10, builder.build());
        } else {
            nm.notify(10, builder.getNotification());
        }
    }
}
