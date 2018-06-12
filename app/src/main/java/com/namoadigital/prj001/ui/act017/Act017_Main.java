package com.namoadigital.prj001.ui.act017;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Module_Schedules_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act008.Act008_Main;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act038.Act038_Main;
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

    private TextView tv_title;
    private ListView lv_schedules;
    private Bundle bundle;
    private String scheduled_date;
    private Act017_Main_Presenter mPresenter;
    private Module_Schedules_Adapter mAdapter;
    //Implementação AP
    private boolean filter_form;
    private boolean filter_form_ap;
    private LinearLayout ll_filter;
    private TextView tv_filter;
    private ImageView iv_filter;
    private HMAux hmAux_Trans_Extra = new HMAux();
    private TextView tv_no_result;

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
        bundle = getIntent().getExtras();
        if (bundle != null) {
            scheduled_date = bundle.getString(Act016_Main.ACT016_SELECTED_DATE);
            filter_form = bundle.getBoolean(Act016_Main.ACT016_FILTER_FORM, false);
            filter_form_ap = bundle.getBoolean(Act016_Main.ACT016_FILTER_FORM_AP, false);
        } else {
            filter_form = false;
            filter_form_ap = false;
        }
    }

    private void loadTranslation() {
        //
        List<String> translateList = new ArrayList<>();
        translateList.add("alert_ttl_exists_in_processing");
        translateList.add("alert_msg_exists_in_processing");
        translateList.add("alert_ttl_start_new_processing");
        translateList.add("alert_msg_start_new_processing");
        translateList.add("filter_lbl");
        translateList.add("alert_filter_dialog_msg");
        translateList.add("msg_no_result");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );

        /*
         * ENQUANTO NÃO FOR DEFINIDO MODULO NÃO TRAUDZIVEL PARA O TEXTO
         * DO NOME DOS MODULOS, SERÁ USADO ESSE METODO ABAIXO QUE BUSCA DIRETAMENTE
         * DO RECURSO DA ACT005
         * */
        List<String> transList_Extra = new ArrayList<String>();
        transList_Extra.add("lbl_checklist");
        transList_Extra.add("lbl_form_ap");

        hmAux_Trans_Extra = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                ToolBox_Inf.getResourceCode(
                        context,
                        mModule_Code,
                        Constant.ACT005
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transList_Extra
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
        ll_filter = (LinearLayout) findViewById(R.id.act017_ll_filter);
        //
        tv_filter = (TextView) findViewById(R.id.act017_tv_filter_lbl);
        tv_filter.setText(hmAux_Trans.get("filter_lbl"));
        //
        iv_filter = (ImageView) findViewById(R.id.act017_iv_filter);
        //
        tv_title = (TextView) findViewById(R.id.act017_tv_title);
        //
        lv_schedules = (ListView) findViewById(R.id.act017_lv_schedules);
        //
        tv_no_result = (TextView) findViewById(R.id.act017_tv_no_result);
        tv_no_result.setText(hmAux_Trans.get("msg_no_result"));
        tv_no_result.setVisibility(View.GONE);
        //
        //mPresenter.getSchedules(scheduled_date,filter_form, filter_form_ap);
        applyModuleFilter();
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
                            .replace("DD", "dd")
                            .replace("/", " ")
                            .replace("MM", "**")
                            .replace("RRRR", "yyyy");
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

    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
        //
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
        //
        mAdapter = new Module_Schedules_Adapter(
                context,
                R.layout.module_schedules_cell,
                R.layout.namoa_ap_cell,
                schedules
        );
        //
        mAdapter.setSite_id_preference(ToolBox_Con.getPreference_Site_Code(context));
        //
        lv_schedules.setAdapter(mAdapter);
        //
        if (schedules.size() == 0) {
            lv_schedules.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
        } else {
            tv_no_result.setVisibility(View.GONE);
            lv_schedules.setVisibility(View.VISIBLE);
        }

    }

    private void showFilterDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        //
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.module_filter_dialog, null);
        //
        TextView tv_title = (TextView) view.findViewById(R.id.module_filter_dialog_tv_title);
        tv_title.setText(hmAux_Trans.get("alert_filter_dialog_msg"));
        //
        final CheckBox chk_form = (CheckBox) view.findViewById(R.id.module_filter_dialog_chk_n_form);
        chk_form.setText(hmAux_Trans_Extra.get("lbl_checklist"));
        chk_form.setChecked(filter_form);
        chk_form.setTag(filter_form);
        //
        final CheckBox chk_form_ap = (CheckBox) view.findViewById(R.id.module_filter_dialog_chk_n_form_ap);
        chk_form_ap.setText(hmAux_Trans_Extra.get("lbl_form_ap"));
        chk_form_ap.setChecked(filter_form_ap);
        chk_form_ap.setTag(filter_form_ap);
        //
        alert
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filter_form = chk_form.isChecked();
                        filter_form_ap = chk_form_ap.isChecked();
                        //
                        applyModuleFilter();
                    }
                });
        //
        alert.show();
    }

    private void applyModuleFilter() {
        mPresenter.getSchedules(scheduled_date, filter_form, filter_form_ap);
        //
        if (filter_form || filter_form_ap) {
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
        } else {
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
        }
    }

    @Override
    public void showMsg(String type, final HMAux item) {
        String title = "";
        String msg = "";
        DialogInterface.OnClickListener listener = null;
        Integer btnNegative = null;

        switch (type) {
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
                        boolean hasSerial = item.get(GE_Custom_Form_LocalDao.SERIAL_ID).length() > 0;

                        mPresenter.prepareOpenForm(item, hasSerial);
                    }
                };
                break;

        }

        if (btnNegative != null) {
            ToolBox.alertMSG(
                    this,
                    title,
                    msg,
                    listener,
                    btnNegative
            );
        }
    }

    private String getDayTranslate(Date date) {
        String dayTrans = "";

        switch (date.getDay()) {
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

        return dayTrans;
    }

    private String getMonthTranslate(Date date) {
        String monthTrans = "";

        switch (date.getMonth()) {
            case 0:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monJanuary");
                break;
            case 1:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monFebruary");
                break;
            case 2:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monMarch");
                break;
            case 3:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monApril");
                break;
            case 4:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monMay");
                break;
            case 5:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monJune");
                break;
            case 6:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monJuly");
                break;
            case 7:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monAugust");
                break;
            case 8:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monSeptember");
                break;
            case 9:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monOctober");
                break;
            case 10:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monNovember");
                break;
            case 11:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monDecember");
                break;
            default:
                break;
        }

        return monthTrans;
    }

    @Override
    public void callAct008(Context context, Bundle bundle) {
        bundle.putString(Act016_Main.ACT016_SELECTED_DATE, scheduled_date);
        Intent mIntent = new Intent(context, Act008_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct016(Context context) {
        Intent mIntent = new Intent(context, Act016_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        bundle.putBoolean(Act016_Main.ACT016_FILTER_FORM, filter_form);
        bundle.putBoolean(Act016_Main.ACT016_FILTER_FORM_AP, filter_form_ap);
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
    public void callAct038(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act038_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Adicionan schedule_date no bundle
        bundle.putString(Act016_Main.ACT016_SELECTED_DATE, scheduled_date);
        bundle.putBoolean(Act016_Main.ACT016_FILTER_FORM, filter_form);
        bundle.putBoolean(Act016_Main.ACT016_FILTER_FORM_AP, filter_form_ap);
        //
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

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
