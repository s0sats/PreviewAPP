package com.namoadigital.prj001.ui.act053;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Address_Suggestion;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Add;
import com.namoadigital.prj001.service.WS_IO_Outbound_Item_Add;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act061.Act061_Main;
import com.namoadigital.prj001.ui.act062.Act062_Main;
import com.namoadigital.prj001.ui.act067.Act067_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_edit.Frg_Serial_Edit;

import java.util.ArrayList;
import java.util.List;

public class Act053_Main extends Base_Activity implements Act053_Main_Contract.I_View {

    private Act053_Main_Presenter mPresenter;
    private Bundle bundle;
    private String requesting_act;
    private Long bundle_product_code;
    private String bundle_serial_id;
    private boolean bundle_new_serial = false;
    private MD_Product mdProduct;
    private MD_Product_Serial mdProductSerial;
    private String wsProcess;
    private FragmentManager fm;
    private Frg_Serial_Edit frgSerialEdit;
    private LinearLayout contentMain;
    private String mResource_Code_Frag;
    private HMAux hmAux_Trans_Frag;
    private String ioProcess;
    private boolean isIoProcess = false;
    private String ioPrefix;
    private String ioCode;
    private boolean itemSavedOk = true;
    private boolean avoid_serial_hide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act053_main);
        //
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
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT053
        );
        //
        mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_EDIT
        );
        //
        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        //
        transList.add("act053_title");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_save_serial_return_ttl");
        transList.add("alert_no_serial_return_msg");
        transList.add("alert_save_serial_ok_msg");
        transList.add("alert_save_serial_error_msg");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("dialog_result_product_lbl");
        transList.add("dialog_result_serial_lbl");
        transList.add("dialog_result_msg_lbl");
        transList.add("dialog_results_ttl");
        transList.add("btn_create");
        //
        transList.add("alert_address_suggestion_fails_ttl");
        transList.add("alert_address_suggestion_fails_msg");
        transList.add("item_lbl");
        transList.add("serial_lbl");
        transList.add("inbound_lbl");
        transList.add("outbound_lbl");
        transList.add("message_lbl");
        transList.add("alert_add_item_empty_return_ttl");
        transList.add("alert_add_item_empty_return_msg");
        transList.add("alert_add_item_error_on_return_ttl");
        transList.add("alert_add_item_error_on_return_msg");
        transList.add("alert_add_item_results_ttl");
        transList.add("alert_leave_add_item_ttl");
        transList.add("alert_not_save_item_will_be_lost_msg");
        transList.add("alert_error_item_save_ttl");
        transList.add("alert_error_item_save_msg");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
        //
        hmAux_Trans_Frag = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code_Frag,
                ToolBox_Con.getPreference_Translate_Code(context),
                //transListFrag
                Frg_Serial_Edit.getFragTranslationsVars()
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        mPresenter = new Act053_Main_Presenter(
                context,
                this,
                hmAux_Trans,
                bundle_product_code
        );
        //
        contentMain = findViewById(R.id.content_main);

        mPresenter.getProductInfo(bundle_product_code);
        //
        initFrag();
        //
        checkForHideSerialFlow(false);

    }

    /**
     *  BARRIONUEVO 23-04-2020
     *  Trata a visibilidade do fragmento antes de carregar as informacoes
     *  É chamado na interface onFragIsReady para performar o click e verificar a possibilidade
     *  de avançar.
     */
    private void checkForHideSerialFlow(boolean performClick) {
        if(ToolBox_Inf.hasForceNotShowSerialInfo(context) && !bundle_new_serial && !avoid_serial_hide) {
            contentMain.setVisibility(View.INVISIBLE);
            if (performClick) {
                if (frgSerialEdit.getBtn_action() != null) {
                    frgSerialEdit.getBtn_action().performClick();
                }
            }
        }else{
            contentMain.setVisibility(View.VISIBLE);
        }
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle_product_code = Long.parseLong(bundle.getString(MD_ProductDao.PRODUCT_CODE));
            bundle_serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            bundle_new_serial = bundle.getBoolean(Constant.MAIN_SERIAL_CREATION, false);
            requesting_act = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT051);
            avoid_serial_hide = bundle.getBoolean(ConstantBaseApp.AVOID_SERIAL_HIDE, false);
            //
            if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {
                mdProductSerial = (MD_Product_Serial) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            } else {
                mdProductSerial = new MD_Product_Serial();
            }
            //DADOS INBOUND / OUTBOUND ITEM
            ioProcess = bundle.getString(Constant.HMAUX_PROCESS_KEY, "");
            isIoProcess = ioProcess.equals(ConstantBaseApp.IO_INBOUND) || ioProcess.equals(ConstantBaseApp.IO_OUTBOUND) ||ioProcess.equals(ConstantBaseApp.IO_SERIAL_EDIT) ;
            //
            ioPrefix = bundle.getString(ConstantBaseApp.HMAUX_PREFIX_KEY,"-1");
            ioCode = bundle.getString(ConstantBaseApp.HMAUX_CODE_KEY,"-1");
        } else {
            bundle_product_code = 0L;
            ioProcess = "";
            isIoProcess = false;
            ioPrefix ="-1";
            ioCode = "-1";
            avoid_serial_hide =false;
        }
    }

    private void initFrag() {
        frgSerialEdit = (Frg_Serial_Edit) fm.findFragmentById(R.id.act053_frg_serial_edit);
        frgSerialEdit.setmModule_Code(mModule_Code);
        frgSerialEdit.setmResource_Code(mResource_Code);
        frgSerialEdit.setHmAux_Trans(hmAux_Trans_Frag);
        frgSerialEdit.setNew_serial(bundle_new_serial);
        controls_sta.addAll(frgSerialEdit.getControlsSta());
        frgSerialEdit.setMdProduct(mdProduct);
        frgSerialEdit.setMdProductSerial(mdProductSerial);
        frgSerialEdit.setBtnActionLabel(hmAux_Trans.get("btn_create"));
        frgSerialEdit.setViewMode(Frg_Serial_Edit.VIEW_FULL_EDIT);
        frgSerialEdit.setShowCategorySegmentoInfo(false);
        frgSerialEdit.setIOProcess(isIoProcess);
        if(isIoProcess && !ioProcess.equals(ConstantBaseApp.IO_SERIAL_EDIT)){
            frgSerialEdit.setForceSaveAgain(true);
        }

        //
        frgSerialEdit.setDelegate(new Frg_Serial_Edit.I_Frg_Serial_Edit() {

            @Override
            public void onCheckButtonClick(long product_code, String product_id, String serial_id, String tracking) {
                if(ToolBox_Con.isOnline(context)) {
                    mPresenter.executeSerialSearch(
                            product_code,
                            serial_id
                    );
                }else{
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }

            @Override
            public void onSaveNoChangesClick(MD_Product_Serial md_product_serial, boolean serial_id_changes) {
                //Atualiza obj da tela com o do frag.
                mdProductSerial = md_product_serial;
                //Salva os dados do serial no banco local.
                mPresenter.updateSerialData(mdProductSerial);
                //
                checkFlow();
            }

            @Override
            public void onSaveWithChangesClick(MD_Product_Serial mdProductSerialFrag, boolean serial_id_changes) {
                mPresenter.updateSerialData(mdProductSerialFrag);
                //
                mdProductSerial = mdProductSerialFrag;
                //
                if (ToolBox_Con.isOnline(context)) {
                    mPresenter.executeSerialSave();
                } else {
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }

            @Override
            public void onTrackingSearchClick(long product_code, long serial_code, String tracking, String site_code) {
                mPresenter.executeTrackingSearch(product_code, serial_code, tracking, site_code);
            }

            @Override
            public void onProductOrSerialNull() {
                mPresenter.onBackPressedClicked(requesting_act);
            }

            @Override
            public void onFragIsReady() {
               //
                checkForHideSerialFlow(true);
            }

            @Override
            public void onAbortFragLoad() {
                mPresenter.onBackPressedClicked(requesting_act);
            }

            @Override
            public void onAddOrRemoveControl(MKEditTextNM mket_control, boolean add) {
                if(add) {
                    controls_sta.add(mket_control);
                }else{
                    controls_sta.remove(mket_control);
                }
            }

            @Override
            public void onAddressSuggestionRequired(String site_code, long product_code) {
                if(ToolBox_Con.isOnline(context)) {
                    mPresenter.executeAddressSuggestion(site_code, product_code);
                }

            }
            @Override
            public void onHideSerialInfoErrorListner() {
                if(contentMain.getVisibility() == View.INVISIBLE){
                    mPresenter.onBackPressedClicked(requesting_act);
                }
            }
        });
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT053;
        mAct_Title = Constant.ACT053 + "_" + "title";
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
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void setProductValues(MD_Product md_product) {
        mdProduct = md_product;
    }

    @Override
    public void showAlertDialog(String title, String msg) {
        ToolBox.alertMSG(
                context,
                title,
                msg,
                null,
                0
        );
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
    public MD_Product_Serial getProductSerial() {
        return mdProductSerial;
    }

    @Override
    public String getIoPrefix() {
        return ioPrefix;
    }

    @Override
    public String getIoCode() {
        return ioCode;
    }
    @Override
    public boolean isItemSavedOk() {
        return itemSavedOk;
    }

    @Override
    public void setItemSavedOk(boolean itemSavedOk) {
        this.itemSavedOk = itemSavedOk;
    }

    @Override
    public void showSingleResultMsg(String ttl, String msg, final boolean saveOk) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(saveOk) {
                            if(frgSerialEdit != null){
                                frgSerialEdit.setForceSaveAgain(false);
                            }
                            checkFlow();
                            //
                            dialog.dismiss();
                        }else{

                        }

                    }
                },
                0
        );
    }

    private void checkFlow(){
        if( isIoProcess
            && (ioProcess.equals(ConstantBaseApp.IO_INBOUND) || ioProcess.equals(ConstantBaseApp.IO_OUTBOUND))
        ) {
            mPresenter.defineWsRetFlow(ioProcess,requesting_act);
        }else {
            mPresenter.defineFlow(requesting_act);
        }
    }
    @Override
    public void showSerialResults(ArrayList<HMAux> returnList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);
        //
        tv_title.setVisibility(View.GONE);
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        hmAux_Trans.put(Generic_Results_Adapter.LABEL_ITEM_1, hmAux_Trans.get("dialog_result_product_lbl"));
        hmAux_Trans.put(Generic_Results_Adapter.LABEL_ITEM_2, hmAux_Trans.get("dialog_result_serial_lbl"));
        hmAux_Trans.put(Generic_Results_Adapter.LABEL_ITEM_3, hmAux_Trans.get("dialog_result_msg_lbl"));

        //
        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        returnList,
                        Generic_Results_Adapter.CONFIG_3_ITENS,
                        hmAux_Trans
                )
        );

        builder.setTitle(hmAux_Trans.get("dialog_results_ttl"));
        builder.setView(view);
        //builder.setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"),null);
        builder.setCancelable(false);
        //
        boolean hasSerialReturnedOk = false;
        for(HMAux aux: returnList){
            if( aux.hasConsistentValue(MD_Product_SerialDao.PRODUCT_CODE)
                    && aux.hasConsistentValue(Generic_Results_Adapter.VALUE_ITEM_2)
                    && aux.get(MD_Product_SerialDao.PRODUCT_CODE).equals(String.valueOf(mdProductSerial.getProduct_code()))
                    && aux.get(Generic_Results_Adapter.VALUE_ITEM_2).equals(mdProductSerial.getSerial_id())
            ){
                hasSerialReturnedOk = true;
            }
        }
        //
        final AlertDialog show = builder.show();
        //
        final boolean finalHasSerialReturnedOk = hasSerialReturnedOk;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                //
                if(finalHasSerialReturnedOk) {
                    //
                    if(frgSerialEdit != null){
                        frgSerialEdit.setForceSaveAgain(false);
                    }
                    //
                    checkFlow();
                }else{
                    //Se retorno do serial for false, não prosseguir.
                    //Também não é necessario exibir alert, pois o usr já foi avisado no dialog.
                }
            }
        });

    }

    @Override
    public void refreshUI() {
        if (frgSerialEdit != null) {
            frgSerialEdit.refreshUi();
        }
    }

    @Override
    public void reApplySerialIdToFrag() {
        frgSerialEdit.reApplySerialId();
    }

    @Override
    public void applyReceivedSerialToFrag(MD_Product_Serial serial_returned) {
        mdProductSerial = serial_returned;
        frgSerialEdit.applyReceivedSerial(serial_returned);
    }

    @Override
    public void updateProductSerialValues(MD_Product_Serial mdProductSerial) {
        this.mdProductSerial = mdProductSerial;
        //
        if (frgSerialEdit != null) {
            frgSerialEdit.setMdProductSerial(this.mdProductSerial);
        }
    }

    @Override
    public void setMdProductSerial(MD_Product_Serial mdProductSerial) {

    }

    @Override
    public void reportAddressSuggestion(Integer zone_code, String zone_id, String zone_desc, Integer local_code, String local_id) {
        if (frgSerialEdit != null) {
            frgSerialEdit.updateAddressSuggestion(
                zone_code,
                zone_id,
                zone_desc,
                local_code,
                local_id
            );
        }
    }

    @Override
    public String getIoProcess() {
        return ioProcess;
    }

    @Override
    public void showResultDialog(ArrayList<HMAux> resultList, final boolean itemAdd) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);
        //trad
        tv_title.setText(hmAux_Trans.get("alert_add_item_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        lv_results.setAdapter(
            new Generic_Results_Adapter(
                context,
                resultList,
                Generic_Results_Adapter.CONFIG_3_ITENS_NEW,
                hmAux_Trans
            )
        );
        //
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //
                if (itemAdd) {
                    itemSavedOk = true;
                    //
                    onBackPressed();
                }else{
                    if(ToolBox_Inf.hasForceNotShowSerialInfo(context)) {
                        callAct062();
                    }
                }

            }
        });
    }

    private void callAct062() {
        Intent mIntent = new Intent(context, Act062_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(bundle == null) {
            bundle = new Bundle();
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,requesting_act);
            bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, ioProcess);
            bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY,ioPrefix);
            bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY,ioCode);
        }
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct061(Bundle bundle) {
        Intent mIntent = new Intent(context, Act061_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,requesting_act);
        bundle.putString(Act061_Main.FIRST_FRAG_TO_LOAD,Act061_Main.INBOUND_FRAG_ITEM);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct067(Bundle bundle) {
        Intent mIntent = new Intent(context, Act067_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,requesting_act);
        bundle.putString(Act061_Main.FIRST_FRAG_TO_LOAD, Act067_Main.OUTBOUND_FRAG_ITEM);
        //
        mIntent.putExtras(bundle);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked(requesting_act);
    }

    /**
     * Alguns WS mais antigos executam a chamada dessa assinatura do metodo
     * processCloseACT e aqui serão "encaminhados" para a segunda assinatura,
     * consolidando as tratativas em um unico metodo.
     *
     * No caso dessa act, o WS_Serial_Search retorna os dados aqui.
     * @param mLink
     * @param mRequired
     */
    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //
        processCloseACT(mLink,mRequired,new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if(wsProcess.equals(WS_Serial_Tracking_Search.class.getName())){
            frgSerialEdit.processTrackingResult(hmAux);
        } else if(wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())){
            mPresenter.extractSearchResult(mLink);
        }else if(wsProcess.equalsIgnoreCase(WS_Serial_Save.class.getName())){
            frgSerialEdit.setNew_serial(false);
            if (hmAux.size() > 0) {
                mPresenter.processSerialSaveResult(mdProductSerial.getProduct_code(), mdProductSerial.getSerial_id(), hmAux);
            } else {
                showSingleResultMsg(
                        hmAux_Trans.get("alert_save_serial_return_ttl"),
                        hmAux_Trans.get("alert_no_serial_return_msg"),
                    false);
            }
        }else if(wsProcess.equals(WS_IO_Address_Suggestion.class.getName())){
            mPresenter.processAddresSuggestionResult(mLink);
        }else if(wsProcess.equals(WS_IO_Inbound_Item_Add.class.getName())){
            mPresenter.processInboundItemAdd(mLink);
            //onBackPressed();
        }else if(wsProcess.equals(WS_IO_Outbound_Item_Add.class.getName())){
            mPresenter.processOutboundItemAdd(mLink);
            //onBackPressed();
        }
        //
        progressDialog.dismiss();
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
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
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        if(contentMain.getVisibility() == View.INVISIBLE){
            mPresenter.onBackPressedClicked(requesting_act);
        }
        progressDialog.dismiss();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        if(contentMain.getVisibility() == View.INVISIBLE){
            mPresenter.onBackPressedClicked(requesting_act);
        }
        //
        disableProgressDialog();
    }
}
