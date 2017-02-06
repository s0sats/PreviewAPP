package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.model.TSerial_Env;
import com.namoadigital.prj001.model.TSerial_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 03/02/2017.
 */

public class WS_Serial extends IntentService {

    private StringBuilder sResult;

    public WS_Serial() {
        super("WS_Serial");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            Long product_code = bundle.getLong(Constant.GS_SERIAL_PRODUCT_CODE);
            String serial_id = bundle.getString(Constant.GS_SERIAL_ID);
            int jumpValidation = bundle.getInt(Constant.GC_STATUS_JUMP);
            int jumpOD = bundle.getInt(Constant.GC_STATUS);
            sResult = new StringBuilder();

            processWS_Serial(product_code,serial_id,jumpValidation,jumpOD);

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

            WBR_Serial.completeWakefulIntent(intent);
        }

    }

    private void processWS_Serial(Long product_code, String serial_id, int jumpValidation, int jumpOD) {

        Gson gson = new GsonBuilder().serializeNulls().create();

        TSerial_Env env =  new TSerial_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setProduct_code(product_code);
        env.setSerial_id(serial_id);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Checking Serial Number ...", "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SERIAL,
                gson.toJson(env)
        );

        TSerial_Rec rec = gson.fromJson(
                resultado,
                TSerial_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                jumpValidation,
                jumpOD
        )
                ) {
            return;
        }

        checkSerialReturn(rec.getSerial(),rec.getError_msg());

    }

    private boolean checkSerialReturn(String serial, String error_msg) {

        switch (serial){
            case "OK":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "SERIAL_OK", error_msg, "", "0");
            return true;

            case "NOT_EXISTS":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "SERIAL_NOT_EXISTS", error_msg, "", "0");
                return true;

            case "ERROR_SERIAL_NULL":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", "ERROR_SERIAL_NULL", "", "0");
                return false;
            default:
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", "Internal Error: Serial property is null", "", "0");
                return false;
        }
    }

}
