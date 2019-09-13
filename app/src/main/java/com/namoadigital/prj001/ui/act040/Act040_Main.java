package com.namoadigital.prj001.ui.act040;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.service.WS_SO_Pack_Express_Local;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act048.Act048_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 09/03/2018.
 */

public class Act040_Main extends Base_Activity implements Act040_Main_View {

    private static final int PROCESSO_PRODUCT_CODE = 100;
    public static final String EXPRESS_PACK_CODE = "express_pack_code";

    private Bundle bundle;
    private Act040_Main_Presenter_Impl mPresenter;
    private SO_Pack_Express mSo_pack_express;
    private MD_Partner md_partner;
    private MD_Product md_product;
    //private MKEditTextNM mket_produto;
    //private ImageView iv_search_produto;

    private MKEditTextNM mket_serial;
    private ImageView iv_search_serial;

    private MKEditTextNM mket_barcode;
    private ImageView iv_search_barcode;

    private boolean connectionStatusAlter;

    private TextView tv_status;
    private TextView tv_prod_desc;
    private SearchableSpinner ss_partner;
    private Button btn_create_so;
    private String wsProcess = "";
    private String requestingAct = "";
    private String bundle_express_pack_code = "";
    private String bundle_partner_code = "-1";
    private String bundle_serial_id = "";
    private ArrayList<HMAux> wsAuxResult = new ArrayList<>();
    private boolean exitProcess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act040_main);

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
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT040
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act040_title");
        transList.add("ss_partner_hint");
        transList.add("ss_partner_ttl");
        transList.add("status_no_pack_msg");
        transList.add("status_no_partner_msg");
        transList.add("status_no_product_msg");
        transList.add("status_no_serial_msg");
        transList.add("alert_express_error_tll");
        transList.add("alert_express_no_tll");
        transList.add("alert_express_no_msg");
        transList.add("alert_express_general_error_ttl");
        transList.add("alert_express_general_error_msg");
        transList.add("progress_sync_express_ttl");
        transList.add("progress_sync_express_msg");
        transList.add("toast_express_saved_msg");
        transList.add("tv_product_hint");
        transList.add("tv_serial_hint");
        transList.add("tv_barcode_hint");
        transList.add("btn_create_so");
        transList.add("express_desc");
        transList.add("express_status");
        transList.add("express_label");
        transList.add("alert_create_so_express_ttl");
        transList.add("alert_create_so_express_msg");
        transList.add("express_send_error_ttl");
        transList.add("express_send_error_msg");
        //
        transList.add("alert_so_pack_error_tll");
        transList.add("alert_site_or_operation_not_found_ttl");
        transList.add("alert_site_or_operation_not_found_msg");
        transList.add("alert_pending_so_express_exists_ttl");
        transList.add("alert_pending_so_express_exists_msg");
        transList.add("alert_offline_serial_not_found_ttl");
        transList.add("alert_product_not_allow_new_serial_msg");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_product_not_found_ttl");
        transList.add("alert_product_not_found_msg");
        transList.add("serial_result_ttl");
        transList.add("product_result_lbl");
        transList.add("serial_result_lbl");
        transList.add("express_order_result_ttl");
        transList.add("progress_serial_save_ttl");
        transList.add("progress_serial_save_msg");
        transList.add("express_order_ttl");
        transList.add("alert_serial_save_error_ttl");
        transList.add("alert_serial_save_error_msg");
        transList.add("alert_data_success_sent_ttl");
        transList.add("alert_data_success_sent_msg");
        transList.add("alert_data_not_sent_ttl");
        transList.add("alert_data_not_sent_msg");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        mPresenter = new Act040_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new SO_Pack_ExpressDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new SO_Pack_Express_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_PartnerDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ), new MD_SiteDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ), new MD_OperationDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ), new MD_Product_SerialDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
                )
        );
        //
        mket_barcode = (MKEditTextNM) findViewById(R.id.act040_mket_barcode);
        iv_search_barcode = (ImageView) findViewById(R.id.act040_iv_search_barcode);
        mket_barcode.setHint(hmAux_Trans.get("tv_barcode_hint"));
        mket_barcode.requestFocus();
        //
        mket_serial = (MKEditTextNM) findViewById(R.id.act040_mket_serial);
        iv_search_serial = (ImageView) findViewById(R.id.act040_iv_search_serial);
        mket_serial.setHint(hmAux_Trans.get("tv_serial_hint"));
        //09/01/18 - Luche
        //Nos campos mket referentes a serial, o valores de mOcr e mBarcode serão preenchidos
        //via parametro do profile.
        mket_serial.setmOCR(false);
        mket_serial.setmBARCODE(
                ToolBox_Inf.profileExists(
                        context,
                        Constant.PROFILE_MENU_PROFILE,
                        Constant.PROFILE_MENU_PROFILE_SERIAL_BARCODE
                )
        );
        //
        //Verifica se a lib de OCR esta importada no flavor
        //e se o user possui acesso ao OCR
        if(ToolBox_Inf.isMicroBlinkImported()) {
            mket_serial.setmOCRVin(
                    ToolBox_Inf.profileExists(
                            context,
                            Constant.PROFILE_MENU_PROFILE,
                            Constant.PROFILE_MENU_PROFILE_SERIAL_OCR_VIN
                    )
            );
        }else{
            mket_serial.setmOCRVin(false);
        }
        //
        btn_create_so = (Button) findViewById(R.id.act040_btn_create_so);
        btn_create_so.setText(hmAux_Trans.get("btn_create_so"));
        //
        tv_status = (TextView) findViewById(R.id.act040_tv_status);
        tv_prod_desc = (TextView) findViewById(R.id.act040_tv_prod_desc);
        //
        ss_partner = (SearchableSpinner) findViewById(R.id.act040_ss_partner);
        ss_partner.setmShowLabel(false);
        ss_partner.setmHint(hmAux_Trans.get("ss_partner_hint"));
        ss_partner.setmTitle(hmAux_Trans.get("ss_partner_ttl"));
        //
        //Add controles no array list.
        controls_sta.add(mket_serial);
        controls_sta.add(mket_barcode);
        //
        //mPresenter.checkJump(ToolBox_Con.getPreference_Customer_Code(context));
        mPresenter.loadPartners(bundle_partner_code);
        //
        connectionStatusAlter = false;
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            requestingAct = bundle.getString(Constant.MAIN_REQUESTING_ACT,"");
            /*
            * Com excessão do codigo do parceiro, os dados recuperados via bundle
            * são aplicados ao final do metodo initAction, pois nesse momento, os "eventos"
            * ja estão instanciados e apenas o fato de setar o valor no campo ja garante
            * o preenhcimento das variaiveis necessarios para criação da O.S
            */

            if(requestingAct.equals(Constant.ACT048) || requestingAct.equals(Constant.ACT049)){
                bundle_express_pack_code = bundle.getString(EXPRESS_PACK_CODE,"");
                bundle_partner_code = bundle.getString(MD_PartnerDao.PARTNER_CODE,"-1");
                bundle_serial_id = bundle.getString(Constant.MAIN_MD_PRODUCT_SERIAL_ID,"");
            }

        } else {
            bundle_express_pack_code = "";
            bundle_partner_code = "-1";
            bundle_serial_id = "";
        }
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    public void setExitProcess(boolean exitProcess) {
        this.exitProcess = exitProcess;
    }

    @Override
    public boolean isExitProcess() {
        return exitProcess;
    }

    @Override
    public void loadSO_Pack_Express(SO_Pack_Express so_pack_express, String express_code) {
        mSo_pack_express = so_pack_express;
        //
        if (mSo_pack_express != null) {
            tv_status.setText(mSo_pack_express.getPack_desc());
            iv_search_serial.setVisibility(View.VISIBLE);
            iv_search_serial.setEnabled(true);
            //
            md_product = mPresenter.getProdutctInfo(so_pack_express.getProduct_code());
            //
            if (md_product == null) {
                tv_prod_desc.setText("");
                mket_serial.setmInputTypeValidator(null);
                mket_serial.setmRequired(true);
                mket_serial.setmMinSize(null);
                mket_serial.setmMaxSize(null);
                mket_serial.setmIgnoreMaxMinSize(true);
            } else {
                tv_prod_desc.setText(md_product.getProduct_id() + " - " + md_product.getProduct_desc());
                mket_serial.setmInputTypeValidator(md_product.getSerial_rule());
                mket_serial.setmRequired(true);
                mket_serial.setmMinSize(md_product.getSerial_min_length());
                mket_serial.setmMaxSize(md_product.getSerial_max_length());
                //
                mket_serial.setmIgnoreMaxMinSize(true);
            }
            //
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mket_barcode.getWindowToken(), 0);
        } else {
            tv_prod_desc.setText("");
            iv_search_serial.setVisibility(View.GONE);
            iv_search_serial.setEnabled(false);
            //Reseta configuração do produto no mket de serial
            mket_serial.setmInputTypeValidator(null);
            mket_serial.setmMinSize(null);
            mket_serial.setmMaxSize(null);
            //
            if (express_code.isEmpty()) {
                tv_status.setText("");
            } else {
                tv_status.setText(hmAux_Trans.get("status_no_pack_msg"));
            }
        }
    }


    @Override
    public void setPartner(HMAux partner) {
        this.md_partner = getMdPartnerFromHMAux(partner);
        //
//        HMAux md_partner_hm = new HMAux();
//        md_partner_hm.put("description", md_partner.getPartner_desc());
//        md_partner_hm.put("partner_id", md_partner.getPartner_id());
//        md_partner_hm.put("id", String.valueOf(md_partner.getPartner_code()));
//        md_partner_hm.put("customer_code", String.valueOf(md_partner.getCustomer_code()));
        //
        this.ss_partner.setmValue(partner);
    }

    private MD_Partner getMdPartnerFromHMAux(HMAux partner) {
        MD_Partner mdPartner = new MD_Partner();
        //
        try {
            mdPartner.setCustomer_code(Long.parseLong(partner.get(MD_PartnerDao.CUSTOMER_CODE)));
            mdPartner.setPartner_code(Integer.parseInt(partner.get(SearchableSpinner.CODE)));
            //mdPartner.setPartner_id(partner.get(MD_PartnerDao.PARTNER_ID));
            mdPartner.setPartner_id(partner.get(SearchableSpinner.ID));
            mdPartner.setPartner_desc(partner.get(SearchableSpinner.DESCRIPTION));
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            //
            //showMsg();
        }
        //
        return mdPartner;
    }

    @Override
    public void setPartnerList(ArrayList<HMAux> partnerList) {
        ss_partner.setmOption(partnerList);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT040;
        mAct_Title = Constant.ACT040 + "_" + "title";
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
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {
        mket_barcode.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (b) {
                    mPresenter.searchSO_Pack_Express(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            s
                    );
                } else {
                    tv_status.setText("");
                    iv_search_serial.setEnabled(false);
                    iv_search_serial.setVisibility(View.GONE);
                }
            }
        });

        ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (hmAux.size() > 0) {
                    /*mPresenter.setMD_Partner(
                            Long.parseLong(hmAux.get("customer_code")),
                            Long.parseLong(hmAux.get(SearchableSpinner.ID))
                    );*/
                    md_partner = getMdPartnerFromHMAux(hmAux);
                }else{
                    md_partner = null;
                }
            }
        });

        btn_create_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Seta variavel de controle.
                exitProcess = false;
                //
                if (mSo_pack_express != null && md_partner != null && md_product != null && mket_serial.isValid() && !mSo_pack_express.getExpress_code().equalsIgnoreCase(ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString()))) {
                    ToolBox.alertMSG_YES_NO(
                            context,
                            hmAux_Trans.get("alert_create_so_express_ttl"),
                            hmAux_Trans.get("alert_create_so_express_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(mPresenter.checkOrderAlreadyExists(
                                            ToolBox_Con.getPreference_Customer_Code(context),
                                            ToolBox_Con.getPreference_Site_Code(context),
                                            ToolBox_Con.getPreference_Operation_Code(context),
                                            md_product.getProduct_code(),
                                            mSo_pack_express.getExpress_code(),
                                            ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString())
                                        )
                                    ){
                                        ToolBox.alertMSG_YES_NO(
                                                context,
                                                hmAux_Trans.get("alert_pending_so_express_exists_ttl"),
                                                hmAux_Trans.get("alert_pending_so_express_exists_msg"),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mPresenter.onCreateSo_Pack_Express(
                                                                mSo_pack_express,
                                                                md_partner,
                                                                md_product,
                                                                ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim()),
                                                                connectionStatusAlter
                                                        );
                                                    }
                                                },
                                                2,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        automationCleanForm();
                                                    }
                                                },
                                                false
                                        );
                                    }else{
                                        mPresenter.onCreateSo_Pack_Express(
                                                mSo_pack_express,
                                                md_partner,
                                                md_product,
                                                ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim()),
                                                connectionStatusAlter
                                        );
                                    }
                                }
                            },
                            1,
                            false
                    );
                } else {

                    String result = "";

                    if (mSo_pack_express == null) {
                        result = hmAux_Trans.get("status_no_pack_msg");
                    } else if (md_partner == null) {
                        result = hmAux_Trans.get("status_no_partner_msg");
                    } else if (md_product == null) {
                        result = hmAux_Trans.get("status_no_product_msg");
                    } else if (mSo_pack_express.getExpress_code().equalsIgnoreCase(ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString()))) {
                        result = hmAux_Trans.get("pack_equals_serial_msg");
                    } else if (!mket_serial.isValid()) {
                        //result = hmAux_Trans.get("status_no_serial_msg");
                        result = mket_serial.getmErrorMSG();
                    } else {
                        result = "";
                    }

                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_so_pack_error_tll"),
                            result,
                            null,
                            -1,
                            false
                    );
                }

            }
        });
        //08/10/2018 - Busca de serial
        iv_search_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(md_product != null) {
                    mPresenter.executeSerialSearch(md_product, ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString()));
                }else{
                    showMsg(
                            hmAux_Trans.get("alert_product_not_found_ttl"),
                            hmAux_Trans.get("alert_product_not_found_msg")
                    );
                }
            }
        });
        //
        //Se pacote expresso enviado pelo bundle, seta valores.
        if(!bundle_express_pack_code.equalsIgnoreCase("")){
            mket_barcode.setText(bundle_express_pack_code);
            mket_serial.setText(bundle_serial_id);
        }
    }

    @Override
    public void addWsAuxResult(HMAux auxResult) {
        wsAuxResult.add(auxResult);
    }

    @Override
    public void disablePartnerSelector() {
        ss_partner.setmEnabled(false);
    }

    @Override
    public void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct048(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act048_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        if(bundle == null){
            bundle = new Bundle();
        }
        //
        bundle.putString(EXPRESS_PACK_CODE, mSo_pack_express.getExpress_code()); //mdProduct != null ? mdProduct.getProduct_id() : "");
        if(md_partner != null ) {
            bundle.putString(MD_PartnerDao.PARTNER_CODE, String.valueOf(md_partner.getPartner_code()));
        }
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();

    }

    @Override
    public void automationCleanForm() {
        mket_barcode.setText("");
        mket_serial.setText("");
        mket_serial.setmInputTypeValidator("");
        tv_status.setText("");
        tv_prod_desc.setText("");
        iv_search_serial.setVisibility(View.GONE);
        iv_search_serial.setEnabled(true);
        //
        mSo_pack_express = null;
        md_product = null;
        //
        mket_barcode.requestFocus();
        wsAuxResult.clear();
        exitProcess = false;
    }

    @Override
    public void onBackPressed() {

        mPresenter.onBackPressedClicked(
                md_product == null ? null : md_product.getProduct_code(),
                ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString())
        );
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //progressDialog.dismiss();
        processCloseACT(mLink,mRequired,new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired);
        //
        if(wsProcess.equalsIgnoreCase(WS_SO_Pack_Express_Local.class.getName())) {
            progressDialog.dismiss();
            //
            if (hmAux.keySet().size() == 0) {
                if(!exitProcess) {
                    automationCleanForm();
                    //
                    showMsg(
                            hmAux_Trans.get("alert_express_no_tll"),
                            hmAux_Trans.get("alert_express_no_msg")
                    );
                }else{
                    exitProcessMsg(true);
                }
            } else {
                showResults(hmAux);
            }
        }else if(wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())){
            progressDialog.dismiss();
            mPresenter.extractSearchResult(md_product, ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString()), mLink);
        }else if(wsProcess.equals(WS_Serial_Save.class.getName())){
            mPresenter.processSerialSaveResult(hmAux);
            mPresenter.executeSO_Pack_Express_Local(connectionStatusAlter);
        }
    }

    @Override
    public void exitProcessMsg(boolean successMsg) {
        String ttl = successMsg ? hmAux_Trans.get("alert_data_success_sent_ttl") : hmAux_Trans.get("alert_data_not_sent_ttl");
        String msg = successMsg ? hmAux_Trans.get("alert_data_success_sent_msg") : hmAux_Trans.get("alert_data_not_sent_msg");
        //Se progress sendo exibido na msg negativa, o desabilita antes de exibir a caixa.
        if(!successMsg && progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        //
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callAct021(context);
                    }
                },
                0
        );
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
        //
//        automationCleanForm();
//        //
//        showMsg(
//                hmAux_Trans.get("alert_express_general_error_ttl"),
//                hmAux_Trans.get("alert_express_general_error_msg")
//        );

        if(wsProcess.equalsIgnoreCase(WS_SO_Pack_Express_Local.class.getName())) {
            //
            automationCleanForm();
            //
            showMsg(
                    hmAux_Trans.get("alert_express_general_error_ttl"),
                    hmAux_Trans.get("alert_express_general_error_msg")
            );
        }else if(wsProcess.equalsIgnoreCase(WS_Serial_Save.class.getName())){
            showMsg(
                    hmAux_Trans.get("alert_serial_save_error_ttl"),
                    hmAux_Trans.get("alert_serial_save_error_msg")
            );
        }
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
    public void showPD(String ttl, String msg) {
        if(progressDialog == null || !progressDialog.isShowing()) {
            enableProgressDialog(
                    ttl,
                    msg,
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
        }else{
            progressDialog.setTitle(ttl);
            progressDialog.setMessage(msg);
        }

    }

    private void showResults(HMAux so_express) {
        ArrayList<HMAux> mSO_Express = new ArrayList<>();

        for (String sKey : so_express.keySet()) {
            HMAux hmAux = new HMAux();
            //
            String[] sParts = sKey.split(Constant.MAIN_CONCAT_STRING);

            String sFinal = sParts[0] + "\n" + sParts[1] + "\n" + sParts[2];

            //hmAux.put("so_express_code", sFinal);
            //hmAux.put("so_express_result", so_express.get(sKey));

            hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("express_order_ttl"));

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, hmAux_Trans.get("express_label"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, sParts[0] + "   -   " + sParts[2]);

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_2, hmAux_Trans.get("express_desc"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_2, sParts[1]);

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_3, hmAux_Trans.get("express_status"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_3, so_express.get(sKey));

            //mSO_Express.add(hmAux);
            addWsAuxResult(hmAux);
        }

        showResultsDialog();
    }

    public void showResultsDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        wsAuxResult,
                        Generic_Results_Adapter.CONFIG_3_ITENS_NEW,
                        hmAux_Trans
                )
        );

        //builder.setTitle(hmAux_Trans.get("alert_results_ttl"));
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
                //
                if(exitProcess){
                    automationCleanForm();
                    onBackPressed();
                }else{
                    automationCleanForm();
                }
            }
        });
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
    public void showMsgToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setConnectionStatusAlter(boolean connectionStatusAlter) {
        this.connectionStatusAlter = connectionStatusAlter;
    }
    @Override
    public boolean isConnectionStatusAlter() {
        return connectionStatusAlter;
    }
}
