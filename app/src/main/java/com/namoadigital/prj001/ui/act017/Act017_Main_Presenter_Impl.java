package com.namoadigital.prj001.ui.act017;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_004;
import com.namoadigital.prj001.sql.Sql_Act017_001;
import com.namoadigital.prj001.sql.Sql_Act017_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;


/**
 * Created by DANIEL.LUCHE on 17/04/2017.
 */

public class Act017_Main_Presenter_Impl implements Act017_Main_Presenter {

    private Context context;
    private Act017_Main_View mView;
    private GE_Custom_Form_LocalDao formLocalDao;
    private GE_Custom_Form_ApDao formApDao;
    private HMAux hmAux_Trans;


    public Act017_Main_Presenter_Impl(Context context, Act017_Main_View mView, GE_Custom_Form_LocalDao formLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.formLocalDao = formLocalDao;
        this.formApDao = new GE_Custom_Form_ApDao(context);
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSchedules(String selected_date, boolean filter_form, boolean filter_form_ap) {
        ArrayList<HMAux> schedules = new ArrayList<>();

        if(filter_form || (!filter_form && !filter_form_ap)) {
            ArrayList<HMAux> schedulesForm =
                    (ArrayList<HMAux>) formLocalDao.query_HM(
                            new Sql_Act017_001(
                                    context,
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    selected_date
                            ).toSqlQuery()
                    );
            if(schedulesForm != null){
                schedules.addAll(schedulesForm);
            }
        }
        //
        if(filter_form_ap || (!filter_form && !filter_form_ap)){
            ArrayList<HMAux> schedulesFormAP =
                    (ArrayList<HMAux>) formApDao.query_HM(
                            new Sql_Act017_002(
                                    context,
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    selected_date
                            ).toSqlQuery()
                    );
            if(schedulesFormAP != null){
                schedules.addAll(schedulesFormAP);
            }
        }
        mView.loadSchedules(schedules);
    }

    @Override
    public void checkScheduleFlow(HMAux item) {

        switch (item.get(Act017_Main.ACT017_MODULE_KEY)){

            case Constant.MODULE_CHECKLIST:
                if(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equals(Constant.SYS_STATUS_SCHEDULE)) {
                    if (isAnyFormInProcessing(item)) {
                        mView.showMsg(Act017_Main.MODULE_CHECKLIST_FORM_IN_PROCESSING, item);
                    } else {
                        mView.showMsg(Act017_Main.MODULE_CHECKLIST_START_FORM, item);
                    }
                }else{
                    boolean hasSerial = false;
                    if(item.get(GE_Custom_Form_LocalDao.SERIAL_ID).length() > 0){
                        hasSerial = true;
                    }
                    prepareOpenForm(item, hasSerial);
                }

            break;

            case Constant.MODULE_FORM_AP:
                    prepareOpenFormAP(item);
                break;

        }

    }

    private void prepareOpenFormAP(HMAux hmAux) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017);
        bundle.putString(GE_Custom_Form_ApDao.CUSTOMER_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        bundle.putString(GE_Custom_Form_ApDao.AP_CODE, hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
        //
        mView.callAct038(context,bundle);
    }

    @Override
    public void prepareOpenForm(HMAux item, boolean hasSerial) {
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

        if(hasSerial){
            mView.callAct011(context,bundle);
        }else if(!item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(Constant.SYS_STATUS_SCHEDULE)){
            mView.callAct011(context,bundle);
        }else{
            mView.callAct008(context,bundle);
        }

        //mView.callAct011(context,bundle);

    }

    private void updateFormStatus(HMAux item) {

        formLocalDao.addUpdate(
                new GE_Custom_Form_Local_Sql_004(
                        item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA),
                        Constant.SYS_STATUS_IN_PROCESSING
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
