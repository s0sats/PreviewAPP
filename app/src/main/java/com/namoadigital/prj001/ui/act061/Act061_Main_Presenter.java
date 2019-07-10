package com.namoadigital.prj001.ui.act061;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class Act061_Main_Presenter implements Act061_Main_Contract.I_Presenter {

    private Context context;
    private Act061_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_InboundDao inboundDao;
    private MD_Product_SerialDao serialDao;
    private IO_MoveDao moveDao;
    private Gson gson;

    public Act061_Main_Presenter(Context context, Act061_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.inboundDao = new IO_InboundDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.serialDao = new MD_Product_SerialDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.moveDao = new IO_MoveDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.gson = new GsonBuilder().serializeNulls().create();
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
        checkWaitingSyncStatusChange(ioInbound);
        //
        return ioInbound;
    }

    /**
     * Analisa se inbound deve ser atualizado pro status wainting sync.
     *
     * @param ioInbound - Inbound a ser carregada na tela.
     */
    private void checkWaitingSyncStatusChange(IO_Inbound ioInbound) {
        //Se condições atendidas, altera status da inbound.
        if( ioInbound != null
            && ioInbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_PROCESS)
            && ioInbound.getDone_automatic() == 1
            && allItemsDone(ioInbound)
        ){
            ioInbound.setStatus(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            DaoObjReturn daoObj = inboundDao.addUpdate(ioInbound);
            if(daoObj.hasError()){
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_error_update_inbound_to_wainting_sync_ttl"),
                    hmAux_Trans.get("alert_error_update_inbound_to_wainting_sync_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressedClicked(mView.getRequestingAct());
                        }
                    },
                    0
                );
            }
        }
    }

    /**
     * Verifica se todos os item foram finalizados.
     * @param mInbound - Obj inbound carregado.
     * @return - True se todos os itens estiverem finalizados.
     */
    private boolean allItemsDone(IO_Inbound mInbound) {
        HMAux hmAux = inboundDao.getByStringHM(
                                    new Sql_Act061_002(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        mInbound.getInbound_prefix(),
                                        mInbound.getInbound_code()
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
    public void executeWsSearchOutbound(String from_site, String transportOrder) {
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
            bundle.putString(IO_InboundDao.TRANSPORT_ORDER, transportOrder);
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
            T_IO_From_Site_Search_Rec rec = null;
            //
            try {
                rec = gson.fromJson(
                    wsReturn,
                    T_IO_From_Site_Search_Rec.class
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
                //
                mView.showAlert(
                    hmAux_Trans.get("alert_from_outbound_error_ttl"),
                    hmAux_Trans.get("alert_from_outbound_error_msg")
                );
            }
            //
            if(rec != null){
                if(rec.getOutbound() != null && rec.getOutbound().size() > 0){
                    mView.setFromOutboundList(
                        rec.getOutbound()
                    );
                } else {
                    mView.showAlert(
                        hmAux_Trans.get("alert_from_outbound_ttl"),
                        hmAux_Trans.get("alert_from_outbound_not_found_msg")
                    );
                }
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
        HMAux auxUpdate = inboundDao.getByStringHM(
            new IO_Inbound_Sql_011(
                ToolBox_Con.getPreference_Customer_Code(context),
                mPrefix,
                mCode
            ).toSqlQuery()
        );
        //
        if (
            auxUpdate != null
            && auxUpdate.hasConsistentValue(IO_Inbound_Sql_010.HAS_UPDATE_TO_DO)
            && auxUpdate.get(IO_Inbound_Sql_010.HAS_UPDATE_TO_DO).equals("1")
        ){
            return true;
        }
        return  false;
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
    public void checkSyncProcess(IO_Inbound mInbound) {

         if(hasUpdateRequired(mInbound.getInbound_prefix(),mInbound.getInbound_code()) || isInboundInTokenFile(mInbound.getInbound_prefix(),mInbound.getInbound_code())){
             //Se itens pendentes de envio, chama o save que, se finalizado com sucesso,
             //retona inbound full. Nesse caso náo precisa baixar a Inbound depois
             //
             executeWsSaveItem();
         }else{
             //Se não tem update_required, apenas atualiza full
             String inboundList = mInbound.getInbound_prefix() +"."+ mInbound.getInbound_code();
             executeWsInboundDownload(inboundList);
         }
    }

    private void executeWsInboundDownload(String inboundList) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Inbound_Download.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_inbound_download_ttl"),
                hmAux_Trans.get("dialog_inbound_download_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Inbound_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(IO_InboundDao.INBOUND_CODE, inboundList);
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
       return hasUpdateRequired(mPrefix,mCode) || isInboundInTokenFile(mPrefix,mCode);
    }

    private boolean isInboundInTokenFile(int inbound_prefix, int inbound_code) {
        boolean retToken = false;
        File[] inboundToken = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_INBOUND_PREFIX);
        if(inboundToken != null && inboundToken.length > 0){
            try {
                T_IO_Inbound_Item_Env env =
                    gson.fromJson(
                        ToolBox_Inf.getContents(inboundToken[0]),
                        T_IO_Inbound_Item_Env.class
                    );
                //
                if(env != null){
                    for(T_IO_Inbound_Item_Env.IO_Inbound_Header inboundHeader : env.getInbound()){
                        if(
                            inboundHeader.getCustomer_code() == ToolBox_Con.getPreference_Customer_Code(context)
                            && inboundHeader.getInbound_prefix() == inbound_prefix
                            && inboundHeader.getInbound_code() == inbound_code
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
    public void executeWsSearchTransportOrder(String transportOrder) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Transport_Order_Out_Search.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_transport_order_search_ttl"),
                hmAux_Trans.get("dialog_transport_order_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Transport_Order_Out_Search.class);
            Bundle bundle = new Bundle();
            bundle.putString(IO_InboundDao.TRANSPORT_ORDER, transportOrder);
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processFromTransportOrderRet(String outboundJson) {
        if(outboundJson != null && !outboundJson.isEmpty() ) {
            ArrayList<IO_From_Outbound_Search_Record> outbounds = null;
            try {
                outbounds = gson.fromJson(
                                outboundJson,
                                new TypeToken<ArrayList<IO_From_Outbound_Search_Record>>(){
                                }.getType()
                            );

            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(),e);
                //
                mView.showAlert(
                    hmAux_Trans.get("alert_transport_order_return_ttl"),
                    hmAux_Trans.get("alert_on_processing_return_msg")
                );
            }
            //
            if(outbounds != null && outbounds.size() > 0){
                IO_From_Outbound_Search_Record outbound = outbounds.get(0);
                if(outbound.getCount() == 0){
                    mView.showAlert(
                        hmAux_Trans.get("alert_transport_order_return_ttl"),
                        hmAux_Trans.get("alert_outbound_not_found_msg")
                    );
                } else if(outbound.getCount() == 1) {
                    //Se encontrou outbound, verifica se to_site da outbound é igual ao site logado
                    //(que é o to_site da inbound)
                    if(ToolBox_Con.getPreference_Site_Code(context).equals(String.valueOf(outbound.getOutbound_to_site_code()))){
                        mView.setTransportOrderData(outbound);
                    } else{
                        mView.showAlert(
                            hmAux_Trans.get("alert_transport_order_return_ttl"),
                            hmAux_Trans.get("alert_outbound_to_another_site_msg")
                                +"\n" + hmAux_Trans.get("alert_outbound_destination_msg")
                                +"\n"+ outbound.getOutbound_to_site_desc()
                        );
                    }

                } else{
                    mView.showAlert(
                        hmAux_Trans.get("alert_transport_order_return_ttl"),
                        outbound.getCount() +" "+ hmAux_Trans.get("alert_x_outbound_founded_msg")
                    );
                }
            }else{
                mView.showAlert(
                    hmAux_Trans.get("alert_transport_order_return_ttl"),
                    hmAux_Trans.get("alert_outbound_not_found_msg")
                );
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_transport_order_return_ttl"),
                hmAux_Trans.get("alert_on_processing_return_msg")
            );
        }

    }



    @Override
    public void onBackPressedClicked(String requestingAct) {
        switch (requestingAct){
            //Quando o 52, retorna para tela de busca de Serial, act051
            case ConstantBaseApp.ACT052:
                mView.callAct051();
                break;
            case ConstantBaseApp.ACT057:
                mView.callAct057();
                break;
            case ConstantBaseApp.ACT056:
            case ConstantBaseApp.ACT058:
            case ConstantBaseApp.ACT059:
            default:
                mView.callAct056();
        }

    }
}
