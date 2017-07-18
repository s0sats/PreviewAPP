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
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
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

public class Act028_Main extends Base_Activity_Frag implements Act028_Main_View, Act028_Opc.IAct028_Opc {

    private Context context;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mDrawerStatus = true;

    private FragmentManager fm;
    private Act028_Opc act028_opc;
    private Act028_Task act028_task;

    private Bundle bundle;

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

        act028_opc = (Act028_Opc) fm.findFragmentById(R.id.act028_opc);
        //act027_opc.setOnMenuOptionsSelected(this);
        act028_opc.setData(mData);


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

    }

    @Override
    public void newExec() {

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
    public void showNewOptDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.act006_dialog_new_opt, null);
//
//        /**
//         * Ini Vars
//         */
//
//        ListView lv_opt = (ListView) view.findViewById(R.id.act006_dialog_opt_lv_opt);
//
//        String[] from = {NEW_OPT_LABEL};
//        //int[] to = {android.R.id.text1};
//        int[] to = {R.id.namoa_custom_cell_3_tv_item};
//
//
//        lv_opt.setAdapter(
//                new SimpleAdapter(
//                        context,
//                        getNewOpts(),
//                        //android.R.layout.simple_list_item_1,
//                        R.layout.namoa_custom_cell_3,
//                        from,
//                        to
//                )
//        );
//
//        /**
//         * Ini Action
//         */
//
//        lv_opt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HMAux item = (HMAux) parent.getItemAtPosition(position);
//                mPresenter.defineFlow(item);
//
//            }
//        });
//
//        builder.setTitle(hmAux_Trans.get("alert_new_opt_ttl"));
//        builder.setView(view);
//        builder.setCancelable(true);
//
//        builder.show();
    }

    private List<HMAux> getNewOpts() {
        List<HMAux> opts = new ArrayList<>();
//
//        HMAux aux =  new HMAux();
//        aux.put(NEW_OPT_ID, NEW_OPT_TP_PRODUCT);
//        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_product_lbl"));
//        opts.add(aux);
//
//        aux = new HMAux();
//        aux.put(NEW_OPT_ID, NEW_OPT_TP_SERIAL);
//        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_serial_lbl"));
//        opts.add(aux);
//
//        aux = new HMAux();
//        aux.put(NEW_OPT_ID, NEW_OPT_TP_LOCATION);
//        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_location_lbl"));
//        //opts.add(aux);

        return opts;
    }
}
