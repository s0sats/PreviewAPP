package com.namoadigital.prj001.ui.act043;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SM_SODao;

public class Act043_Main_Presenter_Impl implements Act043_Main_Presenter {

    private Context context;
    private Act043_Main_View mView;
    private HMAux hmAux_Trans;
    private SM_SODao smSoDao;

    public Act043_Main_Presenter_Impl(Context context, Act043_Main mView, HMAux hmAux_Trans, SM_SODao smSoDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.smSoDao = smSoDao;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct027(context);
    }
}
