package com.namoadigital.prj001.ui.act016;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act017.Act017_Main;
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

    public static final String ACT016_SELECTED_DATE = "selected_date";

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

    }

    private void initActions() {
        cv_schedules.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayPress(Date date) {
                /*Toast.makeText(
                        getBaseContext(),
                        date.toString(),
                        Toast.LENGTH_SHORT
                ).show();*/
                mPresenter.formatDate(date);
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

    @Override
    public void callAct017(Bundle bundle) {
        Intent mIntent = new Intent(context, Act017_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005() {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }
}
