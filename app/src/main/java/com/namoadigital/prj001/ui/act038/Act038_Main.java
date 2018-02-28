package com.namoadigital.prj001.ui.act038;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.MD_Department;
import com.namoadigital.prj001.model.MD_User;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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
    private EditText et_form_what_ttl;
    private TextView tv_form_where_ttl;
    private EditText et_form_where_ttl;
    private TextView tv_form_why_ttl;
    private EditText et_form_why_ttl;
    private TextView tv_form_how_ttl;
    private EditText et_form_how_ttl;
    private TextView tv_form_how_much_ttl;
    private EditText et_form_how_mcuch_ttl;
    private TextView tv_form_comments_ttl;
    private EditText et_form_comments_ttl;
    private ArrayList<View> editable_views_list = new ArrayList<>();
    private Button btn_save;

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
        ss_status.setmLabel("Status -trad");
        ss_status.setmTitle("Procura -trad");
        //
        editable_views_list.add(ss_status);
        //
        et_form_when_ttl = (MkDateTime) findViewById(R.id.act038_header_et_form_when_ttl);
        editable_views_list.add(et_form_when_ttl);
        //
        ss_users = (SearchableSpinner) findViewById(R.id.act038_content_ss_users);
        ss_users.setmLabel("Quem -trad");
        ss_users.setmTitle("Procura -trad");
        editable_views_list.add(ss_users);
        //
        ss_departments = (SearchableSpinner) findViewById(R.id.act038_content_ss_departments);
        ss_departments.setmLabel("Departamento -trad");
        ss_departments.setmTitle("Procura -trad");
        editable_views_list.add(ss_departments);
        //
        tv_form_what_ttl = (TextView) findViewById(R.id.act038_opc_tv_what_ttl);
        et_form_what_ttl = (EditText) findViewById(R.id.act038_opc_et_what_ttl);
        editable_views_list.add(et_form_what_ttl);

        tv_form_where_ttl = (TextView) findViewById(R.id.act038_opc_tv_where_ttl);
        et_form_where_ttl = (EditText) findViewById(R.id.act038_opc_et_where_ttl);
        editable_views_list.add(et_form_where_ttl);

        tv_form_why_ttl = (TextView) findViewById(R.id.act038_opc_tv_why_ttl);
        et_form_why_ttl = (EditText) findViewById(R.id.act038_opc_et_why_ttl);
        editable_views_list.add(et_form_why_ttl);

        tv_form_how_ttl = (TextView) findViewById(R.id.act038_opc_tv_how_ttl);
        et_form_how_ttl = (EditText) findViewById(R.id.act038_opc_et_how_ttl);
        editable_views_list.add(et_form_how_ttl);

        tv_form_how_much_ttl = (TextView) findViewById(R.id.act038_opc_tv_how_much_ttl);
        et_form_how_mcuch_ttl = (EditText) findViewById(R.id.act038_opc_et_how_much_ttl);
        editable_views_list.add(et_form_how_mcuch_ttl);

        tv_form_comments_ttl = (TextView) findViewById(R.id.act038_opc_tv_comments_ttl);
        et_form_comments_ttl = (EditText) findViewById(R.id.act038_opc_et_comments_ttl);
        editable_views_list.add(et_form_comments_ttl);
        //
        btn_save = (Button) findViewById(R.id.act038_btn_save);
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
        mPresenter.loadSSStatus();
        mPresenter.loadSSUsers();
        mPresenter.loadSSDepartments();
        //
        mPresenter.applyUserProfile(editable_views_list);
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            mGe_custom_form_apDao = new GE_Custom_Form_ApDao(context);
            //
            requestingAct = bundle.getString(Constant.MAIN_REQUESTING_ACT, "");
            mCustomer_Code = bundle.getString(GE_Custom_Form_ApDao.CUSTOMER_CODE);
            mCustom_Form_Type = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE);
            mCustom_Form_Code = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE);
            mCustom_Form_Version = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION);
            mCustom_Form_Data = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA);
            mAp_Code = bundle.getString(GE_Custom_Form_ApDao.AP_CODE);

        } else {
        }
    }

    @Override
    public void showBtnSave(boolean visible) {
        btn_save.setVisibility(visible ? View.VISIBLE : View.GONE );
    }

    @Override
    public void loadAP(GE_Custom_Form_Ap ap) {
        mGe_custom_form_ap = ap;
        //
        tv_ap_desc_ttl.setText(String.valueOf(ap.getAp_code()) + " - " + ap.getAp_description());
        tv_customer_code_ttl.setText("Cód.do Cliente - Trad");
        et_customer_code_ttl.setEnabled(false);
        et_customer_code_ttl.setText(String.valueOf(ap.getCustom_form_code() + " - " + ToolBox_Con.getPreference_Customer_Code_NAME(context)));
        //
        tv_form_type_ttl.setText("Tipo de Formulário - Trad");
        et_form_type_ttl.setEnabled(false);
        et_form_type_ttl.setText(String.valueOf(ap.getCustom_form_type() + " - " + ap.getCustom_form_type_desc()));
        //
        tv_form_code_ttl.setText("Cód. do Formulário - Trad");
        et_form_code_ttl.setEnabled(false);
        et_form_code_ttl.setText(String.valueOf(ap.getCustom_form_code() + " - " + ap.getCustom_form_desc()));
        //
        tv_form_version_ttl.setText("Versão do Formulário - Trad");
        et_form_version_ttl.setEnabled(false);
        et_form_version_ttl.setText(String.valueOf(ap.getCustom_form_version()));
        //
        tv_form_seq_ttl.setText("Seq. Execucao - Trad");
        et_form_seq_ttl.setEnabled(false);
        et_form_seq_ttl.setText(String.valueOf(ap.getCustom_form_data()));
        //
        et_form_when_ttl.setmLabel("Quando - Trad");
        et_form_when_ttl.setEnabled(true);
        et_form_when_ttl.setmValue(ap.getAp_when() == null ? "" : String.valueOf(ap.getAp_when()));
        //
        tv_form_what_ttl.setText("O que - Trad");
        et_form_what_ttl.setEnabled(true);
        et_form_what_ttl.setText(ap.getAp_what() == null ? "" : String.valueOf(ap.getAp_what()));

        tv_form_where_ttl.setText("Onde - Trad");
        et_form_where_ttl.setEnabled(true);
        et_form_where_ttl.setText(ap.getAp_where() == null ? "" : String.valueOf(ap.getAp_where()));

        tv_form_why_ttl.setText("Por que - Trad");
        et_form_why_ttl.setEnabled(true);
        et_form_why_ttl.setText(ap.getAp_why() == null ? "" : String.valueOf(ap.getAp_why()));

        tv_form_how_ttl.setText("Como - Trad");
        et_form_how_ttl.setEnabled(true);
        et_form_how_ttl.setText(ap.getAp_how() == null ? "" : String.valueOf(ap.getAp_how()));

        tv_form_how_much_ttl.setText("Quanto - Trad");
        et_form_how_mcuch_ttl.setEnabled(true);
        et_form_how_mcuch_ttl.setText(ap.getAp_how_much() == null ? "" : String.valueOf(ap.getAp_how_much()));

        tv_form_comments_ttl.setText("Comentário - Trad");
        et_form_comments_ttl.setEnabled(true);
        et_form_comments_ttl.setText(ap.getAp_comments() == null ? "" : String.valueOf(ap.getAp_comments()));
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
                        true
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
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //mPresenter.onBackPressedClicked();

        Log.d("DDDD", "Passei por aqui!!!");

        callAct005(context);
    }

    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    }
}
