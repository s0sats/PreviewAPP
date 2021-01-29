package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_TK_Ticket_Product_Save_Rec {
    private ArrayList<T_TK_Ticket_Product_Save_Return_Rec> ticket_return = new ArrayList<>();
    private ArrayList<TK_Ticket> ticket = new ArrayList<>();
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String save;

    public ArrayList<T_TK_Ticket_Product_Save_Return_Rec> getTicket_return() {
        return ticket_return;
    }

    public void setTicket_return(ArrayList<T_TK_Ticket_Product_Save_Return_Rec> ticket_return) {
        this.ticket_return = ticket_return;
    }

    public ArrayList<TK_Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(ArrayList<TK_Ticket> ticket) {
        this.ticket = ticket;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
