package com.namoadigital.prj001.core.database

import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.migrations.checkIfFieldExist


fun SQLiteDatabase.addMissingColumns(
    tableName: String,
    columnsToAdd: List<DatabaseTable.Column>
) {
    columnsToAdd.forEach { column ->
        this.checkIfFieldExist(
            tableName = tableName,
            fieldName = column.name
        ) { this.addColumn(tableName = tableName, column = column) }
    }
}


fun SQLiteDatabase.addColumn(tableName: String, column: DatabaseTable.Column) {
    val nullable = if (column.isNullable) "" else "NOT NULL"
    val defaultValue = column.defaultValue?.let { "DEFAULT $it" } ?: ""
    val collation = column.collation?.let { "COLLATE ${it.name}" } ?: ""
    val columnDefinition = "${column.type.sqlName} $nullable $defaultValue $collation".trim()

    execSQL(
        """
        ALTER TABLE $tableName
        ADD COLUMN [${column.name}] $columnDefinition;
    """.trimIndent()
    )
}

fun SQLiteDatabase.updateColumn(tableName: String, column: String, value: String) {
    execSQL(
        """
        UPDATE $tableName
        SET $column = $value;
    """.trimIndent()
    )
}