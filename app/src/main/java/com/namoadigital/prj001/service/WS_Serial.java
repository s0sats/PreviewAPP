package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.TSerial_Env;
import com.namoadigital.prj001.model.TSerial_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 03/02/2017.
 */

public class WS_Serial extends IntentService {

    private StringBuilder sResult;
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_Serial";

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
            int serial_required = bundle.getInt(Constant.GS_SERIAL_REQUIRED);
            int serial_allow_new = bundle.getInt(Constant.GS_SERIAL_ALLOW_NEW);
            int jumpValidation = bundle.getInt(Constant.GC_STATUS_JUMP);
            int jumpOD = bundle.getInt(Constant.GC_STATUS);
            sResult = new StringBuilder();

            processWS_Serial(product_code, serial_id, serial_required, serial_allow_new, jumpValidation,jumpOD);

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

    private void processWS_Serial(Long product_code, String serial_id, int serial_required, int serial_allow_new, int jumpValidation, int jumpOD) {
        //Seleciona traduções

        loadTranslation();

        Gson gson = new GsonBuilder().serializeNulls().create();

        TSerial_Env env =  new TSerial_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setProduct_code(product_code);
        env.setSerial_id(serial_id);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_checking_serial"), "", "0");

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

        checkSerialReturn(rec.getSerial(),rec.getError_msg(), serial_required, serial_allow_new);

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_checking_serial");
        translist.add("msg_serial_ok");
        translist.add("msg_new_serial_not_allow");
        translist.add("msg_create_new_serial");
        translist.add("msg_error_serial_null");

        mResource_Code = ToolBox_Inf.getResourceCode(
                getApplicationContext(),
                mModule_Code,
                mResource_Name
        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
                getApplicationContext(),
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
                translist);

    }

    private boolean checkSerialReturn(String serial, String error_msg, int serial_required, int serial_allow_new) {

        switch (serial){
            case "OK":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "SERIAL_OK", hmAux_Trans.get("msg_serial_ok"), "", "0");
                return true;

            case "NOT_EXISTS":
                //Serial não existe
                //Se produto não permite novo serial , dispara msg de erro.
                if (serial_allow_new == 0){
                    ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_new_serial_not_allow"), "", "0");

                }else{
                    //Se produto não permite novo serial
                    //pergunta para o USR o que fazer.
                    ToolBox_Inf.sendBCStatus(getApplicationContext(), "SERIAL_NOT_EXISTS", hmAux_Trans.get("msg_create_new_serial"), "", "0");
                }
                return true;

            case "ERROR_SERIAL_NULL":default:
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_serial_null"), "", "0");
                return false;
        }
    }
}
