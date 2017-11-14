package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.ctls.TextViewCT;
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
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.namoadigital.prj001.ui.act023.Act023_Main.SITE_DESC_OWNER;
import static com.namoadigital.prj001.util.ToolBox_Inf.setSSmValue;

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
    private MKEditTextNM et_info1;
    private MKEditTextNM et_info2;
    private MKEditTextNM et_info3;
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
    //Revisão S.O 3 Tracking
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
    //
    private TextView tv_brand_model_color;
    private ImageView iv_serial_add_info;
    private String dialog_serial_category_price_desc;
    private String dialog_serial_segment_desc;
    private String brand_model_color_lbl;

    private Button btn_action;

    private View.OnClickListener listnerSaveSerial;

    private SM_SO mSm_so;
    private Act027_Main mMain;

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
        //
        this.product_code = mSm_so.getProduct_code();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setRetainInstance(true);
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
        mMain = (Act027_Main) getActivity();
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
                mSm_so,
                new MD_Product_Serial_TrackingDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                tracking_list
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
        //
        tv_brand_model_color = (TextView) view.findViewById(R.id.act027_serial_content_tv_brand_model_color);
        //
        tv_serial_ttl = (TextView) view.findViewById(R.id.act027_serial_content_tv_serial_ttl);
        tv_serial_ttl.setTag("serial_ttl");
        //
        iv_serial_add_info = (ImageView) view.findViewById(R.id.act027_serial_content_iv_serial_add_info);
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
        //ss_site.setmLabel(hmAux_Trans.get("site_lbl"));
        ss_site.setmHint(hmAux_Trans.get("site_lbl"));
        ss_site.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_site_zone = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_site_zone);
        //ss_site_zone.setmLabel(hmAux_Trans.get("site_zone_lbl"));
        ss_site_zone.setmHint(hmAux_Trans.get("site_zone_lbl"));
        ss_site_zone.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //
        ss_site_zone_local = (SearchableSpinner) view.findViewById(R.id.act027_serial_content_ss_site_zone_local);
        //ss_site_zone_local.setmLabel(hmAux_Trans.get("site_zone_local_lbl"));
        ss_site_zone_local.setmHint(hmAux_Trans.get("site_zone_local_lbl"));
        ss_site_zone_local.setmTitle(hmAux_Trans.get("searchable_spinner_lbl"));
        //TRACKIG
        ll_tracking = (LinearLayout) view.findViewById(R.id.act027_serial_content_ll_serial_tracking);
        tv_tracking = (TextView) view.findViewById(R.id.act027_serial_content_tv_serial_tracking_ttl);
        tv_tracking.setTag("tracking_ttl");
        tv_tracking.setText(hmAux_Trans.get("tracking_ttl"));
        iv_add_tracking = (ImageView) view.findViewById(R.id.act027_serial_content_iv_add_tracking);
        ll_tracking_content = (LinearLayout) view.findViewById(R.id.act027_serial_content_ll_tracking_container);
        //
        ll_serial_add_info = (LinearLayout) view.findViewById(R.id.act027_serial_content_ll_serial_add_info);
        //
        tv_serial_add_info_ttl = (TextView) view.findViewById(R.id.act027_serial_content_tv_serial_add_info_ttl);
        tv_serial_add_info_ttl.setTag("serial_add_info_ttl");
        tv_serial_add_info_ttl.setText(hmAux_Trans.get("serial_add_info_ttl"));
        //
        et_info1 = (MKEditTextNM) view.findViewById(R.id.act027_serial_content_et_info1);
        et_info1.setTag("add_info1_lbl");
        //
        et_info2 = (MKEditTextNM) view.findViewById(R.id.act027_serial_content_et_info2);
        et_info2.setTag("add_info2_lbl");
        //
        et_info3 = (MKEditTextNM) view.findViewById(R.id.act027_serial_content_et_info3);
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
        views.add(tv_tracking);
        //
        //Adiciona Componentes com dados do serial ao arrayList de validação
        serialProperties.add(ss_site);
        serialProperties.add(ss_site_zone);
        serialProperties.add(ss_site_zone_local);
        serialProperties.add(ss_brand);
        serialProperties.add(ss_brand_model);
        serialProperties.add(ss_brand_color);
        serialProperties.add(ss_segment);
        serialProperties.add(ss_category_price);
        serialProperties.add(ss_site_owner);
        serialProperties.add(et_info1);
        serialProperties.add(et_info2);
        serialProperties.add(et_info3);
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
                if (checkSerialChangesV2()) {
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
            public void onItemPreSelected(HMAux hmAuxOld) {
                oldSite = hmAuxOld;
                oldZone.putAll(ss_site_zone.getmValue());
                oldLocal.putAll(ss_site_zone_local.getmValue());
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
                if (hmAux.size() == 0 && oldSite.size() > 0 && tracking_list.size() > 0) {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_clear_tracking_list_ttl"),
                            hmAux_Trans.get("alert_clear_tracking_list_msg"),
                            dialogClearTrackingListner,
                            2,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ss_site.setmValue(oldSite);
                                    ss_site_zone.setmValue(oldZone);
                                    ss_site_zone_local.setmValue(oldLocal);
                                    //
                                    loadZoneSS(false);
                                    //
                                    loadLocalSS(false);
                                }
                            }

                    );//
                } else {
                    if (ss_site.hasChanged() && tracking_list.size() > 0) {
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
                    if(ss_site_zone_local.getmOption().size() == 1){
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
                    setSSmValue(ss_site, hmAux.get(MD_SiteDao.SITE_CODE), hmAux.get(MD_SiteDao.SITE_DESC), false);
                    //Seta valor da zone e refaz HmAux baseado no novo site.
                    loadZoneSS(true);
                    setSSmValue(ss_site_zone, hmAux.get(MD_Site_ZoneDao.ZONE_CODE), hmAux.get(MD_Site_ZoneDao.ZONE_DESC), false);
                    //
                    loadLocalSS(false);
                    //
                    skip_validation = false;
                }
            }
        });

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
        iv_add_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String site_val = ss_site.getmValue().get(SearchableSpinner.ID);
                if (site_val != null && !site_val.equals("null")) {
                    showTrackingDialog();
                } else {
                    showAlertDialog(
                            hmAux_Trans.get("alert_no_site_selected_ttl"),
                            hmAux_Trans.get("alert_no_site_selected_msg")
                    );
                }
            }
        });
        //
        iv_serial_add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSerialAddInfoDialog();
            }
        });
        //
        //
        tvCtListner = new TextViewCT.ITextViewCT() {
            @Override
            public void removeViews(TextViewCT textViewCT) {
                int idx = ll_tracking_content.indexOfChild(textViewCT);
                //
                tracking_list.remove(idx);
                //
                ll_tracking_content.removeView(textViewCT);
                //
                setTrackingListChanged(true);
            }
        };
        //Listner que zera trackingList.
        //Usado no NÃO da troca de site e no SIM do "limpar" site

        dialogClearTrackingListner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                ll_tracking_content.removeAllViews();
                //
                serialObj.setTracking_list(new ArrayList<MD_Product_Serial_Tracking>());
                //
                tracking_list = serialObj.getTracking_list();
                //
                mPresenter.updateTrackingReference(tracking_list);
            }
        };


        getSerialInfo();
    }

    private void showSerialAddInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.namoa_dialog_serial_more_info,null);
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
        /*
        *Add valores
        */
        tv_dialog_ttl.setText(hmAux_Trans.get("dialog_serial_info_ttl"));
        //
        tv_category_lbl.setText(hmAux_Trans.get("dialog_serial_category_price_lbl"));
        tv_category_val.setText(dialog_serial_category_price_desc);
        //
        tv_segment_lbl.setText(hmAux_Trans.get("dialog_serial_segment_lbl"));
        tv_segment_val.setText(dialog_serial_segment_desc);
        //
        tv_add_info1_lbl.setText(hmAux_Trans.get("dialog_serial_add_info1_lbl"));
        tv_add_info1_val.setText(serialObj.getAdd_inf1());
        //
        tv_add_info2_lbl.setText(hmAux_Trans.get("dialog_serial_add_info2_lbl"));
        tv_add_info2_val.setText(serialObj.getAdd_inf2());
        //
        tv_add_info3_lbl.setText(hmAux_Trans.get("dialog_serial_add_info3_lbl"));
        tv_add_info3_val.setText(serialObj.getAdd_inf3());
        //
        builder.setView(view);

        AlertDialog dialog = builder.show();

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
        //tv_product_desc_value.setText(md_product.getProduct_desc());
        tv_product_desc_value.setText( md_product.getProduct_id() +" - " +md_product.getProduct_desc());

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
    public void setWsProcess(String ws_process) {
        Act027_Main mAct = (Act027_Main) getActivity();
        mAct.setWs_process(ws_process);
    }

    @Override
    public void setSerialValuesV2(HMAux md_product_serial, MD_Product_Serial serialObjDb) {
        mket_serial_id.setmBARCODE(false);
        mket_serial_id.setmOCR(false);
        mket_serial_id.setmNFC(false);

        mket_serial_id.setEnabled(false);
        //
        if (serialObjDb != null) {
            serialObj = serialObjDb;
        }
        //Seta Tracking na lista e atualiza referencia no presenter.
        tracking_list = serialObj.getTracking_list();
        mPresenter.updateTrackingReference(tracking_list);
        //
        ll_serial_full_desc.setVisibility(View.VISIBLE);
        //
        setSSmValue(ss_site, String.valueOf(serialObj.getSite_code()), md_product_serial.get(MD_SiteDao.SITE_DESC), true, true);
        //
        setSSmValue(ss_site_zone, String.valueOf(serialObj.getZone_code()), md_product_serial.get(MD_Site_ZoneDao.ZONE_DESC), true, true);
        //
        setSSmValue(ss_site_zone_local, String.valueOf(serialObj.getLocal_code()), md_product_serial.get(MD_Site_Zone_LocalDao.LOCAL_ID), true, true);
        //
        //
        setSSmValue(ss_brand, String.valueOf(serialObj.getBrand_code()), md_product_serial.get(MD_BrandDao.BRAND_DESC), true, false);
        brand_model_color_lbl =  md_product_serial.get(MD_BrandDao.BRAND_DESC).length() == 0 ? "" : "| " + md_product_serial.get(MD_BrandDao.BRAND_DESC) + " ";
        //
        setSSmValue(ss_brand_model, String.valueOf(serialObj.getModel_code()), md_product_serial.get(MD_Brand_ModelDao.MODEL_DESC), true, false);
        brand_model_color_lbl += md_product_serial.get(MD_Brand_ModelDao.MODEL_DESC).length() == 0 ? "" :  "| " +  md_product_serial.get(MD_Brand_ModelDao.MODEL_DESC)+ " ";
        //
        setSSmValue(ss_brand_color, String.valueOf(serialObj.getColor_code()), md_product_serial.get(MD_Brand_ColorDao.COLOR_DESC), true, false);
        brand_model_color_lbl += md_product_serial.get(MD_Brand_ColorDao.COLOR_DESC).length() == 0 ? "" :"| " +   md_product_serial.get(MD_Brand_ColorDao.COLOR_DESC)+ " ";
        //
        if( brand_model_color_lbl.length() > 0){
            brand_model_color_lbl = brand_model_color_lbl.substring(1,brand_model_color_lbl.length());
            tv_brand_model_color.setText(brand_model_color_lbl);
            tv_brand_model_color.setVisibility(View.VISIBLE);
        }else{
            tv_brand_model_color.setText("");
            tv_brand_model_color.setVisibility(View.GONE);
        }
        //
        setSSmValue(ss_segment, String.valueOf(serialObj.getSegment_code()), md_product_serial.get(MD_SegmentDao.SEGMENT_DESC), true, false);
        dialog_serial_segment_desc = md_product_serial.get(MD_SegmentDao.SEGMENT_ID) + " - " + md_product_serial.get(MD_SegmentDao.SEGMENT_DESC);
        //
        setSSmValue(ss_category_price, String.valueOf(serialObj.getCategory_price_code()), md_product_serial.get(MD_Category_PriceDao.CATEGORY_PRICE_DESC), true, false);
        dialog_serial_category_price_desc = md_product_serial.get(MD_Category_PriceDao.CATEGORY_PRICE_ID) + " - " + md_product_serial.get(MD_Category_PriceDao.CATEGORY_PRICE_DESC);
        //
        //
        setSSmValue(ss_site_owner, String.valueOf(serialObj.getSite_code_owner()), md_product_serial.get(SITE_DESC_OWNER), true, false);
        //
        et_info1.setText(serialObj.getAdd_inf1());
        et_info1.setTag(serialObj.getAdd_inf1());
        //
        et_info2.setText(serialObj.getAdd_inf2());
        et_info2.setTag(serialObj.getAdd_inf2());
        //
        et_info3.setText(serialObj.getAdd_inf3());
        et_info3.setTag(serialObj.getAdd_inf3());
        //
        ll_tracking_content.removeAllViews();
        //Insere lista de tracking vindo do banco.
        for (int i = 0; i < serialObj.getTracking_list().size(); i++) {
            appendTracking(serialObj.getTracking_list().get(i).getTracking());
        }
        //Chama metodo que carrea todas as listas do SS
        spinnersInitializer();
        //
        if(!mMain.hasExecutionProfile()){
            disabledEdition();
        }
    }

    private void disabledEdition() {

        for (Object obj: serialProperties) {
            if (obj instanceof SearchableSpinner) {
                SearchableSpinner ss = (SearchableSpinner) obj;
                ss.setmEnabled(false);
            }
            if (obj instanceof MKEditTextNM) {
                MKEditTextNM mket = (MKEditTextNM) obj;
                mket.setEnabled(false);
            }
        }
        //
        for (int i = 0; i < ll_tracking_content.getChildCount() ; i++) {
            TextViewCT viewCT = (TextViewCT) ll_tracking_content.getChildAt(i);
            viewCT.setmEnabled(false);
        }
        iv_add_tracking.setEnabled(false);
        btn_action.setVisibility(View.GONE);
    }

    @Override
    public void appendTracking(String tracking) {
        TextViewCT viewCT = new TextViewCT(context);
        viewCT.setmValue(tracking);
        viewCT.setOnRemoveViewsListener(tvCtListner);
        //
        ll_tracking_content.addView(viewCT);
    }

    @Override
    public void scrollToTracking() {
        int x = (int) ll_tracking_content.getX();
        int y = ll_tracking_content.getTop() + ((View) ll_tracking_content.getParent()).getTop();

        sv_serial.smoothScrollTo(x, y);
    }

    @Override
    public void setTrackingListChanged(boolean trackingListChanged) {
        this.trackingListChanged = trackingListChanged;
    }

    @Override
    public String getSearched_tracking() {
        return searched_tracking;
    }

    @Override
    public void cleanSearched_tracking() {
        //
        searched_tracking = "";
    }

    private void showTrackingDialog() {
        //final Act027_Main mAct = (Act027_Main) getActivity();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.namoa_dialog_add_tracking, null);

        final MKEditTextNM mket_tracking = (MKEditTextNM) view.findViewById(R.id.namoa_dialog_add_tracking_mket_tracking);
        //mket_tracking.setHint(hmAux_Trans.get("tracking_hint_lbl"));
        controls_sta.add(mket_tracking);
        //mAct.addControlToList(mket_tracking);
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
                //mAct.removeControlToList(mket_tracking);
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

                    if (!mPresenter.isTrackingListed(mket_text)) {
                        if (ToolBox_Con.isOnline(context)) {
                            ToolBox_Inf.closeKeyboard(context, mket_tracking.getWindowToken());
                            //
                            searched_tracking = mket_text;
                            //
                            mPresenter.executeTrackingSearch(
                                    serialObj.getProduct_code(),
                                    serialObj.getSerial_code(),
                                    mket_text,
                                    ss_site.getmValue().get(SearchableSpinner.ID)
                            );
                            //
                            show.dismiss();
                        } else {
                            ToolBox_Inf.showNoConnectionDialog(context);
                        }
                    } else {
                        showAlertDialog(
                                hmAux_Trans.get("alert_tracking_already_listed_ttl"),
                                hmAux_Trans.get("alert_tracking_already_listed_msg")
                        );
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
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
     * Faz loop no arraylist de itens verificando se
     * houve alteração de valor.
     * V2 - Utiliza metodo do proprio Spinner para validar se conteudo mudou
     * e analisa tb tracking list.
     *
     * @return
     */
    private boolean checkSerialChangesV2() {

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
            setSSmValue(ss_site_owner, null, null, false, false);
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
            setSSmValue(ss_category_price, null, null, false, false);
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
            setSSmValue(ss_segment, null, null, false, false);
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
            setSSmValue(ss_brand_color, null, null, false, false);
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
            setSSmValue(ss_brand_model, null, null, false, false);
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
            setSSmValue(ss_brand, null, null, false, false);
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
            setSSmValue(ss_site, null, null, false, true);
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
            setSSmValue(ss_site_zone, null, null, false, true);
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
            setSSmValue(ss_site_zone_local, null, null, false, true);
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
                mSm_so.getSerial_code());
    }

    public void callProcessSerialSaveResult(String product_code, int serial_code, HMAux hmSaveResult) {
        mPresenter.processSerialSaveResult(product_code, serial_code, hmSaveResult);
    }

    public void callProcessTrackingResult(HMAux auxResult){
        mPresenter.processTrackingResult(auxResult, serialObj);
    }

}

