package com.namoadigital.prj001.ui.act026;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.SO_Header_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act023.Act023_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.ui.act050.Act050_Main;
import com.namoadigital.prj001.util.Constant;
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
    private TextView tv_filter_lbl;
    private TextView tv_empty_state;
    private Switch sw_filter;
    private MKEditTextNM mket_filter;
    private Button btnNewOs;
    private View lv_so_footer;
    private MD_Product_Serial mdProductSerial;
    private int listSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act026_main);

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

        if (hasNewOsFlow()) {
            lv_so_footer = View.inflate(this, R.layout.act026_list_os_footer, null);
            lv_so.addFooterView(lv_so_footer, null, false);
            setBtnNewOs();
        }


        //
        tv_filter_lbl = (TextView) findViewById(R.id.act026_tv_filter_lbl);
        tv_filter_lbl.setText(hmAux_Trans.get("only_avaliable_filter_lbl"));
        setTvEmptyState();
        //views.add(tv_filter_lbl);
        //
        mket_filter = (MKEditTextNM) findViewById(R.id.act026_mket_filter);
        mket_filter.setHint(hmAux_Trans.get("filter_hint"));
        //
        sw_filter = (Switch) findViewById(R.id.act026_sw_filter);
        //
        mPresenter.getSOListTotalCount(product_code, serial_id);
        mPresenter.getSOList(product_code, serial_id, sw_filter.isChecked());
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
        btnNewOs = (Button) findViewById(R.id.act026_os_list_btn_new_os);
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
                requesting_act = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT023);
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
        ToolBox_Inf.buildFooterDialog(context);
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
                                        mPresenter.onBackPressedClicked();
                                    }
                                },
                                0
                        );
                    }
                }
            });
        }
        sw_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.getSOList(product_code, serial_id, isChecked);
            }
        });
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
        mAdapter = new SO_Header_Adapter(
                context,
                soList,
                configType,
                R.layout.so_header_cell,
                R.layout.so_header_cell
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



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();

    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
