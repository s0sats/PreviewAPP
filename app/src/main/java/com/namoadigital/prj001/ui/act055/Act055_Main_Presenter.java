package com.namoadigital.prj001.ui.act055;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.T_IO_Move_Download_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Move_Download;
import com.namoadigital.prj001.service.WS_IO_Move_Download;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act055_Main_Presenter implements Act055_Main_Contract.I_Presenter {

    private Context context;
    private Act055_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;

    public Act055_Main_Presenter(Context context, Act055_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act){
            case ConstantBaseApp.ACT054:
            default:
                mView.callAct054();
                break;
        }
    }

    @Override
    public void getDownloadedMove(String moveCode){
        mView.setWsProcess(WS_IO_Move_Download.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_serial_search_ttl"),
                hmAux_Trans.get("dialog_serial_search_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_IO_Move_Download.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(IO_MoveDao.MOVE_CODE, moveCode);
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("dialog_serial_search_start"), "", "0");
    }

    @Override
    public void processSearchReturn(HMAux searchRet) {

        try {
            if(searchRet.hasConsistentValue(Constant.HMAUX_PREFIX_KEY)
              && searchRet.hasConsistentValue(Constant.HMAUX_CODE_KEY)
            && searchRet.hasConsistentValue(Constant.HMAUX_PROCESS_KEY)){
                Bundle bundle = new Bundle();
                bundle.putString(IO_MoveDao.MOVE_PREFIX, searchRet.get(Constant.HMAUX_PREFIX_KEY));
                bundle.putString(IO_MoveDao.MOVE_CODE, searchRet.get(Constant.HMAUX_CODE_KEY));
                //

                switch (searchRet.get(Constant.HMAUX_PROCESS_KEY)){
                    case ConstantBaseApp.IO_INBOUND:
                        Toast.makeText(context,"type: "+searchRet.get(Constant.HMAUX_PROCESS_KEY), Toast.LENGTH_SHORT ).show();
                        break;
                    case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                        mView.callAct058(bundle);
                        break;
                    case ConstantBaseApp.IO_OUTBOUND:
                        Toast.makeText(context,"type: "+searchRet.get(Constant.HMAUX_PROCESS_KEY), Toast.LENGTH_SHORT ).show();
                        break;
                    default:
                        Log.d("Move_type", "type: "+searchRet.get(Constant.HMAUX_PROCESS_KEY));
                        break;
                }

            }else{
                mView.showAlert(
                        hmAux_Trans.get("alert_no_move_found_ttl"),
                        hmAux_Trans.get("alert_no_move_found_msg")
                );
            }

        }catch (Exception e){
            mView.showAlert(
                    hmAux_Trans.get("alert_error_on_processing_return_ttl"),
                    hmAux_Trans.get("alert_error_on_processing_return_msg")
            );
            //Gerar Exception ?!
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
    }

    @Override
    public void getOfflineMove(String moveKey) {

    }
}
