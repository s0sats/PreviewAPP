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
import com.namoadigital.prj001.ui.act001.Act001_Main;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.ui.act004.Act004_Main;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act005_Main extends Base_Activity implements Act005_Main_View{

    public static final String MENU_ID = "menu_id";
    public static final String MENU_ICON = "menu_icon";
    public static final String MENU_DESC = "menu_desc";
    public static final String MENU_ID_CHECKLIST = "menu_checklist";
    public static final String MENU_ID_PENDING_DATA = "menu_pending_data";
    public static final String MENU_ID_SEND_DATA = "menu_send_data";
    public static final String MENU_ID_SYNC_DATA = "menu_sync_data";
    public static final String MENU_ID_CLOSE = "menu_close_app";

    private Context context;
    private List<HMAux> menu_list;
    private GridView gv_menu;
    private Act005_Main_Presenter mPresenter;
    private Act005_Adapter mAdapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager fm;
    private Act005_Opc fragOpc;

    private String alertTitle = "";
    private String alertMsg = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act005_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        initVars();
        iniUIFooter();
        initActions();

    }

    private void iniSetup() {
        context = getBaseContext();
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
        //
    }

    private void initVars() {
        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.act005_drawer);

        mPresenter = new Act005_Main_Presenter_Impl(context, this);
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
        fragOpc.setHmAux_Trans(hmAux_Trans,mModule_Code,mResource_Code);
        fragOpc.setOnOpcItemClicked(new Act005_Opc.IAct005_Opc() {
            @Override
            public void itemClicked(String index) {
                DialogInterface.OnClickListener listener = null;

                switch (index){
                    case Act005_Opc.DRAWER_OPC_CUSTOMER:
                        //
                        setDrawerAlertTranslation("drawer_change_customer_alert_ttl", "drawer_change_customer_alert_msg");
                        //
                        listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Reseta preferencias do Customer e volta para
                                //Act002 - lista de customer
                                ToolBox_Con.setPreference_Customer_Code(context,-1);
                                ToolBox_Con.setPreference_Translate_Code(context,"");
                                ToolBox_Con.setPreference_Site_Code(context,"-1");
                                ToolBox_Con.setPreference_Operation_Code(context,-1);

                                callAct002(context);
                            }
                        };

                        break;
                    case Act005_Opc.DRAWER_OPC_SITE:
                        //
                        setDrawerAlertTranslation("drawer_change_site_alert_ttl", "drawer_change_site_alert_msg");
                        //Apaga preferencia de Site, Operatione volta a lista de site
                        listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Reseta preferencias do Customer e volta para
                                ToolBox_Con.setPreference_Site_Code(context,"-1");
                                ToolBox_Con.setPreference_Operation_Code(context,-1);
                                //
                                callAct003(context);
                            }
                        };
                        break;
                    case Act005_Opc.DRAWER_OPC_OPERATION:
                        //
                        setDrawerAlertTranslation("drawer_change_operation_alert_ttl", "drawer_change_operation_alert_msg");
                        //
                        listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Apaga preferencia de Operatione volta a ista de operation
                                ToolBox_Con.setPreference_Operation_Code(context,-1);
                                //
                                callAct004(context);
                            }
                        };
                        break;
                    case Act005_Opc.DRAWER_OPC_LOGOUT:
                        //
                        setDrawerAlertTranslation("drawer_logout_alert_ttl", "drawer_logout_alert_msg");
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
                if (listener != null){
                    ToolBox.alertMSG(
                            Act005_Main.this,
                            alertTitle,
                            alertMsg,
                            listener
                    );

                }
                //Fecha Drawer
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });


    }

    private void setDrawerAlertTranslation(String title_txt_code,String msg_txt_code) {
        if (hmAux_Trans.get(title_txt_code) != null) {
            alertTitle = hmAux_Trans.get(title_txt_code);
        } else {
            alertTitle = ToolBox.setNoTrans(mModule_Code, mResource_Code, title_txt_code);
        }

        if (hmAux_Trans.get(msg_txt_code) != null) {
            alertMsg = hmAux_Trans.get(msg_txt_code);
        } else {
            alertMsg = ToolBox.setNoTrans(mModule_Code, mResource_Code, msg_txt_code);
        }
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

        mAdapter = new Act005_Adapter(context, R.layout.act005_item_menu, menus);
        gv_menu.setAdapter(mAdapter);
    }

    private void loadTranslation() {
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context)
        );

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
    public void showPD() {
        enableProgressDialog(
                "Get Sync",
                "Start Processing...",
                "Cancel",
                "Ok"
        );
    }

    @Override
    public void callAct006(Context context) {
        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    public void callAct002(Context context) {
        Intent mIntent = new Intent(context, Act002_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    public void callAct003(Context context) {
        Intent mIntent = new Intent(context, Act003_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    public void callAct004(Context context) {
        Intent mIntent = new Intent(context, Act004_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void closeApp() {
        finish();
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
        if (id == R.id.act05_action_settings) {

            ToolBox_Con.cleanPreferences(context);

            Intent mIntent = new Intent(context, Act001_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //mDrawerLayout.closeDrawer(GravityCompat.START);
}
