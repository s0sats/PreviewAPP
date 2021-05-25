package com.namoadigital.prj001.model

import java.util.*

class T_TK_Ticket_Search_Not_Focus_Env : Main_Header_Env() {
    private var search = ArrayList<T_TK_Ticket_Search_Not_Focus_Param>()

    fun getSearch(): ArrayList<T_TK_Ticket_Search_Not_Focus_Param> {
        return search
    }

    fun setSearch(search: ArrayList<T_TK_Ticket_Search_Not_Focus_Param>) {
        this.search = search
    }
}