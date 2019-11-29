package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 29/11/2019.
 */

public class T_TK_Ticket_Download_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private ArrayList<TK_Ticket> ticket = new ArrayList<>();

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

    public ArrayList<TK_Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(ArrayList<TK_Ticket> ticket) {
        this.ticket = ticket;
    }
}
