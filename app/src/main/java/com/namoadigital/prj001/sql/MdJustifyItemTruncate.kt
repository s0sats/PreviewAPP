package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MdJustifyItemDao
import com.namoadigital.prj001.database.Specification

class MdJustifyItemTruncate: Specification {
    override fun toSqlQuery() = "DELETE FROM ${MdJustifyItemDao.TABLE}"
}