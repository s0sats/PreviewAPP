package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

public class MD_Schedule_Exec {
    private long customer_code;
    private int schedule_prefix;
    private int schedule_code;
    private int schedule_exec;
    private String schedule_desc;
    private String status;
    private int site_code;
    private String site_id;
    private String site_desc;
    private int operation_code;
    private String operation_id;
    private String operation_desc;
    private int product_code;
    private String product_id;
    private String product_desc;
    @Nullable
    private Integer serial_code;
    @Nullable
    private String serial_id;
    private int custom_form_type;
    private String custom_form_type_desc;
    private int custom_form_code;
    private int custom_form_version;
    private String custom_form_desc;
    private String date_start;
    private String date_end;
    @Nullable
    private String comments;
    private int require_serial;
    private int allow_new_serial_cl;
    private int require_serial_done;
    private int sync_process;

    /**
     * LUCHE - 14/02/2020
     *
     * Valida se o agendamento passado é valido, ou seja, se obj com valores de pk preenchidos.
     * @param md_schedule_exec
     * @return
     */
    public static boolean isValidScheduleExec(MD_Schedule_Exec md_schedule_exec){
        return  md_schedule_exec != null
                && md_schedule_exec.getCustomer_code() > 0
                && md_schedule_exec.getSchedule_prefix() > 0
                && md_schedule_exec.getSchedule_code() > 0
                && md_schedule_exec.getSchedule_exec() > 0;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCustom_form_type_desc() {
        return custom_form_type_desc;
    }

    public void setCustom_form_type_desc(String custom_form_type_desc) {
        this.custom_form_type_desc = custom_form_type_desc;
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

    public String getCustom_form_desc() {
        return custom_form_desc;
    }

    public void setCustom_form_desc(String custom_form_desc) {
        this.custom_form_desc = custom_form_desc;
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

    public int getRequire_serial() {
        return require_serial;
    }

    public void setRequire_serial(int require_serial) {
        this.require_serial = require_serial;
    }

    public int getAllow_new_serial_cl() {
        return allow_new_serial_cl;
    }

    public void setAllow_new_serial_cl(int allow_new_serial_cl) {
        this.allow_new_serial_cl = allow_new_serial_cl;
    }

    public int getRequire_serial_done() {
        return require_serial_done;
    }

    public void setRequire_serial_done(int require_serial_done) {
        this.require_serial_done = require_serial_done;
    }

    public int getSync_process() {
        return sync_process;
    }

    public void setSync_process(int sync_process) {
        this.sync_process = sync_process;
    }
}
