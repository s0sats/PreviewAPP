package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdProductSerialTpDeviceItemHistMatDao
import com.namoadigital.prj001.database.Specification

class MdProductSerialTpDeviceItemHistMatSqlTruncate : Specification {
    override fun toSqlQuery() = "DELETE FROM ${MdProductSerialTpDeviceItemHistMatDao.TABLE}"
}