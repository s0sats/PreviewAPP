package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.UserDao;
import com.namoadigital.prj001.receiver.WBR_Login;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_Sync extends IntentService {

    private UserDao userDao;
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

//            String user = bundle.getString(Constant.GC_USER_CODE);
//            String password = bundle.getString(Constant.GC_PWD);
//            String nfc = bundle.getString(Constant.GC_NFC);
//            String status = bundle.getString(Constant.GC_STATUS);
//            String statusjump = bundle.getString(Constant.GC_STATUS_JUMP);
//
//            sResult = new StringBuilder();

//            processWSLO(user, password, nfc, status, statusjump);

        } catch (Exception e) {

            String results = "ERROR: ";

            if (e.toString().contains("JsonSyntaxException")) {
                results += "JsonParse - " + sResult.toString();
                sb.append(results);

            } else {
                sb.append(results)
                        .append(e.toString());
            }

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Sync.completeWakefulIntent(intent);

        }

    }

    private void processWSLO(String user, String password, String nfc, String status, String statusjump) {

        userDao = new UserDao(getApplicationContext());
        ev_user_customerDao = new EV_User_CustomerDao(getApplicationContext());

        Gson gson = new Gson();




    }
}
