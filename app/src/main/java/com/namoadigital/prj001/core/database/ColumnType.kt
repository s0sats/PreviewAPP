package com.namoadigital.prj001.core.database

enum class ColumnType(val sqlName: String) {
    INT("INT"),
    TEXT("TEXT"),
    REAL("REAL"),
    BLOB("BLOB"),
    BOOLEAN("BOOLEAN"),
}