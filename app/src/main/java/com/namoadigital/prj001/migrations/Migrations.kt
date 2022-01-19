package com.namoadigital.prj001.migrations

import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.database.MigrationSQLite

val MigrationV1 = object : MigrationSQLite(1, 2){
    override fun migrate(db: SQLiteDatabase) {
        TODO("Not yet implemented")
    }
}


