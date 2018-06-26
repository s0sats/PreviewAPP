package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.Chat_Add_User_Into_Room_Env;
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.receiver_chat.WBR_Add_User_Room_AP;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_Add_User_Room_AP extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_add_user_room_ap";

    public WS_Add_User_Room_AP() {
        super("WS_Add_User_Room_AP");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String socket_id = bundle.getString(Constant.CHAT_WS_SOCKET_ID_PARAM,"");
            String room_code = bundle.getString(CH_RoomDao.ROOM_CODE,"");
            String custom_form_type = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE,"");;
            String custom_form_code = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE,"");
            String custom_form_version= bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION,"");
            String custom_form_data = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA,"");
            String ap_code = bundle.getString(GE_Custom_Form_ApDao.AP_CODE,"");
            String user_code_sql = bundle.getString(CH_RoomDao.USER_CODE,"");

            processAddUsrRoomAP(socket_id, room_code,custom_form_type, custom_form_code,custom_form_version,custom_form_data,ap_code,user_code_sql);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Add_User_Room_AP.completeWakefulIntent(intent);
        }

    }

    private void processAddUsrRoomAP(String socket_id, String room_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data, String ap_code, String user_code_sql) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        Chat_Add_User_Into_Room_Env env = new Chat_Add_User_Into_Room_Env();
        //
        env.setSocket_id(socket_id);
        env.setRoom_code(room_code);
        env.setCustom_form_type(custom_form_type);
        env.setCustom_form_code(custom_form_code);
        env.setCustom_form_version(custom_form_version);
        env.setCustom_form_data(custom_form_data);
        env.setAp_code(ap_code);
        env.setUser_code_sql(user_code_sql);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_CHAT_ADD_USER_FORM_AP,
                gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");
        //
        //
        if (resultado.equals("")) {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_info_return"), "", "0");
            return;
        }
        //
        if (resultado.contains("error_msg")) {
            //
            Chat_C_Error cError =
                    gson.fromJson(
                            ToolBox_Inf.getWebSocketJsonParam(resultado),
                            Chat_C_Error.class
                    );
            //
            ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "ERROR_1",
                    cError != null ? cError.getError_msg() : getString(R.string.generic_error_lbl),
                    "",
                    "0"
            );
            return;
        }else{
            /*String tst =  ToolBox_Inf.getWebSocketJsonParam(resultado);
            ArrayList<Chat_Add_User_Into_Room_Rec> rec =
                    gson.fromJson(
                            ToolBox_Inf.getWebSocketJsonParam(resultado),
                            new TypeToken<ArrayList<Chat_Add_User_Into_Room_Rec>>() {
                            }.getType()
                    );*/
            /**
             * POR HORA NÃO EXISTE TRATIVA NO RETORNO
             *
             * SE NÃO FOR VAZIO OU ERRO, ENTÃO CONSIDERA SUCESSO!!!
             * SERÁ MODIFICADO EM BREVE QUANDO O MIOLO VOLTAR.
             */
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_ending_processing"),new HMAux(), "" , "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_no_info_return");
        translist.add("msg_receiving_data");
        translist.add("msg_sending_data");
        translist.add("msg_ending_processing");

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
