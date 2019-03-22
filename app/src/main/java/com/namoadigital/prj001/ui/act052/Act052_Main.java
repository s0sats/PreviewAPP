package com.namoadigital.prj001.ui.act052;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act052_IO_Serial_List_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Download;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.ui.act051.Act051_Main;
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
    private Button btnBlindMove;
    private TextView tvEmptyState;
    private RecyclerView.LayoutManager mSerialListLayoutManager;
    private Act052_IO_Serial_List_Adapter mSerialListAdapter;
    private Act052_Main_Presenter mPresenter;
    private String wsProcess ="";
    private long record_count;
    private long record_page;
    private String mSerial_id;
    private String mProduct_id;
    private MD_Product md_product;
    private String ws_process;
    private boolean serial_jump;
    private LinearLayout llLimitExceeded;

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
        transList.add("btn_create_serial");
        transList.add("btn_blind_serial_move");
        transList.add("no_record_found_lbl");
        transList.add("records_lbl");
        transList.add("records_found_lbl");
        transList.add("records_display_limit_lbl");
        transList.add("btn_create_serial");
        transList.add("dialog_process_download_ttl");
        transList.add("dialog_process_download_starting_msg");
        //
        transList.add("alert_serial_out_site_title");
        transList.add("alert_serial_out_site_msg");
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
        bindViews();
        setSerialList();
        setTvSerialListSize();
        setBtnCreateSerial();
        mPresenter = new Act052_Main_Presenter(this, Act052_Main.this, hmAux_Trans);

    }

    private void bindViews() {
        mSerialRecyclerView = findViewById(R.id.act052_rv_serial);
        tvSerialListSize = findViewById(R.id.act052_tv_serial_list_size);
        tvSerialListRecordLimit = findViewById(R.id.act052_tv_record_limit);
        tvSerialListRecordCount = findViewById(R.id.act052_tv_record_count);
        btn_create_serial = findViewById(R.id.act052_btn_create_serial);
        btnBlindMove = findViewById(R.id.act052_btn_blind_move);
        tvEmptyState = findViewById(R.id.act052_tv_empty_state);
        llLimitExceeded = findViewById(R.id.act052_ll_limit_exceeded);
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    private void setBtnCreateSerial() {
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
        mSerialListLayoutManager = new LinearLayoutManager(this);
        mSerialRecyclerView.setLayoutManager(mSerialListLayoutManager);
        if(serialListData.isEmpty()) {
            mSerialRecyclerView.setVisibility(View.INVISIBLE);
            tvEmptyState.setText(hmAux_Trans.get("no_record_found_lbl"));
            tvEmptyState.setVisibility(View.VISIBLE);
            llLimitExceeded.setVisibility(View.GONE);
            tvSerialListSize.setVisibility(View.GONE);
            if(hasMoveBlind()){
                btnBlindMove.setVisibility(View.VISIBLE);
            }
        }else{
            tvEmptyState.setVisibility(View.GONE);
            mSerialListAdapter = new Act052_IO_Serial_List_Adapter(this, serialListData, this, hmAux_Trans, isOnline, serial_jump);
            mSerialRecyclerView.setAdapter(mSerialListAdapter);
            mSerialRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean isScrolling;

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = mSerialListLayoutManager.getChildCount();
                    int totalItemCount = mSerialListLayoutManager.getItemCount();
                    int pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        //End of list
                        if(hasMoveBlind()) {
                            btnBlindMove.setVisibility(View.VISIBLE);
                        }
                    }else {
                        if (hasMoveBlind()) {
                            btnBlindMove.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

    }

    private void setTvSerialListSize() {
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

    private boolean hasMoveBlind() {
        return ToolBox_Inf.profileExists(
                context,
                Constant.PROFILE_MENU_IO,
                Constant.PROFILE_MENU_IO_BLIND_MOVE
        ) && !ToolBox_Con.isOnline(context);
    }

    private void initAction() {
        btn_create_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean serial_creation = true;
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
    public void showAlertSerialOut(String title, String msg) {
        ToolBox.alertMSG(
                context,
                title,
                msg,
                null,
                0
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
