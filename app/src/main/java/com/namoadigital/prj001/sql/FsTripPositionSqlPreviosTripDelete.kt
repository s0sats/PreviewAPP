package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.database.Specification

class FsTripPositionSqlPreviosTripDelete(val customerCode: Long, val tripPrefix: Int, val tripCode: Int):
    Specification {
    override fun toSqlQuery() = """
        DELETE
          FROM ${FsTripPositionDao.TABLE} 
         WHERE ${FsTripPositionDao.CUSTOMER_CODE}|${FsTripPositionDao.TRIP_PREFIX}|${FsTripPositionDao.TRIP_CODE}
           not in (
                SELECT ${FsTripPositionDao.CUSTOMER_CODE}|${FsTripPositionDao.TRIP_PREFIX}|${FsTripPositionDao.TRIP_CODE} 
                  FROM ${FsTripPositionDao.TABLE}
                 WHERE ${FsTripPositionDao.CUSTOMER_CODE} = $customerCode     
                   AND ${FsTripPositionDao.TRIP_PREFIX} = $tripPrefix     
                   AND ${FsTripPositionDao.TRIP_CODE} = $tripCode 
                   )      
    """.trimIndent()
}