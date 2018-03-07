package com.namoadigital.prj001.ui.act038;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.MD_Department;
import com.namoadigital.prj001.model.MD_User;
import com.namoadigital.prj001.receiver.WBR_AP_Save;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.ui.act037.Act037_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act038_Main extends Base_Activity implements Act038_Main_View {

    private Act038_Main_Presenter_Impl mPresenter;

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
    private LinearLayout ll_header;

    private ImageView iv_opc_show_hide;
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
    private SearchableSpinner ss_status;
    private MkDateTime et_form_when_ttl;
    private SearchableSpinner ss_users;
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
    //Ap Agendado
    private String scheduled_date;
    private boolean filter_form;
    private boolean filter_form_ap;

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
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT038
        );

        loadTranslation();
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
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
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
                        )
                );

        iv_header_show_hide = (ImageView) findViewById(R.id.act038_header_iv_show_hide);
        ll_header = (LinearLayout) findViewById(R.id.act038_header_ll_show_hide);

        iv_opc_show_hide = (ImageView) findViewById(R.id.act038_opc_iv_show_hide);
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
        ss_status = (SearchableSpinner) findViewById(R.id.act038_content_ss_status);
        //ss_status.setmLabel(hmAux_Trans.get("status_lbl"));
        ss_status.setmTitle(hmAux_Trans.get("status_search_lbl"));
        //
        editable_views_list.add(ss_status);
        //
        et_form_when_ttl = (MkDateTime) findViewById(R.id.act038_header_et_form_when_ttl);
        editable_views_list.add(et_form_when_ttl);
        //
        ss_users = (SearchableSpinner) findViewById(R.id.act038_content_ss_users);
        ss_users.setmLabel(hmAux_Trans.get("ap_who_lbl"));
        ss_users.setmTitle(hmAux_Trans.get("ap_who_search_lbl"));
        editable_views_list.add(ss_users);
        //
        ss_departments = (SearchableSpinner) findViewById(R.id.act038_content_ss_departments);
        ss_departments.setmLabel(hmAux_Trans.get("department_lbl"));
        ss_departments.setmTitle(hmAux_Trans.get("department_search_lbl"));
        editable_views_list.add(ss_departments);
        //
        tv_form_what_ttl = (TextView) findViewById(R.id.act038_opc_tv_what_ttl);
        et_form_what_ttl = (TextView) findViewById(R.id.act038_opc_et_what_ttl);
        et_form_what_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialog(0);
            }
        });
        editable_views_list.add(et_form_what_ttl);
        editable_views_list_long.add(et_form_what_ttl);

        tv_form_where_ttl = (TextView) findViewById(R.id.act038_opc_tv_where_ttl);
        et_form_where_ttl = (TextView) findViewById(R.id.act038_opc_et_where_ttl);
        et_form_where_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialog(1);
            }
        });
        editable_views_list.add(et_form_where_ttl);
        editable_views_list_long.add(et_form_where_ttl);

        tv_form_why_ttl = (TextView) findViewById(R.id.act038_opc_tv_why_ttl);
        et_form_why_ttl = (TextView) findViewById(R.id.act038_opc_et_why_ttl);
        et_form_why_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialog(2);
            }
        });
        editable_views_list.add(et_form_why_ttl);
        editable_views_list_long.add(et_form_why_ttl);

        tv_form_how_ttl = (TextView) findViewById(R.id.act038_opc_tv_how_ttl);
        et_form_how_ttl = (TextView) findViewById(R.id.act038_opc_et_how_ttl);
        et_form_how_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialog(3);
            }
        });
        editable_views_list.add(et_form_how_ttl);
        editable_views_list_long.add(et_form_how_ttl);

        tv_form_how_much_ttl = (TextView) findViewById(R.id.act038_opc_tv_how_much_ttl);
        et_form_how_mcuch_ttl = (MKEditTextNM) findViewById(R.id.act038_opc_et_how_much_ttl);
        editable_views_list.add(et_form_how_mcuch_ttl);

        tv_form_comments_ttl = (TextView) findViewById(R.id.act038_opc_tv_comments_ttl);
        et_form_comments_ttl = (TextView) findViewById(R.id.act038_opc_et_comments_ttl);
        et_form_comments_ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Edit_InfoDialog(4);
            }
        });
        editable_views_list.add(et_form_comments_ttl);
        editable_views_list_long.add(et_form_comments_ttl);
        //
        btn_pdf = (Button) findViewById(R.id.act038_content_btn_pdf);
        btn_chat_nav = (Button) findViewById(R.id.act038_content_btn_chat_nav);
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
        mPresenter.loadSSStatus(mGe_custom_form_ap.getAp_status());
        mPresenter.loadSSUsers();
        mPresenter.loadSSDepartments();
        //
        mPresenter.applyUserProfile(editable_views_list);
        //
        if (mGe_custom_form_ap.getSync_required() == 1) {
            if (ToolBox_Con.isOnline(context)) {
                mPresenter.executeApSyncWs();
            } else {
                ToolBox_Inf.showNoConnectionDialog(context);
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
            //Fluxo vindo do agendamento
            scheduled_date = bundle.getString(Act016_Main.ACT016_SELECTED_DATE,ToolBox.sDTFormat_Agora("yyyy-MM-dd").replace(":",""));
            filter_form = bundle.getBoolean(Act016_Main.ACT016_FILTER_FORM,true);
            filter_form_ap = bundle.getBoolean(Act016_Main.ACT016_FILTER_FORM_AP,true);

        } else {
        }
    }

    @Override
    public void showBtnSave(boolean visible) {
        btn_save.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void loadAP(GE_Custom_Form_Ap ap) {
        mGe_custom_form_ap = ap;
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
        et_form_when_ttl.setmLabel(hmAux_Trans.get("ap_when_lbl"));
        et_form_when_ttl.setEnabled(true);
        et_form_when_ttl.setmValue(ap.getAp_when() == null ? "" : String.valueOf(ap.getAp_when()));
        //
        tv_form_what_ttl.setText(hmAux_Trans.get("ap_what_lbl"));
        et_form_what_ttl.setEnabled(true);
        et_form_what_ttl.setText(ap.getAp_what() == null ? "" : String.valueOf(ap.getAp_what()));
        editable_views_list_long_names.add(hmAux_Trans.get("ap_what_lbl"));

        tv_form_where_ttl.setText(hmAux_Trans.get("ap_where_lbl"));
        et_form_where_ttl.setEnabled(true);
        et_form_where_ttl.setText(ap.getAp_where() == null ? "" : String.valueOf(ap.getAp_where()));
        editable_views_list_long_names.add(hmAux_Trans.get("ap_where_lbl"));

        tv_form_why_ttl.setText(hmAux_Trans.get("ap_why_lbl"));
        et_form_why_ttl.setEnabled(true);
        et_form_why_ttl.setText(ap.getAp_why() == null ? "" : String.valueOf(ap.getAp_why()));
        editable_views_list_long_names.add(hmAux_Trans.get("ap_why_lbl"));

        tv_form_how_ttl.setText(hmAux_Trans.get("ap_how_lbl"));
        et_form_how_ttl.setEnabled(true);
        et_form_how_ttl.setText(ap.getAp_how() == null ? "" : String.valueOf(ap.getAp_how()));
        editable_views_list_long_names.add(hmAux_Trans.get("ap_how_lbl"));

        tv_form_how_much_ttl.setText(hmAux_Trans.get("ap_how_much_lbl"));
        et_form_how_mcuch_ttl.setEnabled(true);
        et_form_how_mcuch_ttl.setText(ap.getAp_how_much() == null ? "" : String.valueOf(ap.getAp_how_much()).replace(",", "."));

        tv_form_comments_ttl.setText(hmAux_Trans.get("ap_comments_lbl"));
        et_form_comments_ttl.setEnabled(true);
        et_form_comments_ttl.setText(ap.getAp_comments() == null ? "" : String.valueOf(ap.getAp_comments()));
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
        iv_header_show_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_header.getVisibility() != View.VISIBLE) {
                    ll_header.setVisibility(View.VISIBLE);
                } else {
                    ll_header.setVisibility(View.GONE);
                }
            }
        });

        btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mGe_custom_form_ap.getCustom_form_url_local().trim().length() != 0) {

                    File file = new File(Constant.CACHE_PATH + "/" + mGe_custom_form_ap.getCustom_form_url_local().trim());

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
                    intent.setDataAndType(Uri.fromFile(new File(Constant.CACHE_PDF + "/" + mGe_custom_form_ap.getCustom_form_url_local().trim())), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    startActivity(intent);
                }
            }
        });

        btn_chat_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "chat_nav", Toast.LENGTH_SHORT).show();
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

        iv_opc_show_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_opc.getVisibility() != View.VISIBLE) {
                    ll_opc.setVisibility(View.VISIBLE);
                } else {
                    ll_opc.setVisibility(View.GONE);
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGe_custom_form_ap.setAp_status(ss_status.getmValue().get(SearchableSpinner.ID));
                mGe_custom_form_ap.setAp_when(et_form_when_ttl.getmValue());
                if (ss_departments.getmValue().get(SearchableSpinner.ID) != null && !ss_departments.getmValue().get(SearchableSpinner.ID).isEmpty()) {
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

                mPresenter.loadSSStatus(mGe_custom_form_ap.getAp_status());
            }
        });
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //mPresenter.onBackPressedClicked();

        switch (requestingAct.toLowerCase()) {
            case Constant.ACT035:
                callAct035(context);
                break;
            case Constant.ACT017:
                callAct017(context);
                break;
            default:
                callAct037(context);
                break;
        }

    }

    private void callAct017(Context context) {
        Intent mIntent = new Intent(context, Act017_Main.class);
        //
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Act016_Main.ACT016_SELECTED_DATE, scheduled_date);
        bundle.putBoolean(Act016_Main.ACT016_FILTER_FORM,filter_form);
        bundle.putBoolean(Act016_Main.ACT016_FILTER_FORM_AP,filter_form_ap);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();

    }

    public void callAct035(Context context) {
        Intent mIntent = new Intent(context, Act035_Main.class);
        //
        Bundle bundle = new Bundle();
        bundle.putString(CH_MessageDao.ROOM_CODE, mGe_custom_form_ap.getRoom_code());
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
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        progressDialog.dismiss();
        //
        String sKey = mCustomer_Code + "." + mCustom_Form_Type + "." + mCustom_Form_Code + "." + mCustom_Form_Version + "." + mCustom_Form_Data + "." + mAp_Code;
        String sValue = hmAux.get(sKey);
        //
        showResults(hmAux, sKey, sValue);
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

    private void showResults(HMAux aps, String ap_current, String ap_current_value) {
        ArrayList<HMAux> mAps = new ArrayList<>();


        for (String sKey : aps.keySet()) {
            HMAux hmAux = new HMAux();
            //
            hmAux.put("ap_code", sKey);
            hmAux.put("ap_result", sKey + " - " + (aps.get(sKey).equals("1") ? "OK" : aps.get(sKey)));
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

        String[] from = {"ap_result"};
        int[] to = {R.id.act038_results_adapter_cell_tv_msg_value};


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
                show.dismiss();
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
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
    }

    private void testWsApSave() {
        Intent mIntent = new Intent(context, WBR_AP_Save.class);
        Bundle bundle = new Bundle();
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

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
}
