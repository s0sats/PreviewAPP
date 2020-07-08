package com.namoadigital.prj001.ui.act070.model;

public class StepMain extends BaseStep{

    private String endDate;
    private String stepStatus;

    public StepMain() {
    }

    public StepMain(String stepTtl,String endDate, String stepStatus) {
        this.stepDescription = stepTtl;
        this.endDate = endDate;
        this.stepStatus = stepStatus;
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
}
