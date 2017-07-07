package com.namoadigital.prj001.ui.act021;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.ui.act006.Act006_Main;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main_Presenter_Impl implements Act021_Main_Presenter {


    private Context context;
    private Act021_Main_View mView;


    public Act021_Main_Presenter_Impl(Context context, Act021_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void defineFlow(HMAux item) {

        switch (item.get(Act006_Main.NEW_OPT_ID)){
            case Act006_Main.NEW_OPT_TP_PRODUCT:
                mView.callAct022(context);
                break;
            case Act006_Main.NEW_OPT_TP_SERIAL:
                mView.callAct025(context);
                break;
            case Act006_Main.NEW_OPT_TP_LOCATION:
            default:
                break;
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
