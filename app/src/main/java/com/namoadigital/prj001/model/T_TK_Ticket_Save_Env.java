package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 06/12/2019.
 */

public class T_TK_Ticket_Save_Env extends Main_Header_Env {
    @Expose
    private ArrayList<TK_Ticket> ticket = new ArrayList<>();
//   private ArrayList<WS_TK_Ticket_Obj> ticket = new ArrayList<>();
    @Expose
    private String token;
    @Expose
    private int reprocess;

    public ArrayList<TK_Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(ArrayList<TK_Ticket> ticket) {
        this.ticket = ticket;
    }


//    public ArrayList<WS_TK_Ticket_Obj> getTicket() {
//        return ticket;
//    }
//
//    public void setTicket(ArrayList<WS_TK_Ticket_Obj> ticket) {
//        this.ticket = ticket;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getReprocess() {
        return reprocess;
    }

    public void setReprocess(int reprocess) {
        this.reprocess = reprocess;
    }
}
