package com.namoadigital.prj001.ui.act070.model;

public class StepMain extends BaseStep{
    private String stepNum;
    private String endDate;
    private String stepStatus;
    private boolean currentStep;

    public StepMain() {
    }

    public StepMain(String stepTtl, String stepNum, String endDate, String stepStatus, boolean currentStep) {
        this.stepNum = stepNum;
        this.currentStep = currentStep;
        this.stepDescription = stepTtl;
        this.endDate = endDate;
        this.stepStatus = stepStatus;
    }

    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
}
