package com.namoadigital.prj001.ui.act070.view;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.util.ConstantBaseApp;

public abstract class TK_Ticket_Ctrl_Super extends LinearLayout{

    protected transient Context context;
    protected CardView cvRoot;
    protected int ticketProductCode;
    protected int ticketSerialCode;
    protected TK_Ticket_Ctrl mTicketCtrl;
    protected HMAux hmAuxTrans;
    protected OnClickListener ivActionClickListener;
    protected TextView tvType;
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
        return mTicketCtrl.getCtrl_status();
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
        if(tvSerialId != null) {
            if (ticketProductCode != mTicketCtrl.getProduct_code()
                || ticketSerialCode != mTicketCtrl.getSerial_code()
            ) {
                tvSerialId.setVisibility(VISIBLE);
                tvSerialId.setTextColor(ContextCompat.getColor(context, R.color.namoa_color_danger_red));
            } else {
                tvSerialId.setVisibility(GONE);
            }
        }
    }

    private void highlightProductWhenDiff(){
        if(tvProducDesc != null) {
            if (ticketProductCode != mTicketCtrl.getProduct_code()) {
                tvProducDesc.setVisibility(VISIBLE);
                tvProducDesc.setTextColor(ContextCompat.getColor(context, R.color.namoa_color_danger_red));
            }else{
                tvProducDesc.setVisibility(GONE);
            }
        }
    }

    public void setVisible(boolean visible) {
        if(cvRoot != null) {
            cvRoot.setVisibility(visible ? VISIBLE : GONE);
        }
    }

    public abstract void applyFilterVisibility();

    protected int getTypeColor(String ctrl_type){
        int color = 0;
        switch (ctrl_type){
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                color = R.color.namoa_status_stop;
                break;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
            default:
                color = R.color.namoa_status_pending;
                break;
        }
       //
        return getResources().getColor(color);
    }



}
