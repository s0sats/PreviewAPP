package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_TK_Ticket_Save_Rec_Result_Step {
    private int customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int step_code;
    private String step_desc;
    private String ret_status;
    private String ret_msg;
    private ArrayList<T_TK_Ticket_Save_Rec_Result_Ctrl> ctrl = new ArrayList<>();

    public int getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(int customer_code) {
        this.customer_code = customer_code;
    }

    public int getTicket_prefix() {
        return ticket_prefix;
    }

    public void setTicket_prefix(int ticket_prefix) {
        this.ticket_prefix = ticket_prefix;
    }

    public int getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(int ticket_code) {
        this.ticket_code = ticket_code;
    }

    public int getStep_code() {
        return step_code;
    }

    public void setStep_code(int step_code) {
        this.step_code = step_code;
    }

    public String getRet_status() {
        return ret_status;
    }

    public void setRet_status(String ret_status) {
        this.ret_status = ret_status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public String getStep_desc() {
        return step_desc;
    }

    public void setStep_desc(String step_desc) {
        this.step_desc = step_desc;
    }

    public ArrayList<T_TK_Ticket_Save_Rec_Result_Ctrl> getCtrl() {
        return ctrl;
    }

    public void setCtrl(ArrayList<T_TK_Ticket_Save_Rec_Result_Ctrl> ctrl) {
        this.ctrl = ctrl;
    }

    public static class T_TK_Ticket_Save_Rec_Result_Ctrl{
        public int ticket_seq_tmp;

        public int getTicket_seq_tmp() {
            return ticket_seq_tmp;
        }

        public void setTicket_seq_tmp(int ticket_seq_tmp) {
            this.ticket_seq_tmp = ticket_seq_tmp;
        }
    }
}
