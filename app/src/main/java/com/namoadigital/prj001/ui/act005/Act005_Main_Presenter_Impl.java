package com.namoadigital.prj001.ui.act005;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.Sql_Act005_001;
import com.namoadigital.prj001.sql.Sql_Act005_002;
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
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private HMAux hmAux_Trans = new HMAux();

    public Act005_Main_Presenter_Impl(Context context, Act005_Main_View mView, GE_Custom_Form_LocalDao customFormLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    String[] menuId = {
            Act005_Main.MENU_ID_CHECKLIST,
            Act005_Main.MENU_ID_PENDING_DATA,
            Act005_Main.MENU_ID_HISTORIC_DATA,
            Act005_Main.MENU_ID_SEND_DATA,
     //       Act005_Main.MENU_ID_SYNC_DATA,
            Act005_Main.MENU_ID_CLOSE
    };

    String[] menuDesc = {
            "lbl_checklist",
            "lbl_pending_data",
            "lbl_historic_data",
            "lbl_send_data",
    //        "lbl_sync_data",
            "lbl_close_app"
    };

    String[] icon ={
            String.valueOf(R.drawable.ic_n_form),
            String.valueOf(R.drawable.ic_pendente),
            String.valueOf(R.drawable.ic_historico),
            String.valueOf(R.drawable.ic_enviar),
     //       String.valueOf(R.drawable.ic_sincronizar),
            String.valueOf(R.drawable.ic_sair)
    };



    @Override
    public void getMenuItens(HMAux hmAux_Trans) {
        List<HMAux> menuList = new ArrayList<>();

        for (int i = 0; i < menuId.length;i++ ){
            HMAux Aux = new HMAux();
            String qty = "";
            Aux.put(Act005_Main.MENU_ID, menuId[i]);
            Aux.put(Act005_Main.MENU_ICON, icon[i]);
            Aux.put(Act005_Main.MENU_DESC,menuDesc[i]);

            switch (menuId[i]){
                case Act005_Main.MENU_ID_PENDING_DATA:
                    qty = customFormLocalDao.getByStringHM(
                            new Sql_Act005_001(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                            ).toSqlQuery()
                    ).get(Sql_Act005_001.BADGE_IN_PROCESSING_QTY);

                    Aux.put(Act005_Main.MENU_BADGE,qty);
                    break;

                case Act005_Main.MENU_ID_SEND_DATA:
                    qty = customFormLocalDao.getByStringHM(
                            new Sql_Act005_002(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                            ).toSqlQuery()
                    ).get(Sql_Act005_002.BADGE_FINALIZED_QTY);

                    Aux.put(Act005_Main.MENU_BADGE,qty);
                    break;

                default:
                    Aux.put(Act005_Main.MENU_BADGE,qty);
                    break;
            }

            menuList.add(Aux);

        }
        mView.loadMenu(menuList);
    }

    @Override
    public void executeSyncProcess(int jump_validation_UR) {

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_MAIN);
        data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
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
        ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_start_sync"), "", "0");

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

                case Act005_Main.MENU_ID_HISTORIC_DATA:
                    mView.callAct014(context);
                    break;

                case Act005_Main.MENU_ID_SEND_DATA:
                    if(ToolBox_Con.isOnline(context)){
                        mView.setWsProcess(Act005_Main.WS_PROCESS_SEND);
                        mView.showPD();
                        executeSaveProcess();
                    }else{
                        mView.showNoConnectionDialog();
                    }

                    break;

                case Act005_Main.MENU_ID_SYNC_DATA:
                    if(ToolBox_Con.isOnline(context)) {
                        mView.setWsProcess(Act005_Main.WS_PROCESS_SYNC);
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
        ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_preparing_to_send_data"), "", "0");

    }


}
