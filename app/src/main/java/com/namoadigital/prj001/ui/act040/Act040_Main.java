package com.namoadigital.prj001.ui.act040;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act040SOExpressPackServicesAdapter;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.dao.SoPackExpressPacksLocalDao;
import com.namoadigital.prj001.databinding.Act040MainBinding;
import com.namoadigital.prj001.databinding.Act040MainContentBinding;
import com.namoadigital.prj001.databinding.Act040MainDuplicatedDialogBinding;
import com.namoadigital.prj001.extensions.AppCompatActivityKt;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.model.SoPackExpressPacksLocal;
import com.namoadigital.prj001.service.WS_SO_Pack_Express_Local;
import com.namoadigital.prj001.service.WS_SO_Service_Search;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act042.Act042_Main;
import com.namoadigital.prj001.ui.act048.Act048_Main;
import com.namoadigital.prj001.ui.act091.Act091_Main;
import com.namoadigital.prj001.ui.act091.bottomstate.Act091_BottomSheet;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by d.luche on 09/03/2018.
 */

public class Act040_Main extends Base_Activity implements Act040_Main_View {

    private static final int PROCESSO_PRODUCT_CODE = 100;
    public static final String EXPRESS_PACK_CODE = "express_pack_code";
    public static final String HAS_SERVICE_ADDED = "HAS_SERVICE_ADDED";

    private Bundle bundle;
    private Act040_Main_Presenter mPresenter;
    private SO_Pack_Express mSo_pack_express;
    private MD_Partner md_partner;
    private MD_Product md_product;
    private String wsProcess = "";
    private String requestingAct = "";
    private String bundle_express_pack_code = "";
    private String bundle_partner_code = "-1";
    private String bundle_serial_id = "";
    private String bundle_billing_info1 = "";
    private String bundle_billing_info2 = "";
    private String bundle_billing_info3 = "";
    private long bundle_express_tmp = -1;
    private boolean hasServiceAdded = false;
    private final String bundle_category_price_code = "";
    private final String bundle_contract_code = "";
    private final String bundle_product_code = "";
    private final ArrayList<HMAux> wsAuxResult = new ArrayList<>();
    private boolean exitProcess = false;
    public static final String LABEL_TRANS_OS_EXPRESS= "lbl_type_service_order_express";
    private final ArrayList<MKEditTextNM> trackingFields = new ArrayList<>();

    private Act040MainContentBinding binding;
    private Act040SOExpressPackServicesAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        Act040MainBinding act040MainBinding = Act040MainBinding.inflate(getLayoutInflater());
        binding = act040MainBinding.act040MainContent;
        setContentView(act040MainBinding.getRoot());
        setSupportActionBar(act040MainBinding.toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        transList.add("btn_pendency_so");
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
        transList.add(LABEL_TRANS_OS_EXPRESS);
        //
        transList.add("billing_add_info1_lbl");
        transList.add("billing_add_info2_lbl");
        transList.add("billing_add_info3_lbl");
        transList.add("last_express_in_site_x_operation_lbl");
        transList.add("dialog_duplicate_ttl");
        transList.add("dialog_duplicate_warning_msg");
        transList.add("dialog_serial_lbl");
        transList.add("dialog_last_date_lbl");
        transList.add("dialog_duplicate_confirm_msg");
        transList.add("dialog_btn_confirm");
        transList.add("toast_express_successfully_saved_msg");
        transList.add("alert_no_express_os_history_found_ttl");
        transList.add("alert_no_express_os_history_found_msg");
        transList.add("alert_leave_express_creation_ttl");
        transList.add("alert_leave_express_creation_confirm");
        transList.add("tracking_duplicated_msg");
        transList.add("express_order_pack_service_list_lbl");
        transList.add("express_order_pack_service_empty_list_lbl");
        transList.add("dialog_service_search_ttl");
        transList.add("dialog_service_search_msg");
        transList.add("so_express_service_various_comments");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
        List<String> transListFrag = new ArrayList<String>();
        transListFrag.add("serial_rule_lbl");
        transListFrag.add("serial_min_length_lbl");
        transListFrag.add("serial_min_max_separator_lbl");
        transListFrag.add("serial_max_length_lbl");
        //
        hmAux_Trans.putAll(
            ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                ToolBox_Inf.getResourceCode(
                    context,
                    mModule_Code,
                    Constant.FRG_SERIAL_SEARCH
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transListFrag
            )
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
                ), new MD_Site_ZoneDao(
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
                ), new SoPackExpressPacksLocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
                )
        );
        //
        mPresenter.getLastExpressInfoInSiteOper();
        //
        binding.tilPack.setHint(hmAux_Trans.get("tv_barcode_hint"));
        //
        binding.tilSerial.setHint(hmAux_Trans.get("tv_serial_hint"));
        //09/01/18 - Luche
        //Nos campos mket referentes a serial, o valores de mOcr e mBarcode serão preenchidos
        //via parametro do profile.
        binding.mketSerial.setmOCR(
            ToolBox_Inf.profileExists(
                context,
                Constant.PROFILE_MENU_PROFILE,
                Constant.PROFILE_MENU_PROFILE_SERIAL_OCR_MOSOLF
            )
        );
        binding.mketSerial.setmBARCODE(
                ToolBox_Inf.profileExists(
                        context,
                        Constant.PROFILE_MENU_PROFILE,
                        Constant.PROFILE_MENU_PROFILE_SERIAL_BARCODE
                )
        );
        //
        binding.clFinalizeBtn.setEnabled(false);
        binding.tvFinalize.setText(hmAux_Trans.get("btn_create_so"));
        //
        binding.tvServiceListLbl.setText(hmAux_Trans.get("express_order_pack_service_list_lbl"));
        binding.tvAddPackServicesPlaceholder.setText(hmAux_Trans.get("express_order_pack_service_empty_list_lbl"));
        binding.tvPartner.setText(hmAux_Trans.get("ss_partner_hint"));
        binding.ssPartner.setmShowLabel(false);
        binding.ssPartner.setmCanClean(false);
        binding.ssPartner.setmHint(hmAux_Trans.get("ss_partner_hint"));
        binding.ssPartner.setmTitle(hmAux_Trans.get("ss_partner_ttl"));
        //
        //Add controles no array list.
        controls_sta.add(binding.mketSerial);
        controls_sta.add(binding.mketPack);
        controls_sta.add(binding.mketAddInfo1);
        controls_sta.add(binding.mketAddInfo2);
        controls_sta.add(binding.mketAddInfo3);
        //
        //mPresenter.checkJump(ToolBox_Con.getPreference_Customer_Code(context));
        mPresenter.loadPartners(bundle_partner_code);
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

            if(requestingAct.equals(Constant.ACT048)
                    || requestingAct.equals(Constant.ACT049)
                    || requestingAct.equals(Constant.ACT042)
                    || requestingAct.equals(Constant.ACT091)
            ){
                bundle_express_pack_code = bundle.getString(EXPRESS_PACK_CODE,"");
                bundle_partner_code = bundle.getString(MD_PartnerDao.PARTNER_CODE,"-1");
                bundle_serial_id = bundle.getString(Constant.MAIN_MD_PRODUCT_SERIAL_ID,"");
                bundle_billing_info1 = bundle.getString(SO_Pack_Express_LocalDao.BILLING_ADD_INF1_VALUE,"");
                bundle_billing_info2 = bundle.getString(SO_Pack_Express_LocalDao.BILLING_ADD_INF2_VALUE,"");
                bundle_billing_info3 = bundle.getString(SO_Pack_Express_LocalDao.BILLING_ADD_INF3_VALUE,"");
                bundle_express_tmp = bundle.getLong(SO_Pack_Express_LocalDao.EXPRESS_TMP,-1);
                hasServiceAdded = bundle.getBoolean(HAS_SERVICE_ADDED,false);
            }

        } else {
            bundle_express_pack_code = "";
            bundle_partner_code = "-1";
            bundle_serial_id = "";
            bundle_billing_info1 = "";
            bundle_billing_info2 = "";
            bundle_billing_info3 = "";
        }
    }

    @Override
    public void setLastExpressInfo(SO_Pack_Express_Local lastExpressInSiteOper) {
        if(lastExpressInSiteOper != null){
            binding.clLastOrder.setVisibility(View.VISIBLE);
            binding.tvLastOrderTtl.setText(hmAux_Trans.get("last_express_in_site_x_operation_lbl"));
            binding.tvPackDesc.setText(mPresenter.getServicesDetailsResume(lastExpressInSiteOper));
            binding.tvSerialId.setText(lastExpressInSiteOper.getSerial_id());
            binding.tvLogDate.setText(
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(lastExpressInSiteOper.getLog_date()),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                )
            );
        }else{
            binding.clLastOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void refreshPackServiceList(List<SoPackExpressPacksLocal> packsLocal, SoPackExpressPacksLocal item, int position) {
        List<SoPackExpressPacksLocal> soExpressList = mAdapter.getSoExpressList();
        if(position >= 0) {
            mAdapter.highlightItemChange(position, item);
        }else{
            soExpressList.clear();
            if(packsLocal.size() > 0) {
                soExpressList.addAll(packsLocal);
                mAdapter.notifyDataSetChanged();
                binding.rvAddPackServices.setVisibility(View.VISIBLE);
                binding.tvAddPackServicesPlaceholder.setVisibility(View.GONE);
            }else{
                binding.rvAddPackServices.setVisibility(View.INVISIBLE);
                binding.tvAddPackServicesPlaceholder.setVisibility(View.VISIBLE);
            }
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
            String tilPackHelper = mSo_pack_express.getPack_desc();

            binding.clSerialSearch.setVisibility(View.VISIBLE);
            binding.clSerialSearchBtn.setEnabled(true);
            //
            md_product = mPresenter.getProdutctInfo(so_pack_express.getProduct_code());
            //
            if (md_product == null) {
                binding.mketSerial.setmInputTypeValidator(null);
                binding.mketSerial.setmRequired(true);
                binding.mketSerial.setmMinSize(null);
                binding.mketSerial.setmMaxSize(null);
                binding.mketSerial.setmIgnoreMaxMinSize(true);
                //Seta helper do serial
                setSerialHelper(null, null, null);
            } else {
                tilPackHelper = mSo_pack_express.getPack_desc() +"\n" + md_product.getProduct_desc();
                binding.mketSerial.setmInputTypeValidator(md_product.getSerial_rule());
                binding.mketSerial.setmRequired(true);
                binding.mketSerial.setmMaxSize(md_product.getSerial_max_length());
                //
                binding.mketSerial.setmIgnoreMaxMinSize(true);
                ////Seta helper do serial
                setSerialHelper(
                    md_product.getSerial_rule(),
                    md_product.getSerial_min_length(),
                    md_product.getSerial_max_length()
                );
            }
            binding.tilPack.setHelperText(tilPackHelper);
            binding.tilPack.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_ok));
            //
        } else {
            binding.tilPack.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_error));
            binding.tilPack.setHelperText(null);
            binding.clSerialSearch.setVisibility(View.GONE);
            binding.clSerialSearchBtn.setEnabled(false);
            //Reseta configuração do produto no mket de serial
            binding.mketSerial.setmInputTypeValidator(null);
            binding.mketSerial.setmMinSize(null);
            binding.mketSerial.setmMaxSize(null);
            //Seta helper do serial
            setSerialHelper(null, null, null);
            //
            if (express_code.isEmpty()) {
                binding.tilPack.setHelperText(null);
            } else {
                binding.tilPack.setError(hmAux_Trans.get("status_no_pack_msg"));
            }
        }
        //
        refreshAddInfoVisibility();
    }

    @Override
    public void setPartner(HMAux partner) {
        this.md_partner = getMdPartnerFromHMAux(partner);
        binding.ssPartner.setmValue(partner);
        binding.tilPartner.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_ok));
        binding.tvPartner.setVisibility(View.VISIBLE);
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
        binding.ssPartner.setmOption(partnerList);
        binding.ssPartner.requestFocus();
    }

    private void refreshAddInfoVisibility(){
        trackingFields.clear();
        //
       if(mSo_pack_express != null) {
            configBillingInfoView(
                binding.tilAddInfo1,
                binding.mketAddInfo1,
                mSo_pack_express.getBilling_add_inf1_view(),
                mSo_pack_express.getBilling_add_inf1_text(),
                mSo_pack_express.getBilling_add_inf1_tracking(),
                hmAux_Trans.get("billing_add_info1_lbl")
            );
            configBillingInfoView(
                binding.tilAddInfo2,
                binding.mketAddInfo2,
                mSo_pack_express.getBilling_add_inf2_view(),
                mSo_pack_express.getBilling_add_inf2_text(),
                mSo_pack_express.getBilling_add_inf2_tracking(),
                hmAux_Trans.get("billing_add_info2_lbl")
            );
            configBillingInfoView(
                binding.tilAddInfo3,
                binding.mketAddInfo3,
                mSo_pack_express.getBilling_add_inf3_view(),
                mSo_pack_express.getBilling_add_inf3_text(),
                mSo_pack_express.getBilling_add_inf3_tracking(),
                hmAux_Trans.get("billing_add_info3_lbl")
            );
            if(mSo_pack_express.getAdd_pack_service() == 1
            && ToolBox_Inf.profileExists(
                    context,
                    Constant.PROFILE_MENU_SO,
                    Constant.PROFILE_MENU_SO_PARAM_EDIT
                )
            ){
                binding.clAddPackServices.setVisibility(View.VISIBLE);
                //
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                binding.rvAddPackServices.setLayoutManager(linearLayoutManager);
                List<SoPackExpressPacksLocal> packs = new ArrayList<>();
                SO_Pack_Express_Local so_pack_express_local = mPresenter.getExpressPackLocal(
                        mSo_pack_express.getCustomer_code(),
                        mSo_pack_express.getProduct_code(),
                        mSo_pack_express.getSite_code(),
                        mSo_pack_express.getOperation_code(),
                        mSo_pack_express.getExpress_code(),
                        (int) bundle_express_tmp);
                if(so_pack_express_local != null) {
                    packs = so_pack_express_local.getPacksLocals();
                }else {
                    packs = mPresenter.getExpressPacks(mSo_pack_express, md_partner, md_product);
                }
                //
                if (packs != null && packs.size() > 0) {
                    //
                    mAdapter = new Act040SOExpressPackServicesAdapter(
                            packs,
                            ToolBox_Inf.profileExists(
                                    context,
                                    Constant.PROFILE_MENU_SO,
                                    Constant.PROFILE_MENU_SO_SHOW_SERVICE_PRICE
                            ),
                            hmAux_Trans,
                            getHighlightedPosition(packs),
                            (packsLocal, position) -> {
                                if (mPresenter.hasPackServiceFile(mSo_pack_express.getContract_code(), mSo_pack_express.getProduct_code(), mSo_pack_express.getCategory_price_code(), mSo_pack_express.getSite_code(), mSo_pack_express.getOperation_code())) {
                                    callBottomSheet(packsLocal, position);
                                } else {
                                    mPresenter.executeWS_SO_Service_Search(mSo_pack_express, Objects.requireNonNull(binding.mketSerial.getText()).toString(), packsLocal);
                                }
                                return null;
                            }
                    );
                    //
                    binding.rvAddPackServices.setAdapter(mAdapter);
                    binding.rvAddPackServices.setVisibility(View.VISIBLE);
                    binding.tvAddPackServicesPlaceholder.setVisibility(View.INVISIBLE);
                    if(hasServiceAdded){
                        binding.svMain.post(new Runnable() {
                            public void run() {
                                binding.svMain.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                }else{
                    binding.rvAddPackServices.setVisibility(View.INVISIBLE);
                    binding.tvAddPackServicesPlaceholder.setVisibility(View.VISIBLE);
                }
                //
            }else{
                binding.clAddPackServices.setVisibility(View.GONE);
            }
        }else{
            binding.tilAddInfo1.setVisibility(View.GONE);
            binding.tilAddInfo2.setVisibility(View.GONE);
            binding.tilAddInfo3.setVisibility(View.GONE);
            binding.mketAddInfo1.setOnReportTextChangeListner(null);
            binding.mketAddInfo2.setOnReportTextChangeListner(null);
            binding.mketAddInfo3.setOnReportTextChangeListner(null);
            binding.mketAddInfo1.setText("");
            binding.mketAddInfo2.setText("");
            binding.mketAddInfo3.setText("");
            binding.mketAddInfo1.setTag("");
            binding.mketAddInfo2.setTag("");
            binding.mketAddInfo3.setTag("");
            binding.clAddPackServices.setVisibility(View.GONE);
            mPresenter.deleteExpressAllPackLocal();
        }
    }

    private int getHighlightedPosition(List<SoPackExpressPacksLocal> packs) {
        return hasServiceAdded ? packs.size() - 1 : -1;
    }

    private void callBottomSheet(SoPackExpressPacksLocal soPackExpressPacksLocal, int position) {
        Gson gson = new Gson();

        Act091_BottomSheet packServicesEditFragment = Act091_BottomSheet.Companion.getInstance(gson.toJson(soPackExpressPacksLocal), true, position);
        packServicesEditFragment.setOnAddServices(item -> {
            mPresenter.updateExpressPackage(
                    item,
                    mSo_pack_express.getCustomer_code(),
                    mSo_pack_express.getProduct_code(),
                    mSo_pack_express.getSite_code(),
                    mSo_pack_express.getOperation_code(),
                    mSo_pack_express.getExpress_code(),
                    (int) bundle_express_tmp,
                    position
            );
            return null;
        });
        //
        packServicesEditFragment.setOnDeleteServices(new Function1<SoPackExpressPacksLocal, Unit>() {
            @Override
            public Unit invoke(SoPackExpressPacksLocal item) {
                mPresenter.deleteSelectedExpressPackLocal( item,
                        mSo_pack_express.getCustomer_code(),
                        mSo_pack_express.getProduct_code(),
                        mSo_pack_express.getSite_code(),
                        mSo_pack_express.getOperation_code(),
                        mSo_pack_express.getExpress_code(),
                        (int) bundle_express_tmp,
                        -1
                );
                return null;
            }
        });
        packServicesEditFragment.show(getSupportFragmentManager(), "bottomSheet");
    }

    private void configBillingInfoView(TextInputLayout til, MKEditTextNM mketAddInfo, String viewDef, String hint, int isTracking, String helperText){
        if(!ConstantBaseApp.MASK_VIEW_TYPE_HIDE.equals(viewDef)){
            til.setHint(hint);
            til.setHelperTextEnabled(true);
            til.setHelperText(helperText);
            til.setVisibility(View.VISIBLE);
            til.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_ok));
            //Se required, seta borda de required e configura listener para aplicara e remover baseado
            //no conteudo digitado
            if(ConstantBaseApp.MASK_VIEW_TYPE_REQUIRED.equals(viewDef)){
                til.setBackground(
                    ContextCompat.getDrawable(
                        context,
                        mketAddInfo.getText() != null && mketAddInfo.getText().toString().trim().isEmpty()
                        ? R.drawable.shape_error
                        : R.drawable.shape_ok
                    )
                );
                //
                mketAddInfo.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
                    @Override
                    public void reportTextChange(String s) {

                    }

                    @Override
                    public void reportTextChange(String s, boolean sNotEmpty) {
                        applyBackgroundForRequireField(til,sNotEmpty);
                    }
                });
            }
            //Se item for visivel e for tracking, adiciona na lista de views conf como tracking
            //Tambem seta na tag do mket o valor do helper do til , pois será usado para msg de erro.
            if(isTracking == 1){
                mketAddInfo.setTag(til.getHelperText());
                trackingFields.add(mketAddInfo);
            }
        }else{
            til.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 07/11/2019
     *
     * Metodo que seta no textHelper as regras do produto.
     *
     * A validação helper != null é uma gambiarra para corrigir o que parece ser um problema
     * de "redesenho" da espaço do textHelper.
     * A correção foi necessaria pois, quando o produto é selecionado via act de seleção de produto,
     * ao setar o valor no textHelper, o topo do texto ficava "comido", como se tivesse por baixo
     * do Mket do serial.
     *
     * LUCHE - 08/11/2019
     * Removido a gambiarra de desabilitar e abilitar o textHelper, pois
     * descobrimos que o que gerava o "bug" era a segunda chamada do metodo
     * setSerialRule dentro do metodo processResult()
     *
     * @param serial_rule - Regra do Serial ou null se não houver
     * @param min         - Qtd Min de caracteres ou null se não houver
     * @param max         - Qtd Max de caracteres ou null se não houver
     */
    private void setSerialHelper(String serial_rule, Integer min, Integer max) {
        //Chama metodo que retorna texto ja formatado
        binding.tilSerial.setHelperText(
            mPresenter.getFormattedRuleHelper(
                hmAux_Trans,
                serial_rule,
                min,
                max
            )
        );
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
        binding.mketPack.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
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
                    //Reseta pacote
                    mSo_pack_express = null;
                    binding.clSerialSearchBtn.setEnabled(false);
                    binding.clSerialSearch.setVisibility(View.GONE);
                    //chama validação
                    loadSO_Pack_Express(mSo_pack_express," ");
                }
                //Valida liberação do botão
                validateEnableFinalizeBtn();
            }
        });

        binding.mketSerial.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean sNotEmpty) {
                applyBackgroundForRequireField(binding.clSerial,sNotEmpty);
            }
        });

        binding.ssPartner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (hmAux.size() > 0) {
                    md_partner = getMdPartnerFromHMAux(hmAux);
                    binding.tilPartner.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_ok));
                    binding.tvPartner.setVisibility(View.VISIBLE);
                }else{
                    md_partner = null;
                    binding.tilPartner.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_error));
                    binding.tvPartner.setVisibility(View.GONE);
                }
                //Valida liberação do botão
                validateEnableFinalizeBtn();
            }
        });

        binding.clFinalizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Seta variavel de controle.
                exitProcess = false;
                final String finalSerial = ToolBox_Inf.removeAllLineBreaks(binding.mketSerial.getText().toString());
                boolean hasNoTrackingRepeated = mPresenter.hasNoTrackingDuplicated(trackingFields);
                //
                handleSerialIdCharConstraints();
                if (isRequiredFieldsValid()
                    && binding.mketSerial.isValid()
                    && !mSo_pack_express.getExpress_code().equalsIgnoreCase(finalSerial)
                    && hasNoTrackingRepeated
                ) {
                    ToolBox.alertMSG(
                            context,
                            finalSerial.toUpperCase(),
                            hmAux_Trans.get("alert_create_so_express_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SO_Pack_Express_Local lastExpressIn3hour = mPresenter.checkOrderAlreadyExists(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        ToolBox_Con.getPreference_Site_Code(context),
                                        ToolBox_Con.getPreference_Operation_Code(context),
                                        md_product.getProduct_code(),
                                        mSo_pack_express.getExpress_code(),
                                        finalSerial
                                    );
                                    if(lastExpressIn3hour != null){
                                        showDuplicateExpressDialog(lastExpressIn3hour);
                                    }else{
                                        prepareCreateSoPackExpress();
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
                    } else if (mSo_pack_express.getExpress_code().equalsIgnoreCase(finalSerial)) {
                        result = hmAux_Trans.get("pack_equals_serial_msg");
                    } else if (!binding.mketSerial.isValid()) {
                        //Seta foco
                        binding.mketSerial.requestFocus();
                        //Se ponteiro no final do mket
                        binding.mketSerial.setSelection(binding.mketSerial.getText().length());
                        //Chama Kotlin extension que força abertura do teclado.
                        AppCompatActivityKt.forceOpenKeyboard(Act040_Main.this);
                        //define msg de erro
                        result = binding.mketSerial.getmErrorMSG();
                    } else if(!hasNoTrackingRepeated){
                        result = mPresenter.getFormattedTrackingDuplicated(
                            hmAux_Trans.get("tracking_duplicated_msg"),
                            trackingFields
                        );
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
        //

        binding.clHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.handleHistClick();
            }
        });

        //08/10/2018 - Busca de serial
        binding.clSerialSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSerialIdCharConstraints();

                if(md_product != null) {
                    mPresenter.executeSerialSearch(md_product, ToolBox_Inf.removeAllLineBreaks(binding.mketSerial.getText().toString()));
                }else{
                    showMsg(
                            hmAux_Trans.get("alert_product_not_found_ttl"),
                            hmAux_Trans.get("alert_product_not_found_msg")
                    );
                }
            }
        });
        //
        binding.ivAddPackServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSo_pack_express != null) {
                    //
                    if (mPresenter.hasPackServiceFile(mSo_pack_express.getContract_code(), mSo_pack_express.getProduct_code(), mSo_pack_express.getCategory_price_code(), mSo_pack_express.getSite_code(), mSo_pack_express.getOperation_code())) {
                        setSoPackExpressLocal();
                        callAct091();
                    }else{
                        mPresenter.executeWS_SO_Service_Search(mSo_pack_express, binding.mketSerial.getText().toString(), null);
                    }
                }
            }
        });

        //
        //Se pacote expresso, ou parceiro ou serial enviado pelo bundle, seta valores.
        if(
            !bundle_partner_code.equalsIgnoreCase("-1")
            || !bundle_express_pack_code.equalsIgnoreCase("")
            || !bundle_serial_id.equalsIgnoreCase("")
        ){
            if(!bundle_express_pack_code.equalsIgnoreCase("")) {
                binding.mketPack.setText(bundle_express_pack_code);
            }
            binding.mketSerial.setText(bundle_serial_id);
            if( binding.tilAddInfo1.getVisibility() == View.VISIBLE) {
                binding.mketAddInfo1.setText(bundle_billing_info1);
            }
            if( binding.tilAddInfo2.getVisibility() == View.VISIBLE) {
                binding.mketAddInfo2.setText(bundle_billing_info2);
            }
            if( binding.tilAddInfo3.getVisibility() == View.VISIBLE) {
                binding.mketAddInfo3.setText(bundle_billing_info3);
            }
        }
    }

    private void setSoPackExpressLocal() {
        SO_Pack_Express_Local so_pack_express_local = null;
        if(bundle_express_tmp > 0) {
            so_pack_express_local = mPresenter.getExpressPackLocal(
                    mSo_pack_express.getCustomer_code(),
                    mSo_pack_express.getProduct_code(),
                    mSo_pack_express.getSite_code(),
                    mSo_pack_express.getOperation_code(),
                    mSo_pack_express.getExpress_code(),
                    (int) bundle_express_tmp);
        }
        //
        if(so_pack_express_local == null) {
            so_pack_express_local = mPresenter.onCreateSo_Pack_Express_Structure(
                    mSo_pack_express,
                    md_partner,
                    md_product,
                    ToolBox_Inf.removeAllLineBreaks(binding.mketSerial.getText().toString().trim()),
                    getBillingInfoFromUi(mSo_pack_express.getBilling_add_inf1_view(), binding.mketAddInfo1),
                    getBillingInfoFromUi(mSo_pack_express.getBilling_add_inf2_view(), binding.mketAddInfo2),
                    getBillingInfoFromUi(mSo_pack_express.getBilling_add_inf3_view(), binding.mketAddInfo3)
            );
            bundle_express_tmp = so_pack_express_local.getExpress_tmp();
        }
    }

    private void callAct091() {
        Intent mIntent = new Intent(context, Act091_Main.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT040);
        setFieldsBundle(bundle);
        //
        bundle.putInt(SO_Pack_ExpressDao.CONTRACT_CODE, mSo_pack_express.getContract_code());
        bundle.putLong(SO_Pack_ExpressDao.PRODUCT_CODE, mSo_pack_express.getProduct_code());
        bundle.putInt(SO_Pack_ExpressDao.CATEGORY_PRICE_CODE, mSo_pack_express.getCategory_price_code());
        bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, binding.mketSerial.getText().toString().trim());
        bundle.putString(SO_Pack_ExpressDao.EXPRESS_CODE, mSo_pack_express.getExpress_code());
        bundle.putLong(SO_Pack_Express_LocalDao.EXPRESS_TMP, bundle_express_tmp);
        bundle.putSerializable(Constant.PARAM_KEY_TYPE_SO_EXPRESS, mSo_pack_express);
        //
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    /**
     * Metodo que concentra a chamada da criação da expresso
     */
    private void prepareCreateSoPackExpress() {
        mPresenter.onCreateSo_Pack_Express(
                    mSo_pack_express,
                    md_partner,
                    md_product,
                    ToolBox_Inf.removeAllLineBreaks(binding.mketSerial.getText().toString().trim()),
                    getBillingInfoFromUi(mSo_pack_express.getBilling_add_inf1_view(), binding.mketAddInfo1),
                    getBillingInfoFromUi(mSo_pack_express.getBilling_add_inf2_view(), binding.mketAddInfo2),
                    getBillingInfoFromUi(mSo_pack_express.getBilling_add_inf3_view(), binding.mketAddInfo3),
                    bundle_express_tmp
        );
    }

    /**
     * Metodo que recebe a configuração de view do billing info e o seu mket e retorna o valor valido
     * par ao componente.
     * Se componente HIDE, retorna null, caso contrario resgata valor digitado no mket
     * @param billingAddInfView
     * @param mketAddInfo
     * @return
     */
    private String getBillingInfoFromUi(String billingAddInfView, MKEditTextNM mketAddInfo) {
        if(!ConstantBaseApp.MASK_VIEW_TYPE_HIDE.equals(billingAddInfView)){
            return  mketAddInfo.getText() != null
                    ? mketAddInfo.getText().toString().trim()
                    : null;
        }
        //
        return null;
    }

    private void showDuplicateExpressDialog(SO_Pack_Express_Local lastExpressIn3hour) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Act040MainDuplicatedDialogBinding dialogBinding = Act040MainDuplicatedDialogBinding.inflate(getLayoutInflater());
        View view = dialogBinding.getRoot();

        dialogBinding.tvDialogTtl.setText(hmAux_Trans.get("dialog_duplicate_ttl"));
        dialogBinding.tvWarningLbl.setText(hmAux_Trans.get("dialog_duplicate_warning_msg"));
        dialogBinding.tvSerialLbl.setText(hmAux_Trans.get("dialog_serial_lbl"));
        dialogBinding.tvSerialVal.setText(lastExpressIn3hour.getSerial_id());
        dialogBinding.tvLogDateLbl.setText(hmAux_Trans.get("dialog_last_date_lbl"));
        dialogBinding.tvLogDateVal.setText(
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(lastExpressIn3hour.getLog_date()),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            )
        );
        dialogBinding.tvConfirmLbl.setText(hmAux_Trans.get("dialog_duplicate_confirm_msg"));
        dialogBinding.btnCancel.setText(hmAux_Trans.get("sys_alert_btn_cancel"));
        dialogBinding.btnConfirm.setText(hmAux_Trans.get("dialog_btn_confirm"));
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        //
        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialogBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareCreateSoPackExpress();
                alertDialog.dismiss();
            }
        });
        //
        alertDialog.show();
    }

    private void applyBackgroundForRequireField(View til, boolean isStringNotEmpty) {
        if(isStringNotEmpty){
            til.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_ok));
        }else{
            til.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_error));
        }
        //
        validateEnableFinalizeBtn();
    }

    private void validateEnableFinalizeBtn() {
        binding.clFinalizeBtn.setEnabled(isRequiredFieldsValid());
    }

    private boolean isRequiredFieldsValid() {
       return  mSo_pack_express != null
            && md_partner != null
            && md_product != null
            && !binding.mketSerial.getText().toString().trim().isEmpty()
            && isBillingInfoValid(mSo_pack_express.getBilling_add_inf1_view(), binding.mketAddInfo1)
            && isBillingInfoValid(mSo_pack_express.getBilling_add_inf2_view(), binding.mketAddInfo2)
            && isBillingInfoValid(mSo_pack_express.getBilling_add_inf3_view(), binding.mketAddInfo3)
            && isPackServiceAdditionValid();

    }

    private boolean isPackServiceAdditionValid() {
        return hasPackServicesAdded() || mSo_pack_express.getAdd_pack_service() == 0;
    }

    private boolean isBillingInfoValid(String billingInfoView, MKEditTextNM billingField) {
        boolean ret = !ConstantBaseApp.MASK_VIEW_TYPE_REQUIRED.equals(billingInfoView)
            || (billingField.getText() != null && !billingField.getText().toString().trim().isEmpty());

        return ret;
    }

    private void handleSerialIdCharConstraints() {
        String serial_id = binding.mketSerial.getText().toString();
        binding.mketSerial.setText(ToolBox_Inf.removeForbidenChars(serial_id));
    }

    @Override
    public void addWsAuxResult(HMAux auxResult) {
        wsAuxResult.add(auxResult);
    }

    @Override
    public void disablePartnerSelector() {
        binding.ssPartner.setmEnabled(false);
        binding.tilPartner.setVisibility(View.GONE);
        binding.mketPack.requestFocus();
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
        setFieldsBundle(bundle);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();

    }

    private void setFieldsBundle(Bundle bundle) {
        if(mSo_pack_express != null) {
            bundle.putString(EXPRESS_PACK_CODE, mSo_pack_express.getExpress_code()); //mdProduct != null ? mdProduct.getProduct_id() : "");
        }
        if(md_partner != null ) {
            bundle.putString(MD_PartnerDao.PARTNER_CODE, String.valueOf(md_partner.getPartner_code()));
        }
        bundle.putString(SO_Pack_Express_LocalDao.BILLING_ADD_INF1_VALUE,binding.mketAddInfo1.getText().toString().trim());
        bundle.putString(SO_Pack_Express_LocalDao.BILLING_ADD_INF2_VALUE,binding.mketAddInfo2.getText().toString().trim());
        bundle.putString(SO_Pack_Express_LocalDao.BILLING_ADD_INF3_VALUE,binding.mketAddInfo3.getText().toString().trim());
        if(bundle_express_tmp >0){
            bundle.putLong(SO_Pack_Express_LocalDao.EXPRESS_TMP,bundle_express_tmp);
        }
    }

    @Override
    public void automationCleanForm() {
        //Reseta infos do serial
        binding.mketSerial.setText("");
        binding.clSerial.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_error));
        binding.clSerialSearch.setVisibility(View.GONE);
        binding.clSerialSearchBtn.setEnabled(true);
        //Reseta infos de billing
        binding.mketAddInfo1.setText("");
        binding.mketAddInfo2.setText("");
        binding.mketAddInfo3.setText("");
        bundle_express_tmp =-1;
        //LUCHE - 30/11/2021 -
        //Os dados de pacote agora são mantidos após o save, então chama metodo que revalida campos
        //configura helper do serial e libera lupa de busca.
        //Add var para caso campo null, não crashar(acontecia se clicasse na nuvem de enviar sem ter
        // conexão e sem pack definido) Tratado tando aqui quando no clique da nuvem offline.
        if(mSo_pack_express != null) {
            loadSO_Pack_Express(mSo_pack_express, mSo_pack_express.getExpress_code());
        }
        //Da foco no serial
        binding.mketSerial.requestFocus();
        wsAuxResult.clear();
        exitProcess = false;
        //Chama Refresh do ultimo item enviado.
        mPresenter.getLastExpressInfoInSiteOper();
    }

    @Override
    public void onBackPressed() {
        handleSerialIdCharConstraints();
        //
        mPresenter.onBackPressedClicked(
            mSo_pack_express,
                hasPackServicesAdded(),
            ToolBox_Inf.removeAllLineBreaks(binding.mketSerial.getText().toString()),
            false
        );
    }

    private boolean hasPackServicesAdded() {
        if(mSo_pack_express != null && mSo_pack_express.getAdd_pack_service() == 1){
            return mAdapter!= null && mAdapter.getItemCount() > 0;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //Se tem serial ou expressa pendente, exibe icone.
        if(mPresenter.hasSerialOrExpressOsPendency()){
            Drawable wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context,R.drawable.ic_cloud_upload));
            DrawableCompat.setTint(wrappedDrawable.mutate(), ContextCompat.getColor(context, R.color.namoa_cancel_red));
            //
            menu.add(0, 1, Menu.FIRST, hmAux_Trans.get("menu_sync_express_lbl"));
            menu.getItem(0).setIcon(wrappedDrawable);
            menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            if(ToolBox_Con.isOnline(context)) {
                if (mPresenter.hasSerialUpdateRequired()) {
                    mPresenter.executeSerialSave();
                } else {
                    mPresenter.executeSO_Pack_Express_Local();
                }
            }else{
                ToolBox_Inf.showNoConnectionDialog(context);
            }
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
            //Atualiza item de nuvem na toolbar
            invalidateOptionsMenu();
            //
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
            handleSerialIdCharConstraints();
            mPresenter.extractSearchResult(md_product, ToolBox_Inf.removeAllLineBreaks(binding.mketSerial.getText().toString()), mLink);
        }else if(wsProcess.equals(WS_Serial_Save.class.getName())){
            mPresenter.processSerialSaveResult(hmAux);
            mPresenter.executeSO_Pack_Express_Local();
        }else if(wsProcess.equals(WS_SO_Service_Search.class.getName())){
            progressDialog.dismiss();
            Gson gson = new GsonBuilder().create();
            if(mLink.isEmpty()){
                setSoPackExpressLocal();
                callAct091();
            }else{
                SoPackExpressPacksLocal soPackExpressPacksLocal = gson.fromJson(mLink, SoPackExpressPacksLocal.class);
                if(soPackExpressPacksLocal!=null){
                    callBottomSheet(soPackExpressPacksLocal, 0);
                }
            }
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
                        callAct005(context);
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
        int okSoCounter = 0;
        for (String sKey : so_express.keySet()) {
            HMAux hmAux = new HMAux();
            //
            String[] sParts = sKey.split(Constant.MAIN_CONCAT_STRING);
            String status = so_express.get(sKey);
            //Se erro, gera item do extrato, se não, somente  contador de OK
            if(!ConstantBaseApp.MAIN_RESULT_OK.equals(status)) {
                String sFinal = sParts[0] + "\n" + sParts[1] + "\n" + sParts[2];

                //hmAux.put("so_express_code", sFinal);
                //hmAux.put("so_express_result", so_express.get(sKey));

                hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("express_order_ttl"));

                hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, hmAux_Trans.get("express_label"));
                hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, sParts[0] + "   -   " + sParts[2]);

                hmAux.put(Generic_Results_Adapter.LABEL_ITEM_2, hmAux_Trans.get("express_desc"));
                hmAux.put(Generic_Results_Adapter.VALUE_ITEM_2, sParts[1]);

                hmAux.put(Generic_Results_Adapter.LABEL_ITEM_3, hmAux_Trans.get("express_status"));
                hmAux.put(Generic_Results_Adapter.VALUE_ITEM_3, status);

                addWsAuxResult(hmAux);
            }else{
                okSoCounter++;
            }
        }
        //Se itens com erro, chama showResultsDialog
        //Se itens OK exibe toast
        //Se nenhum dos dois, chama showResultsDialog
        if(wsAuxResult != null && wsAuxResult.size() > 0) {
            showResultsDialog();
        }else if(okSoCounter > 0){
            showMsgToast(hmAux_Trans.get("toast_express_successfully_saved_msg"));
        }else{
            //Esse else não faz sentido, mas ja era asism antes, se nada no form, chamava esse metodo
            //então, continua assim
            showResultsDialog();
        }
    }

    public void showResultsDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView)  view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView)  view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button)  view.findViewById(R.id.act028_dialog_btn_ok);

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
        invalidateOptionsMenu();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        automationCleanForm();
    }

    public void callAct042(Context context) {
        Intent mIntent = new Intent(context, Act042_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();

        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT040);
        setFieldsBundle(bundle);
        bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, binding.mketSerial.getText().toString().trim());

        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

}
