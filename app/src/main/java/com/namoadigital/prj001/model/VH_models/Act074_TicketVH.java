package com.namoadigital.prj001.model.VH_models;

import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;

public class Act074_TicketVH {
//    public static final String CTRLS_SERIAL_LIST = "CTRLS_SERIAL_LIST";

    private int ticket_prefix;
    private int ticket_code;
    private String ticket_id;
    private String ticket_status;
    private String ticket_prod_desc;
    private String ticket_site_desc;
    private String ticket_serial;
    private String ticket_step_desc;
    private String ticket_origin_desc;
    private String ticket_forecast_date;
    private String ticket_step_id;
    //Add atribuots do agendamento - Ainda faz sentido?
    private String schedulePk;
    @Nullable
    private Integer schedule_prefix;
    @Nullable
    private Integer schedule_code;
    @Nullable
    private Integer schedule_exec;
    @Nullable
    private String fcm_new_status;
    @Nullable
    private String fcm_user_nick;
    @Nullable
    private String schedule_erro_msg;


    public Act074_TicketVH(int ticket_prefix, int ticket_code, String ticket_id, String ticket_status, String ticket_prod_desc, String ticket_site_desc, String ticket_serial, String ticket_step_desc, String ticket_origin_desc, String ticket_forecast_date, String ticket_step_id, String schedulePk, @Nullable Integer schedule_prefix, @Nullable Integer schedule_code, @Nullable Integer schedule_exec, @Nullable String fcm_new_status, @Nullable String fcm_user_nick, @Nullable String schedule_erro_msg) {
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_id = ticket_id;
        this.ticket_status = ticket_status;
        this.ticket_prod_desc = ticket_prod_desc;
        this.ticket_site_desc = ticket_site_desc;
        this.ticket_serial = ticket_serial;
        this.ticket_step_desc = ticket_step_desc;
        this.ticket_origin_desc = ticket_origin_desc;
        this.ticket_forecast_date = ticket_forecast_date;
        this.ticket_step_id = ticket_step_id;
        this.schedulePk = schedulePk;
        this.schedule_prefix = schedule_prefix;
        this.schedule_code = schedule_code;
        this.schedule_exec = schedule_exec;
        this.fcm_new_status = fcm_new_status;
        this.fcm_user_nick = fcm_user_nick;
        this.schedule_erro_msg = schedule_erro_msg;
    }

    public static Act074_TicketVH getTicketVHObj(HMAux hmAux) {
        //
        return null;
//                new Act074_TicketVH(
//        );
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

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public String getTicket_prod_desc() {
        return ticket_prod_desc;
    }

    public void setTicket_prod_desc(String ticket_prod_desc) {
        this.ticket_prod_desc = ticket_prod_desc;
    }

    public String getTicket_site_desc() {
        return ticket_site_desc;
    }

    public void setTicket_site_desc(String ticket_site_desc) {
        this.ticket_site_desc = ticket_site_desc;
    }

    public String getTicket_serial() {
        return ticket_serial;
    }

    public void setTicket_serial(String ticket_serial) {
        this.ticket_serial = ticket_serial;
    }

    public String getTicket_step_desc() {
        return ticket_step_desc;
    }

    public void setTicket_step_desc(String ticket_step_desc) {
        this.ticket_step_desc = ticket_step_desc;
    }

    public String getTicket_origin_desc() {
        return ticket_origin_desc;
    }

    public void setTicket_origin_desc(String ticket_origin_desc) {
        this.ticket_origin_desc = ticket_origin_desc;
    }

    public String getTicket_forecast_date() {
        return ticket_forecast_date;
    }

    public void setTicket_forecast_date(String ticket_forecast_date) {
        this.ticket_forecast_date = ticket_forecast_date;
    }

    public String getTicket_step_id() {
        return ticket_step_id;
    }

    public void setTicket_step_id(String ticket_step_id) {
        this.ticket_step_id = ticket_step_id;
    }

    public String getSchedulePk() {
        return schedulePk;
    }

    public void setSchedulePk(String schedulePk) {
        this.schedulePk = schedulePk;
    }

    @Nullable
    public Integer getSchedule_prefix() {
        return schedule_prefix;
    }

    public void setSchedule_prefix(@Nullable Integer schedule_prefix) {
        this.schedule_prefix = schedule_prefix;
    }

    @Nullable
    public Integer getSchedule_code() {
        return schedule_code;
    }

    public void setSchedule_code(@Nullable Integer schedule_code) {
        this.schedule_code = schedule_code;
    }

    @Nullable
    public Integer getSchedule_exec() {
        return schedule_exec;
    }

    public void setSchedule_exec(@Nullable Integer schedule_exec) {
        this.schedule_exec = schedule_exec;
    }

    @Nullable
    public String getFcm_new_status() {
        return fcm_new_status;
    }

    public void setFcm_new_status(@Nullable String fcm_new_status) {
        this.fcm_new_status = fcm_new_status;
    }

    @Nullable
    public String getFcm_user_nick() {
        return fcm_user_nick;
    }

    public void setFcm_user_nick(@Nullable String fcm_user_nick) {
        this.fcm_user_nick = fcm_user_nick;
    }

    @Nullable
    public String getSchedule_erro_msg() {
        return schedule_erro_msg;
    }

    public void setSchedule_erro_msg(@Nullable String schedule_erro_msg) {
        this.schedule_erro_msg = schedule_erro_msg;
    }
    //
    public String getAllFieldForFilter(){
        return  (
                ticket_prefix+ "|" +
                        ticket_code+ "|" +
                        ticket_id+ "|" +
                        ticket_status+ "|" +
                        ticket_prod_desc+ "|" +
                        ticket_site_desc+ "|" +
                        ticket_serial + "|" +
                        ticket_step_desc + "|" +
                        ticket_origin_desc + "|" +
                        ticket_forecast_date +"|" +
                        ticket_step_id +"|" +
                        schedulePk)
                .replace("null|","")
                .replace("null","")
                ;
    }
}
