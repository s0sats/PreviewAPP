package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_C_Room;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver_chat.WBR_C_Add_Room;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_C_Add_Room extends IntentService {

    public WS_C_Add_Room() {
        super("WS_C_Add_Room");
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

            WBR_C_Add_Room.completeWakefulIntent(intent);
        }

    }

    private void processC_Room(String json_param) {
        boolean firstRoom = true;
        String room_codes = "";

        Gson gson = new GsonBuilder().serializeNulls().create();
        CH_RoomDao roomDao = new CH_RoomDao(getApplicationContext());
        //
        ArrayList<Chat_C_Room> rooms =
                gson.fromJson(
                        json_param,
                        new TypeToken<ArrayList<Chat_C_Room>>() {
                        }.getType());
        //
        ArrayList<CH_Room> chRooms = Chat_C_Room.toCH_RoomList(rooms);
        //
        for (CH_Room chRoom : chRooms) {

            if (!firstRoom) {
                room_codes += "#" + chRoom.getRoom_code();
            } else {
                firstRoom = false;
                room_codes += chRoom.getRoom_code();
            }

            CH_Room dbRoom = roomDao.getByString(
                    new CH_Room_Sql_001(
                            chRoom.getRoom_code()
                    ).toSqlQuery()
            );
            //Valida retornou registro do banco.
            if (dbRoom != null && dbRoom.getRoom_code().length() > 0) {

                //Se existe o registro, valida se a url da imagem enviada é a mesma
                //que do registro no banco.
                //Se não for, zera url local e apaga imagem do app para que a nova
                //imagem seja baixada.
                if (chRoom.getRoom_image() == null ||
                        !chRoom.getRoom_image().equalsIgnoreCase(dbRoom.getRoom_image())
                        ) {
                    File file = new File(Constant.CACHE_PATH + "/" + dbRoom.getRoom_image_local());
                    //
                    if (file.exists() && file.isFile()) {
                        file.delete();
                    }
                } else {
                    chRoom.setRoom_image_local(dbRoom.getRoom_image_local());
                }
            }
        }
        //
        roomDao.addUpdate(chRooms, false);
        //
        startDownloadService();
        //
        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
        //
        singletonWebSocket.attemptSendPendingMessages(chRooms.get(0).getRoom_code());
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM);
        //
        // Processamento de get para distribuição.
        try {
            ToolBox_Con.connHttpGet(
                    Constant.WS_CHAT_MESSAGE_DIST + "msg_prefix=" + chRooms.get(0).getMsg_prefix() + "&msg_code=" + chRooms.get(0).getMsg_code(),
                    ""
            );

        } catch (Exception e) {
            String nn = e.toString();
        }

        // Private Rooms
        //
        Intent cRoomPrivateIntent = new Intent(Constant.CHAT_EVENT_C_ROOM_PRIVATE);
        cRoomPrivateIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Bundle bundleAux = new Bundle();
        bundleAux.putString(Constant.CHAT_WS_JSON_PARAM, room_codes);
        cRoomPrivateIntent.putExtras(bundleAux);
        //
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(cRoomPrivateIntent);
    }

    private void startDownloadService() {
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        //
        getApplicationContext().sendBroadcast(mIntentPIC);
    }
}
