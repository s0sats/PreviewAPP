package com.namoadigital.prj001.sql.trip

import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.database.Specification

class PositionUpdateRequired constructor(
    private val updateRequired: Int,
    private val customerCode: Long,
    private val tripPrefix: Int,
    private val tripCode: Int,
    private val tripPositionSeq: Int,
): Specification {
    
    override fun toSqlQuery() = """
        UPDATE ${FsTripPositionDao.TABLE}
        SET ${FsTripPositionDao.UPDATE_REQUIRED} = $updateRequired
        WHERE ${FsTripPositionDao.CUSTOMER_CODE} = $customerCode 
          AND ${FsTripPositionDao.TRIP_PREFIX} = $tripPrefix
          AND ${FsTripPositionDao.TRIP_CODE} = $tripCode
          AND ${FsTripPositionDao.TRIP_POSITION_SEQ} = $tripPositionSeq
    """.trimIndent()
}