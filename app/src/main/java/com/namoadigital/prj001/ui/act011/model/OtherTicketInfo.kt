package com.namoadigital.prj001.ui.act011.model

import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem

data class OtherTicketInfo(
    val ticketPrefix: Int,
    val ticketCode: Int
) {
    val formattedTicketId: String
        get() = "$ticketPrefix.$ticketCode"


    companion object {

        val OtherTicketInfoLbl = "other_ticket_info_lbl"
        val DeviceItemOtherTicket = "device_item_other_ticket"

        fun hasOtherTicketInfo(
            item: GeOsDeviceItem,
            currentFormTicketPrefix: Int?,
            currentFormTicketCode: Int?
        ): OtherTicketInfo? {
            var itemOtherTicketInfo: OtherTicketInfo? = null
            val itemTicketPrefix = item.ticket_prefix
            val itemTicketCode = item.ticket_code

            if (itemTicketPrefix != null && itemTicketCode != null &&
                (itemTicketPrefix != currentFormTicketPrefix || itemTicketCode != currentFormTicketCode)
            ) {
                itemOtherTicketInfo = OtherTicketInfo(itemTicketPrefix, itemTicketCode)
            }
            return itemOtherTicketInfo
        }
    }
}
