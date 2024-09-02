package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdTagDao
import com.namoadigital.prj001.dao.TkTicketTypeDao
import com.namoadigital.prj001.dao.TkTicketTypeOperationDao
import com.namoadigital.prj001.dao.TkTicketTypeProductDao
import com.namoadigital.prj001.dao.TkTicketTypeSiteDao
import com.namoadigital.prj001.database.Specification

class Sql_Act009_002(
    var s_customer_code: Long,
    var s_product_code: Long,
    var s_translate_code: String,
    var s_operation_code: Long,
    var s_site_code: String,
    var s_serial_id: String,
):Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT DISTINCT T.${MdTagDao.TAG_CODE},
                   T.${MdTagDao.TAG_DESC}
            FROM ${MdTagDao.TABLE} T,
            ${TkTicketTypeDao.TABLE} TP 
            LEFT JOIN ${TkTicketTypeProductDao.TABLE}  p on p.customer_code = tp.customer_code
                                                      and p.ticket_type_code = tp.ticket_type_code
                                                      and p.product_code = $s_product_code
            LEFT JOIN
                  ${TkTicketTypeOperationDao.TABLE} o on o.customer_code = tp.customer_code
                                          and o.ticket_type_code = tp.ticket_type_code
                                          and o.operation_code = $s_operation_code 
            LEFT JOIN
                ${TkTicketTypeSiteDao.TABLE} s on s.customer_code = tp.customer_code
                                          and s.ticket_type_code = tp.ticket_type_code
                                          and s.site_code = $s_site_code
            WHERE t.customer_code = tp.customer_code
              AND t.tag_code = tp.tag_operational_code
              AND t.customer_code = $s_customer_code
              AND (TP.all_product = 1 OR p.product_code = $s_product_code)
              AND (TP.all_operation = 1 OR o.operation_code = $s_operation_code) 
              AND (TP.all_site = 1 OR s.site_code = $s_site_code)
              AND ( '$s_serial_id' IS NOT NULL)
       """.trimIndent()
    }
}