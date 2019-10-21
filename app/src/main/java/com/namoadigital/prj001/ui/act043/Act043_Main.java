package com.namoadigital.prj001.ui.act043;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Params_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_SO_Service_Cancel;
import com.namoadigital.prj001.service.WS_SO_Service_Search;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.ui.act027.Act027_Opc;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.ServiceRegisterDialog;

import java.util.ArrayList;
import java.util.List;

public class Act043_Main extends Base_Activity_Frag_NFC_Geral
        implements Act043_Main_View, Act027_Opc.IAct027_Opc, onSmSoRequestObject, Act043_I_Add_Service_Interaction, Act043_Frag_Package_Detail_List.OnListFragmentInteractionListener{

    public static final String SELECTION_FRAG_PREVIEW = "FRAG_PREVIEW";
    public static final String SELECTION_FRAG_PACKAGE_DETAIL_LIST = "SELECTION_FRAG_PACKAGE_DETAIL_LIST";
    public static final String SELECTION_FRAG_SERVICE_LIST = "FRAG_SERVICE_LIST";

    public static final String TYPE_PS = "TYPE_PS";
    public static final String TYPE_PS_PACK = "P";
    public static final String TYPE_PS_SERVICE = "S";

    private Bundle bundle;
    private Act043_Main_Presenter_Impl mPresenter;
    private android.support.v4.app.FragmentManager fm;
    private String ws_process;
    private SM_SO mSm_so;
    private SM_SODao sm_soDao;
    private DrawerLayout mDrawerLayout;
    //FRAGMENTS
    private Act043_Frag_Preview act043_frag_preview;
    private Act043_Frag_Service_List act043_frag_service_list;
    private Act043_Frag_Package_Detail_List act043_frag_package_detail_list;
    private Act027_Opc act027_opc_;
    private String currentFrag = "";
    private Act043_Frag_Service_List mServiceList;
    private HMAux hmAux_TransDrawer = new HMAux();
    //Receiver do que captura disparo do FCM
    //LUCHE - 16/07/2019
    private FCMReceiver fcmReceiver;
    private ArrayList<MD_Partner> partner_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act043_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        hideKeyBoard();

        fm = getSupportFragmentManager();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT043
        );
        //
        sm_soDao = new SM_SODao(context);
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act043_title");
        transList.add("alert_discard_services_ttl");
        transList.add("alert_discard_services_msg");
        transList.add("alert_service_list_not_found_ttl");
        transList.add("alert_service_list_not_found_msg");
        //
        //FragPreview
        transList.add("btn_search_service");
        transList.add("services_tll");
        transList.add("total_lbl");
        transList.add("total_val");
        transList.add("dialog_service_search_ttl");
        transList.add("dialog_service_search_msg");
//        transList.add("alert_remove_service_confirm_ttl");
//        transList.add("alert_remove_service_confirm_msg");
        transList.add("alert_service_cancel_ttl");
        transList.add("alert_service_cancel_confirm");
        transList.add("dialog_service_cancel_start");
        transList.add("dialog_service_cancel_msg");
        //
        //Frag_Service_List
        transList.add("tv_service_list_title");
        transList.add("btn_save_service");
        transList.add("dialog_start_add_service_ttl");
        transList.add("dialog_start_add_service_msg");
        transList.add("dialog_receiving_add_service_msg");
        transList.add("alert_service_desc");
        transList.add("alert_service_id");
        transList.add("alert_package_id");
        transList.add("alert_service_qtd");
        transList.add("alert_service_price");
        transList.add("alert_service_price_hint");
        transList.add("alert_service_comments");
        transList.add("alert_service_remove");
        transList.add("alert_package_details");
        transList.add("alert_site_lbl");
        transList.add("alert_zone_lbl");
        transList.add("alert_partner_lbl");
        transList.add("alert_so_status");
        transList.add("alert_no_service_found_ttl");
        transList.add("alert_no_service_found_msg");
        transList.add("alert_invalid_service_value_ttl");
        transList.add("alert_invalid_service_value_msg");
        transList.add("alert_invalid_package_total_value_ttl");
        transList.add("alert_invalid_package_total_value_msg");
        transList.add("service_or_pack_filter_hint");
        //Frag_Package_Detail_List
        transList.add("tv_package_detail_list_ttl");
        transList.add("btn_save_package_detail");
        transList.add("btn_cancel_package_detail");
        transList.add("alert_service_without_price_ttl");
        transList.add("alert_service_without_price_msg");
        transList.add("site_package_detail_lbl");
        transList.add("zone_package_detail_lbl");
        transList.add("partner_package_detail_lbl");
        transList.add("comment_package_detail_lbl");
        transList.add("amount_package_detail_lbl");
        transList.add("btn_back");
        transList.add("alert_add_pack_ttl");
        transList.add("alert_add_pack_confirm");
        transList.add("alert_invalid_pack_ttl");
        transList.add("alert_invalid_pack_msg");
        transList.add("alert_remove_pack_ttl");
        transList.add("alert_remove_pack_confirm");
        //Drawer
        List<String> transListdrawer = new ArrayList<String>();
        transListdrawer.add("so_lbl");
        transListdrawer.add("so_id_lbl");
        transListdrawer.add("so_code_lbl");
        transListdrawer.add("product_lbl");
        transListdrawer.add("product_id_lbl");
        transListdrawer.add("product_header_lbl");
        transListdrawer.add("product_id_header_lbl");

        transListdrawer.add("product_description_lbl");
        transListdrawer.add("serial_lbl");
        transListdrawer.add("deadline_lbl");
        transListdrawer.add("status_lbl");
        transListdrawer.add("priority_lbl");

        transListdrawer.add("product_ll_lbl");
        transListdrawer.add("approval_ll_lbl");
        transListdrawer.add("services_ll_lbl");
        transListdrawer.add("serial_ll_lbl");
        transListdrawer.add("header_ll_lbl");
        transListdrawer.add("service_edition_ll_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
        //
        hmAux_TransDrawer = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                ToolBox_Inf.getResourceCode(
                        context,
                        mModule_Code,
                        Constant.ACT027
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transListdrawer
        );


    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(SM_SODao.SO_PREFIX) && bundle.containsKey(SM_SODao.SO_CODE)) {
                mSm_so = loadSM_So(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX, "0")),
                        Integer.parseInt(bundle.getString(SM_SODao.SO_CODE, "0"))
                );
            } else {
                //
            }
        } else {
        }
    }

    private SM_SO loadSM_So(long customer_code, int so_prefix, int so_code) {
        SM_SO sm_so = sm_soDao.getByString(
                new SM_SO_Sql_001(
                        customer_code,
                        so_prefix,
                        so_code
                ).toSqlQuery()
        );
        //
        return sm_so;
    }

    private void initVars() {
        //
        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.act027_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act043_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                act027_opc_.loadDataToScreen();

                ActivityCompat.invalidateOptionsMenu(Act043_Main.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                ActivityCompat.invalidateOptionsMenu(Act043_Main.this);

            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //
        recoverIntentsInfo();
        //
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        //
        mPresenter = new Act043_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                sm_soDao
        );
        //
        initFrags();
        //
        setFrag(act043_frag_preview, SELECTION_FRAG_PREVIEW);
        //mServiceList = new Act043_Frag_Service_List();
        //
        //setFrag(act043_frag_service_list, SELECTION_FRAG_SERVICE_LIST);
        //LUCHE - 16/07/2019
        initFCMReceiver();
    }
    //LUCHE - 16/07/2019
    private void initFCMReceiver() {
        fcmReceiver = new FCMReceiver();
        //
        startStopFCMReceiver(true);
    }

    private void initFrags() {
        // Drawer Opc
        act027_opc_ = (Act027_Opc) fm.findFragmentById(R.id.act027_opc);
        // Dialog Acess
        act027_opc_.setBaInfra(this);
        // Translation Access
        act027_opc_.setHmAux_Trans(hmAux_TransDrawer);
        // SO Acess
        act027_opc_.setmSm_so(mSm_so);
        //
        act027_opc_.setOnMenuOptionsSelected(this);
        //
        act027_opc_.serviceEditionColor();
        //
        act043_frag_preview = new Act043_Frag_Preview();
        act043_frag_preview.setBaInfra(this);
        act043_frag_preview.setHmAux_Trans(hmAux_Trans);


        act043_frag_package_detail_list = Act043_Frag_Package_Detail_List.newInstance(
                1,
                hmAux_Trans
        );

//        act043_frag_preview.setmSm_so(mSm_so);
        //
        act043_frag_service_list = new Act043_Frag_Service_List();
        act043_frag_service_list.setProgressAction(new Act043_Frag_Service_List.IAct043_Frag_Service_List() {
            @Override
            public void progressAction(String title, String message, String action) {
                switch (action.toUpperCase()) {
                    case "SHOW":
                        showPD(title, message);
                        break;
                    case "HIDE":
                        disableProgressDialog();
                        break;
                    case "CALLACT027":
                        callAct027(context);
                        break;
                    case "RELOAD_SO":
                        reloadSO();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void showResults(HMAux so) {
                Act043_Main.this.showResults(so);
            }
        });
        act043_frag_service_list.setBaInfra(this);
        act043_frag_service_list.setHmAux_Trans(hmAux_Trans);
//        act043_frag_service_list.setmService(mSm_so);
    }

    private void reloadSO() {
        mSm_so = loadSM_So(
                ToolBox_Con.getPreference_Customer_Code(context),
                Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX, "0")),
                Integer.parseInt(bundle.getString(SM_SODao.SO_CODE, "0"))
        );
        //
        act043_frag_preview.setmSm_so(mSm_so);
        //
        if(SELECTION_FRAG_PREVIEW.equals(currentFrag)){
            act043_frag_preview.loadDataToScreen();
        }
        //
        setFrag(act043_frag_preview, SELECTION_FRAG_PREVIEW);
    }

    //region Metodo interface onSmSoRequestObject
    @Override
    public SM_SO getSmSo() {
        return mSm_so;
    }

    @Override
    public HMAux getHMAux_Trans() {
        return hmAux_Trans;
    }
    //endregion

    //region Metodo interface Act043_I_Add_Service_Interaction
    @Override
    public ArrayList<TSO_Service_Search_Obj> getPackServiceAdapterList(ArrayList<TSO_Service_Search_Obj> packServiceList) {
        return mPresenter.prepareListToAdapter(packServiceList);
    }

    @Override
    public ArrayList<MD_Partner> getPartnerList() {
        return partner_list != null ? partner_list : new ArrayList<MD_Partner>();
    }

    @Override
    public ArrayList<HMAux> generateSiteOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone) {
        return mPresenter.generateSiteOption(rawSiteZone);
    }

    @Override
    public ArrayList<HMAux> generateSiteZoneOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone) {
        return mPresenter.generateSiteZoneOption(rawSiteZone);
    }

    @Override
    public void calculateTotalPrice(TSO_Service_Search_Obj packService) {
        mPresenter.calculateTotalPrice(packService);
    }

    @Override
    public void setPackageServiceDetailList(TSO_Service_Search_Obj packService) {
        act043_frag_package_detail_list.setPackageDataset(packService);
    }

    @Override
    public void resetPackService(TSO_Service_Search_Obj packService) {
        mPresenter.resetPackService(packService);
    }
    //endregion

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act043_main_ll, type, sTag);
            ft.commit();
            setCurrentFrag(sTag);
        }
    }

    @Override
    public void menuOptionsSelected(String type) {
        switch (type.toUpperCase()) {
            case Act027_Main.SELECTION_PRODUCT_LIST:
                callAct027(context, Act027_Main.SELECTION_PRODUCT_LIST);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case Act027_Main.SELECTION_SERVICES:
                callAct027(context, Act027_Main.SELECTION_SERVICES);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case Act027_Main.SELECTION_SERIAL:
                callAct027(context, Act027_Main.SELECTION_SERIAL);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case Act027_Main.SELECTION_APPROVAL:

                if (mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_CLIENT)) {

                    if (mSm_so.getClient_type().equalsIgnoreCase(Constant.CLIENT_TYPE_CLIENT)) {
                        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_APPROVE_CLIENT)) {
                            callAct027(context, Act027_Main.SELECTION_APPROVAL);
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_no_profile_ttl"),
                                    hmAux_Trans.get("alert_no_profile_msg"),
                                    null,
                                    0
                            );
                        }
                    } else {
                        callAct027(context, Act027_Main.SELECTION_APPROVAL);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }

                } else if (mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_QUALITY)) {
                    callAct027(context, Act027_Main.SELECTION_APPROVAL);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    callAct027(context, Act027_Main.SELECTION_APPROVAL);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }

                break;
            case Act027_Main.SELECTION_SERVICE_EDITION:
                if(ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_EDIT)) {
                    if (mSm_so.getSync_required() == 0 && mSm_so.getUpdate_required() == 0 && !act027_opc_.isSoWithinTokenFile()) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_sync_before_edit_service_ttl"),
                                hmAux_Trans.get("alert_sync_before_edit_service_msg"),
                                null,
                                0
                        );
                    }
                }else{
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_so_edit_profile_ttl"),
                            hmAux_Trans.get("alert_no_so_edit_profile_msg"),
                            null,
                            0
                    );
                }
                break;
            case Act027_Main.SELECTION_HEADER:
            default:
                callAct027(context, Act027_Main.SELECTION_HEADER);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public void soSyncClick() {
        callAct027(context,Act027_Main.SELECTION_SYNC_SERVICE);
//        ToolBox.alertMSG(
//                context,
//                hmAux_Trans.get("alert_so_sync_ttl"),
//                hmAux_Trans.get("alert_so_sync_msg"),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (ToolBox_Con.isOnline(context)) {
//                            //Seta S.O como update required.
//                            /*sm_soDao.addUpdate(
//                                    new SM_SO_Sql_009(
//                                            ToolBox_Con.getPreference_Customer_Code(context),
//                                            mSm_so.getSo_prefix(),
//                                            mSm_so.getSo_code()
//                                    ).toSqlQuery()
//                            );*/
//                            //
//                            setWs_process(WBR_SO_Search.class.getName());
//                            //
//                            executeSoSync();
//                        } else {
//                            ToolBox_Inf.showNoConnectionDialog(context);
//                        }
//                    }
//                },
//                1
//        );

    }

    public void setCurrentFrag(String currentFrag) {
        this.currentFrag = currentFrag;
    }

    public ArrayList<MD_Partner> getPartner_list() {
        return partner_list;
    }

    @Override
    public void setFragByTag(String tag) {
        switch (tag){
            case SELECTION_FRAG_PREVIEW:
                setFrag(act043_frag_preview,SELECTION_FRAG_PREVIEW);
                break;
            case  SELECTION_FRAG_SERVICE_LIST:
                setFrag(act043_frag_service_list,SELECTION_FRAG_SERVICE_LIST);
                break;
            case SELECTION_FRAG_PACKAGE_DETAIL_LIST:
                setFrag(act043_frag_package_detail_list,SELECTION_FRAG_PACKAGE_DETAIL_LIST);
                break;
        }
    }

    @Override
    public boolean hasItemAdded() {
        if(act043_frag_service_list != null){
           return act043_frag_service_list.hasAnyItemAdded();
        }else{
            return false;
        }
    }

    @Override
    public TSO_Service_Search_Obj getPackDetailObj() {
        if(act043_frag_package_detail_list != null){
            return act043_frag_package_detail_list.getPackObj();
        }else{
            return new TSO_Service_Search_Obj();
        }
    }

    public void resetFragListPosition(){
        //Reseta var de posição do item da lista
        if(act043_frag_service_list != null){
            act043_frag_service_list.resetClickedItemPosition();
        }
    }

    @Override
    public String getCurrentFrag() {
        return currentFrag;
    }

    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
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

//    private void executeSoSync(){
//        setWs_process(WBR_SO_Search.class.getName());
//        //
//        showPD(
//            hmAux_Trans.get("progress_so_sync_ttl"),
//            hmAux_Trans.get("progress_so_sync_msg")
//        );
//        //
//        Intent mIntent = new Intent(context, WBR_SO_Search.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constant.WS_SO_SEARCH_SO_MULT, String.valueOf(mSm_so.getSo_prefix() + "." + mSm_so.getSo_code()));
//        //
//        mIntent.putExtras(bundle);
//        //
//        context.sendBroadcast(mIntent);
//    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT043;
        mAct_Title = Constant.ACT043 + "_" + "title";
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
    public void callAct027(Context context) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Bundle bundle = new Bundle();
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    public void callAct027(Context context, String type) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Bundle bundle = new Bundle();
        bundle.putString(Act027_Main.REQUEST_SET_FRAG,type);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005() {
        Intent mIntent = new Intent(getApplicationContext(), Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private void hideKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    public void alertPackDetailRemoveConfirm(final TSO_Service_Search_Obj packageDetailObj) {
        ToolBox.alertMSG_YES_NO(
            context,
            hmAux_Trans.get("alert_remove_pack_ttl"),
            hmAux_Trans.get("alert_remove_pack_confirm"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    resetPackService(packageDetailObj);
                    setFragByTag(Act043_Main.SELECTION_FRAG_SERVICE_LIST);
                }
            },
            1
        );
    }

    private void showService_Pack_Details(final TSO_Service_Search_Obj item) {
        int dialogType = 0;

        ArrayList<HMAux> siteOption = new ArrayList<>();
        ArrayList<HMAux> siteZoneOption = new ArrayList<>();
        if(Act043_Main.TYPE_PS_SERVICE.equalsIgnoreCase(item.getType_ps())){
            dialogType =1;
        }
        if(item.getSite_zone() != null && !item.getSite_zone().isEmpty() ){
            siteOption = generateSiteOption(item.getSite_zone());
            siteZoneOption = generateSiteZoneOption(item.getSite_zone());
        }

        final ServiceRegisterDialog dialog =
            new ServiceRegisterDialog(
                context,
                dialogType,
                hmAux_Trans,
                item,
                siteOption,
                siteZoneOption,
                getPartnerList()
            );
        //
        final int finalDialogType = dialogType;
        final ArrayList<HMAux> finalSiteOption = siteOption;
        dialog.setBtnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (finalDialogType ){
                    case 0:

                        break;
                    case 1:
                        if(dialog.getCb_remove_val()){
                            Toast.makeText(context, "Delete item", Toast.LENGTH_SHORT).show();
                        }else {
                            if (dialog.getMk_qtd_val() != null && !dialog.getMk_qtd_val().isEmpty()
                                && dialog.getMk_price_val() != null && !dialog.getMk_price_val().isEmpty()
                                && ((dialog.get_ss_site_content().hasConsistentValue(SearchableSpinner.CODE)
                                && finalSiteOption.size() > 0) || finalSiteOption.isEmpty())
                                && ((dialog.get_ss_zone_content().hasConsistentValue(SearchableSpinner.CODE)
                                && finalSiteOption.size() > 0) || finalSiteOption.isEmpty())
                            ) {
                                item.setSelected(true);
                                item.setQty(Integer.valueOf(dialog.getMk_qtd_val().toString()));
                                item.setZone_code_selected(Integer.valueOf(dialog.get_ss_zone_content().get(SearchableSpinner.CODE)));
                                item.setSite_code_selected(Integer.valueOf(dialog.get_ss_site_content().get(SearchableSpinner.CODE)));
                                if (dialog.get_ss_partner_content().hasConsistentValue(SearchableSpinner.CODE)) {
                                    item.setPartner_code_selected(Integer.valueOf(dialog.get_ss_partner_content().get(SearchableSpinner.CODE)));
                                } else {
                                    item.setPartner_code_selected(null);
                                }
                                item.setComment(dialog.getMk_comments_val());
                                calculateTotalPrice(item);
                                dialog.dismiss();
                            } else {
                                ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_invalid_service_value_ttl"),
                                    hmAux_Trans.get("alert_invalid_service_value_msg"),
                                    null,
                                    0
                                );
                            }
                        }
                        break;

                }


//                if (checkFields(
//                        item,
//                        dialog.getCb_remove_val() ? "" : dialog.getMk_qtd_val(),
//                        dialog.getMk_price_val(),
//                        dialog.getCb_remove_val() ? "" : dialog.getMk_comments_val()
//                )) {

//                } else {
//                    dialog.resetMk_price_val();
//                }
            }
        });
        dialog.setBtnCancelListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setBtnPackageDetaillListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPackageServiceDetailList(item);
                //delegateMainView.setFragByTag(Act043_Main.SELECTION_FRAG_PACKAGE_DETAIL_LIST);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //region TRATATIVA_FCM

    /**
     * LUCHE - 16/07/2019
     */
    private class FCMReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if( bundle != null
                && bundle.containsKey(ConstantBaseApp.SW_TYPE)
                && bundle.getString(ConstantBaseApp.SW_TYPE).equals(ConstantBaseApp.FCM_ACTION_SM_SO_UPDATE)
                && act027_opc_ != null
            ){
                act027_opc_.loadDataToScreen();
            }
        }
    }

    private void startStopFCMReceiver(boolean start) {
        if(start){
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConstantBaseApp.WS_FCM);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            LocalBroadcastManager.getInstance(this).registerReceiver(fcmReceiver, filter);
        }else{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        //todo REVER
        mPresenter.deleteJsonFile();
        //Para receiver que ouve o FCM
        startStopFCMReceiver(false);
        //
        super.onDestroy();
    }

    //endregion

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    //region WS_Return
    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (ws_process.equalsIgnoreCase(WS_SO_Service_Search.class.getName())) {
            //
            if (hmAux != null && hmAux.hasConsistentValue(ConstantBaseApp.WS_RETURN_FILENAME)) {
                mPresenter.setFileName(hmAux.get(ConstantBaseApp.WS_RETURN_FILENAME));
                //
                if(mPresenter.jsonFileExists()) {
                    partner_list = mPresenter.getPackServicePartnerList();
                    ArrayList<TSO_Service_Search_Obj> servicesList = mPresenter.processServiceList();
                    if (servicesList != null && servicesList.size() > 0) {
                        act043_frag_service_list.setmService(mSm_so);
                        act043_frag_service_list.setAdapterData(
                            mPresenter.prepareListToAdapter(new ArrayList<>(servicesList))
                        );
                        setFrag(act043_frag_service_list, SELECTION_FRAG_SERVICE_LIST);
                    } else {
                        ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_service_found_ttl"),
                            hmAux_Trans.get("alert_no_service_found_msg"),
                            null,
                            0
                        );
                    }
                } else{
                    showAlert(
                        hmAux_Trans.get("alert_service_list_not_found_ttl"),
                        hmAux_Trans.get("alert_service_list_not_found_msg"),
                        null

                    );
                }
            } else {
                //DEFINIR MSG DE ERRO
            }
        //}else if(ws_process.equalsIgnoreCase(WBR_SO_Search.class.getName())){
        }else if(ws_process.equalsIgnoreCase(WS_SO_Service_Cancel.class.getName())){
            showResults(hmAux);
        }
        disableProgressDialog();
    }

    private void showResults(HMAux so) {
        ArrayList<HMAux> mSO = new ArrayList<>();

        for (String sKey : so.keySet()) {
            HMAux hmAux = new HMAux();
            //
            String sParts = sKey;

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, "SO");
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, sKey);

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_2, "alert_so_status");
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_2, so.get(sKey));

            mSO.add(hmAux);
        }

        showResultsDialog(mSO);
    }

    private void showResultsDialog(final List<HMAux> so_express) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

//        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
//        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        lv_results.setAdapter(
            new Generic_Results_Adapter(
                context,
                so_express,
                Generic_Results_Adapter.CONFIG_2_ITENS,
                hmAux_Trans
            )
        );

        //builder.setTitle(hmAux_Trans.get("alert_results_ttl"));
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                //
                if (so_express.size() > 0) {
                    if (so_express.get(0).get(Generic_Results_Adapter.VALUE_ITEM_2).equalsIgnoreCase("OK")) {
                        reloadSO();
                    } else {
                        reloadSO();
                    }
                }
            }
        });
    }

    private void showAlert(String ttl, String msg,@Nullable DialogInterface.OnClickListener listenerOK) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            listenerOK,
            0
        );
    }
//
//      AVALIAR QUAL METODO SERÁ USADO, CLIQUE NO SYNC CHAMADA ACT027
//      OU SE CHAMA WS DIRETAMENTE DESTA ACT
//
//    private void processSoDownloadResult(HMAux so_download_result) {
//        if (so_download_result.containsKey(WS_SO_Search.SO_PREFIX_CODE) && so_download_result.containsKey(WS_SO_Search.SO_LIST_QTY)) {
//            if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 0) {
//                //
//                ToolBox.alertMSG(
//                        context,
//                        hmAux_Trans.get("alert_so_invalid_status_ttl"),
//                        hmAux_Trans.get("alert_so_invalid_status_msg"),
//                        null,
//                        0
//                );
//
//            } else if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 1) {
//                //
//                ToolBox.alertMSG(
//                        context,
//                        hmAux_Trans.get("alert_so_sync_ok_ttl"),
//                        hmAux_Trans.get("alert_so_sync_ok_msg"),
//                        null,
//                        0
//                );
//
//            } else {
//                //
//                ToolBox.alertMSG(
//                        context,
//                        hmAux_Trans.get("alert_so_sync_ok_ttl"),
//                        hmAux_Trans.get("alert_so_sync_ok_msg"),
//                        null,
//                        0
//                );
//            }
//        } else {
//            // ToolBox_Inf.alertBundleNotFound(this,hmAux_Trans);
//            ToolBox.alertMSG(
//                    context,
//                    hmAux_Trans.get("alert_so_sync_param_error_ttl"),
//                    hmAux_Trans.get("alert_so_sync_param_error_msg"),
//                    null,
//                    0
//            );
//        }
//    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        disableProgressDialog();
        //REMOVER A LINHA ABAIXO APOS WS FUNCIONAR DIREITO
        //setFrag(act043_frag_service_list, SELECTION_FRAG_SERVICE_LIST);
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        disableProgressDialog();
        //REMOVER A LINHA ABAIXO APOS WS FUNCIONAR DIREITO
        //setFrag(act043_frag_service_list, SELECTION_FRAG_SERVICE_LIST);
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

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
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
    //endregion
}
