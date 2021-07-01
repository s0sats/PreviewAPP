package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification


/**
 * LUCHE - 28/06/2021
 * Query que seta TODOS OS TICKET para sync_required e scn para 0
 * Essa query é usado quando o app recebe o FCM de SYNC_REQUIRED_FULL, atualmente disparado quando
 * há alguma alteração em algum workgroup e que pode mudar o user_focus de um ticket. Como essa ação,
 * de alteração do workgroup, não muda o FCM do ticket, ja que não alterar de fato o ticket, foi necessario
 * criar um "ajuste"
 */
class SqlMyFirebaseMessagingTicket001(
    private val customerCode: String
) : Specification {
    override fun toSqlQuery(): String {
        var query = """ UPDATE ${TK_TicketDao.TABLE} set
                            scn = '0',
                            sync_required = '1'
                        WHERE
                            customer_code = '$customerCode'
                        """
        return query
    }
}