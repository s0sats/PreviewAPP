package com.namoadigital.prj001.ui.act070.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.DecimalFormat;

public class TK_Ticket_Ctrl_Measure_V extends TK_Ticket_Ctrl_Super {

    private TextView tvMeasureDesc;
    private TextView tvMeasureValueLbl;
    private TextView tvMeasureValueVal;
    private TextView tvMeasureDateLbl;
    private TextView tvMeasureDateVal;
    private TextView tvMeasureInfo;
    private TK_Ticket_Ctrl_Measure_I delegate;

    public interface TK_Ticket_Ctrl_Measure_I {
        boolean checkPartnerProfile(Integer partnerCode);
    }

    public TK_Ticket_Ctrl_Measure_V(
                                    Context context,
                                    int ticketProductCode,
                                    int ticketSerialCode,
                                    TK_Ticket_Ctrl mTicketCtrl,
                                    HMAux HmAuxTrans,
                                    TK_Ticket_Ctrl_Measure_I delegate
                                    )
    {
        super(context, ticketProductCode, ticketSerialCode, mTicketCtrl, HmAuxTrans, null);
        this.delegate = delegate;
        //
        initialize();
    }

    public void setDelegate(TK_Ticket_Ctrl_Measure_I delegate) {
        this.delegate = delegate;
    }

    private void initialize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.act070_measure_cell, this);
        //
        bindViews();
        //
        bindData();
        //
        handleFieldsVisibility();
    }

    private void bindViews() {
        //Super Views
        cvRoot = findViewById(R.id.act070_measure_cell_cv_root);
        tvType = findViewById(R.id.act070_measure_cell_tv_ctrl_type);
        tvSeq = findViewById(R.id.act070_measure_cell_tv_seq);
        tvStatus = findViewById(R.id.act070_measure_cell_tv_status);
        tvProducDesc = findViewById(R.id.act070_measure_cell_tv_product);
        tvSerialId = findViewById(R.id.act070_measure_cell_tv_serial);
        //ivAction = findViewById(R.id.act070_measure_cell_iv_action);
        //
        tvMeasureDesc = findViewById(R.id.act070_measure_cell_tv_measure_desc);
        tvMeasureValueLbl = findViewById(R.id.act070_measure_cell_tv_measure_value_lbl);
        tvMeasureValueVal = findViewById(R.id.act070_measure_cell_tv_measure_value_val);
        tvMeasureDateLbl = findViewById(R.id.act070_measure_cell_tv_measure_dt_lbl);
        tvMeasureDateVal = findViewById(R.id.act070_measure_cell_tv_measure_dt_val);
        tvMeasureInfo = findViewById(R.id.act070_measure_cell_tv_measure_info);
    }

    private void bindData() {
        defineType();
        tvSeq.setText(String.valueOf(getmSeq()));
        tvStatus.setText(hmAuxTrans.get(getmStatus()));
        tvStatus.setTextColor(ToolBox_Inf.getStatusColorV2(context,mTicketCtrl.getCtrl_status()));
        tvProducDesc.setText(getmProductDesc());
        tvSerialId.setText(getmSerialID());
        //
        tvMeasureDesc.setText(getFormattedDesc(getmTicketCtrl()));
        tvMeasureValueLbl.setText(hmAuxTrans.get("measure_value_lbl"));
        tvMeasureValueVal.setText(getFormattedValue(getmTicketCtrl().getMeasure().getMeasure_value()));
        tvMeasureDateLbl.setText(hmAuxTrans.get("measure_date_lbl"));
        tvMeasureDateVal.setText(
            ToolBox_Inf.millisecondsToString(
            ToolBox_Inf.dateToMilliseconds(getmTicketCtrl().getMeasure().getMeasure_date()),
            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        ));
        //
        tvMeasureInfo.setText(getmTicketCtrl().getMeasure().getMeasure_info());
    }

    private void defineType() {
        tvType.setText(hmAuxTrans.get(mTicketCtrl.getCtrl_type()));
        tvType.setTextColor(getTypeColor(mTicketCtrl.getCtrl_type()));
    }

    /**
     * Formata o valor para "inteiro".
     * Como por hora só existe medição int, a exibição será dessa maneira.
     * @param measure_value
     * @return
     */
    private String getFormattedValue(float measure_value) {
        //return (new DecimalFormat("###0.00").format(measure_value)).replace(",", ".");
        return (new DecimalFormat("###0").format(measure_value));
    }

    private String getFormattedDesc(TK_Ticket_Ctrl ctrl) {
        String finalDesc = "";
        finalDesc += ctrl.getMeasure().getMeasure_tp_id();
        if(
            ctrl.getMeasure().getMeasure_tp_id() != null && !ctrl.getMeasure().getMeasure_tp_id().isEmpty()
            && ctrl.getMeasure().getMeasure_tp_desc() != null && !ctrl.getMeasure().getMeasure_tp_desc().isEmpty()
        ) {
            finalDesc += " - ";
        }
        finalDesc += ctrl.getMeasure().getMeasure_tp_desc();
        //
        return finalDesc;
    }

    private void handleFieldsVisibility() {
        if(getmTicketCtrl().getMeasure().getMeasure_info() != null && !getmTicketCtrl().getMeasure().getMeasure_info().isEmpty()){
            tvMeasureInfo.setVisibility(VISIBLE);
        }else{
            tvMeasureInfo.setVisibility(GONE);
        }
    }

    public String getmComment() {
        return mTicketCtrl.getAction().getAction_comments();
    }

    public Integer getmPartnerCode() {
        return mTicketCtrl.getPartner_code();
    }

    public String getmPartnerDesc() {
        return mTicketCtrl.getPartner_desc();
    }

    public int getmMeasureCode(){
        return mTicketCtrl.getMeasure().getMeasure_tp_code();
    }

    @Override
    public void applyFilterVisibility() {
        setVisible(true);
    }
}
