package com.namoadigital.prj001.ui.act070.model;

public class StepChecklist extends BaseStep {
    private String productDesc;
    private String serialId;
    private String siteDesc;
    private String startDate;
    private String endDate;
    private String endUser;
    private String partnerDesc;
    private String processStatus;
    private boolean currentStep;

    public StepChecklist() {
    }

    public StepChecklist(String stepDescription, String productDesc, String serialId, String siteDesc, String startDate, String endDate, String endUser, String partnerDesc, String processStatus, boolean currentStep) {
        this.stepDescription = stepDescription;
        this.productDesc = productDesc;
        this.serialId = serialId;
        this.siteDesc = siteDesc;
        this.startDate = startDate;
        this.endDate = endDate;
        this.endUser = endUser;
        this.partnerDesc = partnerDesc;
        this.processStatus = processStatus;
        this.currentStep = currentStep;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndUser() {
        return endUser;
    }

    public void setEndUser(String endUser) {
        this.endUser = endUser;
    }

    public String getPartnerDesc() {
        return partnerDesc;
    }

    public void setPartnerDesc(String partnerDesc) {
        this.partnerDesc = partnerDesc;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public boolean isCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(boolean currentStep) {
        this.currentStep = currentStep;
    }
}
