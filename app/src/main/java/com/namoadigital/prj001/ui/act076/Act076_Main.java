package com.namoadigital.prj001.ui.act076;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act074_Next_Tickets_Adapter;
import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act068.Act068_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act076_Main extends Base_Activity implements Act076_Main_Contract.I_View{

    private MKEditTextNM mketFilter;
    //    private ImageView ivFilters;
    private RecyclerView rvTickets;
    private TextView tvNoResult;
    private Act076_Main_Presenter mPresenter;
    private Act074_Next_Tickets_Adapter mAdapter;
    private String requestingAct;
    private String wsProcess;
    //
    private long ticketProductCode = -1;
    private long ticketSerialCode = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act076_main);
        //
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
    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT076
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act076_title");
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
        recoverIntentsInfo();
        //
        mPresenter = new Act076_Main_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        mPresenter.getTicketList();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mketFilter.setText(bundle.getString(ConstantBaseApp.FILTER_TEXT,""));
            requestingAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT068);
        }else{
            requestingAct = ConstantBaseApp.ACT068;
        }
    }

    private void bindViews() {
        //
        mketFilter = findViewById(R.id.act076_mket_filter);
        rvTickets = findViewById(R.id.act076_rv_ticket_list);
        tvNoResult = findViewById(R.id.act076_tv_no_result);
        //
        setTranslation();
    }

    private void setTranslation() {
        mketFilter.setHint(hmAux_Trans.get("filter_hint"));
        tvNoResult.setText(hmAux_Trans.get("no_record_lbl"));
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT076;
        mAct_Title = Constant.ACT076 + "_" + "title";
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
    public void loadTicketList(ArrayList<Act074_TicketVH> tickets) {
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

    @Override
    public void callAct005() {
        Intent intent = new Intent(context, Act005_Main.class);
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

    //
    @Override
    public void callAct070(Bundle bundle) {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT076);
        if (mketFilter.getText().toString().isEmpty()) {
            bundle.putString(ConstantBaseApp.FILTER_TEXT,mketFilter.getText().toString());
        }
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    //
    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
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