package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act028_Main extends Base_Activity_Frag {

    private Context context;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mDrawerStatus = true;

    private FragmentManager fm;


    private Bundle bundle;

    private long mCustomer_code;
    private int mSO_PREFIX;
    private int mSO_CODE;

    private HashMap<String, String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act028_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
    }

    private void iniSetup() {
        context = Act028_Main.this;

        fm = getSupportFragmentManager();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT028
        );

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act028_title");

//        sm_soDao = new SM_SODao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        ToolBox_Inf.libTranslation(getApplicationContext());
    }

    private void initVars() {
        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.act028_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act028_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                mDrawerStatus = true;

                ActivityCompat.invalidateOptionsMenu(Act028_Main.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                mDrawerStatus = false;

                ActivityCompat.invalidateOptionsMenu(Act028_Main.this);

            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recoverGetIntents();

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

//        act027_opc = (Act027_Opc) fm.findFragmentById(act027_opc);
//        act027_opc.setOnMenuOptionsSelected(this);
//
//        data = sm_soDao.getByStringHM(
//                new SM_SO_Sql_002(
//                        mCustomer_code,
//                        mSO_PREFIX,
//                        mSO_CODE
//                ).toSqlQuery()
//        );
//
//        act027_opc.setData(data);
//
//        act027_services = new Act027_Services();
//        act027_services.setOnItemClickListener(this);
//        act027_services.setData((ArrayList<HMAux>) sm_soDao.query_HM(
//                new SM_SO_Service_Sql_003(
//                        mCustomer_code,
//                        mSO_PREFIX,
//                        mSO_CODE
//                ).toSqlQuery()
//        ));
//
//        act027_serial = new Act027_Serial();
//        act027_serial.setBaInfra(this);
//        act027_serial.setBundle(bundle);
//        act027_serial.setHmAux_Trans(hmAux_Trans);
//        act027_serial.setData(data);
//
//        act027_header = new Act027_Header();
//        act027_header.setData(sm_soDao.getByStringHM(
//                new SM_SO_Sql_002(
//                        mCustomer_code,
//                        mSO_PREFIX,
//                        mSO_CODE
//                ).toSqlQuery()
//        ));
//
//        setFrag(act027_services, "SERVICES");

        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void recoverGetIntents() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mCustomer_code = ToolBox_Con.getPreference_Customer_Code(context);
            mSO_PREFIX = Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX, "-1"));
            mSO_CODE = Integer.parseInt(bundle.getString(SM_SODao.SO_CODE, "-1"));
            mData = (HashMap<String, String>) bundle.getSerializable("data");
        } else {
            mCustomer_code = ToolBox_Con.getPreference_Customer_Code(context);
            mSO_PREFIX = -1;
            mSO_CODE = -1;
            mData = new HMAux();
        }
    }

    private void iniUIFooter() {
        iniFooter();

        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT028;
        mAct_Title = Constant.ACT028 + "_" + "title";

        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();


        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);
        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);
        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value = hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl = hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value = hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;
    }

    private void initActions() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.act011_main_menu, menu);

        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));

        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerStatus) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //
            bundle.remove("data");
            //
            Intent mIntent = new Intent(context, Act027_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundle);
            //
            startActivity(mIntent);
            finish();
        }
    }

}
