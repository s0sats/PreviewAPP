package com.namoadigital.prj001.model;

import java.util.List;

public class T_TK_Ticket_Product_Save_Return_Rec {

    List<T_TK_Ticket_Save_Product_Return_Rec> product;
    private int customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int scn;
    private String ret_status;
    private String ret_msg;

    public List<T_TK_Ticket_Save_Product_Return_Rec> getProduct() {
        return product;
    }

    public void setProduct(List<T_TK_Ticket_Save_Product_Return_Rec> product) {
        this.product = product;
    }

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
}
