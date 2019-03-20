package com.namoadigital.prj001.ui.act052;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act052_Main_Presenter implements Act052_Main_Contract.I_Presenter {
    private Context context;
    private Act052_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_Serial_Process_Record record;

    private static final String IN_CONF  = "IN_CONF";
    private static final String IN_PUT_AWAY  = "IN_PUT_AWAY";
    private static final String MOVE_PLANNED  = "MOVE_PLANNED";
    private static final String MOVE  = "MOVE";
    private static final String OUT_PICKING = "OUT_PICKING";
    private static final String OUT_CONF  = "OUT_CONF";
    private MD_Product_Serial tProductSerial;
    private CH_RoomDao syncChecklistDao;

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
    public void defineIOSerialFlow(IO_Serial_Process_Record data) {
        switch (data.getProcess_type()){
            case IN_CONF:
                Toast.makeText(context, "IN_CONF", Toast.LENGTH_SHORT).show();
                break;
            case IN_PUT_AWAY:
                Toast.makeText(context, "IN_PUT_AWAY", Toast.LENGTH_SHORT).show();
                break;
            case MOVE_PLANNED:
                Toast.makeText(context, "MOVE_PLANNED", Toast.LENGTH_SHORT).show();
                break;
            case MOVE:
                Toast.makeText(context, "MOVE", Toast.LENGTH_SHORT).show();
                break;
            case OUT_PICKING:
                Toast.makeText(context, "OUT_PICKING", Toast.LENGTH_SHORT).show();
                break;
            case OUT_CONF:
                Toast.makeText(context, "OUT_CONF", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void defineFlow(MD_Product_Serial productSerial, boolean no_serial) {
        tProductSerial = productSerial;
        //
        if (!hasSyncRegister()) {
            if (ToolBox_Con.isOnline(context)) {
                executeSyncProcess();
            } else {
                //ToolBox_Inf.showNoConnectionDialog(context);
                if(no_serial) {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_connection_no_form_found_ttl"),
                            hmAux_Trans.get("alert_no_form_found_msg"),
                            null,
                            0
                    );
                }else{
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_form_found_ttl"),
                            hmAux_Trans.get("alert_no_form_but_go_to_serial_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    prepareAct008();
                                }
                            },
                            1
                    );
                }

            }
        } else {
            //Se for um criação sem serial, chama metodo que encaminha para lista de tipo de formulários.
            if(no_serial){
//                prepareAct009();
            }else{
//                prepareAct008();
            }

        }
    }

    private void executeSyncProcess() {
        if (ToolBox_Con.isOnline(context)) {

            mView.setWs_process(WS_Sync.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_sync_title"),
                    hmAux_Trans.get("progress_sync_msg")
            );

            ArrayList<String> data_package = new ArrayList<>();
            data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
            //
            Intent mIntent = new Intent(context, WBR_Sync.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
            bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
            bundle.putLong(Constant.GS_PRODUCT_CODE, tProductSerial.getProduct_code());
            bundle.putInt(Constant.GC_STATUS_JUMP, 1);
            bundle.putInt(Constant.GC_STATUS, 1);

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    public boolean hasSyncRegister() {
        List<HMAux> syncChecklists = checkSyncChecklist();

        if (syncChecklists == null || syncChecklists.size() == 0) {
            return false;
        }
        return true;
    }

    private List<HMAux> checkSyncChecklist() {
        List<HMAux> hmAuxList =
                syncChecklistDao.query_HM(
                        new Sync_Checklist_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                tProductSerial.getProduct_code()
                        ).toSqlQuery()
                );

        return hmAuxList;
    }


}
