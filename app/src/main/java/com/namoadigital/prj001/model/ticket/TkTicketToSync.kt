package com.namoadigital.prj001.model.ticket

data class TkTicketToSync(
    val customerCode:String,
    val ticketPrefix:String,
    val ticketCode:String,
    val scn:String,
    val productCode:String?,
    val serialCode:String?,
    val serialId:String?,
)