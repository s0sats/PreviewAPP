package com.namoadigital.prj001.ui.act066;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.namoadigital.prj001.adapter.Act066_Outbound_Download_Adapter;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.IO_Outbound_Search_Record;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Outbound_Download;
import com.namoadigital.prj001.service.WS_IO_Outbound_Search;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.ui.act065.Act065_Main;
import com.namoadigital.prj001.ui.act067.Act067_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act066_Main extends Base_Activity implements Act066_Main_Contract.I_View {
    public static final String LIST_PENDENCIES_KEY = "LIST_PENDENCIES_KEY";
    private Act066_Main_Presenter mPresenter;
    private ConstraintLayout cl_no_result;
    private ConstraintLayout cl_result;
    private ConstraintLayout cl_limit_exceeded;
    private TextView tv_no_records;
    private TextView tv_records;
    private TextView tv_record_limit;
    private TextView tv_record_count;
    private MKEditTextNM mket_filter;
    private ImageView iv_status_filter;
    private RecyclerView rv_outbound;
    private Button btn_download;
    private boolean filter_pending = true;
    private boolean filter_process = true;
    private boolean filter_waiting = true;
    private boolean isOnline = true;
    private String wsProcess;
    private String bundle_zone_code;
    private String bundle_local_code;
    private String bundle_outbound_id;
    private String bundle_invoice;
    private long bundle_record_count;
    private long bundle_record_page;
    private ArrayList<IO_Outbound_Search_Record> records = new ArrayList<>();
    private Act066_Outbound_Download_Adapter mAdapter;
    private String requestingAct;
    private boolean listPendencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act066_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT066
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act066_title");
        transList.add("filter_hint");
        transList.add("dialog_filter_title");
        //
        transList.add("dialog_outbound_download_ttl");
        transList.add("dialog_outbound_download_start");
        transList.add("download_lbl");
        transList.add("alert_no_outbound_selected_ttl");
        transList.add("alert_no_outbound_selected_msg");
        transList.add("no_record_found_lbl");
        transList.add("records_found_lbl");
        transList.add("records_display_limit_lbl");
        transList.add("records_lbl");
        transList.add("showing_lbl");
        transList.add("alert_qty_records_exceeded_ttl");
        transList.add("alert_qty_records_exceeded_msg");
        transList.add("alert_qty_records_founded");
        transList.add("alert_download_return_ttl");
        transList.add("alert_download_return_error_msg");
        transList.add("alert_outbound_different_to_site_ttl");
        transList.add("alert_outbound_different_to_site_msg");
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
        //
        mPresenter = new Act066_Main_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        bindViews();
        setupViews();
        //
        if (listPendencies) {
            if (requestingAct.equals(ConstantBaseApp.ACT014)) {
                mPresenter.getHistoricList();
            } else {
                mPresenter.getPendenciesList();
            }
        } else {
            mPresenter.processListInfo(bundle_record_count, bundle_record_page, records);
        }
        //
        updateIvFilterState();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            bundle_zone_code = bundle.getString(IO_InboundDao.ZONE_CODE_CONF, "");
            bundle_local_code = bundle.getString(IO_InboundDao.LOCAL_CODE_CONF, "");
            bundle_outbound_id = bundle.getString(IO_InboundDao.INBOUND_ID, "");
            bundle_invoice = bundle.getString(IO_InboundDao.INVOICE_NUMBER, "");
            bundle_record_count = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, 0);
            bundle_record_page = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, 0);
            try {
                records = (ArrayList<IO_Outbound_Search_Record>) bundle.getSerializable(Constant.MAIN_WS_LIST_VALUES);
                //Se lista vazia, veio pelos pendentes.
                isOnline = records.size() > 0;
            } catch (Exception e) {
                records = new ArrayList<>();
                isOnline = false;
            }
            requestingAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT056);
            listPendencies = bundle.getBoolean(LIST_PENDENCIES_KEY, false);
            //Filtro de waitingSync só existe na listegem de pendentes.
            filter_waiting = listPendencies;
        } else {
            bundle_zone_code = "";
            bundle_local_code = "";
            bundle_outbound_id = "";
            bundle_invoice = "";
            bundle_record_count = 0;
            bundle_record_page = 0;
            records = new ArrayList<>();
            requestingAct = ConstantBaseApp.ACT056;
            listPendencies = false;
        }
    }

    private void bindViews() {
        cl_no_result = findViewById(R.id.act066_cl_no_result);
        cl_result = findViewById(R.id.act066_cl_result);
        cl_limit_exceeded = findViewById(R.id.act066_cl_limit_exceeded);
        tv_no_records = findViewById(R.id.act066_tv_no_result);
        tv_records = findViewById(R.id.act066_tv_records);
        tv_record_limit = findViewById(R.id.act066_tv_record_limit);
        tv_record_count = findViewById(R.id.act066_tv_record_count);
        mket_filter = findViewById(R.id.act066_mket_filter);
        iv_status_filter = findViewById(R.id.act066_iv_status_filter);
        rv_outbound = findViewById(R.id.act066_rv_outbound_list);
        btn_download = findViewById(R.id.act066_btn_download);
    }

    private void setupViews() {
        tv_no_records.setText(hmAux_Trans.get("no_record_found_lbl"));
        //tv_records.setText(hmAux_Trans.get("records_found_lbl"));
        mket_filter.setHint(hmAux_Trans.get("filter_hint"));
        btn_download.setText(hmAux_Trans.get("download_lbl"));
        btn_download.setEnabled(false);
        btn_download.setVisibility(isOnline ? View.VISIBLE : View.GONE);
        controls_sta.add(mket_filter);
    }


    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT066;
        mAct_Title = Constant.ACT066 + "_" + "title";
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

    private void initActions() {
        iv_status_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusFilterDialog();
            }
        });
        //
        mket_filter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(s.trim());
                }
            }
        });
        //
        if (mAdapter != null) {
            mAdapter.setOnItemClickListner(new Act066_Outbound_Download_Adapter.OnItemClickListner() {
                @Override
                public void onItemClick(IO_Outbound_Search_Record item) {
                    if (item.isSameSiteAsLoggedOrFree()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, Constant.IO_OUTBOUND);
                        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(item.getOutbound_prefix()));
                        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(item.getOutbound_code()));
                        //
                        callAct067(bundle);
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_outbound_different_to_site_ttl"),
                                hmAux_Trans.get("alert_outbound_different_to_site_msg"),
                                null,
                                0
                        );
                    }
                }
            });
            //
            mAdapter.setOnItemCheckedChangeListener(new Act066_Outbound_Download_Adapter.OnItemCheckedChangeListener() {
                @Override
                public void onItemCheckedChange(int downloadCounter) {
                    btn_download.setText(
                            hmAux_Trans.get("download_lbl") + " (" + downloadCounter + ")"
                    );
                    //
                    btn_download.setEnabled(downloadCounter > 0);
                }
            });
        }
        //
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter != null) {
                    String outbounds = mAdapter.getOutboundsToDownload();
                    if (outbounds != null && outbounds.trim().length() > 0) {
                        mPresenter.executeOutboundDownload(outbounds);

                    } else {
                        showAlert(
                                hmAux_Trans.get("alert_no_outbound_selected_ttl"),
                                hmAux_Trans.get("alert_no_outbound_selected_msg")
                        );
                    }
                }
            }
        });
    }

    /**
     * Aplica cor no icone de filtro dependendo do valor dos filtros
     */
    private void updateIvFilterState() {
        if (requestingAct.equals(ConstantBaseApp.ACT014)) {
            iv_status_filter.setVisibility(View.GONE);
        } else {
            if (filter_pending || filter_process || filter_waiting) {
                iv_status_filter.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
            } else {
                iv_status_filter.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
            }
        }
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if (wsProcess.equals(WS_IO_Outbound_Download.class.getName())) {
            progressDialog.dismiss();
            //
            mPresenter.processDownloadReturn(hmAux);
//        }else if(wsProcess.equals(WS_IO_Outbound_Item_Save.class.getName())){
//            progressDialog.dismiss();
//            //
//            mPresenter.processOutboundItemReturn(mLink);
        } else {
            progressDialog.dismiss();
        }
    }

    //region Handling WS Errors
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
    //endregion


    @Override
    public void showAlert(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null
                , 0
        );
    }

    @Override
    public void setRecordInfo(int record_size) {
        if (record_size > 0) {
            cl_no_result.setVisibility(View.GONE);
            cl_limit_exceeded.setVisibility(View.GONE);
            cl_result.setVisibility(View.VISIBLE);
            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + record_size + " " + hmAux_Trans.get("records_lbl"));
            //
            if (bundle_record_count > bundle_record_page) {
                showQtyExceededMsg();
            }
        } else {
            tv_records.setVisibility(View.GONE);
            cl_no_result.setVisibility(View.VISIBLE);
            tv_no_records.setVisibility(View.VISIBLE);
            cl_result.setVisibility(View.GONE);
        }
    }

    @Override
    public void showQtyExceededMsg() {
        cl_limit_exceeded.setVisibility(View.VISIBLE);
        tv_record_limit.setText(hmAux_Trans.get("records_display_limit_lbl") + " " + bundle_record_page);
        tv_record_count.setText(hmAux_Trans.get("records_lbl") + " " + bundle_record_count);
        //
        showAlert(
                hmAux_Trans.get("alert_qty_records_exceeded_ttl"),
                hmAux_Trans.get("alert_qty_records_exceeded_msg") + "\n" +
                        bundle_record_count + " " + hmAux_Trans.get("alert_qty_records_founded")
        );
    }

    @Override
    public void loadOutboundList(ArrayList<IO_Outbound_Search_Record> records) {
        mAdapter = new Act066_Outbound_Download_Adapter(
                context,
                records,
                filter_pending,
                filter_process,
                filter_waiting,
                isOnline,
                listPendencies
        );
        //
        rv_outbound.setAdapter(mAdapter);
        rv_outbound.setLayoutManager(
                new LinearLayoutManager(context)
        );
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
    public void callAct067(Bundle bundle) {
        Intent mIntent = new Intent(context, Act067_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(bundle != null){
            if(listPendencies && requestingAct.equals(ConstantBaseApp.ACT065)){
                bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT066);
            }else{
                bundle.putString(Constant.MAIN_REQUESTING_ACT,requestingAct);
            }
        }

        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void setOnline(boolean online) {

    }

    @Override
    public void callAct012() {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct014() {
        Intent mIntent = new Intent(context, Act014_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct065() {
        Intent mIntent = new Intent(context, Act065_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(MD_Site_Zone_LocalDao.ZONE_CODE, bundle_zone_code);
        bundle.putString(MD_Site_Zone_LocalDao.LOCAL_CODE, bundle_local_code);
        bundle.putString(WS_IO_Outbound_Search.KEY_CODE_ID, bundle_outbound_id);
        bundle.putString(IO_InboundDao.INVOICE_NUMBER, bundle_invoice);
        //
        startActivity(mIntent);
        finish();
    }

    private void showStatusFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = (View) LayoutInflater.from(context).inflate(R.layout.act066_filter_dialog, null);
        //
        TextView tvTitle = view.findViewById(R.id.act066_filter_dialog_tv_title);
        final CheckBox chkPending = view.findViewById(R.id.act066_filter_dialog_chk_pending);
        final CheckBox chkProcess = view.findViewById(R.id.act066_filter_dialog_chk_process);
        final CheckBox chkWaitingSync = view.findViewById(R.id.act066_filter_dialog_chk_waiting_sync);
        //
        tvTitle.setText(hmAux_Trans.get("dialog_filter_title"));
        //
        chkPending.setText(hmAux_Trans.get(Constant.SYS_STATUS_PENDING));
        chkPending.setChecked(filter_pending);
        chkPending.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_PENDING))));
        chkPending.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_PENDING))));
        //
        chkProcess.setText(hmAux_Trans.get(Constant.SYS_STATUS_PROCESS));
        chkProcess.setChecked(filter_process);
        chkProcess.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_PROCESS))));
        chkProcess.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_PROCESS))));
        //Esse ultimo stats só existe no quando lista origem do pendentes.
        chkWaitingSync.setText(hmAux_Trans.get(Constant.SYS_STATUS_WAITING_SYNC));
        chkWaitingSync.setChecked(filter_waiting);
        chkWaitingSync.setButtonTintList(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_WAITING_SYNC))));
        chkWaitingSync.setTextColor(ColorStateList.valueOf(getResources().getColor(ToolBox_Inf.getApStatusColor(Constant.SYS_STATUS_WAITING_SYNC))));
        chkWaitingSync.setVisibility(listPendencies ? View.VISIBLE : View.GONE );
        chkWaitingSync.setEnabled(listPendencies );
        //
        builder
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(
                        hmAux_Trans.get("sys_alert_btn_ok"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                filter_pending = chkPending.isChecked();
                                filter_process = chkProcess.isChecked();
                                filter_waiting = chkWaitingSync.isChecked();
                                updateIvFilterState();
                                if(mAdapter != null){
                                    mAdapter.updateStatusFilter(filter_pending,filter_process, filter_waiting );
                                    mAdapter.getFilter().filter(mket_filter.getText().toString().trim());
                                }
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        mPresenter.onBackPressedClicked(requestingAct);
    }
}
