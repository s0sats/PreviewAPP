package com.namoadigital.prj001.ui.act013;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.sql.Sql_Act013_001;
import com.namoadigital.prj001.util.ToolBox_Con;

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
    public void getPendencies() {

        List<HMAux> pendencies =
                customFormLocalDao.query_HM(
                        new Sql_Act013_001(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                );

        mView.loadPendencies(pendencies);

    }

    @Override
    public void onBackPressedClicked() {

    }
}
