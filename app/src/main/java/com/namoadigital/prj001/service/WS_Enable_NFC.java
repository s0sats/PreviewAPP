package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.ErrorCfg;
import com.namoadigital.prj001.model.TEnableNFC_Env;
import com.namoadigital.prj001.model.TEnableNFC_Rec;
import com.namoadigital.prj001.receiver.WBR_Enable_NFC;
import com.namoadigital.prj001.sql.EV_User_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 08/05/2017.
 */

public class WS_Enable_NFC extends IntentService {

    private EV_UserDao userDao;
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_enable_nfc";

    public WS_Enable_NFC() {
        super("WS_Enable_NFC");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        //
        loadTranslation();

        try {



            processEnableNFC();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Enable_NFC.completeWakefulIntent(intent);
        }

    }

    private void processEnableNFC() throws Exception {
        userDao = new EV_UserDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_enabling_nfc"), "", "0");

        Gson gson = new GsonBuilder().serializeNulls().create();

        TEnableNFC_Env env = new TEnableNFC_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_ENABLE_NFC,
                gson.toJson(env)
        );

        TEnableNFC_Rec rec = gson.fromJson(
                resultado,
                TEnableNFC_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidationNFCAuth(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                rec.getRet(),
                rec.getRet_error()
                )
        ) {
            return;
        }

        EV_User user = userDao.getByString(
                new EV_User_Sql_001(
                    ToolBox_Con.getPreference_User_Code(getApplicationContext())
                ).toSqlQuery(),
                new ErrorCfg()
        );

        user.setNfc_blocked(0);
        //user.setExist_nfc(1);

        userDao.addUpdate(user, new ErrorCfg());

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_nfc_enabled") , "", "0");

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_enabling_nfc");
        translist.add("msg_nfc_enabled");


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
