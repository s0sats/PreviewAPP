package com.namoadigital.prj001.ui.act058.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act054.Act054_Main;
import com.namoadigital.prj001.ui.act058.frag.Frag_Move_Create;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act058_Main extends Base_Activity_Frag implements Act058_Main_Contract.I_View, Frag_Move_Create.OnFragmentInteractionListener {

    public static final String FRAGMENT_MOVE = "FRAGMENT_MOVE";
    private FragmentManager fm;
    private Frag_Move_Create frag_move_create;
    private Act058_Main_Presenter mPresenter;
    private int moveCode;
    private int movePrefix;
    private String mResource_Code_Frag;
    private HMAux hmAux_Trans_Frag;
    private String ws_process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act058_main);
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
        //movido para utilizar o objeto na criação da
        recoverIntentsInfo();
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT058
        );
        //
        mResource_Code_Frag= ToolBox_Inf.getResourceCode(
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
        transList.add("act058_title");

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

    private void restoreSavedIntance(Bundle savedInstanceState) {

    }

    private void initVars() {
        mPresenter = new Act058_Main_Presenter(context, this, hmAux_Trans);

        IO_Move moveInfo = mPresenter.getMoveInfo(movePrefix, moveCode);
        int viewMode = mPresenter.getViewMode(moveInfo);

        MD_Product_Serial serialInfo = mPresenter.getSerialInfo(moveInfo.getProduct_code(), moveInfo.getSerial_code());

        frag_move_create = Frag_Move_Create.newInstance(moveInfo,
                                                        serialInfo,
                                                        viewMode,
                                                        true,
                                                        hmAux_Trans_Frag);

        setFrag(frag_move_create, FRAGMENT_MOVE);
    }

    private void recoverIntentsInfo() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            movePrefix = Integer.valueOf(bundle.getString(IO_MoveDao.MOVE_PREFIX));
            moveCode = Integer.valueOf(bundle.getString(IO_MoveDao.MOVE_CODE));
        } else {
            movePrefix = 0;
            moveCode = 0;
        }

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
    protected void processCloseACT(String mLink, String mRequired) {
//        super.processCloseACT(mLink, mRequired);
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if(ws_process.equals(WS_Serial_Tracking_Search.class.getName())) {
            frag_move_create.processTrackingResult(hmAux);
        }
        disableProgressDialog();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        callAct054();
    }

    @Override
    public boolean isOnline() {
        return ToolBox_Con.isOnline(context);
    }

    @Override
    public ArrayList<HMAux> getReasonOption() {
        return mPresenter.getMoveReasonList();
    }

    @Override
    public void onAddOrRemoveControl(MKEditTextNM mket_tracking, boolean b) {

    }

    @Override
    public void onTrackingSearchClick(long product_code, long serial_code, String tracking, String site_code) {
        mPresenter.executeTrackingSearch(product_code,serial_code,tracking,site_code);
    }

    @Override
    public ArrayList<HMAux> getClassList() {
        return mPresenter.getClassList();
    }

    @Override
    public void callLogAct(Intent logIntent) {
        startActivityForResult(logIntent, Constant.REQUEST_CODE_SERIAL_LOG);
    }

    @Override
    public void persistIoMove(long customer_code,
                              int move_prefix,
                              int move_code,
                              Integer to_zone_code,
                              Integer to_local_code,
                              Integer to_class_code,
                              Integer reason_code,
                              String done_date,
                              ArrayList<MD_Product_Serial> serial){
        IO_Move io_move = new IO_Move();
        io_move.setCustomer_code(customer_code);
        io_move.setMove_prefix(move_prefix);
        io_move.setMove_code(move_code);
        io_move.setTo_zone_code(to_zone_code);
        io_move.setTo_local_code(to_local_code);
        io_move.setTo_class_code(to_class_code);
        io_move.setReason_code(reason_code);
        io_move.setDone_date(done_date);
        io_move.setSerial(serial);
        mPresenter.executeMovePersistence(io_move);
    }

    @Override
    protected void footerCreateDialog() {
//        super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
    public void showAlert(String ttl, String msg) {

    }

    @Override
    public void callAct054() {
        Intent mIntent = new Intent(context, Act054_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
