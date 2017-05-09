package com.namoadigital.prj001.ui.act005;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act005_Logout_Adapter;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.receiver.WBR_Cancel_NFC;
import com.namoadigital.prj001.receiver.WBR_Enable_NFC;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_004;
import com.namoadigital.prj001.sql.FCMMessage_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act005_001;
import com.namoadigital.prj001.sql.Sql_Act005_002;
import com.namoadigital.prj001.sql.Sql_Act005_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

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

    //logout dialog
    private String logoutList = "";
    private transient Dialog logoutDialog;
    private Act005_Logout_Adapter mAdapter;
    private ListView lv_customer;
    private List<HMAux> customer_list;

    public Act005_Main_Presenter_Impl(Context context, Act005_Main_View mView, GE_Custom_Form_LocalDao customFormLocalDao, HMAux hmAux_Trans, EV_User_CustomerDao userCustomerDao, FCMMessageDao fcmMessageDao) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.hmAux_Trans = hmAux_Trans;
        this.userCustomerDao = userCustomerDao;
        this.fcmMessageDao = fcmMessageDao;
    }

    String[] menuId = {
            Act005_Main.MENU_ID_CHECKLIST,
            Act005_Main.MENU_ID_SCHEDULE_DATA,
            Act005_Main.MENU_ID_PENDING_DATA,
            Act005_Main.MENU_ID_HISTORIC_DATA,
            Act005_Main.MENU_ID_MESSAGES,
            Act005_Main.MENU_ID_SEND_DATA,
            //       Act005_Main.MENU_ID_SYNC_DATA,
            Act005_Main.MENU_ID_CLOSE
    };

    String[] menuDesc = {
            "lbl_checklist",
            "lbl_schedule_data",
            "lbl_pending_data",
            "lbl_historic_data",
            "lbl_messages",
            "lbl_send_data",
            //        "lbl_sync_data",
            "lbl_close_app"
    };

    String[] icon = {
            String.valueOf(R.drawable.ic_n_form),
            String.valueOf(R.drawable.ic_calendario),
            String.valueOf(R.drawable.ic_pendente),
            String.valueOf(R.drawable.ic_historico),
            String.valueOf(R.drawable.ic_notificacao),
            String.valueOf(R.drawable.ic_enviar),
            //       String.valueOf(R.drawable.ic_sincronizar),
            String.valueOf(R.drawable.ic_sair)
    };

    String[] parameter = {
            "",
            Constant.PARAM_SCHEDULE_CHECKLIST,
            "",
            "",
            "",
            "",
            // "",
            ""
    };


    @Override
    public void getMenuItens(HMAux hmAux_Trans) {
        List<HMAux> menuList = new ArrayList<>();

        for (int i = 0; i < menuId.length; i++) {
            //SÓ exibe item de menu se menu não requer param
            //ou se customer possui acesso ao param
            boolean showMenu = parameter[i].equals("") || ToolBox_Inf.parameterExists(context, parameter[i]);
            //
            if (showMenu) {
                HMAux Aux = new HMAux();
                String qty = "";
                Aux.put(Act005_Main.MENU_ID, menuId[i]);
                Aux.put(Act005_Main.MENU_ICON, icon[i]);
                Aux.put(Act005_Main.MENU_DESC, menuDesc[i]);

                switch (menuId[i]) {
                    case Act005_Main.MENU_ID_PENDING_DATA:
                        qty = customFormLocalDao.getByStringHM(
                                new Sql_Act005_001(
                                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                                ).toSqlQuery()
                        ).get(Sql_Act005_001.BADGE_IN_PROCESSING_QTY);

                        Aux.put(Act005_Main.MENU_BADGE, qty);
                        break;

                    case Act005_Main.MENU_ID_SEND_DATA:
                        qty = customFormLocalDao.getByStringHM(
                                new Sql_Act005_002(
                                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                                ).toSqlQuery()
                        ).get(Sql_Act005_002.BADGE_FINALIZED_QTY);

                        Aux.put(Act005_Main.MENU_BADGE, qty);
                        break;

                    case Act005_Main.MENU_ID_SCHEDULE_DATA:
                        qty = customFormLocalDao.getByStringHM(
                                new Sql_Act005_003(
                                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                                ).toSqlQuery()
                        ).get(Sql_Act005_003.BADGE_SCHEDULED_QTY);

                        Aux.put(Act005_Main.MENU_BADGE, qty);
                        break;

                    case Act005_Main.MENU_ID_MESSAGES:
                        qty = fcmMessageDao.getByStringHM(
                                new FCMMessage_Sql_003().toSqlQuery()
                        ).get(FCMMessage_Sql_003.BADGE_MESSAGES_QTY);

                        Aux.put(Act005_Main.MENU_BADGE, qty);

                    default:
                        Aux.put(Act005_Main.MENU_BADGE, qty);
                        break;
                }

                menuList.add(Aux);
            }
        }
        mView.loadMenu(menuList);
    }

    @Override
    public void executeSyncProcess(int jump_validation_UR) {

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_MAIN);
        data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
        if (ToolBox_Inf.parameterExists(context,Constant.PARAM_SCHEDULE_CHECKLIST)) {
            data_package.add(DataPackage.DATA_PACKAGE_SCHEDULE);
        }

        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
        bundle.putInt(Constant.GC_STATUS_JUMP, jump_validation_UR);//Valida Update require
        bundle.putInt(Constant.GC_STATUS, 1);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_start_sync"), "", "0");

    }

    @Override
    public void accessMenuItem(String menu_id, int jump_validation_UR) {

        try {
            switch (menu_id) {
                case Act005_Main.MENU_ID_CHECKLIST:
                    mView.callAct006(context);
                    break;
                case Act005_Main.MENU_ID_SCHEDULE_DATA:
                    mView.callAct016(context);
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
                        mView.showPD();
                        executeSaveProcess();
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

                case Act005_Main.MENU_ID_CLOSE:
                    mView.closeApp();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    //
                    logoutList = logoutList.substring(0, logoutList.length() - 1);
                    //
                    if (ToolBox_Con.isOnline(context)) {
                        executeLogout(logoutList);
                    } else {
                        if (ToolBox_Con.getPreference_Customer_Code(context) == -1L) {
                            mView.callLoginProcess();
                        }
                    }
                }
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

    private void executeSaveProcess() {

        Intent mIntent = new Intent(context, WBR_Save.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);//Pula validação Update require
        bundle.putInt(Constant.GC_STATUS, 1);//Pula validação de other device

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_preparing_to_send_data"), "", "0");

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
        //ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_preparing_to_send_data"), "", "0");

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
    public void executeSupport() {
        mView.setWsProcess(Act005_Main.WS_PROCESS_SUPPORT);

        //mView.showPD();

    }
}
