package com.namoadigital.prj001.extensions.dao

import android.database.Cursor

fun Cursor.getString(columnName: String): String =
    getString(getColumnIndexOrThrow(columnName))

fun Cursor.getStringOrNull(columnName: String): String? =
    getColumnIndex(columnName).takeIf { it >= 0 }?.let {
        if (isNull(it)) null else getString(it)
    }

fun Cursor.getInt(columnName: String): Int =
    getInt(getColumnIndexOrThrow(columnName))

fun Cursor.getIntOrNull(columnName: String): Int? =
    getColumnIndex(columnName).takeIf { it >= 0 }?.let {
        if (isNull(it)) null else getInt(it)
    }

fun Cursor.getLong(columnName: String): Long =
    getLong(getColumnIndexOrThrow(columnName))

fun Cursor.getLongOrNull(columnName: String): Long? =
    getColumnIndex(columnName).takeIf { it >= 0 }?.let {
        if (isNull(it)) null else getLong(it)
    }

fun Cursor.getDouble(columnName: String): Double =
    getDouble(getColumnIndexOrThrow(columnName))

fun Cursor.getDoubleOrNull(columnName: String): Double? =
    getColumnIndex(columnName).takeIf { it >= 0 }?.let {
        if (isNull(it)) null else getDouble(it)
    }

fun Cursor.getFloat(columnName: String): Float =
    getFloat(getColumnIndexOrThrow(columnName))

fun Cursor.getFloatOrNull(columnName: String): Float? =
    getColumnIndex(columnName).takeIf { it >= 0 }?.let {
        if (isNull(it)) null else getFloat(it)
    }

fun Cursor.getBlob(columnName: String): ByteArray =
    getBlob(getColumnIndexOrThrow(columnName))

fun Cursor.getBlobOrNull(columnName: String): ByteArray? =
    getColumnIndex(columnName).takeIf { it >= 0 }?.let {
        if (isNull(it)) null else getBlob(it)
    }

// ─────────────────────────────────────────────
// BOOLEAN (SQLite não tem boolean nativo)
// ─────────────────────────────────────────────

fun Cursor.getBoolean(columnName: String): Boolean =
    getInt(columnName) != 0

fun Cursor.getBooleanOrNull(columnName: String): Boolean? =
    getIntOrNull(columnName)?.let { it != 0 }

// ─────────────────────────────────────────────
// ITERAÇÃO FUNCIONAL
// ─────────────────────────────────────────────

/**
 * Itera cada linha e executa o bloco. Fecha o cursor ao final.
 * Uso: cursor.forEach { println(it.getString("name")) }
 */
inline fun Cursor.forEach(block: (Cursor) -> Unit) {
    use {
        if (moveToFirst()) {
            do { block(this) } while (moveToNext())
        }
    }
}

/**
 * Mapeia cada linha para um objeto T. Fecha o cursor ao final.
 * Uso: val nomes = cursor.map { it.getString("name") }
 */
inline fun <T> Cursor.map(transform: (Cursor) -> T): List<T> {
    val result = mutableListOf<T>()
    forEach { result.add(transform(it)) }
    return result
}

/**
 * Retorna o primeiro resultado ou null se vazio. Fecha o cursor ao final.
 * Uso: val user = cursor.firstOrNull { it.toUser() }
 */
inline fun <T> Cursor.firstOrNull(transform: (Cursor) -> T): T? {
    return use {
        if (moveToFirst()) transform(this) else null
    }
}

/**
 * Converte para uma sequência lazy (não fecha o cursor automaticamente).
 * Uso: cursor.asSequence().filter { it.getInt("age") > 18 }.map { ... }
 */
fun Cursor.asSequence(): Sequence<Cursor> = sequence {
    if (moveToFirst()) {
        do { yield(this@asSequence) } while (moveToNext())
    }
}

// ─────────────────────────────────────────────
// VERIFICAÇÕES ÚTEIS
// ─────────────────────────────────────────────

/** Verdadeiro se o cursor está vazio */
val Cursor.isEmpty: Boolean get() = count == 0

/** Verdadeiro se o cursor tem pelo menos uma linha */
val Cursor.isNotEmpty: Boolean get() = count > 0

/** Verifica se uma coluna existe no cursor */
fun Cursor.hasColumn(columnName: String): Boolean =
    getColumnIndex(columnName) >= 0

/** Verifica se o valor de uma coluna é null */
fun Cursor.isNull(columnName: String): Boolean =
    getColumnIndex(columnName).let { if (it < 0) true else isNull(it) }
