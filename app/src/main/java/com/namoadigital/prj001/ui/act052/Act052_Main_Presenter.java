package com.namoadigital.prj001.ui.act052;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;

public class Act052_Main_Presenter implements Act052_Main_Contract.I_Presenter {
    private Context context;
    private Act052_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_Serial_Process_Record record;

    public Act052_Main_Presenter(Context context, Act052_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct051();
    }
}
