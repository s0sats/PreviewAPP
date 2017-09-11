package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TUser_Author_Env;
import com.namoadigital.prj001.model.TUser_Author_Rec;
import com.namoadigital.prj001.receiver.WBR_UserAuthor;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_UserAuthor extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_userauthor";

    public WS_UserAuthor() {
        super("WS_UserAuthor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            long so_customer = bundle.getLong(Constant.SO_PARAM_CUSTOMER_CODE, -1);
            long so_prefix = bundle.getLong(Constant.SO_PARAM_SO_PREFIX, 1900);
            long so_code = bundle.getLong(Constant.SO_PARAM_SO_CODE, 0);
            String auth_type = bundle.getString(Constant.SO_PARAM_AUTH_TYPE, Constant.SO_PARAM_AUTH_TYPE_CLIENT);
            String auth_nick_mail = bundle.getString(Constant.SO_PARAM_AUTH_NICK_MAIL, "");
            String auth_password = bundle.getString(Constant.SO_PARAM_AUTH_PASSWORD, "");
            String auth_nfc = bundle.getString(Constant.SO_PARAM_AUTH_NFC, "");

            processWS_UserAuthor(
                    so_customer,
                    so_prefix,
                    so_code,
                    auth_type,
                    auth_nick_mail,
                    auth_password,
                    auth_nfc
            );

        } catch (Exception e) {
            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            WBR_UserAuthor.completeWakefulIntent(intent);
        }

    }

    private void processWS_UserAuthor(
            long customer_code,
            long so_prefix,
            long so_code,
            String auth_type,
            String auth_nick_mail,
            String auth_password,
            String auth_nfc
    ) throws Exception {

        //Seleciona traduções
        loadTranslation();
        HMAux hmAuxRet = new HMAux();

        TUser_Author_Env env = new TUser_Author_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setCustomer_code(String.valueOf(customer_code));
        env.setSo_prefix(String.valueOf(so_prefix));
        env.setSo_code(String.valueOf(so_code));
        env.setAuth_type(auth_type);
        env.setAuth_nick_mail(auth_nick_mail);
        env.setAuth_password(auth_password.isEmpty() ? "" : ToolBox_Inf.md5(auth_password));
        env.setAuth_nfc(auth_nfc);
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SERVER_AUTH_USER,
                gson.toJson(env)
        );
        //
        TUser_Author_Rec rec = gson.fromJson(
                resultado,
                TUser_Author_Rec.class
        );

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

        processUserAuthorRet(rec);

    }

    private void processUserAuthorRet(TUser_Author_Rec rec) {
        HMAux hmAux = new HMAux();

        if (rec.getAuth().isEmpty()) {
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("generic_error_lbl") + "\n" + rec.getAuth_msg(), "", "0");

        } else {

            hmAux.put(Constant.SO_PARAM_RETURN_STATUS, rec.getAuth());
            hmAux.put(Constant.SO_PARAM_RETURN_MSG, rec.getAuth_msg());


            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
        }
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
