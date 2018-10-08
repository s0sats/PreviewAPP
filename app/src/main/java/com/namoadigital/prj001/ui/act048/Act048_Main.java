package com.namoadigital.prj001.ui.act048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act020_Prod_Serial_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.ui.act040.Act040_Main;
import com.namoadigital.prj001.ui.act049.Act049_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act048_Main extends Base_Activity_Frag implements Act048_Main_Contract.I_View {

    private Bundle bundle;
    private String wsProcess;
    private Act048_Main_Contract.I_Presenter mPresenter;
    private TextView tv_records;
    private LinearLayout ll_records;
    private TextView tv_records_limit;
    private TextView tv_records_count;
    private ListView lv_prod_serial_list;
    private TextView tv_no_result;
    private Act020_Prod_Serial_Adapter mAdapter;
    private String ws_process;
    private ArrayList<MD_Product_Serial> serial_list = new ArrayList<>();
    private Button btn_no_serial;
    private Button btn_create_serial;
    private long product_code;
    private MD_Product md_product;
    private boolean mJump;
    private long record_count;
    private long record_page;
    private String serial_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act048_main);
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

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT048
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act048_title");
        transList.add("alert_product_not_found_ttl");
        transList.add("alert_product_not_found_msg");
        transList.add("records_display_limit_lbl");
        transList.add("records_found_lbl");
        transList.add("alert_qty_records_exceeded_ttl");
        transList.add("alert_qty_records_exceeded_msg");
        transList.add("alert_qty_records_founded");
        transList.add("showing_lbl");
        transList.add("records_lbl");
        transList.add("no_record_found_lbl");
        transList.add("btn_create_serial");
        transList.add("btn_no_serial");
        transList.add("alert_product_not_found_ttl");
        transList.add("alert_product_not_found_msg");

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
        mPresenter = new Act048_Main_Presenter(
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
        mPresenter.getProductInfo(product_code);
        //
        tv_no_result = (TextView) findViewById(R.id.act048_tv_no_result);
        ll_records = (LinearLayout) findViewById(R.id.act048_ll_limit_exceeded);
        tv_records = (TextView) findViewById(R.id.act048_tv_record_info);
        tv_records_count = (TextView) findViewById(R.id.act048_tv_record_count);
        tv_records_limit = (TextView) findViewById(R.id.act048_tv_record_limit);
        btn_create_serial = (Button) findViewById(R.id.act048_btn_create_serial);
        btn_no_serial = (Button) findViewById(R.id.act048_btn_no_serial);
        lv_prod_serial_list = (ListView) findViewById(R.id.act048_lv_prod_serial);
        //
        tv_no_result.setText(hmAux_Trans.get("no_record_found_lbl"));
        btn_create_serial.setText(hmAux_Trans.get("btn_create_serial") + " (" + serial_id + ")");
        btn_no_serial.setText(hmAux_Trans.get("btn_no_serial"));
        //
        configButtons();
        //
        mPresenter.checkSerialListJump(serial_list);
    }

    private void configButtons() {
        if (md_product != null) {
            //Nessa tela não é possivel seguir sem serial
            btn_no_serial.setVisibility(View.GONE);
            /*if (md_product.getRequire_serial() == 1) {
                btn_no_serial.setVisibility(View.GONE);
            } else {
                btn_no_serial.setVisibility(View.VISIBLE);
            }*/
            //Se produto permite novo serial e usuario tem permissão de edição e existe serial digita
            //exibe btn de criação de serial e , nesse caso, tb desabilita tb de sem serial.
            if ( md_product.getAllow_new_serial_cl() == 1
                 && ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, Constant.PROFILE_PRJ001_PRODUCT_SERIAL_PARAM_EDIT)
                 && !serial_id.equals("")
            ) {
                btn_no_serial.setVisibility(View.GONE);
                btn_create_serial.setVisibility(View.VISIBLE);
            } else {
                btn_create_serial.setVisibility(View.GONE);
            }
        } else {
            btn_no_serial.setVisibility(View.GONE);
            btn_create_serial.setVisibility(View.GONE);
        }
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {
                product_code = bundle.getLong(MD_ProductDao.PRODUCT_CODE);
                mJump = bundle.getBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP);
                serial_list = (ArrayList<MD_Product_Serial>) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
                record_count = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT);
                record_page = bundle.getLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE);
                serial_id = bundle.getString(Constant.MAIN_MD_PRODUCT_SERIAL_ID);
            }
        }
    }

    @Override
    public void setProductObj(MD_Product mdProduct) {
        this.md_product = mdProduct;
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

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT048;
        mAct_Title = Constant.ACT048 + "_" + "title";
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
    public void loadSerialList(ArrayList<MD_Product_Serial> serialList) {
        //Esconde tv com msg de nenhum busca feita
        //e ll com informações de limite de excedido.
        tv_no_result.setVisibility(View.GONE);
        ll_records.setVisibility(View.GONE);
        //
        setRecordInfo();
        //
        mAdapter = new Act020_Prod_Serial_Adapter(
                context,
                R.layout.act020_cell,
                serialList
        );
        //
        mAdapter.setSite_id_preference(ToolBox_Con.getPreference_Site_Code(context));
        //
        lv_prod_serial_list.setAdapter(mAdapter);
        //Verifica se salto para edição deve ser feito.
        if (serialList.size() == 1 && mJump) {
            lv_prod_serial_list.performItemClick(
                    lv_prod_serial_list.getAdapter().getView(0, null, null),
                    0,
                    lv_prod_serial_list.getAdapter().getItemId(0)
            );
        }
    }

    @Override
    public void setRecordInfo() {
        if (serial_list.size() > 0) {
            tv_records.setText(hmAux_Trans.get("showing_lbl") + " " + serial_list.size() + " " + hmAux_Trans.get("records_lbl"));
        } else {
            tv_records.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
            ll_records.setVisibility(View.VISIBLE);
        }
        //
        if (record_count > record_page) {
            showQtyExceededMsg(record_count, record_page);
        }
    }

    private void showQtyExceededMsg(long record_count, long record_page) {
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
        btn_create_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.prepareEditionParams(
                        md_product.createNewSerialForThisProduct(serial_id),
                        true
                );
            }
        });
        //
        lv_prod_serial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MD_Product_Serial productSerial = (MD_Product_Serial) parent.getItemAtPosition(position);

                mPresenter.prepareEditionParams(productSerial, false);
            }
        });

    }

    @Override
    public void callAct040(Context context) {
        Intent mIntent = new Intent(context, Act040_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct049(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act049_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }


    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
    }
    //TRATA MSG SESSION NOT FOUND
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
    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }
    //Metodo chamado ao finalizar o download da atualização.
    @Override
    protected void processCloseAPP(String mLink, String mRequired) {
        super.processCloseAPP(mLink, mRequired);
        //
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
        bundle.putString(Constant.WS_LOGOUT_USER_CODE, String.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        ToolBox_Con.cleanPreferences(context);

        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }


}
