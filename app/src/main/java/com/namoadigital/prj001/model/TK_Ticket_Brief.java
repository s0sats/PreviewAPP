package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TK_Ticket_Brief {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private String ticket_id;
    private int scn;
    @SerializedName("open_site_code")
    @Expose
    private Integer open_site_code;
    @SerializedName("open_site_desc")
    @Expose
    private String open_site_desc;
    @SerializedName("open_product_desc")
    @Expose
    private String open_product_desc;
    @SerializedName("open_serial_id")
    @Expose
    private String open_serial_id;
    @SerializedName("current_step_order")
    @Expose
    private Integer current_step_order;
    @SerializedName("ticket_status")
    @Expose
    private String ticket_status;
    @SerializedName("origin_desc")
    @Expose
    private String origin_desc;
    @SerializedName("step_desc")
    @Expose
    private String step_desc;
    @SerializedName("forecast_start")
    @Expose
    private String forecast_start;
    @SerializedName("forecast_end")
    @Expose
    private String forecast_end;
    @SerializedName("step_count")
    @Expose
    private Integer step_count;

    private int fcm;


    public TK_Ticket_Brief() {
    }

    public TK_Ticket_Brief(long customer_code, int ticket_prefix, int ticket_code, String ticket_id, int scn, Integer open_site_code, String open_site_desc, String open_product_desc, String open_serial_id, Integer current_step_order, String ticket_status, String origin_desc, String step_desc, String forecast_start, String forecast_end, Integer step_count, int fcm) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_id = ticket_id;
        this.scn = scn;
        this.open_site_code = open_site_code;
        this.open_site_desc = open_site_desc;
        this.open_product_desc = open_product_desc;
        this.open_serial_id = open_serial_id;
        this.current_step_order = current_step_order;
        this.ticket_status = ticket_status;
        this.origin_desc = origin_desc;
        this.step_desc = step_desc;
        this.forecast_start = forecast_start;
        this.forecast_end = forecast_end;
        this.step_count = step_count;
        this.fcm = fcm;
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

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getScn() {
        return scn;
    }

    public void setScn(int scn) {
        this.scn = scn;
    }

    public Integer getOpen_site_code() {
        return open_site_code;
    }

    public void setOpen_site_code(Integer open_site_code) {
        this.open_site_code = open_site_code;
    }

    public String getOpen_site_desc() {
        return open_site_desc;
    }

    public void setOpen_site_desc(String open_site_desc) {
        this.open_site_desc = open_site_desc;
    }

    public String getOpen_product_desc() {
        return open_product_desc;
    }

    public void setOpen_product_desc(String open_product_desc) {
        this.open_product_desc = open_product_desc;
    }

    public String getOpen_serial_id() {
        return open_serial_id;
    }

    public void setOpen_serial_id(String open_serial_id) {
        this.open_serial_id = open_serial_id;
    }

    public Integer getCurrent_step_order() {
        return current_step_order;
    }

    public void setCurrent_step_order(Integer current_step_order) {
        this.current_step_order = current_step_order;
    }

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public String getOrigin_desc() {
        return origin_desc;
    }

    public void setOrigin_desc(String origin_desc) {
        this.origin_desc = origin_desc;
    }

    public String getStep_desc() {
        return step_desc;
    }

    public void setStep_desc(String step_desc) {
        this.step_desc = step_desc;
    }

    public String getForecast_start() {
        return forecast_start;
    }

    public void setForecast_start(String forecast_start) {
        this.forecast_start = forecast_start;
    }

    public String getForecast_end() {
        return forecast_end;
    }

    public void setForecast_end(String forecast_end) {
        this.forecast_end = forecast_end;
    }

    public Integer getStep_count() {
        return step_count;
    }

    public void setStep_count(Integer step_count) {
        this.step_count = step_count;
    }

    public int getFcm() {
        return fcm;
    }

    public void setFcm(int fcm) {
        this.fcm = fcm;
    }

}
