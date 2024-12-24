package com.namoadigital.prj001.sql.trip

import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.database.Specification

class DestinationGetMax(
    val tripPrefix: Int,
    val tripCode: Int,
) : Specification {
    @Throws(Exception::class)
    override fun toSqlQuery() = """
            SELECT ifnull(MAX(${FsTripDestinationDao.DESTINATION_SEQ}), 0) + 1 ${FsTripDestinationDao.DESTINATION_SEQ} 
            FROM ${FsTripDestinationDao.TABLE}
            WHERE ${FsTripDestinationDao.TRIP_PREFIX} = $tripPrefix
            AND ${FsTripDestinationDao.TRIP_CODE} = $tripCode
        """.trimIndent()
}