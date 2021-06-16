package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public abstract class Act070_Step_Abstract_ProcessVH extends RecyclerView.ViewHolder {
    protected Context context;
    protected String transStartProcess;
    protected String transReviewProcess;
    protected String transWaitingSync;
    protected String transContinue;
    protected boolean isInWgEditMode;
    protected boolean inReadOnlyMode = false;

    public Act070_Step_Abstract_ProcessVH(Context context, @NonNull View itemView, String transStartProcess, String transReviewProcess, String transContinueProcess, String transWaitingSync, boolean isInWgEditMode,boolean inReadOnlyMode) {
        super(itemView);
        this.context = context;
        this.transStartProcess = transStartProcess;
        this.transReviewProcess = transReviewProcess;
        this.transContinue = transContinueProcess;
        this.transWaitingSync = transWaitingSync;
        this.isInWgEditMode = isInWgEditMode;
        this.inReadOnlyMode = inReadOnlyMode;
    }

    protected void setProductAndSerialVisibility(TextView tvProduct, TextView tvSerial, boolean isProductDifferentThanTicket, boolean isSerialDifferentThanTicket){
        int visibility =  isProductDifferentThanTicket || isSerialDifferentThanTicket ? View.VISIBLE : View.GONE;
        tvProduct.setVisibility(visibility);
        tvSerial.setVisibility(visibility);
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
            case ConstantBaseApp.SYS_STATUS_WAITING_SYNC:
                tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
                drawable = context.getDrawable(R.drawable.ic_baseline_hourglass_empty_24dp_black);
                processActionText = transWaitingSync;
                break;
            case ConstantBaseApp.SYS_STATUS_CANCELLED:
            case ConstantBaseApp.SYS_STATUS_REJECTED:
                tintColor = ToolBox_Inf.getStatusColorV2(context,processStatus);
                drawable = null;
                processActionText = "";
                break;
            case ConstantBaseApp.SYS_STATUS_PENDING:
            case ConstantBaseApp.SYS_STATUS_PROCESS:
            default:
                processActionText = ConstantBaseApp.SYS_STATUS_PROCESS.equals(processStatus) ? transContinue : transStartProcess;
                tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PROCESS);
                drawable = context.getDrawable(R.drawable.ic_baseline_play_arrow_24dp);
                break;
        }
        //
        if(drawable != null){
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
        }
        //TODO VALIDAR ESSE IF, POIS FOI ADICIONANDO O isInWgEditMode
        if(
             !ConstantBaseApp.SYS_STATUS_CANCELLED.equals(processStatus)
              && !ConstantBaseApp.SYS_STATUS_REJECTED.equals(processStatus)
              &&( ConstantBaseApp.SYS_STATUS_DONE.equals(processStatus)
                  || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(processStatus)
                  || (isProcessCheckedIn(stepType,isCurrentStep,isStepAlreadyCheckedIn) && !isInWgEditMode)
            )
        ) {
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

    protected void applyHighlightBackground(ConstraintLayout clBackground, String stepStatus, boolean isCurrentStep,String StepType, boolean isStepAlreadyCheckedIn) {
        int backgroundColor = R.color.padrao_TRANSPARENT;
        Drawable drawable = context.getDrawable(R.drawable.pipeline_step_states);
        //Se step atual, verifica o destaque
        if(!ConstantBaseApp.SYS_STATUS_DONE.equals(stepStatus)
           && !ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(stepStatus)
           && !ConstantBaseApp.SYS_STATUS_CANCELLED.equals(stepStatus)
           && !ConstantBaseApp.SYS_STATUS_REJECTED.equals(stepStatus)
        ) {
            if(isCurrentStep && (!isInWgEditMode && !inReadOnlyMode)) {
                //Se start_end, se tiver checkin, fica amarelo , se não fica cinza indicando que falta q
                //não é possivel mexer.
                if (ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(StepType)) {
                    backgroundColor =
                        isStepAlreadyCheckedIn
                            ? R.color.namoa_color_ticket_process_highlight
                            : R.color.namoa_color_pipeline_cur_step_no_checkin;
                    //
                    drawable =
                        isStepAlreadyCheckedIn
                            ? context.getDrawable(R.drawable.pipeline_step_highligh_states)
                            : context.getDrawable(R.drawable.pipeline_step_no_checkin);
                } else {
                    //Se ONE_TOUCH, fica amarelo.
                    backgroundColor = R.color.namoa_color_ticket_process_highlight;
                    drawable = context.getDrawable(R.drawable.pipeline_step_highligh_states);
                }
                //
            }else{
                drawable = null;
                clBackground.setOnClickListener(null);
            }
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

    protected void highlightNavVh(ConstraintLayout clBackground, boolean backProcessHighlight){
        if(backProcessHighlight) {
            Animation anim = new AlphaAnimation(0.5f, 1.0f);
            anim.setDuration(800); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(5);
            //
            clBackground.startAnimation(anim);
        }
    }
    /**
     * BARRIONUEVO 07-06-2021
     * Forçar o readOnly em steps sem focus quando o usuer nao tiver o perfil de acesso PROFILE_MENU_TICKET_PARAM_CLAIM_SPECIAL_EXECUTION_PERMITION
     */
    protected void applyStepReadOnly(boolean isUserFocus){

        if(!inReadOnlyMode
                && !ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET , Constant.PROFILE_MENU_TICKET_PARAM_CLAIM_SPECIAL_EXECUTION_PERMITION)
                && !isUserFocus) {
            inReadOnlyMode =true;
        }
    }
}
