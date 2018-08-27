package com.namoadigital.prj001.ui.act047;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act047_SO_Next_Orders_Adapter;
import com.namoadigital.prj001.model.SO_Next_Orders_Obj;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_SO_Next_Orders;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act047_Main extends Base_Activity implements Act047_Main_Contract.I_View {

    private TextView tv_site;
    private TextView tv_zone;
    private ListView lv_services;
    private Act047_SO_Next_Orders_Adapter mAdapter;
    private String requestingAct = "";
    private Act047_Main_Contract.I_Presenter mPresenter;
    private String wsProcess ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.act047_main);

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
                Constant.ACT047
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("alert_empty_order_list_ttl");
        transList.add("alert_empty_order_list_msg");
        transList.add("alert_no_orders_found_ttl");
        transList.add("alert_no_orders_found_msg");
        transList.add("dialog_next_orders_search_ttl");
        transList.add("dialog_next_orders_search_msg");
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
        //
        recoverIntentsInfo();
        //
        tv_site = (TextView) findViewById(R.id.act047_tv_site_val);
        //
        tv_zone = (TextView) findViewById(R.id.act047_tv_zone_val);
        //
        lv_services = (ListView) findViewById(R.id.act047_lv_services);
        //
        mPresenter = new Act047_Main_Presenter(
                context,
                this,
                requestingAct,
                hmAux_Trans
        );
        setLocationInfo();
        //
        mPresenter.executeNextOrdersSearch();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            requestingAct = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
        } else {
            requestingAct = Constant.ACT005;
        }
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    private void setLocationInfo() {
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        //
        tv_site.setText(mFooter.get(Constant.FOOTER_SITE));
        //
        tv_zone.setVisibility(View.GONE);
        if( ToolBox_Inf.profileExists(context,Constant.PROFILE_PRJ001_SO,null)
            && mFooter.containsKey(Constant.FOOTER_ZONE)
            && !mFooter.get(Constant.FOOTER_ZONE).isEmpty()
        ) {
            tv_zone.setVisibility(View.VISIBLE);
            tv_zone.setText(mFooter.get(Constant.FOOTER_ZONE));
        }
    }

    @Override
    public void loadNextOrders(ArrayList<SO_Next_Orders_Obj> nextOrdersObjs) {
        mAdapter = new Act047_SO_Next_Orders_Adapter(
                context,
                nextOrdersObjs,
                R.layout.act047_cell
        );
        //
        lv_services.setAdapter(mAdapter);
    }

    @Override
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void showNoConnecionMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_no_conection_ttl"),
                hmAux_Trans.get("alert_no_conection_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                },
                0
        );
    }

    @Override
    public void showEmptyLogMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_empty_order_list_ttl"),
                hmAux_Trans.get("alert_empty_order_list_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                },
                0
        );
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT047;
        mAct_Title = Constant.ACT047 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
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
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (wsProcess.equals(WS_SO_Next_Orders.class.getName())) {
            mPresenter.processNextOrderList(hmAux.get(WS_SO_Next_Orders.SO_NEXT_SERVICES));
            disableProgressDialog();
        }

    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        disableProgressDialog();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        disableProgressDialog();
    }


    //TRATA MSG SESSION NOT FOUND
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

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }

    //Metodo chamado ao finalizar o download da atualização.
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
