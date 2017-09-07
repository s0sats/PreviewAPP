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
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SO_Save_Return;
import com.namoadigital.prj001.model.TSO_Approval_Env;
import com.namoadigital.prj001.model.TSO_Approval_Rec;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_SO_Approval;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.service.WS_SO_Serial_Save.SO_RETURN_FULL_REFRESH;
import static com.namoadigital.prj001.service.WS_SO_Serial_Save.SO_RETURN_LIST;
import static com.namoadigital.prj001.service.WS_SO_Serial_Save.SO_RETURN_STATUS;


/**
 * Created by d.luche on 27/07/2017.
 */

public class WS_SO_Approval extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_so_approval";
    private SM_SODao soDao;

    public WS_SO_Approval() {
        super("WS_SO_Approval");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            soDao = new SM_SODao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);

            //int so_prefix = bundle.getInt(Constant.SO_PARAM_PREFIX, 1900);
            //int so_code = bundle.getInt(Constant.SO_PARAM_CODE, 0);
            String nfc_code = bundle.getString(Constant.GC_NFC, null);
            String pwd =  bundle.getString(Constant.GC_PWD, null);

            //processWSSOAproval(so_prefix, so_code, nfc_code, pwd);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Approval.completeWakefulIntent(intent);
        }
    }

    private void processWSSOAproval(int so_prefix, int so_code, String nfc_code, String pwd) {

        //Seleciona traduções
        loadTranslation();
        //
        SM_SO so = soDao.getByString(
                new SM_SO_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        so_prefix,
                        so_code
                ).toSqlQuery()
        );
        //
        if(so == null){
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_so_found"), "", "0");
            return;
        }
        String action = defineAction(so.getStatus());

        TSO_Approval_Env env = new TSO_Approval_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setCustomer_code(so.getCustomer_code());
        env.setSo_prefix(so.getSo_prefix());
        env.setSo_code(so.getSo_code());
        env.setSo_scn(so.getSo_scn());
        env.setAction(action);
        env.setClient_date(so.getClient_approval_date());
        env.setClient_image(so.getClient_approval_image_name());
        env.setClient_type_sig( so.getClient_type() == "CLIENT" ? so.getClient_type()  : null );
        env.setClient_password(pwd);
        env.setClient_nfc(nfc_code);
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SAVE,
                gson.toJson(env)
        );
        //
        TSO_Approval_Rec rec = gson.fromJson(
                resultado,
                TSO_Approval_Rec.class
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
        processSOSaveRet(rec);

    }

    private void processSOSaveRet(TSO_Approval_Rec rec) {
        HMAux hmAux = new HMAux();
        String so_list_ret = "";
        String so_list_status = "";

        if(!rec.getSo_status().get(0).equals("OK")){
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("generic_error_lbl") +"\n" + rec.getSo_status().get(0).getRet_msg(), "", "0");
        }else{
            if (rec.getSo() != null) {
                for (SM_SO so : rec.getSo()) {
                    so.setPK();
                    //Apaga So do Banco
                    soDao.removeFull(so);
                    //Insere So novamente no banco
                    soDao.addUpdate(so);
                }
                //Monta String com dados das S.O enviadas para processamento
                for (SO_Save_Return so_ret : rec.getSo_status()) {
                    so_list_ret += "#" + so_ret.getSo_prefix() + "." + so_ret.getSo_code();
                    so_list_status += "#" + so_ret.getRet_status();
                }

                hmAux.put(SO_RETURN_LIST, so_list_ret.substring(1, so_list_ret.length()));
                hmAux.put(SO_RETURN_STATUS, so_list_status.substring(1, so_list_status.length()));
                hmAux.put(SO_RETURN_FULL_REFRESH, String.valueOf(1));

                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
                //
                startDownloadServices();
            }else{
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_so_full_returned"), hmAux, "", "0");
            }

        }

    }

    private void startDownloadServices() {
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        getApplicationContext().sendBroadcast(mIntentPIC);
    }

    private String defineAction(String so_status) {
        String action = "";

        switch(so_status){
            case Constant.SO_STATUS_WAITING_CLIENT:
                action = "approve_client";
                break;
            case Constant.SO_STATUS_WAITING_QUALITY:
                action = "approve_quality";
                break;
            case Constant.SO_STATUS_WAITING_BUDGET:
                action = "approve_quality";
                break;
            default:
                break;
        }

        return action;
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        translist.add("msg_no_so_found");
        translist.add("msg_save_ok");
        translist.add("msg_no_so_full_returned");

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
