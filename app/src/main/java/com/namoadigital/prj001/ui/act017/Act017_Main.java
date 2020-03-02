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

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Module_Schedules_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.ui.act008.Act008_Main;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.ui.act033.Act033_Main;
import com.namoadigital.prj001.ui.act038.Act038_Main;
import com.namoadigital.prj001.ui.act046.Act046_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_FORM;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_FORM_AP;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_LATE;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_SITE;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_NO_SELECTED_DATE;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;


/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 * <p>
 * Modificado by DANIEL.LUCHE on 16/08/2018.
 * Implementado interface do clique no icone de comentario do agendamento
 */

public class Act017_Main extends Base_Activity implements Act017_Main_View {

    public static final String ACT017_MODULE_KEY = "module_key";
    public static final String ACT017_ADAPTER_DATE_REF = "adapter_date_ref";
    public static final String ACT017_ADAPTER_DATE_REF_MS = "adapter_date_ref_ms";
    //
    public static final String MODULE_CHECKLIST_FORM_IN_PROCESSING = "checklist_form_in_processing";
    public static final String MODULE_CHECKLIST_START_FORM = "checklist_start_form";
    public static final String MODULE_SCHEDULE_DATE_REF = "module_schedule_date_ref";
    public static final String MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR = "module_schedule_form_data_creation_error";
    public static final String EMPTY_SERIAL_SEARCH = "empty_serial_search";

    private ListView lv_schedules;
    private Bundle bundle;
    private String scheduled_date;
    private Act017_Main_Presenter mPresenter;
    private Module_Schedules_Adapter mAdapter;
    //Implementação AP
    private boolean filter_form;
    private boolean filter_form_ap;
    private boolean filter_site;
    private boolean filter_ticket;

    private LinearLayout ll_filter;
    private TextView tv_filter;
    private ImageView iv_filter;
    private HMAux hmAux_Trans_Extra = new HMAux();
    private TextView tv_no_result;
    //Implementação Agendamento 2.0 03/07/18
    private String serial_id = "";
    private boolean late = false;
    private String mRequesting_ACT;
    private LinearLayout ll_serial;
    private TextView tv_serial_lbl;
    private TextView tv_serial;
    private TextView tv_qty;

    private static final int PROCESSO_SELECAO_ZONA = 10;
    private String site_code_back;
    private int zone_code_back;
    private HMAux item_selected;
    private String wsProcess;

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
            scheduled_date = bundle.getString(ACT_SELECTED_DATE, null);
            //
            serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            //
            mRequesting_ACT = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT046);
            //
            late = bundle.getBoolean(ACT_FILTER_LATE, false);
        } else {
            scheduled_date = null;
            serial_id = "";
            mRequesting_ACT = Constant.ACT046;
            late = false;
        }
        // Cópia do Site_Code e do Zone_Code para o mudanca no Agendamento
        site_code_back = ToolBox_Con.getPreference_Site_Code(context);
        zone_code_back = ToolBox_Con.getPreference_Zone_Code(context);
        item_selected = new HMAux();
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
        translateList.add("alert_define_serial_ttl");
        translateList.add("alert_define_serial_msg");
        translateList.add("alert_form_site_restriction_ttl");
        translateList.add("alert_form_site_restriction_msg");
        translateList.add("alert_form_site_restriction_no_access_msg");
        translateList.add("alert_form_site_restriction_confirm");
        //
        translateList.add("lbl_site");
        translateList.add("alert_schedule_comment_ttl");
        translateList.add("alert_error_on_create_form_ttl");
        translateList.add("alert_error_on_create_form_msg");
        //
        translateList.add("alert_no_serial_found_ttl");
        translateList.add("alert_no_serial_found_msg");
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
        //
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
        ll_serial = (LinearLayout) findViewById(R.id.act017_ll_serial);
        //
        tv_serial_lbl = (TextView) findViewById(R.id.act017_tv_serial_lbl);
        //
        tv_serial = (TextView) findViewById(R.id.act017_tv_serial);
        //
        tv_qty = (TextView) findViewById(R.id.act017_tv_qty);
        //
        lv_schedules = (ListView) findViewById(R.id.act017_lv_schedules);
        //
        tv_no_result = (TextView) findViewById(R.id.act017_tv_no_result);
        tv_no_result.setText(hmAux_Trans.get("msg_no_result"));
        tv_no_result.setVisibility(View.GONE);
        //
        tv_serial.setText(serial_id);
        ll_serial.setVisibility(!serial_id.equals("") ? View.VISIBLE : View.INVISIBLE);
        //LUCHE - 21/02/2020
        //Aplicação dos filtros via preferencia
        loadFilterPreferences();
        //
        applyModuleFilter();

    }

    private void loadFilterPreferences() {
        filter_site = mPresenter.loadCheckboxStatusFromPreferencie(ConstantBaseApp.SCHEDULE_SITE_LOGGED_FILTER_PREFERENCE,false);
        filter_form = mPresenter.loadCheckboxStatusFromPreferencie(ConstantBaseApp.SCHEDULE_N_FORM_FILTER_PREFERENCE,true);
        filter_form_ap = mPresenter.loadCheckboxStatusFromPreferencie(ConstantBaseApp.SCHEDULE_N_FORM_AP_FILTER_PREFERENCE,true);
        filter_ticket = mPresenter.loadCheckboxStatusFromPreferencie(ConstantBaseApp.SCHEDULE_N_TICKET_FILTER_PREFERENCE,true);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT017;
        mAct_Title = Constant.ACT017 + "_" + "title";
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
    public void setQty(int list_qty, int tot_qty) {
        String sQty = "( list_qty / tot_qty )";
        //
        tv_qty.setText(
                sQty.replace("list_qty", String.valueOf(list_qty))
                        .replace("tot_qty", String.valueOf(tot_qty))
        );
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

                item_selected.putAll(item);

                mPresenter.checkScheduleFlow(item);
            }
        });

    }

    @Override
    public void loadSchedules(List<HMAux> schedules) {
        //
        mAdapter = new Module_Schedules_Adapter(
                context,
                schedules,
                R.layout.module_schedules_cell,
                R.layout.namoa_ap_cell,
                R.layout.module_schedules_date_cell
        );
        //16/08/18
        mAdapter.setOnIvCommentClickListner(new Module_Schedules_Adapter.OnIvCommentClickListner() {
            @Override
            public void OnIvCommentClick(String comment) {
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_schedule_comment_ttl"),
                        comment,
                        null,
                        0
                );
            }
        });

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
        //
        alert
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filter_form = chk_form.isChecked();
                        filter_form_ap = chk_form_ap.isChecked();
                        filter_site = chk_site.isChecked();
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
        mPresenter.getSchedules(scheduled_date, filter_form, filter_form_ap, serial_id, late, filter_site);
        //
        if (filter_form || filter_form_ap || filter_site || filter_ticket) {
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
                        boolean hasSerial = item.get(MD_Schedule_ExecDao.SERIAL_ID).length() > 0;

                        mPresenter.prepareOpenForm(item, hasSerial);
                    }
                };
                break;
            case MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR:
                title = hmAux_Trans.get("alert_error_on_create_form_ttl");
                msg = hmAux_Trans.get("alert_error_on_create_form_msg");
                btnNegative = 0;
                break;
            case EMPTY_SERIAL_SEARCH:
                title = hmAux_Trans.get("alert_no_serial_found_ttl");
                msg = hmAux_Trans.get("alert_no_serial_found_msg");
                btnNegative = 0;
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

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
            ttl,
            msg,
            hmAux_Trans.get("sys_alert_btn_cancel"),
            hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void callAct008(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act008_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //16/08/2018 - Add filtros no bundle para act008
        bundle.putBoolean(ACT_FILTER_FORM, filter_form);
        bundle.putBoolean(ACT_FILTER_FORM_AP, filter_form_ap);
        bundle.putBoolean(ACT_FILTER_SITE, filter_site);
        //
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
    public void callAct016(Context context) {
        Intent mIntent = new Intent(context, Act016_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        bundle.remove(Constant.MAIN_REQUESTING_ACT);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct033(Context context) {
        Intent mIntent = new Intent(context, Act033_Main.class);
        //
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017);
        //
        mIntent.putExtras(bundle);
        startActivityForResult(mIntent, PROCESSO_SELECAO_ZONA);
    }

    @Override
    public void callAct020(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act020_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PROCESSO_SELECAO_ZONA:
                if (resultCode == RESULT_OK){
                    mPresenter.getSchedules(scheduled_date, filter_form, filter_form_ap, serial_id, late, filter_site);
                    iniUIFooter();
                    //
                    mPresenter.checkScheduleFlow(item_selected);
                } else {
                    ToolBox_Con.setPreference_Site_Code(context, site_code_back);
                    ToolBox_Con.setPreference_Zone_Code(context, zone_code_back);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void callAct038(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act038_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putBoolean(ACT_FILTER_FORM, filter_form);
        bundle.putBoolean(ACT_FILTER_FORM_AP, filter_form_ap);
        bundle.putBoolean(ACT_FILTER_SITE, filter_site);
        bundle.putBoolean(ACT_FILTER_LATE, late);
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, serial_id);
        bundle.putBoolean(ACT_NO_SELECTED_DATE, scheduled_date == null);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct046(Context context) {
        Intent mIntent = new Intent(context, Act046_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public String getmRequesting_ACT() {
        return mRequesting_ACT;
    }

    //region WS_Returns
    @Override
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);
        progressDialog.dismiss();
        mPresenter.extractSearchResult(result);
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        progressDialog.dismiss();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //implementar dialog confirmando busca offline
        progressDialog.dismiss();
    }
    //TRATA SESSION_NOT_FOUND
    @Override
    protected void processLogin() {
        super.processLogin();
        //
        ToolBox_Con.cleanPreferences(context);
        //
        ToolBox_Inf.call_Act001_Main(context);
        //
        finish();
    }
    //endregion

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
