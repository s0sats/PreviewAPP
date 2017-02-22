package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_Cleanning extends IntentService {

    public WS_Cleanning() {
        super("WS_Cleanning");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            Toast.makeText(
                    getApplicationContext(),
                    "Rodou o Alarm",
                    Toast.LENGTH_SHORT
            ).show();

            Log.d("ALARM", "EXECUTOU ALARM");


        } catch (Exception e) {
            String results = e.toString();
        } finally {
        }
    }
}
