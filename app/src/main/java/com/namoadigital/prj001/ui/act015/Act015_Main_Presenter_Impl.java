package com.namoadigital.prj001.ui.act015;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.sql.Sql_Act015_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public class Act015_Main_Presenter_Impl implements Act015_Main_Presenter {

    private Context context;
    private Act015_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private HMAux hmAux_Trans;

    public Act015_Main_Presenter_Impl(Context context, Act015_Main mView, GE_Custom_Form_LocalDao customFormLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSentData() {
        List<HMAux> sent_datas =
                customFormLocalDao.query_HM(
                        new Sql_Act015_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                context
                        ).toSqlQuery()
                );
        //
        mView.loadSentData(sent_datas);

    }

    @Override
    public void addFormInfoToBundle(HMAux item) {
        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        //bundle.putString(Constant.ACT007_PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        //bundle.putString(Constant.ACT008_PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        //bundle.putString(Constant.ACT008_SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_TYPE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE));
        //bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        //bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE));
        //bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION));
        //bundle.putString(Constant.ACT010_CUSTOM_FORM_VERSION,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION));
        // DIFERENTE VERIFICAR
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        //bundle.putString(Constant.ACT013_CUSTOM_FORM_DATA,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));

        mView.callAct011(context,bundle);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct014(context);

    }
}
