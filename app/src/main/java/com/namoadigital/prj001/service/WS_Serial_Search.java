package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.model.TSerial_Search_Env;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 22/05/2017.
 */

public class WS_Serial_Search extends IntentService {

    public WS_Serial_Search() {
        super("WS_Serial_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String product_code = bundle.getString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE);
            String serial_code = bundle.getString(Constant.WS_SERIAL_SEARCH_SERIAL_CODE);
            String serial_id = bundle.getString(Constant.WS_SERIAL_SEARCH_SERIAL_ID);

            processWSSerialSearch(product_code, serial_code ,serial_id );

        }catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Serial_Search.completeWakefulIntent(intent);
        }

    }

    private void processWSSerialSearch(String product_code, String serial_code, String serial_id) {

        //Seleciona traduções
        //loadTranslation();

        Gson gson = new GsonBuilder().serializeNulls().create();

        TSerial_Search_Env env  =  new TSerial_Search_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setProduct_code(product_code);
        env.setSerial_code(serial_code);
        env.setSerial_id(serial_id);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS",""/* hmAux_Trans.get("msg_checking_serial")*/, "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SERIAL_SEARCH,
                gson.toJson(env)
        );


        TSerial_Search_Rec rec = gson.fromJson(
                resultado,
                TSerial_Search_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                1,
                1
                )
        ) {
            return;
        }


        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT","Processando lista", resultado , "0");

    }
}
