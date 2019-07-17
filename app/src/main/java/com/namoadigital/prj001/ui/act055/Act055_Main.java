package com.namoadigital.prj001.ui.act055;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act055_IO_Move_Order_List_Adapter;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Move_Download;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.ui.act054.Act054_Main;
import com.namoadigital.prj001.ui.act058.act.Act058_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


public class Act055_Main extends Base_Activity implements Act055_Main_Contract.I_View, Act055_IO_Move_Order_List_Adapter.Act055ListListener {

    private Act055_Main_Presenter mPresenter;
    private RecyclerView rvMoveOrderList;
    private MKEditTextNM mketFilterDesc;
    private Act055_IO_Move_Order_List_Adapter mAdapter;
    //    private boolean serial_jump;
    private List<IO_Move_Search_Record> mMoveSearchList;
    private String wsProcess;
    private String requestAct;
    private boolean isLocalProcess;
    private boolean fromHistoric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act055_main);

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
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT055
        );

        loadTranslation();
        //
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act055_title");
        transList.add("move_order_filter_hint");
        transList.add("alert_error_on_processing_return_ttl");
        transList.add("alert_error_on_processing_return_msg");
        transList.add("dialog_download_move_ttl");
        transList.add("dialog_download_move_start");
        transList.add("alert_error_on_processing_return_ttl");
        transList.add("alert_error_on_processing_return_msg");
        transList.add("alert_no_move_found_ttl");
        transList.add("alert_no_move_found_msg");
        transList.add("alert_offline_search_ttl");
        transList.add("alert_offline_search_msg");
        //Traducao para itens da lista

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        mPresenter = new Act055_Main_Presenter(context, this, hmAux_Trans);
        recoverIntentsInfo();
        bindViews();
        setInitialView();
    }

    private void setInitialView() {
        if(isLocalProcess && !fromHistoric){
            mMoveSearchList = mPresenter.getPendenciesList();
        }
        mAdapter = new Act055_IO_Move_Order_List_Adapter(context, mMoveSearchList, this, hmAux_Trans);
        rvMoveOrderList.setLayoutManager(new LinearLayoutManager(context));
        rvMoveOrderList.setAdapter(mAdapter);
        mketFilterDesc.setHint(hmAux_Trans.get("move_order_filter_hint"));
    }

    private void bindViews() {
        rvMoveOrderList = findViewById(R.id.act055_rv_move_order_list);
        mketFilterDesc = findViewById(R.id.act055_mket_filter_desc);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mMoveSearchList = (List<IO_Move_Search_Record>) bundle.getSerializable(Constant.MAIN_WS_LIST_VALUES);
            isLocalProcess = bundle.getBoolean(ConstantBaseApp.IS_LOCAL_PROCESS, false);
            fromHistoric = bundle.getBoolean(ConstantBaseApp.FROM_HISTORIC, false);
            requestAct = bundle.getString(Constant.MAIN_REQUESTING_ACT);
        } else {
            requestAct = Constant.ACT005;
            mMoveSearchList = new ArrayList<>();
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT055;
        mAct_Title = Constant.ACT055 + Constant.title_lbl;
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

        mketFilterDesc.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
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

    }


    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (wsProcess.equals(WS_IO_Move_Download.class.getName())) {
            mPresenter.processSearchReturn(hmAux);
        }

        disableProgressDialog();

    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked(requestAct);
    }

    @Override
    public void showAlertSerialOut(String title, String msg) {
        showAlert(title, msg);
    }

    @Override
    public void onClickListItem(IO_Move_Search_Record record) {

        if(!fromHistoric) {
            if (isLocalProcess) {
                HMAux hmMove = new HMAux();
                hmMove.put(Constant.HMAUX_PREFIX_KEY, String.valueOf(record.getMove_prefix()));
                hmMove.put(Constant.HMAUX_CODE_KEY, String.valueOf(record.getMove_code()));
                hmMove.put(Constant.HMAUX_PROCESS_KEY, record.getMove_type());
                mPresenter.processSearchReturn(hmMove);
            }else if (ToolBox_Con.isOnline(context)) {
                mPresenter.getDownloadedMove(record.getMove_prefix() + "." + record.getMove_code());
            } else {
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_offline_search_ttl"),
                        hmAux_Trans.get("alert_offline_search_msg"),
                        null,
                        0
                );
            }
        }
    }


    @Override
    public void showPD(String title, String desc) {
        enableProgressDialog(
                title,
                desc,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void callAct054() {
        Intent mIntent = new Intent(context, Act054_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
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
    public void showAlert(String title, String msg) {
        ToolBox.alertMSG(
                context,
                title,
                msg,
                null,
                0
        );
    }

    @Override
    public void callAct058(Bundle bundle) {
        Intent mIntent = new Intent(context, Act058_Main.class);
        if (bundle != null) {
            bundle.putBoolean(ConstantBaseApp.IS_LOCAL_PROCESS, isLocalProcess);
            if(!isLocalProcess) {
                bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT054);
            }else{
                if(requestAct.equals(ConstantBaseApp.ACT054)){
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT055);
                }else {
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, requestAct);
                }
            }
            mIntent.putExtras(bundle);
        }
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
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
