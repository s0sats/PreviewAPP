package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class TkTicketVG(
    @SerializedName("customer_code") var customerCode: Long,
    @SerializedName("ticket_prefix") var ticketPrefix: Int,
    @SerializedName("ticket_code") var ticketCode: Int,
    @SerializedName("vg_code") var vgCode: Int,
){
    fun setPk(tkTicket: TK_Ticket){
        customerCode = tkTicket.customer_code
        ticketPrefix = tkTicket.ticket_prefix
        ticketCode = tkTicket.ticket_code
    }


    fun isSameTicketGroup(
        prefix: Int,
        code: Int,
        groupCode: Int,
    ): Boolean = prefix == ticketPrefix && code == ticketCode && groupCode == vgCode
}