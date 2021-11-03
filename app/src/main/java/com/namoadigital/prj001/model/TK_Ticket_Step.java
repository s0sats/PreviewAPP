
package com.namoadigital.prj001.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

public class TK_Ticket_Step implements Serializable {
    private long customer_code;//pk
    private int ticket_prefix;//pk
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
    @Expose
    @Nullable
    private String step_start_date;
    @Nullable
    private Integer step_start_user;
    @Nullable
    private String step_start_user_nick;
    @Expose
    @Nullable
    private String step_end_date;
    @Nullable
    private Integer step_end_user;
    @Nullable
    private String step_end_user_nick;
    private String step_status;
    private int user_focus;
    private Integer has_item_check;
    @Nullable
    private Integer group_code;
    @Nullable
    private String group_desc;
    @Nullable
    private Integer zone_site_group_code;
    @Nullable
    private String zone_site_group_desc;
    @Nullable
    private String pc_level_target;
    @Nullable
    private Integer ap_group_code;
    @Nullable
    private String ap_group_desc;
    @Nullable
    private Integer ap_zone_site_group_code;
    @Nullable
    private String ap_zone_site_group_desc;
    @Nullable
    private String ap_pc_level_target;
    private int update_required;
    @Expose
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
            ctrl.get(i).setPK(this);
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

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    public int getUser_focus() {
        return user_focus;
    }

    public void setUser_focus(int user_focus) {
        this.user_focus = user_focus;
    }

    public Integer getHas_item_check() {
        return has_item_check;
    }

    public void setHas_item_check(Integer has_item_check) {
        this.has_item_check = has_item_check;
    }

    @Nullable
    public Integer getGroup_code() {
        return group_code;
    }

    public void setGroup_code(@Nullable Integer group_code) {
        this.group_code = group_code;
    }

    @Nullable
    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(@Nullable String group_desc) {
        this.group_desc = group_desc;
    }

    @Nullable
    public Integer getZone_site_group_code() {
        return zone_site_group_code;
    }

    public void setZone_site_group_code(@Nullable Integer zone_site_group_code) {
        this.zone_site_group_code = zone_site_group_code;
    }

    @Nullable
    public String getZone_site_group_desc() {
        return zone_site_group_desc;
    }

    public void setZone_site_group_desc(@Nullable String zone_site_group_desc) {
        this.zone_site_group_desc = zone_site_group_desc;
    }

    @Nullable
    public String getPc_level_target() {
        return pc_level_target;
    }

    public void setPc_level_target(@Nullable String pc_level_target) {
        this.pc_level_target = pc_level_target;
    }

    @Nullable
    public Integer getAp_group_code() {
        return ap_group_code;
    }

    public void setAp_group_code(@Nullable Integer ap_group_code) {
        this.ap_group_code = ap_group_code;
    }

    @Nullable
    public String getAp_group_desc() {
        return ap_group_desc;
    }

    public void setAp_group_desc(@Nullable String ap_group_desc) {
        this.ap_group_desc = ap_group_desc;
    }

    @Nullable
    public Integer getAp_zone_site_group_code() {
        return ap_zone_site_group_code;
    }

    public void setAp_zone_site_group_code(@Nullable Integer ap_zone_site_group_code) {
        this.ap_zone_site_group_code = ap_zone_site_group_code;
    }

    @Nullable
    public String getAp_zone_site_group_desc() {
        return ap_zone_site_group_desc;
    }

    public void setAp_zone_site_group_desc(@Nullable String ap_zone_site_group_desc) {
        this.ap_zone_site_group_desc = ap_zone_site_group_desc;
    }

    @Nullable
    public String getAp_pc_level_target() {
        return ap_pc_level_target;
    }

    public void setAp_pc_level_target(@Nullable String ap_pc_level_target) {
        this.ap_pc_level_target = ap_pc_level_target;
    }

    public ArrayList<TK_Ticket_Ctrl> getCtrl() {
        return ctrl;
    }

    public void setCtrl(ArrayList<TK_Ticket_Ctrl> ctrl) {
        this.ctrl = ctrl;
    }

    public static String getStepNumFormatted(int step_order, Integer step_order_seq) {
        return step_order + (step_order_seq == null ? "" : "." + step_order_seq);
    }
}
