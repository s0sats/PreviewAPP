package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class TicketCreationRec(
    @SerializedName("ticket_prefix") val ticketPrefix : Int?,
    @SerializedName("ticket_code") val ticketCode : Int?
    ): Main_Header_Rec(){

}
