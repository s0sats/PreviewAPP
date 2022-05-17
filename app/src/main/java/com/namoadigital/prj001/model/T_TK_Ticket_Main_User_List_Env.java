package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_TK_Ticket_Main_User_List_Env extends Main_Header_Env {
    @SerializedName("customer_code") private int customer_code;
    @SerializedName("ticket_prefix") private int ticket_prefix;
    @SerializedName("ticket_code") private int ticket_code;
    @SerializedName("scn") private int scn;
    @SerializedName("edit_header") private int edit_header;

    public int getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(int customer_code) {
        this.customer_code = customer_code;
    }

    public int getTicket_prefix() {
        return ticket_prefix;
    }

    public void setTicket_prefix(int ticket_prefix) {
        this.ticket_prefix = ticket_prefix;
    }

    public int getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(int ticket_code) {
        this.ticket_code = ticket_code;
    }

    public int getScn() {
        return scn;
    }

    public void setScn(int scn) {
        this.scn = scn;
    }

    public int getEdit_header() {
        return edit_header;
    }

    public void setEdit_header(int edit_header) {
        this.edit_header = edit_header;
    }
}
