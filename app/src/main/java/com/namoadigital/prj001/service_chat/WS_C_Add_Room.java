package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_C_Room;
import com.namoadigital.prj001.model.Chat_Room_Obj_Form_AP;
import com.namoadigital.prj001.model.Chat_Room_Obj_SO;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Process_Form_Ap;
import com.namoadigital.prj001.receiver_chat.WBR_C_Add_Room;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_C_Add_Room extends IntentService {

    public WS_C_Add_Room() {
        super("WS_C_Add_Room");
    }

    private boolean startFormApService = false;

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

            WBR_C_Add_Room.completeWakefulIntent(intent);
        }

    }

    private void processC_Room(String json_param, String ws_event) throws Exception {
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
            //Preenche campo com status da room
            switch (chRoom.getRoom_type()) {
                case Constant.CHAT_ROOM_TYPE_SO:
                    Chat_Room_Obj_SO roomObjSo = gson.fromJson(
                            ToolBox_Inf.getRoomObjJsonParam(
                                    chRoom.getRoom_obj()
                            ),
                            Chat_Room_Obj_SO.class
                    );
                    if(roomObjSo != null){
                        chRoom.setRoom_status(roomObjSo.getSo_status());
                    }
                    break;
                case Constant.CHAT_ROOM_TYPE_AP:
                    Chat_Room_Obj_Form_AP objFormAp = gson.fromJson(
                            ToolBox_Inf.getRoomObjJsonParam(
                                    chRoom.getRoom_obj()
                            ),
                            Chat_Room_Obj_Form_AP.class
                    );
                    if(objFormAp != null){
                        chRoom.setRoom_status(objFormAp.getAp_status());
                        //Processa dados do Ap.
                        processFormAP(objFormAp,chRoom.getRoom_code(),chRoom.getRoom_obj());
                    }
                    break;
                default:
            }
        }
        //
        roomDao.addUpdate(chRooms, false);
        //
        startDownloadService();
        //
        if(startFormApService){
            startFormApService();
        }
        //
        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
        //
        singletonWebSocket.attemptSendPendingMessages(chRooms.get(0).getRoom_code());
        //
        if (chRooms.get(0).getMsg_prefix() != null && chRooms.get(0).getMsg_code() != null) {
            ToolBox_Con.connHttpGet(
                    Constant.WS_CHAT_MESSAGE_DIST + "msg_prefix=" + chRooms.get(0).getMsg_prefix() + "&msg_code=" + chRooms.get(0).getMsg_code(),
                    ""
            );
        }
        //
        if (ws_event.equals(Constant.CHAT_EVENT_POST_ROOM_PRIVATE)) {
            // Private Rooms
            HMAux hmAux = new HMAux();
            hmAux.put(CH_RoomDao.ROOM_CODE, chRooms.get(0).getRoom_code());
            ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM_PRIVATE_ADD, hmAux);

        } else if(ws_event.equals(Constant.CHAT_EVENT_POST_ROOM_AP)){
            HMAux hmAux = new HMAux();
            hmAux.put(CH_RoomDao.ROOM_CODE,chRooms.get(0).getRoom_code());
            //
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT","",hmAux, "" , "0");

        } else {
            //
            ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM);
        }
    }
    /*
    * Verifica se o ap da room existe no banco local,
    * Se existir, atualiza o room_code e verifica se scn server é maior e se for,
    * apenas seta sync_required para 1, indicando que o ap precisa
    * ser atualizado.
    */
    private void processFormAP(Chat_Room_Obj_Form_AP objFormAp, String room_code, String room_obj) {
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(objFormAp.getCustomer_code()),
                Constant.DB_VERSION_CUSTOM
        );
        //
        GE_Custom_Form_Ap dbFormAp = formApDao.getByString(
                new GE_Custom_Form_Ap_Sql_005(
                        String.valueOf(objFormAp.getCustomer_code()),
                        String.valueOf(objFormAp.getCustom_form_type()),
                        String.valueOf(objFormAp.getCustom_form_code()),
                        String.valueOf(objFormAp.getCustom_form_version()),
                        String.valueOf(objFormAp.getCustom_form_data()),
                        String.valueOf(objFormAp.getAp_code()),
                        GE_Custom_Form_Ap_Sql_005.RETURN_SQL_OBJ
                ).toSqlQuery()
        );
        //
        if(dbFormAp != null && dbFormAp.getCustomer_code() > 0){
            //Atualiza room code no ap, caso room_code seja null
            dbFormAp.setRoom_code(dbFormAp.getRoom_code() == null ? room_code : dbFormAp.getRoom_code());
            //
            if(dbFormAp.getAp_scn() < objFormAp.getAp_scn()){
                dbFormAp.setSync_required(1);
                //
                formApDao.addUpdate(dbFormAp);
            }else{
                formApDao.addUpdate(dbFormAp);
            }
        }else{
            if(objFormAp.getAp_when() != null && ToolBox_Con.getPreference_User_Code(getApplicationContext()).equalsIgnoreCase(String.valueOf(objFormAp.getAp_who()))){
                //cria arquivo com o form ap
                createMsgsFile(room_obj, Constant.CHAT_MESSAGE_TYPE_FORM_AP +"_"+ objFormAp.getPk().replace("|","_"));
                //Se false, seta variavel de inicio do serviço de form ap para true.
                if (!startFormApService) {
                    startFormApService = true;
                }
            }
        }

    }

    private void startFormApService() {
        Intent mIntentFormAp = new Intent(getApplicationContext(), WBR_Process_Form_Ap.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(getApplicationContext()));
        mIntentFormAp.putExtras(bundle);
        //
        getApplicationContext().sendBroadcast(mIntentFormAp);
    }

    private String createMsgsFile(String param, String type) {
        String fileName = Constant.CHAT_PREFIX +
                (type != null ? type : "") +
                ToolBox_Inf.getToken(getApplicationContext()) +
                "_" + UUID.randomUUID().toString() +
                ".txt";
        //
        File msgListFile = new File(Constant.CHAT_PATH, fileName);
        try {
            ToolBox_Inf.writeIn(param, msgListFile);
            return msgListFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            ToolBox_Inf.registerException(getClass().getName(), e);
            return null;
        }
    }

    private void startDownloadService() {
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(getApplicationContext()));
        mIntentPIC.putExtras(bundle);
        //
        getApplicationContext().sendBroadcast(mIntentPIC);
    }
}
