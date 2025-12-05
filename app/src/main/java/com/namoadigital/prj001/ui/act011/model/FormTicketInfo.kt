package com.namoadigital.prj001.ui.act011.model

import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem

class FormTicketInfo(
    val currentFormTicketPrefix: Int?,
    val currentFormTicketCode: Int?
) {
    val formattedTicketId: String
        get() = "$currentFormTicketPrefix.$currentFormTicketCode"

    fun getTicketFormType(
        item: GeOsDeviceItem,
    ): TicketFormType {

        val itemTicketPrefix = item.ticket_prefix
        val itemTicketCode = item.ticket_code

        if (itemTicketPrefix != null && itemTicketCode != null){
            if(itemTicketPrefix == currentFormTicketPrefix && itemTicketCode == currentFormTicketCode) {
                return TicketFormType.SAME_TICKET
            } else {
                return TicketFormType.OTHER_TICKET
            }
        }

        return TicketFormType.NO_TICKET
    }

    companion object {
        const val DEVICE_ITEM_TICKET_INFO = "device_item_other_ticket"
    }
    enum class TicketFormType {
        SAME_TICKET,
        OTHER_TICKET,
        NO_TICKET
    }
}
