package com.namoadigital.prj001.ui.act034;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_RoomDao;
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
        transList.add("customer_ttl");
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
                hmAux_Trans
        );
        //
        initFragments();
        //
        setFrag(act034_room,FRAG_TAG_ROOM);
    }

    @Override
    public void startReceivers(boolean start_stop) {
        brRoomReceiver = new BR_Room();
        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);
        //
        if(start_stop){
            LocalBroadcastManager.getInstance(Act034_Main.this).registerReceiver(brRoomReceiver,brRoomFilter);
        }else{
            LocalBroadcastManager.getInstance(Act034_Main.this).unregisterReceiver(brRoomReceiver);
        }

    }

    private void initFragments() {
        //Drawer
        act034_opc = (Act034_Opc) fm.findFragmentById(R.id.act034_opc);
        //
        act034_opc.setHmAux_Trans(hmAux_Trans);

        /*
        * Room Fragment
        */
        act034_room = new Act034_Room();
        //
        act034_room.setBaInfra(this);
        act034_room.setHmAux_Trans(hmAux_Trans);
        act034_room.loadDataToScreen();

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
        bundle.putString(CH_RoomDao.ROOM_CODE,item.get(CH_RoomDao.ROOM_CODE));
        bundle.putString("position",item.get("position"));
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    /*
    * Receivers
    */

    private class BR_Room extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.CHAT_BR_TYPE);

            type += "";

            switch (type){
                case Constant.CHAT_BR_TYPE_ROOM:
                case Constant.CHAT_BR_TYPE_MSG:
                    if(currentFrag.equalsIgnoreCase(FRAG_TAG_ROOM)){
                        act034_room.loadRoomList();
                    }
                    break;
                default:
                    break;
            }
        }
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();

    }

    @Override
    protected void onDestroy() {
        startReceivers(false);
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

        /*if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        return true;
    }

}
