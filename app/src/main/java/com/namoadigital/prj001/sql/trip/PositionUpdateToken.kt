package com.namoadigital.prj001.sql.trip

import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.database.Specification

class PositionUpdateToken constructor(
    private val customerCode: Long,
    private val tripPrefix: Int,
    private val tripCode: Int,
    private val tripPositionSeq: Int,
    private val token: String,
) : Specification {
    override fun toSqlQuery() = """
        UPDATE ${FsTripPositionDao.TABLE}
        SET ${FsTripPositionDao.TOKEN} = '$token'
        WHERE ${FsTripPositionDao.CUSTOMER_CODE} = $customerCode 
          AND ${FsTripPositionDao.TRIP_PREFIX} = $tripPrefix
          AND ${FsTripPositionDao.TRIP_CODE} = $tripCode
          AND ${FsTripPositionDao.TRIP_POSITION_SEQ} = $tripPositionSeq        
    """.trimIndent()
}