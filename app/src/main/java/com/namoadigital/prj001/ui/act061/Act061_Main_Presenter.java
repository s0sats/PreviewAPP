package com.namoadigital.prj001.ui.act061;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.T_IO_Master_Data_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Master_Data;
import com.namoadigital.prj001.service.WS_IO_Master_Data;
import com.namoadigital.prj001.sql.IO_Inbound_Sql_002;
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
    public void onBackPressedClicked() {
        mView.callAct056();
    }
}
