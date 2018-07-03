package com.namoadigital.prj001.ui.act016;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.sql.Sql_Act016_001;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 */

public class Act016_Main_Presenter_Impl implements Act016_Main_Presenter {

    private Context context;
    private Act016_Main_View mView;
    private GE_Custom_Form_LocalDao formLocalDao;
    private GE_Custom_Form_ApDao formApDao;
    private HMAux hmAux_Trans;


    public Act016_Main_Presenter_Impl(Context context, Act016_Main mView, GE_Custom_Form_LocalDao formLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.formLocalDao = formLocalDao;
        this.formApDao = new GE_Custom_Form_ApDao(context);
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSchedule(boolean filter_form, boolean filter_form_ap) {
        ArrayList<HMAux> schedules = new ArrayList<>();
        if (filter_form || filter_form_ap) {
            schedules =
                    (ArrayList<HMAux>) formLocalDao.query_HM(
                            new Sql_Act016_001(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                    filter_form,
                                    filter_form_ap
                            ).toSqlQuery()
                    );
        }else{
            //SE ambos falso, chama query com amboos parametro true
            schedules =
                    (ArrayList<HMAux>) formLocalDao.query_HM(
                            new Sql_Act016_001(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                    true,
                                    true
                            ).toSqlQuery()
                    );
        }
        //
        mView.loadSchedule(schedules);
    }

    @Override
    public void formatDate(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        // String selected_date = (String) android.text.format.DateFormat.format("yyyy-MM-dd",date);
        String selected_date = formater.format(date);

        Bundle bundle = new Bundle();

        bundle.putString(Act016_Main.ACT016_SELECTED_DATE, selected_date);

        mView.callAct017(bundle);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct046();
    }
}
