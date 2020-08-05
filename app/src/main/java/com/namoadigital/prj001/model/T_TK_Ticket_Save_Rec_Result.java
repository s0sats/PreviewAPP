package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 09/12/2019.
 */

public class T_TK_Ticket_Save_Rec_Result {
    private int customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int scn;
    private String ret_status;
    private String ret_msg;
//    private Integer old_ticket_prefix;
//    private Integer old_ticket_code;
//    private TK_Ticket ticket;
    private ArrayList<T_TK_Ticket_Save_Rec_Result_Step> step;
    private ArrayList<T_TK_Ticket_Save_Rec_Result_Step> product;
    private int ticket_update;
//
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

    public int getScn() {
        return scn;
    }

    public void setScn(int scn) {
        this.scn = scn;
    }

    public String getRet_status() {
        return ret_status;
    }

    public void setRet_status(String ret_status) {
        this.ret_status = ret_status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }
//
//    public Integer getOld_ticket_prefix() {
//        return old_ticket_prefix;
//    }
//
//    public void setOld_ticket_prefix(Integer old_ticket_prefix) {
//        this.old_ticket_prefix = old_ticket_prefix;
//    }
//
//    public Integer getOld_ticket_code() {
//        return old_ticket_code;
//    }
//
//    public void setOld_ticket_code(Integer old_ticket_code) {
//        this.old_ticket_code = old_ticket_code;
//    }
//
//    public TK_Ticket getTicket() {
//        return ticket;
//    }
//
//    public void setTicket(TK_Ticket ticket) {
//        this.ticket = ticket;
//    }
    public ArrayList<T_TK_Ticket_Save_Rec_Result_Step> getStep() {
        return step;
    }

    public void setStep(ArrayList<T_TK_Ticket_Save_Rec_Result_Step> step) {
        this.step = step;
    }

    public ArrayList<T_TK_Ticket_Save_Rec_Result_Step> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<T_TK_Ticket_Save_Rec_Result_Step> product) {
        this.product = product;
    }

    public int getTicket_update() {
        return ticket_update;
    }

    public void setTicket_update(int ticket_update) {
        this.ticket_update = ticket_update;
    }
}
