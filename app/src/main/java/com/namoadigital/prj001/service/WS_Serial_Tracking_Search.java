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
import com.namoadigital.prj001.model.TSerial_Tracking_Search_Env;
import com.namoadigital.prj001.model.TSerial_Tracking_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 06/09/2017.
 */

public class WS_Serial_Tracking_Search extends IntentService {
    public static final String TRACKING_RESULT_KEY = "TRACKING_RESULT_KEY";
    public static final String NOT_EXISTS = "NOT_EXISTS";
    public static final String EXISTS = "EXISTS";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_serial_tracking_search";

    public WS_Serial_Tracking_Search() {
        super("WS_Serial_Tracking_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String product_code = bundle.getString(Constant.WS_SERIAL_TRACKING_SEARCH_PRODUCT_CODE);
            String serial_code = bundle.getString(Constant.WS_SERIAL_TRACKING_SEARCH_SERIAL_CODE);
            String tracking = bundle.getString(Constant.WS_SERIAL_TRACKING_SEARCH_TRACKING);
            String site_code = bundle.getString(Constant.WS_SERIAL_TRACKING_SEARCH_SITE_CODE);

            processWSTrackingSearch(product_code, serial_code, tracking, site_code);

        }catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Serial_Tracking_Search.completeWakefulIntent(intent);
        }

        }

    private void processWSTrackingSearch(String product_code, String serial_code, String tracking, String site_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        TSerial_Tracking_Search_Env env = new TSerial_Tracking_Search_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setSite_code(site_code);
        env.setProduct_code(product_code);
        env.setSerial_code(serial_code);
        env.setTracking(tracking);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS",hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SERIAL_TRACKING_SEARCH,
                gson.toJson(env)
        );
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS",hmAux_Trans.get("msg_receiving_data"), "", "0");
        //
        TSerial_Tracking_Search_Rec rec = gson.fromJson(
                resultado,
                TSerial_Tracking_Search_Rec.class
        );
        //
        if (
                !ToolBox_Inf.processWSCheckValidation(
                        getApplicationContext(),
                        rec.getValidation(),
                        rec.getError_msg(),
                        rec.getLink_url(),
                        1,
                        1)
                        ||
                        !ToolBox_Inf.processoOthersError(
                                getApplicationContext(),
                                getResources().getString(R.string.generic_error_lbl),
                                rec.getError_msg())
                ) {
            return;
        }
        //
        processTrackingReturn(rec);

    }

    private void processTrackingReturn(TSerial_Tracking_Search_Rec rec) {
        if(rec.getTracking_ret() == 0){
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", rec.getTracking_msg(), "", "0");
        }else{
            HMAux hmAuxRet = new HMAux();
            hmAuxRet.put(TRACKING_RESULT_KEY,rec.getTracking_msg());
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");
        }
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_save_ok");

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
