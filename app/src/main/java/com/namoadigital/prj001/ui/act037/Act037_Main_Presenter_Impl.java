package com.namoadigital.prj001.ui.act037;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act037_Main_Presenter_Impl implements Act037_Main_Presenter {

    private Context context;
    private Act037_Main_View mView;
    private HMAux hmAux_Trans;

    public Act037_Main_Presenter_Impl(Context context, Act037_Main_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }

}
