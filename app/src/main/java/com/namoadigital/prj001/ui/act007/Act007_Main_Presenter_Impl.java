package com.namoadigital.prj001.ui.act007;

import android.content.Context;

import com.namoadigital.prj001.ui.act006.Act006_Main_View;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act007_Main_Presenter_Impl implements Act007_Main_Presenter{

    private Context context;
    private Act007_Main_View mView;

    public Act007_Main_Presenter_Impl(Context context, Act007_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void setAdapterData(long product_code, String filter) {

    }

    @Override
    public void onCategoryProductClicked(String product_code) {
        mView.callAct008(context, product_code);
    }

    @Override
    public void onBtnHomeClicked() {
        setAdapterData(0, "");
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct006(context);
    }
}
