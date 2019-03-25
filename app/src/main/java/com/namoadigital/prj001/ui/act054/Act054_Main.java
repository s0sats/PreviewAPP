package com.namoadigital.prj001.ui.act054;

import android.content.Intent;
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
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.service.WS_IO_Move_Search;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act055.Act055_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act054_Main extends Base_Activity implements Act054_Main_Contract.I_View{

    public static final String IO_MOVE_RECORDS = "IO_MOVE_RECORDS";
    CheckBox cbInbound;
    CheckBox cbOutbound;
    CheckBox cbPlannedMove;
    CheckBox cbIoOrigins;
    CheckBox cbIoDestiny;
    LinearLayout llIoZone;
    SearchableSpinner ssIoZone;
    ImageView ivIoZoneRemove;
    TextView tvIoOrientationLbl;
    Button searchBtnMoveOrder;
    Button btnMoveOrderPendency;

    private Act054_Main_Presenter mPresenter;
    private String wsProcess;

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
        mPresenter = new Act054_Main_Presenter(context,this, hmAux_Trans);
        recoverIntentsInfo();
        bindViews();
        setInitialView();
    }

    private void setInitialView() {
        ArrayList<HMAux> zoneList = new ArrayList<>();
        zoneList.addAll(mPresenter.getZoneList());
        ssIoZone.setmOption(zoneList);
        ssIoZone.setmTitle(hmAux_Trans.get("user_zone_lbl"));
        ssIoZone.setmLabel(hmAux_Trans.get("user_zone_lbl"));
        ssIoZone.setmShowLabel(true);
        ssIoZone.setmStyle(1);
        cbIoDestiny.setEnabled(false);
        cbIoOrigins.setEnabled(false);
        for (HMAux hmAux: zoneList) {
            if(hmAux.hasConsistentValue(SearchableSpinner.ID)){
                String s = hmAux.get(SearchableSpinner.ID);
                String preference_zone_code = String.valueOf(ToolBox_Con.getPreference_Zone_Code(this));
                if (hmAux.get(SearchableSpinner.ID).equals(preference_zone_code)){
                    ssIoZone.setmValue(hmAux);
                    cbIoDestiny.setEnabled(true);
                    cbIoOrigins.setEnabled(true);
                    break;
                }
            }
        }

    }

    private void recoverIntentsInfo() {

    }

    private void bindViews() {
        cbInbound = findViewById(R.id.act054_cb_inbound);
        cbOutbound = findViewById(R.id.act054_cb_outbound);
        cbPlannedMove = findViewById(R.id.act054_cb_planned_move);
        llIoZone = findViewById(R.id.act054_ll_io_zone);
        ssIoZone = findViewById(R.id.act054_ss_io_zone);
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
        ssIoZone.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if(hmAux.hasConsistentValue(SearchableSpinner.ID)){
                    cbIoDestiny.setEnabled(true);
                    cbIoOrigins.setEnabled(true);
                }else{
                    cbIoDestiny.setEnabled(false);
                    cbIoDestiny.setChecked(false);
                    cbIoOrigins.setEnabled(false);
                    cbIoOrigins.setChecked(false);
                }
            }
        });

        ivIoZoneRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        searchBtnMoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zoneDesc;
                if(!ssIoZone.getmValue().hasConsistentValue(SearchableSpinner.ID)){
                    zoneDesc = "";
                }else{
                    zoneDesc = ssIoZone.getmValue().get(SearchableSpinner.ID);
                }
                if(validateField(zoneDesc)){
                    mPresenter.getMovements(
                            cbInbound.isChecked(),
                            cbOutbound.isChecked(),
                            cbPlannedMove.isChecked(),
                            zoneDesc,
                            cbIoOrigins.isChecked(),
                            cbIoDestiny.isChecked()
                    );
                }else{
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_value_filled_ttl"),
                            hmAux_Trans.get("alert_no_value_filled_msg"),
                            null,
                            0
                    );
                }
            }
        });
    }
    @Override
    public void callAct055(ArrayList<IO_Move_Search_Record> record_list) {
        Intent mIntent = new Intent(context, Act055_Main.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IO_MOVE_RECORDS, record_list);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }
    /**
     * Alguns WS mais antigos executam a chamada dessa assinatura do metodo
     * processCloseACT e aqui serão "encaminhados" para a segunda assinatura,
     * consolidando as tratativas em um unico metodo.
     *
     * No caso dessa act, o WS_Serial_Search retorna os dados aqui.
     * @param mLink
     * @param mRequired
     */
    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //
        processCloseACT(mLink,mRequired,new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if(wsProcess.equals(WS_IO_Move_Search.class.getName())) {
            mPresenter.processIOMoveSearch(mLink);
        }
        //
        progressDialog.dismiss();
    }


    private boolean validateField(String zoneDesc) {
        return (cbInbound.isChecked() || cbOutbound.isChecked() || cbPlannedMove.isChecked() )
                && ((!zoneDesc.isEmpty() && (cbIoOrigins.isChecked() || cbIoDestiny.isChecked())) || zoneDesc.isEmpty()
        );
    }

    @Override
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.onBackPressedClicked(Constant.ACT051);
    }
}
