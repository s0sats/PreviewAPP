package com.namoadigital.prj001.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.model.FCMMessage;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        FCMMessageDao fcmMessageDao = new FCMMessageDao(
                getApplicationContext(),
                Constant.DB_FULL_BASE,
                Constant.DB_VERSION_BASE
        );

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

            FCMMessage fcmMessage = new FCMMessage();

            fcmMessage.setCustomer(remoteMessage.getData().get("customer"));
            fcmMessage.setType(remoteMessage.getData().get("type"));
            fcmMessage.setTitle(remoteMessage.getData().get("title"));
            fcmMessage.setMsg_short(remoteMessage.getData().get("msg_short"));
            fcmMessage.setMsg_long(remoteMessage.getData().get("msg_long"));
            fcmMessage.setModule(remoteMessage.getData().get("module"));
            fcmMessage.setSender(remoteMessage.getData().get("sender"));
            fcmMessage.setSync(remoteMessage.getData().get("sync"));
            fcmMessage.setStatus("1");
            String sDate = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");
            fcmMessage.setDate_create(sDate);
            fcmMessage.setDate_create_ms(ToolBox.dateToMilliseconds(sDate));
            //
            fcmMessageDao.addUpdate(fcmMessage);

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
