package com.namoadigital.prj001.ui.act026;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.SO_Header_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.design.list.IOnRememberListView;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act023.Act023_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.ui.act050.Act050_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by neomatrix on 03/07/17.
 */

public class Act026_Main extends Base_Activity_Frag implements Act026_Main_View {

    private Act026_Main_Presenter mPresenter;
    private ListView lv_so;
    private SO_Header_Adapter mAdapter;
    private String requesting_act;
    private String product_code;
    private String serial_id;
    //
    private TextView tv_empty_state;
    private MKEditTextNM mket_filter;
    private TextInputLayout filter_layout;
    private MaterialButton btnNewOs;
    private ConstraintLayout bottom_bar;
    private View lv_so_footer;
    private ImageView zoneFilter;
    private MD_Product_Serial mdProductSerial;
    private int listSize;
    private boolean applyZoneFilter = false;
    private TextView tv_color;
    private TextView tv_brand;
    private TextView tv_model;
    private TextView tv_serial;
    private TextView tv_product;
    private TextView tracking_desc;
    private ImageView iconClass;

    private ConstraintLayout layout_serial_header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act026_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT026
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act026_title");
        transList.add("btn_new");
        transList.add("btn_new_os");
        transList.add("btn_download");
        transList.add("alert_download_mult_so_ttl");
        transList.add("alert_download_mult_so_msg");
        transList.add("alert_download_so_ttl");
        transList.add("alert_download_so_msg");
        transList.add("alert_segment_price_category_so_ttl");
        transList.add("alert_segment_price_category_so_msg");
        transList.add("empty_list_state_so_msg");
        transList.add("alert_no_so_selected");
        transList.add("progress_downloading_so_ttl");
        transList.add("progress_downloading_so_msg");
        transList.add("alert_no_so_founded_ttl");
        transList.add("alert_no_so_founded_msg");
        transList.add("only_avaliable_filter_lbl");
        transList.add("filter_hint");
        transList.add("alert_leave_so_creation_ttl");
        transList.add("alert_leave_so_creation_confirm");
        transList.add("empty_list_state_so_not_listed_msg");
        transList.add("apply_zone_on_lbl");
        transList.add("apply_zone_off_lbl");
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
        //
        recoverIntentsInfo();
        //
        mPresenter =
                new Act026_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans,
                        requesting_act,
                        new SM_SODao(
                                context,
                                ToolBox_Con.customDBPath(
                                        ToolBox_Con.getPreference_Customer_Code(context)
                                ),
                                Constant.DB_VERSION_CUSTOM
                        )
                );
        //
        lv_so = (ListView) findViewById(R.id.act026_lv_so);


        //
        setTvEmptyState();
        //views.add(tv_filter_lbl);
        //
        bottom_bar = findViewById(R.id.bottom_bar_layout);
        //
        mket_filter = (MKEditTextNM) findViewById(R.id.so_mket_filter);
        filter_layout = findViewById(R.id.actTextLayout);
        filter_layout.setHint(hmAux_Trans.get("filter_hint"));
        //
        zoneFilter = findViewById(R.id.main_zone_selection);
        zoneFilter.setVisibility(View.VISIBLE);
        //
        tv_brand = findViewById(R.id.act026_tv_brand_val);
        tv_model = findViewById(R.id.act026_tv_model_val);
        tv_color = findViewById(R.id.act026_tv_color_val);
        tv_serial = findViewById(R.id.serial_title);
        tv_product = findViewById(R.id.product_description);
        iconClass = findViewById(R.id.iconClassColor);
        layout_serial_header = findViewById(R.id.layout_top_header_bar);
        tracking_desc = findViewById(R.id.tracking_desc);

        if (mdProductSerial != null) {
            iconClass.setVisibility(mdProductSerial.getClass_color() == null || mdProductSerial.getClass_color().isEmpty() ? View.GONE : View.VISIBLE);
            tv_serial.setVisibility(mdProductSerial.getSerial_id() == null || mdProductSerial.getSerial_id().isEmpty() ? View.GONE : View.VISIBLE);
            tv_product.setVisibility(mdProductSerial.getProduct_desc() == null || mdProductSerial.getProduct_desc().isEmpty() ? View.GONE : View.VISIBLE);
            tv_brand.setVisibility(mdProductSerial.getBrand_desc() == null || mdProductSerial.getBrand_desc().isEmpty() ? View.GONE : View.VISIBLE);
            tv_model.setVisibility(mdProductSerial.getModel_desc() == null || mdProductSerial.getModel_desc().isEmpty() ? View.GONE : View.VISIBLE);
            tv_color.setVisibility(mdProductSerial.getColor_desc() == null || mdProductSerial.getColor_desc().isEmpty() ? View.GONE : View.VISIBLE);
            tracking_desc.setVisibility(mdProductSerial.getTracking_list() == null || mdProductSerial.getTracking_list().isEmpty() ? View.GONE : View.VISIBLE);

            if (mdProductSerial.getClass_color() != null && !mdProductSerial.getClass_color().isEmpty()) {
                iconClass.setColorFilter(Color.parseColor(mdProductSerial.getClass_color()));
            }
            tv_serial.setText(mdProductSerial.getSerial_id() == null || mdProductSerial.getSerial_id().isEmpty() ? "" : mdProductSerial.getSerial_id());
            tv_product.setText(mdProductSerial.getProduct_desc() == null || mdProductSerial.getProduct_desc().isEmpty() ? "" : mdProductSerial.getProduct_desc());
            tv_brand.setText(mdProductSerial.getBrand_desc() == null || mdProductSerial.getBrand_desc().isEmpty() ? "" : mdProductSerial.getBrand_desc());
            tv_model.setText(mdProductSerial.getModel_desc() == null || mdProductSerial.getModel_desc().isEmpty() ? "" : "| " + mdProductSerial.getModel_desc());
            tv_color.setText(mdProductSerial.getColor_desc() == null || mdProductSerial.getColor_desc().isEmpty() ? "" : "| " + mdProductSerial.getColor_desc());
            if (mdProductSerial.getTracking_list().size() == 1) {
                tracking_desc.setText(mdProductSerial.getTracking_list().get(0).getTracking());
            } else {
                List<String> trackings = new ArrayList<>();
                for (int i = 0; i < mdProductSerial.getTracking_list().size(); i++) {
                    trackings.add(mdProductSerial.getTracking_list().get(i).getTracking());
                }
                tracking_desc.setText(String.join(" | ", trackings));
            }
        }
        layout_serial_header.setVisibility(tv_serial.getVisibility());

        if (hasNewOsFlow()) {
            bottom_bar.setVisibility(View.VISIBLE);
            setBtnNewOs();
        }
        //
        mPresenter.getSOListTotalCount(product_code, serial_id);
        //mPresenter.getSOList(product_code, serial_id, sw_filter.isChecked());
        mPresenter.getSOList(product_code, serial_id, false);
        //
        ToolBox_Inf.cleanUpApproval(
                context,
                new SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );

    }

    private void setBtnNewOs() {
        btnNewOs = findViewById(R.id.btn_create_action);
        btnNewOs.setText(hmAux_Trans.get("btn_new_os"));
    }

    private void setTvEmptyState() {
        tv_empty_state = (TextView) findViewById(R.id.act026_tv_empty_state);
        tv_empty_state.setText(hmAux_Trans.get("empty_list_state_so_msg"));
    }

    private boolean hasNewOsFlow() {
        return ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_NEW)
        && mdProductSerial != null;
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_REQUESTING_ACT)) {
                requesting_act = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005);
                product_code = bundle.getString(MD_ProductDao.PRODUCT_CODE, null);
                serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, null);
                mdProductSerial = (MD_Product_Serial) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            } else {
                //Tratar quando lista de s.o não for enviado.
                //Caixa de alerta e volta para menu?!?
                ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
            }
        } else {
            //Tratar caso não exista bundle
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT026;
        mAct_Title = Constant.ACT026 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
        //
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context, true);
    }

    private void initActions() {

        lv_so.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux so = (HMAux) parent.getItemAtPosition(position);
                //
                mPresenter.defineForwardFlow(so);

            }
        });
        //
        mket_filter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                applySearchFilter();
            }
        });
        //
        if (hasNewOsFlow()) {

            btnNewOs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean hasSegment = true;
                    boolean hasCategory = true;

                    if(mdProductSerial.getSegment_code() == null){
                        hasSegment = false;
                    }
                    if(mdProductSerial.getCategory_price_code() == null){
                        hasCategory = false;
                    }

                    if(hasSegment && hasCategory){
                        Bundle bundle = new Bundle();
                        bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, mdProductSerial.getProduct_code());
                        bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, mdProductSerial.getSerial_code());
                        callAct050(context,bundle);
                    }else{
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_segment_price_category_so_ttl"),
                                hmAux_Trans.get("alert_segment_price_category_so_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                /*
                                    17/09/2019 BARRIONUEVO
                                    Mesmo quando o usuario seleciona para ocultar os dados do serial
                                    ele será direcionado para o fragmento do serial para edição
                                    caso o serial nao possua segmento ou categoria.
                                 */
//                                        mPresenter.onBackPressedClicked();
                                        callAct023(context);
                                    }
                                },
                                0
                        );
                    }
                }
            });
        }


        zoneFilter.setOnClickListener(view -> {
            applyZoneFilter = !applyZoneFilter;
            setIvMainUserSelection();
            setAvailableFilter(applyZoneFilter);
            applySearchFilter();
            swToastMessage();
        });
    }


    private void swToastMessage() {
        String message = applyZoneFilter ? hmAux_Trans.get("apply_zone_on_lbl") : hmAux_Trans.get("apply_zone_off_lbl");
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void setIvMainUserSelection() {
        if (applyZoneFilter) {
            zoneFilter.setImageDrawable(null);
            zoneFilter.setBackground(context.getDrawable(R.drawable.my_action_toogle_pressed));
            Drawable drawable = DrawableCompat.wrap(
                    ContextCompat.getDrawable(context, R.drawable.ic_location_on_24));
            DrawableCompat.setTint(
                    drawable.mutate(), ContextCompat.getColor(context, R.color.padrao_WHITE)
            );

            zoneFilter.setImageDrawable(drawable);
            zoneFilter.postInvalidate();
        } else {
            zoneFilter.setImageDrawable(null);
            zoneFilter.setBackground(context.getDrawable(R.drawable.my_action_toogle_default));
            Drawable drawable = DrawableCompat.wrap(
                    ContextCompat.getDrawable(context, R.drawable.outline_wrong_location_24));
            DrawableCompat.setTint(
                    drawable.mutate(), ContextCompat.getColor(context, R.color.m3_namoa_secondary)
            );

            zoneFilter.setImageDrawable(drawable);
            zoneFilter.postInvalidate();
        }
    }


    private void setAvailableFilter(boolean isChecked) {
        if (mAdapter != null) {
            mAdapter.setShowOnlyAvailable(isChecked);
            applySearchFilter();
        }
    }

    private void applySearchFilter() {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(mket_filter.getText().toString().trim());
        }
    }

    @Override
    public void loadSOList(List<HMAux> soList) {
        String configType = product_code == null || serial_id == null ? SO_Header_Adapter.CONFIG_TYPE_EXIBITION_FULL : SO_Header_Adapter.CONFIG_TYPE_EXIBITION_SO;
        //
        setTitleLanguage(" (" + soList.size() + " / " + soList.size() + ")");
        //
        mAdapter = new SO_Header_Adapter(
                context,
                soList,
                configType,
                R.layout.so_item_list,
                R.layout.so_item_list,
                mket_filter != null ? mket_filter.getText().toString().trim() : null,
                list -> {
                    setTitleLanguage(" (" + list.size() + " / " + soList.size() + ")");
                    new IOnRememberListView<HMAux>(
                            lv_so, tv_empty_state
                    ).dataChanged(list);
                },
                mdProductSerial == null
        );
        //
        if(soList.isEmpty()) {
            if(this.listSize >0){
                tv_empty_state.setText(hmAux_Trans.get("empty_list_state_so_not_listed_msg"));

                tv_empty_state.setTextColor(getResources().getColor(R.color.customff_background_error));
            }
            tv_empty_state.setVisibility(View.VISIBLE);
        }else{
            tv_empty_state.setVisibility(View.GONE);
        }
        lv_so.setAdapter(mAdapter);
        //LUCHE - 01/11/2019
        setAvailableFilter(applyZoneFilter);
    }

    @Override
    public void setListSOSize(int listSize) {
        this.listSize = listSize;
    }

    @Override
    public void callAct012(Context context) {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();

    }

    @Override
    public void callAct023(Context context) {
        Intent mIntent = new Intent(context, Act023_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, product_code );
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, serial_id);
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL);
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL,mdProductSerial);
        bundle.putBoolean(ConstantBaseApp.PREFERENCE_HIDE_SERIAL_INFO, false);

        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct027(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        //Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    public void callAct050(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act050_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }
    /*
        BARRIONUEVO 17-04-2020
            - Atualiza info do footer
            - Recarrega lista e aplica filtro
    */
    @Override
    protected void processRefreshMessage(String mType, String mValue, String mActivity) {
        super.processRefreshMessage(mType, mValue, mActivity);
        mPresenter.getSOListTotalCount(product_code, serial_id);
        //mPresenter.getSOList(product_code, serial_id, sw_filter.isChecked());
        mPresenter.getSOList(product_code, serial_id, false);
        setAvailableFilter(applyZoneFilter);
        iniUIFooter();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
