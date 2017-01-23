package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_Sync extends IntentService {

    private EV_UserDao userDao;
    private EV_User_CustomerDao ev_user_customerDao;

    private StringBuilder sResult;


    public WS_Sync() {
        super("WS_Sync");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String session_app = bundle.getString(Constant.GS_SESSION_APP);
            ArrayList<String> data_package = bundle.getStringArrayList(Constant.GS_DATA_PACKAGE);
            int jumpValidation = bundle.getInt(Constant.GC_STATUS_JUMP);
            int jumpOD = bundle.getInt(Constant.GC_STATUS);
            sResult = new StringBuilder();

            processWS_Sync(session_app,data_package,jumpValidation,jumpOD);

        }catch (Exception e) {

        String results = "ERROR: ";

        if (e.toString().contains("JsonSyntaxException")) {
            results += "JsonParse - " + sResult.toString();
            sb.append(results);

        } else if(e.toString().contains("ORA-")) {
            results += "Oracle - " + sResult.toString();
            sb.append(results);

        }else{
            sb.append(results)
                    .append(e.toString());
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Sync.completeWakefulIntent(intent);
        }

    }

    private void processWS_Sync(String session_app, ArrayList<String> data_package, int jumpValidation, int jumpOD) {
        EV_Module_ResDao moduleResDao = new EV_Module_ResDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_TxtDao moduleResTxtDao =  new EV_Module_Res_TxtDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_Txt_TransDao moduleResTxtTransDao = new EV_Module_Res_Txt_TransDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_SiteDao siteDao = new MD_SiteDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_OperationDao operationDao = new MD_OperationDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_ProductDao productDao = new MD_ProductDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);

        Gson gson = new Gson();




    }

}
