package com.namoadigital.prj001.model;

import androidx.annotation.Nullable;

public class WS_TK_Ticket_Ctrl_Obj{
    private int ticket_seq;
    private Integer ticket_seq_tmp;
    private String ctrl_type;
    private String ctrl_start_date;
    private String ctrl_end_date;
    @Nullable
    private TK_Ticket_Action action;
    @Nullable
    private TK_Ticket_Approval approval;

    public int getTicket_seq() {
        return ticket_seq;
    }

    public void setTicket_seq(int ticket_seq) {
        this.ticket_seq = ticket_seq;
    }

    public Integer getTicket_seq_tmp() {
        return ticket_seq_tmp;
    }

    public void setTicket_seq_tmp(Integer ticket_seq_tmp) {
        this.ticket_seq_tmp = ticket_seq_tmp;
    }

    public String getCtrl_type() {
        return ctrl_type;
    }

    public void setCtrl_type(String ctrl_type) {
        this.ctrl_type = ctrl_type;
    }

    public String getCtrl_start_date() {
        return ctrl_start_date;
    }

    public void setCtrl_start_date(String ctrl_start_date) {
        this.ctrl_start_date = ctrl_start_date;
    }

    public String getCtrl_end_date() {
        return ctrl_end_date;
    }

    public void setCtrl_end_date(String ctrl_end_date) {
        this.ctrl_end_date = ctrl_end_date;
    }

    @Nullable
    public TK_Ticket_Action getAction() {
        return action;
    }

    public void setAction(@Nullable TK_Ticket_Action action) {
        this.action = action;
    }

    @Nullable
    public TK_Ticket_Approval getApproval() {
        return approval;
    }

    public void setApproval(@Nullable TK_Ticket_Approval approval) {
        this.approval = approval;
    }
}