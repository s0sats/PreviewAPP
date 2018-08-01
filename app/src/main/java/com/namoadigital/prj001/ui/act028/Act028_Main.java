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
import android.util.Log;
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
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.receiver.WBR_SO_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_003;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Sql_SS_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_006;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Sql_009;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.ui.act009.Act009_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by neomatrix on 18/08/17.
 */

public class Act028_Main extends Base_Activity_Frag implements Act028_Opc.IAct028_Opc, Act028_Task_List.IAct028_Task_List, Act028_Task.IAct028_Task {

    public static final String SELECTION_EMPTY = "EMPTY";
    public static final String SELECTION_TASK_LIST = "TASK_LIST";
    public static final String SELECTION_TASK = "TASK";

    public static final String CREATE_TASK = "CREATE_TASK";
    public static final String CREATE_SAVE = "CREATE_SAVE";
    public static final String CREATE_NULL = "CREATE_NULL";
    public static final String CREATE_NOT_EXEC = "CREATE_NOT_EXEC";
    //Var para controle de chamada do WS de Sync Form
    public static final String WS_PROCESS_N_FORM_SYNC = "WS_PROCESS_N_FORM_SYNC";

    public String full_status;

    private Context context;
    private Bundle bundle;

    private DrawerLayout mDrawerLayout;
    //private ActionBarDrawerToggle mDrawerToggle;

    private boolean mDrawerStatus = true;
    private boolean mShortCut = false;
    private boolean mTaskCall = false;

    public void setmTaskCall(boolean mTaskCall) {
        this.mTaskCall = mTaskCall;
    }

    private String MTASK_STATUS;

    private FragmentManager fm;
    private Act028_Empty act028_empty_;
    private Act028_Opc act028_opc;
    private Act028_Task_List act028_task_list;
    private Act028_Task act028_task;

    private SM_SO_Service mService;
    private SM_SO_Service_Exec mExec;
    private SM_SO_Service_Exec mExec_Aux;
    private SM_SO_Service_Exec_Task mTask;

    private SM_SODao sm_soDao;
    private SM_SO_ServiceDao sm_so_serviceDao;
    private SM_SO_Service_ExecDao sm_so_service_execDao;
    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;
    private Sync_ChecklistDao syncChecklistDao;

    private HMAux partnerAux = new HMAux();

    private int index = 0;

    private String ws_process = "";
    private String wsSoProcess;

    private ArrayList<HMAux> wsResults = new ArrayList<>();

    public void setWsSoProcess(String wsSoProcess) {
        this.wsSoProcess = wsSoProcess;
    }

    private int original_update_required;
    //Profile de EXECUTION
    private boolean executionProfile = false;
    //Variavel utilizada para validações de N-Service e criação do N-Form
    private HMAux mSoAux = new HMAux();
    private String so_status;
    //N-Form, seleção de produto
    private MD_ProductDao productDao;
    private ArrayList<HMAux> nFormProductList = new ArrayList<>();
    private HMAux nFormProductSelected = new HMAux();

    public void setMTASK_STATUS(String MTASK_STATUS) {
        this.MTASK_STATUS = MTASK_STATUS;
    }

    public int getOriginal_update_required() {
        return original_update_required;
    }

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
        transList.add("alert_task_lost_data_ttl");
        transList.add("alert_task_lost_data_msg");
        transList.add("alert_partner_selection_ttl");
        transList.add("alert_partner_selection_msg");
        transList.add("alert_cancel_task_ttl");
        transList.add("alert_cancel_task_msg");
        transList.add("task_code_lbl");
        //
        transList.add("exec_code_lbl");
        transList.add("partner_lbl");
        transList.add("service_lbl");
        //
        transList.add("exec_task_lbl");
        transList.add("additional_info_lbl");
        //
        transList.add("dialog_so_lbl");
        transList.add("dialog_service_lbl");
        transList.add("dialog_product_lbl");
        transList.add("dialog_serial_lbl");
        transList.add("dialog_tracking_lbl");
        transList.add("dialog_info_ttl");
        //
        transList.add("toolbar_info_lbl");
        transList.add("alert_unsaved_data_will_be_lost");
        //
        transList.add("dialog_form_prod_selection_ttl");
        transList.add("dialog_form_prod_ss_product_lbl");
        transList.add("dialog_form_prod_ss_product_search_ttl");
        transList.add("dialog_form_prod_serial_lbl");

        sm_soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

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

        syncChecklistDao = new Sync_ChecklistDao(
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
        //
        executionProfile = ToolBox_Inf.profileExists(
                context,
                Constant.PROFILE_MENU_SO,
                Constant.PROFILE_MENU_SO_PARAM_EXECUTION
        );
        //
        act028_opc = (Act028_Opc) fm.findFragmentById(R.id.act028_opc);
        act028_opc.setOnMenuOptionsSelected(this);
        act028_opc.setmService(mService);
        act028_opc.setHmAux_Trans(hmAux_Trans);

        full_status = act028_opc.verificarStatus_SO(
                mService.getSo_prefix(),
                mService.getSo_code(),
                mService
        ) ? "1" : "0";

        act028_empty_ = new Act028_Empty();
        act028_empty_.setHmAux_Trans(hmAux_Trans);

        act028_task_list = new Act028_Task_List();
        act028_task_list.setBaInfra(this);
        act028_task_list.setOnTaskSelected(this);
        act028_task_list.setHmAux_Trans(hmAux_Trans);

        act028_task = new Act028_Task();
        act028_task.setBaInfra(this);
        act028_task.setHmAux_Trans(hmAux_Trans);

        controls_frags.add(act028_task);

        if (mShortCut) {
            act028_task.setmService(mService);
            act028_task.setmTask(mTask);
            act028_task.setFull_status(full_status);
            setFrag(act028_task, SELECTION_TASK);
        } else {
            setFrag(act028_empty_, SELECTION_EMPTY);
        }
        //
        //N-Form via N-Service
        productDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //carrega HmAux com list de produtos que o user
        loadNFormProductList();
        //
        resetHmAuxProdutcSelected();
    }

    public boolean hasExecutionProfile() {
        return executionProfile;
    }

    //region Recover Intent Parameters
    private void recoverGetIntents() {
        bundle = getIntent().getExtras();

        if (bundle != null) {

            mShortCut = bundle.getBoolean(Constant.ACT027_IS_SHORTCUT);

            if (bundle.containsKey(Constant.ACT027_ORIGINAL_UPDATE_REQUIRED)) {
                original_update_required = bundle.getInt(Constant.ACT027_ORIGINAL_UPDATE_REQUIRED);
            }

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
            //
            mSoAux = sm_soDao.getByStringHM(
                    new SM_SO_Sql_002(
                            mService.getCustomer_code(),
                            mService.getSo_prefix(),
                            mService.getSo_code()
                    ).toSqlQuery()
            );
            //Força DONE se null, para não exibir o menu para N-form
            so_status = mSoAux != null ? mSoAux.get(SM_SODao.STATUS) : Constant.SYS_STATUS_DONE;
            //
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
                        Integer.parseInt(bundle.getString(SM_SO_Service_Exec_TaskDao.EXEC_TMP))
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
            //Força DONE, para não exibir o menu para N-form
            so_status = Constant.SYS_STATUS_DONE;
            mService = null;
            mExec = null;
            mTask = null;
        }

        if (mShortCut) {
            setDrawerState(false);
        } else {
            setDrawerState(true);
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
                                        int exec_tmp) {

        SM_SO_Service_Exec mSo_service_exec = sm_so_service_execDao.getByString(
                new SM_SO_Service_Exec_Sql_006(
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

    private <T extends Fragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act028_main_ll, type, sTag);
            ft.commit();
        }
    }


    @Override
    public void onBackPressed() {
        if (mShortCut) {

            String title = "";
            String message = "";


            if (mService.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_YES_NO) &&
                    mTask.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS)
                    ) {

                title = hmAux_Trans.get("alert_task_lost_data_ttl");
                message = hmAux_Trans.get("alert_task_lost_data_msg");

            } else {

                title = hmAux_Trans.get("alert_service_list_title");
                message = hmAux_Trans.get("alert_service_list_msg");

            }

            ToolBox.alertMSG(
                    context,
                    title,
                    message,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bundle.remove("data");
                            //
                            if (mService.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
                                act028_task.updateTaskOnLeave();
                            } else {
                                act028_task.removeTaskOnLeave();
                            }
                            //
                            callAct027();
                        }
                    },
                    1,
                    false
            );
        } else {
            if (index == 1) {
                if (mService.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
                    index = 0;

                    mTask = null;

                    act028_task.updateTaskOnLeave();
                    act028_opc.loadDataToScreen();

                    setFrag(act028_task_list, SELECTION_TASK_LIST);
                } else {
                    String user_code = ToolBox_Con.getPreference_User_Code(context);

                    if (mTask != null && String.valueOf(mTask.getTask_user()).equalsIgnoreCase(user_code)) {

                        if (mService.getExec_type().equalsIgnoreCase(Constant.SO_SERVICE_TYPE_YES_NO)
                                && mTask.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS)) {

                            ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_task_lost_data_ttl"),
                                    hmAux_Trans.get("alert_task_lost_data_msg"),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            index = 0;

                                            mDrawerStatus = true;
                                            mDrawerLayout.openDrawer(GravityCompat.START);
                                            //
                                            act028_task.removeTaskOnLeave();
                                            act028_opc.loadDataToScreen();
                                            //
                                            mExec = null;
                                            mTask = null;
                                            //
                                            setFrag(act028_empty_, SELECTION_EMPTY);
                                        }
                                    },
                                    1,
                                    false
                            );

                        } else {
                            index = 0;

                            mTask = null;

                            setFrag(act028_task_list, SELECTION_TASK_LIST);
                        }
                    } else {
                        index = 0;

                        mTask = null;

                        setFrag(act028_task_list, SELECTION_TASK_LIST);
                    }
                }
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
                                    if (mService.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
                                        act028_task.updateTaskOnLeave();
                                    } else {
                                        act028_task.removeTaskOnLeave();
                                    }

                                    callAct027();
                                }
                            },
                            1,
                            false
                    );
                }
            }
        }
    }

    public void executeSerialSave() {
        setWsSoProcess(WS_Serial_Save.class.getSimpleName());

        cleanUpResults();

        if (ToolBox_Con.isOnline(context)) {
            setWsSoProcess(WS_Serial_Save.class.getSimpleName());

            enableProgressDialog(
                    hmAux_Trans.get("alert_task_title"),
                    hmAux_Trans.get("alert_so_list_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );

            Intent mIntent = new Intent(context, WBR_Serial_Save.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            executeSoSave(true);
        }
    }

    public void executeSoSave(boolean jumpOnlineExecution) {
        setWsSoProcess(WS_SO_Save.class.getSimpleName());

        if (ToolBox_Con.isOnline(context) && !jumpOnlineExecution) {

            Intent mIntent = new Intent(context, WBR_SO_Save.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);
            mIntent.putExtras(bundle);

            context.sendBroadcast(mIntent);

            activateUpload(context);
        } else {
            if (mTaskCall) {
                mTaskCall = false;
                offLineProcess();
            }
        }
    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        if (mTaskCall) {
            mTaskCall = false;
            offLineProcess();
        }
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (wsSoProcess.equalsIgnoreCase(WS_Serial_Save.class.getSimpleName())) {
            setWsSoProcess("");

            if (!hmAux.isEmpty() && hmAux.size() > 0) {
                for (Map.Entry<String, String> item : hmAux.entrySet()) {
                    HMAux aux = new HMAux();
                    String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                    String status = item.getValue();
                    String productInfo = getProductInfo(Long.parseLong(pk[0]));
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", "" + productInfo + " - " + pk[1]);
                    mHmAux.put("type", "SERIAL");
                    mHmAux.put("status", status);
                    mHmAux.put("final_status", productInfo + " - " + pk[1] + " / " + status);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                    }
                }
            }

            executeSoSave(false);

        } else if (wsSoProcess.equalsIgnoreCase(WS_SO_Save.class.getSimpleName())) {
            setWsSoProcess("");

            String so[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            String so_current_reload = hmAux.get(String.valueOf(mService.getSo_prefix()) + "." + String.valueOf(mService.getSo_code()));

            if (so != null) {
                disableProgressDialog();
                //
                showResults(so, so_current_reload);
            } else {
                Log.d("SO_RETURN", "SO RETURN null!!!");
            }
        } else {
        }

//        String so[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);
//
//        String so_current_reload = hmAux.get(String.valueOf(mService.getSo_prefix()) + "." + String.valueOf(mService.getSo_code()));
//
//        if (so != null) {
//            disableProgressDialog();
//            //
//            showResults(so, so_current_reload);
//        } else {
//            //refreshUI();
//            Log.d("SO_RETURN", "SO RETURN null!!!");
//        }
    }

    private void showResults(String[] so, String so_current_reload) {
        ArrayList<HMAux> sos = new ArrayList<>();
        for (int i = 0; i < so.length; i++) {
            String fields[] = so[i].split(Constant.MAIN_CONCAT_STRING_2);
            //
            HMAux mHmAux = new HMAux();
            mHmAux.put("label", fields[0]);
            mHmAux.put("type", "S.O.");
            mHmAux.put("status", fields[1]);
            mHmAux.put("final_status", fields[0] + " / " + fields[1]);
            //
            sos.add(mHmAux);
            //
            wsResults.add(mHmAux);
        }

        //if (sos.size() == 1 && sos.get(0).get("status").equalsIgnoreCase("Ok")) {
        if (wsResults.size() == 1 && wsResults.get(0).get("status").equalsIgnoreCase("Ok")) {

            if (mShortCut) {
                callAct027();
            } else {
                if (so_current_reload.equalsIgnoreCase("1")) {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_so_list_title"),
                            hmAux_Trans.get("alert_so_list_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refreshUI();
                                }
                            },
                            -1,
                            false
                    );
                } else {

                    if (MTASK_STATUS.equalsIgnoreCase(CREATE_TASK)) {

                        if (mTask != null) {
                            mTask = loadTask(
                                    mTask.getCustomer_code(),
                                    mTask.getSo_prefix(),
                                    mTask.getSo_code(),
                                    mTask.getPrice_list_code(),
                                    mTask.getPack_code(),
                                    mTask.getPack_seq(),
                                    mTask.getCategory_price_code(),
                                    mTask.getService_code(),
                                    mTask.getService_seq(),
                                    mTask.getExec_tmp(),
                                    mTask.getTask_tmp()
                            );
                            //
                            callFragTAsk(mTask);
                        }
                        // return;

                    } else {
                        refreshUI();
                    }

                }
            }
        } else {
            //showNewOptDialog(sos, so_current_reload);
            showNewOptDialog(wsResults, so_current_reload);
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

                if (mShortCut) {
                    callAct027();
                } else {
                    if (so_current_reload.equalsIgnoreCase("1")) {

                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_so_list_title"),
                                hmAux_Trans.get("alert_so_list_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        show.dismiss();
                                        //
                                        refreshUI();
                                    }
                                },
                                -1,
                                false
                        );

                    } else {
                        show.dismiss();
                    }
                }
            }
        });
    }

    private void refreshUI() {

        mService = loadService(
                mService.getCustomer_code(),
                mService.getSo_prefix(),
                mService.getSo_code(),
                mService.getPrice_list_code(),
                mService.getPack_code(),
                mService.getPack_seq(),
                mService.getCategory_price_code(),
                mService.getService_code(),
                mService.getService_seq()
        );

        act028_opc.setmService(mService);
        act028_opc.loadDataToScreen();

        full_status = act028_opc.verificarStatus_SO(
                mService.getSo_prefix(),
                mService.getSo_code(),
                mService
        ) ? "1" : "0";
        setDrawerState(true);
        setFrag(act028_empty_, SELECTION_EMPTY);

        if (index == 0) {
            if (progressDialog != null && progressDialog.isShowing()) {
                disableProgressDialog();
            }
        } else {
            index = 0;
            //
            if (progressDialog != null && progressDialog.isShowing()) {
                disableProgressDialog();
            }
        }
        //
        mSoAux = sm_soDao
                .getByStringHM(
                        new SM_SO_Sql_002(
                                mService.getCustomer_code(),
                                mService.getSo_prefix(),
                                mService.getSo_code()
                        ).toSqlQuery()
                );
        //
        if (mSoAux != null && mSoAux.size() > 0) {
            original_update_required = Integer.parseInt(mSoAux.get(SM_SODao.UPDATE_REQUIRED));
            so_status = mSoAux.get(SM_SODao.STATUS);
        }
        //
        startDownloadServices();
    }


    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        if (mShortCut) {
            callAct027();
        } else {
            refreshUI();
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Act028_Main.this);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_new_partner_opt, null);

        SearchableSpinner ss_partner = (SearchableSpinner) view.findViewById(R.id.act028_dialog_new_partner_opt_ss_partner);

        ss_partner.setmLabel(hmAux_Trans.get("ss_partner_list_ttl"));
        ss_partner.setmTitle(hmAux_Trans.get("ss_partner_list_search_ttl"));

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
            hmAux.put("description", hmAux_Trans.get("ss_partner_list_ttl"));

            ss_partner.setmValue(hmAux);
        }

        ss_partner.setmOption(partners);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog show = builder.show();

        ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
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
    public void menuTaskSelected(HashMap<String, String> data) {

        mTask = loadTask(
                ToolBox_Con.getPreference_Customer_Code(context),
                Integer.parseInt(data.get(SM_SODao.SO_PREFIX)),
                Integer.parseInt(data.get(SM_SODao.SO_CODE)),
                Integer.parseInt(data.get(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE)),
                Integer.parseInt(data.get(SM_SO_Service_Exec_TaskDao.PACK_CODE)),
                Integer.parseInt(data.get(SM_SO_Service_Exec_TaskDao.PACK_SEQ)),
                Integer.parseInt(data.get(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE)),
                Integer.parseInt(data.get(SM_SO_Service_Exec_TaskDao.SERVICE_CODE)),
                Integer.parseInt(data.get(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ)),
                Long.parseLong(data.get(SM_SO_Service_Exec_TaskDao.EXEC_TMP)),
                Long.parseLong(data.get(SM_SO_Service_Exec_TaskDao.TASK_TMP))
        );

        act028_task.setmService(mService);
        act028_task.setmTask(mTask);
        act028_task.setFull_status(full_status);
        //
        index = 1;
        //
        setFrag(act028_task, SELECTION_TASK);
    }

    @Override
    public void menuTaskCreated(SM_SO_Service_Exec_Task mTask) {
        this.mTask = mTask;

        if (mService.getExec_type().equalsIgnoreCase(Constant.SO_SERVICE_TYPE_YES_NO)) {
            callFragTAsk(mTask);
        } else {
            if (!ToolBox_Con.isOnline(context)) {
                callFragTAsk(mTask);
            }
        }
    }

    public void callFragTAsk(SM_SO_Service_Exec_Task mTask) {
        act028_task.setmService(mService);
        act028_task.setmTask(mTask);
        act028_task.setFull_status(full_status);
        act028_task.loadDataToScreen();
        //
        index = 1;
        //
        setFrag(act028_task, SELECTION_TASK);
    }

    @Override
    public void exec_task_tmp(String exec_tmp, String task_tmp) {
    }

    @Override
    public void menuOptionsSelected(SM_SO_Service_Exec sm_so_service_exec, String opc_full_status) {

        mDrawerLayout.closeDrawer(GravityCompat.START);

        this.full_status = opc_full_status;
        this.mExec_Aux = sm_so_service_exec;
        this.mTask = null;

        act028_task_list.setSm_so_service_exec(sm_so_service_exec, full_status);
        act028_task_list.loadDataToScreen();
        //
        index = 0;
        //
        setFrag(act028_task_list, SELECTION_TASK_LIST);
    }

    @Override
    public void newExec(SM_SO_Service sm_so_service, SM_SO_Service_Exec sm_so_service_exec, String full_status) {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        this.full_status = full_status;
        this.mExec_Aux = sm_so_service_exec;
        this.mTask = null;

        act028_task_list.setSm_so_service_exec(sm_so_service_exec, full_status);
        act028_task_list.loadDataToScreen();
        //
        index = 0;
        //
        //setFrag(act028_task_list, SELECTION_TASK_LIST);

        SM_SO_Service_Exec_Task task = new SM_SO_Service_Exec_Task();
        task.setTask_code(0);
        task.setTask_seq_oper(1);
        task.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        task.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        if (sm_so_service.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
            task.setTask_perc(0);
        } else {
            task.setTask_perc(100);
        }
        task.setQty_people(1);
        task.setStatus(Constant.SYS_STATUS_PROCESS);

        task.setStart_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"));
        task.setEnd_date("");
        task.setComments(null);

        task.setPK(sm_so_service_exec);
        task.setTask_tmp(201);
        sm_so_service_exec_taskDao.addUpdateTmp(task);

        /**
         * Calling WebService
         */

        sm_soDao.getByString(
                new SM_SO_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        sm_so_service_exec.getSo_prefix(),
                        sm_so_service_exec.getSo_code()
                ).toSqlQuery()
        );

        setMTASK_STATUS(Act028_Main.CREATE_TASK);

        if (sm_so_service.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
            setMTASK_STATUS(Act028_Main.CREATE_TASK);
            //
            setmTaskCall(false);
            executeSoSave(true);
        } else {
            setMTASK_STATUS(Act028_Main.CREATE_NULL);
        }

        menuTaskCreated(task);
    }

    @Override
    public void notExec(SM_SO_Service sm_so_service, SM_SO_Service_Exec sm_so_service_exec, String full_status) {
        mDrawerLayout.openDrawer(GravityCompat.START);

        this.full_status = full_status;
        this.mExec_Aux = sm_so_service_exec;
        this.mTask = null;

        act028_task_list.setSm_so_service_exec(sm_so_service_exec, full_status);
        act028_task_list.loadDataToScreen();
        //
        index = 0;
        //
        //setFrag(act028_task_list, SELECTION_TASK_LIST);

        setFrag(act028_empty_, SELECTION_EMPTY);

        SM_SO_Service_Exec_Task task = new SM_SO_Service_Exec_Task();
        task.setTask_code(0);
        task.setTask_seq_oper(1);
        task.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        task.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        task.setTask_perc(100);
        task.setQty_people(1);
        task.setStatus(Constant.SYS_STATUS_NOT_EXECUTED);

        task.setStart_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"));
        task.setEnd_date(task.getStart_date());
        task.setComments(null);

        task.setPK(sm_so_service_exec);

        long nTaskTemp = Long.parseLong(sm_so_service_exec_taskDao.getByStringHM(
                new SM_SO_Service_Exec_Task_Sql_004(
                        task.getCustomer_code(),
                        task.getSo_prefix(),
                        task.getSo_code(),
                        task.getPrice_list_code(),
                        task.getPack_code(),
                        task.getPack_seq(),
                        task.getCategory_price_code(),
                        task.getService_code(),
                        task.getService_seq(),
                        task.getExec_tmp()

                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_Sql_004.NEXT_TMP));

        task.setTask_tmp(nTaskTemp);
        sm_so_service_exec_taskDao.addUpdateTmp(task);
        sm_so_service_exec_taskDao.updateStatusOffLine(task);

        act028_opc.loadDataToScreen();

        /**
         * Calling WebService
         */

        sm_soDao.getByString(
                new SM_SO_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        sm_so_service_exec.getSo_prefix(),
                        sm_so_service_exec.getSo_code()
                ).toSqlQuery()
        );

        setMTASK_STATUS(Act028_Main.CREATE_NOT_EXEC);

        setmTaskCall(false);
        executeSoSave(true);
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

    public void offLineProcess() {
        if (mShortCut) {
            callAct027();
        } else {
            refreshUI();
        }
    }

    private void callAct027() {
        bundle.remove("data");
        bundle.putString(
                Constant.ACT028_SERVICE_UPDATED,
                ToolBox_Con.getPreference_Customer_Code(context) + "|" +
                        bundle.getString(SM_SODao.SO_PREFIX) + "|" +
                        bundle.getString(SM_SODao.SO_CODE) + "|" +
                        bundle.getString(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE) + "|" +
                        bundle.getString(SM_SO_Service_Exec_TaskDao.PACK_CODE) + "|" +
                        bundle.getString(SM_SO_Service_Exec_TaskDao.PACK_SEQ) + "|" +
                        bundle.getString(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE) + "|" +
                        bundle.getString(SM_SO_Service_Exec_TaskDao.SERVICE_CODE) + "|" +
                        bundle.getString(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ)
        );
        //
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    public void showInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_task_dialog_info, null);

        /**
         * Ini Vars
         */
        TextView tv_so_lbl = (TextView) view.findViewById(R.id.act028_dialog_info_tv_so_lbl);
        TextView tv_so_val = (TextView) view.findViewById(R.id.act028_dialog_info_tv_so_val);
        //
        TextView tv_service_lbl = (TextView) view.findViewById(R.id.act028_dialog_info_tv_service_lbl);
        TextView tv_service_val = (TextView) view.findViewById(R.id.act028_dialog_info_tv_service_val);
        //
        TextView tv_product_lbl = (TextView) view.findViewById(R.id.act028_dialog_info_tv_product_lbl);
        TextView tv_product_val = (TextView) view.findViewById(R.id.act028_dialog_info_tv_product_val);
        //
        TextView tv_serial_lbl = (TextView) view.findViewById(R.id.act028_dialog_info_tv_serial_id_lbl);
        TextView tv_serial_val = (TextView) view.findViewById(R.id.act028_dialog_info_tv_serial_id_val);
        //
        TextView tv_tracking_lbl = (TextView) view.findViewById(R.id.act028_dialog_info_tv_tracking_lbl);
        TextView tv_tracking_val = (TextView) view.findViewById(R.id.act028_dialog_info_tv_tracking_val);
        //
        ArrayList<HMAux> tracking_list = (ArrayList<HMAux>)
                new MD_Product_Serial_TrackingDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ).query_HM(
                        new MD_Product_Serial_Tracking_Sql_003(
                                Long.parseLong(mSoAux.get(SM_SODao.CUSTOMER_CODE)),
                                Long.parseLong(mSoAux.get(SM_SODao.PRODUCT_CODE)),
                                Long.parseLong(mSoAux.get(SM_SODao.SERIAL_CODE))

                        ).toSqlQuery()
                );
        //
        tv_so_lbl.setText(hmAux_Trans.get("dialog_so_lbl"));
        //
        String so_prefix_code = mSoAux.get(SM_SODao.SO_PREFIX) + "." + mSoAux.get(SM_SODao.SO_CODE);
        tv_so_val.setText(mSoAux.get(SM_SODao.SO_ID).equals(so_prefix_code) ? so_prefix_code : mSoAux.get(SM_SODao.SO_ID));
        //
        tv_service_lbl.setText(hmAux_Trans.get("dialog_service_lbl"));
        tv_service_val.setText(mService.getService_id() + " - " + mService.getService_desc());
        //
        tv_product_lbl.setText(hmAux_Trans.get("dialog_product_lbl"));
        tv_product_val.setText(mSoAux.get(SM_SODao.PRODUCT_ID) + " - " + mSoAux.get(SM_SODao.PRODUCT_DESC));
        //
        tv_serial_lbl.setText(hmAux_Trans.get("dialog_serial_lbl"));
        tv_serial_val.setText(mSoAux.get(SM_SODao.SERIAL_ID));
        //
        if (tracking_list != null && tracking_list.size() > 0) {
            tv_tracking_lbl.setText(hmAux_Trans.get("dialog_tracking_lbl"));
            //
            String trackingList = "";
            for (int i = 0; i < tracking_list.size(); i++) {
                trackingList += " º " + tracking_list.get(i).get(MD_Product_Serial_TrackingDao.TRACKING);
                if (i < tracking_list.size()) {
                    trackingList += "\n";
                }
            }
            //
            tv_tracking_val.setText(trackingList);
        } else {
            tv_tracking_lbl.setVisibility(View.GONE);
            tv_tracking_val.setVisibility(View.GONE);
        }

        builder.setTitle(hmAux_Trans.get("dialog_info_ttl"));
        builder.setView(view);
        builder.setCancelable(true);

        builder.show();
    }

    @Override
    protected void callAct021() {

        if (mService.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
            act028_task.updateTaskOnLeave();
        } else {
            act028_task.removeTaskOnLeave();
        }

        super.callAct021();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 2, Menu.FIRST + 3, hmAux_Trans.get("toolbar_info_lbl"));
        menu.findItem(2).setIcon(getResources().getDrawable(R.drawable.ic_info));
        menu.findItem(2).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.findItem(2).setTitle(hmAux_Trans.get("toolbar_info_lbl"));
        //
        if (
                ToolBox_Inf.parameterExists(context, Constant.PARAM_CHECKLIST) &&
                        hasExecutionProfile() &&
                        !so_status.equalsIgnoreCase(Constant.SYS_STATUS_DONE)
                ) {
            menu.add(0, 3, Menu.FIRST + 4, hmAux_Trans.get("toolbar_n_form_lbl"));
            menu.findItem(3).setIcon(getResources().getDrawable(R.drawable.ic_n_form));
            menu.findItem(3).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
            menu.findItem(3).setTitle(hmAux_Trans.get("toolbar_n_form_lbl"));
        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        int id = item.getItemId();
        //
        switch (id) {
            case 2:
                showInfo();
                return true;
            case 3:
                if (nFormProductList != null && nFormProductList.size() > 0) {
                    showNFormProductDialog();
                } else {
                    callNFormMsg();
                }
                return true;
        }
        //
        return super.onOptionsItemSelected(item);
    }

    private void loadNFormProductList() {
        nFormProductList = (ArrayList<HMAux>) productDao.query_HM(
                new MD_Product_Sql_SS_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mSoAux.get(SM_SODao.PRODUCT_CODE)
                ).toSqlQuery()
        );
    }

    private void showNFormProductDialog() {
        resetHmAuxProdutcSelected();
        //
        final AlertDialog.Builder productDialog = new AlertDialog.Builder(context, com.namoa_digital.namoa_library.R.style.AlertDialogTheme);
        //
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.namoa_dialog_ss_selection, null);
        //IniVar
        TextView tv_main_ttl = (TextView) view.findViewById(R.id.namoa_dialog_ss_selection_tv_main_ttl);
        final SearchableSpinner ss_product = (SearchableSpinner) view.findViewById(R.id.namoa_dialog_ss_selection_ss_main);
        final TextView tv_serial_lbl = (TextView) view.findViewById(R.id.namoa_dialog_ss_selection_tv_item_lbl);
        final TextView tv_serial_val = (TextView) view.findViewById(R.id.namoa_dialog_ss_selection_tv_item_val);
        //
        tv_main_ttl.setText(hmAux_Trans.get("dialog_form_prod_selection_ttl"));
        //
        ss_product.setmLabel(hmAux_Trans.get("dialog_form_prod_ss_product_lbl"));
        ss_product.setmTitle(hmAux_Trans.get("dialog_form_prod_ss_product_search_ttl"));
        ss_product.setmOption(nFormProductList);
        //
        HMAux auxProd = new HMAux();
        auxProd.put(SearchableSpinner.ID, String.valueOf(mSoAux.get(SM_SODao.PRODUCT_CODE)));
        auxProd.put(SearchableSpinner.DESCRIPTION, String.valueOf(mSoAux.get(SM_SODao.PRODUCT_DESC)));
        auxProd.put(SM_SODao.PRODUCT_CODE, String.valueOf(mSoAux.get(SM_SODao.PRODUCT_CODE)));
        auxProd.put(SM_SODao.PRODUCT_ID,String.valueOf(mSoAux.get(SM_SODao.PRODUCT_ID)));
        auxProd.put(SM_SODao.PRODUCT_DESC, String.valueOf(mSoAux.get(SM_SODao.PRODUCT_DESC)));
        //
        ss_product.setmValue(auxProd);
        //
        /*ToolBox_Inf.setSSmValue(
                ss_product,
                mSoAux.get(SM_SODao.PRODUCT_CODE),
                mSoAux.get(SM_SODao.PRODUCT_DESC),
                true,
                false
        );*/
        //
        tv_serial_lbl.setText(hmAux_Trans.get("dialog_form_prod_serial_lbl"));
        tv_serial_val.setText(mSoAux.get(SM_SODao.SERIAL_ID));
        //
        ss_product.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (!hmAux.get(SearchableSpinner.ID).equalsIgnoreCase(mSoAux.get(SM_SODao.PRODUCT_CODE))) {
                    tv_serial_lbl.setVisibility(View.GONE);
                    tv_serial_val.setVisibility(View.GONE);
                    tv_serial_val.setText("");
                } else {
                    tv_serial_lbl.setVisibility(View.VISIBLE);
                    tv_serial_val.setVisibility(View.VISIBLE);
                    tv_serial_val.setText(mSoAux.get(SM_SODao.SERIAL_ID));
                }
            }
        });

        //
        productDialog
                .setView(view)
                .setPositiveButton(
                        hmAux_Trans.get("sys_alert_btn_ok"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nFormProductSelected = ss_product.getmValue();
                                //
                                processNFormFlow();
                            }
                        }
                )
                .setNegativeButton(
                        hmAux_Trans.get("sys_alert_btn_cancel"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetHmAuxProdutcSelected();
                            }
                        }
                )
                .setCancelable(false);

        productDialog.show();

    }

    private void resetHmAuxProdutcSelected() {
        nFormProductSelected.put(SearchableSpinner.ID, "-1");
        nFormProductSelected.put(SearchableSpinner.DESCRIPTION, "");
        nFormProductSelected.put(SM_SODao.PRODUCT_CODE, "-1");
        nFormProductSelected.put(SM_SODao.PRODUCT_ID, "");
        nFormProductSelected.put(SM_SODao.PRODUCT_DESC, "");
    }


    private void callNFormMsg() {

        String msg =
                hmAux_Trans.get("alert_open_n_form_msg");

        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_open_n_form_ttl"),
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Salva os dados mexidos na task.
                        if (fm.findFragmentByTag(SELECTION_TASK) != null) {
                            act028_task.updateTaskOnLeave();
                        }
                        processNFormFlow();
                    }
                },
                1
        );
    }

    private void processNFormFlow() {
        if (mSoAux != null && mSoAux.size() > 0) {
            //
            HMAux syncProduct = syncChecklistDao
                    .getByStringHM(
                            new Sync_Checklist_Sql_002(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    !nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? Long.parseLong(nFormProductSelected.get(SM_SODao.PRODUCT_CODE)) : Long.parseLong(mSoAux.get(SM_SODao.PRODUCT_CODE))

                            ).toSqlQuery()
                    );
            //Se Forms do produto ainda não foram sincronizados,
            //Chama Ws para baixar os Forms.
            //Caso contrario, verifica se ja existe form aberto antes de
            //direcionar par act009
            if (syncProduct == null || syncProduct.size() == 0) {
                //Chamar WS
                executeSyncProcess();
            } else {
                callAct009();
            }
        }

    }

    private void executeSyncProcess() {
        if (ToolBox_Con.isOnline(context)) {
            ws_process = WS_PROCESS_N_FORM_SYNC;
            //
            enableProgressDialog(
                    hmAux_Trans.get("progress_n_form_sync_ttl"),
                    hmAux_Trans.get("progress_n_form_sync_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
            //
            ArrayList<String> data_package = new ArrayList<>();
            data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
            //
            Intent mIntent = new Intent(context, WBR_Sync.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
            bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
            bundle.putLong(Constant.GS_PRODUCT_CODE, !nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? Long.parseLong(nFormProductSelected.get(SM_SODao.PRODUCT_CODE)) : Long.parseLong(mSoAux.get(SM_SODao.PRODUCT_CODE)));
            bundle.putInt(Constant.GC_STATUS_JUMP, 1);
            bundle.putInt(Constant.GC_STATUS, 1);

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //
        progressDialog.dismiss();
        //
        if (ws_process.equals(WS_PROCESS_N_FORM_SYNC)) {
            updateSyncChecklist();
            //
            callAct009();
            //
            ws_process = "";
        }
    }

    public void updateSyncChecklist() {
        //Pega data atual
        Calendar cDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String last_update = dateFormat.format(cDate.getTime());

        Sync_Checklist syncChecklist = new Sync_Checklist();

        syncChecklist.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        syncChecklist.setProduct_code(!nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? Long.parseLong(nFormProductSelected.get(SM_SODao.PRODUCT_CODE)) : Long.parseLong(mSoAux.get(SM_SODao.PRODUCT_CODE)));
        syncChecklist.setLast_update(last_update);

        syncChecklistDao.addUpdate(syncChecklist);
        //
        startDownloadServices();
    }

    private void startDownloadServices() {
        Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
        Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
        Intent mIntentLogo = new Intent(context, WBR_DownLoad_Customer_Logo.class);
        Bundle bundle = new Bundle();
        //
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        //
        mIntentPDF.putExtras(bundle);
        mIntentPIC.putExtras(bundle);
        mIntentLogo.putExtras(bundle);
        //
        context.sendBroadcast(mIntentPDF);
        context.sendBroadcast(mIntentPIC);
        context.sendBroadcast(mIntentLogo);
    }


    private void callAct009() {
        Intent mIntent = new Intent(context, Act009_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(SM_SODao.SO_PREFIX, mSoAux.get(SM_SODao.SO_PREFIX));
        bundle.putString(SM_SODao.SO_CODE, mSoAux.get(SM_SODao.SO_CODE));
        bundle.putString(SM_SODao.SITE_CODE, mSoAux.get(SM_SODao.SITE_CODE));
        bundle.putString(SM_SODao.OPERATION_CODE, mSoAux.get(SM_SODao.OPERATION_CODE));
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(!nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? nFormProductSelected.get(SM_SODao.PRODUCT_CODE) : mSoAux.get(SM_SODao.PRODUCT_CODE)));
        //bundle.putString(Constant.ACT007_PRODUCT_CODE, String.valueOf(!nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? nFormProductSelected.get(SM_SODao.PRODUCT_CODE) : mSoAux.get(SM_SODao.PRODUCT_CODE)));
        bundle.putString(MD_ProductDao.PRODUCT_ID, !nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? nFormProductSelected.get(SM_SODao.PRODUCT_ID) : mSoAux.get(SM_SODao.PRODUCT_ID));
        //bundle.putString(Constant.ACT008_PRODUCT_ID, !nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? nFormProductSelected.get(SM_SODao.PRODUCT_ID) : mSoAux.get(SM_SODao.PRODUCT_ID));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, !nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? nFormProductSelected.get(SM_SODao.PRODUCT_DESC) : mSoAux.get(SM_SODao.PRODUCT_DESC));
        //bundle.putString(Constant.ACT008_PRODUCT_DESC, !nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? nFormProductSelected.get(SM_SODao.PRODUCT_DESC) : mSoAux.get(SM_SODao.PRODUCT_DESC));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase(mSoAux.get(SM_SODao.PRODUCT_CODE)) || nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? mSoAux.get(SM_SODao.SERIAL_ID) : "");
        //bundle.putString(Constant.ACT008_SERIAL_ID, nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase(mSoAux.get(SM_SODao.PRODUCT_CODE)) || nFormProductSelected.get(SM_SODao.PRODUCT_CODE).equalsIgnoreCase("-1") ? mSoAux.get(SM_SODao.SERIAL_ID) : "");
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT028);
        //Dados do Serviço
        bundle.putString(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE, String.valueOf(mService.getPrice_list_code()));
        bundle.putString(SM_SO_Service_Exec_TaskDao.PACK_CODE, String.valueOf(mService.getPack_code()));
        bundle.putString(SM_SO_Service_Exec_TaskDao.PACK_SEQ, String.valueOf(mService.getPack_seq()));
        bundle.putString(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE, String.valueOf(mService.getCategory_price_code()));
        bundle.putString(SM_SO_Service_Exec_TaskDao.SERVICE_CODE, String.valueOf(mService.getService_code()));
        bundle.putString(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ, String.valueOf(mService.getService_seq()));
        bundle.putBoolean(Constant.ACT027_IS_SHORTCUT, mShortCut);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    public String getProductInfo(Long product_code) {
        MD_ProductDao mdProductDao = new MD_ProductDao(context);
        MD_Product mdProduct = mdProductDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
        //
        if (mdProduct != null) {
            return mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc();
        } else {
            return "";
        }
    }

    public void cleanUpResults() {
        if (wsResults != null) {
            wsResults.clear();
        } else {
            wsResults = new ArrayList<>();
        }
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
