package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class WS_TK_Ticket_Step_Obj{
    private int step_code = -1;
    private String step_start_date;
    private String step_end_date;
    private ArrayList<WS_TK_Ticket_Ctrl_Obj> ctrl = new ArrayList<>();

    public int getStep_code() {
        return step_code;
    }

    public void setStep_code(int step_code) {
        this.step_code = step_code;
    }

    public String getStep_start_date() {
        return step_start_date;
    }

    public void setStep_start_date(String step_start_date) {
        this.step_start_date = step_start_date;
    }

    public String getStep_end_date() {
        return step_end_date;
    }

    public void setStep_end_date(String step_end_date) {
        this.step_end_date = step_end_date;
    }

    public ArrayList<WS_TK_Ticket_Ctrl_Obj> getCtrl() {
        return ctrl;
    }

    public void setCtrl(ArrayList<WS_TK_Ticket_Ctrl_Obj> ctrl) {
        this.ctrl = ctrl;
    }
}