package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.util.Constant;
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
    private ImageView ivMainUser;
    private TextView tvMainUser;
    private View vBottomDivider;
    private boolean childShown = false;
    private boolean currentStep = false;
    private final Act070_Steps_Adapter.OnMainClickListener onClickListener;
    private final Act070_Steps_Adapter.OnWorkgroupSpinnerListeners onWorkgroupSpinnerClickListener;
    private boolean isInWgEditMode = false;
    private String approvalType;
    private boolean statusAllowEdition = false;
    private boolean inReadOnlyMode = false;

    public Act070_Step_MainVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnMainClickListener onClickListener, Act070_Steps_Adapter.OnWorkgroupSpinnerListeners onWorkgroupSpinnerClickListener, boolean isInWgEditMode, boolean inReadOnlyMode) {
        super(itemView);
        this.context = context;
        this.onClickListener = onClickListener;
        this.onWorkgroupSpinnerClickListener = onWorkgroupSpinnerClickListener;
        this.isInWgEditMode = isInWgEditMode;
        this.inReadOnlyMode = inReadOnlyMode;
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
        ivMainUser = this.itemView.findViewById(R.id.step_main_iv_main_user);
        tvMainUser = this.itemView.findViewById(R.id.step_main_tv_main_user);
        vBottomDivider = this.itemView.findViewById(R.id.step_main_v_divider);
        //
        configSSWorkgroup();
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

    private void configSSWorkgroup() {
         ssWorkgroup.setmShowBarcode(false);
         ssWorkgroup.setmShowLabel(false);
         ssWorkgroup.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
             @Override
             public void onItemPreSelected(HMAux hmAux) {

             }

             @Override
             public void onItemPostSelected(HMAux hmAux) {
                onWorkgroupSpinnerClickListener.notifySpinnerItemSelected(getAdapterPosition(), hmAux, ssWorkgroup.hasChangedBD());
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
        approvalType = stepMain.getAp_type();
        statusAllowEdition = ConstantBaseApp.SYS_STATUS_PENDING.equals(stepMain.getStepStatus())
                            || ConstantBaseApp.SYS_STATUS_PROCESS.equals(stepMain.getStepStatus());
        /**
         * BARRIONUEVO 07-06-2021
         * Forçar o readOnly em steps sem focus quando o usuer nao tiver o perfil de acesso PROFILE_MENU_TICKET_PARAM_CLAIM_SPECIAL_EXECUTION_PERMITION
         */
        if(!inReadOnlyMode
        && !ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET , Constant.PROFILE_MENU_TICKET_PARAM_CLAIM_SPECIAL_EXECUTION_PERMITION)
        && !stepMain.isUser_focus()) {
            inReadOnlyMode =true;
        }
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
                tvDesc.setTextColor(context.getResources().getColor(R.color.namoa_status_process));
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
        //
        applyInEditUIChanges(stepMain);
        setToggleIcon();
        setStatusColorChanges(stepMain);
    }

    private void applyInEditUIChanges(StepMain stepMain) {
        if(!inReadOnlyMode){
                if (statusAllowEdition) {
                    if (isInWgEditMode
                            && ConstantBaseApp.TK_PIPELINE_APPROVAL_GET_MATERIAL.equals(stepMain.getAp_type())
                            && stepMain.getMain_user() != null && !stepMain.getMain_user().isEmpty()
                    ) {
                        tvMainUser.setText(stepMain.getMain_user());
                    }
                    //
                    if (isInWgEditMode) {
                        HMAux mValue = new HMAux();
                        HMAux mDbValue = new HMAux();
                        if ((ToolBox_Inf.hasConsistentValueString(stepMain.getWorkgroup_code())
                                || ToolBox_Inf.hasConsistentValueString(stepMain.getAp_workgroup_code())
                                || stepMain.isGroupChanged())
                        ) {
                            //SE HOUVER DADOS AP_* ELES É QUE DEVEM SER EXIBIDOS.

                            if (stepMain.isGroupChanged()) {
                                mValue = generateWorkGroupValue(stepMain.getSelected_group_code(), stepMain.getSelected_group_desc());
                                //TODO rever por possivel bug
                                //POSSIVEL BUG AQUI , CASO NÃO TENHA A VAR PREENCHIDA...TALVEZ TEREI QUE ENVIAR PRO
                                // STEPMAIN SE SEU PRIMEIRO FILHO É UMA APROVAÇÃO E NÃO CONFIAR SOMENTE EM QUAL ESTA PREENCHIDA.
                                if (ToolBox_Inf.hasConsistentValueString(stepMain.getAp_workgroup_code())) {
                                    mDbValue = generateWorkGroupValue(stepMain.getAp_workgroup_code(), stepMain.getAp_workgroup_desc());
                                } else {
                                    mDbValue = generateWorkGroupValue(stepMain.getWorkgroup_code(), stepMain.getWorkgroup_desc());
                                }
                            } else {
                                if (ToolBox_Inf.hasConsistentValueString(stepMain.getAp_workgroup_code())) {
                                    mValue = mDbValue = generateWorkGroupValue(stepMain.getAp_workgroup_code(), stepMain.getAp_workgroup_desc());
                                } else {
                                    mValue = mDbValue = generateWorkGroupValue(stepMain.getWorkgroup_code(), stepMain.getWorkgroup_desc());
                                }
                            }
                        }
                        //
                        ssWorkgroup.setmValue(mValue);
                        ssWorkgroup.setmValueBD(mDbValue);
                        //
                        ssWorkgroup.setOnSpinnerClickListner(new SearchableSpinner.OnSpinnerClickListner() {
                            @Override
                            public void onSpinnerClickListner(boolean b) {
                                ArrayList<HMAux> workgroupList = onWorkgroupSpinnerClickListener.onWorkgroupSpinnerClick();
                                ssWorkgroup.setmOption(workgroupList);
                            }
                        });
                    }
                    //
                    if (isInWgEditMode
                            && (ToolBox_Inf.hasConsistentValueString(stepMain.getZone_site_group_desc()) || ToolBox_Inf.hasConsistentValueString(stepMain.getAp_zone_site_group_desc()))
                    ) {
                        //SE HOUVER DADOS AP_* ELES É QUE DEVEM SER EXIBIDOS.
                        tvZoneSiteGroup.setText(
                                ToolBox_Inf.hasConsistentValueString(stepMain.getAp_zone_site_group_desc())
                                        ? stepMain.getAp_zone_site_group_desc()
                                        : stepMain.getZone_site_group_desc()
                        );
                    }
                    //
                    if (isInWgEditMode
                            && (ToolBox_Inf.hasConsistentValueString(stepMain.getPc_level_target()) || ToolBox_Inf.hasConsistentValueString(stepMain.getAp_pc_level_target()))
                    ) {
                        tvPcLevelTarget.setText(
                                ToolBox_Inf.hasConsistentValueString(stepMain.getAp_pc_level_target())
                                        ? stepMain.getAp_pc_level_target()
                                        : stepMain.getPc_level_target()
                        );
                    }
                }
            }
        }

    private HMAux generateWorkGroupValue(String workgroup_code, String workgroup_desc) {
        HMAux hmAux = new HMAux();
        if(workgroup_code != null && !workgroup_code.isEmpty() && !"0".equals(workgroup_code)) {
            hmAux.put(SearchableSpinner.CODE, workgroup_code);
            hmAux.put(SearchableSpinner.DESCRIPTION, workgroup_desc);
        }
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
        if(!inReadOnlyMode && statusAllowEdition && isInWgEditMode){
            if(ConstantBaseApp.TK_PIPELINE_APPROVAL_GET_MATERIAL.equals(approvalType)){
                ivMainUser.setVisibility(childShown && !tvMainUser.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
                tvMainUser.setVisibility(childShown && !tvMainUser.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
                //
                ivWorkgroup.setVisibility(View.GONE);
                ssWorkgroup.setVisibility(View.GONE);
                ivZoneSiteGroup.setVisibility(View.GONE);
                tvZoneSiteGroup.setVisibility(View.GONE);
                ivPcLevelTarget.setVisibility(View.GONE);
                tvPcLevelTarget.setVisibility(View.GONE);
            }else {
                //TODO VERIFICA POIS O SS PODE SER NULL ENTÃO NESSE CASO DEVERIA SER EXIBIDO MESMO QUE SEM VALOR...
                ivWorkgroup.setVisibility(childShown ? View.VISIBLE : View.GONE);
                ssWorkgroup.setVisibility(childShown ? View.VISIBLE : View.GONE);
                ivZoneSiteGroup.setVisibility(childShown && !tvZoneSiteGroup.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
                tvZoneSiteGroup.setVisibility(childShown && !tvZoneSiteGroup.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
                ivPcLevelTarget.setVisibility(childShown && !tvPcLevelTarget.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
                tvPcLevelTarget.setVisibility(childShown && !tvPcLevelTarget.getText().toString().isEmpty() ? View.VISIBLE : View.GONE);
                //
                ivMainUser.setVisibility(View.GONE);
                tvMainUser.setVisibility(View.GONE);
            }
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
        vBottomDivider.setVisibility(View.GONE);
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
        ivMainUser.setVisibility(View.GONE);
        tvMainUser.setVisibility(View.GONE);
        tvMainUser.setText("");
    }
}
