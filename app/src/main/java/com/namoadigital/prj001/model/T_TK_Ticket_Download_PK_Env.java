package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 29/11/2019.
 */

public class T_TK_Ticket_Download_PK_Env {

    private String customer_code;
    private String ticket_prefix;
    private String ticket_code;
    private String scn;

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getTicket_prefix() {
        return ticket_prefix;
    }

    public void setTicket_prefix(String ticket_prefix) {
        this.ticket_prefix = ticket_prefix;
    }

    public String getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(String ticket_code) {
        this.ticket_code = ticket_code;
    }

    public String getScn() {
        return scn;
    }

    public void setScn(String scn) {
        this.scn = scn;
    }
}
