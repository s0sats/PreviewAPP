package com.namoadigital.prj001.ui.act022;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT011;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT022;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.namoa_digital.namoa_library.ctls.ButtonNFC;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BarCode_Activity;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoa_digital.namoa_library.view.scanner.BaseScannerActivity;
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
    private String serial_id_from_barcode;
    private boolean back_pressed_flow = false;

    private MD_Product mdProduct;


    private TextView tv_qrcode_reader_ttl;
    private MaterialButton btn_qrcode_reader;
    private MaterialButton btn_cancel;
    private ButtonNFC btn_nfc_reader;

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
                ACT022
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
        transList.add("qr_code_reader_lbl");
        transList.add("nfc_reader_lbl");
        transList.add("alert_serial_confirmation_ttl");

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
        tv_qrcode_reader_ttl  = findViewById(R.id.tv_qrcode_reader_ttl);
        btn_qrcode_reader  = findViewById(R.id.btn_qrcode_reader);
        btn_cancel  =  findViewById(R.id.btn_cancel);
        btn_nfc_reader  = findViewById(R.id.btn_nfc_reader);
//        mk_serial_id  = findViewById(R.id.act022_mket_serial_id);

        tv_qrcode_reader_ttl.setText(hmAux_Trans.get("alert_serial_confirmation_ttl"));
        btn_qrcode_reader.setText(hmAux_Trans.get("qr_code_reader_lbl"));
        btn_nfc_reader.setText(hmAux_Trans.get("nfc_reader_lbl"));
        btn_cancel.setText(hmAux_Trans.get("sys_alert_btn_cancel"));
        //LUCHE - 10/06/2019
//        setupMketSerialInputTech();

        if (supportNFC) {
            btn_nfc_reader.setVisibility(View.VISIBLE);
            btn_nfc_reader.setmCustomer_code(String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
            btn_nfc_reader.setmProduct(true);
            btn_nfc_reader.setmSerial(true);
            btn_nfc_reader.setmProgressClose(true);
        } else {
            btn_nfc_reader.setVisibility(View.GONE);
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

        mdProduct = mPresenter.getMD_Produt(product_code);

        //LUCHE - 10/06/2019
//        setSerialRule(mdProduct != null ? mdProduct.getSerial_rule() : null);
//        controls_sta.add(mk_serial_id);
        callBarcodeActivity();

    }

    private void callBarcodeActivity() {
        Intent intent = new Intent(this, BarCode_Activity.class);

        intent.putExtra(BarCode_Activity.SERIAL_VALIDATION, serial_id);
        intent.putExtra(BarCode_Activity.SERIAL_VALIDATION_BACK_TTL, hmAux_Trans.get("alert_nfc_auth_cancel_ttl"));
        intent.putExtra(BarCode_Activity.SERIAL_VALIDATION_BACK_MSG, hmAux_Trans.get("alert_nfc_auth_cancel_msg"));


        startActivity(intent);

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
        mAct_Info = ACT022;
        mAct_Title = ACT022 + "_" + "title";
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

        btn_nfc_reader.setOnClickListener(actionBTN);

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

        btn_qrcode_reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBarcodeActivity();
            }
        });
    }

    /**
     * LUCHE - 10/06/2019
     * <p>
     * Metodo que seta quais são as tecnologias de entrada de dados
     * usando os parametros do profile.
     */
//    private void setupMketSerialInputTech() {
//        //LUCHE - 10/06/2019
//        //Nos campos mket referentes a serial, o valores de mOcr e mBarcode serão preenchidos
//        //via parametro do profile.
//        //mk_serial_id.setmOCR(false);
//        //LUCHE - 26/09/2019
//        //Agora o parametro ocr padrão é o leitor OCR da mosolf e esta sendo utlizado
//        //com a var mOCR
//        mk_serial_id.setmOCR(
//            ToolBox_Inf.profileExists(
//                context,
//                Constant.PROFILE_MENU_PROFILE,
//                Constant.PROFILE_MENU_PROFILE_SERIAL_OCR_MOSOLF
//            )
//        );
//        mk_serial_id.setmNFC(false);
//        mk_serial_id.setmBARCODE(
//            ToolBox_Inf.profileExists(
//                context,
//                Constant.PROFILE_MENU_PROFILE,
//                Constant.PROFILE_MENU_PROFILE_SERIAL_BARCODE
//            )
//        );
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    /**
     * 14/11/18 - LUCHE
     * Adicionando metodo para que a validação a "quebra de leitura de vin" fosse possivel. Necessidade
     * MOSOLF
     * Seta regra de validação no campo.
     * Seta também variaveis para ignorarem mim e max e validação de vin após leitura de barcode
     * para true;
//     * @param serial_rule
     */
//    public void setSerialRule(String serial_rule){
//        mk_serial_id.setmInputTypeValidator(serial_rule);
//        mk_serial_id.setmIgnoreVINValidationOnRead(true);
//        mk_serial_id.setmIgnoreMaxMinSize(true);
//    }

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
    public void onResume() {
        super.onResume();
        /**
         * BARRIONUEVO 26-06-2025
         * O Fluxo do barcode retorna o controle para a tela antes de limpeza das preferencias
         * para garantir que a proxima tela terá o valor seguinte foi feita a tratativa no onResume().
         */
        if(serial_id_from_barcode!=null) {
            mPresenter.processValidation(product_code, serial_id, "", serial_id_from_barcode);
        } else if(back_pressed_flow){
            sendReturn("");
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
    protected void getBarcodeCallback(Integer id, String value) {

        serial_id_from_barcode = null;
        back_pressed_flow = false;
        switch (value){
            case BaseScannerActivity.ON_BACK_PRESSED:
                back_pressed_flow = true;
                break;
            case BaseScannerActivity.ON_NFC_PROCESS:
//                btn_nfc_reader.performClick();
                break;
            default:
                serial_id_from_barcode = ToolBox_Inf.removeForbidenChars(value);
                break;
        }
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
}
