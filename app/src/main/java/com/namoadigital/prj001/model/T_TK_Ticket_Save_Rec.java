package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by d.luche on 09/12/2019.
 */

public class T_TK_Ticket_Save_Rec {
    @SerializedName("app") private String app;
    @SerializedName("validation") private String validation;
    @SerializedName("link_url") private String link_url;
    @SerializedName("error_msg") private String error_msg;
    @SerializedName("save") private String save;
    @SerializedName("ticket_return") private ArrayList<T_TK_Ticket_Save_Rec_Result> ticket_return = new ArrayList<>();
    @SerializedName("ticket") private ArrayList<TK_Ticket> ticket = new ArrayList<>();
    @SerializedName("from_to") private ArrayList<T_TK_Ticket_Save_Rec_From_To> from_to = new ArrayList<>();

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

    public ArrayList<T_TK_Ticket_Save_Rec_Result> getTicket_return() {
        return ticket_return;
    }

    public void setTicket_return(ArrayList<T_TK_Ticket_Save_Rec_Result> ticket_return) {
        this.ticket_return = ticket_return;
    }

    public ArrayList<TK_Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(ArrayList<TK_Ticket> ticket) {
        this.ticket = ticket;
    }

    public ArrayList<T_TK_Ticket_Save_Rec_From_To> getFrom_to() {
        return from_to;
    }

    public void setFrom_to(ArrayList<T_TK_Ticket_Save_Rec_From_To> from_to) {
        this.from_to = from_to;
    }
}
