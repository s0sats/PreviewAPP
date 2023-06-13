package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.database.Specification

class SMSOGetSyncRequiredList(private val customerCode: Long): Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
              FROM ${SM_SODao.TABLE}
             WHERE  ${SM_SODao.CUSTOMER_CODE} = $customerCode
               AND  ${SM_SODao.SYNC_REQUIRED} = 1
        """.trimIndent()
    }
}