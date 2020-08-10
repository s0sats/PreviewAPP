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
    private boolean currentStep;
    private boolean stepOpen;
    private boolean scan_serial;
    private boolean allow_new_obj;
    private boolean move_next_step;

    public StepMain() {
    }

    public StepMain(int stepCode, String stepTtl, String stepNum, String forecastStartDate, String forecastEndDate, String checkInDate, String checkOutDate, String stepType, String stepStatus, boolean currentStep,boolean scan_serial, boolean allow_new_obj , boolean move_next_step) {
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

    public static boolean usesStatusColorInStep(String stepStatus){
        return ConstantBaseApp.SYS_STATUS_DONE.equals(stepStatus)
            || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(stepStatus)
            || ConstantBaseApp.SYS_STATUS_CANCELLED.equals(stepStatus)
            || ConstantBaseApp.SYS_STATUS_REJECTED.equals(stepStatus)
            ;

    }
}
