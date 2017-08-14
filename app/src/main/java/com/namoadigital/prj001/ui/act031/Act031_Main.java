package com.namoadigital.prj001.ui.act031;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.MD_Category_PriceDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.sql.MD_Brand_Color_Sql_SS;
import com.namoadigital.prj001.sql.MD_Brand_Model_Sql_SS;
import com.namoadigital.prj001.sql.MD_Brand_Sql_SS;
import com.namoadigital.prj001.sql.MD_Category_Price_Sql_SS;
import com.namoadigital.prj001.sql.MD_Segment_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.ui.act030.Act030_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.dao.MD_Product_SerialDao.SITE_CODE_OWNER;
import static com.namoadigital.prj001.ui.act023.Act023_Main.SITE_DESC_OWNER;


/**
 * Created by neomatrix on 03/07/17.
 */

public class Act031_Main extends Base_Activity implements Act031_Main_View {

    public static final String SO_WS_SEARCH_SERIAL = "SO_WS_SEARCH_SERIAL";
    public static final String SO_WS_SEARCH_SAVE = "SO_WS_SEARCH_SAVE";

    private Act031_Main_Presenter mPresenter;
    private ScrollView sv_serial;
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
    private EditText et_info1;
    private EditText et_info2;
    private EditText et_info3;
    private TextView tv_serial_properties_ttl;
    private LinearLayout ll_serial_properties;
    private SearchableSpinner ss_brand;
    private SearchableSpinner ss_brand_model;
    private SearchableSpinner ss_brand_color;
    private SearchableSpinner ss_segment;
    private SearchableSpinner ss_category_price;
    private SearchableSpinner ss_site_owner;
    private MD_Product product;
    private MD_Product_Serial serialObj;
    private long product_code;
    private String bundle_serial_id;
    private int serial_required;
    private int serial_allow_new;
    private String ws_process;
    private boolean skip_validation = false;
    private boolean serialInfoChanges = false;
    private ArrayList<Object> serialProperties;
    private Bundle bundle;
    private boolean new_serial;
    //
    private Button btn_action;
    //Listners do btnAction
    //private View.OnClickListener listnerSearchSerial;
    private View.OnClickListener listnerSearchSO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act031_main);

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
                Constant.ACT031
        );

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act031_title");
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
        transList.add("btn_serial_search");
        transList.add("btn_so_search");
        transList.add("progress_so_search_ttl");
        transList.add("progress_so_search_msg");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("alert_no_so_found_ttl");
        transList.add("alert_no_so_found_msg");
        transList.add("alert_save_serial_error_ttl");
        transList.add("alert_save_serial_error_msg");
        transList.add("alert_save_serial_return_ttl");
        transList.add("alert_save_serial_ok_msg");
        transList.add("alert_invalid_serial_local_ttl");
        transList.add("alert_invalid_serial_local_msg");
        transList.add("alert_no_data_changes_ttl");
        transList.add("alert_no_data_changes_msg");
        transList.add("progress_serial_save_ttl");
        transList.add("progress_serial_save_msg");
        //
        transList.add("dialog_results_ttl");
        transList.add("dialog_result_product_lbl");
        transList.add("dialog_result_serial_lbl");
        transList.add("dialog_result_msg_lbl");
        transList.add("alert_no_serial_return_msg");
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
        mPresenter = new Act031_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_Product_SerialDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                product_code,
                new_serial
        );
        //
        serialProperties = new ArrayList<>();
        //
        serialObj = new MD_Product_Serial();
        //
        sv_serial = (ScrollView) findViewById(R.id.act031_sv_serial);
        //
        mket_serial_id = (MKEditTextNM) findViewById(R.id.act031_mket_serial);
        mket_serial_id.setmNFC(true);
        controls_sta.add(mket_serial_id);
        mket_serial_id.setHint(hmAux_Trans.get("mket_search_hint"));
        //
        tv_product_ttl = (TextView) findViewById(R.id.act031_tv_product_ttl);
        tv_product_ttl.setTag("product_ttl");
        //
        tv_product_code_label = (TextView) findViewById(R.id.act031_tv_product_code_lbl);
        tv_product_code_label.setTag("product_label");
        //
        tv_product_id_label = (TextView) findViewById(R.id.act031_tv_product_id_lbl);
        tv_product_id_label.setTag("product_id_label");
        //
        tv_product_desc_value = (TextView) findViewById(R.id.act031_tv_product_desc_value);
        tv_product_desc_value.setTag("product_desc_label");
        //
        ll_require_serial = (LinearLayout) findViewById(R.id.act031_ll_require_serial);
        //
        tv_required_lbl = (TextView) findViewById(R.id.act031_tv_require_serial_lbl);
        tv_required_lbl.setTag("chk_required");
        //
        tv_required_val = (TextView) findViewById(R.id.act031_tv_require_serial_val);
        //
        tv_allow_new_lbl = (TextView) findViewById(R.id.act031_tv_allow_lbl);
        tv_allow_new_lbl.setTag("chk_allow_new");
        //
        tv_allow_new_val = (TextView) findViewById(R.id.act031_tv_allow_val);
        //
        tv_serial_ttl = (TextView) findViewById(R.id.act031_tv_serial_ttl);
        tv_serial_ttl.setTag("serial_ttl");
        //
        ll_serial_full_desc = (LinearLayout) findViewById(R.id.act031_ll_serial_full_desc);
        //
        ll_serial_location = (LinearLayout) findViewById(R.id.act031_ll_serial_location);
        //
        tv_serial_location_ttl = (TextView) findViewById(R.id.act031_tv_serial_location_ttl);
        tv_serial_location_ttl.setTag("serial_location_ttl");
        //
        ss_site = (SearchableSpinner) findViewById(R.id.act031_ss_site);
        ss_site.setmLabel(hmAux_Trans.get("site_lbl"));
        ss_site.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_site_zone = (SearchableSpinner) findViewById(R.id.act031_ss_site_zone);
        ss_site_zone.setmLabel(hmAux_Trans.get("site_zone_lbl"));
        ss_site_zone.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_site_zone_local = (SearchableSpinner) findViewById(R.id.act031_ss_site_zone_local);
        ss_site_zone_local.setmLabel(hmAux_Trans.get("site_zone_local_lbl"));
        ss_site_zone_local.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ll_serial_add_info = (LinearLayout) findViewById(R.id.act031_ll_serial_add_info);
        //
        tv_serial_add_info_ttl = (TextView) findViewById(R.id.act031_tv_serial_add_info_ttl);
        tv_serial_add_info_ttl.setTag("serial_add_info_ttl");
        //
        et_info1 = (EditText) findViewById(R.id.act031_et_info1);
        //
        et_info2 = (EditText) findViewById(R.id.act031_et_info2);
        //
        et_info3 = (EditText) findViewById(R.id.act031_et_info3);
        //
        ll_serial_properties = (LinearLayout) findViewById(R.id.act031_ll_serial_properties);
        //
        tv_serial_properties_ttl = (TextView) findViewById(R.id.act031_tv_serial_properties_ttl);
        tv_serial_properties_ttl.setTag("serial_properties_ttl");
        //
        ss_brand = (SearchableSpinner) findViewById(R.id.act031_ss_brand);
        ss_brand.setmLabel(hmAux_Trans.get("brand_lbl"));
        ss_brand.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_brand_model = (SearchableSpinner) findViewById(R.id.act031_ss_brand_model);
        ss_brand_model.setmLabel(hmAux_Trans.get("brand_model_lbl"));
        ss_brand_model.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_brand_color = (SearchableSpinner) findViewById(R.id.act031_ss_brand_color);
        ss_brand_color.setmLabel(hmAux_Trans.get("brand_color_lbl"));
        ss_brand_color.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_segment = (SearchableSpinner) findViewById(R.id.act031_ss_segment);
        ss_segment.setmLabel(hmAux_Trans.get("segment_lbl"));
        ss_segment.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_category_price = (SearchableSpinner) findViewById(R.id.act031_ss_category_price);
        ss_category_price.setmLabel(hmAux_Trans.get("category_price_lbl"));
        ss_category_price.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_site_owner = (SearchableSpinner) findViewById(R.id.act031_ss_site_owner);
        ss_site_owner.setmLabel(hmAux_Trans.get("site_owner_lbl"));
        ss_site_owner.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        btn_action = (Button) findViewById(R.id.act031_btn_action);
        //
        //Adiciona Views na lista de tradução
        views.add(tv_product_ttl);
        views.add(tv_required_lbl);
        views.add(tv_allow_new_lbl);
        views.add(tv_serial_ttl);
        views.add(tv_serial_location_ttl);
        views.add(tv_serial_properties_ttl);
        //
        //spinnersInitializer();
        //
        iniListners();
        //
        mPresenter.getProductInfo();
        //
        mket_serial_id.setText(bundle_serial_id);
        //
        if(new_serial) {
            mPresenter.saveNewSerialInfo(product_code,bundle_serial_id);
        }else{
            mPresenter.serialFlow(bundle_serial_id);
        }
        //
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT031;
        mAct_Title = Constant.ACT031 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value = hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl = hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value = hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé -fim

    }

    private void initActions() {
        //
        /*ss_brand.setmEnabled(false);
        ss_brand_model.setmEnabled(false);
        ss_brand_color.setmEnabled(false);
        //
        et_info1.setEnabled(false);
        et_info2.setEnabled(false);
        et_info3.setEnabled(false);
        //
        ss_segment.setmEnabled(false);
        ss_category_price.setmEnabled(false);
        ss_site_owner.setmEnabled(false);*/
        ss_site_owner.setVisibility(View.GONE);
        //
        ss_site.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(HMAux hmAux) {
                if (!skip_validation) {
                    loadZoneSS(true);
                    //
                    loadLocalSS(true);
                }
            }
        });
        //
        ss_site_zone.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(HMAux hmAux) {
                if (!skip_validation) {
                    loadLocalSS(true);
                }
            }
        });
        //
        ss_site_zone_local.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(HMAux hmAux) {
                //Se Site estiver em branco, a seleção do local preenche os outros campos.
                if (ss_site.getmValue().get(SearchableSpinner.ID) == null) {
                    //Seta var para impedir que a troca de valores nos spinners dispare
                    //o evento.
                    skip_validation = true;
                    //Seta valor do site
                    ToolBox_Inf.setSSmValue(ss_site, hmAux.get(MD_SiteDao.SITE_CODE), hmAux.get(MD_SiteDao.SITE_DESC), false);
                    //Seta valor da zone e refaz HmAux baseado no novo site.
                    loadZoneSS(true);
                    ToolBox_Inf.setSSmValue(ss_site_zone, hmAux.get(MD_Site_ZoneDao.ZONE_CODE), hmAux.get(MD_Site_ZoneDao.ZONE_DESC), false);
                    //
                    loadLocalSS(false);
                    //
                    skip_validation = false;
                }
            }
        });
        //
        ss_brand.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(HMAux hmAux) {
                //
                loadModelSS(true);
                //
                loadColorSS(true);
            }
        });

        /*btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSerialChanges(serialProperties)) {
                    buildSerialFull();
                     mPresenter.updateSerialInfo(serialObj);
                } else {
                     mPresenter.executeSaveSerial(product_code, mket_serial_id.getText().toString().trim(), serialInfoChanges);
                }
            }
        });*/

        btn_action.setOnClickListener(listnerSearchSO);

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_PRODUCT_CODE) && bundle.containsKey(Constant.MAIN_SERIAL_ID)) {
                product_code = Long.parseLong(bundle.getString(Constant.MAIN_PRODUCT_CODE, "0"));
                bundle_serial_id = bundle.getString(Constant.MAIN_SERIAL_ID, "");
                new_serial = bundle.getBoolean(Act030_Main.NEW_SERIAL,false);

            } else {
                ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
            }

        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }


    private void iniListners() {
        //Listner busca Serial.
        /*listnerSearchSerial = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                mPresenter.serialFlow(mket_serial_id.getText().toString().trim() );
            }
        };*/
        //Listner busca so
        listnerSearchSO = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mket_serial_id.setEnabled(false);
                //validateSerialProperties esta comentado , mas seráa validação oficial no futuro.
                //if(validateSerialProperties(serialProperties)) {
                if(validadeSerialLocation()) {
                    if (checkSerialChanges(serialProperties)) {
                        buildSerialFull();
                        //
                        serialObj.setSerial_id(mket_serial_id.getText().toString().trim());
                        //
                        mPresenter.updateSerialInfo(serialObj);
                    } else {
                        //mPresenter.executeSaveSerial(product_code, mket_serial_id.getText().toString().trim(), serialInfoChanges);
                        showAlertDialog(
                                hmAux_Trans.get("alert_no_data_changes_ttl"),
                                hmAux_Trans.get("alert_no_data_changes_msg")
                        );
                    }
                }else{
                    showAlertDialog(
                            hmAux_Trans.get("alert_invalid_serial_local_ttl"),
                            hmAux_Trans.get("alert_invalid_serial_local_msg")
                    );
                }
            }
        };

    }
    private boolean validadeSerialLocation(){
        boolean site = ss_site.getmValue().get(SearchableSpinner.ID) != null && ss_site.getmValue().get(SearchableSpinner.ID).length() > 0;
        boolean zone = ss_site_zone.getmValue().get(SearchableSpinner.ID) != null && ss_site_zone.getmValue().get(SearchableSpinner.ID).length() > 0;
        boolean local = ss_site_zone_local.getmValue().get(SearchableSpinner.ID) != null && ss_site_zone_local.getmValue().get(SearchableSpinner.ID).length() > 0;
        //
        if(site && zone && local){
            //limpa marcação de erro.
            ss_site.setBackground(null);
            ss_site_zone.setBackground(null);
            ss_site_zone_local.setBackground(null);
            return true;
        }

        if(!site && !zone && !local){
            //limpa marcação de erro.
            ss_site.setBackground(null);
            ss_site_zone.setBackground(null);
            ss_site_zone_local.setBackground(null);
            return true;
        }
        //Seta marcação de erro nos SS
        ss_site.setBackground(context.getDrawable(R.drawable.shape_error));
        ss_site_zone.setBackground(context.getDrawable(R.drawable.shape_error));
        ss_site_zone_local.setBackground(context.getDrawable(R.drawable.shape_error));
        //Pega posição do ultimo item e faz Scroll
        int x = (int) ss_site_zone_local.getX();
        int y =  ss_site_zone_local.getTop() + ((View) ss_site_zone_local.getParent()).getTop();
        sv_serial.smoothScrollTo(x,y);

        return false;
    }


    /**
     * Esta comentada, mas será usada no futuro.
     * Faz loop no arraylist de itens validando se algum SS que não permite null esta null.
     * houve alteração de valor.
     * @param properties ArrayList com propriedade do serial, SS e editText
     * @return
     */
    private boolean validateSerialProperties(ArrayList<Object> properties){
        boolean finalRet = true;
        for (int i = 0; i < properties.size(); i++) {
            Object propertie = properties.get(i);
            if (propertie instanceof SearchableSpinner) {
                boolean hasCodeVal = false;
                //Se valor do Spinner é null ou "" seta controle como false;
                //OBS:Verficar com hugo pq dessa loucura.
                if(
                    ((SearchableSpinner) propertie).getmValue().get(SearchableSpinner.ID) != null
                            &&
                    ((SearchableSpinner) propertie).getmValue().get(SearchableSpinner.ID).length() > 0
                ){
                    hasCodeVal = true;
                }
                //
                if( !hasCodeVal && ((SearchableSpinner) propertie).getTag(R.id.SS_NULLS_ACCEPT).toString().toLowerCase().equals("false")){
                    ((SearchableSpinner) propertie).setBackground(context.getDrawable(R.drawable.shape_error));
                    ((SearchableSpinner) propertie).requestFocus();
                    //int[] position = {0,0};
                    int x = (int) ((SearchableSpinner) propertie).getX();
                    //int y = (int)((SearchableSpinner) propertie).getY() + ((SearchableSpinner) propertie).getHeight();
                    int y = ((SearchableSpinner) propertie).getTop() +  ( (View) ((SearchableSpinner) propertie).getParent()).getTop();
                    sv_serial.smoothScrollTo(x,y);

                    finalRet= false;
                }else{
                    ((SearchableSpinner) propertie).setBackground(null);
                }
            }
        }

        return finalRet;
    }

    /**
     * Faz loop no arraylist de itens verificando se
     * houve alteração de valor.
     *
     * @return
     */
    private boolean checkSerialChanges(ArrayList<Object> properties) {
        for (int i = 0; i < properties.size(); i++) {
            Object propertie = properties.get(i);
            //Se for SearchableSpinner
            if (propertie instanceof SearchableSpinner) {
                if (((SearchableSpinner) propertie).getmValue().get(SearchableSpinner.ID) == null || !((SearchableSpinner) propertie).getmValue().get(SearchableSpinner.ID).equals(((SearchableSpinner) propertie).getTag().toString())) {
                    serialInfoChanges = true;
                    return true;
                }
            } else {
                //Se for EditText
                if (propertie instanceof EditText) {
                    if (!((EditText) propertie).getText().toString().equals(((EditText) propertie).getTag().toString())) {
                        serialInfoChanges = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void spinnersInitializer() {
        // Site
        loadSiteSS(false);
        // Site Zone
        loadZoneSS(false);
        // Site Zone Local
        loadLocalSS(false);
        // Site Brand
        loadBrandSS(false);
        // Site Brand Model
        loadModelSS(false);
        // Site Brand Color
        loadColorSS(false);
        // Segment
        loadSegment(false);
        // Category Price
        loadCategoryPrice(false);
        // Site Owner
        loadSiteOwner(false);
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
    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }

    @Override
    public void setProductValues(MD_Product md_product) {
        product = md_product;
        //
        tv_product_code_label.setText(
                hmAux_Trans.get("product_label") + " " +
                        String.valueOf(md_product.getProduct_code())

        );
        tv_product_id_label.setText(
                hmAux_Trans.get("product_id_label") + " " +
                        md_product.getProduct_id());
        tv_product_desc_value.setText(md_product.getProduct_desc());
        //
        serial_required = md_product.getRequire_serial();
        serial_allow_new = md_product.getAllow_new_serial_cl();
        //
        tv_required_val.setText("(" + hmAux_Trans.get("NO").toUpperCase() + ")");
        if (md_product.getRequire_serial() == 1) {
            tv_required_val.setText("(" + hmAux_Trans.get("YES").toUpperCase() + ")");
        }
        //
        tv_allow_new_val.setText("(" + hmAux_Trans.get("NO").toUpperCase() + ")");
        if (md_product.getAllow_new_serial_cl() == 1) {
            tv_allow_new_val.setText("(" + hmAux_Trans.get("YES").toUpperCase() + ")");
        }
    }


    @Override
    public void setSerialValues(HMAux md_product_serial) {
        //
        if(!new_serial){
            mket_serial_id.setEnabled(false);
        }
        //}
        //
        btn_action.setOnClickListener(listnerSearchSO);
        btn_action.setText(hmAux_Trans.get("btn_action_lbl"));
        //
        ll_serial_full_desc.setVisibility(View.VISIBLE);

        if(md_product_serial != null) {
            //
            ToolBox_Inf.setSSmValue(ss_site, md_product_serial.get(MD_SiteDao.SITE_CODE), md_product_serial.get(MD_SiteDao.SITE_DESC), true, true);
            serialProperties.add(ss_site);
            //
            ToolBox_Inf.setSSmValue(ss_site_zone, md_product_serial.get(MD_Site_ZoneDao.ZONE_CODE), md_product_serial.get(MD_Site_ZoneDao.ZONE_DESC), true, true);
            serialProperties.add(ss_site_zone);
            //
            ToolBox_Inf.setSSmValue(ss_site_zone_local, md_product_serial.get(MD_Site_Zone_LocalDao.LOCAL_CODE), md_product_serial.get(MD_Site_Zone_LocalDao.LOCAL_ID), true, true);
            serialProperties.add(ss_site_zone_local);
            //
            ToolBox_Inf.setSSmValue(ss_brand, md_product_serial.get(MD_BrandDao.BRAND_CODE), md_product_serial.get(MD_BrandDao.BRAND_DESC), true, false);
            serialProperties.add(ss_brand);
            //
            ToolBox_Inf.setSSmValue(ss_brand_model, md_product_serial.get(MD_Brand_ModelDao.MODEL_CODE), md_product_serial.get(MD_Brand_ModelDao.MODEL_DESC), true, false);
            serialProperties.add(ss_brand_model);
            //
            ToolBox_Inf.setSSmValue(ss_brand_color, md_product_serial.get(MD_Brand_ColorDao.COLOR_CODE), md_product_serial.get(MD_Brand_ColorDao.COLOR_DESC), true, false);
            serialProperties.add(ss_brand_color);
            //
            ToolBox_Inf.setSSmValue(ss_segment, md_product_serial.get(MD_SegmentDao.SEGMENT_CODE), md_product_serial.get(MD_SegmentDao.SEGMENT_DESC), true, false);
            serialProperties.add(ss_segment);
            //
            ToolBox_Inf.setSSmValue(ss_category_price, md_product_serial.get(MD_Category_PriceDao.CATEGORY_PRICE_CODE), md_product_serial.get(MD_Category_PriceDao.CATEGORY_PRICE_DESC), true, false);
            serialProperties.add(ss_category_price);
            //
            ToolBox_Inf.setSSmValue(ss_site_owner, md_product_serial.get(SITE_CODE_OWNER), md_product_serial.get(SITE_DESC_OWNER), true, false);
            serialProperties.add(ss_site_owner);
            //
            et_info1.setText(md_product_serial.get(MD_Product_SerialDao.ADD_INF1));
            et_info1.setTag(md_product_serial.get(MD_Product_SerialDao.ADD_INF1));
            serialProperties.add(et_info1);
            //
            et_info2.setText(md_product_serial.get(MD_Product_SerialDao.ADD_INF2));
            et_info2.setTag(md_product_serial.get(MD_Product_SerialDao.ADD_INF2));
            serialProperties.add(et_info2);
            //
            et_info3.setText(md_product_serial.get(MD_Product_SerialDao.ADD_INF3));
            et_info3.setTag(md_product_serial.get(MD_Product_SerialDao.ADD_INF3));
            serialProperties.add(et_info3);
            //Monta obj serial com os dados retornados.
            buildSerialPk(md_product_serial);
        }else{

            ToolBox_Inf.setSSmValue(ss_site,null,null, true, true);
            serialProperties.add(ss_site);
            //
            ToolBox_Inf.setSSmValue(ss_site_zone, null,null, true, true);
            serialProperties.add(ss_site_zone);
            //
            ToolBox_Inf.setSSmValue(ss_site_zone_local,null,null, true, true);
            serialProperties.add(ss_site_zone_local);
            //
            ToolBox_Inf.setSSmValue(ss_brand,null,null, true, false);
            serialProperties.add(ss_brand);
            //
            ToolBox_Inf.setSSmValue(ss_brand_model, null,null, true, false);
            serialProperties.add(ss_brand_model);
            //
            ToolBox_Inf.setSSmValue(ss_brand_color,null,null, true, false);
            serialProperties.add(ss_brand_color);
            //
            ToolBox_Inf.setSSmValue(ss_segment,null,null,true, false);
            serialProperties.add(ss_segment);
            //
            ToolBox_Inf.setSSmValue(ss_category_price,null,null, true, false);
            serialProperties.add(ss_category_price);
            //
            ToolBox_Inf.setSSmValue(ss_site_owner,null,null, true, false);
            serialProperties.add(ss_site_owner);
            //
            et_info1.setTag(md_product_serial.get(MD_Product_SerialDao.ADD_INF1));
            serialProperties.add(et_info1);
            //
            et_info2.setTag(md_product_serial.get(MD_Product_SerialDao.ADD_INF2));
            serialProperties.add(et_info2);
            //
            et_info3.setTag(md_product_serial.get(MD_Product_SerialDao.ADD_INF3));
            serialProperties.add(et_info3);
        }
        //Chama metodo que carrea todas as listas do SS
        spinnersInitializer();
    }

    private void buildSerialPk(HMAux md_product_serial) {
        serialObj.setCustomer_code(Long.parseLong(md_product_serial.get(MD_Product_SerialDao.CUSTOMER_CODE)));
        serialObj.setProduct_code(Long.parseLong(md_product_serial.get(MD_Product_SerialDao.PRODUCT_CODE)));
        serialObj.setSerial_code(Integer.parseInt(md_product_serial.get(MD_Product_SerialDao.SERIAL_CODE)));
        serialObj.setSerial_id(md_product_serial.get(MD_Product_SerialDao.SERIAL_ID));
        //
    }

    private void buildSerialFull() {
        //
        serialObj.setSite_code(ToolBox_Inf.mIntegerParse(ss_site.getmValue().get(SearchableSpinner.ID)));
        serialObj.setZone_code(ToolBox_Inf.mIntegerParse(ss_site_zone.getmValue().get(SearchableSpinner.ID)));
        serialObj.setLocal_code(ToolBox_Inf.mIntegerParse(ss_site_zone_local.getmValue().get(SearchableSpinner.ID)));
        //
        serialObj.setAdd_inf1(et_info1.getText().toString().trim());
        serialObj.setAdd_inf2(et_info2.getText().toString().trim());
        serialObj.setAdd_inf3(et_info3.getText().toString().trim());
        //
        serialObj.setBrand_code(ToolBox_Inf.mIntegerParse(ss_brand.getmValue().get(SearchableSpinner.ID)));
        serialObj.setModel_code(ToolBox_Inf.mIntegerParse(ss_brand_model.getmValue().get(SearchableSpinner.ID)));
        serialObj.setColor_code(ToolBox_Inf.mIntegerParse(ss_brand_color.getmValue().get(SearchableSpinner.ID)));
        serialObj.setSegment_code(ToolBox_Inf.mIntegerParse(ss_segment.getmValue().get(SearchableSpinner.ID)));
        serialObj.setCategory_price_code(ToolBox_Inf.mIntegerParse(ss_category_price.getmValue().get(SearchableSpinner.ID)));
        serialObj.setSite_code_owner(ToolBox_Inf.mIntegerParse(ss_site_owner.getmValue().get(SearchableSpinner.ID)));
        //
        serialObj.setUpdate_required(1);
        serialObj.setOnly_position(0);
        //
    }

    //RETORNO DO WS_SO_Search
    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        progressDialog.dismiss();
        //
        if (ws_process.equals(SO_WS_SEARCH_SAVE)) {

            if(hmAux.size() > 0) {
              mPresenter.processSerialSaveResult(serialObj.getProduct_code(),serialObj.getSerial_id(),hmAux);
            }
        }

    }

    @Override
    public void showSingleResultMsg(String ttl, String msg) {
        //
        sv_serial.fullScroll(ScrollView.FOCUS_UP);
        //
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
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
        hmAux_Trans.put(Generic_Results_Adapter.LABEL_ITEM_1,hmAux_Trans.get("dialog_result_product_lbl"));
        hmAux_Trans.put(Generic_Results_Adapter.LABEL_ITEM_2,hmAux_Trans.get("dialog_result_serial_lbl"));
        hmAux_Trans.put(Generic_Results_Adapter.LABEL_ITEM_3,hmAux_Trans.get("dialog_result_msg_lbl"));

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
            }
        });

    }

    //RETORNO DO WS_Search_Serial
    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);

        if (ws_process.equals(SO_WS_SEARCH_SERIAL)) {
            mPresenter.getSerialInfo(product_code, mket_serial_id.getText().toString().trim());
        }

        progressDialog.dismiss();

    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
    }

    /**
     * Carrega lista que sera exibida no spinner.
     * Se parameto true, apaga valor atual do spinner.
     *
     * @param reset_val
     */
    private void loadSiteOwner(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_site_owner, null, null, false);
        }
        //
        MD_SiteDao siteDao = new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> siteOwnerList = (ArrayList<HMAux>) siteDao.query_HM(
                new MD_Site_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );
        //
        ss_site_owner.setmOption(siteOwnerList);
    }

    private void loadCategoryPrice(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_category_price, null, null, false);
        }
        //
        MD_Category_PriceDao categoryPriceDao = new MD_Category_PriceDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> categoryPriceList = (ArrayList<HMAux>) categoryPriceDao.query_HM(
                new MD_Category_Price_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(product_code)
                ).toSqlQuery()
        );
        //
        ss_category_price.setmOption(categoryPriceList);
    }

    private void loadSegment(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_segment, null, null, false);
        }
        //
        MD_SegmentDao segmentDao = new MD_SegmentDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> segmentList = (ArrayList<HMAux>) segmentDao.query_HM(
                new MD_Segment_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(product_code)
                ).toSqlQuery()
        );
        //
        ss_segment.setmOption(segmentList);
    }

    private void loadColorSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_brand_color, null, null, false);
        }
        //
        MD_Brand_ColorDao brandColorDao = new MD_Brand_ColorDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> colorList = (ArrayList<HMAux>) brandColorDao.query_HM(
                new MD_Brand_Color_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(product_code),
                        ss_brand.getmValue().get(SearchableSpinner.ID)
                ).toSqlQuery()
        );
        //
        ss_brand_color.setmOption(colorList);
    }

    private void loadModelSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_brand_model, null, null, false);
        }
        //
        MD_Brand_ModelDao brandModelDao = new MD_Brand_ModelDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> modelList = (ArrayList<HMAux>) brandModelDao.query_HM(
                new MD_Brand_Model_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(product_code),
                        ss_brand.getmValue().get(SearchableSpinner.ID)
                ).toSqlQuery()
        );
        //
        ss_brand_model.setmOption(modelList);
    }

    private void loadBrandSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_brand, null, null, false);
        }
        //
        MD_BrandDao brandDao = new MD_BrandDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> brandList = (ArrayList<HMAux>) brandDao.query_HM(
                new MD_Brand_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(product_code)
                ).toSqlQuery()
        );
        //
        ss_brand.setmOption(brandList);
    }


    private void loadSiteSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_site, null, null, false);
        }

        MD_SiteDao siteDao = new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> siteList = (ArrayList<HMAux>) siteDao.query_HM(
                new MD_Site_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );
        //
        ss_site.setmOption(siteList);
    }

    private void loadZoneSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_site_zone, null, null, false);
        }
        //
        MD_Site_ZoneDao siteZoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        ArrayList<HMAux> zoneList = (ArrayList<HMAux>) siteZoneDao.query_HM(
                new MD_Site_Zone_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        ss_site.getmValue().get(SearchableSpinner.ID)
                ).toSqlQuery()
        );
        //
        ss_site_zone.setmOption(zoneList);
    }

    private void loadLocalSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_site_zone_local, null, null, false);
        }
        //
        MD_Site_Zone_LocalDao siteZoneLocalDao = new MD_Site_Zone_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> localList = (ArrayList<HMAux>) siteZoneLocalDao.query_HM(
                new MD_Site_Zone_Local_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        ss_site.getmValue().get(SearchableSpinner.ID),
                        ss_site_zone.getmValue().get(SearchableSpinner.ID)
                ).toSqlQuery()
        );
        //
        ss_site_zone_local.setmOption(localList);
    }


    @Override
    public void callAct030(Context context) {
        Intent mIntent = new Intent(context, Act030_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
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
