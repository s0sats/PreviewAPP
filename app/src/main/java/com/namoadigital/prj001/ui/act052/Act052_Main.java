package com.namoadigital.prj001.ui.act052;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act052_IO_Serial_List_Adapter;
import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Download;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act053.Act053_Main;
import com.namoadigital.prj001.ui.act058.act.Act058_Main;
import com.namoadigital.prj001.ui.act059.Act059_Main;
import com.namoadigital.prj001.ui.act061.Act061_Main;
import com.namoadigital.prj001.ui.act064.Act064_Main;
import com.namoadigital.prj001.ui.act067.Act067_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act052_Main extends Base_Activity implements Act052_Main_Contract.I_View, OnRecyclerViewClickListener {

    private ArrayList<IO_Serial_Process_Record> serialListData;
    private boolean isOnline;

    private TextView tvSerialListSize;
    private TextView tvSerialListRecordLimit;
    private TextView tvSerialListRecordCount;
    private RecyclerView mSerialRecyclerView;
    private Button btn_create_serial;
    private TextView tvEmptyState;
    private RecyclerView.LayoutManager mSerialListLayoutManager;
    private Act052_IO_Serial_List_Adapter mSerialListAdapter;
    private Act052_Main_Presenter mPresenter;
    private String wsProcess ="";
    private long record_count;
    private long record_page;
    private String mSerial_id;
    private String mProduct_id;
    private boolean serial_jump;
    private LinearLayout llLimitExceeded;
    private boolean allowBlindMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act052_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSetup();
        initVars();
        initFooter();
        initAction();

    }

    private void initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT052
        );
        //
        loadTranslation();

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act052_title");
        transList.add("btn_blind_serial_move");
        transList.add("no_record_found_lbl");
        transList.add("records_lbl");
        transList.add("records_found_lbl");
        transList.add("records_display_limit_lbl");
        transList.add("btn_create_serial");
        transList.add("dialog_process_download_ttl");
        transList.add("dialog_process_download_starting_msg");
        //
        transList.add("alert_inbound_not_found_ttl");
        transList.add("alert_inbound_not_found_msg");
        transList.add("alert_outbound_not_found_ttl");
        transList.add("alert_outbound_not_found_msg");
        transList.add("alert_serial_out_site_title");
        transList.add("alert_serial_out_site_msg");
        transList.add("alert_serial_not_stored_ttl");
        transList.add("alert_serial_not_stored_msg");
        transList.add("alert_qty_records_exceeded_ttl");
        transList.add("alert_qty_records_exceeded_msg");
        transList.add("alert_qty_records_founded");
        transList.add("alert_serial_not_stored_ttl");
        transList.add("alert_serial_not_stored_msg");
        transList.add("alert_serial_not_found_ttl");
        transList.add("alert_serial_not_found_msg");
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
        mPresenter = new Act052_Main_Presenter(context, this, hmAux_Trans);
        bindViews();
        setSerialList();
        setTvSerialListSize();
        setBtnCreateSerial();
    }

    private void bindViews() {
        mSerialRecyclerView = findViewById(R.id.act052_rv_serial);
        tvSerialListSize = findViewById(R.id.act052_tv_serial_list_size);
        tvSerialListRecordLimit = findViewById(R.id.act052_tv_record_limit);
        tvSerialListRecordCount = findViewById(R.id.act052_tv_record_count);
        btn_create_serial = findViewById(R.id.act052_btn_create_serial);
        tvEmptyState = findViewById(R.id.act052_tv_empty_state);
        llLimitExceeded = findViewById(R.id.act052_ll_limit_exceeded);
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    private void setBtnCreateSerial() {
        btn_create_serial.setVisibility(View.GONE);
        //
        if (mPresenter.hasCreateSerialPermission(mProduct_id, mSerial_id,serial_jump) ) {
            btn_create_serial.setText(hmAux_Trans.get("btn_create_serial") + " (" + mSerial_id + ")");
            btn_create_serial.setVisibility(View.VISIBLE);
        }
    }
    private void setSerialList() {
        mSerialListLayoutManager = new LinearLayoutManager(this);
        mSerialRecyclerView.setLayoutManager(mSerialListLayoutManager);
        if(serialListData.isEmpty()) {
            tvEmptyState.setText(hmAux_Trans.get("no_record_found_lbl"));
            tvEmptyState.setVisibility(View.VISIBLE);
            llLimitExceeded.setVisibility(View.GONE);
            tvSerialListSize.setVisibility(View.GONE);
        }else{
            tvEmptyState.setVisibility(View.GONE);
        }
        //
        mSerialListAdapter = new Act052_IO_Serial_List_Adapter(
            this,
            serialListData,
            this,
            isOnline,
            serial_jump,
            hasMoveBlind() ? mSerial_id : ""
        );

        mSerialRecyclerView.setAdapter(mSerialListAdapter);
    }

    private void setTvSerialListSize() {
        tvSerialListSize.setText(hmAux_Trans.get("records_found_lbl") + " " + serialListData.size());
        tvSerialListRecordLimit.setText(hmAux_Trans.get("records_display_limit_lbl") + " " + record_page);
        tvSerialListRecordCount.setText(hmAux_Trans.get("records_found_lbl") + " " + record_count);
        //
        if(record_count > record_page){
            llLimitExceeded.setVisibility(View.VISIBLE);
            showAlert(
                hmAux_Trans.get("alert_qty_records_exceeded_ttl"),
                hmAux_Trans.get("alert_qty_records_exceeded_msg") + "\n" +
                    record_count + " " + hmAux_Trans.get("alert_qty_records_founded"),
                null
            );
        }else{
            llLimitExceeded.setVisibility(View.GONE);
        }
    }

    private void initFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT052;
        mAct_Title = Constant.ACT052 + Constant.title_lbl;
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
//        super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private boolean hasMoveBlind() {
        return ToolBox_Inf.profileExists(
                context,
                Constant.PROFILE_MENU_IO,
                Constant.PROFILE_MENU_IO_PARAM_BLIND_MOVE
        ) && !ToolBox_Con.isOnline(context)
                && !mSerial_id.isEmpty()
                && !mProduct_id.isEmpty()
                && allowBlindMove;
    }

    private void initAction() {
        btn_create_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.createNewSerialFlow(mProduct_id, mSerial_id);
            }
        });
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            serialListData = (ArrayList<IO_Serial_Process_Record>) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            isOnline = bundle.getBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_IS_ONLINE_PROCESS, true);
            record_count = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT);
            record_page = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE);
            mSerial_id = bundle.getString(Constant.MAIN_MD_PRODUCT_SERIAL_ID);
            mProduct_id = bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER);
            serial_jump = bundle.getBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
            allowBlindMove = bundle.getBoolean(IO_Blind_MoveDao.FLAG_BLIND, false);
        } else {
            serialListData = new ArrayList<>();
            isOnline = true;
            record_count = 0;
            record_page = 0;
            mSerial_id = "";
            mProduct_id = "";
            serial_jump = false;
            allowBlindMove = false;
        }
        //
    }

    @Override
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void showAlert(String ttl, String msg, @Nullable DialogInterface.OnClickListener listener) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            listener,
            0
        );
    }

    @Override
    public boolean getSerialJump() {
        return serial_jump;
    }

    @Override
    public void onClickListItem(IO_Serial_Process_Record data) {
        mPresenter.processListItem(data);
    }

    @Override
    public void onBlindMoveClick() {
        mPresenter.callBlindMove(mProduct_id, mSerial_id);
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct053(Bundle bundle) {
        Intent mIntent = new Intent(context, Act053_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct061(Bundle bundle) {
        Intent mIntent = new Intent(context, Act061_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct064(Bundle bundle) {
        Intent mIntent = new Intent(context, Act064_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct058(Bundle bundle) {
        Intent mIntent = new Intent(context, Act058_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct059(Bundle bundle) {
        Intent mIntent = new Intent(context, Act059_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct067(Bundle bundle) {
        Intent mIntent = new Intent(context, Act067_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if(wsProcess.equals(WS_IO_Serial_Process_Download.class.getName())){
            progressDialog.dismiss();
            //
            mPresenter.defineIOSerialFlow(hmAux);
        }
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
}
