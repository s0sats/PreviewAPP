package com.namoadigital.prj001.ui.act074;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act074_Next_Tickets_Adapter;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.T_TK_Next_Ticket_WS_Response;
import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Next_Ticket;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.ui.act068.Act068_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act069.Act069_Main.FILTER_PARTNER_EMPTY;
import static com.namoadigital.prj001.ui.act069.Act069_Main.FILTER_PARTNER_NO_PROFILE;
import static com.namoadigital.prj001.ui.act069.Act069_Main.FILTER_PARTNER_PROFILE;
import static com.namoadigital.prj001.util.ConstantBaseApp.FILTER_TEXT;

public class Act074_Main extends Base_Activity implements Act074_Main_Contract.I_View{
    private MKEditTextNM mketFilter;
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
    private HMAux mTicketDownloaded;

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
        //
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
        transList.add("no_record_lbl");
        transList.add("alert_error_on_generate_list_ttl");
        transList.add("alert_error_on_generate_list_msg");
        //
        transList.add("dialog_download_ticket_ttl");
        transList.add("dialog_download_ticket_start");
        //
        transList.add("alert_no_next_tickets_ttl");
        transList.add("alert_no_next_tickets_msg");
        transList.add("progress_next_tickets_ttl");
        transList.add("progress_next_tickets_msg");
        transList.add("progress_sync_ttl");
        transList.add("progress_sync_msg");
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
        mPresenter = new Act074_Main_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        if(ToolBox_Con.isOnline(context)) {
            mPresenter.getTicketList();
        }else{
            ToolBox_Inf.showNoConnectionDialogWithInteraction(context, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.onBackPressedClicked(requestingAct);
                }
            });
        }
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
            ticketProductCode = bundle.getLong(TK_TicketDao.OPEN_PRODUCT_CODE, -1);
            ticketSerialCode = bundle.getLong(TK_TicketDao.OPEN_SERIAL_CODE, -1);
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
        rvTickets = findViewById(R.id.act074_rv_ticket_list);
        tvNoResult = findViewById(R.id.act074_tv_no_result);
        //
        setTranslation();
    }

    private void setTranslation() {
        mketFilter.setHint(hmAux_Trans.get("filter_hint"));
        tvNoResult.setText(hmAux_Trans.get("no_record_lbl"));
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        processCloseACT(mLink,mRequired,new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if(WS_TK_Next_Ticket.class.getName().equalsIgnoreCase(wsProcess)) {
            wsProcess = "";
            progressDialog.dismiss();
            Gson gson = new GsonBuilder().serializeNulls().create();
            //
            T_TK_Next_Ticket_WS_Response rec = gson.fromJson(
                    mLink,
                    T_TK_Next_Ticket_WS_Response.class
            );
            //
            mPresenter.setTicketVH(rec.getNext_tickets());
        } else if(WS_TK_Ticket_Download.class.getName().equalsIgnoreCase(wsProcess)) {
            wsProcess = "";
            progressDialog.dismiss();
            if(mPresenter.verifyProductForForm()){
                mTicketDownloaded = hmAux;
            }else {
                processTicketDownloaded(hmAux);
            }
        } else if(WS_Sync.class.getName().equalsIgnoreCase(wsProcess)) {
            wsProcess = "";
            progressDialog.dismiss();
            processTicketDownloaded(mTicketDownloaded);
        }
    }

    private void processTicketDownloaded(HMAux hmAux) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, Integer.parseInt(hmAux.get(TK_TicketDao.TICKET_PREFIX)));
        bundle.putInt(TK_TicketDao.TICKET_CODE, Integer.parseInt(hmAux.get(TK_TicketDao.TICKET_CODE)));
        callAct070(bundle);
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        if(WS_TK_Next_Ticket.class.getName().equalsIgnoreCase(wsProcess)) {
            mPresenter.onBackPressedClicked(requestingAct);
        }
        //
        disableProgressDialog();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        if(WS_TK_Next_Ticket.class.getName().equalsIgnoreCase(wsProcess)) {
            mPresenter.onBackPressedClicked(requestingAct);
        }
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
    public void loadTicketList(ArrayList<Act074_TicketVH> tickets){
        if(tickets!= null && tickets.size() > 0) {
            tvNoResult.setVisibility(View.GONE);
            rvTickets.setVisibility(View.VISIBLE);
            //
            rvTickets.setLayoutManager(new LinearLayoutManager(context));
            //
            mAdapter = new Act074_Next_Tickets_Adapter(
                    context,
                    R.layout.act074_ticket_cell,
                    tickets
            );
            //
            if(mAdapter != null){
                mAdapter.setOnTicketClickListener(new Act074_Next_Tickets_Adapter.OnTicketClickListener() {
                    @Override
                    public void onTicketClickListner(Act074_TicketVH item) {

                        mPresenter.executeTicketSync(item);
                    }
                });
            }
            //
            mAdapter.getFilter().filter(mketFilter.getText().toString().trim());
            rvTickets.setAdapter(mAdapter);

            rvTickets.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mketFilter.setEnabled(true);
                    } else {
                        mketFilter.setEnabled(false);
                    }
                }
            });
        }else{
            tvNoResult.setVisibility(View.VISIBLE);
            rvTickets.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showMsg(String title, String msg) {
        ToolBox.alertMSG(
                context,
                title,
                msg,
                null,
                0
        );
    }

    @Override
    public void showEmptyListMsg(String title, String msg) {
        ToolBox.alertMSG(
                context,
                title,
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.onBackPressedClicked(requestingAct);
                    }
                },
                0
        );
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked(requestingAct);
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

    @Override
    public void callAct070(Bundle bundle) {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT074);
        intent.putExtras(bundle);
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
}