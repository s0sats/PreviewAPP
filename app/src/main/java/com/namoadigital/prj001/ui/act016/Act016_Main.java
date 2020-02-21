package com.namoadigital.prj001.ui.act016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.ui.act046.Act046_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;

/**
 * Created by DANIEL.LUCHE on 10/04/2017.
 */

public class Act016_Main extends Base_Activity implements Act016_Main_View {

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
    private boolean filter_form;
    private boolean filter_form_ap;
    private boolean filter_site;
    private boolean filter_ticket;
    private boolean noDateSelected;

    private HMAux hmAux_Trans_Extra = new HMAux();

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
        translateList.add("alert_filter_dialog_msg");
        translateList.add("module_n_form");
        translateList.add("module_n_form_ap");
        translateList.add("lbl_site");
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
        transList_Extra.add("lbl_ticket");

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

    private void getBundleInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(ACT_SELECTED_DATE)) {
            noDateSelected = bundle.getString(ACT_SELECTED_DATE) == null;
            selected_date = ToolBox.generateDate(bundle.getString(ACT_SELECTED_DATE));
        } else {
            noDateSelected = true;
            selected_date = ToolBox.generateDate("");
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
        //LUCHE - 21/02/2020
        //Aplicação dos filtros via preferencia
        loadFilterPreferences();
        //mPresenter.getSchedule(filter_form, filter_form_ap);
        applyModuleFilter();
    }

    private void loadFilterPreferences() {
        filter_site = mPresenter.loadCheckboxStatusFromPreferencie(ConstantBaseApp.SCHEDULE_SITE_LOGGED_FILTER_PREFERENCE,false);
        filter_form = mPresenter.loadCheckboxStatusFromPreferencie(ConstantBaseApp.SCHEDULE_N_FORM_FILTER_PREFERENCE,true);
        filter_form_ap = mPresenter.loadCheckboxStatusFromPreferencie(ConstantBaseApp.SCHEDULE_N_FORM_AP_FILTER_PREFERENCE,true);
        filter_ticket = mPresenter.loadCheckboxStatusFromPreferencie(ConstantBaseApp.SCHEDULE_N_TICKET_FILTER_PREFERENCE,true);
    }

    private void initActions() {
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chamar dialog de filter
                showFilterDialog();
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

    private void showFilterDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        //
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.module_filter_dialog, null);
        //
        TextView tv_title = (TextView) view.findViewById(R.id.module_filter_dialog_tv_title);
        tv_title.setText(hmAux_Trans.get("alert_filter_dialog_msg"));
        //
        final CheckBox chk_site = (CheckBox) view.findViewById(R.id.schedule_filter_chk_site_logged);
        chk_site.setText(hmAux_Trans.get("lbl_site"));
        chk_site.setChecked(filter_site);
        chk_site.setTag(ConstantBaseApp.SCHEDULE_SITE_LOGGED_FILTER_PREFERENCE);
        //
        final CheckBox chk_form = (CheckBox) view.findViewById(R.id.schedule_filter_chk_n_form);
        chk_form.setText(hmAux_Trans_Extra.get("lbl_checklist"));
        chk_form.setChecked(filter_form);
        chk_form.setTag(ConstantBaseApp.SCHEDULE_N_FORM_FILTER_PREFERENCE);
        //
        final CheckBox chk_form_ap = (CheckBox) view.findViewById(R.id.schedule_filter_chk_n_form_ap);
        chk_form_ap.setText(hmAux_Trans_Extra.get("lbl_form_ap"));
        chk_form_ap.setChecked(filter_form_ap);
        chk_form_ap.setTag(ConstantBaseApp.SCHEDULE_N_FORM_AP_FILTER_PREFERENCE);
        //
        final CheckBox chk_ticket = (CheckBox) view.findViewById(R.id.schedule_filter_chk_n_ticket);
        chk_ticket.setText(hmAux_Trans_Extra.get("lbl_ticket"));
        chk_ticket.setChecked(filter_ticket);
        chk_ticket.setTag(ConstantBaseApp.SCHEDULE_N_TICKET_FILTER_PREFERENCE);

        alert
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filter_site = chk_site.isChecked();
                        filter_form = chk_form.isChecked();
                        filter_form_ap = chk_form_ap.isChecked();
                        filter_ticket = chk_ticket.isChecked();
                        //LUCHE - 21/02/2020
                        //Salva alterações na preferencias.Como esse dialog, só aplica os filtros se usr der ok
                        //não foi possivel colocar o save no listener de troca de dados.
                        mPresenter.saveCheckBoxStatusIntoPreference(String.valueOf(chk_site.getTag()),chk_site.isChecked());
                        mPresenter.saveCheckBoxStatusIntoPreference(String.valueOf(chk_form.getTag()),chk_form.isChecked());
                        mPresenter.saveCheckBoxStatusIntoPreference(String.valueOf(chk_form_ap.getTag()),chk_form_ap.isChecked());
                        mPresenter.saveCheckBoxStatusIntoPreference(String.valueOf(chk_ticket.getTag()),chk_ticket.isChecked());
                        //
                        applyModuleFilter();
                    }
                });
        //
        alert.show();
    }

    private void applyModuleFilter() {
        mPresenter.getSchedule(filter_form, filter_form_ap, filter_site );
        //
        if (filter_form || filter_form_ap || filter_site || filter_ticket) {
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
        } else {
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT016;
        mAct_Title = Constant.ACT016 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
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
        events.clear();
        //
        events.addAll(scheduleData);
        //Se volta da Act017, lista de agendados, passa data clicada
        //para carregar calendario na data correta.
        //if(selected_date != null){
        if(!noDateSelected){
            cv_schedules.updateCalendar(selected_date,events);
        }else{
            cv_schedules.updateCalendar(events);
        }
    }

    private Date getTodayDate(String format) {
        String sDate = "";
        Calendar ca1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        //
        try {
            sDate = sdf.format(ca1.getTime());
        } catch (Exception var5) {
            sDate = "1900-01-01";
        }
        //
        return ToolBox.generateDate(sDate);
    }

    @Override
    public void callAct017(Bundle bundle) {
        Intent mIntent = new Intent(context, Act017_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT016);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct046() {
        Intent mIntent = new Intent(context, Act046_Main.class);
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
