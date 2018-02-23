package com.namoadigital.prj001.ui.act005;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act005_Adapter;
import com.namoadigital.prj001.adapter.Act028_Results_Adapter;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.fcm.RegistrationIntentService;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.ScreenStatusService;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_004;
import com.namoadigital.prj001.sql.EV_User_Sql_001;
import com.namoadigital.prj001.sql.MD_Operation_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_002;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.ui.act004.Act004_Main;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act018.Act018_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act030.Act030_Main;
import com.namoadigital.prj001.ui.act033.Act033_Main;
import com.namoadigital.prj001.ui.act034.Act034_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.ui.act036.Act036_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act005_Main extends Base_Activity_Frag implements Act005_Main_View {

    public static final String MENU_ID = "menu_id";
    public static final String MENU_ICON = "menu_icon";
    public static final String MENU_DESC = "menu_desc";
    public static final String MENU_BADGE = "menu_badge";
    public static final String MENU_BADGE2 = "menu_badgeso";

    public static final String MENU_ID_CHECKLIST = "menu_checklist";
    public static final String MENU_ID_FORM_AP = "menu_form_ap";
    public static final String MENU_ID_SERVICE = "menu_service";
    public static final String MENU_ID_SERIAL = "menu_serial";
    public static final String MENU_ID_SCHEDULE_DATA = "menu_schedule_data";
    public static final String MENU_ID_PENDING_DATA = "menu_pending_data";
    public static final String MENU_ID_HISTORIC_DATA = "menu_id_historic_data";
    public static final String MENU_ID_MESSAGES = "menu_messages";

    public static final String MENU_ID_SEND_DATA = "menu_send_data";
    public static final String MENU_ID_SYNC_DATA = "menu_sync_data";
    public static final String MENU_ID_CHAT = "menu_chat";
    public static final String MENU_ID_CLOSE = "menu_close_app";

    public static final String WS_PROCESS_SYNC = "ws_process_sync";
    public static final String WS_PROCESS_SEND = "ws_process_send";

    public static final String WS_PROCESS_SO_STATUS = "ws_process_so_status";
    public static final String WS_PROCESS_SO_SAVE = "ws_process_so_save";
    public static final String WS_PROCESS_SO_SAVE_APPROVAL = "ws_process_so_save_approval";
    public static final String WS_PROCESS_SO_SYNC = "ws_process_so_sync";

    public static final String WS_PROCESS_LOGOUT = "ws_process_logout";
    public static final String WS_PROCESS_ENABLE_NFC = "ws_process_enable_nfc";
    public static final String WS_PROCESS_CANCEL_NFC = "ws_process_cancel_nfc";
    public static final String WS_PROCESS_SUPPORT = "ws_process_support";
    //
    public static final String WS_PROCESS_SEND_N_FORM = "ws_process_send_n_form";
    public static final String WS_PROCESS_SEND_SO = "ws_process_send_so";

    public static final String WS_LIST_ITEM = "ws_list_item";
    public static final String WS_LIST_ITEM_RETURN = "ws_list_item_return";
    public static final String WS_LIST_ITEM_LABEL = "ws_list_item_label";

    //toolbar constants
    private static final int TOOLBAR_NAMOA_LOGO = 1;
    private static final int TOOLBAR_ENABLE_NFC = 2;
    private static final int TOOLBAR_CANCEL_NFC = 3;
    private static final int TOOLBAR_SUPPORT = 4;

    private ArrayList<HMAux> wsResults = new ArrayList<>();

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
    private String wsSoProcess;

    //
    private ArrayList<HMAux> wsProcessList = new ArrayList<>();

    private FCMReceiver fcmReceiver;
    private BR_Chat chatReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act005_main);
        //
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
        //
        Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
        Bundle bundle = new Bundle();
        mIntentPIC.putExtras(bundle);
        context.sendBroadcast(mIntentPIC);
        //
        if (ToolBox_Inf.isUsrAppLogged(context) && !ScreenStatusService.isRunning) {
            Intent mScreenStatusService = new Intent(context, ScreenStatusService.class);
            context.startService(mScreenStatusService);
        }
        //
        fcmReceiver = new FCMReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.WS_FCM);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(fcmReceiver, filter);
        //
//        chatReceiver = new BR_Chat();
//        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
//        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);
//        LocalBroadcastManager.getInstance(this).registerReceiver(chatReceiver, brRoomFilter);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(chatReceiver);
        super.onDestroy();
    }

    public String sDTFormat_30_Days(String sDTFormatS) {
        String sResults = "";
        Calendar ca1 = Calendar.getInstance();
        ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) - 36);
        //Teste
        //ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) + 6);

        SimpleDateFormat sdf = new SimpleDateFormat(sDTFormatS) {
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
                StringBuffer toFix = super.format(date, toAppendTo, pos);
                return toFix.insert(toFix.length() - 2, ':');
            }
        };

        try {
            sResults = sdf.format(ca1.getTime());
        } catch (Exception var5) {
            sResults = "1900-01-01 00:00:00";
        }

        return sResults;
    }


    private void iniSetup() {

        context = Act005_Main.this;
        //
        ToolBox_Inf.cleanningFormLocal(context);
        Constant.DATEFORMATDT = ToolBox_Con.getPreference_Customer_nls_date_format(context).toLowerCase().replaceAll("m", "M").replaceAll("r", "y");
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
        transList.add("lbl_so");
        transList.add("lbl_serial_data");
        //toolbar
        transList.add("toolbar_enable_nfc");
        transList.add("toolbar_cancel_nfc");
        transList.add("toolbar_support");
        transList.add("alert_enable_nfc_ttl");
        transList.add("alert_enable_nfc_msg");
        transList.add("alert_cancel_nfc_ttl");
        transList.add("alert_cancel_nfc_msg");
        transList.add("alert_support_ttl");
        transList.add("alert_support_msg");

        transList.add("progress_enable_nfc_ttl");
        transList.add("progress_enable_nfc_msg");
        transList.add("progress_cancel_nfc_ttl");
        transList.add("progress_cancel_nfc_msg");
        transList.add("progress_support_ttl");
        transList.add("progress_support_msg");

        transList.add("alert_enable_nfc_finish_ttl");
        transList.add("alert_enable_nfc_finish_msg");
        transList.add("alert_cancel_nfc_finish_ttl");
        transList.add("alert_cancel_nfc_finish_msg");
        transList.add("alert_support_finish_ttl");
        transList.add("alert_support_finish_msg");
        //alert support
        transList.add("support_dialog_ttl");
        //
        transList.add("lbl_change_zone");
        transList.add("drawer_change_zone_one_zone_alert_ttl");
        transList.add("drawer_change_zone_one_zone_alert_msg");
        transList.add("drawer_change_zone_alert_ttl");
        transList.add("drawer_change_zone_alert_msg");
        //
        transList.add("lbl_chat");
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

        Constant.DATEFORMATDT = ToolBox_Inf.nlsDateFormat(context);
        Constant.DATEFORMATDTH = ToolBox_Inf.nlsDateFormat(context) + " HH:mm";

        Constant.HM_ICON_NAMOA_SERVICES_TEXT = hmAux_Trans.get("lbl_so");
    }

    private void initVars() {
        Act035_Main.mRoom_code = "";

        wsProcess = "";
        wsSoProcess = "";

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
                ),
                new SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
        //
        gv_menu = (GridView) findViewById(R.id.act005_gv_menu);
        //
        ToolBox_Inf.mkDirectory();
        ToolBox_Inf.cleanUpApproval(
                context,
                new SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
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
                                    ToolBox_Con.setPreference_Zone_Code(context, -1);
                                    ToolBox_Con.setPreference_Operation_Code(context, -1);
                                    //
                                    callAct003(context);
                                }
                            };
                        }
                        break;
                    case Act005_Opc.DRAWER_OPC_ZONE:
                        MD_Site_ZoneDao zoneDao = new MD_Site_ZoneDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                        );

                        int qty_zones = zoneDao.query_HM(
                                new MD_Site_Zone_Sql_002(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context))
                                ).toSqlQuery()
                        ).size();

                        if (qty_zones <= 1) {
                            //Se apenas um site, da alert e não permite troca.
                            alertTitle = hmAux_Trans.get("drawer_change_zone_one_zone_alert_ttl");
                            alertMsg = hmAux_Trans.get("drawer_change_zone_one_zone_alert_msg");

                            listener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            };

                            negativeBtn = 0;
                        } else {
                            //
                            alertTitle = hmAux_Trans.get("drawer_change_zone_alert_ttl");
                            alertMsg = hmAux_Trans.get("drawer_change_zone_alert_msg");
                            //
                            //Apaga preferencia de zona volta a lista de zona
                            listener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Reseta preferencias da zona e volta para
                                    ToolBox_Con.setPreference_Zone_Code(context, -1);
                                    //
                                    callAct033(context);
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
                        /*
                        *
                        *
                        * Esse case não funciona mais, o metodo chamado no "botão logout" é
                        * a interface logoutClicked()
                        *
                        * */
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

    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
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

            case Act005_Main.WS_PROCESS_ENABLE_NFC:
                alertTitle = hmAux_Trans.get("progress_enable_nfc_ttl");
                alertMsg = hmAux_Trans.get("progress_enable_nfc_msg");
                break;

            case Act005_Main.WS_PROCESS_CANCEL_NFC:
                alertTitle = hmAux_Trans.get("progress_cancel_nfc_ttl");
                alertMsg = hmAux_Trans.get("progress_cancel_nfc_msg");
                break;

            case Act005_Main.WS_PROCESS_SUPPORT:
                alertTitle = hmAux_Trans.get("progress_support_ttl");
                alertMsg = hmAux_Trans.get("progress_support_msg");
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
    public void setWsSoProcess(String wsSoProcess) {
        this.wsSoProcess = wsSoProcess;
    }

    @Override
    public void setWsProcessList(ArrayList<HMAux> wsProcessList) {
        this.wsProcessList = wsProcessList;
    }

    @Override
    public void callAct006(Context context) {
        //
        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct036(Context context) {
        //
        Intent mIntent = new Intent(context, Act036_Main.class);
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
    public void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct030(Context context) {
        Intent mIntent = new Intent(context, Act030_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }


    private void callAct033(Context context) {
        Intent mIntent = new Intent(context, Act033_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct034(Context context) {
        Intent mIntent = new Intent(context, Act034_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
        mIntent.putExtras(bundle);
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

    private void setRes(String label, String status, String final_status) {
        HMAux res = new HMAux();

        res.put("label", label);
        res.put("status", status);
        res.put("final_status", final_status);

        wsResults.add(res);
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //progressDialog.dismiss();

        if (!wsProcess.equals("")) {
            if (wsProcess.equals(Act005_Main.WS_PROCESS_LOGOUT)) {
                progressDialog.dismiss();
                if (ToolBox_Con.getPreference_Customer_Code(context) == -1L) {
                    mPresenter.stopChatServices();
                    //
                    EV_User_CustomerDao userCustomerDao = new EV_User_CustomerDao(
                            context,
                            Constant.DB_FULL_BASE,
                            Constant.DB_VERSION_BASE
                    );
                    //
                    List<HMAux> sessionsOn = userCustomerDao
                            .query_HM(
                                    new EV_User_Customer_Sql_004(
                                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                            ToolBox_Con.getPreference_User_Code(context)
                                    ).toSqlQuery()
                            );

                    if (sessionsOn != null && sessionsOn.size() != 0) {
                        changeCustomer();
                    } else {
                        processLogin();
                    }
                } else {
                    // fechar drawer Hugo
                }
            } else {

                if (!wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_STATUS)) {
                    progressDialog.dismiss();
                    showSuccessDialog();
                    //Atualiza traduções
                    loadTranslation();
                    //Atualiza menu e os badges
                    mPresenter.getMenuItens(hmAux_Trans);
                    //Fecha Drawer
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {

                    setRes("N-Form", hmAux_Trans.get("alert_send_finish_msg"), "");
                    mPresenter.executeSoSave();

                }
            }
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE)) {

            String so[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            if (so.length > 0 && !so[0].isEmpty()) {
                for (int i = 0; i < so.length; i++) {
                    String fields[] = so[i].split(Constant.MAIN_CONCAT_STRING_2);
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", "" + fields[0]);
                    mHmAux.put("status", fields[1]);
                    mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                    }
                }
            } else {
            }

            mPresenter.executeSoSaveApproval();
        } else if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE_APPROVAL)) {
            setWsSoProcess("");

            String approval[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            if (approval.length > 0 && !approval[0].isEmpty()) {
                for (int i = 0; i < approval.length; i++) {
                    String fields[] = approval[i].split(Constant.MAIN_CONCAT_STRING_2);
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", fields[0]);
                    mHmAux.put("status", fields[1]);
                    mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                    }
                }
            } else {

            }

            mPresenter.getMenuItens(hmAux_Trans);
            progressDialog.dismiss();

            if (wsResults.size() > 1) {
                showResults(wsResults);
            } else {
                showSuccessDialog();
            }

        } else {
            setWsSoProcess("");
            mPresenter.getMenuItens(hmAux_Trans);
            progressDialog.dismiss();

            if (wsResults.size() > 1) {
                showResults(wsResults);
            } else {
                showSuccessDialog();
            }
        }
    }

    public void showResults(List<HMAux> res) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        lv_results.setAdapter(
                new Act028_Results_Adapter(
                        context,
                        R.layout.act028_results_adapter_cell,
                        res
                )
        );

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

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_STATUS)) {

            setRes("N-Form", hmAux_Trans.get("N-Form Error"), "");

            enableProgressDialog(
                    hmAux_Trans.get("progress_so_save_ttl"),
                    hmAux_Trans.get("progress_so_save_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );

            mPresenter.executeSoSave();

        } else if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE)) {

            setRes("N-Service", hmAux_Trans.get("N-Service SO Save Error"), "");

            super.processError_1(mLink, mRequired);
            mPresenter.getMenuItens(hmAux_Trans);

        } else if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE_APPROVAL)) {

            setRes("N-Service", hmAux_Trans.get("N-Service SO Approval Error"), "");

            super.processError_1(mLink, mRequired);
            mPresenter.getMenuItens(hmAux_Trans);

        } else {

            setRes("N-Geral", hmAux_Trans.get("N-Geral Error"), "");

            super.processError_1(mLink, mRequired);
            mPresenter.getMenuItens(hmAux_Trans);
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_STATUS)) {
            setWsSoProcess("");
            setRes("N-Form", hmAux_Trans.get("N-Form Error"), "");
            mPresenter.getMenuItens(hmAux_Trans);
            progressDialog.dismiss();

        } else if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE)) {
            setWsSoProcess("");
            mPresenter.getMenuItens(hmAux_Trans);
            setRes("N-Service", hmAux_Trans.get("N-Service SO Save Error"), "");
            progressDialog.dismiss();

        } else if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE_APPROVAL)) {
            setWsSoProcess("");
            setRes("N-Service", hmAux_Trans.get("N-Service SO Approval Error"), "");
            mPresenter.getMenuItens(hmAux_Trans);
            progressDialog.dismiss();

        } else if (wsProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SYNC)) {
            changeCustomer();
        } else {
            progressDialog.dismiss();
        }
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
                startDownloadServices();
                //
                break;
            case Act005_Main.WS_PROCESS_ENABLE_NFC:
                alertTitle = hmAux_Trans.get("alert_enable_nfc_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_enable_nfc_finish_msg");
                invalidateOptionsMenu();
                break;

            case Act005_Main.WS_PROCESS_CANCEL_NFC:
                alertTitle = hmAux_Trans.get("alert_cancel_nfc_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_cancel_nfc_finish_msg");
                invalidateOptionsMenu();
                break;

            case Act005_Main.WS_PROCESS_SUPPORT:
                alertTitle = hmAux_Trans.get("alert_support_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_support_finish_msg");
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

    @Override
    public void callChangeCustomerProcess() {
        changeCustomer();
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
    public void cleanUpResults() {
        if (wsResults != null) {
            wsResults.clear();
        } else {
            wsResults = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        closeApp();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }

        EV_UserDao userDao = new EV_UserDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        EV_User user = userDao.getByString(
                new EV_User_Sql_001(
                        ToolBox_Con.getPreference_User_Code(getApplicationContext())
                ).toSqlQuery()
        );

        //
        //Menu Namoa logo
        menu.add(0, TOOLBAR_NAMOA_LOGO, Menu.FIRST + 0, getResources().getString(R.string.app_name));
        menu.findItem(TOOLBAR_NAMOA_LOGO).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.findItem(TOOLBAR_NAMOA_LOGO).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(TOOLBAR_NAMOA_LOGO).setTitle(getResources().getString(R.string.app_name));

        //
        //Menu Habilita nfc
        if (user.getNfc_blocked() == 1) {
//            Drawable nfc_icon = getDrawable(R.drawable.ic_nfc);
//            nfc_icon.setColorFilter(getResources().getColor(R.color.namoa_color_success_green), PorterDuff.Mode.SRC_ATOP);

            menu.add(0, TOOLBAR_ENABLE_NFC, Menu.FIRST + 1, hmAux_Trans.get("toolbar_enable_nfc"));
            menu.findItem(TOOLBAR_ENABLE_NFC).setIcon(R.drawable.ic_nfc_green);
            menu.findItem(TOOLBAR_ENABLE_NFC).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
            menu.findItem(TOOLBAR_ENABLE_NFC).setTitle(hmAux_Trans.get("toolbar_enable_nfc"));
        }

        //
        //Menu Cancela nfc
        if (user.getExist_nfc() == 1) {
//            Drawable nfc_icon2 = getDrawable(R.drawable.ic_nfc);
//            nfc_icon2.setColorFilter(getResources().getColor(R.color.namoa_color_danger_red), PorterDuff.Mode.SRC_ATOP);

            menu.add(0, TOOLBAR_CANCEL_NFC, Menu.FIRST + 2, hmAux_Trans.get("toolbar_cancel_nfc"));
            menu.findItem(TOOLBAR_CANCEL_NFC).setIcon(R.drawable.ic_nfc_red);
            menu.findItem(TOOLBAR_CANCEL_NFC).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
            menu.findItem(TOOLBAR_CANCEL_NFC).setTitle(hmAux_Trans.get("toolbar_cancel_nfc"));
        }
        //
        //Menu Suporte
        menu.add(0, TOOLBAR_SUPPORT, Menu.FIRST + 3, hmAux_Trans.get("toolbar_support"));
        menu.findItem(TOOLBAR_SUPPORT).setIcon(getResources().getDrawable(R.drawable.ic_file_upload_black_24dp));
        menu.findItem(TOOLBAR_SUPPORT).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.findItem(TOOLBAR_SUPPORT).setTitle(hmAux_Trans.get("toolbar_support"));

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

        DialogInterface.OnClickListener listener = null;

        switch (id) {

            case TOOLBAR_NAMOA_LOGO:
                return true;

            case TOOLBAR_ENABLE_NFC:
                alertTitle = hmAux_Trans.get("alert_enable_nfc_ttl");
                alertMsg = hmAux_Trans.get("alert_enable_nfc_msg");
                listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.executeEnableNFC();
                    }
                };
                break;

            case TOOLBAR_CANCEL_NFC:
                alertTitle = hmAux_Trans.get("alert_cancel_nfc_ttl");
                alertMsg = hmAux_Trans.get("alert_cancel_nfc_msg");
                listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.executeCancelNFC();
                    }
                };
                break;

            case TOOLBAR_SUPPORT:
                mPresenter.showSupportDialog();
                /*alertTitle = hmAux_Trans.get("alert_support_ttl");
                alertMsg = hmAux_Trans.get("alert_support_nfc_msg");
                listener =  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.showSupportDialog();
                        //mPresenter.executeSupport("MSG");
                    }
                };*/
                break;
            default:
                return true;
        }

        if (listener != null) {
            ToolBox.alertMSG(
                    Act005_Main.this,
                    alertTitle,
                    alertMsg,
                    listener,
                    1
            );
        }

        return true;
        // return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        ToolBox_Inf.mkDirectory();
        //
        mPresenter.getMenuItens(hmAux_Trans);
    }

    public void startDownloadServices() {

        Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
        Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
        Intent mIntentLogo = new Intent(context, WBR_DownLoad_Customer_Logo.class);
        Bundle bundle = new Bundle();
        mIntentPDF.putExtras(bundle);
        mIntentPIC.putExtras(bundle);
        mIntentLogo.putExtras(bundle);
        //
        context.sendBroadcast(mIntentPDF);
        context.sendBroadcast(mIntentPIC);
        context.sendBroadcast(mIntentLogo);
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
        //
        finish();
    }

    private class FCMReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.getMenuItens(hmAux_Trans);
        }
    }

    private class BR_Chat extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.CHAT_BR_TYPE);

            switch (type) {
                case Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE:
                    mPresenter.getMenuItens(hmAux_Trans);
                    break;
                case Constant.CHAT_BR_TYPE_CHAT_LOGGED_STATUS_CHANGE:
                    mPresenter.getMenuItens(hmAux_Trans);
                    break;
                default:
            }

        }
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
