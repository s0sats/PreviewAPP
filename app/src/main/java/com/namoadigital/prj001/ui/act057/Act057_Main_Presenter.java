package com.namoadigital.prj001.ui.act057;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound_Search_Record;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Download;
import com.namoadigital.prj001.service.WS_IO_Inbound_Download;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act057_Main_Presenter implements Act057_Main_Contract.I_Presenter{

    private Context context;
    private Act057_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;

    public Act057_Main_Presenter(Context context, Act057_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void processListInfo(long record_count, long record_page, ArrayList<IO_Inbound_Search_Record> records) {
        mView.setRecordInfo();
        //
        if(records != null || records.size() > 0){
            mView.loadInboundList();
            //
//            if(records.size() == 1){
//               defineFlow();
//            }
        }
    }

    @Override
    public void executeInboundDownload(String inboundList) {
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
    public void processDownloadReturn(HMAux hmAux) {
        if(hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_PROCESS_KEY)){
            if(hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_PREFIX_KEY)
               && hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_CODE_KEY)
            ){
                Bundle bundle = new Bundle();
                bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY,hmAux.get(ConstantBaseApp.HMAUX_PROCESS_KEY));
                bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY,hmAux.get(ConstantBaseApp.HMAUX_PREFIX_KEY));
                bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY,hmAux.get(ConstantBaseApp.HMAUX_CODE_KEY));

                mView.callAct061(bundle);
            }else{
                mView.callAct062();
            }
        }else{
            mView.showAlert(
                    hmAux.get("alert_download_return_ttl"),
                    hmAux.get("alert_download_return_error_msg")
            );
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct056();
    }
}
