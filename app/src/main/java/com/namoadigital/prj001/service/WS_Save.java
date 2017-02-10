package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 10/02/2017.
 */

public class WS_Save extends IntentService {

    private StringBuilder sResult;

    public WS_Save() {
        super("WS_Save");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String user = bundle.getString(Constant.GC_USER_CODE);
            String password = bundle.getString(Constant.GC_PWD);
            String nfc = bundle.getString(Constant.GC_NFC);
            int statusjump = bundle.getInt(Constant.GC_STATUS_JUMP);

            sResult = new StringBuilder();

            processWS_Save(user, password, nfc, statusjump);

        } catch (Exception e) {

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

            WBR_Save.completeWakefulIntent(intent);

        }

    }

    private void processWS_Save(String user, String password, String nfc, int statusjump) {

    }
}
