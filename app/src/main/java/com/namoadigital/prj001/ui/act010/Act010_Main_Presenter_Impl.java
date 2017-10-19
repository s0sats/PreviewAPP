package com.namoadigital.prj001.ui.act010;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_002;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act010_Main_Presenter_Impl implements Act010_Main_Presenter{

    private Context context;
    private Act010_Main_View mView;
    private GE_Custom_FormDao custom_formDao;
    private GE_Custom_Form_DataDao customFormDataDao;
    private long product_code;
    private String serial_id;
    private String so_prefix;
    private String so_code;

    public Act010_Main_Presenter_Impl(Context context, Act010_Main_View mView, GE_Custom_FormDao custom_formDao, GE_Custom_Form_DataDao customFormDataDao, long product_code, String serial_id, String so_prefix, String so_code) {
        this.context = context;
        this.mView = mView;
        this.custom_formDao = custom_formDao;
        this.customFormDataDao = customFormDataDao;
        this.product_code = product_code;
        this.serial_id = serial_id;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public void setAdapterData(long product_code, int custom_form_type, String filter) {

        List<HMAux> data =
                custom_formDao.query_HM(
                        new GE_Custom_Form_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                custom_form_type,
                                ToolBox_Con.getPreference_Translate_Code(context),
                                String.valueOf(this.product_code),
                                ToolBox_Con.getPreference_Operation_Code(context)
                        ).toSqlQuery()
                );
        //
        if(data != null && data.size() == 1){
            validateOpenForm(data.get(0));
        }
        //
        mView.loadForms(data);
    }

    @Override
    public void validateOpenForm(HMAux item) {
        //
        if(ToolBox_Inf.checkFormIsReady(
                    context,
                    Long.parseLong(item.get(GE_Custom_FormDao.CUSTOMER_CODE)),
                    Integer.parseInt(item.get(GE_Custom_FormDao.CUSTOM_FORM_TYPE)),
                    Integer.parseInt(item.get(GE_Custom_FormDao.CUSTOM_FORM_CODE)),
                    Integer.parseInt(item.get(GE_Custom_FormDao.CUSTOM_FORM_VERSION))
            )
        ){
            //
            mView.addFormInfoToBundle(item);
            //
            mView.callAct011(context);

        }else{
            mView.alertFormNotReady();
        }
    }

    private boolean hasOpenFormLinkedToSO(HMAux item){

        GE_Custom_Form_Data formData = customFormDataDao.getByString(
                new GE_Custom_Form_Local_Sql_003(
                        item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                        "0",
                        String.valueOf(product_code),
                        serial_id
                ).toSqlQuery().toLowerCase()
        );

        if(formData == null){
            return false;
        }else{
            if(so_prefix.equals("") && so_code.equals("")
                && formData.getSo_prefix() == null && formData.getSo_code() == null){
                return false;
            }

        }

        return false;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct009(context);
    }
}
