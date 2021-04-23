package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdTagDao
import com.namoadigital.prj001.database.Specification

class MdTagSql001(
        private val customer_code: Int,
        private val tag_code: Int,
        ) : Specification
{
    override fun toSqlQuery(): String {
       return """
                SELECT
                  t.*
                FROM
                    ${MdTagDao.TABLE} t
                WHERE
                    t.${MdTagDao.CUSTOMER_CODE} = '$customer_code'
                    AND  t.${MdTagDao.TAG_CODE} = '$tag_code'
                 """.trimIndent()
    }
}