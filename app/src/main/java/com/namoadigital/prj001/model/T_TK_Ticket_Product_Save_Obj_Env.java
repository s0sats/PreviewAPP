package com.namoadigital.prj001.model;

import java.util.List;

public class T_TK_Ticket_Product_Save_Obj_Env {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int ticket_scn;
    private List<T_TK_Ticket_Product_Save_Obj_Product_Env> product;


    public T_TK_Ticket_Product_Save_Obj_Env(long customer_code, int ticket_prefix, int ticket_code, int ticket_scn, List<T_TK_Ticket_Product_Save_Obj_Product_Env> product) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_scn = ticket_scn;
        this.product = product;
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

    public int getTicket_scn() {
        return ticket_scn;
    }

    public void setTicket_scn(int ticket_scn) {
        this.ticket_scn = ticket_scn;
    }
}
