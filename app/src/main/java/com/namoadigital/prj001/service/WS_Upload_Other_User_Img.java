package com.namoadigital.prj001.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import com.namoadigital.prj001.model.TUploadImg_Env;
import com.namoadigital.prj001.model.TUploadImg_Rec;
import com.namoadigital.prj001.receiver.WBR_Upload_Other_User_Img;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.Calendar;

/**
 * Created by d.luche 10/05/019.
 */

public class WS_Upload_Other_User_Img extends IntentService {


    public WS_Upload_Other_User_Img() {
        super("WS_Upload_Other_User_Img");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Gson gson = new Gson();
            TUploadImg_Env env = new TUploadImg_Env();
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            //
            File[] unsentImgs = new File(ConstantBaseApp.UNSENT_IMG_PATH).listFiles();
            //SE NENHUM ITEM A ENVIAR, SAI DO SERVIÇO SEM CHAMAR NOTIFICAÇÃO
            if (unsentImgs.length == 0) {
                return;
            }
            //Verifica necessida de notificação de upload
            if (!ToolBox_Inf.isUploadRunning()) {
                //WBR_Upload_Img.IS_RUNNING = true;
                //Chama notificação.
                ToolBox_Inf.showNotification(
                        getApplicationContext(),
                        Constant.NOTIFICATION_UPLOAD
                );
            }

            WBR_Upload_Other_User_Img.IS_RUNNING = true;
            //
            for (File file : unsentImgs) {

                env.setFile_path(file.getName());
                //
                String sResults = ToolBox_Inf.uploadFileUnsentImg(
                        gson.toJson(env),
                        ConstantBaseApp.UNSENT_IMG_PATH,
                        file.getName(),
                        null

                );

                TUploadImg_Rec rec = gson.fromJson(
                        sResults,
                        TUploadImg_Rec.class
                );

                if (rec.getSave().equalsIgnoreCase("OK")) {
                    //Não há necessidade de verificar o delete.
                    file.delete();
                }
            }


        } catch (Exception e) {
            programAlarm(getApplicationContext());
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            WBR_Upload_Other_User_Img.IS_RUNNING = false;
            WBR_Upload_Other_User_Img.completeWakefulIntent(intent);
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
                WBR_Upload_Other_User_Img.class
        );
        //
        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                ConstantBaseApp.ALARM_REQUEST_CODE_WS_UPLOAD_UNSENT_IMGS,
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
