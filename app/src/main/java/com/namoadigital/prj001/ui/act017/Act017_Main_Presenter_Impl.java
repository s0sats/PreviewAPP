package com.namoadigital.prj001.ui.act017;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_004;
import com.namoadigital.prj001.sql.Sql_Act017_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;


/**
 * Created by DANIEL.LUCHE on 17/04/2017.
 */

public class Act017_Main_Presenter_Impl implements Act017_Main_Presenter {

    private Context context;
    private Act017_Main_View mView;
    private GE_Custom_Form_LocalDao formLocalDao;
    private HMAux hmAux_Trans;


    public Act017_Main_Presenter_Impl(Context context, Act017_Main_View mView, GE_Custom_Form_LocalDao formLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.formLocalDao = formLocalDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSchedules(String selected_date) {

        List<HMAux> schedules =
                formLocalDao.query_HM(
                        new Sql_Act017_001(
                                context,
                                ToolBox_Con.getPreference_Customer_Code(context),
                                selected_date
                        ).toSqlQuery()
                );

        mView.loadSchedules(schedules);
    }

    @Override
    public void checkScheduleFlow(HMAux item) {

        switch (item.get(Act017_Main.ACT017_MODULE_KEY)){

            case Constant.MODULE_CHECKLIST:
                if(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equals(Constant.CUSTOM_FORM_STATUS_SCHEDULED)) {
                    if (isAnyFormInProcessing(item)) {
                        mView.showMsg(Act017_Main.MODULE_CHECKLIST_FORM_IN_PROCESSING, item);
                    } else {
                        mView.showMsg(Act017_Main.MODULE_CHECKLIST_START_FORM, item);
                    }
                }else{
                    prepareOpenForm(item);
                }

            break;

        }

    }

    @Override
    public void prepareOpenForm(HMAux item) {
        //Atualiza status do form para in_processing
        //foi comentando pois a atualização do status já corre na act011
        //e pq se o form a ser aberto tem status inprocessing, fom ja abre
        //com as bas e campos sendo validados.
        //updateFormStatus(item);

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

    private void updateFormStatus(HMAux item) {

        formLocalDao.addUpdate(
                new GE_Custom_Form_Local_Sql_004(
                        item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA),
                        Constant.CUSTOM_FORM_STATUS_IN_PROCESSING
                ).toSqlQuery()
        );
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct016(context);
    }

    public boolean isAnyFormInProcessing(HMAux item) {

        GE_Custom_Form_Local customFormLocal =
                formLocalDao.getByString(
                        new GE_Custom_Form_Local_Sql_003(
                                item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                                "0",
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE),
                                item.get(GE_Custom_Form_LocalDao.SERIAL_ID)
                        ).toSqlQuery()
                );

        if(customFormLocal != null){
            return true;
        }
        return false;
    }
}
