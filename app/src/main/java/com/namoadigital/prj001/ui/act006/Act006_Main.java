package com.namoadigital.prj001.ui.act006;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act013.Act013_Main;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;
import com.namoadigital.prj001.view.frag.frg_serial_search.On_Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search.PRODUCT_ID;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act006_Main extends Base_Activity_Frag_NFC_Geral implements Act006_Main_View, On_Frg_Serial_Search {

    public static final String LIST_LABEL = "list_label";
    public static final String LIST_OPT = "list_opt";

    private Act006_Main_Presenter mPresenter;

    private View vNFormSelected;

    private int pendencies_qty;

    private FragmentManager fm;
    private Frg_Serial_Search mFrgSerialSearch;

    private HMAux hmAux_Trans_frg_serial_search;
    protected String mResource_CodeSS = "0";

    private String fragProduct_ID;
    private String fragSerial_ID;
    private String fragTracking;
    private boolean fragIsOnlyOne;

    //variaveis enviadas para o fluxo de finalizar + novo.
    private String productCode;
    private String productDesc;
    private String productId;
    private String serialId;
    private String customFormType;
    private String customFormTypeDesc;
    private String customFormCode;
    private String customFormVersion;
    private String customFormCodeDesc;
    private boolean blockedByExecutionLimitReach;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act006_main);

        vNFormSelected = findViewById(R.id.act006_nform_in_progress);

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
                Constant.ACT006
        );

        loadTranslation();
        //
        mResource_CodeSS = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_SEARCH
        );
        //
        loadTranslationFrg_Serial_Search();

    }

    private boolean hasNFormSelected() {
        if(customFormType.isEmpty()){
            return false;
        }
        return true;
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act006_title");
        transList.add("act006_lbl_new");
        transList.add("act006_lbl_barcode");
        transList.add("btn_pendencies");
        transList.add("btn_check_exists");
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
        transList.add("alert_new_opt_ttl");
        transList.add("alert_new_opt_product_lbl");
        transList.add("alert_new_opt_serial_lbl");
        transList.add("mket_serial_hint");
        transList.add("alert_no_value_filled_ttl");
        transList.add("alert_no_value_filled_msg");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        //
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        //
        transList.add("alert_turn_offline_mode_on_ttl");
        transList.add("alert_turn_offline_mode_on_msg");
        //
        transList.add("alert_free_execution_blocked_ttl");
        transList.add("alert_free_execution_blocked_msg");
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
                mFrgSerialSearch.getFragTranslationsVars()
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        fm = getSupportFragmentManager();
        //
        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act006_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        controls_sta.addAll(mFrgSerialSearch.getControlsSta());
        mFrgSerialSearch.setClickListener(actionBTN);
        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {
                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_01:
                        if(blockedByExecutionLimitReach) {
                            showExecutionBlockMsg();
                        }else{
                            processSerialSearch(optionsInfo);
                        }
                        break;
                    case Frg_Serial_Search.BTN_OPTION_02:
                            processPendencies(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_03:
                        break;
                    default:
                        break;
                }
            }
        });
        //
        mFrgSerialSearch.setShowHideTracking(ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1 ? true : false);
        mFrgSerialSearch.setBtn_Option_01_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_01_Label(hmAux_Trans.get("btn_check_exists"));
        mFrgSerialSearch.setBtn_Option_02_BackGround(R.drawable.namoa_cell_2_states);
        mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_pendencies"));
        mFrgSerialSearch.setBtn_Option_03_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_04_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);

        mPresenter = new Act006_Main_Presenter_Impl(
                context,
                this,
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans

        );

        hideSoftKeyboard();

        mPresenter.getPendencies();
        mPresenter.getMD_Products();

        if(!productId.isEmpty()) {
            mFrgSerialSearch.setProductIdText(productId);
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(false);
        }

        if (!fragProduct_ID.isEmpty()&& productId.isEmpty()) {
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

        if(hasNFormSelected()){
            vNFormSelected.setVisibility(View.VISIBLE);
        }
        //
        checkSiteAvailablity();
    }

    private void checkSiteAvailablity() {
        blockedByExecutionLimitReach = ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context);
        //
        applyBlockExecutionAction();
    }

    /**
     * LUCHE - 14/01/2021
     * Metodo que exibe msg de  bloqueio e some com btn NFC em caso de bloqueio.
     */
    private void applyBlockExecutionAction() {
        if(blockedByExecutionLimitReach){
            showExecutionBlockMsg();
            mFrgSerialSearch.setSupportNFC(false);
        }else{
            mFrgSerialSearch.setSupportNFC(true);
        }
    }

    @Override
    /**
     * LUCHE - 13/01/2021
     * Metodo que aplica as alterações na interface para impedir que o usuario crie uma nova execução.
     */
    public void showExecutionBlockMsg() {
        showMsg(
            hmAux_Trans.get("alert_free_execution_blocked_ttl"),
            hmAux_Trans.get("alert_free_execution_blocked_msg")
        );
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            fragProduct_ID = bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, "");
            fragSerial_ID = bundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
            fragTracking = bundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");

            productCode = bundle.getString(MD_ProductDao.PRODUCT_CODE, "");
            productDesc = bundle.getString(MD_ProductDao.PRODUCT_DESC, "");
            productId = bundle.getString(MD_ProductDao.PRODUCT_ID, "");
            serialId = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            customFormType = bundle.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, "");
            customFormTypeDesc = bundle.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, "");
            customFormCode = bundle.getString(GE_Custom_FormDao.CUSTOM_FORM_CODE, "");
            customFormVersion = bundle.getString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, "");
            customFormCodeDesc = bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, "");

        } else {
            fragProduct_ID = "";
            fragSerial_ID = "";
            fragTracking = "";

            productCode = "";
            productDesc = "";
            productId = "";
            serialId = "";
            customFormType = "";
            customFormTypeDesc = "";
            customFormCode = "";
            customFormVersion = "";
            customFormCodeDesc = "";
        }
    }

    private void processSerialSearch(HMAux optionsInfo) {
        if (optionsInfo.get(PRODUCT_ID).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0

                ) {

            mPresenter.executeSerialSearch(
                    optionsInfo.get(PRODUCT_ID),
                    optionsInfo.get(Frg_Serial_Search.SERIAL),
                    optionsInfo.get(Frg_Serial_Search.TRACKING),
                    mFrgSerialSearch.isForceExactSearch()
            );
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_no_value_filled_ttl"),
                    hmAux_Trans.get("alert_no_value_filled_msg"),
                    null,
                    0
            );
        }
    }

    private void processPendencies(HMAux optionsInfo) {
        mPresenter.checkPendenciesFlow(pendencies_qty);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT006;
        mAct_Title = Constant.ACT006 + "_" + "title";
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
        if(hasNFormSelected()){
            ToolBox_Inf.buildFooterDialog(context, false);
        }else {
            ToolBox_Inf.buildFooterDialog(context, true);
        }
    }

    private void initActions() {
        if(hasNFormSelected()){
            ImageView ivClose = vNFormSelected.findViewById(R.id.iv_nform_new_header);
            TextView tvNFormSelected = vNFormSelected.findViewById(R.id.tv_process_new_header);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vNFormSelected.setVisibility(View.GONE);
                    recoverInitialNFormState();
                }
            });
            tvNFormSelected.setText(customFormCodeDesc);
        }
    }

    private void recoverInitialNFormState() {
        productCode = "";
        productDesc = "";
        productId = "";
        serialId = "";
        customFormType = "";
        customFormTypeDesc = "";
        customFormCode = "";
        customFormVersion = "";
        customFormCodeDesc = "";
        mPresenter.getMD_Products();
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void setPendenciesQty(int qty) {
        pendencies_qty = qty;
        String btn_dependency_qty_text = hmAux_Trans.get("btn_pendencies") + " (" + pendencies_qty + ")";

        mFrgSerialSearch.setBtn_Option_02_Label(btn_dependency_qty_text);
    }

    @Override
    public void showMsg(String title, String msg) {
        ToolBox.alertMSG(
                Act006_Main.this,
                title,
                msg,
                null,
                0
        );
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
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void callAct013(Context context) {
        Intent mIntent = new Intent(context, Act013_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT006);
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct020(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act020_Main.class);
        if(!customFormCodeDesc.isEmpty()){
            buildBundleFOrNforFinishPlusNew(bundle);

        }
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }

    private void buildBundleFOrNforFinishPlusNew(Bundle bundle) {
        bundle.putString(MD_ProductDao.PRODUCT_CODE, productCode);
        bundle.putString(MD_ProductDao.PRODUCT_DESC,productDesc);
        bundle.putString(MD_ProductDao.PRODUCT_ID,productId);
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, serialId);
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, customFormType);
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, customFormTypeDesc);
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, customFormCode);
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION,customFormVersion);
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, customFormCodeDesc);
    }

    @Override
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);
        //
        progressDialog.dismiss();
        mPresenter.extractSearchResult(result);
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        progressDialog.dismiss();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //implementar dialog confirmando busca offline
        progressDialog.dismiss();
        /*
            07/02/2020 - Decidido tratar o erro de Http no metodo processError_http.
                         Para qualquer alteracao futura, considerar o sync_required do serial
                         pois o resultado da consulta de serial online atropela o serial offline

            14/08/2019 -  Inicio do fluxo offline para N-Form, ao nao conseguir realizar a pesquisa
            de serial devido mah conexao, exibi um dialog perguntando se o user gostaria de ligar o
            modo of-line
         */
//        ToolBox.alertMSG(
//                context,
//                hmAux_Trans.get("alert_turn_offline_mode_on_ttl"),
//                hmAux_Trans.get("alert_turn_offline_mode_on_msg"),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ToolBox.setPreference_Offline_Mode(context, true);
//                        mPresenter.offlineSerialSearch();
//                    }
//                },
//                1
//        );
    }
        /*
            07/02/2020 - Barrionuevo - Continuar fluxo via offline quando ha falha no HTTP.
            Decidido tratar o erro de Http no metodo processError_http.
            Para qualquer alteracao futura, considerar o sync_required do serial
            pois o resultado da consulta de serial online atropela o serial offline

        */
    @Override
    protected void processError_http() {
        //Super realiza o mesmo comportamento do error_1
//        super.processError_http();
        progressDialog.dismiss();
        //LUCHE - 17/03/2021 - Aplicado busca exata tb no offline
        //Nesse caso em especifico, de erro http, não faz exato
        mPresenter.offlineSerialSearch(false);
    }

    // Hugo
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
    public void onBackPressed() {
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
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
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
                        mPresenter.executeSerialSearch(product_id, "", "", false);
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
                        mPresenter.executeSerialSearch(hmAux.get(PRODUCT_ID), value[3], "", true);
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
    public boolean hasHideSerialInfoChk() {
        return true;
    }
    /*
        BARRIONUEVO 17-04-2020
        Atualiza info do footer
     */
    @Override
    protected void processRefreshMessage(String mType, String mValue, String mActivity) {
        super.processRefreshMessage(mType, mValue, mActivity);
        iniUIFooter();
        checkSiteAvailablity();
    }
}
