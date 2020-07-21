package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

public class TK_Ticket_Measure {
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
    private int measure_tp_code;
    private String measure_tp_id;
    private String measure_tp_desc;
    private float measure_value;
    private String value_sufix;
    private String measure_date;
    private String measure_info;

    public TK_Ticket_Measure() {
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

    public int getMeasure_tp_code() {
        return measure_tp_code;
    }

    public void setMeasure_tp_code(int measure_tp_code) {
        this.measure_tp_code = measure_tp_code;
    }

    public String getMeasure_tp_id() {
        return measure_tp_id;
    }

    public void setMeasure_tp_id(String measure_tp_id) {
        this.measure_tp_id = measure_tp_id;
    }

    public String getMeasure_tp_desc() {
        return measure_tp_desc;
    }

    public void setMeasure_tp_desc(String measure_tp_desc) {
        this.measure_tp_desc = measure_tp_desc;
    }

    public float getMeasure_value() {
        return measure_value;
    }

    public void setMeasure_value(float measure_value) {
        this.measure_value = measure_value;
    }

    public String getValue_sufix() {
        return value_sufix;
    }

    public void setValue_sufix(String value_sufix) {
        this.value_sufix = value_sufix;
    }

    public String getMeasure_date() {
        return measure_date;
    }

    public void setMeasure_date(String measure_date) {
        this.measure_date = measure_date;
    }

    public String getMeasure_info() {
        return measure_info;
    }

    public void setMeasure_info(String measure_info) {
        this.measure_info = measure_info;
    }
}
