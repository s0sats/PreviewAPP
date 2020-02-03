package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 29/11/2019.
 */

public class T_TK_Ticket_Download_Serial_Env extends Main_Header_Env {

    private ArrayList<T_TK_Ticket_Download_Serial_PK_Env> serial = new ArrayList<>();

    public ArrayList<T_TK_Ticket_Download_Serial_PK_Env> getSerial() {
        return serial;
    }

    public void setSerial(ArrayList<T_TK_Ticket_Download_Serial_PK_Env> serial) {
        this.serial = serial;
    }
}
