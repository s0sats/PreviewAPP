package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.namoa_digital.namoa_library.view.BaseFragment;
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
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.MD_Brand_Color_Sql_SS;
import com.namoadigital.prj001.sql.MD_Brand_Model_Sql_SS;
import com.namoadigital.prj001.sql.MD_Brand_Sql_SS;
import com.namoadigital.prj001.sql.MD_Category_Price_Sql_SS;
import com.namoadigital.prj001.sql.MD_Segment_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

import static com.namoadigital.prj001.dao.MD_Product_SerialDao.SITE_CODE_OWNER;
import static com.namoadigital.prj001.ui.act023.Act023_Main.SITE_DESC_OWNER;

/**
 * Created by neomatrix on 14/08/17.
 */

public class Act027_Serial extends BaseFragment implements Act027_Serial_View {

    private boolean bStatus;

    private Context context;

    public Act027_Serial_Presenter mPresenter;

    private Bundle bundle;
    public String requesting_process = "";
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
    //agendamento
    private boolean skip_validation = false;
    private boolean serialInfoChanges = false;
    private ArrayList<Object> serialProperties;

    private Button btn_action;

    private View.OnClickListener listnerSaveSerial;

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    private SM_SO mSm_so;

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
        //
        this.product_code = mSm_so.getProduct_code();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bStatus = true;

        View view = inflater.inflate(R.layout.act027_serial_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
    }

    private void iniVar(View view) {
        context = getActivity();
        //
        recoverIntentsInfo();
        //
        mPresenter = new Act027_Serial_Presenter_Impl(
                context,
                this,
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                product_code,
                hmAux_Trans,
                new MD_Product_SerialDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                mSm_so
        );
        //
        iniListners();
        //
        serialProperties = new ArrayList<>();
        //
        serialObj = new MD_Product_Serial();
        //
        sv_serial = (ScrollView) view.findViewById(R.id.act027_serial_content_sv_serial);
        //
        mket_serial_id = (MKEditTextNM) view.findViewById(R.id.act027_serial_content_mket_serial);
        mket_serial_id.setmNFC(false);
        mket_serial_id.setmBARCODE(false);

        controls_sta.add(mket_serial_id);
        //mket_serial_id.setHint(hmAux_Trans.get("mket_search_hint"));
        //
        tv_product_ttl = (TextView) view.findViewById(R.id.act027_serial_content_tv_product_ttl);
        tv_product_ttl.setTag("product_ttl");
        //
        tv_product_code_label = (TextView) view.findViewById(R.id.act027_serial_content_tv_product_code_lbl);
        tv_product_code_label.setTag("product_label");
        //
        tv_product_id_label = (TextView) view.findViewById(R.id.act027_serial_content_tv_product_id_lbl);
        tv_product_id_label.setTag("product_id_label");
        //
        tv_product_desc_value = (TextView) view.findViewById(R.id.act027_serial_content_tv_product_desc_value);
        tv_product_desc_value.setTag("product_desc_label");
        //
        ll_require_serial = (LinearLayout) view.findViewById(R.id.act027_serial_content_ll_require_serial);
        //
        tv_required_lbl = (TextView) view.findViewById(R.id.act027_serial_content_tv_require_serial_lbl);
        tv_required_lbl.setTag("chk_required");
        //
        tv_required_val = (TextView) view.findViewById(R.id.act027_serial_content_tv_require_serial_val);
        //
        tv_allow_new_lbl = (TextView) view.findViewById(R.id.act027_serial_content_tv_allow_lbl);
        tv_allow_new_lbl.setTag("chk_allow_new");
        //
        tv_allow_new_val = (TextView) view.findViewById(R.id.act027_serial_content_tv_allow_val);
        //
        tv_serial_ttl = (TextView) view.findViewById(R.id.act027_serial_content_tv_serial_ttl);
        tv_serial_ttl.setTag("serial_ttl");
        //
        ll_serial_full_desc = (LinearLayout) view.findViewById(R.id.act027_serial_content_ll_serial_full_desc);
        //
        ll_serial_location = (LinearLayout) view.findViewById(R.id.act027_serial_content_ll_serial_location);
        //
        tv_serial_location_ttl = (TextView) view.findViewById(R.id.act027_serial_content_tv_serial_location_ttl);
        tv_serial_location_ttl.setTag("serial_location_ttl");
        tv_serial_location_ttl.setText(hmAux_Trans.get("serial_location_ttl"));
        //
        ss_site = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_site);
        ss_site.setmLabel(hmAux_Trans.get("site_lbl"));
        ss_site.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_site_zone = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_site_zone);
        ss_site_zone.setmLabel(hmAux_Trans.get("site_zone_lbl"));
        ss_site_zone.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_site_zone_local = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_site_zone_local);
        ss_site_zone_local.setmLabel(hmAux_Trans.get("site_zone_local_lbl"));
        ss_site_zone_local.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ll_serial_add_info = (LinearLayout) view.findViewById(R.id.act027_serial_content_ll_serial_add_info);
        //
        tv_serial_add_info_ttl = (TextView) view.findViewById(R.id.act027_serial_content_tv_serial_add_info_ttl);
        tv_serial_add_info_ttl.setTag("serial_add_info_ttl");
        tv_serial_add_info_ttl.setText(hmAux_Trans.get("serial_add_info_ttl"));
        //
        et_info1 = (EditText) view.findViewById(R.id.act027_serial_content_et_info1);
        et_info1.setTag("add_info1_lbl");
        //
        et_info2 = (EditText) view.findViewById(R.id.act027_serial_content_et_info2);
        et_info2.setTag("add_info2_lbl");
        //
        et_info3 = (EditText) view.findViewById(R.id.act027_serial_content_et_info3);
        et_info3.setTag("add_info3_lbl");
        //
        ll_serial_properties = (LinearLayout) view.findViewById(R.id.act027_serial_content_ll_serial_properties);
        //
        tv_serial_properties_ttl = (TextView) view.findViewById(R.id.act027_serial_content_tv_serial_properties_ttl);
        tv_serial_properties_ttl.setTag("serial_properties_ttl");
        tv_serial_properties_ttl.setText(hmAux_Trans.get("serial_properties_ttl"));
        //
        ss_brand = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_brand);
        ss_brand.setmLabel(hmAux_Trans.get("brand_lbl"));
        ss_brand.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_brand_model = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_brand_model);
        ss_brand_model.setmLabel(hmAux_Trans.get("brand_model_lbl"));
        ss_brand_model.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_brand_color = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_brand_color);
        ss_brand_color.setmLabel(hmAux_Trans.get("brand_color_lbl"));
        ss_brand_color.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_segment = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_segment);
        ss_segment.setmLabel(hmAux_Trans.get("segment_lbl"));
        ss_segment.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_category_price = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_category_price);
        ss_category_price.setmLabel(hmAux_Trans.get("category_price_lbl"));
        ss_category_price.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_site_owner = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_site_owner);
        ss_site_owner.setmLabel(hmAux_Trans.get("site_owner_lbl"));
        ss_site_owner.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        btn_action = (Button) view.findViewById(R.id.act027_serial_content_btn_action);
        //
        //Adiciona Views na lista de tradução
        views.add(tv_product_ttl);
        views.add(tv_required_lbl);
        views.add(tv_allow_new_lbl);
        views.add(tv_serial_ttl);
        views.add(tv_serial_location_ttl);
        views.add(et_info1);
        views.add(et_info2);
        views.add(et_info3);
        views.add(tv_serial_properties_ttl);
        //
        layoutConfiguration();
    }

    private void recoverIntentsInfo() {
        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_REQUESTING_PROCESS)) {
                requesting_process = bundle.getString(Constant.MAIN_REQUESTING_PROCESS, "");
                product_code = Long.parseLong(bundle.getString(Constant.MAIN_PRODUCT_CODE, "0"));
                bundle_serial_id = bundle.getString(Constant.MAIN_SERIAL_ID, "");
            } else {
            }

        } else {
        }
    }

    private void iniListners() {
        listnerSaveSerial = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if (checkSerialChanges()) {
                    buildSerialFull();
                    //
                    mPresenter.updateSerialInfo(serialObj);
                } else {
                    showAlertDialog(
                            hmAux_Trans.get("alert_no_data_changes_ttl"),
                            hmAux_Trans.get("alert_no_data_changes_msg")
                    );
                }
            }
        };
    }

    private void layoutConfiguration() {
        btn_action.setOnClickListener(listnerSaveSerial);
        btn_action.setText(hmAux_Trans.get("btn_serial_save"));
    }

    private void iniAction() {
//        mPresenter.getProductInfo();
//        //
        ss_brand.setmEnabled(false);
        ss_brand_model.setmEnabled(false);
        ss_brand_color.setmEnabled(false);
        //
        et_info1.setEnabled(false);
        et_info2.setEnabled(false);
        et_info3.setEnabled(false);
        //
        ss_segment.setmEnabled(false);
        ss_category_price.setmEnabled(false);
        ss_site_owner.setmEnabled(false);
        ss_site_owner.setVisibility(View.GONE);
        //
        ss_site.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
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
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (!skip_validation) {
                    loadLocalSS(true);
                }
            }
        });
        //
        ss_brand.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                loadModelSS(true);
                //
                loadColorSS(true);
            }
        });
        //
        ss_site_zone_local.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                //Se Site estiver em branco, a seleção do local preenche os outros campos.
                if (ss_site.getmValue().get(SearchableSpinner.ID) == null) {
                    //Seta var para impedir que a troca de valores nos spinners dispare
                    //o evento.
                    skip_validation = true;
                    //Seta valor do site
                    setSSmValue(ss_site, hmAux.get(MD_SiteDao.SITE_CODE), hmAux.get(MD_SiteDao.SITE_DESC), false);
                    //Seta valor da zone e refaz HmAux baseado no novo site.
                    loadZoneSS(true);
                    setSSmValue(ss_site_zone, hmAux.get(MD_Site_ZoneDao.ZONE_CODE), hmAux.get(MD_Site_ZoneDao.ZONE_DESC), false);
                    //
                    skip_validation = false;
                }
            }
        });

        getSerialInfo();
    }

    public void loadDataToScreen() {
        if (bStatus) {
            mket_serial_id.setText(mSm_so.getSerial_id());
            //
            mPresenter.getProductInfo();
            //
            getSerialInfo();
        }

    }

    public void loadScreenToData() {
//        if (bStatus) {
//        }
    }

    @Override
    public void setProductValues(MD_Product md_product) {
        product = md_product;
        //
        tv_product_code_label.setText(
                hmAux_Trans.get("product_header_lbl") + " " +
                        String.valueOf(mSm_so.getProduct_code())
        );

        tv_product_id_label.setText(
                hmAux_Trans.get("product_id_header_lbl") + " " +
                        mSm_so.getProduct_id()
        );

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
        //
        mket_serial_id.setText(mSm_so.getSerial_id());
    }

    @Override
    public void setSerialValues(HMAux md_product_serial) {
        mket_serial_id.setEnabled(false);
        //
        btn_action.setOnClickListener(listnerSaveSerial);
        //
        ll_serial_full_desc.setVisibility(View.VISIBLE);
        //
        setSSmValue(ss_site, md_product_serial.get(MD_SiteDao.SITE_CODE), md_product_serial.get(MD_SiteDao.SITE_DESC), true);
        serialProperties.add(ss_site);
        //
        setSSmValue(ss_site_zone, md_product_serial.get(MD_Site_ZoneDao.ZONE_CODE), md_product_serial.get(MD_Site_ZoneDao.ZONE_DESC), true);
        serialProperties.add(ss_site_zone);
        //
        setSSmValue(ss_site_zone_local, md_product_serial.get(MD_Site_Zone_LocalDao.LOCAL_CODE), md_product_serial.get(MD_Site_Zone_LocalDao.LOCAL_ID), true);
        serialProperties.add(ss_site_zone_local);
        //
        setSSmValue(ss_brand, md_product_serial.get(MD_BrandDao.BRAND_CODE), md_product_serial.get(MD_BrandDao.BRAND_DESC), true);
        serialProperties.add(ss_brand);
        //
        setSSmValue(ss_brand_model, md_product_serial.get(MD_Brand_ModelDao.MODEL_CODE), md_product_serial.get(MD_Brand_ModelDao.MODEL_DESC), true);
        serialProperties.add(ss_brand_model);
        //
        setSSmValue(ss_brand_color, md_product_serial.get(MD_Brand_ColorDao.COLOR_CODE), md_product_serial.get(MD_Brand_ColorDao.COLOR_DESC), true);
        serialProperties.add(ss_brand_color);
        //
        setSSmValue(ss_segment, md_product_serial.get(MD_SegmentDao.SEGMENT_CODE), md_product_serial.get(MD_SegmentDao.SEGMENT_DESC), true);
        serialProperties.add(ss_segment);
        //
        setSSmValue(ss_category_price, md_product_serial.get(MD_Category_PriceDao.CATEGORY_PRICE_CODE), md_product_serial.get(MD_Category_PriceDao.CATEGORY_PRICE_DESC), true);
        serialProperties.add(ss_category_price);
        //
        setSSmValue(ss_site_owner, md_product_serial.get(SITE_CODE_OWNER), md_product_serial.get(SITE_DESC_OWNER), true);
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
        //Chama metodo que carrea todas as listas do SS
        spinnersInitializer();
    }

    private void buildSerialPk(HMAux md_product_serial) {
        serialObj.setCustomer_code(Long.parseLong(md_product_serial.get(MD_Product_SerialDao.CUSTOMER_CODE)));
        serialObj.setProduct_code(Long.parseLong(md_product_serial.get(MD_Product_SerialDao.PRODUCT_CODE)));
        serialObj.setSerial_code(Integer.parseInt(md_product_serial.get(MD_Product_SerialDao.SERIAL_CODE)));
        serialObj.setSerial_id(md_product_serial.get(MD_Product_SerialDao.SERIAL_ID));
    }

    private void buildSerialFull() {
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
        serialObj.setOnly_position(1);
    }

    @Override
    public void showPD(String title, String msg) {
        if (baInfra != null) {
            baInfra.enableProgressDialog(
                    title,
                    msg,
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
        }
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
            }
        });
    }

    /**
     * Monta HMAux para inserir no spinner
     *
     * @param ss_component
     * @param code
     * @param desc
     * @param source_val
     */
    private void setSSmValue(SearchableSpinner ss_component, String code, String desc, boolean source_val) {
        HMAux hmAux = new HMAux();
        hmAux.put(SearchableSpinner.ID, code);
        hmAux.put(SearchableSpinner.DESCRIPTION, desc);
        ss_component.setmValue(hmAux);
        if (source_val) {
            ss_component.setTag(code);
        }
    }

    /**
     * Faz loop no arraylist de itens verificando se
     * houve alteração de valor.
     *
     * @return
     */
    private boolean checkSerialChanges() {
        for (int i = 0; i < serialProperties.size(); i++) {
            Object propertie = serialProperties.get(i);
            //Se for SearchableSpinner
            if (propertie instanceof SearchableSpinner) {
                if (!((SearchableSpinner) propertie).getmValue().get(SearchableSpinner.ID).equals(((SearchableSpinner) propertie).getTag().toString())) {
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

    /**
     * Carrega lista que sera exibida no spinner.
     * Se parameto true, apaga valor atual do spinner.
     *
     * @param reset_val
     */
    private void loadSiteOwner(boolean reset_val) {
        if (reset_val) {
            setSSmValue(ss_site_owner, null, null, false);
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
            setSSmValue(ss_category_price, null, null, false);
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
            setSSmValue(ss_segment, null, null, false);
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
            setSSmValue(ss_brand_color, null, null, false);
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
            setSSmValue(ss_brand_model, null, null, false);
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
            setSSmValue(ss_brand, null, null, false);
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
            setSSmValue(ss_site, null, null, false);
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
            setSSmValue(ss_site_zone, null, null, false);
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
            setSSmValue(ss_site_zone_local, null, null, false);
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

    public void getSerialInfo() {
        mPresenter.getSerialInfo(
                product_code,
                mSm_so.getSerial_id());
    }

    public void callProcessSerialSaveResult(String product_code, String serial_id, HMAux hmSaveResult) {
        mPresenter.processSerialSaveResult(product_code, serial_id, hmSaveResult);
    }

}

