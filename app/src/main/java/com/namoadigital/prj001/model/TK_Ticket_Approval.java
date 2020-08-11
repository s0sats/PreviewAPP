
package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

public class TK_Ticket_Approval {
    @Expose
    private long customer_code;//pk
    @Expose
    private int ticket_prefix;//pk
    @Expose
    private int ticket_code;//pk
    @Expose
    private int ticket_seq;//pk
    @Expose
    private int step_code;//pk
    @Expose
    private String approval_status;
    @Nullable
    private String approval_question;
    @Nullable
    private String approval_type;
    @Nullable
    @Expose
    private String approval_comments;

    public TK_Ticket_Approval() {
        this.customer_code = -1;
        this.ticket_prefix = -1;
        this.ticket_code = -1;
        this.ticket_seq = -1;
        this.step_code = -1;
    }

    public void setPK(TK_Ticket_Ctrl tk_ticket_ctrl) {
        this.customer_code = tk_ticket_ctrl.getCustomer_code();
        this.ticket_prefix = tk_ticket_ctrl.getTicket_prefix();
        this.ticket_code = tk_ticket_ctrl.getTicket_code();
        this.ticket_seq = tk_ticket_ctrl.getTicket_seq();
        this.step_code = tk_ticket_ctrl.getStep_code();
        this.approval_status = tk_ticket_ctrl.getCtrl_status();
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
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

    public int getTicket_seq() {
        return ticket_seq;
    }

    public void setTicket_seq(int ticket_seq) {
        this.ticket_seq = ticket_seq;
    }

    public int getStep_code() {
        return step_code;
    }

    public void setStep_code(int step_code) {
        this.step_code = step_code;
    }

    @Nullable
    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(@Nullable String approval_status) {
        this.approval_status = approval_status;
    }

    @Nullable
    public String getApproval_question() {
        return approval_question;
    }

    public void setApproval_question(@Nullable String approval_question) {
        this.approval_question = approval_question;
    }

    @Nullable
    public String getApproval_type() {
        return approval_type;
    }

    public void setApproval_type(@Nullable String approval_type) {
        this.approval_type = approval_type;
    }

    @Nullable
    public String getApproval_comments() {
        return approval_comments;
    }

    public void setApproval_comments(@Nullable String approval_comments) {
        this.approval_comments = approval_comments;
    }
}
