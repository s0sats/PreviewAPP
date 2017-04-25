package com.namoadigital.prj001.ui.act005;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act005_Adapter;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.fcm.RegistrationIntentService;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.sql.MD_Operation_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_002;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.ui.act004.Act004_Main;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act018.Act018_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act005_Main extends Base_Activity implements Act005_Main_View {

    public static final String MENU_ID = "menu_id";
    public static final String MENU_ICON = "menu_icon";
    public static final String MENU_DESC = "menu_desc";
    public static final String MENU_BADGE = "menu_badge";

    public static final String MENU_ID_CHECKLIST = "menu_checklist";
    public static final String MENU_ID_SCHEDULE_DATA = "menu_schedule_data";
    public static final String MENU_ID_PENDING_DATA = "menu_pending_data";
    public static final String MENU_ID_HISTORIC_DATA = "menu_id_historic_data";
    public static final String MENU_ID_MESSAGES = "menu_messages";

    public static final String MENU_ID_SEND_DATA = "menu_send_data";
    public static final String MENU_ID_SYNC_DATA = "menu_sync_data";
    public static final String MENU_ID_CLOSE = "menu_close_app";

    public static final String WS_PROCESS_SYNC = "ws_process_sync";
    public static final String WS_PROCESS_SEND = "ws_process_send";
    public static final String WS_PROCESS_LOGOUT = "ws_process_logout";


    private Context context;
    private GridView gv_menu;
    private Act005_Main_Presenter mPresenter;
    private Act005_Adapter mAdapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager fm;
    private Act005_Opc fragOpc;

    private String alertTitle = "";
    private String alertMsg = "";

    private String wsProcess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act005_main);

        //Hugo Remover
        //ToolBox_Inf.generateNotification(getApplicationContext(), 200);
        ToolBox_Inf.reprogramAlarms_Full_Quarter(Act005_Main.this);
        //

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
        //
        Intent mIntent = new Intent(getApplicationContext(), RegistrationIntentService.class);
        startService(mIntent);
    }

    private void iniSetup() {

        context = Act005_Main.this;
        //
        // Hugo
        ToolBox_Inf.cleanningFormLocal(context);
        //
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT005
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("drawer_change_customer_alert_ttl");
        transList.add("drawer_change_customer_alert_msg");
        transList.add("drawer_change_site_alert_ttl");
        transList.add("drawer_change_site_alert_msg");
        transList.add("drawer_change_operation_alert_ttl");
        transList.add("drawer_change_operation_alert_msg");
        transList.add("drawer_logout_alert_ttl");
        transList.add("drawer_logout_alert_msg");
        transList.add("alert_sync_ttl");
        transList.add("alert_sync_msg");
        transList.add("alert_exit_confirm_ttl");
        transList.add("alert_exit_confirm_msg");
        transList.add("alert_sync_finish_ttl");
        transList.add("alert_sync_finish_msg");
        transList.add("alert_send_finish_ttl");
        transList.add("alert_send_finish_msg");
        transList.add("drawer_sync_alert_ttl");
        transList.add("drawer_sync_alert_msg");
        transList.add("drawer_change_site_one_site_alert_ttl");
        transList.add("drawer_change_site_one_site_alert_msg");
        transList.add("drawer_change_operation_one_operation_alert_ttl");
        transList.add("drawer_change_operation_one_operation_alert_msg");
        transList.add("msg_start_sync");
        transList.add("msg_preparing_to_send_data");
        transList.add("logout_dialog_btn");
        transList.add("logout_dialog_ttl");
        transList.add("alert_logout_ttl");
        transList.add("alert_logout_msg");
        transList.add("lbl_sync_data");
        transList.add("lbl_logout");
        transList.add("lbl_schedule_data");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        //Carrega traduções da biblioteca
        ToolBox_Inf.libTranslation(getApplicationContext());
    }

    private void initVars() {
        wsProcess = "";

        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.act005_drawer);

        mPresenter = new Act005_Main_Presenter_Impl(
                context,
                this,
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans,
                new EV_User_CustomerDao(
                        context,
                        Constant.DB_FULL_BASE,
                        Constant.DB_VERSION_BASE
                ),
                new FCMMessageDao(
                        context,
                        Constant.DB_FULL_BASE,
                        Constant.DB_VERSION_BASE
                )
        );
        //
        gv_menu = (GridView) findViewById(R.id.act005_gv_menu);
        //
        mPresenter.getMenuItens(hmAux_Trans);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act005_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //
                invalidateOptionsMenu();
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //
        mDrawerToggle.syncState();
        //
        fragOpc = (Act005_Opc) fm.findFragmentById(R.id.act005_frag_opc);
        fragOpc.setHmAux_Trans(hmAux_Trans, mModule_Code, mResource_Code);
        fragOpc.setOnOpcItemClicked(new Act005_Opc.IAct005_Opc() {
            @Override
            public void itemClicked(String index) {
                DialogInterface.OnClickListener listener = null;
                int negativeBtn = 1;

                switch (index) {
                    case Act005_Opc.DRAWER_OPC_CUSTOMER:
                        //
                        alertTitle = hmAux_Trans.get("drawer_change_customer_alert_ttl");
                        alertMsg = hmAux_Trans.get("drawer_change_customer_alert_msg");
                        //
                        listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if(ToolBox_Con.isOnline(context)) {
                                //Reseta preferencias do Customer e volta para
                                //Act002 - lista de customer
                                changeCustomer();
//                                }else{
//                                    ToolBox_Inf.showNoConnectionDialog(Act005_Main.this);
//                                }
                            }
                        };

                        break;
                    case Act005_Opc.DRAWER_OPC_SITE:
                        MD_SiteDao siteDao = new MD_SiteDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                        );

                        int qty_sites = siteDao.query_HM(
                                new MD_Site_Sql_002(
                                        ToolBox_Con.getPreference_Customer_Code(context)
                                ).toSqlQuery()
                        ).size();

                        if (qty_sites <= 1) {
                            //Se apenas um site, da alert e não permite troca.
                            alertTitle = hmAux_Trans.get("drawer_change_site_one_site_alert_ttl");
                            alertMsg = hmAux_Trans.get("drawer_change_site_one_site_alert_msg");

                            listener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            };

                            negativeBtn = 0;
                        } else {
                            //
                            alertTitle = hmAux_Trans.get("drawer_change_site_alert_ttl");
                            alertMsg = hmAux_Trans.get("drawer_change_site_alert_msg");
                            //
                            //Apaga preferencia de Site, Operatione volta a lista de site
                            listener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Reseta preferencias do Customer e volta para
                                    ToolBox_Con.setPreference_Site_Code(context, "-1");
                                    ToolBox_Con.setPreference_Operation_Code(context, -1);
                                    //
                                    callAct003(context);
                                }
                            };
                        }
                        break;
                    case Act005_Opc.DRAWER_OPC_OPERATION:
                        MD_OperationDao operationDao = new MD_OperationDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                        );

                        int qty_operation = operationDao.query_HM(
                                new MD_Operation_Sql_001(
                                        ToolBox_Con.getPreference_Customer_Code(context)
                                ).toSqlQuery()
                        ).size();

                        if (qty_operation <= 1) {
                            //Se apenas uma operação, da alert e não permite troca.
                            alertTitle = hmAux_Trans.get("drawer_change_operation_one_operation_alert_ttl");
                            alertMsg = hmAux_Trans.get("drawer_change_operation_one_operation_alert_msg");

                            listener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            };

                            negativeBtn = 0;
                        } else {
                            //
                            alertTitle = hmAux_Trans.get("drawer_change_operation_alert_ttl");
                            alertMsg = hmAux_Trans.get("drawer_change_operation_alert_msg");
                            //
                            listener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Apaga preferencia de Operatione volta a ista de operation
                                    ToolBox_Con.setPreference_Operation_Code(context, -1);
                                    //
                                    callAct004(context);
                                }
                            };

                        }

                        break;
                    case Act005_Opc.DRAWER_OPC_LOGOUT:
                        //
                        alertTitle = hmAux_Trans.get("drawer_logout_alert_ttl");
                        alertMsg = hmAux_Trans.get("drawer_logout_alert_msg");
                        //
                        listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ToolBox_Con.cleanPreferences(Act005_Main.this);
                                ToolBox_Inf.call_Act001_Main(Act005_Main.this);
                                finish();
                            }
                        };
                        break;
                    default:
                        break;
                }
                //Verifica se listner foi setado,
                //se foi, exibe Dialog.
                if (listener != null) {
                    ToolBox.alertMSG(
                            Act005_Main.this,
                            alertTitle,
                            alertMsg,
                            listener,
                            negativeBtn
                    );

                }
                //Fecha Drawer
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }

            @Override
            public void syncClicked() {

                ToolBox.alertMSG(
                        Act005_Main.this,
                        hmAux_Trans.get("drawer_sync_alert_ttl"),
                        hmAux_Trans.get("drawer_sync_alert_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mPresenter.accessMenuItem(MENU_ID_SYNC_DATA, 0);
                            }
                        },
                        1
                );

            }

            @Override
            public void logoutClicked() {

                mPresenter.showLogoutDialog();

               /* Intent mIntent = new Intent(context, WBR_Logout.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST,"1|5");//Pula validação de other device

                mIntent.putExtras(bundle);
                //
                context.sendBroadcast(mIntent);
                //ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_preparing_to_send_data"), "", "0");

*/

//                ToolBox.alertMSG(
//                        Act005_Main.this,
//                        hmAux_Trans.get("drawer_logout_alert_ttl"),
//                        hmAux_Trans.get("drawer_logout_alert_msg"),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ToolBox_Con.cleanPreferences(Act005_Main.this);
//                                ToolBox_Inf.call_Act001_Main(Act005_Main.this);
//                                finish();
//                            }
//                        },
//                        1
//                );

            }
        });


    }

    private void changeCustomer() {
        ToolBox_Con.resetCustomerSiteOperationPreferences(context);

        callAct002(context);

    }

    private void initActions() {
        gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.accessMenuItem(item.get(Act005_Main.MENU_ID), 0);
            }
        });

    }

    @Override
    public void loadMenu(List<HMAux> menus) {
        //Traduz menus
        for (HMAux item : menus) {
            if (hmAux_Trans.get(item.get(Act005_Main.MENU_DESC)) != null) {
                item.put(Act005_Main.MENU_DESC, hmAux_Trans.get(item.get(Act005_Main.MENU_DESC)));
            } else {
                item.put(Act005_Main.MENU_DESC, ToolBox.setNoTrans(mModule_Code, mResource_Code, item.get(Act005_Main.MENU_DESC)));
            }
        }

        mAdapter = new Act005_Adapter(context, R.layout.act005_item_menu_badge, menus);
        gv_menu.setAdapter(mAdapter);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT005;
        mAct_Title = Constant.ACT005 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value = hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl = hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value = hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

    }

    @Override
    public void showPD() {

        switch (wsProcess) {
            case Act005_Main.WS_PROCESS_SEND:
                alertTitle = hmAux_Trans.get("alert_send_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_send_finish_msg");
                break;
            case Act005_Main.WS_PROCESS_SYNC:
                alertTitle = hmAux_Trans.get("alert_sync_ttl");
                alertMsg = hmAux_Trans.get("alert_sync_msg");
                break;
            case Act005_Main.WS_PROCESS_LOGOUT:
                alertTitle = hmAux_Trans.get("alert_logout_ttl");
                alertMsg = hmAux_Trans.get("alert_logout_msg");
                break;
            default:
                break;

        }
        //
        enableProgressDialog(
                alertTitle,
                alertMsg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void showNoConnectionDialog() {
        ToolBox_Inf.showNoConnectionDialog(Act005_Main.this);
    }

    @Override
    public void setWsProcess(String ws_called) {
        wsProcess = ws_called;
    }

    @Override
    public void callAct006(Context context) {
        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct012(Context context) {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    public void callAct002(Context context) {
        Intent mIntent = new Intent(context, Act002_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXECUTE_WS_GET_CUSTOMER, 1);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        //
        finish();
    }

    public void callAct003(Context context) {
        Intent mIntent = new Intent(context, Act003_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    public void callAct004(Context context) {
        Intent mIntent = new Intent(context, Act004_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct014(Context context) {
        Intent mIntent = new Intent(context, Act014_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct016(Context context) {
        Intent mIntent = new Intent(context, Act016_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct018(Context context) {
        Intent mIntent = new Intent(context, Act018_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void closeApp() {

        ToolBox.alertMSG(
                Act005_Main.this,
                hmAux_Trans.get("alert_exit_confirm_ttl"),
                hmAux_Trans.get("alert_exit_confirm_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToolBox_Con.getPreference_MessageClear(getApplicationContext()).equalsIgnoreCase("");

                        finish();
                    }
                },
                1
        );

    }

    //TRATA UPDATE_REQUIRED - CANCEL
    @Override
    protected void processGo() {
        super.processGo();
        mPresenter.executeSyncProcess(1);
    }

    //TRATA UPDATE_REQUIRED - OK
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);
        //
        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        progressDialog.dismiss();

        if (!wsProcess.equals("")) {
            if (wsProcess.equals(Act005_Main.WS_PROCESS_LOGOUT)) {
                if (ToolBox_Con.getPreference_Customer_Code(context) == -1L) {
                    processLogin();
                }
            } else {
                showSuccessDialog();
                //Atualiza traduções
                loadTranslation();
                //Atualiza menu e os badges
                mPresenter.getMenuItens(hmAux_Trans);
                //Fecha Drawer
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        changeCustomer();
    }

    private void showSuccessDialog() {
        switch (wsProcess) {
            case Act005_Main.WS_PROCESS_SEND:
                alertTitle = hmAux_Trans.get("alert_send_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_send_finish_msg");
                break;

            case Act005_Main.WS_PROCESS_SYNC:
                alertTitle = hmAux_Trans.get("alert_sync_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_sync_finish_msg");
                //
                // Hugo
                startDownloadServices();
                //
                break;

            default:
                break;

        }
        //Reseta variavel.
        wsProcess = "";

        ToolBox.alertMSG(
                Act005_Main.this,
                alertTitle,
                alertMsg,
                null,
                0
        );

    }

    @Override
    public void callLoginProcess() {
        processLogin();
    }

    //TRATA SESSION_NOT_FOUND
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
    public void onBackPressed() {
        //super.onBackPressed();
        closeApp();

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //return super.onPrepareOptionsMenu(menu);
        //Pega os settings do menu e esconde
        MenuItem item = menu.findItem(R.id.act05_action_settings);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act005_main_menu, menu);

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

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.act05_action_settings) {

            ToolBox_Con.cleanPreferences(context);

            Intent mIntent = new Intent(context, Act001_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);

            finish();

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        mPresenter.getMenuItens(hmAux_Trans);
    }

    public void startDownloadServices() {

        Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
        Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
        Bundle bundle = new Bundle();
        mIntentPDF.putExtras(bundle);
        mIntentPIC.putExtras(bundle);
        //
        context.sendBroadcast(mIntentPDF);
        context.sendBroadcast(mIntentPIC);
    }

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
}
