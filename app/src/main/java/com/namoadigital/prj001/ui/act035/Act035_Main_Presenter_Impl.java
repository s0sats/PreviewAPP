package com.namoadigital.prj001.ui.act035;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_FileDao;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_File;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_S_Message;
import com.namoadigital.prj001.receiver_chat.WBR_Upload_Img_Chat;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.Sql_Act035_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act035_Main_Presenter_Impl implements Act035_Main_Presenter {

    private Context context;
    private Act035_Main_View mView;
    private HMAux hmAux_Trans;


    private CH_MessageDao ch_messageDao;


    public Act035_Main_Presenter_Impl(Context context, Act035_Main_View mView, HMAux hmAux_Trans, CH_MessageDao ch_messageDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.ch_messageDao = ch_messageDao;
    }

    @Override
    public void setData(String mRoom_code) {
        mView.reloadMessages(
                (ArrayList<HMAux>) ch_messageDao.query_HM(
                        new Sql_Act035_001(
                                mRoom_code
                        ).toSqlQuery()

                ));
    }

    @Override
    public void sendMessage(String mRoom_code, String message, String imagem) {

        CH_Message chMessage = new CH_Message();
        chMessage.setMsg_prefix(Integer.parseInt(ToolBox_Inf.yearMonthPrefix()));
        chMessage.setMsg_code(0);
        chMessage.setTmp(ToolBox_Inf.chatNextMSGCode(context));
        //
        chMessage.setRoom_code(mRoom_code);
        chMessage.setMsg_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));

        try {

            JSONObject jsonObject = new JSONObject();

            if (imagem.isEmpty()) {
                jsonObject.put("type", "TEXT");
                jsonObject.put("data", message);
            } else {
                jsonObject.put("type", "IMAGE");
                //jsonObject.put("data", imagem);
                jsonObject.put("data", "");
            }

            JSONObject jMessage = new JSONObject();
            jMessage.put("message", jsonObject);

            chMessage.setMsg_obj(jMessage.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        chMessage.setMessage_image_local(imagem);
        chMessage.setMsg_origin("APP");
        chMessage.setDelivered(0);
        chMessage.setDelivered_date(null);
        chMessage.setRead(0);
        chMessage.setRead_date(null);
        chMessage.setMsg_pk(null);
        chMessage.setUser_code(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        chMessage.setUser_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        //
        ch_messageDao.addUpdateTmp(chMessage);
        //
        setData(mRoom_code);
        //
        mView.cleanTextControl();
        //
        enviarMensagemServer(mRoom_code, message, chMessage);
    }

    private void enviarMensagemServer(String mRoom_code, String message, CH_Message chMessage) {
        Chat_S_Message s_message = new Chat_S_Message();
        //
        s_message.setRoom_code(mRoom_code);
        if (message.isEmpty()) {
            s_message.setType(Constant.CHAT_MESSAGE_TYPE_IMAGE);
        } else {
            s_message.setType(Constant.CHAT_MESSAGE_TYPE_TEXT);
        }
        s_message.setData(message);
        s_message.setTmp(chMessage.getTmp());
        //
        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);

        Gson gson = new GsonBuilder().serializeNulls().create();

        singletonWebSocket.attemptSendMessages(gson.toJson(s_message));
    }

    @Override
    public void onOnItemClicked(HMAux item) {
        if (!item.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).equalsIgnoreCase("")) {
            File file = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + item.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL));
            if (file.exists()) {
                mView.callCamera(-1, 1, item.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL), false, false);
            }
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }

    @Override
    public void uploadFile(String sCh_file) {
        CH_FileDao chFileDao = new CH_FileDao(context);

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

    @Override
    public void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img_Chat.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
