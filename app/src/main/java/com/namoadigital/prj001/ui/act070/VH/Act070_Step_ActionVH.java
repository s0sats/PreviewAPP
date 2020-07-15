package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
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
    private ImageView ivStartDateIcon;
    private TextView tvStartDate;
    private ImageView ivEndDateIcon;
    private TextView tvEndDate;
    private ImageView ivUserIcon;
    private TextView tvUser;
    private View vDivider;
    private ImageView ivPartner;
    private TextView tvPartner;
    private ImageView ivProcessAction;
    private TextView tvProcessAction;

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
        ivStartDateIcon =  this.itemView.findViewById(R.id.step_action_iv_start_date);
        tvStartDate =  this.itemView.findViewById(R.id.step_action_tv_start_date);
        ivEndDateIcon =  this.itemView.findViewById(R.id.step_action_iv_end_date);
        tvEndDate =  this.itemView.findViewById(R.id.step_action_tv_end_date);
        ivUserIcon =  this.itemView.findViewById(R.id.step_action_iv_user);
        tvUser =  this.itemView.findViewById(R.id.step_action_tv_user);
        vDivider =  this.itemView.findViewById(R.id.step_action_v_divider);
        ivPartner =  this.itemView.findViewById(R.id.step_action_iv_partner);
        tvPartner =  this.itemView.findViewById(R.id.step_action_tv_partner);
        ivProcessAction =  this.itemView.findViewById(R.id.step_action_iv_process_action);
        tvProcessAction =  this.itemView.findViewById(R.id.step_action_tv_process_action);
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
        //Sem necessidade de chamar o hasConsistentValueString, pois já é chamado internamento
        // no metodo equalsToLoggedSite
        if(ToolBox_Inf.equalsToLoggedSite(context,stepAction.getSiteDesc())) {
            tvSite.setVisibility(View.VISIBLE);
            tvSite.setText(stepAction.getSiteDesc());
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
        if(ToolBox_Inf.hasConsistentValueString(stepAction.getPartnerDesc())) {
            ivPartner.setVisibility(View.VISIBLE);
            tvPartner.setVisibility(View.VISIBLE);
            tvPartner.setText(stepAction.getPartnerDesc());
        }
        //
        applyHistoryLayout(stepAction);
        applyHighlightBackground(stepAction.isCurrentStep());
        configProcessAction(stepAction);
    }




    private void configProcessAction(StepAction stepAction) {
        if(stepAction.isCurrentStep()){
            int tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PROCESS);
            Drawable drawable = null;
            switch (stepAction.getProcessStatus()){
                case ConstantBaseApp.SYS_STATUS_PENDING:
                case ConstantBaseApp.SYS_STATUS_PROCESS:
                    drawable = context.getDrawable(R.drawable.ic_baseline_play_arrow_24dp);
                    break;
                case ConstantBaseApp.SYS_STATUS_DONE:
                    drawable = context.getDrawable(R.drawable.ic_baseline_open_in_new_24dp);
                    tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PENDING);
                    break;
            }
            //
            if(drawable != null){
                drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
            }
            ivProcessAction.setImageDrawable(drawable);
            tvProcessAction.setTextColor(tintColor);
            ivProcessAction.setVisibility(View.VISIBLE);
            tvProcessAction.setVisibility(View.VISIBLE);
        }
    }

    private void applyHighlightBackground(boolean currentStep) {
        clBackground.setBackgroundColor(
            context.getResources().getColor(
            currentStep
                ? R.color.namoa_color_ticket_process_highlight
                : R.color.padrao_TRANSPARENT
            )
        );
        //
        if(currentStep){
            vStepContinousLine.setVisibility(View.GONE);
            ivStepDashedLine.setVisibility(View.VISIBLE);
        }else{
            vStepContinousLine.setVisibility(View.VISIBLE);
            ivStepDashedLine.setVisibility(View.GONE);
        }
    }

    private void applyHistoryLayout(StepAction stepAction) {
        int fontColor = R.color.namoa_color_dark_blue;
        //
        if(!ConstantBaseApp.SYS_STATUS_PENDING.equals(stepAction.getProcessStatus())){
            fontColor = R.color.namoa_color_gray_4;
        }
        tvActionDesc.setTextColor(ContextCompat.getColor(context, fontColor));
        tvProduct.setTextColor(ContextCompat.getColor(context, fontColor));
        tvSerial.setTextColor(ContextCompat.getColor(context, fontColor));
        tvStartDate.setTextColor(ContextCompat.getColor(context, fontColor));
        tvEndDate.setTextColor(ContextCompat.getColor(context, fontColor));
        tvUser.setTextColor(ContextCompat.getColor(context, fontColor));
        tvPartner.setTextColor(ContextCompat.getColor(context, fontColor));
    }

    private void resetVisibility() {
        tvProduct.setVisibility(View.GONE);
        tvSerial.setVisibility(View.GONE);
        tvSite.setVisibility(View.GONE);
        ivEndDateIcon.setVisibility(View.GONE);
        tvEndDate.setVisibility(View.GONE);
        ivUserIcon.setVisibility(View.GONE);
        tvUser.setVisibility(View.GONE);
        ivPartner.setVisibility(View.GONE);
        tvPartner.setVisibility(View.GONE);
        ivProcessAction.setVisibility(View.GONE);
        tvProcessAction.setVisibility(View.GONE);
    }

}
