package com.namoadigital.prj001.ui.act082;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.Act082_Form_Data;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_TK_Header_N_Group_Save;
import com.namoadigital.prj001.service.WS_TK_Main_User_List;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
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

    private TextView tv_start_date_lbl;
    private TextView tv_start_time_lbl;
    private MkDateTime mkdt_start_date_val;
    private CheckBox chk_shift_ticket_start_date;
    private CheckBox chk_shift_step_start_date;
    private TextView tv_end_date_lbl;
    private TextView tv_end_time_lbl;
    private MkDateTime mkdt_end_date_val;
    private CheckBox chk_shift_ticket_end_date;
    private CheckBox chk_shift_step_end_date;
    private TextView tv_service_time_day_lbl;
    private TextView tv_service_time_hour_lbl;
    private TextView tv_service_time_minutes_lbl;
    private EditText edt_service_time_day_val;
    private EditText edt_service_time_hour_val;
    private EditText edt_service_time_minutes_val;
    private CheckBox chk_shift_step_service_time;
    private TextView tv_start_date;
    private TextView tv_end_date;
    private TextView tv_service_time;

    private TextView tv_elapsed_time_lbl;
    private TextView tv_elapsed_time_val;
    private TextView tv_remaining_time_lbl;
    private TextView tv_remaining_time_val;

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
    private Act082_Form_Data headerEditDataObj;
    private TextView tv_main_user_no_profile_lbl;
    private TextView tv_main_user_no_profile_val;
    private TextView tv_internal_comments_val;

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
        //
        applyFormDataToRadio();
        //
        updateBtnSaveState();
        //
        deleteAndResetFormDataFile();
    }

    private void updateBtnSaveState() {
        btn_save_header_form.setEnabled(hasAnyFieldValueChange());
    }

    private void bindViews() {
        ll_header_infos = findViewById(R.id.act082_ll_header_infos);
        ll_edit_buttons = findViewById(R.id.act082_ll_edit_buttons);
        ss_main_user = findViewById(R.id.act082_ss_main_user);
        //
        tv_main_user_no_profile_lbl = findViewById(R.id.act082_tv_main_user_lbl);
        tv_main_user_no_profile_val = findViewById(R.id.act082_tv_main_user_val);
        //
        tv_internal_comments_lbl = findViewById(R.id.act082_tv_internal_comments_lbl);
        mket_internal_comments = findViewById(R.id.act082_mket_internal_comments_val);
        tv_internal_comments_val = findViewById(R.id.act082_tv_internal_comments_val);
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
        //
        tv_elapsed_time_lbl = findViewById(R.id.act082_tv_elapsed_time_lbl);
        tv_elapsed_time_val = findViewById(R.id.act082_tv_elapsed_time_val);
        tv_remaining_time_lbl = findViewById(R.id.act082_tv_remaining_time_lbl);
        tv_remaining_time_val = findViewById(R.id.act082_tv_remaining_time_val);
    }


    private void initVars() {
        mPresenter = new Act082_Main_Presenter(context, this, hmAux_Trans);
        //
        recoverIntentBundle();
        //
        setLabels();
        //
        refreshUI();
        //
        ss_main_user.setmOption(mPresenter.getSSMainUserList(mTk_ticket));
        ss_main_user.setmShowLabel(false);
        tv_main_user_no_profile_val.setText(mPresenter.getSSMainUserCurrentDesc(ss_main_user.getmValue()));
        //
        mket_internal_comments.setmBARCODE(false);
        //
        handleReadOnly(false);
        //
        if(!mTk_ticket.isReadOnly(context)) {
            if (mPresenter.getDateEditionProfile() || mPresenter.getHeaderEditionProfile()) {
                if (mPresenter.hasAnyOnlinePendency(context, mTk_ticket)) {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_ticket_has_pendency_ttl"),
                            hmAux_Trans.get("alert_ticket_has_pendency_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    handleReadOnly(true);
                                }
                            },
                            0
                    );
                }
            }
        }
        //
    }

    @Override
    public void handleReadOnly(boolean forceReadOnly) {
        if (forceReadOnly
        || mTk_ticket.isReadOnly(context)) {
            setDateReadOnly();
            setHeaderReadOnly();
            ll_edit_buttons.setVisibility(View.GONE);
        } else {
            if (!mPresenter.getDateEditionProfile()) {
                setDateReadOnly();
            }
            if (!mPresenter.getHeaderEditionProfile()) {
                setHeaderReadOnly();
            }
            if (!mPresenter.getHeaderEditionProfile() && !mPresenter.getDateEditionProfile()) {
                ll_edit_buttons.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setMainUserSSList(ArrayList<HMAux> hmAuxMainUser) {
        ss_main_user.setmOption(hmAuxMainUser);
    }

    private void setHeaderReadOnly() {
        mket_internal_comments.setEnabled(false);
        ss_main_user.setmEnabled(false);
        ss_main_user.setmCanClean(false);
        //LUCHE - 24/06/2021 - Substituido o ss e mket pelo textView quando sem param edit_header
        ss_main_user.setVisibility(View.GONE);
        tv_main_user_no_profile_val.setVisibility(View.VISIBLE);
        mket_internal_comments.setVisibility(View.GONE);
        tv_internal_comments_val.setVisibility(View.VISIBLE);
    }
    private void setDateReadOnly() {
        ColorStateList buttonTintColorList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.padrao_TRANSPARENT));
        rb_start_date.setEnabled(false);
        rb_end_date.setEnabled(false);
        rb_time.setEnabled(false);
        rg_date_and_forecast_infos.setEnabled(false);
        //LUCHE - 24/06/2021 - "REMOVIDO" radio button quando desabilitado
        rb_start_date.setButtonTintList(buttonTintColorList);
        rb_end_date.setButtonTintList(buttonTintColorList);
        rb_time.setButtonTintList(buttonTintColorList);
    }

    private void setVisibilityByProfile() {
        //
        ll_date_and_forecast_infos.setVisibility(View.VISIBLE);
        ll_header_infos.setVisibility(View.VISIBLE);
        //
        if (mPresenter.getDateEditionProfile() || mPresenter.getHeaderEditionProfile()) {
            if (mPresenter.getDateEditionProfile()) {
                if (mPresenter.getStepEditTimeProfile()) {
                    chk_shift_step_start_date.setVisibility(View.VISIBLE);
                    chk_shift_step_end_date.setVisibility(View.VISIBLE);
                } else {
                    chk_shift_step_start_date.setVisibility(View.GONE);
                    chk_shift_step_end_date.setVisibility(View.GONE);
                }
            }
        } else {
            ll_edit_buttons.setVisibility(View.GONE);
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
        tv_main_user_no_profile_lbl.setText(hmAux_Trans.get("main_user_lbl"));
        //
        tv_start_date_lbl.setText(hmAux_Trans.get("start_date_lbl"));
        tv_start_time_lbl.setText(hmAux_Trans.get("start_time_lbl"));
        chk_shift_ticket_start_date.setText(hmAux_Trans.get("change_end_date_opt"));
        chk_shift_step_start_date.setText(hmAux_Trans.get("change_step_deadline_opt"));
        tv_end_date_lbl.setText(hmAux_Trans.get("end_date_lbl"));
        tv_end_time_lbl.setText(hmAux_Trans.get("end_time_lbl"));
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
        mkdt_start_date_val.setmCanClean(false);
        mkdt_end_date_val.setmLabel("");
        mkdt_end_date_val.setmCanClean(false);
        //
        tv_elapsed_time_lbl.setText(hmAux_Trans.get("elapsed_time_lbl"));
        tv_remaining_time_lbl.setText(hmAux_Trans.get("remaining_time_lbl"));
        //
    }

    private void recoverIntentBundle() {
        requestingBundle = getIntent().getExtras();
        //
        if (requestingBundle != null) {
            mTkPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
        } else {
            mTkPrefix = -1;
            mTkCode = -1;
        }
    }

    @Override
    public void onBackPressed() {
        if (hasAnyFieldValueChange()) {
            ToolBox.alertMSG(context,
                    hmAux_Trans.get("exit_without_save_ttl"),
                    hmAux_Trans.get("exit_without_save_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callAct070();
                        }
                    },
                    1
            );
        } else {
            callAct070();
        }
    }

    private boolean hasAnyFieldValueChange() {
        boolean startDateHasChanged = mkdt_start_date_val.hasChanged() && rb_start_date.isChecked();
        boolean endDateHasChanged = mkdt_end_date_val.hasChanged() && rb_end_date.isChecked();
        boolean forecastTimeHasChanged = hasForecastTimeChanged();
        boolean mainUserHasChanged = ss_main_user.hasChangedBD();
        boolean internalCommentsHasChanged = !mket_internal_comments.getText().toString().equals(mket_internal_comments.getTag());

        return endDateHasChanged
                || startDateHasChanged
                || forecastTimeHasChanged
                || mainUserHasChanged
                || internalCommentsHasChanged;
    }

    private boolean hasForecastTimeChanged() {
        String forecast_day = edt_service_time_day_val.getText().toString();
        String forecast_hour = edt_service_time_hour_val.getText().toString();
        String forecast_minutes = edt_service_time_minutes_val.getText().toString();
        //
        if (forecast_day == null || forecast_day.isEmpty() || "0".equals(forecast_day)) {
            forecast_day = "";
        } else {
            forecast_day = forecast_day + " ";
        }
        //
        if (forecast_hour == null || forecast_hour.isEmpty()) {
            forecast_hour = "00";
        }
        //
        if (forecast_minutes == null || forecast_minutes.isEmpty()) {
            forecast_minutes = "00";
        }
        String mForecastTime = forecast_day + forecast_hour + ":" + forecast_minutes;

        return !mForecastTime.equals(mTk_ticket.getForecast_time()) && rb_time.isChecked();
    }


    public void callAct070() {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.putBoolean(Act082_Main.FROM_EDIT_HEADER, false);
        intent.putExtras(requestingBundle);
        mPresenter.deleteMainUserListFile();
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
    public void showMsg(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
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
        transList.add("change_end_date_opt");
        transList.add("change_step_deadline_opt");
        transList.add("change_start_date_opt");
        transList.add("days_lbl");
        transList.add("hour_lbl");
        transList.add("start_date_lbl");
        transList.add("start_time_lbl");
        transList.add("end_date_lbl");
        transList.add("end_time_lbl");
        transList.add("minutes_lbl");
        transList.add("recalculate_step_duration");
        transList.add("btn_save_lbl");
        //
        transList.add("dialog_main_user_search_ttl");
        transList.add("dialog_main_user_search_start");
        transList.add("dialog_edit_header_date_ttl");
        transList.add("dialog_edit_header_date_start");
        //
        transList.add("alert_no_fields_changes_ttl");
        transList.add("alert_no_fields_changes_msg");
        //
        transList.add("alert_ticket_has_pendency_ttl");
        transList.add("alert_ticket_has_pendency_msg");
        //
        transList.add("exit_without_save_ttl");
        transList.add("exit_without_save_msg");
        //
        transList.add("elapsed_time_lbl");
        transList.add("remaining_time_lbl");
        //
        transList.add("alert_invalid_forecast_time_ttl");
        transList.add("alert_invalid_forecast_time_msg");
        //
        transList.add("alert_invalid_date_range_ttl");
        transList.add("alert_invalid_date_range_msg");
        //
        transList.add("alert_ticket_results_ok");
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
                String timeAction = "";
                String forecast_time = null;
                String start_date = null;
                String forecast_date = null;
                String internalComments = null;
                int move_other_date = 0;
                int move_steps = 0;
                boolean hasForecastError = false;
                boolean hasDateError = false;
                //
                String mInternalComments = mket_internal_comments.getText() == null ? "" : mket_internal_comments.getText().toString();
                String tkInternalComments = mTk_ticket.getInternal_comments() == null ? "" : mTk_ticket.getInternal_comments();
                if (!mInternalComments.equals(tkInternalComments)) {
                    header_data_has_changed = true;
                    internalComments = mInternalComments;
                }
                Integer mainUserValue = -1;
                if (ss_main_user.hasChangedBD()) {
                    header_data_has_changed = true;
                    if (ss_main_user.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                        mainUserValue = Integer.valueOf(ss_main_user.getmValue().get(SearchableSpinner.CODE));
                    } else {
                        mainUserValue = 0;
                    }
                }
                //
                if (rb_start_date.isChecked()) {
                    if (header_data_has_changed) {
                        timeAction = ConstantBaseApp.TK_TICKET_START_DATE_AND_HEADER;
                    } else {
                        timeAction = ConstantBaseApp.TK_TICKET_START_DATE;
                    }
                    start_date = mkdt_start_date_val.getmValue();
                    move_other_date = chk_shift_ticket_start_date.isChecked() ? 1 : 0;
                    move_steps = chk_shift_step_start_date.isChecked() ? 1 : 0;
                    if(move_other_date == 0){
                        hasDateError = ToolBox_Inf.dateToMilliseconds(start_date) > ToolBox_Inf.dateToMilliseconds(mTk_ticket.getForecast_date());
                    }
                } else if (rb_end_date.isChecked()) {
                    if (header_data_has_changed) {
                        timeAction = ConstantBaseApp.TK_TICKET_FORECAST_DATE_AND_HEADER;
                    } else {
                        timeAction = ConstantBaseApp.TK_TICKET_FORECAST_DATE;
                    }
                    move_other_date = chk_shift_ticket_end_date.isChecked() ? 1 : 0;
                    move_steps = chk_shift_step_end_date.isChecked() ? 1 : 0;
                    forecast_date = mkdt_end_date_val.getmValue();
                    if(move_other_date == 0){
                        hasDateError = ToolBox_Inf.dateToMilliseconds(forecast_date) < ToolBox_Inf.dateToMilliseconds(mTk_ticket.getStart_date());
                    }
                } else if (rb_time.isChecked()) {
                    if (header_data_has_changed) {
                        timeAction = ConstantBaseApp.TK_TICKET_FORECAST_TIME_AND_HEADER;
                    } else {
                        timeAction = ConstantBaseApp.TK_TICKET_FORECAST_TIME;
                    }
                    move_steps = chk_shift_step_service_time.isChecked() ? 1 : 0;
                    //
                    String forecast_day = edt_service_time_day_val.getText().toString();
                    String forecast_hour = edt_service_time_hour_val.getText().toString();
                    String forecast_minutes = edt_service_time_minutes_val.getText().toString();

                    if (forecast_day == null || forecast_day.isEmpty()) {
                        forecast_day = "00";
                        edt_service_time_hour_val.setText(forecast_day);
                    }
                    //
                    if (forecast_hour == null || forecast_hour.isEmpty()) {
                        forecast_hour = "00";
                        edt_service_time_hour_val.setText(forecast_hour);
                    } else {
                        int hour = Integer.valueOf(forecast_hour);
                        if (hour > 23) {
                            hasForecastError = true;
                            forecast_hour = "00";
                        }
                    }
                    //
                    if (forecast_minutes == null || forecast_minutes.isEmpty()) {
                        forecast_minutes = "00";
                        edt_service_time_minutes_val.setText(forecast_minutes);
                    } else {
                        int minutes = Integer.valueOf(forecast_minutes);
                        if (minutes > 59) {
                            hasForecastError = true;
                            forecast_hour = "00";
                        }
                    }
                    //
                    forecast_time = mPresenter.getTimeFromForm(forecast_day, forecast_hour, forecast_minutes);
                } else {
                    if (header_data_has_changed) {
                        timeAction = ConstantBaseApp.TK_TICKET_EDIT_HEADER;
                    }
                }
                //
                if (!hasForecastError) {
                    if (!hasDateError) {
                        if (hasFieldValueChange(forecast_time, start_date, forecast_date)) {
                            retrieveKeyboard();
                            restoreRadioBtnAfterSave();
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
                        } else {
                            showMsg(
                                    hmAux_Trans.get("alert_no_fields_changes_ttl"),
                                    hmAux_Trans.get("alert_no_fields_changes_msg")
                            );
                        }
                    } else {
                        showMsg(
                                hmAux_Trans.get("alert_invalid_date_range_ttl"),
                                hmAux_Trans.get("alert_invalid_date_range_msg")
                        );
                    }
                } else {
                    showMsg(
                            hmAux_Trans.get("alert_invalid_forecast_time_ttl"),
                            hmAux_Trans.get("alert_invalid_forecast_time_msg")
                    );
                }
            }
        });
        //
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
                retrieveKeyboard();
                if (rb_start_date_is_checked) {
                    rb_start_date_is_checked = false;
                    rg_date_and_forecast_infos.clearCheck();
                } else {
                    btn_save_header_form.setEnabled(true);
                    rb_start_date_is_checked = true;
                    rb_time_is_checked = false;
                    rb_end_date_is_checked = false;
                }
                //
                if (rb_start_date_is_checked) {
                    v_start_date_form.setVisibility(View.VISIBLE);
//                    String start_date = tv_start_date.getText().toString();
                    mkdt_start_date_val.setmValue(headerEditDataObj.getStart_date());
                    mkdt_start_date_val.setmValueDb(mTk_ticket.getStart_date());
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
                retrieveKeyboard();
                if (rb_end_date_is_checked) {
                    rb_end_date_is_checked = false;
                    rg_date_and_forecast_infos.clearCheck();
                } else {
                    btn_save_header_form.setEnabled(true);
                    rb_start_date_is_checked = false;
                    rb_end_date_is_checked = true;
                    rb_time_is_checked = false;
                }
                //
                if (rb_end_date_is_checked) {
//                    String end_date = tv_end_date.getText().toString();
                    mkdt_end_date_val.setmValue(headerEditDataObj.getEnd_date());
                    mkdt_end_date_val.setmValueDb(mTk_ticket.getForecast_date());
                    //
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
                retrieveKeyboard();
                if (rb_time_is_checked) {
                    rb_time_is_checked = false;
                    rg_date_and_forecast_infos.clearCheck();
                } else {
                    btn_save_header_form.setEnabled(true);
                    rb_time_is_checked = true;
                    rb_end_date_is_checked = false;
                    rb_start_date_is_checked = false;
                }
                //
                if (rb_time_is_checked) {
                    if (headerEditDataObj.getForecast_time() != null) {
                        //String service_time = tv_service_time.getText().toString();
                        String service_time = headerEditDataObj.getForecast_time();
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
                    } else {
                        edt_service_time_day_val.setText("");
                        edt_service_time_hour_val.setText("");
                        edt_service_time_minutes_val.setText("");
                    }
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
        //
        mkdt_start_date_val.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String start_date = mkdt_start_date_val.getmValue();
                if (!hasFocus
                        && !start_date.equals(mTk_ticket.getStart_date())) {
                    btn_save_header_form.setEnabled(true);
                }
            }
        });
        //
        mkdt_end_date_val.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String end_date = mkdt_end_date_val.getmValue();
                if (!hasFocus
                        && !end_date.equals(mTk_ticket.getForecast_date())) {
                    btn_save_header_form.setEnabled(true);
                }
            }
        });
        //
        mket_internal_comments.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
                if (!mket_internal_comments.getTag().equals(s)) {
                    btn_save_header_form.setEnabled(true);
                }
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (!mket_internal_comments.getTag().equals(s)) {
                    btn_save_header_form.setEnabled(true);
                }

            }
        });
        //
        ss_main_user.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (ss_main_user.hasChangedBD()) {
                    btn_save_header_form.setEnabled(true);
                } else {
                    btn_save_header_form.setEnabled(false);
                }
            }
        });
    }

    /**
     * BARRIONUEVO   18-12-2020
     * Metodo que restaura os radio buttons pos save
     */
    private void restoreRadioBtnAfterSave() {
        if (rb_start_date.isChecked()) {
            rb_start_date.performClick();
        }
        //
        if (rb_end_date.isChecked()) {
            rb_end_date.performClick();
        }
        //
        if (rb_time.isChecked()) {
            rb_time.performClick();
        }
        //
    }

    private boolean hasFieldValueChange(String forecast_time, String start_date, String forecast_date) {
        return header_data_has_changed
                || (rb_start_date.isChecked() && start_date != null && !start_date.isEmpty() && mkdt_start_date_val.hasChanged())
                || (rb_end_date.isChecked() && forecast_date != null && !forecast_date.isEmpty() && mkdt_end_date_val.hasChanged())
                || (rb_time.isChecked() && forecast_time != null && !forecast_time.isEmpty() && !forecast_time.equals(mTk_ticket.getForecast_time()));
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
            mPresenter.processMainUserList();
        } else if (wsProcess.equals(WS_TK_Header_N_Group_Save.class.getName())) {
            header_data_has_changed = false;
            refreshUI();
            Toast.makeText(context, hmAux_Trans.get("alert_ticket_results_ok"), Toast.LENGTH_SHORT).show();
        }
        //
        progressDialog.dismiss();
    }

    private void refreshUI() {
        mTk_ticket = mPresenter.getTicketData(mTkPrefix, mTkCode);
        headerEditDataObj = mPresenter.getFormDataJsonInfo(mTk_ticket);
        //
        setHeaderFragment(mTk_ticket);
        //
        if (headerEditDataObj.getInternal_comments() == null) {
            mket_internal_comments.setText("");
            mket_internal_comments.setTag("");
            tv_internal_comments_val.setText("");
        } else {
            mket_internal_comments.setText(headerEditDataObj.getInternal_comments());
            mket_internal_comments.setTag(mTk_ticket.getInternal_comments());
            tv_internal_comments_val.setText(headerEditDataObj.getInternal_comments());
        }
        //
        tv_start_date.setText(ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(mTk_ticket.getStart_date()),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        ));
        //
        if (mTk_ticket.getForecast_date() != null) {
            tv_end_date.setText(ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(mTk_ticket.getForecast_date()),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            ));
        } else {
            tv_end_date.setText(" - ");
        }
        //
        if (mTk_ticket.getForecast_time() == null) {
            tv_service_time.setText(" - ");
        } else {
            tv_service_time.setText(mTk_ticket.getForecast_time());
        }
        //
        if (mTk_ticket.getMain_user() != null && mTk_ticket.getMain_user() > 0) {
            HMAux hmAuxMainUser = new HMAux();
            //
            hmAuxMainUser.put(SearchableSpinner.CODE, String.valueOf(mTk_ticket.getMain_user()));
            hmAuxMainUser.put(SearchableSpinner.ID, mTk_ticket.getMain_user_nick());
            hmAuxMainUser.put(SearchableSpinner.DESCRIPTION, mTk_ticket.getMain_user_name());
            ss_main_user.setmValueBD(hmAuxMainUser);
        }else{
            ss_main_user.setmValueBD(new HMAux());
        }
        //
        if (headerEditDataObj.getMain_user_code() != null && ToolBox_Inf.convertStringToInt(headerEditDataObj.getMain_user_code()) > 0) {
            //ORIGINAL OU  VINDO DO JSON
            HMAux hmAuxMainUserFormData = new HMAux();
            hmAuxMainUserFormData.put(SearchableSpinner.CODE, headerEditDataObj.getMain_user_code());
            hmAuxMainUserFormData.put(SearchableSpinner.ID, headerEditDataObj.getMain_user_id());
            hmAuxMainUserFormData.put(SearchableSpinner.DESCRIPTION, headerEditDataObj.getMain_user_desc());
            ss_main_user.setmValue(hmAuxMainUserFormData);
        }
        //
        setVisibilityByProfile();
        //
        btn_save_header_form.setEnabled(false);
        rb_start_date.setChecked(false);
        rb_end_date.setChecked(false);
        rb_time.setChecked(false);
        //
        v_start_date_form.setVisibility(View.GONE);
        tv_start_date.setVisibility(View.VISIBLE);
        v_end_date_form.setVisibility(View.GONE);
        tv_end_date.setVisibility(View.VISIBLE);
        v_time_form.setVisibility(View.GONE);
        tv_service_time.setVisibility(View.VISIBLE);
        Long elapsedTime = mPresenter.getElapsedTime(mTk_ticket);
        if (elapsedTime < 0) {
            tv_elapsed_time_val.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
        } else {
            tv_elapsed_time_val.setTextColor(context.getResources().getColor(R.color.namoa_light_blue));
        }
        tv_elapsed_time_val.setText(mPresenter.getFormattedDate(elapsedTime));
        Long remainingTime = mPresenter.getRemainingTime(mTk_ticket);
        if (remainingTime != null) {
            if (remainingTime < 0) {
                tv_remaining_time_val.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
            } else {
                tv_remaining_time_val.setTextColor(context.getResources().getColor(R.color.namoa_light_blue));
            }
            tv_remaining_time_val.setText(mPresenter.getFormattedDate(remainingTime));
        } else {
            tv_remaining_time_lbl.setVisibility(View.GONE);
            tv_remaining_time_val.setVisibility(View.GONE);
        }
        //
        retrieveKeyboard();
    }

    private void applyFormDataToRadio() {
        if (headerEditDataObj.getRb_value() != null && !headerEditDataObj.getRb_value().isEmpty()) {
            switch (headerEditDataObj.getRb_value()) {
                case ConstantBaseApp.TK_TICKET_START_DATE:
                    rb_start_date.performClick();
                    chk_shift_ticket_start_date.setChecked(headerEditDataObj.isChk_shift_ticket_start_date());
                    chk_shift_step_start_date.setChecked(headerEditDataObj.isChk_shift_step_start_date());
                    break;
                case ConstantBaseApp.TK_TICKET_FORECAST_DATE:
                    rb_end_date.performClick();
                    chk_shift_ticket_end_date.setChecked(headerEditDataObj.isChk_shift_ticket_end_date());
                    chk_shift_step_end_date.setChecked(headerEditDataObj.isChk_shift_step_end_date());
                    break;
                case ConstantBaseApp.TK_TICKET_FORECAST_TIME:
                    rb_time.performClick();
                    chk_shift_step_service_time.setChecked(headerEditDataObj.isChk_shift_step_service_time());
                    break;
            }
        }
    }

    private void deleteAndResetFormDataFile() {
        mPresenter.deleteHeaderEditionFile();
        headerEditDataObj = mPresenter.getFormDataJsonInfo(mTk_ticket);
    }


    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        handleReadOnly(true);
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
            boolean isFileCreated = mPresenter.checkForHeaderEditFileCreation(
                    hasAnyFieldValueChange(),
                    ss_main_user.getmValue(),
                    mket_internal_comments.getText().toString(),
                    rb_start_date.isChecked(),
                    rb_end_date.isChecked(),
                    rb_time.isChecked(),
                    rb_start_date.isChecked() && mkdt_start_date_val.getmValue() != null ? mkdt_start_date_val.getmValue() : mTk_ticket.getStart_date(),
                    rb_end_date.isChecked() && mkdt_end_date_val.getmValue() != null ? mkdt_end_date_val.getmValue() : mTk_ticket.getForecast_date(),
                    getForecastTimeParam(),
                    chk_shift_ticket_start_date.isChecked(),
                    chk_shift_step_start_date.isChecked(),
                    chk_shift_ticket_end_date.isChecked(),
                    chk_shift_step_end_date.isChecked(),
                    chk_shift_step_service_time.isChecked()
            );
            //
            if (hasAnyFieldValueChange() && !isFileCreated) {
                showMsg(
                        hmAux_Trans.get("alert_error_on_create_wg_changes_file_ttl"),
                        hmAux_Trans.get("alert_error_on_create_wg_changes_file_msg")
                );
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                requestingBundle.putInt(TK_TicketDao.TICKET_PREFIX, mTkPrefix);
                requestingBundle.putInt(TK_TicketDao.TICKET_CODE, mTkCode);
                requestingBundle.putBoolean(Act082_Main.FROM_EDIT_HEADER, true);
                intent.putExtras(requestingBundle);
                startActivity(intent);
                finish();
            }
        }
    }

    private String getForecastTimeParam() {
        return rb_time.isChecked()
                ? mPresenter.getTimeFromForm(edt_service_time_day_val.getText().toString(), edt_service_time_hour_val.getText().toString(), edt_service_time_minutes_val.getText().toString())
                : mTk_ticket.getForecast_time();
    }
    //endregion

    void retrieveKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ss_main_user.getWindowToken(), 0);
    }

}