package com.namoadigital.prj001.ui.act034;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.model.Chat_Room_Info_Env;
import com.namoadigital.prj001.model.Chat_Room_Info_Rec;
import com.namoadigital.prj001.model.Chat_UserList_Info_Env;
import com.namoadigital.prj001.model.Chat_UserList_Info_Rec;
import com.namoadigital.prj001.receiver.NotificationReceiver;
import com.namoadigital.prj001.receiver_chat.WBR_Add_User_Room_AP;
import com.namoadigital.prj001.receiver_chat.WBR_Leave_Room;
import com.namoadigital.prj001.receiver_chat.WBR_Room_Private;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Main extends Base_Activity_Frag implements Act034_Main_View {

    public static final String FRAG_TAG_ROOM = "ROOM";

    public static boolean bTT = false;

    private String room_code_private = "";

    private Act034_Main_Presenter mPresenter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fm;
    private Act034_Opc act034_opc;
    private Act034_Room act034_room;
    private String currentFrag = "";
    private BR_Room brRoomReceiver;
    private AlertDialog infoDialog = null;
    private Bundle bundle;
    private String returnedRoomCode = null;
    private String lastFirstAdapterPosition = "0";
    //
    private LinearLayout ll_info;
    private ImageView iv_info_icon;
    private TextView tv_info_msg_1;
    private TextView tv_info_msg_2;
    private ArrayList<HMAux> customer_list = new ArrayList<>();
    private RoomInfoTask roomInfoTask;
    //private DownloadMemberImgTask downloadMemberImgTask;
    private UserListInfoTask userListInfoTask;
    private long selected_customer;
    private HashMap<String,String> auxFilters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.act034_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
        //
        startReceivers(true);
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT034
        );
        //
        fm = getSupportFragmentManager();
        //
        loadTranslation();
        //
        mActivity_ID = Constant.ACT034;

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("customer_list_ttl");
        transList.add("other_customers_msg_lbl");
        transList.add("trying_to_reconnect_lbl");
        transList.add("search_room_hint");
        transList.add("no_room_found_lbl");
        transList.add("room_type_workgroup_lbl");
        transList.add("room_type_private_lbl");
        transList.add("room_type_so_lbl");
        transList.add("room_type_pa_lbl");
        transList.add("room_dialog_filter_ttl");
        transList.add("ws_room_info_ttl");
        transList.add("ws_room_info_msg");
        transList.add("ws_room_info_msg");
        transList.add("progress_create_room_ttl");
        transList.add("progress_create_room_msg ");
        transList.add("progress_remove_room_ttl");
        transList.add("progress_remove_room_msg");
        transList.add("progress_leave_room_ttl");
        transList.add("progress_leave_room_msg");
        transList.add("progress_user_list_ttl");
        transList.add("progress_user_list_msg");
        //FragRoom
        transList.add("alert_room_info_members_ttl");
        transList.add("alert_room_info_no_members_ttl");
        transList.add("alert_create_room_ttl");
        transList.add("alert_create_room_confirm_msg");
        transList.add("alert_remove_room_ttl");
        transList.add("alert_remove_room_confirm_msg");
        transList.add("alert_user_list_user_lbl");
        transList.add("alert_user_list_no_user_lbl");
        transList.add("alert_no_room_info_ttl");
        transList.add("alert_no_room_info_msg");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.act034_drawer);
        //
        mDrawerToggle = new ActionBarDrawerToggle(
                Act034_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_opened
        ) {
            //@SuppressLint("RestrictedApi")
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //
                act034_opc.loadDataToScreen();
                //
                invalidateOptionsMenu();
            }

            //@SuppressLint("RestrictedApi")
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //
                act034_opc.loadDataToScreen();
                //
                invalidateOptionsMenu();
            }
        };
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //
        mDrawerToggle.syncState();
        //
        mPresenter = new Act034_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new EV_User_CustomerDao(context),
                new CH_RoomDao(context)
        );
        //
        mPresenter.tryToRestartChatService();
        //
        ll_info = (LinearLayout) findViewById(R.id.act034_main_ll_info);
        //
        iv_info_icon = (ImageView) findViewById(R.id.act034_main_iv_icon);
        iv_info_icon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation_anim));
        //
        tv_info_msg_1 = (TextView) findViewById(R.id.act034_main_tv_msg_1);
        //
        tv_info_msg_2 = (TextView) findViewById(R.id.act034_main_tv_msg_2);
        //Recebe lista com nome dos customers
        /*customer_list = mPresenter.getCustomerMessageList(
                mPresenter.getCustomerNameList()
        );*/
        //
        recoverIntentsInfo();
        //
        initFragments();
        //
        setFrag(act034_room, FRAG_TAG_ROOM);
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        bTT = getIntent().getBooleanExtra(NotificationReceiver.NOTIFICATION, false);
        auxFilters = new HashMap<>();
        //Por padrão, seta o customer atual como o selecionado
        selected_customer = ToolBox_Con.getPreference_Customer_Code(context);
        if (bundle != null) {
            returnedRoomCode = bundle.getString(CH_MessageDao.ROOM_CODE);
            String mReload = bundle.getString(Constant.CHAT_RELOAD, "0");
            selected_customer = bundle.getLong(CH_RoomDao.CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context));
            lastFirstAdapterPosition = bundle.getString(Constant.CHAT_ROOM_POSITION, "0");
            if(bundle.containsKey(CH_RoomDao.ROOM_TYPE)){
                auxFilters = (HashMap<String, String>) bundle.getSerializable(CH_RoomDao.ROOM_TYPE);
            }
            if (mReload.equalsIgnoreCase("1")) {
                HMAux item = new HMAux();
                item.put(CH_RoomDao.ROOM_CODE, returnedRoomCode);
                item.put(Constant.CHAT_ROOM_POSITION, String.valueOf(act034_room != null ? act034_room.getFirstVisiblePosition() : 0));
                //
                callAct035(context, item, mReload);
            } else {
                SingletonWebSocket.mRoom_private = "";
            }
        } else {
        }
    }

    public String getReturnedRoomCode() {
        return returnedRoomCode;
    }

    public String getLastFirstAdapterPosition() {
        return lastFirstAdapterPosition;
    }

    public void setLastFirstAdapterPosition(String lastFirstAdapterPosition) {
        this.lastFirstAdapterPosition = lastFirstAdapterPosition;
    }

    public ArrayList<HMAux> getCustomer_list() {
        return customer_list;
    }

    @Override
    public long getSelected_Customer() {
        return selected_customer;
    }

    @Override
    public void startReceivers(boolean start_stop) {
        if (brRoomReceiver == null) {
            brRoomReceiver = new BR_Room();
        }
        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);

        IntentFilter roomPrivateFilter = new IntentFilter(Constant.CHAT_EVENT_C_ROOM_PRIVATE);
        roomPrivateFilter.addCategory(Intent.CATEGORY_DEFAULT);
        //
        if (start_stop) {
            LocalBroadcastManager.getInstance(Act034_Main.this).registerReceiver(brRoomReceiver, brRoomFilter);
        } else {
            LocalBroadcastManager.getInstance(Act034_Main.this).unregisterReceiver(brRoomReceiver);
            //
            brRoomReceiver = null;
        }
    }

    private void initFragments() {
        //Drawer
        act034_opc = (Act034_Opc) fm.findFragmentById(R.id.act034_opc);
        //
        act034_opc.setHmAux_Trans(hmAux_Trans);
        //
        updateDrawerCustomerList();
        //
        act034_opc.loadDataToScreen();
        //
        /*
        * Room Fragment
        */
        act034_room = new Act034_Room();
        //
        act034_room.setBaInfra(this);
        act034_room.setHmAux_Trans(hmAux_Trans);
        act034_room.loadDataToScreen();
        act034_room.setFilterValues(auxFilters);

    }

    private void updateDrawerCustomerList() {
        customer_list = mPresenter.getCustomerMessageList(
                mPresenter.getCustomerNameList()
        );
        //
        act034_opc.setCustomerList(customer_list);
        //
        /*boolean customerInList = false;
        for (int i = 0; i < customer_list.size(); i++) {
            if (
                    customer_list.get(i).get(CH_RoomDao.CUSTOMER_CODE).equals(
                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)))
                    ) {
                customerInList = true;
                break;
            }
        }
        //
        if (!customerInList) {
            HMAux aux = new HMAux();
            aux.put(CH_RoomDao.CUSTOMER_CODE, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
            aux.put(EV_User_CustomerDao.CUSTOMER_NAME, ToolBox_Con.getPreference_Customer_Code_NAME(context));
            aux.put(Sql_Act034_001.MSG_QTY, "0");
            //
            customer_list.add(0, aux);
        }*/

        setDrawerState(customer_list.size() > 1);

    }

    public void setSelectedCustomer(long customer_code) {
        selected_customer = customer_code;
        //updateDrawerCustomerList();
        act034_room.loadDataToScreen();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public String getSeletedCustomerName() {
        String customer_name = "";
        for (HMAux hmAux : customer_list) {
            if (hmAux.get(EV_User_CustomerDao.CUSTOMER_CODE).equalsIgnoreCase(String.valueOf(selected_customer))) {
                customer_name = hmAux.get(EV_User_CustomerDao.CUSTOMER_NAME);
                break;
            }
        }
        return customer_name;
    }

    /**
     * Habilita ou desabilita o drawer
     *
     * @param isEnabled
     */
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            //mDrawerLayout.openDrawer(GravityCompat.START);
            mDrawerLayout.closeDrawer(GravityCompat.START);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mDrawerToggle.syncState();

        } else {

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerLayout.closeDrawer(GravityCompat.START);

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);

            mDrawerToggle.setDrawerIndicatorEnabled(false);

            mDrawerToggle.syncState();
        }
    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act034_main_ll, type, sTag);
            ft.commit();
            setCurrentFrag(sTag);
        }
    }

    public void setCurrentFrag(String currentFrag) {
        this.currentFrag = currentFrag;
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT034;
        mAct_Title = Constant.ACT034 + "_" + "title";
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

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct035(Context context, HMAux item, String mReload) {
        Intent mIntent = new Intent(context, Act035_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtra(NotificationReceiver.NOTIFICATION, bTT);
        //
        Bundle bundle = new Bundle();
        bundle.putString(CH_RoomDao.ROOM_CODE, item.get(CH_RoomDao.ROOM_CODE));
        bundle.putLong(CH_RoomDao.CUSTOMER_CODE, selected_customer);
        bundle.putString(Constant.CHAT_ROOM_POSITION, lastFirstAdapterPosition);
        //
        bundle.putSerializable(CH_RoomDao.ROOM_TYPE, mReload.equalsIgnoreCase("0") ?  act034_room.getFilterArrayValues() : auxFilters);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    /*
    * Receivers
    */

    private class BR_Room extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.CHAT_BR_TYPE);
            HMAux auxParam = (HMAux) intent.getSerializableExtra(Constant.CHAT_BR_PARAM);
            type += "";

            switch (type) {
                case Constant.CHAT_BR_TYPE_ROOM:
                case Constant.CHAT_BR_TYPE_MSG:
                    //Atualiza drawer
                    updateDrawerCustomerList();
                    //
                    if (currentFrag.equalsIgnoreCase(FRAG_TAG_ROOM)) {
                        //act034_room.loadRoomList();
                        act034_room.loadDataToScreen();
                    }
                    if (act034_opc != null) {
                        act034_opc.loadDataToScreen();
                    }
                    break;
                case Constant.CHAT_BR_TYPE_ROOM_PRIVATE_ADD:
                    if (currentFrag.equalsIgnoreCase(FRAG_TAG_ROOM)) {
                        processRoomPrivateReturn(auxParam);
                    }
                    break;
                case Constant.CHAT_BR_TYPE_ROOM_PRIVATE_REMOVE:
                    if (currentFrag.equalsIgnoreCase(FRAG_TAG_ROOM)) {
                        act034_room.loadDataToScreen();
                        disablePD();
                    }
                    break;//
                case Constant.CHAT_BR_TYPE_LEAVE_ROOM:
                    if (currentFrag.equalsIgnoreCase(FRAG_TAG_ROOM)) {
                        act034_room.loadDataToScreen();
                        disablePD();
                    }
                    break;//CHAT_BR_TYPE_LEAVE_ROOM
                case Constant.CHAT_BR_TYPE_RECONNECTED:
                    //toogleInfoMsg(false, null);
                    //changeConectionMenu();
                    break;
                case Constant.CHAT_BR_TYPE_RECONNECTING:
                    String qtd = String.valueOf(auxParam == null ? 0 : auxParam.get(Constant.CHAT_BR_PARAM_RECONNECTING_QTD));
                    //toogleInfoMsg(true, qtd);
                    //changeConectionMenu();
                    break;
                case Constant.CHAT_BR_TYPE_CHAT_LOGGED_STATUS_CHANGE:
                    changeConectionMenu();
                    break;
                default:
                    break;
            }
        }
    }

    private void processRoomPrivateReturn(HMAux auxParam) {
        String room_code = auxParam.get(CH_RoomDao.ROOM_CODE);
        //
        disablePD();
        //
        if (room_code != null) {
            callAct035(context, auxParam, "0");
        } else {
            act034_room.loadDataToScreen();
        }
    }

    private void changeConectionMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void showPD(String ttl, String msg, boolean cancelable) {
        //
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
        //
        //progressDialog.setCancelable(cancelable);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (roomInfoTask != null) {
                    roomInfoTask.cancel(true);
                }
            }
        });

    }

    @Override
    public void disablePD() {
        disableProgressDialog();
    }

    private void toogleInfoMsg(boolean visible, String qtd) {

        if (visible) {
            ll_info.setVisibility(View.VISIBLE);
            tv_info_msg_1.setText(hmAux_Trans.get("trying_to_reconnect_lbl"));
            tv_info_msg_2.setText(qtd);
        } else {
            ll_info.setVisibility(View.GONE);
            tv_info_msg_2.setText(null);
        }

    }

    private void hideShowReconnectingDialog(int on_off, int qtd) {

        if (on_off == 0) {
            if (infoDialog != null && infoDialog.isShowing()) {
                infoDialog.dismiss();
                return;
            }
        }
        //
        if (on_off == 1) {
            if (infoDialog != null && !infoDialog.isShowing()) {
                infoDialog = showReconnectingDialog(context, hmAux_Trans.get("trying_to_reconnect_lbl"), qtd != 0 ? String.valueOf(qtd) : null);
                return;
            }
        }
    }

    public static AlertDialog showReconnectingDialog(Context context, @Nullable String msg_1, @Nullable String msg_2) {
        int w = (int) ToolBox_Inf.convertDpToPixel(context, 100 * 1.1f);
        int h = (int) ToolBox_Inf.convertDpToPixel(context, 100 * 1.1f);
        //
        AlertDialog.Builder imageBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act034_reconnecting_dialog, null);

        ImageView iv_room = (ImageView) view.findViewById(R.id.act034_reconnecting_iv_icon);
        TextView tv_msg1 = (TextView) view.findViewById(R.id.act034_reconnecting_tv_msg_1);
        TextView tv_msg2 = (TextView) view.findViewById(R.id.act034_reconnecting_tv_msg_2);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotation_anim);
        iv_room.startAnimation(animation);
        //
        if (msg_1 != null) {
            tv_msg1.setText(msg_1);
            tv_msg1.setVisibility(View.VISIBLE);
        } else {
            tv_msg1.setVisibility(View.GONE);
        }
        //
        if (msg_2 != null) {
            tv_msg2.setText(msg_2);
            tv_msg2.setVisibility(View.VISIBLE);
        } else {
            tv_msg2.setVisibility(View.GONE);
        }

        imageBuilder.setView(view);
        imageBuilder.setCancelable(true);

        android.app.AlertDialog imageDialog = imageBuilder.create();
        //imageDialog.setContentView(view);
        imageDialog.show();
        //imageDialog.getWindow().setLayout(w,h);
        //imageDialog.getWindow().setLayout( w ,h );

        return imageDialog;
    }

    @Override
    public void startRoomInfoTask(String socket_id, String room_code) {
        roomInfoTask = new RoomInfoTask();
        roomInfoTask.execute(socket_id, room_code);
    }
    //

    @Override
    public void startDownloadMemberImgTask(String[] imgUrlList) {
//        if (downloadMemberImgTask != null) {
//            downloadMemberImgTask.cancel(true);
//        }
//        downloadMemberImgTask = new DownloadMemberImgTask();
//        downloadMemberImgTask.execute(imgUrlList);
    }

    @Override
    public void startUserListInfoTask(String socket_id, String customer_code, String room_type, String room_code) {
        userListInfoTask = new UserListInfoTask();
        userListInfoTask.execute(socket_id, customer_code,room_type,room_code);
    }

    @Override
    public void startRoomPrivateWS(String user_code, String customer_code, Integer active, @Nullable String room_code) {
        if (active == 1) {
            showPD(
                    hmAux_Trans.get("progress_create_room_ttl"),
                    hmAux_Trans.get("progress_create_room_msg"),
                    false);
        } else {
            showPD(
                    hmAux_Trans.get("progress_remove_room_ttl"),
                    hmAux_Trans.get("progress_remove_room_msg"),
                    false);
        }
        //
        Intent roomPrivateIntent = new Intent(context, WBR_Room_Private.class);
        Bundle roomPrivateBundle = new Bundle();
        roomPrivateBundle.putInt(Constant.CHAT_WS_ROOM_PRIVATE_ACTIVE_PARAM, active);
        roomPrivateBundle.putString(CH_RoomDao.USER_CODE, user_code);
        roomPrivateBundle.putString(CH_RoomDao.CUSTOMER_CODE, customer_code);
        roomPrivateBundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        roomPrivateIntent.putExtras(roomPrivateBundle);
        //
        context.sendBroadcast(roomPrivateIntent);
    }

    @Override
    public void startAddUserRoomAp(String socket_id, String room_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data, String ap_code, String user_code_sql) {
        showPD(
                hmAux_Trans.get("progress_create_room_ttl"),
                hmAux_Trans.get("progress_create_room_msg"),
                false);
        //
        //
        Intent addUsrRoomAp = new Intent(context, WBR_Add_User_Room_AP.class);
        Bundle addUserRoomApBundle = new Bundle();
        //
        addUserRoomApBundle.putString(Constant.CHAT_WS_SOCKET_ID_PARAM, socket_id);
        addUserRoomApBundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, custom_form_type);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, custom_form_code);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, custom_form_version);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, custom_form_data);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.AP_CODE, ap_code);
        addUserRoomApBundle.putString(CH_RoomDao.USER_CODE, user_code_sql);
        addUsrRoomAp.putExtras(addUserRoomApBundle);
        //
        context.sendBroadcast(addUsrRoomAp);

    }

    @Override
    public void startLeaveRoomWS(String user_code, String room_code) {
        showPD(
                hmAux_Trans.get("progress_leave_room_ttl"),
                hmAux_Trans.get("progress_leave_room_msg"),
                false);
        //
        Intent leaveRoomIntent = new Intent(context, WBR_Leave_Room.class);
        Bundle leaveRoomBundle = new Bundle();
        leaveRoomBundle.putString(CH_RoomDao.USER_CODE, user_code);
        leaveRoomBundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        leaveRoomIntent.putExtras(leaveRoomBundle);
        //
        context.sendBroadcast(leaveRoomIntent);
    }

    //region AsyncTask
    private class RoomInfoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ChatEvent", "RoomAsyncTask PreExecute");
            //
            showPD(
                    hmAux_Trans.get("ws_room_info_ttl"),
                    hmAux_Trans.get("ws_room_info_msg"),
                    true);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ChatEvent", "RoomAsyncTask DoInBackground");
            String resultado = "";
            try {
                String socket_id = params[0];
                String room_code = params[1];
                //
                Gson gson = new GsonBuilder().serializeNulls().create();
                //
                Chat_Room_Info_Env env = new Chat_Room_Info_Env();
                //
                env.setSession_app(ToolBox_Con.getPreference_Session_App(context));
                env.setRoom_code(room_code);
                env.setActive(1);
                //
                resultado = ToolBox_Con.connWebService(
                        Constant.WS_CHAT_ROOM_INFO,
                        gson.toJson(env)
                );

            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
                this.cancel(true);
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            Log.d("ChatEvent", "RoomAsyncTask OnPost");
            super.onPostExecute(resultado);
            Gson gson = new GsonBuilder().serializeNulls().create();
            //Se não retornou nada, finaliza execução.
            if (resultado.equals("")) {
                this.cancel(true);
                return;
            }
            try {
                //
                if (resultado.contains("error_msg")) {
                    //
                    Chat_C_Error cError =
                            gson.fromJson(
                                    ToolBox_Inf.getWebSocketJsonParam(resultado),
                                    Chat_C_Error.class
                            );
                    //
                    ToolBox.sendBCStatus(
                            context,
                            "ERROR_1",
                            cError != null ? cError.getError_msg() : "Error",
                            "",
                            "0"
                    );

                } else {
                    //
                    disablePD();
                    ArrayList<Chat_Room_Info_Rec> roomInfoList = gson
                            .fromJson(
                                    ToolBox_Inf.getWebSocketJsonParam(resultado),
                                    new TypeToken<ArrayList<Chat_Room_Info_Rec>>() {
                                    }.getType()
                            );
                    //
                    ArrayList<String> auxList = new ArrayList<>();
                    for (Chat_Room_Info_Rec info_rec : roomInfoList) {
                        if (info_rec.getSys_user_image() != null) {
                            auxList.add(
                                    info_rec.getUser_code()
                                            + Constant.MAIN_CONCAT_STRING + info_rec.getSys_user_image()
                                            + Constant.MAIN_CONCAT_STRING + info_rec.getSys_user_image_name()
                            );
                        }
                    }
                    //
                    act034_room.showRoomInfoDialog(roomInfoList);
                    //
                    String[] imgUrlList = new String[auxList.size()];
                    startDownloadMemberImgTask(auxList.toArray(imgUrlList));
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
                this.cancel(true);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ChatEvent", "RoomAsyncTask Cancelada");
            disablePD();
        }
    }

//    private class DownloadMemberImgTask extends AsyncTask<String, String, Void> {
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            //doInBackground NÃO TEM ACESSO A ATUALIZAR TELA
//            //QUANDO HOUVER NECESSIDADE DE ATUALIZAR,
//            //CHAMAR O METODO publishProgress() QUE TEM ACESSO.
//            for (int i = 0; i < strings.length; i++) {
//                //Se foi cancelado ou se não tem conexão aborta.
//                if (isCancelled() || !ToolBox_Con.isOnline(context)) {
//                    this.cancel(true);
//                    break;
//                } else {
//                    try {
//                        String[] downloadParam = strings[i].split(Constant.MAIN_CONCAT_STRING);
//                        String user_code = downloadParam[0];
//                        String url = downloadParam[1];
//
//                        String image_name = "ch_" + (!downloadParam[2].equals("null") ? downloadParam[2].substring(0, downloadParam[2].length() - 4) : Constant.CHAT_NO_USER_IMAGE);
//                        //
//                        if (!ToolBox_Inf.verifyDownloadFileInf(image_name + ".jpg", Constant.CACHE_CHAT_PATH)) {
//
//                            ToolBox_Inf.deleteDownloadFileInf(image_name + ".tmp", Constant.CACHE_CHAT_PATH);
//                            //
//                            ToolBox_Inf.downloadImagePDF(
//                                    url,
//                                    Constant.CACHE_CHAT_PATH + "/" + image_name + ".tmp"
//                            );
//                            //
//                            ToolBox_Inf.renameDownloadFileInf(image_name, ".jpg", Constant.CACHE_CHAT_PATH);
//                        }
//                        publishProgress(user_code, image_name + ".jpg");
//
//                    } catch (Exception e) {
//                        ToolBox_Inf.registerException(getClass().getName(), e);
//                        this.cancel(true);
//                    }
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//            //
//            act034_room.updateMemberImage(values[0], values[1]);
//        }
//
//        @Override
//        protected void onCancelled(Void aVoid) {
//            super.onCancelled(aVoid);
//            Log.d("ChatEvent", "DownloadAsyncTask Cancelada");
//        }
//    }

    private class UserListInfoTask extends AsyncTask<String, Integer, String> {
        private String room_type = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ChatEvent", "UserListInfoTask PreExecute");
            //
            showPD(
                    hmAux_Trans.get("progress_user_list_ttl"),
                    hmAux_Trans.get("progress_user_list_msg"),

                    true);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ChatEvent", "UserListInfoTask DoInBackground");
            String resultado = "";
            try {
                String socket_id = params[0];
                String customer_code = params[1];
                room_type = params[2];
                String room_code = params[3];
                //
                Gson gson = new GsonBuilder().serializeNulls().create();
                //
                Chat_UserList_Info_Env env = new Chat_UserList_Info_Env();
                //
                env.setSession_app(ToolBox_Con.getPreference_Session_App(context));
                env.setCustomer_code(customer_code);
                if(room_type.equals(Constant.CHAT_ROOM_TYPE_AP)){
                    env.setProfile("AP");
                    env.setRoom_code(room_code);
                }
                //
                resultado = ToolBox_Con.connWebService(
                        Constant.WS_CHAT_ROOM_USER_LIST,
                        gson.toJson(env)
                );

            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
                this.cancel(true);
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            Log.d("ChatEvent", "UserListInfoTask OnPost");
            super.onPostExecute(resultado);
            Gson gson = new GsonBuilder().serializeNulls().create();
            //Se não retornou nada, finaliza execução.
            if (resultado.equals("")) {
                this.cancel(true);
                return;
            }
            try {
                //
                if (resultado.contains("error_msg")) {
                    //
                    Chat_C_Error cError =
                            gson.fromJson(
                                    ToolBox_Inf.getWebSocketJsonParam(resultado),
                                    Chat_C_Error.class
                            );
                    //
                    ToolBox.sendBCStatus(
                            context,
                            "ERROR_1",
                            cError != null ? cError.getError_msg() : "Error",
                            "",
                            "0"
                    );

                } else {
                    //
                    String tt = ToolBox_Inf.getWebSocketJsonParam(resultado);
                    //
                    disablePD();
                    ArrayList<Chat_UserList_Info_Rec> userListInfoList = gson
                            .fromJson(
                                    ToolBox_Inf.getWebSocketJsonParam(resultado),
                                    new TypeToken<ArrayList<Chat_UserList_Info_Rec>>() {
                                    }.getType()
                            );

                    switch (room_type) {
                        case Constant.CHAT_ROOM_TYPE_PRIVATE_CUSTOMER:
                            act034_room.showUserListInfoDialog(userListInfoList);
                            break;
                        case Constant.CHAT_ROOM_TYPE_AP:
                            act034_room.showMultiUserListDialog(userListInfoList);
                            break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
                this.cancel(true);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ChatEvent", "UserListInfoTask Cancelada");
            disablePD();
        }
    }

    //endregion


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (bTT) {
            bTT = false;
            finish();
        } else {
            mPresenter.onBackPressedClicked();
        }

    }

    @Override
    protected void onDestroy() {
        startReceivers(false);
        if (roomInfoTask != null) {
            roomInfoTask.cancel(true);
        }
//        if (downloadMemberImgTask != null) {
//            downloadMemberImgTask.cancel(true);
//        }
        //
        if (userListInfoTask != null) {
            userListInfoTask.cancel(true);
        }

        //
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));
        menu.add(0, 2, Menu.NONE + 1, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        if (ToolBox_Inf.isUsrAdmin(context)) {
            menu.getItem(1).setIcon(R.drawable.ic_swap_vertical_circle_green_24dp);
            menu.getItem(1).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            menu.getItem(1).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == 2) {
            if (ToolBox_Inf.isUsrAdmin(context)) {
                ToolBox_Inf.showChatAdminInfo(context, hmAux_Trans);
            }
        }

        return true;
    }

    @Override
    public void changeRoom_Private_Code(String room_private) {
        room_code_private = room_private;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        super.processNotification_close(mValue, mActivity);
    }
}
