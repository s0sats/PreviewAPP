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
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
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

import static com.namoadigital.prj001.view.frag.Frg_Serial_Search.PRODUCT_ID;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act030_Main extends Base_Activity_Frag_NFC_Geral implements Act030_Main_View {

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
    //private Bundle bundle;

    private String fragProduct_ID;
    private String fragSerial_ID;
    private String fragTracking;
    private boolean fragIsOnlyOne;

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
        hmAux_Trans_frg_serial_search = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_CodeSS,
                ToolBox_Con.getPreference_Translate_Code(context),
                mFrgSerialSearch.getFragTranslationsVars()
        );
    }

    private void initVars() {
        recoverIntentsInfo();

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

        hideSoftKeyboard();

        mPresenter.getMD_Products();

        if (!fragProduct_ID.isEmpty()) {
            mFrgSerialSearch.setProductIdText(fragProduct_ID);

            if (fragIsOnlyOne) {
                mFrgSerialSearch.setShowTree(false);
                mFrgSerialSearch.setShowAll(false);
            } else {
                mFrgSerialSearch.setShowTree(true);
            }
        }

        if (!fragSerial_ID.isEmpty() || !fragTracking.isEmpty()) {
            mFrgSerialSearch.setSerialIdText(fragSerial_ID);
            mFrgSerialSearch.setTrackingText(fragTracking);
        }
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            fragProduct_ID = bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, "");
            fragSerial_ID = bundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
            fragTracking = bundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");
        } else {
            fragProduct_ID = "";
            fragSerial_ID = "";
            fragTracking = "";
        }
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
    public void setProduto(ArrayList<MD_Product> list) {
        if (list.size() > 1) {
            mFrgSerialSearch.setProductIdText(hmAux_Trans_frg_serial_search.get("product_all_lbl"));
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(true);
            fragIsOnlyOne = false;
        } else if (list.size() == 1) {
            mFrgSerialSearch.setProductIdText(list.get(0).getProduct_id());
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(false);
            fragIsOnlyOne = true;
        } else {
            mFrgSerialSearch.setProductIdText("");
        }
    }

    @Override
    public void setProductInfoToDrawer(MD_Product md_product) {
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
    }

    @Override
    public void loadProductSerialList(ArrayList<MD_Product_Serial> prod_serial_list) {
    }

    @Override
    public void showQtyExceededMsg(long record_page, long record_count) {
    }

    @Override
    public void showNewSerialMsg() {
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
            ToolBox_Inf.hideSoftKeyboard(Act030_Main.this);
            String product_id = "";
            //
            switch (value[0]) {
                case PRODUCT:
                    product_id = mPresenter.searchProductInfo(value[2], "");

                    if (!product_id.equals("")) {
                        mFrgSerialSearch.setProductIdText(product_id);
                        mFrgSerialSearch.setSerialIdText("");
                        mFrgSerialSearch.setTrackingText("");
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
                    //HMAux hmAux = mFrgSerialSearch.getHMAuxValues();
                    product_id = mFrgSerialSearch.searchProductInfo(value[2], "");

                    if (!product_id.equals("") || value[2].equalsIgnoreCase("")) {

                        if (!product_id.equals("")) {
                            mFrgSerialSearch.setProductIdText(product_id);
                        }
                        mFrgSerialSearch.setSerialIdText(value[3]);
                        mFrgSerialSearch.setTrackingText("");
                        //
                        HMAux hmAux = mFrgSerialSearch.getHMAuxValues();
                        mPresenter.executeSerialSearch(hmAux.get(PRODUCT_ID), value[3], "");
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
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);
        //
        progressDialog.dismiss();
        //
        mPresenter.extractSearchResult(result);
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

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        progressDialog.dismiss();
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
    }
}
