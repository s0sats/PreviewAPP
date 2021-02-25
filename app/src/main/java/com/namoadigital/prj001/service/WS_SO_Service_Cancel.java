package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SO_Save_Return;
import com.namoadigital.prj001.model.TSO_SO_Service_Rec;
import com.namoadigital.prj001.model.TSO_Service_Cancel_Env;
import com.namoadigital.prj001.receiver.WBR_SO_Service_Cancel;
import com.namoadigital.prj001.ui.act043.Act043_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Service_Cancel extends IntentService {
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Service_Search";
    private Gson gson;


    public WS_SO_Service_Cancel() {super("WS_SO_Service_Cancel");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        //
        try {
            String so_prefix = bundle.getString(SM_SO_ServiceDao.SO_PREFIX,"0");
            String so_code = bundle.getString(SM_SO_ServiceDao.SO_CODE,"0");
            String type_ps = bundle.getString(Act043_Main.TYPE_PS,"");
            String price_list_code = bundle.getString(SM_SO_ServiceDao.PRICE_LIST_CODE,"0");
            String pack_code = bundle.getString(SM_SO_ServiceDao.PACK_CODE,"0");
            String pack_seq = bundle.getString(SM_SO_ServiceDao.PACK_SEQ,"0");
            String category_price_code = bundle.getString(SM_SO_ServiceDao.CATEGORY_PRICE_CODE,"0");
            String service_code = bundle.getString(SM_SO_ServiceDao.SERVICE_CODE,"");
            String service_seq = bundle.getString(SM_SO_ServiceDao.SERVICE_SEQ,"");
            String exec_code = bundle.getString(SM_SO_Service_ExecDao.EXEC_CODE,"");
            //
            processSOServiceCancel(
                so_prefix,
                so_code,
                type_ps,
                price_list_code,
                pack_code,
                pack_seq,
                category_price_code,
                service_code,
                service_seq,
                exec_code
            );

        }catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Service_Cancel.completeWakefulIntent(intent);
        }


    }

    private void processSOServiceCancel(String so_prefix, String so_code, String type_ps, String price_list_code, String pack_code, String pack_seq, String category_price_code, String service_code, String service_seq, String exec_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        gson = new GsonBuilder().serializeNulls().create();
        //
        TSO_Service_Cancel_Env env = new TSO_Service_Cancel_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSo_prefix(so_prefix);
        env.setSo_code(so_code);
        env.setType_ps(type_ps);
        env.setPrice_list_code(price_list_code);
        env.setPack_code(pack_code);
        env.setPack_seq(pack_seq);
        env.setCategory_price_code(category_price_code);
        env.setService_code(service_code);
        env.setService_seq(service_seq);
        env.setExec_code(exec_code);
        env.setToken(ToolBox_Inf.getToken(getApplicationContext()));
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_SO_SERVICE_REMOVE,
            gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
        //
        TSO_SO_Service_Rec rec = gson.fromJson(
            resultado,
            TSO_SO_Service_Rec.class
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
        processServiceCancelReturn(rec);
    }

    private void processServiceCancelReturn(TSO_SO_Service_Rec rec) {
        SM_SODao soDao = new SM_SODao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
        HMAux hmAuxRet = new HMAux();
        //
        if (rec.getSo_list() != null) {
            for (SM_SO sm_so : rec.getSo_list().getSo()) {
                //Apaga SO completa
                soDao.removeFull(sm_so);
                //
                sm_so.setPK();
            }
            //
            soDao.addUpdate(rec.getSo_list().getSo(), false);
        }
        //
        for (SO_Save_Return so_ret : rec.getSo_return()) {
            String so_pk = so_ret.getSo_prefix() + "." + so_ret.getSo_code();
            //
            if (hmAuxRet == null) {
                hmAuxRet = new HMAux();
            }
            //
            hmAuxRet.put(so_pk, "0");
            //
            if (!so_ret.getRet_status().equalsIgnoreCase("OK")) {
                hmAuxRet.put(so_pk, so_ret.getRet_msg());
            } else {
                hmAuxRet.put(so_pk, "OK");
            }
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), hmAuxRet, "", "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_process_finalized_msg");

        hmAux_Trans = ToolBox_Inf.setLanguage(
            getApplicationContext(),
            mModule_Code,
            ToolBox_Inf.getResourceCode(
                getApplicationContext(),
                mModule_Code,
                ""
            ),
         ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
         translist
        );

    }
}
