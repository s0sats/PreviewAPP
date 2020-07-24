package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepProcessBtn;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_Process_Btn_VH extends RecyclerView.ViewHolder{
    private Context context;
    private View vTopLine;
    private ConstraintLayout clBackground;
    private ImageView ivBtnIcon;
    private TextView tvBtnLbl;
    private Act070_Steps_Adapter.OnProcessBtnClickListener onProcessBtnClickListener;

    public Act070_Step_Process_Btn_VH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnProcessBtnClickListener onProcessBtnClickListener) {
        super(itemView);
        this.context = context;
        this.onProcessBtnClickListener = onProcessBtnClickListener;
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
        resetVisibility();
        configBtnByType(stepProcessBtn.getProcessType());
        if(ToolBox_Inf.hasConsistentValueString(stepProcessBtn.getStepDescription())){
            ivBtnIcon.setVisibility(View.VISIBLE);
            tvBtnLbl.setVisibility(View.VISIBLE);
            tvBtnLbl.setText(stepProcessBtn.getStepDescription());
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
        ivBtnIcon.setVisibility(View.GONE);
        tvBtnLbl.setVisibility(View.GONE);
    }
}
