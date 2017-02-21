package com.namoadigital.prj001.ui.act005;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act005_Main_Presenter_Impl implements Act005_Main_Presenter {

    private Context context;
    private Act005_Main_View mView;
    private HMAux item;


    public Act005_Main_Presenter_Impl(Context context, Act005_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }

    String[] menuId = {
            Act005_Main.MENU_ID_CHECKLIST,
            Act005_Main.MENU_ID_PENDING_DATA,
            Act005_Main.MENU_ID_SEND_DATA,
            Act005_Main.MENU_ID_SYNC_DATA,
            Act005_Main.MENU_ID_CLOSE
    };

    String[] menuDesc = {
            "lbl_checklist",
            "lbl_pending_data",
            "lbl_send_data",
            "lbl_sync_data",
            "lbl_close_app"
    };

    String[] icon ={
            String.valueOf(R.drawable.cloud_upload),
            String.valueOf(R.drawable.cloud_upload),
            String.valueOf(R.drawable.cloud_upload),
            String.valueOf(R.drawable.cloud_upload),
            String.valueOf(R.drawable.cloud_upload)
    };

    @Override
    public void getMenuItens(HMAux hmAux_Trans) {
        List<HMAux> menuList = new ArrayList<>();

        for (int i = 0; i < menuId.length;i++ ){
            HMAux Aux = new HMAux();
            Aux.put(Act005_Main.MENU_ID, menuId[i]);
            Aux.put(Act005_Main.MENU_ICON, icon[i]);
            Aux.put(Act005_Main.MENU_DESC,menuDesc[i]);
            menuList.add(Aux);

        }
        mView.loadMenu(menuList);
    }

    @Override
    public void executeSyncProcess(int jump_validation_UR) {

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_MAIN);
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP,ToolBox_Con.getPreference_Session_App(context));
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE,data_package);
        bundle.putInt(Constant.GC_STATUS_JUMP, jump_validation_UR);//Valida Update require
        bundle.putInt(Constant.GC_STATUS, 1);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", "Starting to sync ...", "", "0");

    }

    @Override
    public void accessMenuItem(String menu_id, int jump_validation_UR) {

        try {
            switch (menu_id){
                case Act005_Main.MENU_ID_CHECKLIST:
                    mView.callAct006(context);
                    break;

                case Act005_Main.MENU_ID_PENDING_DATA:
                    mView.callAct012(context);
                    break;

                case Act005_Main.MENU_ID_SEND_DATA:
                    if(ToolBox_Con.isOnline(context)){
                        mView.showPD();
                        executeSaveProcess();
                    }else{
                        mView.showNoConnectionDialog();
                    }

                    break;

                case Act005_Main.MENU_ID_SYNC_DATA:
                    if(ToolBox_Con.isOnline(context)) {
                        mView.showPD();
                        executeSyncProcess(jump_validation_UR);
                    }else{
                        mView.showNoConnectionDialog();
                    }
                    break;

                case Act005_Main.MENU_ID_CLOSE:
                    mView.closeApp();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeSaveProcess() {

        Intent mIntent = new Intent(context, WBR_Save.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);//Pula validação Update require
        bundle.putInt(Constant.GC_STATUS, 1);//Pula validação de other device

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", "Prepering to send form data ...", "", "0");

    }


}
