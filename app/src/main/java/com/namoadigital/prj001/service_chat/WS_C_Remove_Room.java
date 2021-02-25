package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_C_Remove_Room;
import com.namoadigital.prj001.model.Chat_Room_Obj_Form_AP;
import com.namoadigital.prj001.receiver_chat.WBR_C_Remove_Room;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_C_Remove_Room extends IntentService {

    public WS_C_Remove_Room() {
        super("WS_C_Remove_Room");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String json_param = bundle.getString(Constant.CHAT_WS_JSON_PARAM);
            String ws_event = bundle.getString(Constant.CHAT_WS_EVENT_PARAM);

            processC_Room(json_param, ws_event);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_C_Remove_Room.completeWakefulIntent(intent);
        }

    }

    private void processC_Room(String json_param, String ws_event) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        Chat_C_Remove_Room room =
                gson.fromJson(
                        json_param,
                        Chat_C_Remove_Room.class
                );
        //
        CH_RoomDao roomDao = new CH_RoomDao(getApplicationContext());
        //
//        roomDao.removeFull(
//                new CH_Room_Sql_004(
//                        room.getRoom_code()
//                ).toSqlQuery()
//        );

        CH_Room ccRoom = roomDao.getByString(
                new CH_Room_Sql_001(
                        room.getRoom_code()
                ).toSqlQuery()
        );
        //23/08/2018
        //Verifica se obj room é null.
        //Se for, aborta processamento, pois significa que a room não existe mais
        //no banco local.
        //OBS: Não deveria acontecer,pois o bug do server que gerar msg de removeFull room com atraso
        //já foi corrigido...
        //
        if(ccRoom == null){
            return;
        }
        //
        ToolBox_Inf.cleanRoom_RoomMessages(
                getApplicationContext(),
                ccRoom
        );
        //
        if (ccRoom.getRoom_type().equalsIgnoreCase(Constant.CHAT_ROOM_TYPE_AP)) {
            Chat_Room_Obj_Form_AP objFormAp = gson.fromJson(
                    ToolBox_Inf.getRoomObjJsonParam(
                            ccRoom.getRoom_obj()
                    ),
                    Chat_Room_Obj_Form_AP.class
            );
            if (objFormAp != null) {
                //Processa dados do Ap.
                ToolBox_Inf.checkForApExclusion(
                        getApplicationContext(),
                        String.valueOf(objFormAp.getCustomer_code()),
                        String.valueOf(objFormAp.getCustom_form_type()),
                        String.valueOf(objFormAp.getCustom_form_code()),
                        String.valueOf(objFormAp.getCustom_form_version()),
                        String.valueOf(objFormAp.getCustom_form_data()),
                        String.valueOf(objFormAp.getAp_code())
                );
            }

        }
        //
        HMAux hmAux = new HMAux();
        //
        switch (ws_event) {
            case Constant.CHAT_EVENT_POST_ROOM_PRIVATE:
                hmAux.put(CH_RoomDao.ROOM_CODE, room.getRoom_code());
                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM_PRIVATE_REMOVE, hmAux);
                break;
            case Constant.CHAT_EVENT_POST_LEAVEROOM:
                hmAux.put(CH_RoomDao.ROOM_CODE, room.getRoom_code());
                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_LEAVE_ROOM, hmAux);
                break;
            case Constant.CHAT_EVENT_C_REMOVE_ROOM:
                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM);
                break;
            default:
        }
       /* if(ws_event.equals(Constant.CHAT_EVENT_POST_ROOM_PRIVATE)){
            HMAux hmAux = new HMAux();
            hmAux.put(CH_RoomDao.ROOM_CODE, room.getRoom_code());
            ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM_PRIVATE_REMOVE, hmAux);
        }else if(ws_event.equals(Constant.CHAT_EVENT_POST_LEAVEROOM)){
            HMAux hmAux = new HMAux();
            hmAux.put(CH_RoomDao.ROOM_CODE, room.getRoom_code());
            ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_LEAVE_ROOM, hmAux);
        } else {
            //
            ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM);
        }*/
    }

}
