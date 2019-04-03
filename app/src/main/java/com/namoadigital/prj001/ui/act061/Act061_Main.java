package com.namoadigital.prj001.ui.act061;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.ui.act056.Act056_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act061_Main extends Base_Activity_Frag implements  Act061_Main_Contract.I_View,
                                                                Act061_Frag_Drawer.onFragDrawerInteraction {
    public static final String INBOUND_FRAG_HEADER = "INBOUND_FRAG_HEADER";
    public static final String INBOUND_FRAG_ITEM = "INBOUND_FRAG_ITEM";
    public static final String NEW_IO_PROCESS_KEY = "NEW_IO_PROCESS_KEY";

    private Bundle bundle;
    private FragmentManager fm;
    private Act061_Frag_Drawer act061_frag_drawer;
    private Act061_Frag_Header act061_frag_header;
    private Act061_Frag_Item act061_frag_item;
    private DrawerLayout mDrawerLayout;
    private Act061_Main_Presenter mPresenter;
    private IO_Inbound mInbound;
    private String mIoProcess;
    private int mPrefix;
    private int mCode;
    private boolean bNewProcess;

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

        if(savedInstanceState != null){
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
        //Trad Frag Drawer
        transList.addAll(Act061_Frag_Drawer.getFragTranslationsVars());
        //Trad Frag Header
        transList.addAll(Act061_Frag_Header.getFragTranslationsVars());
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
        ){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                //act061_frag_drawer.loadDataToScreen();

                ActivityCompat.invalidateOptionsMenu(Act061_Main.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                ActivityCompat.invalidateOptionsMenu(Act061_Main.this);

            }

        };
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
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
    }

    private void loadInbound() {
        mInbound = mPresenter.getInbound(mPrefix,mCode);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            mIoProcess = bundle.getString(ConstantBaseApp.HMAUX_PROCESS_KEY, "");
            mPrefix = Integer.parseInt(bundle.getString(ConstantBaseApp.HMAUX_PREFIX_KEY, "-1"));
            mCode = Integer.parseInt(bundle.getString(ConstantBaseApp.HMAUX_CODE_KEY, "-1"));
            bNewProcess = bundle.getBoolean(NEW_IO_PROCESS_KEY, false);
        } else {
            mIoProcess = "";
            mPrefix = -1;
            mCode = -1;
            bNewProcess = false;
        }
    }

    private void initFragment() {
        act061_frag_drawer = Act061_Frag_Drawer.getInstance(hmAux_Trans,mPrefix,mCode);
        act061_frag_header = Act061_Frag_Header.getInstance(hmAux_Trans,mPrefix,mCode,bNewProcess);
        act061_frag_item = new Act061_Frag_Item();
        //
        setDrawer(act061_frag_drawer,"DRAWER");
        setFrag(act061_frag_header,INBOUND_FRAG_HEADER);
    }

    private void setDrawer(Act061_Frag_Drawer frag, String sTag){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act061_opc, frag, sTag);
        ft.addToBackStack(null);
        ft.commit();
    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act061_main_ll, type, sTag);
            ft.addToBackStack(null);
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
    //region DrawerFragment
    @Override
    public IO_Inbound getInboundFromAct(int prefix, int code) {
        //return mInbound;
        return mPresenter.getInbound(mPrefix,mCode);
    }
    //endregion


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
}
