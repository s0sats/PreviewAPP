package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepProcessBtn;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_Process_Btn_VH extends RecyclerView.ViewHolder{
    private Context context;
    private View vTopLine;
    private ConstraintLayout clBackground;
    private ImageView ivBtnIcon;
    private TextView tvBtnLbl;
    private Act070_Steps_Adapter.OnProcessBtnClickListener onProcessBtnClickListener;
    private boolean isInWgEditMode = false;
    protected boolean inReadOnlyMode = false;

    public Act070_Step_Process_Btn_VH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnProcessBtnClickListener onProcessBtnClickListener, boolean isInWgEditMode, boolean inReadOnlyMode) {
        super(itemView);
        this.context = context;
        this.onProcessBtnClickListener = onProcessBtnClickListener;
        this.isInWgEditMode = isInWgEditMode;
        this.inReadOnlyMode = inReadOnlyMode;
        bindViews();
    }

    private void bindViews() {
        clBackground = this.itemView.findViewById(R.id.step_process_btn_cl_background);
        vTopLine = this.itemView.findViewById(R.id.step_process_btn_v_line);
        ivBtnIcon = this.itemView.findViewById(R.id.step_process_btn_iv_icon);
        tvBtnLbl = this.itemView.findViewById(R.id.step_process_btn_tv_ttl);
        clBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onProcessBtnClickListener != null){
                    onProcessBtnClickListener.onProcessBtnClick(
                        getAdapterPosition()
                    );
                }
            }
        });
    }

    public void bindData(StepProcessBtn stepProcessBtn){
        applyStepFocusReadOnly(stepProcessBtn.isUserFocus());
        resetVisibility();
        configBtnByType(stepProcessBtn.getProcessType());
        if(ToolBox_Inf.hasConsistentValueString(stepProcessBtn.getStepDescription())){
            ivBtnIcon.setVisibility(View.VISIBLE);
            tvBtnLbl.setVisibility(View.VISIBLE);
            tvBtnLbl.setText(stepProcessBtn.getStepDescription());
        }
        //LUCHE - 03/12/2020
        applyEditVisivilityIfNeeds();
    }

    private void applyStepFocusReadOnly(boolean isUserFocus) {
        if(!inReadOnlyMode
                && !ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET , Constant.PROFILE_MENU_TICKET_PARAM_CLAIM_SPECIAL_EXECUTION_PERMITION)
                && !isUserFocus) {
            inReadOnlyMode =true;
        }
    }

    /**
     * LUCHE - 03 /12/2020
     * Se modo edição, oculta botões de processo
     */
    private void applyEditVisivilityIfNeeds() {
        if(isInWgEditMode || inReadOnlyMode){
            clBackground.setVisibility(View.GONE);
        }
    }

    private void configBtnByType(String processType) {
        int tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PROCESS);
        Drawable drawable = null;
        switch (processType){
            case ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_CHECKIN:
                tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PROCESS);
                drawable = context.getDrawable(R.drawable.ic_baseline_input_24dp_black);
                break;
            case ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_CHECKOUT:
                tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PROCESS);
                drawable = context.getDrawable(R.drawable.ic_skip_next_yellow_24px);
                break;
            case ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_ADD_NEW:
                tintColor = ToolBox_Inf.getStatusColorV2(context,ConstantBaseApp.SYS_STATUS_PENDING);
                drawable = context.getDrawable(R.drawable.ic_add_plus);
                break;
        }
        if(drawable != null){
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
        }
        ivBtnIcon.setImageDrawable(drawable);
        tvBtnLbl.setTextColor(tintColor);
    }

    private void resetVisibility() {
        clBackground.setVisibility(View.VISIBLE);
        ivBtnIcon.setVisibility(View.GONE);
        tvBtnLbl.setVisibility(View.GONE);
    }
}
