package com.namoadigital.prj001.core.database

enum class CollationType {
    NOCASE, // Case-insensitive
    BINARY, // Case-sensitive (padrão no SQLite)
    RTRIM   // Ignora espaços à direita
}