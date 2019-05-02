package com.namoadigital.prj001.ui.act061;

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
import android.view.*;
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
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.*;
import com.namoadigital.prj001.ui.act053.Act053_Main;
import com.namoadigital.prj001.ui.act056.Act056_Main;
import com.namoadigital.prj001.ui.act058.act.Act058_Main;
import com.namoadigital.prj001.ui.act059.Act059_Main;
import com.namoadigital.prj001.ui.act061.frag_drawer.Act061_Frag_Drawer;
import com.namoadigital.prj001.ui.act061.frag_header.Act061_Frag_Header;
import com.namoadigital.prj001.ui.act061.frg_item.Act061_Frag_Items;
import com.namoadigital.prj001.ui.act062.Act062_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act061_Main extends Base_Activity_Frag implements Act061_Main_Contract.I_View,
    Act061_Frag_Drawer.onFragDrawerInteraction,
    Act061_Frag_Header.onFragHeaderInteraction,
    Act061_Frag_Items.onFragItemInteraction {
    public static final String INBOUND_FRAG_HEADER = "INBOUND_FRAG_HEADER";
    public static final String INBOUND_FRAG_ITEM = "INBOUND_FRAG_ITEM";
    public static final String INBOUND_FRAG_DRAWER = "INBOUND_FRAG_DRAWER";
    public static final String FIRST_FRAG_TO_LOAD = "FIRST_FRAG_TO_LOAD";

    private Bundle bundle;
    private FragmentManager fm;
    private Act061_Frag_Drawer act061_frag_drawer;
    private Act061_Frag_Header act061_frag_header;
    private Act061_Frag_Items act061_frag_item;
    private DrawerLayout mDrawerLayout;
    private Act061_Main_Presenter mPresenter;
    private IO_Inbound mInbound;
    private String mIoProcess;
    private int mPrefix;
    private int mCode;
    private boolean bNewProcess;
    private String wsProcess;
    private String fragToLoad = INBOUND_FRAG_HEADER;
    //Receiver do que captura disparo do FCM
    private FCMReceiver fcmReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.act061_main);
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
            Constant.ACT061
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        //Trad Act061
        transList.add("act061_title");

        transList.add("dialog_io_master_data_ttl");
        transList.add("dialog_io_master_data_start");
        transList.add("dialog_from_outbound_ttl");
        transList.add("dialog_from_outbound_start");
        transList.add("dialog_inbound_header_save_ttl");
        transList.add("dialog_inbound_header_save_start");
        transList.add("alert_header_save_error_ttl");
        transList.add("alert_header_save_no_return_msg ");
        transList.add("alert_header_save_process_error_msg");
        transList.add("alert_header_save_error_msg");
        transList.add("alert_io_master_data_error_ttl");
        transList.add("alert_io_master_data_error_msg");
        transList.add("progress_save_inbound_item_ttl");
        transList.add("progress_save_inbound_item_msg");
        transList.add("inbound_lbl");
        transList.add("alert_move_results_ttl");
        transList.add("alert_header_save_ttl");
        transList.add("alert_header_save_only_online_msg");
        transList.add("alert_from_outbound_error_ttl");
        transList.add("alert_from_outbound_error_msg");
        //
        transList.add("dialog_inbound_download_ttl");
        transList.add("dialog_inbound_download_msg");
        transList.add("alert_download_return_ttl");
        transList.add("alert_download_return_msg");
        transList.add("alert_sync_ok_refresh_is_needed_msg");
        transList.add("alert_sync_data_ttl");
        transList.add("alert_sync_data_msg");
        //Trad Frag Drawer
        transList.addAll(Act061_Frag_Drawer.getFragTranslationsVars());
        //Trad Frag Header
        transList.addAll(Act061_Frag_Header.getFragTranslationsVars());
        //Trad Frag Item
        transList.addAll(Act061_Frag_Items.getFragTranslationsVars());
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
            findViewById(R.id.act061_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
            Act061_Main.this,
            mDrawerLayout,
            R.string.act005_drawer_opened,
            R.string.act005_drawer_closed
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                act061_frag_drawer.loadDataToScreen();

                ActivityCompat.invalidateOptionsMenu(Act061_Main.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //
                act061_frag_drawer.loadDataToScreen();
                //
                ActivityCompat.invalidateOptionsMenu(Act061_Main.this);

            }

        };
        //Usa o status de criação para definir de drawer aberto ou fechado no carregamento da tela.
        //Se novo, fecha o drawer
        setDrawerState(!bNewProcess);
        //Se criação trava o drawer
        setDrawerLocked(bNewProcess);
        //
        mPresenter = new Act061_Main_Presenter(
            context,
            this,
            hmAux_Trans
        );
        //
        loadInbound();
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
        if (act061_frag_drawer != null && fragToLoad != null && !fragToLoad.isEmpty()) {
            // act061_frag_drawer.forceFragSelection(fragToLoad);
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

    private void loadInbound() {
        mInbound = mPresenter.getInbound(mPrefix, mCode);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            mIoProcess = bundle.getString(ConstantBaseApp.HMAUX_PROCESS_KEY, ConstantBaseApp.IO_INBOUND);
            mPrefix = Integer.parseInt(bundle.getString(ConstantBaseApp.HMAUX_PREFIX_KEY, "-1"));
            mCode = Integer.parseInt(bundle.getString(ConstantBaseApp.HMAUX_CODE_KEY, "-1"));
            bNewProcess = bundle.getBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY, false);
            fragToLoad = bundle.getString(Act061_Main.FIRST_FRAG_TO_LOAD, INBOUND_FRAG_HEADER);
        } else {
            mIoProcess = "";
            mPrefix = -1;
            mCode = -1;
            bNewProcess = false;
            fragToLoad = INBOUND_FRAG_HEADER;
        }
    }

    private void initFragment() {
        act061_frag_drawer = Act061_Frag_Drawer.getInstance(hmAux_Trans, mPrefix, mCode);
        act061_frag_header = Act061_Frag_Header.getInstance(hmAux_Trans, mPrefix, mCode, bNewProcess);
        act061_frag_item = Act061_Frag_Items.getInstance(hmAux_Trans, mPrefix, mCode);
        //
        setDrawer(act061_frag_drawer, INBOUND_FRAG_DRAWER);
        //
        setFrag(act061_frag_header, INBOUND_FRAG_HEADER);
    }

    private void setDrawer(Act061_Frag_Drawer frag, String sTag) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act061_opc, frag, sTag);
        ft.addToBackStack(null);
        ft.commit();
    }

//    /**
//     * LUCHE 01/05/2019 - FELIZ DIA DO TRABALHO TRABALHANDO....
//     * Esse metodo funciona como o outro setFrag, porem sem destruir os fragmentos ja construido
//     * Apenas escondendo e mostrando uma vez q o frag ja foi construido
//     * Por hora nao será usa pois seria necessario rever o fluxo dos Frag
//     *
//     * Com esse metodo, não é preciso usar o retainInstance nos frags
//     * @param type
//     * @param sTag
//     * @param <T>
//     */
//    private <T extends BaseFragment> void setFrag(T type, String sTag) {
//        FragmentTransaction ft = fm.beginTransaction();
//        //FAz loop no fragmentos do mannager
//        //Esconde todos que são o drawer
//        for(Fragment frag :  fm.getFragments()){
//            if(!frag.getTag().equals(INBOUND_FRAG_DRAWER)){
//                ft.hide(frag);
//            }
//        }
//        //Se o frag seleciona NÃO esta no fm,
//        //  Adiciona e commit
//        //Se ESTIVER, apenas o "mostra" e commit
//        if (fm.findFragmentByTag(sTag) == null) {
//            ft.add(R.id.act061_main_ll, type, sTag);
//            ft.commit();
//        } else {
//            ft.show(fm.findFragmentByTag(sTag));
//            ft.commit();
//        }
//        //Após exibi o frag, roda o loadDataToScreen
//        //Opcional, mas pode fazer sentido para remover esse metodo de dentro do
//        //onResume.
//        //type.loadDataToScreen();
//    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act061_main_ll, type, sTag);
            ft.commit();
        } else {
            //type.loadDataToScreen();
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT061;
        mAct_Title = Constant.ACT061 + "_" + "title";
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
        tv_title.setText(hmAux_Trans.get("alert_inbound_results_ttl"));
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
        if(act061_frag_drawer != null){
            act061_frag_drawer.loadDataToScreen();
        }
        if(act061_frag_header != null){
            //act061_frag_header.loadDataToScreen();
            //Load inbound chama o loadDataToScreen após atualiza dados da inbound.
            act061_frag_header.loadInbound();
        }
        if(act061_frag_item != null){
            act061_frag_item.loadDataToScreen();
        }
    }

    @Override
    public void prepareFullRefresh() {
        loadInbound();
        //
        refreshUi();
    }

    @Override
    public void updateHeaderData(int inbound_prefix, int inbound_code, boolean newProcess) {
        //Se era um processo novo
        if (newProcess) {
            //Atualiza parametro recebido via bundle.
            mPrefix = inbound_prefix;
            mCode = inbound_code;
            bNewProcess = false;
        }
        //
        act061_frag_header.toggleIvEditStates(true);
        act061_frag_header.applyInboundCreated(hmAux_Trans, mPrefix, mCode, bNewProcess, false);
        //
        act061_frag_drawer.loadDataToScreen();
        //Chama atualização do Drawer
        setDrawerLocked(bNewProcess);

    }

    //region FragInterface

    @Override
    public IO_Inbound getInboundFromAct(int prefix, int code) {
        //return mInbound;
        return mPresenter.getInbound(mPrefix, mCode);
    }

    @Override
    public void fromTypeSelected(String from_type) {
        mPresenter.executeWSMasterData(from_type, bNewProcess);
    }

    @Override
    public void searchFromOutboundList(String from_site) {
        mPresenter.executeWsSearchOutbound(from_site);
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
    public void saveInboundHeader(IO_Inbound mInbound) {
        this.mInbound = mInbound;
        if (ToolBox_Con.isOnline(context)) {
            //mPresenter.saveInboundData(mInbound);
            mPresenter.executeWsSaveInboundHeader(mInbound, bNewProcess);
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
    //region DrawerFragment

    @Override
    public void setFragToContainer(String fragTag) {
        switch (fragTag) {
            case INBOUND_FRAG_HEADER:
                setFrag(act061_frag_header, INBOUND_FRAG_HEADER);
                break;
            case INBOUND_FRAG_ITEM:
                setFrag(act061_frag_item, INBOUND_FRAG_ITEM);
                break;
        }
    }

    @Override
    public void updateDrawerState(boolean stateOpen) {
        setDrawerState(stateOpen);
    }

    @Override
    public String getFirstFragToLoad() {
        return fragToLoad != null ? fragToLoad : INBOUND_FRAG_HEADER;
    }

    @Override
    public void prepareSyncProcess() {
        mPresenter.checkSyncProcess(mInbound);
    }

    //endregion

    //region FragItems

    @Override
    public void addFragItemsControlsMk(ArrayList<MKEditTextNM> controls_sta) {
        this.controls_sta.addAll(controls_sta);
    }

    @Override
    public void callAddItemAct() {
        mPresenter.checkForUpdateRequired(mPrefix,mCode);
    }

    @Override
    public void callInConfCreateItemAct(HMAux item) {
        callAct059(item);
    }

    @Override
    public void callPutAwayCreateItemAct(HMAux item) {
        mPresenter.processPutAwayMove(item);
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

    private void callAct059(HMAux item) {
        Intent mIntent = new Intent(context, Act059_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT061);
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, mIoProcess);
        bundle.putString(IO_Inbound_ItemDao.INBOUND_PREFIX, String.valueOf(mPrefix));
        bundle.putString(IO_Inbound_ItemDao.INBOUND_CODE, String.valueOf(mCode));
        if (mInbound.getZone_code_conf() != null) {
            bundle.putInt(IO_InboundDao.ZONE_CODE_CONF, mInbound.getZone_code_conf());
        }
        if (mInbound.getLocal_code_conf() != null) {
            bundle.putInt(IO_InboundDao.LOCAL_CODE_CONF, mInbound.getLocal_code_conf());
        }
        bundle.putInt(IO_InboundDao.PUT_AWAY_PROCESS, mInbound.getPut_away_process());
        bundle.putString(IO_Inbound_ItemDao.INBOUND_ITEM, String.valueOf(item.get(IO_Inbound_ItemDao.INBOUND_ITEM)));
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
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT061);
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
        if (act061_frag_header != null) {
            act061_frag_header.updateMDLists(sites, partners, modals);
        }
    }

    @Override
    public void setFromOutboundList(ArrayList<IO_Outbound_Search_Record> outbound) {
        if (act061_frag_header != null) {
            act061_frag_header.updateFromOutboundList(outbound);
        }
    }

    @Override
    public void callAct062() {
        Intent mIntent = new Intent(context, Act062_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT061);
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, mIoProcess);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(mPrefix));
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(mCode));
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        mDrawerToggle.syncState();
        //
        super.onPostCreate(savedInstanceState);

    }

    private class FCMReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if( bundle != null
                && bundle.containsKey(ConstantBaseApp.SW_TYPE)
                && bundle.getString(ConstantBaseApp.SW_TYPE).equals(ConstantBaseApp.FCM_ACTION_IO_INBOUND_UPDATE)
                && act061_frag_drawer != null
            ){
                act061_frag_drawer.loadDataToScreen();
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
        //super.processCloseACT(mLink, mRequired);
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if (wsProcess.equalsIgnoreCase(WS_IO_Master_Data.class.getName())) {
            mPresenter.processIOMasterDataRet(mLink);
            progressDialog.dismiss();
        } else if (wsProcess.equalsIgnoreCase(WS_IO_From_Site_Search.class.getName())) {
            mPresenter.processFromOutboundRet(mLink);
            progressDialog.dismiss();
        } else if (wsProcess.equalsIgnoreCase(WS_IO_Inbound_Header_Save.class.getName())) {
            mPresenter.processHeaderSave(mPrefix, mCode, mLink);
            progressDialog.dismiss();
        } else if(wsProcess.equals(WS_IO_Inbound_Item_Save.class.getName())) {
            mPresenter.processItemSaveReturn(mPrefix, mCode, mLink);
            progressDialog.dismiss();
        } else if(wsProcess.equalsIgnoreCase(WS_IO_Inbound_Download.class.getName())) {
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
    public void callAct056() {
        Intent mIntent = new Intent(context, Act056_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
