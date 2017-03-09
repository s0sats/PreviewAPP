package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.ui.act007.Act007_Main;
import com.namoadigital.prj001.ui.act009.Act009_Main;
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
    private TextView tv_product_code_label;
    private TextView tv_product_code_value;
    private TextView tv_product_desc_label;
    private TextView tv_product_desc_value;
    private MKEditTextNM mket_serial_id;
    private CheckBox chk_required;
    private CheckBox chk_allow_new;
    private Button btn_create;

    private Act008_Main_Presenter mPresenter;

    private Bundle bundle;
    private long product_code;
    private int serial_required;
    private int serial_allow_new;

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
                hmAux_Trans
                );
        //
        mket_serial_id = (MKEditTextNM) findViewById(R.id.act008_mket_serial);
        controls_sta.add(mket_serial_id);
        //
        tv_product_code_label = (TextView) findViewById(R.id.act008_tv_product_code);
        tv_product_code_label.setTag("product_label");
        //
        tv_product_code_value = (TextView) findViewById(R.id.act008_tv_product_code_value);
        //
        tv_product_desc_label = (TextView) findViewById(R.id.act008_tv_description);
        tv_product_desc_label.setTag("product_desc_label");
        tv_product_desc_value = (TextView) findViewById(R.id.act008_tv_description_value);
        //
        chk_required = (CheckBox) findViewById(R.id.act008_chk_require_serial);
        chk_required.setTag("chk_required");
        chk_allow_new = (CheckBox) findViewById(R.id.act008_chk_allow);
        chk_allow_new.setTag("chk_allow_new");

        btn_create = (Button) findViewById(R.id.act008_btn_create);
        btn_create.setTag("btn_create");

        //Adiciona Views na lista de tradução

        views.add(tv_product_code_label);
        views.add(tv_product_desc_label);
        views.add(chk_required);
        views.add(chk_allow_new);
        views.add(btn_create);

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
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
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé - fim
    }

    private void initActions() {
        //
        mPresenter.getProductInfo();
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
        //
        tv_product_code_value.setText(String.valueOf(md_product.getProduct_code()));
        tv_product_desc_value.setText(md_product.getProduct_id()+ " - " + md_product.getProduct_desc());
        //
        serial_required = md_product.getRequire_serial();
        serial_allow_new = md_product.getAllow_new_serial_cl();
        //
        chk_required.setChecked( md_product.getRequire_serial() == 1 ? true : false );
        chk_allow_new.setChecked( md_product.getAllow_new_serial_cl() == 1 ? true : false);
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
                        callAct009(context);
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

        callAct009(context);

    }
    //Trata retorno de serial OK
    @Override
    protected void processSerialOk() {
        super.processSerialOk();

        disableProgressDialog();

        callAct009(context);
    }

    @Override
    public void callAct009(Context context) {
        Intent mIntent =  new Intent(context, Act009_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putString(Constant.ACT008_SERIAL_ID,mket_serial_id.getText().toString().trim());
        bundle.putString(Constant.ACT008_PRODUCT_DESC,tv_product_desc_value.getText().toString().trim());

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
}
