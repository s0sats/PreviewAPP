package com.namoadigital.prj001.ui.act013;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Local_Data_List_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act008.Act008_Main;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.ui.act033.Act033_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act013_Main extends Base_Activity implements Act013_Main_View {
    private static final int PROCESSO_SELECAO_ZONA = 10;

    public static final String FORM_IN_PROCESSING = "form_in_processing";
    public static final String START_FORM = "start_form";
    public static final String FORM_DATA_CREATION_ERROR = "form_data_creation_error";
    public static final String EMPTY_SERIAL_SEARCH = "empty_serial_search" ;
    public static final String SERIAL_CREATION_DENIED = "serial_creation_denied" ;
    public static final String FORM_STATUS_PREVENT_TO_OPEN = "form_status_prevent_to_open" ;
    public static final String FREE_EXECUTION_BLOCKED = "free_execution_blocked" ;

    private Act013_Main_Presenter mPresenter;
    private Local_Data_List_Adapter mAdapter;

    private ListView lv_pendencies;
    private TextView tv_filter;
    private CheckBox chk_processing;
    private CheckBox chk_scheduled;
    private CheckBox chk_waiting_sync;
    private ImageView iv_help;
    private List<CheckBox> checkBoxList;
    private String requesting_act = "";

    private boolean accessToSchedule;
    private String wsProcess;
    private boolean filterInProcessing;
    private boolean filterSchedule;
    private boolean filterWaitingSync;
    //LUCHE - 23/04/2020
    //Implementação troca de site
    private String site_code_back;
    private int zone_code_back;
    private HMAux item_selected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act013_main);

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
                Constant.ACT013
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("alert_form_not_ready_title");
        translateList.add("alert_form_not_ready_msg");
        translateList.add("alert_helper_dialog_ttl");
        translateList.add("alert_helper_dialog_msg");
        translateList.add("lbl_chk_in_processing");
        translateList.add("lbl_chk_scheduled");
        translateList.add("lbl_chk_finalized");
        //
        translateList.add("alert_ttl_exists_in_processing");
        translateList.add("alert_msg_exists_in_processing");
        translateList.add("alert_ttl_start_new_processing");
        translateList.add("alert_msg_start_new_processing");
        //
        translateList.add("alert_schedule_comment_ttl");
        translateList.add("alert_define_serial_ttl");
        translateList.add("alert_define_serial_msg");
        //
        translateList.add("alert_error_on_create_form_ttl");
        translateList.add("alert_error_on_create_form_msg");
        translateList.add("alert_no_serial_found_ttl");
        translateList.add("alert_no_serial_found_msg");
        translateList.add("dialog_serial_search_ttl");
        translateList.add("dialog_serial_search_start");
        translateList.add("alert_product_no_allow_new_serial_msg");
        //
        translateList.add("dialog_schedule_warning_ttl");
        translateList.add("dialog_schedule_warning_new_status_lbl");
        translateList.add("dialog_schedule_warning_user_nick_lbl");
        translateList.add("dialog_schedule_warning_error_msg_lbl");
        //
        translateList.add("alert_form_status_prevents_to_open_ttl");
        translateList.add("alert_form_status_prevents_to_open_msg");
        //
        translateList.add("alert_form_turn_gps_on_title");
        translateList.add("alert_form_turn_gps_on_msg");
        //
        translateList.add("alert_form_site_restriction_ttl");
        translateList.add("alert_form_site_restriction_no_access_msg");
        translateList.add("alert_form_site_restriction_confirm");
        //
        translateList.add("alert_free_execution_blocked_ttl");
        translateList.add("alert_free_execution_blocked_msg");
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

        recoverIntentsInfo();

        mPresenter =
                new Act013_Main_Presenter_Impl(
                        context,
                        this,
                        new GE_Custom_Form_LocalDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                        ),
                        hmAux_Trans
                );
        //Verifica se usr tem acesso aos agendados
        //accessToSchedule = ToolBox_Inf.parameterExists(context,Constant.PARAM_SCHEDULE_CHECKLIST);
        accessToSchedule = ToolBox_Inf.profileExists(context,Constant.PROFILE_PRJ001_SCHEDULE_CHECKLIST,null);
        //
        lv_pendencies = (ListView) findViewById(R.id.act013_lv_pendencies);
        //
        tv_filter = (TextView) findViewById(R.id.act013_tv_filter);
        tv_filter.setTag("lbl_filter");
        views.add(tv_filter);
        //
        checkBoxList = new ArrayList<>();
        //
        chk_processing = (CheckBox) findViewById(R.id.act013_chk_in_process);
        chk_processing.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_IN_PROCESSING))));
        //
        chk_scheduled = (CheckBox) findViewById(R.id.act013_chk_scheduled);
        chk_scheduled.setVisibility(accessToSchedule ? View.VISIBLE : View.GONE);
        chk_scheduled.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_SCHEDULE))));
        //
        chk_waiting_sync = (CheckBox) findViewById(R.id.act013_chk_waiting_sync);
        chk_waiting_sync.setButtonTintList(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_WAITING_SYNC)));
        //
        initChkValues();
        //Add checkbox na lista
        checkBoxList.add(chk_processing);
        checkBoxList.add(chk_scheduled);
        checkBoxList.add(chk_waiting_sync);
        //
        iv_help = (ImageView) findViewById(R.id.act013_iv_help);
        //
        filterApply();
    }

    /**
     * LUCHE - 09/03/2020
     * Metodo que inicializa os valores dos checkbox via parametros do bundle.
     * Necessario após novo agendamento
     */
    private void initChkValues() {
        chk_processing.setChecked(filterInProcessing);
        chk_scheduled.setChecked(filterSchedule);
        chk_waiting_sync.setChecked(filterWaitingSync);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            requesting_act = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT012);
            filterInProcessing = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_IN_PROCESSING, true);
            filterSchedule = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_SCHEDULE, false);
            filterWaitingSync = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_WAITING_SYNC, false);
        } else {
            requesting_act = Constant.ACT012;
            filterInProcessing = true;
            filterSchedule = false;
            filterWaitingSync = false;
        }
        // Cópia do Site_Code e do Zone_Code para o mudanca no Agendamento
        site_code_back = ToolBox_Con.getPreference_Site_Code(context);
        zone_code_back = ToolBox_Con.getPreference_Zone_Code(context);
        item_selected = new HMAux();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT013;
        mAct_Title = Constant.ACT013 + "_" + "title";
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
    private void initActions() {

        lv_pendencies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                item_selected.putAll(item);
                mPresenter.validateOpenForm(item);
            }
        });

        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelperDialog();
            }
        });

        chk_processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterApply();
            }
        });

        chk_waiting_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterApply();
            }
        });

        chk_scheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterApply();
            }
        });
    }

    /**
     * Analisa todos checklist
     * e chama função que monsta lista ja passando os
     * filtros selecionados.
     */
    private void filterApply() {
        boolean filterInProcessing = false;
        boolean filterWaitingSync = false;
        boolean filterScheduled = false;

        for ( CheckBox checkBox :checkBoxList ) {
            switch (checkBox.getId() ){
                case R.id.act013_chk_in_process:

                    if( chk_processing.getVisibility() == View.VISIBLE
                        && chk_processing.isChecked() ){
                        filterInProcessing = true;
                    }
                    break;
                case R.id.act013_chk_waiting_sync:

                    if( chk_waiting_sync.getVisibility() == View.VISIBLE
                        && chk_waiting_sync.isChecked() ){
                        filterWaitingSync = true;
                    }

                    break;
                case R.id.act013_chk_scheduled:
                    if( chk_scheduled.getVisibility() == View.VISIBLE
                        && chk_scheduled.isChecked() ){
                        filterScheduled = true;
                    }
                    break;
                default:
                    break;
            }
        }

        mPresenter.getPendencies(filterInProcessing,filterWaitingSync,filterScheduled);

    }

    private void showHelperDialog() {
        AlertDialog.Builder alert =  new AlertDialog.Builder(Act013_Main.this);

        LayoutInflater inflater =  this.getLayoutInflater();
        View view = inflater.inflate(R.layout.act013_helper_dialog,null);
        //
        TextView tv_title = (TextView) view.findViewById(R.id.act013_helper_dialog_tv_title);
        tv_title.setText(hmAux_Trans.get("alert_helper_dialog_msg"));

        CheckBox chk_processing = (CheckBox) view.findViewById(R.id.act013_helper_dialog_chk_processing);
        chk_processing.setText(hmAux_Trans.get(Constant.SYS_STATUS_PROCESS));
        chk_processing.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_IN_PROCESSING))));
        //
        CheckBox chk_scheduled = (CheckBox) view.findViewById(R.id.act013_helper_dialog_chk_scheduled);
        chk_scheduled.setText(hmAux_Trans.get(Constant.SYS_STATUS_SCHEDULE));
        chk_scheduled.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_SCHEDULE))));
        chk_scheduled.setVisibility(accessToSchedule ? View.VISIBLE : View.GONE);
        //
        CheckBox chk_waiting_sync = (CheckBox) view.findViewById(R.id.act013_helper_dialog_chk_waiting_sync);
        chk_waiting_sync.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_WAITING_SYNC));
        chk_waiting_sync.setButtonTintList(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_WAITING_SYNC)));

        alert
            .setView(view)
            .setCancelable(true)
            ;

        alert.show();
    }

    @Override
    public void loadPendencies(List<HMAux> pendencies) {
        mAdapter =
                new Local_Data_List_Adapter(
                        context,
                        R.layout.local_data_list_cell,
                        pendencies
                );
        //
        mAdapter.setOnIvScheduleWarningClickListner(new Local_Data_List_Adapter.OnIvScheduleWarningClickListner() {
            @Override
            public void OnIvScheduleWarningClick(String fcmNewStatus, String fcmUserNick, String errorMsg) {
                ToolBox_Inf.showScheduleWarningDialog(
                    context,
                    hmAux_Trans.get("dialog_schedule_warning_ttl"),
                    hmAux_Trans.get("dialog_schedule_warning_new_status_lbl"),
                    hmAux_Trans.get(fcmNewStatus),
                    hmAux_Trans.get("dialog_schedule_warning_user_nick_lbl"),
                    fcmUserNick,
                    hmAux_Trans.get("dialog_schedule_warning_error_msg_lbl"),
                    errorMsg
                );
            }
        });
        //
        lv_pendencies.setAdapter(mAdapter);

    }

    @Override
    public void showMsg(String type, final HMAux item) {
        String title = "";
        String msg = "";
        DialogInterface.OnClickListener listener = null;
        Integer btnNegative = null;

        switch (type){
            case FORM_IN_PROCESSING:
                title = hmAux_Trans.get("alert_ttl_exists_in_processing");
                msg = hmAux_Trans.get("alert_msg_exists_in_processing");
                btnNegative = 0;
                break;

            case START_FORM:
                title = hmAux_Trans.get("alert_ttl_start_new_processing");
                msg = hmAux_Trans.get("alert_msg_start_new_processing");
                btnNegative = 1;
                listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.processScheduleFlow(item);
                    }
                };
                break;

            case FORM_DATA_CREATION_ERROR:
                title = hmAux_Trans.get("alert_error_on_create_form_ttl");
                msg = hmAux_Trans.get("alert_error_on_create_form_msg");
                btnNegative = 0;
                break;

            case EMPTY_SERIAL_SEARCH:
                title = hmAux_Trans.get("alert_no_serial_found_ttl");
                msg = hmAux_Trans.get("alert_no_serial_found_msg");
                btnNegative = 0;
                break;
            case SERIAL_CREATION_DENIED:
                title = hmAux_Trans.get("alert_no_serial_found_ttl");
                msg = hmAux_Trans.get("alert_product_no_allow_new_serial_msg");
                btnNegative = 0;
                break;
            case FORM_STATUS_PREVENT_TO_OPEN:
                title = hmAux_Trans.get("alert_form_status_prevents_to_open_ttl");
                msg = hmAux_Trans.get("alert_form_status_prevents_to_open_msg");
                btnNegative = 0;
                break;
            case FREE_EXECUTION_BLOCKED:
                title = hmAux_Trans.get("alert_free_execution_blocked_ttl");
                msg = hmAux_Trans.get("alert_free_execution_blocked_msg");
                btnNegative = 0;
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
    public void addControlToActivity(MKEditTextNM mketSerial) {
        controls_sta.add(mketSerial);
    }

    @Override
    public void removeControlFromActivity(MKEditTextNM mketSerial) {
        controls_sta.remove(mketSerial);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PROCESSO_SELECAO_ZONA:
                if (resultCode == RESULT_OK){
                    filterApply();
                    iniUIFooter();
                    mPresenter.validateOpenForm(item_selected);
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
    public void callAct011(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct012(Context context) {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();

    }

    @Override
    public void callAct006(Context context) {
        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }
    //17/08/2018
    @Override
    public void callAct008(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act008_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Add flag inficando qual actChamou
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT013);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct020(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act020_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT013);
            bundle.putBoolean(ConstantBaseApp.SYS_STATUS_IN_PROCESSING, chk_processing.isChecked());
            bundle.putBoolean(ConstantBaseApp.SYS_STATUS_SCHEDULE, chk_scheduled.isChecked());
            bundle.putBoolean(ConstantBaseApp.SYS_STATUS_WAITING_SYNC, chk_waiting_sync.isChecked());
            //
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct033(Context context) {
        Intent mIntent = new Intent(context, Act033_Main.class);
        //
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT013);
        mIntent.putExtras(bundle);
        startActivityForResult(mIntent, PROCESSO_SELECAO_ZONA);
    }

    @Override
    public void alertFormNotReady() {

        ToolBox.alertMSG(
                Act013_Main.this,
                hmAux_Trans.get("alert_form_not_ready_title"),
                hmAux_Trans.get("alert_form_not_ready_msg"),
                null,
                0
        );
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked(requesting_act);
    }

    //region WS_Returns
    @Override
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);
        if(wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            mPresenter.extractSearchResult(result);
        }else{
            progressDialog.dismiss();
        }
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

    @Override
    public void alertActiveGPSResource(final HMAux item) {

        ToolBox.alertMSG(
                Act013_Main.this,
                hmAux_Trans.get("alert_form_turn_gps_on_title"),
                hmAux_Trans.get("alert_form_turn_gps_on_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.validateGPSResource(item);
                    }
                },
                1
        );

    }
}
