package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act027_Main extends Base_Activity {

    private Context context;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager fm;
    private Act027_Opc act027_opc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act027_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
    }

    private void iniSetup() {
        context = Act027_Main.this;

        fm = getSupportFragmentManager();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT027
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
//        transList.add("drawer_change_customer_alert_ttl");
//        transList.add("drawer_change_customer_alert_msg");
//        transList.add("drawer_change_site_alert_ttl");
//        transList.add("drawer_change_site_alert_msg");
//        transList.add("drawer_change_operation_alert_ttl");
//        transList.add("drawer_change_operation_alert_msg");
//        transList.add("drawer_logout_alert_ttl");
//        transList.add("drawer_logout_alert_msg");
//        transList.add("alert_sync_ttl");
//        transList.add("alert_sync_msg");
//        transList.add("alert_exit_confirm_ttl");
//        transList.add("alert_exit_confirm_msg");
//        transList.add("alert_sync_finish_ttl");
//        transList.add("alert_sync_finish_msg");
//        transList.add("alert_send_finish_ttl");
//        transList.add("alert_send_finish_msg");
//        transList.add("drawer_sync_alert_ttl");
//        transList.add("drawer_sync_alert_msg");
//        transList.add("drawer_change_site_one_site_alert_ttl");
//        transList.add("drawer_change_site_one_site_alert_msg");
//        transList.add("drawer_change_operation_one_operation_alert_ttl");
//        transList.add("drawer_change_operation_one_operation_alert_msg");
//        transList.add("msg_start_sync");
//        transList.add("msg_preparing_to_send_data");
//        transList.add("logout_dialog_btn");
//        transList.add("logout_dialog_ttl");
//        transList.add("alert_logout_ttl");
//        transList.add("alert_logout_msg");
//        transList.add("lbl_sync_data");
//        transList.add("lbl_logout");
//        transList.add("lbl_schedule_data");
//        transList.add("lbl_so");
//
//        transList.add("toolbar_enable_nfc");
//        transList.add("toolbar_cancel_nfc");
//        transList.add("toolbar_support");
//        transList.add("alert_enable_nfc_ttl");
//        transList.add("alert_enable_nfc_msg");
//        transList.add("alert_cancel_nfc_ttl");
//        transList.add("alert_cancel_nfc_msg");
//        transList.add("alert_support_ttl");
//        transList.add("alert_support_msg");
//
//        transList.add("progress_enable_nfc_ttl");
//        transList.add("progress_enable_nfc_msg");
//        transList.add("progress_cancel_nfc_ttl");
//        transList.add("progress_cancel_nfc_msg");
//        transList.add("progress_support_ttl");
//        transList.add("progress_support_msg");
//
//        transList.add("alert_enable_nfc_finish_ttl");
//        transList.add("alert_enable_nfc_finish_msg");
//        transList.add("alert_cancel_nfc_finish_ttl");
//        transList.add("alert_cancel_nfc_finish_msg");
//        transList.add("alert_support_finish_ttl");
//        transList.add("alert_support_finish_msg");
//
//        transList.add("support_dialog_ttl");

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
                findViewById(R.id.act027_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act027_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                ActivityCompat.invalidateOptionsMenu(Act027_Main.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                ActivityCompat.invalidateOptionsMenu(Act027_Main.this);

            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        act027_opc = (Act027_Opc)fm.findFragmentById(R.id.act027_opc);
    }

    private void iniUIFooter() {
        iniFooter();

        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT027;
        mAct_Title = Constant.ACT027 + "_" + "title";

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

//        if (id == R.id.act11_action_settings) {
//
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

}
