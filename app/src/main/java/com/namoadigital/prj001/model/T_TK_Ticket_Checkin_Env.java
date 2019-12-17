package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 06/12/2019.
 */

public class T_TK_Ticket_Checkin_Env extends Main_Header_Env {

    private ArrayList<T_TK_Ticket_Checkin_Obj_Env> ticket = new ArrayList<>();
    private String token;

    public ArrayList<T_TK_Ticket_Checkin_Obj_Env> getTicket() {
        return ticket;
    }

    public void setTicket(ArrayList<T_TK_Ticket_Checkin_Obj_Env> ticket) {
        this.ticket = ticket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
