package com.namoadigital.prj001.ui.act016;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.MyActionFilterParam;
import com.namoadigital.prj001.sql.Sql_Act016_002;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;

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
    public void getSchedule() {
        ArrayList<HMAux> schedules = new ArrayList<>();
        schedules =
                (ArrayList<HMAux>) formLocalDao.query_HM(
                        new Sql_Act016_002(
                                context,
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                        ).toSqlQuery()
                );
        //
        mView.loadSchedule(schedules);
    }

    @Override
    public void formatDate(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String selected_date = formater.format(date);
        Bundle bundle = new Bundle();
        bundle.putString(ACT_SELECTED_DATE, selected_date);
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM,getMyActionFilterParam(selected_date));
        bundle.putSerializable(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,ConstantBaseApp.ACT016);
        //mView.callAct017(bundle);
        mView.callAct083(bundle);
    }

    private MyActionFilterParam getMyActionFilterParam(String selected_date) {
        return new MyActionFilterParam(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            selected_date
        );
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct005();
    }
}
