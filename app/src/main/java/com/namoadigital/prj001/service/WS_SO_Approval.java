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
import com.namoadigital.prj001.sql.SM_SO_Sql_009;
import com.namoadigital.prj001.sql.SM_SO_Sql_013;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.service.WS_SO_Save.SO_NO_EMPTY_LIST;
import static com.namoadigital.prj001.service.WS_SO_Save.SO_RETURN_LIST;


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



            soDao = new SM_SODao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM);

            processWSSOAproval();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "CUSTOM_ERROR", sb.toString(), "", "0");

        } finally {

            WBR_SO_Approval.completeWakefulIntent(intent);
        }
    }

    private void processWSSOAproval() throws Exception {
        loadTranslation();
        //
        ArrayList<SM_SO> sm_sos = (ArrayList<SM_SO>) soDao.query(
                new SM_SO_Sql_013(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ).toSqlQuery()
        );
        //
        if (sm_sos.size() == 0) {

            HMAux hmAuxRet = new HMAux();
            hmAuxRet.put(SO_NO_EMPTY_LIST,"1");
            hmAuxRet.put(SO_RETURN_LIST,"");
            //
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_approval_to_send"), hmAuxRet, "", "0");
            return;
        }

        TSO_Approval_Env env = new TSO_Approval_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken(ToolBox_Inf.getToken(getApplicationContext()));

        ArrayList<TSO_Approval_Env.SO_Approval_Item> so_status = new ArrayList<>();

        for (SM_SO so : sm_sos) {
            TSO_Approval_Env.SO_Approval_Item item = new TSO_Approval_Env.SO_Approval_Item();
            //
            item.setCustomer_code(so.getCustomer_code());
            item.setSo_prefix(so.getSo_prefix());
            item.setSo_code(so.getSo_code());
            item.setSo_scn(so.getSo_scn());

            String sType = ToolBox_Con.getApproval_Type(getApplicationContext());

            if (sType.equalsIgnoreCase(Constant.SYS_STATUS_WAITING_QUALITY)){
                item.setAction(Constant.PROFILE_MENU_SO_PARAM_APPROVE_QUALITY);
                item.setQuality_user(String.valueOf(so.getQuality_approval_user()));
                item.setQuality_date(so.getQuality_approval_date());

            } else {
                item.setAction(Constant.PROFILE_MENU_SO_PARAM_APPROVE_CLIENT);
                item.setClient_date(so.getClient_approval_date());
                item.setClient_image(so.getClient_approval_image_name());
                item.setClient_type_sig(so.getClient_type().equals("CLIENT") ? so.getClient_approval_type_sig() : null);

                item.setClient_user(so.getClient_approval_user());
                item.setClient_name(so.getClient_name());
            }

            so_status.add(item);
        }

        env.setSo_status(so_status);
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
        String so_list_ret = "";
        HMAux hmAuxRet = new HMAux();
        //
        //Gera extrato baseada no serve e seta update_required nas S.Os com erro.
        //Monta HMaux ja inserindo as S.O e setando full_refresh como 0
        for (SO_Save_Return so_ret : rec.getSo_status()) {
            String so_pk = so_ret.getSo_prefix() + "." + so_ret.getSo_code();
            //
            hmAuxRet.put(so_pk, "0");
            //
            so_list_ret += Constant.MAIN_CONCAT_STRING + so_pk
                    + Constant.MAIN_CONCAT_STRING_2 + so_ret.getRet_status();
            //
            if (!so_ret.getRet_status().equalsIgnoreCase("OK")) {
                so_list_ret += ":\n" + so_ret.getRet_msg();
            }
            //
            if (!so_ret.getRet_status().toUpperCase().equals("OK")) {
                soDao.addUpdate(
                        new SM_SO_Sql_009(
                                so_ret.getCustomer_code(),
                                so_ret.getSo_prefix(),
                                so_ret.getSo_code()
                        ).toSqlQuery()

                );
            }
        }

        //Insere so_list_ret no hmAuxRet
        hmAuxRet.put(SO_RETURN_LIST, so_list_ret.length() > 0 ? so_list_ret.substring(Constant.MAIN_CONCAT_STRING.length(), so_list_ret.length()) : "");

        if (rec.getSo() != null) {

            for (SM_SO so : rec.getSo()) {
                //Se S.O Full, atualiza hmAux de full_refresh
                hmAuxRet.put(so.getSo_prefix() + "." + so.getSo_code(), "1");
                so.setPK();
                //Apaga So do Banco
                soDao.removeFull(so);
                //Insere So novamente no banco
                soDao.addUpdate(so);
            }
        }

        callFinishProcessing(hmAuxRet);
    }

    private void callFinishProcessing(HMAux hmAuxRet) {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");
        //
        startDownloadServices();
    }

    private void startDownloadServices() {
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        getApplicationContext().sendBroadcast(mIntentPIC);
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
