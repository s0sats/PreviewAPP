package com.namoadigital.prj001.ui.act016;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 10/04/2017.
 */

public class Act016_Main extends Base_Activity implements Act016_Main_View {

    private Context context;
    private ListView lv_schedules;
    private CalendarView cv_schedules;
    private HashSet<HMAux> events;
    private Act016_Main_Presenter_Impl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act016_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();

    }

    private void iniSetup() {
        context = getBaseContext();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT016
        );

        loadTranslation();
    }

    private void loadTranslation() {
        //
        List<String> translateList = new ArrayList<>();
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

    private void initVars() {
        //
        mPresenter =
                new Act016_Main_Presenter_Impl(
                        context,
                        this,
                        new GE_Custom_Form_LocalDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                                ),
                        hmAux_Trans
                );
        //
        lv_schedules = (ListView) findViewById(R.id.act016_lv_schedules);
        //
        cv_schedules = (CalendarView) findViewById(R.id.act016_cv_schedules);
        //
        events = new HashSet<>();
        //
        mPresenter.getSchedule();



        /*HMAux a1 = new HMAux();
        a1.put(CalendarView.DT, "2017-04-04");
        a1.put(CalendarView.DELAYED_COUNT, "1");
        a1.put(CalendarView.INPROCESSING_COUNT, "0");
        a1.put(CalendarView.SCHEDULED_COUNT, "0");
        a1.put(CalendarView.FINALIZED_COUNT, "0");
        events.add(a1);
        //
        HMAux a2 = new HMAux();
        a2.put(CalendarView.DT, "2017-04-19");
        a2.put(CalendarView.DELAYED_COUNT, "0");
        a2.put(CalendarView.INPROCESSING_COUNT, "1");
        a2.put(CalendarView.SCHEDULED_COUNT, "0");
        a2.put(CalendarView.FINALIZED_COUNT, "0");
        events.add(a2);
        //
        HMAux a3 = new HMAux();
        a3.put(CalendarView.DT, "2017-04-05");
        a3.put(CalendarView.DELAYED_COUNT, "0");
        a3.put(CalendarView.INPROCESSING_COUNT, "0");
        a3.put(CalendarView.SCHEDULED_COUNT, "1");
        a3.put(CalendarView.FINALIZED_COUNT, "0");
        events.add(a3);
        //
        HMAux a4 = new HMAux();
        a4.put(CalendarView.DT, "2017-04-18");
        a4.put(CalendarView.DELAYED_COUNT, "0");
        a4.put(CalendarView.INPROCESSING_COUNT, "0");
        a4.put(CalendarView.SCHEDULED_COUNT, "0");
        a4.put(CalendarView.FINALIZED_COUNT, "1");
        events.add(a4);
        cv_schedules.updateCalendar(events);*/
    }

    private void initActions() {
        cv_schedules.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayPress(Date date) {
                Toast.makeText(
                        getBaseContext(),
                        date.toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT016;
        mAct_Title = Constant.ACT016 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value =  hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl =  hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value =  hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé - fim
    }


    @Override
    public void loadSchedule(List<HMAux> scheduleData) {
        //
        events.addAll(scheduleData);
        //
        cv_schedules.updateCalendar(events);

    }
}
