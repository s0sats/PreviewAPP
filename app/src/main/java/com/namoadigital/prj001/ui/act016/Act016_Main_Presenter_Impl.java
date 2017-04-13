package com.namoadigital.prj001.ui.act016;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.sql.Sql_Act016_001;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 */

public class Act016_Main_Presenter_Impl implements Act016_Main_Presenter {

    private Context context;
    private Act016_Main mView;
    private GE_Custom_Form_LocalDao formLocalDao;
    private HMAux hmAux_Trans;


    public Act016_Main_Presenter_Impl(Context context, Act016_Main mView, GE_Custom_Form_LocalDao formLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.formLocalDao = formLocalDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSchedule() {
        List<HMAux> schedules =
                formLocalDao.query_HM(
                        new Sql_Act016_001().toSqlQuery()
                );

        mView.loadSchedule(schedules);
    }
}
