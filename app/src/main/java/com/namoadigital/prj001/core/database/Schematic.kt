package com.namoadigital.prj001.core.database


data class DatabaseTable(
    val name: String,
    val columns: List<Column>,
    val primaryKey: List<String>
) {

    data class Column(
        val name: String,
        val type: ColumnType,
        val isNullable: Boolean = false,
        val defaultValue: String? = null,
        val collation: CollationType? = null
    )

    fun generateCreateTableScript(): String {
        val columnsDefinition = columns.joinToString(",\n        ") { column ->
            val nullable = if (column.isNullable) "" else "NOT NULL"
            val defaultValue = column.defaultValue?.let { "DEFAULT $it" } ?: ""
            val collation = column.collation?.let { "COLLATE ${it.name}" } ?: ""

            "[${column.name}] ${column.type.sqlName} $nullable $defaultValue $collation".trim()
        }

        val primaryKeyDefinition = if (primaryKey.isNotEmpty()) {
            ",\n        PRIMARY KEY(${primaryKey.joinToString(", ") { "[$it]" }})"
        } else ""

        return """
            CREATE TABLE IF NOT EXISTS [$name](
                $columnsDefinition$primaryKeyDefinition
            );
        """.trimIndent()
    }
}