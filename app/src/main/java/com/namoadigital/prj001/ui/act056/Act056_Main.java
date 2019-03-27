package com.namoadigital.prj001.ui.act056;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Button;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act056_Main extends Base_Activity implements Act056_Main_Contract.I_View {

    private Act056_Main_Presenter mPresenter;
    private SearchableSpinner ss_zone;
    private SearchableSpinner ss_local;
    private MKEditTextNM mket_inbound;
    private MKEditTextNM mket_invoice;
    private Button btn_search;
    private Button btn_creation;
    private Button btn_pendencies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act056_main);
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
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT056
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act056_title");
        transList.add("zone_hint");
        transList.add("local_hint");
        transList.add("inbound_code_or_id_hint");
        transList.add("invoince_hint");
        transList.add("btn_search_inbound");
        transList.add("btn_new_inbound");
        transList.add("btn_pendencies");
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
        mPresenter = new Act056_Main_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        bindViews();
        //
        configSS();

    }

    private void bindViews() {
        ss_zone = findViewById(R.id.act056_ss_zone);
        ss_local = findViewById(R.id.act056_ss_local);
        mket_inbound = findViewById(R.id.act056_mket_inbound_id);
        mket_invoice = findViewById(R.id.act056_mket_invoice);
        btn_search = findViewById(R.id.act056_btn_inbound_search);
        btn_creation = findViewById(R.id.act056_btn_inbound_creation);
        btn_pendencies = findViewById(R.id.act056_btn_inbound_pendency);
        //Seta Traduções
        ss_zone.setmHint(hmAux_Trans.get("zone_hint"));
        ss_local.setmHint(hmAux_Trans.get("local_hint"));
        mket_inbound.setHint(hmAux_Trans.get("inbound_code_or_id_hint"));
        mket_invoice.setHint(hmAux_Trans.get("invoince_hint"));
        btn_search.setText(hmAux_Trans.get("btn_search_inbound"));
        btn_creation.setText(hmAux_Trans.get("btn_new_inbound"));
        btn_pendencies.setText(hmAux_Trans.get("btn_pendencies"));
        //Add componentes nas listas da base act
        controls_ss.add(ss_zone);
        controls_ss.add(ss_local);
        controls_sta.add(mket_inbound);
        controls_sta.add(mket_invoice);
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

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT056;
        mAct_Title = Constant.ACT056 + "_" + "title";
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


    }

    private void processLocalValueChange(HMAux hmAux) {
        if(!ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)){
            mPresenter.loadZoneSS(ss_zone,false,true);
            //
            ToolBox_Inf.setSSmValue(
                    ss_zone,
                    hmAux.get(MD_Site_ZoneDao.ZONE_CODE),
                    hmAux.get(MD_Site_ZoneDao.ZONE_ID),
                    hmAux.get(MD_Site_ZoneDao.ZONE_DESC),
                    false
            );
            //
            mPresenter.loadLocalSS(ss_zone,ss_local,false);
        }
    }

    /**
     * Metodo chamado quando o valor do spinner de site é alterado, seja via leitura do barcode ou
     * via mudança via spinner.
     * @param hmAux
     */
    private void processZoneValueChange(HMAux hmAux) {
        mPresenter.loadLocalSS(ss_zone,ss_local,true);
        //
        if (hmAux != null && hmAux.size() > 0 && ss_local.getmOption().size() == 1) {
            ss_local.setmValue(ss_local.getmOption().get(0));
        }
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
        mPresenter.onBackPressedClicked();
    }
}
