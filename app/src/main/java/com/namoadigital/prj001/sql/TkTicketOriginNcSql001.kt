package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketOriginNcDao
import com.namoadigital.prj001.database.Specification
/**
 * BARRIONUEVO 05-08-2021
 * Consulta NC geradora do Ticket pela PK.
 */
class TkTicketOriginNcSql001(
    private var customer_code: Long,
    private var ticket_prefix: Int,
    private var ticket_code: Int,
    private var page: Int,
    private var custom_form_order: Int,

    ) : Specification {
    override fun toSqlQuery(): String {
        val query = """
                SELECT o.*
                FROM ${TkTicketOriginNcDao.TABLE} o
                WHERE o.${TkTicketOriginNcDao.CUSTOMER_CODE} = $customer_code
                      AND o.${TkTicketOriginNcDao.TICKET_PREFIX} = $ticket_prefix
                      AND o.${TkTicketOriginNcDao.TICKET_CODE} = $ticket_code
                      AND o.${TkTicketOriginNcDao.PAGE} = $page
                      AND o.${TkTicketOriginNcDao.CUSTOM_FORM_ORDER} = $custom_form_order
      """.trimIndent()
        return query
    }
}