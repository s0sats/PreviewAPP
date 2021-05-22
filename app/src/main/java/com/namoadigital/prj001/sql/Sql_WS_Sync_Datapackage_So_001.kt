package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class Sql_WS_Sync_Datapackage_So_001(
         private val customerCode: Long
        ):Specification {
    override fun toSqlQuery(): String {
        var s = """ SELECT
                     s.${SM_SODao.CUSTOMER_CODE},
                     s.${SM_SODao.SO_PREFIX},
                     s.${SM_SODao.SO_CODE},
                     s.${SM_SODao.SO_SCN}
                    FROM
                     ${SM_SODao.TABLE} s
                    WHERE
                     s.${SM_SODao.CUSTOMER_CODE} = $customerCode
                     AND s.${SM_SODao.STATUS} NOT IN ('${ConstantBaseApp.SYS_STATUS_REJECTED}','${ConstantBaseApp.SYS_STATUS_DONE}','${ConstantBaseApp.SYS_STATUS_CANCELLED}')             
                """
        return s.trimIndent()
    }
}