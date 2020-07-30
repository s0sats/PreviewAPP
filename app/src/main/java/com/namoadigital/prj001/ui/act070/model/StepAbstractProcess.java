package com.namoadigital.prj001.ui.act070.model;

public abstract class StepAbstractProcess extends BaseStep{
    protected String stepType;
    protected String processStatus;
    protected boolean currentStep;
    protected boolean stepAlreadyCheckedIn;
    protected boolean processPlanned;
    //TicketSeq
    protected int processTkSeq;

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
}
