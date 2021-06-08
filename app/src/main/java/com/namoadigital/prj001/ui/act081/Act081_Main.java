package com.namoadigital.prj001.ui.act081;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MyActionFilterParam;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;
import com.namoadigital.prj001.view.frag.frg_serial_search.On_Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search.PRODUCT_ID;

public class Act081_Main extends Base_Activity_Frag_NFC_Geral implements
    Act081_Main_Contract.I_View,
    On_Frg_Serial_Search,
    On_Frg_Serial_Search.onProductSelectionReturnListener,
    On_Frg_Serial_Search.onProductTypingListener
{

    public static final String LIST_LABEL = "list_label";
    public static final String LIST_OPT = "list_opt";

    private Act081_Main_Presenter mPresenter;

    private View vStepSelected;

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
    private int productCode;
    private String productDesc;
    private String productId;
    private String serialId;
    private String customFormType;
    private String customFormTypeDesc;
    private String customFormCode;
    private String customFormVersion;
    private String customFormCodeDesc;
    private int ticketPrefix;
    private String stepDesc;
    private int ticketCode;
    private String ticketId;
    private int stepCode;
    private String mainRequestingAct;
    private boolean forceExactSearch = false;
    private String wsProcess;
    private boolean isForm;
    private String room_code;
    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act081_main);
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

    private void initActions() {
        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {
                fragProduct_ID = optionsInfo.get(PRODUCT_ID);
                fragSerial_ID = optionsInfo.get(Frg_Serial_Search.SERIAL);
                fragTracking = optionsInfo.get(Frg_Serial_Search.TRACKING);
                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_01:
                        isForm = false;
                        processSerialSearch(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_02:
//                        Toast.makeText(context, "Função em Desenvolvimento", Toast.LENGTH_SHORT).show();
                        isForm = true;
                        processSerialSearch(optionsInfo);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void processSerialSearch(HMAux optionsInfo) {
        if (optionsInfo.get(PRODUCT_ID).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0) {

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

    private void initVars() {
        recoverIntentsInfo();
        //
        fm = getSupportFragmentManager();
        //
        mPresenter = new Act081_Main_Presenter(this,context,hmAux_Trans);
        //
        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act006_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        controls_sta.addAll(mFrgSerialSearch.getControlsSta());
        mFrgSerialSearch.setClickListener(actionBTN);
        //
        mFrgSerialSearch.setShowHideTracking(ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1 ? true : false);
        mFrgSerialSearch.setBtn_Option_01_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_01_Label(hmAux_Trans.get("btn_start_action"));
        mFrgSerialSearch.setBtn_Option_02_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_start_form"));
        mFrgSerialSearch.setBtn_Option_03_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_04_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);
        //LUCHE - 08/06/2021 - evita que ao ler barcode, seja disparado o click no btn principal.
        mFrgSerialSearch.setPerformClickOnEspecialistReturn(false);

//        mPresenter = new Act081_Main_Presenter();

        mPresenter.getMD_Products();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        
        if (!fragProduct_ID.isEmpty()) {
            mFrgSerialSearch.setProductIdText(fragProduct_ID);
            mFrgSerialSearch.setShowTree(true);
            fragIsOnlyOne = false;
        }
        //
//        if (!fragSerial_ID.isEmpty()){
//            mFrgSerialSearch.setSerialIdText(fragSerial_ID);
//        }
        //LUCHE - 05/11/2020
        //Correção do bug que não exibia o serial enviado da act070.
        restoreSerialIdValue();
        //
        if (!fragTracking.isEmpty()) {
            mFrgSerialSearch.setTrackingText(fragTracking);
        }
        //
        vStepSelected = findViewById(R.id.act081_current_step);
        ImageView ivClose = vStepSelected.findViewById(R.id.iv_nform_new_header);
        TextView tvNFormSelected = vStepSelected.findViewById(R.id.tv_process_new_header);
        ivClose.setVisibility(View.GONE);
        tvNFormSelected.setText(ticketId + " - " + stepDesc);
        vStepSelected.setVisibility(View.VISIBLE);

    }

    private void recoverIntentsInfo() {
        mBundle = getIntent().getExtras();
        //
        if (mBundle != null) {
            fragProduct_ID = mBundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, "");
            fragSerial_ID = mBundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
            fragTracking = mBundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");
            room_code = mBundle.getString(CH_RoomDao.ROOM_CODE);
            productCode = mBundle.getInt(MD_ProductDao.PRODUCT_CODE, -1);
            productDesc = mBundle.getString(MD_ProductDao.PRODUCT_DESC, "");
            productId = mBundle.getString(MD_ProductDao.PRODUCT_ID, "");
            serialId = mBundle.getString(MD_Product_SerialDao.SERIAL_ID, "");

            ticketPrefix = mBundle.getInt(TK_TicketDao.TICKET_PREFIX,-1);
            ticketCode = mBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            ticketId = mBundle.getString(TK_TicketDao.TICKET_ID, "");
            stepCode = mBundle.getInt(TK_Ticket_StepDao.STEP_CODE, -1);
            stepDesc = mBundle.getString(TK_Ticket_StepDao.STEP_DESC, "");
            mainRequestingAct = mBundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT070);

        } else {
            fragProduct_ID = "";
            fragSerial_ID = "";
            fragTracking = "";

            productCode = -1;
            productDesc = "";
            productId = "";
            serialId = "";
            ticketPrefix = -1;
            ticketCode = -1;
            ticketId = "";
            stepCode = -1;
            stepDesc = "";
        }
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT081
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

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act081_title");
        transList.add("act081_lbl_new");
        transList.add("act081_lbl_barcode");
        transList.add("btn_start_form");
        transList.add("btn_start_action");
//        transList.add("alert_no_pendencies_title");
//        transList.add("alert_no_pendencies_msg");
//        transList.add("alert_new_opt_ttl");
//        transList.add("alert_new_opt_product_lbl");
//        transList.add("alert_new_opt_serial_lbl");
//        transList.add("mket_serial_hint");
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


    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());

    }

    @Override
    protected void processCloseACT(String result, String mRequired, HMAux hmAux) {
        super.processCloseACT(result, mRequired, hmAux);
        if (wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            mPresenter.extractSearchResult(result);
        }

        progressDialog.dismiss();
    }

    @Override
    public boolean hasHideSerialInfoChk() {
        return true;
    }

    /**
     * LUCHE - 10/11/2020
     * Implementação da interface disparada quando a Act_Product_Selection envia seu retorno.
     * Nessa tela, foi estabelicida como regra que se o prooduto selecionado for diferente do produto
     * já informado, o serial deve ser apagado.
     * @param current_product_id - Texto do campo Product_id
     * @param returned_product_id - Product_Id do obj MD_product retornado pela Act_Product_Selection
     */
    @Override
    public void onProductSelectionReturn(String current_product_id, String returned_product_id) {
        if( current_product_id != null && returned_product_id != null
            && !current_product_id.equals(returned_product_id)
        ){
            mFrgSerialSearch.setSerialIdText("");
        }
    }

    /**
     * LUCHE - 13/11/2020
     * Implementaçãoda interface acionada quando o usuario digita no textView do productId
     * Se alterou o texto, apaga serial.
     * @param typed_product_id
     */
    @Override
    public void onProductTyping(String typed_product_id) {
        mFrgSerialSearch.setSerialIdText("");
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT081;
        mAct_Title = Constant.ACT081 + "_" + "title";
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
        mPresenter.onBackPressedClicked(mainRequestingAct);
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
                        forceExactSearch = false;
//                        mPresenter.executeSerialSearch(product_id, "", "", false, isForm);
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
                        forceExactSearch = true;
                        HMAux hmAux = mFrgSerialSearch.getHMAuxValues();
//                        mPresenter.executeSerialSearch(hmAux.get(PRODUCT_ID), value[3], "", true, isForm);
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
    public void callAct070() {
        Intent intent = new Intent(context, Act070_Main.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, mainRequestingAct);
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix);
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode);
        bundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        //
        bundle.putString(
                ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                mBundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,ConstantBaseApp.ACT005)
        );
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM,ToolBox_Inf.getMyActionFilterParam(mBundle));
        //
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        //
        startActivity(intent);
        finish();
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void showPD(String progress_ttl, String progress_start) {
        enableProgressDialog(
                progress_ttl,
                progress_start,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void showMsg(String show_ttl, String show_msg) {
        ToolBox.alertMSG(
                context,
                show_ttl,
                show_msg,
                null,
                0
        );
    }

    @Override
    public void setProduct(ArrayList<MD_Product> productList) {

        if (productList.size() > 1) {
            if(fragProduct_ID.isEmpty() && productId.isEmpty()) {
                mFrgSerialSearch.setProductIdText(hmAux_Trans_frg_serial_search.get("product_all_lbl"));
                mFrgSerialSearch.setShowTree(false);
            }else{
                restoreProductIdValue();
                mFrgSerialSearch.setShowTree(true);
            }
            mFrgSerialSearch.setShowAll(true);
            fragIsOnlyOne = false;
        } else if (productList.size() == 1) {
            restoreProductIdValue();
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(false);
            fragIsOnlyOne = true;
        } else {
            mFrgSerialSearch.setProductIdText("");
        }

    }

    private void restoreProductIdValue() {
        String restoredProductId = productId;
        if(!fragProduct_ID.isEmpty()){
            restoredProductId = fragProduct_ID;
        }
        mFrgSerialSearch.setProductIdText(restoredProductId);
    }

    /**
     * LUCHE - 05/11/2020
     * Metodo que seta valor do serialId analisandoa s duas var de bundle possiveis
     */
    private void restoreSerialIdValue() {
        String restoreSerialId = serialId;
        if(!fragSerial_ID.isEmpty()){
            restoreSerialId = fragSerial_ID;
        }
        mFrgSerialSearch.setSerialIdText(restoreSerialId);
    }


    @Override
    public void callAct020(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act020_Main.class);

        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, mainRequestingAct);
            bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, fragProduct_ID);
            bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, fragSerial_ID);
            bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, fragTracking);
            bundle.putString(CH_RoomDao.ROOM_CODE, room_code);

            bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix);
            bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode);
            bundle.putString(TK_TicketDao.TICKET_ID, ticketId);
            bundle.putInt(TK_Ticket_StepDao.STEP_CODE, stepCode);
            bundle.putString(TK_Ticket_StepDao.STEP_DESC, stepDesc);
            bundle.putBoolean(Constant.TK_TICKET_IS_FORM_OFF_HAND, isForm);
            bundle.putString(
                    ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                    mBundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,ConstantBaseApp.ACT005)
            );
            bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM,ToolBox_Inf.getMyActionFilterParam(mBundle));
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }
}