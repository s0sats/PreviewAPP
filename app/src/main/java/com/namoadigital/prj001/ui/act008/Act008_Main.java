package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
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
    private TextView tv_product_value_label;
    private TextView tv_product_value;
    private TextView tv_product_desc_label;
    private TextView tv_product_desc;
    private MKEditTextNM mket_serial_id;
    private CheckBox chk_required;
    private CheckBox chk_allow_new;
    private BootstrapButton btn_back;
    private BootstrapButton btn_create;

    private Act008_Main_Presenter mPresenter;



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
        mPresenter =  new Act008_Main_Presenter_Impl(
                context,
                this
                );
        //
        mket_serial_id = (MKEditTextNM) findViewById(R.id.act008_mket_serial);
        //


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

        mPresenter.validadeSerial(14L,"s001huehue");

    }

    @Override
    public void fieldFocus() {
        mket_serial_id.requestFocus();
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
}
