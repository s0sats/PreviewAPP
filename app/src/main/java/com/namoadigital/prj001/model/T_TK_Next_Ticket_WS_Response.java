package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class T_TK_Next_Ticket_WS_Response {
    @SerializedName("app")
    @Expose
    private String app;
    @SerializedName("validation")
    @Expose
    private String validation;
    @SerializedName("link_url")
    @Expose
    private String link_url;
    @SerializedName("error_msg")
    @Expose
    private String error_msg;
    @SerializedName("ticket")
    @Expose
    private List<TK_Next_Ticket> next_tickets = null;

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

    public List<TK_Next_Ticket> getNext_tickets() {
        return next_tickets;
    }

    public void setNext_tickets(List<TK_Next_Ticket> next_tickets) {
        this.next_tickets = next_tickets;
    }
}
