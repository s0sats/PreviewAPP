package com.namoadigital.prj001.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.FCMMessage;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message;
import com.namoadigital.prj001.sql.FCMMessage_Sql_002;
import com.namoadigital.prj001.sql.FCMMessage_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Sql_018;
import com.namoadigital.prj001.ui.act018.Act018_Main;
import com.namoadigital.prj001.ui.act019.Act019_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

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
            fcmMessage.setStatus("0");
            String sDate = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");
            fcmMessage.setDate_create(sDate);
            fcmMessage.setDate_create_ms(ToolBox.dateToMilliseconds(sDate));
            //
            if (fcmMessage.getModule().trim().equalsIgnoreCase(Constant.CHAT_NOTIFICATION_TYPE_CHAT)) {
                if (ToolBox_Inf.isUsrAppLogged(getApplicationContext())) {
                    if(fcmMessage.getTitle().equals("<CHAT_MSG>")){
                        String param = ToolBox_Inf.getWebSocketJsonParam(fcmMessage.getMsg_long().trim());
                        //
                        Intent cMessageIntent = new Intent(getApplicationContext(), WBR_C_Message.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                        bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_C_MESSAGE_FCM);
                        cMessageIntent.putExtras(bundle);
                        getApplicationContext().sendBroadcast(cMessageIntent);
                    }else{
                        ToolBox_Inf.showChatNotification(
                                getApplicationContext(),
                                fcmMessage.getModule().toUpperCase(),
                                "0",
                                "",//fcmMessage.getTitle().trim(),
                                ""//fcmMessage.getMsg_short().trim()
                        );
                        //
                    }

                }
                return;
            }
            //
            if (fcmMessage.getTitle().trim().equalsIgnoreCase("<SM_SO_UPDATE>") &&
                    fcmMessage.getModule().trim().equalsIgnoreCase("SM_")) {

                checkNService_SO_Status(fcmMessage);

            } else {
                //
                fcmMessageDao.addUpdate(fcmMessage);
                long fcmmessage_code = Long.parseLong(
                        fcmMessageDao.getByStringHM(
                                new FCMMessage_Sql_002().toSqlQuery()
                        ).get(FCMMessage_Sql_002.FCMMESSAGE_CODE)
                );

                int fcmmessage_qty = Integer.parseInt(
                        fcmMessageDao.getByStringHM(
                                new FCMMessage_Sql_003().toSqlQuery()
                        ).get(FCMMessage_Sql_003.BADGE_MESSAGES_QTY)
                );

                Log.d("msg", "Message data payload: " + sb.toString());

                makeNF(
                        getApplicationContext(),
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("msg_short"),
                        fcmmessage_code,
                        fcmmessage_qty
                );

                if (fcmMessage.getSync().equalsIgnoreCase("1")) {
                    ToolBox_Con.setPreference_SYNC_REQUIRED(getApplicationContext(), "1");
                    ToolBox_Inf.call_Notification_Sync(getApplicationContext(), 11);
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("msg", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void checkNService_SO_Status(FCMMessage fcmMessage) {

        // Update So
        try {

            SM_SODao sm_soDao = new SM_SODao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            JSONObject jsonObjectRoot = new JSONObject(fcmMessage.getMsg_long());
            JSONObject jsonObject = jsonObjectRoot.getJSONObject("so_update");

            String customer_code = fcmMessage.getCustomer();
            String so_prefix = jsonObject.getString("so_prefix");
            String so_code = jsonObject.getString("so_code");
            String so_scn = jsonObject.getString("so_scn");
            String status = jsonObject.getString("status");

            // Update S.O.
            sm_soDao.addUpdate(
                    new SM_SO_Sql_018(
                            Long.parseLong(customer_code),
                            Integer.parseInt(so_prefix),
                            Integer.parseInt(so_code),
                            Integer.parseInt(so_scn)
                    ).toSqlQuery()
            );


        } catch (JSONException e) {

            String error_r = e.toString();

        }

        sendFCMStatus();
    }

    private void sendFCMStatus() {
        Intent mIntent = new Intent();
        mIntent.setAction(Constant.WS_FCM);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mIntent);
    }


    private void makeNF(Context context, String title, String message, long fcmmessage_code, int fcmmessage_qty) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        Intent mIntent = null;

        if (fcmmessage_qty > 1) {
            Bundle bundle = new Bundle();
            bundle.putString("action", "NOTIFICATION");
            mIntent = new Intent(getBaseContext(), Act018_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("action", "NOTIFICATION");
            bundle.putLong("fcmmessage_code", fcmmessage_code);
            mIntent = new Intent(getBaseContext(), Act019_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundle);
        }
        //
        PendingIntent pi = PendingIntent.getActivity(this, 0, mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        //
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_namoa);
        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        builder.setContentIntent(pi);
        if (fcmmessage_qty > 1) {
            builder.setContentText("(" + String.valueOf(fcmmessage_qty) + ") " + context.getResources().getString(R.string.message_received_notification_sync));
        } else {
            builder.setContentText(message);
        }
        builder.setOngoing(true);
        //
        long dt_last = ToolBox_Con.getPreference_Google_ID_DT(getApplicationContext());
        Calendar cal_now = Calendar.getInstance();
        long dt_now = cal_now.getTimeInMillis();
        //
        if ((dt_last == 0) || ((dt_now - dt_last) > (120 * 1000))) {
            builder.setDefaults(
                    Notification.DEFAULT_SOUND |
                            Notification.DEFAULT_VIBRATE);
        } else {
        }
        //
        ToolBox_Con.setPreference_Google_ID_DT(getApplicationContext(), dt_now);
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
