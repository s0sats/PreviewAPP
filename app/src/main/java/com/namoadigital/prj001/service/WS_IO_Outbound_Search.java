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
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.T_IO_Inbound_Search_Env;
import com.namoadigital.prj001.model.T_IO_Outbound_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Outbound_Search extends IntentService {

    public static final String KEY_CODE_ID = "KEY_CODE_ID";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_outbound_search";
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public WS_IO_Outbound_Search() {
        super("WS_IO_Outbound_Search");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String site_code = bundle.getString(MD_Site_Zone_LocalDao.SITE_CODE,"-1");
            String zone_code = bundle.getString(MD_Site_Zone_LocalDao.ZONE_CODE,"");
            String local_code = bundle.getString(MD_Site_Zone_LocalDao.LOCAL_CODE,"");
            String code_id = bundle.getString(KEY_CODE_ID,"");
            String invoice = bundle.getString(IO_InboundDao.INVOICE_NUMBER,"");
            //
            processWsOutboundSearch(site_code,zone_code,local_code,code_id,invoice);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Outbound_Search.completeWakefulIntent(intent);
        }
    }

    private void processWsOutboundSearch(String site_code, String zone_code, String local_code, String code_id, String invoice) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        T_IO_Inbound_Search_Env env = new T_IO_Inbound_Search_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSite_code(site_code);
        env.setZone_code(zone_code);
        env.setLocal_code(local_code);
        env.setCode_id(code_id);
        env.setInvoice(invoice);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_IO_OUTBOUND_SEARCH,
                gson.toJson(env)
        );
        //
        T_IO_Outbound_Search_Rec rec = gson.fromJson(
                resultado,
                T_IO_Outbound_Search_Rec.class
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
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_processing_list"), resultado, "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_processing_list");

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
