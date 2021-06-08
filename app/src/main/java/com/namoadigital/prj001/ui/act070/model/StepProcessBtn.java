package com.namoadigital.prj001.ui.act070.model;

public class StepProcessBtn extends BaseStep {

    private String processType;

    public StepProcessBtn() {
    }
    /**
     * BARRIONUEVO 07-06-2021
     * Controle de acesso por foco do step.
     * @return
     */
    public StepProcessBtn(int stepCode,String stepTtl, String processType, boolean userFocus){
        this.stepCode = stepCode;
        this.stepDescription = stepTtl;
        this.processType = processType;
        this.userFocus = userFocus;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }
}
