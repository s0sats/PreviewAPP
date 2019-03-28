package com.namoadigital.prj001.ui.act057;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.IO_Inbound_Search_Record;
import com.namoadigital.prj001.service.WS_IO_Inbound_Search;
import com.namoadigital.prj001.ui.act056.Act056_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act057_Main extends Base_Activity implements Act057_Main_Contract.I_View {

    private Act057_Main_Presenter mPresenter;
    private MKEditTextNM mket_filter;
    private ImageView iv_status_filter;
    private RecyclerView rv_inbound;
    private boolean filter_pending = true;
    private boolean filter_process = true;

    private String bundle_zone_code;
    private String bundle_local_code;
    private String bundle_inbound_id;
    private String bundle_invoice;
    private long bundle_record_count;
    private long bundle_record_page;
    private ArrayList<IO_Inbound_Search_Record> records = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.act057_main);
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
        initActions();
    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT057
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act056_title");
        transList.add("filter_hint");
        transList.add("dialog_filter_title");
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
        mPresenter = new Act057_Main_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        bindViews();


    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            bundle_zone_code = bundle.getString(IO_InboundDao.ZONE_CODE_CONF,"");
            bundle_local_code = bundle.getString(IO_InboundDao.LOCAL_CODE_CONF,"");
            bundle_inbound_id = bundle.getString(IO_InboundDao.INBOUND_ID,"");
            bundle_invoice = bundle.getString(IO_InboundDao.INVOICE_NUMBER,"");
            bundle_record_count = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT,0);
            bundle_record_page = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE,0);
            try {
                records = (ArrayList<IO_Inbound_Search_Record>) bundle.getSerializable(Constant.MAIN_WS_LIST_VALUES);
            }catch (Exception e){
                records = new ArrayList<>();
            }
        } else {
            bundle_zone_code = "";
            bundle_local_code = "";
            bundle_inbound_id = "";
            bundle_invoice = "";
            bundle_record_count = 0;
            bundle_record_page = 0;
            records = new ArrayList<>();
        }
    }

    private void bindViews() {
        mket_filter = findViewById(R.id.act057_mket_filter);
        iv_status_filter = findViewById(R.id.act057_iv_status_filter);
        rv_inbound = findViewById(R.id.act057_rv_inbound_list);
        //
        mket_filter.setHint(hmAux_Trans.get("filter_hint"));
        controls_sta.add(mket_filter);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT057;
        mAct_Title = Constant.ACT057 + "_" + "title";
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

    private void initActions() {
        iv_status_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusFilterDialog();
            }
        });
    }

    private void showStatusFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = (View) LayoutInflater.from(context).inflate(R.layout.act057_filter_dialog,null);
        //
        TextView tvTitle = view.findViewById(R.id.act057_filter_dialog_tv_title);
        final CheckBox chkPending = view.findViewById(R.id.act057_filter_dialog_chk_pending);
        final CheckBox chkProcess = view.findViewById(R.id.act057_filter_dialog_chk_process);
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
    public void callAct056() {
        Intent mIntent = new Intent(context, Act056_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(MD_Site_Zone_LocalDao.ZONE_CODE,bundle_zone_code);
        bundle.putString(MD_Site_Zone_LocalDao.LOCAL_CODE,bundle_local_code);
        bundle.putString(WS_IO_Inbound_Search.KEY_CODE_ID,bundle_inbound_id);
        bundle.putString(IO_InboundDao.INVOICE_NUMBER,bundle_invoice);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }
}
