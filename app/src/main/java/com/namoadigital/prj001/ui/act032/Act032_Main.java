package com.namoadigital.prj001.ui.act032;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC_Geral;
import com.namoa_digital.namoa_library.view.SignaTure_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.service.WS_SO_Serial_Save;
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

    public static final String WS_PROCESS_SERIAL = "WS_PROCESS_SERIAL";
    public static final String WS_PROCESS_SO_SYNC = "WS_PROCESS_SO_SYNC";
    public static final String WS_PROCESS_SO_SAVE = "WS_PROCESS_SO_SAVE";

    private Bundle bundle;

    private String mSignature;
    private String mStatus;
    private String mClientType;

    HMAux typeAux = new HMAux();

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

    private Handler handler;
    private Runnable runnable;

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

        //
        // LIMBO PODERÁ SER DESCARTADA.
        //

    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT032
        );

        loadTranslation();

        setNFC_PARAMS_TECH_LOGIN(true);

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("generic_nfc_timeout_msg"), "", "0");
            }
        };
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
        transList.add("alert_approval_title");
        transList.add("alert_approval_msg");
        transList.add("alert_nfc_title");
        transList.add("alert_nfc_msg");
        transList.add("alert_no_signature_title");
        transList.add("alert_no_signature_msg");
        transList.add("btn_approval_lbl");
        transList.add("btn_nfc_lbl");
        transList.add("btn_password_lbl");
        //
        transList.add("msg_approval");
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
        btn_approval.setText(hmAux_Trans.get("btn_approval_lbl"));
        btn_nfc = (Button) findViewById(R.id.act032_main_content_btn_nfc);
        btn_nfc.setText(hmAux_Trans.get("btn_nfc_lbl"));
        btn_password = (Button) findViewById(R.id.act032_main_content_btn_password);
        btn_password.setText(hmAux_Trans.get("btn_password_lbl"));

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

        switch (data.get(SM_SODao.CLIENT_TYPE).toUpperCase()) {
            case Constant.CLIENT_TYPE_USER:
                if (data.get(SM_SODao.CLIENT_USER).equalsIgnoreCase(ToolBox_Con.getPreference_User_Code(context))) {
                    btn_approval.setVisibility(View.VISIBLE);
                    btn_nfc.setVisibility(View.GONE);
                    btn_password.setVisibility(View.GONE);
                } else {
                    btn_approval.setVisibility(View.GONE);
                    btn_nfc.setVisibility(View.VISIBLE);
                    btn_password.setVisibility(View.VISIBLE);
                }

                break;
            default:
                btn_approval.setVisibility(View.GONE);
                btn_nfc.setVisibility(View.GONE);
                btn_password.setVisibility(View.GONE);
                break;
        }
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            data = (HashMap<String, String>) bundle.getSerializable("data");

            mSignature = "s_" +
                    "CC" + data.get(SM_SODao.CUSTOMER_CODE) +
                    "_SP" + data.get(SM_SODao.SO_PREFIX) +
                    "_SC" + data.get(SM_SODao.SO_CODE) +
                    ".png";
        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }

    public void showType_SigDialog(final SM_SO_Service_Exec sm_so_service_exec) {

        final ArrayList<HMAux> type_sigs = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_new_partner_opt, null);

        SearchableSpinner ss_type_sig = (SearchableSpinner) view.findViewById(R.id.act028_dialog_new_partner_opt_ss_partner);

        typeAux.put("id", "CLIENT");
        typeAux.put("description", "Client");

        ss_type_sig.setmLabel("Selecao de Partner");
        ss_type_sig.setmTitle("Busca de Partner");

        HMAux hmAuxClient = new HMAux();
        hmAuxClient.put("id", "CLIENT");
        hmAuxClient.put("description", "Client");

        HMAux hmAuxUser = new HMAux();
        hmAuxClient.put("id", "USER");
        hmAuxClient.put("description", "USER");

        type_sigs.add(hmAuxClient);
        type_sigs.add(hmAuxUser);

        ss_type_sig.setmValue(typeAux);
        ss_type_sig.setmOption(type_sigs);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                callSignature();
            }
        });

        final AlertDialog show = builder.show();

        ss_type_sig.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                typeAux.clear();
                typeAux.putAll(hmAux);

                callSignature();

            }
        });
    }

    public void callSignature() {
        try {
            Bundle bundleN = new Bundle();
            bundleN.putInt(ConstantBase.PID, -1);
            bundleN.putInt(ConstantBase.PTYPE, 0);
            bundleN.putString(ConstantBase.PPATH, CACHE_PATH_PHOTO + "/" + mSignature);
            bundleN.putBoolean(ConstantBase.BLOCK_NAME, true);

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
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {

        btn_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.get(SM_SODao.CLIENT_TYPE).equalsIgnoreCase(Constant.CLIENT_TYPE_USER)) {

                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_approval_title"),
                            hmAux_Trans.get("alert_approval_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.onProcessApproval(data);
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
                //
                handler.postDelayed(runnable, 15 * 1000);
                //
                enableProgressDialog(
                        hmAux_Trans.get("alert_nfc_title"),
                        hmAux_Trans.get("alert_nfc_msg"),
                        hmAux_Trans.get("sys_alert_btn_cancel"),
                        hmAux_Trans.get("sys_alert_btn_ok")
                );
            }
        });

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPassWord();
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
        //
        setbNFCStatus(false);
        //
        handler.removeCallbacks(runnable);
        //
        if (status) {
            mPresenter.onProcessNFCPassWord(data, value[1], null);
        }
    }

    @Override
    protected void getSignatueF(String mValue) {
        mPresenter.onProcessSignature(data, mSignature, typeAux.get("id"));
    }

    private void callPassWord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act032_dialog_password, null);

        final TextView tv_password_title = (TextView) view.findViewById(R.id.act032_dialog_tv_password_title);
        final EditText et_password = (EditText) view.findViewById(R.id.act032_dialog_et_password);

        builder.setView(view);
        builder.setCancelable(false);
        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mPresenter.onProcessNFCPassWord(data, null, et_password.getText().toString());


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);
            }
        });

        AlertDialog show = builder.show();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void dismissPD() {
        try {
            disableProgressDialog();
        } catch (Exception e) {
        }
    }

    @Override
    public void errorMsg(String title, String error) {
        ToolBox.alertMSG(
                context,
                title,
                error,
                null,
                -1
        );
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
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (!hmAux.get(WS_SO_Serial_Save.SO_RETURN_FULL_REFRESH).equals("0")) {

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_so_list_title"),
                    hmAux_Trans.get("alert_so_list_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bundle.remove("data");
                            //
                            Intent mIntent = new Intent(context, Act027_Main.class);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mIntent.putExtras(bundle);
                            //
                            activateUpload(context);
                            //
                            startActivity(mIntent);
                            finish();
                        }
                    },
                    -1,
                    false
            );

        } else {

            disableProgressDialog();

        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        disableProgressDialog();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedAction();
    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
