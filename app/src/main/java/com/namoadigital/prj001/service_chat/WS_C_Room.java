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
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_C_Room;
import com.namoadigital.prj001.model.Chat_Room_Obj_Form_AP;
import com.namoadigital.prj001.model.Chat_Room_Obj_SO;
import com.namoadigital.prj001.model.Chat_S_Historical_Message;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.receiver_chat.WBR_C_Room;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_013;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
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
        /*ArrayList<Chat_C_Room> rooms =
                gson.fromJson(
                        json_param,
                        new TypeToken<ArrayList<Chat_C_Room>>() {
                        }.getType());*/
        File cRoomFile  = new File(json_param);
        ArrayList<Chat_C_Room> rooms =
                gson.fromJson(
                        ToolBox_Inf.getContents(cRoomFile),
                        new TypeToken<ArrayList<Chat_C_Room>>() {
                        }.getType());

        //
        ArrayList<CH_Room> chRooms = Chat_C_Room.toCH_RoomList(rooms);
        //
        for (CH_Room chRoom : chRooms) {
            chRoom.setStatus_update(1);
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
                        processFormAP(objFormAp);
                    }
                    break;
                default:
            }
        }
        //
        roomDao.addUpdate(chRooms, false);
        //
        //
        if (cRoomFile != null){
            cRoomFile.delete();
        }
        //
        cleanUPRooms(chRooms);
        //Chama limpa Ap's desnecessarios.
        ToolBox_Inf.deleteUnnecessaryAP(getApplicationContext());
        //LUCHE - 30/06/2020
        //Substituido o antigo serviço pelo Worker de Download de Img
        ToolBox_Inf.scheduleDownloadPictureWork(getApplicationContext());
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM);
        //
        callCHistoricalMsg();
    }

    /*
       * Verifica se o ap da room existe no banco local,
       * Se existir, atualiza o room_code e verifica se scn server é maior e se for,
       * apenas seta sync_required para 1, indicando que o ap precisa
       * ser atualizado.
       */
    private void processFormAP(Chat_Room_Obj_Form_AP objFormAp) {
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
            if(dbFormAp.getAp_scn() < objFormAp.getAp_scn()){
                dbFormAp.setSync_required(1);
                //
                formApDao.addUpdate(dbFormAp);
            }
        }

    }

    private void cleanUPRooms(ArrayList<CH_Room> chRooms) {
        ToolBox_Inf.cleanRoom_RoomMessages(getApplicationContext());
    }

    private void callCHistoricalMsg() {
        String customer_filter = "";
        CH_MessageDao messageDao = new CH_MessageDao(getApplicationContext());
        //
        ArrayList<HMAux> rawlist = ToolBox_Inf.getSessionCustomerChatList(getApplicationContext());
        //
        customer_filter = ToolBox_Inf.returnHmAuxListInString(rawlist,EV_User_CustomerDao.CUSTOMER_CODE,",");
        //
        HMAux msgAux = messageDao.getByStringHM(
                new CH_Message_Sql_013(
                        customer_filter.length() > 0 ? customer_filter : String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()))
                ).toSqlQuery()
        );
        //
        //LUCHE - 25/06/2020
        //Foi identificado que o refJson estava gerando alto consumo de processamento no servidor,
        //,pois como muitos usr não entram nas salas, todas as msg ficavam com all_read = 0 e eram
        // enviadas nesse momento.(Login)
        //Modificado o conceito para que essas msg sejam enviadas somente ao acessar a sala.
        //region REF_JSON
//        ArrayList<HMAux> refJsonAux = (ArrayList<HMAux>) messageDao.query_HM(
//            new CH_Message_Sql_018(
//                getApplicationContext(),
//                ToolBox_Inf.returnHmAuxListInString(
//                    ToolBox_Inf.getSessionCustomerChatList(getApplicationContext()),
//                    EV_User_CustomerDao.CUSTOMER_CODE,
//                    ","
//                ),
//                ToolBox_Con.getPreference_User_Code(getApplicationContext())
//            ).toSqlQuery()
//        );
//        ArrayList<Chat_Ref_Json> ref_json = new ArrayList<>();
//        if (refJsonAux != null && refJsonAux.size() > 0) {
//            for (HMAux hmAux : refJsonAux) {
//                if (hmAux.get(CH_MessageDao.MSG_PREFIX) != null && hmAux.get(CH_MessageDao.MSG_CODE) != null) {
//                    Chat_Ref_Json refAux = new Chat_Ref_Json();
//                    refAux.setMsg_prefix(
//                        Integer.valueOf(hmAux.get(CH_MessageDao.MSG_PREFIX))
//                    );
//                    refAux.setMsg_code(
//                        Integer.valueOf(hmAux.get(CH_MessageDao.MSG_CODE))
//                    );
//                    //
//                    ref_json.add(refAux);
//                }
//            }
//        }
        //endregion
        //
        Chat_S_Historical_Message sHistoricalMessage = new Chat_S_Historical_Message();
        sHistoricalMessage.setMsg_ref_prefix(msgAux == null ? null : Integer.valueOf(msgAux.get(CH_MessageDao.MSG_PREFIX)));
        sHistoricalMessage.setMsg_ref_code(msgAux == null ? null : Integer.valueOf(msgAux.get(CH_MessageDao.MSG_CODE)));
        sHistoricalMessage.setAction(Constant.CHAT_HISTORICAL_MSG_ACTION_LOGIN);
        // LUCHE - 25/06/2020 - Comentado processo do ref_json
        //sHistoricalMessage.setRef_json(ref_json);
        sHistoricalMessage.setRef_json(null);
        //
        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
        singletonWebSocket.attemptSendHistoricalMessages(ToolBox_Inf.setWebSocketJsonParam(sHistoricalMessage));
    }
}
