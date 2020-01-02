package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class TK_Ticket_Ctrl {
    @Expose
    private long customer_code;//pk
    @Expose
    private int ticket_prefix;//pk
    @Expose
    private int ticket_code;//pk
    @Expose
    private int ticket_seq;//pk
    @Expose
    private String ctrl_type;
    @Expose
    private int site_code;
    private String site_id;
    private String site_desc;
    @Expose
    private int operation_code;
    private String operation_id;
    private String operation_desc;
    @Expose
    private int product_code;
    private String product_id;
    private String product_desc;
    @Expose
    private int serial_code;
    private String serial_id;
    private String ctrl_start_date;
    private int ctrl_start_user;
    private String ctrl_start_user_name;
    @Nullable
    private String ctrl_end_date;
    private Integer ctrl_end_user;
    @Nullable
    private String ctrl_end_user_name;
    @Expose
    private String ctrl_status;
    @Expose
    private Integer partner_code;
    @Nullable
    private String partner_id;
    @Nullable
    private String partner_desc;
    @Expose
    @Nullable
    private TK_Ticket_Action action;
    @Expose
    @Nullable
    private TK_Ticket_Measure measure;

    public TK_Ticket_Ctrl() {
        this.customer_code = -1;
        this.ticket_prefix = -1;
        this.ticket_code = -1;
        this.ticket_seq = -1;
    }

    public void setPK(TK_Ticket tk_ticket) {
        this.customer_code = tk_ticket.getCustomer_code();
        this.ticket_prefix = tk_ticket.getTicket_prefix();
        this.ticket_code = tk_ticket.getTicket_code();
        //Seta a PK no tipo do controle
        switch (this.ctrl_type) {
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                this.action.setPK(this);
                break;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                this.measure.setPK(this);
                break;
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

    public int getTicket_seq() {
        return ticket_seq;
    }

    public void setTicket_seq(int ticket_seq) {
        this.ticket_seq = ticket_seq;
    }

    public String getCtrl_type() {
        return ctrl_type;
    }

    public void setCtrl_type(String ctrl_type) {
        this.ctrl_type = ctrl_type;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }

    public int getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(int operation_code) {
        this.operation_code = operation_code;
    }

    public String getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(String operation_id) {
        this.operation_id = operation_id;
    }

    public String getOperation_desc() {
        return operation_desc;
    }

    public void setOperation_desc(String operation_desc) {
        this.operation_desc = operation_desc;
    }

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public int getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(int serial_code) {
        this.serial_code = serial_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public String getCtrl_start_date() {
        return ctrl_start_date;
    }

    public void setCtrl_start_date(String ctrl_start_date) {
        this.ctrl_start_date = ctrl_start_date;
    }

    public int getCtrl_start_user() {
        return ctrl_start_user;
    }

    public void setCtrl_start_user(int ctrl_start_user) {
        this.ctrl_start_user = ctrl_start_user;
    }

    public String getCtrl_start_user_name() {
        return ctrl_start_user_name;
    }

    public void setCtrl_start_user_name(String ctrl_start_user_name) {
        this.ctrl_start_user_name = ctrl_start_user_name;
    }

    @Nullable
    public String getCtrl_end_date() {
        return ctrl_end_date;
    }

    public void setCtrl_end_date(@Nullable String ctrl_end_date) {
        this.ctrl_end_date = ctrl_end_date;
    }

    public Integer getCtrl_end_user() {
        return ctrl_end_user;
    }

    public void setCtrl_end_user(Integer ctrl_end_user) {
        this.ctrl_end_user = ctrl_end_user;
    }

    @Nullable
    public String getCtrl_end_user_name() {
        return ctrl_end_user_name;
    }

    public void setCtrl_end_user_name(@Nullable String ctrl_end_user_name) {
        this.ctrl_end_user_name = ctrl_end_user_name;
    }

    public String getCtrl_status() {
        return ctrl_status;
    }

    public void setCtrl_status(String ctrl_status) {
        this.ctrl_status = ctrl_status;
    }

    public Integer getPartner_code() {
        return partner_code;
    }

    public void setPartner_code(Integer partner_code) {
        this.partner_code = partner_code;
    }

    @Nullable
    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(@Nullable String partner_id) {
        this.partner_id = partner_id;
    }

    @Nullable
    public String getPartner_desc() {
        return partner_desc;
    }

    public void setPartner_desc(@Nullable String partner_desc) {
        this.partner_desc = partner_desc;
    }

    public TK_Ticket_Action getAction() {
        return action;
    }

    public void setAction(TK_Ticket_Action action) {
        this.action = action;
    }

    public TK_Ticket_Measure getMeasure() {
        return measure;
    }

    public void setMeasure(TK_Ticket_Measure measure) {
        this.measure = measure;
    }
}
