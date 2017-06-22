package com.namoadigital.prj001.ui.act021;

import android.content.Context;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main_Presenter_Impl implements Act021_Main_Presenter {


    private Context context;
    private Act021_Main_View mView;


    public Act021_Main_Presenter_Impl(Context context, Act021_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
