package com.namoadigital.prj001.ui.act038;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.MD_Department;
import com.namoadigital.prj001.model.MD_User;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.service.WS_AP_Save;
import com.namoadigital.prj001.service.WS_AP_Search;
import com.namoadigital.prj001.service_chat.WS_Room_AP;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.ui.act037.Act037_Main;
import com.namoadigital.prj001.ui.act039.Act039_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.namoa_digital.namoa_library.util.ToolBox.SW_TYPE_BR_AP;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_FORM;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_FORM_AP;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_LATE;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_SITE;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_NO_SELECTED_DATE;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act038_Main extends Base_Activity implements Act038_Main_View {

    private Act038_Main_Presenter_Impl mPresenter;

    private HMAux hmAux_Trans_Extra = new HMAux();

    private Bundle bundle;

    private int backAction;
    private String requestingAct;

    private String mCustomer_Code;
    private String mCustom_Form_Type;
    private String mCustom_Form_Code;
    private String mCustom_Form_Version;
    private String mCustom_Form_Data;
    private String mAp_Code;

    private GE_Custom_Form_Ap mGe_custom_form_ap;
    private GE_Custom_Form_ApDao mGe_custom_form_apDao;

    private ImageView iv_header_show_hide;
    private CheckBox cb_header_show_hide;
    private LinearLayout ll_header;

    private ImageView iv_opc_show_hide;
    private CheckBox cb_opc_show_hide;

    private LinearLayout ll_opc;

    private TextView tv_ap_desc_ttl;
    private TextView tv_customer_code_ttl;
    private EditText et_customer_code_ttl;
    private TextView tv_form_type_ttl;
    private EditText et_form_type_ttl;
    private TextView tv_form_code_ttl;
    private EditText et_form_code_ttl;
    private TextView tv_form_version_ttl;
    private EditText et_form_version_ttl;
    private TextView tv_form_seq_ttl;
    private EditText et_form_seq_ttl;
    private TextView tv_creation_ttl;
    private EditText et_create_date_ttl;
    private EditText et_create_user_ttl;

    private TextView tv_product_ttl;
    private EditText et_product_val;
    private TextView tv_serial_ttl;
    private EditText et_serial_val;
    private SearchableSpinner ss_status;
    private MkDateTime et_form_when_ttl;
    private SearchableSpinner ss_users;
    private TextView tv_ap_extra_into_ttl;
    private SearchableSpinner ss_departments;
    private TextView tv_form_what_ttl;
    private TextView et_form_what_ttl;
    private TextView tv_form_where_ttl;
    private TextView et_form_where_ttl;
    private TextView tv_form_why_ttl;
    private TextView et_form_why_ttl;
    private TextView tv_form_how_ttl;
    private TextView et_form_how_ttl;
    private TextView tv_form_how_much_ttl;
    private MKEditTextNM et_form_how_mcuch_ttl;
    private TextView tv_form_comments_ttl;
    private TextView et_form_comments_ttl;
    private ArrayList<View> editable_views_list = new ArrayList<>();

    private ArrayList<View> editable_views_list_long = new ArrayList<>();
    private ArrayList<String> editable_views_list_long_names = new ArrayList<>();
    private TextView editable_views_current;
    private int editable_views_current_Index;

    private Button btn_pdf;
    private Button btn_chat_nav;
    private ImageView iv_up;
    private ImageView iv_down;

    private Button btn_save;
    private boolean mDataChanged = false;

    private ArrayList<Object> properties;
    private String ws_process = "";

    //Ap Agendado
    private String scheduled_date;
    private boolean filter_form;
    private boolean filter_form_ap;
    private boolean filter_site;
    private boolean filter_late;
    private boolean no_selected_date;
    private String filter_serial_id;

    private PDFStatusReceiver mPdfStatusReceiver;

    private pdfDownload mPdfDownload;

    private int repeatTry = 0;
    private String mRoom_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act038_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT038
        );

        loadTranslation();
    }

    @Override
    protected void onDestroy() {
        startReceivers(false);
        //
        super.onDestroy();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act038_title");
        //
        transList.add("customer_code_lbl");
        transList.add("form_type_lbl");
        transList.add("form_code_lbl");
        transList.add("form_version_lbl");
        transList.add("form_data_lbl");
        transList.add("ap_when_lbl");
        transList.add("ap_what_lbl");
        transList.add("ap_where_lbl");
        transList.add("ap_why_lbl");
        transList.add("ap_how_lbl");
        transList.add("ap_how_much_lbl");
        transList.add("ap_comments_lbl");
        transList.add("status_lbl");
        transList.add("status_search_lbl");
        transList.add("ap_who_lbl");
        transList.add("ap_who_search_lbl");
        transList.add("department_lbl");
        transList.add("department_search_lbl");
        transList.add("btn_save_lbl");
        transList.add("ap_extra_into_ttl");
        transList.add("alert_sync_detected_tll");
        transList.add("alert_sync_detected_msg");
        transList.add("progress_save_ap_ttl");
        transList.add("progress_save_ap_msg");
        transList.add("progress_sync_ap_ttl");
        transList.add("progress_sync_ap_msg");
        transList.add("cell_ap_lbl");
        transList.add("alert_join_room_ap_ttl");
        transList.add("alert_join_room_ap_msg");
        transList.add("dialog_join_room_ap_ttl");
        transList.add("dialog_join_room_ap_msg");
        transList.add("alert_no_pdf_tll");
        transList.add("alert_no_pdf_msg");
        transList.add("alert_ap_save_tll");
        transList.add("alert_ap_save_msg");
        transList.add("alert_ap_exit_no_save_tll");
        transList.add("alert_ap_exit_no_save_msg");
        transList.add("alert_discard_changes_ttl");
        transList.add("alert_discard_changes_msg");
        transList.add("product_lbl");
        transList.add("serial_lbl");
        transList.add("create_info_lbl");
        transList.add("alert_partial_ap_detected_tll");
        transList.add("alert_partial_ap_detected_msg");
        transList.add("alert_form_ap_pdf_download_error_msg");


        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        List<String> transList_Extra = new ArrayList<String>();
        transList_Extra.add("lbl_checklist");
        transList_Extra.add("lbl_chat");

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
        recoverIntentsInfo();
        //
        mPresenter =
                new Act038_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans,
                        new GE_Custom_Form_ApDao(
                                context
                        ),
                        new CH_RoomDao(
                                context
                        )
                );

        properties = new ArrayList<>();

        //iv_header_show_hide = (ImageView) findViewById(R.id.act038_header_iv_show_hide);
        cb_header_show_hide = (CheckBox) findViewById(R.id.act038_header_cb_show_hide);
        ll_header = (LinearLayout) findViewById(R.id.act038_header_ll_show_hide);

        //iv_opc_show_hide = (ImageView) findViewById(R.id.act038_opc_iv_show_hide);
        cb_opc_show_hide = (CheckBox) findViewById(R.id.act038_opc_cb_show_hide);
        ll_opc = (LinearLayout) findViewById(R.id.act038_opc_ll_show_hide);

        tv_ap_desc_ttl = (TextView) findViewById(R.id.act038_header_tv_ap_desc_ttl);
        tv_customer_code_ttl = (TextView) findViewById(R.id.act038_header_tv_customer_code_ttl);
        et_customer_code_ttl = (EditText) findViewById(R.id.act038_header_et_customer_code_ttl);
        //
        tv_form_type_ttl = (TextView) findViewById(R.id.act038_header_tv_form_type_ttl);
        et_form_type_ttl = (EditText) findViewById(R.id.act038_header_et_form_type_ttl);
        //
        tv_form_code_ttl = (TextView) findViewById(R.id.act038_header_tv_form_code_ttl);
        et_form_code_ttl = (EditText) findViewById(R.id.act038_header_et_form_code_ttl);
        //
        tv_form_version_ttl = (TextView) findViewById(R.id.act038_header_tv_form_version_ttl);
        et_form_version_ttl = (EditText) findViewById(R.id.act038_header_et_form_version_ttl);
        //
        tv_form_seq_ttl = (TextView) findViewById(R.id.act038_header_tv_form_seq_ttl);
        et_form_seq_ttl = (EditText) findViewById(R.id.act038_header_et_form_seq_ttl);
        //
        tv_creation_ttl = (TextView) findViewById(R.id.act038_header_tv_form_creation_ttl);
        et_create_date_ttl = (EditText) findViewById(R.id.act038_header_et_form_create_date_ttl);
        et_create_user_ttl = (EditText) findViewById(R.id.act038_header_et_form_create_user_ttl);
        //
        tv_product_ttl = (TextView) findViewById(R.id.act038_header_tv_product_ttl);
        et_product_val = (EditText) findViewById(R.id.act038_header_et_product_val);
        //
        tv_serial_ttl = (TextView) findViewById(R.id.act038_header_tv_serial_ttl);
        et_serial_val = (EditText) findViewById(R.id.act038_header_et_serial_val);
        //
        ss_status = (SearchableSpinner) findViewById(R.id.act038_content_ss_status);
        ss_status.setmStyle(1);
        ss_status.setmTextSizeLabel(15);
        ss_status.setmTextSizeValue(15);
        ss_status.setmShowLabel(false);
        ss_status.setmCanClean(false);
        //ss_status.setmLabel(hmAux_Trans.get("status_lbl"));
        ss_status.setmTitle(hmAux_Trans.get("status_search_lbl"));
        properties.add(ss_status);
        //
        editable_views_list.add(ss_status);
        //
        et_form_when_ttl = (MkDateTime) findViewById(R.id.act038_header_et_form_when_ttl);
        et_form_when_ttl.setmStyle(1);
        et_form_when_ttl.setmTextSizeLabel(15);
        et_form_when_ttl.setmTextSizeValue(15);
        editable_views_list.add(et_form_when_ttl);
        properties.add(et_form_when_ttl);
        //
        ss_users = (SearchableSpinner) findViewById(R.id.act038_content_ss_users);
        ss_users.setmLabel(hmAux_Trans.get("ap_who_lbl"));
        ss_users.setmTitle(hmAux_Trans.get("ap_who_search_lbl"));
        ss_users.setmStyle(1);
        ss_users.setmTextSizeLabel(15);
        ss_users.setmTextSizeValue(15);
        editable_views_list.add(ss_users);
        properties.add(ss_users);
        //
        tv_ap_extra_into_ttl = (TextView) findViewById(R.id.act038_extra_info_tv_ttl);
        tv_ap_extra_into_ttl.setText(hmAux_Trans.get("ap_extra_into_ttl"));
        //
        ss_departments = (SearchableSpinner) findViewById(R.id.act038_content_ss_departments);
        ss_departments.setmLabel(hmAux_Trans.get("department_lbl"));
        ss_departments.setmTitle(hmAux_Trans.get("department_search_lbl"));
        ss_departments.setmStyle(1);
        ss_departments.setmTextSizeLabel(15);
        ss_departments.setmTextSizeValue(15);
        editable_views_list.add(ss_departments);
        properties.add(ss_departments);
        //
        tv_form_what_ttl = (TextView) findViewById(R.id.act038_opc_tv_what_ttl);
        et_form_what_ttl = (TextView) findViewById(R.id.act038_opc_et_what_ttl);
        et_form_what_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialogV2(0);
            }
        });
        editable_views_list.add(et_form_what_ttl);
        editable_views_list_long.add(et_form_what_ttl);
        properties.add(et_form_what_ttl);

        tv_form_where_ttl = (TextView) findViewById(R.id.act038_opc_tv_where_ttl);
        et_form_where_ttl = (TextView) findViewById(R.id.act038_opc_et_where_ttl);
        et_form_where_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialogV2(1);
            }
        });
        editable_views_list.add(et_form_where_ttl);
        editable_views_list_long.add(et_form_where_ttl);
        properties.add(et_form_where_ttl);

        tv_form_why_ttl = (TextView) findViewById(R.id.act038_opc_tv_why_ttl);
        et_form_why_ttl = (TextView) findViewById(R.id.act038_opc_et_why_ttl);
        et_form_why_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialogV2(2);
            }
        });
        editable_views_list.add(et_form_why_ttl);
        editable_views_list_long.add(et_form_why_ttl);
        properties.add(et_form_why_ttl);

        tv_form_how_ttl = (TextView) findViewById(R.id.act038_opc_tv_how_ttl);
        et_form_how_ttl = (TextView) findViewById(R.id.act038_opc_et_how_ttl);
        et_form_how_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialogV2(3);
            }
        });
        editable_views_list.add(et_form_how_ttl);
        editable_views_list_long.add(et_form_how_ttl);
        properties.add(et_form_how_ttl);

        tv_form_how_much_ttl = (TextView) findViewById(R.id.act038_opc_tv_how_much_ttl);
        et_form_how_mcuch_ttl = (MKEditTextNM) findViewById(R.id.act038_opc_et_how_much_ttl);
        editable_views_list.add(et_form_how_mcuch_ttl);
        properties.add(et_form_how_mcuch_ttl);

        tv_form_comments_ttl = (TextView) findViewById(R.id.act038_opc_tv_comments_ttl);
        et_form_comments_ttl = (TextView) findViewById(R.id.act038_opc_et_comments_ttl);
        et_form_comments_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialogV2(4);
            }
        });
        editable_views_list.add(et_form_comments_ttl);
        editable_views_list_long.add(et_form_comments_ttl);
        properties.add(et_form_comments_ttl);
        //
        btn_pdf = (Button) findViewById(R.id.act038_content_btn_pdf);
        btn_pdf.setText(hmAux_Trans_Extra.get("lbl_checklist"));
        btn_chat_nav = (Button) findViewById(R.id.act038_content_btn_chat_nav);
        btn_chat_nav.setText(hmAux_Trans_Extra.get("lbl_chat"));
        iv_up = (ImageView) findViewById(R.id.act038_content_iv_up);
        iv_down = (ImageView) findViewById(R.id.act038_content_iv_down);
        //
        btn_save = (Button) findViewById(R.id.act038_btn_save);
        btn_save.setText(hmAux_Trans.get("btn_save_lbl"));
        //
        mPresenter.getloadAP(
                mCustomer_Code,
                mCustom_Form_Type,
                mCustom_Form_Code,
                mCustom_Form_Version,
                mCustom_Form_Data,
                mAp_Code
        );
        //
        if (mGe_custom_form_ap != null) {
            mPresenter.loadSSStatus(mGe_custom_form_ap.getAp_status());
            mPresenter.loadSSUsers();
            mPresenter.loadSSDepartments();
            //
            startReceivers(true);
            //
            if (mGe_custom_form_ap.getSync_required() == 1) {
                if (ToolBox_Con.isOnline(context)) {
                    mPresenter.executeApSyncWs();
                } else {
                    if(mGe_custom_form_ap.getCreate_date() == null && mGe_custom_form_ap.getCreate_user() == null){
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_partial_ap_detected_tll"),
                                hmAux_Trans.get("alert_partial_ap_detected_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                    }
                                },
                                0
                        );
                    }else{
                        ToolBox_Inf.showNoConnectionDialog(context);
                    }
                    //ToolBox_Inf.showNoConnectionDialog(context);
                }
            }
            //
            if (mGe_custom_form_ap.getCustom_form_url_local() == null && mGe_custom_form_ap.getCustom_form_url_local().isEmpty()) {
                if (WBR_DownLoad_PDF.IS_RUNNING == false && ToolBox_Con.isOnline(context)) {
                    activateDownLoadPDF(context);
                }
            }

        }
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            mGe_custom_form_apDao = new GE_Custom_Form_ApDao(context);
            //
            requestingAct = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT037);
            mCustomer_Code = bundle.getString(GE_Custom_Form_ApDao.CUSTOMER_CODE);
            mCustom_Form_Type = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE);
            mCustom_Form_Code = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE);
            mCustom_Form_Version = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION);
            mCustom_Form_Data = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA);
            mAp_Code = bundle.getString(GE_Custom_Form_ApDao.AP_CODE);
            mRoom_code = bundle.getString(CH_RoomDao.ROOM_CODE);
            //Fluxo vindo do agendamento
            scheduled_date = bundle.getString(ACT_SELECTED_DATE, ToolBox.sDTFormat_Agora("yyyy-MM-dd").replace(":", ""));
            filter_form = bundle.getBoolean(ACT_FILTER_FORM, true);
            filter_form_ap = bundle.getBoolean(ACT_FILTER_FORM_AP, true);
            filter_site = bundle.getBoolean(ACT_FILTER_SITE, true);
            filter_late = bundle.getBoolean(ACT_FILTER_LATE, true);
            filter_serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            no_selected_date = bundle.getBoolean(ACT_NO_SELECTED_DATE, false);

        } else {
        }
    }

    @Override
    public void showBtnSave(boolean visible) {
        btn_save.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showBtnChatNav(boolean visible) {
        btn_chat_nav.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setWSProcess(String ws_process) {
        this.ws_process = ws_process;
    }

    @Override
    public void loadAP(GE_Custom_Form_Ap ap) {
        mGe_custom_form_ap = ap;
        //
        if (mGe_custom_form_ap == null) {

            requestingAct = Constant.ACT037;

            onBackPressed();
        } else {
            //
            tv_ap_desc_ttl.setText(String.valueOf(ap.getAp_code()) + " - " + ap.getAp_description());
            tv_customer_code_ttl.setText(hmAux_Trans.get("customer_code_lbl"));
            et_customer_code_ttl.setEnabled(false);
            et_customer_code_ttl.setText(String.valueOf(ap.getCustomer_code() + " - " + ToolBox_Con.getPreference_Customer_Code_NAME(context)));
            //
            tv_form_type_ttl.setText(hmAux_Trans.get("form_type_lbl"));
            et_form_type_ttl.setEnabled(false);
            et_form_type_ttl.setText(String.valueOf(ap.getCustom_form_type() + " - " + ap.getCustom_form_type_desc()));
            //
            tv_form_code_ttl.setText(hmAux_Trans.get("form_code_lbl"));
            et_form_code_ttl.setEnabled(false);
            et_form_code_ttl.setText(String.valueOf(ap.getCustom_form_code() + " - " + ap.getCustom_form_desc()));
            //
            tv_form_version_ttl.setText(hmAux_Trans.get("form_version_lbl"));
            et_form_version_ttl.setEnabled(false);
            et_form_version_ttl.setText(String.valueOf(ap.getCustom_form_version()));
            //
            tv_form_seq_ttl.setText(hmAux_Trans.get("form_data_lbl"));
            et_form_seq_ttl.setEnabled(false);
            et_form_seq_ttl.setText(String.valueOf(ap.getCustom_form_data()));
            //
            tv_creation_ttl.setText(hmAux_Trans.get("create_info_lbl"));
            et_create_date_ttl.setEnabled(false);
            et_create_user_ttl.setEnabled(false);
            //A situação de um dos itens abaixo serem nulos, só deve acontecer quando
            //o form ap for inserido via MSG do chat.
            if(ap.getCreate_date() != null && ap.getCreate_user() != null ) {
                tv_creation_ttl.setVisibility(View.VISIBLE);
                et_create_date_ttl.setVisibility(View.VISIBLE);
                et_create_user_ttl.setVisibility(View.VISIBLE);
                //
                et_create_date_ttl.setText(ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(ap.getCreate_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
                //
                et_create_user_ttl.setText(ap.getCreate_user());
            }else{
                tv_creation_ttl.setVisibility(View.GONE);
                et_create_date_ttl.setVisibility(View.GONE);
                et_create_user_ttl.setVisibility(View.GONE);
            }
            //
            tv_product_ttl.setText(hmAux_Trans.get("product_lbl"));
            et_product_val.setEnabled(false);
            et_product_val.setText(ap.getProduct_id() + " - " + ap.getProduct_desc());
            //
            tv_serial_ttl.setText(hmAux_Trans.get("serial_lbl"));
            et_serial_val.setEnabled(false);
            if (ap.getSerial_code() != null) {
                tv_serial_ttl.setVisibility(View.VISIBLE);
                et_serial_val.setText(ap.getSerial_id());
                et_serial_val.setVisibility(View.VISIBLE);
            } else {
                tv_serial_ttl.setVisibility(View.GONE);
                et_serial_val.setVisibility(View.GONE);
            }
            //
            et_form_when_ttl.setmLabel(hmAux_Trans.get("ap_when_lbl"));
            et_form_when_ttl.setEnabled(true);
            et_form_when_ttl.setmValue(ap.getAp_when() == null ? "" : ap.getAp_when(),ap.getAp_when() != null);
            //et_form_when_ttl.setTag(ap.getAp_when() == null ? "" : String.valueOf(ap.getAp_when()));
            //
            tv_form_what_ttl.setText(hmAux_Trans.get("ap_what_lbl"));
            et_form_what_ttl.setEnabled(true);
            et_form_what_ttl.setText(ap.getAp_what() == null ? "" : String.valueOf(ap.getAp_what()));
            //et_form_what_ttl.setTag(ap.getAp_what() == null ? "" : String.valueOf(ap.getAp_what()));
            editable_views_list_long_names.add(hmAux_Trans.get("ap_what_lbl"));

            tv_form_where_ttl.setText(hmAux_Trans.get("ap_where_lbl"));
            et_form_where_ttl.setEnabled(true);
            et_form_where_ttl.setText(ap.getAp_where() == null ? "" : String.valueOf(ap.getAp_where()));
            //et_form_where_ttl.setTag(ap.getAp_where() == null ? "" : String.valueOf(ap.getAp_where()));
            editable_views_list_long_names.add(hmAux_Trans.get("ap_where_lbl"));

            tv_form_why_ttl.setText(hmAux_Trans.get("ap_why_lbl"));
            et_form_why_ttl.setEnabled(true);
            et_form_why_ttl.setText(ap.getAp_why() == null ? "" : String.valueOf(ap.getAp_why()));
            //et_form_why_ttl.setTag(ap.getAp_why() == null ? "" : String.valueOf(ap.getAp_why()));
            editable_views_list_long_names.add(hmAux_Trans.get("ap_why_lbl"));

            tv_form_how_ttl.setText(hmAux_Trans.get("ap_how_lbl"));
            et_form_how_ttl.setEnabled(true);
            et_form_how_ttl.setText(ap.getAp_how() == null ? "" : String.valueOf(ap.getAp_how()));
            //et_form_how_ttl.setTag(ap.getAp_how() == null ? "" : String.valueOf(ap.getAp_how()));
            editable_views_list_long_names.add(hmAux_Trans.get("ap_how_lbl"));

            tv_form_how_much_ttl.setText(hmAux_Trans.get("ap_how_much_lbl"));
            et_form_how_mcuch_ttl.setEnabled(true);
            et_form_how_mcuch_ttl.setText(ap.getAp_how_much() == null ? "" : String.valueOf(ap.getAp_how_much()).replace(",", "."));
            //et_form_how_mcuch_ttl.setTag(ap.getAp_how_much() == null ? "" : String.valueOf(ap.getAp_how_much()).replace(",", "."));

            tv_form_comments_ttl.setText(hmAux_Trans.get("ap_comments_lbl"));
            et_form_comments_ttl.setEnabled(true);
            et_form_comments_ttl.setText(ap.getAp_comments() == null ? "" : String.valueOf(ap.getAp_comments()));
            //et_form_comments_ttl.setTag(ap.getAp_comments() == null ? "" : String.valueOf(ap.getAp_comments()));
            editable_views_list_long_names.add(hmAux_Trans.get("ap_comments_lbl"));

            if (mGe_custom_form_ap.getSync_required() == 1) {
                iv_down.setVisibility(View.VISIBLE);
            } else {
                iv_down.setVisibility(View.GONE);
            }
            //
            if (mGe_custom_form_ap.getUpload_required() == 1) {
                iv_up.setVisibility(View.VISIBLE);
            } else {
                iv_up.setVisibility(View.GONE);
            }
            //
            if ((mGe_custom_form_ap.getSync_required() == 1) && (mGe_custom_form_ap.getUpload_required() == 1)) {
                iv_down.setVisibility(View.VISIBLE);
                iv_up.setVisibility(View.GONE);
            }

            setTags(mGe_custom_form_ap);

            mPresenter.loadSSStatus(mGe_custom_form_ap.getAp_status());
            mPresenter.applyUserProfile(editable_views_list, mGe_custom_form_ap.getAp_status());
        }
    }

    @Override
    public void loadSSStatus(ArrayList<HMAux> statusList) {
        ss_status.setmOption(statusList);
        //
        ToolBox_Inf.setSSmValue(
                ss_status,
                String.valueOf(mGe_custom_form_ap.getAp_status()),
                hmAux_Trans.get(mGe_custom_form_ap.getAp_status()),
                true
        );
    }

    @Override
    public void loadSSUsers(ArrayList<HMAux> statusList) {
        ss_users.setmOption(statusList);
        //
        if (mGe_custom_form_ap.getAp_who() != null) {
            MD_User mUser =
                    mPresenter.loadUser(
                            String.valueOf(mGe_custom_form_ap.getCustomer_code()),
                            String.valueOf(mGe_custom_form_ap.getAp_who())
                    );
            //
            if (mUser != null) {
                ToolBox_Inf.setSSmValue(
                        ss_users,
                        String.valueOf(mUser.getUser_code()),
                        mUser.getUser_nick(),
                        true
                );
            }
        }
    }

    @Override
    public void loadSSDepartment(ArrayList<HMAux> statusList) {
        ss_departments.setmOption(statusList);
        //
        if (mGe_custom_form_ap.getDepartment_code() != null) {
            MD_Department dpto =
                    mPresenter.loadDepartment(
                            String.valueOf(mGe_custom_form_ap.getCustomer_code()),
                            String.valueOf(mGe_custom_form_ap.getDepartment_code())
                    );
            //
            if (dpto != null) {
                ToolBox_Inf.setSSmValue(
                        ss_departments,
                        String.valueOf(dpto.getDepartment_code()),
                        dpto.getDepartment_desc(),
                        true,
                        "department_id",
                        dpto.getDepartment_id()
                );
            }
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT038;
        mAct_Title = Constant.ACT038 + "_" + "title";
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
//        iv_header_show_hide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ll_header.getVisibility() != View.VISIBLE) {
//                    ll_header.setVisibility(View.VISIBLE);
//                } else {
//                    ll_header.setVisibility(View.GONE);
//                }
//            }
//        });

        cb_header_show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    ll_header.setVisibility(View.VISIBLE);
                } else {
                    ll_header.setVisibility(View.GONE);
                }

            }
        });

        btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPDF();

//                if (mGe_custom_form_ap.getCustom_form_url_local().trim().length() != 0) {
//
//                    File file = new File(Constant.CACHE_PATH + "/" + mGe_custom_form_ap.getCustom_form_url_local().trim());
//
//                    try {
//
//                        ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF);
//
//                        ToolBox_Inf.copyFile(
//                                file,
//                                new File(Constant.CACHE_PDF)
//                        );
//                    } catch (Exception e) {
//                        ToolBox_Inf.registerException(getClass().getName(), e);
//                    }
//
//
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(new File(Constant.CACHE_PDF + "/" + mGe_custom_form_ap.getCustom_form_url_local().trim())), "application/pdf");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                    startActivity(intent);
//                } else {
//                    showPD(
//                            hmAux_Trans.get("alert_no_pdf_tll"),
//                            hmAux_Trans.get("alert_no_pdf_msg")
//                    );
//                }
            }
        });


        btn_chat_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGe_custom_form_ap == null) {
                    requestingAct = Constant.ACT037;
                    //
                    actionBackPressed();
                } else {
                    if (mGe_custom_form_ap.getAp_status().equalsIgnoreCase(Constant.SYS_STATUS_CANCELLED) ||
                            mGe_custom_form_ap.getAp_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)) {

                        mPresenter.chatFlow(mGe_custom_form_ap, false);

                    } else {

                        boolean hasDataChange = checkDataChanges(properties);

                        if (hasDataChange) {
                            ToolBox.alertMSG(
                                    Act038_Main.this,
                                    hmAux_Trans.get("alert_ap_exit_no_save_tll"),
                                    hmAux_Trans.get("alert_ap_exit_no_save_msg"),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mPresenter.chatFlow(mGe_custom_form_ap, false);
                                        }
                                    },
                                    2,
                                    null
                            );

                        } else {
                            mPresenter.chatFlow(mGe_custom_form_ap, false);
                        }
                    }
                }
            }
        });

        iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "up", Toast.LENGTH_SHORT).show();
            }
        });

        iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.executeApSyncWs();
                //Toast.makeText(context, "down", Toast.LENGTH_SHORT).show();
            }
        });

//        iv_opc_show_hide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ll_opc.getVisibility() != View.VISIBLE) {
//                    ll_opc.setVisibility(View.VISIBLE);
//                } else {
//                    ll_opc.setVisibility(View.GONE);
//                }
//            }
//        });

        cb_opc_show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_opc.setVisibility(View.VISIBLE);
                } else {
                    ll_opc.setVisibility(View.GONE);
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    ToolBox.alertMSG(
                            Act038_Main.this,
                            hmAux_Trans.get("alert_ap_save_tll"),
                            hmAux_Trans.get("alert_ap_save_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    save();
                                }
                            },
                            2,
                            null
                    );
                } else {
                    showError();
                }
            }
        });
    }

    private boolean validate() {

        if (!checkDataChanges(properties)) {
            if (mGe_custom_form_ap.getUpload_required() == 1 && mGe_custom_form_ap.getSync_required() == 0) {
                mDataChanged = false;
                //Se tudo ok, remove borda de erro dos campos
                ss_status.setBackground(null);
                ss_users.setBackground(null);
                et_form_when_ttl.setBackground(null);
                return true;
            } else {
                return false;
            }
        }

        HMAux aux = ss_status.getmValue();

        if (aux.isEmpty()) {
            ss_status.setBackground(context.getDrawable(R.drawable.shape_error));
            return false;
        } else {
            ss_status.setBackground(null);
        }

        switch (aux.get(SearchableSpinner.ID)) {
            case Constant.SYS_STATUS_PROCESS:
            case Constant.SYS_STATUS_WAITING_ACTION:
            case Constant.SYS_STATUS_DONE:

                HMAux mUser = ss_users.getmValue();

                if (mUser.get(SearchableSpinner.ID) == null ||
                        mUser.get(SearchableSpinner.ID) == "null" ||
                        mUser.get(SearchableSpinner.ID).isEmpty() ||
                        et_form_when_ttl.getmValue() == null ||
                        et_form_when_ttl.getmValue().isEmpty()) {

                    if (mUser.get(SearchableSpinner.ID) == null ||
                            mUser.get(SearchableSpinner.ID) == "null" ||
                            mUser.get(SearchableSpinner.ID).isEmpty()) {
                        ss_users.setBackground(context.getDrawable(R.drawable.shape_error));
                    } else {
                        ss_users.setBackground(null);
                    }
                    //
                    if (et_form_when_ttl.getmValue() == null || et_form_when_ttl.getmValue().isEmpty()) {
                        et_form_when_ttl.setBackground(context.getDrawable(R.drawable.shape_error));
                    } else {
                        et_form_when_ttl.setBackground(null);
                    }
                    return false;

                }
                //
                break;

            case Constant.SYS_STATUS_EDIT:
            case Constant.SYS_STATUS_CANCELLED:
            default:
                break;
        }
        //Se tudo ok, remove borda de erro dos campos
        ss_status.setBackground(null);
        ss_users.setBackground(null);
        et_form_when_ttl.setBackground(null);
        return true;
    }

    private boolean checkDataChanges(ArrayList<Object> properties) {
        mDataChanged = false;
        //
        for (int i = 0; i < properties.size(); i++) {
            Object propertie = properties.get(i);
            //Se for SearchableSpinner
            if (propertie instanceof SearchableSpinner) {
                if (((SearchableSpinner) propertie).hasChangedBD()) {
                    mDataChanged = true;
                    //
                    return true;
                }

            } else if (propertie instanceof MKEditTextNM) {
                String tag = (String) ((MKEditTextNM) propertie).getTag() == null ? "" : (String) ((MKEditTextNM) propertie).getTag();
                String text = ((MKEditTextNM) propertie).getText().toString();

                if (!text.equals(tag)) {
                    mDataChanged = true;

                    return true;
                }
            } else if (propertie instanceof TextView) {
                String tag = (String) ((TextView) propertie).getTag() == null ? "" : (String) ((TextView) propertie).getTag();
                String text = ((TextView) propertie).getText().toString();

                if (!text.equals(tag)) {
                    mDataChanged = true;

                    return true;
                }
            } else if (propertie instanceof MkDateTime) {

                if (((MkDateTime) propertie).hasChanged()) {
                    mDataChanged = true;

                    return true;
                } else {
                }

            } else {
            }
        }
        //
        return mDataChanged;
    }

    private void save() {
        if (mPresenter.detectApSyncRequired(
                mCustomer_Code,
                mCustom_Form_Type,
                mCustom_Form_Code,
                mCustom_Form_Version,
                mCustom_Form_Data,
                mAp_Code
        )) {

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_sync_detected_tll"),
                    hmAux_Trans.get("alert_sync_detected_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ToolBox_Con.isOnline(context)) {
                                mPresenter.executeApSyncWs();
                            } else {
                                ToolBox_Inf.showNoConnectionDialog(context);

                            }
                        }
                    },
                    -1,
                    false
            );
        } else {

            mGe_custom_form_ap.setAp_status(ss_status.getmValue().get(SearchableSpinner.ID));
            mGe_custom_form_ap.setAp_when(et_form_when_ttl.getmValue());

            if (ss_users.getmValue().get(SearchableSpinner.ID) != null && ss_users.getmValue().get(SearchableSpinner.ID) != "null" && !ss_users.getmValue().get(SearchableSpinner.ID).isEmpty()) {
                mGe_custom_form_ap.setAp_who(Integer.parseInt(ss_users.getmValue().get(SearchableSpinner.ID)));
                mGe_custom_form_ap.setAp_who_nick(ToolBox_Inf.getFullNick(
                        ss_users.getmValue().get(SearchableSpinner.DESCRIPTION),
                        ss_users.getmValue().get(SearchableSpinner.ID)
                ));

            } else {
                mGe_custom_form_ap.setAp_who(null);
                mGe_custom_form_ap.setAp_who_nick(null);
            }

            if (ss_departments.getmValue().get(SearchableSpinner.ID) != null && ss_departments.getmValue().get(SearchableSpinner.ID) != "null" && !ss_departments.getmValue().get(SearchableSpinner.ID).isEmpty()) {
                mGe_custom_form_ap.setDepartment_code(Integer.parseInt(ss_departments.getmValue().get(SearchableSpinner.ID)));
                mGe_custom_form_ap.setDepartment_desc(ss_departments.getmValue().get(SearchableSpinner.DESCRIPTION));
                mGe_custom_form_ap.setDepartment_id(ss_departments.getmValue().get("department_id"));
            } else {
                mGe_custom_form_ap.setDepartment_code(null);
                mGe_custom_form_ap.setDepartment_desc(null);
                mGe_custom_form_ap.setDepartment_id(null);
            }
            mGe_custom_form_ap.setAp_what(ToolBox_Inf.prepareForNull(et_form_what_ttl.getText().toString()));
            mGe_custom_form_ap.setAp_where(ToolBox_Inf.prepareForNull(et_form_where_ttl.getText().toString()));
            mGe_custom_form_ap.setAp_why(ToolBox_Inf.prepareForNull(et_form_why_ttl.getText().toString()));
            mGe_custom_form_ap.setAp_how(ToolBox_Inf.prepareForNull(et_form_how_ttl.getText().toString()));
            mGe_custom_form_ap.setAp_how_much(et_form_how_mcuch_ttl.getText().toString().trim().replace(".", ","));
            mGe_custom_form_ap.setAp_what(ToolBox_Inf.prepareForNull(et_form_what_ttl.getText().toString()));
            mGe_custom_form_ap.setAp_comments(ToolBox_Inf.prepareForNull(et_form_comments_ttl.getText().toString()));
            mGe_custom_form_ap.setLast_update(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            mGe_custom_form_ap.setUpload_required(1);
            //
            if (ToolBox_Con.isOnline(context)) {
                mPresenter.executeWsApSave(mGe_custom_form_ap);
            } else {
                ToolBox_Inf.showNoConnectionDialog(context);
                //
                mGe_custom_form_apDao.addUpdate(
                        mGe_custom_form_ap
                );
                //
                mPresenter.getloadAP(
                        mCustomer_Code,
                        mCustom_Form_Type,
                        mCustom_Form_Code,
                        mCustom_Form_Version,
                        mCustom_Form_Data,
                        mAp_Code
                );
            }

            //mPresenter.loadSSStatus(mGe_custom_form_ap.getAp_status());
            //mPresenter.applyUserProfile(editable_views_list, mGe_custom_form_ap.getAp_status());

            setTags(mGe_custom_form_ap);
        }
    }

    private void setTags(GE_Custom_Form_Ap ap) {
        //21/11/2018 - LUCHE
        //Linha abaixo não é mais necessaria após a criação do
        //mValueDb no componente MkDateTime
        //et_form_when_ttl.setTag(ap.getAp_when() == null ? null : ap.getAp_when().replaceAll("[:][0-9][0-9] ", ":00 "));
        et_form_what_ttl.setTag(ap.getAp_what() == null ? "" : String.valueOf(ap.getAp_what()));
        et_form_where_ttl.setTag(ap.getAp_where() == null ? "" : String.valueOf(ap.getAp_where()));
        et_form_why_ttl.setTag(ap.getAp_why() == null ? "" : String.valueOf(ap.getAp_why()));
        et_form_how_ttl.setTag(ap.getAp_how() == null ? "" : String.valueOf(ap.getAp_how()));
        et_form_how_mcuch_ttl.setTag(ap.getAp_how_much() == null ? "" : String.valueOf(ap.getAp_how_much()).replace(",", "."));
        et_form_comments_ttl.setTag(ap.getAp_comments() == null ? "" : String.valueOf(ap.getAp_comments()));
        //
        ToolBox_Inf.setSSmValue(
                ss_status,
                String.valueOf(mGe_custom_form_ap.getAp_status()),
                hmAux_Trans.get(mGe_custom_form_ap.getAp_status()),
                true
        );

        ToolBox_Inf.setSSmValue(
                ss_users,
                String.valueOf(mGe_custom_form_ap.getAp_who()),
                mGe_custom_form_ap.getAp_who_nick(),
                true
        );

        ToolBox_Inf.setSSmValue(
                ss_departments,
                String.valueOf(mGe_custom_form_ap.getDepartment_code()),
                mGe_custom_form_ap.getDepartment_desc(),
                true,
                "department_id",
                mGe_custom_form_ap.getDepartment_id()
        );
    }

    private void showError() {

        if (mDataChanged) {
            showAlertDialog(
                    hmAux_Trans.get("alert_invalid_data_local_ttl"),
                    hmAux_Trans.get("alert_invalid_data_local_msg")
            );
        } else {
            showAlertDialog(
                    hmAux_Trans.get("alert_no_data_changes_ttl"),
                    hmAux_Trans.get("alert_no_data_changes_msg")
            );
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //mPresenter.onBackPressedClicked();
        if (mGe_custom_form_ap == null ||
                mGe_custom_form_ap.getAp_status().equalsIgnoreCase(Constant.SYS_STATUS_CANCELLED) ||
                mGe_custom_form_ap.getAp_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)) {
            actionBackPressed();

        } else {
            if (checkDataChanges(properties)) {
                ToolBox.alertMSG(
                        Act038_Main.this,
                        hmAux_Trans.get("alert_ap_exit_no_save_tll"),
                        hmAux_Trans.get("alert_ap_exit_no_save_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                actionBackPressed();
                            }
                        },
                        2,
                        null
                );
            } else {
                actionBackPressed();
            }
        }
    }

    private void actionBackPressed() {
        switch (requestingAct.toLowerCase()) {
            case Constant.ACT035:
                callAct035(context, mRoom_code);
                break;
            case Constant.ACT017:
                callAct017(context);
                break;
            case Constant.ACT039:
                callAct039(context);
                break;
            default:
                callAct037(context);
                break;
        }
    }

    private void callAct039(Context context) {
        Intent mIntent = new Intent(context, Act039_Main.class);
        //
        Bundle bundle = new Bundle();
        //
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    private void callAct017(Context context) {
        Intent mIntent = new Intent(context, Act017_Main.class);
        //
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        //no_selected_date tratativa para caso o item selecionado
        //tenha sido selecionado em uma listagem que não usava data
        //como filtro.
        //O scheduled_date sempre é enviado, pois a data recebida no bundle é
        //a data do item selecionada na lista.
        bundle.putString(ACT_SELECTED_DATE, no_selected_date ? null : scheduled_date);
        bundle.putBoolean(ACT_FILTER_FORM, filter_form);
        bundle.putBoolean(ACT_FILTER_FORM_AP, filter_form_ap);
        bundle.putBoolean(ACT_FILTER_SITE, filter_site);
        bundle.putBoolean(ACT_FILTER_LATE, filter_late);
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, filter_serial_id);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();

    }

    @Override
    public void callAct035(Context context, String room_code) {
        Intent mIntent = new Intent(context, Act035_Main.class);
        //
        Bundle bundle = new Bundle();
        //bundle.putString(CH_MessageDao.ROOM_CODE, mGe_custom_form_ap.getRoom_code());
        bundle.putString(CH_MessageDao.ROOM_CODE, room_code);
        bundle.putLong(CH_RoomDao.CUSTOMER_CODE, Long.parseLong(mCustomer_Code));
        //
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    public void callAct037(Context context) {
        Intent mIntent = new Intent(context, Act037_Main.class);
        //
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT036);
        //
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
        progressDialog.dismiss();

    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        if (ws_process.equalsIgnoreCase(WS_AP_Search.class.getSimpleName())) {
            resetWSProcess();
            //
            progressDialog.dismiss();
            //
            showMsg(
                    hmAux_Trans.get("alert_sync_success_ttl"),
                    hmAux_Trans.get("alert_sync_success_msg")
            );
            //
            mPresenter.getloadAP(
                    mCustomer_Code,
                    mCustom_Form_Type,
                    mCustom_Form_Code,
                    mCustom_Form_Version,
                    mCustom_Form_Data,
                    mAp_Code
            );
        } else {
            resetWSProcess();
            //
            progressDialog.dismiss();
        }
    }

    private void resetWSProcess() {
        setWSProcess("");
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (ws_process.equalsIgnoreCase(WS_Room_AP.class.getSimpleName())) {
            resetWSProcess();
            //
            progressDialog.dismiss();
            //
            callAct035(context, hmAux.get(CH_RoomDao.ROOM_CODE));

        } else if (ws_process.equalsIgnoreCase(WS_AP_Save.class.getSimpleName())) {
            progressDialog.dismiss();
            //
            String sKey = mCustomer_Code + "." + mCustom_Form_Type + "." + mCustom_Form_Code + "." + mCustom_Form_Version + "." + mCustom_Form_Data + "." + mAp_Code;
            String sValue = hmAux.get(sKey);
            //
            showResults(hmAux, sKey, sValue);
            //
//            mPresenter.getloadAP(
//                    mCustomer_Code,
//                    mCustom_Form_Type,
//                    mCustom_Form_Code,
//                    mCustom_Form_Version,
//                    mCustom_Form_Data,
//                    mAp_Code
//            );
            mGe_custom_form_ap = mPresenter.getAp(
                    mCustomer_Code,
                    mCustom_Form_Type,
                    mCustom_Form_Code,
                    mCustom_Form_Version,
                    mCustom_Form_Data,
                    mAp_Code
            );
            //
            if (mGe_custom_form_ap != null) {
                loadAP(mGe_custom_form_ap);
            }
        } else {
            resetWSProcess();
            //
            progressDialog.dismiss();
        }
    }

    private void showResults(HMAux aps, String ap_current, String ap_current_value) {
        ArrayList<HMAux> mAps = new ArrayList<>();

        for (String sKey : aps.keySet()) {
            HMAux hmAux = new HMAux();
            //
            hmAux.put("ap_code", sKey);
            //hmAux.put("ap_result", sKey + " - " + (aps.get(sKey).equals("1") ? "OK" : aps.get(sKey)));
            //
            String[] res = aps.get(sKey).split(Constant.MAIN_CONCAT_STRING);
            //
            //hmAux.put("ap_result", sKey + " - " + (aps.get(sKey).equals("1") ? "OK" : aps.get(sKey)));
            if (res.length > 1) {
                hmAux.put("ap_result", ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(res[0]), 20) + " - " + (res[1].equals("1") ? "OK" : res[1]));
            } else {
                hmAux.put("ap_result", "Tag Not Found - " + res[0]);
            }
            //
            hmAux.put("ap_label", hmAux_Trans.get("cell_ap_lbl"));

            mAps.add(hmAux);
        }

        showResultsDialog(mAps);
    }

    public void showResultsDialog(List<HMAux> aps) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        String[] from = {"ap_label", "ap_result"};
        int[] to = {R.id.act038_results_adapter_cell_tv_ttl, R.id.act038_results_adapter_cell_tv_msg_value};


        lv_results.setAdapter(
                new SimpleAdapter(
                        context,
                        aps,
                        R.layout.act038_results_adapter_cell,
                        from,
                        to
                )
        );

        //builder.setTitle(hmAux_Trans.get("alert_results_ttl"));
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGe_custom_form_ap != null) {
                    mPresenter.getloadAP(
                            mCustomer_Code,
                            mCustom_Form_Type,
                            mCustom_Form_Code,
                            mCustom_Form_Version,
                            mCustom_Form_Data,
                            mAp_Code
                    );
                    //
                    show.dismiss();
                } else {
                    actionBackPressed();
                }
            }
        });
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

    private void showMsg(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
    }

    @Override
    public void showAlertDialog(String title, String msg) {
        ToolBox.alertMSG(
                context,
                title,
                msg,
                null,
                0
        );
    }

    /**
     * Metodo antigo que alterava o dado diretamente no EditText
     * Os dados algora só devem ser salvos se o usr clicar em um
     * dos botões de ação.(Retroceder,Avançar ou Aplicar)
     * Implementado em 03/04/18
     *
     * @param index
     */
    public void show_Edit_InfoDialogV2(int index) {

        if (index >= 0 && index < editable_views_list_long.size()) {
            editable_views_current = (TextView) editable_views_list_long.get(index);
            editable_views_current_Index = index;
        }

        try {
            //
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.act038_edit_info, null);

            final TextView tv_field_desc = (TextView) view.findViewById(R.id.act038_edit_info_tv_field_desc);
            final MKEditTextNM mk_value = (MKEditTextNM) view.findViewById(R.id.act038_edit_info_mk_value);
            final ImageView iv_prev = (ImageView) view.findViewById(R.id.act038_edit_info_iv_prev);
            final ImageView iv_next = (ImageView) view.findViewById(R.id.act038_edit_info_iv_next);
            final ImageView iv_cancel = (ImageView) view.findViewById(R.id.act038_edit_info_iv_cancel);
            final ImageView iv_save = (ImageView) view.findViewById(R.id.act038_edit_info_iv_save);

            tv_field_desc.setText(editable_views_list_long_names.get(index));

            mk_value.setOnReportTextChangeListner(null);
            //
            mk_value.setText(editable_views_current.getText().toString().trim());
            //
            //mk_value.setOnReportTextChangeListner(txtChange);
            //
            iv_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Salva alterações no texto
                    commitMketChages(mk_value.getText().toString().trim());
                    //
                    int indexAux = editable_views_current_Index - 1;

                    if (indexAux >= 0 && indexAux < editable_views_list_long.size()) {
                        editable_views_current = (TextView) editable_views_list_long.get(indexAux);
                        editable_views_current_Index = indexAux;
                        //
                        tv_field_desc.setText(editable_views_list_long_names.get(indexAux));
                        //
                        mk_value.setOnReportTextChangeListner(null);
                        //
                        mk_value.setText(editable_views_current.getText().toString().trim());
                        //
                        //mk_value.setOnReportTextChangeListner(txtChange);
                    } else {
                    }
                }
            });
            //
            iv_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Salva alterações no texto
                    commitMketChages(mk_value.getText().toString().trim());
                    //
                    int indexAux = editable_views_current_Index + 1;

                    if (indexAux >= 0 && indexAux < editable_views_list_long.size()) {
                        editable_views_current = (TextView) editable_views_list_long.get(indexAux);
                        editable_views_current_Index = indexAux;
                        //
                        tv_field_desc.setText(editable_views_list_long_names.get(indexAux));
                        //
                        mk_value.setOnReportTextChangeListner(null);
                        //
                        mk_value.setText(editable_views_current.getText().toString().trim());
                        //
                        //mk_value.setOnReportTextChangeListner(txtChange);
                    } else {
                    }
                }
            });
            //
            builder
                    .setView(view)
                    .setCancelable(false);
            //
            final AlertDialog dialog = builder.create();
            dialog.show();
            //
            iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog innerDialog = dialog;
                    String txtNew = mk_value.getText().toString().trim();
                    String txtOld = editable_views_current.getText().toString().trim();
                    if (!txtNew.equals(txtOld)) {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_discard_changes_ttl"),
                                hmAux_Trans.get("alert_discard_changes_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        innerDialog.dismiss();
                                    }
                                },
                                1
                        );
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            //
            iv_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commitMketChages(mk_value.getText().toString().trim());
                    //
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }

    }

    private void commitMketChages(String new_text) {
        editable_views_current.setText(new_text);
    }

    /**
     * Metodo antigo que alterava o dado diretamente no EditText
     * Foi substituido pelo V2
     *
     * @param index
     */
    public void show_Edit_InfoDialog(int index) {

        if (index >= 0 && index < editable_views_list_long.size()) {
            editable_views_current = (TextView) editable_views_list_long.get(index);
            editable_views_current_Index = index;
        }

        try {
            //
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.act038_edit_info, null);

            final TextView tv_field_desc = (TextView) view.findViewById(R.id.act038_edit_info_tv_field_desc);
            final MKEditTextNM mk_value = (MKEditTextNM) view.findViewById(R.id.act038_edit_info_mk_value);
            final ImageView iv_prev = (ImageView) view.findViewById(R.id.act038_edit_info_iv_prev);
            final ImageView iv_next = (ImageView) view.findViewById(R.id.act038_edit_info_iv_next);
            final ImageView iv_cancel = (ImageView) view.findViewById(R.id.act038_edit_info_iv_cancel);
            final ImageView iv_save = (ImageView) view.findViewById(R.id.act038_edit_info_iv_save);

            tv_field_desc.setText(editable_views_list_long_names.get(index));

            mk_value.setOnReportTextChangeListner(null);
            //
            mk_value.setText(editable_views_current.getText().toString().trim());
            //
            mk_value.setOnReportTextChangeListner(txtChange);
            //
            iv_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int indexAux = editable_views_current_Index - 1;

                    if (indexAux >= 0 && indexAux < editable_views_list_long.size()) {
                        editable_views_current = (TextView) editable_views_list_long.get(indexAux);
                        editable_views_current_Index = indexAux;
                        //
                        tv_field_desc.setText(editable_views_list_long_names.get(indexAux));
                        //
                        mk_value.setOnReportTextChangeListner(null);
                        //
                        mk_value.setText(editable_views_current.getText().toString().trim());
                        //
                        mk_value.setOnReportTextChangeListner(txtChange);
                    } else {
                    }
                }
            });
            //
            iv_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int indexAux = editable_views_current_Index + 1;

                    if (indexAux >= 0 && indexAux < editable_views_list_long.size()) {
                        editable_views_current = (TextView) editable_views_list_long.get(indexAux);
                        editable_views_current_Index = indexAux;
                        //
                        tv_field_desc.setText(editable_views_list_long_names.get(indexAux));
                        //
                        mk_value.setOnReportTextChangeListner(null);
                        //
                        mk_value.setText(editable_views_current.getText().toString().trim());
                        //
                        mk_value.setOnReportTextChangeListner(txtChange);
                    } else {
                    }
                }
            });
            //
            builder
                    .setView(view)
                    .setCancelable(true);
            //
            final AlertDialog dialog = builder.create();
            dialog.show();
            //
            iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            //
            iv_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } catch (
                Exception e)

        {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }

    }

    private MKEditTextNM.IMKEditTextChangeText txtChange = new MKEditTextNM.IMKEditTextChangeText() {
        @Override
        public void reportTextChange(String s) {
            editable_views_current.setText(s);
        }

        @Override
        public void reportTextChange(String s, boolean b) {

        }
    };

    //Tratativa SESSION NOT FOUND
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

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        progressDialog.dismiss();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
    }

    /**
     * 20/11/2018 - LUCHE
     * BR que é acionado somente pelo serviço de download de PDF e somente essa Act recebe ele.
     * A cada iteração do loop de download de PDF de FORM AP, esse BR é disparado.
     * Receiver analisa se tipo é ok ou error e se BR for do Ap carregado na tela, trata
     * o retorno, abrindo o PDF ou fechando o Dialog Informando o erro.
     */
    protected class PDFStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String mType = intent.getStringExtra(ToolBox.SW_TYPE);
            HMAux hmAux = (HMAux) intent.getSerializableExtra(ToolBox.SW_HMAUX);
            //
            if (mType.equalsIgnoreCase(Constant.TYPE_BR_AP_OK)) {
                processPDF(hmAux);
            }else if(mType.equalsIgnoreCase(Constant.TYPE_BR_AP_ERROR)){
                processPDFDownloadError(hmAux);
            }
        }
    }

    private void processPDFDownloadError(HMAux hmAux) {
        String sKey = mCustomer_Code + "." + mCustom_Form_Type + "." + mCustom_Form_Code + "." + mCustom_Form_Version + "." + mCustom_Form_Data;
        //
        if (hmAux.get("pk").equalsIgnoreCase(sKey)) {
            //ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", "AP_DOWNLOAD_ERROR", "", "0");
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("alert_form_ap_pdf_download_error_msg"), "", "0");
        }
    }

    private void processPDF(HMAux hmAux) {
        String sKey = mCustomer_Code + "." + mCustom_Form_Type + "." + mCustom_Form_Code + "." + mCustom_Form_Version + "." + mCustom_Form_Data;

        if (hmAux.get("pk").equalsIgnoreCase(sKey)) {
            mGe_custom_form_ap.setCustom_form_url_local(hmAux.get("value"));
            //
            if (progressDialog != null && progressDialog.isShowing()) {
                disableProgressDialog();
            }
            //
            btn_pdf.performClick();
        }
    }

    @Override
    public void startReceivers(boolean start_stop) {
        if (mPdfStatusReceiver == null) {
            mPdfStatusReceiver = new PDFStatusReceiver();
        }
        IntentFilter mPdfStatusFilter = new IntentFilter(SW_TYPE_BR_AP);
        mPdfStatusFilter.addCategory(Intent.CATEGORY_DEFAULT);

        if (start_stop) {
            LocalBroadcastManager.getInstance(Act038_Main.this).registerReceiver(mPdfStatusReceiver, mPdfStatusFilter);
        } else {
            LocalBroadcastManager.getInstance(Act038_Main.this).unregisterReceiver(mPdfStatusReceiver);
            //
            mPdfStatusReceiver = null;
        }
    }

    private void activateDownLoadPDF(Context context) {
        Intent mIntent = new Intent(context, WBR_DownLoad_PDF.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void openPDF() {
        File file = new File(Constant.CACHE_PATH + "/" +
                "form_ap_" +
                String.valueOf(mGe_custom_form_ap.getCustomer_code()) + "_" +
                String.valueOf(mGe_custom_form_ap.getCustom_form_type()) + "_" +
                String.valueOf(mGe_custom_form_ap.getCustom_form_code()) + "_" +
                String.valueOf(mGe_custom_form_ap.getCustom_form_version()) + "_" +
                String.valueOf(mGe_custom_form_ap.getCustom_form_data()) +
                ".pdf"
        );

        if (file.exists()) {
            repeatTry = 0;
            try {

                ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF);

                ToolBox_Inf.copyFile(
                        file,
                        new File(Constant.CACHE_PDF)
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Constant.CACHE_PDF + "/" +
                            "form_ap_" +
                            String.valueOf(mGe_custom_form_ap.getCustomer_code()) + "_" +
                            String.valueOf(mGe_custom_form_ap.getCustom_form_type()) + "_" +
                            String.valueOf(mGe_custom_form_ap.getCustom_form_code()) + "_" +
                            String.valueOf(mGe_custom_form_ap.getCustom_form_version()) + "_" +
                            String.valueOf(mGe_custom_form_ap.getCustom_form_data()) +
                            ".pdf")),
                    "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            startActivity(intent);
        } else {

            if (!ToolBox_Con.isOnline(context)) {
                ToolBox_Inf.showNoConnectionDialog(context);
                //
                return;
            }

            if (repeatTry >= 1) {
                repeatTry = 0;

                return;
            } else {
                repeatTry++;
            }

            mPdfDownload =

                    new pdfDownload();


            mPdfDownload.execute(
                    "form_ap_" +
                            String.valueOf(mGe_custom_form_ap.getCustomer_code()) + "_" +
                            String.valueOf(mGe_custom_form_ap.getCustom_form_type()) + "_" +
                            String.valueOf(mGe_custom_form_ap.getCustom_form_code()) + "_" +
                            String.valueOf(mGe_custom_form_ap.getCustom_form_version()) + "_" +
                            String.valueOf(mGe_custom_form_ap.getCustom_form_data()),
                    mGe_custom_form_ap.getCustom_form_url()
            );

            showPDPDF(
                    hmAux_Trans.get("alert_no_pdf_tll"),
                    hmAux_Trans.get("alert_no_pdf_msg"),
                    false
            );

//            ToolBox.alertMSG(
//                    context,
//                    hmAux_Trans.get("alert_no_pdf_tll"),
//                    hmAux_Trans.get("alert_no_pdf_msg"),
//                    null,
//                    -1,
//                    false
//            );
        }

    }

    public void showPDPDF(String ttl, String msg, boolean cancelable) {
        //
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_no"),
                hmAux_Trans.get("sys_alert_btn_yes")
        );
        //
        progressDialog.setCancelable(cancelable);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mPdfDownload != null) {
                    mPdfDownload.cancel(true);
                }

                repeatTry = 0;
            }
        });
    }

    private class pdfDownload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                if (!ToolBox_Inf.verifyDownloadFileInf(Constant.CACHE_PATH + "/" +
                        strings[0] + ".pdf")) {

                    ToolBox_Inf.deleteDownloadFileInf(Constant.CACHE_PATH + "/" +
                            strings[0] + ".tmp");
                    //
                    ToolBox_Inf.downloadImagePDF(
                            strings[1],
                            Constant.CACHE_PATH + "/" +
                                    strings[0] + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(strings[0], ".pdf");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //
            disablePD();
            //
            openPDF();
        }
    }

    public void disablePD() {
        disableProgressDialog();
    }

}

