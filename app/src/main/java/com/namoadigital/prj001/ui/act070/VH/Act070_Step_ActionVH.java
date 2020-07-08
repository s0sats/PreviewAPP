package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_ActionVH extends RecyclerView.ViewHolder {
    private Context context;
    private View vStepContinousLine;
    private ImageView ivStepDashedLine;
    private ConstraintLayout clBackground;
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

    public Act070_Step_ActionVH(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        bindViews();
    }

    private void bindViews() {
        vStepContinousLine =  this.itemView.findViewById(R.id.step_action_v_line);
        ivStepDashedLine =  this.itemView.findViewById(R.id.step_action_iv_line);
        clBackground =  this.itemView.findViewById(R.id.step_action_cl_background);
        tvActionDesc =  this.itemView.findViewById(R.id.step_action_tv_desc);
        tvProduct =  this.itemView.findViewById(R.id.step_action_tv_prod);
        tvSerial =  this.itemView.findViewById(R.id.step_action_tv_serial);
        tvSite =  this.itemView.findViewById(R.id.step_action_tv_site);
        tvOperation =  this.itemView.findViewById(R.id.step_action_tv_operation);
        ivStartDateIcon =  this.itemView.findViewById(R.id.step_action_iv_start_date);
        tvStartDate =  this.itemView.findViewById(R.id.step_action_tv_start_date);
        ivEndDateIcon =  this.itemView.findViewById(R.id.step_action_iv_end_date);
        tvEndDate =  this.itemView.findViewById(R.id.step_action_tv_end_date);
        ivUserIcon =  this.itemView.findViewById(R.id.step_action_iv_user);
        tvUser =  this.itemView.findViewById(R.id.step_action_tv_user);
        ivActionIcon =  this.itemView.findViewById(R.id.step_action_iv_process_icon);
        ivBtnAction =  this.itemView.findViewById(R.id.step_action_iv_action);
        vDivider =  this.itemView.findViewById(R.id.step_action_v_divider);
        ivWorkgroupIcon =  this.itemView.findViewById(R.id.step_action_iv_workgroup);
        tvWorkgroup =  this.itemView.findViewById(R.id.step_action_tv_workgroup);
        ivPartner =  this.itemView.findViewById(R.id.step_action_iv_partner);
        tvPartner =  this.itemView.findViewById(R.id.step_action_tv_partner);
    }

    public void bindData(StepAction stepAction){
        resetVisibility();
        //
        tvActionDesc.setText(stepAction.getStepDescription());
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getProductDesc())) {
            tvProduct.setVisibility(View.VISIBLE);
            tvProduct.setText(stepAction.getProductDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getSerialId())) {
            tvSerial.setVisibility(View.VISIBLE);
            tvSerial.setText(stepAction.getSerialId());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getSiteDesc())) {
            tvSite.setVisibility(View.VISIBLE);
            tvSite.setText(stepAction.getSiteDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getOperationDesc())) {
            tvOperation.setVisibility(View.VISIBLE);
            tvOperation.setText(stepAction.getOperationDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getStartDate())) {
            tvStartDate.setText(stepAction.getStartDate());
        }else{
            tvStartDate.setText("SEM PLANEJAMENTO -TRAD");
        }
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getEndDate())) {
            ivEndDateIcon.setVisibility(View.VISIBLE);
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(stepAction.getEndDate());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getEndUser())) {
            ivUserIcon.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.VISIBLE);
            tvUser.setText(stepAction.getEndUser());
        }
        /*if(ToolBox_Inf.hasConsistentValueString(stepAction.getWorkgroupDesc())) {
            ivWorkgroupIcon.setVisibility(View.VISIBLE);
            tvWorkgroup.setVisibility(View.VISIBLE);
            tvWorkgroup.setText(stepAction.getWorkgroupDesc());
        }*/
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getPartnerDesc())) {
            ivPartner.setVisibility(View.VISIBLE);
            tvPartner.setVisibility(View.VISIBLE);
            tvPartner.setText(stepAction.getPartnerDesc());
        }
        //
        applyHistoryLayout(stepAction);
        applyHighlightBackground(stepAction.isCurrentStep());
    }

    private void applyHighlightBackground(boolean currentStep) {
        clBackground.setBackgroundColor(
            context.getResources().getColor(
            currentStep
                ? R.color.namoa_color_ticket_process_highlight
                : R.color.padrao_TRANSPARENT
            )
        );
    }

    private void applyHistoryLayout(StepAction stepAction) {
        int fontColor = R.color.namoa_color_dark_blue;
        int processIcon = R.drawable.ic_edit_black_24dp;
        //
        if(!ConstantBaseApp.SYS_STATUS_PENDING.equals(stepAction.getProcessStatus())){
            fontColor = R.color.namoa_color_gray3;
            processIcon = R.drawable.ic_baseline_history_24_black;
        }
        tvProduct.setTextColor(fontColor);
        tvSerial.setTextColor(fontColor);
        tvSite.setTextColor(fontColor);
        tvOperation.setTextColor(fontColor);
        tvEndDate.setTextColor(fontColor);
        tvUser.setTextColor(fontColor);
        tvWorkgroup.setTextColor(fontColor);
        tvPartner.setTextColor(fontColor);
        Drawable drawable =  context.getDrawable(processIcon);
        drawable.setColorFilter(context.getResources().getColor(R.color.namoa_color_gray3), PorterDuff.Mode.SRC_ATOP);
        ivActionIcon.setImageDrawable(drawable);
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
        ivPartner.setVisibility(View.GONE);
        tvPartner.setVisibility(View.GONE);
    }

}
