package com.namoadigital.prj001.sql.trip

import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.database.Specification

class FsTripDestinationSqlGetDestinationByStatus(
    val customerCode: Long,
    val tripPrefix: Int,
    val tripCode: Int,
    val status: String,
): Specification {
    override fun toSqlQuery() = """
            SELECT * 
              FROM ${FsTripDestinationDao.TABLE} 
             WHERE ${FsTripDestinationDao.CUSTOMER_CODE} = $customerCode 
               AND ${FsTripDestinationDao.TRIP_PREFIX} =  $tripPrefix
               AND ${FsTripDestinationDao.TRIP_CODE} =  $tripCode
               AND ${FsTripDestinationDao.DESTINATION_STATUS} =  '$status'
        """.trimIndent()
}