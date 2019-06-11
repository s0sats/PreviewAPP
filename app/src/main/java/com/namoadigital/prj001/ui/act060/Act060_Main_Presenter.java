package com.namoadigital.prj001.ui.act060;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

public class Act060_Main_Presenter implements Act060_Main_Contract.I_Presenter  {
    Context context;
    Act060_Main mView;
    HMAux hmAux_trans;

    public Act060_Main_Presenter(Context context, Act060_Main mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
    }


}
