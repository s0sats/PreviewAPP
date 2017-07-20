package com.namoadigital.prj001.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.TUploadImg_Env;
import com.namoadigital.prj001.model.TUploadImg_Rec;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.sql.GE_File_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by neomatrix on 20/01/17.
 */

public class WS_Upload_Img extends IntentService {

    public WS_Upload_Img() {
        super("WS_Upload_Img");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            if (!ToolBox_Inf.isUploadRunning()) {
                WBR_Upload_Img.IS_RUNNING = true;
                //Chama notificação.
                ToolBox_Inf.showNotification(
                        getApplicationContext(),
                        Constant.NOTIFICATION_UPLOAD
                );
            }

            Gson gson = new Gson();
            TUploadImg_Env env = new TUploadImg_Env();
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));

            Bundle bundle = intent.getExtras();
            //
            ArrayList<GE_File> geFiles;
            //
            GE_FileDao geFileDao = new GE_FileDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            geFiles = (ArrayList<GE_File>) geFileDao.query(
                    new GE_File_Sql_001().toSqlQuery()
            );
            //
            for (GE_File geFile : geFiles) {

                env.setFile_path(geFile.getFile_path_new() != null ? geFile.getFile_path_new() : geFile.getFile_path());
                //
                String sResults = ToolBox_Inf.uploadFile(
                        gson.toJson(env),
                        geFile.getFile_path_new() != null ? geFile.getFile_path_new() : geFile.getFile_path()
                );

                // Hugo Erro
                //throw new Exception("TimeOut Exception");

                TUploadImg_Rec rec = gson.fromJson(
                        sResults,
                        TUploadImg_Rec.class
                );

                if (rec.getSave().equalsIgnoreCase("OK")) {
                    geFile.setFile_status("SENT");
                    geFileDao.addUpdate(geFile);
                }
            }


        } catch (Exception e) {
            programAlarm(getApplicationContext());
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
            WBR_Upload_Img.IS_RUNNING = false;
            WBR_Upload_Img.completeWakefulIntent(intent);
            //
            if(!ToolBox_Inf.isUploadRunning()){
                ToolBox_Inf.cancelNotification(getApplicationContext(),Constant.NOTIFICATION_UPLOAD);
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
                WBR_Upload_Img.class
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
