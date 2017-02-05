package com.namoadigital.prj001.ui.act009;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.ui.act010.Act010_Main_View;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act009_Main_Presenter_Impl implements Act009_Main_Presenter{

    private Context context;
    private Act009_Main_View mView;

    private EV_Module_Res_Txt_TransDao module_res_txt_transDao;
    private GE_Custom_Form_TypeDao custom_form_typeDao;

    public Act009_Main_Presenter_Impl(Context context, Act009_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_Form_TypeDao custom_form_typeDao) {
        this.context = context;
        this.mView = mView;

        this.module_res_txt_transDao = module_res_txt_transDao;
        this.custom_form_typeDao = custom_form_typeDao;
    }

    @Override
    public void setAdapterData(long product_code, String filter) {


        ArrayList<HMAux> data = new ArrayList<>();

        mView.loadForm_Types(data);

    }

    @Override
    public void onFormTypeClicked(String product_code) {
        //mView.callAct008(context, product_code);
    }

    @Override
    public void onBackPressedClicked() {

    }
}
