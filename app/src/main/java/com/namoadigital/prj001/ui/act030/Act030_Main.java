package com.namoadigital.prj001.ui.act030;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act031.Act031_Main;
import com.namoadigital.prj001.ui.act045.Act045_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act030_Main extends Base_Activity_NFC_Geral implements Act030_Main_View {

    public static final String PROGRESS_WS_SERIAL_SEARCH = "progress_ws_serial_search";
    public static final String PROGRESS_WS_SYNC = "progress_ws_sync";
    public static final String PROGRESS_NFC = "progress_nfc";
    public static final String NEW_SERIAL = "new_serial";

    private Act030_Main_Presenter mPresenter;
    private FragmentManager fm;
    private Frg_Serial_Search mFrgSerialSearch;

    private HMAux hmAux_Trans_frg_serial_search;
    protected String mResource_CodeSS = "0";

    private String ws_process = "";
    private ArrayList<HMAux> wsResults = new ArrayList<>();

    private MD_Product_Serial serial;
    private Bundle bundle;

    //    private String ws_process;
    //    private Act020_Prod_Serial_Adapter mAdapter;
    //    private TextView tv_no_result;
    //    private ListView lv_prod_serial_list;
    //    private TextView tv_records_count;
    //    private TextView tv_records_limit;
    //    private LinearLayout ll_records;
    //    private TextView tv_records;
    //    private ActionBarDrawerToggle mDrawerToggle;
    //    private Act030_Frag_Filter fragFilters;
    //    private FragmentManager fm;
//    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act030_main);

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
                Constant.ACT030
        );
        //
        fm = getSupportFragmentManager();
        //
        loadTranslation();

        mResource_CodeSS = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_SEARCH
        );
        //
        loadTranslationFrg_Serial_Search();
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
        transList.add("progress_sync_title");
        transList.add("progress_sync_msg");
        transList.add("alert_product_not_found_ttl");
        transList.add("alert_product_not_found_msg");
        transList.add("alert_new_serial_ttl");
        transList.add("alert_new_serial_msg");
        transList.add("alert_new_serial_not_allow_ttl");
        transList.add("alert_new_serial_not_allow_msg");
        //
        transList.add("drawer_tracking_lbl");
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        transList.add("alert_tracking_not_found_ttl");
        transList.add("alert_tracking_not_found_msg");
        //
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        //
        transList.add("btn_check_exists");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void loadTranslationFrg_Serial_Search() {
        List<String> transList = new ArrayList<String>();
        transList.add("btn_enable_nfc");
        transList.add("product_lbl");
        transList.add("serial_lbl");
        transList.add("tracking_lbl");
        transList.add("btn_option_01");
        transList.add("btn_option_02");
        transList.add("btn_option_03");
        transList.add("product_hint");
        transList.add("serial_hint");
        transList.add("tracking_hint");
        transList.add("product_all_lbl");

        hmAux_Trans_frg_serial_search = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_CodeSS,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        bundle = new Bundle();

        fm = getSupportFragmentManager();

        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act030_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        controls_sta.addAll(mFrgSerialSearch.getControlsSta());
        mFrgSerialSearch.setClickListener(actionBTN);
        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {

                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_01:
                        processLoadSO(optionsInfo);
                        break;
                    default:
                        break;
                }
            }
        });

        mFrgSerialSearch.setShowHideTracking(ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1 ? true : false);
        mFrgSerialSearch.setBtn_Option_01_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_01_Label(hmAux_Trans.get("btn_check_exists"));
        mFrgSerialSearch.setBtn_Option_02_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_03_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_04_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);
        //
        mPresenter = new Act030_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
//        //
//        tv_records = (TextView) findViewById(R.id.act030_tv_record_info);
//        //
//        ll_records = (LinearLayout) findViewById(R.id.act030_ll_limit_exceeded);
//        //
//        tv_records_limit = (TextView) findViewById(R.id.act030_tv_record_limit);
//        //
//        tv_records_count = (TextView) findViewById(R.id.act030_tv_record_count);
//        //
//        lv_prod_serial_list = (ListView) findViewById(R.id.act030_lv_prod_serial);
//        //
//        tv_no_result = (TextView) findViewById(R.id.act030_tv_no_result);
//        tv_no_result.setText(hmAux_Trans.get("no_search_realized"));
//        //
//        /*
//         * Drawer setup
//         */
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.act030_drawer);
//
//        mDrawerToggle = new ActionBarDrawerToggle(
//                Act030_Main.this,
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
//        //
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        //
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        //
//        mDrawerToggle.syncState();
//        //
//        fragFilters = (Act030_Frag_Filter) fm.findFragmentById(R.id.act030_frag_filter);
//        //
//        fragFilters.setHmAux_Trans(hmAux_Trans);
//        //
//        fragFilters.setSupportNFC(supportNFC);
//        //
//        controls_sta.addAll(fragFilters.getControlsSta());
//        //
//        fragFilters.setClickListener(actionBTN);
//        //
//        fragFilters.setOnDrawerClick(new Act030_Frag_Filter.IAct030_Filter() {
//            @Override
//            public void onIvSearchClick(String product_id, String serial, String tracking) {
//                ToolBox_Inf.hideSoftKeyboard(Act030_Main.this);
//                if ((product_id.trim().length() > 0 && serial.trim().length() > 0)
//                        || tracking.trim().length() > 0
//                        ) {
//                    //Se somente tracking preenchido, chama Ws sem validar produto local
//                    if (tracking.trim().length() > 0 && product_id.trim().length() == 0 && serial.trim().length() == 0) {
//                        mPresenter.executeSerialSearch(product_id, serial, tracking);
//                    } else {
//                        //Se tudo preenchido, valida se produto existe
//                        if (mPresenter.checkProductExists(
//                                product_id.trim(),
//                                serial.trim()
//                        )
//                                ) {
//                            //
//                            mPresenter.executeSerialSearch(product_id, serial, tracking);
//                        }
//                    }
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
//        //
//        mPresenter.checkSingleProduct();
//        /*
//         * Drawer setup end
//         */

        hideSoftKeyboard();

        mPresenter.getMD_Products();
    }

    private void processLoadSO(HMAux optionsInfo) {
        if (optionsInfo.get(Frg_Serial_Search.PRODUCT_ID).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0

                ) {

            mPresenter.executeSerialSearch(
                    optionsInfo.get(Frg_Serial_Search.PRODUCT_ID),
                    optionsInfo.get(Frg_Serial_Search.SERIAL),
                    optionsInfo.get(Frg_Serial_Search.TRACKING)
            );

//            //Se somente tracking preenchido, chama Ws sem validar produto local
//            if (optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0 &&
//                    optionsInfo.get(Frg_Serial_Search.PRODUCT_ID).trim().length() == 0 &&
//                    optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() == 0) {
//                mPresenter.executeSerialSearch(
//                        optionsInfo.get(Frg_Serial_Search.PRODUCT_ID),
//                        optionsInfo.get(Frg_Serial_Search.SERIAL),
//                        optionsInfo.get(Frg_Serial_Search.TRACKING)
//                );
//            } else {
//                //Se tudo preenchido, valida se produto existe
//                if (mPresenter.checkProductExists(
//                        optionsInfo.get(Frg_Serial_Search.PRODUCT_ID).trim(),
//                        optionsInfo.get(Frg_Serial_Search.SERIAL).trim()
//                )
//                        ) {
//                    //
//                    mPresenter.executeSerialSearch(
//                            optionsInfo.get(Frg_Serial_Search.PRODUCT_ID),
//                            optionsInfo.get(Frg_Serial_Search.SERIAL),
//                            optionsInfo.get(Frg_Serial_Search.TRACKING)
//                    );
//                }
//            }
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_no_search_parameter_ttl"),
                    hmAux_Trans.get("alert_no_search_parameter_msg"),
                    null,
                    0
            );
        }
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT030;
        mAct_Title = Constant.ACT030 + "_" + "title";
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
//        //Abre drawer ao carregar a tela
//        mDrawerLayout.openDrawer(GravityCompat.START);
//        //Sincroniza icone do hambuguer
//        mDrawerToggle.syncState();
//        //
//        lv_prod_serial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (ToolBox_Con.isOnline(context)) {
//                    MD_Product_Serial productSerial = (MD_Product_Serial) parent.getItemAtPosition(position);
//                    //
//                    mPresenter.defineFlow(productSerial, false);
//                } else {
//                    ToolBox_Inf.showNoConnectionDialog(context);
//                }
//            }
//        });
    }

    @Override
    public void setProduto(ArrayList<MD_Product> list) {
        if (list.size() > 1) {
            mFrgSerialSearch.setProductIdText(hmAux_Trans_frg_serial_search.get("product_all_lbl"));
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(true);
        } else if (list.size() == 1) {
            mFrgSerialSearch.setProductIdText(list.get(0).getProduct_id());
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(false);
        } else {
            mFrgSerialSearch.setProductIdText("");
        }
    }

    @Override
    public void setProductInfoToDrawer(MD_Product md_product) {
//        fragFilters.setProductCodeText(String.valueOf(md_product.getProduct_code()));
//        fragFilters.setProductIdText(md_product.getProduct_id());
    }

    @Override
    public void setTProductSerial(MD_Product_Serial serial) {
        this.serial = serial;
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

            case PROGRESS_WS_SYNC:
                title = hmAux_Trans.get("progress_sync_title");
                msg = hmAux_Trans.get("progress_sync_msg");
                break;

            case PROGRESS_NFC:
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
    public void showMsg(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
    }

    @Override
    public void setRecordInfo(long record_size, long record_page) {
//        if (record_size > 0) {
//            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + record_size + " " + hmAux_Trans.get("records_lbl"));
//        } else {
//            tv_records.setText(hmAux_Trans.get("no_record_found_lbl"));
//
//        }
    }

    @Override
    public void loadProductSerialList(ArrayList<MD_Product_Serial> prod_serial_list) {
//        //Esconde tv com msg de nenhum busca feita
//        //e ll com informações de limite de excedido.
//        tv_no_result.setVisibility(View.GONE);
//        ll_records.setVisibility(View.GONE);
//        //
//        mAdapter = new Act020_Prod_Serial_Adapter(
//                context,
//                R.layout.act020_cell,
//                prod_serial_list
//        );
//        //
//        lv_prod_serial_list.setAdapter(mAdapter);
    }

    @Override
    public void showQtyExceededMsg(long record_page, long record_count) {
//        ll_records.setVisibility(View.VISIBLE);
//
//        tv_records_limit.setText(
//                hmAux_Trans.get("records_display_limit_lbl") + " " + record_page
//        );
//
//        tv_records_count.setText(
//                hmAux_Trans.get("records_found_lbl") + " " + record_count
//        );
//
//        ToolBox.alertMSG(
//                context,
//                hmAux_Trans.get("alert_qty_records_exceeded_ttl"),
//                hmAux_Trans.get("alert_qty_records_exceeded_msg") + "\n" + record_count + " " + hmAux_Trans.get("alert_qty_records_founded"),
//                null,
//                0);
    }

    @Override
    public void showNewSerialMsg() {
//        //Se nenhum serial encontrado e produto permite novo serial
//        //exibe caixa perguntando se deseja criar novo serial.
//        if (fragFilters.getTrackingText().length() > 0
//                && fragFilters.getProductIdText().length() == 0
//                && fragFilters.getSerialIdText().length() == 0
//                ) {
//            ToolBox.alertMSG(
//                    context,
//                    hmAux_Trans.get("alert_tracking_not_found_ttl"),
//                    hmAux_Trans.get("alert_tracking_not_found_msg"),
//                    null,
//                    0
//            );
//        } else {
//
//            if (mPresenter.productAllowNewSerial(fragFilters.getProductCodeText(), fragFilters.getProductIdText())) {
//                ToolBox.alertMSG(
//                        context,
//                        hmAux_Trans.get("alert_new_serial_ttl"),
//                        hmAux_Trans.get("alert_new_serial_msg"),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                mPresenter.defineFlow(serial, true);
//                            }
//                        },
//                        1
//                );
//            }
//        }
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct031(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act031_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct045(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act045_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
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
            mFrgSerialSearch.cleanFields();
            ToolBox_Inf.hideSoftKeyboard(Act030_Main.this);
            String product_id = "";
            //
            switch (value[0]) {
                case PRODUCT:
                    product_id = mPresenter.searchProductInfo(value[2], "");

                    if (!product_id.equals("")) {
                        mFrgSerialSearch.setProductIdText(product_id);
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


//                    fragFilters.setNFCText(hmAux_Trans.get("drawer_product_lbl"));
//                    fragFilters.setProductCodeText(value[2]);
//                    fragFilters.setProductIdText(product_id);

                    break;
                case SERIAL:
                    product_id = mPresenter.searchProductInfo(value[2], "");
                    //
                    if (!product_id.equals("")) {
                        mFrgSerialSearch.setProductIdText(product_id);
                        mFrgSerialSearch.setSerialIdText(value[3]);
                        mFrgSerialSearch.setTrackingText("");
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

//                    if (!product_id.equals("")) {
//                        fragFilters.setNFCText(hmAux_Trans.get("drawer_serial_lbl"));
//                        fragFilters.setProductCodeText(value[2]);
//                        fragFilters.setSerialIdText(value[3]);
//                        if (fragFilters.getProductCodeText().length() > 0
//                                && fragFilters.getSerialIdText().length() > 0
//                                ) {
//                            mPresenter.executeSerialSearch(value[2], "", value[3]);
//                        }
//                    } else {
//                        ToolBox.alertMSG(
//                                context,
//                                hmAux_Trans.get("alert_local_product_not_found_ttl"),
//                                hmAux_Trans.get("alert_local_product_not_found_msg"),
//                                null,
//                                0
//                        );
//                    }

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
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);

//        mDrawerLayout.closeDrawer(GravityCompat.START);
//        //
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPresenter.getProductSerialList(ws_retorno);
//                //
//                progressDialog.dismiss();
//            }
//        }, 150);

        progressDialog.dismiss();
        //
        mPresenter.extractSearchResult(result);

        //
//        progressDialog.dismiss();
//        mPresenter.extractSearchResult(result);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
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
