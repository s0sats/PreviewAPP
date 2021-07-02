package com.namoadigital.prj001.ui.act070.model;

public abstract class StepAbstractProcess extends BaseStep{
    protected String stepType;
    protected String processStatus;
    protected boolean currentStep;
    protected boolean stepAlreadyCheckedIn;
    protected boolean processPlanned;
    //TicketSeq
    protected int processTkSeq;
    protected int processTkSeqTmp;
    protected boolean isProductDifferentThanTicket;
    protected boolean isSerialDifferentThanTicket;
    protected boolean backProcessHighlight;
    protected Integer startUserCode;

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
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

    public boolean isStepAlreadyCheckedIn() {
        return stepAlreadyCheckedIn;
    }

    public void setStepAlreadyCheckedIn(boolean stepAlreadyCheckedIn) {
        this.stepAlreadyCheckedIn = stepAlreadyCheckedIn;
    }

    public boolean isProcessPlanned() {
        return processPlanned;
    }

    public void setProcessPlanned(boolean processPlanned) {
        this.processPlanned = processPlanned;
    }

    public int getProcessTkSeq() {
        return processTkSeq;
    }

    public void setProcessTkSeq(int processTkSeq) {
        this.processTkSeq = processTkSeq;
    }

    public int getProcessTkSeqTmp() {
        return processTkSeqTmp;
    }

    public void setProcessTkSeqTmp(int processTkSeqTmp) {
        this.processTkSeqTmp = processTkSeqTmp;
    }

    public boolean isProductDifferentThanTicket() {
        return isProductDifferentThanTicket;
    }

    public void setProductDifferentThanTicket(boolean productDifferentThanTicket) {
        isProductDifferentThanTicket = productDifferentThanTicket;
    }

    public boolean isSerialDifferentThanTicket() {
        return isSerialDifferentThanTicket;
    }

    public void setSerialDifferentThanTicket(boolean serialDifferentThanTicket) {
        isSerialDifferentThanTicket = serialDifferentThanTicket;
    }

    public void setBackProcessHighlight(boolean backProcessHighlight) {
        this.backProcessHighlight = backProcessHighlight;
    }

    public boolean isBackProcessHighlight() {
        return backProcessHighlight;
    }

    public Integer getStartUserCode() {
        return startUserCode;
    }

    public void setStartUserCode(Integer startUserCode) {
        this.startUserCode = startUserCode;
    }
}
