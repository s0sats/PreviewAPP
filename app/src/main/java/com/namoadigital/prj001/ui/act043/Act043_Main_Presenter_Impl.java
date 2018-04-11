package com.namoadigital.prj001.ui.act043;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

public class Act043_Main_Presenter_Impl implements Act043_Main_Presenter {

    private Context context;
    private Act043_Main_View mView;
    private HMAux hmAux_Trans;

    public Act043_Main_Presenter_Impl(Context context, Act043_Main mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct027(context);
    }
}
