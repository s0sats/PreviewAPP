package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification

class GeOsDeviceItemSqlAll:Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${GeOsDeviceItemDao.TABLE}
            WHERE (${GeOsDeviceItemDao.EXEC_PHOTO1} is not null
              AND ${GeOsDeviceItemDao.EXEC_PHOTO1} != '')
               OR
               (${GeOsDeviceItemDao.EXEC_PHOTO2} is not null
              AND ${GeOsDeviceItemDao.EXEC_PHOTO2} != '')
               OR
               (${GeOsDeviceItemDao.EXEC_PHOTO3} is not null
              AND ${GeOsDeviceItemDao.EXEC_PHOTO3} != '')
               OR
               (${GeOsDeviceItemDao.EXEC_PHOTO4} is not null
              AND ${GeOsDeviceItemDao.EXEC_PHOTO4} != '')
              
        """.trimIndent()
    }
}