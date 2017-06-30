package com.namoadigital.prj001.ui.act023;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.ui.act022.Act022_Main;
import com.namoadigital.prj001.ui.act024.Act024_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 22/06/2017.
 */

public class Act023_Main extends Base_Activity implements Act023_Main_View {

    private Act023_Main_Presenter mPresenter;
    private Bundle bundle;
    private String requesting_process;
    private TextView tv_product_ttl;
    private TextView tv_product_code_label;
    private TextView tv_product_code_value;
    private TextView tv_product_id_label;
    private TextView tv_product_id_value;
    private TextView tv_product_desc_label;
    private TextView tv_product_desc_value;
    private MKEditTextNM mket_serial_id;
    private TextView tv_required_lbl;
    private TextView tv_required_val;
    private TextView tv_allow_new_lbl;
    private TextView tv_allow_new_val;
    private LinearLayout ll_require_serial;
    private LinearLayout ll_serial_full_desc;
    private LinearLayout ll_serial_location;
    private TextView tv_serial_ttl;
    private TextView tv_serial_location_ttl;
    private SearchableSpinner ss_site;
    private SearchableSpinner ss_site_zone;
    private SearchableSpinner ss_site_zone_local;
    private LinearLayout ll_serial_add_info;
    private TextView tv_serial_add_info_ttl;
    private TextView tv_info1;
    private TextView tv_info2;
    private TextView tv_info3;
    private TextView tv_serial_properties_ttl;
    private LinearLayout ll_serial_properties;
    private SearchableSpinner ss_brand;
    private SearchableSpinner ss_brand_model;
    private SearchableSpinner ss_brand_color;
    private SearchableSpinner ss_segment;
    private SearchableSpinner ss_category_price;
    private SearchableSpinner ss_site_owner;

    private Button btn_action;

    private MD_Product product;

    private long product_code;
    private int serial_required;
    private int serial_allow_new;
    //agendamento
    private boolean isSchedule;


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

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT023
        );

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act023_title");
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
        transList.add("alert_no_form_for_operation_ttl");
        transList.add("alert_no_form_for_operation_msg");
        transList.add("btn_create");
        //
        transList.add("serial_ttl");
        transList.add("serial_location_ttl");
        transList.add("site_lbl");
        transList.add("site_zone_lbl");
        transList.add("site_zone_local_lbl");
        transList.add("serial_add_info_ttl");
        transList.add("add_info1_lbl");
        transList.add("add_info2_lbl");
        transList.add("add_info3_lbl");
        transList.add("serial_properties_ttl");
        transList.add("brand_lbl");
        transList.add("brand_model_lbl");
        transList.add("brand_color_lbl");
        transList.add("segment_lbl");
        transList.add("category_price_lbl");
        transList.add("site_owner_lbl");
        transList.add("btn_so_search");
        transList.add("progress_so_search_ttl");
        transList.add("progress_so_search_msg");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        //Variavel q identifica se dados do produto são chamados do master data ou não.
        isSchedule = false;
        //
        recoverIntentsInfo();
        //
        mPresenter = new Act023_Main_Presenter_Impl(
                context,
                this,
                requesting_process,
                bundle,
                new Sync_ChecklistDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                product_code,
                hmAux_Trans,
                new GE_Custom_Form_OperationDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                isSchedule
        );
        //
        mket_serial_id = (MKEditTextNM) findViewById(R.id.act023_mket_serial);
        mket_serial_id.setmNFC(true);
        controls_sta.add(mket_serial_id);
        mket_serial_id.setHint(hmAux_Trans.get("mket_search_hint"));
        //
        tv_product_ttl = (TextView) findViewById(R.id.act023_tv_product_ttl);
        tv_product_ttl.setTag("product_ttl");
        //
        tv_product_code_label = (TextView) findViewById(R.id.act023_tv_product_code_lbl);
        tv_product_code_label.setTag("product_label");
        //
        tv_product_id_label = (TextView) findViewById(R.id.act023_tv_product_id_lbl);
        tv_product_id_label.setTag("product_id_label");
        //
        tv_product_desc_value = (TextView) findViewById(R.id.act023_tv_product_desc_value);
        tv_product_desc_value.setTag("product_desc_label");
        //
        ll_require_serial = (LinearLayout) findViewById(R.id.act023_ll_require_serial);
        //
        tv_required_lbl = (TextView) findViewById(R.id.act023_tv_require_serial_lbl);
        tv_required_lbl.setTag("chk_required");
        //
        tv_required_val = (TextView) findViewById(R.id.act023_tv_require_serial_val);
        //
        tv_allow_new_lbl = (TextView) findViewById(R.id.act023_tv_allow_lbl);
        tv_allow_new_lbl.setTag("chk_allow_new");
        //
        tv_allow_new_val = (TextView) findViewById(R.id.act023_tv_allow_val);
        //
        tv_serial_ttl = (TextView) findViewById(R.id.act023_tv_serial_ttl);
        tv_serial_ttl.setTag("serial_ttl");
        //
        ll_serial_full_desc = (LinearLayout) findViewById(R.id.act023_ll_serial_full_desc);
        //
        ll_serial_location = (LinearLayout) findViewById(R.id.act023_ll_serial_location);
        //
        tv_serial_location_ttl = (TextView) findViewById(R.id.act023_tv_serial_location_ttl);
        tv_serial_location_ttl.setTag("serial_location_ttl");
        //
        ss_site = (SearchableSpinner) findViewById(R.id.act023_ss_site);
        ss_site.setTag("site_lbl");
        //
        ss_site_zone = (SearchableSpinner) findViewById(R.id.act023_ss_site_zone);
        ss_site_zone.setTag("site_zone_lbl");
        //
        ss_site_zone_local = (SearchableSpinner) findViewById(R.id.act023_ss_site_zone_local);
        ss_site_zone_local.setTag("site_zone_local_lbl");
        //
        ll_serial_add_info = (LinearLayout) findViewById(R.id.act023_ll_serial_add_info);
        //
        tv_serial_add_info_ttl = (TextView) findViewById(R.id.act023_tv_serial_add_info_ttl);
        tv_serial_add_info_ttl.setTag("serial_add_info_ttl");
        //
        tv_info1 = (TextView) findViewById(R.id.act023_tv_info1);
        tv_info1.setTag("add_info1_lbl");
        //
        tv_info2 = (TextView) findViewById(R.id.act023_tv_info2);
        tv_info2.setTag("add_info2_lbl");
        //
        tv_info3 = (TextView) findViewById(R.id.act023_tv_info3);
        tv_info3.setTag("add_info3_lbl");
        //
        ll_serial_properties = (LinearLayout) findViewById(R.id.act023_ll_serial_properties);
        //
        tv_serial_properties_ttl = (TextView) findViewById(R.id.act023_tv_serial_properties_ttl);
        tv_serial_properties_ttl.setTag("serial_properties_ttl");
        //
        ss_brand = (SearchableSpinner) findViewById(R.id.act023_ss_brand);
        ss_brand.setTag("brand_lbl");
        //
        ss_brand_model = (SearchableSpinner) findViewById(R.id.act023_ss_brand_model);
        ss_brand_model.setTag("brand_model_lbl");
        //
        ss_brand_color = (SearchableSpinner) findViewById(R.id.act023_ss_brand_color);
        ss_brand_color.setTag("brand_color_lbl");
        //
        ss_segment = (SearchableSpinner) findViewById(R.id.act023_ss_segment);
        ss_segment.setTag("segment_lbl");
        //
        ss_category_price = (SearchableSpinner) findViewById(R.id.act023_ss_category_price);
        ss_category_price.setTag("category_price_lbl");
        //
        ss_site_owner = (SearchableSpinner) findViewById(R.id.act023_ss_site_owner);
        ss_site_owner.setTag("site_owner_lbl");
        //
        btn_action = (Button) findViewById(R.id.act023_btn_action);
        btn_action.setTag("btn_action_lbl");
        //
        //Adiciona Views na lista de tradução
        views.add(tv_product_ttl);
//        views.add(tv_product_code_label);
//        views.add(tv_product_id_label);
//        views.add(tv_product_desc_label);
        views.add(tv_required_lbl);
        views.add(tv_allow_new_lbl);
        views.add(tv_serial_ttl);
        views.add(tv_serial_location_ttl);
        views.add(ss_site);
        views.add(ss_site_zone);
        views.add(ss_site_zone_local);
        views.add(tv_serial_add_info_ttl);
        views.add(tv_info1);
        views.add(tv_info2);
        views.add(tv_info3);
        views.add(tv_serial_properties_ttl);
        views.add(ss_brand);
        views.add(ss_brand_model);
        views.add(ss_brand_color);
        views.add(ss_segment);
        views.add(ss_category_price);
        views.add(ss_site_owner);
        views.add(btn_action);

        //
        layoutConfiguration();

    }

    private void layoutConfiguration() {
        switch (requesting_process){

            case Constant.MODULE_CHECKLIST:
                ll_serial_full_desc.setVisibility(View.GONE);
                ll_require_serial.setVisibility(View.VISIBLE);
                btn_action.setText(hmAux_Trans.get("btn_create"));
                break;
            case Constant.MODULE_SO:default:
                ll_serial_full_desc.setVisibility(View.VISIBLE);
                ll_require_serial.setVisibility(View.GONE);
                btn_action.setText(hmAux_Trans.get("btn_so_search"));
                break;

        }
    }

    private void recoverIntentsInfo() {

        bundle = getIntent().getExtras();
        //
        if(bundle != null){
            if(bundle.containsKey(Constant.MAIN_REQUESTING_PROCESS)){
                requesting_process = bundle.getString(Constant.MAIN_REQUESTING_PROCESS,"");
                product_code = Long.parseLong(bundle.getString(Constant.MAIN_PRODUCT_CODE,"0"));
                isSchedule = bundle.getBoolean(Constant.MAIN_IS_SCHEDULE,false);
            }else{
                /**
                 *
                 *
                 *
                 *  TRATAR CASO NÃO RECEBA O PARAMETRO DE REQUESTING PROCESS
                 *
                 *
                 *
                 *
                 */
            }

        }else{
            /**
             *
             *
             *
             *  TRATAR CASO NÃO RECEBA BUNDLE
             *
             *
             *
             *
             */

        }



    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT023;
        mAct_Title = Constant.ACT023 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value =  hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl =  hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value =  hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value =Constant.PRJ001_VERSION;

        //Aplica informações do rodapé -fim


    }

    @Override
    public void setProductValues(MD_Product md_product) {
        product = md_product;
        //
        tv_product_code_label.setText(
                hmAux_Trans.get("product_label")+" "+
                        String.valueOf(md_product.getProduct_code())

        );
        tv_product_id_label.setText(
                hmAux_Trans.get("product_id_label")+" "+
                        md_product.getProduct_id());
        tv_product_desc_value.setText(md_product.getProduct_desc());
        //
        serial_required = md_product.getRequire_serial();
        serial_allow_new = md_product.getAllow_new_serial_cl();
        //
        tv_required_val.setText("("+hmAux_Trans.get("NO").toUpperCase()+")");
        if( md_product.getRequire_serial() == 1){
            tv_required_val.setText("("+hmAux_Trans.get("YES").toUpperCase()+")");
        }
        //
        tv_allow_new_val.setText("("+hmAux_Trans.get("NO").toUpperCase()+")");
        if( md_product.getAllow_new_serial_cl() == 1){
            tv_allow_new_val.setText("("+hmAux_Trans.get("YES").toUpperCase()+")");
        }
    }

    private void initActions() {
        //
        mPresenter.getProductInfo();
        //
        ss_site.setmEnabled(false);
        //
        ss_site_zone.setmEnabled(false);
        //
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.validadeSerialFlow(
                        mket_serial_id.getText().toString(),
                        serial_required,
                        serial_allow_new
                );
            }
        });

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
    public void fieldFocus() {
        mket_serial_id.requestFocus();
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
    public void continueOffline() {

    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);

        switch (requesting_process){

            case Constant.MODULE_CHECKLIST:
                //mView.callAct008(context,product_code);
                break;

            case Constant.MODULE_SO:default:
                Gson gson = new GsonBuilder().serializeNulls().create();
                ArrayList<SM_SO> sos = gson.fromJson(
                        mLink,
                        new TypeToken<ArrayList<SM_SO>>() {
                        }.getType());

                if(sos != null){
                   mPresenter.defineForwardFlow(mLink);
                }
                break;
        }
        progressDialog.dismiss();

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
    public void callAct024(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act024_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
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
}
