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

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

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
    private ImageView ivWorkgroup;
    private SearchableSpinner ssWorkgroup;
    private ImageView ivZoneSiteGroup;
    private TextView tvZoneSiteGroup;
    private ImageView ivPcLevelTarget;
    private TextView tvPcLevelTarget;
    private View vBottomDivider;
    private boolean childShown = false;
    private boolean currentStep = false;
    private final Act070_Steps_Adapter.OnMainClickListener onClickListener;
    private final Act070_Steps_Adapter.OnWorkgroupSpinnerClickListener onWorkgroupSpinnerClickListener;
    private boolean isInWgEditMode = false;

    public Act070_Step_MainVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnMainClickListener onClickListener, Act070_Steps_Adapter.OnWorkgroupSpinnerClickListener onWorkgroupSpinnerClickListener,boolean isInWgEditMode) {
        super(itemView);
        this.context = context;
        this.onClickListener = onClickListener;
        this.onWorkgroupSpinnerClickListener = onWorkgroupSpinnerClickListener;
        this.isInWgEditMode = isInWgEditMode;
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
        ivWorkgroup = this.itemView.findViewById(R.id.step_main_iv_workgroup);
        ssWorkgroup = this.itemView.findViewById(R.id.step_main_ss_workgroup);
        ivZoneSiteGroup = this.itemView.findViewById(R.id.step_main_iv_zone_site_group);
        tvZoneSiteGroup = this.itemView.findViewById(R.id.step_main_tv_zone_site_group);
        ivPcLevelTarget = this.itemView.findViewById(R.id.step_main_iv_pc_level_target);
        tvPcLevelTarget = this.itemView.findViewById(R.id.step_main_tv_pc_level_target);
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
            tvDesc.setTextColor(context.getResources().getColor(R.color.font_normal));
            if ( stepMain.isCurrentStep()
                    && stepMain.isUser_focus()
                    && !ConstantBaseApp.SYS_STATUS_DONE.equals(stepMain.getStepStatus())){
                tvDesc.setTextColor(context.getResources().getColor(R.color.namoa_sync_pipeline_background_btn));
            }
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
        if(isInWgEditMode
            && (ToolBox_Inf.hasConsistentValueString(stepMain.getWorkgroup_code()) || ToolBox_Inf.hasConsistentValueString(stepMain.getAp_workgroup_code()))
        ){
            //SE HOUVER DADOS AP_* ELES É QUE DEVEM SER EXIBIDOS.
            if(ToolBox_Inf.hasConsistentValueString(stepMain.getAp_workgroup_code())){
                ssWorkgroup.setmValue(generateWorkGroupValue(stepMain.getAp_workgroup_code() , stepMain.getAp_workgroup_desc()));
            }else{
                ssWorkgroup.setmValue(generateWorkGroupValue(stepMain.getWorkgroup_code(), stepMain.getWorkgroup_desc()));
            }
            ssWorkgroup.setOnSpinnerClickListner(new SearchableSpinner.OnSpinnerClickListner() {
                @Override
                public void onSpinnerClickListner(boolean b) {
                    ArrayList<HMAux> workgroupList = onWorkgroupSpinnerClickListener.onWorkgroupSpinnerClick();
                    ssWorkgroup.setmOption(workgroupList);
                }
            });
        }
        if(isInWgEditMode
            && (ToolBox_Inf.hasConsistentValueString(stepMain.getZone_site_group_desc()) || ToolBox_Inf.hasConsistentValueString(stepMain.getAp_zone_site_group_desc()))
        ){
            //SE HOUVER DADOS AP_* ELES É QUE DEVEM SER EXIBIDOS.
            tvZoneSiteGroup.setText(
                ToolBox_Inf.hasConsistentValueString(stepMain.getAp_zone_site_group_desc())
                    ? stepMain.getAp_zone_site_group_desc()
                    : stepMain.getZone_site_group_desc()
            );
        }
        //
        if(isInWgEditMode
            && (ToolBox_Inf.hasConsistentValueString(stepMain.getPc_level_target()) || ToolBox_Inf.hasConsistentValueString(stepMain.getAp_pc_level_target()))
        ){
            tvPcLevelTarget.setText(
                ToolBox_Inf.hasConsistentValueString(stepMain.getAp_pc_level_target())
                    ? stepMain.getAp_pc_level_target()
                    : stepMain.getPc_level_target()
            );
        }
        setToggleIcon();
        setStatusColorChanges(stepMain);
    }

    private HMAux generateWorkGroupValue(String workgroup_code, String workgroup_desc) {
        HMAux hmAux = new HMAux();
        hmAux.put(SearchableSpinner.CODE,workgroup_code);
        hmAux.put(SearchableSpinner.DESCRIPTION,workgroup_desc);
        return hmAux;
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
            if(StepMain.usesStatusColorInStep(stepMain.getStepStatus())){
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
        if(isInWgEditMode){
            //TODO VERIFICA POIS O SS PODE SER NULL ENTÃO NESSE CASO DEVERIA SER EXIBIDO MESMO QUE SEM VALOR...
            ivWorkgroup.setVisibility(childShown && ssWorkgroup.getmValue().size() > 0 ? View.VISIBLE : View.GONE);
            ssWorkgroup.setVisibility(childShown && ssWorkgroup.getmValue().size() > 0  ? View.VISIBLE : View.GONE);
            ivZoneSiteGroup.setVisibility(childShown && !tvZoneSiteGroup.getText().toString().isEmpty()? View.VISIBLE : View.GONE);
            tvZoneSiteGroup.setVisibility(childShown && !tvZoneSiteGroup.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
            ivPcLevelTarget.setVisibility(childShown && !tvPcLevelTarget.getText().toString().isEmpty()? View.VISIBLE : View.GONE);
            tvPcLevelTarget.setVisibility(childShown && !tvPcLevelTarget.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
        }
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
        //
        ivWorkgroup.setVisibility(View.GONE);
        ssWorkgroup.setVisibility(View.GONE);
        ssWorkgroup.getmValue().clear();
        ivZoneSiteGroup.setVisibility(View.GONE);
        tvZoneSiteGroup.setVisibility(View.GONE);
        tvZoneSiteGroup.setText("");
        ivPcLevelTarget.setVisibility(View.GONE);
        tvPcLevelTarget.setVisibility(View.GONE);
        tvPcLevelTarget.setText("");
        vBottomDivider.setVisibility(View.GONE);
    }
}
