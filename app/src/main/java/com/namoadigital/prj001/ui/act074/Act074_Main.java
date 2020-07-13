package com.namoadigital.prj001.ui.act074;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act074_Next_Tickets_Adapter;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act069.Act069_Main.FILTER_PARTNER_EMPTY;
import static com.namoadigital.prj001.ui.act069.Act069_Main.FILTER_PARTNER_NO_PROFILE;
import static com.namoadigital.prj001.ui.act069.Act069_Main.FILTER_PARTNER_PROFILE;
import static com.namoadigital.prj001.ui.act069.Act069_Main.FILTER_TEXT;

public class Act074_Main extends Base_Activity implements Act074_Main_Contract{
    private MKEditTextNM mketFilter;
    private ImageView ivFilters;
    private RecyclerView rvTickets;
    private TextView tvNoResult;
    Act074_Next_Tickets_Adapter mAdapter;
    private boolean bStatusPending;
    private boolean bStatusProcess;
    private boolean bStatusWaitingSync;
    private boolean bStatusDone;
    private boolean bParterEmpty;
    private boolean bParterProfile;
    private boolean bParterNoProfile;
    private String requestingAct;
    private String wsProcess;
    private long ticketProductCode = -1;
    private long ticketSerialCode = -1;
    //Novos Filtros do historico
    private boolean bStatusNotExecuted;
    private boolean bStatusIgnored;
    private boolean bStatusCanceled;
    private boolean bStatusRejected;
    private Act074_Main_Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act074_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();

        initAction();

    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT074
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act074_title");
        transList.add("filter_hint");
        transList.add("btn_sync_tickets");
        transList.add("no_record_lbl");
        transList.add("alert_error_on_generate_list_ttl");
        transList.add("alert_error_on_generate_list_msg");
        transList.add("dialog_filter_title");
        transList.add("dialog_status_lbl");
        transList.add("dialog_partner_lbl");
        transList.add("chk_allow_no_partner_lbl");
        transList.add("chk_my_partner_lbl");
        transList.add("chk_partner_no_profile_lbl");
        //
        transList.add("alert_ticket_to_send_ttl");
        transList.add("alert_ticket_to_send_msg");
        transList.add("dialog_download_ticket_ttl");
        transList.add("dialog_download_ticket_start");
        //
        transList.add("dialog_schedule_warning_ttl");
        transList.add("dialog_schedule_warning_new_status_lbl");
        transList.add("dialog_schedule_warning_user_nick_lbl");
        transList.add("dialog_schedule_warning_error_msg_lbl");
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
        bindViews();
        //
        iniFilters();
        //
        recoverIntentsInfo();
        //
//        mPresenter = new Act069_Main_Presenter(
//                context,
//                this,
//                hmAux_Trans
//        );
        //
        updateIvFilterState();
        //
        mPresenter.getTicketList(
                requestingAct.equalsIgnoreCase(ConstantBaseApp.ACT014),
                bStatusPending,
                bStatusProcess,
                bStatusWaitingSync,
                bStatusDone,
                bParterEmpty,
                bParterProfile,
                ticketProductCode,
                ticketSerialCode,
                bStatusNotExecuted,
                bStatusIgnored,
                bStatusCanceled,
                bStatusRejected,
                bParterNoProfile
        );
        //
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            mketFilter.setText(bundle.getString(FILTER_TEXT,""));
            bStatusPending = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_PENDING,true);
            bStatusProcess = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_PROCESS,true);
            bStatusWaitingSync = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_WAITING_SYNC,true);
            bParterEmpty = bundle.getBoolean(FILTER_PARTNER_EMPTY,true);
            bParterProfile= bundle.getBoolean(FILTER_PARTNER_PROFILE,true);
            bParterNoProfile= bundle.getBoolean(FILTER_PARTNER_NO_PROFILE,false);
            requestingAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT068);
            //
            ticketProductCode = bundle.getLong(TK_TicketDao.CURRENT_PRODUCT_CODE, -1);
            ticketSerialCode = bundle.getLong(TK_TicketDao.CURRENT_SERIAL_CODE, -1);
            //Aplica inicialização pelo historico
            if(ConstantBaseApp.ACT014 .equalsIgnoreCase(requestingAct)){
                bStatusPending = false;
                bStatusProcess = false;
                bStatusWaitingSync = false;
                bParterEmpty = false;
                bParterProfile = false;
                bParterNoProfile = false;
                //
                //LUCHE - 31/03/2020
                bStatusDone = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_DONE,true);
                bStatusNotExecuted = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_NOT_EXECUTED,true);
                bStatusIgnored = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_IGNORED,false);
                bStatusCanceled = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_CANCELLED,false);
                bStatusRejected = bundle.getBoolean(ConstantBaseApp.SYS_STATUS_REJECTED,false);
            }
        }else{
            requestingAct = ConstantBaseApp.ACT068;
            iniFilters();
        }
    }

    private void bindViews() {
        mketFilter = findViewById(R.id.act074_mket_filter);
        ivFilters = findViewById(R.id.act074_iv_status_filter);
        rvTickets = findViewById(R.id.act074_rv_ticket_list);
        tvNoResult = findViewById(R.id.act074_tv_no_result);
        //
        setTranslation();
    }

    private void setTranslation() {
        mketFilter.setHint(hmAux_Trans.get("filter_hint"));
        tvNoResult.setText(hmAux_Trans.get("no_record_lbl"));
    }

    private void updateIvFilterState() {
        if(requestingAct.equals(ConstantBaseApp.ACT014)){
            //ivFilters.setVisibility(View.GONE);
            if (bStatusDone || bStatusNotExecuted || bStatusIgnored ||bStatusCanceled || bStatusRejected) {
                ivFilters.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
            } else {
                ivFilters.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
            }
        }else {
            if (bStatusPending || bStatusProcess || bStatusWaitingSync ||bParterEmpty || bParterProfile || bParterNoProfile) {
                ivFilters.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
            } else {
                ivFilters.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
            }
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT074;
        mAct_Title = Constant.ACT074 + "_" + "title";
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

    private void initAction() {
        mketFilter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }
            //
            @Override
            public void reportTextChange(String s, boolean b) {
                if(mAdapter != null){
                    mAdapter.getFilter().filter(mketFilter.getText().toString().trim());
                }
            }
        });
        //
        ivFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
        //
    }
    private void iniFilters() {
        bStatusPending = true;
        bStatusProcess = true;
        bStatusWaitingSync = true;
        bParterEmpty = true;
        bParterProfile = true;
        bParterNoProfile = false;
        bStatusDone = false;
        //LUCHE - 31/03/2020
        bStatusNotExecuted = false;
        bStatusIgnored = false;
        bStatusCanceled = false;
        bStatusRejected = false;
    }

    private void showFilterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = (View) LayoutInflater.from(context).inflate(R.layout.act069_filter_dialog,null);
        //
        TextView tvTitle = view.findViewById(R.id.act069_filter_dialog_tv_title);
        TextView tvStatusLbl = view.findViewById(R.id.act069_filter_dialog_tv_status_lbl);
        TextView tvPartnerLbl = view.findViewById(R.id.act069_filter_dialog_tv_partner_lbl);
        final CheckBox chkStatusPending = view.findViewById(R.id.act069_filter_dialog_chk_pending);
        final CheckBox chkStatusProcess = view.findViewById(R.id.act069_filter_dialog_chk_process);
        final CheckBox chkStatusWaitingSync = view.findViewById(R.id.act069_filter_dialog_chk_waiting_sync);
        final CheckBox chkPartnerEmpty = view.findViewById(R.id.act069_filter_dialog_chk_no_partner);
        final CheckBox chkPartnerProfile = view.findViewById(R.id.act069_filter_dialog_chk_profile_partner);
        final CheckBox chkPartnerNoProfile = view.findViewById(R.id.act069_filter_dialog_chk_profile_partner_others);
        final CheckBox chkStatusDone = view.findViewById(R.id.act069_filter_dialog_chk_done);
        final CheckBox chkStatusNotExecuted = view.findViewById(R.id.act069_filter_dialog_chk_not_exec);
        final CheckBox chkStatusIgnored = view.findViewById(R.id.act069_filter_dialog_chk_ignored);
        final CheckBox chkStatusCanceled = view.findViewById(R.id.act069_filter_dialog_chk_canceled);
        final CheckBox chkStatusRejected = view.findViewById(R.id.act069_filter_dialog_chk_rejected);
        Group gpPending = view.findViewById(R.id.act069_filter_dialog_gp_pending);
        Group gpHistoric = view.findViewById(R.id.act069_filter_dialog_gp_historic);
        //
        tvTitle.setText(hmAux_Trans.get("dialog_filter_title"));
        tvStatusLbl.setText(hmAux_Trans.get("dialog_status_lbl"));
        tvPartnerLbl.setText(hmAux_Trans.get("dialog_partner_lbl"));
        //
        chkStatusPending.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_PENDING));
        chkStatusPending.setChecked(bStatusPending);
        chkStatusPending.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(ConstantBaseApp.SYS_STATUS_PENDING))));
        chkStatusPending.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(ConstantBaseApp.SYS_STATUS_PENDING))));
        //
        chkStatusProcess.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_PROCESS));
        chkStatusProcess.setChecked(bStatusProcess);
        chkStatusProcess.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(ConstantBaseApp.SYS_STATUS_PROCESS))));
        chkStatusProcess.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(ConstantBaseApp.SYS_STATUS_PROCESS))));
        //Esse ultimo stats só existe no quando lista origem do pendentes.
        chkStatusWaitingSync.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_WAITING_SYNC));
        chkStatusWaitingSync.setChecked(bStatusWaitingSync);
        chkStatusWaitingSync.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(ConstantBaseApp.SYS_STATUS_WAITING_SYNC))));
        chkStatusWaitingSync.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(ConstantBaseApp.SYS_STATUS_WAITING_SYNC))));
        //
        chkPartnerEmpty.setText(hmAux_Trans.get("chk_allow_no_partner_lbl"));
        chkPartnerEmpty.setChecked(bParterEmpty);
        //
        chkPartnerProfile.setText(hmAux_Trans.get("chk_my_partner_lbl"));
        chkPartnerProfile.setChecked(bParterProfile);
        //
        chkPartnerNoProfile.setText(hmAux_Trans.get("chk_partner_no_profile_lbl"));
        chkPartnerNoProfile.setChecked(bParterNoProfile);
        //Dados do historico
        chkStatusDone.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_DONE));
        chkStatusDone.setChecked(bStatusDone);
        chkStatusDone.setButtonTintList(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_DONE)));
        chkStatusDone.setTextColor(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_DONE));
        //
        chkStatusNotExecuted.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_NOT_EXECUTED));
        chkStatusNotExecuted.setChecked(bStatusNotExecuted);
        chkStatusNotExecuted.setButtonTintList(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_NOT_EXECUTED)));
        chkStatusNotExecuted.setTextColor(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_NOT_EXECUTED));
        chkStatusNotExecuted.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_NOT_EXECUTED));
        //
        chkStatusIgnored.setChecked(bStatusIgnored);
        chkStatusIgnored.setButtonTintList(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_IGNORED)));
        chkStatusIgnored.setTextColor(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_IGNORED));
        chkStatusIgnored.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_IGNORED));
        //
        chkStatusCanceled.setChecked(bStatusCanceled);
        chkStatusCanceled.setButtonTintList(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_CANCELLED)));
        chkStatusCanceled.setTextColor(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_CANCELLED));
        chkStatusCanceled.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_CANCELLED));
        //
        chkStatusRejected.setChecked(bStatusRejected);
        chkStatusRejected.setButtonTintList(ColorStateList.valueOf(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_REJECTED)));
        chkStatusRejected.setTextColor(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_REJECTED));
        chkStatusRejected.setText(hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_REJECTED));
        //Seta quais filtros serão exibidos
        if(requestingAct.equalsIgnoreCase(ConstantBaseApp.ACT014)){
            gpPending.setVisibility(View.GONE);
            gpHistoric.setVisibility(View.VISIBLE);
        }else{
            gpPending.setVisibility(View.VISIBLE);
            gpHistoric.setVisibility(View.GONE);
        }
        //
        builder
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(
                        hmAux_Trans.get("sys_alert_btn_ok"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bStatusPending = chkStatusPending.isChecked();
                                bStatusProcess = chkStatusProcess.isChecked();
                                bStatusWaitingSync = chkStatusWaitingSync.isChecked();
                                bParterEmpty = chkPartnerEmpty.isChecked();
                                bParterProfile = chkPartnerProfile.isChecked();
                                bParterNoProfile = chkPartnerNoProfile.isChecked();
                                //historico
                                bStatusDone = chkStatusDone.isChecked();
                                bStatusNotExecuted = chkStatusNotExecuted.isChecked();
                                bStatusIgnored = chkStatusIgnored.isChecked();
                                bStatusCanceled = chkStatusCanceled.isChecked();
                                bStatusRejected = chkStatusRejected.isChecked();
                                //
                                updateIvFilterState();
                                //
                                mPresenter.getTicketList(
                                        requestingAct.equalsIgnoreCase(ConstantBaseApp.ACT014), bStatusPending,
                                        bStatusProcess,
                                        bStatusWaitingSync,
                                        bStatusDone,
                                        bParterEmpty,
                                        bParterProfile,
                                        ticketProductCode,
                                        ticketSerialCode,
                                        bStatusNotExecuted,
                                        bStatusIgnored,
                                        bStatusCanceled,
                                        bStatusRejected,
                                        bParterNoProfile);
                            }
                        }
                )
                .setNegativeButton(
                        hmAux_Trans.get("sys_alert_btn_cancel"),
                        null
                );
        //
        builder.show();
    }

}