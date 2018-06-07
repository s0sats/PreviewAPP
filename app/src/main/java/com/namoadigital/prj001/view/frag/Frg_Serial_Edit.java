package com.namoadigital.prj001.view.frag;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.ctls.TextViewCT;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.MD_Category_PriceDao;
import com.namoadigital.prj001.dao.MD_ClassDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.MD_Brand_Color_Sql_SS;
import com.namoadigital.prj001.sql.MD_Brand_Model_Sql_SS;
import com.namoadigital.prj001.sql.MD_Brand_Sql_SS;
import com.namoadigital.prj001.sql.MD_Category_Price_Sql_SS;
import com.namoadigital.prj001.sql.MD_Class_Sql_SS;
import com.namoadigital.prj001.sql.MD_Segment_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Sql_SS_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Frg_Serial_Edit extends Fragment {
    //ESSA CONSTANTE É USADA PELO SERVER NO SAVE
    //SE FOR ALTERAR AVISAR CESAR CALDI
    public static final String VIEW_FULL_EDIT = "VIEW_FULL_EDIT";
    public static final String VIEW_SO_EDIT = "VIEW_SO_EDIT";

    private Context context;
    private boolean bStatus;
    private HMAux hmAux_Trans;
    private String mModule_Code;
    private String mResource_Code;
    private ArrayList<MKEditTextNM> controls_sta;
    private String viewMode = "";
    private ScrollView sv_serial;
    private TextView tv_product_code_label;
    private TextView tv_product_id_label;
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
    private MKEditTextNM mket_info1;
    private MKEditTextNM mket_info2;
    private MKEditTextNM mket_info3;
    private TextView tv_serial_properties_ttl;
    private LinearLayout ll_serial_properties;
    private SearchableSpinner ss_brand;
    private SearchableSpinner ss_brand_model;
    private SearchableSpinner ss_brand_color;
    private SearchableSpinner ss_segment;
    private SearchableSpinner ss_category_price;
    private LinearLayout ll_serial_class;
    private SearchableSpinner ss_class;
    private ImageView iv_class_icon;
    private FloatingActionButton fab_anchor;
    private MD_Product mdProduct;
    private MD_Product_Serial mdProductSerial;
    private int serial_required;
    private int serial_allow_new;
    private ArrayList<Object> serialProperties;
    private boolean skip_validation = false;
    private boolean serialInfoChanges = false;
    private boolean new_serial = false;
    private Button btn_action;
    private View.OnClickListener checkExistSerialListner;
    private View.OnClickListener saveSerialListner;
    private LinearLayout ll_tracking;
    private TextView tv_tracking;
    private ImageView iv_add_tracking;
    private LinearLayout ll_tracking_content;
    private TextViewCT.ITextViewCT tvCtListner;
    private String searched_tracking = "";
    private ArrayList<MD_Product_Serial_Tracking> tracking_list;
    private boolean trackingListChanged = false;
    private DialogInterface.OnClickListener dialogClearTrackingListner;
    private HMAux oldSite = new HMAux();
    private HMAux oldZone = new HMAux();
    private HMAux oldLocal = new HMAux();
    private boolean serialIdChanged = false;
    private boolean pausedByScan = false;
    private boolean siteChanged = false;
    //VIEW_SO_EDIT
    private TextView tv_brand_model_color;
    private ImageView iv_serial_dialog_info;
    private String brand_model_color_lbl;
    private boolean showCategorySegmentoInfo = true;
    //
//    private String sql_ss_site;
//    private String sql_ss_site_zone;
//    private String sql_ss_site_zone_local;
//    private String sql_ss_brand;
//    private String sql_ss_brand_model;
//    private String sql_ss_brand_color;
//    private String sql_ss_segment;
//    private String sql_ss_category_price;
//    private String sql_ss_class;
    private ArrayList<View> views = new ArrayList<>();
    private LinearLayout ll_io_info;
    private TextView tv_io_info_ttl;
    private TextView tv_inbound_lbl;
    private MKEditTextNM mket_inbound_id;
    private TextView tv_inbound_date_conf_lbl;
    private MKEditTextNM mket_inbound_date_conf_val;
    private TextView tv_move_code_lbl;
    private MKEditTextNM mket_move_code_val;
    private TextView tv_move_group_lbl;
    private MKEditTextNM mket_move_group_val;
    private TextView tv_outbound_lbl;
    private MKEditTextNM mket_outbound_id;
    private I_Frg_Serial_Edit delegate;
    private boolean useTracking;

    //region Interfaces
    public interface I_Frg_Serial_Edit {

        void onCheckButtonClick(
                String product_id,
                String serial_id,
                String tracking
        );

        //
        void onSaveNoChangesClick(
                MD_Product_Serial md_product_serial,
                boolean serial_id_changes
        );

        //
        void onSaveWithChangesClick(
                MD_Product_Serial md_product_serial,
                boolean serial_id_changes
        );

        //
        void onTrackingSearchClick(long product_code, long serial_code, String tracking, String site_code);
    }
    //endregion

    //region GETTERS SETTERS

    public void setDelegate(I_Frg_Serial_Edit delegate) {
        this.delegate = delegate;
    }

    public void setBtnActionLabel(String label) {
        btn_action.setText(label);
    }

    public void setmModule_Code(String mModule_Code) {
        this.mModule_Code = mModule_Code;
    }

    public void setmResource_Code(String mResource_Code) {
        this.mResource_Code = mResource_Code;
    }

    public void setHmAux_Trans(HMAux hmAux_Trans) {
        this.hmAux_Trans = hmAux_Trans;
        setTranslation();
    }

    public MD_Product getMdProduct() {
        return mdProduct;
    }

    public void setMdProduct(MD_Product mdProduct) {
        this.mdProduct = mdProduct;
    }

    public MD_Product_Serial getMdProductSerial() {
        return mdProductSerial;
    }

    public void setMdProductSerial(MD_Product_Serial mdProductSerial) {
        this.mdProductSerial = mdProductSerial;
    }

    public void setNew_serial(boolean new_serial) {
        this.new_serial = new_serial;
    }

    public ArrayList<MKEditTextNM> getControlsSta() {
        return controls_sta;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public boolean isShowCategorySegmentoInfo() {
        return showCategorySegmentoInfo;
    }

    public void setShowCategorySegmentoInfo(boolean showCategorySegmentoInfo) {
        this.showCategorySegmentoInfo = showCategorySegmentoInfo;
    }

    public void reApplySerialId() {
        mket_serial_id.setTag(mket_serial_id.getText().toString());
        //
        mdProductSerial.setSerial_code(0);
        mdProductSerial.setSerial_tmp(0);
        mdProductSerial.setSerial_id(mket_serial_id.getText().toString());
        mdProductSerial.setTracking_list(new ArrayList<MD_Product_Serial_Tracking>());
        //
        serialIdChanged = false;
        //
        setUIDataToSerialObj();
        //
        showAlertDialog(
                hmAux_Trans.get("alert_serial_not_exists_ttl"),
                hmAux_Trans.get("alert_serial_not_exists_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_action.setOnClickListener(saveSerialListner);
                        //applyProfile();
                        loadDataToScreen();
                        cleanDbReference();
                    }
                }
        );
    }

    public void applyReceivedSerial(MD_Product_Serial received_serial) {
        //
        serialIdChanged = false;
        setNew_serial(false);
        setMdProductSerial(received_serial);
        //
        showAlertDialog(
                hmAux_Trans.get("alert_serial_exists_ttl"),
                hmAux_Trans.get("alert_serial_exists_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_action.setOnClickListener(saveSerialListner);
                        //
                        loadDataToScreen();
                    }
                }
        );

    }
//
//    public void setSql_ss_site(String sql_ss_site) {
//        this.sql_ss_site = sql_ss_site;
//    }
//
//    public void setSql_ss_site_zone(String sql_ss_site_zone) {
//        this.sql_ss_site_zone = sql_ss_site_zone;
//    }
//
//    public void setSql_ss_site_zone_local(String sql_ss_site_zone_local) {
//        this.sql_ss_site_zone_local = sql_ss_site_zone_local;
//    }
//
//    public void setSql_ss_brand(String sql_ss_brand) {
//        this.sql_ss_brand = sql_ss_brand;
//    }
//
//    public String getSql_ss_brand_model() {
//        return sql_ss_brand_model;
//    }
//
//    public void setSql_ss_brand_model(Specification sql_ss_brand_model) {
//        this.sql_ss_brand_model = sql_ss_brand_model;
//    }
//
//    public void setSql_ss_brand_color(String sql_ss_brand_color) {
//        this.sql_ss_brand_color = sql_ss_brand_color;
//    }
//
//    public void setSql_ss_segment(String sql_ss_segment) {
//        this.sql_ss_segment = sql_ss_segment;
//    }
//
//    public void setSql_ss_category_price(String sql_ss_category_price) {
//        this.sql_ss_category_price = sql_ss_category_price;
//    }
//
//    public void setSql_ss_class(String sql_ss_class) {
//        this.sql_ss_class = sql_ss_class;
//    }
    //endregion

    private void setTranslation() {
        mket_serial_id.setHint(hmAux_Trans.get("mket_search_hint"));
        ss_site.setmLabel(hmAux_Trans.get("site_lbl"));
        ss_site.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        ss_site_zone.setmLabel(hmAux_Trans.get("site_zone_lbl"));
        ss_site_zone.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        ss_site_zone_local.setmLabel(hmAux_Trans.get("site_zone_local_lbl"));
        ss_site_zone_local.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        tv_tracking.setText(hmAux_Trans.get("tracking_ttl"));
        mket_info1.setHint(hmAux_Trans.get("add_info1_lbl"));
        mket_info2.setHint(hmAux_Trans.get("add_info2_lbl"));
        mket_info3.setHint(hmAux_Trans.get("add_info3_lbl"));
        ss_brand.setmLabel(hmAux_Trans.get("brand_lbl"));
        ss_brand.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        ss_brand_model.setmLabel(hmAux_Trans.get("brand_model_lbl"));
        ss_brand_model.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        ss_brand_color.setmLabel(hmAux_Trans.get("brand_color_lbl"));
        ss_brand_color.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        ss_segment.setmLabel(hmAux_Trans.get("segment_lbl"));
        ss_segment.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        ss_category_price.setmLabel(hmAux_Trans.get("category_price_lbl"));
        ss_category_price.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        ss_class.setmLabel(hmAux_Trans.get("class_lbl"));
        ss_class.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        for (View view : views) {
            if (view != null && hmAux_Trans.get((String) view.getTag()) != null) {
                ((TextView) view).setText(hmAux_Trans.get((String) view.getTag()));
            } else {
                ((TextView) view).setText(ToolBox.setNoTrans(mModule_Code, mResource_Code, (String) view.getTag()));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;

        View view = inflater.inflate(R.layout.frg_serial_edit, container, false);

        iniVar(view);
        iniAction();

        return view;
    }

    private void iniVar(View view) {
        context = getActivity();
        //
        controls_sta = new ArrayList<>();
        //
        mdProductSerial = mdProductSerial == null ? new MD_Product_Serial() : mdProductSerial;
        //
        serialProperties = new ArrayList<>();
        tracking_list = new ArrayList<>();
        useTracking = ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1;
        //
        sv_serial = (ScrollView) view.findViewById(R.id.frg_serial_edit_sv_serial);
        //
        mket_serial_id = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_mket_serial);
        mket_serial_id.setmBARCODE(true);
        controls_sta.add(mket_serial_id);
        //
        tv_product_code_label = (TextView) view.findViewById(R.id.frg_serial_edit_tv_product_code_lbl);
        tv_product_code_label.setTag("product_label");
        //
        tv_product_id_label = (TextView) view.findViewById(R.id.frg_serial_edit_tv_product_id_lbl);
        tv_product_id_label.setTag("product_id_label");
        //
        tv_product_desc_value = (TextView) view.findViewById(R.id.frg_serial_edit_tv_product_desc_value);
        tv_product_desc_value.setTag("product_desc_label");
        //
        ll_require_serial = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_require_serial);
        //
        tv_required_lbl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_require_serial_lbl);
        tv_required_lbl.setTag("chk_required");
        //
        tv_required_val = (TextView) view.findViewById(R.id.frg_serial_edit_tv_require_serial_val);
        //
        tv_allow_new_lbl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_allow_lbl);
        tv_allow_new_lbl.setTag("chk_allow_new");
        //
        tv_allow_new_val = (TextView) view.findViewById(R.id.frg_serial_edit_tv_allow_val);
        //
        tv_serial_ttl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_serial_ttl);
        tv_serial_ttl.setTag("serial_ttl");
        //
        ll_serial_full_desc = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_serial_full_desc);
        //
        ll_serial_location = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_serial_location);
        //
        tv_serial_location_ttl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_serial_location_ttl);
        tv_serial_location_ttl.setTag("serial_location_ttl");
        //
        ss_site = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_site);
        //
        ss_site_zone = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_site_zone);
        //
        ss_site_zone_local = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_site_zone_local);
        //
        ll_tracking = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_serial_tracking);
        tv_tracking = (TextView) view.findViewById(R.id.frg_serial_edit_tv_serial_tracking_ttl);
        tv_tracking.setTag("tracking_ttl");

        iv_add_tracking = (ImageView) view.findViewById(R.id.frg_serial_edit_iv_add_tracking);
        ll_tracking_content = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_tracking_container);
        //
        ll_serial_add_info = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_serial_add_info);
        //
        tv_serial_add_info_ttl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_serial_add_info_ttl);
        tv_serial_add_info_ttl.setTag("serial_add_info_ttl");
        //
        mket_info1 = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_et_info1);
        //
        mket_info2 = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_et_info2);
        //
        mket_info3 = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_et_info3);
        //
        ll_serial_properties = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_serial_properties);
        //
        tv_serial_properties_ttl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_serial_properties_ttl);
        tv_serial_properties_ttl.setTag("serial_properties_ttl");
        //
        ss_brand = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_brand);
        //
        ss_brand_model = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_brand_model);
        //
        ss_brand_color = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_brand_color);
        //
        ss_segment = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_segment);
        //
        ss_category_price = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_category_price);
        //
        ll_serial_class = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_class);
        //
        ss_class = (SearchableSpinner) view.findViewById(R.id.frg_serial_edit_ss_class);
        //
        iv_class_icon = (ImageView) view.findViewById(R.id.frg_serial_edit_iv_class_icon);
        //
        fab_anchor = (FloatingActionButton) view.findViewById(R.id.frg_serial_edit_fab_anchor);
        //
        btn_action = (Button) view.findViewById(R.id.frg_serial_edit_btn_action);
        //
        ll_io_info = (LinearLayout) view.findViewById(R.id.frg_serial_edit_ll_io_info);
        //
        tv_io_info_ttl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_io_info_ttl);
        tv_io_info_ttl.setTag("io_info_ttl");
        //
        tv_inbound_lbl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_inbound_lbl);
        tv_inbound_lbl.setTag("inbound_lbl");
        //
        mket_inbound_id = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_mket_inbound_id);
        mket_inbound_id.setEnabled(false);
        //
        tv_inbound_date_conf_lbl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_inbound_date_conf_lbl);
        tv_inbound_date_conf_lbl.setTag("inbound_date_conf_lbl");
        //
        mket_inbound_date_conf_val = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_mket_inbound_date_conf_val);
        mket_inbound_date_conf_val.setEnabled(false);
        //
        tv_move_code_lbl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_move_code_lbl);
        tv_move_code_lbl.setTag("move_code_lbl");
        //
        mket_move_code_val = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_mket_move_code_val);
        mket_move_code_val.setEnabled(false);
        //
        tv_move_group_lbl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_move_group_lbl);
        tv_move_group_lbl.setTag("move_group_lbl");
        //
        mket_move_group_val = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_mket_move_group_val);
        mket_move_group_val.setEnabled(false);
        //
        tv_outbound_lbl = (TextView) view.findViewById(R.id.frg_serial_edit_tv_outbound_lbl);
        tv_outbound_lbl.setTag("outbound_lbl");
        //
        mket_outbound_id = (MKEditTextNM) view.findViewById(R.id.frg_serial_edit_mket_outbound_id);
        mket_outbound_id.setEnabled(false);
        //COMPONENTS VIEW_SO_EDIT
        tv_brand_model_color = (TextView) view.findViewById(R.id.frg_serial_edit_tv_brand_model_color);
        iv_serial_dialog_info = (ImageView) view.findViewById(R.id.frg_serial_edit_iv_serial_dialog_info);
        //
        //Adiciona Views na lista de tradução
        views.add(tv_required_lbl);
        views.add(tv_allow_new_lbl);
        views.add(tv_serial_ttl);
        views.add(tv_serial_location_ttl);
        views.add(tv_serial_properties_ttl);
        views.add(tv_tracking);
        views.add(tv_serial_add_info_ttl);
        views.add(tv_io_info_ttl);
        views.add(tv_inbound_lbl);
        views.add(tv_inbound_date_conf_lbl);
        views.add(tv_move_code_lbl);
        views.add(tv_move_group_lbl);
        views.add(tv_outbound_lbl);

        //Adiciona Componentes com dados do serial ao arrayList de validação
        serialProperties.add(ss_site);
        serialProperties.add(ss_site_zone);
        serialProperties.add(ss_site_zone_local);
        serialProperties.add(ss_brand);
        serialProperties.add(ss_brand_model);
        serialProperties.add(ss_brand_color);
        serialProperties.add(ss_segment);
        serialProperties.add(ss_category_price);
        serialProperties.add(ss_class);
        serialProperties.add(mket_info1);
        serialProperties.add(mket_info2);
        serialProperties.add(mket_info3);
        //
        listnersInitializer();
    }

    private void loadDataToScreen() {
        //
        if (bStatus) {
            setProductData();
            //
            setSerialData();
            //
            //sqlInitializer();
            //
            spinnersInitializer();
            //
            applyViewMode();
        }
    }

    public void refreshUi(){
        loadDataToScreen();
    }

    private void setProductData() {
        tv_product_id_label.setText(hmAux_Trans.get("product_lbl"));
        tv_product_desc_value.setText(mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc());
        tv_required_val.setText(mdProduct.getRequire_serial() == 1 ? "(" + hmAux_Trans.get("YES").toUpperCase() + ")" : "(" + hmAux_Trans.get("NO").toUpperCase() + ")");
        tv_allow_new_val.setText(mdProduct.getAllow_new_serial_cl() == 1 ? "(" + hmAux_Trans.get("YES").toUpperCase() + ")" : "(" + hmAux_Trans.get("NO").toUpperCase() + ")");
    }

    private void setSerialData() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mket_serial_id.setTag(mdProductSerial.getSerial_id());
        mket_serial_id.setText(mdProductSerial.getSerial_id());
        mket_serial_id.setmMinSize(mdProduct.getSerial_min_length());
        mket_serial_id.setmMaxSize(mdProduct.getSerial_max_length());
        mket_serial_id.setmInputTypeValidator(mdProduct.getSerial_rule());
        mket_serial_id.setmIgnoreMaxMinSize(true);
        mket_serial_id.setmBARCODE(true);
        mket_serial_id.setmRequired(true);
        //
        if (!new_serial) {
            mket_serial_id.setmBARCODE(false);
            mket_serial_id.setmOCR(false);
            mket_serial_id.setmNFC(false);
            mket_serial_id.setEnabled(false);
        }
        //Processa lista de trackings
        ll_tracking_content.removeAllViews();
        //Insere lista de tracking vindo do banco.
        for (int i = 0; i < mdProductSerial.getTracking_list().size(); i++) {
            appendTracking(mdProductSerial.getTracking_list().get(i).getTracking());
        }
        //region SS Class
        ToolBox_Inf.setSSmValue(
                ss_class,
                String.valueOf(mdProductSerial.getClass_code()),
                mdProductSerial.getClass_id(),
                true,
                MD_ClassDao.CLASS_ID, mdProductSerial.getClass_id(),
                MD_ClassDao.CLASS_TYPE, mdProductSerial.getClass_type(),
                MD_ClassDao.CLASS_COLOR, mdProductSerial.getClass_color(),
                MD_ClassDao.CLASS_AVAILABLE, String.valueOf(mdProductSerial.getClass_available())
        );
        //
        setClassIcon(ss_class.getmValue());
        //endregion
        //region SS Site
        ToolBox_Inf.setSSmValue(
                ss_site,
                String.valueOf(mdProductSerial.getSite_code()),
                mdProductSerial.getSite_desc(),
                true,
                MD_SiteDao.SITE_ID, mdProductSerial.getSite_id(),
                MD_SiteDao.IO_CONTROL, String.valueOf(mdProductSerial.getSite_io_control()),
                MD_SiteDao.INBOUND_AUTO_CREATE, String.valueOf(mdProductSerial.getInbound_auto_create())
        );
        //endregion
        //region SS Site Zone
        ToolBox_Inf.setSSmValue(
                ss_site_zone,
                String.valueOf(mdProductSerial.getZone_code()),
                mdProductSerial.getZone_desc(),
                true,
                MD_Site_ZoneDao.ZONE_ID, mdProductSerial.getSite_id()
        );
        //endregion
        //region SS Site Zone
        ToolBox_Inf.setSSmValue(
                ss_site_zone,
                String.valueOf(mdProductSerial.getZone_code()),
                mdProductSerial.getZone_desc(),
                true,
                MD_Site_ZoneDao.ZONE_ID, mdProductSerial.getSite_id()
        );
        //endregion
        //region SS Site Zone Local
        ToolBox_Inf.setSSmValue(
                ss_site_zone_local,
                String.valueOf(mdProductSerial.getLocal_code()),
                mdProductSerial.getLocal_id(),
                true,
                MD_Site_Zone_LocalDao.SITE_CODE, String.valueOf(mdProductSerial.getSite_code()),
                MD_SiteDao.SITE_DESC, mdProductSerial.getSite_desc(),
                MD_Site_Zone_LocalDao.ZONE_CODE, String.valueOf(mdProductSerial.getZone_code()),
                MD_Site_ZoneDao.ZONE_DESC, mdProductSerial.getZone_desc()
        );
        //endregion
        //region SS Brand
        ToolBox_Inf.setSSmValue(
                ss_brand,
                String.valueOf(mdProductSerial.getBrand_code()),
                mdProductSerial.getBrand_desc(),
                true,
                MD_BrandDao.BRAND_ID, mdProductSerial.getBrand_id()
        );
        //endregion
        //region SS Brand Model
        ToolBox_Inf.setSSmValue(
                ss_brand_model,
                String.valueOf(mdProductSerial.getModel_code()),
                mdProductSerial.getModel_desc(),
                true,
                MD_Brand_ModelDao.MODEL_ID, mdProductSerial.getModel_id()
        );
        //endregion
        //region SS Brand color
        ToolBox_Inf.setSSmValue(
                ss_brand_color,
                String.valueOf(mdProductSerial.getColor_code()),
                mdProductSerial.getColor_desc(),
                true,
                MD_Product_SerialDao.COLOR_ID, mdProductSerial.getColor_id()
        );
        //endregion
        //region BrandModelColor
        brand_model_color_lbl = mdProductSerial.getBrand_desc() == null ? "" : "| " + mdProductSerial.getBrand_desc() + " ";
        brand_model_color_lbl += mdProductSerial.getModel_desc() == null ? "" : "| " + mdProductSerial.getModel_desc() + " ";
        brand_model_color_lbl += mdProductSerial.getColor_desc() == null ? "" : "| " + mdProductSerial.getColor_desc() + " ";
        //
        if (brand_model_color_lbl.length() > 0) {
            brand_model_color_lbl = brand_model_color_lbl.substring(1, brand_model_color_lbl.length());
            tv_brand_model_color.setText(brand_model_color_lbl);
            tv_brand_model_color.setVisibility(View.VISIBLE);
        } else {
            tv_brand_model_color.setText("");
            tv_brand_model_color.setVisibility(View.GONE);
        }

        //region SS Segment
        ToolBox_Inf.setSSmValue(
                ss_segment,
                String.valueOf(mdProductSerial.getSegment_code()),
                mdProductSerial.getSegment_desc(),
                true,
                MD_Product_SerialDao.SEGMENT_ID, mdProductSerial.getSegment_id()
        );
        //endregion
        //region SS Category
        ToolBox_Inf.setSSmValue(
                ss_category_price,
                String.valueOf(mdProductSerial.getCategory_price_code()),
                mdProductSerial.getCategory_price_desc(),
                true,
                MD_Product_SerialDao.CATEGORY_PRICE_ID, mdProductSerial.getCategory_price_id()
        );
        //endregion
        //region Add Info
        mket_info1.setText(mdProductSerial.getAdd_inf1());
        mket_info1.setTag(mdProductSerial.getAdd_inf1());
        mket_info2.setText(mdProductSerial.getAdd_inf2());
        mket_info2.setTag(mdProductSerial.getAdd_inf2());
        mket_info3.setText(mdProductSerial.getAdd_inf3());
        mket_info3.setTag(mdProductSerial.getAdd_inf3());
        //endregion
        //region I/O Info
        if (mdProduct.getIo_control() == 1) {
            mket_inbound_id.setText(mdProductSerial.getInbound_id());
            mket_inbound_id.setVisibility(mdProductSerial.getInbound_id() == null ? View.GONE : View.VISIBLE);
            tv_inbound_lbl.setVisibility(mket_inbound_id.getVisibility());
            if (mdProductSerial.getInbound_conf_date() != null) {
                mket_inbound_date_conf_val.setText(
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(mdProductSerial.getInbound_conf_date()),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
            } else {
                mket_inbound_date_conf_val.setText(null);
            }
            mket_inbound_date_conf_val.setVisibility(mdProductSerial.getInbound_conf_date() == null ? View.GONE : View.VISIBLE);
            tv_inbound_date_conf_lbl.setVisibility(mket_inbound_date_conf_val.getVisibility());

            mket_move_code_val.setText(mdProductSerial.getMove_prefix() + "." + mdProductSerial.getMove_code());
            mket_move_code_val.setVisibility(mdProductSerial.getMove_prefix() == null ? View.GONE : View.VISIBLE);
            tv_move_code_lbl.setVisibility(mket_move_code_val.getVisibility());

            mket_move_group_val.setText(String.valueOf(mdProductSerial.getMove_group_code()));
            mket_move_group_val.setVisibility(mdProductSerial.getMove_group_code() == null ? View.GONE : View.VISIBLE);
            tv_move_group_lbl.setVisibility(mket_move_group_val.getVisibility());

            mket_outbound_id.setText(mdProductSerial.getOutbound_id());
            mket_outbound_id.setVisibility(mdProductSerial.getOutbound_id() == null ? View.GONE : View.VISIBLE);
            tv_outbound_lbl.setVisibility(mket_outbound_id.getVisibility());
            //
            setIoVisibility();
        } else {
            ll_io_info.setVisibility(View.GONE);
        }
        //endregion
        //
        applyProfile();
    }

    private boolean checkDbValInOption(SearchableSpinner ssComponent, String value) {
        //Se o value passado for null,
        //Considera q resultado ja existe, dessa form não será inserido pela rotina
        if (value == null || value.equalsIgnoreCase("null")) {
            return true;
        }
        //
        if (ssComponent != null) {
            if (ssComponent.getmOption() != null) {
                if(ssComponent.getmOption().size() == 0){
                    return true;
                }
                //
                for (HMAux aux : ssComponent.getmOption()) {
                    if (aux.get(SearchableSpinner.ID).equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    private void sqlInitializer() {
//        sql_ss_site = sql_ss_site != null ? sql_ss_site : new MD_Site_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
//        ).toSqlQuery();
//        //
//        sql_ss_site_zone = sql_ss_site_zone != null ? sql_ss_site_zone : new MD_Site_Zone_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
//                ss_site.getmValue().get(SearchableSpinner.ID)
//        ).toSqlQuery();
//        //
//        sql_ss_site_zone_local = sql_ss_site_zone_local != null ? sql_ss_site_zone_local : new MD_Site_Zone_Local_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
//                ss_site.getmValue().get(SearchableSpinner.ID),
//                ss_site_zone.getmValue().get(SearchableSpinner.ID)
//        ).toSqlQuery();
//        //
//        sql_ss_brand = sql_ss_brand != null ? sql_ss_brand : new MD_Brand_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
//                String.valueOf(mdProduct.getProduct_code())
//        ).toSqlQuery();
//        //
//        sql_ss_brand_model = sql_ss_brand_model != null ? new sql_ss_brand_model() : new MD_Brand_Model_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
//                String.valueOf(mdProduct.getProduct_code()),
//                ss_brand.getmValue().get(SearchableSpinner.ID)
//        ).toSqlQuery();
//        //
//        sql_ss_brand_color = sql_ss_brand_color != null ? sql_ss_brand_color : new MD_Brand_Color_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
//                String.valueOf(mdProduct.getProduct_code()),
//                ss_brand.getmValue().get(SearchableSpinner.ID)
//        ).toSqlQuery();
//        //
//        sql_ss_segment = sql_ss_segment != null ? sql_ss_segment : new MD_Segment_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
//                String.valueOf(mdProduct.getProduct_code())
//        ).toSqlQuery();
//        //
//        sql_ss_category_price = sql_ss_category_price != null ? sql_ss_category_price : new MD_Category_Price_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
//                String.valueOf(mdProduct.getProduct_code())
//        ).toSqlQuery();
//        //
//        sql_ss_class = sql_ss_class != null ? sql_ss_class : new MD_Class_Sql_SS(
//                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
//        ).toSqlQuery();
//
//    }

    private void listnersInitializer() {
        checkExistSerialListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    if(mket_serial_id.isValid()) {
                        delegate.onCheckButtonClick(
                                mdProduct.getProduct_id(),
                                ToolBox_Inf.removeAllLineBreaks(mket_serial_id.getText().toString()),
                                ""
                        );
                    }else{
                        showAlertDialog(
                                hmAux_Trans.get("alert_serial_validation_ttl"),
                                mket_serial_id.getmErrorMSG()
                        );
                    }
                }
            }
        };
        //
        saveSerialListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    if (!new_serial || mket_serial_id.isValid()) {
                        //Valida se alteração de site é valida.
                        if (!siteChanged || validateSiteChange()) {
                            if (validadeSerialLocation()) {
                                if (new_serial ||checkSerialChanges()) {
                                    //
                                    setUIDataToSerialObj();
                                    //
                                    delegate.onSaveWithChangesClick(
                                            mdProductSerial,
                                            !mket_serial_id.getText().toString().equals(mket_serial_id.getTag())
                                    );
                                } else {
                                    delegate.onSaveNoChangesClick(
                                            mdProductSerial,
                                            !mket_serial_id.getText().toString().equals(mket_serial_id.getTag())
                                    );
                                }
                            } else {
                                showAlertDialog(
                                        hmAux_Trans.get("alert_invalid_serial_local_ttl"),
                                        hmAux_Trans.get("alert_invalid_serial_local_msg")
                                );
                            }
                        } else {
                            showAlertDialog(
                                    hmAux_Trans.get("alert_serial_validation_ttl"),
                                    hmAux_Trans.get("alert_invalid_site_change_msg")
                            );
                        }

                    } else {
                        showAlertDialog(
                                hmAux_Trans.get("alert_serial_validation_ttl"),
                                mket_serial_id.getmErrorMSG()
                        );
                    }
                }
            }
        };

    }

    /**
     * FUNCIONLIDADE:
     *      Metodo que zera as referencias "default" de cada componente.
     * QUANDO É USADO:
     *      Após o usr alterar o serial digitado e a busca retornar que o serial digitado
     *  também é novo
     */
    private void cleanDbReference(){
        for (int i = 0; i < serialProperties.size(); i++) {
            Object propertie = serialProperties.get(i);
            //Se for SearchableSpinner
            if (propertie instanceof SearchableSpinner) {
                ((SearchableSpinner) propertie).setmValueBD(new HMAux());
            } else {
                //Se for EditText
                if (propertie instanceof MKEditTextNM) {
                    ((MKEditTextNM) propertie).setTag(null);
                }
            }
        }
    }

    private void iniAction() {
        mket_serial_id.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (!serialIdChanged && !s.equalsIgnoreCase((String) mket_serial_id.getTag())) {
                    serialIdChanged = true;
                    btn_action.setText(hmAux_Trans.get("btn_check_exists"));
                    btn_action.setOnClickListener(checkExistSerialListner);
                    blockAllProperties();
                }
            }
        });
        //
        mket_serial_id.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {
            @Override
            public void reportTextBySpecialist(String s) {
                pausedByScan = true;
            }
        });
        //
        btn_action.setOnClickListener(saveSerialListner);
        //
        ss_site.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAuxOld) {
                oldSite = hmAuxOld;
                oldZone.putAll(ss_site_zone.getmValue());
                oldLocal.putAll(ss_site_zone_local.getmValue());
                siteChanged = true;
            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                //
                if (!skip_validation) {
                    loadZoneSS(true);
                    //
                    loadLocalSS(true);
                }
                //final String tag = (String) ss_site.getTag() == null ? "" : (String) ss_site.getTag();
                //
                if (hmAux.size() == 0 && oldSite.size() > 0 && mdProductSerial.getTracking_list().size() > 0) {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_clear_tracking_list_ttl"),
                            hmAux_Trans.get("alert_clear_tracking_list_msg"),
                            dialogClearTrackingListner,
                            2,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    siteChanged = false;
                                    ss_site.setmValue(oldSite);
                                    ss_site_zone.setmValue(oldZone);
                                    ss_site_zone_local.setmValue(oldLocal);
                                    //
                                    loadZoneSS(false);
                                    //
                                    loadLocalSS(false);
                                }
                            }

                    );
                } else {
                    if (ss_site.hasChanged() && mdProductSerial.getTracking_list().size() > 0) {
                        //if (!hmAux.get(SearchableSpinner.ID).equals(oldSite.get(SearchableSpinner.ID)) && tracking_list.size() > 0) {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_keep_tracking_list_ttl"),
                                hmAux_Trans.get("alert_keep_tracking_list_msg"),
                                null,
                                2,
                                dialogClearTrackingListner
                        );
                    }
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
                    //Ao setar Zona, se só possuir um local, o seta automaticamente
                    if (ss_site_zone_local.getmOption().size() == 1) {
                        ss_site_zone_local.setmValue(ss_site_zone_local.getmOption().get(0));
                    }
                }
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
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                //
                loadModelSS(true);
                //
                loadColorSS(true);
            }
        });
        //
        ss_class.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                setClassIcon(hmAux);
            }
        });
        //
        iv_add_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useTracking) {
                    String site_val = ss_site.getmValue().get(SearchableSpinner.ID);
                    if (site_val != null && !site_val.equals("null")) {
                        showTrackingDialog();
                    } else {
                        showAlertDialog(
                                hmAux_Trans.get("alert_no_site_selected_ttl"),
                                hmAux_Trans.get("alert_no_site_selected_msg"),
                                null);
                    }
                }
            }
        });
        //
        tvCtListner = new TextViewCT.ITextViewCT() {
            @Override
            public void removeViews(TextViewCT textViewCT) {
                int idx = ll_tracking_content.indexOfChild(textViewCT);
                //
                mdProductSerial.getTracking_list().remove(idx);
                //
                ll_tracking_content.removeView(textViewCT);
                //
                setTrackingListChanged(true);
            }
        };
        //
        //Listner que zera trackingList.
        //Usado no NÃO da troca de site e no SIM do "limpar" site

        dialogClearTrackingListner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                ll_tracking_content.removeAllViews();
                //
                mdProductSerial.setTracking_list(new ArrayList<MD_Product_Serial_Tracking>());

            }
        };
        //
        fab_anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Anchora?", Toast.LENGTH_LONG).show();
            }
        });
        //
        iv_serial_dialog_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSerialInfoDialog();
            }
        });
    }

    private void showTrackingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.namoa_dialog_add_tracking, null);

        final MKEditTextNM mket_tracking = (MKEditTextNM) view.findViewById(R.id.namoa_dialog_add_tracking_mket_tracking);
        //mket_tracking.setHint(hmAux_Trans.get("tracking_hint_lbl"));
        controls_sta.add(mket_tracking);
        //
        TextView tv_tracking_ttl = (TextView) view.findViewById(R.id.namoa_dialog_add_tracking_tv_lbl);
        ImageView iv_action = (ImageView) view.findViewById(R.id.namoa_dialog_add_tracking_iv_action);
        final ImageView iv_close = (ImageView) view.findViewById(R.id.namoa_dialog_add_tracking_iv_close);
        //
        tv_tracking_ttl.setText(hmAux_Trans.get("dialog_tracking_ttl"));
        builder.setView(view);
        builder.setCancelable(false);
        //
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                controls_sta.remove(mket_tracking);
            }
        });
        //
        final AlertDialog show = builder.show();
        /*
         *Ini Action
         */
        iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mket_text = mket_tracking.getText().toString().trim().toUpperCase();

                if (mket_text.length() > 0) {
                    if (!isTrackingListed(mket_text)) {
                        if (ToolBox_Con.isOnline(context)) {
                            ToolBox_Inf.closeKeyboard(context, mket_tracking.getWindowToken());
                            //
                            searched_tracking = mket_text;
                            //
                            if (delegate != null) {
                                delegate.onTrackingSearchClick(
                                        mdProductSerial.getProduct_code(),
                                        mdProductSerial.getSerial_code(),
                                        mket_text,
                                        ss_site.getmValue().get(SearchableSpinner.ID)
                                );
                            }
                            //
                            show.dismiss();
                        } else {
                            ToolBox_Inf.showNoConnectionDialog(context);
                        }
                    } else {
                        showAlertDialog(
                                hmAux_Trans.get("alert_tracking_already_listed_ttl"),
                                hmAux_Trans.get("alert_tracking_already_listed_msg"),
                                null);
                    }
                }
            }
        });
        //
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                show.dismiss();
            }
        });
    }

    private void showSerialInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.namoa_dialog_serial_more_info, null);
        //
        /*
         * IniVar
         */
        TextView tv_dialog_ttl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_ttl);
        //
        LinearLayout ll_category = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_category);
        TextView tv_category_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_category_lbl);
        TextView tv_category_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_category_val);
        //
        LinearLayout ll_segment = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_segment);
        TextView tv_segment_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_segment_lbl);
        TextView tv_segment_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_segment_val);
        //
        LinearLayout ll_add_info1 = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_add_info1);
        TextView tv_add_info1_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_add_info1_lbl);
        TextView tv_add_info1_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_add_info1_val);
        //
        LinearLayout ll_add_info2 = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_add_info2);
        TextView tv_add_info2_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_add_info2_lbl);
        TextView tv_add_info2_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_add_info2_val);
        //
        LinearLayout ll_add_info3 = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_add_info3);
        TextView tv_add_info3_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_add_info3_lbl);
        TextView tv_add_info3_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_add_info3_val);
        //
        LinearLayout ll_io_info = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_io_info);
        LinearLayout ll_inbound = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_inbound);
        TextView tv_inbound_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_inbound_lbl);
        TextView tv_inbound_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_inbound_val);
        //
        LinearLayout ll_inbound_date = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_inbound_date);
        TextView tv_inbound_date_conf_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_inbound_date_conf_lbl);
        TextView tv_inbound_date_conf_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_inbound_date_conf_val);
        //
        LinearLayout ll_move = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_move);
        TextView tv_move_code_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_move_code_lbl);
        TextView tv_move_code_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_move_code_val);
        //
        LinearLayout ll_move_group = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_move_group);
        TextView tv_move_group_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_move_group_lbl);
        TextView tv_move_group_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_move_group_val);
        //
        LinearLayout ll_outbound = (LinearLayout) view.findViewById(R.id.dialog_serial_info_ll_outbound);
        TextView tv_outbound_lbl = (TextView) view.findViewById(R.id.dialog_serial_info_tv_outbound_lbl);
        TextView tv_outbound_val = (TextView) view.findViewById(R.id.dialog_serial_info_tv_outbound_val);
        //
        /*
         *Add valores
         */
        tv_dialog_ttl.setText(hmAux_Trans.get("dialog_serial_info_ttl"));
        //
        if (showCategorySegmentoInfo) {
            tv_category_lbl.setText(hmAux_Trans.get("dialog_serial_category_price_lbl"));
            tv_category_val.setText("");
            if(mdProductSerial.getCategory_price_id() != null && !mdProductSerial.getCategory_price_id().isEmpty() ){
                tv_category_val.setText(mdProductSerial.getCategory_price_id() + " - " + mdProductSerial.getCategory_price_desc());
            }
            //
            tv_segment_lbl.setText(hmAux_Trans.get("dialog_serial_segment_lbl"));
            tv_segment_val.setText("");
            if(mdProductSerial.getSegment_id() != null && !mdProductSerial.getSegment_id().isEmpty() ){
                tv_segment_val.setText(mdProductSerial.getSegment_id() + " - " + mdProductSerial.getSegment_desc());
            }
            ll_category.setVisibility(View.VISIBLE);
            ll_segment.setVisibility(View.VISIBLE);
        } else {
            ll_category.setVisibility(View.GONE);
            ll_segment.setVisibility(View.GONE);
        }
        //
        tv_add_info1_lbl.setText(hmAux_Trans.get("dialog_serial_add_info1_lbl"));
        tv_add_info1_val.setText(mdProductSerial.getAdd_inf1());
        //
        tv_add_info2_lbl.setText(hmAux_Trans.get("dialog_serial_add_info2_lbl"));
        tv_add_info2_val.setText(mdProductSerial.getAdd_inf2());
        //
        tv_add_info3_lbl.setText(hmAux_Trans.get("dialog_serial_add_info3_lbl"));
        tv_add_info3_val.setText(mdProductSerial.getAdd_inf3());
        //IO Info, somente se produto controlar I/O
        if (mdProductSerial.getInbound_id() == null
                && mdProductSerial.getInbound_conf_date() == null
                && mdProductSerial.getMove_code() == null
                && mdProductSerial.getMove_group_code() == null
                && mdProductSerial.getOutbound_id() == null) {
            ll_io_info.setVisibility(View.GONE);
        } else {
            if (mdProductSerial.getInbound_id() != null) {
                tv_inbound_lbl.setText(hmAux_Trans.get("dialog_serial_inbound_lbl"));
                tv_inbound_val.setText(mdProductSerial.getInbound_id());
            } else {
                ll_inbound.setVisibility(View.GONE);
            }
            //
            if (mdProductSerial.getInbound_conf_date() != null) {
                tv_inbound_date_conf_lbl.setText(hmAux_Trans.get("dialog_serial_inbound_date_lbl"));
                tv_inbound_date_conf_val.setText(ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(mdProductSerial.getInbound_conf_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
            } else {
                ll_inbound_date.setVisibility(View.GONE);
            }
            //
            if (mdProductSerial.getMove_code() != null) {
                tv_move_code_lbl.setText(hmAux_Trans.get("dialog_serial_move_lbl"));
                tv_move_code_val.setText(mdProductSerial.getMove_prefix() + "." + mdProductSerial.getMove_code());
            } else {
                ll_move.setVisibility(View.GONE);
            }
            //
            if (mdProductSerial.getMove_group_code() != null) {
                tv_move_group_lbl.setText(hmAux_Trans.get("dialog_serial_move_group_lbl"));
                tv_move_group_val.setText(mdProductSerial.getMove_group_code());
            } else {
                ll_move_group.setVisibility(View.GONE);
            }
            //
            if (mdProductSerial.getOutbound_id() != null) {
                tv_outbound_lbl.setText(hmAux_Trans.get("dialog_serial_outbound_lbl"));
                tv_outbound_val.setText(mdProductSerial.getOutbound_id());
            } else {
                ll_outbound.setVisibility(View.GONE);
            }
        }
        //
        builder.setView(view);
        //
        AlertDialog dialog = builder.show();

    }

    private void showAlertDialog(String title, String msg) {
        showAlertDialog(title, msg, null);
    }

    private void showAlertDialog(String title, String msg, @Nullable DialogInterface.OnClickListener positiveListner) {
        ToolBox.alertMSG(
                context,
                title,
                msg,
                positiveListner,
                0
        );
    }

    public void appendTracking(String tracking) {
        TextViewCT viewCT = new TextViewCT(context);
        viewCT.setmValue(tracking);
        viewCT.setOnRemoveViewsListener(tvCtListner);
        //
        ll_tracking_content.addView(viewCT);
    }

    public void cleanSearched_tracking() {
        searched_tracking = "";
    }

    public boolean isTrackingListed(String tracking) {
        for (int i = 0; i < mdProductSerial.getTracking_list().size(); i++) {
            if (mdProductSerial.getTracking_list().get(i).getTracking().equals(tracking)) {
                return true;
            }
        }
        return false;
    }

    public void processTrackingResult(HMAux auxResult) {
        if (auxResult.containsKey(WS_Serial_Tracking_Search.TRACKING_RESULT_KEY)) {
            if (auxResult.get(WS_Serial_Tracking_Search.TRACKING_RESULT_KEY).equals(WS_Serial_Tracking_Search.NOT_EXISTS)) {
                //
                mdProductSerial.getTracking_list().add(
                        buildTrackingObj(searched_tracking)
                );
                //
                appendTracking(searched_tracking);
                //
                setTrackingListChanged(true);
                //
                cleanSearched_tracking();
                //
                scrollToTracking(ll_tracking_content);
            } else {
                showAlertDialog(
                        hmAux_Trans.get("alert_tracking_unavailable_ttl"),
                        hmAux_Trans.get("alert_tracking_unavailable_msg"),
                        null);
            }
        }
    }

    private void scrollToTracking(View view) {
        int x = (int) view.getX();
        int y = view.getTop() + ((View) view.getParent()).getTop();

        sv_serial.smoothScrollTo(x, y);
    }

    private void setTrackingListChanged(boolean trackingListChanged) {
        this.trackingListChanged = trackingListChanged;
    }

    private MD_Product_Serial_Tracking buildTrackingObj(String searched_tracking) {
        MD_Product_Serial_Tracking auxTracking = new MD_Product_Serial_Tracking();
        //
        auxTracking.setTracking(searched_tracking);
        auxTracking.setPk(mdProductSerial);
        //
        return auxTracking;
    }

    private void setClassIcon(HMAux item) {
        if (item != null && item.containsKey(MD_ClassDao.CLASS_AVAILABLE) && item.get(MD_ClassDao.CLASS_AVAILABLE) != null && item.containsKey(MD_ClassDao.CLASS_COLOR) && item.get(MD_ClassDao.CLASS_COLOR) != null) {
            iv_class_icon.setVisibility(View.VISIBLE);
            if (item.get(MD_ClassDao.CLASS_AVAILABLE).equals("1")) {
                Drawable drawable = context.getDrawable(R.drawable.ic_tag_black_24dp);
                drawable.setColorFilter(Color.parseColor(item.get(MD_ClassDao.CLASS_COLOR)), PorterDuff.Mode.SRC_ATOP);
                iv_class_icon.setImageDrawable(drawable);
            } else {
                Drawable drawable = context.getDrawable(R.drawable.ic_ban_black_24dp);
                drawable.setColorFilter(Color.parseColor(item.get(MD_ClassDao.CLASS_COLOR)), PorterDuff.Mode.SRC_ATOP);
                iv_class_icon.setImageDrawable(drawable);
            }
        } else {
            iv_class_icon.setImageDrawable(null);
            iv_class_icon.setVisibility(View.INVISIBLE);
        }
    }

    private void setUIDataToSerialObj() {
        //
//        if (mdProductSerial.getProduct_code() == -1) {
//            mdProductSerial.setCustomer_code(mdProduct.getCustomer_code());
//            mdProductSerial.setProduct_code(mdProduct.getProduct_code());
//            mdProductSerial.setProduct_id(mdProduct.getProduct_id());
//            mdProductSerial.setProduct_io_control(mdProduct.getIo_control());
//            mdProductSerial.setLocal_control(mdProduct.getLocal_control());
//        }
        //
        mdProductSerial.setClass_code(ToolBox_Inf.mIntegerParse(ss_class.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setClass_id(ss_class.getmValue().get(MD_ClassDao.CLASS_ID));
        mdProductSerial.setClass_type(ss_class.getmValue().get(MD_ClassDao.CLASS_TYPE));
        mdProductSerial.setClass_available(ToolBox_Inf.mIntegerParse(ss_class.getmValue().get(MD_ClassDao.CLASS_AVAILABLE)));
        mdProductSerial.setClass_color(ss_class.getmValue().get(MD_ClassDao.CLASS_COLOR));
        //
        mdProductSerial.setSite_code(ToolBox_Inf.mIntegerParse(ss_site.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setSite_id(ss_site.getmValue().get(MD_SiteDao.SITE_ID));
        mdProductSerial.setSite_desc(ss_site.getmValue().get(SearchableSpinner.DESCRIPTION));
        mdProductSerial.setSite_io_control(ToolBox_Inf.mIntegerParse(ss_site.getmValue().get(MD_SiteDao.IO_CONTROL)));
        mdProductSerial.setInbound_auto_create(ToolBox_Inf.mIntegerParse(ss_site.getmValue().get(MD_SiteDao.INBOUND_AUTO_CREATE)));
        //
        mdProductSerial.setZone_code(ToolBox_Inf.mIntegerParse(ss_site_zone.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setZone_id(ss_site_zone.getmValue().get(MD_Site_ZoneDao.ZONE_ID));
        mdProductSerial.setZone_desc(ss_site_zone.getmValue().get(SearchableSpinner.DESCRIPTION));
        //
        mdProductSerial.setLocal_code(ToolBox_Inf.mIntegerParse(ss_site_zone_local.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setLocal_id(ss_site_zone_local.getmValue().get(SearchableSpinner.DESCRIPTION));
        //
        mdProductSerial.setAdd_inf1(mket_info1.getText().toString().trim());
        mdProductSerial.setAdd_inf2(mket_info2.getText().toString().trim());
        mdProductSerial.setAdd_inf3(mket_info3.getText().toString().trim());
        //
        mdProductSerial.setBrand_code(ToolBox_Inf.mIntegerParse(ss_brand.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setBrand_id(ss_brand.getmValue().get(MD_BrandDao.BRAND_ID));
        mdProductSerial.setBrand_desc(ss_brand.getmValue().get(SearchableSpinner.DESCRIPTION));
        //
        mdProductSerial.setModel_code(ToolBox_Inf.mIntegerParse(ss_brand_model.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setModel_id(ss_brand_model.getmValue().get(MD_Brand_ModelDao.MODEL_ID));
        mdProductSerial.setModel_desc(ss_brand_model.getmValue().get(SearchableSpinner.DESCRIPTION));
        //
        mdProductSerial.setColor_code(ToolBox_Inf.mIntegerParse(ss_brand_color.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setColor_id(ss_brand_color.getmValue().get(MD_Brand_ColorDao.COLOR_ID));
        mdProductSerial.setColor_desc(ss_brand_color.getmValue().get(SearchableSpinner.DESCRIPTION));
        //
        mdProductSerial.setSegment_code(ToolBox_Inf.mIntegerParse(ss_segment.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setSegment_id(ss_segment.getmValue().get(MD_SegmentDao.SEGMENT_ID));
        mdProductSerial.setSegment_desc(ss_segment.getmValue().get(SearchableSpinner.DESCRIPTION));
        //
        mdProductSerial.setCategory_price_code(ToolBox_Inf.mIntegerParse(ss_category_price.getmValue().get(SearchableSpinner.ID)));
        mdProductSerial.setCategory_price_id(ss_category_price.getmValue().get(MD_Category_PriceDao.CATEGORY_PRICE_ID));
        mdProductSerial.setCategory_price_desc(ss_category_price.getmValue().get(SearchableSpinner.DESCRIPTION));
        //
        mdProductSerial.setUpdate_required(1);
        //
        String profile = ToolBox_Inf.getMenuProfilesAsStringConcat(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL,"|");
        mdProductSerial.setProfile(profile);
        //
        mdProductSerial.setEdit_mode(viewMode);
        //
        mdProductSerial.setLog_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
    }

    //region Profile And View
    private void applyProfile() {
        //Bloqueia todos os profiles
        blockAllProperties();
        if (!serialIdChanged) {
            if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, Constant.PROFILE_PRJ001_PRODUCT_SERIAL_PARAM_CHANGE_CLASS)) {
                iv_class_icon.setEnabled(true);
                ss_class.setmEnabled(true);
            }
            //
            if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, Constant.PROFILE_PRJ001_PRODUCT_SERIAL_PARAM_CHANGE_LOCATION)) {
                ss_site.setmEnabled(true);
                ss_site_zone.setmEnabled(true);
                ss_site_zone_local.setmEnabled(true);
                iv_add_tracking.setEnabled(true);
                //
                for (int i = 0; i < ll_tracking_content.getChildCount(); i++) {
                    if (ll_tracking_content.getChildAt(i) instanceof TextViewCT) {
                        ((TextViewCT) ll_tracking_content.getChildAt(i)).setmEnabled(true);
                    }
                }
            }
            //
            if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, Constant.PROFILE_PRJ001_PRODUCT_SERIAL_PARAM_EDIT)) {
                iv_class_icon.setEnabled(true);
                ss_class.setmEnabled(true);
                ss_site.setmEnabled(true);
                ss_site_zone.setmEnabled(true);
                ss_site_zone_local.setmEnabled(true);
                iv_add_tracking.setEnabled(true);
                //
                for (int i = 0; i < ll_tracking_content.getChildCount(); i++) {
                    if (ll_tracking_content.getChildAt(i) instanceof TextViewCT) {
                        ((TextViewCT) ll_tracking_content.getChildAt(i)).setmEnabled(true);
                    }
                }
                //
                ss_brand.setmEnabled(true);
                ss_brand_model.setmEnabled(true);
                ss_brand_color.setmEnabled(true);
                ss_segment.setmEnabled(true);
                ss_category_price.setmEnabled(true);
                mket_info1.setEnabled(true);
                mket_info2.setEnabled(true);
                mket_info3.setEnabled(true);
            }
        }
        if (mdProduct.getLocal_control() == 0) {
            ll_serial_location.setVisibility(View.GONE);
        }

        //Por fim aplica "profile do customer" se deve ou não exibir os dados de tracking
        if (!useTracking) {
            ll_tracking.setVisibility(View.GONE);
            ll_tracking_content.setVisibility(View.GONE);
        }
    }

    private void blockAllProperties() {
        iv_class_icon.setEnabled(false);
        ss_class.setmEnabled(false);
        ss_site.setmEnabled(false);
        ss_site_zone.setmEnabled(false);
        ss_site_zone_local.setmEnabled(false);
        iv_add_tracking.setEnabled(false);
        //
        for (int i = 0; i < ll_tracking_content.getChildCount(); i++) {
            if (ll_tracking_content.getChildAt(i) instanceof TextViewCT) {
                ((TextViewCT) ll_tracking_content.getChildAt(i)).setmEnabled(false);
            }
        }
        //
        ss_brand.setmEnabled(false);
        ss_brand_model.setmEnabled(false);
        ss_brand_color.setmEnabled(false);
        ss_segment.setmEnabled(false);
        ss_category_price.setmEnabled(false);
        mket_info1.setEnabled(false);
        mket_info2.setEnabled(false);
        mket_info3.setEnabled(false);

    }

    private void applyViewMode() {
        switch (viewMode) {
            case VIEW_FULL_EDIT:
                tv_brand_model_color.setVisibility(View.GONE);
                iv_serial_dialog_info.setVisibility(View.GONE);
                ll_serial_class.setVisibility(View.VISIBLE);
                ll_serial_location.setVisibility(View.VISIBLE);
                ll_serial_properties.setVisibility(View.VISIBLE);
                ll_serial_add_info.setVisibility(View.VISIBLE);
                setIoVisibility();
                btn_action.setVisibility(View.VISIBLE);
                fab_anchor.setVisibility(View.VISIBLE);
                break;
            case VIEW_SO_EDIT:
                tv_brand_model_color.setVisibility(View.VISIBLE);
                iv_serial_dialog_info.setVisibility(View.VISIBLE);
                ll_serial_class.setVisibility(View.VISIBLE);
                ll_serial_location.setVisibility(View.VISIBLE);
                ll_serial_properties.setVisibility(View.GONE);
                ll_serial_add_info.setVisibility(View.GONE);
                ll_io_info.setVisibility(View.GONE);
                btn_action.setVisibility(View.VISIBLE);
                fab_anchor.setVisibility(View.GONE);
                break;
            default:
                tv_brand_model_color.setVisibility(View.GONE);
                iv_serial_dialog_info.setVisibility(View.GONE);
                ll_serial_class.setVisibility(View.GONE);
                ll_serial_location.setVisibility(View.GONE);
                ll_serial_properties.setVisibility(View.GONE);
                ll_serial_add_info.setVisibility(View.GONE);
                ll_io_info.setVisibility(View.GONE);
                btn_action.setVisibility(View.GONE);
                fab_anchor.setVisibility(View.GONE);
        }
    }

    private void setIoVisibility() {
        if (mdProduct.getIo_control() == 1) {
            if (mket_inbound_id.getVisibility() == View.GONE &&
                    mket_inbound_date_conf_val.getVisibility() == View.GONE &&
                    mket_move_code_val.getVisibility() == View.GONE &&
                    mket_move_group_val.getVisibility() == View.GONE &&
                    mket_outbound_id.getVisibility() == View.GONE
                    ) {
                ll_io_info.setVisibility(View.GONE);
            } else {
                ll_io_info.setVisibility(View.VISIBLE);
            }
        } else {
            ll_io_info.setVisibility(View.GONE);
        }
    }
    //endregion

    //region Validations
    private boolean validateSiteChange() {
        //Se não houve mudança de site, retonar verdadeiro.
        if(!siteChanged){
            return true;
        }
        //
        HMAux siteSelected = ss_site.getmValue();
        //Por mais maluco que seja, esse if abaixo significa
        //if site origem null == site.atual null
        if (
                (ss_site.getmValueBD().size() == 0
                        && siteSelected.containsKey(SearchableSpinner.ID)
                        && siteSelected.get(SearchableSpinner.ID).equals("null")
                ) || (ss_site.getmValue().containsKey(SearchableSpinner.ID)
                        && ss_site.getmValue().get(SearchableSpinner.ID).equals("null")
                        && siteSelected.containsKey(SearchableSpinner.ID)
                        && siteSelected.get(SearchableSpinner.ID).equals("null")
                )
                ) {
            Log.d("SITE_CHANGE", "true");
            return true;
        }
        //Valida se hmAux tem as chave, mas ainda pode ser null
//        if (
//            !siteSelected.containsKey(SearchableSpinner.ID)
//            || !siteSelected.containsKey(SearchableSpinner.DESCRIPTION)
//            || !siteSelected.containsKey(MD_SiteDao.SITE_ID)
//            || !siteSelected.containsKey(MD_SiteDao.IO_CONTROL)
//            || !siteSelected.containsKey(MD_SiteDao.INBOUND_AUTO_CREATE)
//        ) {
//            return false;
//        }
        //
        if (mdProductSerial.getProduct_io_control() == 0) {
            Log.d("SITE_CHANGE", "true");
            return true;

        } else {
            if (mdProductSerial.getSite_code() == null || mdProductSerial.getSite_io_control() == 0) {
                if (siteSelected.size() == 0//size 0 igual a site null selecionado
                        || siteSelected.get(MD_SiteDao.IO_CONTROL).equals("0")
                        || (siteSelected.get(MD_SiteDao.IO_CONTROL).equals("1")
                        && siteSelected.get(MD_SiteDao.INBOUND_AUTO_CREATE).equals("1")
                )
                        ) {
                    Log.d("SITE_CHANGE", "true");
                    return true;
                }
            }
        }
        Log.d("SITE_CHANGE", "false");
        //
        return false;
    }

    private boolean validadeSerialLocation() {
        boolean site = ss_site.getmValue().get(SearchableSpinner.ID) != null && !ss_site.getmValue().get(SearchableSpinner.ID).equals("null") && ss_site.getmValue().get(SearchableSpinner.ID).length() > 0;
        boolean zone = ss_site_zone.getmValue().get(SearchableSpinner.ID) != null && !ss_site_zone.getmValue().get(SearchableSpinner.ID).equals("null") && ss_site_zone.getmValue().get(SearchableSpinner.ID).length() > 0;
        boolean local = ss_site_zone_local.getmValue().get(SearchableSpinner.ID) != null && !ss_site_zone_local.getmValue().get(SearchableSpinner.ID).equals("null") && ss_site_zone_local.getmValue().get(SearchableSpinner.ID).length() > 0;
        //
        if (site && zone && local) {
            //limpa marcação de erro.
            ss_site.setBackground(null);
            ss_site_zone.setBackground(null);
            ss_site_zone_local.setBackground(null);
            return true;
        }

        if (!site && !zone && !local) {
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
        int y = ss_site_zone_local.getTop() + ((View) ss_site_zone_local.getParent()).getTop();
        sv_serial.smoothScrollTo(x, y);

        return false;
    }

    /**
     * Faz loop no arraylist de itens verificando se
     * houve alteração de valor.
     * V2 - Usando metodo do proprio Spinner para validar se valor original
     * , valor foi alterado.
     *
     * @return
     */
    private boolean checkSerialChanges() {
        if (new_serial && !mket_serial_id.getText().toString().equals(mket_serial_id.getTag())) {
            serialInfoChanges = true;
            return true;
        }

        if (trackingListChanged) {
            serialInfoChanges = true;
            return true;
        }

        for (int i = 0; i < serialProperties.size(); i++) {
            Object propertie = serialProperties.get(i);
            //Se for SearchableSpinner
            if (propertie instanceof SearchableSpinner) {
                if (((SearchableSpinner) propertie).hasChangedBD()) {
                    serialInfoChanges = true;
                    return true;
                }
            } else {
                //Se for EditText
                if (propertie instanceof MKEditTextNM) {
                    String tag = (String) ((MKEditTextNM) propertie).getTag() == null ? "" : (String) ((MKEditTextNM) propertie).getTag();
                    String text = ((MKEditTextNM) propertie).getText().toString();

                    if (!text.equals(tag)) {
                        // if (!((EditText) propertie).getText().toString().equals((String)((EditText) propertie).getTag())) {
                        serialInfoChanges = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //endregion

    /*public void saveSerialInDb(){
        MD_Product_Serial_TrackingDao trackingDao = new MD_Product_Serial_TrackingDao(context);
        //Remove os tracking para reinserir os que ficaram
        trackingDao.remove(new
                MD_Product_Serial_Tracking_Sql_002(
                        mdProductSerial.getCustomer_code(),
                    mdProductSerial.getProduct_code(),
                    mdProductSerial.getSerial_tmp()
                ).toSqlQuery()
        );
        //Salva dados alterados do S.O
        md.addUpdateTmp(productSerial);

    }*/
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
        loadClass(false);
    }

    /**
     * Carrega lista que sera exibida no spinner.
     * Se parameto true, apaga valor atual do spinner.
     *
     * @param reset_val
     */
    private void loadClass(boolean reset_val) {
        ArrayList<HMAux> classList = new ArrayList<>();
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_class, null, null, false, false);
        }
        //
        MD_ClassDao classDao = new MD_ClassDao(context);
        //
        classList = (ArrayList<HMAux>) classDao.query_HM(new MD_Class_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
        ).toSqlQuery());
        ss_class.setmOption(classList);
        //
        if (ss_class.getmValue() != null && ss_class.getmValue().size() > 0 && !checkDbValInOption(ss_class, String.valueOf(mdProductSerial.getClass_code()))) {
            ArrayList<HMAux> newOption = new ArrayList<>();
            newOption.add(ss_class.getmValue());
            newOption.addAll(classList);
            classList = newOption;
            ss_class.setmOption(classList);
        }

    }

    private void loadCategoryPrice(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_category_price, null, null, false, false);
        }
        //
        MD_Category_PriceDao categoryPriceDao = new MD_Category_PriceDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> categoryPriceList = (ArrayList<HMAux>) categoryPriceDao.query_HM(new MD_Category_Price_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                String.valueOf(mdProduct.getProduct_code())
        ).toSqlQuery());
        //
        ss_category_price.setmOption(categoryPriceList);
        //
        if (ss_category_price.getmValue() != null && ss_category_price.getmValue().size() > 0 && !checkDbValInOption(ss_category_price, String.valueOf(mdProductSerial.getCategory_price_code()))) {
            ArrayList<HMAux> newOption = new ArrayList<>();
            newOption.add(ss_category_price.getmValue());
            newOption.addAll(categoryPriceList);
            categoryPriceList = newOption;
            //
            ss_category_price.setmOption(categoryPriceList);
        }

    }

    private void loadSegment(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_segment, null, null, false, false);
        }
        //
        MD_SegmentDao segmentDao = new MD_SegmentDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> segmentList = (ArrayList<HMAux>) segmentDao.query_HM(new MD_Segment_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                String.valueOf(mdProduct.getProduct_code())
        ).toSqlQuery());
        //
        ss_segment.setmOption(segmentList);
        //
        if (!checkDbValInOption(ss_segment, String.valueOf(mdProductSerial.getSegment_code()))) {
            ArrayList<HMAux> newOption = new ArrayList<>();
            newOption.add(ss_segment.getmValue());
            newOption.addAll(segmentList);
            segmentList = newOption;
            //
            ss_segment.setmOption(segmentList);
        }

    }

    private void loadColorSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_brand_color, null, null, false, false);
        }
        //
        MD_Brand_ColorDao brandColorDao = new MD_Brand_ColorDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> colorList = (ArrayList<HMAux>) brandColorDao.query_HM(new MD_Brand_Color_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                String.valueOf(mdProduct.getProduct_code()),
                ss_brand.getmValue().get(SearchableSpinner.ID)
        ).toSqlQuery());
        //
        ss_brand_color.setmOption(colorList);
        //
        if( !ss_brand.hasChangedBD()) {
            if (ss_brand_color.getmValue() != null && ss_brand_color.getmValue().size() > 0 && !checkDbValInOption(ss_brand_color, String.valueOf(mdProductSerial.getColor_code()))) {
                ArrayList<HMAux> newOption = new ArrayList<>();
                newOption.add(ss_brand_color.getmValue());
                newOption.addAll(colorList);
                colorList = newOption;
                //
                ss_brand_color.setmOption(colorList);
            }
        }

    }

    private void loadModelSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_brand_model, null, null, false, false);
        }
        //
        MD_Brand_ModelDao brandModelDao = new MD_Brand_ModelDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> modelList = (ArrayList<HMAux>) brandModelDao.query_HM(
                new MD_Brand_Model_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(mdProduct.getProduct_code()),
                        ss_brand.getmValue().get(SearchableSpinner.ID)
                ).toSqlQuery()
        );
        //
        ss_brand_model.setmOption(modelList);
        //
        if( !ss_brand.hasChangedBD()) {
            if (ss_brand_model.getmValue() != null && ss_brand_model.getmValue().size() > 0 && !checkDbValInOption(ss_brand_model, String.valueOf(mdProductSerial.getModel_code()))) {
                ArrayList<HMAux> newOption = new ArrayList<>();
                newOption.add(ss_brand_model.getmValue());
                newOption.addAll(modelList);
                modelList = newOption;
                //
                ss_brand_model.setmOption(modelList);
            }
        }

    }

    private void loadBrandSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_brand, null, null, false, false);
        }
        //
        MD_BrandDao brandDao = new MD_BrandDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> brandList = (ArrayList<HMAux>) brandDao.query_HM(new MD_Brand_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                String.valueOf(mdProduct.getProduct_code())
        ).toSqlQuery());
        //
        ss_brand.setmOption(brandList);
        //
        if (ss_brand.getmValue() != null && ss_brand.getmValue().size() > 0 && !checkDbValInOption(ss_brand, String.valueOf(mdProductSerial.getBrand_code()))) {
            ArrayList<HMAux> newOption = new ArrayList<>();
            newOption.add(ss_brand.getmValue());
            newOption.addAll(brandList);
            brandList = newOption;
            //
            ss_brand.setmOption(brandList);
        }

    }

    private void loadSiteSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_site, null, null, false, true);
        }

        MD_SiteDao siteDao = new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> siteList = (ArrayList<HMAux>) siteDao.query_HM(
                new MD_Site_Sql_SS_002(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );
        //Se Site atual controla IO,exibe apenas ele mesmo como opção.
        if (mdProductSerial.getSite_io_control() == null || mdProductSerial.getSite_io_control() == 0) {
            ss_site.setmOption(siteList);
        } else {
            siteList.clear();
            ss_site.setmOption(null);
            //ss_site.setmEnabled(false);
        }
        //
        if (ss_site.getmValue() != null && ss_site.getmValue().size() > 0 && !checkDbValInOption(ss_site, String.valueOf(mdProductSerial.getSite_code()))) {
            ArrayList<HMAux> newOption = new ArrayList<>();
            newOption.add(ss_site.getmValue());
            newOption.addAll(siteList);
            siteList = newOption;
            //
            ss_site.setmOption(siteList);
        }

    }

    private void loadZoneSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_site_zone, null, null, false, true);
        }
        //
        MD_Site_ZoneDao siteZoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        ArrayList<HMAux> zoneList = (ArrayList<HMAux>) siteZoneDao.query_HM(new MD_Site_Zone_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                ss_site.getmValue().get(SearchableSpinner.ID)
        ).toSqlQuery());
        //
        ss_site_zone.setmOption(zoneList);
        //
        if( !ss_site.hasChangedBD()) {
            if ( ss_site_zone.getmValue() != null && ss_site_zone.getmValue().size() > 0 && !checkDbValInOption(ss_site_zone, String.valueOf(mdProductSerial.getZone_code()))) {
                ArrayList<HMAux> newOption = new ArrayList<>();
                newOption.add(ss_site_zone.getmValue());
                newOption.addAll(zoneList);
                zoneList = newOption;
                //
                ss_site_zone.setmOption(zoneList);
            }
        }

    }

    private void loadLocalSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_site_zone_local, null, null, false, true);
        }
        //
        MD_Site_Zone_LocalDao siteZoneLocalDao = new MD_Site_Zone_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        ArrayList<HMAux> localList = (ArrayList<HMAux>) siteZoneLocalDao.query_HM(new MD_Site_Zone_Local_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                ss_site.getmValue().get(SearchableSpinner.ID),
                ss_site_zone.getmValue().get(SearchableSpinner.ID)
        ).toSqlQuery());
        //
        ss_site_zone_local.setmOption(localList);
        //
        if( !ss_site.hasChangedBD() && !ss_site_zone.hasChangedBD()) {
            if (ss_site_zone_local.getmValue() != null && ss_site_zone_local.getmValue().size() > 0 && !checkDbValInOption(ss_site_zone_local, String.valueOf(mdProductSerial.getLocal_code()))) {
                ArrayList<HMAux> newOption = new ArrayList<>();
                newOption.add(ss_site_zone_local.getmValue());
                newOption.addAll(localList);
                localList = newOption;
                //
                ss_site_zone_local.setmOption(localList);
            }
        }

    }

    //region FragLifeCycle
    @Override
    public void onResume() {
        super.onResume();
        if (!pausedByScan) {
            loadDataToScreen();
        }
        pausedByScan = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }
    //endregion
}
