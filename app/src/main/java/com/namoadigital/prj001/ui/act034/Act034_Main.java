package com.namoadigital.prj001.ui.act034;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act034_Msg_Adapter;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Main extends Base_Activity implements Act034_Main_View {


    private Act034_Main_Presenter mPresenter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fm;
    private Act034_Opc act034_opc;
    private TextView tv_customer_msg_lbl;
    private TextView tv_customer_msg_qty;
    private ListView lv_msg;
    private Act034_Msg_Adapter mAdapter;

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
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //
                act034_opc.loadDataToScreen();
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
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //
        mDrawerToggle.syncState();
        //
        act034_opc = (Act034_Opc) fm.findFragmentById(R.id.act034_opc);
        //
        act034_opc.setHmAux_Trans(hmAux_Trans);
        //
        lv_msg = (ListView) findViewById(R.id.act034_lv_msg);

        loadMsgList();


    }

    private void loadMsgList() {
        ArrayList<HMAux> auxList = new ArrayList<>();

        //
        HMAux aux5 = new HMAux();
        aux5.put("type","alert");
        aux5.put("user","");
        aux5.put("date","");
        aux5.put("status","");
        aux5.put("msg","");
        aux5.put("alert","User Add: ");
        aux5.put("alert_msg","Luche entrou");
        auxList.add(aux5);

        HMAux aux1 = new HMAux();
        aux1.put("type","others");
        aux1.put("user","Cesar(3)");
        aux1.put("date","22/01/1988 16:00:32");
        aux1.put("status","0");
        aux1.put("msg","Olá batata");
        aux1.put("alert","");
        aux1.put("alert_msg","");
        auxList.add(aux1);
        //
        HMAux aux2 = new HMAux();
        aux2.put("type","mine");
        aux2.put("user","Luche(52)");
        aux2.put("date","28/11/2017 11:00:32");
        aux2.put("status","0");
        aux2.put("msg","Fala viado");
        aux2.put("alert","");
        aux2.put("alert_msg","");
        auxList.add(aux2);
        //
        HMAux aux3 = new HMAux();
        aux3.put("type","mine");
        aux3.put("user","Luche(52)");
        aux3.put("date","28/11/2017 11:01:32");
        aux3.put("status","1");
        aux3.put("msg","ta ouvindo viado?!");
        aux3.put("alert","");
        aux3.put("alert_msg","");
        auxList.add(aux3);
        //
        HMAux aux4 = new HMAux();
        aux4.put("type","mine");
        aux4.put("user","Luche(52)");
        aux4.put("date","28/11/2017 11:10:32");
        aux4.put("status","2");
        aux4.put("msg","ta ouvindo peste ???");
        aux4.put("alert","");
        aux4.put("alert_msg","");
        auxList.add(aux4);
        //
        //
        HMAux aux6 = new HMAux();
        aux6.put("type","alert");
        aux6.put("user","");
        aux6.put("date","");
        aux6.put("status","");
        aux6.put("msg","");
        aux6.put("alert","User Add: ");
        aux6.put("alert_msg","Luche entrou");
        auxList.add(aux6);

        HMAux aux7 = new HMAux();
        aux7.put("type","others");
        aux7.put("user","Cesar(3)");
        aux7.put("date","22/01/1988 16:00:32");
        aux7.put("status","0");
        aux7.put("msg","Olá batata");
        aux7.put("alert","");
        aux7.put("alert_msg","");
        auxList.add(aux7);
        //
        HMAux aux8 = new HMAux();
        aux8.put("type","mine");
        aux8.put("user","Luche(52)");
        aux8.put("date","28/11/2017 11:00:32");
        aux8.put("status","0");
        aux8.put("msg","Fala viado");
        aux8.put("alert","");
        aux8.put("alert_msg","");
        auxList.add(aux8);
        //
        HMAux aux9 = new HMAux();
        aux9.put("type","mine");
        aux9.put("user","Luche(52)");
        aux9.put("date","28/11/2017 11:01:32");
        aux9.put("status","1");
        aux9.put("msg","ta ouvindo viado?!");
        aux9.put("alert","");
        aux9.put("alert_msg","");
        auxList.add(aux9);
        //
        HMAux aux10 = new HMAux();
        aux10.put("type","mine");
        aux10.put("user","Luche(52)");
        aux10.put("date","28/11/2017 11:10:32");
        aux10.put("status","2");
        aux10.put("msg","ta ouvindo peste ???");
        aux10.put("alert","");
        aux10.put("alert_msg","");
        auxList.add(aux10);
        mAdapter = new Act034_Msg_Adapter(
                context,
                auxList
        );
        //
        lv_msg.setAdapter(mAdapter);
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
    public void onBackPressed() {
        //super.onBackPressed();

        mPresenter.onBackPressedClicked();
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
