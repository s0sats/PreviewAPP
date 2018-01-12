package com.namoadigital.prj001.ui.act035;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_S_Historical_Message;
import com.namoadigital.prj001.model.Chat_S_Message;
import com.namoadigital.prj001.model.Chat_S_Read;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_016;
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
    public void setData(String mRoom_code, String offSet) {
        mView.reloadMessages(
                (ArrayList<HMAux>) ch_messageDao.query_HM(
                        new Sql_Act035_001(
                                mRoom_code,
                                offSet
                        ).toSqlQuery()

                ));
    }

    @Override
    public void updateReadStatus(ArrayList<HMAux> hmAuxs) {
        updateReadStatus(hmAuxs, "");
    }

    @Override
    public void updateReadStatus(ArrayList<HMAux> hmAuxs, String type) {
        CH_MessageDao chMessageDao = new CH_MessageDao(context);
        //
        String sRead = "1";
        String sRead_Date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");
        //
        switch (type) {
            case "FULL":
                for (HMAux hmAux : hmAuxs) {
                    if (hmAux.get(CH_MessageDao.READ).equalsIgnoreCase("0")) {
                        hmAux.put(CH_MessageDao.READ, sRead);
                        hmAux.put(CH_MessageDao.READ_DATE, sRead_Date);
                    }
                }
                //
                chMessageDao.addUpdate(
                        new CH_Message_Sql_016(
                                hmAuxs,
                                sRead,
                                sRead_Date
                        ).toSqlQuery()
                );
                //
                sendRead(hmAuxs);

                break;
            default:
                ArrayList<HMAux> hmAuxsForUpdate = new ArrayList<>();
                //
                for (HMAux hmAux : hmAuxs) {
                    if (hmAux.get(CH_MessageDao.READ).equalsIgnoreCase("0")) {
                        hmAux.put(CH_MessageDao.READ, sRead);
                        hmAux.put(CH_MessageDao.READ_DATE, sRead_Date);
                        //
                        hmAuxsForUpdate.add(hmAux);
                    }
                }
                //
                if (hmAuxsForUpdate.size() != 0) {
                    chMessageDao.addUpdate(
                            new CH_Message_Sql_016(
                                    hmAuxsForUpdate,
                                    sRead,
                                    sRead_Date
                            ).toSqlQuery()
                    );
                    //
                    sendRead(hmAuxsForUpdate);
                }

                break;
        }
    }

    @Override
    public void sendHistoricalScrollUp(String mRoom_code, String msg_prefix, String msg_code) {
        if (!msg_prefix.equalsIgnoreCase("0") && !msg_code.equalsIgnoreCase("0")) {
            Chat_S_Historical_Message sHistoricalMessage = new Chat_S_Historical_Message();
            sHistoricalMessage.setRoom_code(mRoom_code);
            sHistoricalMessage.setMsg_ref_prefix(Integer.parseInt(msg_prefix));
            sHistoricalMessage.setMsg_ref_code(Integer.parseInt(msg_code));
            sHistoricalMessage.setAction(Constant.CHAT_HISTORICAL_MSG_ACTION_SCROLL_UP);
            //
            SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);

            Gson gson = new GsonBuilder().serializeNulls().create();

            singletonWebSocket.attemptSendHistoricalMessages(ToolBox_Inf.setWebSocketJsonParam(sHistoricalMessage));
        }
    }

    @Override
    public void sendMessage(String mRoom_code, String message, String imagem, String offSet) {

        CH_Message chMessage = new CH_Message();
        chMessage.setMsg_prefix(0);
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
        chMessage.setStatus_update(1);
        chMessage.setRead_date(null);
        chMessage.setMsg_pk(null);
        chMessage.setUser_code(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        chMessage.setUser_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        //
        ch_messageDao.addUpdateTmp(chMessage);
        //
        setData(mRoom_code, offSet);
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
    public void sendRead(ArrayList<HMAux> hmAuxs) {
        enviarReadServer(hmAuxs);
    }

    private void enviarReadServer(ArrayList<HMAux> hmAuxs) {

        Gson gson = new GsonBuilder().serializeNulls().create();

        JsonArray sReadList = new JsonArray();

        for (HMAux hmAux : hmAuxs) {
            Chat_S_Read sRead = new Chat_S_Read();
            //
            sRead.setMsg_prefix(Integer.parseInt(hmAux.get(CH_MessageDao.MSG_PREFIX)));
            sRead.setMsg_code(Integer.parseInt(hmAux.get(CH_MessageDao.MSG_CODE)));
            //sRead.setRoom_code(hmAux.get(CH_MessageDao.ROOM_CODE));
            //
            sReadList.add(gson.toJsonTree(sRead));
        }

        if (sReadList.size() > 0) {
            SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
            singletonWebSocket.attemptToReadMessage(
                    ToolBox_Inf.setWebSocketJsonParam(sReadList)
            );
        }
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
        mView.callAct034(context);
    }
}
