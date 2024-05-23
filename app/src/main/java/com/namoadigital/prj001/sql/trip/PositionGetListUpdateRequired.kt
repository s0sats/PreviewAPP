package com.namoadigital.prj001.sql.trip

import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.database.Specification

object PositionGetListUpdateRequired : Specification {
    override fun toSqlQuery() = """
        SELECT * FROM ${FsTripPositionDao.TABLE}
        WHERE ${FsTripPositionDao.UPDATE_REQUIRED} = '1'
    """.trimIndent()

    fun toSqlQueryWithToken() = """
        SELECT * FROM ${FsTripPositionDao.TABLE}
        WHERE ${FsTripPositionDao.UPDATE_REQUIRED} = '1'
        AND ${FsTripPositionDao.TOKEN} IS NOT NULL;
    """.trimIndent()

    fun toSqlQueryWithoutToken() = """
        SELECT * FROM ${FsTripPositionDao.TABLE}
        WHERE ${FsTripPositionDao.UPDATE_REQUIRED} = '1'
        AND ${FsTripPositionDao.TOKEN} IS NULL;
    """.trimIndent()
}