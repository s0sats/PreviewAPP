package com.namoadigital.prj001.model.VH_models;

import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.TK_Next_Ticket;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act074_TicketVH {
//    public static final String CTRLS_SERIAL_LIST = "CTRLS_SERIAL_LIST";
    private String ticket_pk;
    private int ticket_prefix;
    private int ticket_code;
    private String ticket_id;
    private String ticket_status;
    private String ticket_prod_desc;
    private String ticket_site_desc;
    private String ticket_serial;
    private String ticket_step_desc;
    private String ticket_origin_desc;
    private String ticket_forecast_start_date;
    private String ticket_forecast_end_date;
    private String ticket_step_id;
    private String ticket_current_step_order;
    private int ticket_step_qty;
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


    public Act074_TicketVH(int ticket_customer, int ticket_scn, int ticket_prefix, int ticket_code, String ticket_id, String ticket_status, String ticket_prod_desc, String ticket_site_desc, String ticket_serial, String ticket_step_desc, String ticket_origin_desc, String ticket_forecast_start_date, String ticket_forecast_end_date, String ticket_step_id, String ticket_current_step_order, int ticket_step_qty, String schedulePk, @Nullable Integer schedule_prefix, @Nullable Integer schedule_code, @Nullable Integer schedule_exec, @Nullable String fcm_new_status, @Nullable String fcm_user_nick, @Nullable String schedule_erro_msg) {
        String separator = "|";
        this.ticket_pk = ticket_customer +  separator +  ticket_prefix +  separator + ticket_code +  separator +  ticket_scn;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_id = ticket_id;
        this.ticket_status = ticket_status;
        this.ticket_prod_desc = ticket_prod_desc;
        this.ticket_site_desc = ticket_site_desc;
        this.ticket_serial = ticket_serial;
        this.ticket_step_desc = ticket_step_desc;
        this.ticket_origin_desc = ticket_origin_desc;
        this.ticket_forecast_start_date = ticket_forecast_start_date;
        this.ticket_forecast_end_date = ticket_forecast_end_date;
        this.ticket_step_id = ticket_step_id;
        this.ticket_current_step_order = ticket_current_step_order;
        this.ticket_step_qty = ticket_step_qty;
        this.schedulePk = schedulePk;
        this.schedule_prefix = schedule_prefix;
        this.schedule_code = schedule_code;
        this.schedule_exec = schedule_exec;
        this.fcm_new_status = fcm_new_status;
        this.fcm_user_nick = fcm_user_nick;
        this.schedule_erro_msg = schedule_erro_msg;
    }

    public static Act074_TicketVH getTicketVHObj(HMAux hmAux) {

        return new Act074_TicketVH(
                        ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.CUSTOMER_CODE)),
                        ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.SCN)),
                        ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.TICKET_PREFIX)),
                        ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.TICKET_CODE)),
                        hmAux.get(TK_TicketDao.TICKET_ID),
                        hmAux.get(TK_TicketDao.TICKET_STATUS),
                        hmAux.get(TK_TicketDao.OPEN_PRODUCT_DESC),
                        hmAux.get(TK_TicketDao.OPEN_SITE_DESC),
                        hmAux.get(TK_TicketDao.OPEN_SERIAL_ID),
                        hmAux.get(TK_Ticket_StepDao.STEP_DESC),
                        hmAux.get(TK_TicketDao.ORIGIN_DESC),
                        hmAux.get(TK_Ticket_StepDao.FORECAST_START),
                        hmAux.get(TK_Ticket_StepDao.FORECAST_END),
                        hmAux.get(TK_Ticket_StepDao.STEP_ID),
                        hmAux.get(TK_TicketDao.CURRENT_STEP_ORDER),
                ToolBox_Inf.convertStringToInt(hmAux.get(TK_Ticket_StepDao.STEP_QTY)),
                        " ",
                       0,
                        0,
                        0,
                     " ",
                      " ",
                  " "
        );
    }

    public static Act074_TicketVH getTicketVHObj(TK_Next_Ticket ticket) {

        return new Act074_TicketVH(
                ticket.getCustomerCode(),
                ticket.getScn(),
                ticket.getTicketPrefix(),
                ticket.getTicketCode(),
                ticket.getTicketPrefix() + "." + ticket.getTicketCode(),
                ticket.getTicketStatus(),
                ticket.getOpenProductDesc(),
                ticket.getOpenSiteDesc(),
                ticket.getOpenSerialId(),
                ticket.getStepDesc(),
                ticket.getOriginDesc(),
                ticket.getForecastStart(),
                ticket.getForecastEnd(),
                        "",
                String.valueOf(ticket.getCurrentStepOrder()),
                ticket.getStepCount(),
                        " ",
                       0,
                        0,
                        0,
                     " ",
                      " ",
                  " "
        );
    }

    public String getTicket_pk() {
        return ticket_pk;
    }

    public void setTicket_pk(String ticket_pk) {
        this.ticket_pk = ticket_pk;
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

    public String getTicket_forecast_start_date() {
        return ticket_forecast_start_date;
    }

    public void setTicket_forecast_start_date(String ticket_forecast_start_date) {
        this.ticket_forecast_start_date = ticket_forecast_start_date;
    }

    public String getTicket_forecast_end_date() {
        return ticket_forecast_end_date;
    }

    public void setTicket_forecast_end_date(String ticket_forecast_end_date) {
        this.ticket_forecast_end_date = ticket_forecast_end_date;
    }

    public String getTicket_step_id() {
        return ticket_step_id;
    }

    public void setTicket_step_id(String ticket_step_id) {
        this.ticket_step_id = ticket_step_id;
    }

    public String getTicket_current_step_order() {
        return ticket_current_step_order;
    }

    public void setTicket_current_step_order(String ticket_current_step_order) {
        this.ticket_current_step_order = ticket_current_step_order;
    }

    public int getTicket_step_qty() {
        return ticket_step_qty;
    }

    public void setTicket_step_qty(int ticket_step_qty) {
        this.ticket_step_qty = ticket_step_qty;
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
                        ticket_forecast_start_date +"|" +
                        ticket_step_id +"|" +
                        schedulePk)
                .replace("null|","")
                .replace("null","")
                ;
    }
}
