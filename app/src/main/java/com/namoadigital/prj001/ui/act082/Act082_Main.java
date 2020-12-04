package com.namoadigital.prj001.ui.act082;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
import java.util.List;

public class Act082_Main extends Base_Activity_Frag_NFC_Geral implements Act082_Main_Contract.I_View {

    private Bundle requestingBundle;

    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private SearchableSpinner ss_main_user;
    private TextView tv_internal_comments_lbl;
    private MKEditTextNM mket_internal_comments;
    private LinearLayout ll_date_and_forecast_infos;
    private LinearLayout ll_header_infos;
    private TextView tv_date_and_forecast_infos_lbl;
    private RadioGroup rg_date_and_forecast_infos;
    private RadioButton rb_start_date;
    private RadioButton rb_end_date;
    private RadioButton rb_time;
    private View v_start_date_form;
    private View v_end_date_form;
    private View v_time_form;

    TextView tv_start_date_lbl;
    TextView tv_start_time_lbl;
    EditText edt_start_date_val;
    EditText edt_start_time_val;
    CheckBox chk_shift_ticket_start_date;
    CheckBox chk_shift_step_start_date;
    TextView tv_end_date_lbl;
    TextView tv_end_time_lbl;
    EditText edt_end_date_val;
    EditText edt_end_time_val;
    CheckBox chk_shift_ticket_end_date;
    CheckBox chk_shift_step_end_date;
    TextView tv_service_time_day_lbl;
    TextView tv_service_time_hour_lbl;
    TextView tv_service_time_minutes_lbl;
    EditText edt_service_time_day_val;
    EditText edt_service_time_hour_val;
    EditText edt_service_time_minutes_val;
    CheckBox chk_shift_step_service_time;

    private Button btn_cancel_header_form;
    private Button btn_save_header_form;
    private Act082_Main_Contract.I_Presenter mPresenter;
    private TK_Ticket mTk_ticket;
    private int mTkPrefix;
    private int mTkCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act082_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        bindViews();
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void bindViews() {
        ss_main_user = findViewById(R.id.act082_ss_main_user);
        ll_header_infos = findViewById(R.id.act082_ll_header_infos);
        tv_internal_comments_lbl = findViewById(R.id.act082_tv_internal_comments_lbl);
        mket_internal_comments = findViewById(R.id.act082_mket_internal_comments_val);
        ll_date_and_forecast_infos = findViewById(R.id.act082_ll_date_and_forecast_infos);
        tv_date_and_forecast_infos_lbl = findViewById(R.id.act082_tv_date_and_forecast_infos_lbl);
        rg_date_and_forecast_infos = findViewById(R.id.act082_rg_date_and_forecast_infos);
        rb_start_date = findViewById(R.id.act082_rb_start_date);
        v_start_date_form = findViewById(R.id.act082_start_date_form);
        rb_end_date = findViewById(R.id.act082_rb_end_date);
        v_end_date_form = findViewById(R.id.act082_end_date_form);
        rb_time = findViewById(R.id.act082_rb_time);
        v_time_form = findViewById(R.id.act082_cl_service_time_form);
        btn_cancel_header_form = findViewById(R.id.act082_btn_cancel_header_form);
        btn_save_header_form = findViewById(R.id.act082_btn_save_header_form);
        //
        tv_start_date_lbl = v_start_date_form.findViewById(R.id.tv_date_lbl);
        tv_start_time_lbl = v_start_date_form.findViewById(R.id.tv_time_lbl);
        edt_start_date_val = v_start_date_form.findViewById(R.id.tv_date_val);
        edt_start_time_val = v_start_date_form.findViewById(R.id.tv_time_val);
        chk_shift_ticket_start_date = v_start_date_form.findViewById(R.id.chk_shift_ticket_date);
        chk_shift_step_start_date = v_start_date_form.findViewById(R.id.chk_shift_step);
        //
        tv_end_date_lbl = v_end_date_form.findViewById(R.id.tv_date_lbl);
        tv_end_time_lbl = v_end_date_form.findViewById(R.id.tv_time_lbl);
        edt_end_date_val = v_end_date_form.findViewById(R.id.tv_date_val);
        edt_end_time_val = v_end_date_form.findViewById(R.id.tv_time_val);
        chk_shift_ticket_end_date = v_end_date_form.findViewById(R.id.chk_shift_ticket_date);
        chk_shift_step_end_date = v_end_date_form.findViewById(R.id.chk_shift_step);
        //
        tv_service_time_day_lbl = v_time_form.findViewById(R.id.act082_tv_service_time_day_lbl);
        tv_service_time_hour_lbl = v_time_form.findViewById(R.id.act082_tv_service_time_hour_lbl);
        tv_service_time_minutes_lbl = v_time_form.findViewById(R.id.act082_tv_service_time_minute_lbl);
        edt_service_time_day_val = v_time_form.findViewById(R.id.act082_edt_service_time_day_val);
        edt_service_time_hour_val = v_time_form.findViewById(R.id.act082_edt_service_time_hour_val);
        edt_service_time_minutes_val = v_time_form.findViewById(R.id.act082_edt_service_time_minute_val);
        chk_shift_step_service_time = v_time_form.findViewById(R.id.act082_chk_shift_service_time_refresh_step_duration);
    }


    private void initVars() {
        mPresenter = new Act082_Main_Presenter(context, this, hmAux_Trans);
        //
        recoverIntentBundle();
        //
        setLabels();
        //
        mTk_ticket = mPresenter.getTicketData(mTkPrefix, mTkCode);
        //
        setHeaderFragment(mTk_ticket);
        //
        setVisibilityByProfile();
    }

    private void setVisibilityByProfile() {
        if (mPresenter.getDateEditionProfile()) {
            ll_date_and_forecast_infos.setVisibility(View.VISIBLE);
            if (mPresenter.getStepEditTimeProfile()) {

            } else {

            }
        } else {
            ll_date_and_forecast_infos.setVisibility(View.GONE);
        }
        //
        if (mPresenter.getHeaderEditionProfile()) {
            ll_header_infos.setVisibility(View.VISIBLE);
        } else {
            ll_header_infos.setVisibility(View.GONE);
        }
        //
    }

    private void setLabels() {
        tv_internal_comments_lbl.setText(hmAux_Trans.get(""));
        tv_date_and_forecast_infos_lbl.setText(hmAux_Trans.get(""));
        rb_start_date.setText(hmAux_Trans.get(""));
        rb_end_date.setText(hmAux_Trans.get(""));
        rb_time.setText(hmAux_Trans.get(""));
        ss_main_user.setmTitle(hmAux_Trans.get(""));
        //
        tv_start_date_lbl.setText(hmAux_Trans.get(""));
        tv_start_time_lbl.setText(hmAux_Trans.get(""));
        chk_shift_ticket_start_date.setText(hmAux_Trans.get(""));
        chk_shift_step_start_date.setText(hmAux_Trans.get(""));
        tv_end_date_lbl.setText(hmAux_Trans.get(""));
        tv_end_time_lbl.setText(hmAux_Trans.get(""));
        chk_shift_ticket_end_date.setText(hmAux_Trans.get(""));
        chk_shift_step_end_date.setText(hmAux_Trans.get(""));
        tv_service_time_day_lbl.setText(hmAux_Trans.get(""));
        tv_service_time_hour_lbl.setText(hmAux_Trans.get(""));
        tv_service_time_minutes_lbl.setText(hmAux_Trans.get(""));
        chk_shift_step_service_time.setText(hmAux_Trans.get(""));
    }


    private void recoverIntentBundle() {

        requestingBundle = getIntent().getExtras();

        if (requestingBundle != null) {
            mTkPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        callAct070();
    }

    public void callAct070() {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(requestingBundle);
        //
        startActivity(intent);
        finish();
    }

    @Override
    public void setWsProcess(String wsProcess) {

    }

    @Override
    public void showPD(String progress_ttl, String progress_start) {

    }

    @Override
    public void showMsg(String show_ttl, String show_msg) {

    }

    @Override
    public void setProduct(ArrayList<MD_Product> productList) {

    }

    @Override
    public void callAct020(Context context, Bundle bundle) {

    }

    private void setHeaderFragment(TK_Ticket tkTicket) {
        fm = getSupportFragmentManager();
        String step_end_date = "-";
        String step_end_user_nick = "-";

        TK_Ticket_Step originStep = tkTicket.getStep().get(0);
        step_end_date = originStep.getStep_end_date();
        step_end_user_nick = originStep.getStep_end_user_nick();
        mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForOrigin(
                tkTicket,
                tkTicket.getTicket_id(),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(tkTicket.getOpen_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                tkTicket.getOpen_site_code(),
                tkTicket.getOpen_site_desc(),
                tkTicket.getOpen_serial_id(),
                tkTicket.getOpen_product_desc(),
                hmAux_Trans.get("schedule_action_origin_type_lbl"),
                context.getResources().getColor(R.color.grid_header_normal),
                "",
                ToolBox_Inf.getFormattedTicketOriginDesc(tkTicket.getOrigin_type(), tkTicket.getOrigin_desc()),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(originStep.getStep_end_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                tkTicket.getOpen_user_name()
        );
        //
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act082_frg_pipeline_header, mFrgPipelineHeader, mFrgPipelineHeader.getTag());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void iniSetup() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT082
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act082_title");
        transList.add("internal_comments_lbl");
        transList.add("date_and_forecast_infos_lbl");
        transList.add("edit_start_date_option");
        transList.add("edit_end_date_option");
        transList.add("edit_time_option");

        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initActions() {

        btn_save_header_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_cancel_header_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rg_date_and_forecast_infos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.act082_rb_start_date:

                        break;
                    case R.id.act082_rb_end_date:

                        break;
                    case R.id.act082_rb_time:

                        break;
                }

            }
        });


    }

    //region settings methods
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT082;
        mAct_Title = Constant.ACT082 + "_title";
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
        ToolBox_Inf.buildFooterDialog(context, false);
    }
    //endregion

}