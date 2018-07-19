package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TSerial_Search_Env;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.util.ToolBox_Con.isHostAvailable;

/**
 * Created by d.luche on 22/05/2017.
 */

public class WS_Serial_Search extends IntentService {


    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_serial_search";
    private Gson gson = new GsonBuilder().serializeNulls().create();


    public WS_Serial_Search() {
        super("WS_Serial_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            if (!isHostAvailable()) {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("ws_exception_server_not_found"), "", "0");
                //
                return;
            }

            String product_code = bundle.getString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE);
            String product_id = bundle.getString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID);
            String serial_id = bundle.getString(Constant.WS_SERIAL_SEARCH_SERIAL_ID);
            String tracking = bundle.getString(Constant.WS_SERIAL_SEARCH_TRACKING);
            int serial_exact = bundle.getInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);

            processWSSerialSearch(product_code, product_id, serial_id, tracking, serial_exact);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Serial_Search.completeWakefulIntent(intent);
        }

    }

    private void processWSSerialSearch(String product_code, String product_id, String serial_id, String tracking, int serial_exact) throws Exception {
        //Seleciona traduções
        loadTranslation();

        TSerial_Search_Env env = new TSerial_Search_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setProduct_code(product_code);
        env.setProduct_id(product_id);
        env.setSerial_id(serial_id);
        env.setSerial_exact(serial_exact);
        env.setTracking(tracking);
        env.setSite_code(ToolBox_Con.getPreference_Site_Code(getApplicationContext()));

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receving_data"), "", "0");

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
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        rec.getError_msg())
                ) {
            return;
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_processing_list"), resultado, "0");

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_processing_list");
        translist.add("msg_receving_data");
        translist.add("msg_no_serial_found");


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
}
