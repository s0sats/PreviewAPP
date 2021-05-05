package com.namoadigital.prj001.sql

import com.namoadigital.prj001.database.Specification

class Sql_Act005_001_v2 (private val customerCode: Long) : Specification {

    override fun toSqlQuery(): String? {
        val sb = StringBuilder()
        return sb.append(" SELECT   " +
                        "FROM ge_custom_forms_local l  " +
                        "WHERE  l.customer_code = '$customerCode'   ")
                .append(";")
                .toString()
    }
}
