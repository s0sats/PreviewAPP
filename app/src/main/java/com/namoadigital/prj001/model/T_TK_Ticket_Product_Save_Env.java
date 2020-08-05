package com.namoadigital.prj001.model;

import java.util.List;

public class T_TK_Ticket_Product_Save_Env extends Main_Header_Env {
    private String token;
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
