package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.database.Specification

class FsTripDestinationSqlDelete(val customerCode: Long, val tripPrefix: Int, val tripCode: Int): Specification {
    override fun toSqlQuery() = """
        DELETE
          FROM ${FsTripDestinationDao.TABLE} 
          WHERE ${FSTripDao.CUSTOMER_CODE} = $customerCode     
           AND ${FSTripDao.TRIP_PREFIX} = $tripPrefix     
           AND ${FSTripDao.TRIP_CODE} = $tripCode    
    """.trimIndent()
}