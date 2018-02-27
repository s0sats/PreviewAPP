package com.namoadigital.prj001.ui.act036;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_002;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.receiver.WBR_AP_Search;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act036_Main_Presenter_Impl implements Act036_Main_Presenter {

    private Context context;
    private Act036_Main_View mView;
    private HMAux hmAux_Trans;

    private GE_Custom_Form_ApDao ge_custom_form_apDao;

    public Act036_Main_Presenter_Impl(Context context, Act036_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_ApDao ge_custom_form_apDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.ge_custom_form_apDao = ge_custom_form_apDao;
    }

    @Override
    public void getPendencies() {

        String qty = ge_custom_form_apDao.getByStringHM(
                new GE_Custom_Form_Ap_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        ).get(GE_Custom_Form_Ap_Sql_001.BADGE_IN_PROCESSING_QTY);


        mView.setPendencies(Integer.parseInt(qty), qty);
    }

    @Override
    public void getSync() {
        String qty = ge_custom_form_apDao.getByStringHM(
                new GE_Custom_Form_Ap_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        ).get(GE_Custom_Form_Ap_Sql_002.BADGE_SYNC_REQUIRED_QTY);

        mView.setSync(Integer.parseInt(qty));
    }

    @Override
    public void executeApSyncWs() {
        mView.showPD(
                hmAux_Trans.get("progress_sync_ap_ttl"),
                hmAux_Trans.get("progress_sync_ap_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_AP_Search.class);
        Bundle bundle = new Bundle();
        bundle.putInt(GE_Custom_Form_ApDao.SYNC_REQUIRED,1);
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
