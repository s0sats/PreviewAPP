package com.namoadigital.prj001.ui.act025;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act020_Prod_Serial_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act023.Act023_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act025_Main extends Base_Activity_Frag_NFC_Geral implements Act025_Main_View {

    public static final String PROGRESS_WS_SERIAL_SEARCH = "progress_ws_serial_search";
    //public static final String PROGRESS_WS_SYNC = "progress_ws_sync";
    //public static final String PROGRESS_NFC = "progress_nfc";

    private Act025_Main_Presenter mPresenter;
    //private DrawerLayout mDrawerLayout;
    //private FragmentManager fm;
    //private Act025_Frag_Filter fragFilters;
    //private ActionBarDrawerToggle mDrawerToggle;
    private TextView tv_records;
    private LinearLayout ll_records;
    private TextView tv_records_limit;
    private TextView tv_records_count;
    private ListView lv_prod_serial_list;
    private TextView tv_no_result;
    private Act020_Prod_Serial_Adapter mAdapter;
    private String ws_process;
    private ArrayList<MD_Product_Serial> serial_list = new ArrayList<>();
    private String tracking_searched = "";

    private MD_Product md_product;
    private boolean mJump;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act025_main);

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
        //context = Act025_Main.this;
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT025
        );
        //
        //fm = getSupportFragmentManager();
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
        transList.add("drawer_tracking_lbl");
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
        transList.add("progress_sync_title");
        transList.add("progress_sync_msg");
        transList.add("alert_no_form_for_operation_ttl");
        transList.add("alert_no_form_for_operation_msg");
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");

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
        mPresenter = new Act025_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
        //
        tv_records = (TextView) findViewById(R.id.act025_tv_record_info);
        //
        ll_records = (LinearLayout) findViewById(R.id.act025_ll_limit_exceeded);
        //
        tv_records_limit = (TextView) findViewById(R.id.act025_tv_record_limit);
        //
        tv_records_count = (TextView) findViewById(R.id.act025_tv_record_count);
        //
        lv_prod_serial_list = (ListView) findViewById(R.id.act025_lv_prod_serial);
        //
        tv_no_result = (TextView) findViewById(R.id.act025_tv_no_result);
        tv_no_result.setText(hmAux_Trans.get("no_search_realized"));
        //
        /*
         * Drawer setup
         */
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.act025_drawer);

//        mDrawerToggle = new ActionBarDrawerToggle(
//                Act025_Main.this,
//                mDrawerLayout,
//                R.string.act005_drawer_opened,
//                R.string.act005_drawer_opened
//        ) {
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                //
//                invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                //
//                invalidateOptionsMenu();
//            }
//
//        };
        //
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        //
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        //
//        mDrawerToggle.syncState();
//        //
//        fragFilters = (Act025_Frag_Filter) fm.findFragmentById(R.id.act025_frag_filter);
//        //
//        fragFilters.setHmAux_Trans(hmAux_Trans);
//        //
//        fragFilters.setSupportNFC(supportNFC);
//        //
//        controls_sta.addAll(fragFilters.getControlsSta());
//        //
//        fragFilters.setClickListener(actionBTN);
//        //
//        fragFilters.setOnDrawerClick(new Act025_Frag_Filter.IAct025_Filter() {
//            @Override
//            public void onIvSearchClick(String product_id, String serial, String tracking) {
//                ToolBox_Inf.hideSoftKeyboard(Act025_Main.this);
//                if (product_id.trim().length() > 0
//                        || serial.trim().length() > 0
//                        || tracking.trim().length() > 0
//                        ) {
//                    if (product_id.trim().length() > 0 && mPresenter.searchProductInfo("", product_id.trim()).equals("")) {
//                        ToolBox.alertMSG(
//                                context,
//                                hmAux_Trans.get("alert_local_product_not_found_ttl"),
//                                hmAux_Trans.get("alert_local_product_not_found_msg"),
//                                null,
//                                0
//                        );
//                    } else {
//                        mPresenter.executeSerialSearch(product_id, serial, tracking);
//                    }
//
//                } else {
//                    ToolBox.alertMSG(
//                            context,
//                            hmAux_Trans.get("alert_no_search_parameter_ttl"),
//                            hmAux_Trans.get("alert_no_search_parameter_msg"),
//                            null,
//                            0
//                    );
//                }
//            }
//
//        });
//       /*
//        * Drawer setup end
//        */
        hideSoftKeyboard();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {

                mJump = bundle.getBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP);
                serial_list = (ArrayList<MD_Product_Serial>) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);

                MD_ProductDao mdProductDao = new MD_ProductDao(context);

                String product_id = bundle.getString(MD_ProductDao.PRODUCT_ID);

                md_product = mdProductDao.getByString(
                        new MD_Product_Sql_003(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                "",
                                product_id
                        ).toSqlQuery()
                );
            }
//            if (bundle.containsKey(Constant.MAIN_SERIAL_TRACKING)) {
//                tracking_searched = bundle.getString(Constant.MAIN_SERIAL_TRACKING, "");
//            }
        }
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT025;
        mAct_Title = Constant.ACT025 + "_" + "title";
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

        lv_prod_serial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ToolBox_Con.isOnline(context)) {
                    MD_Product_Serial productSerial = (MD_Product_Serial) parent.getItemAtPosition(position);
                    //
                    mPresenter.defineFlow(productSerial);
                } else {
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }
        });

        if (serial_list != null && serial_list.size() != 0) {
//            fragFilters.setSerialIdText(serial_list.get(0).getSerial_id());
//            //
//            if (!tracking_searched.equals("")) {
//                fragFilters.setTrackingText(tracking_searched);
//            }
            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + serial_list.size() + " " + hmAux_Trans.get("records_lbl"));
            loadProductSerialList(serial_list);

            if (serial_list.size() == 1 && mJump) {
                lv_prod_serial_list.performItemClick(
                        lv_prod_serial_list.getAdapter().getView(0, null, null),
                        0,
                        lv_prod_serial_list.getAdapter().getItemId(0)
                );
            } else {
            }


        } else {
//            //Se user tiver somente um produto, ja carrega os dados dele no drawer
//            mPresenter.checkSingleProduct();
//            //Abre drawer ao carregar a tela
//            mDrawerLayout.openDrawer(GravityCompat.START);
//            //Sincroniza icone do hambuguer
//            mDrawerToggle.syncState();
        }
    }

    @Override
    public void showPD() {
        String title = "";
        String msg = "";
        switch (ws_process) {

            case PROGRESS_WS_SERIAL_SEARCH:
                title = hmAux_Trans.get("progress_serial_search_ttl");
                msg = hmAux_Trans.get("progress_serial_search_msg");
                break;

//            case PROGRESS_WS_SYNC:
//                title = hmAux_Trans.get("progress_sync_title");
//                msg = hmAux_Trans.get("progress_sync_msg");
//                break;

//            case PROGRESS_NFC:
            default:
                title = hmAux_Trans.get("progress_nfc_ttl");
                msg = hmAux_Trans.get("progress_nfc_msg");
                break;

        }

        if (progressDialog == null || !progressDialog.isShowing()) {

            enableProgressDialog(
                    title,
                    msg,
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
        }
    }

    @Override
    public void setRecordInfo(long record_size, long record_page) {
        if (record_size > 0) {
            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + record_size + " " + hmAux_Trans.get("records_lbl"));
        } else {
            tv_records.setText(hmAux_Trans.get("no_record_found_lbl"));

        }
    }

    @Override
    public void loadProductSerialList(ArrayList<MD_Product_Serial> prod_serial_list) {
        //Esconde tv com msg de nenhum busca feita
        //e ll com informações de limite de excedido.
        tv_no_result.setVisibility(View.GONE);
        ll_records.setVisibility(View.GONE);
        //
        mAdapter = new Act020_Prod_Serial_Adapter(
                context,
                R.layout.act020_cell,
                prod_serial_list
        );
        //
        lv_prod_serial_list.setAdapter(mAdapter);
    }

    @Override
    public void showQtyExceededMsg(long record_page, long record_count) {
        //
        ll_records.setVisibility(View.VISIBLE);

        tv_records_limit.setText(
                hmAux_Trans.get("records_display_limit_lbl") + " " + record_page
        );

        tv_records_count.setText(
                hmAux_Trans.get("records_found_lbl") + " " + record_count
        );
        //
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_qty_records_exceeded_ttl"),
                hmAux_Trans.get("alert_qty_records_exceeded_msg") + "\n" + record_count + " " + hmAux_Trans.get("alert_qty_records_founded"),
                null,
                0);

    }

    @Override
    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }

    @Override
    public void setProductInfo(MD_Product product) {
//        fragFilters.setProductCodeText(String.valueOf(product.getProduct_code()));
//        //
//        fragFilters.setProductIdText(product.getProduct_id());
    }

    @Override
    protected void nfcData(boolean status, int id, String... value) {
        super.nfcData(status, id, value);

        if (!status) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_nfc_return"),
                    value[0],
                    null,
                    0
            );

        } else {
//            fragFilters.cleanFields();
            ToolBox_Inf.hideSoftKeyboard(Act025_Main.this);
            String product_id = "";
            //
            switch (value[0]) {
                case PRODUCT:
                    product_id = mPresenter.searchProductInfo(value[2], "");
                    //
                    if (!product_id.equals("")) {
//                        fragFilters.setNFCText(hmAux_Trans.get("drawer_product_lbl"));
//                        fragFilters.setProductCodeText(value[2]);
//                        fragFilters.setProductIdText(product_id);
                        mPresenter.executeSerialSearch(product_id, "", "");
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_local_product_not_found_ttl"),
                                hmAux_Trans.get("alert_local_product_not_found_msg"),
                                null,
                                0
                        );
                    }
                    break;
                case SERIAL:
                    product_id = mPresenter.searchProductInfo(value[2], "");
                    //
                    if (!product_id.equals("")) {
//                        fragFilters.setNFCText(hmAux_Trans.get("drawer_serial_lbl"));
//                        fragFilters.setProductCodeText(value[2]);
//                        fragFilters.setProductIdText(product_id);
//                        fragFilters.setSerialIdText(value[3]);
                        mPresenter.executeSerialSearch(product_id, value[3], "");
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_local_product_not_found_ttl"),
                                hmAux_Trans.get("alert_local_product_not_found_msg"),
                                null,
                                0
                        );
                    }
                    break;

                default:
                    break;
            }
        }

    }

    @Override
    protected void nfcDataError(boolean status, int id, String... value) {
        super.nfcDataError(status, id, value);
    }

    @Override
    protected void processCloseACT(final String ws_retorno, String mRequired) {
        super.processCloseACT(ws_retorno, mRequired);
        //
//        mDrawerLayout.closeDrawer(GravityCompat.START);
        //
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //
                mPresenter.getProductSerialList(ws_retorno);
                //
                progressDialog.dismiss();
            }
        }, 150);
    }

    @Override
    public void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct023(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act023_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();

    }

    //Tratativa SESSION NOT FOUND
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

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        return true;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

}
