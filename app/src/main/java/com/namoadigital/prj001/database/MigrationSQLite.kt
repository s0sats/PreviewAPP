package com.namoadigital.prj001.database

import android.database.sqlite.SQLiteDatabase

abstract class MigrationSQLite(private val oldVersion: Int, private val newVersion: Int) {
    abstract fun migrate(db: SQLiteDatabase)
}