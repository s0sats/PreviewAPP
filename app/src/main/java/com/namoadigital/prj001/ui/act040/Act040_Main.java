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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act041.Act041_Main;
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

    private MD_Product mdProduct;

    private MKEditTextNM mket_produto;
    private ImageView iv_search_produto;

    private MKEditTextNM mket_serial;
    private ImageView iv_search_serial;

    private MKEditTextNM mket_barcode;
    private ImageView iv_search_barcode;

    private boolean connectionStatusAlter;

    private TextView tv_status;
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
        mket_produto = (MKEditTextNM) findViewById(R.id.act040_mket_product);
        iv_search_produto = (ImageView) findViewById(R.id.act040_iv_search_product);
        //
        mket_serial = (MKEditTextNM) findViewById(R.id.act040_mket_serial);
        iv_search_serial = (ImageView) findViewById(R.id.act040_iv_search_serial);
        //
        mket_barcode = (MKEditTextNM) findViewById(R.id.act040_mket_barcode);
        iv_search_barcode = (ImageView) findViewById(R.id.act040_iv_search_barcode);
        //
        btn_create_so = (Button) findViewById(R.id.act040_btn_create_so);
        //
        tv_status = (TextView) findViewById(R.id.act040_tv_status);
        //
        ss_partner = (SearchableSpinner) findViewById(R.id.act040_ss_partner);
        ss_partner.setmShowLabel(false);
        ss_partner.setmHint("Partner - Trad");
        ss_partner.setmTitle("Title - Trad");
        //
        //Add controles no array list.
        controls_sta.add(mket_produto);
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
    public void loadSO_Pack_Express(SO_Pack_Express so_pack_express) {
        mSo_pack_express = so_pack_express;
        //
        if (mSo_pack_express != null) {
            tv_status.setText(mSo_pack_express.getPack_desc());
            //
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mket_barcode.getWindowToken(), 0);
        } else {
            tv_status.setText("Nao Encontrado - Trad");
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
    public void loadMD_Product(MD_Product md_product) {
        this.md_product = md_product;
        //
        mPresenter.setSO_Pack_Express(
                ToolBox_Con.getPreference_Customer_Code(context),
                Long.parseLong(ToolBox_Con.getPreference_Site_Code(context)),
                ToolBox_Con.getPreference_Operation_Code(context),
                md_product.getProduct_code(),
                mket_barcode.getText().toString().trim()
        );
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

        iv_search_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAct041(context);
            }
        });

        mket_barcode.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (b) {
                    mPresenter.setSO_Pack_Express(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            Long.parseLong(ToolBox_Con.getPreference_Site_Code(context)),
                            ToolBox_Con.getPreference_Operation_Code(context),
                            md_product != null ? md_product.getProduct_code() : -1,
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
                mPresenter.setMD_Partner(
                        Long.parseLong(hmAux.get("customer_code")),
                        Long.parseLong(hmAux.get("partner_id"))
                );
            }
        });

        btn_create_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSo_pack_express != null && md_partner != null && md_product != null && mket_serial.getText().toString().trim().length() != 0) {

                    ToolBox.alertMSG(
                            context,
                            "Criacao de S.O.",
                            "Deseja Realmente Criar a S.O.?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.onCreateSo_Pack_Express(
                                            mSo_pack_express,
                                            md_partner,
                                            md_product,
                                            mket_serial.getText().toString().trim(),
                                            connectionStatusAlter
                                    );
                                }
                            },
                            1,
                            false
                    );
                } else {
                    ToolBox.alertMSG(
                            context,
                            "Erros",
                            "Campos Obrigatórios",
                            //hmAux_Trans.get("alert_no_pdf_tll"),
                            //hmAux_Trans.get("alert_no_pdf_msg"),
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
    public void jumpToOne() {
        iv_search_produto.setVisibility(View.GONE);
        callAct041(context);
    }

    @Override
    public void callAct041(Context context) {
        Intent mIntent = new Intent(context, Act041_Main.class);
        //mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            bundle = new Bundle();
        }

        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_PACK_EXPRESS);
        mIntent.putExtras(bundle);

        startActivityForResult(mIntent, PROCESSO_PRODUCT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PROCESSO_PRODUCT_CODE:
                processProduct_Code(resultCode, data);
                break;
            default:
                break;
        }
    }

    private void processProduct_Code(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MD_ProductDao mdProductDao = new MD_ProductDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            mdProduct = mdProductDao.getByString(
                    new MD_Product_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            Long.parseLong(data.getStringExtra("product_code"))
                    ).toSqlQuery()
            );
            //
            if (mdProduct != null) {
                mket_produto.setText(mdProduct.getProduct_desc());
                //
                loadMD_Product(mdProduct);
            } else {
                mket_produto.setText("");
            }

        } else {
        }
    }

    @Override
    public void automationCleanForm() {
        mket_serial.setText("");
        mket_barcode.setText("");
        //
        mket_serial.requestFocus();
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
                    "Orderm Express - Trad",
                    "Sem Orderm Express - Trad"
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
                "Orderm Express - Trad",
                "Orderm Express aguardando na fila para envio - Trad"
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
            hmAux.put("so_express_code", sKey);
            hmAux.put("so_express_result", so_express.get(sKey));

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


        lv_results.setAdapter(
                new SimpleAdapter(
                        context,
                        so_express,
                        R.layout.act038_results_adapter_cell,
                        from,
                        to
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

    private void showMsg(String ttl, String msg) {
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
