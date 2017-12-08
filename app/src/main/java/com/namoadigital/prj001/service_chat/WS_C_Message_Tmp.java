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
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_C_Message_Tmp;
import com.namoadigital.prj001.model.Chat_S_Message_Tmp;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

            String json_param = bundle.getString(Constant.CHAT_WS_JSON_PARAM, null);

            if (json_param == null) {
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

    private void processC_Message_Tmp(String json_param) throws JSONException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        ArrayList<Chat_C_Message_Tmp> messageTmpList =
                gson.fromJson(
                        json_param,
                        new TypeToken<ArrayList<Chat_C_Message_Tmp>>() {
                        }.getType());
        //
        if (messageTmpList != null) {

            for (Chat_C_Message_Tmp messageTmp : messageTmpList) {
                CH_Message ch_message =
                        messageDao.getByString(
                                new CH_Message_Sql_003(
                                        messageTmp.getMsg_prefix(),
                                        messageTmp.getTmp()
                                ).toSqlQuery()
                        );

                String oldNameFile = null;

                if (ch_message != null && ch_message.getMsg_prefix() > -1) {
                    ch_message.setMsg_code(messageTmp.getMsg_code());
                    ch_message.setMsg_pk(String.valueOf(messageTmp.getMsg_prefix() + "_" + ToolBox_Inf.lPad(20, messageTmp.getMsg_code())));

                    oldNameFile = ch_message.getMessage_image_local();
                    ch_message.setMessage_image_local(ch_message.getMsg_prefix() + "." + ch_message.getMsg_code() + ".jpg");

                    messageDao.addUpdateTmp(ch_message);
                }
                //
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());

                JSONObject msg_obj = new JSONObject(ch_message.getMsg_obj());
                JSONObject msg_obj_content = (JSONObject) msg_obj.get("message");
                String msg_obj_type = (String) msg_obj_content.get("type");
                String msg_obj_data = (String) msg_obj_content.get("data");
                //
                if (!msg_obj_type.equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_IMAGE)) {
                    //
                    Chat_S_Message_Tmp sMessageTmp = new Chat_S_Message_Tmp();
                    sMessageTmp.setMsg_prefix(messageTmp.getMsg_prefix());
                    sMessageTmp.setMsg_code(messageTmp.getMsg_code());
                    //
                    singletonWebSocket.attemptSendMessageTmp(
                            ToolBox_Inf.setWebSocketJsonParam(sMessageTmp)
                    );
                    //Notifica telas
                    ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG);
                } else {
                    //
                    String curr_name = oldNameFile; //ch_message.getMessage_image_local();
                    String new_name = ch_message.getMessage_image_local(); //ch_message.getMsg_prefix() + "." + ch_message.getMsg_code() + ".jpg";
                    //
                    renameImage(Constant.CACHE_PATH_PHOTO, curr_name, new_name);
                    //
                    curr_name = curr_name.replace(".jpg", "_thumb.jpg");
                    new_name = new_name.replace(".jpg", "_thumb.jpg");
                    //
                    renameImage(Constant.THU_PATH, curr_name, new_name);

                    // chamar o upload da Imagem com os códigos atualizados
                    // para incluir a imagem na lista com nome / novo nome iguais

                    HMAux hmAux = new HMAux();
                    hmAux.put(CH_MessageDao.MSG_PREFIX, String.valueOf(ch_message.getMsg_prefix()));
                    hmAux.put(CH_MessageDao.TMP, String.valueOf(ch_message.getTmp()));
                    //
                    ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_IMAGE, hmAux);
                }
            }
        }
    }

    private boolean renameImage(String path, String curr_name, String new_name) {
        File from = new File(path + "/", curr_name);
        File to = new File(path + "/", new_name);
        //
        return from.renameTo(to);
    }
}
