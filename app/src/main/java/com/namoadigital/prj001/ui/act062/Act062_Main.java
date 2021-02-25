package com.namoadigital.prj001.ui.act062;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act061.Act061_Main;
import com.namoadigital.prj001.ui.act063.Act063_Main;
import com.namoadigital.prj001.ui.act067.Act067_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;
import com.namoadigital.prj001.view.frag.frg_serial_search.On_Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search.PRODUCT_ID;

public class Act062_Main extends Base_Activity_Frag_NFC_Geral implements Act062_Main_Contract.I_View, On_Frg_Serial_Search {

    private FragmentManager fm;
    private Frg_Serial_Search mFrgSerialSearch;
    private HMAux hmAux_Trans_frg_serial_search;
    protected String mResource_CodeSS = "0";
    private String fragProduct_ID;
    private String fragProduct_CODE;
    private String fragSerial_ID;
    private String fragTracking;
    private boolean fragIsOnlyOne;
    private Act062_Main_Presenter mPresenter;
    private String wsProcess;
    private String requestingAct;
    private String requestingActProcess;
    private String requestingActPrefix;
    private String requestingActCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.act062_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSetup();
        initVars();
        initFooter();
        initAction();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    private void initSetup() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            Constant.ACT062
        );
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
        transList.add("act062_title");
        transList.add("btn_check_exists");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_no_search_parameter_ttl");
        transList.add("alert_no_search_parameter_msg");
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        transList.add("alert_serial_without_inbound_ttl");
        transList.add("alert_serial_without_inbound_msg");
        //
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
            Frg_Serial_Search.getFragTranslationsVars()
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        mPresenter = new Act062_Main_Presenter(
            context,
            this,
            hmAux_Trans
        );
        //
        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act062_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        mFrgSerialSearch.setClickListener(actionBTN);
        mFrgSerialSearch.setBtn_Option_01_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_01_Label(hmAux_Trans.get("btn_check_exists"));
        //
        controls_sta.addAll(mFrgSerialSearch.getControlsSta());
        //
        hideUnsedButtons();
        //
        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {

                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_01:
                        serialSearch(optionsInfo);
                        break;
                    default:
                        break;
                }
            }
        });
        //
        hideSoftKeyboard();
        //Busca lista de produtos e seta no frag
        mPresenter.getMD_Products();
        //
        defineProdutcTree();
    }

    private void defineProdutcTree() {
        if (!fragProduct_ID.isEmpty()) {
            mFrgSerialSearch.setProductIdText(fragProduct_ID);
            if (fragIsOnlyOne) {
                mFrgSerialSearch.setShowTree(false);
                mFrgSerialSearch.setShowAll(false);
            } else {
                mFrgSerialSearch.setShowTree(true);
            }
        }
        //
        if (!fragSerial_ID.isEmpty() || !fragTracking.isEmpty()) {
            mFrgSerialSearch.setSerialIdText(fragSerial_ID);
            mFrgSerialSearch.setTrackingText(fragTracking);
        }
    }

    @Override
    public void setWs_process(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
            ttl,
            msg,
            hmAux_Trans.get("sys_alert_btn_cancel"),
            hmAux_Trans.get("sys_alert_btn_ok")
        );
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
    public void callAct063(Bundle bundle) {
        Intent mIntent = new Intent(context, Act063_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        if(bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,requestingAct);
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY,requestingActProcess);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY,requestingActPrefix);
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY,requestingActCode);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();

    }

    @Override
    public void setProduct(ArrayList<MD_Product> productList) {
        if (productList.size() > 1) {
            mFrgSerialSearch.setProductIdText(hmAux_Trans_frg_serial_search.get("product_all_lbl"));
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(true);
            fragIsOnlyOne = false;
        } else if (productList.size() == 1) {
            mFrgSerialSearch.setProductIdText(productList.get(0).getProduct_id());
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(false);
            fragIsOnlyOne = true;
        } else {
            mFrgSerialSearch.setProductIdText("");
        }

    }

    private void serialSearch(HMAux optionsInfo) {
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

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            fragProduct_ID = bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, "");
            fragSerial_ID = bundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
            fragTracking = bundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");
            requestingAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT051);
            //
            requestingActProcess = bundle.getString(ConstantBaseApp.HMAUX_PROCESS_KEY,"");
            requestingActPrefix = bundle.getString(ConstantBaseApp.HMAUX_PREFIX_KEY,"-1");
            requestingActCode = bundle.getString(ConstantBaseApp.HMAUX_CODE_KEY,"-1");

        } else {
            fragProduct_ID = "";
            fragSerial_ID = "";
            fragTracking = "";
            requestingAct = ConstantBaseApp.ACT051;
            requestingActProcess = "";
            requestingActPrefix = "-1";
            requestingActCode = "-1";
        }
    }

    private void hideUnsedButtons() {
        //Seta todos btn como invisiveis
        mFrgSerialSearch.setBtn_Option_02_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_03_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_04_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);
    }

    private void initFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT062;
        mAct_Title = Constant.ACT062 + Constant.title_lbl;
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
//        super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initAction() {

    }

    //LUCHE - 14/08/2019
    @Override
    public String getRequestingActProcess() {
        return requestingActProcess;
    }

    // NFC Processing Data
    @Override
    protected void nfcData(boolean status, int id, String... value) {
        super.nfcData(status, id, value);

        Log.d("NFC", value[0]);

        if (!status) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            ToolBox.alertMSG(
                context,
                "Erro:",
                value[0],
                null,
                0
            );

        } else {
            String product_id = "";
            //
            switch (value[0]) {
                case PRODUCT:
                    product_id = mFrgSerialSearch.searchProductInfo(value[2], "");
                    //
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
    protected void processCloseACT(String mLink, String mRequired) {
        //super.processCloseACT(mLink, mRequired);
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (wsProcess.equals(WS_Serial_Search.class.getName())) {
            mPresenter.extractSearchResult(mLink);

        }
        //
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked(requestingAct);
    }

    @Override
    public void callAct061() {
        Intent mIntent = new Intent(context, Act061_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY,requestingActProcess);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY,requestingActPrefix);
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY,requestingActCode);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,requestingAct);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct067() {
        Intent mIntent = new Intent(context, Act067_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY,requestingActProcess);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY,requestingActPrefix);
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY,requestingActCode);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,requestingAct);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    //region WsReturnUnspected

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        progressDialog.dismiss();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);

        progressDialog.dismiss();
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

    //endregion
    @Override
    public boolean hasHideSerialInfoChk() {
        return true;
    }
}
