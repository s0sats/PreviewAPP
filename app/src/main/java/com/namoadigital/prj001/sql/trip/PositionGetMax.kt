package com.namoadigital.prj001.sql.trip

import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.database.Specification

class PositionGetMax : Specification {
    override fun toSqlQuery() = """
            SELECT ifnull(MAX(${FsTripPositionDao.TRIP_POSITION_SEQ}), 0) + 1 ${FsTripPositionDao.TRIP_POSITION_SEQ} 
            FROM ${FsTripPositionDao.TABLE}
        """.trimIndent()
}