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
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SO_Save_Return;
import com.namoadigital.prj001.model.TSO_Product_Event_Cancel_Env;
import com.namoadigital.prj001.model.TSO_Product_Event_Cancel_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Product_Event_Cancel;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Product_Event_Cancel extends IntentService {

    private int so_prefix;
    private int so_code;
    private int so_scn;
    private int seq;

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = ConstantBaseApp.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_so_product_event_cancel";

    public WS_SO_Product_Event_Cancel() {
        super("WS_SO_Product_Event_Cancel");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            so_prefix = bundle.getInt(SM_SO_Product_EventDao.SO_PREFIX, -1);
            so_code = bundle.getInt(SM_SO_Product_EventDao.SO_CODE, -1);
            seq = bundle.getInt(SM_SO_Product_EventDao.SEQ, -1);
            so_scn = bundle.getInt(SM_SODao.SO_SCN, -1);
            //
            loadTranslation();
            //
            processSO_Product_Event_Cancel();
        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_SO_Product_Event_Cancel.completeWakefulIntent(intent);
        }
    }

    private void processSO_Product_Event_Cancel() throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_so_data"), "", "0");
        //
        String mToken = ToolBox_Inf.getToken(getApplicationContext());
        TSO_Product_Event_Cancel_Env env = new TSO_Product_Event_Cancel_Env();
        env.setSo_code(so_code);
        env.setSo_prefix(so_prefix);
        env.setSo_scn(so_scn);
        env.setSeq(seq);
        env.setToken(mToken);
        Gson gson = new GsonBuilder().serializeNulls().create();

        //set Header
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_PRODUCT_EVENT_CANCEL,
                gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_so_data"), "", "0");
        //
        TSO_Product_Event_Cancel_Rec rec = gson.fromJson(
                resultado,
                TSO_Product_Event_Cancel_Rec.class
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
        SM_SODao soDao = new SM_SODao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        //
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
        HMAux hmAuxRet = new HMAux();
        for (SO_Save_Return so_ret : rec.getSo_return()) {
            String so_pk = so_ret.getSo_prefix() + "." + so_ret.getSo_code();
            //
            if (hmAuxRet == null) {
                hmAuxRet = new HMAux();
            }
            //
            hmAuxRet.put(so_pk, "0");
            //
            hmAuxRet.put("label", so_pk );
            hmAuxRet.put("type", "S.O.");

            String status;
            if (!so_ret.getRet_status().equalsIgnoreCase("OK")) {
                status = so_ret.getRet_msg();
            } else {
                status = "OK";
            }
            hmAuxRet.put("status", status);
            hmAuxRet.put("final_status",so_pk  + " / " + status);
        }

        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_preparing_so_data");
        translist.add("msg_loading_so_from_token");
        translist.add("msg_sending_so_data");
        translist.add("msg_receiving_so_data");
        translist.add("msg_processing_from_to_data");
        translist.add("msg_re_processing_so_data");
        translist.add("msg_token_file_error");
        translist.add("error_from_to_processing");

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
