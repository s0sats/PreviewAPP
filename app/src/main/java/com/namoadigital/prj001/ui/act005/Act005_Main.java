package com.namoadigital.prj001.ui.act005;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoa_digital.namoa_library.view.NamoaPermissionRequest;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act005_Adapter;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MainTagMenu;
import com.namoadigital.prj001.model.MenuMainNamoa;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.service.WS_AP_Save;
import com.namoadigital.prj001.service.WS_IO_Blind_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Outbound_Item_Save;
import com.namoadigital.prj001.service.WS_SO_Pack_Express_Local;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_015;
import com.namoadigital.prj001.sql.GE_File_Sql_001;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.ui.act004.Act004_Main;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act026.Act026_Main;
import com.namoadigital.prj001.ui.act030.Act030_Main;
import com.namoadigital.prj001.ui.act033.Act033_Main;
import com.namoadigital.prj001.ui.act034.Act034_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.ui.act036.Act036_Main;
import com.namoadigital.prj001.ui.act040.Act040_Main;
import com.namoadigital.prj001.ui.act046.Act046_Main;
import com.namoadigital.prj001.ui.act047.Act047_Main;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act068.Act068_Main;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.ui.act083.Act083_Main;
import com.namoadigital.prj001.ui.act084.Act084Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.SendResumeDialog;
import com.namoadigital.prj001.view.frag.frg_main_home.FrgMainHome;
import com.namoadigital.prj001.view.frag.frg_main_home_alt.FrgMainHomeAlt;

import org.jetbrains.annotations.NotNull;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.namoadigital.prj001.ui.act005.Act005_Main_Presenter_Impl.SYNC_FOR_TICKETS_FORM;
import static com.namoadigital.prj001.util.ConstantBaseApp.FCM_ACTION_TK_TICKET_UPDATE;
import static com.namoadigital.prj001.util.ConstantBaseApp.FCM_MODULE_SYNC;
import static com.namoadigital.prj001.util.ConstantBaseApp.FCM_MODULE_TICKET;
import static com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION;
import static com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION;
import static com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_FOCUS_FILTER;
import static com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION;
import static com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER;
import static com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_SITES_FILTER;
import static com.namoadigital.prj001.view.frag.frg_main_home.FrgMainHome.OnFrgMainHomeIteract;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act005_Main extends Base_Activity_Frag implements Act005_Main_View, Act005Opc.Act005DrawerInteraction,  OnFrgMainHomeIteract, FrgMainHomeAlt.OnFrgMainHomeAltInteract {

    public static final String MENU_ID = "menu_id";
    public static final String MENU_ICON = "menu_icon";
    public static final String MENU_DESC = "menu_desc";
    public static final String MENU_BADGE = "menu_badge";
    public static final String MENU_BADGE2 = "menu_badgeso";

    public static final String MENU_ID_CHECKLIST = "menu_checklist";
    public static final String MENU_ID_FORM_AP = "menu_form_ap";
    public static final String MENU_ID_TICKET = "menu_ticket";
    public static final String MENU_ID_SERVICE = "menu_service";
    public static final String MENU_ID_SERIAL = "menu_serial";
    public static final String MENU_ID_SCHEDULE_DATA = "menu_schedule_data";
    public static final String MENU_ID_PENDING_DATA = "menu_pending_data";
    public static final String MENU_ID_IO_ASSETS = "menu_io_assets";
    public static final String MENU_ID_HISTORIC_DATA = "menu_id_historic_data";
    public static final String MENU_ID_MESSAGES = "menu_messages";
    public static final String MENU_ID_FAKE = "menu_id_fake";

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
    public static final String WS_PROCESS_SO_PACK_EXPRESS = "ws_process_so_pack_express";

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
    //Constantes para a chave type do hmAux do wsResult
    public static final String WS_RESULT_TYPE_SERIAL = "SERIAL";
    public static final String WS_RESULT_TYPE_NFORM = "N-FORM";
    public static final String WS_RESULT_TYPE_AP = "A.P.";
    public static final String WS_RESULT_TYPE_SO = "S.O.";
    public static final String WS_RESULT_TYPE_SO_EXPRESS = "SO_EXPRESS";
    public static final String WS_RESULT_TYPE_ASSETS = "ASSETS";
    public static final String WS_RESULT_TYPE_ASSETS_MOVE_PLANNED = "ASSETS_MOVE_PLANNED";
    public static final String WS_RESULT_TYPE_ASSETS_MOVE = "ASSETS_MOVE";
    public static final String WS_RESULT_TYPE_ASSETS_INBOUND_ITEM = "ASSETS_INBOUND_ITEM";
    public static final String WS_RESULT_TYPE_ASSETS_OUTBOUND_ITEM = "ASSETS_OUTBOUND_ITEM";
    public static final String WS_RESULT_TYPE_TICKET = "TICKET";
    public static final String WS_RESULT_TYPE_GENERAL = "GENERAL";

    //toolbar constants
    private static final int TOOLBAR_NAMOA_LOGO = 1;
    private static final int TOOLBAR_ENABLE_NFC = 2;
    private static final int TOOLBAR_CANCEL_NFC = 3;
    private static final int TOOLBAR_SUPPORT = 4;
    private static final int TOOLBAR_SYNC_DATA_STATUS = 5;
    private static final int TOOLBAR_SEND_RECEIVE_DATA = 6;
    public static final int SETTINGS_FOR_DATETIME = 10001;

    private ArrayList<HMAux> wsResults = new ArrayList<>();

    private Context context;

    private Act005_Main_Presenter mPresenter;
    private Act005_Adapter mAdapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager fm;
    private Act005Opc fragOpc;

    private String alertTitle = "";
    private String alertMsg = "";

    private String wsProcess;
    private String wsSoProcess;

    //
    private ArrayList<HMAux> wsProcessList = new ArrayList<>();

    private FCMReceiver fcmReceiver;
    private BR_Chat chatReceiver;
    private boolean syncAfterSave;
    private SendResumeDialog sendResumeDialog;
    private int sOProcessErrorAmount=0;
    private int sOProcessAmount=0;
    private int assetsProcessErrorAmount=0;
    private String move_planned[];
    private String blinds[];
    ArrayList<HMAux> inbound_items;
    int inboundItensTotal=0;
    int outboundItensTotal=0;
    ArrayList<HMAux> outbound_items;
    Toolbar toolbar;
    private boolean masterDataSyncFlow = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act005_main);
        //LUCHE - 24/06/2020 Worker de agendamento
        ToolBox_Inf.scheduleQuarterScheduleNotification(context);
        ToolBox_Inf.schedule4HoursScheduleNotification(context);
        ToolBox_Inf.scheduleCleanningWork(context);
        //LUCHE - 06/07/2021 - Add chamada aqui, pois se tem arquivo de outro usr deve ser enviado
        ToolBox_Inf.scheduleUploadOtherUserImgWork(context);
        //
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
        //LUCHE - 12/02/2021 - substituido IntentService pelo worker
        ToolBox_Inf.scheduleFirebaseRegistrationWork(context);
        //LUCHE - 22/02/2021 - Comentado chamada pois agora não será recorrente será apenas quando FCM de ROom
        //ToolBox_Inf.scheduleWorkQuarterChatRefresh();
        //
        ToolBox_Inf.scheduleDownloadPictureWork(context);
        ToolBox_Inf.scheduleDownloadCustomerLogoWork(context);
        //
        /*
            BARRIONUEVO 02-02-2021
            Remocao de ScreenStatusService para Android 10+
       */
       /* if (ToolBox_Inf.isUsrAppLogged(context) && !ScreenStatusService.isRunning) {
            Intent mScreenStatusService = new Intent(context, ScreenStatusService.class);
            context.startService(mScreenStatusService);
        }*/
        //
        fcmReceiver = new FCMReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.WS_FCM);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(fcmReceiver, filter);
        //
        // Hugo Reativou
        chatReceiver = new BR_Chat();
        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(chatReceiver, brRoomFilter);
        /*
         * Desloga user se preferencia login_status != OK
         */
        if (ToolBox_Con.getPreference_Status_Login(context).equals(Constant.LOGIN_STATUS_SESSION_NOT_FOUND)) {
            forceLogoutBySessionNotFound();
        }
        /**
         * BARRIONUEVO - 30/04/2020
         * Metodo que chama serviço de localizacao caso usuario esteja logado, o servico parado e
         * pendecias de envio.
         */
        if (!SV_LocationTracker.status && ToolBox_Inf.isUsrAppLogged(context)) {
            int pendencies = ToolBox_Inf.getLocationPendencies(context);
            if(pendencies>0) {
                retryGetLocation();
            }
        }

        setFragments();

        ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
        /**
         *  BARRIONUEVO 14-06-2021
         *  Remocao de foco no menu do hamburguer e do menu calendario.
         */
        mDrawerLayout.requestFocus();
    }

    private void retryGetLocation() {
        requestPermissions(
                Act005_Main.this,
                NamoaPermissionRequest.MULTIIPLE_PERMISSION_REQUEST_WITHOUT_RATIONALE,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                new NamoaPermissionRequest() {
                    @Override
                    public void accessGranted() {
                        ToolBox_Inf.call_Location_Tracker_On_Background(context, SV_LocationTracker.LOCATION_BACKGROUND);
                    }

                    @Override
                    public void accessDenied(final String[] permissions) {
                        showPermissionRationaleDialog(
                                Act005_Main.this,
                                com.namoa_digital.namoa_library.R.drawable.ic_alert_n,
                                hmAux_Trans.get("alert_gps_denied_permission_ttl"),
                                hmAux_Trans.get("alert_gps_denied_permission_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        callRequestPermission(MULTIIPLE_PERMISSION_REQUEST_WITHOUT_RATIONALE,permissions);
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }
                        );
                    }

                    @Override
                    public void requestPermissionRationale(final String[] permissions) {

                        showPermissionRationaleDialog(
                                Act005_Main.this,
                                com.namoa_digital.namoa_library.R.drawable.ic_alert_n,
                                hmAux_Trans.get("alert_gps_rationale_permission_ttl"),
                                hmAux_Trans.get("alert_gps_rationale_permission_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        callRequestPermission(MULTIIPLE_PERMISSION_REQUEST_WITHOUT_RATIONALE,permissions);
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }
                        );
                    }

                    @Override
                    public void accessDeniedNeverAskAgain(String[] permissions) {

                        showPermissionNeverAskAgainDialog(
                                R.drawable.ic_location_on_24,
                                hmAux_Trans.get("alert_gps_never_ask_again_permission_ttl"),
                                hmAux_Trans.get("alert_gps_never_ask_again_permission_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }
                        );
                    }

                    @Override
                    public void informAppDetailSettingsReturn() {
                        callRequestPermission(MULTIIPLE_PERMISSION_REQUEST_WITHOUT_RATIONALE,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
                    }
                }
        );
    }

    private void forceLogoutBySessionNotFound() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_logout_data_to_send_ttl"),
                hmAux_Trans.get("alert_logout_data_to_send_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Limpa a sessão local
                        mPresenter.clearLocalSession();
                        //
                        if (mPresenter.existOthersSession()) {
                            changeCustomer();
                        } else {
                            processLogin();
                        }
                    }
                },
                0
        );
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver);
        //
        // Hugo Reativou
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatReceiver);
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
        transList.add("lbl_so_express");
        transList.add("lbl_assets_move");
        transList.add("lbl_assets_outbound");
        transList.add("lbl_assets_inbound");
        transList.add("lbl_serial_data");
        transList.add("lbl_ticket");
        transList.add("lbl_assets");
        transList.add("alert_ws_assets_error_msg");
        transList.add("alert_ws_ticket_error_msg");
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
        transList.add("alert_support_contact");

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
        transList.add("alert_support_hint");
        transList.add("alert_support_contact_hint");
        transList.add("alert_support_empty_msg");
        transList.add("alert_support_empty_contact");
        //
        transList.add("lbl_change_zone");
        transList.add("drawer_change_zone_one_zone_alert_ttl");
        transList.add("drawer_change_zone_one_zone_alert_msg");
        transList.add("drawer_change_zone_alert_ttl");
        transList.add("drawer_change_zone_alert_msg");
        //
        transList.add("lbl_chat");
        //
        transList.add("alert_send_to_sync_ttl");
        transList.add("alert_send_to_sync_msg");
        transList.add("progress_so_save_ttl");
        transList.add("progress_so_save_msg");
        transList.add("progress_ap_save_ttl");
        transList.add("progress_ap_save_msg");
        transList.add("alert_ws_so_error_msg");
        transList.add("alert_ws_ap_error_msg");
        transList.add("alert_ws_general_error_ttl");
        transList.add("alert_ws_general_error_msg");
        transList.add("alert_results_ttl");
        transList.add("alert_ws_serial_error_msg");
        //
        transList.add("alert_forced_logout_ttl");
        transList.add("alert_forced_logout_msg");
        //
        transList.add("alert_data_to_send_ttl");
        transList.add("alert_data_to_send_msg");
        //
        transList.add("alert_changecustomer_data_to_send_ttl");
        transList.add("alert_changecustomer_data_to_send_msg");
        //
        transList.add("alert_logout_data_to_send_ttl");
        transList.add("alert_logout_data_to_send_msg");
        //
        transList.add("lbl_unfinished_data");
        transList.add("alert_pending_data_ttl");
        transList.add("alert_pending_data_msg");
        transList.add("alert_pending_form_logout_msg");
        transList.add("alert_site_or_operation_not_found_ttl");
        transList.add("alert_site_or_operation_not_found_msg");
        transList.add("lbl_io_assets");
        transList.add("alert_site_no_io_control_ttl");
        transList.add("alert_site_no_io_control_msg");
        transList.add("alert_unsent_img_copy_error_ttl");
        transList.add("alert_unsent_img_copy_error_msg");
        transList.add("alert_unsent_gps_nform_ttl");
        transList.add("alert_unsent_gps_nform_msg");
        //Ws_Sync do Form de ticket
        transList.add("progress_sync_tickets_form_ttl");
        transList.add("progress_sync_tickets_form_msg");
        //Ws_Sync do ticket
        transList.add("progress_download_ticket_ttl");
        transList.add("progress_download_ticket_start");
        //
        transList.add("lbl_invalid_datetime_warning");
        transList.add("alert_invalid_local_datetime_ttl");
        transList.add("alert_invalid_local_datetime_msg");
        transList.add("alert_go_to_settings");
        //
        transList.add("alert_gps_rationale_permission_ttl");
        transList.add("alert_gps_rationale_permission_msg");
        transList.add("alert_gps_denied_permission_ttl");
        transList.add("alert_gps_denied_permission_msg");
        transList.add("alert_gps_never_ask_again_permission_ttl");
        transList.add("alert_gps_never_ask_again_permission_msg");
        //
        transList.add("drawer_historic_lbl");
        transList.add("drawer_loading_lbl");
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
                ),
                new GE_Custom_Form_ApDao(context),
                new SO_Pack_Express_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new CH_MessageDao(context)
        );
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
        //mPresenter.getMenuItens(hmAux_Trans);
//        mPresenter.getMenuItensV2(hmAux_Trans);
        //
        syncAfterSave = false;
        //
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
                //
//                if(fragOpc != null){
//                    fragOpc.setPendingForms(getPendingForms());
//                }
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
//        fragOpc = (Act005_Opc_Bkp) fm.findFragmentById(R.id.act005_frag_opc);
//        fragOpc.setHmAux_Trans(hmAux_Trans, mModule_Code, mResource_Code);
//        fragOpc.setPendingForms(getPendingForms());
//        fragOpc.setOnOpcItemClicked(new Act005_Opc_Bkp.IAct005_Opc() {
//            @Override
//            public void itemClicked(String index) {
//                DialogInterface.OnClickListener listener = null;
//                int negativeBtn = 1;
//
//                switch (index) {
//                    case Act005_Opc_Bkp.DRAWER_OPC_CUSTOMER:
//                        //
//                        if (getSendBadgeQty() > 0 || getImagesToUpload() > 0) {
//
//                            ToolBox.alertMSG(
//                                    context,
//                                    hmAux_Trans.get("alert_changecustomer_data_to_send_ttl"),
//                                    hmAux_Trans.get("alert_changecustomer_data_to_send_msg"),
//                                    null,
//                                    -1,
//                                    null
//                            );
//
//                        } else {
//                            if(!ToolBox_Inf.isLocalDatetimeOk(context)){
//                                handleInvalidLocalDatetime();
//                            }else {
//                                alertTitle = hmAux_Trans.get("drawer_change_customer_alert_ttl");
//                                alertMsg = hmAux_Trans.get("drawer_change_customer_alert_msg");
//                                //
//                                listener = new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        //if(ToolBox_Con.isOnline(context)) {
//                                        //Reseta preferencias do Customer e volta para
//                                        //Act002 - lista de customer
//                                        changeCustomer();
////                                }else{
////                                    ToolBox_Inf.showNoConnectionDialog(Act005_Main.this);
////                                }
//                                    }
//                                };
//                            }
//                        }
//
//                        break;
//                    case Act005_Opc_Bkp.DRAWER_OPC_SITE:
//                        MD_SiteDao siteDao = new MD_SiteDao(
//                                context,
//                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                                Constant.DB_VERSION_CUSTOM
//                        );
//
//                        int qty_sites = siteDao.query_HM(
//                                new MD_Site_Sql_002(
//                                        ToolBox_Con.getPreference_Customer_Code(context)
//                                ).toSqlQuery()
//                        ).size();
//
//                        if (qty_sites <= 1) {
//                            //Se apenas um site, da alert e não permite troca.
//                            alertTitle = hmAux_Trans.get("drawer_change_site_one_site_alert_ttl");
//                            alertMsg = hmAux_Trans.get("drawer_change_site_one_site_alert_msg");
//
//                            listener = new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            };
//
//                            negativeBtn = 0;
//                        } else {
//                            //
//                            alertTitle = hmAux_Trans.get("drawer_change_site_alert_ttl");
//                            alertMsg = hmAux_Trans.get("drawer_change_site_alert_msg");
//                            //
//                            //Apaga preferencia de Site, Operatione volta a lista de site
//                            listener = new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    //Reseta preferencias do Customer e volta para
//                                    ToolBox_Con.setPreference_Site_Code(context, "-1");
//                                    ToolBox_Con.setPreference_Zone_Code(context, -1);
//                                    ToolBox_Con.setPreference_Operation_Code(context, -1);
//                                    //
//                                    callAct003(context);
//                                }
//                            };
//                        }
//                        break;
//                    case Act005_Opc_Bkp.DRAWER_OPC_ZONE:
//                        MD_Site_ZoneDao zoneDao = new MD_Site_ZoneDao(
//                                context,
//                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                                Constant.DB_VERSION_CUSTOM
//                        );
//
//                        int qty_zones = zoneDao.query_HM(
//                                new MD_Site_Zone_Sql_002(
//                                        ToolBox_Con.getPreference_Customer_Code(context),
//                                        Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context))
//                                ).toSqlQuery()
//                        ).size();
//
//                        if (qty_zones <= 1) {
//                            //Se apenas um site, da alert e não permite troca.
//                            alertTitle = hmAux_Trans.get("drawer_change_zone_one_zone_alert_ttl");
//                            alertMsg = hmAux_Trans.get("drawer_change_zone_one_zone_alert_msg");
//
//                            listener = new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            };
//
//                            negativeBtn = 0;
//                        } else {
//                            //
//                            alertTitle = hmAux_Trans.get("drawer_change_zone_alert_ttl");
//                            alertMsg = hmAux_Trans.get("drawer_change_zone_alert_msg");
//                            //
//                            //Apaga preferencia de zona volta a lista de zona
//                            listener = new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    //Reseta preferencias da zona e volta para
//                                    ToolBox_Con.setPreference_Zone_Code(context, -1);
//                                    //
//                                    callAct033(context);
//                                }
//                            };
//                        }
//                        break;
//                    case Act005_Opc_Bkp.DRAWER_OPC_OPERATION:
//                        MD_OperationDao operationDao = new MD_OperationDao(
//                                context,
//                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                                Constant.DB_VERSION_CUSTOM
//                        );
//
//                        int qty_operation = operationDao.query_HM(
//                                new MD_Operation_Sql_001(
//                                        ToolBox_Con.getPreference_Customer_Code(context)
//                                ).toSqlQuery()
//                        ).size();
//
//                        if (qty_operation <= 1) {
//                            //Se apenas uma operação, da alert e não permite troca.
//                            alertTitle = hmAux_Trans.get("drawer_change_operation_one_operation_alert_ttl");
//                            alertMsg = hmAux_Trans.get("drawer_change_operation_one_operation_alert_msg");
//
//                            listener = new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            };
//
//                            negativeBtn = 0;
//                        } else {
//                            //
//                            alertTitle = hmAux_Trans.get("drawer_change_operation_alert_ttl");
//                            alertMsg = hmAux_Trans.get("drawer_change_operation_alert_msg");
//                            //
//                            listener = new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    //Apaga preferencia de Operatione volta a ista de operation
//                                    ToolBox_Con.setPreference_Operation_Code(context, -1);
//                                    //
//                                    callAct004(context);
//                                }
//                            };
//
//                        }
//
//                        break;
//                    case Act005_Opc_Bkp.DRAWER_OPC_LOGOUT:
//                        /*
//                         *
//                         *
//                         * Esse case não funciona mais, o metodo chamado no "botão logout" é
//                         * a interface logoutClicked()
//                         *
//                         * */
//                        //
//                        alertTitle = hmAux_Trans.get("drawer_logout_alert_ttl");
//                        alertMsg = hmAux_Trans.get("drawer_logout_alert_msg");
//                        //
//                        listener = new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ToolBox_Con.cleanPreferences(Act005_Main.this);
//                                ToolBox_Inf.call_Act001_Main(Act005_Main.this);
//                                finish();
//                            }
//                        };
//                        break;
//                    default:
//                        break;
//                }
//                //Verifica se listner foi setado,
//                //se foi, exibe Dialog.
//                if (listener != null) {
//                    ToolBox.alertMSG(
//                            Act005_Main.this,
//                            alertTitle,
//                            alertMsg,
//                            listener,
//                            negativeBtn
//                    );
//
//                }
//                //Fecha Drawer
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//            }
//
//            @Override
//            public void syncClicked() {
//
//                ToolBox.alertMSG(
//                        Act005_Main.this,
//                        hmAux_Trans.get("drawer_sync_alert_ttl"),
//                        hmAux_Trans.get("drawer_sync_alert_msg"),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //mPresenter.accessMenuItem(MENU_ID_SYNC_DATA, 0);
//                                mPresenter.syncFlow(mAdapter.getBadgeQty(MENU_ID_SEND_DATA));
//                            }
//                        },
//                        1
//                );
//
//            }
//
//            @Override
//            public void logoutClicked() {
//                if (getSendBadgeQty() > 0 || getImagesToUpload() > 0) {
//                    //
//                    callSendAction("LOGOUT");
//                } else if (getPendingForms()) {
//                    ToolBox.alertMSG_YES_NO(
//                            Act005_Main.this,
//                            hmAux_Trans.get("alert_pending_data_ttl"),
//                            hmAux_Trans.get("alert_pending_form_logout_msg"),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    mPresenter.showLogoutDialog();
//                                }
//                            },
//                            2,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                                }
//                            }
//                    );
//
//                } else {
//                    mPresenter.showLogoutDialog();
//                }
//            }
//        });
          initilizeDrawer();


        //
    }

    /**
     * Metodo que inicializa o frag do drawer
     */
    private void initilizeDrawer() {
        fragOpc = (Act005Opc) fm.findFragmentById(R.id.act005_frag_opc);
        if(fragOpc != null) {
            fragOpc.setHmAux_Trans(hmAux_Trans);
        }
    }

    @NotNull
    @Override
    public Bitmap getCustomerLogo() {return mPresenter.getLogoBitmap();}

    @Override
    public boolean hasPendencies() {
        return getPendingForms();
    }

    @Override
    public boolean showEnableNfcOption() {
        return mPresenter.showEnableNfcOption();
    }

    @Override
    public boolean showDisableNfcOption() {
        return mPresenter.showDisableNfcOption();
    }

    @Override
    public boolean showChangeCustomerOption() {
        return mPresenter.showChangeCustomerOption();
    }

    @Override
    public void onHistoricClick() {
        //callAct014(context);
        if(mPresenter.hasSOProfile()){
            callAct014(context);
        }else {
            callAct084();
        }
    }

    @Override
    public void onEnableNfcClick() {
        showDrawerAlertConfirm(
            hmAux_Trans.get("alert_enable_nfc_ttl"),
            hmAux_Trans.get("alert_enable_nfc_msg"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.executeEnableNFC();
                }
            }
        );

    }

    @Override
    public void onDisableNfcClick() {
        showDrawerAlertConfirm(
            hmAux_Trans.get("alert_cancel_nfc_ttl"),
            hmAux_Trans.get("alert_cancel_nfc_msg"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.executeCancelNFC();
                }
            }
        );
    }

    @Override
    public void onSupportDataClick() {
        mPresenter.showSupportDialog();
    }

    @Override
    public void onChangeCustomerClick() {
        if (getSendBadgeQty() > 0 || getImagesToUpload() > 0) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_changecustomer_data_to_send_ttl"),
                    hmAux_Trans.get("alert_changecustomer_data_to_send_msg"),
                    null,
                    -1,
                    null
            );

        } else {
            if(!ToolBox_Inf.isLocalDatetimeOk(context)){
                handleInvalidLocalDatetime();
            }else {
                ToolBox.alertMSG_YES_NO(
                    context,
                    hmAux_Trans.get("drawer_change_customer_alert_ttl"),
                    hmAux_Trans.get("drawer_change_customer_alert_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            changeCustomer();
                        }
                    },
                    1
                );
            }
        }
    }

    @Override
    public void onLogoutClick() {
        if (getSendBadgeQty() > 0 || getImagesToUpload() > 0) {
            //
            callSendAction("LOGOUT");
        } else if (getPendingForms()) {
            ToolBox.alertMSG_YES_NO(
                    Act005_Main.this,
                    hmAux_Trans.get("alert_pending_data_ttl"),
                    hmAux_Trans.get("alert_pending_form_logout_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mPresenter.showLogoutDialog();
                        }
                    },
                    2,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
            );

        } else {
            mPresenter.showLogoutDialog();
        }
    }

    private void showDrawerAlertConfirm(String ttl, String msg, DialogInterface.OnClickListener clickListener) {
        ToolBox.alertMSG_YES_NO(
            context,
            ttl,
            msg,
            clickListener,
            1
        );
    }

    @Override
    public boolean getPendingForms() {

//        GE_Custom_Form_LocalDao geCustomFormLocalDao = new GE_Custom_Form_LocalDao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//        );
//
//        ArrayList<HMAux> openedSessions = ToolBox_Inf.getActiveCustomerSession(context);
//        ArrayList<HMAux> pendingTotals = (ArrayList<HMAux>) geCustomFormLocalDao.query_HM(
//                new GE_Custom_Form_Local_Sql_015().toSqlQuery()
//        );
//
//        boolean pendingExists = false;
//
//        for (int i = 0; i < openedSessions.size(); i++) {
//            for (int j = 0; j < pendingTotals.size(); j++) {
//                if (openedSessions.get(i).get("customer_code").equalsIgnoreCase(pendingTotals.get(j).get("customer_code"))) {
//                    pendingExists = true;
//                    break;
//                }
//            }
//        }
//
//        return pendingExists;

        boolean pendingExists = false;
        ArrayList<HMAux> openedSessions = ToolBox_Inf.getActiveCustomerSession(context);
        //
        for (int i = 0; i < openedSessions.size(); i++) {
            GE_Custom_Form_LocalDao geCustomFormLocalDao = new GE_Custom_Form_LocalDao(
                    context,
                    ToolBox_Con.customDBPath(
                            Long.parseLong(openedSessions.get(i).get(EV_User_CustomerDao.CUSTOMER_CODE))
                    ),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            HMAux pendingTotals = geCustomFormLocalDao.getByStringHM(
                    new GE_Custom_Form_Local_Sql_015(
                            openedSessions.get(i).get(EV_User_CustomerDao.CUSTOMER_CODE)
                    ).toSqlQuery()
            );
            int qtyPendings = pendingTotals == null ? 0 : ToolBox_Inf.convertStringToInt(pendingTotals.get(GE_Custom_Form_Local_Sql_015.PENDING_QTY));
            //
            if(qtyPendings > 0){
                pendingExists = true;
                break;
            }
        }
        //
        return pendingExists;

    }

    @Override
    public void callAct051(Context context) {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct040(Context context) {
        Intent mIntent = new Intent(context, Act040_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }


    public void callAct026(Context context) {
        Intent mIntent = new Intent(context, Act026_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct068(Context context) {
        Intent mIntent = new Intent(context, Act068_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct069(Context context) {
        Intent mIntent = new Intent(context, Act069_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private void changeCustomer() {
        ToolBox_Con.resetCustomerSiteOperationPreferences(context);
        //
        callAct002(context);

    }

    private void initActions() {
//        gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MenuMainNamoa item = (MenuMainNamoa) parent.getItemAtPosition(position);
//                mPresenter.accessMenuItem(item.getMenu_id(), 0);
//            }
//        });

    }

    public void loadMenuV2(ArrayList<MenuMainNamoa> menus, int columnsQty) {
//        gv_menu.setNumColumns(columnsQty);
        int idxFakeSpaceStart = mPresenter.processFakeMenus(menus,columnsQty);
        //
        mAdapter = new Act005_Adapter(
            context,
            R.layout.act005_item_menu_badge,
            menus,
            idxFakeSpaceStart,
            columnsQty
        );
        //
//        gv_menu.setAdapter(mAdapter);
        //
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT005;
        mAct_Title = Constant.ACT005 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        //setTitleLanguage();
        setTitleAsCustomerName();
        //
        setFooter();
        //TRATATIVA ESPECIFICA DA ACT005
        if( mFooter.containsKey(Constant.FOOTER_SITE_NOT_FOUND)
            || mFooter.containsKey(Constant.FOOTER_OPERATION_NOT_FOUND)
        ){
            String msg = hmAux_Trans.get("alert_site_or_operation_not_found_msg");
            //
//            if( mFooter.containsKey(Constant.FOOTER_SITE_NOT_FOUND)){
//                msg += mFooter.get(Constant.FOOTER_SITE_NOT_FOUND) +"\n";
//            }
//            if( mFooter.containsKey(Constant.FOOTER_OPERATION_NOT_FOUND)){
//                msg += mFooter.get(Constant.FOOTER_OPERATION_NOT_FOUND) +"\n";
//            }
            //
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_site_or_operation_not_found_ttl"),
                    msg,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeCustomer();
                        }
                    },
                    0
            );
        }
    }

    private void setTitleAsCustomerName() {
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null) {
            supportActionBar.setTitle( ToolBox_Con.getPreference_Customer_Code_NAME(context));
        }else{
            setTitleLanguage();
        }
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();

        ToolBox_Inf.buildFooterDialog(context, true);
    }
    /*
        BARRIONUEVO 17-04-2020
            - Atualiza info do footer
            - Recarrega gridview com site, zona e operação aplicados.
     */
    @Override
    protected void processRefreshMessage(String mType, String mValue, String mActivity) {
        super.processRefreshMessage(mType, mValue, mActivity);
        /*
            BARRIONUEVO 17-04-2020
            desabilita acesso a modulos enquanto atuliza tela.
         */
//        gv_menu.setClickable(false);
        refreshUiData();
//        mPresenter.getMenuItensV2(hmAux_Trans);
        iniUIFooter();
//        gv_menu.setClickable(true);
    }

    private void refreshTagList() {
        FrgMainHome currentFragment = (FrgMainHome) fm.findFragmentById(R.id.act005_frg_placeholder);
        currentFragment.refreshList(getTagList( ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_PERIOD_FILTER, PREFERENCE_HOME_ALL_TIME_OPTION),
                ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_SITES_FILTER, PREFERENCE_HOME_ALL_SITE_OPTION),
                ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_FOCUS_FILTER, PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)));
        currentFragment.setDatetimeVisibility();
    }

    private void refreshModuleList() {
        FrgMainHomeAlt currentFragment = (FrgMainHomeAlt) fm.findFragmentById(R.id.act005_frg_placeholder);
        currentFragment.refreshModuleList();
        currentFragment.setDatetimeVisibility();
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
            case SYNC_FOR_TICKETS_FORM:
                alertTitle = hmAux_Trans.get("progress_sync_tickets_form_ttl");
                alertMsg = hmAux_Trans.get("progress_sync_tickets_form_msg");
                break;
            case Act005_Main_Presenter_Impl.SYNC_TICKETS:
                alertTitle =  hmAux_Trans.get("progress_download_ticket_ttl");
                alertMsg = hmAux_Trans.get("progress_download_ticket_start");
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
    public void setSyncAfterSave(boolean syncAfterSave) {
        this.syncAfterSave = syncAfterSave;
    }

    @Override
    public int getSendBadgeQty() {
        return mPresenter.hasUpdateRequired() ? 1 : 0;
    }

    @Override
    public int getImagesToUpload() {
        GE_FileDao geFileDao = new GE_FileDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
        );

        ArrayList<GE_File> geFiles;

        geFiles = (ArrayList<GE_File>) geFileDao.query(
                new GE_File_Sql_001().toSqlQuery()
        );

        return geFiles.size();
    }

    @Override
    public int getOpenForms(HMAux customers) {
        return 0;
    }

    @Override
    public void callAct006(Context context) {
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
    public void callAct046(Context context) {
        Intent mIntent = new Intent(context, Act046_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct018(Context context) {
        Intent mIntent = new Intent(context, Act083_Main.class);
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

    private void callAct083(MainTagMenu mainTagMenu) {
        Intent mIntent = new Intent(context, Act083_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = mPresenter.getAct083BundleParams(mainTagMenu);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    private void callAct084() {
        Intent mIntent = new Intent(context, Act084Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

                        if (getSendBadgeQty() > 0 || getImagesToUpload() > 0) {
                            callSendAction("EXIT");
                        } else {
                            ToolBox_Con.getPreference_MessageClear(getApplicationContext()).equalsIgnoreCase("");
                            //
                            finish();
                        }
                    }
                },
                1
        );

    }

    public void callSendAction(final String sAction) {
        switch (sAction.toUpperCase()) {
            case "EXIT":
                callExitAction();
                break;
            case "LOGOUT":
                callLogOutAction();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    private void callExitAction() {
        ToolBox.alertMSG_YES_NO(
                Act005_Main.this,
                hmAux_Trans.get("alert_data_to_send_ttl"),
                hmAux_Trans.get("alert_data_to_send_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.accessMenuItem(Act005_Main.MENU_ID_SEND_DATA, 0);
                        ToolBox_Inf.scheduleUploadImgWork(context);
                    }
                },
                2,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToolBox_Inf.scheduleUploadImgWork(context);
                        //
                        ToolBox_Con.getPreference_MessageClear(getApplicationContext()).equalsIgnoreCase("");
                        //
                        finish();
                    }
                }
        );
    }

    private void callLogOutAction() {
        ToolBox.alertMSG(
                Act005_Main.this,
                hmAux_Trans.get("alert_logout_data_to_send_ttl"),
                hmAux_Trans.get("alert_logout_data_to_send_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (ToolBox_Con.isOnline(context)) {
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            mPresenter.accessMenuItem(Act005_Main.MENU_ID_SEND_DATA, 0);
                            ToolBox_Inf.scheduleUploadImgWork(context);
                        }
                    }
                },
                -1,
                null
        );
    }

    //TRATA UPDATE_REQUIRED - CANCEL
    @Override
    protected void processGo() {
        super.processGo();
        //
        if(ToolBox_Con.getPreference_BkpUnsentImg(context)){
            ToolBox_Con.setPreference_BkpUnsentImg(context,false);
            //
            ToolBox_Inf.scheduleUploadImgWork(context);
        }

        mPresenter.executeSyncProcess(1);
    }

    //TRATA UPDATE_REQUIRED - OK
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);
        //
        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);

        /**
         * LUCHE - 13/05/2019
         * Se usr decidiu atualizar e há troca de versão do banco,
         * busca imagens pendentes de transmissao e tenta a copia das imagens.
         * EM CASO DE ERRO AO COPIAR IMGS, IMPEDE ATUALIZAÇÃO DE SOFTWARE
         *
         */
        if(ToolBox_Con.getPreference_BkpUnsentImg(context)){
            if(!ToolBox_Inf.moveUnsentImgs(context)){
                progressDialog.dismiss();
                //
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_unsent_img_copy_error_ttl"),
                    hmAux_Trans.get("alert_unsent_img_copy_error_msg"),
                    null,
                    0
                );
            }else{
                //Reseta preferencia
                ToolBox_Con.setPreference_BkpUnsentImg(context,false);
                //
                ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
            }
        }else{
            ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        }
    }

    private void setRes(String type, String label, String status, String final_status) {
        HMAux res = new HMAux();

        res.put("type", type);
        res.put("label", label);
        res.put("status", status);
        res.put("final_status", final_status);

        wsResults.add(res);
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);


        if (!wsProcess.equals("")) {
            if (wsProcess.equals(Act005_Main.WS_PROCESS_LOGOUT)) {
                progressDialog.dismiss();
                //Atualiza lbl de form pendentes
                //TODO VER IF ABAIXO APLICA NO NOVO DRAWER
//                if(fragOpc != null){
//                    fragOpc.setPendingForms(getPendingForms());
//                }
                //
                if (ToolBox_Con.getPreference_Customer_Code(context) == -1L) {
                    mPresenter.stopChatServices();
                    //
                    if (mPresenter.existOthersSession()) {
                        //
                        cleanLocalSession();
                        //
                        changeCustomer();
                    } else {
                        processLogin();
                    }
                } else {
                }
            } else {
                if (!wsSoProcess.equalsIgnoreCase(WS_Save.class.getSimpleName())
                && !wsSoProcess.equalsIgnoreCase(SYNC_FOR_TICKETS_FORM)) {
                    progressDialog.dismiss();
                    showSuccessDialog();
                    //Atualiza traduções
                    loadTranslation();
                    //Atualiza menu e os badges
                    //mPresenter.getMenuItens(hmAux_Trans);
//                    mPresenter.getMenuItensV2(hmAux_Trans);
                    //Fecha Drawer
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    //LUCHE - 27/02/2020
                    //Add tratativa pós save após implementação do novo agendamento
                    processCloseACT(mLink, mRequired, new HMAux());
                }
            }
        } else {
            progressDialog.dismiss();
        }
    }

    private void cleanLocalSession() {

    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (wsSoProcess.equalsIgnoreCase(WS_Serial_Save.class.getSimpleName())) {
            setWsSoProcess("");
            //
            int errorAmount = 0;
            if (!hmAux.isEmpty() && hmAux.size() > 0) {
                for (Map.Entry<String, String> item : hmAux.entrySet()) {
                    HMAux aux = new HMAux();
                    String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                    String status = item.getValue();
                    String productInfo = mPresenter.getProductInfo(Long.parseLong(pk[0]));
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", "" + productInfo + " - " + pk[1]);
                    mHmAux.put("type", WS_RESULT_TYPE_SERIAL);
                    mHmAux.put("status", status);
                    mHmAux.put("final_status", productInfo + " - " + pk[1] + " / " + status);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                        errorAmount++;
                    }
                }
            }

            if(errorAmount > 0){
                try {
                    sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_serial, false, errorAmount,hmAux.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_serial, true, hmAux.size(), hmAux.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mPresenter.executeSaveProcess();

        } else if (wsSoProcess.equalsIgnoreCase(WS_Save.class.getSimpleName())) {
            setWsSoProcess("");
            //LUCHE - 27/02/2020
            //Add tratativa pós save após implementação do novo agendamento
            mPresenter.processWS_SaveReturn(mLink);

            mPresenter.executeApSave(); // 3

        } else if (wsSoProcess.equalsIgnoreCase(WS_AP_Save.class.getSimpleName())) {
            setWsSoProcess("");
            int errorAmount = 0;
            for (String sKey : hmAux.keySet()) {
                HMAux mHmAux = new HMAux();
                //
                String[] res = hmAux.get(sKey).split(Constant.MAIN_CONCAT_STRING);

                mHmAux.put("label", res[0]);
                mHmAux.put("type", WS_RESULT_TYPE_AP);
                mHmAux.put("status", (res[1].equals("1") ? "OK" : res[1]));
                mHmAux.put("final_status", ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(res[0]), 20) + " - " + (res[1].equals("1") ? "OK" : res[1]));
                //
                if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                    wsResults.add(mHmAux);
                    errorAmount++;
                }
            }
            if(errorAmount > 0 ){
                try {
                    sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_form_ap, false, errorAmount, hmAux.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_form_ap, true, hmAux.size(), hmAux.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mPresenter.executeSOPackExpress();  // 4

        } else if (wsSoProcess.equalsIgnoreCase(WS_SO_Pack_Express_Local.class.getSimpleName())) {
            setWsSoProcess("");
            //
            int errorAmount = 0;
            if (!hmAux.isEmpty() && hmAux.size() > 0) {
                for (Map.Entry<String, String> item : hmAux.entrySet()) {
                    HMAux aux = new HMAux();
                    String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                    ;
                    String status = item.getValue();
                    String soInfo = pk[0];
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", "" + soInfo + "  -  " + pk[2] + "\n" + pk[1]);
                    mHmAux.put("type", WS_RESULT_TYPE_SO_EXPRESS);
                    mHmAux.put("status", status);
                    mHmAux.put("final_status", soInfo + " / " + status);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                        errorAmount++;
                    }
                }
            }

            if(errorAmount>0){
                try {
                    sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_express_so, false, errorAmount, hmAux.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_express_so, true, hmAux.size(),hmAux.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mPresenter.executeSoSave();  // 5

        } else if (wsSoProcess.equalsIgnoreCase(WS_PROCESS_SO_SAVE)) {
            setWsSoProcess("");

            String approval[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            if (approval.length > 0 && !approval[0].isEmpty()) {
                sOProcessAmount +=approval.length;
                for (int i = 0; i < approval.length; i++) {
                    String fields[] = approval[i].split(Constant.MAIN_CONCAT_STRING_2);
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", fields[0]);
                    mHmAux.put("type", WS_RESULT_TYPE_SO);
                    mHmAux.put("status", fields[1]);
                    mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                        sOProcessErrorAmount++;
                    }
                }
            }

            mPresenter.executeSoSaveApproval();

        } else if (wsSoProcess.equalsIgnoreCase(WS_PROCESS_SO_SAVE_APPROVAL)) {
            setWsSoProcess("");

            String approval[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            if (approval.length > 0 && !approval[0].isEmpty()) {
                sOProcessAmount += approval.length;
                for (int i = 0; i < approval.length; i++) {
                    String fields[] = approval[i].split(Constant.MAIN_CONCAT_STRING_2);
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", fields[0]);
                    mHmAux.put("type", WS_RESULT_TYPE_SO);
                    mHmAux.put("status", fields[1]);
                    mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                        sOProcessErrorAmount++;
                    }
                }
            }
            if(sOProcessErrorAmount>0) {
                try {
                    sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_so, false, sOProcessAmount- sOProcessErrorAmount, sOProcessAmount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_so, true, sOProcessAmount, sOProcessAmount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mPresenter.executeMoveSave();  // 6
        } else if (wsSoProcess.equalsIgnoreCase(WS_IO_Move_Save.class.getSimpleName())) {
            setWsSoProcess("");
            assetsProcessErrorAmount=0;
            move_planned = hmAux.get(WS_IO_Move_Save.MOVE_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            if (move_planned.length > 0 && !move_planned[0].isEmpty()) {
                for (int i = 0; i < move_planned.length; i++) {
                    String fields[] = move_planned[i].split(Constant.MAIN_CONCAT_STRING_2);
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", fields[0]);
                    mHmAux.put("type", WS_RESULT_TYPE_ASSETS_MOVE_PLANNED);
                    mHmAux.put("status", fields[1]);
                    mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                        assetsProcessErrorAmount++;
                    }
                }
            }

            mPresenter.executeBlindMoveSave();  // 7

        }  else if (wsSoProcess.equalsIgnoreCase(WS_IO_Blind_Move_Save.class.getSimpleName())) {
            setWsSoProcess("");

            blinds = hmAux.get(WS_IO_Move_Save.MOVE_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            if (blinds.length > 0 && !blinds[0].isEmpty()) {
                for (int i = 0; i < blinds.length; i++) {
                    String fields[] = blinds[i].split(Constant.MAIN_CONCAT_STRING_2);
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", fields[0]);
                    mHmAux.put("type", WS_RESULT_TYPE_ASSETS_MOVE);
                    mHmAux.put("status", fields[1]);
                    mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                        wsResults.add(mHmAux);
                        assetsProcessErrorAmount++;
                    }
                }
            }

            mPresenter.executeItemInboundSave();  // 8

        }   else if (wsSoProcess.equalsIgnoreCase(WS_IO_Inbound_Item_Save.class.getSimpleName())) {
            setWsSoProcess("");

            inbound_items = mPresenter.processInboundItemSaveReturn(mLink, WS_RESULT_TYPE_ASSETS_INBOUND_ITEM);
//            inboundItensOk = mPresenter.countInboundItemSaveReturnOk(mLink, WS_RESULT_TYPE_ASSETS_INBOUND_ITEM);
            inboundItensTotal=0;

            inboundItensTotal = mPresenter.countInboundItemSaveReturnTotal(mLink, WS_RESULT_TYPE_ASSETS_INBOUND_ITEM);
            if(inbound_items != null) {
                wsResults.addAll(inbound_items);
                for (HMAux item :inbound_items) {
                    if(item != null
                    && item.hasConsistentValue("status"))
                    if( "OK".equalsIgnoreCase(item.get("status"))){

                    }else{
                        assetsProcessErrorAmount++;
                    }
                }
            }else{
                inbound_items = new ArrayList<>();
            }

            mPresenter.executeItemOutboundSave();  // 9

        }  else if (wsSoProcess.equalsIgnoreCase(WS_IO_Outbound_Item_Save.class.getSimpleName())) {
            setWsSoProcess("");

            outbound_items = mPresenter.processOutboundItemSaveReturn(mLink, WS_RESULT_TYPE_ASSETS_OUTBOUND_ITEM);
//            outboundItensOk = mPresenter.countOutboundItemSaveReturnOk(mLink, WS_RESULT_TYPE_ASSETS_OUTBOUND_ITEM);
            outboundItensTotal=0;

            outboundItensTotal = mPresenter.countOutboundItemSaveReturnTotal(mLink, WS_RESULT_TYPE_ASSETS_OUTBOUND_ITEM);
            if(outbound_items != null) {
                wsResults.addAll(outbound_items);
                for (HMAux item :outbound_items) {
                    if(item != null
                            && item.hasConsistentValue("status"))
                        if( "OK".equalsIgnoreCase(item.get("status"))){

                        }else{
                            assetsProcessErrorAmount++;
                        }
                }
            }else{
                outbound_items = new ArrayList<>();
            }
            assetsProcessErrorAmount = assetsProcessErrorAmount + outbound_items.size();
            setAssetsResume();
            mPresenter.executeTicketSave(); //10

/*
            mPresenter.getMenuItensV2(hmAux_Trans);
            progressDialog.dismiss();

            if (wsResults.size() > 0) {
                showResults(wsResults);
            } else {
                if (syncAfterSave) {
                    setSyncAfterSave(false);
                    //
                    mPresenter.accessMenuItem(Act005_Main.MENU_ID_SYNC_DATA, 0);
                } else {
                    showSuccessDialog();
                }
            }*/
        } else if (wsSoProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getSimpleName())) {
            setWsSoProcess("");
            boolean sucess = false;
            ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn> total_tickets = new ArrayList<>();
            total_tickets = mPresenter.getTicketSaveActReturns(mLink, total_tickets);
            ArrayList<HMAux> ticket_items = mPresenter.processTicketSaveReturn(mLink, WS_RESULT_TYPE_TICKET);
            int total_tickets_amount = 0;
            boolean isDone = true;
            int ticket_errors = 0;

            if (total_tickets != null) {
                total_tickets_amount = total_tickets.size();
            }

            if (ticket_items != null && ticket_items.size() > 0) {
                wsResults.addAll(ticket_items);
                for (HMAux item :ticket_items) {
                    if(item != null
                            && item.hasConsistentValue("status"))
                        if(!"OK".equalsIgnoreCase(item.get("status"))){
                            isDone  = false;
                            ticket_errors++;
                        }
                }

            }

//            mPresenter.getMenuItensV2(hmAux_Trans);
            refreshUiData();
            progressDialog.dismiss();
            try {
                sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_ticket, isDone, total_tickets_amount - ticket_errors, total_tickets_amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(sendResumeDialog != null) {
                sendResumeDialog.setBtnOKEnable(true);
            }
        } else if (wsSoProcess.equalsIgnoreCase(Act005_Main_Presenter_Impl.SYNC_TICKETS)) {
            progressDialog.dismiss();
            wsProcess ="";
            boolean productOutdate = false;

            if(masterDataSyncFlow){
                syncAfterSave = true;
                executeSync();
            }else {
                if(ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET ,null)){
                    productOutdate = ToolBox_Inf.hasFormProductOutdate(context);
                }
                //
                if(productOutdate){
                    mPresenter.callWsSyncForTicketsForm();
                }else{
                    refreshUiData();
                }
            }
        } else if (wsSoProcess.equalsIgnoreCase(SYNC_FOR_TICKETS_FORM)) {
            progressDialog.dismiss();
            setWsSoProcess("");
            setWsProcess("");
            refreshUiData();
        }  else {
            if(sendResumeDialog != null) {
                sendResumeDialog.setBtnOKEnable(true);
            }
            setWsSoProcess("");
            refreshUiData();
//            mPresenter.getMenuItensV2(hmAux_Trans);
            progressDialog.dismiss();
        }
    }

    private void refreshUiData() {
        loadTranslation();
        invalidateOptionsMenu();
        updateDrawerInfo();
        if (mPresenter.hasSOProfile()) {
            refreshModuleList();
        } else {
            refreshTagList();
        }
    }

    private void setAssetsResume() {
        int movePlannedLenght=0;
        try {
            movePlannedLenght = getVectorStringLength(move_planned);
        } catch (Exception e) {
            e.printStackTrace();
            movePlannedLenght=0;
        }

        int moveBlindLenght=0;
        try {
            moveBlindLenght = getVectorStringLength(blinds);
        } catch (Exception e) {
            e.printStackTrace();
            moveBlindLenght=0;
        }

        int totalAmount = movePlannedLenght + moveBlindLenght + inboundItensTotal + outboundItensTotal;
        if(assetsProcessErrorAmount >0){
            try {
                sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_assets, false, totalAmount - assetsProcessErrorAmount, totalAmount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                sendResumeDialog.updateResumeStatus(R.id.act005_send_resume_assets, true, totalAmount, totalAmount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getVectorStringLength(String[] moves) throws Exception{
        if(moves.length > 0 && !moves[0].isEmpty() ){
            return moves.length;
        }
        return 0;
    }

    @Override
    public void addWsResults(ArrayList<HMAux> auxResults) {
        wsResults.addAll(
            auxResults
        );
    }

    /**
     * BARRIONUEVO - 18-11-2020
     * Metodo responsavel por alertar o usuario sobre a necessidade de arrumar o relogio para o tempo
     * correto.
     */
    @Override
    public void handleInvalidLocalDatetime() {
        android.app.AlertDialog.Builder alertInvalidDatetime = new android.app.AlertDialog.Builder(context);

        alertInvalidDatetime.setTitle(hmAux_Trans.get("alert_invalid_local_datetime_ttl"));
        alertInvalidDatetime.setMessage(hmAux_Trans.get("alert_invalid_local_datetime_msg"));
        alertInvalidDatetime.setCancelable(false);
        //
        alertInvalidDatetime.setPositiveButton(hmAux_Trans.get("alert_go_to_settings"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
            }
        });

        alertInvalidDatetime.setNegativeButton(hmAux_Trans.get("sys_alert_btn_cancel"), null);
        //
        alertInvalidDatetime.show();

    }

    @Override
    public void refreshResume(int layout_id, boolean isDone, int sucessAmount, int totalAmount) {
        try {
            sendResumeDialog.updateResumeStatus(layout_id, isDone, sucessAmount, totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
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

        /*lv_results.setAdapter(
                new Act028_Results_Adapter(
                        context,
                        R.layout.act028_results_adapter_cell,
                        res
                )
        );*/
        List<HMAux> gAdapterRes = new ArrayList<>();
        for (HMAux item : res) {
            HMAux hmAux = new HMAux();
            //
            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.get("label"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.get("status"));
            //
            switch (item.get("type")) {
                case WS_RESULT_TYPE_SERIAL:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_serial_data"));
                    break;
                case ConstantBaseApp.SYS_STATUS_SCHEDULE:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_schedule_data"));
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.get("final_status")+"\n"+item.get("status"));
                    break;
                case TSave_Rec.Error_Process.ERROR_TYPE_TICKET:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_ticket"));
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.get("final_status")+"\n"+item.get("status"));
                    break;
                case WS_RESULT_TYPE_AP:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_form_ap"));
                    break;
                case WS_RESULT_TYPE_SO:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_so"));
                    break;
                case WS_RESULT_TYPE_SO_EXPRESS:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_so_express"));
                    break;
                case WS_RESULT_TYPE_ASSETS_MOVE_PLANNED:
                case WS_RESULT_TYPE_ASSETS_MOVE:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_assets_move"));
                    break;
                case WS_RESULT_TYPE_ASSETS_INBOUND_ITEM:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_assets_inbound"));
                    break;
                case WS_RESULT_TYPE_ASSETS_OUTBOUND_ITEM:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_assets_outbound"));
                    break;
                case WS_RESULT_TYPE_ASSETS:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_assets"));
                    break;
                case WS_RESULT_TYPE_TICKET:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_ticket"));
                    break;
                case WS_RESULT_TYPE_GENERAL:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("alert_ws_general_error_ttl"));
                    break;
            }
            //
            gAdapterRes.add(hmAux);
        }
        //
        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        gAdapterRes,
                        Generic_Results_Adapter.CONFIG_MENU_SEND_RET,
                        hmAux_Trans
                )
        );
        //
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
                    //
                }
                //
                if(mPresenter.hasTicketSyncRequired()) {
                    mPresenter.executeWSTicketDownload();
                }else{
                    if(masterDataSyncFlow){
                        ToolBox_Inf.hasFormProductOutdate(context);
                        executeSync();
                    }
                }
            }
        });
    }

    @Override
    protected void processError_http() {
        super.processError_http();
        if(sendResumeDialog != null) {
            sendResumeDialog.dismiss();
        }
    }


    @Override
    protected void processError_Resume() {
        super.processError_Resume();
        if(sendResumeDialog != null) {
            sendResumeDialog.dismiss();
        }
    }

    /**
     * LUCHE - 07/01/2020
     * <p>
     * Revisado metodo, pois não havia logica por tras. Todas as opções geravam um hmAux que não era
     * utilizado para nada e setavam o valor de syncAfterSave para false(somente no else final a var não
     * era resetada o que estava causando problemas ja que estavam falando if's para os modulos assets
     * e ticket.
     * <p>
     * Simplificado metodo para sempre setar syncAfterSave = false e recarregar os menus.
     *
     * @param mLink
     * @param mRequired
     */
    @Override
    protected void processError_1(String mLink, String mRequired) {
        if(sendResumeDialog != null) {
            sendResumeDialog.dismiss();
        }
        setSyncAfterSave(false);
//        mPresenter.getMenuItensV2(hmAux_Trans);
        refreshUiData();
//        if (wsSoProcess.equalsIgnoreCase(WS_Serial_Save.class.getSimpleName())) {
//            setRes(WS_RESULT_TYPE_SERIAL,hmAux_Trans.get("lbl_serial_data"), hmAux_Trans.get("alert_ws_serial_error_msg"), "");
//            setSyncAfterSave(false);
//        } else if (wsSoProcess.equalsIgnoreCase(WS_Save.class.getSimpleName())) {
//            setRes(WS_RESULT_TYPE_NFORM,hmAux_Trans.get("lbl_checklist"), hmAux_Trans.get("alert_ws_form_error_msg"), "");
//            setSyncAfterSave(false);
//        } else if (wsSoProcess.equalsIgnoreCase(WS_AP_Save.class.getSimpleName())) {
//            setRes(WS_RESULT_TYPE_AP,hmAux_Trans.get("lbl_form_ap"), hmAux_Trans.get("alert_ws_ap_error_msg"), "");
//            setSyncAfterSave(false);
//        } else if (wsSoProcess.equalsIgnoreCase(WS_SO_Pack_Express_Local.class.getSimpleName())) {
//            setRes(WS_RESULT_TYPE_SO_EXPRESS,hmAux_Trans.get("lbl_so"), hmAux_Trans.get("alert_ws_so_express_error_msg"), "");
//            super.processError_1(mLink, mRequired);
//            setSyncAfterSave(false);
//        } else if (wsSoProcess.equalsIgnoreCase(WS_PROCESS_SO_SAVE)) {
//            setRes(WS_RESULT_TYPE_SO,hmAux_Trans.get("lbl_so"), hmAux_Trans.get("alert_ws_so_error_msg"), "");
//            setSyncAfterSave(false);
//        } else if (wsSoProcess.equalsIgnoreCase(WS_PROCESS_SO_SAVE_APPROVAL)) {
//            setRes(WS_RESULT_TYPE_SO,hmAux_Trans.get("lbl_so"), hmAux_Trans.get("alert_ws_so_approval_error_msg"), "");
//            setSyncAfterSave(false);
//        } else if (
//            wsSoProcess.equalsIgnoreCase(WS_IO_Move_Save.class.getSimpleName())
//            || wsSoProcess.equalsIgnoreCase(WS_IO_Blind_Move_Save.class.getSimpleName())
//            || wsSoProcess.equalsIgnoreCase(WS_IO_Inbound_Item_Save.class.getSimpleName())
//            || wsSoProcess.equalsIgnoreCase(WS_IO_Outbound_Item_Save.class.getSimpleName())
//        ) {
//            setRes(WS_RESULT_TYPE_ASSETS,hmAux_Trans.get("lbl_assets"), hmAux_Trans.get("alert_ws_assets_error_msg"), "");
//            setSyncAfterSave(false);
//        } else if (wsSoProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getSimpleName())) {
//            setRes(WS_RESULT_TYPE_TICKET,hmAux_Trans.get("lbl_ticket"), hmAux_Trans.get("alert_ws_ticket_error_msg"), "");
//            setSyncAfterSave(false);
//        } else {
//            setRes(WS_RESULT_TYPE_GENERAL,hmAux_Trans.get("alert_ws_general_error_ttl"), hmAux_Trans.get("alert_ws_general_error_msg"), "");
//            setSyncAfterSave(false);
//        }
//        //
//        if (syncAfterSave) {
//            setSyncAfterSave(false);
//            mPresenter.accessMenuItem(Act005_Main.MENU_ID_SYNC_DATA, 0);
//        } else {
//            mPresenter.getMenuItensV2(hmAux_Trans);
//        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        if(sendResumeDialog != null) {
            sendResumeDialog.dismiss();
        }
        if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE_APPROVAL)) {
            processError_1(mLink, mRequired);
        } else if (wsProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SYNC)) {
            changeCustomer();
        } else {
            progressDialog.dismiss();
        }

//        else if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_STATUS)) {
//            setWsSoProcess("");
//            setRes(hmAux_Trans.get("lbl_checklist"), hmAux_Trans.get("alert_ws_form_error_msg"), "");
//            mPresenter.getMenuItensV2(hmAux_Trans);
//            progressDialog.dismiss();
//
//        } else if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE)) {
//            setWsSoProcess("");
//            mPresenter.getMenuItensV2(hmAux_Trans);
//            setRes(hmAux_Trans.get("lbl_so"), hmAux_Trans.get("alert_ws_so_error_msg"), "");
//            progressDialog.dismiss();
//
//        } else if (wsSoProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SO_SAVE_APPROVAL)) {
//            setWsSoProcess("");
//            setRes(hmAux_Trans.get("lbl_so"), hmAux_Trans.get("alert_ws_so_approval_error_msg"), "");
//            mPresenter.getMenuItensV2(hmAux_Trans);
//            progressDialog.dismiss();
//
//        } else if (wsSoProcess.equalsIgnoreCase(WS_AP_Save.class.getSimpleName())) {
//            setWsSoProcess("");
//            setRes(hmAux_Trans.get("lbl_form_ap"), hmAux_Trans.get("alert_ws_ap_error_msg"), "");
//            mPresenter.getMenuItensV2(hmAux_Trans);
//            progressDialog.dismiss();
//
//        } else if (wsSoProcess.equalsIgnoreCase(WS_SO_Pack_Express_Local.class.getSimpleName())) {
//            setWsSoProcess("");
//            setRes(hmAux_Trans.get("lbl_so_express"), hmAux_Trans.get("alert_ws_so_express_error_msg"), "");
//            mPresenter.getMenuItensV2(hmAux_Trans);
//            progressDialog.dismiss();
//        }
//        else if (wsSoProcess.equalsIgnoreCase(WS_Serial_Save.class.getSimpleName())) {
//            setWsSoProcess("");
//            setRes(hmAux_Trans.get("lbl_send_data"), hmAux_Trans.get("alert_ws_serial_error_msg"), "");
//            mPresenter.getMenuItensV2(hmAux_Trans);
//            progressDialog.dismiss();
//
//        } else if (wsProcess.equalsIgnoreCase(Act005_Main.WS_PROCESS_SYNC)) {
//            changeCustomer();
//        } else {
//            progressDialog.dismiss();
//        }
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
                //LUCHE - 30/06/2020
                //Substituido o metodo antigo pelo metodo que agenda todos os workers.
                ToolBox_Inf.scheduleAllDownloadWorkers(context);
                ToolBox_Inf.updateUserCustomerSync(getApplicationContext(), String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ToolBox_Con.getPreference_User_Code(getApplicationContext()), 0);
                setFragments();
                refreshUiData();
                //
                break;
            case Act005_Main.WS_PROCESS_ENABLE_NFC:
                alertTitle = hmAux_Trans.get("alert_enable_nfc_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_enable_nfc_finish_msg");
                updateDrawerInfo();
                break;

            case Act005_Main.WS_PROCESS_CANCEL_NFC:
                alertTitle = hmAux_Trans.get("alert_cancel_nfc_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_cancel_nfc_finish_msg");
                updateDrawerInfo();
                break;

            case Act005_Main.WS_PROCESS_SUPPORT:
                alertTitle = hmAux_Trans.get("alert_support_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_support_finish_msg");
                break;
            case SYNC_FOR_TICKETS_FORM:
                alertTitle = hmAux_Trans.get("alert_sync_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_sync_finish_msg");
                progressDialog.dismiss();
                refreshUiData();
                break;
            case Act005_Main_Presenter_Impl.SYNC_TICKETS:
                alertTitle = hmAux_Trans.get("alert_sync_finish_ttl");
                alertMsg = hmAux_Trans.get("alert_sync_finish_msg");
                break;
            default:
                break;
        }

        wsProcess = "";

        ToolBox.alertMSG(
                Act005_Main.this,
                alertTitle,
                alertMsg,
                null,
                0
        );

    }

    private void setFragments() {
        if (mPresenter.hasSOProfile()) {
            initAltFragment();
        } else {
            initTagFragment();
        }
    }

    /**
     * LUCHE - 04/05/2021
     * Metodo que chama atualização das opções do drawer
     */
    private void updateDrawerInfo() {
        if(fragOpc != null){
            fragOpc.setHmAux_Trans(hmAux_Trans);
            fragOpc.revalidateOptionSetup();
        }
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
        /*
            BARRIONUEVO  13-03-2020
            Instancia Dialog de resumo dos servicos.
         */
        if( sendResumeDialog ==null) {
            sendResumeDialog = getSendResumeDialog();
        }else{
            sendResumeDialog.cancel();
            sendResumeDialog = getSendResumeDialog();
        }
        sendResumeDialog.setCancelable(false);
        sendResumeDialog.show();
        if (wsResults != null) {
            wsResults.clear();
        } else {
            wsResults = new ArrayList<>();
        }
    }

    @NonNull
    private SendResumeDialog getSendResumeDialog() {
        return new SendResumeDialog(context, hmAux_Trans, new SendResumeDialog.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                if(sendResumeDialog != null) {
                    sendResumeDialog.dismiss();
                }
                progressDialog.dismiss();
                invalidateOptionsMenu();
                refreshUiData();

                if (wsResults.size() > 0) {
                    showResults(wsResults);
                } else {
                    if(masterDataSyncFlow){
                        ToolBox_Inf.hasFormProductOutdate(context);
                        executeSync();
                    }else {
                        if(mPresenter.hasTicketSyncRequired()) {
                            mPresenter.executeWSTicketDownload();
                        }
                    }
                }

            }
        });
    }

    /**
     *       BARRIONUEVO 11-02-2020
     *
     *       Correção de verificação de n-forms com localização pendente no
     *       fluxo de sincronização do app, evitando que o usuario atualize o
     *       app e perca dados.
     */
    private void executeSync() {

        boolean productOutdate = false;
        if(ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET ,null)){
            productOutdate = ToolBox_Inf.hasFormProductOutdate(context);
        }

        if (syncAfterSave) {
            setSyncAfterSave(false);
            if(ToolBox_Inf.getLocationPendencies(context) == 0) {
                //
                mPresenter.accessMenuItem(Act005_Main.MENU_ID_SYNC_DATA, 0);
            }else{
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_unsent_gps_nform_ttl"),
                        hmAux_Trans.get("alert_unsent_gps_nform_msg"),
                        null,
                        0
                );
            }
        }else{
            if(productOutdate){
                mPresenter.callWsSyncForTicketsForm();
            }
        }
    }

    @Override
    public void onBackPressed() {
        closeApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        int iconColor = 0;
        boolean hasUpdateRequired = mPresenter.hasUpdateRequired();
        boolean hasTicketSyncRequired = mPresenter.hasTicketSyncRequired();
        //
        Drawable wrappedDrawable = setSyncIcon(iconColor, hasUpdateRequired, hasTicketSyncRequired);
        //
        menu.add(0, TOOLBAR_SYNC_DATA_STATUS, Menu.FIRST + 0, hmAux_Trans.get("lbl_sync_data"));
        menu.findItem(TOOLBAR_SYNC_DATA_STATUS).setIcon(wrappedDrawable);
        menu.findItem(TOOLBAR_SYNC_DATA_STATUS).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //
        Drawable wrappedSyncIcon = DrawableCompat.wrap(context.getDrawable(R.drawable.ic_sync_black_24dp));
        if (wrappedSyncIcon != null) {
            DrawableCompat.setTint(wrappedSyncIcon.mutate(), ContextCompat.getColor(context,  android.R.color.white));
            if(mPresenter.hasMasterDataSyncRequired()){
                DrawableCompat.setTint(wrappedSyncIcon.mutate(), ContextCompat.getColor(context,  android.R.color.holo_red_light));
            }
        }

        menu.add(0, TOOLBAR_SEND_RECEIVE_DATA, Menu.FIRST + 1, hmAux_Trans.get("lbl_sync_data"));
        menu.findItem(TOOLBAR_SEND_RECEIVE_DATA).setIcon(wrappedSyncIcon);
        menu.findItem(TOOLBAR_SEND_RECEIVE_DATA).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(TOOLBAR_SEND_RECEIVE_DATA).setTitle(hmAux_Trans.get("lbl_sync_data"));


//        menu.findItem(TOOLBAR_ENABLE_NFC).setTitle(hmAux_Trans.get("lbl_sync_data"));

        return true;
    }

    @NotNull
    private Drawable setSyncIcon(int iconColor, boolean hasUpdateRequired, boolean hasTicketSyncRequired) {
        int icon;
        if(hasUpdateRequired && hasTicketSyncRequired){
            icon = R.drawable.ic_sync_main_menu_data;
        }else if(hasUpdateRequired){
            icon = R.drawable.ic_cloud_upload;
            iconColor = R.color.namoa_cancel_red;
        }else if(hasTicketSyncRequired){
            icon = R.drawable.ic_baseline_cloud_download_24;
            iconColor = R.color.custom_yellow_sync;
        }else{
            iconColor = R.color.namoa_color_pipeline_origin_icon;
            icon = R.drawable.ic_baseline_cloud_done_24;
        }
        //
        Drawable wrappedDrawable = DrawableCompat.wrap(context.getDrawable(icon));
        if (wrappedDrawable != null && iconColor>0) {
            DrawableCompat.setTint(wrappedDrawable.mutate(), ContextCompat.getColor(context, iconColor));
        }
        return wrappedDrawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //
        DialogInterface.OnClickListener listener = null;
        boolean hasUpdateRequired = mPresenter.hasUpdateRequired();
        boolean hasTicketSyncRequired = mPresenter.hasTicketSyncRequired();
        switch (id) {
            //TODO REVISAR AQUI, POIS FOI MODIFICADO APENAS PARA SYNC GAMBIS
            case TOOLBAR_NAMOA_LOGO:
                return true;

            case TOOLBAR_SEND_RECEIVE_DATA:
                ToolBox.alertMSG(
                        Act005_Main.this,
                        hmAux_Trans.get("drawer_sync_alert_ttl"),
                        hmAux_Trans.get("drawer_sync_alert_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //mPresenter.accessMenuItem(MENU_ID_SYNC_DATA, 0);
                                masterDataSyncFlow = true;
                                if(hasTicketSyncRequired){
                                    mPresenter.executeWSTicketDownload();
                                }else {
                                    mPresenter.syncFlow(mPresenter.hasUpdateRequired());
                                }
                            }
                        },
                        1
                );
                break;
            case TOOLBAR_SYNC_DATA_STATUS:
                masterDataSyncFlow = false;
                if(!hasUpdateRequired && hasTicketSyncRequired){
                    mPresenter.executeWSTicketDownload();
                } else if (hasUpdateRequired) {
                    if (ToolBox_Con.isOnline(context)) {
                        setWsProcess(Act005_Main.WS_PROCESS_SEND);
                        setWsSoProcess(WS_Serial_Save.class.getSimpleName());
                        showPD();
                        cleanUpResults();
                        //executeSaveProcess();
                        mPresenter.executeSerialSave();
                    } else {
                        showNoConnectionDialog();
                    }
                }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        ToolBox_Inf.mkDirectory();
        // LUCHE - 19/12/2019
        //O comando getMenuItensV2, foi adicionado em 19 Apr 2017, commit 9ff6860d31decd335f5f2b3d40e75822fa609348
        //Aparentemente, serve apenas para quando o usuario, estando com a act005,
        //abre uma noticação do app e é enviado para act019
        //Rever isso no momento propicio
//        mPresenter.getMenuItensV2(hmAux_Trans);

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

    @Override
    public void onSelectMenuTagItem(@NotNull MainTagMenu item) {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct083(item);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectCalendar() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct016(context);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectSearch() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct068(context);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectMessenger() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct034(context);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectFABAssetLocal() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct006(context);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @NotNull
    @Override
    public List<MainTagMenu> getTagList(@NotNull String periodFilter, @NotNull String sitesFilter, @NotNull String focusFilter) {
        //
        List<HMAux> queryResult = mPresenter.getMenuItensV3(periodFilter, sitesFilter, focusFilter);
        //
        ArrayList<MainTagMenu> mainTagMenus = new ArrayList<>();
        //
        int tagListItemCount = 0;
        for(HMAux aux: queryResult){
            //
            int updateRequired = aux.hasConsistentValue("update_required")? Integer.parseInt(aux.get("update_required")) : 0;
            int syncRequired   = aux.hasConsistentValue("sync_required")? Integer.parseInt(aux.get("sync_required")) : 0;
            //
            tagListItemCount += aux.hasConsistentValue("qty")? Integer.parseInt(aux.get("qty")) : 0;
            mainTagMenus.add( new MainTagMenu(
                            aux.hasConsistentValue("tag_operational_code")? Integer.parseInt(aux.get("tag_operational_code")) : 0,
                            aux.hasConsistentValue("tag_operational_code")? aux.get("tag_operational_desc") : "null",
                            aux.hasConsistentValue("qty")? Integer.parseInt(aux.get("qty")) : 0,
                            aux.hasConsistentValue("in_processing")? Integer.parseInt(aux.get("in_processing")) : 0,
                            updateRequired,
                            syncRequired
                    )
            );
        }
        //
        if(mainTagMenus.size() > 1 ) {
            mainTagMenus.add(new MainTagMenu(0,
                    "",
                    tagListItemCount,
                    0,
                    0,
                    0)
            );
        }
        return mainTagMenus;
    }

    @Override
    public void onSelectAsset() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct051(context);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectTags() {
        //Força a chamada de todas as tags.
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct083(new MainTagMenu(0,
                    null,
                    0,
                    0,
                    0,
                    0));
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectTagsBySerialSearch() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct006(context);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectOS() {

        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct026(context);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectOSVinSearch() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct021(context);
        }else{
            handleInvalidLocalDatetime();
        }
    }

    @Override
    public void onSelectOSExpress() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct040(context);
        }else{
            handleInvalidLocalDatetime();
        }

    }

    @Override
    public void onSelectOSNext() {
        if (ToolBox_Inf.isLocalDatetimeOk(context)) {
            callAct047(context);
        }else{
            handleInvalidLocalDatetime();
        }

    }


    private void callAct047(Context context) {
        Intent mIntent = new Intent(context, Act047_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }
    /**
     *  BARRIONUEVO 23-05-2021
     *  Metodo que aproveita a variavel de tradução.
     */
    @NotNull
    @Override
    public String getDatetimeWarning() {
        return hmAux_Trans.get("lbl_invalid_datetime_warning");
    }

    /**
     * BARRIONUEVO 02-06-2021
     * Metodo que calcula mensagens do chat para fragmento de lista de acoes.
     * @return
     */
    @Override
    public int getChatBadgeQty() {
        return mPresenter.getChatBadgeQty();
    }

    private class FCMReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            mPresenter.getMenuItensV2(hmAux_Trans);

            Bundle bundle = intent.getExtras();
            if(bundle != null){
                String fcmTitle = (String) bundle.get(ConstantBaseApp.SW_TYPE);
                if(fcmTitle != null) {
//                    Log.d("FCM", "fcmTitle: " + fcmTitle);
                    if (fcmTitle.equals(FCM_MODULE_SYNC)) {
                        invalidateOptionsMenu();
                    } else if (fcmTitle.equals(FCM_MODULE_TICKET) || fcmTitle.equals(ConstantBaseApp.FCM_MODULE_FORM_AP)|| fcmTitle.equals(FCM_ACTION_TK_TICKET_UPDATE)) {
                        invalidateOptionsMenu();
                        refreshUiData();
                    }
                }
            }
        }
    }

    private class BR_Chat extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.CHAT_BR_TYPE);

//            switch (type) {
//                case Constant.CHAT_BR_TYPE_CHAT_STATUS_CHANGE:
//                    break;
//                case Constant.CHAT_BR_TYPE_CHAT_LOGGED_STATUS_CHANGE:
//                    break;
//                case Constant.CHAT_BR_TYPE_ROOM:
//                    break;
//                default:
//                    mPresenter.getMenuItensV2(hmAux_Trans);
//                    break;
//            }
            //
            switch (type) {
                case Constant.CHAT_BR_TYPE_MSG:
                case Constant.CHAT_EVENT_C_MESSAGE_FCM:
                    //Refresh apos receber mensagem de chat.
                    if(mPresenter.hasSOProfile()){
                        FrgMainHomeAlt currentFragment = (FrgMainHomeAlt) fm.findFragmentById(R.id.act005_frg_placeholder);
                        if(currentFragment != null) {
                            currentFragment.refreshChatBadge();
                        }
                    }else{
                        FrgMainHome currentFragment = (FrgMainHome) fm.findFragmentById(R.id.act005_frg_placeholder);
                        int chatBadgeQty = mPresenter.getChatBadgeQty();
                        currentFragment.refreshChatBadge(chatBadgeQty);
                    }
//                    if (mAdapter != null) {
//                        mAdapter.updateMenuItemBadge(
//                                MENU_ID_CHAT,
//                                1,
//                                mPresenter.getChatBadgeQty()
//                        );
//                    }
                    //
                    break;

            }

        }
    }


    @Override
    protected void processNotification_close(String mValue, String mActivity) {
    }

    private void initTagFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment =  fm.findFragmentById(R.id.act005_frg_placeholder);
        if(fragment == null
                || !(fragment instanceof FrgMainHome)) {
            fragment = FrgMainHome.newInstance(mModule_Code);
        }
        //act050_favorite_fragment.setHmAux_Trans(hmAux_Trans);
        transaction.replace(R.id.act005_frg_placeholder, fragment, fragment.getTag());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initAltFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment =  fm.findFragmentById(R.id.act005_frg_placeholder);
        if(fragment == null
        || !(fragment instanceof FrgMainHomeAlt)) {
            fragment = FrgMainHomeAlt.newInstance();
        }
        //act050_favorite_fragment.setHmAux_Trans(hmAux_Trans);
        transaction.replace(R.id.act005_frg_placeholder, fragment, fragment.getTag());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
