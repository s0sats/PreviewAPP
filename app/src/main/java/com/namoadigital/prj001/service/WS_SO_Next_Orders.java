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
import com.namoadigital.prj001.model.TSO_Next_Orders_Env;
import com.namoadigital.prj001.model.TSO_Next_Orders_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Next_Orders;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Next_Orders extends IntentService {

    public static final String SO_NEXT_SERVICES =  "SO_NEXT_SERVICES";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_so_next_orders";
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public WS_SO_Next_Orders() {
        super("WS_SO_Next_Orders");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            long customer_code = bundle.getLong(Constant.LOGIN_CUSTOMER_CODE);
            String site_code = bundle.getString(Constant.LOGIN_SITE_CODE);
            int zone_code = bundle.getInt(Constant.LOGIN_ZONE_CODE);
            long operation_code = bundle.getInt(Constant.LOGIN_OPERATION_CODE);

            processWSSoNextServices(customer_code, site_code, zone_code, operation_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Next_Orders.completeWakefulIntent(intent);
        }
    }

    private void processWSSoNextServices(long customer_code, String site_code, int zone_code, long operation_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        TSO_Next_Orders_Env env = new TSO_Next_Orders_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setCustomer_code(customer_code);
        env.setSite_code(site_code);
        env.setZone_code(zone_code);
        env.setOperation_code(operation_code);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receving_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_NEXT_ORDERS,
                gson.toJson(env)
        );
        //
        TSO_Next_Orders_Rec rec = gson.fromJson(
                resultado,
                TSO_Next_Orders_Rec.class
        );
        //
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
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_orders"), "", "0");
        //
        HMAux auxName = new HMAux();
        auxName.put(SO_NEXT_SERVICES,gson.toJson(rec.getObj()));
        //
        ToolBox.sendBCStatus(
                getApplicationContext(),
                "CLOSE_ACT",
                hmAux_Trans.get("msg_process_finalized"),
                auxName,
                rec.getLink_url(),
                "0"
        );
        //

    }
    //
    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_sending_data");
        translist.add("msg_receving_data");
        translist.add("msg_processing_orders");
        translist.add("msg_process_finalized");
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                getApplicationContext(),
                mModule_Code,
                mResource_Name
        );
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                getApplicationContext(),
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
                translist);
    }
}
