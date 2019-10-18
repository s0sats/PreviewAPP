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
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SO_Save_Return;
import com.namoadigital.prj001.model.TSO_SO_Service_Rec;
import com.namoadigital.prj001.model.TSO_Set_Service_Edit_Env;
import com.namoadigital.prj001.receiver.WBR_SO_Set_Service_For_Edit;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Set_Service_For_Edit extends IntentService {
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Set_Service_For_Edit";
    private Gson gson;

    public WS_SO_Set_Service_For_Edit() {
        super("WS_SO_Set_Service_For_Edit");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            TSO_Set_Service_Edit_Env service_edit_env = (TSO_Set_Service_Edit_Env) bundle.getSerializable(WS_SO_Set_Service_For_Edit.class.getName());
            int site_zone = bundle.getInt(SM_SO_ServiceDao.SITE_CODE, -1);
            int zone_code = bundle.getInt(SM_SO_ServiceDao.ZONE_CODE, -1);
            int partner_code = bundle.getInt(SM_SO_ServiceDao.PARTNER_CODE, -1);
            int category_price_code = bundle.getInt(SM_SO_ServiceDao.CATEGORY_PRICE_CODE, -1);
            int pack_code = bundle.getInt(SM_SO_ServiceDao.PACK_CODE, -1);
            int pack_seq = bundle.getInt(SM_SO_ServiceDao.PACK_SEQ, -1);
            int price_list_code = bundle.getInt(SM_SO_ServiceDao.PRICE_LIST_CODE, -1);
            int so_prefix = bundle.getInt(SM_SO_ServiceDao.SO_PREFIX, -1);
            int so_code = bundle.getInt(SM_SO_ServiceDao.SO_CODE, -1);
            int service_code = bundle.getInt(SM_SO_ServiceDao.SERVICE_CODE, -1);
            int service_seq = bundle.getInt(SM_SO_ServiceDao.SERVICE_SEQ, -1);
            int so_scn = bundle.getInt(SM_SODao.SO_SCN, -1);
            //
            processSOSetServiceEdit(zone_code, site_zone, partner_code, category_price_code, pack_code, pack_seq, price_list_code, so_prefix, so_code, service_code, service_seq, so_scn);
            //
        }catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Set_Service_For_Edit.completeWakefulIntent(intent);
        }
    }

    private void processSOSetServiceEdit(int zone_code, int site_zone, int partner_code, int category_price_code, int pack_code, int pack_seq, int price_list_code, int so_prefix, int so_code, int service_code, int service_seq, int so_scn) throws Exception {

        loadTranslation();

        gson = new GsonBuilder().serializeNulls().create();

        TSO_Set_Service_Edit_Env env = new TSO_Set_Service_Edit_Env();
        //header
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //body
        env.setToken(ToolBox_Inf.getToken(getApplicationContext()));
        if(zone_code == -1) {
            env.setZone_code(null);
        }else{
            env.setZone_code(zone_code);
        }
        if(site_zone == -1) {
            env.setSite_code(null);
        }else{
            env.setSite_code(site_zone);
        }
        if(partner_code == -1) {
            env.setPartner_code(null);
        }else{
            env.setPartner_code(partner_code);
        }
        env.setCategory_price_code(category_price_code);
        env.setPack_code(pack_code);
        env.setPack_seq(pack_seq);
        env.setPrice_list_code(price_list_code);
        env.setSo_prefix(so_prefix);
        env.setSo_code(so_code);
        env.setService_code(service_code);
        env.setService_seq(service_seq);
        env.setSo_scn(so_scn);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_so_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SERVICE_EDIT_SET,
                gson.toJson(env)
        );

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_so_data"), "", "0");

        TSO_SO_Service_Rec rec = gson.fromJson(
                resultado,
                TSO_SO_Service_Rec.class
        );
        //
        if (!ToolBox_Inf.processWSCheckValidation(
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

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_so_return"), "", "0");
        //
        processSetServiceEditRet(rec);
    }

    private void processSetServiceEditRet(TSO_SO_Service_Rec rec) {
        SM_SODao soDao = new SM_SODao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        //
        HMAux hmAux = new HMAux();
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
            if (hmAux == null) {
                hmAux = new HMAux();
            }
            //
            hmAux.put(so_pk, "0");
            //
            if (!so_ret.getRet_status().equalsIgnoreCase("OK")) {
                hmAux.put(so_pk, so_ret.getRet_msg());
            } else {
                hmAux.put(so_pk, "OK");
            }
        }
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_sending_so_data");
        translist.add("msg_receiving_so_data");
        translist.add("msg_processing_so_return");
        translist.add("msg_save_ok");
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
