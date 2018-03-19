package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.Chat_Room_Info_Env;
import com.namoadigital.prj001.receiver_chat.WBR_Room_Info;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_Room_Info extends IntentService {

    public WS_Room_Info() {
        super("WS_C_All_Read");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String socket_id = bundle.getString(Constant.CHAT_WS_SOCKET_ID_PARAM,"");
            String room_code = bundle.getString(Constant.CHAT_WS_ROOM_CODE_PARAM,"");

            processRoomInfo(socket_id,room_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Room_Info.completeWakefulIntent(intent);
        }

    }

    private void processRoomInfo(String socket_id, String room_code) throws Exception {

        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        Chat_Room_Info_Env env = new Chat_Room_Info_Env();
        //
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setRoom_code(room_code);
        env.setActive(1);
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_CHAT_ROOM_INFO,
                gson.toJson(env)
        );
        //
        HMAux hmAux = new HMAux();
        hmAux.put(Constant.CHAT_BR_TYPE_ROOM_INFO,ToolBox_Inf.getWebSocketJsonParam(resultado));
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM_INFO,hmAux);
    }

}
