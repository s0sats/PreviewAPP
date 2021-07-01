package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 29/11/2019.
 *
 * LUCHE - 30/06/2021
 * <P></P>
 * Add atributo login que será usado quando esse ws for chamado no login
 */

public class T_TK_Ticket_Download_Env extends Main_Header_Env {

    private int login = 0;
    private ArrayList<T_TK_Ticket_Download_PK_Env> ticket = new ArrayList<>();

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public ArrayList<T_TK_Ticket_Download_PK_Env> getTicket() {
        return ticket;
    }

    public void setTicket(ArrayList<T_TK_Ticket_Download_PK_Env> ticket) {
        this.ticket = ticket;
    }
}
