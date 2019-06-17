package com.namoadigital.prj001.ui.act065;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Outbound_Search;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act066.Act066_Main;
import com.namoadigital.prj001.ui.act067.Act067_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act065_Main extends Base_Activity implements Act065_Main_Contract.I_View {

    private Act065_Main_Presenter mPresenter;
    private SearchableSpinner ss_zone;
    private SearchableSpinner ss_local;
    private MKEditTextNM mket_outbound;
    private MKEditTextNM mket_invoice;
    private Button btn_search;
    private Button btn_creation;
    private Button btn_pendencies;
    private String wsProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act065_main);
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
        ss_zone.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
                                              @Override
                                              public void onItemPreSelected(HMAux hmAux) {

                                              }

                                              @Override
                                              public void onItemPostSelected(HMAux hmAux) {
                                                  processZoneValueChange(hmAux);
                                              }
                                          }
        );
        //
        ss_zone.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                processZoneValueChange(hmAux);
            }
        });
        //
        ss_local.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                processLocalValueChange(hmAux);
            }
        });
        //
        ss_local.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                processLocalValueChange(hmAux);
            }
        });
        //
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter.checkSearchParamFilled(ss_zone, ss_local, mket_outbound, mket_invoice)) {
                    mPresenter.checkSearchFlow();
                } else {
                    showAlert(
                            hmAux_Trans.get("alert_fill_search_field_ttl"),
                            hmAux_Trans.get("alert_fill_search_field_msg")
                    );
                }
            }
        });
        //
        btn_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAct067();
            }
        });

        btn_pendencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter.getOutboundPendencies().equalsIgnoreCase("0")) {
                    showAlert(
                            hmAux_Trans.get("alert_no_pendencies_ttl"),
                            hmAux_Trans.get("alert_no_pendencies_msg")
                    );
                } else {
                    Bundle bundle = new Bundle();
                    //
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT066);
                    bundle.putBoolean(Act066_Main.LIST_PENDENCIES_KEY,true);
                    //
                    callAct066(bundle);
                }
            }
        });
    }

    private void callAct067() {
        Intent mIntent = new Intent(context, Act067_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, Constant.IO_OUTBOUND);
        bundle.putBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY, true);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT065
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void processLocalValueChange(HMAux hmAux) {
        if (!ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            mPresenter.loadZoneSS(ss_zone, false, true);
            //
            ToolBox_Inf.setSSmValue(
                    ss_zone,
                    hmAux.get(MD_Site_ZoneDao.ZONE_CODE),
                    hmAux.get(MD_Site_ZoneDao.ZONE_ID),
                    hmAux.get(MD_Site_ZoneDao.ZONE_DESC),
                    false
            );
            //
            mPresenter.loadLocalSS(ss_zone, ss_local, false);
        }
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if(wsProcess.equals(WS_IO_Outbound_Search.class.getName())){
            progressDialog.dismiss();
            //
            mPresenter.processSearchReturn(mLink);
//        }else if(wsProcess.equals(WS_IO_Outbound_Item_Save.class.getName())){
//            progressDialog.dismiss();
//            //
//            mPresenter.processOutboundItemReturn(mLink);
        }else{
            progressDialog.dismiss();
        }
    }

    //region Handling WS Errors
    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        disableProgressDialog();
    }


    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
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
    //endregion

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    @Override
    public void showAlert(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void callSearchOutbound() {
        mPresenter.executeOutboundSearch(
                ss_zone.getmValue().get(SearchableSpinner.CODE),
                ss_local.getmValue().get(SearchableSpinner.CODE),
                mket_outbound.getText().toString().trim(),
                mket_invoice.getText().toString().trim()
        );
    }

    @Override
    public void showPD(String dialog_outbound_search_ttl, String dialog_outbound_search_start) {
        enableProgressDialog(
                dialog_outbound_search_ttl,
                dialog_outbound_search_start,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct066(Bundle bundle) {
        Intent mIntent = new Intent(context, Act066_Main.class);

        if(bundle != null ){
            mIntent.putExtras(bundle);
        }

        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act065_title");
        transList.add("zone_hint");
        transList.add("local_hint");
        transList.add("outbound_code_or_id_hint");
        transList.add("invoince_hint");
        transList.add("btn_search_outbound");
        transList.add("btn_new_outbound");
        transList.add("btn_pendencies");
        transList.add("dialog_outbound_search_ttl");
        transList.add("dialog_outbound_search_start");
        transList.add("alert_no_pendencies_ttl");
        transList.add("alert_no_pendencies_msg");
        transList.add("alert_fill_search_field_ttl");
        transList.add("alert_fill_search_field_msg");
        transList.add("alert_error_on_processing_return_ttl");
        transList.add("alert_error_on_processing_return_msg");
        transList.add("alert_no_outbound_found_ttl");
        transList.add("alert_no_outbound_found_msg");
        transList.add("outbound_lbl");
        transList.add("alert_outbound_results_ttl");
        transList.add("progress_save_outbound_item_ttl");
        transList.add("progress_save_outbound_item_msg");
        transList.add("outbound_item_ret_empty_ttl");
        transList.add("outbound_item_ret_empty_msg");
        transList.add("alert_no_inbound_found_ttl");
        transList.add("alert_no_inbound_found_msg");
        transList.add("dialog_inbound_search_ttl");
        transList.add("dialog_inbound_search_start");
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
        mPresenter = new Act065_Main_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        bindViews();
        //
        setupViews();
        //
        configSS();

    }

    private void configSS() {
        ss_zone.setmShowBarcode(true);
        //mPresenter.setDefaultZone(ss_zone);
        mPresenter.loadZoneSS(ss_zone, true, false);
        //
        ss_local.setmShowLabel(false);
        ss_local.setmShowBarcode(true);
        mPresenter.loadLocalSS(ss_zone, ss_local, false);
        //Força validação que, caso só encontre um valor, preenche local.
        processZoneValueChange(ss_zone.getmValue());
    }

    private void setupViews() {
        ss_zone.setmHint(hmAux_Trans.get("zone_hint"));
        ss_local.setmHint(hmAux_Trans.get("local_hint"));
        mket_outbound.setHint(hmAux_Trans.get("outbound_code_or_id_hint"));
        mket_invoice.setHint(hmAux_Trans.get("invoince_hint"));
        btn_search.setText(hmAux_Trans.get("btn_search_outbound"));
        btn_creation.setText(hmAux_Trans.get("btn_new_outbound"));
        btn_creation.setVisibility(View.GONE);
        btn_pendencies.setText(setBtnPendenciesQty());
        //
        //Se profile de criação de outbound, exibi btn
        if (ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_IO, ConstantBaseApp.PROFILE_MENU_IO_PARAM_INBOUND_NEW)) {
            btn_creation.setVisibility(View.VISIBLE);
        }
        //Add componentes nas listas da base act
        controls_ss.add(ss_zone);
        controls_ss.add(ss_local);
        controls_sta.add(mket_outbound);
        controls_sta.add(mket_invoice);
    }

    private String setBtnPendenciesQty() {
        return hmAux_Trans.get("btn_pendencies")
                + " (" + mPresenter.getOutboundPendencies() + ")";
    }

    private void bindViews() {
        ss_zone = findViewById(R.id.act065_ss_zone);
        ss_local = findViewById(R.id.act065_ss_local);
        mket_outbound = findViewById(R.id.act065_mket_outbound_id);
        mket_invoice = findViewById(R.id.act065_mket_invoice);
        btn_search = findViewById(R.id.act065_btn_outbound_search);
        btn_creation = findViewById(R.id.act065_btn_outbound_creation);
        btn_pendencies = findViewById(R.id.act065_btn_outbound_pendency);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT065;
        mAct_Title = Constant.ACT065 + "_" + "title";
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

    /**
     * Metodo chamado quando o valor do spinner de site é alterado, seja via leitura do barcode ou
     * via mudança via spinner.
     *
     * @param hmAux
     */
    private void processZoneValueChange(HMAux hmAux) {
        mPresenter.loadLocalSS(ss_zone, ss_local, true);
        //
        if (hmAux != null && hmAux.size() > 0 && ss_local.getmOption().size() == 1) {
            ss_local.setmValue(ss_local.getmOption().get(0));
        }
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }
}
