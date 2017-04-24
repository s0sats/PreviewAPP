package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_007;
import com.namoadigital.prj001.sql.GE_File_Sql_005;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_001;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_AL_Full extends IntentService {

    public WS_AL_Full() {
        super("WS_AL_Full");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            ToolBox_Inf.generateNotification(getApplicationContext(), 100);

        } catch (Exception e) {
            String results = e.toString();
        } finally {
        }
    }


}
