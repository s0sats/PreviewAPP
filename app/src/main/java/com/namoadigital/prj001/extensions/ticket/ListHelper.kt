package com.namoadigital.prj001.extensions.ticket

import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec_Result

fun ArrayList<T_TK_Ticket_Save_Rec_Result>.getTicketProcessResult(
    customer_code: Int,
    ticket_prefix: Int,
    ticket_code: Int
): T_TK_Ticket_Save_Rec_Result? {
    for (recResult in this) {
        if (recResult.customer_code == customer_code && recResult.ticket_prefix == ticket_prefix && recResult.ticket_code == ticket_code
        ) {
            return recResult
        }
    }
    return null
}

fun ArrayList<TK_Ticket>.getTicketFromSentList(customer_code: Int, ticket_prefix: Int, ticket_code: Int): TK_Ticket? {
    for (tkTicket in this) {
        if (tkTicket.customer_code == customer_code.toLong()
            && tkTicket.ticket_prefix == ticket_prefix && tkTicket.ticket_code == ticket_code
        ) {
            return tkTicket
        }
    }
    return null
}