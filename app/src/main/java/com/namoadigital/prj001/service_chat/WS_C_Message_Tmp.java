package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_C_Message_Tmp;
import com.namoadigital.prj001.model.Chat_S_Message_Tmp;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by d.luche on 01/12/2017.
 */

public class WS_C_Message_Tmp extends IntentService {

    private CH_MessageDao messageDao;

    public WS_C_Message_Tmp() {
        super("WS_C_Message_Tmp");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            messageDao = new CH_MessageDao(getApplicationContext());

            String json_param = bundle.getString(Constant.CHAT_WS_JSON_PARAM,null);

            if(json_param == null){
                throw new Exception();
            }

            processC_Message_Tmp(json_param);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_C_Message.completeWakefulIntent(intent);
        }

    }

    private void processC_Message_Tmp(String json_param) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        ArrayList<Chat_C_Message_Tmp> messageTmpList =
                gson.fromJson(
                        json_param,
                        new TypeToken<ArrayList<Chat_C_Message_Tmp>>() {
                        }.getType());
        //
        if(messageTmpList != null ){

            for (Chat_C_Message_Tmp messageTmp :messageTmpList) {
                CH_Message ch_message =
                        messageDao.getByString(
                                new CH_Message_Sql_003(
                                        messageTmp.getMsg_prefix(),
                                        messageTmp.getTmp()
                                ).toSqlQuery()
                        );
                if(ch_message != null && ch_message.getMsg_prefix() > -1){
                   ch_message.setMsg_code(messageTmp.getMsg_code());
                   ch_message.setMsg_pk(String.valueOf(messageTmp.getMsg_prefix() +"_" + ToolBox_Inf.lPad(20,messageTmp.getMsg_code())));
                   messageDao.addUpdateTmp(ch_message);
                }
                //
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
                //
                Chat_S_Message_Tmp sMessageTmp = new Chat_S_Message_Tmp();
                sMessageTmp.setMsg_prefix(messageTmp.getMsg_prefix());
                sMessageTmp.setMsg_code(messageTmp.getMsg_code());
                //
                singletonWebSocket.attemptSendMessageTmp(
                        ToolBox_Inf.setWebSocketJsonParam(sMessageTmp)
                );
            }

        }
        //Notifica telas
        ToolBox_Inf.sendBRChat(getApplicationContext(),Constant.CHAT_BR_TYPE_MSG);
    }
}
