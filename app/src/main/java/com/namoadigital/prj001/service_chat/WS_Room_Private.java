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
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.model.Chat_RoomPrivate_Env;
import com.namoadigital.prj001.receiver_chat.WBR_C_Add_Room;
import com.namoadigital.prj001.receiver_chat.WBR_C_Remove_Room;
import com.namoadigital.prj001.receiver_chat.WBR_Room_Private;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_Room_Private extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_room_private";

    public WS_Room_Private() {
        super("WS_Room_Private");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            Integer activeRoom = bundle.getInt(Constant.CHAT_WS_ROOM_PRIVATE_ACTIVE_PARAM);
            String user_code = bundle.getString(CH_RoomDao.USER_CODE,"");
            String customer_code = bundle.getString(CH_RoomDao.CUSTOMER_CODE,"");
            String room_code = bundle.getString(CH_RoomDao.ROOM_CODE,"");

            processRoomPrivate(user_code, customer_code, activeRoom, room_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Room_Private.completeWakefulIntent(intent);
        }

    }

    private void processRoomPrivate(String user_code, String customer_code, int activeRoom, String room_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        Chat_RoomPrivate_Env env = new Chat_RoomPrivate_Env();
        //
        String session_app = ToolBox_Inf.getCustomerSession(
                getApplicationContext(),
                ToolBox_Con.getPreference_User_Code(getApplicationContext()),
                Long.valueOf(customer_code)
        );
        env.setSession_app(session_app != null ? session_app : ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setUser_code(Integer.parseInt(user_code));
        env.setCustomer_code(Long.valueOf(customer_code));
        env.setActive(activeRoom);
        //
        ToolBox.sendBCStatus(
                getApplicationContext(),
                "STATUS",
                 hmAux_Trans.get("msg_receiving_data"),
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
            if(activeRoom == 1) {
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
                        hmAux_Trans.get("msg_saving_new_room"),
                        "",
                        "0"
                );
                //
                getApplicationContext().sendBroadcast(cRoomIntent);
            }else if(activeRoom == 0){
                //
                /*Chat_C_Remove_Room param = new Chat_C_Remove_Room();
                param.setRoom_code(room_code);*/

                Intent cRoomIntent = new Intent(getApplicationContext(), WBR_C_Remove_Room.class);
                Bundle bundle = new Bundle();
                //
                bundle.putString(Constant.CHAT_WS_JSON_PARAM, ToolBox_Inf.getWebSocketJsonParam(resultado));
                bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_POST_ROOM_PRIVATE);
                cRoomIntent.putExtras(bundle);
                //
                ToolBox.sendBCStatus(
                        getApplicationContext(),
                        "STATUS",
                        hmAux_Trans.get("msg_removing_room"),
                        "",
                        "0"
                );
                getApplicationContext().sendBroadcast(cRoomIntent);
            }
        }
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_no_info_return");
        translist.add("msg_receiving_data");
        translist.add("msg_saving_new_room");
        translist.add("msg_removing_room");

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
