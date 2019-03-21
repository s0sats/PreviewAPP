package com.namoadigital.prj001.ui.act052;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act052_IO_Serial_List_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Download;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.ui.act002.Act002_Main_Presenter;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class Act052_Main extends Base_Activity implements Act052_Main_Contract.I_View, OnRecyclerViewClickListener {

    private ArrayList<IO_Serial_Process_Record> serialListData;
    private boolean isOnline;


    private TextView tvSerialListSize;
    private TextView tvSerialListRecordLimit;
    private TextView tvSerialListRecordCount;
    private RecyclerView mSerialRecyclerView;
    private RecyclerView.LayoutManager mSerialListLayoutManager;
    private Act052_IO_Serial_List_Adapter mSerialListAdapter;
    private Act052_Main_Presenter mPresenter;
    private String wsProcess ="";
    private long record_count;
    private long record_page;
    private String mSerial_id;
    private Button btn_create_serial;
    private String mProduct_id;
    private MD_Product md_product;
    private String ws_process;
    private boolean serial_jump;

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
                Constant.ACT021
        );
        //
        loadTranslation();

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act051_title");
        transList.add("btn_new_serial");
        transList.add("btn_blind_serial_move");
        transList.add("empty_list_state_so_msg");

        transList.add("alert_new_opt_ttl");
        transList.add("alert_new_opt_product_lbl");
        transList.add("alert_new_opt_serial_lbl");
        transList.add("alert_new_opt_location_lbl");
        transList.add("alert_so_to_send_ttl");
        transList.add("alert_so_to_send_msg");
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
        transList.add("mket_serial_hint");
        transList.add("mket_tracking_hint");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_no_value_filled_ttl");
        transList.add("alert_no_value_filled_msg");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("alert_sync_success_ttl");
        transList.add("alert_sync_success_msg");
        transList.add("progress_so_save_approval_ttl");
        transList.add("progress_so_save_approval_msg");
        transList.add("progress_so_sync_ttl");
        transList.add("progress_so_sync_msg");
        transList.add("progress_so_save_ttl");
        transList.add("progress_so_save_msg");
        transList.add("alert_results_ttl");
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        transList.add("btn_create_serial");
        transList.add("records_found_lbl");
        transList.add("records_display_limit_lbl");
        transList.add("records_found_lbl");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        //
        transList.add("dialog_process_download_ttl");
        transList.add("dialog_process_download_starting_msg");
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
        setSerialList();
        setTvSerialListSize();
        setBtnCreateSerial();
        mPresenter = new Act052_Main_Presenter(this, Act052_Main.this, hmAux_Trans);

    }
    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    private void setBtnCreateSerial() {
        btn_create_serial = findViewById(R.id.act052_btn_create_serial);

        md_product = getMd_product();

        btn_create_serial.setVisibility(View.GONE);
        if (mProduct_id != null) {
            if (md_product.getAllow_new_serial_cl() == 1) {
                if(mSerial_id != null && !mSerial_id.isEmpty() && !serial_jump) {
                    btn_create_serial.setText(hmAux_Trans.get("btn_create_serial") + " (" + mSerial_id + ")");
                    btn_create_serial.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private MD_Product getMd_product() {
        MD_ProductDao mdProductDao = new MD_ProductDao(context);
        return mdProductDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        "",
                        mProduct_id
                ).toSqlQuery()
        );
    }

    private void setSerialList() {
        mSerialRecyclerView = findViewById(R.id.act052_rv_serial);

        mSerialListLayoutManager = new LinearLayoutManager(this);
        mSerialRecyclerView.setLayoutManager(mSerialListLayoutManager);

        mSerialListAdapter = new Act052_IO_Serial_List_Adapter(this, serialListData, this, hmAux_Trans, isOnline);
        mSerialRecyclerView.setAdapter(mSerialListAdapter);
    }

    private void setTvSerialListSize() {
        tvSerialListSize = findViewById(R.id.act052_tv_serial_list_size);
        tvSerialListRecordLimit = findViewById(R.id.act052_tv_record_limit);
        tvSerialListRecordCount = findViewById(R.id.act052_tv_record_count);

        tvSerialListSize.setText(hmAux_Trans.get("records_found_lbl") + " " + serialListData.size());
        tvSerialListRecordLimit.setText(hmAux_Trans.get("records_display_limit_lbl") + " " + record_page);
        tvSerialListRecordCount.setText(hmAux_Trans.get("records_found_lbl") + " " + record_count);


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

    private void initAction() {
        btn_create_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean serial_creation = true;
                //
                mPresenter.defineFlow(md_product.createNewSerialForThisProduct(mSerial_id),false);
                //mPresenter.createNewSerialFlow(md_product,serial_id);
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
        } else {
            serialListData = new ArrayList<>();
            isOnline = true;
            record_count = 0;
            record_page = 0;
            mSerial_id = "";
            mProduct_id = "";
            serial_jump = false;
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
    public void onClickListItem(IO_Serial_Process_Record data) {
        mPresenter.executeWsProcessDownload(data);
    }

    @Override
    public void onClickListButton() {
        Toast.makeText(context, "Indo para a movimentação cega", Toast.LENGTH_SHORT).show();
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

    //Tratativa SESSION NOT FOUND
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
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
    }
}
