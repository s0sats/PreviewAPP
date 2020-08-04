package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_ActionVH extends Act070_Step_Abstract_ProcessVH {
    private View vStepContinousLine;
    private ImageView ivStepDashedLine;
    private ConstraintLayout clBackground;
    private TextView tvActionDesc;
    private TextView tvProduct;
    private TextView tvSerial;
    private TextView tvSite;
    private ImageView ivStartEndDateIcon;
    private TextView tvEndDate;
    private ImageView ivUserIcon;
    private TextView tvUser;
    private View vDivider;
    private ImageView ivPartner;
    private TextView tvPartner;
    private ImageView ivProcessAction;
    private TextView tvProcessAction;
    private Act070_Steps_Adapter.OnActionClickListener actionClickListener;

    public Act070_Step_ActionVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnActionClickListener actionClickListener, String transStartProcess, String transReviewProcess,String transWaitingSync) {
        super(context,itemView,transStartProcess,transReviewProcess,transWaitingSync);
        this.actionClickListener = actionClickListener;
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
        ivStartEndDateIcon =  this.itemView.findViewById(R.id.step_action_iv_end_date);
        tvEndDate =  this.itemView.findViewById(R.id.step_action_tv_end_date);
        ivUserIcon =  this.itemView.findViewById(R.id.step_action_iv_user);
        tvUser =  this.itemView.findViewById(R.id.step_action_tv_user);
        vDivider =  this.itemView.findViewById(R.id.step_action_v_divider);
        ivPartner =  this.itemView.findViewById(R.id.step_action_iv_partner);
        tvPartner =  this.itemView.findViewById(R.id.step_action_tv_partner);
        ivProcessAction =  this.itemView.findViewById(R.id.step_action_iv_process_action);
        tvProcessAction =  this.itemView.findViewById(R.id.step_action_tv_process_action);
        clBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actionClickListener != null){
                    actionClickListener.onActionClick(getAdapterPosition());
                }
            }
        });
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
            defineCheckInOutIcon(ivStartEndDateIcon,ToolBox_Inf.hasConsistentValueString(stepAction.getEndDate()));
            ivStartEndDateIcon.setVisibility(View.VISIBLE);
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(
                ToolBox_Inf.getStepStartEndDateFormated(context,stepAction.getStartDate(),stepAction.getEndDate())
            );
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
        applyHistoryLayout(
            stepAction.isCurrentStep(),
            tvActionDesc,
            tvProduct,
            tvSerial,
            tvEndDate,
            tvUser,
            tvPartner
        );
        applyHighlightBackground(
            clBackground,
            stepAction.getProcessStatus(),
            stepAction.isCurrentStep(),
            stepAction.getStepType(),
            stepAction.getStartDate()
        );
        configProcessAction(
            ivProcessAction,
            tvProcessAction,
            stepAction.getProcessStatus(),
            stepAction.getStepType(),
            stepAction.isCurrentStep(),
            stepAction.isStepAlreadyCheckedIn()
        );
    }

    private void resetVisibility() {
        tvProduct.setVisibility(View.GONE);
        tvSerial.setVisibility(View.GONE);
        tvSite.setVisibility(View.GONE);
        ivStartEndDateIcon.setVisibility(View.GONE);
        tvEndDate.setVisibility(View.GONE);
        ivUserIcon.setVisibility(View.GONE);
        tvUser.setVisibility(View.GONE);
        ivPartner.setVisibility(View.GONE);
        tvPartner.setVisibility(View.GONE);
        ivProcessAction.setVisibility(View.GONE);
        tvProcessAction.setVisibility(View.GONE);
    }
}
