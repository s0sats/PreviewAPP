package com.namoadigital.prj001.ui.act061;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_IO_From_Site_Search;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Header_Save;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Item_Save;
import com.namoadigital.prj001.receiver.WBR_IO_Master_Data;
import com.namoadigital.prj001.service.WS_IO_From_Site_Search;
import com.namoadigital.prj001.service.WS_IO_Inbound_Header_Save;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Master_Data;
import com.namoadigital.prj001.sql.*;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

public class Act061_Main_Presenter implements Act061_Main_Contract.I_Presenter {

    private Context context;
    private Act061_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_InboundDao inboundDao;
    private MD_Product_SerialDao serialDao;
    private IO_MoveDao moveDao;

    public Act061_Main_Presenter(Context context, Act061_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.inboundDao = new IO_InboundDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.serialDao = new MD_Product_SerialDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.moveDao = new IO_MoveDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }


    @Override
    public IO_Inbound getInbound(int prefix, int code) {
        IO_Inbound ioInbound = inboundDao.getByString(
            new IO_Inbound_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                prefix,
                code
            ).toSqlQuery()
        );
        //
        return ioInbound;
    }

    @Override
    public void executeWSMasterData(String type, boolean bNewIo) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Master_Data.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_io_master_data_ttl"),
                hmAux_Trans.get("dialog_io_master_data_start")
            );
            //
            //
            Intent mIntent = new Intent(context, WBR_IO_Master_Data.class);
            Bundle bundle = new Bundle();
            bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            bundle.putString(IO_InboundDao.FROM_TYPE, type);
            bundle.putString(ConstantBaseApp.IO_ACTION_KEY, bNewIo ? ConstantBaseApp.IO_ACTION_NEW : ConstantBaseApp.IO_ACTION_EDIT);
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }


    @Override
    public void processIOMasterDataRet(String wsReturn) {
        if (wsReturn != null && !wsReturn.trim().isEmpty()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            //
            try {
                T_IO_Master_Data_Rec rec = gson.fromJson(
                    wsReturn,
                    T_IO_Master_Data_Rec.class
                );
                //
                mView.setMDList(
                    rec.getSite(),
                    rec.getPartner(),
                    rec.getModal()
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
                //
                mView.showAlert(
                    hmAux_Trans.get("alert_io_master_data_error_ttl"),
                    hmAux_Trans.get("alert_io_master_data_error_msg")
                );
            }
        }
    }

    @Override
    public void executeWsSearchOutbound(String from_site) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_From_Site_Search.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_from_outbound_ttl"),
                hmAux_Trans.get("dialog_from_outbound_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_From_Site_Search.class);
            Bundle bundle = new Bundle();
            bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            bundle.putString(IO_InboundDao.FROM_SITE_CODE, from_site);
            bundle.putString(IO_InboundDao.TO_SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processFromOutboundRet(String wsReturn) {
        if (wsReturn != null && !wsReturn.trim().isEmpty()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            //
            try {
                T_IO_From_Site_Search_Rec rec = gson.fromJson(
                    wsReturn,
                    T_IO_From_Site_Search_Rec.class
                );
                //
                mView.setFromOutboundList(
                    rec.getOutbound()
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
                //
                mView.showAlert(
                    hmAux_Trans.get("alert_io_master_data_error_ttl"),
                    hmAux_Trans.get("alert_io_master_data_error_msg")
                );
            }
        }
    }

    @Override
    public void executeWsSaveInboundHeader(IO_Inbound mInbound, boolean newProcess) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Inbound_Header_Save.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_inbound_header_save_ttl"),
                hmAux_Trans.get("dialog_inbound_header_save_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Inbound_Header_Save.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantBaseApp.IO_OBJ_KEY, mInbound);
            bundle.putBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY, newProcess);
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processHeaderSave(int mPrefix, int mCode, String actReturnJson) {
        WS_IO_Inbound_Header_Save.InboundHeaderSaveActReturn retObj = null;

        //
        if (actReturnJson == null || actReturnJson.length() == 0) {
            mView.showAlert(
                hmAux_Trans.get("alert_header_save_error_ttl"),
                hmAux_Trans.get("alert_header_save_no_return_msg")
            );
        } else {
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                //
                retObj =
                    gson.fromJson(
                        actReturnJson,
                        WS_IO_Inbound_Header_Save.InboundHeaderSaveActReturn.class
                    );

            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
                //
                mView.showAlert(
                    hmAux_Trans.get("alert_header_save_error_ttl"),
                    hmAux_Trans.get("alert_header_save_process_error_msg")
                );
            }
            //
            if (retObj != null) {
                if (retObj.isRetStatusOk()) {
                    mView.updateHeaderData(
                        retObj.getInbound_prefix(),
                        retObj.getInbound_code(),
                        retObj.isNewProcess()
                    );
                } else {
                    mView.showAlert(
                        hmAux_Trans.get("alert_header_save_error_ttl"),
                        hmAux_Trans.get("alert_header_save_error_msg")
                            + "\n" + retObj.getMsg()
                    );
                }
            }

        }

    }

    @Override
    public void processPutAwayMove(HMAux item) {
        Bundle bundle = new Bundle();
        IO_MoveDao ioMoveDao = new IO_MoveDao(context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM);

        IO_Move io_move;
        try {
            io_move = ioMoveDao.getByString(
                new IO_Move_Order_Item_Sql_006(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    item.get(IO_Inbound_ItemDao.INBOUND_PREFIX),
                    item.get(IO_Inbound_ItemDao.INBOUND_CODE),
                    item.get(IO_Inbound_ItemDao.INBOUND_ITEM)
                ).toSqlQuery()
            );
        } catch (NullPointerException e) {
            e.printStackTrace();
            io_move = null;
        }

        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT061);
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, io_move.getMove_type());
        bundle.putString(IO_MoveDao.MOVE_PREFIX, String.valueOf(io_move.getMove_prefix()));
        bundle.putString(IO_MoveDao.MOVE_CODE, String.valueOf(io_move.getMove_code()));
        bundle.putInt(MD_Product_SerialDao.PRODUCT_CODE, (int) io_move.getProduct_code());
        bundle.putInt(MD_Product_SerialDao.SERIAL_CODE, io_move.getSerial_code());

        mView.callAct058(bundle);
    }

    @Override
    public void processSerialEdition(HMAux item) {
        MD_Product_Serial serial = getSerial(item);
        //
        if (serial != null) {
            Bundle bundle = new Bundle();
            //
            bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, item.get(IO_Inbound_ItemDao.PRODUCT_CODE));
            bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(MD_Product_SerialDao.SERIAL_ID));
            bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, false);
            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial);
            bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, ConstantBaseApp.IO_SERIAL_EDIT);
            //
            mView.callAct053(bundle);
        }

    }

    private MD_Product_Serial getSerial(HMAux item) {
        MD_Product_Serial serial = null;
        try {
            serial = serialDao.getByString(
                new MD_Product_Serial_Sql_009(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    Long.parseLong(item.get(IO_Inbound_ItemDao.PRODUCT_CODE)),
                    Integer.parseInt(item.get(IO_Inbound_ItemDao.SERIAL_CODE))
                ).toSqlQuery()
            );
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
        //
        return serial;
    }

    @Override
    public void checkForUpdateRequired(int mPrefix, int mCode) {
        if (ToolBox_Con.isOnline(context)) {
            HMAux auxUpdate = inboundDao.getByStringHM(
                new IO_Inbound_Sql_011(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    mPrefix,
                    mCode
                ).toSqlQuery()
            );
            //
            if (auxUpdate != null && auxUpdate.hasConsistentValue(IO_Inbound_Sql_010.HAS_UPDATE_TO_DO)) {
                if (auxUpdate.get(IO_Inbound_Sql_010.HAS_UPDATE_TO_DO).equals("0")
                ) {
                    mView.callAct062();
                } else {
                    executeWsSaveItem();
                }
            }else{
                mView.callAct062();
            }
        }else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void executeWsSaveItem() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Inbound_Item_Save.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("progress_save_inbound_item_ttl"),
                hmAux_Trans.get("progress_save_inbound_item_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Inbound_Item_Save.class);
            Bundle bundle = new Bundle();
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processItemSaveReturn(int mPrefix, int mCode, String jsonRet) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn> actReturnList = null;
        ArrayList<HMAux> resultList = new ArrayList<>();
        try{
            actReturnList  = gson.fromJson(
                jsonRet,
                new TypeToken<ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn>>(){
                }.getType() );
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
        //
        if(actReturnList != null && actReturnList.size() > 0){
            boolean inboundResult = true;
            int inboundNextIdx = 0;
            HMAux auxResult = new HMAux();
            //Monta lista por inbound
            for (WS_IO_Inbound_Item_Save.InboundItemSaveActReturn actReturn : actReturnList) {
                String inboundCode = "";
                //
                if(actReturn.isMove()){
                    IO_Move ioMove =
                        moveDao.getByString(
                            new IO_Move_Order_Item_Sql_001(
                                actReturn.getCustomer_code(),
                                actReturn.getPrefix(),
                                actReturn.getCode()
                            ).toSqlQuery()
                        );
                    if(ioMove != null){
                        inboundCode = ioMove.getInbound_prefix()+"."+ioMove.getInbound_code();
                    }
                }else{
                    inboundCode = actReturn.getPrefix() +"."+actReturn.getCode();
                }
                if(!auxResult.containsKey(inboundCode)
                   ||(auxResult.containsKey(inboundCode)
                        && !actReturn.getRetStatus().equals("OK")
                    )
                ) {
                    auxResult.put(inboundCode, actReturn.getRetStatus());
                }
            }
            //For no resumido por inbound montando msg a ser exibida
            for(Map.Entry<String, String> item : auxResult.entrySet()){
                String inboundPk = mPrefix+"."+mCode;
                HMAux hmAux = new HMAux();
                //
                //Monta HmAux
                hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("inbound_lbl") );
                hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.getKey());
                hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1,item.getValue());
                //
                if(item.getKey().equals(inboundPk)){
                    inboundResult = item.getValue().equals("OK");
                    resultList.add(inboundNextIdx,hmAux);
                    inboundNextIdx++;
                }else{
                    resultList.add(hmAux);
                }

            }
            //
            mView.showResult(resultList,inboundResult);
        }
    }

    private String formatInboundInfo(WS_IO_Inbound_Item_Save.InboundItemSaveActReturn actReturn){
        return actReturn.getPrefix() +"."+ actReturn.getCode()
            +( !actReturn.isMove() ?"."+actReturn.getItem(): "");
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct056();
    }
}
