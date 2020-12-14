package com.namoadigital.prj001.ui.act082;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_TK_Main_User_List;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Act082_Main extends Base_Activity_Frag_NFC_Geral implements Act082_Main_Contract.I_View, Frg_Pipeline_Header.OnPipelineFragmentOriginListener {

    public static final String FROM_EDIT_HEADER = "FROM_EDIT_HEADER";
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
    private boolean rb_start_date_is_checked = false;
    private RadioButton rb_end_date;
    private boolean rb_end_date_is_checked = false;
    private RadioButton rb_time;
    private boolean rb_time_is_checked = false;
    private View v_start_date_form;
    private View v_end_date_form;
    private View v_time_form;

    TextView tv_start_date_lbl;
    TextView tv_start_time_lbl;
    MkDateTime mkdt_start_date_val;
    CheckBox chk_shift_ticket_start_date;
    CheckBox chk_shift_step_start_date;
    TextView tv_end_date_lbl;
    TextView tv_end_time_lbl;
    MkDateTime mkdt_end_date_val;
    CheckBox chk_shift_ticket_end_date;
    CheckBox chk_shift_step_end_date;
    TextView tv_service_time_day_lbl;
    TextView tv_service_time_hour_lbl;
    TextView tv_service_time_minutes_lbl;
    EditText edt_service_time_day_val;
    EditText edt_service_time_hour_val;
    EditText edt_service_time_minutes_val;
    CheckBox chk_shift_step_service_time;
    TextView tv_start_date;
    TextView tv_end_date;
    TextView tv_service_time;

    private Button btn_cancel_header_form;
    private Button btn_save_header_form;
    private Act082_Main_Contract.I_Presenter mPresenter;
    private TK_Ticket mTk_ticket;
    private int mTkPrefix;
    private int mTkCode;
    private TextView tv_service_time_val;
    private LinearLayout ll_edit_buttons;
    private String wsProcess;
    private boolean header_data_has_changed = false;
    private boolean hasInternalCommentsChanged = false;

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
        ll_header_infos = findViewById(R.id.act082_ll_header_infos);
        ll_edit_buttons = findViewById(R.id.act082_ll_edit_buttons);
        ss_main_user = findViewById(R.id.act082_ss_main_user);
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
        tv_start_date = findViewById(R.id.act082_tv_start_date);
        tv_end_date = findViewById(R.id.act082_tv_end_date);
        tv_service_time = findViewById(R.id.act082_tv_service_time);
        //
        tv_start_date_lbl = v_start_date_form.findViewById(R.id.tv_date_lbl);
        tv_start_time_lbl = v_start_date_form.findViewById(R.id.tv_time_lbl);
        mkdt_start_date_val = v_start_date_form.findViewById(R.id.tv_date_val);
        chk_shift_ticket_start_date = v_start_date_form.findViewById(R.id.chk_shift_ticket_date);
        chk_shift_step_start_date = v_start_date_form.findViewById(R.id.chk_shift_step);
        //
        tv_end_date_lbl = v_end_date_form.findViewById(R.id.tv_date_lbl);
        tv_end_time_lbl = v_end_date_form.findViewById(R.id.tv_time_lbl);
        mkdt_end_date_val = v_end_date_form.findViewById(R.id.tv_date_val);
        chk_shift_ticket_end_date = v_end_date_form.findViewById(R.id.chk_shift_ticket_date);
        chk_shift_step_end_date = v_end_date_form.findViewById(R.id.chk_shift_step);
        //
        tv_service_time_val = v_time_form.findViewById(R.id.act082_tv_service_time);
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
        mket_internal_comments.setmBARCODE(false);
        tv_start_date.setText(ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(mTk_ticket.getStart_date()),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        ));
        //
        tv_end_date.setText(ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(mTk_ticket.getForecast_date()),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        ));
        //
        tv_service_time.setText(mTk_ticket.getForecast_time());
        //
        setVisibilityByProfile();
        //
        if (mTk_ticket.getMain_user() != null && mTk_ticket.getMain_user() > 0) {
            HMAux hmAuxMainUser = new HMAux();
            //
            hmAuxMainUser.put(SearchableSpinner.CODE, String.valueOf(mTk_ticket.getMain_user()));
            hmAuxMainUser.put(SearchableSpinner.ID, mTk_ticket.getMain_user_name());
            hmAuxMainUser.put(SearchableSpinner.DESCRIPTION, mTk_ticket.getMain_user_nick());
            ss_main_user.setmValue(hmAuxMainUser);
        }
        //
        mPresenter.callMainUserService(mTk_ticket);
    }

    @Override
    public void handleReadOnly(boolean offlineMode) {
        if (!mPresenter.getDateEditionProfile() || offlineMode) {
            setDateReadOnly();
        } else {
            enableHeaderEdit();
        }
        //
        if (!mPresenter.getHeaderEditionProfile() || offlineMode) {
            setHeaderReadOnly();
        }
    }

    @Override
    public void setMainUserSSList(ArrayList<HMAux> hmAuxMainUser) {
        //
        ss_main_user.setmOption(hmAuxMainUser);
        //
    }

    private void setHeaderReadOnly() {
        mket_internal_comments.setEnabled(false);
        ss_main_user.setmEnabled(false);
        ss_main_user.setmCanClean(false);
    }

    private void enableHeaderEdit() {
        mket_internal_comments.setEnabled(true);
        ss_main_user.setmEnabled(true);
        ss_main_user.setmCanClean(true);
    }

    private void setDateReadOnly() {
        rb_start_date.setEnabled(false);
        rb_end_date.setEnabled(false);
        rb_time.setEnabled(false);
        rg_date_and_forecast_infos.setEnabled(false);
    }

    private void setVisibilityByProfile() {
        //
        ll_date_and_forecast_infos.setVisibility(View.VISIBLE);
        ll_header_infos.setVisibility(View.VISIBLE);
        //
        if (mPresenter.getDateEditionProfile()) {
            if (mPresenter.getStepEditTimeProfile()) {
                chk_shift_step_start_date.setVisibility(View.VISIBLE);
                chk_shift_step_end_date.setVisibility(View.VISIBLE);
            } else {
                chk_shift_step_start_date.setVisibility(View.GONE);
                chk_shift_step_end_date.setVisibility(View.GONE);
            }
        }
        //
    }

    private void setLabels() {
        tv_internal_comments_lbl.setText(hmAux_Trans.get("internal_comments_lbl"));
        tv_date_and_forecast_infos_lbl.setText(hmAux_Trans.get("forecast_service_date_lbl"));
        rb_start_date.setText(hmAux_Trans.get("start_service_datetime_opt"));
        rb_end_date.setText(hmAux_Trans.get("end_service_datetime_opt"));
        rb_time.setText(hmAux_Trans.get("service_duration_time_opt"));
        ss_main_user.setmTitle(hmAux_Trans.get("main_user_lbl"));
        ss_main_user.setmLabel(hmAux_Trans.get("main_user_lbl"));
        //
        tv_start_date_lbl.setText(hmAux_Trans.get("service_datetime_start_lbl"));
        chk_shift_ticket_start_date.setText(hmAux_Trans.get("change_end_date_opt"));
        chk_shift_step_start_date.setText(hmAux_Trans.get("change_step_deadline_opt"));
        tv_end_date_lbl.setText(hmAux_Trans.get("service_datetime_end_lbl"));
        chk_shift_ticket_end_date.setText(hmAux_Trans.get("change_start_date_opt"));
        chk_shift_step_end_date.setText(hmAux_Trans.get("change_step_deadline_opt"));
        tv_service_time_day_lbl.setText(hmAux_Trans.get("days_lbl"));
        tv_service_time_hour_lbl.setText(hmAux_Trans.get("hour_lbl"));
        tv_service_time_minutes_lbl.setText(hmAux_Trans.get("minutes_lbl"));
        chk_shift_step_service_time.setText(hmAux_Trans.get("recalculate_step_duration"));
        //
        btn_save_header_form.setText(hmAux_Trans.get("btn_save_lbl"));
        btn_cancel_header_form.setText(hmAux_Trans.get("sys_alert_btn_cancel"));
        //
        mkdt_start_date_val.setmLabel("");
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
        this.wsProcess = wsProcess;
    }

    @Override
    public void showPD(String progress_ttl, String progress_start) {
        enableProgressDialog(
                progress_ttl,
                progress_start,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
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

        mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForHeaderEdit(
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
                ToolBox_Inf.getFormattedTicketOriginDesc(tkTicket.getOrigin_type(), tkTicket.getOrigin_desc())
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
        transList.add("forecast_service_date_lbl");
        transList.add("start_service_datetime_opt");
        transList.add("end_service_datetime_opt");
        transList.add("service_duration_time_opt");
        transList.add("main_user_lbl");
        transList.add("service_datetime_start_lbl");
        transList.add("change_end_date_opt");
        transList.add("change_step_deadline_opt");
        transList.add("service_datetime_end_lbl");
        transList.add("change_start_date_opt");
        transList.add("change_step_deadline_opt");
        transList.add("days_lbl");
        transList.add("hour_lbl");
        transList.add("minutes_lbl");
        transList.add("recalculate_step_duration");
        transList.add("btn_save_lbl");
        //
        transList.add("dialog_main_user_search_ttl");
        transList.add("dialog_main_user_search_msg");
        transList.add("alert_invalid_scn_ttl");
        transList.add("alert_invalid_scn_msg");
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
                String timeAction="";
                String forecast_time = null;
                String start_date = null;
                String forecast_date = null;
                String internalComments = null;
                int move_other_date = 0;
                int move_steps = 0;
                //
                String mInternalComments = mket_internal_comments.getText() == null ? "" : mket_internal_comments.getText().toString();
                String tkInternalComments =  mTk_ticket.getInternal_comments() == null ? "" : mTk_ticket.getInternal_comments();
                if(hasInternalCommentsChanged){
                    header_data_has_changed = true;
                    internalComments = mInternalComments;
                }
                Integer mainUserValue = null;
                if(ss_main_user.hasChanged()) {
                    if(ss_main_user.getmValue().hasConsistentValue(SearchableSpinner.CODE)){
                        mainUserValue = Integer.valueOf(ss_main_user.getmValue().get(SearchableSpinner.CODE));
                    }
                }else{
                    mainUserValue = -1;
                }
                //
                if (rb_start_date.isChecked()) {
                    if(header_data_has_changed){
                        timeAction = ConstantBaseApp.TK_TICKET_START_DATE_AND_HEADER;
                    }else{
                        timeAction = ConstantBaseApp.TK_TICKET_START_DATE;
                    }
                    start_date = mkdt_start_date_val.getmValue();
                } else if (rb_end_date.isChecked()) {
                    if(header_data_has_changed){
                        timeAction = ConstantBaseApp.TK_TICKET_FORECAST_DATE_AND_HEADER;
                    }else{
                        timeAction = ConstantBaseApp.TK_TICKET_FORECAST_DATE;
                    }
                    forecast_date = mkdt_end_date_val.getmValue();
                } else if (rb_time.isChecked()) {
                    if(header_data_has_changed){
                        timeAction = ConstantBaseApp.TK_TICKET_FORECAST_TIME_AND_HEADER;
                    }else{
                        timeAction = ConstantBaseApp.TK_TICKET_FORECAST_TIME;
                    }
                    forecast_time = mPresenter.getTimeFromForm(edt_service_time_day_val.getText().toString(), edt_service_time_hour_val.getText().toString(), edt_service_time_minutes_val.getText().toString());
                }else{
                    if(header_data_has_changed){
                        timeAction = ConstantBaseApp.TK_TICKET_EDIT_HEADER;
                    }
                }
                //
                mPresenter.callEditHeaderService(
                        mTk_ticket.getTicket_prefix(),
                        mTk_ticket.getTicket_code(),
                        mTk_ticket.getScn(),
                        mainUserValue,
                        ss_main_user.getmValue().get(SearchableSpinner.DESCRIPTION),
                        ss_main_user.getmValue().get(SearchableSpinner.ID),
                        forecast_time,
                        start_date,
                        forecast_date,
                        timeAction,
                        internalComments,
                        move_other_date,
                        move_steps
                );
            }
        });

        btn_cancel_header_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //
        rb_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_start_date_is_checked) {
                    rb_start_date_is_checked = false;
                    rg_date_and_forecast_infos.clearCheck();
                } else {
                    rb_start_date_is_checked = true;
                    rb_time_is_checked = false;
                    rb_end_date_is_checked = false;
                }
                //
                if (rb_start_date_is_checked) {
                    v_start_date_form.setVisibility(View.VISIBLE);
//                    String start_date = tv_start_date.getText().toString();

                    mkdt_start_date_val.setmValue(mTk_ticket.getStart_date(), true);
//                    edt_start_time_val.setText(dateSplit[1]);
                    v_end_date_form.setVisibility(View.GONE);
                    v_time_form.setVisibility(View.GONE);
                    tv_start_date.setVisibility(View.GONE);
                    tv_end_date.setVisibility(View.VISIBLE);
                    tv_service_time.setVisibility(View.VISIBLE);
                } else {
                    tv_start_date.setVisibility(View.VISIBLE);
                    v_start_date_form.setVisibility(View.GONE);
                }
            }
        });
        //
        rb_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_end_date_is_checked) {
                    rb_end_date_is_checked = false;
                    rg_date_and_forecast_infos.clearCheck();
                } else {
                    rb_start_date_is_checked = false;
                    rb_end_date_is_checked = true;
                    rb_time_is_checked = false;
                }
                //
                if (rb_end_date_is_checked) {
//                    String end_date = tv_end_date.getText().toString();
                    mkdt_end_date_val.setmValue(mTk_ticket.getForecast_date(), true);

                    v_end_date_form.setVisibility(View.VISIBLE);
                    v_time_form.setVisibility(View.GONE);
                    v_start_date_form.setVisibility(View.GONE);
                    tv_end_date.setVisibility(View.GONE);
                    tv_start_date.setVisibility(View.VISIBLE);
                    tv_service_time.setVisibility(View.VISIBLE);
                } else {
                    tv_end_date.setVisibility(View.VISIBLE);
                    v_end_date_form.setVisibility(View.GONE);
                }
            }
        });
        //
        rb_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_time_is_checked) {
                    rb_time_is_checked = false;
                    rg_date_and_forecast_infos.clearCheck();
                } else {
                    rb_time_is_checked = true;
                    rb_end_date_is_checked = false;
                    rb_start_date_is_checked = false;
                }
                //
                if (rb_time_is_checked) {
                    String service_time = tv_service_time.getText().toString();
                    String[] dayTimeSplit = service_time.split(" ");
                    String[] timeSplit = new String[3];

                    timeSplit[0] = dayTimeSplit.length > 1 ? dayTimeSplit[0] : "0";
                    int firstIdx = dayTimeSplit.length > 1 ? 1 : 0;
                    String[] aux = dayTimeSplit[firstIdx].split(":");
                    timeSplit[1] = aux[0];
                    timeSplit[2] = aux[1];

                    edt_service_time_day_val.setText(timeSplit[0]);
                    edt_service_time_hour_val.setText(timeSplit[1]);
                    edt_service_time_minutes_val.setText(timeSplit[2]);
                    v_time_form.setVisibility(View.VISIBLE);
                    v_start_date_form.setVisibility(View.GONE);
                    v_end_date_form.setVisibility(View.GONE);
                    tv_service_time.setVisibility(View.GONE);
                    tv_start_date.setVisibility(View.VISIBLE);
                    tv_end_date.setVisibility(View.VISIBLE);
                } else {
                    tv_service_time.setVisibility(View.VISIBLE);
                    v_time_form.setVisibility(View.GONE);
                }
            }
        });


        mkdt_start_date_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setDateValue((TextView) v);
            }
        });
        //
        mkdt_end_date_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setDateValue((TextView) v);
            }
        });
        //
        mket_internal_comments.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                hasInternalCommentsChanged =true;
            }
        });
        //
    }

    private void setDateValue(final TextView v) {
        int year;
        int month;
        int day;
        String value = v.getText().toString();
        //
        if(value != null
                && !value.isEmpty()) {
            Calendar calendario = Calendar.getInstance();
            year  = calendario.get(Calendar.YEAR);
            month = calendario.get(Calendar.MONTH);
            day   = calendario.get(Calendar.DAY_OF_MONTH);
        }else{
            String[] split = value.split("/");
            try {
                year = Integer.parseInt(split[0]);
                month = Integer.parseInt(split[1]);
                day = Integer.parseInt(split[2]);
            }catch (Exception e){
                e.printStackTrace();
                year = 2015;
                month = 0;
                day = 1;
            }
        }
        //
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/"
                        + (month + 1) + "/" + year;
                v.setText(date);
            }
        }, year, month, day);
        //
        datePickerDialog.show();
    }

    private void setTimeValue(final TextView v) {
        int hour;
        int minute;
        String value = v.getText().toString();
        if(value == null
        || value.isEmpty()) {
            Calendar calendario = Calendar.getInstance();

            hour = calendario.get(Calendar.HOUR_OF_DAY);
            minute = calendario.get(Calendar.MINUTE);
        }else{
            String[] split = value.split(":");
            try {
                hour = Integer.parseInt(split[0]);
                minute = Integer.parseInt(split[1]);
            }catch (Exception e){
                e.printStackTrace();
                hour = 0;
                minute = 0;

            }
        }

        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String sMinute = String.valueOf(minute);
                if(minute == 0){
                    sMinute = "00";
                }
                String time = hourOfDay + ":" + sMinute;
                v.setText(time);
            }
        }, hour, minute, true).show();
    }

    //region network methods

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (wsProcess.equals(WS_TK_Main_User_List.class.getName())) {
            mPresenter.setMainUserList(mLink);
        }
        //
        progressDialog.dismiss();
    }


    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        disableProgressDialog();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        if (wsProcess.equals(WS_TK_Main_User_List.class.getName())) {
            if ("INVALID_SCN".equals(mLink)) {
                handleReadOnly(true);
            }
        }
        //
        disableProgressDialog();

    }

    //TRATA MSG SESSION NOT FOUND
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

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }

    //Metodo chamado ao finalizar o download da atualização.
    @Override
    protected void processCloseAPP(String mLink, String mRequired) {
        super.processCloseAPP(mLink, mRequired);
        //
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
        bundle.putString(Constant.WS_LOGOUT_USER_CODE, String.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        ToolBox_Con.cleanPreferences(context);

        finish();
    }
    //end region


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

    @Override
    public void callOrigin() {
        Intent intent = ToolBox_Inf.getOriginIntent(context, mTk_ticket.getOrigin_type());
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            requestingBundle.putInt(TK_TicketDao.TICKET_PREFIX, mTkPrefix);
            requestingBundle.putInt(TK_TicketDao.TICKET_CODE, mTkCode);
            requestingBundle.putBoolean(Act082_Main.FROM_EDIT_HEADER, true);
            intent.putExtras(requestingBundle);
            startActivity(intent);
            finish();
        }
    }
    //endregion

}