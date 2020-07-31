package com.namoadigital.prj001.ui.act070.model;

public class StepProcessBtn extends BaseStep {

    private String processType;

    public StepProcessBtn() {
    }
    public StepProcessBtn(int stepCode,String stepTtl, String processType){
        this.stepCode = stepCode;
        this.stepDescription = stepTtl;
        this.processType = processType;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }
}
