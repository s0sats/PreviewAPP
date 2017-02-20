package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;
import com.namoa_digital.namoa_library.util.ToolBox;
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
            // Hugo
            // Hugo

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
            //GE_File aux = new GE_File();
            //aux.setFile_code(1L);
            //aux.setFile_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            //aux.setFile_status("OPENED");
            //aux.setFile_path("PICTURE_1_31_1_1_13.jpg");
            //
            //geFileDao.addUpdate(aux);
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
        }
    }
}
