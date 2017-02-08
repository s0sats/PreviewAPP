package com.namoadigital.prj001.ui.act011;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.ui.act009.Act009_Main_View;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act011_Main_Presenter_Impl implements Act011_Main_Presenter {

    private Context context;
    private Act011_Main_View mView;

    private EV_Module_Res_Txt_TransDao module_res_txt_transDao;

    private GE_Custom_FormDao custom_formDao;
    private GE_Custom_Form_FieldDao custom_form_fieldDao;

    private GE_Custom_Form_DataDao custom_form_dataDao;
    private GE_Custom_Form_Data_FieldDao custom_form_data_fieldDao;

    private GE_Custom_Form_LocalDao custom_form_LocalDao;
    private GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao;


    public Act011_Main_Presenter_Impl(Context context, Act011_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_FormDao custom_formDao, GE_Custom_Form_FieldDao custom_form_fieldDao, GE_Custom_Form_DataDao custom_form_dataDao, GE_Custom_Form_Data_FieldDao custom_form_data_fieldDao, GE_Custom_Form_LocalDao custom_form_LocalDao, GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao) {
        this.context = context;
        this.mView = mView;
        this.module_res_txt_transDao = module_res_txt_transDao;
        this.custom_formDao = custom_formDao;
        this.custom_form_fieldDao = custom_form_fieldDao;
        this.custom_form_dataDao = custom_form_dataDao;
        this.custom_form_data_fieldDao = custom_form_data_fieldDao;
        this.custom_form_LocalDao = custom_form_LocalDao;
        this.custom_form_field_LocalDao = custom_form_field_LocalDao;
    }

    @Override
    public void setData(String customer_code, String formtype_code, String form_code, String formversion_code) {

        GE_Custom_Form_Local formLocal = custom_form_LocalDao.getByString(
                new GE_Custom_Form_Local_Sql_003(
                        "1",
                        "22",
                        "2",
                        "1"
                ).toSqlQuery().toString().toLowerCase()
        );

        List<HMAux> cf_fields = null;

        if (formLocal != null) {
            // puxar campos
        } else {
            // cricar cabelhaco
            // criar campos
        }

        mView.loadFragment_CF_Fields(cf_fields);
    }

    @Override
    public void onBackPressedClicked() {

    }
}
