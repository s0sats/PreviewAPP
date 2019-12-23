package com.namoadigital.prj001.ui.act070.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class TK_Ticket_Ctrl_Action_V extends TK_Ticket_Ctrl_Super {

    private TextView tvComment;
    private TextView tvPartnerLbl;
    private TextView tvPartnerVal;
    private ImageView ivPhoto;
    private TK_Ticket_Ctrl_Action_I delegate;

    public interface TK_Ticket_Ctrl_Action_I{
        boolean checkPartnerProfile(Integer partnerCode);
    }

    public TK_Ticket_Ctrl_Action_V(Context context,int ticketProductCode, int ticketSerialCode,TK_Ticket_Ctrl mTicketCtrl,HMAux HmAuxTrans, OnClickListener ivActionClickListener,TK_Ticket_Ctrl_Action_I delegate) {
        super(context,ticketProductCode,ticketSerialCode,mTicketCtrl,HmAuxTrans,ivActionClickListener);
        this.delegate = delegate;
        //
        initialize();
    }

    public void setDelegate(TK_Ticket_Ctrl_Action_I delegate) {
        this.delegate = delegate;
    }

    private void initialize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.act070_action_cell, this);
        //
        bindViews();
        //
        bindData();
        //
        handleFieldsVisibility();
    }

    private void bindViews() {
        //Super Views
        cvRoot = findViewById(R.id.act070_action_cell_cv_root);
        tvType = findViewById(R.id.act070_measure_cell_tv_ctrl_type);
        tvSeq = findViewById(R.id.act070_action_cell_tv_seq);
        tvStatus = findViewById(R.id.act070_action_cell_tv_status);
        tvProducDesc = findViewById(R.id.act070_action_cell_tv_product);
        tvSerialId = findViewById(R.id.act070_action_cell_tv_serial);
        ivAction = findViewById(R.id.act070_action_cell_iv_action);
        //
        tvComment = findViewById(R.id.act070_action_cell_tv_comment);
        tvPartnerLbl = findViewById(R.id.act070_action_cell_tv_partner_lbl);
        tvPartnerVal = findViewById(R.id.act070_action_cell_tv_partner_val);
        ivPhoto = findViewById(R.id.act070_action_cell_iv_photo);
    }

    private void bindData() {
        defineType();
        tvSeq.setText(String.valueOf(getmSeq()));
        tvStatus.setText(hmAuxTrans.get(getmStatus()));
        tvStatus.setTextColor(getResources().getColor(ToolBox_Inf.getStatusColor(mTicketCtrl.getCtrl_status())));
        tvProducDesc.setText(getmProductDesc());
        tvSerialId.setText(getmSerialID());
        //
        tvComment.setText(mTicketCtrl.getAction().getAction_comments());
        tvPartnerLbl.setText(hmAuxTrans.get("partner_lbl"));
        tvPartnerVal.setText(mTicketCtrl.getPartner_desc());
        setIvPhotoState();
    }

    private void defineType() {
        tvType.setText(hmAuxTrans.get(mTicketCtrl.getCtrl_type()));
        tvType.setTextColor(getTypeColor(mTicketCtrl.getCtrl_type()));
    }

    private void handleFieldsVisibility() {
        if(mTicketCtrl.getAction().getAction_comments() != null && !mTicketCtrl.getAction().getAction_comments().isEmpty()) {
            tvComment.setVisibility(VISIBLE);
        }else{
            tvComment.setVisibility(GONE);
        }
        //
        if(mTicketCtrl.getPartner_desc() != null &&  !mTicketCtrl.getPartner_desc().isEmpty()){
            tvPartnerLbl.setVisibility(VISIBLE);
            tvPartnerVal.setVisibility(VISIBLE);
        }else{
            tvPartnerLbl.setVisibility(GONE);
            tvPartnerVal.setVisibility(GONE);
        }
    }


    private void setIvPhotoState() {
        if( mTicketCtrl.getAction() != null
            && swithOnPhotoIcon()
        ){
            ivPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_on));
        }else{
            ivPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_off));
        }
    }

    private boolean swithOnPhotoIcon() {
        return mTicketCtrl.getAction().getAction_photo_local() != null
               ||( mTicketCtrl.getAction().getAction_photo_name() != null && mTicketCtrl.getAction().getAction_photo() == null)
               || (mTicketCtrl.getAction().getAction_photo() != null && mTicketCtrl.getAction().getAction_photo_changed() == 0);

    }

    public String getmComment() {
        return mTicketCtrl.getAction().getAction_comments();
    }

    public Integer getmPartnerCode(){
        return mTicketCtrl.getPartner_code();
    }
    public String getmPartnerDesc() {
        return mTicketCtrl.getPartner_desc();
    }

    @Override
    public void applyFilterVisibility() {
        if(delegate != null){
            setVisible(
                !ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(getmStatus())
                && !ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equalsIgnoreCase(getmStatus())
                && delegate.checkPartnerProfile(getmPartnerCode())
            );
        }else{
            setVisible(
                !ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(getmStatus())
                && !ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equalsIgnoreCase(getmStatus())
            );
        }
    }
}
