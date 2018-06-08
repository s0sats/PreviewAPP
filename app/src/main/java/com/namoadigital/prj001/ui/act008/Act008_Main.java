package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act009.Act009_Main;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Edit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act008_Main extends Base_Activity implements Act008_Main_View {

    public static final String WS_PROCESS_SYNC = "ws_process_sync";
    public static final String WS_PROCESS_SERIAL = "ws_process_serial";

    private Act008_Main_Presenter mPresenter;

    private Bundle bundle;
    private String requesting_process;
    private Long bundle_product_code;
    private String bundle_serial_id;
    private boolean bundle_new_serial = false;
    private MD_Product mdProduct;
    private MD_Product_Serial mdProductSerial;
    private String ws_process;
    private FragmentManager fm;
    private Frg_Serial_Edit frgSerialEdit;
    private String mResource_Code_Frag;
    private HMAux hmAux_Trans_Frag;

    private int serial_required;
    private int serial_allow_new;
    //agendamento
    private boolean isSchedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act008_main);

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
                Constant.ACT008
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
        transList.add("alert_no_connection_title");
        transList.add("alert_no_connection_msg");
        transList.add("alert_offine_mode_title");
        transList.add("alert_offine_mode_msg");
        transList.add("alert_start_sync_title");
        transList.add("alert_start_sync_msg");
        transList.add("alert_start_serial_title");
        transList.add("alert_start_serial_msg");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("alert_no_serial_typed_title");
        transList.add("alert_no_serial_typed_msg");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        transList.add("product_ttl");
        transList.add("mket_search_hint");
        transList.add("product_label");
        transList.add("product_id_label");
        transList.add("alert_no_form_ttl");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_save_serial_return_ttl");
        transList.add("alert_no_serial_return_msg");
        transList.add("alert_save_serial_error_msg");
        transList.add("alert_save_serial_ok_msg");
        transList.add("progress_serial_save_ttl");
        transList.add("progress_serial_save_msg");
        transList.add("dialog_result_product_lbl");
        transList.add("dialog_result_serial_lbl");
        transList.add("dialog_result_msg_lbl");
        transList.add("alert_no_form_found_ttl");
        transList.add("alert_no_form_found_msg");
        transList.add("alert_no_form_lbl");
        transList.add("alert_no_form_for_product_msg");
        transList.add("alert_no_form_for_operation_msg");
        transList.add("alert_no_form_for_site_msg");
        //Novas traduções
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_site_restriction_violation_msg");


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
        //Variavel q identifica se dados do produto são chamados do master data ou não.
        isSchedule = false;
        //
        recoverIntentsInfo();
        //
        mPresenter =  new Act008_Main_Presenter_Impl(
                context,
                this,
                new Sync_ChecklistDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                        ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                bundle_product_code,
                hmAux_Trans,
                new GE_Custom_Form_OperationDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                isSchedule,
                requesting_process,
                new MD_Product_SerialDao(context),
                new MD_Product_Serial_TrackingDao(context)
                );
        //
        mPresenter.getProductInfo(bundle);
        //
        initFrag();

    }

    private void initFrag() {
        frgSerialEdit = (Frg_Serial_Edit) fm.findFragmentById(R.id.act008_frg_serial_edit);
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
        //
        frgSerialEdit.setDelegate(new Frg_Serial_Edit.I_Frg_Serial_Edit() {

            @Override
            public void onCheckButtonClick(String product_id, String serial_id, String tracking) {
                if(ToolBox_Con.isOnline(context)) {
                    mPresenter.executeSerialSearch(
                            product_id,
                            serial_id
                    );
                }else{
                    mPresenter.searchLocalSerial(mdProduct.getProduct_code(),serial_id);
                }
            }

            @Override
            public void onSaveNoChangesClick(MD_Product_Serial md_product_serial, boolean serial_id_changes) {
                //Atualiza obj da tela com o do frag.
                mdProductSerial = md_product_serial;
                //Salva os dados do serial no banco local.
                mPresenter.updateSerialData(mdProductSerial);
                //
                mPresenter.checkFlow();
            }

            @Override
            public void onSaveWithChangesClick(MD_Product_Serial mdProductSerialFrag, boolean serial_id_changes) {
                mPresenter.updateSerialData(mdProductSerialFrag);
                //
                mdProductSerial = mdProductSerialFrag;
                //
                if(ToolBox_Con.isOnline(context)) {
                    mPresenter.executeSerialSave();
                }else{
                    //ToolBox_Inf.showNoConnectionDialog(context);
                    /*mPresenter.validateSerial(
                            mdProductSerial.getSerial_id(),
                            mdProduct.getRequire_serial(),
                            mdProduct.getAllow_new_serial_cl()
                    );*/
                    mPresenter.checkFlow();
                }
            }

            @Override
            public void onTrackingSearchClick(long product_code, long serial_code, String tracking, String site_code) {
                mPresenter.executeTrackingSearch(product_code,serial_code,tracking,site_code);
            }
        });
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            //Chamada vinda da act017
            if(bundle.containsKey(Act016_Main.ACT016_SELECTED_DATE)){
                isSchedule = true;
            }//
            bundle_product_code = Long.parseLong(bundle.getString(Constant.MAIN_PRODUCT_CODE));
            bundle_serial_id = bundle.getString(Constant.MAIN_SERIAL_ID, "");
            bundle_new_serial = bundle.getBoolean(Constant.MAIN_SERIAL_CREATION, false);
            requesting_process = bundle.getString(Constant.MAIN_REQUESTING_ACT,Constant.ACT005);
            //
            if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {
                mdProductSerial = (MD_Product_Serial) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            } else {
                mdProductSerial = new MD_Product_Serial();
            }
        } else {
            bundle_product_code = 0L;
        }
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.ws_process = wsProcess;
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT008;
        mAct_Title = Constant.ACT008 + "_" + "title";
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
    public void setProductValues(MD_Product md_product) {
        mdProduct = md_product;
    }

//    @Override
//    public void callAct007(Context context) {
//        Intent mIntent =  new Intent(context, Act007_Main.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //Remove produto do bundle
//        bundle.remove(Constant.ACT007_PRODUCT_CODE);
//
//        mIntent.putExtras(bundle);
//
//        startActivity(mIntent);
//        finish();
//
//    }

//    @Override
//    public void fieldFocus() {
//        mket_serial_id.requestFocus();
//    }

    @Override
    public void showAlertDialog(String title, String msg) {

        ToolBox.alertMSG(
                Act008_Main.this,
                title,
                msg,
                null,
                0
        );
    }

    @Override
    public void continueOfflineV2(boolean serial_offline) {
        String title = "";
        String msg = "";
        DialogInterface.OnClickListener listener = null;

        if(serial_offline){
            mPresenter.defineFlow();
        }else {
            if (mdProduct.getAllow_new_serial_cl()  == 0
                   // && ToolBox_Inf.removeAllLineBreaks(mket_serial_id.getText().toString().trim()).length() > 0
                    ) {
                title = hmAux_Trans.get("alert_no_connection_title"); //"Connection";
                msg = hmAux_Trans.get("alert_no_connection_msg"); // "No connection has been found!\nThis mdProduct requires connection to proceed.\nTry again later.";
            } else {
                title = hmAux_Trans.get("alert_offine_mode_title"); //"Continue in offline mode?";
                msg = hmAux_Trans.get("alert_offine_mode_msg"); //"No connection has been found!\nDo you want continue without check the Serial id ?! ";

                listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Aplicado 09/05/2018
                        //Caso seja criação de serial, usa serial_rule para validá-lo
                       /* if(mket_serial_id.isValid()) {
                            mPresenter.defineFlow();
                        }else{
                            showAlertDialog(
                                    hmAux_Trans.get("alert_serial_invalid_ttl"),
                                    mket_serial_id.getmErrorMSG()
                            );
                        }*/
                    }
                };
            }
            //
            ToolBox.alertMSG(
                    Act008_Main.this,
                    title,
                    msg,
                    listener,
                    1
            );
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
    public void showSingleResultMsg(String ttl, String msg) {
        //
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.checkFlow();
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
        //builder.setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"),null);
        builder.setCancelable(false);
        //
        final AlertDialog show = builder.show();
        //
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                //
                //mPresenter.executeSoDownload(mdProductSerial.getProduct_code(), mdProductSerial.getSerial_id());
            }
        });

    }

    @Override
    public void refreshUI() {
        if(frgSerialEdit != null){
            frgSerialEdit.refreshUi();
        }
    }

    //Trata retorno de serial OK
    @Override
    protected void processSerialOk() {
        super.processSerialOk();
        //
        disableProgressDialog();
        //
        mPresenter.defineFlow();
    }

    @Override
    public void callAct009(Context context) {
        boolean formXProductExist = ToolBox_Inf.checkFormXProductExists(context,ToolBox_Con.getPreference_Customer_Code(context),mdProduct.getProduct_code());
        boolean formXOperationExists = ToolBox_Inf.checkFormXOperationExists(context,ToolBox_Con.getPreference_Customer_Code(context),ToolBox_Con.getPreference_Operation_Code(context));
        boolean formXSiteExists = ToolBox_Inf.checkFormXSiteExists(
                context,
                ToolBox_Con.getPreference_Customer_Code(context),
                mdProductSerial.getSite_code() != null ? String.valueOf(mdProductSerial.getSite_code())  : ToolBox_Con.getPreference_Site_Code(context)
        );
        boolean producSiteRestXSite = false;
        if( mdProduct.getSite_restriction() == 1){
            if(mdProductSerial.getSite_code() != null
            && ToolBox_Con.getPreference_Site_Code(context).equals(String.valueOf(mdProductSerial.getSite_code()))
            ){
                producSiteRestXSite = true;
            }
        }else{
            producSiteRestXSite = true;
        }
        //Se existir form para o produto,site e operação e a regra de restrição de site respeitada,
        //avança para o form
        if(formXProductExist && formXOperationExists && formXSiteExists && producSiteRestXSite){
            Intent mIntent =  new Intent(context, Act009_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bundle.putString(Constant.ACT020_PRODUCT_CODE, String.valueOf(mdProduct.getProduct_code()));
            bundle.putString(Constant.ACT008_SERIAL_ID,ToolBox_Inf.removeAllLineBreaks(mdProductSerial.getSerial_id()));
            bundle.putString(Constant.ACT008_PRODUCT_DESC, mdProduct.getProduct_desc().trim());
            bundle.putString(Constant.ACT008_PRODUCT_ID, mdProduct.getProduct_id().trim());
            bundle.putString(Constant.ACT008_SITE_CODE, mdProductSerial.getSite_code() != null ? String.valueOf(mdProductSerial.getSite_code())  : ToolBox_Con.getPreference_Site_Code(context));
            //
            mIntent.putExtras(bundle);
            //
            startActivity(mIntent);
            finish();
        }else{
            String msg = hmAux_Trans.get("alert_no_form_lbl");
            msg += "\n";
            msg = !formXProductExist ? msg + hmAux_Trans.get("alert_no_form_for_product_msg") + "\n" : msg;
            msg = !formXOperationExists ? msg + hmAux_Trans.get("alert_no_form_for_operation_msg") + "\n" : msg;
            msg = !formXSiteExists ? msg + hmAux_Trans.get("alert_no_form_for_site_msg") + "\n" : msg;
            msg = !producSiteRestXSite ? msg + hmAux_Trans.get("alert_site_restriction_violation_msg") + "\n" : msg;

            showAlertDialog(
                    hmAux_Trans.get("alert_no_form_ttl"),
                    msg
            );

        }
    }

    @Override
    public void callAct011(Context context) {
        Intent mIntent =  new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       // bundle.putString(Constant.ACT008_SERIAL_ID,ToolBox_Inf.removeAllLineBreaks(mket_serial_id.getText().toString().trim()));

        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct017(Context context) {
        Intent mIntent =  new Intent(context, Act017_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Remove dados não necessarios para act017
        bundle.remove(Constant.ACT007_PRODUCT_CODE);
        bundle.remove(Constant.ACT008_PRODUCT_DESC);
        bundle.remove(Constant.ACT008_SERIAL_ID);
        bundle.remove(Constant.ACT009_CUSTOM_FORM_TYPE);
        bundle.remove(Constant.ACT009_CUSTOM_FORM_TYPE_DESC);
        bundle.remove(Constant.ACT010_CUSTOM_FORM_CODE);
        bundle.remove(Constant.ACT010_CUSTOM_FORM_VERSION);
        bundle.remove(Constant.ACT010_CUSTOM_FORM_CODE_DESC);
        bundle.remove(Constant.ACT013_CUSTOM_FORM_DATA);

        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct006(Context context) {
        Intent mIntent =  new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if(ws_process.equals(WS_Serial_Search.class.getName())){
            disableProgressDialog();
        }else if(ws_process.equals(WS_Serial_Save.class.getName())){
            frgSerialEdit.setNew_serial(false);
            frgSerialEdit.refreshUi();
            if (hmAux.size() > 0) {
                mPresenter.processSerialSaveResult(mdProductSerial.getProduct_code(), mdProductSerial.getSerial_id(), hmAux);
            } else {
                showSingleResultMsg(
                        hmAux_Trans.get("alert_save_serial_return_ttl"),
                        hmAux_Trans.get("alert_no_serial_return_msg")
                );
            }
        } else if(ws_process.equals(WS_Serial_Tracking_Search.class.getName())) {
            frgSerialEdit.processTrackingResult(hmAux);
        }
        //
        disableProgressDialog();
    }

    //Trata retorno do Serial
    @Override
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);
        //
        disableProgressDialog();
         if(ws_process.equalsIgnoreCase(WS_Serial_Search.class.getName())){
             mPresenter.extractSearchResult(result);
            //
            disableProgressDialog();
        }else {
             //Atualiza data na tabela de produtos loca
             mPresenter.updateSyncChecklist();
             // mPresenter.proceedToSerialProcess(ToolBox_Inf.removeAllLineBreaks(mket_serial_id.getText().toString().trim()) , serial_required);
         }

    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        disableProgressDialog();
    }

    @Override
    public void reApplySerialIdToFrag(){
        frgSerialEdit.reApplySerialId();
    }
    @Override
    public void applyReceivedSerialToFrag(MD_Product_Serial serial_returned){
        mdProductSerial = serial_returned;
        frgSerialEdit.applyReceivedSerial(serial_returned);
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
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
