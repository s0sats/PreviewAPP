package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.ui.act007.Act007_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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
    private BootstrapButton btn_back;
    private BootstrapButton btn_create;

    private Act008_Main_Presenter mPresenter;

    private Bundle bundle;
    private long product_code;

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
    }

    private void loadTranslation() {
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context)
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
                product_code
                );
        //
        mket_serial_id = (MKEditTextNM) findViewById(R.id.act008_mket_serial);
        //
        tv_product_code_label = (TextView) findViewById(R.id.act008_tv_product_code);
        tv_product_code_value = (TextView) findViewById(R.id.act008_tv_product_code_value);
        //
        tv_product_desc_label = (TextView) findViewById(R.id.act008_tv_description);
        tv_product_desc_value = (TextView) findViewById(R.id.act008_tv_description_value);
        //
        chk_required = (CheckBox) findViewById(R.id.act008_chk_require_serial);
        chk_allow_new = (CheckBox) findViewById(R.id.act008_chk_allow);
        //
        btn_back = (BootstrapButton) findViewById(R.id.act008_btn_back);
        btn_create = (BootstrapButton) findViewById(R.id.act008_btn_create);


    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = bundle.getLong(Constant.ACT007_PRODUCT_CODE);
        } else {
            product_code = 0L;
        }

        bundle.remove(Constant.ACT007_PRODUCT_CODE);
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
    }

    private void initActions() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAct007(context);
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                mPresenter.validadeSerial(mket_serial_id.getText().toString());
            }
        });

    }

    @Override
    public void setCheckboxValues(int required, int allow_new) {
        chk_required.setChecked( required == 1 ? true : false );
        chk_allow_new.setChecked( allow_new == 1 ? true : false);
    }

    private void callAct007(Context context) {

        Intent mIntent =  new Intent(context, Act007_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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
                context,
                title,
                msg,
                null
        );
    }

    @Override
    public void showPD(String wsProcess) {

        String alertTitle = "";
        String alertMsg = "";

        switch (wsProcess){
            case WS_PROCESS_SYNC:
                alertTitle = "Sync";
                alertMsg = "Sync Start...";
                break;
            case WS_PROCESS_SERIAL:
                alertTitle = "Serial";
                alertMsg = "Serial Start... ";
                break;
            default:
                break;
        }

        if(alertTitle.length() != 0){
           enableProgressDialog(
                   alertTitle,
                   alertMsg,
                   getResources().getString(R.string.generic_msg_cancel),
                   getResources().getString(R.string.generic_msg_ok)
                   );

        }
    }


    @Override
    protected void processSerialNExist() {
        super.processSerialNExist();

        Toast.makeText(context,"Not Exists",Toast.LENGTH_SHORT).show();
        disableProgressDialog();
    }

    @Override
    protected void processSerialOk() {
        super.processSerialOk();

        Toast.makeText(context,"Exists",Toast.LENGTH_SHORT).show();
        disableProgressDialog();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);



    }
}
