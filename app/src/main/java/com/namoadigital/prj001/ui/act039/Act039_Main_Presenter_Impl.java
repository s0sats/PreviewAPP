package com.namoadigital.prj001.ui.act039;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_003;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

/**
 * Created by d.luche on 09/03/2018.
 */

public class Act039_Main_Presenter_Impl implements Act039_Main_Presenter {

    private Context context;
    private Act039_Main mView;
    private HMAux hmAux_Trans;
    //
    private GE_Custom_Form_ApDao formApDao;


    public Act039_Main_Presenter_Impl(Context context, Act039_Main mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.formApDao = new GE_Custom_Form_ApDao(context);
    }

    @Override
    public void getloadAPs() {
        mView.loadAPs(
                (ArrayList<HMAux>) formApDao.query_HM(
                        new GE_Custom_Form_Ap_Sql_003(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                false,
                                false,
                                false,
                                true,
                                true
                        ).toSqlQuery()
                )
        );
    }
}
