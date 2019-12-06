package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 06/12/2019.
 */

public class T_TK_Ticket_WS_Return {
    private int customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private Integer scn;
    private String ret_status;
    private String error_msg;

    public int getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(int customer_code) {
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

    public Integer getScn() {
        return scn;
    }

    public void setScn(Integer scn) {
        this.scn = scn;
    }

    public String getRet_status() {
        return ret_status;
    }

    public void setRet_status(String ret_status) {
        this.ret_status = ret_status;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
