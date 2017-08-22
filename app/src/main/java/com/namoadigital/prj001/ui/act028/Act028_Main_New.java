package com.namoadigital.prj001.ui.act028;

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

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Results_Adapter;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_001;
import com.namoadigital.prj001.ui.act027.Act027_Main_New;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neomatrix on 18/08/17.
 */

public class Act028_Main_New extends Base_Activity_Frag implements Act028_Opc_New.IAct028_Opc, Act028_Task_List_New.IAct028_Task_List, Act028_Task_New.IAct028_Task {

    public static final String SELECTION_EMPTY = "EMPTY";
    public static final String SELECTION_TASK_LIST = "TASK_LIST";
    public static final String SELECTION_TASK = "TASK";

    private Context context;
    private Bundle bundle;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean mDrawerStatus = true;

    private FragmentManager fm;
    private Act028_Empty_New act028_empty_new;
    private Act028_Opc_New act028_opc;
    private Act028_Task_List_New act028_task_list;

    private Act028_Task act028_task;

    private SM_SO_Service mService;
    private SM_SO_Service_Exec mExec;
    private SM_SO_Service_Exec_Task mTask;

    private SM_SO_ServiceDao sm_so_serviceDao;
    private SM_SO_Service_ExecDao sm_so_service_execDao;
    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;

    private HMAux partnerAux = new HMAux();

    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act028_main_new);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
    }

    private void iniSetup() {
        context = Act028_Main_New.this;

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
        transList.add("alert_service_list_title");
        transList.add("alert_service_list_msg");
        transList.add("alert_so_list_title");
        transList.add("alert_so_list_msg");
        transList.add("alert_task_title");
        transList.add("alert_task_msg");
        transList.add("ss_partner_list_ttl");
        transList.add("ss_partner_list_search_ttl");
        transList.add("btn_new_exec");
        transList.add("btn_new_task");
        transList.add("btn_cancel_task");
        transList.add("task_title_error");
        transList.add("no_exec_selected_lbl");
        transList.add("alert_exec_blocked_title");
        transList.add("alert_exec_blocked_msg");
        transList.add("alert_results_ttl");
        //
        transList.add("exec_code_lbl");
        transList.add("partner_lbl");
        transList.add("service_lbl");

        sm_so_serviceDao = new SM_SO_ServiceDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_execDao = new SM_SO_Service_ExecDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
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
                findViewById(R.id.act028_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act028_Main_New.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                mDrawerStatus = true;

                ActivityCompat.invalidateOptionsMenu(Act028_Main_New.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                mDrawerStatus = false;

                ActivityCompat.invalidateOptionsMenu(Act028_Main_New.this);

            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recoverGetIntents();

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        act028_opc = (Act028_Opc_New) fm.findFragmentById(R.id.act028_opc);
        act028_opc.setOnMenuOptionsSelected(this);
        act028_opc.setmService(mService);
        act028_opc.setHmAux_Trans(hmAux_Trans);

        act028_empty_new = new Act028_Empty_New();
        act028_empty_new.setHmAux_Trans(hmAux_Trans);

        act028_task_list = new Act028_Task_List_New();
        act028_task_list.setBaInfra(this);
        act028_task_list.setOnTaskSelected(this);
        act028_task_list.setHmAux_Trans(hmAux_Trans);

//        act028_task = (Act028_Task) fm.findFragmentById(R.id.act028_lt);
//        act028_task.setBaInfra(this);
//        act028_task.setData(mData);
//        act028_task.setOnExec_List_Opc_Update(this);
//        act028_task.setHmAux_Trans(hmAux_Trans);

        mDrawerLayout.openDrawer(GravityCompat.START);

        controls_frags.add(act028_task);

        //tv_no_exec_selected.setText(hmAux_Trans.get("no_exec_selected_lbl"));

        setFrag(act028_empty_new, SELECTION_EMPTY);
    }

    //region Recover Intent Parameters
    private void recoverGetIntents() {
        bundle = getIntent().getExtras();

        if (bundle != null) {

            mService = loadService(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX)),
                    Integer.parseInt(bundle.getString(SM_SODao.SO_CODE)),
                    Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE)),
                    Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PACK_CODE)),
                    Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PACK_SEQ)),
                    Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE)),
                    Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.SERVICE_CODE)),
                    Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ))
            );

            if (bundle.getString(SM_SO_Service_Exec_TaskDao.EXEC_TMP) != null) {
                mExec = loadExec(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX)),
                        Integer.parseInt(bundle.getString(SM_SODao.SO_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PACK_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PACK_SEQ)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.SERVICE_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ)),
                        Long.parseLong(bundle.getString(SM_SO_Service_Exec_TaskDao.EXEC_TMP))
                );
            } else {
                mExec = null;
            }

            if (bundle.getString(SM_SO_Service_Exec_TaskDao.TASK_TMP) != null) {
                mTask = loadTask(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX)),
                        Integer.parseInt(bundle.getString(SM_SODao.SO_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PACK_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.PACK_SEQ)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.SERVICE_CODE)),
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ)),
                        Long.parseLong(bundle.getString(SM_SO_Service_Exec_TaskDao.EXEC_TMP)),
                        Long.parseLong(bundle.getString(SM_SO_Service_Exec_TaskDao.TASK_TMP))
                );
            } else {
                mTask = null;
            }

        } else {
            mService = null;
            mExec = null;
            mTask = null;
        }
    }

    /**
     * Load Service
     *
     * @param customer_code
     * @param so_prefix
     * @param so_code
     * @param price_list_code
     * @param pack_code
     * @param pack_seq
     * @param category_price_code
     * @param service_code
     * @param service_seq
     * @return
     */
    private SM_SO_Service loadService(long customer_code,
                                      int so_prefix,
                                      int so_code,
                                      int price_list_code,
                                      int pack_code,
                                      int pack_seq,
                                      int category_price_code,
                                      int service_code,
                                      int service_seq) {

        SM_SO_Service mSo_service = sm_so_serviceDao.getByString(
                new SM_SO_Service_Sql_001(
                        customer_code,
                        so_prefix,
                        so_code,
                        price_list_code,
                        pack_code,
                        pack_seq,
                        category_price_code,
                        service_code,
                        service_seq
                ).toSqlQuery()
        );

        return mSo_service;
    }

    /**
     * Load Exec
     *
     * @param customer_code
     * @param so_prefix
     * @param so_code
     * @param price_list_code
     * @param pack_code
     * @param pack_seq
     * @param category_price_code
     * @param service_code
     * @param service_seq
     * @param exec_tmp
     * @return
     */
    private SM_SO_Service_Exec loadExec(long customer_code,
                                        int so_prefix,
                                        int so_code,
                                        int price_list_code,
                                        int pack_code,
                                        int pack_seq,
                                        int category_price_code,
                                        int service_code,
                                        int service_seq,
                                        long exec_tmp) {

        SM_SO_Service_Exec mSo_service_exec = sm_so_service_execDao.getByString(
                new SM_SO_Service_Exec_Sql_004(
                        customer_code,
                        so_prefix,
                        so_code,
                        price_list_code,
                        pack_code,
                        pack_seq,
                        category_price_code,
                        service_code,
                        service_seq,
                        exec_tmp
                ).toSqlQuery()
        );

        return mSo_service_exec;
    }

    /**
     * Load Task
     *
     * @param customer_code
     * @param so_prefix
     * @param so_code
     * @param price_list_code
     * @param pack_code
     * @param pack_seq
     * @param category_price_code
     * @param service_code
     * @param service_seq
     * @param exec_tmp
     * @param task_tmp
     * @return
     */
    private SM_SO_Service_Exec_Task loadTask(long customer_code,
                                             int so_prefix,
                                             int so_code,
                                             int price_list_code,
                                             int pack_code,
                                             int pack_seq,
                                             int category_price_code,
                                             int service_code,
                                             int service_seq,
                                             long exec_tmp,
                                             long task_tmp) {

        SM_SO_Service_Exec_Task mSm_so_service_exec_task = sm_so_service_exec_taskDao.getByString(
                new SM_SO_Service_Exec_Task_Sql_005(
                        customer_code,
                        so_prefix,
                        so_code,
                        price_list_code,
                        pack_code,
                        pack_seq,
                        category_price_code,
                        service_code,
                        service_seq,
                        exec_tmp,
                        task_tmp
                ).toSqlQuery()
        );

        return mSm_so_service_exec_task;
    }
    //endregion

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

    private <T extends Fragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act028_main_ll, type, sTag);
            ft.commit();
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
        if (index == 1) {
            index = 0;
            //
            // Mudar
//            ll_list.setVisibility(View.VISIBLE);
//            ll_task.setVisibility(View.GONE);
            //
            act028_task.updateTaskOnLeave();
            //
//            act028_task_list.setHMAuxScreen();
        } else {
            if (mDrawerStatus) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_service_list_title"),
                        hmAux_Trans.get("alert_service_list_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bundle.remove("data");
                                //
                                Intent mIntent = new Intent(context, Act027_Main_New.class);
                                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mIntent.putExtras(bundle);
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
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        String so[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

        String so_current_reload = hmAux.get(String.valueOf(mService.getSo_prefix()) + "." + String.valueOf(mService.getSo_code()));

        if (so != null) {
            disableProgressDialog();
            //
            showResults(so, so_current_reload);
        } else {
            refreshUI();
        }

    }

    private void showResults(String[] so, String so_current_reload) {
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

        if (sos.size() == 1 && sos.get(0).get("status").equalsIgnoreCase("Ok")) {
            if (so_current_reload.equalsIgnoreCase("1")) {
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_so_list_title"),
                        hmAux_Trans.get("alert_so_list_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bundle.remove("data");
                                //
                                Intent mIntent = new Intent(context, Act027_Main_New.class);
                                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mIntent.putExtras(bundle);
                                //
                                startActivity(mIntent);
                                finish();
                            }
                        },
                        -1,
                        false
                );
            } else {
                return;
            }
        } else {
            showNewOptDialog(sos, so_current_reload);
        }
    }

    public void showNewOptDialog(List<HMAux> sos, final String so_current_reload) {

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

        String[] from = {"final_status"};
        int[] to = {R.id.namoa_custom_cell_3_tv_item};


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

                refreshUI();

                if (so_current_reload.equalsIgnoreCase("1")) {

                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_so_list_title"),
                            hmAux_Trans.get("alert_so_list_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    show.dismiss();

                                    bundle.remove("data");
                                    //
                                    Intent mIntent = new Intent(context, Act027_Main_New.class);
                                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mIntent.putExtras(bundle);
                                    //
                                    startActivity(mIntent);
                                    finish();
                                }
                            },
                            -1,
                            false
                    );

                } else {
                    show.dismiss();
                }
            }
        });
    }

    private void refreshUI() {
        if (index == 0) {
            disableProgressDialog();
        } else {
            index = 0;
            //
            // Mudar
//            ll_list.setVisibility(View.VISIBLE);
//            ll_task.setVisibility(View.GONE);
            //
            //act028_task_list.setHMAuxScreen();
            //
            if (progressDialog != null && progressDialog.isShowing()) {
                disableProgressDialog();
            }
        }
    }

//    private void refreshUI() {
//        mSm_so = loadSM_SO(
//                mSm_so.getCustomer_code(),
//                mSm_so.getSo_prefix(),
//                mSm_so.getSo_code()
//        );
//
//        act027_opc_new.setmSm_so(mSm_so);
//        act027_opc_new.loadDataToScreen();
//
//        act027_services_new.setmSm_so(mSm_so);
//        act027_services_new.loadDataToScreen();
//
//        act027_serial_new.setmSm_so(mSm_so);
//        act027_serial_new.loadDataToScreen();
//
//        act027_header_new.setmSm_so(mSm_so);
//        act027_header_new.loadDataToScreen();
//    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        refreshUI();

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

    public void showPartnerOptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Act028_Main_New.this);

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

        final AlertDialog show = builder.show();

        ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(HMAux hmAux) {

                partnerAux.clear();

                partnerAux.putAll(hmAux);

                show.dismiss();
            }
        });

    }

    @Override
    public void exec_list_opc_update(String sFlag) {

    }

    @Override
    public void menuTaksSelected(HashMap<String, String> data) {

    }

    @Override
    public void exec_task_tmp(String exec_tmp, String task_tmp) {

    }

    @Override
    public void menuOptionsSelected(SM_SO_Service_Exec sm_so_service_exec, String full_status) {

        mDrawerLayout.closeDrawer(GravityCompat.START);

        act028_task_list.setSm_so_service_exec(sm_so_service_exec, full_status);
        act028_task_list.loadDataToScreen();
        //
        index = 0;
        //
        setFrag(act028_task_list, "TASK_LIST");
    }

    @Override
    public void newExec() {
    }

    //region Drawer Visibility
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.openDrawer(GravityCompat.START);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mDrawerStatus = true;

            mDrawerToggle.syncState();

        } else {

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerLayout.closeDrawer(GravityCompat.START);

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);

            mDrawerStatus = false;

            mDrawerToggle.setDrawerIndicatorEnabled(false);

            mDrawerToggle.syncState();
        }
    }
    //endregion


}
