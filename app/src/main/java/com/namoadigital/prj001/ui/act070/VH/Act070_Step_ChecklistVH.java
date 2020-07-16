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
import com.namoadigital.prj001.ui.act070.model.StepChecklist;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;


public class Act070_Step_ChecklistVH extends RecyclerView.ViewHolder {

    private Context context;
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

    public Act070_Step_ChecklistVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnChecklistClickListener onChecklistClickListener) {
        super(itemView);
        this.context = context;
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
        //Sem necessidade de chamar o hasConsistentValueString, pois já é chamado internamento
        // no metodo equalsToLoggedSite
        if(ToolBox_Inf.equalsToLoggedSite(context,stepChecklist.getSiteDesc())) {
            tvSite.setVisibility(View.VISIBLE);
            tvSite.setText(stepChecklist.getSiteDesc());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getStartDate())) {
            defineCheckInOutIcon(ToolBox_Inf.hasConsistentValueString(stepChecklist.getEndDate()));
            ivStartEndDateIcon.setVisibility(View.VISIBLE);
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(
                ToolBox_Inf.getStepStartEndDateFormated(context,stepChecklist.getStartDate(),stepChecklist.getEndDate())
            );
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getEndUser())) {
            ivUserIcon.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.VISIBLE);
            tvUser.setText(stepChecklist.getEndUser());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getPartnerDesc())) {
            ivPartner.setVisibility(View.VISIBLE);
            tvPartner.setVisibility(View.VISIBLE);
            tvPartner.setText(stepChecklist.getPartnerDesc());
        }
        //
        applyHistoryLayout(stepChecklist);
        applyHighlightBackground(stepChecklist);
        configProcessAction(stepChecklist);
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

    private void configProcessAction(StepChecklist stepChecklist) {
        int tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PENDING);
        Drawable drawable = null;
        if(stepChecklist.isCurrentStep()){
            if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getStartDate())){
                drawable = context.getDrawable(R.drawable.ic_baseline_play_arrow_24dp);
                tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PROCESS);
                if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getEndDate())){
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
            if(ToolBox_Inf.hasConsistentValueString(stepChecklist.getStartDate()) && ToolBox_Inf.hasConsistentValueString(stepChecklist.getEndDate()) ) {
                drawable = context.getDrawable(R.drawable.ic_baseline_open_in_new_24dp);
                drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
                ivProcessAction.setImageDrawable(drawable);
                tvProcessAction.setTextColor(tintColor);
                ivProcessAction.setVisibility(View.VISIBLE);
                tvProcessAction.setVisibility(View.VISIBLE);
            }
        }
    }

    private void applyHighlightBackground(StepChecklist stepChecklist) {
        int backgroundColor = R.color.padrao_TRANSPARENT;
        Drawable drawable = context.getDrawable(R.drawable.pipeline_step_states);
        //Se step atual, verifica o destaque
        if(stepChecklist.isCurrentStep()) {
            //Se start_end, se tiver checkin, fica amarelo , se não fica cinza indicando que falta q
            //não é possivel mexer.
            if (ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(stepChecklist.getStepType())) {
                backgroundColor =
                    ToolBox_Inf.hasConsistentValueString(stepChecklist.getStartDate())
                        ? R.color.namoa_color_ticket_process_highlight
                        : R.color.namoa_color_pipeline_cur_step_no_checkin ;
                //
                drawable =
                    ToolBox_Inf.hasConsistentValueString(stepChecklist.getStartDate())
                        ? context.getDrawable(R.drawable.pipeline_step_highligh_states)
                        : context.getDrawable(R.drawable.pipeline_step_gray_states);
            }else{
                //Se ONE_TOUCH, fica amarelo.
                backgroundColor = R.color.namoa_color_ticket_process_highlight;
                drawable = context.getDrawable(R.drawable.pipeline_step_highligh_states);
            }
            //
//        if(currentStep){
//            vStepContinousLine.setVisibility(View.GONE);
//            ivStepDashedLine.setVisibility(View.VISIBLE);
//        }else{
//            vStepContinousLine.setVisibility(View.VISIBLE);
//            ivStepDashedLine.setVisibility(View.GONE);
//        }
        }
        //
        //clBackground.setBackgroundColor(context.getResources().getColor(backgroundColor));
        clBackground.setBackground(drawable);
    }
    private void applyHistoryLayout(StepChecklist stepChecklist) {
        int fontColor = R.color.namoa_color_dark_blue;
        //
        if(!stepChecklist.isCurrentStep()){
            fontColor = R.color.namoa_color_gray_4;
        }
        tvActionDesc.setTextColor(ContextCompat.getColor(context, fontColor));
        tvProduct.setTextColor(ContextCompat.getColor(context, fontColor));
        tvSerial.setTextColor(ContextCompat.getColor(context, fontColor));
        tvEndDate.setTextColor(ContextCompat.getColor(context, fontColor));
        tvUser.setTextColor(ContextCompat.getColor(context, fontColor));
        tvPartner.setTextColor(ContextCompat.getColor(context, fontColor));
    }

    private void defineCheckInOutIcon(boolean hasCheckOutDate) {
        int drawableId =
            hasCheckOutDate
                ? R.drawable.ic_check_white_24dp
                : R.drawable.ic_baseline_input_24dp_black;
        Drawable drawable = context.getDrawable(drawableId);
        ivStartEndDateIcon.setImageDrawable(drawable);
    }
}
