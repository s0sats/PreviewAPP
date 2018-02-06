package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.model.Chat_RoomPrivate_Env;
import com.namoadigital.prj001.receiver_chat.WBR_C_Add_Room;
import com.namoadigital.prj001.receiver_chat.WBR_Room_Private;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_Room_Private extends IntentService {

    public WS_Room_Private() {
        super("WS_Room_Private");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String user_code = bundle.getString(Constant.CHAT_WS_SOCKET_ID_PARAM,"");
            String customer_code = bundle.getString(Constant.CHAT_WS_ROOM_CODE_PARAM,"");

            processRoomPrivate(user_code,customer_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Room_Private.completeWakefulIntent(intent);
        }

    }

    private void processRoomPrivate(String user_code, String customer_code) throws Exception {

        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        Chat_RoomPrivate_Env env = new Chat_RoomPrivate_Env();
        //
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setUser_code(Integer.parseInt(user_code));
        env.setCustomer_code(Long.valueOf(customer_code));
        env.setActive(1);
        //
        //
        ToolBox.sendBCStatus(
                getApplicationContext(),
                "STATUS",
                    /*hmAux_Trans.get("msg_no_info_return")*/"Recebendo dados - Trad",
                "",
                "0"
        );
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_CHAT_ROOM_PRIVATE,
                gson.toJson(env)
        );
        //
        if (resultado.equals("")) {
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1",/* hmAux_Trans.get("msg_no_info_return")*/"Erro - Nenhum dado retornado -Trad", "", "0");
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
                    cError != null ? cError.getError_msg() : "Error-Trad",
                    "",
                    "0"
            );
            return;
        }else{
            //Sem erro, chama AddRoom
            String param = ToolBox_Inf.getWebSocketJsonParam(resultado);
            //
            Intent cRoomIntent = new Intent(getApplicationContext(), WBR_C_Add_Room.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
            bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_POST_ROOM_PRIVATE);
            cRoomIntent.putExtras(bundle);
            //
            ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "STATUS",
                    /*hmAux_Trans.get("msg_no_info_return")*/"Gravando nova sala - Trad",
                    "",
                    "0"
            );
            //
            getApplicationContext().sendBroadcast(cRoomIntent);
        }
    }
}
