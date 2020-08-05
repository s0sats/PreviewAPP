package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_MainVH extends RecyclerView.ViewHolder{
    private Context context;
    private TextView tvStepNum;
    private View vTopStepLine;
    private View vBottomStepLine;
    private ImageView ivBottomStepDashedLine;
    private TextView tvDesc;
    private ImageView ivToggleIcon;
    private ImageView ivForecastDate;
    private TextView tvForecastDate;
    private ImageView ivCheckInAndOutDate;
    private TextView tvCheckInAndOutDate;
    private View vBottomDivider;
    private boolean childShown = false;
    private boolean currentStep = false;
    private final Act070_Steps_Adapter.OnMainClickListener onClickListener;

    public Act070_Step_MainVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnMainClickListener onClickListener) {
        super(itemView);
        this.context = context;
        this.onClickListener = onClickListener;
        bindViews();
    }

    private void bindViews() {
        vTopStepLine = this.itemView.findViewById(R.id.step_main_v_top_step_line);
        vBottomStepLine = this.itemView.findViewById(R.id.step_main_v_bottom_step_line);
        ivBottomStepDashedLine = this.itemView.findViewById(R.id.step_main_iv_bottom_step_dashed_line);
        tvStepNum = this.itemView.findViewById(R.id.step_main_tv_step_num);
        tvDesc = this.itemView.findViewById(R.id.step_main_tv_desc);
        ivToggleIcon = this.itemView.findViewById(R.id.step_main_iv_toggle_icon);
        ivForecastDate = this.itemView.findViewById(R.id.step_main_iv_forecast_date);
        tvForecastDate = this.itemView.findViewById(R.id.step_main_tv_forecast_date);
        tvCheckInAndOutDate = this.itemView.findViewById(R.id.step_main_tv_end_date);
        ivCheckInAndOutDate = this.itemView.findViewById(R.id.step_main_iv_end_date);
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
        childShown = stepMain.isStepOpen();
        currentStep = stepMain.isCurrentStep();
        //
        resetVisibility();
        if(ToolBox_Inf.hasConsistentValueString(stepMain.getStepNum())){
            tvStepNum.setVisibility(View.VISIBLE);
            tvStepNum.setText(stepMain.getStepNum());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepMain.getStepDescription())){
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(stepMain.getStepDescription());
        }
        if(ToolBox_Inf.hasConsistentValueString(stepMain.getForecastStartDate()) && ToolBox_Inf.hasConsistentValueString(stepMain.getForecastEndDate())){
            tvForecastDate.setText(ToolBox_Inf.getStepStartEndDateFormated(context,stepMain.getForecastStartDate(),stepMain.getForecastEndDate()));
        }
        if( ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate())){
            defineCheckInOutIcon(ToolBox_Inf.hasConsistentValueString(stepMain.getCheckOutDate()));
            tvCheckInAndOutDate.setText(
                ToolBox_Inf.getStepStartEndDateFormated(context,stepMain.getCheckInDate(),stepMain.getCheckOutDate())
            );
        }
        setToggleIcon();
        setStatusColorChanges(stepMain);
    }

    private void defineCheckInOutIcon(boolean hasCheckOutDate) {
        int drawableId =
            hasCheckOutDate
                ? R.drawable.ic_check_white_24dp
                : R.drawable.ic_baseline_input_24dp_black;
        Drawable drawable = context.getDrawable(drawableId);
        ivCheckInAndOutDate.setImageDrawable(drawable);
    }

    private void setStatusColorChanges(StepMain stepMain) {
        if(ToolBox_Inf.hasConsistentValueString(stepMain.getStepStatus())){
            int stepColor = ContextCompat.getColor(context,R.color.namoa_color_pipeline_next_step);
            if(ConstantBaseApp.SYS_STATUS_DONE.equals(stepMain.getStepStatus())
               || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(stepMain.getStepStatus())
            ){
                stepColor = ToolBox_Inf.getStatusColorV2(context,stepMain.getStepStatus());
            }else if(stepMain.isCurrentStep()){
                stepColor = ContextCompat.getColor(context,R.color.namoa_status_process);
            }
            //
            tvStepNum.getBackground().setColorFilter(stepColor, PorterDuff.Mode.SRC_ATOP);
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
        //
        vBottomDivider.setVisibility(childShown ? View.VISIBLE : View.GONE);
        ivForecastDate.setVisibility(childShown && !tvForecastDate.getText().toString().isEmpty()  ? View.VISIBLE : View.GONE);
        tvForecastDate.setVisibility(childShown && !tvForecastDate.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
        ivCheckInAndOutDate.setVisibility(childShown && !tvCheckInAndOutDate.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
        tvCheckInAndOutDate.setVisibility(childShown && !tvCheckInAndOutDate.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
        //
//        if(currentStep){
//            ivBottomStepDashedLine.setVisibility(View.VISIBLE);
//            vBottomStepLine.setVisibility(View.GONE);
//        }else{
//            ivBottomStepDashedLine.setVisibility(View.GONE);
//            vBottomStepLine.setVisibility(View.VISIBLE);
//        }
    }

    private void resetVisibility() {
        vTopStepLine.setVisibility(View.VISIBLE);
        vBottomStepLine.setVisibility(View.VISIBLE);
        ivBottomStepDashedLine.setVisibility(View.GONE);
        tvStepNum.setVisibility(View.GONE);
        tvDesc.setVisibility(View.GONE);
        ivToggleIcon.setVisibility(View.VISIBLE);
        ivForecastDate.setVisibility(View.GONE);
        tvForecastDate.setVisibility(View.GONE);
        tvForecastDate.setText("");
        ivCheckInAndOutDate.setVisibility(View.GONE);
        tvCheckInAndOutDate.setVisibility(View.GONE);
        tvCheckInAndOutDate.setText("");
        vBottomDivider.setVisibility(View.GONE);
    }
}
