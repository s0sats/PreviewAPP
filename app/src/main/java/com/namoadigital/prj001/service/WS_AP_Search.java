package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.TSearch_Ap_Env;
import com.namoadigital.prj001.model.TSearch_Ap_Rec;
import com.namoadigital.prj001.receiver.WBR_AP_Search;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/02/2018.
 */

public class WS_AP_Search extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_ap_search";
    //
    private GE_Custom_Form_ApDao formApDao;

    public WS_AP_Search() {
        super("WS_AP_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            int sync_required = bundle.getInt(GE_Custom_Form_ApDao.SYNC_REQUIRED, 0);
            long customer_code = bundle.getLong(GE_Custom_Form_ApDao.CUSTOMER_CODE, -1L);
            int custom_form_type = bundle.getInt(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, -1);
            int custom_form_code = bundle.getInt(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, -1);
            int custom_form_version = bundle.getInt(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, -1);
            long custom_form_data = bundle.getLong(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, -1L);
            Integer ap_code = bundle.getInt(GE_Custom_Form_ApDao.AP_CODE, -1);
            //
            processAPSearch(
                    sync_required,
                    customer_code,
                    custom_form_type,
                    custom_form_code,
                    custom_form_version,
                    custom_form_data,
                    ap_code

            );

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_AP_Search.completeWakefulIntent(intent);
        }

    }

    private void processAPSearch(int sync_required, long customer_code, int custom_form_type, int custom_form_code, int custom_form_version, long custom_form_data, Integer ap_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        formApDao = new GE_Custom_Form_ApDao(getApplicationContext());
        JsonArray apJsonArray = new JsonArray();
        String obj = "";
        //
        if (sync_required == 1) {
            ArrayList<HMAux> apAuxList = (ArrayList<HMAux>) formApDao.query_HM(
                    new GE_Custom_Form_Ap_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                    ).toSqlQuery()
            );
            //
            if (apAuxList != null && apAuxList.size() > 0) {
                //
                for (HMAux hmAux : apAuxList) {
                    TSearch_Ap_Env.ObjAp objAp = new TSearch_Ap_Env.ObjAp();
                    //
                    objAp.setCustomer_code(hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
                    objAp.setCustom_form_type(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
                    objAp.setCustom_form_code(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
                    objAp.setCustom_form_version(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
                    objAp.setCustom_form_data(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
                    objAp.setAp_code(hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
                    objAp.setAp_scn(hmAux.get(GE_Custom_Form_ApDao.AP_SCN));
                    //
                    apJsonArray.add(gson.toJsonTree(objAp));
                }
            } else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_ap_to_sync"), "", "0");
                return;
            }

        } else {
            HMAux hmAux = formApDao.getByStringHM(
                    new GE_Custom_Form_Ap_Sql_005(
                            String.valueOf(customer_code),
                            String.valueOf(custom_form_type),
                            String.valueOf(custom_form_code),
                            String.valueOf(custom_form_version),
                            String.valueOf(custom_form_data),
                            String.valueOf(ap_code),
                            GE_Custom_Form_Ap_Sql_005.RETURN_SQL_HM_AUX
                    ).toSqlQuery()
            );
            //
            if (hmAux != null && hmAux.size() > 0) {
                TSearch_Ap_Env.ObjAp objAp = new TSearch_Ap_Env.ObjAp();
                //
                objAp.setCustomer_code(hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
                objAp.setCustom_form_type(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
                objAp.setCustom_form_code(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
                objAp.setCustom_form_version(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
                objAp.setCustom_form_data(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
                objAp.setAp_code(hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
                objAp.setAp_scn(hmAux.get(GE_Custom_Form_ApDao.AP_SCN));
                //
                apJsonArray.add(gson.toJsonTree(objAp));
            } else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_ap_to_sync"), "", "0");
                return;
            }
        }
        //
        obj = ToolBox_Inf.setWebSocketJsonParam(apJsonArray);
        //
        DataPackage dataPackage = new DataPackage();
        dataPackage.setAP(obj);
        //
        TSearch_Ap_Env env = new TSearch_Ap_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setData_package(dataPackage);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_ap_info"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_AP_SEARCH,
                gson.toJson(env)
        );
        //
        TSearch_Ap_Rec rec = gson.fromJson(
                resultado,
                TSearch_Ap_Rec.class
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
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_list"), "", "0");
        //
        processAPSearchReturn(rec.getObj());
    }

    private void processAPSearchReturn(ArrayList<GE_Custom_Form_Ap> obj) {
        for (GE_Custom_Form_Ap formAp:obj) {
            formAp.setLast_update(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"));
        }
        formApDao.addUpdate(obj,false);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_end_ap_sync"),"", "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_receiving_ap_info");
        translist.add("msg_processing_list");
        translist.add("msg_end_ap_sync");
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
