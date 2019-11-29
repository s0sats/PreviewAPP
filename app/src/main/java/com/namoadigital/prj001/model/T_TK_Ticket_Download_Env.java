package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 29/11/2019.
 */

public class T_TK_Ticket_Download_Env extends Main_Header_Env {

    private ArrayList<T_TK_Ticket_Download_PK_Env> tickets = new ArrayList<>();

    public ArrayList<T_TK_Ticket_Download_PK_Env> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<T_TK_Ticket_Download_PK_Env> tickets) {
        this.tickets = tickets;
    }
}
