package com.namoadigital.prj001.ui.act081;

import static com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search.PRODUCT_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.core.translate.TranslateBuild;
import com.namoadigital.prj001.core.translate.TranslateBuildKt;
import com.namoadigital.prj001.core.translate.di.EventTranslate;
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventActiveUseCase;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MyActionFilterParam;
import com.namoadigital.prj001.service.WS_Product_Serial_Structure;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.GetEventManualUseCase;
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;
import com.namoadigital.prj001.view.frag.frg_serial_search.On_Frg_Serial_Search;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Act081_Main extends Base_Activity_Frag_NFC_Geral implements
        Act081_Main_Contract.I_View,
        On_Frg_Serial_Search,
        On_Frg_Serial_Search.onProductSelectionReturnListener,
        On_Frg_Serial_Search.onProductTypingListener {

    public static final String LIST_LABEL = "list_label";
    public static final String LIST_OPT = "list_opt";

    private Act081_Main_Presenter mPresenter;

    private View vStepSelected;
    private View vNformInProgress;

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
    private int isSoForm;
    private boolean forceSendByFormExec = false;

    @Inject
    GetEventManualUseCase getEventManualUseCase;

    @Inject
    GetEventActiveUseCase getEventUseCase;

    @EventTranslate
    @Inject
    TranslateBuild translateBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act081_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
        //
        if (forceSendByFormExec) {
            mPresenter.executeSerialSave();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initActions() {
        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {
                fragProduct_ID = optionsInfo.get(PRODUCT_ID);
                fragSerial_ID = optionsInfo.get(Frg_Serial_Search.SERIAL);
                fragTracking = optionsInfo.get(Frg_Serial_Search.TRACKING);
                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_02:
                        isForm = false;
                        processSerialSearch(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_03:
//                        Toast.makeText(context, "Função em Desenvolvimento", Toast.LENGTH_SHORT).show();
                        if (mPresenter.hasEventManual()) {
                            showAlertEventInExecution();
                            return;
                        }

                        isForm = true;
                        processSerialSearch(optionsInfo);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void showAlertEventInExecution() {
        showMsg(
                TranslateBuildKt.textOf(hmAux_Trans, EventManualKey.ErrorEventInExecutionTitle),
                TranslateBuildKt.textOf(hmAux_Trans, EventManualKey.ErrorEventInExecutionMsg)
        );
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
        mPresenter = new Act081_Main_Presenter(
                this,
                context,
                hmAux_Trans,
                getEventManualUseCase,
                getEventUseCase
        );
        //
        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act006_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        controls_sta.addAll(mFrgSerialSearch.getControlsSta());
        mFrgSerialSearch.setClickListener(actionBTN);
        //
        mFrgSerialSearch.setShowHideTracking(ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1);
        mFrgSerialSearch.setVisibilityBtnOption01(View.GONE);
        mFrgSerialSearch.setBtn_Option_02_Visibility(View.GONE);
        if (!isFinalizePlusNewProcess()) {
            mFrgSerialSearch.setBtn_Option_02_Visibility(View.VISIBLE);
            mFrgSerialSearch.setBtn_Option_02_BackGround(R.drawable.namoa_cell_3_states);
            mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_start_action"));
        }
        mFrgSerialSearch.setBtn_Option_03_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_03_Label(hmAux_Trans.get("btn_start_form"));
        if (ToolBox_Inf.profileExists(
                context,
                ConstantBaseApp.PROFILE_PRJ001_CHECKLIST,
                ConstantBaseApp.PROFILE_PRJ001_CHECKLIST_PARAM_BLOCK_FORM_SPONTANEOUS
        )) {
            mFrgSerialSearch.setBtn_Option_03_Visibility(View.GONE);
        }
        mFrgSerialSearch.setBtn_Option_04_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);
        //LUCHE - 08/06/2021 - evita que ao ler barcode, seja disparado o click no btn principal.
        mFrgSerialSearch.setPerformClickOnEspecialistReturn(false);

//        mPresenter = new Act081_Main_Presenter();

        mPresenter.getMD_Products();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (!fragProduct_ID.isEmpty()) {
            MD_Product product = mPresenter.searchProduct(fragProduct_ID);
            mFrgSerialSearch.setProductIdText(product.getProduct_desc(), product.getProduct_id());
            mFrgSerialSearch.setShowTree(true);
            fragIsOnlyOne = false;
        }
        //
        if (isFinalizePlusNewProcess()) {
            mFrgSerialSearch.setProductIdText(productDesc, productId);
            mFrgSerialSearch.setProductIdHint(hmAux_Trans_frg_serial_search.get("product_contain_id_lbl"));
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(false);
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
        setNformInProgress();
        //
        setCurrentStepCard();

    }

    private void setNformInProgress() {
        vNformInProgress = findViewById(R.id.act081_nform_in_progress);
        vNformInProgress.setVisibility(View.GONE);
        if (isFinalizePlusNewProcess()) {
            ImageView ivClose = vNformInProgress.findViewById(R.id.card_balloon_icon);
            TextView tvNFormSelected = vNformInProgress.findViewById(R.id.card_balloon_text);
            ivClose.setVisibility(View.VISIBLE);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recoverInitialNFormState();
                    vNformInProgress.setVisibility(View.GONE);
                }
            });
            tvNFormSelected.setText(customFormCodeDesc);
            vNformInProgress.setVisibility(View.VISIBLE);
        }
    }

    private void recoverInitialNFormState() {
        customFormType = "";
        customFormTypeDesc = "";
        customFormCode = "";
        customFormVersion = "";
        customFormCodeDesc = "";
        isSoForm = 0;
        mPresenter.getMD_Products();
        if (!isFinalizePlusNewProcess()) {
            mFrgSerialSearch.setBtn_Option_02_Visibility(View.VISIBLE);
            mFrgSerialSearch.setBtn_Option_02_BackGround(R.drawable.namoa_cell_3_states);
            mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_start_action"));
        }
    }

    private boolean isFinalizePlusNewProcess() {
        return !customFormCodeDesc.isEmpty();
    }

    private void setCurrentStepCard() {
        vStepSelected = findViewById(R.id.act081_current_step);
        ImageView ivClose = vStepSelected.findViewById(R.id.card_balloon_icon);
        TextView tvNFormSelected = vStepSelected.findViewById(R.id.card_balloon_text);
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
            productCode = mBundle.getString(MD_ProductDao.PRODUCT_CODE, "");
            productDesc = mBundle.getString(MD_ProductDao.PRODUCT_DESC, "");
            productId = mBundle.getString(MD_ProductDao.PRODUCT_ID, "");
            serialId = mBundle.getString(MD_Product_SerialDao.SERIAL_ID, "");

            ticketPrefix = mBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            ticketCode = mBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            ticketId = mBundle.getString(TK_TicketDao.TICKET_ID, "");
            stepCode = mBundle.getInt(TK_Ticket_StepDao.STEP_CODE, -1);
            stepDesc = mBundle.getString(TK_Ticket_StepDao.STEP_DESC, "");
            mainRequestingAct = mBundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT070);
            isSoForm = mBundle.getInt(GE_Custom_FormDao.IS_SO, 0);
            customFormType = mBundle.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, "");
            customFormCode = mBundle.getString(GE_Custom_FormDao.CUSTOM_FORM_CODE, "");
            customFormVersion = mBundle.getString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, "");
            customFormCodeDesc = mBundle.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, "");
            forceSendByFormExec = mBundle.getBoolean(Act070_Main.PARAM_FORCE_SEND_BY_FORM_EXEC, false);


        } else {
            fragProduct_ID = "";
            fragSerial_ID = "";
            fragTracking = "";

            productCode = "";
            productDesc = "";
            productId = "";
            serialId = "";
            ticketPrefix = -1;
            ticketCode = -1;
            ticketId = "";
            stepCode = -1;
            stepDesc = "";
            isSoForm = 0;
            forceSendByFormExec = false;
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
        transList.add("dialog_ticket_save_ttl");
        transList.add("dialog_ticket_save_msg");
        transList.add("dialog_ticket_form_save_ttl");
        transList.add("dialog_ticket_form_save_msg");
        transList.add("progress_serial_save_ttl");
        transList.add("progress_serial_save_msg");
        transList.add("alert_offline_save_ttl");
        transList.add("alert_offline_save_msg");
        transList.add("progress_serial_structure_ttl");
        transList.add("progress_serial_structure_msg");
        transList.add("alert_serial_structure_error_ttl");
        transList.add("alert_serial_structure_error_msg");
        //
        transList.add("alert_ticket_results_ok");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        Map<@NotNull String, @NotNull String> translateBuilder = translateBuild.build();

        hmAux_Trans.put(
                EventManualKey.ErrorEventInExecutionTitle.getKey(),
                translateBuilder.get(EventManualKey.ErrorEventInExecutionTitle.getKey())
        );

        hmAux_Trans.put(
                EventManualKey.ErrorEventInExecutionMsg.getKey(),
                translateBuilder.get(EventManualKey.ErrorEventInExecutionMsg.getKey())
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
        } else if (wsProcess.equalsIgnoreCase(WS_Serial_Save.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            mPresenter.callWsSave();
        } else if (wsProcess.equalsIgnoreCase(WS_Save.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            if (mPresenter.hasSerialStructureOutdate()) {
                mPresenter.updateSerialStrucutreAfterWsSave();
            } else {
                mPresenter.executeTicketSaveProcess();
            }
        } else if (wsProcess.equalsIgnoreCase(WS_Product_Serial_Structure.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            mPresenter.executeTicketSaveProcess();
        } else if (wsProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getName())) {
            wsProcess = "";
            Toast.makeText(context, hmAux_Trans.get("alert_ticket_results_ok"), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
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
        if (current_product_id != null && returned_product_id != null
                && !current_product_id.equals(returned_product_id)
        ) {
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

//        Log.d("NFC", value[0]);

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
                        MD_Product product = mPresenter.searchProduct(product_id);
                        mFrgSerialSearch.setProductIdText(product.getProduct_desc(), product.getProduct_id());
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
                            MD_Product product = mPresenter.searchProduct(product_id);
                            mFrgSerialSearch.setProductIdText(product.getProduct_desc(), product.getProduct_id());
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
                mBundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005)
        );
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, ToolBox_Inf.getMyActionFilterParam(mBundle));
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
            if (fragProduct_ID.isEmpty() && productId.isEmpty()) {
                mFrgSerialSearch.setProductIdHint(hmAux_Trans_frg_serial_search.get("product_all_lbl"));
                mFrgSerialSearch.setShowTree(false);
            } else {
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
            mFrgSerialSearch.setProductIdHint(hmAux_Trans_frg_serial_search.get("product_not_found_lbl"));

        }

    }

    private void restoreProductIdValue() {
        String restoredProductId = productId;
        if (!fragProduct_ID.isEmpty()) {
            restoredProductId = fragProduct_ID;
        }
        MD_Product product = mPresenter.searchProduct(restoredProductId);
        mFrgSerialSearch.setProductIdText(product.getProduct_desc(), product.getProduct_id());
    }

    /**
     * LUCHE - 05/11/2020
     * Metodo que seta valor do serialId analisandoa s duas var de bundle possiveis
     */
    private void restoreSerialIdValue() {
        String restoreSerialId = serialId;
        if (!fragSerial_ID.isEmpty()) {
            restoreSerialId = fragSerial_ID;
        }
        mFrgSerialSearch.setSerialIdText(restoreSerialId);
    }

    @Override
    protected void processError_http() {
//        super.processError_http();
        progressDialog.dismiss();
        if (wsProcess.equalsIgnoreCase(WS_Product_Serial_Structure.class.getName())) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_serial_structure_error_ttl"),
                    hmAux_Trans.get("alert_serial_structure_error_msg"),
                    null,
                    0
            );
        } else {
            ToolBox_Con.setBooleanPreference(getApplicationContext(), ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW, true);
            mPresenter.offlineSerialSearch();
        }
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        progressDialog.dismiss();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
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
                    mBundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005)
            );
            if (isFinalizePlusNewProcess()) {
                buildBundleFOrNforFinishPlusNew(bundle);
            }
            bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, ToolBox_Inf.getMyActionFilterParam(mBundle));
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }

    private void buildBundleFOrNforFinishPlusNew(Bundle bundle) {
        bundle.putString(MD_ProductDao.PRODUCT_CODE, productCode);
        bundle.putString(MD_ProductDao.PRODUCT_DESC, productDesc);
        bundle.putString(MD_ProductDao.PRODUCT_ID, productId);
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, serialId);
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, customFormType);
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, customFormCode);
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, customFormVersion);
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, customFormCodeDesc);
        bundle.putInt(GE_Custom_FormDao.IS_SO, isSoForm);
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        //
        ToolBox_Inf.executeLogoffAndUpdateSoftware(context);
    }
}