package com.namoadigital.prj001.ui.act056;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Inbound_Search;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act057.Act057_Main;
import com.namoadigital.prj001.ui.act061.Act061_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
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
    private String wsProcess;

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
        transList.add("dialog_inbound_search_ttl");
        transList.add("dialog_inbound_search_start");
        transList.add("alert_no_pendencies_ttl");
        transList.add("alert_no_pendencies_msg");
        transList.add("alert_fill_search_field_ttl");
        transList.add("alert_fill_search_field_msg");
        transList.add("alert_error_on_processing_return_ttl");
        transList.add("alert_error_on_processing_return_msg");
        transList.add("alert_no_inbound_found_ttl");
        transList.add("alert_no_inbound_found_msg");
        transList.add("inbound_lbl");
        transList.add("alert_inbound_results_ttl");
        transList.add("progress_save_inbound_item_ttl");
        transList.add("progress_save_inbound_item_msg");
        transList.add("inbound_item_ret_empty_ttl");
        transList.add("inbound_item_ret_empty_msg");
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
        setupViews();
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
    }

    private void setupViews() {
        //Seta Traduções
        ss_zone.setmHint(hmAux_Trans.get("zone_hint"));
        ss_local.setmHint(hmAux_Trans.get("local_hint"));
        mket_inbound.setHint(hmAux_Trans.get("inbound_code_or_id_hint"));
        mket_invoice.setHint(hmAux_Trans.get("invoince_hint"));
        btn_search.setText(hmAux_Trans.get("btn_search_inbound"));
        btn_creation.setText(hmAux_Trans.get("btn_new_inbound"));
        btn_creation.setVisibility(View.GONE);
        btn_pendencies.setText(setBtnPendenciesQty());
        //
        //Se profile de criação de inbound, exibi btn
        if(ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_IO,ConstantBaseApp.PROFILE_MENU_IO_PARAM_INBOUND_NEW)) {
            btn_creation.setVisibility(View.VISIBLE);
        }
        //Add componentes nas listas da base act
        controls_ss.add(ss_zone);
        controls_ss.add(ss_local);
        controls_sta.add(mket_inbound);
        controls_sta.add(mket_invoice);
    }

    private String setBtnPendenciesQty() {
        return hmAux_Trans.get("btn_pendencies")
                +" ("+mPresenter.getInboundPendencies()+")";
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
        //
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPresenter.checkSearchParamFilled(ss_zone,ss_local,mket_inbound,mket_invoice)){
                    mPresenter.checkSearchFlow();
                }else{
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
                callAct061();
            }
        });
        //
        btn_pendencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPresenter.getInboundPendencies().equalsIgnoreCase("0")){
                    showAlert(
                            hmAux_Trans.get("alert_no_pendencies_ttl"),
                            hmAux_Trans.get("alert_no_pendencies_msg")
                    );
                }else{
                    Bundle bundle = new Bundle();
                    //
                    bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT056);
                    bundle.putBoolean(Act057_Main.LIST_PENDENCIES_KEY,true);
                    //
                   callAct057(bundle);
                }
            }
        });

    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
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

    //region WsInboundItem
    @Override
    public void callSearchInbound() {
        mPresenter.executeInboundSearch(
            ss_zone.getmValue().get(SearchableSpinner.CODE),
            ss_local.getmValue().get(SearchableSpinner.CODE),
            mket_inbound.getText().toString().trim(),
            mket_invoice.getText().toString().trim()
        );
    }

    @Override
    public void showResult(ArrayList<HMAux> resultList, final boolean hasError) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = view.findViewById(R.id.act028_dialog_btn_ok);

        //trad
        tv_title.setText(hmAux_Trans.get("alert_inbound_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        lv_results.setAdapter(
            new Generic_Results_Adapter(
                context,
                resultList,
                Generic_Results_Adapter.CONFIG_MENU_SEND_RET,
                hmAux_Trans
            )
        );
        //
        builder.setView(view);
        builder.setCancelable(false);
        //
        final AlertDialog show = builder.show();
        //
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                show.dismiss();
                //
                if(!hasError){
                    callSearchInbound();
                }
            }
        });

    }
    //endregion

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct057(Bundle bundle) {
        Intent mIntent = new Intent(context, Act057_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    private void callAct061() {
        Intent mIntent = new Intent(context, Act061_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, Constant.IO_INBOUND);
        bundle.putBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY,true);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
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
        if(wsProcess.equals(WS_IO_Inbound_Search.class.getName())){
            progressDialog.dismiss();
            //
            mPresenter.processSearchReturn(mLink);
        }else if(wsProcess.equals(WS_IO_Inbound_Item_Save.class.getName())){
            progressDialog.dismiss();
            //
            mPresenter.processInboundItemReturn(mLink);
        }else{
            progressDialog.dismiss();
        }
    }

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
