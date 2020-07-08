package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act070.model.StepChecklist;
import com.namoadigital.prj001.util.ToolBox_Inf;


public class Act070_Step_ChecklistVH extends RecyclerView.ViewHolder {

    private Context context;
    private View vStepContinousLine;
    private ImageView ivStepDashedLine;
    private TextView tvActionDesc;
    private TextView tvProduct;
    private TextView tvSerial;
    private TextView tvSite;
    private TextView tvOperation;
    private ImageView ivStartDateIcon;
    private TextView tvStartDate;
    private ImageView ivEndDateIcon;
    private TextView tvEndDate;
    private ImageView ivUserIcon;
    private TextView tvUser;
    private ImageView ivActionIcon;
    private ImageView ivBtnAction;
    private View vDivider;
    private ImageView ivWorkgroupIcon;
    private TextView tvWorkgroup;
    private ImageView ivPartner;
    private TextView tvPartner;

    public Act070_Step_ChecklistVH(Context context,@NonNull View itemView) {
        super(itemView);
        this.context = context;
        bindViews();
    }

    private void bindViews() {
        vStepContinousLine =  this.itemView.findViewById(R.id.step_checklist_v_line);
        ivStepDashedLine =  this.itemView.findViewById(R.id.step_checklist_iv_line);
        tvActionDesc =  this.itemView.findViewById(R.id.step_checklist_tv_desc);
        tvProduct =  this.itemView.findViewById(R.id.step_checklist_tv_prod);
        tvSerial =  this.itemView.findViewById(R.id.step_checklist_tv_serial);
        tvSite =  this.itemView.findViewById(R.id.step_checklist_tv_site);
        tvOperation =  this.itemView.findViewById(R.id.step_checklist_tv_operation);
        ivStartDateIcon =  this.itemView.findViewById(R.id.step_checklist_iv_start_date);
        tvStartDate =  this.itemView.findViewById(R.id.step_checklist_tv_start_date);
        ivEndDateIcon =  this.itemView.findViewById(R.id.step_checklist_iv_end_date);
        tvEndDate =  this.itemView.findViewById(R.id.step_checklist_tv_end_date);
        ivUserIcon =  this.itemView.findViewById(R.id.step_checklist_iv_user);
        tvUser =  this.itemView.findViewById(R.id.step_checklist_tv_user);
        ivActionIcon =  this.itemView.findViewById(R.id.step_checklist_iv_process_icon);
        ivBtnAction =  this.itemView.findViewById(R.id.step_checklist_iv_action);
        vDivider =  this.itemView.findViewById(R.id.step_checklist_v_divider);
        ivWorkgroupIcon =  this.itemView.findViewById(R.id.step_checklist_iv_workgroup);
        tvWorkgroup =  this.itemView.findViewById(R.id.step_checklist_tv_workgroup);
        ivPartner =  this.itemView.findViewById(R.id.step_action_iv_partner);
        tvPartner =  this.itemView.findViewById(R.id.step_checklist_tv_partner);
    }

    public void bindData(StepChecklist stepChecklist){
        resetVisibility();
        //
        tvActionDesc.setText(stepChecklist.getStepDescription());
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getProductDesc())) {
            tvProduct.setVisibility(View.VISIBLE);
            tvProduct.setText(stepChecklist.getProductDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getSerialId())) {
            tvSerial.setVisibility(View.VISIBLE);
            tvSerial.setText(stepChecklist.getSerialId());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getSiteDesc())) {
            tvSite.setVisibility(View.VISIBLE);
            tvSite.setText(stepChecklist.getSiteDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getOperationDesc())) {
            tvOperation.setVisibility(View.VISIBLE);
            tvOperation.setText(stepChecklist.getOperationDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getStartDate())) {
            tvStartDate.setText(stepChecklist.getStartDate());
        }else{
            tvStartDate.setText("SEM PLANEJAMENTO -TRAD");
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getEndDate())) {
            ivEndDateIcon.setVisibility(View.VISIBLE);
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(stepChecklist.getEndDate());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getEndUser())) {
            ivUserIcon.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.VISIBLE);
            tvUser.setText(stepChecklist.getEndUser());
        }
        /*if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getWorkgroupDesc())) {
            ivWorkgroupIcon.setVisibility(View.VISIBLE);
            tvWorkgroup.setVisibility(View.VISIBLE);
            tvWorkgroup.setText(stepChecklist.getWorkgroupDesc());
        }*/
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getPartnerDesc())) {
            ivPartner.setVisibility(View.VISIBLE);
            tvPartner.setVisibility(View.VISIBLE);
            tvPartner.setText(stepChecklist.getPartnerDesc());
        }
    }

    private void resetVisibility() {
        tvProduct.setVisibility(View.GONE);
        tvSerial.setVisibility(View.GONE);
        tvSite.setVisibility(View.GONE);
        tvOperation.setVisibility(View.GONE);
        ivEndDateIcon.setVisibility(View.GONE);
        tvEndDate.setVisibility(View.GONE);
        ivUserIcon.setVisibility(View.GONE);
        tvUser.setVisibility(View.GONE);
        ivWorkgroupIcon.setVisibility(View.GONE);
        tvWorkgroup.setVisibility(View.GONE);
        ivPartner.setVisibility(View.VISIBLE);
        tvPartner.setVisibility(View.GONE);
    }
}
