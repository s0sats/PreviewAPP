package com.namoadigital.prj001.ui.act072;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act020_Prod_Serial_Adapter;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.ui.act068.Act068_Main;
import com.namoadigital.prj001.ui.act073.Act073_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act072_Main extends Base_Activity implements Act072_Main_Contract.I_View {

    private Act072_Main_Contract.I_Presenter mPresenter;
    private TextView tv_records;
    private LinearLayout ll_records;
    private TextView tv_records_limit;
    private TextView tv_records_count;
    private ListView lv_prod_serial_list;
    private TextView tv_no_result;
    private Act020_Prod_Serial_Adapter mAdapter;
    private ArrayList<MD_Product_Serial> serialList = new ArrayList<>();

    private boolean mJump;
    private long record_count;
    private long record_page;

    private String fragProduct_ID;
    private String fragSerial_ID;
    private String fragTracking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act072_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();

        initAction();

    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            Constant.ACT072
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act072_title");
        transList.add("no_record_found_lbl");
        transList.add("showing_lbl");
        transList.add("records_lbl");
        transList.add("records_found_lbl");
        transList.add("records_display_limit_lbl");
        transList.add("alert_qty_records_exceeded_ttl");
        transList.add("alert_qty_records_exceeded_msg");
        transList.add("alert_qty_records_founded");
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
        mPresenter = new Act072_Main_Presenter(
            context,
            this,
            hmAux_Trans
        );
        //
        bindViews();
        setSerialList();
        setSerialListSize();
        //
        hideSoftKeyboard();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null && bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {

            mJump = bundle.getBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP);
            serialList = (ArrayList<MD_Product_Serial>) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            record_count = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT);
            record_page = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE);

            fragProduct_ID = bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, "");
            fragSerial_ID = bundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
            fragTracking = bundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");
        }else {
            serialList = new ArrayList<>();
            record_count = 0;
            record_page = 0;
            fragProduct_ID = "";
            fragSerial_ID = "";
            fragTracking = "";
        }
    }

    private void bindViews() {
        tv_records = findViewById(R.id.act072_tv_record_info);
        ll_records = findViewById(R.id.act072_ll_limit_exceeded);
        tv_records_limit = findViewById(R.id.act072_tv_record_limit);
        tv_records_count = findViewById(R.id.act072_tv_record_count);
        lv_prod_serial_list = findViewById(R.id.act072_lv_prod_serial);
        tv_no_result = findViewById(R.id.act072_tv_no_result);
    }

    private void setSerialList() {
        if(serialList.isEmpty()) {
            tv_no_result.setText(hmAux_Trans.get("no_record_found_lbl"));
            tv_no_result.setVisibility(View.VISIBLE);
            tv_records.setVisibility(View.GONE);
            ll_records.setVisibility(View.INVISIBLE);
        }else{
            tv_no_result.setVisibility(View.GONE);
        }
        //
        mAdapter = new Act020_Prod_Serial_Adapter(
            context,
            R.layout.act020_cell,
            serialList
        );
        mAdapter.setSite_id_preference(ToolBox_Con.getPreference_Site_Code(context));
        lv_prod_serial_list.setAdapter(mAdapter);
    }

    private void setSerialListSize() {
        tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + serialList.size() + " " + hmAux_Trans.get("records_lbl"));
        tv_records_limit.setText(hmAux_Trans.get("records_display_limit_lbl") + " " + record_page);
        tv_records_count.setText(hmAux_Trans.get("records_found_lbl") + " " + record_count);
        //
        if (serialList.size() == 1 && mJump) {
            lv_prod_serial_list.performItemClick(
                lv_prod_serial_list.getAdapter().getView(0, null, null),
                0,
                lv_prod_serial_list.getAdapter().getItemId(0)
            );
        }else {
            //
            if (record_count > record_page) {
                ll_records.setVisibility(View.VISIBLE);
                showAlert(
                    hmAux_Trans.get("alert_qty_records_exceeded_ttl"),
                    hmAux_Trans.get("alert_qty_records_exceeded_msg") + "\n" +
                        record_count + " " + hmAux_Trans.get("alert_qty_records_founded")
                );
            } else {
                ll_records.setVisibility(View.GONE);
            }
        }
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT072;
        mAct_Title = Constant.ACT072 + "_" + "title";
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

    private void initAction() {
        lv_prod_serial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MD_Product_Serial productSerial = (MD_Product_Serial) parent.getItemAtPosition(position);
                //
                mPresenter.defineFlow(productSerial);
            }
        });
    }


    @Override
    public void showAlert(final String ttl, final String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
    }

    @Override
    public void callAct068() {
        Intent intent = new Intent(context, Act068_Main.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, fragProduct_ID);
        bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, fragSerial_ID);
        bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, fragTracking);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct073(Bundle bundle) {
        Intent intent = new Intent(context, Act073_Main.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
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
