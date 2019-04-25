package com.namoadigital.prj001.ui.act061;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.T_IO_From_Site_Search_Rec;
import com.namoadigital.prj001.model.T_IO_Master_Data_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_From_Site_Search;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Header_Save;
import com.namoadigital.prj001.receiver.WBR_IO_Master_Data;
import com.namoadigital.prj001.service.WS_IO_From_Site_Search;
import com.namoadigital.prj001.service.WS_IO_Inbound_Header_Save;
import com.namoadigital.prj001.service.WS_IO_Master_Data;
import com.namoadigital.prj001.sql.IO_Inbound_Sql_002;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_006;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act061_Main_Presenter implements Act061_Main_Contract.I_Presenter {

    private Context context;
    private Act061_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_InboundDao inboundDao;

    public Act061_Main_Presenter(Context context, Act061_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.inboundDao = new IO_InboundDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
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
        if(ToolBox_Con.isOnline(context)){
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
            bundle.putString(ConstantBaseApp.IO_ACTION_KEY, bNewIo ? ConstantBaseApp.IO_ACTION_NEW : ConstantBaseApp.IO_ACTION_EDIT );
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }


    @Override
    public void processIOMasterDataRet(String wsReturn) {
        if(wsReturn != null && !wsReturn.trim().isEmpty()){
            Gson gson = new GsonBuilder().serializeNulls().create();
            //
            try{
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
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
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
        if(ToolBox_Con.isOnline(context)){
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
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processFromOutboundRet(String wsReturn) {
        if(wsReturn != null && !wsReturn.trim().isEmpty()){
            Gson gson = new GsonBuilder().serializeNulls().create();
            //
            try{
                T_IO_From_Site_Search_Rec rec = gson.fromJson(
                    wsReturn,
                    T_IO_From_Site_Search_Rec.class
                );
                //
                mView.setFromOutboundList(
                    rec.getOutbound()
                );
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
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
        if(ToolBox_Con.isOnline(context)){
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
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processHeaderSave(int mPrefix, int mCode, String actReturnJson) {
        WS_IO_Inbound_Header_Save.InboundHeaderSaveActReturn retObj = null;

        //
        if(actReturnJson == null || actReturnJson.length() == 0){
            mView.showAlert(
                hmAux_Trans.get("alert_header_save_error_ttl"),
                hmAux_Trans.get("alert_header_save_no_return_msg")
            );
        }else{
            try{
                Gson gson = new GsonBuilder().serializeNulls().create();
                  //
                retObj =
                    gson.fromJson(
                        actReturnJson,
                        WS_IO_Inbound_Header_Save.InboundHeaderSaveActReturn.class
                    );

            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
                //
                mView.showAlert(
                    hmAux_Trans.get("alert_header_save_error_ttl"),
                    hmAux_Trans.get("alert_header_save_process_error_msg")
                );
            }
            //
            if(retObj != null) {
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
        try{
            io_move = ioMoveDao.getByString(
                new IO_Move_Order_Item_Sql_006(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        item.get(IO_Inbound_ItemDao.INBOUND_PREFIX),
                        item.get(IO_Inbound_ItemDao.INBOUND_CODE),
                        item.get(IO_Inbound_ItemDao.INBOUND_ITEM)
                ).toSqlQuery()
        );
        }catch (NullPointerException e ){
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
    public void onBackPressedClicked() {
        mView.callAct056();
    }
}
