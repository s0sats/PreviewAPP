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
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.TSave_Ap_Env;
import com.namoadigital.prj001.model.TSave_Ap_Rec;
import com.namoadigital.prj001.receiver.WBR_AP_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_006;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/02/2018.
 */

public class WS_AP_Save extends IntentService {
    public static final String AP_RETURN_LIST = "AP_RETURN_LIST";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_ap_save";
    //
    private GE_Custom_Form_ApDao formApDao;

    public WS_AP_Save() {
        super("WS_AP_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            //
            processApSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_AP_Save.completeWakefulIntent(intent);
        }

    }

    private void processApSave() throws Exception {
        //Seleciona traduções
        loadTranslation();
        //Envia JSON apenas com campos marcados pela tag Expose
        Gson gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        Gson gsonRec = new GsonBuilder().serializeNulls().create();
        formApDao = new GE_Custom_Form_ApDao(getApplicationContext());
        //
        ArrayList<GE_Custom_Form_Ap> apList = (ArrayList<GE_Custom_Form_Ap>) formApDao.query(
                new GE_Custom_Form_Ap_Sql_006(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        GE_Custom_Form_Ap_Sql_006.RETURN_SQL_OBJ
                ).toSqlQuery()
        );
        //
        if (apList == null || apList.size() == 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_ap_to_save"), "", "0");
            return;
        }
        //
        TSave_Ap_Env env = new TSave_Ap_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken(ToolBox_Inf.getToken(getApplicationContext()));
        env.setAP(apList);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_ap_data"), "", "0");
        //
        String json = gsonEnv.toJson(env);
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_AP_SAVE,
                json
        );
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_data_returned"), "", "0");
        //
        TSave_Ap_Rec rec = gsonRec.fromJson(
                resultado,
                TSave_Ap_Rec.class
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
        processApSaveReturn(rec,apList);
    }

    private void processApSaveReturn(TSave_Ap_Rec rec, ArrayList<GE_Custom_Form_Ap> apList) {

        switch (rec.getSave()){
            case "OK":
                HMAux auxApReturned = new HMAux();
                //
                for (TSave_Ap_Rec.ApSaveStatus apSaveStatus: rec.getAp()) {
                    String hmAuxPKKey =
                            apSaveStatus.getCustomer_code()+"."+
                            apSaveStatus.getCustom_form_type()+"."+
                            apSaveStatus.getCustom_form_code()+"."+
                            apSaveStatus.getCustom_form_version()+"."+
                            apSaveStatus.getCustom_form_data()+"."+
                            apSaveStatus.getAp_code();

                    String ap_desc = getFormApInfo(apSaveStatus,apList);
                    //
                    auxApReturned.put(
                            hmAuxPKKey,
                            (ap_desc.length() > 0 ? (ap_desc + Constant.MAIN_CONCAT_STRING) : "") +
                            (apSaveStatus.getStatus_code() == 1 ? String.valueOf(apSaveStatus.getStatus_code()) : apSaveStatus.getStatus_msg())
                    );
                    //StatusCode é o codigo do status do server
                    //1 = OK
                    if(apSaveStatus.getStatus_code() == 1 ){
                        formApDao.addUpdate(
                            new GE_Custom_Form_Ap_Sql_009(
                                    apSaveStatus.getCustomer_code(),
                                    apSaveStatus.getCustom_form_type(),
                                    apSaveStatus.getCustom_form_code(),
                                    apSaveStatus.getCustom_form_version(),
                                    apSaveStatus.getCustom_form_data(),
                                    apSaveStatus.getAp_code(),
                                    apSaveStatus.getAp_scn()
                            ).toSqlQuery()
                        );
                    }
                }
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_end_ap_save"),auxApReturned , "", "0");
                break;
            case "ERROR":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", rec.getError_msg(), "", "0");
                break;
            default:
        }

    }

    private String getFormApInfo(TSave_Ap_Rec.ApSaveStatus apSaveStatus, ArrayList<GE_Custom_Form_Ap> apList) {
        for (GE_Custom_Form_Ap formAp : apList) {
            if(
                apSaveStatus.getCustomer_code() == formAp.getCustomer_code() &&
                apSaveStatus.getCustom_form_type() == formAp.getCustom_form_type() &&
                apSaveStatus.getCustom_form_code() == formAp.getCustom_form_code() &&
                apSaveStatus.getCustom_form_version() == formAp.getCustom_form_version() &&
                apSaveStatus.getCustom_form_data() == formAp.getCustom_form_data() &&
                apSaveStatus.getAp_code() == formAp.getAp_code()
            ){
                return formAp.getAp_code() + " - " + formAp.getAp_description();
            }
        }
        //
        return "";
    }


    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_no_ap_to_save");
        translist.add("msg_sending_ap_data");
        translist.add("msg_processing_data_returned");
        translist.add("msg_end_ap_save");
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
