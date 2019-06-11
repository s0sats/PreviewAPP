package com.namoadigital.prj001.ui.act065;

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
//                callAct067();
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
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT056);
//                    bundle.putBoolean(Act057_Main.LIST_PENDENCIES_KEY,true);
                    //
//                    callAct057(bundle);
                }
            }
        });
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
    public void showAlert(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
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
