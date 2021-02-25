package com.namoadigital.prj001.ui.act022;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 22/06/2017.
 */

public class Act022_Main extends Base_Activity_Frag_NFC_Geral implements Act022_Main_Contract.I_View {


    private Act022_Main_Contract.I_Presenter mPresenter;

    private Bundle bundle;

    private String product_code;
    private String serial_id;

    private MD_Product mdProduct;

    private TextView tv_product_desc;
    private MKEditTextNM mk_serial_id;
    private ImageView iv_nfc;
    private Button btn_cancel;
    private Button btn_ok;

    private boolean isShowingAlert = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act022_main);

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
                Constant.ACT022
        );

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_code");
        transList.add("lbl_id");
        transList.add("lbl_desc");
        transList.add("mket_hint_msg");
        transList.add("btn_back");
        transList.add("btn_home");
        transList.add("alert_nfc_auth_cancel_ttl");
        transList.add("alert_nfc_auth_cancel_msg");
        transList.add("serial_hint_lbl");

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
        setbNFCStatus(true);
        setNFC_PARAMS_TECH_SERIAL(true);

        tv_product_desc = (TextView) findViewById(R.id.act022_tv_product_desc_value);
        mk_serial_id = (MKEditTextNM) findViewById(R.id.act022_mket_serial_id);
        iv_nfc = (ImageView) findViewById(R.id.act022_iv_nfc);
        btn_cancel = (Button) findViewById(R.id.act022_btn_cancel);
        btn_cancel.setText(hmAux_Trans.get("sys_alert_btn_cancel"));
        btn_ok = (Button) findViewById(R.id.act022_btn_ok);
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        mk_serial_id.setHint(hmAux_Trans.get("serial_hint_lbl"));
        //LUCHE - 10/06/2019
        setupMketSerialInputTech();

        if (supportNFC) {
            iv_nfc.setVisibility(View.VISIBLE);
        } else {
            iv_nfc.setVisibility(View.GONE);
        }

        mPresenter = new Act022_Main_Presenter(
                context,
                this,
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(
                                ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );

        controls_sta.add(mk_serial_id);

        mdProduct = mPresenter.getMD_Produt(product_code);

        tv_product_desc.setText(mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc());
        //LUCHE - 10/06/2019
        setSerialRule(mdProduct != null ? mdProduct.getSerial_rule() : null);
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            product_code = bundle.getString(MD_Product_SerialDao.PRODUCT_CODE, "");
            serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
        } else {
            product_code = "";
            serial_id = "";
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT022;
        mAct_Title = Constant.ACT022 + "_" + "title";
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

    private void initActions() {

//        mk_serial_id.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {
//            @Override
//            public void reportTextBySpecialist(String s) {
//                mPresenter.processValidation(product_code, serial_id, "", s);
//            }
//        });
        mk_serial_id.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {
            @Override
            public void reportTextBySpecialist(final String s) {

                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                String serial_id_formatted = ToolBox_Inf.removeForbidenChars(s);
                                mk_serial_id.setText(serial_id_formatted);
                                mPresenter.processValidation(product_code, serial_id, "", serial_id_formatted);
                            }
                        },
                        500
                );
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolBox.alertMSG_YES_NO(
                        context,
                        hmAux_Trans.get("alert_nfc_auth_cancel_ttl"),
                        hmAux_Trans.get("alert_nfc_auth_cancel_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendReturn("");
                            }
                        },
                        2,
                        false
                );
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serial_id_formatted = mk_serial_id.getText().toString();
                mk_serial_id.setText(ToolBox_Inf.removeForbidenChars(serial_id_formatted));
                mPresenter.processValidation(product_code, serial_id, "", mk_serial_id.getText().toString());
            }
        });
    }

    /**
     * LUCHE - 10/06/2019
     * <p>
     * Metodo que seta quais são as tecnologias de entrada de dados
     * usando os parametros do profile.
     */
    private void setupMketSerialInputTech() {
        //LUCHE - 10/06/2019
        //Nos campos mket referentes a serial, o valores de mOcr e mBarcode serão preenchidos
        //via parametro do profile.
        //mk_serial_id.setmOCR(false);
        //LUCHE - 26/09/2019
        //Agora o parametro ocr padrão é o leitor OCR da mosolf e esta sendo utlizado
        //com a var mOCR
        mk_serial_id.setmOCR(
            ToolBox_Inf.profileExists(
                context,
                Constant.PROFILE_MENU_PROFILE,
                Constant.PROFILE_MENU_PROFILE_SERIAL_OCR_MOSOLF
            )
        );
        mk_serial_id.setmNFC(false);
        mk_serial_id.setmBARCODE(
            ToolBox_Inf.profileExists(
                context,
                Constant.PROFILE_MENU_PROFILE,
                Constant.PROFILE_MENU_PROFILE_SERIAL_BARCODE
            )
        );
    }

    /**
     * 14/11/18 - LUCHE
     * Adicionando metodo para que a validação a "quebra de leitura de vin" fosse possivel. Necessidade
     * MOSOLF
     * Seta regra de validação no campo.
     * Seta também variaveis para ignorarem mim e max e validação de vin após leitura de barcode
     * para true;
     * @param serial_rule
     */
    public void setSerialRule(String serial_rule){
        mk_serial_id.setmInputTypeValidator(serial_rule);
        mk_serial_id.setmIgnoreVINValidationOnRead(true);
        mk_serial_id.setmIgnoreMaxMinSize(true);
    }

    @Override
    public void showMSG() {
        if (!isShowingAlert) {
            isShowingAlert = true;

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_invalid_serial_ttl"),
                    hmAux_Trans.get("alert_invalid_serial_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isShowingAlert = false;
                        }
                    },
                    -1
            );
        }
    }

    @Override
    public void sendReturn(String status) {
        ToolBox.setPreference_UI_ID(context, -1);
        ToolBox.setPreference_UI_TYPE(context, 2);
        ToolBox.setPreference_UI_VALUE(context, status);
        //
        finish();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }

    @Override
    protected void nfcData(boolean status, int id, final String... value) {
        super.nfcData(status, id, value);
        //
        if (status) {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.processValidation(product_code, serial_id, value[2], value[3]);
                        }
                    },
                    500
            );
        } else {
            if (!isShowingAlert) {
                isShowingAlert = true;

                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_serial_validation_ttl"),
                        value[0],
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isShowingAlert = false;
                            }
                        },
                        -1

                );
            }
        }
    }

//    @Override
//    protected void nfcDataError(boolean status, int id, String... value) {
//        super.nfcDataError(status, id, value);
//        //
//        ToolBox.alertMSG(
//                context,
//                hmAux_Trans.get("alert_serial_validation_ttl"),
//                value[0],
//                null,
//                -1
//        );
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
