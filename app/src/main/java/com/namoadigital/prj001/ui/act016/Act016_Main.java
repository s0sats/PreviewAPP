package com.namoadigital.prj001.ui.act016;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.ui.act083.Act083_Main;
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
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        //setTitleLanguage();
        //Metodo que seta o titulo da tela baseado na origem
        setActBarTitle();
        setFooter();
    }

    private void setActBarTitle() {
        getSupportActionBar().setTitle(ToolBox_Inf.getActTitleByOrigin(context, ConstantBaseApp.ACT016,hmAux_Trans,mAct_Title));
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
    public void callAct005() {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct083(Bundle bundle) {
        Intent mIntent = new Intent(context, Act083_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT016);
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
