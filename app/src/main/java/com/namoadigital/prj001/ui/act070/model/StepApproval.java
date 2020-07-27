package com.namoadigital.prj001.ui.act070.model;

public class StepApproval extends StepAbstractProcess {
    private String approvalType;
    private String approvalStatus;
    private String approvalComment;
    private String startDate;
    private String endDate;
    private String endUser;
    private String partnerDesc;
    private boolean hasRejection;

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
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

    public void setApprovalQuestion(String approvalQuestion){
        this.stepDescription = approvalQuestion;
    }

    public String getApprovalQuestion() {
        return stepDescription;
    }

    public boolean isHasRejection() {
        return hasRejection;
    }

    public void setHasRejection(boolean hasRejection) {
        this.hasRejection = hasRejection;
    }
}
