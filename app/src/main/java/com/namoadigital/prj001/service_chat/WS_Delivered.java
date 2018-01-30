package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.Chat_Post_Delivered;
import com.namoadigital.prj001.receiver_chat.WBR_Delivered;
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
            String json_param = bundle.getString(Constant.CHAT_WS_JSON_PARAM);
            processDelivered(json_param);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Delivered.completeWakefulIntent(intent);
        }

    }

    private void processDelivered(String json_param) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().create();
        EV_User_CustomerDao customerDao = new EV_User_CustomerDao(getApplicationContext());
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
        //
        Chat_Post_Delivered env = new Chat_Post_Delivered();
        //
        env.setJson(json_param);
        env.setSession_app(sessionList);
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_CHAT_ROOM_INFO,
                gson.toJson(env)
        );
        //
        if(resultado.contains("OK")){
           String ret = "OK";
        }
        //
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM_INFO);
    }

}
