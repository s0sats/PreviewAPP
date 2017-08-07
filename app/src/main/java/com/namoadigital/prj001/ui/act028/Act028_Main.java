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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
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

public class Act028_Main extends Base_Activity_Frag implements Act028_Main_View, Act028_Opc.IAct028_Opc, Act028_Task_List.IAct028_Task_List, Act028_Task.IAct028_Task {

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
    private LinearLayout ll_empty;

    private Bundle bundle;

    private HashMap<String, String> mData;

    private HMAux partnerAux = new HMAux();

    private HMAux exec_task_tmp = new HMAux();

    private TextView tv_no_exec_selected;

    private int index = 0;

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
        ll_empty = (LinearLayout) findViewById(R.id.act028_main_lempty);

        tv_no_exec_selected = (TextView) findViewById(R.id.act028_main_tv_no_exec_selected);

        act028_opc = (Act028_Opc) fm.findFragmentById(R.id.act028_opc);
        act028_opc.setOnMenuOptionsSelected(this);
        act028_opc.setData(mData);
        act028_opc.setHmAux_Trans(hmAux_Trans);

        act028_task_list = (Act028_Task_List) fm.findFragmentById(R.id.act028_list);
        act028_task_list.setBaInfra(this);
        act028_task_list.setOnTaskSelected(this);
        act028_task_list.setHmAux_Trans(hmAux_Trans);

        act028_task = (Act028_Task) fm.findFragmentById(R.id.act028_lt);
        act028_task.setBaInfra(this);
        act028_task.setData(mData);
        act028_task.setOnExec_List_Opc_Update(this);
        act028_task.setHmAux_Trans(hmAux_Trans);

        mDrawerLayout.openDrawer(GravityCompat.START);

        controls_frags.add(act028_task);

        ll_list.setVisibility(View.GONE);
        ll_task.setVisibility(View.GONE);
        ll_empty.setVisibility(View.VISIBLE);

        tv_no_exec_selected.setText(hmAux_Trans.get("no_exec_selected_lbl"));
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
    public void menuOptionsSelected(SM_SO_Service_Exec sm_so_service_exec, String full_status) {

        mDrawerLayout.closeDrawer(GravityCompat.START);

        act028_task_list.setSm_so_service_exec(sm_so_service_exec, full_status);
        act028_task_list.setHMAuxScreen();
        //
        ll_list.setVisibility(View.VISIBLE);
        ll_task.setVisibility(View.GONE);
        ll_empty.setVisibility(View.GONE);
        //
        index = 0;
        //
        act028_task.updateTaskOnLeave();
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
        ll_empty.setVisibility(View.GONE);
        //
        index = 1;
        //
        setFrag(act028_task, "TASK");
        //
        act028_task.setHMAuxScreen();
    }

    @Override
    public void exec_list_opc_update() {
        act028_opc.setHMAuxScreen();
        act028_task_list.setHMAuxScreen();
    }

    @Override
    public void exec_task_tmp(String exec_tmp, String task_tmp) {
        exec_task_tmp.put("exec_tmp", exec_tmp);
        exec_task_tmp.put("task_tmp", task_tmp);
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
        if (index == 1) {
            index = 0;
            //
            ll_list.setVisibility(View.VISIBLE);
            ll_task.setVisibility(View.GONE);
            //
            act028_task.updateTaskOnLeave();
            //
            act028_task_list.setHMAuxScreen();
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

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        String so[] = hmAux.get("ListSo").split("@#$N@M0@$#@");

        String so_current_reload = hmAux.get(mData.get("customer_code") + "." + mData.get("so_prefix") + "." + mData.get("so_code"));


        if (so != null && so.length > 0) {
            showResults(so, so_current_reload);
        } else {
            refreshUI();
        }


//        if (!hmAux.get(WS_SO_Serial_Save.SO_RETURN_FULL_REFRESH).equals("0")) {
//
//            ToolBox.alertMSG(
//                    context,
//                    hmAux_Trans.get("alert_so_list_title"),
//                    hmAux_Trans.get("alert_so_list_msg"),
//
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            bundle.remove("data");
//                            //
//                            Intent mIntent = new Intent(context, Act027_Main.class);
//                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            mIntent.putExtras(bundle);
//                            //
//                            startActivity(mIntent);
//                            finish();
//                        }
//                    },
//                    -1,
//                    false
//            );
//
//        } else {
//            if (index == 0) {
//                disableProgressDialog();
//            } else {
//                index = 0;
//                //
//                ll_list.setVisibility(View.VISIBLE);
//                ll_task.setVisibility(View.GONE);
//                //
//                act028_task_list.setHMAuxScreen();
//                //
//                disableProgressDialog();
//            }
//        }
    }

    private void showResults(String[] so, String so_current_reload) {
        ArrayList<HMAux> sos = new ArrayList<>();
        for (int i = 0; i < so.length; i++) {
            String fields[] = so[i].split("$#@n@m0@@#$");
            //
            HMAux mHmAux = new HMAux();
            mHmAux.put("label", fields[0]);
            mHmAux.put("status", fields[1]);
            mHmAux.put("final_status", fields[0] + " / " + fields[1]);
            //
            sos.add(mHmAux);
        }
        //
        showNewOptDialog(sos, so_current_reload);
    }

    public void showNewOptDialog(List<HMAux> sos, final String so_current_reload) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act006_dialog_new_opt, null);

        /**
         * Ini Vars
         */

        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        String[] from = {"final_status"};
        int[] to = {R.id.namoa_custom_cell_3_tv_item};


        lv_results.setAdapter(
                new SimpleAdapter(
                        context,
                        sos,
                        R.layout.namoa_custom_cell_3,
                        from,
                        to
                )
        );

        builder.setTitle(hmAux_Trans.get("alert_new_opt_ttl"));
        builder.setView(view);
        builder.setCancelable(false);

        builder.show();

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
                            "SO Reload",
                            "A SO Precisa ser Recarregada!!!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.remove("data");
                                    //
                                    Intent mIntent = new Intent(context, Act027_Main.class);
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
            ll_list.setVisibility(View.VISIBLE);
            ll_task.setVisibility(View.GONE);
            //
            act028_task_list.setHMAuxScreen();
            //
            disableProgressDialog();
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        index = 0;

        ll_list.setVisibility(View.VISIBLE);
        ll_task.setVisibility(View.GONE);
        //
        act028_task_list.setHMAuxScreen();
        act028_task.setHMAuxScreen();

        disableProgressDialog();
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
                } else {

                }

                show.dismiss();
            }
        });

    }

}
