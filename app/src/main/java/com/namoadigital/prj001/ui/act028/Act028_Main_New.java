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
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.receiver.WBR_SO_Save;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_009;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
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

    public static final String CREATE_TASK = "CREATE_TASK";
    public static final String CREATE_SAVE = "CREATE_SAVE";
    public static final String CREATE_NULL = "CREATE_NULL";
    public static final String CREATE_NOT_EXEC = "CREATE_NOT_EXEC";

    public String full_status;

    private Context context;
    private Bundle bundle;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean mDrawerStatus = true;
    private boolean mShortCut = false;

    private String MTASK_STATUS;

    private FragmentManager fm;
    private Act028_Empty_New act028_empty_new;
    private Act028_Opc_New act028_opc;
    private Act028_Task_List_New act028_task_list;
    private Act028_Task_New act028_task;

    private SM_SO_Service mService;
    private SM_SO_Service_Exec mExec;
    private SM_SO_Service_Exec mExec_Aux;
    private SM_SO_Service_Exec_Task mTask;

    private SM_SODao sm_soDao;
    private SM_SO_ServiceDao sm_so_serviceDao;
    private SM_SO_Service_ExecDao sm_so_service_execDao;
    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;

    private HMAux partnerAux = new HMAux();

    private int index = 0;

    public void setMTASK_STATUS(String MTASK_STATUS) {
        this.MTASK_STATUS = MTASK_STATUS;
    }

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

        full_status = act028_opc.verificarStatus_SO(
                mService.getSo_prefix(),
                mService.getSo_code(),
                mService
        ) ? "1" : "0";

        act028_empty_new = new Act028_Empty_New();
        act028_empty_new.setHmAux_Trans(hmAux_Trans);

        act028_task_list = new Act028_Task_List_New();
        act028_task_list.setBaInfra(this);
        act028_task_list.setOnTaskSelected(this);
        act028_task_list.setHmAux_Trans(hmAux_Trans);

        act028_task = new Act028_Task_New();
        act028_task.setBaInfra(this);
        act028_task.setHmAux_Trans(hmAux_Trans);

        controls_frags.add(act028_task);

        if (mShortCut) {
            act028_task.setmService(mService);
            act028_task.setmTask(mTask);
            act028_task.setFull_status(full_status);
            setFrag(act028_task, SELECTION_TASK);
        } else {
            setFrag(act028_empty_new, SELECTION_EMPTY);
        }
    }

    //region Recover Intent Parameters
    private void recoverGetIntents() {
        bundle = getIntent().getExtras();

        if (bundle != null) {

            mShortCut = bundle.getBoolean(Constant.ACT027_IS_SHORTCUT);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));

        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mShortCut) {

            String message = "";

            if (mService.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP) &&
                    mTask.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PROCESS)
                    ) {
                hmAux_Trans.get("alert_task_msg_delete");
            } else {
                hmAux_Trans.get("alert_service_list_msg");
            }

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_service_list_title"),
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
                    setFrag(act028_task_list, SELECTION_TASK_LIST);
                } else {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_service_list_title"),
                            hmAux_Trans.get("alert_task_msg_delete"),
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
                                    setFrag(act028_empty_new, SELECTION_EMPTY);
                                }
                            },
                            1,
                            false
                    );
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
                                    //
                                    Intent mIntent = new Intent(context, Act027_Main.class);
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
            //refreshUI();
            Log.d("SO_RETURN", "SO RETURN null!!!");
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
//        //
//        if (mExec_Aux != null) {
//            mExec = loadExec(
//                    mExec_Aux.getCustomer_code(),
//                    mExec_Aux.getSo_prefix(),
//                    mExec_Aux.getSo_code(),
//                    mExec_Aux.getPrice_list_code(),
//                    mExec_Aux.getPack_code(),
//                    mExec_Aux.getPack_seq(),
//                    mExec_Aux.getCategory_price_code(),
//                    mExec_Aux.getService_code(),
//                    mExec_Aux.getService_seq(),
//                    mExec_Aux.getExec_tmp()
//            );
//        }

        act028_opc.setmService(mService);
        act028_opc.loadDataToScreen();

        full_status = act028_opc.verificarStatus_SO(
                mService.getSo_prefix(),
                mService.getSo_code(),
                mService
        ) ? "1" : "0";

//        if (mExec != null) {
//
//            act028_task_list.setSm_so_service_exec(
//                    mExec,
//                    full_status
//            );
//            act028_task_list.loadDataToScreen();
//
//            setFrag(act028_task_list, SELECTION_TASK_LIST);
//        } else {
        setDrawerState(true);
        setFrag(act028_empty_new, SELECTION_EMPTY);
//        }

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
        task.setStatus(Constant.SO_STATUS_PROCESS);

        task.setStart_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"));
        task.setEnd_date("");
        task.setComments("");

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

        setMTASK_STATUS(Act028_Main_New.CREATE_TASK);

        if (sm_so_service.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
            setMTASK_STATUS(Act028_Main_New.CREATE_TASK);
            //
            callSoSave(sm_so_service_exec.getSo_prefix(), sm_so_service_exec.getSo_code());
        } else {
            setMTASK_STATUS(Act028_Main_New.CREATE_NULL);
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

        setFrag(act028_empty_new, SELECTION_EMPTY);

        SM_SO_Service_Exec_Task task = new SM_SO_Service_Exec_Task();
        task.setTask_code(0);
        task.setTask_seq_oper(1);
        task.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        task.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        task.setTask_perc(100);

//        if (sm_so_service.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
//            task.setTask_perc(100);
//        } else {
//            task.setTask_perc(100);
//        }

        task.setQty_people(1);
        task.setStatus(Constant.SO_STATUS_NOT_EXECUTED);

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

        setMTASK_STATUS(Act028_Main_New.CREATE_NOT_EXEC);

        callSoSave(sm_so_service_exec.getSo_prefix(), sm_so_service_exec.getSo_code());

//        if (sm_so_service.getExec_type().equalsIgnoreCase(ConstantBaseApp.SO_SERVICE_TYPE_START_STOP)) {
//            setMTASK_STATUS(Act028_Main_New.CREATE_TASK);
//            //
//            callSoSave(sm_so_service_exec.getSo_prefix(), sm_so_service_exec.getSo_code());
//        } else {
//            setMTASK_STATUS(Act028_Main_New.CREATE_NULL);
//        }

//        menuTaskCreated(task);
        //
        //setFrag(act028_empty_new, SELECTION_EMPTY);
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
        //
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    private void callSoSave(int prefix, int code) {

        if (ToolBox_Con.isOnline(context)) {

            enableProgressDialog(
                    hmAux_Trans.get("alert_task_title"),
                    hmAux_Trans.get("alert_so_list_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Save.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {

        }
    }

    public void showInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_task_dialog_info, null);

        /**
         * Ini Vars
         */

        ListView lv_opt = (ListView) view.findViewById(R.id.act028_task_dialog_info_lv_opt);

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

        /**
         * Ini Action
         */

//        lv_opt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HMAux item = (HMAux) parent.getItemAtPosition(position);
//                mPresenter.defineFlow(item);
//
//            }
//        });

        //builder.setTitle(hmAux_Trans.get("alert_new_opt_ttl"));
        builder.setTitle("On Construction");
        builder.setView(view);
        builder.setCancelable(true);

        builder.show();
    }


}
