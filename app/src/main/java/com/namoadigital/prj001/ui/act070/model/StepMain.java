package com.namoadigital.prj001.ui.act070.model;

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

    public StepMain() {
    }

    public StepMain(int stepCode, String stepTtl, String stepNum, String forecastStartDate, String forecastEndDate, String checkInDate, String checkOutDate, String stepType, String stepStatus, boolean currentStep) {
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
}
