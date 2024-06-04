package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.database.Specification

class FsTripSqlAll:Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${FSTripDao.TABLE}
            WHERE (${FSTripDao.FLEET_START_PHOTO_LOCAL} is not null
              AND ${FSTripDao.FLEET_START_PHOTO_LOCAL} != '')
               OR (${FSTripDao.FLEET_END_PHOTO_LOCAL} is not null
              AND ${FSTripDao.FLEET_END_PHOTO_LOCAL} != '')
        """.trimIndent()
    }

}