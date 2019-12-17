package com.namoadigital.prj001.model;

import java.io.Serializable;

/**
 * Created by d.luche on 06/12/2019.
 */

public class T_TK_Ticket_Checkin_Obj_Env implements Serializable {

    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int ticket_scn;
    private int checkin;

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

    public int getTicket_scn() {
        return ticket_scn;
    }

    public void setTicket_scn(int ticket_scn) {
        this.ticket_scn = ticket_scn;
    }

    public int getCheckin() {
        return checkin;
    }

    public void setCheckin(int checkin) {
        this.checkin = checkin;
    }
}
