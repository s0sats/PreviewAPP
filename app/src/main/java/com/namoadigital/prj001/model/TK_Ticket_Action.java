package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

public class TK_Ticket_Action {
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
    @Nullable
    @Expose
    private String action_comments;//Max 500
    @Nullable
    @Expose
    private String action_photo;
    @Nullable
    @Expose
    private String action_photo_local;
    @Nullable
    private String action_photo_name;
    @Expose
    private String action_status;
    @Expose
    private int action_photo_changed;
    private Integer action_photo_code;//somente usado para identificar se foto recebida é diferente da alterior

    public TK_Ticket_Action() {
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
        this.action_status = tk_ticket_ctrl.getCtrl_status();
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

    public String getAction_comments() {
        return action_comments;
    }

    public void setAction_comments(String action_comments) {
        this.action_comments = action_comments;
    }

    public String getAction_photo() {
        return action_photo;
    }

    public void setAction_photo(String action_photo) {
        this.action_photo = action_photo;
    }

    @Nullable
    public String getAction_photo_local() {
        return action_photo_local;
    }

    public void setAction_photo_local(@Nullable String action_photo_local) {
        this.action_photo_local = action_photo_local;
    }

    @Nullable
    public String getAction_photo_name() {
        return action_photo_name;
    }

    public void setAction_photo_name(@Nullable String action_photo_name) {
        this.action_photo_name = action_photo_name;
    }

    public String getAction_status() {
        return action_status;
    }

    public void setAction_status(String action_status) {
        this.action_status = action_status;
    }

    public int getAction_photo_changed() {
        return action_photo_changed;
    }

    public void setAction_photo_changed(int action_photo_changed) {
        this.action_photo_changed = action_photo_changed;
    }

    public Integer getAction_photo_code() {
        return action_photo_code;
    }

    public void setAction_photo_code(Integer action_photo_code) {
        this.action_photo_code = action_photo_code;
    }
}
