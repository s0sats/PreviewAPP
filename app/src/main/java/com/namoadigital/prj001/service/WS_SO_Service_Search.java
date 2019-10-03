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
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.TSO_Service_Search_Env;
import com.namoadigital.prj001.model.TSO_Service_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Service_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Service_Search extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Service_Search";
    private Gson gson;

    public WS_SO_Service_Search() {
        super("WS_SO_Service_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            int contract_code = bundle.getInt(SM_SODao.CONTRACT_CODE,0);
            int product_code = bundle.getInt(SM_SODao.PRODUCT_CODE,0);
            int serial_code = bundle.getInt(SM_SODao.SERIAL_CODE,0);
            int category_price_code = bundle.getInt(SM_SODao.CATEGORY_PRICE_CODE,0);
            int segment_code = bundle.getInt(SM_SODao.SEGMENT_CODE,0);
            int site_code = bundle.getInt(SM_SODao.SITE_CODE,0);
            int operation_code = bundle.getInt(SM_SODao.OPERATION_CODE,0);
            //
            processSOSearchList(
                    contract_code,
                    product_code,
                    serial_code,
                    category_price_code,
                    segment_code,
                    site_code,
                    operation_code
            );

        }catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Service_Search.completeWakefulIntent(intent);
        }

    }

    private void processSOSearchList(int contract_code, int product_code, int serial_code, int category_price_code, int segment_code, int site_code, int operation_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        gson = new GsonBuilder().serializeNulls().create();
        //
        TSO_Service_Search_Env env = new TSO_Service_Search_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setContract_code(contract_code);
        env.setProduct_code(product_code);
        env.setSerial_code(serial_code);
        env.setCategory_price_code(category_price_code);
        env.setSegment_code(segment_code);
        env.setSite_code(site_code);
        env.setOperation_code(operation_code);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_searching_services"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SERVICE_SEARCH,
                gson.toJson(env)
        );
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_services"), "", "0");
        //
        TSO_Service_Search_Rec rec = gson.fromJson(
                resultado,
                TSO_Service_Search_Rec.class
        );
        String tst = "";
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
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_list"), "", "0");
        //
        processSOServiceSearchReturn(rec);
    }
    //
    private void processSOServiceSearchReturn(TSO_Service_Search_Rec rec) {
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_end_proccess"), new HMAux(), gson.toJson(rec.getData()), "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_processing_list");
        translist.add("msg_searching_services");
        translist.add("msg_receiving_services");
        translist.add("msg_end_proccess");


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
