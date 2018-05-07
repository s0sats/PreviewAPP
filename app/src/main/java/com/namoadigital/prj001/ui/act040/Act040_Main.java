package com.namoadigital.prj001.ui.act040;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.sql.MD_Operation_Sql_003;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 09/03/2018.
 */

public class Act040_Main extends Base_Activity implements Act040_Main_View {

    private static final int PROCESSO_PRODUCT_CODE = 100;

    private Bundle bundle;
    private Act040_Main_Presenter_Impl mPresenter;
    private SO_Pack_Express mSo_pack_express;
    private MD_Partner md_partner;
    private MD_Product md_product;
    private MD_Site md_site;
    private MD_Operation md_operation;

    //private MKEditTextNM mket_produto;
    //private ImageView iv_search_produto;

    private MKEditTextNM mket_serial;
    private ImageView iv_search_serial;

    private MKEditTextNM mket_barcode;
    private ImageView iv_search_barcode;

    private boolean connectionStatusAlter;

    private TextView tv_status;
    private TextView tv_prod_desc;
    private SearchableSpinner ss_partner;

    private Button btn_create_so;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act040_main);

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
                Constant.ACT040
        );

        loadTranslation();
    }

    private void loadTranslation() {

        List<String> transList = new ArrayList<String>();
        transList.add("act040_title");
        transList.add("ss_partner_hint");
        transList.add("ss_partner_ttl");
        transList.add("status_no_pack_msg");
        transList.add("status_no_partner_msg");
        transList.add("status_no_product_msg");
        transList.add("status_no_serial_msg");
        transList.add("alert_express_error_tll");
        transList.add("alert_express_no_tll");
        transList.add("alert_express_no_msg");
        transList.add("alert_express_general_error_ttl");
        transList.add("alert_express_general_error_msg");
        transList.add("progress_sync_express_ttl");
        transList.add("progress_sync_express_msg");
        transList.add("toast_express_saved_msg");
        transList.add("tv_product_hint");
        transList.add("tv_serial_hint");
        transList.add("tv_barcode_hint");
        transList.add("btn_create_so");
        transList.add("express_desc");
        transList.add("express_status");
        transList.add("express_label");
        transList.add("alert_create_so_express_ttl");
        transList.add("alert_create_so_express_msg");
        transList.add("express_send_error_ttl");
        transList.add("express_send_error_msg");
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
        mPresenter = new Act040_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new SO_Pack_ExpressDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new SO_Pack_Express_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_PartnerDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
        //
        MD_SiteDao mdSiteDao = new MD_SiteDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        MD_OperationDao mdOperationDao = new MD_OperationDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        md_site = mdSiteDao.getByString(
                new MD_Site_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context)
                ).toSqlQuery()
        );

        md_operation = mdOperationDao.getByString(
                new MD_Operation_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Operation_Code(context)
                ).toSqlQuery()
        );
        //
        mket_barcode = (MKEditTextNM) findViewById(R.id.act040_mket_barcode);
        iv_search_barcode = (ImageView) findViewById(R.id.act040_iv_search_barcode);
        mket_barcode.setHint(hmAux_Trans.get("tv_barcode_hint"));
        mket_barcode.requestFocus();
        //
        mket_serial = (MKEditTextNM) findViewById(R.id.act040_mket_serial);
        iv_search_serial = (ImageView) findViewById(R.id.act040_iv_search_serial);
        mket_serial.setHint(hmAux_Trans.get("tv_serial_hint"));
        //
        btn_create_so = (Button) findViewById(R.id.act040_btn_create_so);
        btn_create_so.setText(hmAux_Trans.get("btn_create_so"));
        //
        tv_status = (TextView) findViewById(R.id.act040_tv_status);
        tv_prod_desc = (TextView) findViewById(R.id.act040_tv_prod_desc);
        //
        ss_partner = (SearchableSpinner) findViewById(R.id.act040_ss_partner);
        ss_partner.setmShowLabel(false);
        ss_partner.setmHint(hmAux_Trans.get("ss_partner_hint"));
        ss_partner.setmTitle(hmAux_Trans.get("ss_partner_ttl"));
        //
        //Add controles no array list.
        controls_sta.add(mket_serial);
        controls_sta.add(mket_barcode);
        //
        mPresenter.checkJump(ToolBox_Con.getPreference_Customer_Code(context));
        mPresenter.setPartners();
        //
        connectionStatusAlter = false;
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
        } else {
        }
    }

    @Override
    public void loadSO_Pack_Express(SO_Pack_Express so_pack_express, String express_code) {
        mSo_pack_express = so_pack_express;
        //
        if (mSo_pack_express != null) {
            tv_status.setText(mSo_pack_express.getPack_desc());
            //
            MD_ProductDao mdProductDao = new MD_ProductDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            md_product = mdProductDao.getByString(
                    new MD_Product_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            mSo_pack_express.getProduct_code()
                    ).toSqlQuery()
            );
            //
            if (md_product == null) {
                tv_prod_desc.setText("");
                mket_serial.setmInputTypeValidator(null);
                mket_serial.setmRequired(true);
                mket_serial.setmMinSize(null);
                mket_serial.setmMaxSize(null);
            } else {
                tv_prod_desc.setText(md_product.getProduct_desc());
                mket_serial.setmInputTypeValidator(md_product.getSerial_rule());
                mket_serial.setmRequired(true);
                mket_serial.setmMinSize(md_product.getSerial_min_length());
                mket_serial.setmMaxSize(md_product.getSerial_max_length());
            }
            //
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mket_barcode.getWindowToken(), 0);
        } else {
            tv_prod_desc.setText("");

            if (express_code.isEmpty()) {
                tv_status.setText("");
            } else {
                tv_status.setText(hmAux_Trans.get("status_no_pack_msg"));
            }
        }
    }


    @Override
    public void loadMD_Partner(MD_Partner md_partner) {
        this.md_partner = md_partner;
        //
        HMAux md_partner_hm = new HMAux();
        md_partner_hm.put("description", md_partner.getPartner_desc());
        md_partner_hm.put("partner_id", md_partner.getPartner_id());
        md_partner_hm.put("id", String.valueOf(md_partner.getPartner_code()));
        md_partner_hm.put("customer_code", String.valueOf(md_partner.getCustomer_code()));
        //
        this.ss_partner.setmValue(md_partner_hm);
    }

    @Override
    public void setPartnerList(ArrayList<HMAux> partnerList) {
        ss_partner.setmOption(partnerList);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT040;
        mAct_Title = Constant.ACT040 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    @Override
    protected void footerCreateDialog() {
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {
        mket_barcode.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (b) {
                    mPresenter.setSO_Pack_Express(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            s
                    );
                } else {
                    tv_status.setText("");
                }
            }
        });

        ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (hmAux.size() > 0) {
                    mPresenter.setMD_Partner(
                            Long.parseLong(hmAux.get("customer_code")),
                            Long.parseLong(hmAux.get(SearchableSpinner.ID))
                    );
                }
            }
        });

        btn_create_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSo_pack_express != null && md_partner != null && md_product != null && mket_serial.isValid() && !mSo_pack_express.getExpress_code().equalsIgnoreCase(mket_serial.getText().toString())) {

                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_create_so_express_ttl"),
                            hmAux_Trans.get("alert_create_so_express_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.onCreateSo_Pack_Express(
                                            mSo_pack_express,
                                            md_partner,
                                            md_product,
                                            mket_serial.getText().toString().trim(),
                                            md_site,
                                            md_operation,
                                            connectionStatusAlter
                                    );
                                }
                            },
                            1,
                            false
                    );
                } else {

                    String result = "";

                    if (mSo_pack_express == null) {
                        result = hmAux_Trans.get("status_no_pack_msg");
                    } else if (md_partner == null) {
                        result = hmAux_Trans.get("status_no_partner_msg");
                    } else if (md_product == null) {
                        result = hmAux_Trans.get("status_no_product_msg");
                    } else if (mSo_pack_express.getExpress_code().equalsIgnoreCase(mket_serial.getText().toString())) {
                        result = hmAux_Trans.get("pack_equals_serial_msg");
                    } else if (!mket_serial.isValid()) {
                        //result = hmAux_Trans.get("status_no_serial_msg");
                        result = mket_serial.getmErrorMSG();
                    } else {
                        result = "";
                    }

                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_so_pack_error_tll"),
                            result,
                            null,
                            -1,
                            false
                    );
                }

            }
        });
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void automationCleanForm() {
        mket_barcode.setText("");
        mket_serial.setText("");
        mket_serial.setmInputTypeValidator("");
        //
        mSo_pack_express = null;
        md_product = null;
        //
        mket_barcode.requestFocus();
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        progressDialog.dismiss();

    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired);
        progressDialog.dismiss();
        //
        if (hmAux.keySet().size() == 0) {
            automationCleanForm();
            //
            showMsg(
                    hmAux_Trans.get("alert_express_no_tll"),
                    hmAux_Trans.get("alert_express_no_msg")
            );
        } else {
            showResults(hmAux);
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
        //
        automationCleanForm();
        //
        showMsg(
                hmAux_Trans.get("alert_express_general_error_ttl"),
                hmAux_Trans.get("alert_express_general_error_msg")
        );
    }


    //Tratativa SESSION NOT FOUND
    @Override
    protected void processLogin() {
        super.processLogin();
        //
        ToolBox_Con.cleanPreferences(context);
        //
        ToolBox_Inf.call_Act001_Main(context);
        //
        finish();

    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        progressDialog.dismiss();
    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    private void showResults(HMAux so_express) {
        ArrayList<HMAux> mSO_Express = new ArrayList<>();

        for (String sKey : so_express.keySet()) {
            HMAux hmAux = new HMAux();
            //
            String[] sParts = sKey.split(Constant.MAIN_CONCAT_STRING);

            String sFinal = sParts[0] + "\n" + sParts[1] + "\n" + sParts[2];

            //hmAux.put("so_express_code", sFinal);
            //hmAux.put("so_express_result", so_express.get(sKey));

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, hmAux_Trans.get("express_label"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, sParts[0] + "   -   " + sParts[2]);

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_2, hmAux_Trans.get("express_desc"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_2, sParts[1]);

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_3, hmAux_Trans.get("express_status"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_3, so_express.get(sKey));

            mSO_Express.add(hmAux);
        }

        showResultsDialog(mSO_Express);
    }

    public void showResultsDialog(List<HMAux> so_express) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        String[] from = {"so_express_code", "so_express_result"};
        int[] to = {R.id.act038_results_adapter_cell_tv_ttl, R.id.act038_results_adapter_cell_tv_msg_value};


//        lv_results.setAdapter(
//                new SimpleAdapter(
//                        context,
//                        so_express,
//                        R.layout.act038_results_adapter_cell,
//                        from,
//                        to
//                )
//        );


        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        so_express,
                        Generic_Results_Adapter.CONFIG_3_ITENS_NEW,
                        hmAux_Trans
                )
        );

        //builder.setTitle(hmAux_Trans.get("alert_results_ttl"));
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                automationCleanForm();
                //
                show.dismiss();
            }
        });
    }

    @Override
    public void showMsg(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
    }

    @Override
    public void showMsgToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setConnectionStatusAlter(boolean connectionStatusAlter) {
        this.connectionStatusAlter = connectionStatusAlter;
    }
}
