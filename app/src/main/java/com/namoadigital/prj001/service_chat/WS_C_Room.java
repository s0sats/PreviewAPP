package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_C_Room;
import com.namoadigital.prj001.model.Chat_S_Historical_Message;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver_chat.WBR_C_Room;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_013;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_C_Room extends IntentService {

    public WS_C_Room() {
        super("WS_C_Room");
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

            WBR_C_Room.completeWakefulIntent(intent);
        }

    }

    private void processC_Room(String json_param) {
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
            CH_Room dbRoom = roomDao.getByString(
                    new CH_Room_Sql_001(
                            chRoom.getRoom_code()
                    ).toSqlQuery()
            );
            //Valida retornou registro do banco.
            if(dbRoom != null && dbRoom.getRoom_code().length() > 0){
                //Se existe o registro, valida se a url da imagem enviada é a mesma
                //que do registro no banco.
                //Se não for, zera url local e apaga imagem do app para que a nova
                //imagem seja baixada.
                if( chRoom.getRoom_image() == null || chRoom.getRoom_image_name() == null ||
                    !chRoom.getRoom_image_name().equalsIgnoreCase(dbRoom.getRoom_image_name())
                ){
                    File file = new File(Constant.CACHE_CHAT_PATH + "/"+dbRoom.getRoom_image_local());
                    //
                    if(file.exists() && file.isFile()) {
                        file.delete();
                    }
                }else{
                    chRoom.setRoom_image_local(dbRoom.getRoom_image_local());
                }
            }
        }
        //
        roomDao.addUpdate(chRooms, false);
        //
        startDownloadService();
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM);
        //
        callCHistoricalMsg();
    }

    private void callCHistoricalMsg() {
        CH_MessageDao messageDao = new CH_MessageDao(getApplicationContext());
        HMAux msgAux = messageDao.getByStringHM(new CH_Message_Sql_013().toSqlQuery());
        //
        Chat_S_Historical_Message sHistoricalMessage = new Chat_S_Historical_Message();
        sHistoricalMessage.setMsg_ref_prefix(msgAux == null ? null : Integer.valueOf(msgAux.get(CH_MessageDao.MSG_PREFIX)));
        sHistoricalMessage.setMsg_ref_code(msgAux == null ? null : Integer.valueOf(msgAux.get(CH_MessageDao.MSG_CODE)));
        sHistoricalMessage.setAction(Constant.CHAT_HISTORICAL_MSG_ACTION_LOGIN);
        //
        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
        singletonWebSocket.attemptSendHistoricalMessages(ToolBox_Inf.setWebSocketJsonParam(sHistoricalMessage));
    }

    private void startDownloadService() {
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        //
        getApplicationContext().sendBroadcast(mIntentPIC);

    }

}
