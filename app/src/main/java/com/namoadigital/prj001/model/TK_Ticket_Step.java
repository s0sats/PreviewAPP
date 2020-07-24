
package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class TK_Ticket_Step {
    @Expose
    private long customer_code;//pk
    @Expose
    private int ticket_prefix;//pk
    @Expose
    private int ticket_code;//pk
    @Expose
    private int step_code;
    @Nullable
    private String step_id;
    @Nullable
    private String step_desc;
    private int step_order;
    @Nullable
    private Integer step_order_seq;
    @Nullable
    private String forecast_start;
    @Nullable
    private String forecast_end;
    private String exec_type;
    private int scan_serial;
    private int allow_new_obj;
    private int move_next_step;
    @Nullable
    private String step_start_date;
    @Nullable
    private Integer step_start_user;
    @Nullable
    private String step_start_user_nick;
    @Nullable
    private String step_end_date;
    @Nullable
    private Integer step_end_user;
    @Nullable
    private String step_end_user_nick;
    private String step_status;
    private ArrayList<TK_Ticket_Ctrl> ctrl = new ArrayList<>();

    public TK_Ticket_Step() {
        this.customer_code = -1;
        this.ticket_prefix = -1;
        this.ticket_code = -1;
        this.step_code = -1;
    }

    public void setPK(TK_Ticket tk_ticket) {
        this.customer_code = tk_ticket.getCustomer_code();
        this.ticket_prefix = tk_ticket.getTicket_prefix();
        this.ticket_code = tk_ticket.getTicket_code();
        //
        for (int i = 0; i < ctrl.size(); i++) {
            ctrl.get(i).setPK(tk_ticket);
        }
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

    public int getStep_code() {
        return step_code;
    }

    public void setStep_code(int step_code) {
        this.step_code = step_code;
    }

    @Nullable
    public String getStep_id() {
        return step_id;
    }

    public void setStep_id(@Nullable String step_id) {
        this.step_id = step_id;
    }

    @Nullable
    public String getStep_desc() {
        return step_desc;
    }

    public void setStep_desc(@Nullable String step_desc) {
        this.step_desc = step_desc;
    }

    public int getStep_order() {
        return step_order;
    }

    public void setStep_order(int step_order) {
        this.step_order = step_order;
    }

    @Nullable
    public Integer getStep_order_seq() {
        return step_order_seq;
    }

    public void setStep_order_seq(@Nullable Integer step_order_seq) {
        this.step_order_seq = step_order_seq;
    }

    @Nullable
    public String getForecast_start() {
        return forecast_start;
    }

    public void setForecast_start(@Nullable String forecast_start) {
        this.forecast_start = forecast_start;
    }

    @Nullable
    public String getForecast_end() {
        return forecast_end;
    }

    public void setForecast_end(@Nullable String forecast_end) {
        this.forecast_end = forecast_end;
    }

    public String getExec_type() {
        return exec_type;
    }

    public void setExec_type(String exec_type) {
        this.exec_type = exec_type;
    }

    public int getScan_serial() {
        return scan_serial;
    }

    public void setScan_serial(int scan_serial) {
        this.scan_serial = scan_serial;
    }

    public int getAllow_new_obj() {
        return allow_new_obj;
    }

    public void setAllow_new_obj(int allow_new_obj) {
        this.allow_new_obj = allow_new_obj;
    }

    public int getMove_next_step() {
        return move_next_step;
    }

    public void setMove_next_step(int move_next_step) {
        this.move_next_step = move_next_step;
    }

    @Nullable
    public String getStep_start_date() {
        return step_start_date;
    }

    public void setStep_start_date(@Nullable String step_start_date) {
        this.step_start_date = step_start_date;
    }

    @Nullable
    public Integer getStep_start_user() {
        return step_start_user;
    }

    public void setStep_start_user(@Nullable Integer step_start_user) {
        this.step_start_user = step_start_user;
    }

    @Nullable
    public String getStep_start_user_nick() {
        return step_start_user_nick;
    }

    public void setStep_start_user_nick(@Nullable String step_start_user_nick) {
        this.step_start_user_nick = step_start_user_nick;
    }

    @Nullable
    public String getStep_end_date() {
        return step_end_date;
    }

    public void setStep_end_date(@Nullable String step_end_date) {
        this.step_end_date = step_end_date;
    }

    @Nullable
    public Integer getStep_end_user() {
        return step_end_user;
    }

    public void setStep_end_user(@Nullable Integer step_end_user) {
        this.step_end_user = step_end_user;
    }

    @Nullable
    public String getStep_end_user_nick() {
        return step_end_user_nick;
    }

    public void setStep_end_user_nick(@Nullable String step_end_user_nick) {
        this.step_end_user_nick = step_end_user_nick;
    }

    public String getStep_status() {
        return step_status;
    }

    public void setStep_status(String step_status) {
        this.step_status = step_status;
    }

    public ArrayList<TK_Ticket_Ctrl> getCtrl() {
        return ctrl;
    }

    public void setCtrl(ArrayList<TK_Ticket_Ctrl> ctrl) {
        this.ctrl = ctrl;
    }
}
