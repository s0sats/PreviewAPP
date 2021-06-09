package com.namoadigital.prj001.ui.act070.model;

import com.namoadigital.prj001.util.ConstantBaseApp;

public class StepMain extends BaseStep{
    private String stepNum;
    private String forecastStartDate;
    private String forecastEndDate;
    private String checkInDate;
    private String checkOutDate;
    private String stepType;
    private String stepStatus;
    private String workgroup_code;
    private String workgroup_desc;
    private String zone_site_group_code;
    private String zone_site_group_desc;
    private String pc_level_target;
    private String ap_workgroup_code;
    private String ap_workgroup_desc;
    private String ap_zone_site_group_code;
    private String ap_zone_site_group_desc;
    private String ap_pc_level_target;
    private String selected_group_code;
    private String selected_group_desc;
    private boolean groupChanged;
    private boolean currentStep;
    private boolean stepOpen;
    private boolean scan_serial;
    private boolean allow_new_obj;
    private boolean move_next_step;
    private String ap_type;
    private String main_user;

    public StepMain() {
    }

    public StepMain(int stepCode, String stepTtl, String stepNum, String forecastStartDate, String forecastEndDate, String checkInDate, String checkOutDate, String stepType, String stepStatus, boolean currentStep, boolean scan_serial, boolean allow_new_obj, boolean move_next_step, boolean user_focus,String workgroup_code,String workgroup_desc,String zone_site_group_code,String zone_site_group_desc,String pc_level_target,String ap_workgroup_code,String ap_workgroup_desc,String ap_zone_site_group_code,String ap_zone_site_group_desc,String ap_pc_level_target, String ap_type, String main_user) {
        this.ap_workgroup_code = ap_workgroup_code;
        this.ap_workgroup_desc = ap_workgroup_desc;
        this.ap_zone_site_group_code = ap_zone_site_group_code;
        this.ap_zone_site_group_desc = ap_zone_site_group_desc;
        this.ap_pc_level_target = ap_pc_level_target;
        this.stepCode = stepCode;
        this.stepDescription = stepTtl;
        this.stepNum = stepNum;
        this.forecastStartDate = forecastStartDate;
        this.forecastEndDate = forecastEndDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.stepType = stepType;
        this.stepStatus = stepStatus;
        this.currentStep = currentStep;
        this.stepOpen = false;
        this.scan_serial = scan_serial;
        this.allow_new_obj = allow_new_obj;
        this.move_next_step = move_next_step;
        this.userFocus = user_focus;
        this.workgroup_code = workgroup_code;
        this.workgroup_desc = workgroup_desc;
        this.zone_site_group_code = zone_site_group_code;
        this.zone_site_group_desc = zone_site_group_desc;
        this.pc_level_target = pc_level_target;
        this.ap_type = ap_type;
        this.main_user = main_user;
    }

    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    public String getForecastStartDate() {
        return forecastStartDate;
    }

    public void setForecastStartDate(String forecastStartDate) {
        this.forecastStartDate = forecastStartDate;
    }

    public String getForecastEndDate() {
        return forecastEndDate;
    }

    public void setForecastEndDate(String forecastEndDate) {
        this.forecastEndDate = forecastEndDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(String stepStatus) {
        this.stepStatus = stepStatus;
    }

    public boolean isCurrentStep() {
        return currentStep;
    }
    public void setCurrentStep(boolean currentStep) {
        this.currentStep = currentStep;
    }

    public boolean isStepOpen() {
        return stepOpen;
    }

    public void setStepOpen(boolean stepOpen) {
        this.stepOpen = stepOpen;
    }

    public boolean isScan_serial() {
        return scan_serial;
    }

    public void setScan_serial(boolean scan_serial) {
        this.scan_serial = scan_serial;
    }

    public boolean isAllow_new_obj() {
        return allow_new_obj;
    }

    public void setAllow_new_obj(boolean allow_new_obj) {
        this.allow_new_obj = allow_new_obj;
    }

    public boolean isMove_next_step() {
        return move_next_step;
    }

    public void setMove_next_step(boolean move_next_step) {
        this.move_next_step = move_next_step;
    }

    /**
     * BARRIONUEVO 07-06-2021
     * Controle de acesso por foco do step.
     * @return
     */
    public boolean isUser_focus() {
        return this.userFocus;
    }

    public void setUser_focus(boolean user_focus) {
        this.userFocus = user_focus;
    }

    public String getWorkgroup_code() {
        return workgroup_code;
    }

    public void setWorkgroup_code(String workgroup_code) {
        this.workgroup_code = workgroup_code;
    }

    public String getWorkgroup_desc() {
        return workgroup_desc;
    }

    public void setWorkgroup_desc(String workgroup_desc) {
        this.workgroup_desc = workgroup_desc;
    }

    public String getZone_site_group_code() {
        return zone_site_group_code;
    }

    public void setZone_site_group_code(String zone_site_group_code) {
        this.zone_site_group_code = zone_site_group_code;
    }

    public String getZone_site_group_desc() {
        return zone_site_group_desc;
    }

    public void setZone_site_group_desc(String zone_site_group_desc) {
        this.zone_site_group_desc = zone_site_group_desc;
    }

    public String getPc_level_target() {
        return pc_level_target;
    }

    public void setPc_level_target(String pc_level_target) {
        this.pc_level_target = pc_level_target;
    }

    public String getAp_workgroup_code() {
        return ap_workgroup_code;
    }

    public void setAp_workgroup_code(String ap_workgroup_code) {
        this.ap_workgroup_code = ap_workgroup_code;
    }

    public String getAp_workgroup_desc() {
        return ap_workgroup_desc;
    }

    public void setAp_workgroup_desc(String ap_workgroup_desc) {
        this.ap_workgroup_desc = ap_workgroup_desc;
    }

    public String getAp_zone_site_group_code() {
        return ap_zone_site_group_code;
    }

    public void setAp_zone_site_group_code(String ap_zone_site_group_code) {
        this.ap_zone_site_group_code = ap_zone_site_group_code;
    }

    public String getAp_zone_site_group_desc() {
        return ap_zone_site_group_desc;
    }

    public void setAp_zone_site_group_desc(String ap_zone_site_group_desc) {
        this.ap_zone_site_group_desc = ap_zone_site_group_desc;
    }

    public String getAp_pc_level_target() {
        return ap_pc_level_target;
    }

    public void setAp_pc_level_target(String ap_pc_level_target) {
        this.ap_pc_level_target = ap_pc_level_target;
    }

    public String getSelected_group_code() {
        return selected_group_code;
    }

    public void setSelected_group_code(String selected_group_code) {
        this.selected_group_code = selected_group_code;
    }

    public String getSelected_group_desc() {
        return selected_group_desc;
    }

    public void setSelected_group_desc(String selected_group_desc) {
        this.selected_group_desc = selected_group_desc;
    }

    public boolean isGroupChanged() {
        return groupChanged;
    }

    public void setGroupChanged(boolean groupChanged) {
        this.groupChanged = groupChanged;
    }

    public String getAp_type() {
        return ap_type;
    }

    public void setAp_type(String ap_type) {
        this.ap_type = ap_type;
    }

    public String getMain_user() {
        return main_user;
    }

    public void setMain_user(String main_user) {
        this.main_user = main_user;
    }

    public static boolean usesStatusColorInStep(String stepStatus){
        return ConstantBaseApp.SYS_STATUS_DONE.equals(stepStatus)
            || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(stepStatus)
            || ConstantBaseApp.SYS_STATUS_CANCELLED.equals(stepStatus)
            || ConstantBaseApp.SYS_STATUS_REJECTED.equals(stepStatus)
            ;

    }
}
