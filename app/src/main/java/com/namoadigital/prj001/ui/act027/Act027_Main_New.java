package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act028.Act028_Main;
import com.namoadigital.prj001.ui.act032.Act032_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by neomatrix on 14/08/17.
 */

public class Act027_Main_New extends Base_Activity_Frag implements Act027_Main_View, Act027_Opc_New.IAct027_Opc, Act027_Services_New.IAct027_Services {

    public static final String SELECTION_SERVICES = "SERVICES";
    public static final String SELECTION_SERIAL = "SERIAL";
    public static final String SELECTION_HEADER = "HEADER";
    public static final String SELECTION_APPROVAL = "APPROVAL";

    public static final String SELECTION_EXPRESS = "EXPRESS";
    public static final String SELECTION_NORMAL = "NORMAL";

    private Context context;
    private Bundle bundle;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager fm;

    private Act027_Opc_New act027_opc_new;
    private Act027_Services_New act027_services_new;
    private Act027_Serial_New act027_serial_new;
    private Act027_Header_New act027_header_new;

    private SM_SODao sm_soDao;
    private SM_SO sm_so;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act027_main_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
    }

    private void iniSetup() {
        context = Act027_Main_New.this;

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
        transList.add("act027_title");
        transList.add("alert_so_exit_title");
        transList.add("alert_so_exit_msg");

        // ACT027_Opc Fragment
        transList.add("so_lbl");
        transList.add("so_id_lbl");
        transList.add("so_code_lbl");
        transList.add("product_lbl");
        transList.add("product_id_lbl");
        transList.add("product_header_lbl");
        transList.add("product_id_header_lbl");

        transList.add("product_description_lbl");
        transList.add("serial_lbl");
        transList.add("deadline_lbl");
        transList.add("status_lbl");
        transList.add("priority_lbl");
        transList.add("services_ll_lbl");
        transList.add("serial_ll_lbl");
        transList.add("header_ll_lbl");

        // ACT027_Serial Fragment
        transList.add("alert_no_connection_title");
        transList.add("alert_no_connection_msg");
        transList.add("alert_offine_mode_title");
        transList.add("alert_offine_mode_msg");
        transList.add("alert_start_sync_title");
        transList.add("alert_start_sync_msg");
        transList.add("alert_start_serial_title");
        transList.add("alert_start_serial_msg");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("alert_no_serial_typed_title");
        transList.add("alert_no_serial_typed_msg");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        transList.add("product_ttl");
        transList.add("mket_search_hint");
        transList.add("product_label");
        transList.add("product_id_label");
        transList.add("alert_no_form_for_operation_ttl");
        transList.add("alert_no_form_for_operation_msg");
        transList.add("btn_create");
        transList.add("serial_ttl");
        transList.add("serial_location_ttl");
        transList.add("site_lbl");
        transList.add("site_zone_lbl");
        transList.add("site_zone_local_lbl");
        transList.add("serial_add_info_ttl");
        transList.add("add_info1_lbl");
        transList.add("add_info2_lbl");
        transList.add("add_info3_lbl");
        transList.add("serial_properties_ttl");
        transList.add("brand_lbl");
        transList.add("brand_model_lbl");
        transList.add("brand_color_lbl");
        transList.add("segment_lbl");
        transList.add("category_price_lbl");
        transList.add("site_owner_lbl");
        transList.add("btn_serial_search");
        transList.add("btn_so_search");
        transList.add("progress_so_search_ttl");
        transList.add("progress_so_search_msg");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("alert_no_so_found_ttl");
        transList.add("alert_no_so_found_msg");
        transList.add("alert_save_serial_error_ttl");
        transList.add("alert_save_serial_error_msg");
        transList.add("btn_serial_save");

        // ACT027_Header Fragment
        transList.add("so_id");
        transList.add("so_desc");
        transList.add("prefix_code");
        transList.add("serial");
        transList.add("category_price_id");
        transList.add("category_price_desc");
        transList.add("segment_id");
        transList.add("segment_desc");
        transList.add("site_id");
        transList.add("site_desc");
        transList.add("operation_id");
        transList.add("operation_desc");
        transList.add("deadline");
        transList.add("status");
        transList.add("priority_desc");
        transList.add("contract_desc");
        transList.add("contract_po_erp");
        transList.add("contract_po_client1");
        transList.add("contract_po_client2");
        transList.add("quality_approval_user");
        transList.add("quality_approval_user_nick");
        transList.add("quality_approval_date");
        transList.add("comments");
        transList.add("client_type");
        transList.add("client_user");
        transList.add("client_code");
        transList.add("client_id");
        transList.add("client_name");
        transList.add("client_email");
        transList.add("client_phone");
        transList.add("client_approval_date");
        transList.add("client_approval_user");
        transList.add("client_approval_user_nick");
        transList.add("total_qty_service");
        transList.add("total_price");
        transList.add("alert_no_data_changes_ttl");
        transList.add("alert_no_data_changes_msg");
        transList.add("progress_save_serial_ttl");
        transList.add("progress_save_serial_msg");
        transList.add("searchable_spinner_lbl");

        sm_soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        ToolBox_Inf.libTranslation(context);
    }

    private void initVars() {
        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.act027_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act027_Main_New.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                act027_opc_new.loadDataToScreen();

                ActivityCompat.invalidateOptionsMenu(Act027_Main_New.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                ActivityCompat.invalidateOptionsMenu(Act027_Main_New.this);

            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recoverGetIntents();

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // Drawer Opc
        act027_opc_new = (Act027_Opc_New) fm.findFragmentById(R.id.act027_opc_new);
        // Dialog Acess
        act027_opc_new.setBaInfra(this);
        // Translation Access
        act027_opc_new.setHmAux_Trans(hmAux_Trans);
        // SO Acess
        act027_opc_new.setmSm_so(sm_so);
        //
        act027_opc_new.setOnMenuOptionsSelected(this);
        // Services
        act027_services_new = new Act027_Services_New();
        // Dialog Acess
        act027_services_new.setBaInfra(this);
        // Translation Access
        act027_services_new.setHmAux_Trans(hmAux_Trans);
        // SO Acess
        act027_services_new.setmSm_so(sm_so);

        // Serial
        act027_serial_new = new Act027_Serial_New();
        // Dialog Acess
        act027_serial_new.setBaInfra(this);
        // Translation Access
        act027_serial_new.setHmAux_Trans(hmAux_Trans);
        // SO Acess
        act027_serial_new.setmSm_so(sm_so);

        // header
        act027_header_new = new Act027_Header_New();
        // Dialog Acess
        act027_header_new.setBaInfra(this);
        // Translation Access
        act027_header_new.setHmAux_Trans(hmAux_Trans);
        // SO Acess
        act027_header_new.setmSm_so(sm_so);

        setFrag(act027_services_new, "SERVICES");
    }

    private void recoverGetIntents() {
        bundle = getIntent().getExtras();

        if (bundle != null) {
            sm_so = loadSM_SO(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX)),
                    Integer.parseInt(bundle.getString(SM_SODao.SO_CODE))
            );
        } else {
            sm_so = null;
        }
    }

    private SM_SO loadSM_SO(long customer_code, int so_prefix, int so_code) {
        SM_SO mSm_so = sm_soDao.getByString(
                new SM_SO_Sql_001(
                        customer_code,
                        so_prefix,
                        so_code
                ).toSqlQuery()
        );

        return sm_so;
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

    //region SERVICE RETURN HANDLING
    // TRATAVIA DE ERROS ESPECIFICOS
    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        progressDialog.dismiss();
    }

    // TRATAVIA DE ENCERRAMENTO SEM PROBLEMAS DO SERVICO
    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
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

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act027_main_ll, type, sTag);
            ft.commit();
        } else {
            //type.loadDataToScreen();
        }
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
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_so_exit_title"),
                hmAux_Trans.get("alert_so_exit_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mIntent = new Intent(context, Act005_Main.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //
                        startActivity(mIntent);
                        finish();

                    }
                },
                1,
                false
        );
    }

    @Override
    public void menuOptionsSelected(String type) {
        switch (type.toUpperCase()) {
            case Act027_Main_New.SELECTION_SERVICES:
                setFrag(act027_services_new, Act027_Main_New.SELECTION_SERVICES);
                break;
            case Act027_Main_New.SELECTION_SERIAL:
                setFrag(act027_serial_new, Act027_Main_New.SELECTION_SERIAL);
                break;
            case Act027_Main_New.SELECTION_HEADER:
                setFrag(act027_header_new, Act027_Main_New.SELECTION_HEADER);
                break;
            case Act027_Main_New.SELECTION_APPROVAL:
                callAct032(context, bundle);
                break;
            default:
                setFrag(act027_header_new, Act027_Main_New.SELECTION_HEADER);
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onServiceSelected(HMAux sService) {
//        bundle.putSerializable("data", sService);
//        callAct028(context, bundle);

        Toast.makeText(
                context,
                "Normal",
                Toast.LENGTH_SHORT
        ).show();

    }

    @Override
    public void callAct028(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act028_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);

        finish();
    }

    private void callAct032(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act032_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);

        finish();
    }


}
