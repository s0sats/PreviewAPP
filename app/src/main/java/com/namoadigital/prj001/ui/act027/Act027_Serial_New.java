package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SM_SO;

import java.util.ArrayList;

/**
 * Created by neomatrix on 14/08/17.
 */

public class Act027_Serial_New extends BaseFragment {

    private boolean bStatus;

    private Context context;

    private SM_SO mSm_so;

    private MD_ProductDao mMdProductDao;

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

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;

        this.product_code = mSm_so.getProduct_code();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bStatus = true;

        View view = inflater.inflate(R.layout.act027_serial_content_new, container, false);
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
//        mPresenter = new Act027_Serial_Presenter_Impl(
//                context,
//                this,
//                new MD_ProductDao(
//                        context,
//                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                        Constant.DB_VERSION_CUSTOM),
//                product_code,
//                hmAux_Trans,
//                new MD_Product_SerialDao(
//                        context,
//                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                        Constant.DB_VERSION_CUSTOM),
//                data
//        );
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
//        views.add(tv_product_code_label);
//        views.add(tv_product_id_label);
//        views.add(tv_product_desc_label);
        views.add(tv_required_lbl);
        views.add(tv_allow_new_lbl);
        views.add(tv_serial_ttl);
        views.add(tv_serial_location_ttl);
//        views.add(ss_site);
//        views.add(ss_site_zone);
//        views.add(ss_site_zone_local);
//        views.add(tv_serial_add_info_ttl);
        views.add(et_info1);
        views.add(et_info2);
        views.add(et_info3);
        views.add(tv_serial_properties_ttl);
//        views.add(ss_brand);
//        views.add(ss_brand_model);
//        views.add(ss_brand_color);
//        views.add(ss_segment);
//        views.add(ss_category_price);
//        views.add(ss_site_owner);
        //views.add(btn_action);
        //


    }

    private void recoverIntentsInfo() {

    }

    private void iniListners() {

    }

    private void iniAction() {

    }

    public void loadDataToScreen() {
        if (bStatus) {

        }

    }

    public void loadScreenToData() {
        if (bStatus) {

        }
    }

}

