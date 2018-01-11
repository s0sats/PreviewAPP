package com.namoadigital.prj001.ui.act034;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
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
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.model.Chat_Room_Info_Env;
import com.namoadigital.prj001.model.Chat_Room_Info_Rec;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Main extends Base_Activity_Frag implements Act034_Main_View {

    public static final String FRAG_TAG_ROOM = "ROOM";

    private Act034_Main_Presenter mPresenter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fm;
    private Act034_Opc act034_opc;
    private Act034_Room act034_room;
    private String currentFrag = "";
    private BR_Room brRoomReceiver;
    private AlertDialog infoDialog = null;
    //
    private LinearLayout ll_info;
    private ImageView iv_info_icon;
    private TextView tv_info_msg_1;
    private TextView tv_info_msg_2;
    private ArrayList<HMAux> customer_list = new ArrayList<>();
    private RoomInfoTask roomInfoTask;
    private DownloadMemberImgTask downloadMemberImgTask;

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
        transList.add("room_dialog_filter_ttl");
        //
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
        initFragments();
        //
        setFrag(act034_room, FRAG_TAG_ROOM);
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void startReceivers(boolean start_stop) {
        if (brRoomReceiver == null) {
            brRoomReceiver = new BR_Room();
        }
        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);
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
        toogleDrawerVisibility();
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
        act034_room.setSelected_customer(customer_list.size() > 0 ? Long.parseLong(customer_list.get(0).get(EV_User_CustomerDao.CUSTOMER_CODE)) : 0);
        act034_room.loadDataToScreen();

    }

    private void toogleDrawerVisibility() {
        customer_list = mPresenter.getCustomerMessageList(
                mPresenter.getCustomerNameList()
        );
        //
        act034_opc.setCustomerList(customer_list);
        //
        setDrawerState(customer_list.size() > 1);

        //
        /*if(customer_list.size() > 1){
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else{
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }*/
    }

    public void setSelectedCustomer(long customer_code) {
        act034_room.setSelected_customer(customer_code);
        act034_room.loadDataToScreen();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.openDrawer(GravityCompat.START);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            //mDrawerStatus = true;

            mDrawerToggle.syncState();

        } else {

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerLayout.closeDrawer(GravityCompat.START);

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);

            //mDrawerStatus = false;

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
    public void callAct035(Context context, HMAux item) {
        Intent mIntent = new Intent(context, Act035_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(CH_RoomDao.ROOM_CODE, item.get(CH_RoomDao.ROOM_CODE));
        bundle.putString("position", item.get("position"));
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
                    if (currentFrag.equalsIgnoreCase(FRAG_TAG_ROOM)) {
                        //act034_room.loadRoomList();
                        act034_room.loadDataToScreen();
                    }
                    //Atualiza drawer
                    toogleDrawerVisibility();
                    break;
                case Constant.CHAT_BR_TYPE_RECONNECTED:
                    toogleInfoMsg(false, null);
                    //hideShowReconnectingDialog(0,0);
                    break;
                case Constant.CHAT_BR_TYPE_RECONNECTING:
                    String qtd = String.valueOf(auxParam == null ? 0 : auxParam.get(Constant.CHAT_BR_PARAM_RECONNECTING_QTD));
                    toogleInfoMsg(true, qtd);
                    //hideShowReconnectingDialog(1, qtd);
                    break;
                case Constant.CHAT_BR_TYPE_ROOM_INFO:
                    if (currentFrag.equalsIgnoreCase(FRAG_TAG_ROOM)) {
                        //showRoomInfoDialog(auxParam);
                        //act034_room.showRoomInfoDialog(auxParam);
                    }

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void showPD(String ttl, String msg) {
        //
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
        //
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(roomInfoTask != null){
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
                infoDialog = showReconnectingDialog(context, "Try connecting", qtd != 0 ? String.valueOf(qtd) : null);
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
        roomInfoTask.execute(socket_id,room_code);
    }
    //

    @Override
    public void startDownloadMemberImgTask(String[] imgUrlList) {
        downloadMemberImgTask = new DownloadMemberImgTask();
        downloadMemberImgTask.execute(imgUrlList);
    }


    //region AsyncTask

    private class RoomInfoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ChatEvent","RoomAsyncTask PreExecute");
            //
            showPD(
                    /*hmAux_Trans.get("ws_room_info_ttl"),
                    hmAux_Trans.get("ws_room_info_msg")*/
                    "Informações da Sala - Trad",
                    "Buscando informações da sala - Trad"
            );
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ChatEvent","RoomAsyncTask DoInBackground");
            String resultado = "";
            try {
                String socket_id = params[0];
                String room_code = params[1];
                //
                Gson gson = new GsonBuilder().serializeNulls().create();
                //
                Chat_Room_Info_Env env = new Chat_Room_Info_Env();
                env.setSocket_id(socket_id);
                env.setRoom_code(room_code);
                env.setActive(1);
                //
                resultado = ToolBox_Con.connWebService(
                        Constant.WS_CHAT_ROOM_INFO,
                        gson.toJson(env)
                );

            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            Log.d("ChatEvent","RoomAsyncTask OnPost");
            super.onPostExecute(resultado);
            Gson gson = new GsonBuilder().serializeNulls().create();
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
                for (Chat_Room_Info_Rec info_rec:roomInfoList) {
                    if(info_rec.getSys_user_image() != null) {
                        auxList.add(info_rec.getSys_user_image());
                    }
                }
                //
                act034_room.showRoomInfoDialog(roomInfoList);
                //
                String[] imgUrlList = new String[auxList.size()];
                startDownloadMemberImgTask(auxList.toArray(imgUrlList));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ChatEvent","RoomAsyncTask Cancelada");
            disablePD();
        }
    }

    private class DownloadMemberImgTask extends AsyncTask<String, Integer, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            for (int i = 0; i < strings.length ; i++) {
                String oi = strings[i];
            }

            return null;
        }
    }

    //endregion


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();

    }

    @Override
    protected void onDestroy() {
        startReceivers(false);
        if(roomInfoTask != null) {
            roomInfoTask.cancel(true);
        }
        if(downloadMemberImgTask != null){
            downloadMemberImgTask.cancel(true);

        }
        //
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        return true;
    }

}
