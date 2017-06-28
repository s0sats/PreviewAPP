package com.namoadigital.prj001.ui.act023;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 22/06/2017.
 */

public class Act023_Main_Presenter_Impl implements Act023_Main_Presenter {

    private Context context;
    private Act023_Main_View mView;
    private String requesting_process;
    private Bundle bundle;

    public Act023_Main_Presenter_Impl(Context context, Act023_Main_View mView, String requesting_process, Bundle bundle) {
        this.context = context;
        this.mView = mView;
        this.requesting_process = requesting_process;
        this.bundle = bundle;
    }

    @Override
    public void defineForwardFlow() {

        switch (requesting_process){

            case Constant.MODULE_CHECKLIST:
                //mView.callAct008(context,product_code);
                break;

            case Constant.MODULE_SO:default:
                mView.callAct024(context,bundle);
                break;

        }
    }

    @Override
    public void defineBackFlow() {
        switch (requesting_process){

            case Constant.MODULE_CHECKLIST:
                //mView.callAct008(context,product_code);
                mView.callAct022(context);
                break;

            case Constant.MODULE_SO:default:
                mView.callAct022(context);
                break;

        }
    }

    @Override
    public void executeSoSearch(int product_code, String serial_id, String so_mult) {

        Intent mIntent = new Intent(context, WBR_SO_Search.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.WS_SO_SEARCH_PRODUCT_CODE,product_code);
        bundle.putString(Constant.WS_SO_SEARCH_SERIAL_ID,serial_id);
        bundle.putString(Constant.WS_SO_SEARCH_SO_MULT, so_mult);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void onBackPressedClicked() {
        defineBackFlow();
    }
}
