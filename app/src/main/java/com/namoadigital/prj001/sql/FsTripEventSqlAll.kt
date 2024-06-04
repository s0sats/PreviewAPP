package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.database.Specification

class FsTripEventSqlAll: Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${FSTripEventDao.TABLE}
            WHERE (${FSTripEventDao.PHOTO_LOCAL} is not null
              AND  ${FSTripEventDao.PHOTO_LOCAL} != '')
        """.trimIndent()
    }

}