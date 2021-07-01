package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepNone;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_NoneVH extends Act070_Step_Abstract_ProcessVH {
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
    private Act070_Steps_Adapter.OnNoneClickListener noneClickListener;

    public Act070_Step_NoneVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnNoneClickListener noneClickListener, String transStartProcess, String transReviewProcess, String transContinueProcess, String transWaitingSync, boolean isInWgEditMode, boolean inReadOnlyMode) {
        super(context,itemView,transStartProcess,transReviewProcess, transContinueProcess,transWaitingSync,isInWgEditMode,inReadOnlyMode);
        this.noneClickListener = noneClickListener;
        bindViews();
    }

    private void bindViews() {
        vStepContinousLine =  this.itemView.findViewById(R.id.step_none_v_line);
        ivStepDashedLine =  this.itemView.findViewById(R.id.step_none_iv_line);
        clBackground =  this.itemView.findViewById(R.id.step_none_cl_background);
        tvActionDesc =  this.itemView.findViewById(R.id.step_none_tv_desc);
        tvProduct =  this.itemView.findViewById(R.id.step_none_tv_prod);
        tvSerial =  this.itemView.findViewById(R.id.step_none_tv_serial);
        tvSite =  this.itemView.findViewById(R.id.step_none_tv_site);
        ivStartEndDateIcon =  this.itemView.findViewById(R.id.step_none_iv_end_date);
        tvEndDate =  this.itemView.findViewById(R.id.step_none_tv_end_date);
        ivUserIcon =  this.itemView.findViewById(R.id.step_none_iv_user);
        tvUser =  this.itemView.findViewById(R.id.step_none_tv_user);
        vDivider =  this.itemView.findViewById(R.id.step_none_v_divider);
        ivPartner =  this.itemView.findViewById(R.id.step_none_iv_partner);
        tvPartner =  this.itemView.findViewById(R.id.step_none_tv_partner);
        ivProcessAction =  this.itemView.findViewById(R.id.step_none_iv_process_action);
        tvProcessAction =  this.itemView.findViewById(R.id.step_none_tv_process_action);
        clBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noneClickListener != null){
                    noneClickListener.onNoneClick(getAdapterPosition());
                }
            }
        });
    }

    public void bindData(StepNone stepNone){
        applyStepReadOnly(stepNone.isUserFocus());
        resetVisibility();
        //
        tvActionDesc.setText(stepNone.getStepDescription());
        tvActionDesc.setVisibility(View.GONE);
        if(ToolBox_Inf.hasConsistentValueString(stepNone.getProductDesc())) {
            setProductAndSerialVisibility(tvProduct,tvSerial, stepNone.isProductDifferentThanTicket(), stepNone.isSerialDifferentThanTicket());
            tvProduct.setText(stepNone.getProductDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepNone.getSerialId())) {
            setProductAndSerialVisibility(tvProduct,tvSerial, stepNone.isProductDifferentThanTicket(), stepNone.isSerialDifferentThanTicket());
            tvSerial.setText(stepNone.getSerialId());
        }
        //Sem necessidade de chamar o hasConsistentValueString, pois já é chamado internamento
        // no metodo equalsToLoggedSite
        if(ToolBox_Inf.equalsToLoggedSite(context,stepNone.getSiteDesc())) {
            tvSite.setVisibility(View.VISIBLE);
            tvSite.setText(stepNone.getSiteDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepNone.getStartDate())) {
            defineCheckInOutIcon(ivStartEndDateIcon,ToolBox_Inf.hasConsistentValueString(stepNone.getEndDate()));
            ivStartEndDateIcon.setVisibility(View.VISIBLE);
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(
                ToolBox_Inf.getStepStartEndDateFormated(context,stepNone.getStartDate(),stepNone.getEndDate())
            );
        }
        if(ToolBox_Inf.hasConsistentValueString(stepNone.getEndUser())) {
            ivUserIcon.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.VISIBLE);
            tvUser.setText(stepNone.getEndUser());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepNone.getPartnerDesc())) {
            ivPartner.setVisibility(View.VISIBLE);
            tvPartner.setVisibility(View.VISIBLE);
            tvPartner.setText(stepNone.getPartnerDesc());
        }
        //
        applyHistoryLayout(
            stepNone.isCurrentStep(),
            tvActionDesc,
            tvProduct,
            tvSerial,
            tvEndDate,
            tvUser,
            tvPartner
        );
        applyHighlightBackground(
            clBackground,
            stepNone.getProcessStatus(),
            stepNone.isCurrentStep(),
            stepNone.getStepType(),
            //stepNone.getStartDate()
            stepNone.isStepAlreadyCheckedIn()
        );
        configProcessAction(
            ivProcessAction,
            tvProcessAction,
            stepNone.getProcessStatus(),
            stepNone.getStepType(),
            stepNone.isCurrentStep(),
            stepNone.isStepAlreadyCheckedIn()
        );
        //
        removeClickByStatus(stepNone.getProcessStatus());
        //
        defineVhVisibility(stepNone);
    }

    private void removeClickByStatus(String processStatus) {
        if(!ConstantBaseApp.SYS_STATUS_PENDING.equals(processStatus)){
            clBackground.setOnClickListener(null);
        }
    }

    /**
     * LUCHE - 10/11/2020
     * <P></P>
     * Metodo que define a visibilidade do viewHolder.
     * <P></P>
     * Em 09/11/2020, foi definido que ele não deve mais aparecer se seu status for != pending
     * ou se for pending , mas o step for start_end.
     * <P></P>
     * Em 30/06/2021, foi definido que o label não deve aparecer nunca, mas como efeito colateral,
     * ficou bizarro. Então foi modificado novamente a regra de visibilide mais uma vez.
     * Regra para ESCONDER o item em 30/06/2021:
     *  * Se status for != pending
     *  * ou Se status for == pending mas step start_end
     *  * ou Se status for == pending mas step não é atual ou é atual mas o usr não tem foco e nem
     *  parametro claim
     *
     * @param stepNone
     */
    private void defineVhVisibility(StepNone stepNone) {
        if(hideNoneItem(stepNone)){
            clBackground.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 30/06/2021
     * <P></P>
     * Metodo que consilida regra de quando esconder o corpo do none
     * @param stepNone
     * @return
     */
    private boolean hideNoneItem(StepNone stepNone) {
        return  !ConstantBaseApp.SYS_STATUS_PENDING.equals(stepNone.getProcessStatus())
                || (ConstantBaseApp.SYS_STATUS_PENDING.equals(stepNone.getProcessStatus())
                    && ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(stepNone.getStepType())
                    )
                || (ConstantBaseApp.SYS_STATUS_PENDING.equals(stepNone.getProcessStatus())
                    && (!stepNone.isCurrentStep() || (!stepNone.isUserFocus() && !ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET , Constant.PROFILE_MENU_TICKET_PARAM_CLAIM_SPECIAL_EXECUTION_PERMITION)))
        );
    }

    @Override
    protected void configProcessAction(ImageView ivProcessAction, TextView tvProcessAction, String processStatus, String stepType, boolean isCurrentStep, boolean isStepAlreadyCheckedIn) {
        super.configProcessAction(ivProcessAction, tvProcessAction, processStatus, stepType, isCurrentStep, isStepAlreadyCheckedIn);
        //Como none é unico,e não tem obj de verdade, após concluido não deve exibir rever processo.
        if(!ConstantBaseApp.SYS_STATUS_PENDING.equals(processStatus)){
            ivProcessAction.setVisibility(View.GONE);
            tvProcessAction.setVisibility(View.GONE);
        }
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
        //Após 09/11/2020 onde o none não deve ser exibido em certos momentos, necessario
        //resetar a visibilidade
        clBackground.setVisibility(View.VISIBLE);
    }
}
