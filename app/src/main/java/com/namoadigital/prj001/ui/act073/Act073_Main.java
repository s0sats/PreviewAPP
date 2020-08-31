package com.namoadigital.prj001.ui.act073;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Ticket_Search;
import com.namoadigital.prj001.ui.act068.Act068_Main;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.ui.act076.Act076_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_edit.Frg_Serial_Edit;

import java.util.ArrayList;
import java.util.List;

public class Act073_Main extends Base_Activity_Frag implements Act073_Main_Contract.I_View {

    private Act073_Main_Presenter mPresenter;
    private Bundle bundle;
    private String requesting_process;
    private long bundle_product_code;
    private String bundle_serial_id;
    private MD_Product mdProduct;
    private MD_Product_Serial mdProductSerial;
    private String wsProcess;
    private FragmentManager fm;
    private Frg_Serial_Edit frgSerialEdit;
    private String mResource_Code_Frag;
    private HMAux hmAux_Trans_Frag;
    private boolean bundle_new_serial = false;
    private LinearLayout contentMain;
    //Variavel que inibe o pulo do fragmento para o caso do serial que necessita de alteração.
    private boolean hide_serial_info;
    private HMAux hmAuxQtyReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act073_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();

        initAction();

    }

    private void iniSetup() {
        //
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            Constant.ACT073
        );
        mResource_Code_Frag = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            Constant.FRG_SERIAL_EDIT
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act073_title");
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
        transList.add("dialog_serial_save_ttl");
        transList.add("dialog_serial_save_start");
        transList.add("dialog_result_product_lbl");
        transList.add("dialog_result_serial_lbl");
        transList.add("dialog_result_msg_lbl");
        transList.add("dialog_results_ttl");
        transList.add("btn_search");
        //
        transList.add("alert_no_ticket_found_ttl");
        transList.add("alert_no_ticket_found_msg");
        transList.add("alert_ticket_params_not_found_ttl");
        transList.add("alert_ticket_params_not_found_msg");
        transList.add("alert_invalid_ticket_return_ttl");
        transList.add("alert_invalid_ticket_return_msg");
        transList.add("dialog_download_ticket_ttl");
        transList.add("dialog_download_ticket_start");
        transList.add("dialog_search_ticket_ttl");
        transList.add("dialog_search_ticket_start");
        transList.add("progress_sync_ttl");
        transList.add("progress_sync_msg");

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
            Frg_Serial_Edit.getFragTranslationsVars()
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        mPresenter = new Act073_Main_Presenter(
            context,
            this,
            hmAux_Trans,
            bundle_product_code
        );
        //Layout principal que será "escondido" caso a opção seja por ocultar serial.
        contentMain = findViewById(R.id.content_main);
        //
        mPresenter.getProductInfo(bundle_product_code);
        //
        initFrag();
        //
        checkForHideSerialFlow(false);
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            requesting_process = bundle.getString(Constant.MAIN_REQUESTING_PROCESS, "");
            bundle_product_code = Long.parseLong(bundle.getString(MD_ProductDao.PRODUCT_CODE));
            bundle_serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            bundle_new_serial = bundle.getBoolean(Constant.MAIN_SERIAL_CREATION, false);
            hide_serial_info = bundle.getBoolean(ConstantBaseApp.PREFERENCE_HIDE_SERIAL_INFO, true);
            //
            if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {
                mdProductSerial = (MD_Product_Serial) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            } else {
                mdProductSerial = new MD_Product_Serial();
            }
        } else {
            bundle_product_code = 0L;
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }


    private void initFrag() {
        frgSerialEdit = (Frg_Serial_Edit) fm.findFragmentById(R.id.act073_frg_serial_edit);
        frgSerialEdit.setmModule_Code(mModule_Code);
        frgSerialEdit.setmResource_Code(mResource_Code);
        frgSerialEdit.setHmAux_Trans(hmAux_Trans_Frag);
        frgSerialEdit.setNew_serial(bundle_new_serial);
        controls_sta.addAll(frgSerialEdit.getControlsSta());
        frgSerialEdit.setMdProduct(mdProduct);
        frgSerialEdit.setMdProductSerial(mdProductSerial);
        frgSerialEdit.setBtnActionLabel(hmAux_Trans.get("btn_search"));
        frgSerialEdit.setViewMode(Frg_Serial_Edit.VIEW_FULL_EDIT);
        frgSerialEdit.setShowCategorySegmentoInfo(false);
        //Interfaces
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
                mPresenter.executeTicketDownload(
                    mdProductSerial.getProduct_code(),
                    mdProductSerial.getSerial_code(),
                    mdProductSerial.getSerial_id()
                );
            }

            @Override
            public void onSaveWithChangesClick(MD_Product_Serial md_product_serial, boolean serial_id_changes) {
                saveWithChangesProcess(mdProductSerial);
            }

            @Override
            public void onTrackingSearchClick(long product_code, long serial_code, String tracking, String site_code) {
                mPresenter.executeTrackingSearch(product_code,serial_code,tracking,site_code);
            }

            @Override
            public void onProductOrSerialNull() {
                mPresenter.onBackPressedClicked();
            }

            @Override
            public void onFragIsReady() {
                //Sem ação nesse caso
                checkForHideSerialFlow(true);
            }

            @Override
            public void onAbortFragLoad() {
                mPresenter.onBackPressedClicked();
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
                //sem ação nesse caso
            }
            @Override
            public void onHideSerialInfoErrorListner() {
                if(contentMain.getVisibility() == View.INVISIBLE){
                    mPresenter.onBackPressedClicked();
                }
            }
        });

    }

    private void saveWithChangesProcess(MD_Product_Serial mdProductSerial) {
        mPresenter.updateSerialData(mdProductSerial);
        //
        if(ToolBox_Con.isOnline(context)) {
            mPresenter.executeSerialSave();
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    private void checkForHideSerialFlow(boolean checkFlow) {
        if(!bundle_new_serial &&
            ToolBox_Inf.hasForceNotShowSerialInfo(context)) {
            contentMain.setVisibility(View.INVISIBLE);
            //
            if(checkFlow) {
//                mPresenter.checkFlow();
                if(frgSerialEdit.getBtn_action() != null){
                    frgSerialEdit.getBtn_action().performClick();
                }
            }
        }else{
            contentMain.setVisibility(View.VISIBLE);
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT073;
        mAct_Title = Constant.ACT073 + "_" + "title";
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

    private void initAction() {

    }

    @Override
    public void setProductValues(MD_Product md_product) {
        mdProduct = md_product;
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void showPD(String title, String msg) {
        if(progressDialog == null) {
            enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
            );
        }else{
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
            //
            if(!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    @Override
    public void showAlert(String ttl, String msg) {

        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(contentMain.getVisibility() == View.INVISIBLE) {
                            onBackPressed();
                        }else{
                            dialog.dismiss();
                        }
                    }
                },
                0
        );
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
                        //
                        mPresenter.executeTicketDownload(
                            mdProductSerial.getProduct_code(),
                            mdProductSerial.getSerial_code(),
                            mdProductSerial.getSerial_id()
                        );
                        //
                        dialog.dismiss();
                    }else{

                    }

                }
            },
            0
        );
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
                    mPresenter.executeTicketDownload(
                        mdProductSerial.getProduct_code(),
                        mdProductSerial.getSerial_code(),
                        mdProductSerial.getSerial_id()
                    );
                }else{
                    //Se retorno do serial for false, não prosseguir.
                    //Também não é necessario exibir alert, pois o usr já foi avisado no dialog.
                }
            }
        });
    }

    @Override
    public void refreshUI() {
        if(frgSerialEdit != null){
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
        //não faz nada
    }

    @Override
    public void reportAddressSuggestion(Integer zone_code, String zone_id, String zone_desc, Integer local_code, String local_id) {
        //Somente faz sentido no I/O
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }

    @Override
    public void callAct068() {
        Intent intent = new Intent(context, Act068_Main.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct069() {
        Intent intent = new Intent(context, Act069_Main.class);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT073);
        bundle.putLong(TK_TicketDao.OPEN_PRODUCT_CODE,mdProductSerial.getProduct_code());
        bundle.putLong(TK_TicketDao.OPEN_SERIAL_CODE,mdProductSerial.getSerial_code());
        //
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    @Override
    public void callAct076() {
        Intent intent = new Intent(context, Act076_Main.class);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT073);
        bundle.putLong(TK_TicketDao.OPEN_PRODUCT_CODE,mdProductSerial.getProduct_code());
        bundle.putLong(TK_TicketDao.OPEN_SERIAL_CODE,mdProductSerial.getSerial_code());
        //
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct070(Bundle buildAct070Bundle) {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.putExtras(buildAct070Bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if(wsProcess.equals(WS_Serial_Tracking_Search.class.getName())){
            frgSerialEdit.processTrackingResult(hmAux);
            disableProgressDialog();
        } else if(wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())){
            mPresenter.extractSearchResult(mLink);
            disableProgressDialog();
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
        } else if(wsProcess.equals(WS_TK_Ticket_Search.class.getName())){
            disableProgressDialog();
            wsProcess = "";
            if(mPresenter.verifyProductForForm()){
                hmAuxQtyReturn = hmAux;
            }else {
                mPresenter.processTicketDownload(hmAux);
            }
        }else if (wsProcess.equalsIgnoreCase(WS_Sync.class.getName())) {
            wsProcess = "";
            disableProgressDialog();
            mPresenter.processTicketDownload(hmAuxQtyReturn);
        }
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        if(contentMain.getVisibility() == View.INVISIBLE){
            mPresenter.onBackPressedClicked();
        }
        //
        disableProgressDialog();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        if(contentMain.getVisibility() == View.INVISIBLE){
            mPresenter.onBackPressedClicked();
        }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
