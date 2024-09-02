package com.namoadigital.prj001.sql.trip

import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.database.Specification

class EventGetMax: Specification {
    override fun toSqlQuery() = """
            SELECT ifnull(MAX(${FSTripEventDao.EVENT_SEQ}), 0) + 1 ${FSTripEventDao.EVENT_SEQ} 
            FROM ${FSTripEventDao.TABLE}
        """.trimIndent()
}