package com.namoadigital.prj001.ui.act032;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC_Geral;
import com.namoa_digital.namoa_library.view.SignaTure_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.namoa_digital.namoa_library.util.ConstantBase.CACHE_PATH_PHOTO;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act032_Main extends Base_Activity_NFC_Geral implements Act032_Main_View {

    private Bundle bundle;

    private String mSignature;
    private String mStatus;
    private String mClientType;

    private HashMap<String, String> data;

    private Act032_Main_Presenter mPresenter;

    private TextView tv_so_label;
    private TextView tv_prefix_code_label;
    private TextView tv_prefix_code_value;
    private TextView tv_product_id_label;
    private TextView tv_product_id_value;

    private TextView tv_desc_label;
    private TextView tv_desc_value;

    private TextView tv_serial_label;
    private TextView tv_serial_value;

    private TextView tv_deadline_label;
    private TextView tv_deadline_value;

    private TextView tv_status_value;
    private TextView tv_priority_value;

    private TextView tv_client_type_label;
    private TextView tv_client_type_value;

    private TextView tv_client_name_label;
    private TextView tv_client_name_value;

    private Button btn_approval;
    private Button btn_nfc;
    private Button btn_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act032_main);
        //
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
                Constant.ACT032
        );

        loadTranslation();

        setNFC_PARAMS_TECH_LOGIN(true);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act032_title");
        transList.add("so_lbl");
        transList.add("prefix_code_lbl");
        transList.add("product_id_lbl");
        transList.add("product_description_lbl");
        transList.add("serial_lbl");
        transList.add("deadline_lbl");
        transList.add("client_type_lbl");
        transList.add("client_name_lbl");
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
        mPresenter = new Act032_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );

        tv_so_label = (TextView) findViewById(R.id.act032_main_content_tv_so_ttl);

        tv_prefix_code_label = (TextView) findViewById(R.id.act032_main_content_tv_prefix_code_label);
        tv_prefix_code_value = (TextView) findViewById(R.id.act032_main_content_tv_prefix_code_value);

        tv_product_id_label = (TextView) findViewById(R.id.act032_main_content_tv_product_id_label);
        tv_product_id_value = (TextView) findViewById(R.id.act032_main_content_tv_product_id_value);

        tv_desc_label = (TextView) findViewById(R.id.act032_main_content_tv_desc_label);
        tv_desc_value = (TextView) findViewById(R.id.act032_main_content_tv_desc_value);

        tv_serial_label = (TextView) findViewById(R.id.act032_main_content_tv_product_serial_label);
        tv_serial_value = (TextView) findViewById(R.id.act032_main_content_tv_product_serial_value);

        tv_deadline_label = (TextView) findViewById(R.id.act032_main_content_tv_deadline_label);
        tv_deadline_value = (TextView) findViewById(R.id.act032_main_content_tv_deadline_value);

        tv_status_value = (TextView) findViewById(R.id.act032_main_content_tv_status_value);
        tv_priority_value = (TextView) findViewById(R.id.act032_main_content_tv_priority_value);

        tv_status_value = (TextView) findViewById(R.id.act032_main_content_tv_status_value);
        tv_priority_value = (TextView) findViewById(R.id.act032_main_content_tv_priority_value);

        tv_client_type_label = (TextView) findViewById(R.id.act032_main_content_tv_client_type_label);
        tv_client_type_value = (TextView) findViewById(R.id.act032_main_content_tv_client_type_value);

        tv_client_name_label = (TextView) findViewById(R.id.act032_main_content_tv_client_name_label);
        tv_client_name_value = (TextView) findViewById(R.id.act032_main_content_tv_client_name_value);

        btn_approval = (Button) findViewById(R.id.act032_main_content_btn_approval);
        btn_nfc = (Button) findViewById(R.id.act032_main_content_btn_nfc);
        btn_password = (Button) findViewById(R.id.act032_main_content_btn_password);

        // Move Data
        tv_so_label.setText(hmAux_Trans.get("so_lbl"));
        tv_prefix_code_label.setText(hmAux_Trans.get("prefix_code_lbl"));
        tv_prefix_code_value.setText(data.get(SM_SODao.CUSTOMER_CODE) + " / " + data.get(SM_SODao.SO_CODE));
        tv_product_id_label.setText(hmAux_Trans.get("product_id_lbl"));
        tv_product_id_value.setText(data.get(SM_SODao.PRODUCT_ID));

        tv_desc_label.setText(hmAux_Trans.get("product_description_lbl"));
        tv_desc_value.setText(data.get(SM_SODao.PRODUCT_DESC));

        tv_serial_label.setText(hmAux_Trans.get("serial_lbl"));
        tv_serial_value.setText(data.get(SM_SODao.SERIAL_ID));

        tv_deadline_label.setText(hmAux_Trans.get("deadline_lbl"));
        tv_deadline_value.setText(data.get(SM_SODao.DEADLINE));

        tv_status_value.setText(data.get(SM_SODao.STATUS));
        tv_priority_value.setText(data.get(SM_SODao.PRIORITY_DESC));

        tv_client_type_label.setText(hmAux_Trans.get("client_type_lbl"));
        tv_client_type_value.setText(data.get(SM_SODao.CLIENT_TYPE));

        tv_client_name_label.setText(hmAux_Trans.get("client_name_lbl"));
        tv_client_name_value.setText(data.get(SM_SODao.CLIENT_NAME));

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            data = (HashMap<String, String>) bundle.getSerializable("data");
        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }

    public void callSignature() {
        try {
            Bundle bundleN = new Bundle();
            bundleN.putInt(ConstantBase.PID, -1);
            bundleN.putInt(ConstantBase.PTYPE, 0);
            bundleN.putString(ConstantBase.PPATH, CACHE_PATH_PHOTO + "/" + mSignature);

            Intent mIntent = new Intent(context, SignaTure_Activity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundleN);

            context.startActivity(mIntent);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT032;
        mAct_Title = Constant.ACT032 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value = hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl = hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value = hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé -fim

    }

    private void initActions() {

        btn_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!data.get(SM_SODao.CLIENT_TYPE).equalsIgnoreCase(Constant.CLIENT_TYPE_USER)) {

                    ToolBox.alertMSG(
                            context,
                            "Aprovacao",
                            "Deseja mesmo aprovar a SO?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            },
                            1,
                            false
                    );

                } else {
                    callSignature();
                }

            }
        });

        btn_nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbNFCStatus(true);
            }
        });

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void nfcData(boolean status, int id, String... value) {
        super.nfcData(status, id, value);


    }

    @Override
    protected void getSignatueF(String mValue) {
        super.getSignatueF(mValue);
    }

    @Override
    public void callAct027(Context context) {
        bundle.remove("data");
        //
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedAction();
    }
}
