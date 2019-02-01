package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.Main_Header_Env;
import com.namoadigital.prj001.model.SO_Client_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Client_List;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class WS_SO_Client_List extends IntentService {

    public static final String SERIAL_LOG_FILE =  "SERIAL_LOG_FILE";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_so_client_list";

    public WS_SO_Client_List() {
        super("WS_SO_Client_List");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            long customer_code = bundle.getLong(SM_SODao.CUSTOMER_CODE);
            //
            processWSSOClientList(customer_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Client_List.completeWakefulIntent(intent);
        }

    }

    private void processWSSOClientList(long customer_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        Main_Header_Env env = new Main_Header_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receving_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SERIAL_LOG,
                gson.toJson(env)
        );
        //
        SO_Client_Rec rec = gson.fromJson(resultado,SO_Client_Rec.class);
    }

    private void loadTranslation() {
    }
}
