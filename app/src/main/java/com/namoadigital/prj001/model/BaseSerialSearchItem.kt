package com.namoadigital.prj001.model

sealed class BaseSerialSearchItem {

    data class SerialSearchExceededItem(
        val exceedMsg: String?,
        val pageLabel: String?,
        val page: Int,
        val foundQtyLbl:String?,
        val foundQty: Int
    ): BaseSerialSearchItem()

    data class SerialSearchItem(
        val productCode: Int,
        val productId: String? = null,
        val productDesc: String,
        val serialCode: Int,
        val serialId: String,
        val siteCode: Int?,
        val siteDesc: String?,
        val ticketOpenQty: Int? = null
    ): BaseSerialSearchItem()

    data class BackupMachineSerialItem(
        val productCode: Int,
        val productId: String? = null,
        val productDesc: String,
        val serialCode: Int,
        val serialId: String,
        val siteCode: Int?,
        val siteDesc: String?
    ): BaseSerialSearchItem()
}