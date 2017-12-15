package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_C_Message;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message;
import com.namoadigital.prj001.sql.CH_Message_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by d.luche on 01/12/2017.
 */

public class WS_C_Message extends IntentService {

    private final String START_WITH_IMAGE_MSG = "{\"message\":{\"type\":\"IMAGE\",";

    private CH_MessageDao messageDao;


    public WS_C_Message() {
        super("WS_C_Message");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();


        try {
            messageDao = new CH_MessageDao(getApplicationContext());
            String json_param = bundle.getString(Constant.CHAT_WS_JSON_PARAM);
            String ws_event = bundle.getString(Constant.CHAT_WS_EVENT_PARAM, "");

            processC_Message(json_param, ws_event);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_C_Message.completeWakefulIntent(intent);
        }

    }

    private void processC_Message(String json_param, String ws_event) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        HMAux msgParam = new HMAux();
        //
        ArrayList<Chat_C_Message> messages =
                gson.fromJson(
                        json_param,
                        new TypeToken<ArrayList<Chat_C_Message>>() {
                        }.getType());
        //Se é um "cMessage" de uma msg minha,
        //atualiza data da msg pra a data do server.
        if (ws_event.equalsIgnoreCase(Constant.CHAT_EVENT_C_MESSAGE)
                && ToolBox_Con.getPreference_User_Code(getApplicationContext())
                .equalsIgnoreCase(String.valueOf(messages.get(0).getUser_code()))
                ) {

            CH_Message chMessage = messageDao.getByString(
                    new CH_Message_Sql_005(
                            messages.get(0).getMsg_prefix(),
                            messages.get(0).getMsg_code()
                    ).toSqlQuery()
            );
            //
            if (chMessage != null && chMessage.getMsg_prefix() > -1) {
                chMessage.setMsg_date(messages.get(0).getMsg_date());
                messageDao.addUpdate(chMessage);
            }
            //
            msgParam.put(Constant.CHAT_BR_PARAM, gson.toJson(chMessage));
            //
            ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG, msgParam);

        } else {
            //Transforma list de objs recebido(Chat_C_Message)
            //em objs do banco(CH_Message)
            ArrayList<CH_Message> chMessages = Chat_C_Message.toCH_MessageList(messages);
            //Salva lista no banco de dados.
            messageDao.addUpdate(chMessages, false);
            //
            boolean startDownload = false;
            //Se ao menos uma msg é uma imagem, dispara serviço de download.
            for (CH_Message ch_message : chMessages) {
                //
                if (!startDownload && ch_message.getMsg_obj().startsWith(START_WITH_IMAGE_MSG)) {
                    startDownload = true;
                    startDownloadService();
                }
                msgParam.put(Constant.CHAT_BR_PARAM, gson.toJson(ch_message));
                //
                //ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG, msgParam);
                //ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG);
                //
                Log.d("Chat", ch_message.getMsg_pk());
            }

            //ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG, msgParam);
        }
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG);
    }

    private void startDownloadService() {
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        //
        getApplicationContext().sendBroadcast(mIntentPIC);

    }
}