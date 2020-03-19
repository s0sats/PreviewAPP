package com.namoadigital.prj001.ui.act069;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act069_Tickets_Adapter;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.VH_models.Act069_TicketVH;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.ui.act068.Act068_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.ui.act071.Act071_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act069_Main extends Base_Activity implements Act069_Main_Contract.I_View {

    public static final String FILTER_TEXT = "FILTER_TEXT";
    public static final String FILTER_PARTNER_EMPTY = "FILTER_PARTNER_EMPTY";
    public static final String FILTER_PARTNER_PROFILE = "FILTER_PARTNER_PROFILE";

    private MKEditTextNM mketFilter;
    private ImageView ivFilters;
    private RecyclerView rvTickets;
    private TextView tvNoResult;
    private Button btnSyncTickets;
    private Act069_Main_Presenter mPresenter;
    private Act069_Tickets_Adapter mAdapter;
    private boolean bStatusPending;
    private boolean bStatusProcess;
    private boolean bStatusWaitingSync;
    private boolean bStatusDone;
    private boolean bParterEmpty;
    private boolean bParterProfile;
    private String requestingAct;
    private String wsProcess;
    //
    private long ticketProductCode = -1;
    private long ticketSerialCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act069_main);

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
            Constant.ACT069
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act069_title");
        transList.add("filter_hint");
        transList.add("btn_sync_tickets");
        transList.add("no_record_lbl");
        transList.add("alert_error_on_generate_list_ttl");
        transList.add("alert_error_on_generate_list_msg");
        transList.add("dialog_filter_title");
        transList.add("dialog_status_lbl");
        transList.add("dialog_partner_lbl");
        transList.add("chk_allow_no_partner_lbl");
        transList.add("chk_hide_other_partner_lbl");
        //
        transList.add("alert_ticket_to_send_ttl");
        transList.add("alert_ticket_to_send_msg");
        transList.add("dialog_download_ticket_ttl");
        transList.add("dialog_download_ticket_start");
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
        mPresenter = new Act069_Main_Presenter(
            context,
            this,
            hmAux_Trans
        );
        //
        updateIvFilterState();
        //
        mPresenter.getTicketList(
            bStatusPending,
            bStatusProcess,
            bStatusWaitingSync,
            bStatusDone,
            bParterEmpty,
            bParterProfile,
            ticketProductCode,
            ticketSerialCode
        );
        //
        setBtnSyncVisibility();
    }

    private void setBtnSyncVisibility() {
        int qtyToSync = mPresenter.checkTicketToSync();
        btnSyncTickets.setVisibility( qtyToSync > 0 ? View.VISIBLE : View.GONE);
        btnSyncTickets.setText(mPresenter.getBtnSyncText(qtyToSync));
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
            requestingAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT068);
            //
            ticketProductCode = bundle.getLong(TK_TicketDao.CURRENT_PRODUCT_CODE, -1);
            ticketSerialCode = bundle.getLong(TK_TicketDao.CURRENT_SERIAL_CODE, -1);
            //
            if(ConstantBaseApp.ACT014 .equalsIgnoreCase(requestingAct)){
                bStatusPending = false;
                bStatusProcess = false;
                bStatusWaitingSync = false;
                bStatusDone = true;
                bParterEmpty = false;
                bParterProfile = false;
            }
        }else{
            requestingAct = ConstantBaseApp.ACT068;
            iniFilters();
        }
    }

    private void bindViews() {
        mketFilter = findViewById(R.id.act069_mket_filter);
        ivFilters = findViewById(R.id.act069_iv_status_filter);
        rvTickets = findViewById(R.id.act069_rv_ticket_list);
        tvNoResult = findViewById(R.id.act069_tv_no_result);
        btnSyncTickets = findViewById(R.id.act069_btn_sync);
        //
        setTranslation();
    }

    private void setTranslation() {
        mketFilter.setHint(hmAux_Trans.get("filter_hint"));
        tvNoResult.setText(hmAux_Trans.get("no_record_lbl"));
        btnSyncTickets.setText(hmAux_Trans.get("btn_sync_tickets"));
    }

    private void iniFilters() {
        bStatusPending = true;
        bStatusProcess = true;
        bStatusWaitingSync = true;
        bStatusDone = false;
        bParterEmpty = true;
        bParterProfile = true;
    }

    @Override
    public void loadTicketList(ArrayList<Act069_TicketVH> tickets) {
        if(tickets!= null && tickets.size() > 0) {
            tvNoResult.setVisibility(View.GONE);
            rvTickets.setVisibility(View.VISIBLE);
            //
            rvTickets.setLayoutManager(new LinearLayoutManager(context));
            //
            mAdapter = new Act069_Tickets_Adapter(
                context,
                R.layout.act069_ticket_cell,
                tickets
            );
            //
            if(mAdapter != null){
                mAdapter.setOnTicketClickListener(new Act069_Tickets_Adapter.OnTicketClickListener() {
                    @Override
                    public void onTicketClickListner(Act069_TicketVH item) {
                        //LUCHE - 18/03/2020
                        //Add chamada do metodo que define qual proximo step
                        mPresenter.checkTicketFlow(item);
                    }
                });
            }
            //
            rvTickets.setAdapter(mAdapter);
            mAdapter.getFilter().filter(mketFilter.getText().toString().trim());
        }else{
            tvNoResult.setVisibility(View.VISIBLE);
            rvTickets.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
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
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
            ttl,
            msg,
            hmAux_Trans.get("sys_alert_btn_cancel"),
            hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT069;
        mAct_Title = Constant.ACT069 + "_" + "title";
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
        btnSyncTickets.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mPresenter.hasTicketInUpdateRequired()) {
                        mPresenter.executeTicketSync();
                    } else{
                        showMsg(
                            hmAux_Trans.get("alert_ticket_to_send_ttl"),
                            hmAux_Trans.get("alert_ticket_to_send_msg")
                        );
                    }
                }
            }
        );
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = (View) LayoutInflater.from(context).inflate(R.layout.act069_filter_dialog,null);
        //
        TextView tvTitle = view.findViewById(R.id.act069_filter_dialog_tv_title);
        TextView tvStatusLbl = view.findViewById(R.id.act069_filter_dialog_tv_status_lbl);
        TextView tvPartnerLbl = view.findViewById(R.id.act069_filter_dialog_tv_partner_lbl);
        final CheckBox chkPending = view.findViewById(R.id.act069_filter_dialog_chk_pending);
        final CheckBox chkProcess = view.findViewById(R.id.act069_filter_dialog_chk_process);
        final CheckBox chkWaitingSync = view.findViewById(R.id.act069_filter_dialog_chk_waiting_sync);
        final CheckBox chkPartnerEmpty = view.findViewById(R.id.act069_filter_dialog_chk_no_partner);
        final CheckBox chkPartnerProfile = view.findViewById(R.id.act069_filter_dialog_chk_profile_partner);
        //
        tvTitle.setText(hmAux_Trans.get("dialog_filter_title"));
        tvStatusLbl.setText(hmAux_Trans.get("dialog_status_lbl"));
            tvPartnerLbl.setText(hmAux_Trans.get("dialog_partner_lbl"));
        //
        chkPending.setText(hmAux_Trans.get(Constant.SYS_STATUS_PENDING));
        chkPending.setChecked(bStatusPending);
        chkPending.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_PENDING))));
        chkPending.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_PENDING))));
        //
        chkProcess.setText(hmAux_Trans.get(Constant.SYS_STATUS_PROCESS));
        chkProcess.setChecked(bStatusProcess);
        chkProcess.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_PROCESS))));
        chkProcess.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_PROCESS))));
        //Esse ultimo stats só existe no quando lista origem do pendentes.
        chkWaitingSync.setText(hmAux_Trans.get(Constant.SYS_STATUS_WAITING_SYNC));
        chkWaitingSync.setChecked(bStatusWaitingSync);
        chkWaitingSync.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_WAITING_SYNC))));
        chkWaitingSync.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_WAITING_SYNC))));
        //
        chkPartnerEmpty.setText(hmAux_Trans.get("chk_allow_no_partner_lbl"));
        chkPartnerEmpty.setChecked(bParterEmpty);
        //
        chkPartnerProfile.setText(hmAux_Trans.get("chk_hide_other_partner_lbl"));
        chkPartnerProfile.setChecked(bParterProfile);
        //
        builder
            .setView(view)
            .setCancelable(true)
            .setPositiveButton(
                hmAux_Trans.get("sys_alert_btn_ok"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bStatusPending = chkPending.isChecked();
                        bStatusProcess = chkProcess.isChecked();
                        bStatusWaitingSync = chkWaitingSync.isChecked();
                        bParterEmpty = chkPartnerEmpty.isChecked();
                        bParterProfile = chkPartnerProfile.isChecked();
                        //
                        updateIvFilterState();
                        //
                        mPresenter.getTicketList(bStatusPending,bStatusProcess,bStatusWaitingSync, bStatusDone, bParterEmpty, bParterProfile, ticketProductCode, ticketSerialCode);
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

    private void updateIvFilterState() {
        if(requestingAct.equals(ConstantBaseApp.ACT014)){
            ivFilters.setVisibility(View.GONE);
        }else {
            if (bStatusPending || bStatusProcess || bStatusWaitingSync ||bParterEmpty || bParterProfile) {
                ivFilters.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
            } else {
                ivFilters.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
            }
        }
    }

    private Bundle generateActFilterBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ConstantBaseApp.SYS_STATUS_PENDING,bStatusPending);
        bundle.putBoolean(ConstantBaseApp.SYS_STATUS_PROCESS,bStatusProcess);
        bundle.putBoolean(ConstantBaseApp.SYS_STATUS_WAITING_SYNC,bStatusWaitingSync);
        bundle.putBoolean(FILTER_PARTNER_EMPTY,bParterEmpty);
        bundle.putBoolean(FILTER_PARTNER_PROFILE,bParterProfile);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,requestingAct);
        //
        return bundle;
    }

    @Override
    public void callAct070(Bundle bundle) {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putAll(generateActFilterBundle());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct071(Bundle bundle) {
        Intent intent = new Intent(context, Act071_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putAll(generateActFilterBundle());
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT069);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct005() {
        Intent intent = new Intent(context, Act005_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct012() {
        Intent intent = new Intent(context, Act012_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct014() {
        Intent intent = new Intent(context, Act014_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct068() {
        Intent intent = new Intent(context, Act068_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if(wsProcess.equals(WS_TK_Ticket_Download.class.getName())){
            wsProcess = "";
            initVars();
        }
        //
        progressDialog.dismiss();

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

    // Hugo
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

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);
        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked(requestingAct);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }


}
