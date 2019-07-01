package com.namoadigital.prj001.ui.act005;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act005_Logout_Adapter;
import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MenuMainNamoa;
import com.namoadigital.prj001.receiver.*;
import com.namoadigital.prj001.service.*;
import com.namoadigital.prj001.sql.*;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act005.Act005_Main.WS_PROCESS_SO_SAVE;
import static com.namoadigital.prj001.ui.act005.Act005_Main.WS_PROCESS_SO_SAVE_APPROVAL;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act005_Main_Presenter_Impl implements Act005_Main_Presenter {

    private Context context;
    private Act005_Main_View mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private HMAux hmAux_Trans = new HMAux();
    private EV_User_CustomerDao userCustomerDao;
    private FCMMessageDao fcmMessageDao;
    private SM_SODao soDao;
    private GE_Custom_Form_ApDao customFormApDao;
    private MD_ProductDao mdProductDao;
    private CH_MessageDao chMessageDao;
    private MD_SiteDao siteDao;

    private SO_Pack_Express_LocalDao soPackExpressLocalDao;

    private String logoutList = "";
    private transient Dialog logoutDialog;
    private Act005_Logout_Adapter mAdapter;
    private ListView lv_customer;
    private List<HMAux> customer_list;
    //
    private ArrayList<MenuMainNamoa> menuList = new ArrayList<>();

    public Act005_Main_Presenter_Impl(Context context, Act005_Main_View mView, GE_Custom_Form_LocalDao customFormLocalDao, HMAux hmAux_Trans, EV_User_CustomerDao userCustomerDao, FCMMessageDao fcmMessageDao, SM_SODao soDao, GE_Custom_Form_ApDao customFormApDao, SO_Pack_Express_LocalDao soPackExpressLocalDao, MD_ProductDao mdProductDao, CH_MessageDao chMessageDao) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.hmAux_Trans = hmAux_Trans;
        this.userCustomerDao = userCustomerDao;
        this.fcmMessageDao = fcmMessageDao;
        this.soDao = soDao;
        this.customFormApDao = customFormApDao;
        this.soPackExpressLocalDao = soPackExpressLocalDao;
        this.mdProductDao = mdProductDao;
        this.chMessageDao = chMessageDao;
        this.siteDao = new MD_SiteDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        buildMenuList();
    }

    /*Montagem novo menu*/
    private void buildMenuList() {
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_CHECKLIST,
                        Constant.PROFILE_PRJ001_CHECKLIST,
                        "lbl_checklist",
                        "lbl_checklist",
                        R.drawable.ic_n_form
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_FORM_AP,
                        Constant.PROFILE_PRJ001_AP,
                        "lbl_form_ap",
                        "lbl_form_ap",
                        R.drawable.ic_n_action_plan

                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_SERVICE,
                        Constant.PROFILE_PRJ001_SO,
                        "lbl_so",
                        "lbl_so",
                        R.drawable.ic_n_service2_24x24
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_SCHEDULE_DATA,
                        Constant.PROFILE_PRJ001_SCHEDULE_CHECKLIST,
                        "lbl_schedule_data",
                        "lbl_schedule_data",
                        R.drawable.ic_calendario
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_SERIAL,
                        Constant.PROFILE_PRJ001_PRODUCT_SERIAL,
                        "lbl_serial_data",
                        "lbl_serial_data",
                        R.drawable.ic_serial_24x24

                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_IO_ASSETS,
                        ConstantBaseApp.PROFILE_PRJ001_OI,
                        "lbl_io_assets",
                        "lbl_io_assets",
                        R.drawable.ic_n_assets
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_PENDING_DATA,
                        "",
                        "lbl_pending_data",
                        "lbl_pending_data",
                        R.drawable.ic_pendente
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_HISTORIC_DATA,
                        "",
                        "lbl_historic_data",
                        "lbl_historic_data",
                        R.drawable.ic_historico
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_MESSAGES,
                        "",
                        "lbl_messages",
                        "lbl_messages",
                        R.drawable.ic_notificacao
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_SEND_DATA,
                        "",
                        "lbl_send_data",
                        "lbl_send_data",
                        R.drawable.ic_enviar
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_CHAT,
                        "",
                        "lbl_chat",
                        "lbl_chat",
                        R.drawable.ic_n_chat
                )
        );
        //
        menuList.add(
                new MenuMainNamoa(
                        Act005_Main.MENU_ID_CLOSE,
                        "",
                        "lbl_close_app",
                        "lbl_close_app",
                        R.drawable.ic_sair
                )
        );
    }

    public void getMenuItensV2(HMAux hmAux_Trans) {
        ArrayList<MenuMainNamoa> grantedMenus = new ArrayList<>();
        for (MenuMainNamoa menu : menuList) {
            boolean showMenu = menu.getMenu_code().equals("") || ToolBox_Inf.profileExists(context, menu.getMenu_code(), null);
            //
            if (showMenu) {
                String qty = "";
                String qtySO = "";
                String qtyAP = "";
                String qtySO_Express = "";
                String qtySerial = "";
                String qtyBadge2 = "";


                //Reseta valores do badge cada vez que metodo for chamado
                menu.resetBadges();
                //Traduz label do menu
                menu.setMenu_desc(hmAux_Trans.get(menu.getMenu_lbl()));
                /*if (hmAux_Trans.get(menu.getMenu_desc()) != null) {
                    menu.setMenu_desc(hmAux_Trans.get(menu.getMenu_desc()));
                } else {
                    menu.setMenu_desc(ToolBox.setNoTrans(mModule_Code, mResource_Code, menu.getMenu_desc()));
                }*/
                //
                switch (menu.getMenu_id()) {
                    case Act005_Main.MENU_ID_SERVICE:
                        try {
                            qty = soDao.getByStringHM(
                                    new Sql_Act021_004(
                                            ToolBox_Con.getPreference_Customer_Code(context)
                                    ).toSqlQuery()
                            ).get(Sql_Act021_004.UPDATE_SYNC_REQUIRED_QTY);
                            //
                        } catch (Exception e) {
                            qty = "0";
                        }
                        try {
                            qtyBadge2 = soDao.getByStringHM(
                                    new Sql_Act005_005(
                                            ToolBox_Con.getPreference_Customer_Code(context),
                                            ToolBox_Con.getPreference_Site_Code(context),
                                            ToolBox_Con.getPreference_Zone_Code(context)
                                    ).toSqlQuery()
                            ).get(Sql_Act005_005.QTD_MY_PENDING_SO);
                        } catch (Exception e) {
                            qtyBadge2 = "0";
                        }
                        //
                        menu.addInBadge1(qty);
                        menu.addInBadge2(qtyBadge2);
                        break;

                    case Act005_Main.MENU_ID_IO_ASSETS:
                         if(!isSiteLoggedIoControl()){
                             menu.setIcon(R.drawable.ic_n_assets_inactive2);
                         }else{
                             menu.setIcon(R.drawable.ic_n_assets);
                         }
                        //tratar badges de pendentes.
                        break;

                    case Act005_Main.MENU_ID_PENDING_DATA:
                        try {
                            qty = customFormLocalDao.getByStringHM(
                                    new Sql_Act005_001(
                                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                                    ).toSqlQuery()
                            ).get(Sql_Act005_001.BADGE_IN_PROCESSING_QTY);
                        } catch (Exception e) {
                            qty = "0";
                        }
                        //24/08/2018 - Add validação se usr tem acesso a S.O
                        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
                            try {
                                qtySO = soDao.getByStringHM(
                                        new Sql_Act021_002(
                                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                                        ).toSqlQuery()
                                ).get(Sql_Act021_002.PENDING_PROCESS_QTY);
                            } catch (Exception e) {
                                qtySO = "0";
                            }
                        } else {
                            qtySO = "0";
                        }
                        //
                        try {
                            qtyAP = customFormApDao.getByStringHM(
                                    new GE_Custom_Form_Ap_Sql_001(
                                            ToolBox_Con.getPreference_Customer_Code(context)
                                    ).toSqlQuery()
                            ).get(GE_Custom_Form_Ap_Sql_001.BADGE_IN_PROCESSING_QTY);
                        } catch (Exception e) {
                            qtyAP = "0";
                        }
                        //24/08/2018 - Add validação se usr tem acesso a S.O Express
                        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, Constant.PROFILE_MENU_SO_EXPRESS)) {
                            try {
                                qtySO_Express = soPackExpressLocalDao.getByStringHM(
                                        new SO_Pack_Express_Local_Sql_010(
                                                ToolBox_Con.getPreference_Customer_Code(context)
                                        ).toSqlQuery()
                                ).get(SO_Pack_Express_Local_Sql_010.BADGE_IN_NEW_QTY);
                            } catch (Exception e) {
                                qtySO_Express = "0";
                            }
                        } else {
                            qtySO_Express = "0";
                        }
                        //Soma Qtd de n-form ,n_service, n_form_ap e so_express
                        menu.addInBadge1(qty);
                        menu.addInBadge1(qtySO);
                        menu.addInBadge1(qtyAP);
                        menu.addInBadge1(qtySO_Express);
//                        qty = String.valueOf(
//                                Integer.parseInt(qty)
//                                        + Integer.parseInt(qtySO)
//                                        + Integer.parseInt(qtyAP)
//                                        + Integer.parseInt(qtySO_Express)
//                        );
                        //
                        //24/08/2018 - Add valudação se usr tem acesso a S.O
                        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
                            try {
                                qtyBadge2 = soDao.getByStringHM(
                                        new Sql_Act005_004(
                                                ToolBox_Con.getPreference_Customer_Code(context),
                                                ToolBox_Con.getPreference_Site_Code(context),
                                                ToolBox_Con.getPreference_Zone_Code(context)
                                        ).toSqlQuery()
                                ).get(Sql_Act005_004.QTD_MY_PENDING_SO);
                            } catch (Exception e) {
                                qtyBadge2 = "0";
                            }
                        } else {
                            qtyBadge2 = "0";
                        }
                        menu.addInBadge2(qtyBadge2);
                        break;

                    case Act005_Main.MENU_ID_SEND_DATA:
                        try {
                            qty = customFormLocalDao.getByStringHM(
                                    new Sql_Act005_002(
                                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                                    ).toSqlQuery()
                            ).get(Sql_Act005_002.BADGE_FINALIZED_QTY);
                        } catch (Exception e) {
                            qty = "0";
                        }
                        try {
                            qtySO = soDao.getByStringHM(
                                    new Sql_Act021_003(
                                            ToolBox_Con.getPreference_Customer_Code(context)
                                    ).toSqlQuery()
                            ).get(Sql_Act021_003.UPDATE_APPROVAL_REQUIRED_QTY);
                        } catch (Exception e) {
                            qtySO = "0";
                        }
                        try {
                            qtyAP = customFormApDao.getByStringHM(
                                    new Sql_Act005_007(
                                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                                    ).toSqlQuery()
                            ).get(Sql_Act005_007.BADGE_TO_SEND_QTY);
                        } catch (Exception e) {
                            qtyAP = "0";
                        }
                        try {
                            qtySO_Express = soPackExpressLocalDao.getByStringHM(
                                    new SO_Pack_Express_Local_Sql_010(
                                            ToolBox_Con.getPreference_Customer_Code(context)
                                    ).toSqlQuery()
                            ).get(SO_Pack_Express_Local_Sql_010.BADGE_IN_NEW_QTY);
                        } catch (Exception e) {
                            qtySO_Express = "0";
                        }
                        //Serial
                        try {
                            qtySerial = mdProductDao.getByStringHM(
                                    new Sql_Act005_008(
                                            ToolBox_Con.getPreference_Customer_Code(context)
                                    ).toSqlQuery()
                            ).get(Sql_Act005_008.BADGE_TO_SEND_QTY);
                        } catch (Exception e) {
                            qtySerial = "0";
                        }

                        //Soma Qtd de n-form e n_service e form_ap
                        menu.addInBadge1(qty);
                        menu.addInBadge1(qtySO);
                        menu.addInBadge1(ToolBox_Inf.isSoWithinTokenFile());
                        menu.addInBadge1(qtySerial);
                        menu.addInBadge1(ToolBox_Inf.isSerialWithinTokenFile());
                        menu.addInBadge1(qtyAP);
                        menu.addInBadge1(qtySO_Express);
//                        qty = String.valueOf(
//                                ToolBox_Inf.convertStringToInt(qty) +
//                                        ToolBox_Inf.convertStringToInt(qtySO) +
//                                        isSoWithinTokenFile() +
//                                        isSerialWithinTokenFile() +
//                                        ToolBox_Inf.convertStringToInt(qtyAP) +
//                                        ToolBox_Inf.convertStringToInt(qtySO_Express)
//                        );
                        //
                        break;

                    case Act005_Main.MENU_ID_SCHEDULE_DATA:
                        //Qtd de horas pra frente q deve ser consderada
                        //na hora de contar itens no badge
                        int forward_hour = 12;
                        try {
                            qty = customFormLocalDao.getByStringHM(
                                    new Sql_Act005_003(
                                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                            forward_hour
                                    ).toSqlQuery()
                            ).get(Sql_Act005_003.BADGE_SCHEDULED_QTY);
                        } catch (Exception e) {
                            qty = "0";
                        }
                        //
                        try {
                            qtyAP = customFormApDao.getByStringHM(
                                    new Sql_Act005_006(
                                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                            forward_hour
                                    ).toSqlQuery()
                            ).get(Sql_Act005_006.BADGE_SCHEDULED_QTY);
                        } catch (Exception e) {
                            qtyAP = "0";
                        }
                        menu.addInBadge1(qty);
                        menu.addInBadge1(qtyAP);
//                        qty = String.valueOf(
//                                ToolBox_Inf.convertStringToInt(qty) +
//                                        ToolBox_Inf.convertStringToInt(qtyAP)
//                        );
                        //
                        break;

                    case Act005_Main.MENU_ID_MESSAGES:
                        try {
                            qty = fcmMessageDao.getByStringHM(
                                    new FCMMessage_Sql_003().toSqlQuery()
                            ).get(FCMMessage_Sql_003.BADGE_MESSAGES_QTY);
                        } catch (Exception e) {
                            qty = "0";
                        }
                        menu.addInBadge1(qty);
                        break;

                    case Act005_Main.MENU_ID_CHAT:
                        //qty = "1";
                        //menu.addInBadge1(qty);

                        try {
                            qty = chMessageDao.getByStringHM(
                                    new CH_Message_Sql_025(
                                            ToolBox_Con.getPreference_User_Code(context)
                                    ).toSqlQuery()
                            ).get(CH_Message_Sql_025.BADGE_MESSAGES_QTY);
                        } catch (Exception e) {
                            qty = "0";
                        }
                        menu.addInBadge1(qty);

                        break;
                    case Act005_Main.MENU_ID_FORM_AP:
                        try {
                            qty = customFormApDao.getByStringHM(
                                    new GE_Custom_Form_Ap_Sql_001(
                                            ToolBox_Con.getPreference_Customer_Code(context)
                                    ).toSqlQuery()
                            ).get(GE_Custom_Form_Ap_Sql_001.BADGE_IN_PROCESSING_QTY);
                        } catch (Exception e) {
                            qty = "0";
                        }
                        //
                        try {
                            qtyBadge2 = customFormApDao.getByStringHM(
                                    new GE_Custom_Form_Ap_Sql_002(
                                            ToolBox_Con.getPreference_Customer_Code(context)
                                    ).toSqlQuery()
                            ).get(GE_Custom_Form_Ap_Sql_002.BADGE_SYNC_REQUIRED_QTY);
                        } catch (Exception e) {
                            qtyBadge2 = "0";
                        }
                        //
                        menu.addInBadge1(qty);
                        menu.addInBadge2(qtyBadge2);

                        break;
                    default:
                        menu.addInBadge1(qty);
                        menu.addInBadge2(qtySO);
                        break;
                }
                grantedMenus.add(menu);
            }
        }
        //
        //mView.loadMenuV2(menuList);
        mView.loadMenuV2(grantedMenus);
    }

    @Override
    public int getChatBadgeQty() {
        String qty = "0";
        //
        try {
            qty = chMessageDao.getByStringHM(
                    new CH_Message_Sql_025(
                            ToolBox_Con.getPreference_User_Code(context)
                    ).toSqlQuery()
            ).get(CH_Message_Sql_025.BADGE_MESSAGES_QTY);
        } catch (Exception e) {
            qty = "0";
        }
        //
        return ToolBox_Inf.convertStringToInt(qty);
    }

    @Override
    public void executeSyncProcess(int jump_validation_UR) {

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_MAIN);
        data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
        //if (ToolBox_Inf.parameterExists(context, Constant.PARAM_SCHEDULE_CHECKLIST)) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SCHEDULE_CHECKLIST, null)) {
            data_package.add(DataPackage.DATA_PACKAGE_SCHEDULE);
        }
        //Não é necessario verificar se tem PARAM_SO_MOV,pois esse parametro sempre
        //vem acompanhado do PARAM_SO.
        //
        // if (ToolBox_Inf.parameterExists(context, Constant.PARAM_SO)) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
            data_package.add(DataPackage.DATA_PACKAGE_SO);
        }
        data_package.add(DataPackage.DATA_PACKAGE_AP);
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
        bundle.putInt(Constant.GC_STATUS_JUMP, jump_validation_UR);//Valida Update require
        bundle.putInt(Constant.GC_STATUS, 1);
        //LUCHE - 07/06/2019
        //Add param que redefine timeout da chamada.Usada somente no sync full
        bundle.putInt(Constant.WS_CONNECTION_TIMEOUT, 120000);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_start_sync"), "", "0");

    }

    @Override
    public void accessMenuItem(String menu_id, int jump_validation_UR) {

        mView.setWsSoProcess("");

        try {
            switch (menu_id) {
                case Act005_Main.MENU_ID_CHECKLIST:
                    mView.callAct006(context);
                    break;

                case Act005_Main.MENU_ID_FORM_AP:
                    mView.callAct036(context);
                    break;

                case Act005_Main.MENU_ID_SERVICE:
                    mView.callAct021(context);
                    break;

                case Act005_Main.MENU_ID_SCHEDULE_DATA:
                    //mView.callAct016(context);
                    mView.callAct046(context);
                    break;

                case Act005_Main.MENU_ID_SERIAL:
                    mView.callAct030(context);
                    break;

                case Act005_Main.MENU_ID_IO_ASSETS:
                    if(isSiteLoggedIoControl()) {
                        mView.callAct051(context);
                    }else{
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_site_no_io_control_ttl"),
                                hmAux_Trans.get("alert_site_no_io_control_msg"),
                                null,
                                0
                        );
                    }
                    break;

                case Act005_Main.MENU_ID_PENDING_DATA:
                    mView.callAct012(context);
                    break;

                case Act005_Main.MENU_ID_HISTORIC_DATA:
                    mView.callAct014(context);
                    break;

                case Act005_Main.MENU_ID_MESSAGES:
                    mView.callAct018(context);
                    break;

                case Act005_Main.MENU_ID_SEND_DATA:
                    if (ToolBox_Con.isOnline(context)) {
                        mView.setWsProcess(Act005_Main.WS_PROCESS_SEND);
                        mView.setWsSoProcess(WS_Serial_Save.class.getSimpleName());
                        mView.showPD();
                        mView.cleanUpResults();
                        //executeSaveProcess();
                        executeSerialSave();
                    } else {
                        mView.showNoConnectionDialog();
                    }

                    break;

                case Act005_Main.MENU_ID_SYNC_DATA:
                    if (ToolBox_Con.isOnline(context)) {
                        mView.setWsProcess(Act005_Main.WS_PROCESS_SYNC);
                        mView.showPD();
                        executeSyncProcess(jump_validation_UR);
                    } else {
                        mView.showNoConnectionDialog();
                    }
                    break;

                case Act005_Main.MENU_ID_CHAT:
                    mView.callAct034(context);
                    break;

                case Act005_Main.MENU_ID_CLOSE:
                    mView.closeApp();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            e.printStackTrace();
        }
    }

    @Override
    public void showSupportDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act005_dialog_support, null);

        /**
         * Ini Vars
         */
        TextView tv_msg = (TextView) view.findViewById(R.id.act005_dialog_support_tv_msg);
        tv_msg.setText(hmAux_Trans.get("alert_support_msg"));
        final MKEditTextNM et_support_msg = (MKEditTextNM) view.findViewById(R.id.act005_dialog_support_et_msg);
        et_support_msg.setHint(hmAux_Trans.get("alert_support_hint"));

        builder.setTitle(hmAux_Trans.get("alert_support_ttl"));
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), null);
        builder.setNegativeButton(hmAux_Trans.get("sys_alert_btn_cancel"), null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                //
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_support_msg.getText().toString().trim().length() > 0) {
                            executeSupport(et_support_msg.getText().toString().trim());
                            //
                            dialog.dismiss();
                        } else {
                            et_support_msg.setText("");
                            et_support_msg.findFocus();
                            //
                            Toast.makeText(
                                    context,
                                    hmAux_Trans.get("alert_support_empty_msg"),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });
            }
        });
        dialog.show();
        //builder.show();
    }

    @Override
    public void showLogoutDialog() {

        logoutDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act005_dialog_logout, null);

        /**
         * Ini Vars
         */

        TextView tv_customer_selection = (TextView) view.findViewById(R.id.act005_dialog_logout_tv_customer_select);
        tv_customer_selection.setText(hmAux_Trans.get("logout_dialog_customer_select"));

        CheckBox chk_all = (CheckBox) view.findViewById(R.id.act005_dialog_logout_chk_all);
        chk_all.setText(hmAux_Trans.get("logout_dialog_check_all"));
        //
        TextView tv_logout_title = (TextView) view.findViewById(R.id.act005_dialog_logout_tv_logout_lbl);
        tv_logout_title.setText(hmAux_Trans.get("logout_dialog_ttl"));
        //
        lv_customer = (ListView) view.findViewById(R.id.act005_dialog_logout_lv_customer);
        //
        Button btn_logout = (Button) view.findViewById(R.id.act005_dialog_logout_btn_logout);
        btn_logout.setText(hmAux_Trans.get("logout_dialog_btn"));

        /**
         * Ini Actions
         */

        customer_list = userCustomerDao.query_HM(
                new EV_User_Customer_Sql_004(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        ToolBox_Con.getPreference_User_Code(context)
                ).toSqlQuery()
        );

        mAdapter = new Act005_Logout_Adapter(context, customer_list);

        lv_customer.setAdapter(mAdapter);

        lv_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                if (item.get(EV_User_Customer_Sql_004.KEY_LOGOUT).equals("0")) {
                    item.put(EV_User_Customer_Sql_004.KEY_LOGOUT, "1");
                } else {
                    item.put(EV_User_Customer_Sql_004.KEY_LOGOUT, "0");
                }

                mAdapter.notifyDataSetChanged();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (HMAux aux : customer_list) {
                    if (aux.get(EV_User_Customer_Sql_004.KEY_LOGOUT).equals("1")) {
                        logoutList += aux.get(EV_User_CustomerDao.CUSTOMER_CODE) + "|";
                        if (aux.get(EV_User_CustomerDao.CUSTOMER_CODE).equals(String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)))) {
                            ToolBox_Con.setPreference_Customer_Code(context, -1L);
                        }
                    }
                }

                if (logoutList.length() > 0) {
                    //Cancela notificações do chat
                    ToolBox_Inf.cancelChatNotification(context);
                    //
                    logoutList = logoutList.substring(0, logoutList.length() - 1);
                    //
                    if (ToolBox_Con.isOnline(context)) {
                        executeLogout(logoutList);
                    } else {
                        if (ToolBox_Con.getPreference_Customer_Code(context) == -1L) {

                            if (existOthersSession()) {
                                mView.callChangeCustomerProcess();
                            } else {
                                mView.callLoginProcess();
                            }
                        } else {
                            // fechar drawer Hugo
                        }
                    }
                }
                //
                logoutList = "";
                logoutDialog.dismiss();
            }
        });

        chk_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                for (HMAux aux : customer_list) {
                    if (checkBox.isChecked()) {
                        aux.put(EV_User_Customer_Sql_004.KEY_LOGOUT, "1");
                    } else {
                        aux.put(EV_User_Customer_Sql_004.KEY_LOGOUT, "0");
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dmW = (float) dm.widthPixels * 0.98F;
        float dmH = (float) dm.heightPixels * 0.90F;

        logoutDialog.setContentView(view);
        logoutDialog.setCancelable(true);
        logoutDialog.getWindow().setLayout((int) dmW, (int) dmH);

        logoutDialog.show();
    }

    @Override
    public void executeSaveProcess() {
        mView.setWsSoProcess(WS_Save.class.getSimpleName());

        Intent mIntent = new Intent(context, WBR_Save.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);//Pula validação Update require
        bundle.putInt(Constant.GC_STATUS, 1);//Pula validação de other device
        bundle.putString(Act005_Main.WS_PROCESS_SO_STATUS, "SEND");

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_preparing_to_send_data"), "", "0");

    }

    @Override
    public void executeSoSave() {
        mView.setWsSoProcess(WS_PROCESS_SO_SAVE);
        //
        Intent mIntent = new Intent(context, WBR_SO_Save.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSoSaveApproval() {
        mView.setWsSoProcess(WS_PROCESS_SO_SAVE_APPROVAL);
        //
        Intent mIntent = new Intent(context, WBR_SO_Approval.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSOPackExpress() {
        mView.setWsSoProcess(WS_SO_Pack_Express_Local.class.getSimpleName());
        //
        Intent mIntent = new Intent(context, WBR_SO_Pack_Express_Local.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeApSave() {
        mView.setWsSoProcess(WS_AP_Save.class.getSimpleName());
        //
        Intent mIntent = new Intent(context, WBR_AP_Save.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSerialSave() {
        mView.setWsSoProcess(WS_Serial_Save.class.getSimpleName());
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void executeLogout(String customer_list) {
        mView.setWsProcess(Act005_Main.WS_PROCESS_LOGOUT);

        mView.showPD();

        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, customer_list);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeEnableNFC() {
        mView.setWsProcess(Act005_Main.WS_PROCESS_ENABLE_NFC);

        mView.showPD();

        Intent mIntent = new Intent(context, WBR_Enable_NFC.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeCancelNFC() {
        mView.setWsProcess(Act005_Main.WS_PROCESS_CANCEL_NFC);

        mView.showPD();

        Intent mIntent = new Intent(context, WBR_Cancel_NFC.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSupport(String support_msg) {

        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(Act005_Main.WS_PROCESS_SUPPORT);

            mView.showPD();

            Intent mIntent = new Intent(context, WBR_Upload_Support.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.WS_SUPPORT_MSG, support_msg);

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

//    private int isSoWithinTokenFile() {
//        try {
//            File[] soToken = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_SO_PREFIX);
//            if (soToken.length > 0) {
//                Gson gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
//                //
//                ArrayList<SM_SO> token_so_list =
//                        gsonEnv.fromJson(
//                                ToolBox_Inf.getContents(soToken[0]),
//                                TSO_Save_Env.class
//                        ).getSo();
//                //
//                return token_so_list.size();
//            } else {
//                return 0;
//            }
//        } catch (Exception e) {
//            ToolBox_Inf.registerException(getClass().getName(), e);
//            //
//            return 0;
//        }
//    }
//
//    private int isSerialWithinTokenFile() {
//        int qty = 0;
//        //
//        try {
//            File[] serialToken = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_SERIAL_PREFIX);
//            if (serialToken.length > 0) {
//                Gson gsonEnv = new GsonBuilder().serializeNulls().create();
//                //
//                ArrayList<MD_Product_Serial> token_serial_list =
//                        gsonEnv.fromJson(
//                                ToolBox_Inf.getContents(serialToken[0]),
//                                TSerial_Save_Env.class
//                        ).getSerial();
//                //
//                return token_serial_list.size() + qty;
//            } else {
//                return 0 + qty;
//            }
//        } catch (Exception e) {
//            ToolBox_Inf.registerException(getClass().getName(), e);
//            //
//            return 0 + qty;
//        }
//    }

    @Override
    public void stopChatServices() {
        //No logoff, verifica se era a ultima sessão ativa.
        //Caso fosse, parar serviços do chat e do screestatus
        if (!existOthersSession()) {
            if (AppBackgroundService.isRunning) {
                //
                Intent socketService = new Intent(context, AppBackgroundService.class);
                context.stopService(socketService);
            }

            if (ScreenStatusService.isRunning) {
                Intent screenService = new Intent(context, ScreenStatusService.class);
                context.stopService(screenService);
            }
        }
    }

    @Override
    public boolean existOthersSession() {
        List<HMAux> sessionsOn = userCustomerDao
                .query_HM(
                        new EV_User_Customer_Sql_004(
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                );

        if (sessionsOn != null && sessionsOn.size() != 0) {
            return true;
        }
        return false;
    }

    @Override
    public void clearLocalSession() {
        userCustomerDao.addUpdate(
                new EV_User_Customer_Sql_005(
                        ToolBox_Con.getPreference_User_Code(context),
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );
    }

    @Override
    public void syncFlow(int to_send_qty) {
        if (to_send_qty > 0) {
            mView.setSyncAfterSave(true);
            //
            accessMenuItem(Act005_Main.MENU_ID_SEND_DATA, 0);

//            ToolBox.alertMSG(
//                    context,
//                    hmAux_Trans.get("alert_send_to_sync_ttl"),
//                    hmAux_Trans.get("alert_send_to_sync_msg"),
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mView.setSyncAfterSave(true);
//                            //
//                            accessMenuItem(Act005_Main.MENU_ID_SEND_DATA, 0);
//                        }
//                    },
//                    0
//            );
        } else {
            accessMenuItem(Act005_Main.MENU_ID_SYNC_DATA, 0);
        }
    }

    @Override
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

    private boolean isSiteLoggedIoControl(){
        MD_Site mdSite = siteDao.getByString(
                new MD_Site_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context)
                ).toSqlQuery()
        );
        //
        if(mdSite != null && mdSite.getIo_control() == 1){
            return true;
        }
        return false;
    }
}
