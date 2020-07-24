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
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepApproval;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_ApprovalVH extends RecyclerView.ViewHolder {
    private Context context;
    private View vStepContinousLine;
    private ImageView ivStepDashedLine;
    private ConstraintLayout clBackground;
    private TextView tvApprovalQuestion;
    private TextView tvApprovalComment;
    private TextView tvApprovalStatus;
    private ImageView ivPartner;
    private TextView tvPartner;
    private ImageView ivUserIcon;
    private TextView tvUser;
    private ImageView ivStartEndDateIcon;
    private TextView tvStartEndDate;
    private View vDivider;
    private ImageView ivProcessAction;
    private TextView tvProcessAction;
    private Act070_Steps_Adapter.OnApprovalClickListener onApprovalClick;


    public Act070_Step_ApprovalVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnApprovalClickListener onApprovalClick) {
        super(itemView);
        this.context = context;
        this.onApprovalClick = onApprovalClick;
        bindViews();
    }

    private void bindViews() {
        vStepContinousLine =  this.itemView.findViewById(R.id.step_approval_v_line);
        ivStepDashedLine =  this.itemView.findViewById(R.id.step_approval_iv_line);
        clBackground =  this.itemView.findViewById(R.id.step_approval_cl_background);
        tvApprovalQuestion =  this.itemView.findViewById(R.id.step_approval_tv_question);
        tvApprovalStatus =  this.itemView.findViewById(R.id.step_approval_tv_status);
        tvApprovalComment =  this.itemView.findViewById(R.id.step_approval_tv_comments);
        ivPartner =  this.itemView.findViewById(R.id.step_approval_iv_partner);
        tvPartner =  this.itemView.findViewById(R.id.step_approval_tv_partner);
        ivUserIcon =  this.itemView.findViewById(R.id.step_approval_iv_user);
        tvUser =  this.itemView.findViewById(R.id.step_approval_tv_user);
        ivStartEndDateIcon =  this.itemView.findViewById(R.id.step_approval_iv_end_date);
        tvStartEndDate =  this.itemView.findViewById(R.id.step_approval_tv_end_date);
        vDivider =  this.itemView.findViewById(R.id.step_approval_v_divider);
        ivProcessAction =  this.itemView.findViewById(R.id.step_approval_iv_process_action);
        tvProcessAction =  this.itemView.findViewById(R.id.step_approval_tv_process_action);
        clBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onApprovalClick != null){
                    onApprovalClick.onApprovalClick(getAdapterPosition());
                }
            }
        });
    }

    public void bindData(StepApproval stepApproval){
        resetVisibility();
        //
        tvApprovalQuestion.setText(stepApproval.getStepDescription());
        if(ToolBox_Inf.hasConsistentValueString(stepApproval.getApprovalStatus())) {
            tvApprovalStatus.setVisibility(View.VISIBLE);
            tvApprovalStatus.setText(stepApproval.getApprovalStatus());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepApproval.getApprovalComment())) {
            tvApprovalComment.setVisibility(View.VISIBLE);
            tvApprovalComment.setText(stepApproval.getApprovalComment());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepApproval.getPartnerDesc())) {
            ivPartner.setVisibility(View.VISIBLE);
            tvPartner.setVisibility(View.VISIBLE);
            tvPartner.setText(stepApproval.getPartnerDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepApproval.getEndUser())) {
            ivUserIcon.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.VISIBLE);
            tvUser.setText(stepApproval.getEndUser());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepApproval.getStartDate())) {
            defineCheckInOutIcon(ToolBox_Inf.hasConsistentValueString(stepApproval.getEndDate()));
            ivStartEndDateIcon.setVisibility(View.VISIBLE);
            tvStartEndDate.setVisibility(View.VISIBLE);
            tvStartEndDate.setText(
                ToolBox_Inf.getStepStartEndDateFormated(context,stepApproval.getStartDate(),stepApproval.getEndDate())
            );
        }
        //
        applyHistoryLayout(stepApproval);
        applyHighlightBackground(stepApproval);
        configProcessAction(stepApproval);
    }

    private void defineCheckInOutIcon(boolean hasCheckOutDate) {
        int drawableId =
            hasCheckOutDate
                ? R.drawable.ic_check_white_24dp
                : R.drawable.ic_baseline_input_24dp_black;
        Drawable drawable = context.getDrawable(drawableId);
        ivStartEndDateIcon.setImageDrawable(drawable);
    }

    private void configProcessAction(StepApproval stepApproval) {
        int tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PENDING);
        Drawable drawable = null;
        if(stepApproval.isCurrentStep()){
            if(ToolBox_Inf.hasConsistentValueString(stepApproval.getStartDate())){
                drawable = context.getDrawable(R.drawable.ic_baseline_play_arrow_24dp);
                tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PROCESS);
                if(ToolBox_Inf.hasConsistentValueString(stepApproval.getEndDate())){
                    drawable = context.getDrawable(R.drawable.ic_baseline_open_in_new_24dp);
                    tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PENDING);
                }
            }
            //
            if(drawable != null){
                drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
            }
            ivProcessAction.setImageDrawable(drawable);
            tvProcessAction.setTextColor(tintColor);
            ivProcessAction.setVisibility(View.VISIBLE);
            tvProcessAction.setVisibility(View.VISIBLE);
        }else{
            if(ToolBox_Inf.hasConsistentValueString(stepApproval.getStartDate()) && ToolBox_Inf.hasConsistentValueString(stepApproval.getEndDate()) ) {
                drawable = context.getDrawable(R.drawable.ic_baseline_open_in_new_24dp);
                drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
                ivProcessAction.setImageDrawable(drawable);
                tvProcessAction.setTextColor(tintColor);
                ivProcessAction.setVisibility(View.VISIBLE);
                tvProcessAction.setVisibility(View.VISIBLE);
            }
        }
    }

    private void applyHighlightBackground(StepApproval stepApproval) {
        int backgroundColor = R.color.padrao_TRANSPARENT;
        Drawable drawable = context.getDrawable(R.drawable.pipeline_step_states);
        //Se step atual, verifica o destaque
        if(stepApproval.isCurrentStep()) {
            //Se start_end, se tiver checkin, fica amarelo , se não fica cinza indicando que falta q
            //não é possivel mexer.
            if (ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(stepApproval.getStepType())) {
                backgroundColor =
                    ToolBox_Inf.hasConsistentValueString(stepApproval.getStartDate())
                        ? R.color.namoa_color_ticket_process_highlight
                        : R.color.namoa_color_pipeline_cur_step_no_checkin;
                //
                drawable =
                    ToolBox_Inf.hasConsistentValueString(stepApproval.getStartDate())
                        ? context.getDrawable(R.drawable.pipeline_step_highligh_states)
                        : context.getDrawable(R.drawable.pipeline_step_gray_states);
            } else {
                //Se ONE_TOUCH, fica amarelo.
                backgroundColor = R.color.namoa_color_ticket_process_highlight;
                drawable = context.getDrawable(R.drawable.pipeline_step_highligh_states);
            }
            //
        }
        clBackground.setBackground(drawable);
    }

    private void applyHistoryLayout(StepApproval stepApproval) {
        int fontColor = R.color.namoa_color_dark_blue;
        //
        if(!stepApproval.isCurrentStep()){
            fontColor = R.color.namoa_color_gray_4;
        }
        tvApprovalQuestion.setTextColor(ContextCompat.getColor(context, fontColor));
        tvApprovalStatus.setTextColor(ContextCompat.getColor(context, fontColor));
        tvApprovalComment.setTextColor(ContextCompat.getColor(context, fontColor));
        tvPartner.setTextColor(ContextCompat.getColor(context, fontColor));
        tvUser.setTextColor(ContextCompat.getColor(context, fontColor));
        tvStartEndDate.setTextColor(ContextCompat.getColor(context, fontColor));
    }

    private void resetVisibility() {
        //tvApprovalQuestion.setVisibility(View.GONE);
        tvApprovalStatus.setVisibility(View.GONE);
        tvApprovalComment.setVisibility(View.GONE);
        ivStartEndDateIcon.setVisibility(View.GONE);
        tvStartEndDate.setVisibility(View.GONE);
        ivUserIcon.setVisibility(View.GONE);
        tvUser.setVisibility(View.GONE);
        ivPartner.setVisibility(View.GONE);
        tvPartner.setVisibility(View.GONE);
        ivProcessAction.setVisibility(View.GONE);
        tvProcessAction.setVisibility(View.GONE);
    }

}
