package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.Chat_C_All_Delivered_Read;
import com.namoadigital.prj001.receiver_chat.WBR_C_All_Delivered;
import com.namoadigital.prj001.sql.CH_Message_Sql_010;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by d.luche on 30/11/2017.
 */

public class WS_C_All_Delivered extends IntentService {

    public WS_C_All_Delivered() {
        super("WS_C_All_Delivered");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String json_param = bundle.getString(Constant.CHAT_WS_JSON_PARAM);

            processC_All_Delivered(json_param);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_C_All_Delivered.completeWakefulIntent(intent);
        }

    }

    private void processC_All_Delivered(String json_param) {

        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        ArrayList<Chat_C_All_Delivered_Read> allDelivereds =
                gson.fromJson(
                        json_param,
                        new TypeToken<ArrayList<Chat_C_All_Delivered_Read>>() {
                        }.getType());
        //
        CH_MessageDao messageDao = new CH_MessageDao(getApplicationContext());
        //
        for (Chat_C_All_Delivered_Read delivered : allDelivereds) {
            messageDao.addUpdate(
                    new CH_Message_Sql_010(
                            delivered.getMsg_prefix(),
                            delivered.getMsg_code(),
                            ToolBox_Inf.chatNextMSGToken(getApplicationContext()),
                            CH_Message_Sql_010.FLAG_ALL_DELIVERED
                    ).toSqlQuery()
            );

        }
        //
        //ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_ALL_DELIVERED);
        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG);
    }

}
