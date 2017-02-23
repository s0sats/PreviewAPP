package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_007;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_001;
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

public class WS_Cleanning extends IntentService {

    public WS_Cleanning() {
        super("WS_Cleanning");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            deleteFormLocal();

        } catch (Exception e) {
            String results = e.toString();
        } finally {
        }
    }

    private void deleteFormLocal() {

        GE_Custom_Form_LocalDao formLocalDao =
                new GE_Custom_Form_LocalDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );

        GE_Custom_Form_Field_LocalDao formFieldLocalDao =
                new GE_Custom_Form_Field_LocalDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );

        GE_Custom_Form_DataDao formDataDao =
                new GE_Custom_Form_DataDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        GE_Custom_Form_Data_FieldDao formDataFieldDao =
                new GE_Custom_Form_Data_FieldDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );

        ArrayList<HMAux> hmAuxs = (ArrayList<HMAux>) formDataDao.query_HM(
                new WS_Cleaning_Sql_001(
                        sDTFormat_5_Days("yyyy-MM-dd HH:mm:ss Z")
                ).toSqlQuery()
        );

        for (HMAux hmAux : hmAuxs) {

            formLocalDao.remove(
                    new GE_Custom_Form_Local_Sql_007(
                            hmAux.get("customer_code"),
                            hmAux.get("custom_form_type"),
                            hmAux.get("custom_form_code"),
                            hmAux.get("custom_form_version"),
                            hmAux.get("custom_form_data")
                    ).toSqlQuery()
            );
            //
            formFieldLocalDao.remove(
                    new GE_Custom_Form_Field_Local_Sql_004(
                            hmAux.get("customer_code"),
                            hmAux.get("custom_form_type"),
                            hmAux.get("custom_form_code"),
                            hmAux.get("custom_form_version"),
                            hmAux.get("custom_form_data")
                    ).toSqlQuery()
            );
            //
            formDataDao.remove(
                    new GE_Custom_Form_Data_Sql_002(
                            hmAux.get("customer_code"),
                            hmAux.get("custom_form_type"),
                            hmAux.get("custom_form_code"),
                            hmAux.get("custom_form_version"),
                            hmAux.get("custom_form_data")
                    ).toSqlQuery()
            );
            //
            formDataFieldDao.remove(
                    new GE_Custom_Form_Data_Field_Sql_002(
                            hmAux.get("customer_code"),
                            hmAux.get("custom_form_type"),
                            hmAux.get("custom_form_code"),
                            hmAux.get("custom_form_version"),
                            hmAux.get("custom_form_data")
                    ).toSqlQuery()
            );
        }
    }

    public String sDTFormat_5_Days(String sDTFormatS) {
        String sResults = "";
        Calendar ca1 = Calendar.getInstance();
        ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) + 10);

        SimpleDateFormat sdf = new SimpleDateFormat(sDTFormatS) {
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
                StringBuffer toFix = super.format(date, toAppendTo, pos);
                return toFix.insert(toFix.length() - 2, ':');
            }
        };

        try {
            sResults = sdf.format(ca1.getTime());
        } catch (Exception var5) {
            sResults = "1900-01-01 00:00:00";
        }

        return sResults;
    }

}
