package com.namoadigital.prj001.ui.act054;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act054_Main extends Base_Activity implements Act054_Main_Contract.I_View{

    CheckBox cbInbound;
    CheckBox cbOutbound;
    CheckBox cbPlannedMove;
    CheckBox cbIoOrigins;
    CheckBox cbIoDestiny;
    LinearLayout llIoZone;
    MKEditTextNM mkeIoZone;
    ImageView ivIoZoneRemove;
    TextView tvIoOrientationLbl;
    Button searchBtnMoveOrder;
    Button btnMoveOrderPendency;

    private Act054_Main_Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act054_main);


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
                Constant.ACT054
        );

        loadTranslation();
        //
        hideSoftKeyboard();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act054_title");
        transList.add("act054_lbl_new");
        transList.add("act054_lbl_barcode");
        transList.add("btn_pendencies");
        transList.add("btn_check_exists");
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
        transList.add("alert_new_opt_ttl");
        transList.add("alert_new_opt_product_lbl");
        transList.add("alert_new_opt_serial_lbl");
        transList.add("mket_serial_hint");
        transList.add("alert_no_value_filled_ttl");
        transList.add("alert_no_value_filled_msg");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        //
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        //
        transList.add("mket_zone_hint");
        transList.add("alert_local_product_not_found_msg");
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initVars() {
        recoverIntentsInfo();
        bindViews();
        setInitialView();
        mPresenter = new Act054_Main_Presenter(context,this, hmAux_Trans);

    }

    private void setInitialView() {
        cbIoDestiny.setEnabled(false);
        cbIoOrigins.setEnabled(false);
        mkeIoZone.setHint(hmAux_Trans.get("mket_zone_hint"));
    }

    private void recoverIntentsInfo() {

    }

    private void bindViews() {
        cbInbound = findViewById(R.id.act054_cb_inbound);
        cbOutbound = findViewById(R.id.act054_cb_outbound);
        cbPlannedMove = findViewById(R.id.act054_cb_planned_move);
        llIoZone = findViewById(R.id.act054_ll_io_zone);
        mkeIoZone = findViewById(R.id.act054_mke_io_zone);
        ivIoZoneRemove = findViewById(R.id.act054_iv_io_zone_remove);
        tvIoOrientationLbl = findViewById(R.id.act054_tv_io_orientation_lbl);
        cbIoOrigins = findViewById(R.id.act054_cb_io_origins);
        cbIoDestiny = findViewById(R.id.act054_cb_io_destiny);
        searchBtnMoveOrder = findViewById(R.id.act054_search_btn_move_order);
        btnMoveOrderPendency = findViewById(R.id.act054_btn_move_order_pendency);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT054;
        mAct_Title = Constant.ACT054 + Constant.title_lbl;
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

    private void initActions() {
        mkeIoZone.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if(s == null || s.isEmpty()){
                    cbIoDestiny.setEnabled(false);
                    cbIoDestiny.setChecked(false);
                    cbIoOrigins.setEnabled(false);
                    cbIoOrigins.setChecked(false);
                }else{
                    cbIoDestiny.setEnabled(true);
                    cbIoOrigins.setEnabled(true);
                }
            }
        });

        ivIoZoneRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mkeIoZone.setText("");
            }
        });

        searchBtnMoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateField()){

                }
            }
        });
    }

    private boolean validateField() {
        return false;
    }
}
