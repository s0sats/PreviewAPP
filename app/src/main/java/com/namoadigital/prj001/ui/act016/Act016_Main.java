package com.namoadigital.prj001.ui.act016;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
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

    private ListView lv_schedules;
    private CalendarView cv_schedules;
    private HashSet<HMAux> events;
    private Act016_Main_Presenter_Impl mPresenter;
    private Bundle bundle;
    private Date selected_date;
    //Implementação AP
    private LinearLayout ll_filter;
    private TextView tv_filter;
    private ImageView iv_filter;

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
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT016
        );
        //
        getBundleInfo();
        //
        loadTranslation();
    }

    private void loadTranslation() {
        //
        List<String> translateList = new ArrayList<>();
        translateList.add("filter_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

    private void getBundleInfo() {
        bundle =  getIntent().getExtras();
        if(bundle != null && bundle.containsKey(Act016_Main.ACT016_SELECTED_DATE)){
            selected_date = ToolBox.generateDate(bundle.getString(Act016_Main.ACT016_SELECTED_DATE));
        }

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
        ll_filter = (LinearLayout) findViewById(R.id.act016_ll_filter);
        //
        tv_filter = (TextView) findViewById(R.id.act016_tv_filter_lbl);
        tv_filter.setText(hmAux_Trans.get("filter_lbl"));
        //
        iv_filter = (ImageView) findViewById(R.id.act016_iv_filter);
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
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chamar dialog de filter
            }
        });
        //
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
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    @Override
    public void loadSchedule(List<HMAux> scheduleData) {
        //
        events.addAll(scheduleData);
        //Se volta da Act017, lista de agendados, passa data clicada
        //para carregar calendario na data correta.
        if(selected_date != null){
            cv_schedules.updateCalendar(selected_date,events);
        }else{
            cv_schedules.updateCalendar(events);
        }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
