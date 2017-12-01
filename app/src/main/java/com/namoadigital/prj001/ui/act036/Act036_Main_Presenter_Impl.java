package com.namoadigital.prj001.ui.act036;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.ui.act035.Act035_Main_Presenter;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act036_Main_Presenter_Impl implements Act035_Main_Presenter {

    private Context context;
    private Act036_Main_View mView;
    private HMAux hmAux_Trans;

    public Act036_Main_Presenter_Impl(Context context, Act036_Main_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }
}
