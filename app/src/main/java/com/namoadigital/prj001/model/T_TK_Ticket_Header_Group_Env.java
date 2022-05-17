package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class T_TK_Ticket_Header_Group_Env extends Main_Header_Env{
    @Expose
    @SerializedName("ticket") List<TK_Ticket> ticket;
    @Expose
    @SerializedName("token") private String token;
    public List<TK_Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(List<TK_Ticket> ticket) {
        this.ticket = ticket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
