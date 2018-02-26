package com.namoadigital.prj001.ui.act038;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act038_Main_Presenter_Impl implements Act038_Main_Presenter {

    private Context context;
    private Act038_Main_View mView;
    private HMAux hmAux_Trans;

    private GE_Custom_Form_ApDao ge_custom_form_apDao;

    public Act038_Main_Presenter_Impl(Context context, Act038_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_ApDao ge_custom_form_apDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.ge_custom_form_apDao = ge_custom_form_apDao;
    }

    @Override
    public void getloadAP() {

        mView.loadAP(null);
    }
}
