package com.namoadigital.prj001.ui.act012;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.sql.Sql_Act012_001;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act012_Main_Presenter_Impl implements Act012_Main_Presenter {

    private Context context;
    private Act012_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;

    public Act012_Main_Presenter_Impl(Context context, Act012_Main mView, GE_Custom_Form_LocalDao customFormLocalDao) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
    }

    @Override
    public void getPendencies() {
        List<HMAux> pendencies =
        customFormLocalDao.query_HM(
                new Sql_Act012_001().toSqlQuery()
        );

        mView.loadPendencies(pendencies);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
