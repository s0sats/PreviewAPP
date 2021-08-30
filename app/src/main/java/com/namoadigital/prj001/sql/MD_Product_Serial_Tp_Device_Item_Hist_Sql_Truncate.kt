package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao
import com.namoadigital.prj001.database.Specification

class MD_Product_Serial_Tp_Device_Item_Hist_Sql_Truncate :Specification {
    override fun toSqlQuery() = "DELETE FROM ${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}"
}