package com.namoadigital.prj001.ui.act011;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_MULTI_SqlSpecification;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification;
import com.namoadigital.prj001.sql.GE_Custom_Form_Fields_Local_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT;
import com.namoadigital.prj001.sql.Sql_Act011_002;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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

    private GE_Custom_Form_BlobDao custom_form_blobDao;
    private GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao;


    public Act011_Main_Presenter_Impl(Context context, Act011_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_FormDao custom_formDao, GE_Custom_Form_FieldDao custom_form_fieldDao, GE_Custom_Form_DataDao custom_form_dataDao, GE_Custom_Form_Data_FieldDao custom_form_data_fieldDao, GE_Custom_Form_LocalDao custom_form_LocalDao, GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao, GE_Custom_Form_BlobDao custom_form_blobDao, GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao) {
        this.context = context;
        this.mView = mView;
        this.module_res_txt_transDao = module_res_txt_transDao;
        this.custom_formDao = custom_formDao;
        this.custom_form_fieldDao = custom_form_fieldDao;
        this.custom_form_dataDao = custom_form_dataDao;
        this.custom_form_data_fieldDao = custom_form_data_fieldDao;
        this.custom_form_LocalDao = custom_form_LocalDao;
        this.custom_form_field_LocalDao = custom_form_field_LocalDao;
        this.custom_form_blobDao = custom_form_blobDao;
        this.custom_form_blob_localDao = custom_form_blob_localDao;
    }

    @Override
    public void setData(String customer_code, String formtype_code, String form_code, String formversion_code, String product_code) {

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
            customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
            customFormLocal.setCustom_form_status("0");
            customFormLocal.setCustom_form_src("0");
            customFormLocal.setCustom_product_code(1);
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

            custom_form_blob_localDao.addUpdate(
                    custom_form_blob_localDao.query(
                            new GE_Custom_Form_Blob_Sql_001(
                                    String.valueOf(customFormLocal.getCustomer_code()),
                                    String.valueOf(customFormLocal.getCustom_form_type()),
                                    String.valueOf(customFormLocal.getCustom_form_code()),
                                    String.valueOf(customFormLocal.getCustom_form_version())
                            ).toSqlQuery().toString().toLowerCase()
                    )
                    ,
                    false
            );
        }

        GE_Custom_Form_Data formData = loadAnswer(
                customFormLocal.getCustomer_code(),
                Long.parseLong(product_code),
                customFormLocal.getCustom_form_type(),
                customFormLocal.getCustom_form_code(),
                customFormLocal.getCustom_form_version(),
                customFormLocal.getCustom_form_data()
        );

        mView.loadFragment_CF_Fields(cf_fields, formData, customFormLocal.getCustom_form_pre());
    }

    private GE_Custom_Form_Data loadAnswer(long customer_code, long product_code, long custom_form_type, long custom_form_code, long custom_form_version, long custom_form_data) {

        GE_Custom_Form_Data form_data = custom_form_dataDao

                .getByString(

                        new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                                String.valueOf(customer_code),
                                String.valueOf(custom_form_type),
                                String.valueOf(custom_form_code),
                                String.valueOf(custom_form_version),
                                String.valueOf(custom_form_data)
                        ).toSqlQuery().toLowerCase()

                );

        if (form_data != null) {
            form_data.setDataFields(

                    custom_form_data_fieldDao.query(
                            new GE_Custom_Form_Data_Field_MULTI_SqlSpecification(
                                    String.valueOf(customer_code),
                                    String.valueOf(custom_form_type),
                                    String.valueOf(custom_form_code),
                                    String.valueOf(custom_form_version),
                                    String.valueOf(form_data.getCustom_form_data())
                            ).toSqlQuery().toLowerCase()
                    )
            );
        } else {
            form_data = new GE_Custom_Form_Data();
            //
            form_data.setCustomer_code(customer_code);
            form_data.setCustom_form_type((int) custom_form_type);
            form_data.setCustom_form_code((int) custom_form_code);
            form_data.setCustom_form_version((int) custom_form_version);
            form_data.setCustom_form_data(custom_form_data);
            form_data.setProduct_code(product_code);
            form_data.setDate_start(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            form_data.setUser_code(Long.parseLong(ToolBox_Con.getPreference_User_Code(context)));
            form_data.setSite_code(Long.parseLong(ToolBox_Con.getPreference_Site_Code(context)));
            form_data.setOperation_code(ToolBox_Con.getPreference_Operation_Code(context));

        }

        return form_data;
    }


    @Override
    public void onBackPressedClicked() {

    }
}
