package com.namoadigital.prj001.ui.act047.model

data class ReservedUser(
    val reservedDate: String? = null,
    val reservedCode: Int? = null,
    val reservedNick: String? = null,
    val reservedName: String? = null,
    val retStatus: String = "",
    val retMsg: String = "",
    val soScn: Int,
){

    fun isValid() = reservedCode != null && reservedNick != null && reservedName != null

}