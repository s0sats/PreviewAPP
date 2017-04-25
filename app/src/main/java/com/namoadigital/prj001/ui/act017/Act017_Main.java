package com.namoadigital.prj001.ui.act017;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Module_Schedules_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 */

public class Act017_Main extends Base_Activity implements Act017_Main_View {

    public static final String ACT017_MODULE_KEY = "module_key";
    //
    public static final String MODULE_CHECKLIST_FORM_IN_PROCESSING = "checklist_form_in_processing";
    public static final String MODULE_CHECKLIST_START_FORM = "checklist_start_form";


    private Context context;
    private TextView tv_title;
    private ListView lv_schedules;
    private Bundle bundle;
    private String scheduled_date;
    private Act017_Main_Presenter mPresenter;
    private Module_Schedules_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act017_main);

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
                Constant.ACT017
        );
        //
        getBundleInfo();
        //
        loadTranslation();

    }

    private void getBundleInfo() {
        bundle =  getIntent().getExtras();
        if(bundle != null){
           scheduled_date = bundle.getString(Act016_Main.ACT016_SELECTED_DATE);
        }

    }

    private void loadTranslation() {
        //
        List<String> translateList = new ArrayList<>();
        translateList.add("alert_ttl_exists_in_processing");
        translateList.add("alert_msg_exists_in_processing");
        translateList.add("alert_ttl_start_new_processing");
        translateList.add("alert_msg_start_new_processing");
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
        mPresenter =
                new Act017_Main_Presenter_Impl(
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
        tv_title = (TextView) findViewById(R.id.act017_tv_title);
        //
        lv_schedules = (ListView) findViewById(R.id.act017_lv_schedules);
        //
        mPresenter.getSchedules(scheduled_date);
        //
        String date_desc = getDateDesc(scheduled_date);

        date_desc += "";
        //
        tv_title.setText(date_desc);
    }

    private String getDateDesc(String scheduled_date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat showFormat = new SimpleDateFormat("EEEE, dd/MMM/yyyy");
        Date date;
        String final_date = "";
        String day_desc = "";
        String month_desc = "";

        try {
            //date = showFormat.format(format.parse(scheduled_date));
            date = format.parse(scheduled_date);
            //
            day_desc = getDayTranslate(date);
            month_desc = getMonthTranslate(date);
            //formata data do oracle para format
            //e troca MM por ** para substituir mes por extenso no final.
            String customer_format =
                    ToolBox_Con.getPreference_Customer_nls_date_format(context)
                            .replace("DD","dd")
                            .replace("/"," ")
                            .replace("MM","**")
                            .replace("RRRR","yyyy");
            //
            showFormat = new SimpleDateFormat(customer_format);
            final_date = day_desc + ", " + showFormat.format(date).replace("**", month_desc);

        } catch (Exception e) {
            date = format.getCalendar().getTime();
            final_date = showFormat.format(date);
        }

        return final_date;

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT017;
        mAct_Title = Constant.ACT017 + "_" + "title";
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

    private void initActions() {

        lv_schedules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.checkScheduleFlow(item);
            }
        });

    }

    @Override
    public void loadSchedules(List<HMAux> schedules) {

        mAdapter =  new Module_Schedules_Adapter(
                            context,
                            R.layout.module_schedules_cell,
                            schedules
                    );

        lv_schedules.setAdapter(mAdapter);

    }

    @Override
    public void showMsg(String type, final HMAux item) {
        String title = "";
        String msg = "";
        DialogInterface.OnClickListener listener = null;
        Integer btnNegative = null;

        switch (type){
            case MODULE_CHECKLIST_FORM_IN_PROCESSING:
                title = hmAux_Trans.get("alert_ttl_exists_in_processing");
                msg = hmAux_Trans.get("alert_msg_exists_in_processing");
                btnNegative = 0;
            break;

            case MODULE_CHECKLIST_START_FORM:
                title = hmAux_Trans.get("alert_ttl_start_new_processing");
                msg = hmAux_Trans.get("alert_msg_start_new_processing");
                btnNegative = 1;
                listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.prepareOpenForm(item);
                    }
                };
                break;

        }

        if(btnNegative != null) {
            ToolBox.alertMSG(
                    this,
                    title,
                    msg,
                    listener,
                    btnNegative
            );
        }
    }

    private String getDayTranslate(Date date){
        String dayTrans = "";

        switch (date.getDay()){
            case 0:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("daySunday");
                break;
            case 1:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayMonday");
                break;
            case 2:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayTuesday");
                break;
            case 3:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayWednesday");
                break;
            case 4:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayThursday");
                break;
            case 5:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayFriday");
                break;
            case 6:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("daySaturday");
                break;
            default:
                break;
        }

        return dayTrans ;
    }

    private String getMonthTranslate(Date date){
        String monthTrans = "";

        switch (date.getMonth()) {
            case 0:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monJanuary");
                break;
            case 1:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monFebruary");
                break;
            case 2:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monMarch");
                break;
            case 3:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monApril");
                break;
            case 4:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monMay");
                break;
            case 5:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monJune");
                break;
            case 6:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monJuly");
                break;
            case 7:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monAugust");
                break;
            case 8:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monSeptember");
                break;
            case 9:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monOctober");
                break;
            case 10:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monNovember");
                break;
            case 11:
                monthTrans =ConstantBase.HMAUX_TRANS_LIB.get("monDecember");
                break;
            default:
                break;
        }

        return monthTrans;
    }

    @Override
    public void callAct016(Context context) {
        Intent mIntent = new Intent(context, Act016_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct011(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
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
}
