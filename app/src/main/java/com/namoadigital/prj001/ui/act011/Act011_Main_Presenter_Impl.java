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
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.sql.GE_Custom_Form_Fields_Local_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT;
import com.namoadigital.prj001.sql.Sql_Act011_002;
import com.namoadigital.prj001.ui.act009.Act009_Main_View;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
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

        GE_Custom_Form_Local customFormLocal = custom_form_LocalDao.getByString(
                new GE_Custom_Form_Local_Sql_003(
                        customer_code,
                        formtype_code,
                        form_code,
                        formversion_code
                ).toSqlQuery().toString().toLowerCase()
        );

        List<HMAux> cf_fields = null;

        if (customFormLocal != null) {

            cf_fields = (ArrayList<HMAux>) custom_form_field_LocalDao.query_HM(
                    new GE_Custom_Form_Fields_Local_Sql_001(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version()),
                            String.valueOf(customFormLocal.getCustom_form_data())
                    ).toSqlQuery().toString().toLowerCase()
            );

            int i = 10;

        } else {

            HMAux ii = custom_formDao.getByStringHM(
                    new GE_Custom_Form_Local_Sql_002().toSqlQuery().toLowerCase()
            );

            GE_Custom_Form customForm = custom_formDao.getByString(

                    new GE_Custom_Form_Sql_001_TT(
                            String.valueOf(customer_code),
                            String.valueOf(formtype_code),
                            String.valueOf(form_code),
                            String.valueOf(formversion_code)
                    ).toSqlQuery().toString().toLowerCase()

            );

            customFormLocal = new GE_Custom_Form_Local();

            customFormLocal.setCustomer_code(customForm.getCustomer_code());
            customFormLocal.setCustom_form_type(customForm.getCustom_form_type());
            customFormLocal.setCustom_form_code(customForm.getCustom_form_code());
            customFormLocal.setCustom_form_version(customForm.getCustom_form_version());
            customFormLocal.setCustom_form_data(Long.parseLong(ii.get("id")));
            customFormLocal.setCustom_form_pre("pre");
            customFormLocal.setCustom_form_status("0");
            customFormLocal.setCustom_form_src("0");
            customFormLocal.setCustom_product_desc("product description");
            customFormLocal.setCustom_form_type_desc("form type descrition");
            customFormLocal.setCustom_form_desc("form description");
            customFormLocal.setRequire_signature(customForm.getRequire_signature());

            custom_form_LocalDao.addUpdate(customFormLocal);

            ArrayList<HMAux> items = (ArrayList<HMAux>) custom_form_fieldDao.query_HM(
                    new Sql_Act011_002(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version()),
                            ToolBox_Con.getPreference_Translate_Code(context),
                            String.valueOf(customFormLocal.getCustom_form_data())
                    ).toSqlQuery().toString().toLowerCase()
            );

            custom_form_field_LocalDao.addUpdate(items);

            cf_fields = (ArrayList<HMAux>) custom_form_field_LocalDao.query_HM(
                    new GE_Custom_Form_Fields_Local_Sql_001(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version()),
                            String.valueOf(customFormLocal.getCustom_form_data())
                    ).toSqlQuery().toString().toLowerCase()
            );

        }

        mView.loadFragment_CF_Fields(cf_fields);
    }

    @Override
    public void onBackPressedClicked() {

    }
}
