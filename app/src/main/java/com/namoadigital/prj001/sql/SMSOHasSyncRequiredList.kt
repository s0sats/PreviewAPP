package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.database.Specification

class SMSOHasSyncRequiredList(
    private val customerCode: Long
): Specification {

    override fun toSqlQuery(): String {
        return """
            SELECT EXISTS( SELECT 1 
              FROM ${SM_SODao.TABLE}
             WHERE  ${SM_SODao.CUSTOMER_CODE} = $customerCode
               AND  ${SM_SODao.SYNC_REQUIRED} = 1
               AND  ${SM_SODao.UPDATE_REQUIRED} = 0
               LIMIT 1
               ) $NEED_SYNC
        """.trimIndent()
    }

    companion object{
        const val NEED_SYNC = "NEED_SYNC"
    }
}