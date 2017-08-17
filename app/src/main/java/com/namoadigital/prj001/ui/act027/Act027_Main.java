package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Results_Adapter;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.receiver.WBR_SO_Save;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Sql_009;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act028.Act028_Main;
import com.namoadigital.prj001.ui.act032.Act032_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act027_Main extends Base_Activity_Frag implements Act027_Main_View, Act027_Opc.IAct027_Opc, Act027_Services.IAct027_Services {

    public static final String WS_PROCESS_SERIAL = "WS_PROCESS_SERIAL";
    public static final String WS_PROCESS_SO_SYNC = "WS_PROCESS_SO_SYNC";
    public static final String WS_PROCESS_SO_SAVE = "WS_PROCESS_SO_SAVE";

    private Context context;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mDrawerStatus = true;

    private FragmentManager fm;
    private Act027_Opc act027_opc;
    private Act027_Services act027_services;
    private Act027_Serial act027_serial;
    private Act027_Header act027_header;

    private Bundle bundle;

    private long mCustomer_code;
    private int mSO_PREFIX;
    private int mSO_CODE;

    private SM_SODao sm_soDao;
    private HMAux data;

    private String ws_process = "";

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        //
        transList.add("alert_save_serial_return_ttl");
        transList.add("alert_no_serial_return_msg");
        transList.add("alert_save_serial_ok_msg");
        transList.add("alert_save_serial_error_msg");
        transList.add("dialog_result_product_lbl");
        transList.add("dialog_result_serial_lbl");
        transList.add("dialog_result_msg_lbl");
        //
        transList.add("alert_so_sync_ttl");
        transList.add("alert_so_sync_msg");
        transList.add("progress_so_sync_ttl");
        transList.add("progress_so_sync_msg");
        transList.add("alert_so_invalid_status_ttl");
        transList.add("alert_so_invalid_status_msg");
        transList.add("alert_so_sync_ok_ttl");
        transList.add("alert_so_sync_ok_msg");
        transList.add("alert_so_sync_param_error_ttl");
        transList.add("alert_so_sync_param_error_msg");
        transList.add("progress_so_save_ttl");
        transList.add("progress_so_save_msg");
        transList.add("alert_so_list_title");
        transList.add("msg_so_save_ok");
        transList.add("msg_starting_sync");
        transList.add("alert_results_ttl");

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

                mDrawerStatus = true;

                ActivityCompat.invalidateOptionsMenu(Act027_Main.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                mDrawerStatus = false;

                ActivityCompat.invalidateOptionsMenu(Act027_Main.this);

            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recoverGetIntents();

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        act027_opc = (Act027_Opc) fm.findFragmentById(R.id.act027_opc);
        act027_opc.setOnMenuOptionsSelected(this);
        act027_opc.setHmAux_Trans(hmAux_Trans);

        data = sm_soDao.getByStringHM(
                new SM_SO_Sql_002(
                        mCustomer_code,
                        mSO_PREFIX,
                        mSO_CODE
                ).toSqlQuery()
        );

        act027_opc.setData(data);

        act027_services = new Act027_Services();
        act027_services.setOnItemClickListener(this);
        act027_services.setHmAux_Trans(hmAux_Trans);
        act027_services.setData((ArrayList<HMAux>) sm_soDao.query_HM(
                new SM_SO_Service_Sql_003(
                        mCustomer_code,
                        mSO_PREFIX,
                        mSO_CODE
                ).toSqlQuery()
        ));

        act027_serial = new Act027_Serial();
        act027_serial.setBaInfra(this);
        act027_serial.setBundle(bundle);
        act027_serial.setHmAux_Trans(hmAux_Trans);
        act027_serial.setData(data);

        act027_header = new Act027_Header();
        act027_header.setHmAux_Trans(hmAux_Trans);
        act027_header.setData(sm_soDao.getByStringHM(
                new SM_SO_Sql_002(
                        mCustomer_code,
                        mSO_PREFIX,
                        mSO_CODE
                ).toSqlQuery()
        ));

        setFrag(act027_services, "SERVICES");

        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void recoverGetIntents() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mCustomer_code = ToolBox_Con.getPreference_Customer_Code(context);
            mSO_PREFIX = Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX, "-1"));
            mSO_CODE = Integer.parseInt(bundle.getString(SM_SODao.SO_CODE, "-1"));
        } else {
            mCustomer_code = ToolBox_Con.getPreference_Customer_Code(context);
            mSO_PREFIX = -1;
            mSO_CODE = -1;
        }
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
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        progressDialog.dismiss();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if(ws_process.equals(WS_PROCESS_SO_SYNC)){
            setWs_process("");
            processSoDownloadResult(hmAux);
            progressDialog.dismiss();
        }else if(ws_process.equals(WS_PROCESS_SO_SAVE)) {
            setWs_process("");
            processSoSave(hmAux);
        }else{
            act027_serial.callProcessSerialSaveResult(data.get(SM_SODao.PRODUCT_CODE),data.get(SM_SODao.SERIAL_ID),hmAux);
            progressDialog.dismiss();
        }
    }

    private void processSoSave(HMAux hmAux) {
        String so[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

        showResults(so);
    }

    private void showResults(String[] so) {
        ArrayList<HMAux> sos = new ArrayList<>();
        for (int i = 0; i < so.length; i++) {
            String fields[] = so[i].split(Constant.MAIN_CONCAT_STRING_2);
            //
            HMAux mHmAux = new HMAux();
            mHmAux.put("label", fields[0]);
            mHmAux.put("status", fields[1]);
            mHmAux.put("final_status", fields[0] + " / " + fields[1]);
            //
            sos.add(mHmAux);
        }

        if (sos.size() == 1) {

            if(sos.get(0).get("status").equalsIgnoreCase("Ok")){
                ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_so_save_ok"), "", "0");
                //
                ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_starting_sync"), "", "0");
                //
                executeSoSync(mSO_PREFIX,mSO_CODE);
            }else{
                progressDialog.dismiss();

                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_so_list_title"),
                        sos.get(0).get("status"),
                        null,
                        0
                );

            }

        } else {
            showNewOptDialog(sos);
        }
    }

    public void showNewOptDialog(List<HMAux> sos) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        lv_results.setAdapter(
                new Act028_Results_Adapter(
                        context,
                        R.layout.act028_results_adapter_cell,
                        sos
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
                executeSoSync(mSO_PREFIX,mSO_CODE);
            }
        });
    }

    private void processSoDownloadResult(HMAux so_download_result) {
        if (so_download_result.containsKey(WS_SO_Search.SO_PREFIX_CODE) && so_download_result.containsKey(WS_SO_Search.SO_LIST_QTY)) {
            if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 0) {
                //
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_so_invalid_status_ttl"),
                        hmAux_Trans.get("alert_so_invalid_status_msg"),
                        null,
                        0
                );

            }else if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 1) {
                //
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_so_sync_ok_ttl"),
                        hmAux_Trans.get("alert_so_sync_ok_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                refreshUI();
                            }
                        },
                        0
                );

            } else {
                //
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_so_sync_ok_ttl"),
                        hmAux_Trans.get("alert_so_sync_ok_msg"),
                        null,
                        0
                );
            }
        }else{
           // ToolBox_Inf.alertBundleNotFound(this,hmAux_Trans);
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_so_sync_param_error_ttl"),
                    hmAux_Trans.get("alert_so_sync_param_error_msg"),
                    null,
                    0
            );
        }

    }

    private void refreshUI() {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        progressDialog.dismiss();
       /* switch (act027_serial.requesting_process) {

            case Constant.MODULE_CHECKLIST:
                //mView.callAct008(context,product_code);
                break;

            case Constant.MODULE_SO:
            case Constant.MODULE_SO_SEARCH_SERIAL:
                if (act027_serial.ws_process.equals(WS_SEARCH_SERIAL)) {
                    //
                    act027_serial.getSerialInfo();
                }
                break;
            default:
                break;

        }
        */

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
    public void menuOptionsSelected(String type) {
        switch (type.toUpperCase()) {
            case "SERVICES":
                setFrag(act027_services, "SERVICES");
                break;
            case "SERIAL":
                setFrag(act027_serial, "SERIAL");
                break;
            case "HEADER":
                setFrag(act027_header, "HEADER");
                break;
            case "APPROVAL":
                callAct032(context, bundle);
                break;
            default:
                setFrag(act027_header, "HEADER");
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void soSyncClick() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_so_sync_ttl"),
                hmAux_Trans.get("alert_so_sync_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Seta S.O como update required.
                        sm_soDao.addUpdate(
                                new SM_SO_Sql_009(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        mSO_PREFIX,
                                        mSO_CODE
                                ).toSqlQuery()
                        );
                        //
                        executeSoSave();
                    }
                },
                1
        );
    }

    private void executeSoSave() {
        setWs_process(WS_PROCESS_SO_SAVE);
        //
        enableProgressDialog(
                hmAux_Trans.get("progress_so_save_ttl"),
                hmAux_Trans.get("progress_so_save_msg"),
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Save.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }


    public void executeSoSync(int so_prefix, int so_code) {
        setWs_process(WS_PROCESS_SO_SYNC);
        //
        if(progressDialog!= null &&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        //
        enableProgressDialog(
                hmAux_Trans.get("progress_so_sync_ttl"),
                hmAux_Trans.get("progress_so_sync_msg"),
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Search.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SO_SEARCH_SO_MULT,so_prefix+"."+so_code);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);

    }

    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }

    @Override
    public void onItemClickListener(HMAux type) {

        bundle.putSerializable("data", type);

        callAct028(context, bundle);

    }

    private <T extends Fragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act027_main_ll, type, sTag);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerStatus) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
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
        bundle.putSerializable("data", data);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

}
