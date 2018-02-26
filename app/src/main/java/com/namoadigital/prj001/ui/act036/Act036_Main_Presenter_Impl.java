package com.namoadigital.prj001.ui.act036;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.receiver.WBR_AP_Search;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act036_Main_Presenter_Impl implements Act036_Main_Presenter {

    private Context context;
    private Act036_Main_View mView;
    private HMAux hmAux_Trans;

    public Act036_Main_Presenter_Impl(Context context, Act036_Main_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getPendencies() {
        mView.setPendencies(4, String.valueOf(4));
    }

    @Override
    public void getSync() {
        mView.setSync(5);
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
