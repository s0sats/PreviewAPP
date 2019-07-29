package com.namoadigital.prj001.ui.act063;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act020_Prod_Serial_Adapter;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.ui.act053.Act053_Main;
import com.namoadigital.prj001.ui.act062.Act062_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act063_Main extends Base_Activity implements Act063_Main_Contract.I_View {

    private Act063_Main_Presenter mPresenter;
    private TextView tv_records;
    private LinearLayout ll_records;
    private TextView tv_records_limit;
    private TextView tv_records_count;
    private ListView lv_prod_serial_list;
    private TextView tv_no_result;
    private Act020_Prod_Serial_Adapter mAdapter;
    private ArrayList<MD_Product_Serial> serial_list = new ArrayList<>();
    private String tracking_searched = "";
    private MD_Product md_product;
    private boolean mJump;
    private long record_count;
    private long record_page;
    private String fragProduct_ID;
    private String fragSerial_ID;
    private String fragTracking;
    private Button btn_create_serial;
    private String requestingAct;
    private String requestingActProcess;
    private String requestingActPrefix;
    private String requestingActCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act063_main);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT063
        );
        //
        loadTranslation();

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act063_title");
        transList.add("search_prod_hint");
        transList.add("search_serial_hint");
        transList.add("progress_nfc_ttl");
        transList.add("progress_nfc_msg");
        transList.add("showing_lbl");
        transList.add("records_lbl");

        transList.add("no_record_found_lbl");
        transList.add("alert_nfc_return");
        transList.add("alert_qty_records_exceeded_ttl");
        transList.add("alert_qty_records_exceeded_msg");
        transList.add("alert_qty_records_founded");
        transList.add("records_display_limit_lbl");
        transList.add("records_found_lbl");
        transList.add("btn_create_serial");
        transList.add("alert_serial_in_another_site_ttl");
        transList.add("alert_serial_in_another_site_msg");
        transList.add("alert_serial_without_inbound_ttl");
        transList.add("alert_serial_without_inbound_msg");
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
        mPresenter = new Act063_Main_Presenter(
                context,
                this,
                hmAux_Trans,
                requestingActProcess
        );
        //
        md_product = mPresenter.getProductInfo(fragProduct_ID);
        //
        bindViews();
        //
        setBtnCreationVisibility();
        //
        //
        mPresenter.prepareDataToScreen(record_count, record_page, serial_list, mJump);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {
                //
                mJump = bundle.getBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP);
                serial_list = (ArrayList<MD_Product_Serial>) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
                record_count = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT);
                record_page = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE);
                fragProduct_ID = bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, "");
                fragSerial_ID = bundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
                fragTracking = bundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");
                //

            }
            //Var Io
            requestingAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT051);
            requestingActProcess = bundle.getString(ConstantBaseApp.HMAUX_PROCESS_KEY, "");
            requestingActPrefix = bundle.getString(ConstantBaseApp.HMAUX_PREFIX_KEY, "-1");
            requestingActCode = bundle.getString(ConstantBaseApp.HMAUX_CODE_KEY, "-1");
        } else {
            requestingAct = ConstantBaseApp.ACT051;
            requestingActProcess = "";
            requestingActPrefix = "-1";
            requestingActCode = "-1";
        }
    }

    private void bindViews() {
        //
        tv_records = (TextView) findViewById(R.id.act063_tv_record_info);
        ll_records = (LinearLayout) findViewById(R.id.act063_ll_limit_exceeded);
        tv_records_limit = (TextView) findViewById(R.id.act063_tv_record_limit);
        tv_records_count = (TextView) findViewById(R.id.act063_tv_record_count);
        lv_prod_serial_list = (ListView) findViewById(R.id.act063_lv_prod_serial);
        tv_no_result = (TextView) findViewById(R.id.act063_tv_no_result);
        tv_no_result.setText(hmAux_Trans.get("no_record_found_lbl"));
        btn_create_serial = findViewById(R.id.act063_btn_create_serial);
        btn_create_serial.setText(hmAux_Trans.get("btn_create_serial") + " (" + fragSerial_ID + ")");
    }

    private void setBtnCreationVisibility() {
        if (md_product != null) {
            //
            if (md_product.getAllow_new_serial_cl() == 1
                    && ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, Constant.PROFILE_PRJ001_PRODUCT_SERIAL_PARAM_EDIT)
                    && !fragSerial_ID.equals("")
                    && (requestingActProcess.equals(ConstantBaseApp.IO_INBOUND))
            ) {
                btn_create_serial.setVisibility(View.VISIBLE);
            } else {
                btn_create_serial.setVisibility(View.GONE);
            }
        } else {
            btn_create_serial.setVisibility(View.GONE);
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT063;
        mAct_Title = Constant.ACT063 + ConstantBaseApp.title_lbl;
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

    @Override
    public void setRecordInfo(int seriaListSize, long record_page) {
        if (seriaListSize > 0) {
            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + seriaListSize + " " + hmAux_Trans.get("records_lbl"));
        } else {
            tv_records.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
            ll_records.setVisibility(View.GONE);
        }
        // presenter é o responsavel por exibir a msg.
//        if (record_count > record_page) {
//            showQtyExceededMsg(record_count, record_page);
//        }
    }

    @Override
    public void loadProductSerialList(ArrayList<MD_Product_Serial> serial_list) {
        //Esconde tv com msg de nenhum busca feita
        //e ll com informações de limite de excedido.
        tv_no_result.setVisibility(View.GONE);
        ll_records.setVisibility(View.GONE);
        //
        //setRecordInfo(record_count, record_page);
        setRecordInfo(serial_list.size(), record_page);
        //
        mAdapter = new Act020_Prod_Serial_Adapter(
                context,
                R.layout.act020_cell,
                serial_list
        );
        //
        mAdapter.setSite_id_preference(ToolBox_Con.getPreference_Site_Code(context));
        //
        lv_prod_serial_list.setAdapter(mAdapter);
    }

    @Override
    public void showQtyExceededMsg(long record_count, long record_page) {
        ll_records.setVisibility(View.VISIBLE);

        tv_records_limit.setText(
                hmAux_Trans.get("records_display_limit_lbl") + " " + record_page
        );

        tv_records_count.setText(
                hmAux_Trans.get("records_found_lbl") + " " + record_count
        );

        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_qty_records_exceeded_ttl"),
                hmAux_Trans.get("alert_qty_records_exceeded_msg") + "\n" + record_count + " " + hmAux_Trans.get("alert_qty_records_founded"),
                null,
                0);
    }

    private void initActions() {
        lv_prod_serial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MD_Product_Serial productSerial = (MD_Product_Serial) parent.getItemAtPosition(position);

                mPresenter.processItemClick(productSerial);

            }
        });
        //
        btn_create_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.defineFlow(md_product.createNewSerialForThisProduct(fragSerial_ID), true);
            }
        });
    }

    @Override
    public void showMsg(String title, String msg) {
        ToolBox.alertMSG(
                context,
                title,
                msg,
                null,
                0
        );
    }

    public void setBtnCreateVisibility(boolean visible) {
        btn_create_serial.setVisibility(visible ? View.VISIBLE : View.GONE);
        btn_create_serial.setEnabled(visible);
    }

    @Override
    public void callAct053(Bundle bundle) {
        Intent mIntent = new Intent(context, Act053_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle == null) {
            bundle = new Bundle();
        }
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, requestingAct);
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, requestingActProcess);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, requestingActPrefix);
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, requestingActCode);

        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct062() {
        Intent mIntent = new Intent(context, Act062_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        //Infos busca do serial
        bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, fragProduct_ID);
        bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, fragSerial_ID);
        bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, fragTracking);
        //Infos IO
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, requestingActProcess);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, requestingActPrefix);
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, requestingActCode);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, requestingAct);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }
}
