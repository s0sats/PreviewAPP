package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepApproval;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_ApprovalVH extends Act070_Step_Abstract_ProcessVH {
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
    private ConstraintLayout clRejectionBackground;
    private View vRejectionDivider;
    private ImageView ivRejection;
    private TextView tvRejection;
    private String  transReviewRejection;
    private String  transApprovedStatus;
    private Act070_Steps_Adapter.OnApprovalClickListener onApprovalClick;


    public Act070_Step_ApprovalVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnApprovalClickListener onApprovalClick, String transStartProcess, String transReviewProcess, String transContinueProcess, String transWaitingSync, String transReviewRejection, String transApprovedStatus, boolean isInWgEditMode, boolean inReadOnlyMode) {
        super(context,itemView,transStartProcess,transReviewProcess,transContinueProcess,transWaitingSync,isInWgEditMode,inReadOnlyMode);
        this.onApprovalClick = onApprovalClick;
        this.transReviewRejection = transReviewRejection;
        this.transApprovedStatus = transApprovedStatus;
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
        clRejectionBackground = this.itemView.findViewById(R.id.step_approval_cl_rejection_background);
        vRejectionDivider =  this.itemView.findViewById(R.id.step_approval_v_rejection_divider);
        ivRejection =  this.itemView.findViewById(R.id.step_approval_iv_rejection);
        tvRejection =  this.itemView.findViewById(R.id.step_approval_tv_rejection);
        //
        clBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onApprovalClick != null){
                    onApprovalClick.onApprovalClick(getAdapterPosition());
                }
            }
        });
        //
        clRejectionBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onApprovalClick != null){
                    onApprovalClick.onShowRejectionClick(getAdapterPosition());
                }
            }
        });
    }

    public void bindData(StepApproval stepApproval){
        applyProcessReadOnlyDueFocusAndClaim(stepApproval.isUserFocus(), stepApproval.getProcessStatus(), stepApproval.getStartUserCode());
        //
        resetVisibility();
        //
        tvApprovalQuestion.setText(stepApproval.getApprovalQuestion());
        if( ToolBox_Inf.hasConsistentValueString(stepApproval.getApprovalStatus())
            && ConstantBaseApp.SYS_STATUS_DONE.equals(stepApproval.getApprovalStatus())
        ) {
            tvApprovalStatus.setVisibility(View.VISIBLE);
            tvApprovalStatus.setText(transApprovedStatus);
            tvApprovalStatus.setTextColor(ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_DONE));
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
            defineCheckInOutIcon(ivStartEndDateIcon,ToolBox_Inf.hasConsistentValueString(stepApproval.getEndDate()));
            ivStartEndDateIcon.setVisibility(View.VISIBLE);
            tvStartEndDate.setVisibility(View.VISIBLE);
            tvStartEndDate.setText(
                ToolBox_Inf.getStepStartEndDateFormated(context,stepApproval.getStartDate(),stepApproval.getEndDate())
            );
        }
        //
        applyHistoryLayout(
            stepApproval.isCurrentStep(),
            tvApprovalQuestion,
            tvApprovalComment,
            tvPartner,
            tvUser,
            tvStartEndDate
        );
        //
        applyHighlightBackground(
            clBackground,
            stepApproval.getProcessStatus(),
            stepApproval.isCurrentStep(),
            stepApproval.getStepType(),
            stepApproval.isStepAlreadyCheckedIn()
        );
        //
        configProcessAction(
            ivProcessAction,
            tvProcessAction,
            stepApproval.getProcessStatus(),
            stepApproval.getStepType(),
            stepApproval.isCurrentStep(),
            stepApproval.isStepAlreadyCheckedIn()
        );
        //
        showRejectedAction(stepApproval.hasRejection());
    }

    private void showRejectedAction(boolean hasRejection) {
        if(hasRejection){
            clRejectionBackground.setVisibility(View.VISIBLE);
            tvRejection.setText(transReviewRejection);
        }
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
        clRejectionBackground.setVisibility(View.GONE);
    }
}
