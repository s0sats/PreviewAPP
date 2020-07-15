package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_MainVH extends RecyclerView.ViewHolder{
    private Context context;
    private TextView tvStepNum;
    private View vTopStepLine;
    private View vBottomStepLine;
    private TextView tvDesc;
    private ImageView ivToggleIcon;
    private ImageView ivEndDate;
    private TextView tvEndDate;
    private View vBottomDivider;
    private boolean childShown = false;
    private final Act070_Steps_Adapter.onMainClickListener onClickListener;

    public Act070_Step_MainVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.onMainClickListener onClickListener) {
        super(itemView);
        this.context = context;
        this.onClickListener = onClickListener;
        bindViews();
    }

    private void bindViews() {
        vTopStepLine = this.itemView.findViewById(R.id.step_main_v_top_step_line);
        vBottomStepLine = this.itemView.findViewById(R.id.step_main_v_bottom_step_line);
        tvStepNum = this.itemView.findViewById(R.id.step_main_tv_step_num);
        tvDesc = this.itemView.findViewById(R.id.step_main_tv_desc);
        ivToggleIcon = this.itemView.findViewById(R.id.step_main_iv_toggle_icon);
        tvEndDate = this.itemView.findViewById(R.id.step_main_tv_end_date);
        ivEndDate = this.itemView.findViewById(R.id.step_main_iv_end_date);
        vBottomDivider = this.itemView.findViewById(R.id.step_main_v_divider);
        //
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onMainClick(
                        isChildShown(),
                        getAdapterPosition()
                    );
                    //
                    childShown = !childShown;
                }
                setToggleIcon();
            }
        });
    }

    public boolean isChildShown() {
        return childShown;
    }

    public void setChildShown(boolean childShown) {
        this.childShown = childShown;
    }

    public void bindData(StepMain stepMain){
        resetVisibility();
        setToggleIcon();
        if(ToolBox_Inf.hasConsistentValueString(stepMain.getStepNum())){
            tvStepNum.setVisibility(View.VISIBLE);
            tvStepNum.setText(stepMain.getStepNum());
        }

        if(ToolBox_Inf.hasConsistentValueString(stepMain.getStepDescription())){
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(stepMain.getStepDescription());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepMain.getEndDate())){
            ivEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(stepMain.getEndDate());
        }
        //
        setStatusColorChanges(stepMain);
    }

    private void setStatusColorChanges(StepMain stepMain) {
        if(ToolBox_Inf.hasConsistentValueString(stepMain.getStepStatus())){
            tvStepNum.getBackground().setColorFilter(
                ToolBox_Inf.getStatusColorV2(context,stepMain.getStepStatus()), PorterDuff.Mode.SRC_ATOP
            );
        }
    }

    private void setToggleIcon() {
        ivToggleIcon.setImageDrawable(
            context.getDrawable(
             childShown
                ? R.drawable.ic_baseline_keyboard_arrow_up_24_black
                : R.drawable.ic_baseline_keyboard_arrow_down_24_black
            )
        );
        vBottomDivider.setVisibility(childShown ? View.VISIBLE : View.GONE );
        //
    }

    private void resetVisibility() {
        vTopStepLine.setVisibility(View.VISIBLE);
        vBottomStepLine.setVisibility(View.VISIBLE);
        tvStepNum.setVisibility(View.GONE);
        tvDesc.setVisibility(View.GONE);
        ivToggleIcon.setVisibility(View.VISIBLE);
        tvEndDate.setVisibility(View.GONE);
        ivEndDate.setVisibility(View.GONE);
        vBottomDivider.setVisibility(View.GONE);
    }
}
