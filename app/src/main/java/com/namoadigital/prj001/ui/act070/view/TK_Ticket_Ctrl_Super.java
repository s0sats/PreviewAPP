package com.namoadigital.prj001.ui.act070.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;

public class TK_Ticket_Ctrl_Super extends LinearLayout{

    protected transient Context context;
    protected int ticketProductCode;
    protected int ticketSerialCode;
    protected TK_Ticket_Ctrl mTicketCtrl;
    protected HMAux hmAuxTrans;
    protected OnClickListener ivActionClickListener;
    protected TextView tvSeq;
    protected TextView tvStatus;
    protected TextView tvProducDesc;
    protected TextView tvSerialId;
    protected ImageView ivAction;
    protected boolean mEnabled;

    public TK_Ticket_Ctrl_Super(Context context,int ticketProductCode, int ticketSerialCode,TK_Ticket_Ctrl mTicketCtrl,HMAux HmAuxTrans, OnClickListener ivActionClickListener) {
        super(context);
        //
        this.context = context;
        this.ticketProductCode = ticketProductCode;
        this.ticketSerialCode = ticketSerialCode;
        this.mTicketCtrl = mTicketCtrl;
        this.hmAuxTrans = HmAuxTrans;
        this.ivActionClickListener = ivActionClickListener;
        //
        initialize();
    }

    private void initialize() {
        setDefaultValues();
    }

    private void setDefaultValues() {
        if(mTicketCtrl == null) {
            mTicketCtrl = new TK_Ticket_Ctrl();
        }
        //
        this.mEnabled = true;
    }

    public TK_Ticket_Ctrl getmTicketCtrl() {
        return mTicketCtrl;
    }

    public int getTicketPrefix(){
        return mTicketCtrl.getTicket_prefix();
    }

    public int getTicketCode(){
        return mTicketCtrl.getTicket_code();
    }

    public int getmSeq() {
        return mTicketCtrl.getTicket_seq();
    }

    public String getmStatus() {
        return hmAuxTrans.get(mTicketCtrl.getCtrl_status());
    }

    public String getmProductDesc() {
        return mTicketCtrl.getProduct_desc();
    }

    public String getmSerialID() {
        return mTicketCtrl.getSerial_id();
    }

    public boolean ismEnabled() {
        return mEnabled;
    }

    public void setmEnabled(boolean mEnabled) {
        this.mEnabled = mEnabled;
        //
        configViewToAttach();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //
        configViewToAttach();

    }

    private void configViewToAttach() {
        if(ivAction != null){
            ivAction.setOnClickListener(ivActionClickListener);
            ivAction.setEnabled(mEnabled);
        }
        //
        highlightProductWhenDiff();
        highlightSerialWhenDiff();
    }

    private void highlightSerialWhenDiff() {
        if( tvSerialId != null
            && (ticketProductCode != mTicketCtrl.getProduct_code()
                || ticketSerialCode != mTicketCtrl.getSerial_code()
            )
        ){
            tvSerialId.setTextColor(ContextCompat.getColor(context,R.color.namoa_color_danger_red));
        }
    }

    private void highlightProductWhenDiff(){
        if(tvProducDesc != null && ticketProductCode != mTicketCtrl.getProduct_code()){
            tvProducDesc.setTextColor(ContextCompat.getColor(context,R.color.namoa_color_danger_red));
        }
    }
}
