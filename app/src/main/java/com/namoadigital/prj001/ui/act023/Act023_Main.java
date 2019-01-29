package com.namoadigital.prj001.ui.act023;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act022.Act022_Main;
import com.namoadigital.prj001.ui.act025.Act025_Main;
import com.namoadigital.prj001.ui.act026.Act026_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.ui.act050.Act050_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Edit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 22/06/2017.
 */

public class Act023_Main extends Base_Activity_Frag implements Act023_Main_View {

    public static final String SITE_DESC_OWNER = "site_desc_owner";
    public static final String SO_FLOW_SEARCH_SO = "SO_FLOW_SEARCH_SO";
    public static final String SO_FLOW_NEW_SO = "SO_FLOW_NEW_SO";

    private Act023_Main_Presenter mPresenter;
    private Bundle bundle;
    private String requesting_process;
    private String bundle_product_code;
    private String bundle_serial_id;
    private MD_Product mdProduct;
    private MD_Product_Serial mdProductSerial;
    private String ws_process;
    private FragmentManager fm;
    private Frg_Serial_Edit frgSerialEdit;
    private String mResource_Code_Frag;
    private HMAux hmAux_Trans_Frag;
    private String soFlow = "";
    private boolean bundle_new_serial = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act023_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
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
                Constant.ACT023
        );
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
        List<String> transList = new ArrayList<String>();
        transList.add("act023_title");
        transList.add("alert_offine_mode_title");
        transList.add("alert_offine_mode_msg");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        transList.add("btn_create");
        transList.add("btn_so_search");
        transList.add("progress_so_search_ttl");
        transList.add("progress_so_search_msg");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("alert_no_so_found_ttl");
        transList.add("alert_no_so_found_msg");
        transList.add("alert_save_serial_error_ttl");
        transList.add("alert_save_serial_error_msg");
        transList.add("tracking_ttl");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_offline_data_not_saved_ttl");
        transList.add("alert_offline_data_not_saved_msg");
        transList.add("alert_save_serial_return_ttl");
        transList.add("alert_no_serial_return_msg");
        transList.add("alert_no_serial_return_msg");
        transList.add("alert_save_serial_error_msg");
        transList.add("alert_save_serial_ok_msg");
        //
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
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

    @Override
    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        mPresenter = new Act023_Main_Presenter_Impl(
                context,
                this,
                requesting_process,
                bundle,
                hmAux_Trans,
                new MD_ProductDao(context),
                bundle_product_code,
                new MD_Product_SerialDao(context),
                new MD_Product_Serial_TrackingDao(context)
        );
        //
        mPresenter.getProductInfo();
        //
        initFrag();
    }

    private void initFrag() {
        frgSerialEdit = (Frg_Serial_Edit) fm.findFragmentById(R.id.act023_frg_serial_edit);
        frgSerialEdit.setmModule_Code(mModule_Code);
        frgSerialEdit.setmResource_Code(mResource_Code);
        frgSerialEdit.setHmAux_Trans(hmAux_Trans_Frag);
        frgSerialEdit.setNew_serial(bundle_new_serial);
        controls_sta.addAll(frgSerialEdit.getControlsSta());
        frgSerialEdit.setMdProduct(mdProduct);
        frgSerialEdit.setMdProductSerial(mdProductSerial);
        frgSerialEdit.setBtnActionLabel(hmAux_Trans.get("btn_so_search"));
        //LUCHE 25/01/2019
        //Agora o fragmento de serial só usará o layout full com alterações.
        //frgSerialEdit.setViewMode(Frg_Serial_Edit.VIEW_SO_EDIT);
        //frgSerialEdit.setShowCategorySegmentoInfo(true);
        frgSerialEdit.setViewMode(Frg_Serial_Edit.VIEW_FULL_EDIT);
        frgSerialEdit.setShowCategorySegmentoInfo(false);
        frgSerialEdit.includeNewOsButton(true);
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
                //Salva os dados do serial no banco local.
                mPresenter.updateSerialData(mdProductSerial);
                //
                mPresenter.executeSoDownload(mdProduct.getProduct_code(),mdProductSerial.getSerial_id());
            }

            @Override
            public void onSaveWithChangesClick(MD_Product_Serial mdProductSerial, boolean serial_id_changes) {
                //seta var que define o fluxo apos o save do serial.
                soFlow = SO_FLOW_SEARCH_SO;
                //
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

            }

            @Override
            public void onAbortFragLoad()  {
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
        });
        //
        frgSerialEdit.setDelegateOS(new Frg_Serial_Edit.I_Frg_Serial_Edit_New_Os() {
            @Override
            public void onNewOsClick(MD_Product_Serial mdProductSerial, boolean serialChanged) {
                if(serialChanged) {
                    //seta var que define o fluxo apos o save do serial.
                    soFlow = SO_FLOW_NEW_SO;
                    saveWithChangesProcess(mdProductSerial);
                }else{
                    mPresenter.updateSerialData(mdProductSerial);
                    //
                    callAct050(context);
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

    private void recoverIntentsInfo() {

        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_REQUESTING_PROCESS)) {
                requesting_process = bundle.getString(Constant.MAIN_REQUESTING_PROCESS, "");
                bundle_product_code = bundle.getString(MD_ProductDao.PRODUCT_CODE, "0");
                bundle_serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
                bundle_new_serial = bundle.getBoolean(Constant.MAIN_SERIAL_CREATION, false);
                //
                if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {
                    mdProductSerial = (MD_Product_Serial) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
                } else {
                    mdProductSerial = new MD_Product_Serial();
                }

            } else {
                ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
            }

        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }

    @Override
    public void setProductValues(MD_Product md_product) {
        mdProduct = md_product;
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT023;
        mAct_Title = Constant.ACT023 + "_" + "title";
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
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
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
    public void showSingleResultMsg(String ttl, String msg, final boolean returnOk) {
        //
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(returnOk) {
                            if (soFlow.equals(SO_FLOW_SEARCH_SO)) {
                                mPresenter.executeSoDownload(mdProductSerial.getProduct_code(), mdProductSerial.getSerial_id());
                            } else {
                                callAct050(context);
                            }
                        }
                        //
                        soFlow = "";
                        //dialog.dismiss();
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
                    if(soFlow.equals(SO_FLOW_SEARCH_SO)) {
                        mPresenter.executeSoDownload(mdProductSerial.getProduct_code(), mdProductSerial.getSerial_id());
                    }else{
                        callAct050(context);
                    }
                }else{
                    //Se retorno do serial for false, não prosseguir.
                    //Também não é necessario exibir alert, pois o usr já foi avisado no dialog.
                }
                //
                soFlow = "";
            }
        });

    }

    /**
     * Metodo executado após a verificação de existencia, quando o serial verificado
     * não existe.
     */
    @Override
    public void reApplySerialIdToFrag() {
        frgSerialEdit.reApplySerialId();
    }

    /**
     * Metodo executado após a verificação de existencia, quando o serial verificado
     * EXISTE.
     * @param serial_returned
     */
    @Override
    public void applyReceivedSerialToFrag(MD_Product_Serial serial_returned) {
        mdProductSerial = serial_returned;
        frgSerialEdit.applyReceivedSerial(serial_returned);
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
        if(ws_process.equals(WS_Serial_Tracking_Search.class.getName())){
            frgSerialEdit.processTrackingResult(hmAux);
        } else if(ws_process.equalsIgnoreCase(WS_Serial_Search.class.getName())){
            mPresenter.extractSearchResult(mLink);

        }else if(ws_process.equalsIgnoreCase(WS_Serial_Save.class.getName())){
            frgSerialEdit.setNew_serial(false);
            //frgSerialEdit.refreshUi();
            if (hmAux.size() > 0) {
                mPresenter.processSerialSaveResult(mdProductSerial.getProduct_code(), mdProductSerial.getSerial_id(), hmAux);
            } else {
                showSingleResultMsg(
                        hmAux_Trans.get("alert_save_serial_return_ttl"),
                        hmAux_Trans.get("alert_no_serial_return_msg"),
                        false);
            }
        }else if(ws_process.equalsIgnoreCase(WS_SO_Search.class.getName())){
            mPresenter.processSoDownloadResult(hmAux);
        }
        //
        progressDialog.dismiss();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        disableProgressDialog();
    }

    @Override
    public void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct022(Context context) {
        Intent mIntent = new Intent(context, Act022_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        bundle.remove(Constant.ACT007_PRODUCT_CODE);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct025(Context context) {
        Intent mIntent = new Intent(context, Act025_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct026(Context context) {
        Intent mIntent = new Intent(context, Act026_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        bundle.remove(Constant.MAIN_REQUESTING_PROCESS);
        bundle.remove(Constant.MAIN_IS_SCHEDULE);
        bundle.remove(Constant.MAIN_MD_PRODUCT_SERIAL);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT023);
        //Quando o fluxo é vindo da seleção de produto e não serial
        //Não existe o serial no bundle, então é necessario adicioná-lo para que
        //a Act026 filtre apenas as SO's desse produto/serial.
//        if (bundle_serial_id == null || bundle_serial_id.equals("")) {
//            bundle.putString(Constant.MAIN_SERIAL_ID, mdProductSerial.getSerial_id());
//        }
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct027(Context context, Bundle bundleSingleSo) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundleSingleSo);
        startActivity(mIntent);
        finish();
    }

    private void callAct050(Context context){
        Intent mIntent = new Intent(context, Act050_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, mdProductSerial.getProduct_code());
        bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, mdProductSerial.getSerial_code());
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();

    }

    /**
     * Metodo executado no retorno do save do serial e que atualiza o objeto serial
     * na tela e no fragmento.
     * Com esse refresh, no caso de um novo serial, esse metodo atualiza o serial_code
     * que antigamente era 0.
     * @param mdProductSerial - Produto serial atualizado e com o serial_code preenchido
     */
    @Override
    public void updateProductSerialValues(MD_Product_Serial mdProductSerial) {
        this.mdProductSerial = mdProductSerial;
        //
        if (frgSerialEdit != null) {
            frgSerialEdit.setMdProductSerial(this.mdProductSerial);
        }
    }

    /**
     * Metodo que aciona o refresh dos dados do obj para componentes da tela.
     */
    @Override
    public void refreshUI() {
        if(frgSerialEdit != null){
            frgSerialEdit.refreshUi();
        }
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
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

}
