package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.model.DaoError;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.TCancelNFC_Env;
import com.namoadigital.prj001.model.TCancelNFC_Rec;
import com.namoadigital.prj001.receiver.WBR_Cancel_NFC;
import com.namoadigital.prj001.sql.EV_User_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 09/05/2017.
 */

public class WS_Cancel_NFC extends IntentService {

    private EV_UserDao userDao;
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_cancel_nfc";

    public WS_Cancel_NFC() {
        super("WS_Cancel_NFC");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        //
        loadTranslation();

        try {



            processCancelNFC();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Cancel_NFC.completeWakefulIntent(intent);
        }

    }


    private void processCancelNFC() throws Exception {
        userDao = new EV_UserDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_canceling_nfc"), "", "0");

        Gson gson = new GsonBuilder().serializeNulls().create();

        TCancelNFC_Env env = new TCancelNFC_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_CANCEL_NFC,
                gson.toJson(env)
        );

        TCancelNFC_Rec rec = gson.fromJson(
                resultado,
                TCancelNFC_Rec.class
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
                ).toSqlQuery()
        );

        user.setNfc_blocked(0);
        user.setExist_nfc(0);

        userDao.addUpdate(user, new DaoError());

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_nfc_cancelled") , "", "0");

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_canceling_nfc");
        translist.add("msg_nfc_cancelled");

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
