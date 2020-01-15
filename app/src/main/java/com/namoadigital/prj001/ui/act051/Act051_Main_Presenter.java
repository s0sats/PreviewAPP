package com.namoadigital.prj001.ui.act051;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.T_IO_Serial_Process_Response;
import com.namoadigital.prj001.receiver.*;
import com.namoadigital.prj001.service.*;
import com.namoadigital.prj001.sql.*;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Act051_Main_Presenter implements Act051_Main_Contract.I_Presenter {

    private Context context;
    private Act051_Main_Contract.I_View mView;

    private MD_ProductDao productDao;
    private MD_Product_SerialDao serialDao;
    private IO_MoveDao moveDao;
    private IO_InboundDao ioInboundDao;
    private IO_OutboundDao ioOutboundDao;

    private HMAux hmAux_Trans;
    private MD_Product mdProduct;

    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;
    private boolean allowBlindMove = true;
    private int waitingSyncMovePendency;
    private int waitingSyncPutAwayPendency;
    private int waitingSyncPickingPendency;
    private int waitingSyncBlindPendency;

    public Act051_Main_Presenter(Context context, Act051_Main_Contract.I_View mView, MD_ProductDao productDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.productDao = productDao;
        this.hmAux_Trans = hmAux_Trans;
        waitingSyncMovePendency = 0;
        waitingSyncPutAwayPendency = 0;
        waitingSyncPickingPendency = 0;
        waitingSyncBlindPendency = 0;
        this.serialDao = new MD_Product_SerialDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        this.moveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        this.ioInboundDao = new IO_InboundDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        this.ioOutboundDao = new IO_OutboundDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public void getMD_Products() {
        ArrayList<MD_Product> productList = (ArrayList<MD_Product>) productDao.query(
                new MD_Product_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        mView.setProduto(productList);
    }

    @Override
    public void executeSerialProcessSearch(String product_id, String serial_id, String tracking) {
        mdProduct = searchProduct(product_id);
        mProduct_id = product_id;
        mSerial_id = serial_id;
        mTracking = tracking;
        boolean isOnline = ToolBox_Con.isOnline(context);
        if (isOnline) {
            mView.setWsProcess(WS_IO_Serial_Process_Search.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_serial_search_ttl"),
                    hmAux_Trans.get("dialog_serial_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Serial_Process_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, mdProduct != null ? String.valueOf(mdProduct.getProduct_code()) : null);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("dialog_serial_search_start"), "", "0");
        } else {
            /**
             *
             *
             * CRIAR METODO QUE GERARÁ ITENS OFFLINE
             *
             * PRIMEIRO NECESSARIO CRIAÇÃO DAS TABELAS DE IO
             *
             *
             */

            ArrayList<IO_Serial_Process_Record> serial_list = hasLocalSerial(product_id, serial_id, tracking);
            //
            defineSearchResultFlow(serial_list, (long) serial_list.size(), (long) serial_list.size(), isOnline);
        }
    }

    public void getPendencies() {

    }

    @Override
    public MD_Product searchProduct(String product_id) {
        MD_Product md_product = productDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        "",
                        product_id
                ).toSqlQuery()
        );
        //
        return md_product;
    }

    private ArrayList<IO_Serial_Process_Record> hasLocalSerial(String product_id, String serial_id, String tracking) {
        ArrayList<IO_Serial_Process_Record> serial_process_records = new ArrayList<>();
        ArrayList<HMAux> serial_list = new ArrayList<>();
        List<HMAux> move_list = serialDao.query_HM(
                new IO_Move_Order_Item_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context),
                        product_id,
                        serial_id,
                        tracking
                ).toSqlQuery()
        );


        List<HMAux> in_conf_list = serialDao.query_HM(
                new IO_Move_Order_Item_Sql_010(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context),
                        product_id,
                        serial_id,
                        tracking
                ).toSqlQuery()
        );

        List<HMAux> out_conf_list = serialDao.query_HM(
            new Sql_Act051_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context),
                product_id,
                serial_id,
                tracking
            ).toSqlQuery()
        );

        List<HMAux> serial_offline_list = serialDao.query_HM(
                new IO_Move_Order_Item_Sql_012(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context),
                        product_id,
                        serial_id,
                        tracking
                ).toSqlQuery()
        );

        serial_list.addAll(move_list);
        serial_list.addAll(in_conf_list);
        serial_list.addAll(out_conf_list);
        serial_list.addAll(serial_offline_list);
        getSerialProcessRecordListFromHMaux(serial_process_records, serial_list, serial_id);
        return serial_process_records;
    }

    private void getSerialProcessRecordListFromHMaux(ArrayList<IO_Serial_Process_Record> serial_process_records, ArrayList<HMAux> serial_list, String serial_id) {
        for (HMAux serial_record : serial_list) {
            IO_Serial_Process_Record record = new IO_Serial_Process_Record();

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.CUSTOMER_CODE)) {
                record.setCustomer_code(Long.parseLong(serial_record.get(MD_Product_SerialDao.CUSTOMER_CODE)));
            }
            if (serial_record.hasConsistentValue(MD_Product_SerialDao.PRODUCT_CODE)) {
                record.setProduct_code(Long.parseLong(serial_record.get(MD_Product_SerialDao.PRODUCT_CODE)));
            }

            record.setProduct_id(serial_record.get(MD_Product_SerialDao.PRODUCT_ID));
            record.setProduct_desc(serial_record.get(MD_Product_SerialDao.PRODUCT_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.SERIAL_CODE)) {
                record.setSerial_code(Integer.parseInt(serial_record.get(MD_Product_SerialDao.SERIAL_CODE)));
            }
            if (serial_id != null && serial_id.equalsIgnoreCase(serial_record.get(MD_Product_SerialDao.SERIAL_ID))) {
                allowBlindMove = false;
            } else {
                allowBlindMove = true;
            }
            record.setSerial_id(serial_record.get(MD_Product_SerialDao.SERIAL_ID));
            if (serial_record.hasConsistentValue(MD_Product_SerialDao.SITE_CODE) && !serial_record.get(MD_Product_SerialDao.SITE_CODE).isEmpty()) {
                record.setSite_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.SITE_CODE)));
            }
            record.setSite_id(serial_record.get(MD_Product_SerialDao.SITE_ID));
            record.setSite_desc(serial_record.get(MD_Product_SerialDao.SITE_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.ZONE_CODE)) {
                try {
                    record.setZone_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.ZONE_CODE)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    record.setZone_code(0);
                }
            }

            record.setZone_id(serial_record.get(MD_Product_SerialDao.ZONE_ID));
            record.setZone_desc(serial_record.get(MD_Product_SerialDao.ZONE_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.LOCAL_CODE)) {
                try {
                    record.setLocal_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.LOCAL_CODE)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    record.setLocal_code(0);
                }
            }

            record.setLocal_id(serial_record.get(MD_Product_SerialDao.LOCAL_ID));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.BRAND_CODE)) {
                try {
                    record.setBrand_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.BRAND_CODE)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    record.setBrand_code(null);
                }
            }

            record.setBrand_id(serial_record.get(MD_Product_SerialDao.BRAND_ID));
            record.setBrand_desc(serial_record.get(MD_Product_SerialDao.BRAND_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.MODEL_CODE)) {
                try {
                    record.setModel_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.MODEL_CODE)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    record.setModel_code(null);
                }
            }

            record.setModel_id(serial_record.get(MD_Product_SerialDao.MODEL_ID));
            record.setModel_desc(serial_record.get(MD_Product_SerialDao.MODEL_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.COLOR_CODE)) {
                try {
                    record.setColor_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.COLOR_CODE)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    record.setColor_code(null);
                }
            }

            record.setColor_id(serial_record.get(MD_Product_SerialDao.COLOR_ID));
            record.setColor_desc(serial_record.get(MD_Product_SerialDao.COLOR_DESC));

            if (serial_record.hasConsistentValue(IO_MoveDao.MOVE_TYPE)) {
                String type;
                switch (serial_record.get(IO_MoveDao.MOVE_TYPE)) {
                    case ConstantBaseApp.IO_INBOUND:
                        //type = ConstantBaseApp.SYS_STATUS_PUT_AWAY;
                        type = ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY;
                        break;
                    case ConstantBaseApp.IO_OUTBOUND:
                        //type = ConstantBaseApp.SYS_STATUS_PICKING;
                        type = ConstantBaseApp.IO_PROCESS_OUT_PICKING;
                        break;
                    case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                        type = ConstantBaseApp.IO_PROCESS_MOVE_PLANNED;
                        break;
                    default:
                        type = ConstantBaseApp.IO_PROCESS_MOVE;
                }
                record.setProcess_type(type);
            } else if (serial_record.hasConsistentValue(ConstantBaseApp.IO_PROCESS_CONF_TYPE)){
                record.setProcess_type(serial_record.get(ConstantBaseApp.IO_PROCESS_CONF_TYPE));
            }else{
                record.setProcess_type(ConstantBaseApp.IO_PROCESS_MOVE);
            }

            serial_process_records.add(record);

        }
    }

    @Override
    public void processSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        T_IO_Serial_Process_Response rec = gson.fromJson(
                result,
                T_IO_Serial_Process_Response.class);
        //
        ArrayList<IO_Serial_Process_Record> serial_list = rec.getRecord();
        //Ultimo parametro forçado como true pois a resposta veio do WS
        defineSearchResultFlow(serial_list, rec.getRecord_count(), rec.getRecord_page(), true);
    }

    @Override
    public void defineSearchResultFlow(ArrayList<IO_Serial_Process_Record> serial_list, long record_count, long record_page, boolean isOnline) {
        if ((serial_list == null || serial_list.size() == 0) && mdProduct == null) {
            mView.showMsg(
                    hmAux_Trans.get("alert_no_serial_found_ttl"),
                    hmAux_Trans.get("alert_no_serial_found_msg")
            );
        } else {

            ArrayList<IO_Serial_Process_Record> results = processEqualCheck(serial_list);

            Bundle bundle = new Bundle();
            bundle.putString(MD_ProductDao.PRODUCT_ID, mdProduct != null ? mdProduct.getProduct_id() : "");

            if (results.size() != 0) {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, results);
            } else {
                if (serial_list.size() == 1 && serial_list.get(0).getSerial_id().equalsIgnoreCase(mSerial_id)) {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
                } else {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
                }
            }

            bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, mSerial_id);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
            bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_IS_ONLINE_PROCESS, isOnline);
            bundle.putBoolean(IO_Blind_MoveDao.FLAG_BLIND, allowBlindMove);
            //
            bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, mProduct_id); //mdProduct != null ? mdProduct.getProduct_id() : "");
            bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, mSerial_id != null ? mSerial_id : "");
            bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, mTracking != null ? mTracking : "");

            mView.callAct052(context, bundle);
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }

    @Override
    public void syncMovements() {
        if (ToolBox_Con.isOnline(context)) {
            callBroadcast(
                    WS_IO_Move_Save.class.getName(),
                    "dialog_save_move_ttl",
                    "dialog_save_move_msg",
                    new Intent(context, WBR_IO_Move_Save.class),
                    new Bundle()
            );
        } else {
            mView.handleNoConnection();
        }
    }

    @Override
    public void syncBlindItem() {
        if (ToolBox_Con.isOnline(context)) {
            callBroadcast(
                    WS_IO_Blind_Move_Save.class.getName(),
                    "dialog_save_move_ttl",
                    "dialog_save_move_msg",
                    new Intent(context, WBR_IO_Blind_Move_Save.class),
                    new Bundle()
            );
        } else {
            mView.handleNoConnection();
        }
    }

    @Override
    public void syncOutobundItem() {
        if (ToolBox_Con.isOnline(context)) {
            callBroadcast(
                    WS_IO_Outbound_Item_Save.class.getName(),
                    "progress_save_outbound_item_ttl",
                    "progress_save_outbound_item_msg",
                    new Intent(context, WBR_IO_Outbound_Item_Save.class),
                    new Bundle()
            );
        } else {
            mView.handleNoConnection();
        }
    }

    @Override
    public void syncInboundItem() {
        if (ToolBox_Con.isOnline(context)) {
            callBroadcast(
                    WS_IO_Inbound_Item_Save.class.getName(),
                    "progress_save_inbound_item_ttl",
                    "progress_save_inbound_item_msg",
                    new Intent(context, WBR_IO_Inbound_Item_Save.class),
                    new Bundle()
            );
        } else {
            mView.handleNoConnection();
        }
    }

    private int getPendencyCounterFromHmaux(HMAux result) {
        int pendencies = 0;
        if (result != null && result.hasConsistentValue(IO_MoveDao.PENDING_QTY)) {
            try {
                pendencies = Integer.valueOf(result.get(IO_MoveDao.PENDING_QTY));
            } catch (Exception e) {
                pendencies = 0;
                e.printStackTrace();
            }
        }
        return pendencies;
    }

    @Override
    public boolean hasWaitingSyncMovePendency() {
        HMAux resultMoveWaitingSync = moveDao.getByStringHM((
                        new IO_Move_Order_Item_Sql_005(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ConstantBaseApp.IO_PROCESS_MOVE_PLANNED,
                                0
                        )
                ).toSqlQuery()
        );
        waitingSyncMovePendency = getPendencyCounterFromHmaux(resultMoveWaitingSync);

        return waitingSyncMovePendency > 0;
    }

    @Override
    public boolean hasWaitingSyncBlindPendency() {
        HMAux resultBlindWaitingSync = moveDao.getByStringHM((
                        new IO_Blind_Move_Sql_006(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        )
                ).toSqlQuery()
        );
        waitingSyncBlindPendency = getPendencyCounterFromHmaux(resultBlindWaitingSync);
        return waitingSyncBlindPendency > 0;
    }

    @Override
    public boolean hasWaitingSyncPickingPendency() {
        //LUCHE - 15/01/2020
        //Adicionado checagem de pendencia de arquivo de token
        waitingSyncPickingPendency = ToolBox_Inf.countOutboundsInTokenFile(ToolBox_Con.getPreference_Customer_Code(context));
        //Se não houver pendencia de token, segue para verificação no db
        if(waitingSyncPickingPendency <= 0) {
            ArrayList<HMAux> outboundPendency = (ArrayList<HMAux>) ioOutboundDao.query_HM(
                new IO_Outbound_Sql_009(
                    ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
            );
            if (outboundPendency != null) {
                waitingSyncPickingPendency = outboundPendency.size();
            } else {
                waitingSyncPickingPendency = 0;
            }
        }
        //
        return waitingSyncPickingPendency > 0;
    }

    @Override
    public boolean hasWaitingSyncPutAwayPendency() {
        //LUCHE - 15/01/2020
        //Adicionado checagem de pendencia de arquivo de token
        waitingSyncPutAwayPendency = ToolBox_Inf.countInboundsInTokenFile(ToolBox_Con.getPreference_Customer_Code(context));
        //Se não houver pendencia de token, segue para verificação no db
        if(waitingSyncPutAwayPendency <= 0) {
            //Selecnio Inbound update_required
            ArrayList<HMAux> inboundAux = (ArrayList<HMAux>) ioInboundDao.query_HM(
                new IO_Inbound_Sql_009(
                    ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
            );
            if (inboundAux != null) {
                waitingSyncPutAwayPendency = inboundAux.size();
            } else {
                waitingSyncPutAwayPendency = 0;
            }
        }
        //
        return waitingSyncPutAwayPendency > 0;
    }


    @Override
    public void processIOItemSaveReturn(String jsonRet, String itemLabel) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn> actReturnList = null;
        ArrayList<HMAux> resultList = new ArrayList<>();
        try {
            actReturnList = gson.fromJson(
                    jsonRet,
                    new TypeToken<ArrayList<WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn>>() {
                    }.getType());
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
        //
        if (actReturnList != null && actReturnList.size() > 0) {
            boolean outboundResult = true;
            int outboundNextIdx = 0;
            HMAux auxResult = new HMAux();
            //Monta lista por inbound
            for (WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn actReturn : actReturnList) {
                String moveCode = "";
                //
                if (actReturn.isMove()) {
                    IO_Move ioMove =
                            moveDao.getByString(
                                    new IO_Move_Order_Item_Sql_001(
                                            actReturn.getCustomer_code(),
                                            actReturn.getPrefix(),
                                            actReturn.getCode()
                                    ).toSqlQuery()
                            );
                    if (ioMove != null) {
                        moveCode = ioMove.getMove_prefix() + "." + ioMove.getMove_code();
                    }
                } else {
                    moveCode = actReturn.getPrefix() + "." + actReturn.getCode();
                }
                if (!auxResult.containsKey(moveCode)
                        || (auxResult.containsKey(moveCode)
                        && !actReturn.getRetStatus().equals("OK")
                )
                ) {
                    String msg = actReturn.getRetStatus() ;
                    if(actReturn.getMsg() != null && !actReturn.getMsg().isEmpty()){
                        msg+= "\n" + actReturn.getMsg();
                    }
                    auxResult.put(moveCode, msg);
                }
            }
            //For no resumido por inbound montando msg a ser exibida
            for (Map.Entry<String, String> item : auxResult.entrySet()) {

                HMAux hmAux = new HMAux();
                //
                //Monta HmAux
                hmAux.put("title", hmAux_Trans.get(itemLabel));
                hmAux.put("label", item.getKey());
                hmAux.put("status", item.getValue());
                //
                resultList.add(hmAux);


            }
            //
            mView.showResult(resultList);
        }else{
            //caso lista vazia segue o fluxo
            mView.handleNoConnection();
        }
    }

    private void callBroadcast(String serviceName, String progress_save_item_ttl, String progress_save_item_msg, Intent mIntent, Bundle bundle) {
        mView.setWsProcess(serviceName);
        //
        mView.showPD(
                hmAux_Trans.get(progress_save_item_ttl),
                hmAux_Trans.get(progress_save_item_msg)
        );
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private ArrayList<IO_Serial_Process_Record> processEqualCheck(ArrayList<IO_Serial_Process_Record> serial_list) {
        ArrayList<IO_Serial_Process_Record> results = new ArrayList<>();

        if (mdProduct == null) {
            return results;
        } else {

            for (IO_Serial_Process_Record psAux : serial_list) {
                String res = "";

                if (!mdProduct.getProduct_id().equalsIgnoreCase(psAux.getProduct_id())) {
                    continue;
                } else {
                    res += "1";
                }
                //
                if (mSerial_id.isEmpty()) {
                    res += "0";
                } else {
                    if (mSerial_id.equalsIgnoreCase(psAux.getSerial_id())) {
                        res += "1";
                    } else {
                        res += "0";
                    }
                }
                //
                if (mTracking.isEmpty()) {
                    res += "1";
                } else {
                    int mSize = psAux.getTracking_list().size();

                    if (mSize == 0) {
                        res += "0";
                    } else {
                        for (int i = 0; i < mSize; i++) {
                            if (mTracking.equalsIgnoreCase(psAux.getTracking_list().get(i).getTracking())) {
                                res += "1";
                                break;
                            }
                        }
                    }
                }

                if (res.equalsIgnoreCase("111")) {
                    results.add(psAux);
                }
            }

            return results;
        }
    }
}
