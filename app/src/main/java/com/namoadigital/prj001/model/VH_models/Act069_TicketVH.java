package com.namoadigital.prj001.model.VH_models;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act069_TicketVH {
    public static final String CTRLS_SERIAL_LIST = "CTRLS_SERIAL_LIST";

    private int ticket_prefix;
    private int ticket_code;
    private String ticket_id;
    private String ticket_status;
    private String type_path;
    private String type_desc;
    private String open_comments;
    private String open_date;
    private String forecast_date;
    private String current_site_desc;
    private String current_product_desc;
    private String current_serial_id;
    private int sync_required;
    private String ctrls_serial_list;

    public Act069_TicketVH() {
    }

    public Act069_TicketVH(int ticket_prefix, int ticket_code, String ticket_id, String ticket_status, String type_path, String type_desc, String open_comments, String open_date, String forecast_date, String current_site_desc, String current_product_desc, String current_serial_id,int sync_required,String ctrls_serial_list) {
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_id = ticket_id;
        this.ticket_status = ticket_status;
        this.type_path = type_path;
        this.type_desc = type_desc;
        this.open_comments = open_comments;
        this.open_date = open_date;
        this.forecast_date = forecast_date;
        this.current_site_desc = current_site_desc;
        this.current_product_desc = current_product_desc;
        this.current_serial_id = current_serial_id;
        this.sync_required = sync_required;
        this.ctrls_serial_list = ctrls_serial_list;
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

    public String getType_path() {
        return type_path;
    }

    public void setType_path(String type_path) {
        this.type_path = type_path;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public String getOpen_comments() {
        return open_comments;
    }

    public void setOpen_comments(String open_comments) {
        this.open_comments = open_comments;
    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getForecast_date() {
        return forecast_date;
    }

    public void setForecast_date(String forecast_date) {
        this.forecast_date = forecast_date;
    }

    public String getCurrent_site_desc() {
        return current_site_desc;
    }

    public void setCurrent_site_desc(String current_site_desc) {
        this.current_site_desc = current_site_desc;
    }

    public String getCurrent_product_desc() {
        return current_product_desc;
    }

    public void setCurrent_product_desc(String current_product_desc) {
        this.current_product_desc = current_product_desc;
    }

    public String getCurrent_serial_id() {
        return current_serial_id;
    }

    public void setCurrent_serial_id(String current_serial_id) {
        this.current_serial_id = current_serial_id;
    }

    public int getSync_required() {
        return sync_required;
    }

    public void setSync_required(int sync_required) {
        this.sync_required = sync_required;
    }

    public static Act069_TicketVH getTicketVHObj(HMAux hmAux) throws Exception{
        //
        return new Act069_TicketVH(
            ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.TICKET_PREFIX)),
            ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.TICKET_CODE)),
            hmAux.get(TK_TicketDao.TICKET_ID),
            hmAux.get(TK_TicketDao.TICKET_STATUS),
            hmAux.get(TK_TicketDao.TYPE_PATH),
            hmAux.get(TK_TicketDao.TYPE_DESC),
            hmAux.get(TK_TicketDao.OPEN_COMMENTS),
            hmAux.get(TK_TicketDao.OPEN_DATE),
            hmAux.get(TK_TicketDao.FORECAST_DATE),
            hmAux.get(TK_TicketDao.CURRENT_SITE_DESC),
            hmAux.get(TK_TicketDao.CURRENT_PRODUCT_DESC),
            hmAux.get(TK_TicketDao.CURRENT_SERIAL_ID),
            ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.SYNC_REQUIRED)),
            hmAux.get(CTRLS_SERIAL_LIST)
        );
    }
    //
    public String getAllFieldForFilter(){
        return  (
                ticket_prefix+ "|" +
                ticket_code+ "|" +
                ticket_id+ "|" +
                ticket_status+ "|" +
                type_path+ "|" +
                type_desc+ "|" +
                open_comments+ "|" +
                open_date+ "|" +
                forecast_date+ "|" +
                current_site_desc+ "|" +
                current_product_desc + "|" +
                current_serial_id+"|" +
                ctrls_serial_list)
            .replace("null|","")
            .replace("null","")
            ;
    }
}
