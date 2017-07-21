package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
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

public class Act028_Main extends Base_Activity_Frag implements Act028_Main_View, Act028_Opc.IAct028_Opc, Act028_Task_List.IAct028_Task_List {

    private Context context;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mDrawerStatus = true;

    private FragmentManager fm;
    private Act028_Opc act028_opc;
    private Act028_Task_List act028_task_list;
    private Act028_Task act028_task;

    private LinearLayout ll_list;
    private LinearLayout ll_task;

    private Bundle bundle;

    private HashMap<String, String> mData;

    private HMAux partnerAux = new HMAux();

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

        ll_list = (LinearLayout) findViewById(R.id.act028_main_llist);
        ll_task = (LinearLayout) findViewById(R.id.act028_main_ltask);

        act028_opc = (Act028_Opc) fm.findFragmentById(R.id.act028_opc);
        act028_opc.setOnMenuOptionsSelected(this);
        act028_opc.setData(mData);

        act028_task_list = (Act028_Task_List) fm.findFragmentById(R.id.act028_list);
        act028_task_list.setOnTaskSelected(this);

        act028_task = (Act028_Task) fm.findFragmentById(R.id.act028_lt);
        act028_task.setData(mData);

        mDrawerLayout.openDrawer(GravityCompat.START);

        controls_frags.add(act028_task);

        ll_list.setVisibility(View.GONE);
        ll_task.setVisibility(View.GONE);
    }

    private void recoverGetIntents() {
        bundle = getIntent().getExtras();

        if (bundle != null) {
            mData = (HashMap<String, String>) bundle.getSerializable("data");
        } else {
            mData = new HashMap<>();
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
    public void menuOptionsSelected(SM_SO_Service_Exec sm_so_service_exec) {

        mDrawerLayout.closeDrawer(GravityCompat.START);

        act028_task_list.setSm_so_service_exec(sm_so_service_exec);
        act028_task_list.setHMAuxScreen();
        //
        ll_list.setVisibility(View.VISIBLE);
        ll_task.setVisibility(View.GONE);
        //
        setFrag(act028_task_list, "TASK_LIST");
    }

    @Override
    public void newExec() {

        mDrawerLayout.closeDrawer(GravityCompat.START);

        showPartnerOptDialog();

    }

    @Override
    public void menuTaksSelected(HashMap<String, String> data) {
        act028_task.setData(data);
        //act028_task.setHMAuxScreen();
        //
        ll_list.setVisibility(View.GONE);
        ll_task.setVisibility(View.VISIBLE);
        //
        setFrag(act028_task, "TASK");
        //
        act028_task.setHMAuxScreen();
    }

    private <T extends Fragment> void setFrag(T type, String sTag) {
//        if (fm.findFragmentByTag(sTag) == null) {
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.act028_main_ll, type, sTag);
//            ft.commit();
//        }
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

    @Override
    public void showPartnerOptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Act028_Main.this);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_new_partner_opt, null);

        SearchableSpinner ss_partner = (SearchableSpinner) view.findViewById(R.id.act028_dialog_new_partner_opt_ss_partner);

        ss_partner.setmLabel("Selecao de Partner");
        ss_partner.setmTitle("Busca de Partner");

        MD_PartnerDao md_partnerDao = new MD_PartnerDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        final ArrayList<HMAux> partners = (ArrayList<HMAux>) md_partnerDao.query_HM(

                new MD_Partner_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );

        if (partners.size() > 0) {
            HMAux hmAux = new HMAux();
            hmAux.put("id", "0");
            hmAux.put("description", "Select a Partner");

            ss_partner.setmValue(hmAux);
        }

        ss_partner.setmOption(partners);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if (partnerAux.size() == 0) {
                    mDrawerLayout.openDrawer(GravityCompat.START);

                    mDrawerToggle.syncState();
                }

            }
        });

        final AlertDialog show = builder.show();

        ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(HMAux hmAux) {

                partnerAux.clear();

                partnerAux.putAll(hmAux);

                if (partnerAux.size() == 0) {
                    mDrawerLayout.openDrawer(GravityCompat.START);

                    mDrawerToggle.syncState();
                }

                show.dismiss();

            }
        });

    }


}
