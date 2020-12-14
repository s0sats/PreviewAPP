package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 06/12/2019.
 */

public class T_TK_Get_Workgroup_List_Env extends Main_Header_Env {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int scn;

    public T_TK_Get_Workgroup_List_Env(String app_code, String app_version, String app_type, String session_app, long customer_code, int ticket_prefix, int ticket_code, int scn) {
        super(app_code, app_version, app_type, session_app);
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.scn = scn;
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

    public int getScn() {
        return scn;
    }

    public void setScn(int scn) {
        this.scn = scn;
    }
}
