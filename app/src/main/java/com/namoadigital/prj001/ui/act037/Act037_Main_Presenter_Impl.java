package com.namoadigital.prj001.ui.act037;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act037_Main_Presenter_Impl implements Act037_Main_Presenter {

    private Context context;
    private Act037_Main_View mView;
    private HMAux hmAux_Trans;

    private GE_Custom_Form_ApDao ge_custom_form_apDao;

    public Act037_Main_Presenter_Impl(Context context, Act037_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_ApDao ge_custom_form_apDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.ge_custom_form_apDao = ge_custom_form_apDao;
    }

    @Override
    public void getloadAPs(boolean filter_edit, boolean filter_process, boolean filter_waiting_action) {

        mView.loadAPs(
                (ArrayList<HMAux>) ge_custom_form_apDao.query_HM(
                        new GE_Custom_Form_Ap_Sql_003(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                filter_edit,
                                filter_process,
                                filter_waiting_action,
                                false,
                                false
                        ).toSqlQuery()
                )
        );
    }

    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act) {
            case Constant.ACT012:
                mView.callAct012(context);
                break;
            case Constant.ACT036:
            default:
                mView.callAct005(context);
                break;


        }
    }
}
