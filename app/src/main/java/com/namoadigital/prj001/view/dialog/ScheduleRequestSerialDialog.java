package com.namoadigital.prj001.view.dialog;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRequestSerialDialog extends AlertDialog {
    private Context context;
    private HMAux  hmAux_Trans;
    private String mResource_Code;
    private String mResourceName = "schedule_request_serial_dialog";
    private HMAux auxSchedule = new HMAux();
    //
    private TextView tvQuestion;
    private TextView tvProduct;
    private RadioGroup rgConfirm;
    private RadioButton rdoNo;
    private RadioButton rdoYes;
    private TextInputLayout tilSerial;
    private MKEditTextNM mketSerial;
    private Button btnAction;
    private final OnScheduleRequestSerialDialogListeners listeners;

    public ScheduleRequestSerialDialog(@NonNull Context context, HMAux auxSchedule, OnScheduleRequestSerialDialogListeners listeners){
        super(context);
        this.context = context;
        this.auxSchedule = auxSchedule;
        this.listeners = listeners;
    }

    public interface OnScheduleRequestSerialDialogListeners{
        void processToForm();
        void processToSearchSerial(String serialID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_request_serial_dialog);
        //
        loadTranslation();
        //
        bindViews();
        //
        setLabels();
        //
        iniActions();
        //
        setConfig();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        //
        transList.add("dialog_ttl");
        transList.add("serial_hint");
        transList.add("btn_open_form");
        transList.add("btn_search_serial");
        transList.add("inform_serial_confirm");
        transList.add("inform_serial_required");
        transList.add("serial_required_lbl");
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            getContext(),
            ConstantBaseApp.APP_MODULE,
            mResourceName
        );
        //
         hmAux_Trans = ToolBox_Inf.setLanguage(
            getContext(),
            ConstantBaseApp.APP_MODULE,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(getContext()),
            transList
        );
    }

    private void bindViews() {
        tvQuestion = findViewById(R.id.schedule_request_serial_dialog_tv_question);
        tvProduct = findViewById(R.id.schedule_request_serial_dialog_tv_product);
        rgConfirm = findViewById(R.id.schedule_request_serial_dialog_rg_confirm);
        rdoNo = findViewById(R.id.schedule_request_serial_dialog_rdo_no);
        rdoYes = findViewById(R.id.schedule_request_serial_dialog_rdo_yes);
        tilSerial = findViewById(R.id.schedule_request_serial_dialog_til_serial);
        mketSerial = findViewById(R.id.schedule_request_serial_dialog_mket_serial);
        btnAction = findViewById(R.id.schedule_request_serial_dialog_btn_action);
    }

    private void setLabels() {
        setTitle( hmAux_Trans.get("dialog_ttl"));
        tvQuestion.setText(hmAux_Trans.get("inform_serial_confirm"));
        //tvQuestion.setText("Deseja informar serial ?");
        tvProduct.setText(
            ToolBox_Inf.getFormatedProductIdDesc(
                auxSchedule.get(MD_Schedule_ExecDao.PRODUCT_ID),
                auxSchedule.get(MD_Schedule_ExecDao.PRODUCT_DESC)
            )
        );
        rdoNo.setText( hmAux_Trans.get("sys_alert_btn_no"));
        rdoYes.setText( hmAux_Trans.get("sys_alert_btn_yes"));
        mketSerial.setHint( hmAux_Trans.get("serial_hint"));
        btnAction.setText( hmAux_Trans.get("btn_search_serial"));
    }

    private void iniActions() {
        rgConfirm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                applyOptionUI(checkedId);
            }
        });
        //
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listeners != null){
                    if(rgConfirm.getCheckedRadioButtonId() ==  R.id.schedule_request_serial_dialog_rdo_yes){
                        listeners.processToSearchSerial(mketSerial.getText().toString().trim());
                    } else{
                        listeners.processToForm();
                    }
                }
            }
        });
    }

    private void setConfig() {
        rdoYes.setChecked(true);
        //
        if( auxSchedule.get(MD_Schedule_ExecDao.REQUIRE_SERIAL).equalsIgnoreCase("1")
            || auxSchedule.get(MD_Schedule_ExecDao.REQUIRE_SERIAL_DONE).equalsIgnoreCase("1")
        ){
            //opção 1
            rdoNo.setEnabled(false);
            tilSerial.setErrorEnabled(true);
            tilSerial.setError(hmAux_Trans.get("inform_serial_required"));

            //opção 2
//            tvQuestion.setText(hmAux_Trans.get("serial_required_lbl"));
//            rgConfirm.setVisibility(View.GONE);
//            rdoNo.setEnabled(false);
//            tilSerial.setErrorEnabled(false);
//            mketSerial.setHint(hmAux_Trans.get("inform_serial_required"));

        }
    }

    private void applyOptionUI(int checkedId) {
        Drawable searchIcon = null;
        String btnText = "";
        if(checkedId == R.id.schedule_request_serial_dialog_rdo_no){
            searchIcon = null;
            mketSerial.setVisibility(View.GONE);
            btnText =  hmAux_Trans.get("btn_open_form");
        } else{
            searchIcon =  context.getResources().getDrawable(R.drawable.icon_lupa_ns);
            searchIcon.setColorFilter(context.getResources().getColor(R.color.padrao_WHITE), PorterDuff.Mode.SRC_ATOP);
            mketSerial.setVisibility(View.VISIBLE);
            btnText =  hmAux_Trans.get("btn_search_serial");
        }
        //
        btnAction.setCompoundDrawablesWithIntrinsicBounds(null,null,searchIcon,null);
        btnAction.setText(btnText);
    }

    public HMAux getAuxSchedule() {
        return auxSchedule;
    }

    public String getSerialId(){
        if(mketSerial != null){
            return mketSerial.getText().toString().trim();
        }
        //
        return "";
    }
}
