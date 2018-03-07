package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.TAdd_Room_AP_Env;
import com.namoadigital.prj001.model.TAdd_Room_AP_Rec;
import com.namoadigital.prj001.receiver_chat.WBR_Add_Room_AP;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 07/03/2018.
 */

public class WS_Add_Room_AP extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_add_room_ap";

    public WS_Add_Room_AP() {
        super("WS_Add_Room_AP");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        //
        try {
            int custom_form_type = bundle.getInt(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE,-1);
            int custom_form_code = bundle.getInt(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE,-1);
            int custom_form_version = bundle .getInt(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION,-1);
            int custom_form_data = bundle.getInt(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA,-1);
            int ap_code = bundle .getInt(GE_Custom_Form_ApDao.AP_CODE,-1);
            //
            processWSAddRoomAP(custom_form_type,custom_form_code,custom_form_version,custom_form_data,ap_code);
        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Add_Room_AP.completeWakefulIntent(intent);
        }

    }

    private void processWSAddRoomAP(int custom_form_type, int custom_form_code, int custom_form_version, int custom_form_data, int ap_code)  throws Exception  {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        TAdd_Room_AP_Env env = new TAdd_Room_AP_Env();
        //
        env.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()));
        env.setTranslate_code(ToolBox_Con.getPreference_Translate_Code(getApplicationContext()));
        env.setUser_code(ToolBox_Con.getPreference_User_Code(getApplicationContext()));
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setCustom_form_type(custom_form_type);
        env.setCustom_form_code(custom_form_code);
        env.setCustom_form_version(custom_form_version);
        env.setCustom_form_data(custom_form_data);
        env.setAp_code(ap_code);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_ap_info"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_AP_SAVE,
                gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_ap_info"), "", "0");
        //
        TAdd_Room_AP_Rec rec = gson.fromJson(
                ToolBox_Inf.getWebSocketJsonParam(resultado),
                TAdd_Room_AP_Rec.class
        );
        //
        if(rec.getError_msg() != null){
            ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "ERROR_1",
                    rec.getError_msg(),
                    "",
                    "0"
            );
            return;

        }else{

        }

    }
}
