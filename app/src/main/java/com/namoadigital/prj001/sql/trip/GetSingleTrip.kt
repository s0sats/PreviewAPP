package com.namoadigital.prj001.sql.trip

import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.database.Specification

class GetSingleTrip( val customerCode: Long,
                     val tripPrefix: Int,
                     val tripCode: Int,
        ) : Specification {
    override fun toSqlQuery() = """
            SELECT * 
            FROM ${FSTripDao.TABLE}
            where ${FSTripDao.CUSTOMER_CODE} = $customerCode 
              and ${FSTripDao.TRIP_PREFIX} = $tripPrefix 
              and ${FSTripDao.TRIP_CODE} = $tripCode 
              
        """.trimIndent()
}