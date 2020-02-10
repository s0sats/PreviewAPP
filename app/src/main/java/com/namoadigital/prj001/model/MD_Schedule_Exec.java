package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

public class MD_Schedule_Exec {
    private long customer_code;
    private int schedule_prefix;
    private int schedule_code;
    private int schedule_exec;
    private String schedule_desc;
    private int site_code;
    private int operation_code;
    private int product_code;
    @Nullable
    private Integer serial_code;
    @Nullable
    private String serial_id;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private String date_start;
    private String date_end;
    @Nullable
    private String comments;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSchedule_prefix() {
        return schedule_prefix;
    }

    public void setSchedule_prefix(int schedule_prefix) {
        this.schedule_prefix = schedule_prefix;
    }

    public int getSchedule_code() {
        return schedule_code;
    }

    public void setSchedule_code(int schedule_code) {
        this.schedule_code = schedule_code;
    }

    public int getSchedule_exec() {
        return schedule_exec;
    }

    public void setSchedule_exec(int schedule_exec) {
        this.schedule_exec = schedule_exec;
    }

    public String getSchedule_desc() {
        return schedule_desc;
    }

    public void setSchedule_desc(String schedule_desc) {
        this.schedule_desc = schedule_desc;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public int getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(int operation_code) {
        this.operation_code = operation_code;
    }

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    @Nullable
    public Integer getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(@Nullable Integer serial_code) {
        this.serial_code = serial_code;
    }

    @Nullable
    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(@Nullable String serial_id) {
        this.serial_id = serial_id;
    }

    public int getCustom_form_type() {
        return custom_form_type;
    }

    public void setCustom_form_type(int custom_form_type) {
        this.custom_form_type = custom_form_type;
    }

    public int getCustom_form_code() {
        return custom_form_code;
    }

    public void setCustom_form_code(int custom_form_code) {
        this.custom_form_code = custom_form_code;
    }

    public int getCustom_form_version() {
        return custom_form_version;
    }

    public void setCustom_form_version(int custom_form_version) {
        this.custom_form_version = custom_form_version;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    @Nullable
    public String getComments() {
        return comments;
    }

    public void setComments(@Nullable String comments) {
        this.comments = comments;
    }
}
