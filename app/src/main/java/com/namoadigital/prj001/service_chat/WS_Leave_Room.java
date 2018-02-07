package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.model.Chat_C_Remove_Room;
import com.namoadigital.prj001.model.Chat_Leave_Room_Env;
import com.namoadigital.prj001.receiver_chat.WBR_C_Remove_Room;
import com.namoadigital.prj001.receiver_chat.WBR_Leave_Room;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_Leave_Room extends IntentService {

    public WS_Leave_Room() {
        super("WS_Leave_Room");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String user_code = bundle.getString(CH_RoomDao.USER_CODE, "");
            String room_code = bundle.getString(CH_RoomDao.ROOM_CODE, "");

            processLeaveRoom(room_code, user_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Leave_Room.completeWakefulIntent(intent);
        }

    }

    private void processLeaveRoom(String room_code, String user_code) throws Exception {

        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        Chat_Leave_Room_Env env = new Chat_Leave_Room_Env();
        //
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setUser_code(user_code);
        env.setRoom_code(room_code);
        //
        ToolBox.sendBCStatus(
                getApplicationContext(),
                "STATUS",
                    /*hmAux_Trans.get("msg_no_info_return")*/"Deixando a sala - Trad",
                "",
                "0"
        );
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_CHAT_LEAVE_ROOM,
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
        } else if(resultado.contains("{\"obj\":\"OK\"}")) {
            Chat_C_Remove_Room param = new Chat_C_Remove_Room();
            param.setRoom_code(room_code);

            Intent cRoomIntent = new Intent(getApplicationContext(), WBR_C_Remove_Room.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.CHAT_WS_JSON_PARAM,gson.toJson(param));
            bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_POST_LEAVEROOM);
            cRoomIntent.putExtras(bundle);
            //
            ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "STATUS",
                    /*hmAux_Trans.get("msg_no_info_return")*/"Removendo sala do banco local - Trad",
                    "",
                    "0"
            );
            getApplicationContext().sendBroadcast(cRoomIntent);
        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1",/* hmAux_Trans.get("msg_no_info_return")*/"Erro - Nenhum dado retornado -Trad", "", "0");
            return;
        }
    }
}

