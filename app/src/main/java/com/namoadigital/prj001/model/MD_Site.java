package com.namoadigital.prj001.model;

import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 16/01/2017.
 */

public class MD_Site {

    private long customer_code;
    private String site_code;
    private String site_id;
    private String site_desc;
    private int io_control;
    private Integer reason_code;
    private int inbound_auto_create;
    private int in_allow_new_item;
    private int in_put_away_process;
    private Integer in_zone_code_conf;
    private Integer in_local_code_conf;
    private int in_done_automatic;
    private int out_allow_new_item;
    private int out_picking_process;
    private Integer out_zone_code_picking;
    private Integer out_local_code_picking;
    private int out_done_automatic;
    //LUCHE - 13/01/2021 - Controle de licença
    private Integer license_enabled;
    private Integer free_executions_max;
    private Integer free_executions_count;
    private int app_executions_count;
    private int license_blocked;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
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

    public int getIo_control() {
        return io_control;
    }

    public void setIo_control(int io_control) {
        this.io_control = io_control;
    }

    public Integer getReason_code() {
        return reason_code;
    }

    public void setReason_code(Integer reason_code) {
        this.reason_code = reason_code;
    }

    public int getInbound_auto_create() {
        return inbound_auto_create;
    }

    public void setInbound_auto_create(int inbound_auto_create) {
        this.inbound_auto_create = inbound_auto_create;
    }

    public int getIn_allow_new_item() {
        return in_allow_new_item;
    }

    public void setIn_allow_new_item(int in_allow_new_item) {
        this.in_allow_new_item = in_allow_new_item;
    }

    public int getIn_put_away_process() {
        return in_put_away_process;
    }

    public void setIn_put_away_process(int in_put_away_process) {
        this.in_put_away_process = in_put_away_process;
    }

    public Integer getIn_zone_code_conf() {
        return in_zone_code_conf;
    }

    public void setIn_zone_code_conf(Integer in_zone_code_conf) {
        this.in_zone_code_conf = in_zone_code_conf;
    }

    public Integer getIn_local_code_conf() {
        return in_local_code_conf;
    }

    public void setIn_local_code_conf(Integer in_local_code_conf) {
        this.in_local_code_conf = in_local_code_conf;
    }

    public int getIn_done_automatic() {
        return in_done_automatic;
    }

    public void setIn_done_automatic(int in_done_automatic) {
        this.in_done_automatic = in_done_automatic;
    }

    public int getOut_allow_new_item() {
        return out_allow_new_item;
    }

    public void setOut_allow_new_item(int out_allow_new_item) {
        this.out_allow_new_item = out_allow_new_item;
    }

    public int getOut_picking_process() {
        return out_picking_process;
    }

    public void setOut_picking_process(int out_picking_process) {
        this.out_picking_process = out_picking_process;
    }

    public Integer getOut_zone_code_picking() {
        return out_zone_code_picking;
    }

    public void setOut_zone_code_picking(Integer out_zone_code_picking) {
        this.out_zone_code_picking = out_zone_code_picking;
    }

    public Integer getOut_local_code_picking() {
        return out_local_code_picking;
    }

    public void setOut_local_code_picking(Integer out_local_code_picking) {
        this.out_local_code_picking = out_local_code_picking;
    }

    public int getOut_done_automatic() {
        return out_done_automatic;
    }

    public void setOut_done_automatic(int out_done_automatic) {
        this.out_done_automatic = out_done_automatic;
    }

    public Integer getLicense_enabled() {
        return license_enabled;
    }

    public void setLicense_enabled(Integer license_enabled) {
        this.license_enabled = license_enabled;
    }

    public Integer getFree_executions_max() {
        return free_executions_max;
    }

    public void setFree_executions_max(Integer free_executions_max) {
        this.free_executions_max = free_executions_max;
    }

    public Integer getFree_executions_count() {
        return free_executions_count;
    }

    public void setFree_executions_count(Integer free_executions_count) {
        this.free_executions_count = free_executions_count;
    }

    public int getApp_executions_count() {
        return app_executions_count;
    }

    public void setApp_executions_count(int app_executions_count) {
        this.app_executions_count = app_executions_count;
    }

    public int getLicense_blocked() {
        return license_blocked;
    }

    public void setLicense_blocked(int license_blocked) {
        this.license_blocked = license_blocked;
    }

    public void updateLicenseBlocked() {
        if(this.license_enabled != null && this.license_enabled == 0) {
            setLicense_blocked(this.free_executions_max - (this.free_executions_count + this.app_executions_count) <= 0 ? 1 : 0);
        }
    }

    public static boolean isValid(MD_Site mdSite){
        return  mdSite != null
                && mdSite.getCustomer_code() > 0
                && ToolBox_Inf.convertStringToInt(mdSite.getSite_code()) > 0;
    }
}
