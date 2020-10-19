package com.namoadigital.prj001.model.VH_models;

import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_BriefDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.util.ToolBox_Inf;

import static com.namoadigital.prj001.dao.TK_Ticket_BriefDao.LOCAL_TICKET;

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
    private String ticket_forecast_date;
    private String step_forecast_start_date;
    private String step_forecast_end_date;
    private String ticket_step_id;
    private String ticket_current_step_order;
    private String step_order_seq;
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
    private int user_focus;
    private int sync_required;
    private int local_ticket;


    public Act074_TicketVH(int ticket_customer, int ticket_scn, int ticket_prefix, int ticket_code, String ticket_id, String ticket_status, String ticket_prod_desc, String ticket_site_desc, String ticket_serial, String ticket_step_desc, String ticket_origin_desc, String ticket_forecast_date, String step_forecast_start_date, String step_forecast_end_date, String ticket_step_id, String ticket_current_step_order, String step_order_seq, int ticket_step_qty, int user_focus, String schedulePk, @Nullable Integer schedule_prefix, @Nullable Integer schedule_code, @Nullable Integer schedule_exec, @Nullable String fcm_new_status, @Nullable String fcm_user_nick, @Nullable String schedule_erro_msg, int local_ticket, int sync_required) {
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
        this.ticket_forecast_date = ticket_forecast_date;
        this.step_forecast_start_date = step_forecast_start_date;
        this.step_forecast_end_date = step_forecast_end_date;
        this.ticket_step_id = ticket_step_id;
        this.ticket_current_step_order = ticket_current_step_order;
        this.step_order_seq = step_order_seq;
        this.ticket_step_qty = ticket_step_qty;
        this.schedulePk = schedulePk;
        this.schedule_prefix = schedule_prefix;
        this.schedule_code = schedule_code;
        this.schedule_exec = schedule_exec;
        this.fcm_new_status = fcm_new_status;
        this.fcm_user_nick = fcm_user_nick;
        this.schedule_erro_msg = schedule_erro_msg;
        this.user_focus = user_focus;
        this.local_ticket = local_ticket;
        this.sync_required = sync_required;
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
                        hmAux.get(TK_TicketDao.FORECAST_DATE),
                        hmAux.get(TK_Ticket_StepDao.FORECAST_START),
                        hmAux.get(TK_Ticket_StepDao.FORECAST_END),
                        hmAux.get(TK_Ticket_StepDao.STEP_ID),
                        hmAux.get(TK_TicketDao.CURRENT_STEP_ORDER),
                        hmAux.get(TK_Ticket_BriefDao.STEP_ORDER_SEQ),
                        ToolBox_Inf.convertStringToInt(hmAux.get(TK_Ticket_StepDao.STEP_QTY)),
                        hmAux.hasConsistentValue(TK_TicketDao.USER_FOCUS)? ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.USER_FOCUS)) : 1,
                        hmAux.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_PK) ? hmAux.get(MD_Schedule_ExecDao.SCHEDULE_PK) : " ",
                        hmAux.hasConsistentValue(TK_TicketDao.SCHEDULE_PREFIX) ? ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.SCHEDULE_PREFIX)) : 0,
                        hmAux.hasConsistentValue(TK_TicketDao.SCHEDULE_CODE) ? ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.SCHEDULE_CODE)) : 0,
                        hmAux.hasConsistentValue(TK_TicketDao.SCHEDULE_EXEC) ? ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.SCHEDULE_EXEC)) : 0,
                        hmAux.hasConsistentValue(MD_Schedule_ExecDao.FCM_NEW_STATUS) ? hmAux.get(MD_Schedule_ExecDao.FCM_NEW_STATUS) : " ",
                        hmAux.hasConsistentValue(MD_Schedule_ExecDao.FCM_USER_NICK) ? hmAux.get(MD_Schedule_ExecDao.FCM_USER_NICK) : " ",
                        hmAux.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_ERRO_MSG) ? hmAux.get(MD_Schedule_ExecDao.SCHEDULE_ERRO_MSG) : " ",
                        hmAux.hasConsistentValue(LOCAL_TICKET) ? Integer.valueOf(hmAux.get(LOCAL_TICKET)) : 0,
                        ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.SYNC_REQUIRED))
        );
    }

//    public static Act074_TicketVH getTicketVHObj(TK_Next_Ticket ticket, int sync_required) {
//
//        return new Act074_TicketVH(
//                ticket.getCustomerCode(),
//                ticket.getScn(),
//                ticket.getTicketPrefix(),
//                ticket.getTicketCode(),
//                ticket.getTicketPrefix() + "." + ticket.getTicketCode(),
//                ticket.getTicketStatus(),
//                ticket.getOpenProductDesc(),
//                ticket.getOpenSiteDesc(),
//                ticket.getOpenSerialId(),
//                ticket.getStepDesc(),
//                ticket.getOriginDesc(),
//                "",
//                ticket.getForecastStart(),
//                ticket.getForecastEnd(),
//                        "",
//                String.valueOf(ticket.getCurrentStepOrder()),
//                null,
//                ticket.getStepCount(),
//                1,
//                        " ",
//                       0,
//                        0,
//                        0,
//                     " ",
//                      " ",
//                  " ",
//                ticket.getTicket_local(),
//                sync_required
//        );
//    }

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

    public String getStep_forecast_start_date() {
        return step_forecast_start_date;
    }

    public void setStep_forecast_start_date(String step_forecast_start_date) {
        this.step_forecast_start_date = step_forecast_start_date;
    }

    public String getStep_forecast_end_date() {
        return step_forecast_end_date;
    }

    public void setStep_forecast_end_date(String step_forecast_end_date) {
        this.step_forecast_end_date = step_forecast_end_date;
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

    public String getStep_order_seq() {
        return step_order_seq;
    }

    public void setStep_order_seq(String step_order_seq) {
        this.step_order_seq = step_order_seq;
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

    public int getUser_focus() {
        return user_focus;
    }

    public void setUser_focus(int user_focus) {
        this.user_focus = user_focus;
    }

    public int getSync_required() {
        return sync_required;
    }

    public void setSync_required(int sync_required) {
        this.sync_required = sync_required;
    }

    public int getLocal_ticket() {
        return local_ticket;
    }

    public void setLocal_ticket(int local_ticket) {
        this.local_ticket = local_ticket;
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
                        step_forecast_start_date +"|" +
                        ticket_step_id +"|" +
                        schedulePk)
                .replace("null|","")
                .replace("null","")
                ;
    }

    public String getTicket_forecast_date() {
        return ticket_forecast_date;
    }

    public void setTicket_forecast_date(String ticket_forecast_date) {
        this.ticket_forecast_date = ticket_forecast_date;
    }
}
