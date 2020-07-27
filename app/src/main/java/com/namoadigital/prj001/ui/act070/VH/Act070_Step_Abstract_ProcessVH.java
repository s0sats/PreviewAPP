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
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public abstract class Act070_Step_Abstract_ProcessVH extends RecyclerView.ViewHolder {
    protected Context context;
    protected String transStartProcess;
    protected String transReviewProcess;

    public Act070_Step_Abstract_ProcessVH(Context context, @NonNull View itemView, String transStartProcess, String transReviewProcess) {
        super(itemView);
        this.context = context;
        this.transStartProcess = transStartProcess;
        this.transReviewProcess = transReviewProcess;
    }

    protected void defineCheckInOutIcon(ImageView ivStartEndDateIcon,  boolean hasCheckOutDate) {
        int drawableId =
            hasCheckOutDate
                ? R.drawable.ic_check_white_24dp
                : R.drawable.ic_baseline_input_24dp_black;
        Drawable drawable = context.getDrawable(drawableId);
        ivStartEndDateIcon.setImageDrawable(drawable);
    }

    protected void configProcessAction(ImageView ivProcessAction,TextView tvProcessAction,String processStatus, String stepType, boolean isCurrentStep, boolean isStepAlreadyCheckedIn) {
        int tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PENDING);
        Drawable drawable = null;
        String processActionText = transStartProcess;
        switch (processStatus){
            case ConstantBaseApp.SYS_STATUS_DONE:
                drawable = context.getDrawable(R.drawable.ic_baseline_open_in_new_24dp);
                processActionText = transReviewProcess;
                break;
            case ConstantBaseApp.SYS_STATUS_PENDING:
            case ConstantBaseApp.SYS_STATUS_PROCESS:
            default:
                tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PROCESS);
                drawable = context.getDrawable(R.drawable.ic_baseline_play_arrow_24dp);
                break;
        }
        //
        if(drawable != null){
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
        }
        if(isProcessCheckedIn(stepType,isCurrentStep,isStepAlreadyCheckedIn) || ConstantBaseApp.SYS_STATUS_DONE.equals(processStatus)) {
            ivProcessAction.setImageDrawable(drawable);
            tvProcessAction.setTextColor(tintColor);
            tvProcessAction.setText(processActionText);
            ivProcessAction.setVisibility(View.VISIBLE);
            tvProcessAction.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Se action for da etapa atual e ou é ONE_TOUCH ou é START_END com checkin(propriedades do stepmain
     * @param stepType
     * @param isCurrentStep
     * @param isStepAlreadyCheckedIn
     * @return
     */
    protected boolean isProcessCheckedIn(String stepType, boolean isCurrentStep, boolean isStepAlreadyCheckedIn) {
        return isCurrentStep
               && ( ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH.equals(stepType)
                   || (ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(stepType) && isStepAlreadyCheckedIn)
            );
    }

    protected void applyHighlightBackground(ConstraintLayout clBackground, String stepStatus, boolean isCurrentStep,String StepType, String startDate) {
        int backgroundColor = R.color.padrao_TRANSPARENT;
        Drawable drawable = context.getDrawable(R.drawable.pipeline_step_states);
        //Se step atual, verifica o destaque
        if(!ConstantBaseApp.SYS_STATUS_DONE.equals(stepStatus) && isCurrentStep) {
            //Se start_end, se tiver checkin, fica amarelo , se não fica cinza indicando que falta q
            //não é possivel mexer.
            if (ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(StepType)) {
                backgroundColor =
                    ToolBox_Inf.hasConsistentValueString(startDate)
                        ? R.color.namoa_color_ticket_process_highlight
                        : R.color.namoa_color_pipeline_cur_step_no_checkin ;
                //
                drawable =
                    ToolBox_Inf.hasConsistentValueString(startDate)
                        ? context.getDrawable(R.drawable.pipeline_step_highligh_states)
                        : context.getDrawable(R.drawable.pipeline_step_gray_states);
            }else{
                //Se ONE_TOUCH, fica amarelo.
                backgroundColor = R.color.namoa_color_ticket_process_highlight;
                drawable = context.getDrawable(R.drawable.pipeline_step_highligh_states);
            }
            //
        }
        //
        //clBackground.setBackgroundColor(context.getResources().getColor(backgroundColor));
        clBackground.setBackground(drawable);
    }

    protected void applyHistoryLayout(boolean isCurrentStep, TextView... tvToChangeColorList) {
        int fontColor = R.color.namoa_color_dark_blue;
        //
        if(!isCurrentStep){
            fontColor = R.color.namoa_color_gray_4;
        }
        if(tvToChangeColorList != null && tvToChangeColorList.length > 0){
            for (TextView textView : tvToChangeColorList) {
                textView.setTextColor(ContextCompat.getColor(context, fontColor));
            }
        }
    }

}
