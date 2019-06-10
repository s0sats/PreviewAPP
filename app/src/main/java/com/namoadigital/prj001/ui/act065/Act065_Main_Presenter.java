package com.namoadigital.prj001.ui.act065;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

public class Act065_Main_Presenter implements Act065_Main_Contract.I_Presenter  {
    Context context;
    Act065_Main_Contract.I_View mView;
    HMAux hmAux_trans;

    public Act065_Main_Presenter(Context context, Act065_Main_Contract.I_View mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
    }


}
