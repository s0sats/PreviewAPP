package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.CH_Room;
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

            processC_Room(json_param);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_C_Remove_Room.completeWakefulIntent(intent);
        }

    }

    private void processC_Room(String json_param) {
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
//        roomDao.remove(
//                new CH_Room_Sql_004(
//                        room.getRoom_code()
//                ).toSqlQuery()
//        );

        CH_Room ccRoom = roomDao.getByString(
                new CH_Room_Sql_001(
                        room.getRoom_code()
                ).toSqlQuery()
        );

        ToolBox_Inf.cleanRoom_RoomMessages(
                getApplicationContext(),
                ccRoom
        );
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM);

    }

    private class Chat_C_Remove_Room {
        String room_code;

        public String getRoom_code() {
            return room_code;
        }

        public void setRoom_code(String room_code) {
            this.room_code = room_code;
        }
    }
}
