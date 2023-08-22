package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdProductSerialTpDeviceItemHistMatDao
import com.namoadigital.prj001.database.Specification

class ChoiceTestAllSQL : Specification {
    override fun toSqlQuery() = "SELECT * FROM ${MdProductSerialTpDeviceItemHistMatDao.TABLE}"
}