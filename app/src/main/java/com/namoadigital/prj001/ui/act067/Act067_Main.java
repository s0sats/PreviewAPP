package com.namoadigital.prj001.ui.act067;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.T_IO_Master_Data_Rec;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Master_Data;
import com.namoadigital.prj001.service.WS_IO_Outbound_Download;
import com.namoadigital.prj001.service.WS_IO_Outbound_Header_Save;
import com.namoadigital.prj001.service.WS_IO_Outbound_Item_Save;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act053.Act053_Main;
import com.namoadigital.prj001.ui.act058.act.Act058_Main;
import com.namoadigital.prj001.ui.act060.Act060_Main;
import com.namoadigital.prj001.ui.act062.Act062_Main;
import com.namoadigital.prj001.ui.act065.Act065_Main;
import com.namoadigital.prj001.ui.act066.Act066_Main;
import com.namoadigital.prj001.ui.act067.frag_drawer.Act067_Frag_Drawer;
import com.namoadigital.prj001.ui.act067.frag_header.Act067_Frag_Header;
import com.namoadigital.prj001.ui.act067.frag_item.Act067_Frag_Items;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act067_Main extends Base_Activity_Frag implements Act067_Main_Contract.I_View,
        Act067_Frag_Drawer.onFragDrawerInteraction,
        Act067_Frag_Header.onFragHeaderInteraction,
        Act067_Frag_Items.onFragItemInteraction{

    public static final String OUTBOUND_FRAG_HEADER = "OUTBOUND_FRAG_HEADER";
    public static final String OUTBOUND_FRAG_ITEM = "OUTBOUND_FRAG_ITEM";
    public static final String OUTBOUND_FRAG_DRAWER = "OUTBOUND_FRAG_DRAWER";
    public static final String FIRST_FRAG_TO_LOAD = "FIRST_FRAG_TO_LOAD";

    private Bundle bundle;
    private FragmentManager fm;
    private Act067_Frag_Drawer act067_frag_drawer;
    private Act067_Frag_Header act067_frag_header;
    private Act067_Frag_Items act067_frag_item;
    private DrawerLayout mDrawerLayout;
    private Act067_Main_Presenter mPresenter;
    private IO_Outbound mOutbound;
    private String mIoProcess;
    private int mPrefix;
    private int mCode;
    private boolean bNewProcess;
    private String wsProcess;
    private String fragToLoad = OUTBOUND_FRAG_HEADER;
    //Receiver do que captura disparo do FCM
    private FCMReceiver fcmReceiver;
    private String requestAct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.act067_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();

        if (savedInstanceState != null) {
            restoreSavedIntance(savedInstanceState);
        }
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT067
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        //Trad Act067
        transList.add("act067_title");

        transList.add("dialog_io_master_data_ttl");
        transList.add("dialog_io_master_data_start");
        transList.add("dialog_outbound_header_save_ttl");
        transList.add("dialog_outbound_header_save_start");
        transList.add("alert_header_save_error_ttl");
        transList.add("alert_header_save_no_return_msg ");
        transList.add("alert_header_save_process_error_msg");
        transList.add("alert_header_save_error_msg");
        transList.add("alert_io_master_data_error_ttl");
        transList.add("alert_io_master_data_error_msg");
        transList.add("progress_save_outbound_item_ttl");
        transList.add("progress_save_outbound_item_msg");
        transList.add("outbound_lbl");
        transList.add("alert_header_save_ttl");
        transList.add("alert_header_save_only_online_msg");
        transList.add("alert_from_outbound_error_ttl");
        transList.add("alert_from_outbound_error_msg");
        //
        transList.add("dialog_outbound_download_ttl");
        transList.add("dialog_outbound_download_start");
        transList.add("alert_download_return_ttl");
        transList.add("alert_download_return_msg");
        transList.add("alert_sync_ok_refresh_is_needed_msg");
        transList.add("alert_sync_data_ttl");
        transList.add("alert_sync_data_msg");
        transList.add("alert_outbound_results_ttl");
        transList.add("alert_download_return_error_msg");
        transList.add("alert_header_changes_will_be_lost_ttl");
        transList.add("alert_header_changes_will_be_lost_msg");

        //Trad Frag Drawer
        transList.addAll(Act067_Frag_Drawer.getFragTranslationsVars());
        //Trad Frag Header
        transList.addAll(Act067_Frag_Header.getFragTranslationsVars());
        //Trad Frag Item
        transList.addAll(Act067_Frag_Items.getFragTranslationsVars());
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void restoreSavedIntance(Bundle savedInstanceState) {

    }

    private void initVars() {
        recoverIntentsInfo();
        //
        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.act067_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act067_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                act067_frag_drawer.loadDataToScreen();

                ActivityCompat.invalidateOptionsMenu(Act067_Main.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //
                act067_frag_drawer.loadDataToScreen();
                //
                ActivityCompat.invalidateOptionsMenu(Act067_Main.this);

            }

        };
        //
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        //Usa o status de criação para definir de drawer aberto ou fechado no carregamento da tela.
        //Se novo, fecha o drawer
        setDrawerState(!bNewProcess);
        //Se criação trava o drawer
        setDrawerLocked(bNewProcess);
        //
        mPresenter = new Act067_Main_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        loadOutbound();
        //
        initFragment();
        //
        initFCMReceiver();
        //
        loadForcedFrag();
    }

    private void initFCMReceiver() {
        fcmReceiver = new FCMReceiver();
        //
        startStopFCMReceiver(true);
    }

    private void startStopFCMReceiver(boolean start) {
        if(start){
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.WS_FCM);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            LocalBroadcastManager.getInstance(this).registerReceiver(fcmReceiver, filter);
        }else{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver);
        }
    }

    private void loadForcedFrag() {

        //O COMANDO ABAIXO RODA ANTES DA VIEW DO FRAG CARREGAR ENTÃO NÃO RODA.
        //PENSAR EM OUTRA MANEIRA
        if (act067_frag_drawer != null && fragToLoad != null && !fragToLoad.isEmpty()) {
            // act067_drawer_content.forceFragSelection(fragToLoad);
        }
    }

    private void setDrawerLocked(boolean lockState) {
        if (lockState) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            //mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);

            mDrawerToggle.setDrawerIndicatorEnabled(false);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            //mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerLayout.openDrawer(GravityCompat.START);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
        //
        mDrawerToggle.syncState();
    }


    private void setDrawerState(boolean drawerState) {
        if (drawerState) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        //
        mDrawerToggle.syncState();
    }

    private void loadOutbound() {
        mOutbound = mPresenter.getOutbound(mPrefix, mCode);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            mIoProcess = bundle.getString(ConstantBaseApp.HMAUX_PROCESS_KEY, ConstantBaseApp.IO_OUTBOUND);
            mPrefix = Integer.parseInt(bundle.getString(ConstantBaseApp.HMAUX_PREFIX_KEY, "-1"));
            mCode = Integer.parseInt(bundle.getString(ConstantBaseApp.HMAUX_CODE_KEY, "-1"));
            bNewProcess = bundle.getBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY, false);
            requestAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT056);
            fragToLoad = bundle.getString(Act067_Main.FIRST_FRAG_TO_LOAD, OUTBOUND_FRAG_HEADER);
        } else {
            mIoProcess = "";
            mPrefix = -1;
            mCode = -1;
            bNewProcess = false;
            fragToLoad = OUTBOUND_FRAG_HEADER;
            requestAct = ConstantBaseApp.ACT056;
        }
    }

    private void initFragment() {
        act067_frag_drawer = Act067_Frag_Drawer.getInstance(hmAux_Trans, mPrefix, mCode);
        act067_frag_header = Act067_Frag_Header.getInstance(hmAux_Trans, mPrefix, mCode, bNewProcess);
        act067_frag_item = Act067_Frag_Items.getInstance(hmAux_Trans, mPrefix, mCode);
        //
        setDrawer(act067_frag_drawer, OUTBOUND_FRAG_DRAWER);
        //
        setFrag(act067_frag_header, OUTBOUND_FRAG_HEADER);
    }

    private void setDrawer(Act067_Frag_Drawer frag, String sTag) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act067_opc, frag, sTag);
        ft.addToBackStack(null);
        ft.commit();
    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act067_main_ll, type, sTag);
            ft.commit();
        } else {
            //type.loadDataToScreen();
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT067;
        mAct_Title = Constant.ACT067 + ConstantBaseApp.title_lbl;
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
    public void showFragAlert(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
    }

    @Override
    public void showResult(ArrayList<HMAux> resultList, boolean inboundResult) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = view.findViewById(R.id.act028_dialog_btn_ok);

        //trad
        tv_title.setText(hmAux_Trans.get("alert_outbound_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        resultList,
                        Generic_Results_Adapter.CONFIG_MENU_SEND_RET,
                        hmAux_Trans
                )
        );
        //
        builder.setView(view);
        builder.setCancelable(false);
        //
        final AlertDialog show = builder.show();
        //
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                refreshUi();
                //
                show.dismiss();
            }
        });

    }

    /**
     * Reload dados dos frag.
     */

    private void refreshUi() {
        if(act067_frag_drawer != null){
            act067_frag_drawer.loadDataToScreen();
        }
        if(act067_frag_header != null){
            //act067_frag_header.loadDataToScreen();
            //Load outbound chama o loadDataToScreen após atualiza dados da inbound.
            act067_frag_header.loadOutbound();
        }
        if(act067_frag_item != null){
            act067_frag_item.loadDataToScreen();
        }
    }

    @Override
    public void prepareFullRefresh() {
        loadOutbound();
        //
        refreshUi();
    }

    @Override
    public String getRequestingAct() {
        return requestAct;
    }

    @Override
    public void updateHeaderData(int outbound_prefix, int outbound_code, boolean newProcess) {
        //Se era um processo novo
        if (newProcess) {
            //Atualiza parametro recebido via bundle.
            mPrefix = outbound_prefix;
            mCode = outbound_code;
            bNewProcess = false;
        }
        //Atualiza dados da inbound naAct
        loadOutbound();
        //
        act067_frag_header.toggleIvEditStates(true);
        act067_frag_header.applyOutboundCreated(hmAux_Trans, mPrefix, mCode, bNewProcess, false);
        //
        act067_frag_drawer.loadDataToScreen();
        //Chama atualização do Drawer
        setDrawerLocked(bNewProcess);

    }

    //region FragInterface

    @Override
    public IO_Outbound getOutboundFromAct(int prefix, int code) {
        //return mOutbound;
        return mPresenter.getOutbound(mPrefix, mCode);
    }

    @Override
    public void toTypeSelected(String from_type) {
        mPresenter.executeWSMasterData(from_type, bNewProcess);
    }

    @Override
    public void showAlert(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
    }

    @Override
    public void saveOutboundHeader(IO_Outbound mOutbound) {
        this.mOutbound = mOutbound;
        if (ToolBox_Con.isOnline(context)) {
            //mPresenter.saveOutboundData(mOutbound);
            mPresenter.executeWsSaveOutboundHeader(mOutbound, bNewProcess);
        } else {
            showAlert(
                    hmAux_Trans.get("alert_header_save_ttl"),
                    hmAux_Trans.get("alert_header_save_only_online_msg")
            );
        }
    }

    @Override
    public void addFragHeaderControlsSS(ArrayList<SearchableSpinner> controls_ss) {
        this.controls_ss.addAll(controls_ss);
    }

    @Override
    public void addFragItemsControlsSta(ArrayList<MKEditTextNM> controls_sta) {
        this.controls_sta.addAll(controls_sta);
    }
    //region DrawerFragment

    @Override
    public void setFragToContainer(String fragTag) {
        switch (fragTag) {
            case OUTBOUND_FRAG_HEADER:
                setFrag(act067_frag_header, OUTBOUND_FRAG_HEADER);
                break;
            case OUTBOUND_FRAG_ITEM:
                setFrag(act067_frag_item, OUTBOUND_FRAG_ITEM);
                break;
        }
    }

    @Override
    public void updateDrawerState(boolean stateOpen) {
        setDrawerState(stateOpen);
    }

    @Override
    public String getFirstFragToLoad() {
        return fragToLoad != null ? fragToLoad : OUTBOUND_FRAG_HEADER;
    }

    @Override
    public void prepareSyncProcess() {
        mPresenter.checkSyncProcess(mOutbound);
    }

    @Override
    public boolean hasOutboundUpdateRequired() {
        return mPresenter.hasUpdateRequiredDbOrToken(mPrefix,mCode);
    }

    //endregion

    //region FragItems

    @Override
    public void addFragItemsControlsMk(ArrayList<MKEditTextNM> controls_sta) {
        this.controls_sta.addAll(controls_sta);
    }

    @Override
    public void removeFragHeaderControlsSS(ArrayList<SearchableSpinner> controls_ss) {
        this.controls_ss.removeAll(controls_ss);
    }

    @Override
    public void removeFragHeaderControlsSta(ArrayList<MKEditTextNM> controls_sta) {
        this.controls_sta.removeAll(controls_sta);
    }

    @Override
    public void callAddItemAct() {
        mPresenter.checkForUpdateRequired(mPrefix,mCode);
    }

    @Override
    public void callOutConfCreateItemAct(HMAux item) {
        callAct060(item);
    }

    @Override
    public void callPickingCreateItemAct(HMAux item) {
        mPresenter.processPickingMove(item);
    }

    @Override
    public void callSerialEdition(HMAux item) {
        mPresenter.processSerialEdition(item);
    }

    @Override
    public void callAct058(Bundle bundle) {
        Intent mIntent = new Intent(context, Act058_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        //
        startActivity(mIntent);
        finish();
    }

    private void callAct060(HMAux item) {
        Intent mIntent = new Intent(context, Act060_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT067);
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, mIoProcess);
        bundle.putString(IO_Outbound_ItemDao.OUTBOUND_PREFIX, String.valueOf(mPrefix));
        bundle.putString(IO_Outbound_ItemDao.OUTBOUND_CODE, String.valueOf(mCode));
        if (mOutbound.getZone_code_picking() != null) {
            bundle.putInt(IO_OutboundDao.ZONE_CODE_PICKING, mOutbound.getZone_code_picking());
        }
        if (mOutbound.getLocal_code_picking() != null) {
            bundle.putInt(IO_OutboundDao.LOCAL_CODE_PICKING, mOutbound.getLocal_code_picking());
        }
        bundle.putInt(IO_OutboundDao.PICKING_PROCESS, mOutbound.getPicking_process());
        bundle.putString(IO_Outbound_ItemDao.OUTBOUND_ITEM, String.valueOf(item.get(IO_Outbound_ItemDao.OUTBOUND_ITEM)));
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();

    }

    @Override
    public void callAct053(Bundle bundle) {
        Intent mIntent = new Intent(context, Act053_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        if (bundle != null) {
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT067);
            bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(mPrefix));
            bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(mCode));

            mIntent.putExtras(bundle);
            //
            startActivity(mIntent);
            finish();
        }else{
            //não deveria acontecer
            //COLOCAR MSG DE DADOS FALTANDO....
        }

    }

    //endregion

    //endregion


    @Override
    public void setMDList(ArrayList<MD_Site> sites, ArrayList<MD_Partner> partners, ArrayList<T_IO_Master_Data_Rec.ModalObj> modals) {
        if (act067_frag_header != null) {
            act067_frag_header.updateMDLists(sites, partners, modals);
        }
    }

    @Override
    public void callAct062() {
        Intent mIntent = new Intent(context, Act062_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT067);
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, mIoProcess);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(mPrefix));
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(mCode));
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }


//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//
//    }

    private class FCMReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if( bundle != null
                    && bundle.containsKey(ConstantBaseApp.SW_TYPE)
                    && bundle.getString(ConstantBaseApp.SW_TYPE).equals(ConstantBaseApp.FCM_ACTION_IO_OUTBOUND_UPDATE)
                    && act067_frag_drawer != null
            ){
                act067_frag_drawer.loadDataToScreen();
            }
        }
    }

    @Override
    protected void onDestroy() {
        //Para receiver que ouve o FCM
        startStopFCMReceiver(false);
        //
        super.onDestroy();

    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if (wsProcess.equalsIgnoreCase(WS_IO_Master_Data.class.getName())) {
            mPresenter.processIOMasterDataRet(mLink);
            progressDialog.dismiss();
        } else if (wsProcess.equalsIgnoreCase(WS_IO_Outbound_Header_Save.class.getName())) {
            mPresenter.processHeaderSave(mPrefix, mCode, mLink);
            progressDialog.dismiss();
        } else if(wsProcess.equals(WS_IO_Outbound_Item_Save.class.getName())) {
            mPresenter.processItemSaveReturn(mPrefix, mCode, mLink);
            progressDialog.dismiss();
        } else if(wsProcess.equalsIgnoreCase(WS_IO_Outbound_Download.class.getName())) {
            mPresenter.processDownloadReturn(mPrefix, mCode,hmAux);
            progressDialog.dismiss();
        } else {
            progressDialog.dismiss();
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


    @Override
    public void callAct014() {
        Intent mIntent = new Intent(context, Act014_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct065() {
        Intent mIntent = new Intent(context, Act065_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct066() {
        Intent mIntent = new Intent(context, Act066_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, requestAct);
        bundle.putBoolean(Act066_Main.LIST_PENDENCIES_KEY,true);
        mIntent.putExtras(bundle);
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
    public void onBackPressed() {
        mPresenter.onBackPressedClicked(requestAct,act067_frag_header != null && act067_frag_header.hasHeaderChanged());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
