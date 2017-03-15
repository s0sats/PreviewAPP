package com.namoadigital.prj001.ui.act013;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.sql.Sql_Act013_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act013_Main_Presenter_Impl implements Act013_Main_Presenter {

    private Context context;
    private Act013_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;

    public Act013_Main_Presenter_Impl(Context context, Act013_Main mView, GE_Custom_Form_LocalDao customFormLocalDao) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
    }

    @Override
    public void getPendencies(boolean filter_in_processing ,boolean filter_finalized,boolean filter_scheduled) {

        List<HMAux> pendencies =
                customFormLocalDao.query_HM(
                        new Sql_Act013_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                filter_in_processing,
                                filter_finalized,
                                filter_scheduled,
                                context
                        ).toSqlQuery()
                );

        mView.loadPendencies(pendencies);

    }

    @Override
    public void addFormInfoToBundle(HMAux item) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ACT007_PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        bundle.putString(Constant.ACT008_PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        bundle.putString(Constant.ACT008_SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE));
        bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE));
        bundle.putString(Constant.ACT010_CUSTOM_FORM_VERSION,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION));
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));
        bundle.putString(Constant.ACT013_CUSTOM_FORM_DATA,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));

        mView.callAct011(context,bundle);
    }

    @Override
    public void validateOpenForm(HMAux item) {
        //
        if(ToolBox_Inf.checkFormIsReady(
                context,
                Long.parseLong(item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION))
                )
         ){

            addFormInfoToBundle(item);
        }else{
            mView.alertFormNotReady();
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct012(context);
    }
}
