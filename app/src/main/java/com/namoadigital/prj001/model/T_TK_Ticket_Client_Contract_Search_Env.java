package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 29/11/2019.
 */

public class T_TK_Ticket_Client_Contract_Search_Env extends Main_Header_Env {

    private ArrayList<T_TK_Ticket_Client_Contract_Search_Param> search = new ArrayList<>();

    public ArrayList<T_TK_Ticket_Client_Contract_Search_Param> getSearch() {
        return search;
    }

    public void setSearch(ArrayList<T_TK_Ticket_Client_Contract_Search_Param> search) {
        this.search = search;
    }
}
