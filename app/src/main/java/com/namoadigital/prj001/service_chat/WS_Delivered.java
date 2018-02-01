package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_Post_Delivered;
import com.namoadigital.prj001.model.Chat_S_Delivered;
import com.namoadigital.prj001.receiver_chat.WBR_Delivered;
import com.namoadigital.prj001.sql.CH_Message_Sql_021;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_007;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_Delivered extends IntentService {

    public WS_Delivered() {
        super("WS_Delivered");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            //String json_param = bundle.getString(Constant.CHAT_WS_JSON_PARAM);
            processDelivered();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Delivered.completeWakefulIntent(intent);
        }

    }

    private void processDelivered() throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().create();
        EV_User_CustomerDao customerDao = new EV_User_CustomerDao(getApplicationContext());
        CH_MessageDao messageDao = new CH_MessageDao(getApplicationContext());
        JsonArray sDeliveredList = new JsonArray();
        String sessionList = "";
        //Seleciona todos customers con sessão ativa e que tem acesso ao chat.
        ArrayList<HMAux> chatSessionCustomers = (ArrayList<HMAux>) customerDao.query_HM(
                new EV_User_Customer_Sql_007(
                        ToolBox_Con.getPreference_User_Code(getApplicationContext())
                ).toSqlQuery()
        );
        //
        if (chatSessionCustomers != null && chatSessionCustomers.size() > 0) {
            for (int i = 0; i < chatSessionCustomers.size(); i++) {
                sessionList += chatSessionCustomers.get(i).get(EV_User_CustomerDao.SESSION_APP) + "|";
            }
            sessionList = sessionList.substring(0, sessionList.length() - 1);
        }
        //Seleciona msg delivered 0
        ArrayList<CH_Message> dbDeliveryList = (ArrayList<CH_Message>) messageDao.query(
                new CH_Message_Sql_021().toSqlQuery()
        );
        //
        if(dbDeliveryList != null  && dbDeliveryList.size() > 0){
            for(CH_Message chMessage : dbDeliveryList){
                //Monta obj para chamar sDelivered
                Chat_S_Delivered sDelivered = new Chat_S_Delivered();
                //
                sDelivered.setMsg_prefix(chMessage.getMsg_prefix());
                sDelivered.setMsg_code(chMessage.getMsg_code());
                sDelivered.setRead(0);
                //
                sDeliveredList.add(gson.toJsonTree(sDelivered));
            }

        }
        //
        if(sDeliveredList.size() == 0){
            Log.d("ChatEvent", "sDeliveredList do post é 0, sai sem processar nada");
            return;
        }
        //
        Chat_Post_Delivered env = new Chat_Post_Delivered();
        //
        env.setJson(ToolBox_Inf.setWebSocketJsonParam(sDeliveredList));
        env.setSession_app(sessionList);
        Log.d("ChatEvent", "Envio do post sDelivered(FCM): " + gson.toJson(env));
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_CHAT_POST_DELIVERED,
                gson.toJson(env)
        );
        //
        Log.d("ChatEvent", "Retorno do post sDelivered(FCM): " + resultado);
        //
        DeliveredRetObj rec = gson.fromJson(
                resultado,
                DeliveredRetObj.class
        );
        //
        if (rec.getObj().equals("OK")) {
            Log.d("ChatEvent", "Retornou OK, atualiza msgs no banco, setando delivered pra 1");
            for(CH_Message chMessage : dbDeliveryList){
                //Atualiza valor de dado entregue
                chMessage.setDelivered(1);
                chMessage.setDelivered_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                chMessage.setStatus_update(1);
                chMessage.setMsg_token(ToolBox_Inf.chatNextMSGToken(getApplicationContext()));
                //
                messageDao.addUpdate(chMessage);
            }
        }
    }

    private class DeliveredRetObj {
        String obj;

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }
    }

}
