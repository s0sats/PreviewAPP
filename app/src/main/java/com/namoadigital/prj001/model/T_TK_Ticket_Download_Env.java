package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 29/11/2019.
 */

public class T_TK_Ticket_Download_Env extends Main_Header_Env {

    private ArrayList<T_TK_Ticket_Download_PK_Env> ticket = new ArrayList<>();

    public ArrayList<T_TK_Ticket_Download_PK_Env> getTicket() {
        return ticket;
    }

    public void setTicket(ArrayList<T_TK_Ticket_Download_PK_Env> ticket) {
        this.ticket = ticket;
    }
}
