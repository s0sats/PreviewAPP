package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.ui.act007.Act007_Main;
import com.namoadigital.prj001.ui.act009.Act009_Main;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act008_Main extends Base_Activity implements Act008_Main_View {

    public static final String WS_PROCESS_SYNC = "ws_process_sync";
    public static final String WS_PROCESS_SERIAL = "ws_process_serial";

    private Context context;
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
    private Button btn_create;

    private Act008_Main_Presenter mPresenter;
    private MD_Product product;

    private Bundle bundle;
    private long product_code;
    private int serial_required;
    private int serial_allow_new;
    //agendamento
    private boolean isSchedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act008_main);

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
        context = getBaseContext();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT008
        );

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("alert_no_connection_title");
        transList.add("alert_no_connection_msg");
        transList.add("alert_offine_mode_title");
        transList.add("alert_offine_mode_msg");
        transList.add("alert_start_sync_title");
        transList.add("alert_start_sync_msg");
        transList.add("alert_start_serial_title");
        transList.add("alert_start_serial_msg");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("alert_no_serial_typed_title");
        transList.add("alert_no_serial_typed_msg");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        transList.add("product_ttl");
        transList.add("mket_search_hint");
        transList.add("product_label");
        transList.add("product_id_label");
        transList.add("alert_no_form_for_operation_ttl");
        transList.add("alert_no_form_for_operation_msg");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        //Variavel q identifica se dados do produto são chamados do master data ou não.
        isSchedule = false;
        //
        recoverIntentsInfo();
        //
        mPresenter =  new Act008_Main_Presenter_Impl(
                context,
                this,
                new Sync_ChecklistDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                        ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                product_code,
                hmAux_Trans,
                new GE_Custom_Form_OperationDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM),
                isSchedule
                );
        //
        mket_serial_id = (MKEditTextNM) findViewById(R.id.act008_mket_serial);
        mket_serial_id.setmNFC(true);
        controls_sta.add(mket_serial_id);
        mket_serial_id.setHint(hmAux_Trans.get("mket_search_hint"));
        //
        tv_product_ttl = (TextView) findViewById(R.id.act008_tv_product_ttl);
        tv_product_ttl.setTag("product_ttl");
        //
        tv_product_code_label = (TextView) findViewById(R.id.act008_tv_product_code_lbl);
        //tv_product_code_label.setTag("product_label");
        //
        tv_product_id_label = (TextView) findViewById(R.id.act008_tv_product_id_lbl);
        //tv_product_id_label.setTag("product_id_label");
        //
      //  tv_product_code_value = (TextView) findViewById(R.id.act008_tv_product_code_val);
      //  tv_product_id_value = (TextView) findViewById(R.id.act008_tv_product_id_val);
        //
       // tv_product_desc_label = (TextView) findViewById(R.id.act008_tv_product_desc_ttl);
        //tv_product_desc_label.setTag("product_desc_label");
        tv_product_desc_value = (TextView) findViewById(R.id.act008_tv_product_desc_value);
        //
        tv_required_lbl = (TextView) findViewById(R.id.act008_tv_require_serial_lbl);
        tv_required_lbl.setTag("chk_required");

        tv_required_val = (TextView) findViewById(R.id.act008_tv_require_serial_val);

        tv_allow_new_lbl = (TextView) findViewById(R.id.act008_tv_allow_lbl);
        tv_allow_new_lbl.setTag("chk_allow_new");

        tv_allow_new_val = (TextView) findViewById(R.id.act008_tv_allow_val);

        btn_create = (Button) findViewById(R.id.act008_btn_create);
        btn_create.setTag("btn_create");

        //Adiciona Views na lista de tradução

        views.add(tv_product_ttl);
        //views.add(tv_product_code_label);
        //views.add(tv_product_id_label);
        //views.add(tv_product_desc_label);
        views.add(tv_required_lbl);
        views.add(tv_allow_new_lbl);
        views.add(btn_create);

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            //Chamada vinda da act017
            if(bundle.containsKey(Act016_Main.ACT016_SELECTED_DATE)){
                isSchedule = true;
            }
            product_code = Long.parseLong(bundle.getString(Constant.ACT007_PRODUCT_CODE));
        } else {
            product_code = 0L;
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT008;
        mAct_Title = Constant.ACT008 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value =  hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl =  hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value =  hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé - fim
    }

    private void initActions() {
        //
        mPresenter.getProductInfo(bundle);
        //
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                mPresenter.validateSerial(
                        mket_serial_id.getText().toString(),
                        serial_required,
                        serial_allow_new
                );
            }
        });

    }

    @Override
    public void setProductValues(MD_Product md_product) {
        product = md_product;
        //
        tv_product_code_label.setText(
                hmAux_Trans.get("product_label")+" "+
                String.valueOf(md_product.getProduct_code())

        );
        tv_product_id_label.setText(
                hmAux_Trans.get("product_id_label")+" "+
                md_product.getProduct_id());
        tv_product_desc_value.setText(md_product.getProduct_desc());
        //
        serial_required = md_product.getRequire_serial();
        serial_allow_new = md_product.getAllow_new_serial_cl();
        //
        tv_required_val.setText("("+hmAux_Trans.get("NO").toUpperCase()+")");
        if( md_product.getRequire_serial() == 1){
            tv_required_val.setText("("+hmAux_Trans.get("YES").toUpperCase()+")");
        }
        //
        tv_allow_new_val.setText("("+hmAux_Trans.get("NO").toUpperCase()+")");
        if( md_product.getAllow_new_serial_cl() == 1){
            tv_allow_new_val.setText("("+hmAux_Trans.get("YES").toUpperCase()+")");
        }

    }

    @Override
    public void callAct007(Context context) {
        Intent mIntent =  new Intent(context, Act007_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Remove produto do bundle
        bundle.remove(Constant.ACT007_PRODUCT_CODE);

        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();

    }

    @Override
    public void fieldFocus() {
        mket_serial_id.requestFocus();
    }

    @Override
    public void showAlertDialog(String title, String msg) {

        ToolBox.alertMSG(
                Act008_Main.this,
                title,
                msg,
                null,
                0
        );
    }

    @Override
    public void continueOffline() {
        String title = "";
        String msg = "";
        DialogInterface.OnClickListener listener = null;

        if (serial_required == 1){
            title = hmAux_Trans.get("alert_no_connection_title"); //"Connection";
            msg = hmAux_Trans.get("alert_no_connection_msg"); // "No connection has been found!\nThis product requires connection to proceed.\nTry again later.";
        }else{

            if(serial_allow_new == 0 &&
               mket_serial_id.getText().toString().trim().length() > 0
            ){
                title = hmAux_Trans.get("alert_no_connection_title"); //"Connection";
                msg = hmAux_Trans.get("alert_no_connection_msg"); // "No connection has been found!\nThis product requires connection to proceed.\nTry again later.";
            }else{
                title = hmAux_Trans.get("alert_offine_mode_title"); //"Continue in offline mode?";
                msg = hmAux_Trans.get("alert_offine_mode_msg"); //"No connection has been found!\nDo you want continue without check the Serial id ?! ";

                listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //callAct009(context);
                        mPresenter.defineFlow();
                    }
                };
            }
        }

        ToolBox.alertMSG(
                Act008_Main.this,
                title,
                msg,
                listener,
                1
        );
    }

    @Override
    public void showPD(String wsProcess) {

        String alertTitle = "";
        String alertMsg = "";

        switch (wsProcess){
            case WS_PROCESS_SYNC:
                alertTitle = hmAux_Trans.get("alert_start_sync_title");
                alertMsg = hmAux_Trans.get("alert_start_sync_msg");
                break;
            case WS_PROCESS_SERIAL:
                alertTitle = hmAux_Trans.get("alert_start_serial_title");
                alertMsg = hmAux_Trans.get("alert_start_serial_msg");
                break;
            default:
                break;
        }

        if(alertTitle.length() != 0){
           enableProgressDialog(
                   alertTitle,
                   alertMsg,
                   hmAux_Trans.get("sys_alert_btn_cancel"),
                   hmAux_Trans.get("sys_alert_btn_ok")
                   );

        }
    }
    //Trata retorno de serial não existente
    @Override
    protected void processSerialNExist() {
        super.processSerialNExist();

        disableProgressDialog();

        //callAct009(context);
        mPresenter.defineFlow();

    }
    //Trata retorno de serial OK
    @Override
    protected void processSerialOk() {
        super.processSerialOk();

        disableProgressDialog();

        //callAct009(context);
        mPresenter.defineFlow();
    }

    @Override
    public void callAct009(Context context) {

        if(mPresenter.checkFormXOperationExists()){

            Intent mIntent =  new Intent(context, Act009_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bundle.putString(Constant.ACT008_SERIAL_ID,mket_serial_id.getText().toString().trim());
            bundle.putString(Constant.ACT008_PRODUCT_DESC,product.getProduct_desc().trim());
            bundle.putString(Constant.ACT008_PRODUCT_ID,product.getProduct_id().trim());

            mIntent.putExtras(bundle);

            startActivity(mIntent);
            finish();
        }else{
            showAlertDialog(
                    hmAux_Trans.get("alert_no_form_for_operation_ttl"),
                    hmAux_Trans.get("alert_no_form_for_operation_msg")
            );

        }
    }

    @Override
    public void callAct011(Context context) {
        Intent mIntent =  new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct017(Context context) {
        Intent mIntent =  new Intent(context, Act017_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Remove dados não necessarios para act017
        bundle.remove(Constant.ACT007_PRODUCT_CODE);
        bundle.remove(Constant.ACT008_PRODUCT_DESC);
        bundle.remove(Constant.ACT008_SERIAL_ID);
        bundle.remove(Constant.ACT009_CUSTOM_FORM_TYPE);
        bundle.remove(Constant.ACT009_CUSTOM_FORM_TYPE_DESC);
        bundle.remove(Constant.ACT010_CUSTOM_FORM_CODE);
        bundle.remove(Constant.ACT010_CUSTOM_FORM_VERSION);
        bundle.remove(Constant.ACT010_CUSTOM_FORM_CODE_DESC);
        bundle.remove(Constant.ACT013_CUSTOM_FORM_DATA);

        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();

    }

    //Trata retorno do Serial
    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);

        disableProgressDialog();
        //Atualiza data na tabela de produtos loca
        mPresenter.updateSyncChecklist();
        mPresenter.proceedToSerialProcess(mket_serial_id.getText().toString().trim() , serial_required);

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
