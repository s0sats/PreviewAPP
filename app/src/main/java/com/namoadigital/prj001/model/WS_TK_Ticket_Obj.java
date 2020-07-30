package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class WS_TK_Ticket_Obj {
    private long customer_code;//pk
    private int ticket_prefix;//pk
    private int ticket_code;//pk
    private int scn;
    private ArrayList<WS_TK_Ticket_Step_Obj> step = new ArrayList<>();
    private ArrayList<WS_TK_Ticket_Product_Obj> product = new ArrayList<>();

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

    public ArrayList<WS_TK_Ticket_Step_Obj> getStep() {
        return step;
    }

    public void setStep(ArrayList<WS_TK_Ticket_Step_Obj> step) {
        this.step = step;
    }

    public ArrayList<WS_TK_Ticket_Product_Obj> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<WS_TK_Ticket_Product_Obj> product) {
        this.product = product;
    }

}



