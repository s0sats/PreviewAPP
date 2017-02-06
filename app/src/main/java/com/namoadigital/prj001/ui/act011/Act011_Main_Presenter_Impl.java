package com.namoadigital.prj001.ui.act011;

import android.content.Context;

import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.ui.act009.Act009_Main_View;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act011_Main_Presenter_Impl implements Act011_Main_Presenter {

    private Context context;
    private Act011_Main_View mView;

    private EV_Module_Res_Txt_TransDao module_res_txt_transDao;

    private GE_Custom_FormDao custom_formDao;
    private GE_Custom_Form_FieldDao custom_form_fieldDao;

    // Criar
    //private GE_Custom_FormDao custom_form_LocalDao;
    //private GE_Custom_Form_FieldDao custom_form_field_LocalDao;

    private GE_Custom_Form_DataDao ge_custom_form_dataDao;
    private GE_Custom_Form_Data_FieldDao ge_custom_form_data_fieldDao;

    public Act011_Main_Presenter_Impl(Context context, Act011_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_FormDao custom_formDao, GE_Custom_Form_FieldDao custom_form_fieldDao, GE_Custom_Form_DataDao ge_custom_form_dataDao, GE_Custom_Form_Data_FieldDao ge_custom_form_data_fieldDao) {
        this.context = context;
        this.mView = mView;
        this.module_res_txt_transDao = module_res_txt_transDao;
        this.custom_formDao = custom_formDao;
        this.custom_form_fieldDao = custom_form_fieldDao;
        this.ge_custom_form_dataDao = ge_custom_form_dataDao;
        this.ge_custom_form_data_fieldDao = ge_custom_form_data_fieldDao;
    }

    @Override
    public void setData(String customer_code, String formtype_code, String form_code, String formversion_code, String translate_code) {

    }

    @Override
    public void onBackPressedClicked() {

    }
}
