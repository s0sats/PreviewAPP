package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepForm;
import com.namoadigital.prj001.util.ToolBox_Inf;


public class Act070_Step_FormVH extends Act070_Step_Abstract_ProcessVH {
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
    private Act070_Steps_Adapter.OnChecklistClickListener onChecklistClickListener;

    public Act070_Step_FormVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnChecklistClickListener onChecklistClickListener, String transStartProcess, String transReviewProcess, String transContinueProcess, String transWaitingSync, boolean isInWgEditMode, boolean inReadOnlyMode) {
        super(context,itemView,transStartProcess,transReviewProcess,transContinueProcess,transWaitingSync,isInWgEditMode,inReadOnlyMode);
        this.onChecklistClickListener = onChecklistClickListener;
        bindViews();
    }

    private void bindViews() {
        vStepContinousLine =  this.itemView.findViewById(R.id.step_checklist_v_line);
        ivStepDashedLine =  this.itemView.findViewById(R.id.step_checklist_iv_line);
        clBackground =  this.itemView.findViewById(R.id.step_checklist_cl_background);
        tvActionDesc =  this.itemView.findViewById(R.id.step_checklist_tv_desc);
        tvProduct =  this.itemView.findViewById(R.id.step_checklist_tv_prod);
        tvSerial =  this.itemView.findViewById(R.id.step_checklist_tv_serial);
        tvSite =  this.itemView.findViewById(R.id.step_checklist_tv_site);;
        ivStartEndDateIcon =  this.itemView.findViewById(R.id.step_checklist_iv_end_date);
        tvEndDate =  this.itemView.findViewById(R.id.step_checklist_tv_end_date);
        ivUserIcon =  this.itemView.findViewById(R.id.step_checklist_iv_user);
        tvUser =  this.itemView.findViewById(R.id.step_checklist_tv_user);
        vDivider =  this.itemView.findViewById(R.id.step_checklist_v_divider);
        ivPartner =  this.itemView.findViewById(R.id.step_checklist_iv_partner);
        tvPartner =  this.itemView.findViewById(R.id.step_checklist_tv_partner);
        ivProcessAction =  this.itemView.findViewById(R.id.step_checklist_iv_process_action);
        tvProcessAction =  this.itemView.findViewById(R.id.step_checklist_tv_process_action);
        //
        clBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onChecklistClickListener != null){
                    onChecklistClickListener.onChecklistClick(getAdapterPosition());
                }
            }
        });
    }

    public void bindData(StepForm stepForm){
        resetVisibility();
        //
        tvActionDesc.setText(stepForm.getStepDescription());
        if(ToolBox_Inf.hasConsistentValueString(stepForm.getProductDesc())) {
            setProductAndSerialVisibility(tvProduct,tvSerial, stepForm.isProductDifferentThanTicket(), stepForm.isSerialDifferentThanTicket());
            tvProduct.setText(stepForm.getProductDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepForm.getSerialId())) {
            setProductAndSerialVisibility(tvProduct,tvSerial, stepForm.isProductDifferentThanTicket(), stepForm.isSerialDifferentThanTicket());
            tvSerial.setText(stepForm.getSerialId());
        }
        //Sem necessidade de chamar o hasConsistentValueString, pois já é chamado internamento
        // no metodo equalsToLoggedSite
        if(ToolBox_Inf.equalsToLoggedSite(context,stepForm.getSiteDesc())) {
            tvSite.setVisibility(View.VISIBLE);
            tvSite.setText(stepForm.getSiteDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepForm.getStartDate())) {
            defineCheckInOutIcon(ivStartEndDateIcon,ToolBox_Inf.hasConsistentValueString(stepForm.getEndDate()));
            ivStartEndDateIcon.setVisibility(View.VISIBLE);
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(
                ToolBox_Inf.getStepStartEndDateFormated(context,stepForm.getStartDate(),stepForm.getEndDate())
            );
        }
        if(ToolBox_Inf.hasConsistentValueString(stepForm.getEndUser())) {
            ivUserIcon.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.VISIBLE);
            tvUser.setText(stepForm.getEndUser());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepForm.getPartnerDesc())) {
            ivPartner.setVisibility(View.VISIBLE);
            tvPartner.setVisibility(View.VISIBLE);
            tvPartner.setText(stepForm.getPartnerDesc());
        }
        //
        applyHistoryLayout(
            stepForm.isCurrentStep(),
            tvActionDesc,
            tvProduct,
            tvSerial,
            tvEndDate,
            tvUser,
            tvPartner
        );
        applyHighlightBackground(
            clBackground,
            stepForm.getProcessStatus(),
            stepForm.isCurrentStep(),
            stepForm.getStepType(),
            stepForm.isStepAlreadyCheckedIn()
        );
        configProcessAction(
            ivProcessAction,
            tvProcessAction,
            stepForm.getProcessStatus(),
            stepForm.getStepType(),
            stepForm.isCurrentStep(),
            stepForm.isStepAlreadyCheckedIn()
        );
        //Chama metodo que executara a animaão no vh
        highlightNavVh(clBackground,stepForm.isBackProcessHighlight());
        stepForm.setBackProcessHighlight(false);
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
