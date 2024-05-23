package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.database.Specification

class FsTripEventSqlDelete(val customerCode: Long, val tripPrefix: Int, val tripCode: Int): Specification {
    override fun toSqlQuery() = """
        DELETE
          FROM ${FSTripEventDao.TABLE} 
         WHERE ${FSTripDao.CUSTOMER_CODE} = $customerCode     
           AND ${FSTripDao.TRIP_PREFIX} = $tripPrefix     
           AND ${FSTripDao.TRIP_CODE} = $tripCode    
    """.trimIndent()
}