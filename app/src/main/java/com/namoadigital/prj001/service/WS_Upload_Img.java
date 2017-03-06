package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
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
import java.util.List;

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
            //Chama notificação.
            showNotification();

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
                env.setFile_path(geFile.getFile_path());
                //
                String sResults = ToolBox_Inf.uploadFile(
                        gson.toJson(env),
                        geFile.getFile_path()
                );

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
            String results = e.toString();
        } finally {
            WBR_Upload_Img.completeWakefulIntent(intent);
            cancelNotification();
        }
    }

    private void showNotification() {

        HMAux hmAux_Trans = loadTranslation();

        final NotificationCompat.Builder  mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.upload_animation)
                        .setContentTitle(hmAux_Trans.get("notification_ttl_upload"))
                        .setContentText(hmAux_Trans.get("notification_msg_upload"))
                        .setTicker("");

        mBuilder.setAutoCancel(true);

        final NotificationManager mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyManager.notify(Constant.NOTIFICATION_UPLOAD, mBuilder.build());

    }

    private HMAux loadTranslation() {
        HMAux hmAux_Trans = new HMAux();

        List<String> translist = new ArrayList<>();

        //Parametros abaixo estão zeradas,
        //pois só precisa das traduções de sys
      return hmAux_Trans = ToolBox_Inf.setLanguage(
                getApplicationContext(),
                "",
                "0",
                ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
                translist);

    }

    private void cancelNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(Constant.NOTIFICATION_UPLOAD);

    }


}
