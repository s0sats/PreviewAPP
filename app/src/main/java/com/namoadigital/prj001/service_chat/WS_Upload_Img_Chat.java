package com.namoadigital.prj001.service_chat;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.CH_FileDao;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_File;
import com.namoadigital.prj001.model.TUploadImg_Chat_Env;
import com.namoadigital.prj001.model.TUploadImg_Chat_Rec;
import com.namoadigital.prj001.receiver_chat.WBR_Upload_Img_Chat;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_File_Sql_001;
import com.namoadigital.prj001.sql.CH_Message_Sql_024;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by neomatrix on 20/01/17.
 */

public class WS_Upload_Img_Chat extends IntentService {

    public WS_Upload_Img_Chat() {
        super("WS_Upload_Img_Chat");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Gson gson = new Gson();
            TUploadImg_Chat_Env env = new TUploadImg_Chat_Env();
            //
            Bundle bundle = intent.getExtras();
            //
            ArrayList<CH_File> chFiles;
            //
            CH_FileDao chFileDao = new CH_FileDao(
                    getApplicationContext()
            );
            //
            CH_MessageDao chMessageDao = new CH_MessageDao(
                    getApplicationContext()
            );
            //
            chFiles = (ArrayList<CH_File>) chFileDao.query(
                    new CH_File_Sql_001().toSqlQuery()
            );
            //Se lista vazia, não verifica necessidade de notificação de upload
            if(chFiles.size() == 0){
                return;
            }
            //
            if (!ToolBox_Inf.isUploadRunning()) {
                //WBR_Upload_Img.IS_RUNNING = true;
                //Chama notificação.
                ToolBox_Inf.showNotification(
                        getApplicationContext(),
                        Constant.NOTIFICATION_UPLOAD
                );
            }
            //
            WBR_Upload_Img_Chat.IS_RUNNING = true;
            //
            env.setApp_code(Constant.PRJ001_CODE);
            //
            for (CH_File chFile : chFiles) {
                String val = chFile.getFile_path().replace(".", "_");
                //String[] vals = chFile.getFile_path().split("_");
                String[] vals = val.split("_");
                //
                env.setMsg_prefix(Long.parseLong(vals[0]));
                env.setMsg_code(Long.parseLong(vals[1]));
                env.setSocket_id(SingletonWebSocket.getmSocket_ID());
                //
                String sResults = ToolBox_Inf.uploadFileChat(
                        gson.toJson(env),
                        chFile.getFile_path(),
                        chFile.getFile_path_new()
                );

                TUploadImg_Chat_Rec rec = gson.fromJson(
                        sResults,
                        TUploadImg_Chat_Rec.class
                );

                if (rec.getResult().toUpperCase().contains(("OK"))) {
                    chFile.setFile_status("SENT");
                    chFileDao.addUpdate(chFile);
                    //
                    String pk[] = chFile.getFile_code().replace(".", "#").split("#");

                    chMessageDao.addUpdate(
                            new CH_Message_Sql_024(
                                    Integer.parseInt(pk[0]),
                                    Long.parseLong(pk[1])
                            ).toSqlQuery()
                    );
                }

                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_IMAGE_ME);

                Log.d("CHFILE", rec.getResult());
            }

            //ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_IMAGE_ME);

        } catch (Exception e) {

            //ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_IMAGE_ME);

            programAlarm(getApplicationContext());
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext());
            WBR_Upload_Img_Chat.IS_RUNNING = false;
            WBR_Upload_Img_Chat.completeWakefulIntent(intent);
            //
            if (!ToolBox_Inf.isUploadRunning()) {
                ToolBox_Inf.cancelNotification(getApplicationContext(), Constant.NOTIFICATION_UPLOAD);
            }
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
                WBR_Upload_Img_Chat.class
        );
        //
        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                10,
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
