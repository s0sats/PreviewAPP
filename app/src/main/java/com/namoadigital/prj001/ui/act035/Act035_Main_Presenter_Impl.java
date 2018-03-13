package com.namoadigital.prj001.ui.act035;

import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_Ref_Json;
import com.namoadigital.prj001.model.Chat_S_Historical_Message;
import com.namoadigital.prj001.model.Chat_S_Message;
import com.namoadigital.prj001.model.Chat_S_Read;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_018;
import com.namoadigital.prj001.sql.CH_Message_Sql_019;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
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
    private GE_Custom_Form_ApDao geCustomFormApDao;


    public Act035_Main_Presenter_Impl(Context context, Act035_Main_View mView, HMAux hmAux_Trans, CH_MessageDao ch_messageDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.ch_messageDao = ch_messageDao;
        this.geCustomFormApDao = new GE_Custom_Form_ApDao(context);
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
                        //
                        chMessageDao.addUpdate(
                                new CH_Message_Sql_019(
                                        hmAux,
                                        sRead,
                                        sRead_Date
                                ).toSqlQuery()
                        );
                    }
                }
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
                        chMessageDao.addUpdate(
                                new CH_Message_Sql_019(
                                        hmAux,
                                        sRead,
                                        sRead_Date
                                ).toSqlQuery()
                        );
                        //
                        hmAuxsForUpdate.add(hmAux);
                    }
                }
                //
                sendRead(hmAuxsForUpdate);

                break;
        }
    }

    @Override
    public void sendHistoricalScrollUp(String mRoom_code, String msg_prefix, String msg_code) {
        if (msg_prefix != null && !msg_prefix.equalsIgnoreCase("0") && !msg_code.equalsIgnoreCase("0")) {
            ArrayList<HMAux> refJsonAux = (ArrayList<HMAux>) ch_messageDao.query_HM(
                    new CH_Message_Sql_018(
                            context,
                            ToolBox_Inf.returnHmAuxListInString(
                                    ToolBox_Inf.getSessionCustomerChatList(context),
                                    EV_User_CustomerDao.CUSTOMER_CODE,
                                    ","
                            ),
                            ToolBox_Con.getPreference_User_Code(context),
                            mRoom_code
                    ).toSqlQuery()
            );
            ArrayList<Chat_Ref_Json> ref_json = new ArrayList<>();
            if (refJsonAux != null && refJsonAux.size() > 0) {
                for (HMAux hmAux : refJsonAux) {
                    if (hmAux.get(CH_MessageDao.MSG_PREFIX) != null && hmAux.get(CH_MessageDao.MSG_CODE) != null) {
                        Chat_Ref_Json refAux = new Chat_Ref_Json();
                        refAux.setMsg_prefix(
                                Integer.valueOf(hmAux.get(CH_MessageDao.MSG_PREFIX))
                        );
                        refAux.setMsg_code(
                                Integer.valueOf(hmAux.get(CH_MessageDao.MSG_CODE))
                        );
                        //
                        ref_json.add(refAux);
                    }
                }
            }
            //
            Chat_S_Historical_Message sHistoricalMessage = new Chat_S_Historical_Message();
            sHistoricalMessage.setRoom_code(mRoom_code);
            sHistoricalMessage.setMsg_ref_prefix(Integer.parseInt(msg_prefix));
            sHistoricalMessage.setMsg_ref_code(Integer.parseInt(msg_code));
            sHistoricalMessage.setAction(Constant.CHAT_HISTORICAL_MSG_ACTION_SCROLL_UP);
            sHistoricalMessage.setRef_json(ref_json);
            //
            SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
            Gson gson = new GsonBuilder().serializeNulls().create();
            singletonWebSocket.attemptSendHistoricalMessages(ToolBox_Inf.setWebSocketJsonParam(sHistoricalMessage));
        } else {
            if (msg_prefix == null ) {
                Chat_S_Historical_Message sHistoricalMessage = new Chat_S_Historical_Message();
                sHistoricalMessage.setRoom_code(mRoom_code);
                sHistoricalMessage.setMsg_ref_prefix(null);
                sHistoricalMessage.setMsg_ref_code(null);
                sHistoricalMessage.setAction(Constant.CHAT_HISTORICAL_MSG_ACTION_SCROLL_UP);
                sHistoricalMessage.setRef_json(null);
                //
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                Gson gson = new GsonBuilder().serializeNulls().create();
                singletonWebSocket.attemptSendHistoricalMessages(ToolBox_Inf.setWebSocketJsonParam(sHistoricalMessage));
            } else {
            }
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
        chMessage.setUser_nick(ToolBox_Con.getPreference_User_Code_Nick(context) + "(" + ToolBox_Con.getPreference_User_Code(context) + ")");
        //
        ch_messageDao.addUpdateTmp(chMessage);
        //
        setData(mRoom_code, offSet);
        //Se sem conectividade ou socket não setado, não tenta enviar msg
        if (ToolBox_Con.isOnline(context) && SingletonWebSocket.isSocketSetted()) {
            enviarMensagemServer(mRoom_code, message, chMessage);
        }

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

        if (sReadList.size() > 0 && ToolBox_Inf.isScreenOn(context)) {
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
    public void checkFormApFlow(HMAux hmAux) {
        HMAux auxAP = geCustomFormApDao.getByStringHM(
                new GE_Custom_Form_Ap_Sql_005(
                        hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE),
                        hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE),
                        hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE),
                        hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION),
                        hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA),
                        hmAux.get(GE_Custom_Form_ApDao.AP_CODE),
                        GE_Custom_Form_Ap_Sql_005.RETURN_SQL_HM_AUX
                ).toSqlQuery()
        );
        //
        if(auxAP != null && auxAP.size() > 0){
            mView.callAct038(context,hmAux);
        }else{
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_download_ap_ttl"),
                    hmAux_Trans.get("alert_download_ap_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //iniciar download da AP
                        }
                    },
                    1
            );
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct034(context);
    }
}
