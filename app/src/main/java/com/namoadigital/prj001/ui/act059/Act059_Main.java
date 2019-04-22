package com.namoadigital.prj001.ui.act059;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.ui.act058.act.Act058_Main_Contract;
import com.namoadigital.prj001.ui.act058.frag.Frag_Move_Create;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act059_Main extends Base_Activity_Frag implements Act058_Main_Contract.I_View, Frag_Move_Create.OnFragmentInteractionListener {
    public static final String FRAGMENT_MOVE = "FRAGMENT_MOVE";
    private FragmentManager fm;
    private String mResource_Code_Frag;
    private HMAux hmAux_Trans_Frag;
    private String ws_process;
    private String actRequest;
    private Act059_Main_Presenter mPresenter;
    private Integer io_prefix;
    private Integer io_code;
    private Integer io_item;
    private IO_Inbound_Item io_inbound_item;
    private String move_type;
    private Frag_Move_Create frag_move_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act059_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        if (savedInstanceState != null) {
            restoreSavedIntance(savedInstanceState);
        }
        //
        initVars();
        //
        iniUIFooter();
        //

    }

    private void iniSetup() {
        recoverIntentsInfo();
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT058
        );
        //
        mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_MOVE_CREATE
        );

        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act059_title");
        transList.add("dialog_save_move_ttl");
        transList.add("dialog_save_move_msg");
        transList.add("alert_results_ttl");
        transList.add("sys_alert_btn_ok");
        transList.add("alert_move_results_ttl");
        transList.add("alert_move_list_title");
        transList.add("alert_move_ttl");
        transList.add("msg_move_save_ok");

        transList.add("alert_offline_save_msg");
        transList.add("alert_offline_save_ttl");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");

        transList.addAll(Frag_Move_Create.getFragTranslationsVars());

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        hmAux_Trans_Frag = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code_Frag,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            io_prefix = Integer.valueOf(bundle.getString(IO_Inbound_ItemDao.INBOUND_PREFIX));
            io_code = Integer.valueOf(bundle.getString(IO_Inbound_ItemDao.INBOUND_CODE));
            try {
                io_item = Integer.valueOf(bundle.getString(IO_Inbound_ItemDao.INBOUND_ITEM));
            }catch (Exception e ){
                e.printStackTrace();
                io_item = null;
            }
            actRequest = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, Constant.ACT005);
        }else{
            io_prefix = null;
            io_code = null;
            io_item = null;
            actRequest = Constant.ACT005;
        }

    }

    private void restoreSavedIntance(Bundle savedInstanceState) {

    }

    private void initVars() {
        Integer to_local_code;
        Integer to_zone_code;
        int move_prefix;
        int move_code;
        Integer reason_code;
        Integer planned_zone_code;
        Integer outbound_prefix;
        Integer inbound_prefix;
        Integer outbound_code;
        Integer inbound_code;
        Integer planned_local_code;
        String status;
        Integer to_class_code;
        MD_Product_Serial serialInfo;
        int viewMode;
        mPresenter = new Act059_Main_Presenter(context, this, hmAux_Trans);

        if(io_item == null){
            //handle IN_CONF?
            io_inbound_item = mPresenter.getInboudItem(io_prefix, io_code, io_item);
            to_local_code = null;
            to_zone_code = null;
            move_prefix = -1;
            move_code =-1;
            reason_code = null;
            outbound_prefix = null;
            inbound_prefix = io_inbound_item.getInbound_prefix();
            outbound_code = null;
            inbound_code = io_inbound_item.getInbound_code();
            status =ConstantBaseApp.SYS_STATUS_PENDING;
            planned_zone_code = io_inbound_item.getPlanned_zone_code();
            planned_local_code = io_inbound_item.getPlanned_local_code();
            to_class_code = null;
            move_type = ConstantBaseApp.IO_PROCESS_IN_CONF;
            viewMode = mPresenter.getViewMode(move_type);
            serialInfo = mPresenter.getSerialInfo(io_inbound_item.getProduct_code(),(int) io_inbound_item.getSerial_code());
        }else{
            io_inbound_item = mPresenter.getInboudItem(io_prefix, io_code, io_item);
            to_local_code = io_inbound_item.getLocal_code();
            to_zone_code = io_inbound_item.getZone_code();
            move_prefix = -1;
            move_code =-1;
            reason_code = null;
            outbound_prefix = null;
            inbound_prefix = io_inbound_item.getInbound_prefix();
            outbound_code = null;
            inbound_code = io_inbound_item.getInbound_code();
            status =ConstantBaseApp.SYS_STATUS_PENDING;
            planned_zone_code = io_inbound_item.getPlanned_zone_code();
            planned_local_code = io_inbound_item.getPlanned_local_code();
            to_class_code = null;
            move_type = ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY;
            viewMode = mPresenter.getViewMode(move_type);
            serialInfo = mPresenter.getSerialInfo(io_inbound_item.getProduct_code(),(int) io_inbound_item.getSerial_code());
        }
        frag_move_create = Frag_Move_Create.newInstance(
                serialInfo,
                viewMode,
                true,
                hmAux_Trans_Frag,
                to_local_code,
                to_zone_code,
                move_prefix,
                move_code,
                reason_code,
                move_type,
                planned_zone_code,
                outbound_prefix,
                inbound_prefix,
                outbound_code,
                inbound_code,
                planned_local_code,
                status,
                to_class_code
        );

        setFrag(frag_move_create, FRAGMENT_MOVE);
    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act058_frg_placeholder, type, sTag);
            ft.addToBackStack(null);
            ft.commit();
        } else {

        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT058;
        mAct_Title = Constant.ACT058 + "_" + "title";
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
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void persistIoMovePlanned(long customer_code, int move_prefix, int move_code, Integer to_zone_code, Integer to_local_code, Integer to_class_code, Integer reason_code, String done_date, MD_Product_Serial serial, List<IO_Move_Tracking> trackingFromMove) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void showAlert(String ttl, String msg) {

    }

    @Override
    public boolean isOnline() {
        return ToolBox_Con.isOnline(context);
    }

    @Override
    public void onAddOrRemoveControl(MKEditTextNM mket_tracking, boolean b) {

    }

    @Override
    public void onAddOrRemoveControlSS(SearchableSpinner searchableSpinner, boolean b) {

    }

    @Override
    public void onTrackingSearchClick(long product_code, long serial_code, String mket_text, String preference_site_code) {

    }

    @Override
    public void callLogAct(Intent logIntent) {

    }

    @Override
    public void callAct054() {

    }

    @Override
    public void setWs_process(String name) {

    }

    @Override
    public void callAct051() {

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
