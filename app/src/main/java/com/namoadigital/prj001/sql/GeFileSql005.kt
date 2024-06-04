package com.namoadigital.prj001.sql

import com.namoadigital.prj001.database.Specification

class GeFileSql005 : Specification {
    override fun toSqlQuery(): String {
        return """
            SELECT ge.*
              FROM ge_files ge
        """.trimIndent()
    }
}