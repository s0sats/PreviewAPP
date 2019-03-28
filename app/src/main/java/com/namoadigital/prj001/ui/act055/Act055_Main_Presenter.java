package com.namoadigital.prj001.ui.act055;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Act055_Main_Presenter implements Act055_Main_Contract.I_Presenter {

    private Context context;
    private Act055_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;

    public Act055_Main_Presenter(Context context, Act055_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act){
            case ConstantBaseApp.ACT054:
            default:
                mView.callAct054();
                break;
        }
    }


}
