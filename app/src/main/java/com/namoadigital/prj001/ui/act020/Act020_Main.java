package com.namoadigital.prj001.ui.act020;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act020_Prod_Serial_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.TProduct_Serial;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 17/05/2017.
 */

public class Act020_Main extends Base_Activity_NFC_Geral implements Act020_Main_View {

    public static final String PROGRESS_WS = "progress_ws";
    public static final String PROGRESS_NFC = "progress_nfc";
    private static final int PROGRESS_TIME_OUT = 10 * 1000;

    private Context context;
    private Act020_Main_Presenter mPresenter;
    private DrawerLayout mDrawerLayout;
    private FragmentManager fm;
    private Act020_Frag_Filter fragFilters;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView tv_records;
    private LinearLayout ll_records;
    private TextView tv_records_limit;
    private TextView tv_records_count;
    private ListView lv_prod_serial_list;
    private TextView tv_no_result;
    private Act020_Prod_Serial_Adapter mAdapter;

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act020_main);

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
        context = Act020_Main.this;

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT020
        );
        //
        fm = getSupportFragmentManager();
        //
        loadTranslation();

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("search_prod_hint");
        transList.add("search_serial_hint");
        transList.add("drawer_product_lbl");
        transList.add("drawer_product_id_lbl");
        transList.add("drawer_serial_lbl");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("alert_no_search_parameter_ttl");
        transList.add("alert_no_search_parameter_msg");
        transList.add("progress_nfc_ttl");
        transList.add("progress_nfc_msg");
        transList.add("showing_lbl");
        transList.add("records_lbl");
        transList.add("no_record_found_lbl");
        transList.add("alert_nfc_return");
        transList.add("alert_qty_records_exceeded_ttl");
        transList.add("alert_qty_records_exceeded_msg");
        transList.add("alert_qty_records_founded");
        transList.add("msg_start_search");
        transList.add("alert_nfc_timeout");
        transList.add("no_search_realized");
        transList.add("records_display_limit_lbl");
        transList.add("records_found_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    private void initVars() {

        mPresenter = new Act020_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
        //
        tv_records = (TextView) findViewById(R.id.act020_tv_record_info);
        //
        ll_records = (LinearLayout) findViewById(R.id.act020_ll_limit_exceeded);
        //
        tv_records_limit = (TextView) findViewById(R.id.act020_tv_record_limit);
        //
        tv_records_count = (TextView) findViewById(R.id.act020_tv_record_count);
        //
        lv_prod_serial_list = (ListView) findViewById(R.id.act020_lv_prod_serial);
        //
        tv_no_result = (TextView) findViewById(R.id.act020_tv_no_result);
        tv_no_result.setText(hmAux_Trans.get("no_search_realized"));
        //
        handler =  new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                ToolBox_Inf.sendBCStatus(context, "ERROR_1", hmAux_Trans.get("alert_nfc_timeout"), "", "0");
                setbNFCStatus(false);
                changeNFCDrawable(fragFilters.getDrawableNFC());
            }
        };

        //
        /*
        * Drawer setup
        */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.act020_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act020_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_opened
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
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //
        mDrawerToggle.syncState();
        //
        fragFilters = (Act020_Frag_Filter) fm.findFragmentById(R.id.act020_frag_filter);
        //
        fragFilters.setHmAux_Trans(hmAux_Trans);
        //
        controls_sta.addAll(fragFilters.getControlsSta());
        //
        fragFilters.setOnDrawerClick(new Act020_Frag_Filter.IAct020_Filter() {
            @Override
            public void onIvSearchClick(String product, String product_id, String serial) {
                //
                if(product.trim().length() > 0
                    || serial.trim().length() > 0 ){
                    mPresenter.executeSerialSearch(product, product_id, serial,serial);
                }else{
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_search_parameter_ttl"),
                            hmAux_Trans.get("alert_no_search_parameter_msg"),
                            null,
                            0
                    );
                }

            }

            @Override
            public void onNFCClick(int id) {
                //
                fragFilters.setNFCText("Ativar NFC");
                //Habilita leitura do NFC
                setbNFCStatus(true);
                //Chama metodo que muda o icone NFC
                changeNFCDrawable(fragFilters.getDrawableNFC());
                //Configura busca do NFC
                setNFCSetUp(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        true,
                        true,
                        id
                );
                //Mostra progress
                showPD(PROGRESS_NFC);

            }
        });
       /*
        * Drawer setup end
        */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT020;
        mAct_Title = Constant.ACT020 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value = hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl = hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value = hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé - fim

    }

    private void initActions() {
        //Abre drawer ao carregar a tela
        mDrawerLayout.openDrawer(GravityCompat.START);
        //Sincroniza icone do hambuguer
        mDrawerToggle.syncState();
        //
    }

    @Override
    public void showPD(String progress_type) {
        String title = "";
        String msg = "";

        switch (progress_type){

            case PROGRESS_WS:
                title = hmAux_Trans.get("progress_serial_search_ttl");
                msg = hmAux_Trans.get("progress_serial_search_msg");
                break;

            case PROGRESS_NFC:
                title = hmAux_Trans.get("progress_nfc_ttl");
                msg = hmAux_Trans.get("progress_nfc_msg");
                handler.postDelayed(runnable, PROGRESS_TIME_OUT);
                break;

        }

        if (progressDialog == null || !progressDialog.isShowing()){

            enableProgressDialog(
                    title,
                    msg,
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
        }if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
        }

        if(progress_type.equals(PROGRESS_NFC)){
            progressDialog.setButton(
                    DialogInterface.BUTTON_NEGATIVE,
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.dismiss();
                        }
                    }
            );
        }
    }

    @Override
    public void setRecordInfo(long record_size, long record_page) {
        if(record_size > 0){
            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + record_size + "  " + hmAux_Trans.get("records_lbl"));
        }else{
            tv_records.setText(hmAux_Trans.get("no_record_found_lbl"));

        }
    }

    @Override
    public void loadProductSerialList(ArrayList<TProduct_Serial> prod_serial_list) {
        //Esconde tv com msg de nenhum busca feita
        //e ll com informações de limite de excedido.
        tv_no_result.setVisibility(View.GONE);
        ll_records.setVisibility(View.GONE);
        //
        mAdapter =  new Act020_Prod_Serial_Adapter(
                context,
                R.layout.act020_cell,
                prod_serial_list
        );
        //
        lv_prod_serial_list.setAdapter(mAdapter);
    }

    @Override
    public void showQtyExceededMsg(long record_page, long record_count) {

        ll_records.setVisibility(View.VISIBLE);

        tv_records_limit.setText(
                hmAux_Trans.get("records_display_limit_lbl") + " " + record_page
        );

        tv_records_count.setText(
                hmAux_Trans.get("records_found_lbl") + " " + record_count
        );

        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_qty_records_exceeded_ttl"),
                hmAux_Trans.get("alert_qty_records_exceeded_msg") +"\n" + record_count + " " + hmAux_Trans.get("alert_qty_records_founded"),
                null,
                0);

    }

    private void changeNFCDrawable(Drawable drawableNFC) {

        if(isbNFCStatus()){
            drawableNFC.setColorFilter(getResources().getColor(R.color.emoticons_green), PorterDuff.Mode.SRC_ATOP);
        }else{
            drawableNFC.setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
        }

        fragFilters.setDrawableNFC(drawableNFC);
    }

    @Override
    public void callAct006(Context context) {
        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void nfcData(boolean status, int id, String... value) {
        super.nfcData(status, id, value);
        //Metodo que modifica cor do icone nfc
        changeNFCDrawable(fragFilters.getDrawableNFC());
        //Cancela timer
        handler.removeCallbacks(runnable);
        //
        progressDialog.dismiss();
        //
        if(!status){
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_nfc_return"),
                    value[0],
                    null,
                    0
            );

        }else{

            switch (value[0]){
                case PRODUCT:
                    fragFilters.setNFCText(hmAux_Trans.get("drawer_product_lbl"));
                    fragFilters.setProductCodeText(value[1]);
                    mPresenter.executeSerialSearch(value[1],"","","");
                    break;
                case SERIAL:
                    fragFilters.setNFCText(hmAux_Trans.get("drawer_serial_lbl"));
                    fragFilters.setProductCodeText(value[1]);
                    fragFilters.setSerialIdText(value[2]);
                    mPresenter.executeSerialSearch(value[1],"","",value[2]);
                    break;

                default:
                    break;
            }


        }

    }

    @Override
    protected void processCloseACT(String ws_retorno, String mRequired) {
        super.processCloseACT(ws_retorno, mRequired);

        mPresenter.getProductSerialList(ws_retorno);
        //
        progressDialog.dismiss();
        //
        mDrawerLayout.closeDrawer(GravityCompat.START);
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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return true;
    }
}
