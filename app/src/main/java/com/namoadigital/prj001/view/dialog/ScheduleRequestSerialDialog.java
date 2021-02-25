package com.namoadigital.prj001.view.dialog;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
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
    private final String serial_rule;
    private final Integer minSerialSize;
    private final Integer maxSerialSize;

    public ScheduleRequestSerialDialog(@NonNull Context context, HMAux auxSchedule, String serial_rule, Integer minSerialSize, Integer maxSerialSize, OnScheduleRequestSerialDialogListeners listeners){
        super(context);
        this.context = context;
        this.auxSchedule = auxSchedule;
        this.listeners = listeners;
        this.serial_rule = serial_rule;
        this.minSerialSize = minSerialSize;
        this.maxSerialSize = maxSerialSize;
    }

    public interface OnScheduleRequestSerialDialogListeners{
        /**
         * Metodo disparado quando o usr não deseja informar serial
         */
        void processToForm();

        /**
         * Metodo disparado quando o usuario deseja informar serial.
         * @param serialID - Serial Id digitado.
         */
        void processToSearchSerial(String serialID);

        /**
         * Metodo que adiciona o mket a lista de controles da act.
         * Necessario para leitura via barcode, OCR e etc.
         * @param mketSerial
         */
        void addMketControl(MKEditTextNM mketSerial);

        /**
         * Metodo que remove mket da lista de controles.
         * @param mketSerial
         */
        void removeMketControl(MKEditTextNM mketSerial);
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
        //Sem essas flags não abre o teclado no campo Mket
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //
        if(listeners != null){
            listeners.addMketControl(mketSerial);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(listeners != null){
            listeners.removeMketControl(mketSerial);
        }
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
        transList.add("serial_field_empty");
        transList.add("serial_rule_lbl");
        transList.add("serial_min_length_lbl");
        transList.add("serial_min_max_separator_lbl");
        transList.add("serial_max_length_lbl");
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
        tvProduct.setText(
            getProductInfo()
        );
        rdoNo.setText( hmAux_Trans.get("sys_alert_btn_no"));
        rdoYes.setText( hmAux_Trans.get("sys_alert_btn_yes"));
        mketSerial.setHint( hmAux_Trans.get("serial_hint"));
        btnAction.setText( hmAux_Trans.get("btn_search_serial"));
    }

    @NonNull
    /**
     * LUCHE - 09/03/2020
     *
     * Metodo que gera a descrição do produto formatada , baseada na chave enviada.
     * Quando dialog chamado da act017, chave do hmAux é PRODUCT_ID, quando act013 CUSTOM_PRODUCT_ID
     */
    private String getProductInfo() {
        String prodId = auxSchedule.containsKey(MD_Schedule_ExecDao.PRODUCT_ID)
            ? auxSchedule.get(MD_Schedule_ExecDao.PRODUCT_ID)
            : auxSchedule.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID);
        //
        String prodDesc = auxSchedule.containsKey(MD_Schedule_ExecDao.PRODUCT_DESC)
            ? auxSchedule.get(MD_Schedule_ExecDao.PRODUCT_DESC)
            : auxSchedule.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC);
        //
        return ToolBox_Inf.getFormatedProductIdDesc(prodId,prodDesc);
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
                        String serialId = mketSerial.getText().toString().trim();
                        if(serialId != null && !serialId.isEmpty()) {
                            listeners.processToSearchSerial(serialId);
                        }else{
                            if(isSerialOptional()) {
                                tilSerial.setErrorEnabled(true);
                                tilSerial.setError(hmAux_Trans.get("serial_field_empty"));
                            }
                        }
                    } else{
                        listeners.processToForm();
                    }
                }
            }
        });
        //
        mketSerial.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if(isSerialOptional()){
                    tilSerial.setErrorEnabled(false);
                }
            }
        });
    }

    private String getFormattedRuleHelper() {
        String sHelper = "";
        String sMin ="";
        String sMax ="";
        //
        if(serial_rule == null && minSerialSize == null && maxSerialSize == null){
            return null;
        }
        //
        if(serial_rule != null && !serial_rule.trim().isEmpty()){
            sHelper += hmAux_Trans.get("serial_rule_lbl") + " " + hmAux_Trans.get(serial_rule) + " ";
        }
        //
        if(minSerialSize != null && minSerialSize > 0){
            sMin = hmAux_Trans.get("serial_min_length_lbl") + minSerialSize;
        }
        //
        if(maxSerialSize != null && maxSerialSize > 0){
            sMax = (minSerialSize != null && minSerialSize > 0 ?  hmAux_Trans.get("serial_min_max_separator_lbl") : "") + hmAux_Trans.get("serial_max_length_lbl") + maxSerialSize;
        }
        //
        if(!sMin.isEmpty() || !sMax.isEmpty() ){
            sHelper += "(" +sMin + sMax +")";
        }
        //
        return sHelper;
    }

    private boolean isSerialOptional() {
        return auxSchedule.get(MD_Schedule_ExecDao.REQUIRE_SERIAL).equalsIgnoreCase("0")
                && auxSchedule.get(MD_Schedule_ExecDao.REQUIRE_SERIAL_DONE).equalsIgnoreCase("0");
    }

    private void setConfig() {
        rdoYes.setChecked(true);
        //
        if( auxSchedule.get(MD_Schedule_ExecDao.REQUIRE_SERIAL).equalsIgnoreCase("1")
            || auxSchedule.get(MD_Schedule_ExecDao.REQUIRE_SERIAL_DONE).equalsIgnoreCase("1")
        ){
            tvQuestion.setText(hmAux_Trans.get("serial_required_lbl"));
            rgConfirm.setVisibility(View.GONE);
            rdoNo.setEnabled(false);
            mketSerial.setHint(hmAux_Trans.get("inform_serial_required"));
        }
    }

    private void applyOptionUI(int checkedId) {
        Drawable searchIcon = null;
        String btnText = "";
        if(checkedId == R.id.schedule_request_serial_dialog_rdo_no){
            searchIcon = null;
            mketSerial.setVisibility(View.GONE);
            btnText =  hmAux_Trans.get("btn_open_form");
            tilSerial.setErrorEnabled(false);
            tilSerial.setError(null);
            tilSerial.setHelperText(null);
        } else{
            searchIcon =  context.getResources().getDrawable(R.drawable.icon_lupa_ns);
            searchIcon.setColorFilter(context.getResources().getColor(R.color.padrao_WHITE), PorterDuff.Mode.SRC_ATOP);
            mketSerial.setVisibility(View.VISIBLE);
            btnText =  hmAux_Trans.get("btn_search_serial");
            tilSerial.setHelperText(getFormattedRuleHelper());
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
            return  ToolBox_Inf.removeAllLineBreaks(mketSerial.getText().toString()) ;
        }
        //
        return "";
    }
}
