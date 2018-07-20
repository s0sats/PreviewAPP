package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_C_Message;
import com.namoadigital.prj001.model.Chat_Message_Obj_Form_Ap;
import com.namoadigital.prj001.model.Chat_S_Delivered;
import com.namoadigital.prj001.model.Chat_S_Read;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Process_Form_Ap;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message_Tmp;
import com.namoadigital.prj001.receiver_chat.WBR_Delivered;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_005;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.sql.CH_Room_Sql_013;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by d.luche on 01/12/2017.
 */

public class WS_C_Message extends IntentService {

    //ANALISAR NECESSIDADE DE MUDANÇA PARA  CRIARA OBJ JSONE  AVALISA SE TYPE = IMAGE
    private final String CONTAINS_IMAGE_MSG = "\"type\":\"IMAGE\"";

    private CH_MessageDao messageDao;


    public WS_C_Message() {
        super("WS_C_Message");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            //
            messageDao = new CH_MessageDao(getApplicationContext());
            String json_param = bundle.getString(Constant.CHAT_WS_JSON_PARAM);
            String ws_event = bundle.getString(Constant.CHAT_WS_EVENT_PARAM, "");
            String messageTmpFile = bundle.getString(Constant.CHAT_WS_MSG_TMP_PARAM, null);
            String historicalAction = bundle.getString(Constant.CHAT_WS_HISTORICAL_ACTION_PARAM, null);
            long messageIncrement = bundle.getLong(Constant.CHAT_WS_MSG_COUNTER_PARAM, 0);
            processC_Message(json_param, ws_event, messageTmpFile, historicalAction, messageIncrement);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_C_Message.completeWakefulIntent(intent);
        }

    }

    private void processC_Message(String json_param, String ws_event, String messageTmpFile, String historicalAction, long messageIncrement) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<Chat_C_Message> messages = new ArrayList<>();
        File msgListFile = null;
        //
        if (ws_event.equalsIgnoreCase(Constant.CHAT_EVENT_C_HISTORICAL_MESSAGES)) {
            msgListFile = new File(json_param);
            //
            messages = gson.fromJson(
                    ToolBox_Inf.getContents(msgListFile),
                    new TypeToken<ArrayList<Chat_C_Message>>() {
                    }.getType());
        } else {
            //
            messages = gson.fromJson(
                    json_param,
                    new TypeToken<ArrayList<Chat_C_Message>>() {
                    }.getType());
        }
        //Se é um "cMessage" de uma msg minha,
        //atualiza data da msg pra a data do server.
        if (ws_event.equalsIgnoreCase(Constant.CHAT_EVENT_C_MESSAGE)
                && ToolBox_Con.getPreference_User_Code(getApplicationContext())
                .equalsIgnoreCase(String.valueOf(messages.get(0).getUser_code()))
                ) {
            JsonArray sDeliveredList = new JsonArray();
            Chat_C_Message chatMessage = messages.get(0);
            boolean startDownloadService = false;

            CH_Message chMessage = messageDao.getByString(
                    new CH_Message_Sql_005(
                            chatMessage.getMsg_prefix(),
                            chatMessage.getMsg_code()
                    ).toSqlQuery()
            );
            //
            if (chMessage != null) {
                if (chMessage.getMsg_prefix() > -1) {
                    chMessage.setMsg_date(chatMessage.getMsg_date());
                } else {
                    chMessage = chatMessage.toCH_MessageObj();
                    chMessage.setTmp(0);
                    //Verifica se precisa iniciar serviço de download
                    if (!startDownloadService && chMessage.getMsg_obj().contains(CONTAINS_IMAGE_MSG)) {
                        startDownloadService = true;
                    }
                }
                //
                if (chatMessage.getDelivered() == 0) {
                    //Atualiza valor de dado entregue
                    chMessage.setDelivered(1);
                    chMessage.setDelivered_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                    //Se msg é minha, mas é msg "sistemica", não seta como lido
                    if (chMessage.getMsg_type().equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_TEXT) ||
                            chMessage.getMsg_type().equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_IMAGE)
                            ) {
                        chMessage.setRead(1);
                        chMessage.setRead_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                    }
                    chMessage.setStatus_update(1);//Sim é necessário
                    chMessage.setMsg_token(ToolBox_Inf.chatNextMSGToken(getApplicationContext()));
                    //Monta obj para chamar sDelivered
                    Chat_S_Delivered sDelivered = new Chat_S_Delivered();
                    //
                    sDelivered.setMsg_prefix(chMessage.getMsg_prefix());
                    sDelivered.setMsg_code(chMessage.getMsg_code());
                    sDelivered.setRead(1);
                    //
                    sDeliveredList.add(gson.toJsonTree(sDelivered));
                }
                /*
                 *
                 * TRATAR SE VEI READ 0 E JA ESTA READ 1 NO BANCO ?!
                 *
                 * */
                //
                messageDao.addUpdate(chMessage);
                //
               /*
               TESTE DE ABRIR MSG_OBJ VIA CLASSE
               INSISTIR NA IDEIA DEPOIS
               Chat_Message_Obj messageObj = gson.fromJson(
                        ToolBox_Inf.getRoomObjJsonParam(chMessage.getMsg_obj()),
                        Chat_Message_Obj.class
                        );
                //
                messageObj.dataConverter();*/
                //
                if (!chMessage.getMsg_type().equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_TEXT) &&
                        !chMessage.getMsg_type().equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_IMAGE)
                        ) {
                    ToolBox_Inf.showChatNotification(getApplicationContext(), Constant.CHAT_NOTIFICATION_TYPE_MESSAGE, null, false);
                }
                //
                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG);
                //
                if (sDeliveredList.size() > 0) {
                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
                    singletonWebSocket.attemptToDeliveryMessage(
                            ToolBox_Inf.setWebSocketJsonParam(sDeliveredList)
                    );
                }
                //
                if (startDownloadService) {
                    startDownloadService();
                }
            }

        } else {
            JsonArray sDeliveredList = new JsonArray();
            JsonArray sReadList = new JsonArray();
            SingletonWebSocket singletonWebSocket = null;
            //
            boolean startDownloadService = false;
            boolean hasNewMsg = false;
            boolean showNotification = true;
            boolean startFormApService = false;
            //Transforma list de objs recebido(Chat_C_Message)
            //em objs do banco(CH_Message)
            ArrayList<CH_Message> chMessages = Chat_C_Message.toCH_MessageList(messages);
            //Se ao menos uma msg é uma imagem, dispara serviço de download.
            for (CH_Message ch_message : chMessages) {
                //Verifica se existe a room da msg, se não houver, ignora
                if (!msgRoomExists(ch_message.getRoom_code())) {
                    continue;
                }
                //
                if (ws_event.equals(Constant.CHAT_EVENT_C_MESSAGE_FCM)) {
                    ch_message.setMsg_pk(String.valueOf(ch_message.getMsg_prefix() + "_" + ToolBox_Inf.lPad(20, ch_message.getMsg_code())));
                    ch_message.setDelivered(0);
                    //ch_message.setDelivered_date("");
                    ch_message.setAll_delivered(0);
                    ch_message.setRead(0);
                    //ch_message.setRead_date("");
                    ch_message.setAll_read(0);
                    ch_message.setStatus_update(1);
                    ch_message.setMsg_token(ToolBox_Inf.chatNextMSGToken(getApplicationContext()));
                } else {
                    if (singletonWebSocket == null) {
                        singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
                    }
                    ///
                    if (ws_event.equals(Constant.CHAT_EVENT_C_MESSAGE)
                            && ch_message.getRoom_code().equals(Act035_Main.mRoom_code)
                            ) {
                        showNotification = false;
                    }
                }
                //
                CH_Message dbMessage =
                        messageDao.getByString(
                                new CH_Message_Sql_005(
                                        ch_message.getMsg_prefix(),
                                        ch_message.getMsg_code()
                                ).toSqlQuery()
                        );
                //Verifica se a necessidade de notificação
                if (singletonWebSocket != null && !singletonWebSocket.isShow_notification() && dbMessage.getTmp() < 0) {
                    singletonWebSocket.setShow_notification(true);
                }
                //Verifica se precisa iniciar serviço de download
                if (!startDownloadService && ch_message.getMsg_obj().contains(CONTAINS_IMAGE_MSG)) {
                    startDownloadService = true;
                }
                //CORREÇÃO DA ATUALIZAÇÃO DE TMP PARA MSG JA EXISTENTE
                //Se msg existe no banco, atualiza valor com o tmp do banco
                if (dbMessage.getTmp() > 0) {
                    ch_message.setTmp(dbMessage.getTmp());
                } else {
                    ch_message.setTmp(0);
                    hasNewMsg = true;
                    //Correção paleativa até o server começar a enviar FCM
                    //pra mesagens minhas quando eu estou ativo na web e o app não.
                    if (
                            ws_event.equals(Constant.CHAT_EVENT_C_HISTORICAL_MESSAGES)
                                    && ToolBox_Con.getPreference_User_Code(getApplicationContext()).equals(String.valueOf(ch_message.getUser_code()))
                                    && ch_message.getDelivered() == 1
                            ) {
                        ch_message.setStatus_update(1);
                        ch_message.setMsg_token(ToolBox_Inf.chatNextMSGToken(getApplicationContext()));
                    }
                }
                //
                if (ch_message.getDelivered() == 0 && !ws_event.equals(Constant.CHAT_EVENT_C_MESSAGE_FCM)) {
                    //Atualiza valor de dado entregue
                    ch_message.setDelivered(1);
                    ch_message.setDelivered_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                    ch_message.setStatus_update(1);
                    ch_message.setMsg_token(ToolBox_Inf.chatNextMSGToken(getApplicationContext()));
                    //Monta obj para chamar sDelivered
                    Chat_S_Delivered sDelivered = new Chat_S_Delivered();
                    //
                    sDelivered.setMsg_prefix(ch_message.getMsg_prefix());
                    sDelivered.setMsg_code(ch_message.getMsg_code());
                    sDelivered.setRead(0);
                    //
                    sDeliveredList.add(gson.toJsonTree(sDelivered));
                }
                //Se o Read do server esta como 0, mas o Read local é 1
                //não atualiza banco de dados e chama evento de leitura
                if (dbMessage.getRead() == 1 && ch_message.getRead() == 0) {
                    //Seta valores do banco no obj
                    ch_message.setRead(dbMessage.getRead());
                    ch_message.setRead_date(dbMessage.getRead_date());
                    //Cria obj para chamar evento de sRead
                    Chat_S_Read sRead = new Chat_S_Read();
                    //
                    sRead.setMsg_prefix(ch_message.getMsg_prefix());
                    sRead.setMsg_code(ch_message.getMsg_code());
                    //sRead.setRoom_code(ch_message.getRoom_code());
                    //
                    sReadList.add(gson.toJsonTree(sRead));
                }
                //Correção de Delivery_all e Read_all não atualizarem tela quando
                //essas flags são alterada via cHistoricalMessage
                if (
                        (dbMessage.getAll_delivered() == 0 && ch_message.getAll_delivered() == 1)
                                || (dbMessage.getAll_read() == 0 && ch_message.getAll_read() == 1)
                        ) {
                    ch_message.setStatus_update(1);
                    ch_message.setMsg_token(ToolBox_Inf.chatNextMSGToken(getApplicationContext()));
                }
                //Se msg for do tipo form Ap, verifica se deve ser criado texto para insert posterior
                if (ch_message.getMsg_type().equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_FORM_AP)) {
                    try {
                        JSONObject jsonObject = new JSONObject(ch_message.getMsg_obj());
                        JSONObject msg = jsonObject.getJSONObject("message");
                        JSONObject data = msg.getJSONObject("data");
                        //
                        Chat_Message_Obj_Form_Ap objFormAp = gson.fromJson(
                                ToolBox_Inf.getRoomObjJsonParam(
                                        //  form_ap.toString()
                                        data.toString()
                                ),
                                Chat_Message_Obj_Form_Ap.class
                        );
                        //Se o form ap possui data, verifica se deve ser inserido
                        if (objFormAp != null && objFormAp.getAp_when() != null) {
                            //Se o usr for o "Quem" do form ap, cria arquivo para inserção.
                            if (ToolBox_Con.getPreference_User_Code(getApplicationContext()).equalsIgnoreCase(String.valueOf(objFormAp.getAp_who()))) {
                                //cria arquivo com o form ap
                                createMsgsFile(data.toString(),Constant.CHAT_MESSAGE_TYPE_FORM_AP);
                                //Se false, seta variavel de inicio do serviço de form ap para true.
                                if(!startFormApService){
                                    startFormApService = true;
                                }
                            } else {
                                CH_RoomDao chRoomDao = new CH_RoomDao(getApplicationContext());
                                //
                                HMAux auxRoom = chRoomDao.getByStringHM(
                                        new CH_Room_Sql_013(
                                                objFormAp.getPk()
                                        ).toSqlQuery()
                                );
                                //
                                //Se o usr tem join com a sala do form ap, cria arquivo para inserção.
                                if(auxRoom != null && auxRoom.containsKey(CH_RoomDao.ROOM_CODE) && auxRoom.get(CH_RoomDao.ROOM_CODE) != null ){
                                    //cria arquivo com o form ap
                                    createMsgsFile(data.toString(),Constant.CHAT_MESSAGE_TYPE_FORM_AP);
                                    //Se false, seta variavel de inicio do serviço de form ap para true.
                                    if(!startFormApService){
                                        startFormApService = true;
                                    }
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //Salva lista no banco de dados.
                messageDao.addUpdate(ch_message);
            }
            //
            if (msgListFile != null) {
                msgListFile.delete();
            }
            //
            if (startDownloadService) {
                startDownloadService();
            }
            //
            if(startFormApService){
                startFormApService();
            }
            //
            if (ws_event.equals(Constant.CHAT_EVENT_C_MESSAGE_FCM)) {
                if (hasNewMsg) {
                    ToolBox_Inf.showChatNotification(getApplicationContext(), Constant.CHAT_NOTIFICATION_TYPE_MESSAGE, null, true);
                }
                //
                Intent postDeliveredIntent = new Intent(getApplicationContext(), WBR_Delivered.class);
                Bundle bundle = new Bundle();
                // bundle.putString(Constant.CHAT_WS_JSON_PARAM,ToolBox_Inf.setWebSocketJsonParam(sDeliveredList));
                postDeliveredIntent.putExtras(bundle);
                getApplicationContext().sendBroadcast(postDeliveredIntent);
                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_EVENT_C_MESSAGE_FCM);

                return;
            }
            //
            if (sDeliveredList.size() > 0 && !ws_event.equals(Constant.CHAT_EVENT_C_MESSAGE_FCM)) {
                singletonWebSocket.attemptToDeliveryMessage(
                        ToolBox_Inf.setWebSocketJsonParam(sDeliveredList)
                );
            }
            //
            if (sReadList.size() > 0) {
                singletonWebSocket.attemptToReadMessage(
                        ToolBox_Inf.setWebSocketJsonParam(sReadList)
                );
            }
            //
            //Se diferente de null, o serviço foi chamado do cHistoricalMessage Action Login
            if (messageTmpFile != null) {
                //Se maior que 0, existe arquivo para ser processado.
                //Se não houver arquivo, tenta enviar msg offline
                if (messageTmpFile.length() > 0) {
                    startCMessageTmpService(messageTmpFile, messageIncrement);
                } else {
                    //Incrementa contador
                    singletonWebSocket.updateCounterMsg(messageIncrement);
                    //Verifica se todas as msg foram processadas
                    //Se foram, reseta contador, dispara broadcast e envia offlines
                    if (singletonWebSocket.areAllMsgProcessed()) {
                        if (singletonWebSocket.isShow_notification()) {
                            ToolBox_Inf.showChatNotification(getApplicationContext(), Constant.CHAT_NOTIFICATION_TYPE_MESSAGE, null, false);
                        }
                        singletonWebSocket.resetProcessMsgCounter();
                        //
                        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG);
                        //
                        //singletonWebSocket.attempSendOfflineMessages();
                        singletonWebSocket.attempSendOfflineMessagesV2();
                    }
                }
            } else {
                if (ws_event.equalsIgnoreCase(Constant.CHAT_EVENT_C_HISTORICAL_MESSAGES)) {
                    ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_SCROLL_UP);
                } else {
                    if (singletonWebSocket.isShow_notification() && showNotification) {
                        ToolBox_Inf.showChatNotification(getApplicationContext(), Constant.CHAT_NOTIFICATION_TYPE_MESSAGE, null, false);
                        //
                        singletonWebSocket.setShow_notification(false);
                    }
                    //
                    ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG);
                }
            }
        }
    }


    private boolean msgRoomExists(String room_code) {
        CH_RoomDao roomDao = new CH_RoomDao(getApplicationContext());
        CH_Room ch_room = roomDao.getByString(
                new CH_Room_Sql_001(
                        room_code
                ).toSqlQuery()
        );
        //
        if (ch_room != null && ch_room.getRoom_code() != null) {
            return true;
        }
        return false;
    }

    private void startCMessageTmpService(String messageTmpFile, long messageIncrement) {
        Intent cMessageTmpIntent = new Intent(getApplicationContext(), WBR_C_Message_Tmp.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CHAT_WS_JSON_PARAM, messageTmpFile);
        bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_C_HISTORICAL_MESSAGES);
        bundle.putLong(Constant.CHAT_WS_MSG_COUNTER_PARAM, messageIncrement);
        cMessageTmpIntent.putExtras(bundle);
        //
        getApplicationContext().sendBroadcast(cMessageTmpIntent);
    }

    private void startDownloadService() {
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        //
        getApplicationContext().sendBroadcast(mIntentPIC);

    }

    private void startFormApService() {
        Intent mIntentFormAp = new Intent(getApplicationContext(), WBR_Process_Form_Ap.class);
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

}
