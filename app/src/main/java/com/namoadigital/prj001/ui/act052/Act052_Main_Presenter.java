package com.namoadigital.prj001.ui.act052;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.receiver.WBR_IO_Serial_Process_Download;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Download;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act052_Main_Presenter implements Act052_Main_Contract.I_Presenter {
    private Context context;
    private Act052_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_Serial_Process_Record record;

    public Act052_Main_Presenter(Context context, Act052_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct051();
    }

    @Override
    public void defineIOSerialFlow(HMAux hmAuxRet) {
        if(hmAuxRet.hasConsistentValue(WS_IO_Serial_Process_Download.HMAUX_PROCESS_KEY)) {
            String processType = hmAuxRet.get(WS_IO_Serial_Process_Download.HMAUX_PROCESS_KEY);
            //
            switch (processType) {
                case ConstantBaseApp.IO_PROCESS_IN_CONF:
                    Toast.makeText(context, "IN_CONF", Toast.LENGTH_SHORT).show();
                    break;
                case ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY:
                    Toast.makeText(context, "IN_PUT_AWAY", Toast.LENGTH_SHORT).show();
                    break;
                case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                    Toast.makeText(context, "MOVE_PLANNED", Toast.LENGTH_SHORT).show();
                    break;
                case ConstantBaseApp.IO_PROCESS_MOVE:
                    Toast.makeText(context, "MOVE", Toast.LENGTH_SHORT).show();
                    break;
                case ConstantBaseApp.IO_PROCESS_OUT_PICKING:
                    Toast.makeText(context, "OUT_PICKING", Toast.LENGTH_SHORT).show();
                    break;
                case ConstantBaseApp.IO_PROCESS_OUT_CONF:
                    Toast.makeText(context, "OUT_CONF", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    @Override
    public void executeWsProcessDownload(IO_Serial_Process_Record data) {
        if(ToolBox_Con.isOnline(context)){
            mView.setWsProcess(WS_IO_Serial_Process_Download.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_process_download_ttl"),
                    hmAux_Trans.get("dialog_process_download_starting_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Serial_Process_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(data.getProduct_code()));
            bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(data.getSerial_code()));
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            //
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }

    }
}
