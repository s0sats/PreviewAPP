package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class T_TK_Ticket_Product_Save_Env extends Main_Header_Env {
    @Expose
    private String token;
    @Expose
    private List<T_TK_Ticket_Product_Save_Obj_Env> ticket;

    public List<T_TK_Ticket_Product_Save_Obj_Env> getTicket() {
        return ticket;
    }

    public void setTicket(List<T_TK_Ticket_Product_Save_Obj_Env> ticket) {
        this.ticket = ticket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
