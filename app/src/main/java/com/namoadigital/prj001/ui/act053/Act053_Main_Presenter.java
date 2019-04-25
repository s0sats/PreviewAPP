package com.namoadigital.prj001.ui.act053;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.*;
import com.namoadigital.prj001.service.*;
import com.namoadigital.prj001.sql.*;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

public class Act053_Main_Presenter implements Act053_Main_Contract.I_Presenter {

    private Context context;
    private Act053_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private long product_code;
    private MD_ProductDao mdProductDao;
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_TrackingDao trackingDao;
    private IO_InboundDao inboundDao;
    private IO_Inbound_ItemDao inboundItemDao;


    public Act053_Main_Presenter(Context context, Act053_Main_Contract.I_View mView,HMAux hmAux_Trans, long product_code) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.product_code = product_code;
        //
        initDaos();
    }

    private void initDaos() {
        mdProductDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        serialDao = new MD_Product_SerialDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        trackingDao = new MD_Product_Serial_TrackingDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        inboundDao = new IO_InboundDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        inboundItemDao= new IO_Inbound_ItemDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void defineFlow(String requesting_act) {
        switch (requesting_act){
            case ConstantBaseApp.ACT051:
            default:
                mView.callAct051();
                break;
        }
    }

    @Override
    public void checkFlow() {

    }

    @Override
    public void getProductInfo(long product_code) {
        MD_Product md_product = getMDProduct();
        //
        if (ToolBox_Inf.isValidProduct(md_product)) {
            mView.setProductValues(md_product);
        } else {
            mView.setProductValues(null);
            //
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_product_not_found_title"),
                    hmAux_Trans.get("alert_product_not_found_msg")
            );
        }
    }

    @Override
    public MD_Product getMDProduct() {
        return mdProductDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
    }

    @Override
    public void executeSerialSearch(long product_code, String serial_id) {
        mView.setWsProcess(WS_Serial_Search.class.getName());

        mView.showPD(
                hmAux_Trans.get("dialog_serial_search_ttl"),
                hmAux_Trans.get("dialog_serial_search_start")
        );

        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, "");
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
        bundle.putBoolean(Constant.WS_SERIAL_SEARCH_SAVE_PROCESS, true);
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult) {
        if (hmSaveResult.size() > 0) {
            ArrayList<HMAux> returnList = new ArrayList<>();
            String ttl = "";
            String msg = "";
            //
            for (Map.Entry<String, String> item : hmSaveResult.entrySet()) {
                HMAux aux = new HMAux();
                String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                String status = item.getValue();

                MD_Product mdProduct = mdProductDao.getByString(
                        new MD_Product_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                Long.parseLong(pk[0])
                        ).toSqlQuery()
                );
                //
                if (mdProduct != null) {
                    aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mdProduct.getProduct_code() + " - " + mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc());
                }
                aux.put(Generic_Results_Adapter.VALUE_ITEM_2, pk[1]);
                aux.put(Generic_Results_Adapter.VALUE_ITEM_3, status);
                aux.put(MD_Product_SerialDao.PRODUCT_CODE, pk[0]);
                returnList.add(aux);
                //
                if (product_code == Long.parseLong(pk[0])
                        && serial_id.equals(pk[1])
                ) {

                    if (status.equals("OK")) {
                        ttl = hmAux_Trans.get("alert_save_serial_return_ttl");
                        msg = hmAux_Trans.get("alert_save_serial_ok_msg");
                    } else {
                        ttl = hmAux_Trans.get("alert_save_serial_return_ttl");
                        msg = hmAux_Trans.get("alert_save_serial_error_msg") + "\n" + status;

                    }
                }
            }
            //Atualiza dados dos serial na tela e spinners
            mView.refreshUI();
            //
            //Atualiza dados dos serial na tela e spinners
            refreshMdProductSerialReference(product_code, serial_id);
            //
            //if(returnList.size() == 1){
            if (returnList.size() == 1) {
                boolean returnOk = false;
                if( returnList.get(0).hasConsistentValue(Generic_Results_Adapter.VALUE_ITEM_3)
                        && returnList.get(0).get(Generic_Results_Adapter.VALUE_ITEM_3).equals("OK")
                ){
                    returnOk = true;
                }
                //
                mView.showSingleResultMsg(ttl, msg, returnOk);
            } else {
                mView.showSerialResults(returnList);
            }
        } else {
            mView.showSingleResultMsg(
                    hmAux_Trans.get("alert_save_serial_return_ttl"),
                    hmAux_Trans.get("alert_no_serial_return_msg"),
                    false
            );
        }

    }

    private void refreshMdProductSerialReference(long product_code, String serial_id) {
        mView.updateProductSerialValues(
                getSerialInfo(
                        product_code,
                        serial_id
                )
        );
        //
        mView.refreshUI();
    }

    private MD_Product_Serial getSerialInfo(long product_code, String serial_id) {
        MD_Product_Serial serialObjDb = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //
        return serialObjDb;
    }


    @Override
    public void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code) {
        mView.setWsProcess( WS_Serial_Tracking_Search.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_tracking_search_ttl"),
                hmAux_Trans.get("progress_tracking_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Tracking_Search.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_SERIAL_CODE, String.valueOf(serial_code));
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_TRACKING, tracking);
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_SITE_CODE, site_code);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void updateSerialData(MD_Product_Serial mdProductSerial) {
        //Remove os tracking para reinserir os que ficaram
        trackingDao.remove(new
                MD_Product_Serial_Tracking_Sql_002(
                        mdProductSerial.getCustomer_code(),
                        mdProductSerial.getProduct_code(),
                        mdProductSerial.getSerial_tmp()
                ).toSqlQuery()
        );
        //Salva dados alterados do S.O
        serialDao.addUpdateTmp(mdProductSerial);
    }

    @Override
    public void executeSerialSave() {
        mView.setWsProcess(WS_Serial_Save.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_serial_search_ttl"),
                hmAux_Trans.get("progress_serial_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void extractSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
                result,
                TSerial_Search_Rec.class);
        //
        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
        //
        if(serial_list != null){
            if(serial_list.size() == 0){
                mView.reApplySerialIdToFrag();
            }else if(serial_list.size() == 1){
                mView.applyReceivedSerialToFrag(serial_list.get(0));
            }else{
                //FUDEU
            }
        }else{
            //FUDEU 2
        }

    }

    @Override
    public void executeAddressSuggestion(String site_code, long product_code) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Address_Suggestion.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("progress_serial_search_ttl"),
                hmAux_Trans.get("progress_serial_search_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Address_Suggestion.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(MD_Product_SerialDao.SITE_CODE, site_code);
            bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(product_code));
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }
    }

    @Override
    public void processAddresSuggestionResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        T_IO_Address_Suggestion_Rec rec = null;
        try {
            rec = gson.fromJson(
                result,
                T_IO_Address_Suggestion_Rec.class
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        //
        if(rec != null){
            mView.reportAddressSuggestion(
                rec.getZone_code(),
                rec.getZone_id(),
                rec.getZone_desc(),
                rec.getLocal_code(),
                rec.getLocal_id()
            );
        }else{
            mView.showAlertDialog(
                hmAux_Trans.get("alert_address_suggestion_fails_ttl"),
                hmAux_Trans.get("alert_address_suggestion_fails_msg")
            );
        }
    }

    @Override
    public void searchLocalSerial(long product_code, String serial_id) {
        //Não existe busca offline nesse caso.
    }

    @Override
    public void defineWsRetFlow(String ioProcess, String requesting_act) {
        if (ioProcess != null || !ioProcess.isEmpty()){
            switch (ioProcess){
                case ConstantBaseApp.IO_INBOUND:
                    generateIoInboundItem();
                    break;
                case ConstantBaseApp.IO_OUTBOUND:
                case ConstantBaseApp.IO_SERIAL_EDIT:
                    defineFlow(requesting_act);
                    break;
            }
        }

    }

    private void generateIoInboundItem() {
        MD_Product_Serial mdProductSerial = mView.getProductSerial();
        //
        if(mdProductSerial != null){
            IO_Inbound_Item inboundItem = new IO_Inbound_Item();
            //
            inboundItem.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
            inboundItem.setInbound_prefix(Integer.parseInt(mView.getIoPrefix()));
            inboundItem.setInbound_code(Integer.parseInt(mView.getIoCode()));
            inboundItem.setInbound_item(0);
            inboundItem.setProduct_code(mdProductSerial.getProduct_code());
            inboundItem.setSerial_code(mdProductSerial.getSerial_code());
            inboundItem.setStatus(ConstantBaseApp.SYS_STATUS_PENDING);
            inboundItem.setSave_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            inboundItem.setUpdate_required(1);
            //Atualiza cabeçalho da inbound para seta como update required
            inboundDao.addUpdate(
                new IO_Inbound_Sql_004(
                    inboundItem.getCustomer_code(),
                    inboundItem.getInbound_prefix(),
                    inboundItem.getInbound_code(),
                    1
                ).toSqlQuery()
            );
            //
            DaoObjReturn daoObjReturn = inboundItemDao.addUpdate(inboundItem);
            if(!daoObjReturn.hasError()){
                //Var que indica q foi salvo no banco, teve retorno OK.
                mView.setItemSavedOk(false);
                //
                executeWSInbounItem();
            }else{
                inboundDao.addUpdate(
                    new IO_Inbound_Sql_004(
                        inboundItem.getCustomer_code(),
                        inboundItem.getInbound_prefix(),
                        inboundItem.getInbound_code(),
                        0
                    ).toSqlQuery()
                );
                //
                mView.showAlertDialog(
                    hmAux_Trans.get("alert_error_item_save_ttl"),
                    hmAux_Trans.get("alert_error_item_save_msg")
                );
            }
        }
    }

    private void executeWSInbounItem() {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Inbound_Item_Add.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("progress_serial_search_ttl"),
                hmAux_Trans.get("progress_serial_search_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Inbound_Item_Add.class);
            Bundle bundle = new Bundle();
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processInboundItemAdd(String wsJsonReturn) {
        if(wsJsonReturn != null && !wsJsonReturn.isEmpty()){
            ArrayList<WS_IO_Inbound_Item_Add.InboundItemSaveActReturn> saveActReturns = null;
            int mPrefix =ToolBox_Inf.convertStringToInt(mView.getIoPrefix());
            int mCode =ToolBox_Inf.convertStringToInt(mView.getIoCode());
            //
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                //
                saveActReturns = gson.fromJson(
                    wsJsonReturn,
                    new
                        TypeToken<ArrayList<WS_IO_Inbound_Item_Add.InboundItemSaveActReturn>>() {
                        }.getType()
                    );
                //
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
                mView.showAlertDialog(
                    hmAux_Trans.get("alert_add_item_error_on_return_ttl"),
                    hmAux_Trans.get("alert_add_item_error_on_return_msg")

                );
            }
            //
            if(saveActReturns != null && saveActReturns.size() > 0){
                boolean finalResult = false;
                ArrayList<HMAux> resultList = new ArrayList<>();
                MD_Product_Serial serial =  mView.getProductSerial();
                //
                for(WS_IO_Inbound_Item_Add.InboundItemSaveActReturn actReturn :saveActReturns){
                    HMAux hmAux = new HMAux();
                    if(actReturn.isRetStatus()
                        &&   actReturn.getInbound_prefix() == mPrefix
                        &&   actReturn.getInbound_code() == mCode
                    ){
                        finalResult = true;
                    }
                    //Monta HmAux
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL,hmAux_Trans.get("item_lbl"));
                    hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1,hmAux_Trans.get("serial_lbl"));
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1,formatProductSerialDes(serial));
                    hmAux.put(Generic_Results_Adapter.LABEL_ITEM_2,hmAux_Trans.get("inbound_lbl") );
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_2, formatInboundInfo(actReturn));
                    hmAux.put(Generic_Results_Adapter.LABEL_ITEM_3, hmAux_Trans.get("message_lbl"));
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_3, actReturn.isRetStatus() ? "OK": actReturn.getMsg());
                    //
                    resultList.add(hmAux);
                }
                //
                mView.showResultDialog(resultList,finalResult);

            }else{
                mView.showAlertDialog(
                    hmAux_Trans.get("alert_add_item_empty_return_ttl"),
                    hmAux_Trans.get("alert_add_item_empty_return_msg")

                );
            }
        }else{
            mView.showAlertDialog(
                hmAux_Trans.get("alert_add_item_empty_return_ttl"),
                hmAux_Trans.get("alert_add_item_empty_return_msg")

            );
        }
    }

    private String formatInboundInfo(WS_IO_Inbound_Item_Add.InboundItemSaveActReturn actReturn) {
        return actReturn.getInbound_prefix()
            +"."+actReturn.getInbound_code()
            +"."+(actReturn.getInbound_item() != null ? actReturn.getInbound_item() : "0");
    }

    private String formatProductSerialDes(MD_Product_Serial serial) {
        return
            serial.getProduct_id() +" - "+ serial.getProduct_desc() + " - " +serial.getSerial_id();

    }

    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act){
            case ConstantBaseApp.ACT061:
            case ConstantBaseApp.ACT063:
                String ioProcess = mView.getIoProcess();
                if(ioProcess.equals(ConstantBaseApp.IO_INBOUND)){
                    proceedCallAct0061();
                }
                break;
            case ConstantBaseApp.ACT051:
            default:
                mView.callAct051();
                break;
        }
    }

    private void proceedCallAct0061() {
        if(mView.isItemSavedOk()){
            mView.callAct061(prepareAct061Bundle());
        } else{
            ToolBox.alertMSG_YES_NO(
                context,
                hmAux_Trans.get("alert_leave_add_item_ttl"),
                hmAux_Trans.get("alert_not_save_item_will_be_lost_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                        mView.callAct061(prepareAct061Bundle());
                    }
                },
                1
            );
        }

    }

    private void deleteItem() {
        //tudo via query sem verificação......
        inboundDao.addUpdate(
            new IO_Inbound_Sql_008(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Inf.convertStringToInt(mView.getIoPrefix()),
                ToolBox_Inf.convertStringToInt(mView.getIoCode())
            ).toSqlQuery()

        );
        //
        inboundItemDao.remove(
            new IO_Inbound_Item_Sql_007(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Inf.convertStringToInt(mView.getIoPrefix()),
                ToolBox_Inf.convertStringToInt(mView.getIoCode()),
                0
            ).toSqlQuery()
        );
    }

    private Bundle prepareAct061Bundle() {
        Bundle bundle = new Bundle();
        //
        return bundle;
    }
}
