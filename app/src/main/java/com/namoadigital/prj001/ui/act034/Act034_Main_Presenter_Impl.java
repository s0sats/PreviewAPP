package com.namoadigital.prj001.ui.act034;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Main_Presenter_Impl implements Act034_Main_Presenter {

    private Context context;
    private Act034_Main_View mView;
    private HMAux hmAux_Trans;

    public Act034_Main_Presenter_Impl(Context context, Act034_Main_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
