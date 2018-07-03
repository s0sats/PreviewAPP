package com.namoadigital.prj001.ui.act045;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act020_Prod_Serial_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.ui.act030.Act030_Main;
import com.namoadigital.prj001.ui.act031.Act031_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act045_Main extends Base_Activity_NFC_Geral implements Act045_Main_Contract.I_View {

    private Act045_Main_Contract.I_Presenter mPresenter;

    private TextView tv_records;
    private LinearLayout ll_records;
    private TextView tv_records_limit;
    private TextView tv_records_count;
    private ListView lv_prod_serial_list;
    private TextView tv_no_result;
    private Act020_Prod_Serial_Adapter mAdapter;

    private ArrayList<MD_Product_Serial> serial_list = new ArrayList<>();

    private Button btn_no_serial;
    private Button btn_create_serial;

    private MD_Product md_product;
    private boolean mJump;
    private long record_count;
    private long record_page;
    private String serial_id;

    private String fragProduct_ID;
    private String fragSerial_ID;
    private String fragTracking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act045_main);

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
                Constant.ACT045
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act045_title");
        transList.add("records_display_limit_lbl");
        transList.add("records_found_lbl");
        transList.add("alert_qty_records_exceeded_ttl");
        transList.add("alert_qty_records_exceeded_msg");
        transList.add("alert_qty_records_founded");
        transList.add("no_search_realized");
        transList.add("showing_lbl");
        transList.add("records_lbl");
        transList.add("btn_create_serial");
        transList.add("no_search_realized");
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
        mPresenter = new Act045_Main_Presenter(
                context,
                this,
                hmAux_Trans,
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
        //
        btn_no_serial = (Button) findViewById(R.id.act045_btn_no_serial);
        //
        btn_create_serial = (Button) findViewById(R.id.act045_btn_create_serial);
        btn_create_serial.setText(hmAux_Trans.get("btn_create_serial") + " (" + serial_id + ")");
        //
        tv_records = (TextView) findViewById(R.id.act045_tv_record_info);
        //
        ll_records = (LinearLayout) findViewById(R.id.act045_ll_limit_exceeded);
        //
        tv_records_limit = (TextView) findViewById(R.id.act045_tv_record_limit);
        //
        tv_records_count = (TextView) findViewById(R.id.act045_tv_record_count);
        //
        lv_prod_serial_list = (ListView) findViewById(R.id.act045_lv_prod_serial);
        //
        tv_no_result = (TextView) findViewById(R.id.act045_tv_no_result);
        tv_no_result.setText(hmAux_Trans.get("no_search_realized"));
        //
        btn_no_serial.setVisibility(View.GONE);

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, Constant.PROFILE_PRJ001_PRODUCT_SERIAL_PARAM_EDIT)
                && md_product != null) {
            btn_create_serial.setVisibility(View.VISIBLE);
        } else {
            btn_create_serial.setVisibility(View.GONE);
        }

        if (serial_id.equals("")) {
            btn_create_serial.setVisibility(View.GONE);
        } else {
            btn_no_serial.setVisibility(View.GONE);
        }

        if (btn_create_serial.getVisibility() == View.VISIBLE) {
            btn_no_serial.setVisibility(View.GONE);
        }

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT045;
        mAct_Title = Constant.ACT045 + "_" + "title";
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

    private void initActions() {

        btn_create_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.defineFlow(
                        md_product.createNewSerialForThisProduct(serial_id),
                        true
                );
            }
        });

        lv_prod_serial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MD_Product_Serial productSerial = (MD_Product_Serial) parent.getItemAtPosition(position);

                mPresenter.defineFlow(productSerial, false);
            }
        });
        //
        if (serial_list != null && serial_list.size() != 0) {
            //fragFilters.setSerialIdText(serial_list.get(0).getSerial_id());
            //
            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + serial_list.size() + " " + hmAux_Trans.get("records_lbl"));
            //
            loadProductSerialList(serial_list);
            //
            if (serial_list.size() == 1 && mJump) {
                lv_prod_serial_list.performItemClick(
                        lv_prod_serial_list.getAdapter().getView(0, null, null),
                        0,
                        lv_prod_serial_list.getAdapter().getItemId(0)
                );
            } else {
            }
        } else {
        }

        //
        setRecordInfo(record_count, record_page);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {

                mJump = bundle.getBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP);
                serial_list = (ArrayList<MD_Product_Serial>) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
                record_count = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT);
                record_page = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE);
                serial_id = bundle.getString(Constant.MAIN_MD_PRODUCT_SERIAL_ID);

                fragProduct_ID = bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, "");
                fragSerial_ID = bundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
                fragTracking = bundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");

                MD_ProductDao mdProductDao = new MD_ProductDao(context);

                String product_id = bundle.getString(MD_ProductDao.PRODUCT_ID);

                md_product = mdProductDao.getByString(
                        new MD_Product_Sql_003(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                "",
                                product_id
                        ).toSqlQuery()
                );
            }
        }
    }

    @Override
    public void loadProductSerialList(ArrayList<MD_Product_Serial> prod_serial_list) {
        //Esconde tv com msg de nenhum busca feita
        //e ll com informações de limite de excedido.
        tv_no_result.setVisibility(View.GONE);
        ll_records.setVisibility(View.GONE);
        //
        mAdapter = new Act020_Prod_Serial_Adapter(
                context,
                R.layout.act020_cell,
                prod_serial_list
        );
        //
        mAdapter.setSite_id_preference(ToolBox_Con.getPreference_Site_Code(context));
        //
        lv_prod_serial_list.setAdapter(mAdapter);
    }

    @Override
    public void setRecordInfo(long record_size, long record_page) {
        if (record_size > 0) {
            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + record_size + " " + hmAux_Trans.get("records_lbl"));
        } else {
            tv_records.setText(hmAux_Trans.get("no_record_found_lbl"));
        }

        if (record_count > record_page) {
            showQtyExceededMsg(record_count, record_page);
        }
    }

    @Override
    public void showQtyExceededMsg(long record_page, long record_count) {
        //
        ll_records.setVisibility(View.VISIBLE);

        tv_records_limit.setText(
                hmAux_Trans.get("records_display_limit_lbl") + " " + record_page
        );

        tv_records_count.setText(
                hmAux_Trans.get("records_found_lbl") + " " + record_count
        );
        //
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_qty_records_exceeded_ttl"),
                hmAux_Trans.get("alert_qty_records_exceeded_msg") + "\n" + record_count + " " + hmAux_Trans.get("alert_qty_records_founded"),
                null,
                0);

    }

    @Override
    public void callAct030(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, fragProduct_ID);
        bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, fragSerial_ID);
        bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, fragTracking);

        Intent mIntent = new Intent(context, Act030_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct031(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act031_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
