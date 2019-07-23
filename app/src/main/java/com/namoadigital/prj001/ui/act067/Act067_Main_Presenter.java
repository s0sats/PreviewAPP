package com.namoadigital.prj001.ui.act067;

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
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.T_IO_Master_Data_Rec;
import com.namoadigital.prj001.model.T_IO_Outbound_Item_Env;
import com.namoadigital.prj001.receiver.WBR_IO_Master_Data;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Download;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Header_Save;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Master_Data;
import com.namoadigital.prj001.service.WS_IO_Outbound_Download;
import com.namoadigital.prj001.service.WS_IO_Outbound_Header_Save;
import com.namoadigital.prj001.service.WS_IO_Outbound_Item_Save;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_001;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_014;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_002;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_010;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_011;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;

import com.namoadigital.prj001.sql.Sql_Act067_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

class Act067_Main_Presenter implements Act067_Main_Contract.I_Presenter{

    private Context context;
    private Act067_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_OutboundDao outboundDao;
    private MD_Product_SerialDao serialDao;
    private IO_MoveDao moveDao;

    public Act067_Main_Presenter(Context context, Act067_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.outboundDao = new IO_OutboundDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.serialDao = new MD_Product_SerialDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.moveDao = new IO_MoveDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }


    @Override
    public IO_Outbound getOutbound(int prefix, int code) {
        IO_Outbound ioOutbound = outboundDao.getByString(
                new IO_Outbound_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        prefix,
                        code
                ).toSqlQuery()
        );
        //
        checkWaitingSyncStatusChange(ioOutbound);
        //
        return ioOutbound;
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
            bundle.putString(IO_OutboundDao.TO_TYPE, type);
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
    public void executeWsSaveOutboundHeader(IO_Outbound mOutbound, boolean newProcess) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Outbound_Header_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_outbound_header_save_ttl"),
                    hmAux_Trans.get("dialog_outbound_header_save_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Outbound_Header_Save.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantBaseApp.IO_OBJ_KEY, mOutbound);
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
        WS_IO_Outbound_Header_Save.OutboundHeaderSaveActReturn retObj = null;

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
                                WS_IO_Outbound_Header_Save.OutboundHeaderSaveActReturn.class
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
                            retObj.getOutbound_prefix(),
                            retObj.getOutbound_code(),
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
    public void processPickingMove(String outboundStatus, HMAux item) {

        if(outboundStatus.equals(ConstantBaseApp.SYS_STATUS_PENDING)){
            mView.showAlert(
                hmAux_Trans.get("alert_not_processing_status_ttl"),
                hmAux_Trans.get("alert_not_processing_status_msg")
            );
        }else {

            Bundle bundle = new Bundle();
            IO_MoveDao ioMoveDao = new IO_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

            IO_Move io_move;
            try {
                io_move = ioMoveDao.getByString(
                    new IO_Move_Order_Item_Sql_014(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        item.get(IO_Outbound_ItemDao.OUTBOUND_PREFIX),
                        item.get(IO_Outbound_ItemDao.OUTBOUND_CODE),
                        item.get(IO_Outbound_ItemDao.OUTBOUND_ITEM)
                    ).toSqlQuery()
                );
            } catch (NullPointerException e) {
                e.printStackTrace();
                io_move = null;
            }
            //
            if(io_move != null) {
                bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT067);
                bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, io_move.getMove_type());
                bundle.putString(IO_MoveDao.MOVE_PREFIX, String.valueOf(io_move.getMove_prefix()));
                bundle.putString(IO_MoveDao.MOVE_CODE, String.valueOf(io_move.getMove_code()));
                bundle.putInt(MD_Product_SerialDao.PRODUCT_CODE, (int) io_move.getProduct_code());
                bundle.putInt(MD_Product_SerialDao.SERIAL_CODE, io_move.getSerial_code());

                mView.callAct058(bundle);
            }else{
                mView.showAlert(
                    hmAux_Trans.get("alert_picking_move_not_found_ttl"),
                    hmAux_Trans.get("alert_picking_move_not_found_msg")
                );
            }
        }
    }

    @Override
    public void processSerialEdition(HMAux item) {
        MD_Product_Serial serial = getSerial(item);
        //
        if (serial != null) {
            Bundle bundle = new Bundle();
            //
            bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, item.get(IO_Outbound_ItemDao.PRODUCT_CODE));
            bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(MD_Product_SerialDao.SERIAL_ID));
            bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, false);
            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial);
            bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, ConstantBaseApp.IO_SERIAL_EDIT);
            bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT067);
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
                            Long.parseLong(item.get(IO_Outbound_ItemDao.PRODUCT_CODE)),
                            Integer.parseInt(item.get(IO_Outbound_ItemDao.SERIAL_CODE))
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
            if (hasUpdateRequired(mPrefix,mCode)) {
                executeWsSaveItem();
            }else{
                mView.callAct062();
            }
        }else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    private boolean hasUpdateRequired(int mPrefix, int mCode){
        HMAux auxUpdate = outboundDao.getByStringHM(
                new IO_Outbound_Sql_011(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mPrefix,
                        mCode
                ).toSqlQuery()
        );
        //
        if (
                auxUpdate != null
                        && auxUpdate.hasConsistentValue(IO_Outbound_Sql_010.HAS_UPDATE_TO_DO)
                        && auxUpdate.get(IO_Outbound_Sql_010.HAS_UPDATE_TO_DO).equals("1")
        ){
            return true;
        }
        return  false;
    }

    @Override
    public void executeWsSaveItem() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Outbound_Item_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_save_outbound_item_ttl"),
                    hmAux_Trans.get("progress_save_outbound_item_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Outbound_Item_Save.class);
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
        ArrayList<WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn> actReturnList = null;
        ArrayList<HMAux> resultList = new ArrayList<>();
        try{
            actReturnList  = gson.fromJson(
                    jsonRet,
                    new TypeToken<ArrayList<WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn>>(){
                    }.getType() );
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
        //
        if(actReturnList != null && actReturnList.size() > 0){
            boolean outboundResult = true;
            int outboundNextIdx = 0;
            HMAux auxResult = new HMAux();
            //Monta lista por outbound
            for (WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn actReturn : actReturnList) {
                String outboundCode = "";
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
                        outboundCode = ioMove.getOutbound_prefix()+"."+ioMove.getOutbound_code();
                    }
                }else{
                    outboundCode = actReturn.getPrefix() +"."+actReturn.getCode();
                }
                if(!auxResult.containsKey(outboundCode)
                        ||(auxResult.containsKey(outboundCode)
                        && !actReturn.getRetStatus().equals("OK")
                )
                ) {
                    auxResult.put(outboundCode, actReturn.getRetStatus());
                }
            }
            //For no resumido por outbound montando msg a ser exibida
            for(Map.Entry<String, String> item : auxResult.entrySet()){
                String outboundPk = mPrefix+"."+mCode;
                HMAux hmAux = new HMAux();
                //
                //Monta HmAux
                hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("outbound_lbl") );
                hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.getKey());
                hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1,item.getValue());
                //
                if(item.getKey().equals(outboundPk)){
                    outboundResult = item.getValue().equals("OK");
                    resultList.add(outboundNextIdx,hmAux);
                    outboundNextIdx++;
                }else{
                    resultList.add(hmAux);
                }

            }
            //
            mView.showResult(resultList,outboundResult);
        }
    }

    private String formatOutboundInfo(WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn actReturn){
        return actReturn.getPrefix() +"."+ actReturn.getCode()
                +( !actReturn.isMove() ?"."+actReturn.getItem(): "");
    }

    @Override
    public void checkSyncProcess(IO_Outbound mOutbound) {

        if(hasUpdateRequired(mOutbound.getOutbound_prefix(),mOutbound.getOutbound_code()) || isOutboundInTokenFile(mOutbound.getOutbound_prefix(),mOutbound.getOutbound_code())){
            //Se itens pendentes de envio, chama o save que, se finalizado com sucesso,
            //retona outbound full. Nesse caso náo precisa baixar a Outbound depois
            //
            executeWsSaveItem();
        }else{
            //Se não tem update_required, apenas atualiza full
            String outboundList = mOutbound.getOutbound_prefix() +"."+ mOutbound.getOutbound_code();
            executeWsOutboundDownload(outboundList);
        }
    }

    private void executeWsOutboundDownload(String outboundList) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Outbound_Download.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_outbound_download_ttl"),
                    hmAux_Trans.get("dialog_outbound_download_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Outbound_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(IO_OutboundDao.OUTBOUND_CODE, outboundList);
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processDownloadReturn(int mPrefix, int mCode, HMAux hmAux) {
        if(hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_PROCESS_KEY)){
            if( hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_PREFIX_KEY)
                    && hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_CODE_KEY)
                    && hmAux.get(ConstantBaseApp.HMAUX_PREFIX_KEY).equals(String.valueOf(mPrefix))
                    && hmAux.get(ConstantBaseApp.HMAUX_CODE_KEY).equals(String.valueOf(mCode))
            ){
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_download_return_ttl"),
                        hmAux_Trans.get("alert_sync_ok_refresh_is_needed_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.prepareFullRefresh();
                            }
                        },
                        0
                );

            }else{
                mView.showAlert(
                        hmAux_Trans.get("alert_download_return_ttl"),
                        hmAux_Trans.get("alert_download_return_error_msg")
                );
            }

        }else{
            mView.showAlert(
                    hmAux_Trans.get("alert_download_return_ttl"),
                    hmAux_Trans.get("alert_download_return_error_msg")
            );
        }
    }

    @Override
    public boolean hasUpdateRequiredDbOrToken(int mPrefix, int mCode) {
        return hasUpdateRequired(mPrefix,mCode) || isOutboundInTokenFile(mPrefix,mCode);
    }

    /**
     * Analisa se inbound deve ser atualizado pro status wainting sync.
     *
     * @param ioOutbound - Inbound a ser carregada na tela.
     */
    private void checkWaitingSyncStatusChange(IO_Outbound ioOutbound) {
        //Se condições atendidas, altera status da inbound.
        if( ioOutbound != null
                && ioOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_PROCESS)
                && ioOutbound.getDone_automatic() == 1
                && allItemsDone(ioOutbound)
        ){
            ioOutbound.setStatus(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            DaoObjReturn daoObj = outboundDao.addUpdate(ioOutbound);
            if(daoObj.hasError()){
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_error_update_inbound_to_wainting_sync_ttl"),
                        hmAux_Trans.get("alert_error_update_inbound_to_wainting_sync_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressedClicked(mView.getRequestingAct(),false);
                            }
                        },
                        0
                );
            }
        }
    }

    /**
     * Verifica se todos os item foram finalizados.
     * @param mOutbound - Obj outbound carregado.
     * @return - True se todos os itens estiverem finalizados.
     */
    private boolean allItemsDone(IO_Outbound mOutbound) {
        HMAux hmAux = outboundDao.getByStringHM(
                new Sql_Act067_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mOutbound.getOutbound_prefix(),
                        mOutbound.getOutbound_code()
                ).toSqlQuery()
        );
        //
        if( hmAux != null
                && hmAux.hasConsistentValue(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)
                && hmAux.get(ConstantBaseApp.SYS_STATUS_WAITING_SYNC).equals("1")
        ){
            return true;
        }
        //
        return false;
    }

    private boolean isOutboundInTokenFile(int outbound_prefix, int outbound_code) {
        boolean retToken = false;
        File[] outboundToken = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_INBOUND_PREFIX);
        if(outboundToken != null && outboundToken.length > 0){
            try {
                Gson gson =  new GsonBuilder().serializeNulls().create();
                T_IO_Outbound_Item_Env env =
                        gson.fromJson(
                                ToolBox_Inf.getContents(outboundToken[0]),
                                T_IO_Outbound_Item_Env.class
                        );
                //
                if(env != null){
                    for(T_IO_Outbound_Item_Env.IO_Outbound_Header outboundHeader : env.getOutbound()){
                        if(
                                outboundHeader.getCustomer_code() == ToolBox_Con.getPreference_Customer_Code(context)
                                        && outboundHeader.getOutbound_prefix() == outbound_prefix
                                        && outboundHeader.getOutbound_code() == outbound_code
                        ){
                            retToken = true;
                            break;
                        }
                    }
                }else{
                    retToken = false;
                }
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
                retToken = false;
            }
        }
        //
        return retToken;
    }

    @Override
    public void onBackPressedClicked(final String requestAct, boolean headerInfoChanged) {
        if(!headerInfoChanged) {
            switch (requestAct) {
                case ConstantBaseApp.ACT012:
                    mView.callAct066();
                    break;
                case ConstantBaseApp.ACT014:
                    mView.callAct066();
                    break;
                case ConstantBaseApp.ACT052:
                    mView.callAct051();
                    break;
                case ConstantBaseApp.ACT066:
                    mView.callAct066();
                    break;
                case ConstantBaseApp.ACT065:
                default:
                    mView.callAct065();
                    break;
            }
        }  else {
        ToolBox.alertMSG_YES_NO(
                context,
                hmAux_Trans.get("alert_header_changes_will_be_lost_ttl"),
                hmAux_Trans.get("alert_header_changes_will_be_lost_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressedClicked(requestAct,false);
                    }
                },
                1
        );
    }

    }

}
