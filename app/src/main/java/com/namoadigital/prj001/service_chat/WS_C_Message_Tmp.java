package com.namoadigital.prj001.service_chat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_FileDao;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_File;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_C_Message_Tmp;
import com.namoadigital.prj001.model.Chat_S_Message_Tmp;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message_Tmp;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;

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
            String ws_event = bundle.getString(Constant.CHAT_WS_EVENT_PARAM, "");
            long messageIncrement = bundle.getLong(Constant.CHAT_WS_MSG_COUNTER_PARAM, 0);

            if (json_param == null) {
                throw new Exception();
            }

            processC_Message_Tmp(json_param,ws_event,messageIncrement);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_C_Message_Tmp.completeWakefulIntent(intent);
        }

    }

    private void processC_Message_Tmp(String json_param, String ws_event, long messageIncrement) throws JSONException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<Chat_C_Message_Tmp> messageTmpList = new ArrayList<>();
        File msgTmpListFile = null;
        //
        if(ws_event.equalsIgnoreCase(Constant.CHAT_EVENT_C_HISTORICAL_MESSAGES)){
            msgTmpListFile = new File(json_param);
            //
            messageTmpList = gson.fromJson(
                    ToolBox_Inf.getContents(msgTmpListFile),
                    new TypeToken<ArrayList<Chat_C_Message_Tmp>>() {
                    }.getType());

        }else {
            messageTmpList =
                    gson.fromJson(
                            json_param,
                            new TypeToken<ArrayList<Chat_C_Message_Tmp>>() {
                            }.getType());
        }
        //
        if (messageTmpList != null) {
            boolean hasImage = false;
            for (Chat_C_Message_Tmp messageTmp : messageTmpList) {
                CH_Message ch_message =
                        messageDao.getByString(
                                new CH_Message_Sql_003(
                                        messageTmp.getMsg_tmp()
                                ).toSqlQuery()
                        );

                String oldNameFile = null;

                if (ch_message != null && ch_message.getMsg_prefix() > -1) {
                    ch_message.setMsg_prefix(messageTmp.getMsg_prefix());
                    ch_message.setMsg_code(messageTmp.getMsg_code());
                    ch_message.setMsg_pk(String.valueOf(messageTmp.getMsg_prefix() + "_" + ToolBox_Inf.lPad(20, messageTmp.getMsg_code())));
                    if(ch_message.getMsg_obj().startsWith(Constant.CHAT_START_WITH_IMAGE_MSG)) {
                        oldNameFile = ch_message.getMessage_image_local();
                        ch_message.setMessage_image_local(ch_message.getMsg_prefix() + "." + ch_message.getMsg_code() + ".jpg");
                    }
                    messageDao.addUpdateTmp(ch_message);
                }
                //
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());

                /*JSONObject msg_obj = new JSONObject(ch_message.getMsg_obj());
                JSONObject msg_obj_content = (JSONObject) msg_obj.get("message");
                String msg_obj_type = (String) msg_obj_content.get("type");
                String msg_obj_data = (String) msg_obj_content.get("data");*/
                HMAux msgObjAux = ToolBox_Inf.getChatMsgContent(ch_message.getMsg_obj());
                String msg_obj_type = msgObjAux.get("type");
                String msg_obj_data = msgObjAux.get("data");
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
                } else {
                    if(!hasImage){
                        hasImage = true;
                    }
                    //
                    String curr_name = oldNameFile; //ch_message.getMessage_image_local();
                    String new_name = ch_message.getMessage_image_local(); //ch_message.getMsg_prefix() + "." + ch_message.getMsg_code() + ".jpg";
                    //
                    renameImage(Constant.CACHE_PATH_PHOTO, curr_name, new_name);
                    //
                    addImageToUploadList(new_name);
                    //
                    curr_name = curr_name.replace(".jpg", "_thumb.jpg");
                    new_name = new_name.replace(".jpg", "_thumb.jpg");
                    //
                    renameImage(Constant.THU_PATH, curr_name, new_name);
                }

                /*HMAux hmAux = new HMAux();
                hmAux.put(CH_MessageDao.MSG_PREFIX, String.valueOf(ch_message.getMsg_prefix()));
                hmAux.put(CH_MessageDao.TMP, String.valueOf(ch_message.getMsg_tmp()));*/
                //
            }
            //
            if(hasImage){
                startUpload(getApplicationContext());
            }

            //
            if(msgTmpListFile != null){
                msgTmpListFile.delete();
                //
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(getApplicationContext());
                //
                singletonWebSocket.updateCounterMsg(messageIncrement);
                //Verifica se todas as msg foram processadas
                //Se foram, reseta contador, dispara broadcast e envia offlines
                if(singletonWebSocket.areAllMsgProcessed()){
                    if(singletonWebSocket.isShow_notification()){
                        ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_TMP);
                    }
                    singletonWebSocket.resetProcessMsgCounter();
                    //
                    //singletonWebSocket.attempSendOfflineMessages();
                    singletonWebSocket.attempSendOfflineMessagesV2();
                }
            }else{
                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_TMP);
            }
        }
    }

    private boolean renameImage(String path, String curr_name, String new_name) {
        File from = new File(path + "/", curr_name);
        File to = new File(path + "/", new_name);
        //
        return from.renameTo(to);
    }


    private void addImageToUploadList(String sCh_file) {
        CH_FileDao chFileDao = new CH_FileDao(getApplicationContext());

        CH_File chFile = null;

        if (sCh_file.endsWith(".jpg")) {
            File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sCh_file);
            if (sFile.exists()) {
                chFile = new CH_File();
                chFile.setFile_code(sCh_file.replace(".jpg", ""));
                chFile.setFile_path(sCh_file);
                chFile.setFile_path_new(sCh_file);
                chFile.setFile_status("OPENED");
                chFile.setFile_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            }
        }

        chFileDao.addUpdate(chFile);
    }
    //

    private void startUpload(Context context) {
       /* Intent mIntent = new Intent(context, WBR_Upload_Img_Chat.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);*/

        //LUCHE - 27/01/2021
        //Substituido esquema antigo de service / receiver pelo agendamento do Worker
        ToolBox_Inf.scheduleUploadImgChat(getApplicationContext());
    }

}
