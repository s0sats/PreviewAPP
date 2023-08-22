package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdProductSerialTpDeviceItemHistMatDao
import com.namoadigital.prj001.database.Specification

class ChoiceTest3SQL : Specification {
    override fun toSqlQuery() =
        "SELECT customer_code, product_code, qty FROM ${MdProductSerialTpDeviceItemHistMatDao.TABLE}"
}