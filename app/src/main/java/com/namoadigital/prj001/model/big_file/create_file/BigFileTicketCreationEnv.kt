package com.namoadigital.prj001.model.big_file.create_file

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env
import com.namoadigital.prj001.model.T_TK_Ticket_Download_PK_Env

data class BigFileTicketCreationEnv(
    @SerializedName("file_type") val fileTypeTicket: String,
    @SerializedName("login") val login:Int = 0,
    @SerializedName("ticket") val ticket: List<T_TK_Ticket_Download_PK_Env>
) : Main_Header_Env()