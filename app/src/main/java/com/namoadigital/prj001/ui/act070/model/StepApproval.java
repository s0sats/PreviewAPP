package com.namoadigital.prj001.ui.act070.model;

public class StepApproval extends BaseStep {
    private String approvalStatus;
    private String approvalComment;
    private String startDate;
    private String endDate;
    private String endUser;
    private String partnerDesc;
    private String stepType;
    private String processStatus;
    private boolean currentStep;

    public StepApproval() {
    }

    public StepApproval(String stepQuestion,String approvalStatus, String endUser, String partnerDesc, String stepType,String processStatus, boolean currentStep) {
        this.stepDescription = stepQuestion;
        this.approvalStatus = approvalStatus;
        this.endUser = endUser;
        this.partnerDesc = partnerDesc;
        this.stepType = stepType;
        this.currentStep = currentStep;
    }

    public StepApproval(String stepQuestion,String approvalStatus,String approvalComment, String startDate, String endDate, String endUser, String partnerDesc, String stepType, String processStatus, boolean currentStep) {
        this.stepDescription = stepQuestion;
        this.approvalStatus = approvalStatus;
        this.processStatus = processStatus;
        this.approvalComment = approvalComment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.endUser = endUser;
        this.partnerDesc = partnerDesc;
        this.stepType = stepType;
        this.currentStep = currentStep;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
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

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public boolean isCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(boolean currentStep) {
        this.currentStep = currentStep;
    }
}
