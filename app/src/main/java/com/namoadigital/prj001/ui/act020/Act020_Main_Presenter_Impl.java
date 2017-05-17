package com.namoadigital.prj001.ui.act020;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;

/**
 * Created by d.luche on 17/05/2017.
 */

public class Act020_Main_Presenter_Impl implements Act020_Main_Presenter{

    private Context context;
    private Act020_Main_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private GE_Custom_Form_LocalDao formLocalDao;

    public Act020_Main_Presenter_Impl(Context context, Act020_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_LocalDao formLocalDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.formLocalDao = formLocalDao;
    }


    @Override
    public void getProductSerialList() {

    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct006(context);
    }
}
