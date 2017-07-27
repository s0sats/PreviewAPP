package com.namoadigital.prj001.ui.act032;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SM_SODao;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act032_Main_Presenter_Impl implements Act032_Main_Presenter {

    private Context context;
    private Act032_Main_View mView;
    private HMAux hmAux_Trans;

    private SM_SODao soDao;

    public Act032_Main_Presenter_Impl(Context context, Act032_Main_View mView, HMAux hmAux_Trans, SM_SODao soDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.soDao = soDao;
    }

    @Override
    public void onBackPressedAction() {
        mView.callAct027(context);
    }
}
