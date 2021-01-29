
package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class TK_Ticket_Approval_Rejection implements Serializable {
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
    private int seq;//pk
    @Expose
    private String rejection_comments;
    @Expose
    private String rejection_date;
    private int rejection_user;
    private String rejection_user_nick;

    public TK_Ticket_Approval_Rejection() {
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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getRejection_comments() {
        return rejection_comments;
    }

    public void setRejection_comments(String rejection_comments) {
        this.rejection_comments = rejection_comments;
    }

    public String getRejection_date() {
        return rejection_date;
    }

    public void setRejection_date(String rejection_date) {
        this.rejection_date = rejection_date;
    }

    public int getRejection_user() {
        return rejection_user;
    }

    public void setRejection_user(int rejection_user) {
        this.rejection_user = rejection_user;
    }

    public String getRejection_user_nick() {
        return rejection_user_nick;
    }

    public void setRejection_user_nick(String rejection_user_nick) {
        this.rejection_user_nick = rejection_user_nick;
    }
}
