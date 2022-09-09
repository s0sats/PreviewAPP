package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdJustifyItemDao
import com.namoadigital.prj001.database.Specification

class MdJustifyItemSql001(
    private val customerCode: Long,
    private val justifyGroupCode: Int
) : Specification {
    override fun toSqlQuery(): String {
        var query = """SELECT
                         i.*
                       FROM
                            ${MdJustifyItemDao.TABLE} i                
                       WHERE
                             i.${MdJustifyItemDao.CUSTOMER_CODE} = '$customerCode'  
                            AND i.${MdJustifyItemDao.JUSTIFY_GROUP_CODE} = '$justifyGroupCode'                               
                    """.trimMargin()
        return query
    }
}