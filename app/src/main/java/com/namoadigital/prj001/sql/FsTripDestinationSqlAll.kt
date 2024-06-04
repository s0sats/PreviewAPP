package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.database.Specification

class FsTripDestinationSqlAll : Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT * 
            FROM ${FsTripDestinationDao.TABLE}
            WHERE (${FsTripDestinationDao.ARRIVED_FLEET_PHOTO_LOCAL} is not null
              AND ${FsTripDestinationDao.ARRIVED_FLEET_PHOTO_LOCAL} != '')
        """.trimIndent()
    }

}