package com.namoadigital.prj001.ui.act035;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_Ref_Json;
import com.namoadigital.prj001.model.Chat_Room_Obj_SO;
import com.namoadigital.prj001.model.Chat_S_Historical_Message;
import com.namoadigital.prj001.model.Chat_S_Message;
import com.namoadigital.prj001.model.Chat_S_Read;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_018;
import com.namoadigital.prj001.sql.CH_Message_Sql_019;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
import com.namoadigital.prj001.sql.MD_Operation_Sql_003;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act035_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
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
            if (msg_prefix == null) {
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
                chMessage.setMsg_type(Constant.CHAT_MESSAGE_TYPE_TEXT);
            } else {
                jsonObject.put("type", "IMAGE");
                //jsonObject.put("data", imagem);
                jsonObject.put("data", "");
                chMessage.setMsg_type(Constant.CHAT_MESSAGE_TYPE_IMAGE);
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
        chMessage.setRead(1);
        chMessage.setStatus_update(1);
        chMessage.setRead_date(null);
        chMessage.setMsg_pk(null);
        chMessage.setFile_status("");
        chMessage.setUser_code(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        chMessage.setUser_nick(ToolBox_Con.getPreference_User_Code_Nick(context) + "(" + ToolBox_Con.getPreference_User_Code(context) + ")");
        //
        ch_messageDao.addUpdateTmp(chMessage);
        //
        setData(mRoom_code, offSet);
        //Se sem conectividade ou socket não setado, não tenta enviar msg
        if (ToolBox_Con.isOnline(context,true) && SingletonWebSocket.isSocketSetted()) {
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
    public void checkFormApFlow(final HMAux hmAux) {
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
        if (auxAP != null && auxAP.size() > 0) {
            mView.callAct038(context, hmAux);
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_download_ap_ttl"),
                    hmAux_Trans.get("alert_download_ap_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ToolBox_Con.isOnline(context,true)) {
                                mView.executeApSyncWsViaInfo(hmAux);
                            } else {
                                ToolBox_Inf.showNoConnectionDialog(context);
                            }
                        }
                    },
                    1
            );
        }
    }

    @Override
    public boolean validateTicketPk(String ticketPk) {
        try {
            String[] pk_fields = getSplitedPk(ticketPk, "|");
            return pk_fields != null && pk_fields.length == 3;
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            return false;
        }

    }

    @Override
    public void validateTicketDownload(final String pk, String site_code, String operation_code, String product_code) {
        if(validateTicketPk(pk)){
            if(checkTicketMdProfile(site_code,operation_code,product_code)){
                if(!hasTicketDownloaded(pk)) {
                    ToolBox.alertMSG_YES_NO(
                            context,
                            hmAux_Trans.get("alert_download_ticket_ttl"),
                            hmAux_Trans.get("alert_download_ticket_confirm"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    executeWsTicketDownload(pk);
                                }
                            },
                            1

                    );
                }else{
                    String[] pk_fields = getSplitedPk(pk, "|");
                    Bundle bundle = new Bundle();
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT035);
                    bundle.putInt(TK_TicketDao.TICKET_PREFIX, Integer.parseInt(pk_fields [1]));
                    bundle.putInt(TK_TicketDao.TICKET_CODE, Integer.parseInt(pk_fields [2]));
                    mView.callAct070(bundle);
                }
            }else{
                mView.showAlert(
                    hmAux_Trans.get("alert_download_ticket_ttl"),
                    hmAux_Trans.get("alert_ticket_profile_missing_msg")
                );
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_download_ticket_ttl"),
                hmAux_Trans.get("alert_ticket_parameter_missing_msg")
            );
        }
        //

    }

    private boolean hasTicketDownloaded(String ticketPk) {
        String[] pk_fields = getSplitedPk(ticketPk, "|");
        int tk_prefix = Integer.parseInt(pk_fields[1]);
        int tk_code = Integer.parseInt(pk_fields[2]);

        final long customer_code = ToolBox_Con.getPreference_Customer_Code(context);
        TK_TicketDao tk_ticketDao = new TK_TicketDao(context,
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
                );
        TK_Ticket ticket = tk_ticketDao.getByString(new TK_Ticket_Sql_001(
                customer_code,
                tk_prefix,
                tk_code).toSqlQuery()
        );
        return ticket != null;
    }

    private void executeWsTicketDownload(String ticketPk) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWSProcess(WS_TK_Ticket_Download.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_download_ticket_ttl"),
                hmAux_Trans.get("dialog_download_ticket_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(TK_TicketDao.TICKET_PREFIX,ticketPk);
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }

    }

    @Override
    public String[] getSplitedPk(String pk, String splitter){
        return pk.replace(splitter, "#").split("#");

    }

    @Override
    public boolean checkTicketMdProfile(String s_site_code, String s_operation_code, String s_product_code) {
        //
        try {
            long customerCode = ToolBox_Con.getPreference_Customer_Code(context);
            long operationCode = Long.parseLong(s_operation_code);
            long productCode = Long.parseLong(s_product_code);

            MD_SiteDao siteDao = new MD_SiteDao(
                context,
                ToolBox_Con.customDBPath(customerCode),
                Constant.DB_VERSION_CUSTOM
            );
            MD_OperationDao operationDao = new MD_OperationDao(
                context,
                ToolBox_Con.customDBPath(customerCode),
                Constant.DB_VERSION_CUSTOM
            );
            MD_ProductDao productDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(customerCode),
                Constant.DB_VERSION_CUSTOM
            );
            //
            MD_Site site = siteDao.getByString(
                new MD_Site_Sql_003(
                    customerCode,
                    s_site_code
                ).toSqlQuery()
            );
            //
            MD_Operation operation = operationDao.getByString(
                new MD_Operation_Sql_003(
                    customerCode,
                    operationCode
                ).toSqlQuery()
            );
            MD_Product product = productDao.getByString(
                new MD_Product_Sql_001(
                    customerCode,
                    productCode
                ).toSqlQuery()
            );
            //
            return site != null && site.getCustomer_code() > -1
                && operation != null && operation.getCustomer_code() > -1
                && product != null && product.getCustomer_code() > -1;
            //
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(), e);
            return false;
        }
    }


    @Override
    public void executeSerialDownload(String productId, String serialId) {
        mView.setWSProcess(WS_Serial_Search.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_serial_download_ttl"),
                hmAux_Trans.get("dialog_serial_download_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, "");
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, productId);
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serialId);
        bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, "");
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    /**
     * LUCHE - 16/01/2020
     *
     * Trata retorno do ws do serial.
     * @param result - Json enviado pelo WS
     * @param roomObjSo - SO da sala.
     */
    @Override
    public void extractSearchResult(String result, Chat_Room_Obj_SO roomObjSo) {
        if (result != null && !result.isEmpty()) {
            ArrayList<MD_Product_Serial> serial_list;
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                TSerial_Search_Rec rec = gson.fromJson(
                        result,
                        TSerial_Search_Rec.class);
                //
                serial_list = rec.getRecord();
            } catch (Exception e) {
                serial_list = new ArrayList<>();
            }
            //
            boolean serialInList = false;
            //
            if (serial_list != null && serial_list.size() > 0) {
                //
                for (MD_Product_Serial serial : serial_list) {
                    if (
                            serial.getCustomer_code() == ToolBox_Con.getPreference_Customer_Code(context)
                                    && serial.getProduct_id().equalsIgnoreCase(roomObjSo.getSo_product_id())
                                    && serial.getSerial_id().equalsIgnoreCase(roomObjSo.getSo_serial())
                    ) {
                        serialInList = true;
                        saveSerialInDb(serial);
                        break;
                    }
                }
                //
                if(serialInList){
                    executeSoDownload(
                            String.valueOf(roomObjSo.getSo_prefix()),
                            String.valueOf(roomObjSo.getSo_code())
                    );
                }else{
                    mView.showAlertWithAction(
                            hmAux_Trans.get("alert_serial_not_returned_ttl"),
                            hmAux_Trans.get("alert_serial_not_returned_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.cleanWsTmpItem();
                                }
                            }
                    );
                }
                //
            } else {
                mView.showAlertWithAction(
                        hmAux_Trans.get("alert_no_serial_returned_ttl"),
                        hmAux_Trans.get("alert_no_serial_returned_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.cleanWsTmpItem();
                            }
                        }
                );
            }
        } else {
            mView.showAlertWithAction(
                    hmAux_Trans.get("alert_no_serial_returned_ttl"),
                    hmAux_Trans.get("alert_no_serial_returned_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.cleanWsTmpItem();
                        }
                    }
            );
        }
    }

    /**
     * LUCHE - 16/01/2020
     *
     * Salva Serial retornado no banco de dados.
     *
     * @param serial
     */
    private void saveSerialInDb(MD_Product_Serial serial) {
        MD_Product_SerialDao serialDao =
                new MD_Product_SerialDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        serialDao.addUpdateTmp(serial);
    }

    @Override
    public void executeSoDownload(String soPrefix, String soCode) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWSProcess(WS_SO_Search.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_so_download_ttl"),
                    hmAux_Trans.get("dialog_so_download_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Search.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.WS_SO_SEARCH_SO_MULT, soPrefix + "." + soCode);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
//            mView.showNoConnecionMsg();
        }
    }

    /**
     * Barrionuevo - 02/06/2020
     *
     * Trata retorno do WS de Download de O.S - Retirado da act047
     *
     * @param soDownloadResult - HmAux com as chaves de qty de itens e o.s concatenadas
     * @param soPrefix
     * @param soCode
     */
    @Override
    public void processSoDownloadResult(HMAux soDownloadResult, String soPrefix, String soCode) {
        if (soDownloadResult.containsKey(WS_SO_Search.SO_PREFIX_CODE)
                && soDownloadResult.containsKey(WS_SO_Search.SO_LIST_QTY)
        ) {
            if (Integer.parseInt(soDownloadResult.get(WS_SO_Search.SO_LIST_QTY)) == 0) {
                mView.showAlert(
                        hmAux_Trans.get("alert_no_so_returned_ttl"),
                        hmAux_Trans.get("alert_no_so_returned_msg")
                );
            } else {
                if (soDownloadResult.get(WS_SO_Search.SO_PREFIX_CODE).contains(Constant.MAIN_CONCAT_STRING)) {
                    String searchedSo = soPrefix + Constant.MAIN_CONCAT_STRING + soCode;
                    if(soDownloadResult.get(WS_SO_Search.SO_PREFIX_CODE).contains(searchedSo)){
                        //
                        mView.callAct027(context);
                    }else{
                        mView.showAlert(
                                hmAux_Trans.get("alert_so_not_returned_ttl"),
                                hmAux_Trans.get("alert_so_not_returned_msg")
                        );
                    }
                } else {
                    mView.showAlert(
                            hmAux_Trans.get("alert_so_download_param_error_ttl"),
                            hmAux_Trans.get("alert_so_download_param_error_msg")
                    );
                }
            }
        } else {
            mView.showAlert(
                    hmAux_Trans.get("alert_so_download_param_error_ttl"),
                    hmAux_Trans.get("alert_so_download_param_error_msg")
            );
        }
    }

    @Override
    public void onBackPressedClicked(String act_request) {
        if (act_request != null
                && act_request.equalsIgnoreCase(ConstantBaseApp.ACT027)) {
            mView.callAct027(context);
        }else {
            mView.callAct034(context);
        }
    }
}
