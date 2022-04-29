package com.namoadigital.prj001.migrations

import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.MigrationSQLite

val MigrationV1 = object : MigrationSQLite(1, 2){
    override fun migrate(db: SQLiteDatabase) {
        //
        db.beginTransaction()
        //
        setZoneCodeIdDesc(db, GE_Custom_Form_LocalDao.TABLE)
        setZoneCodeIdDesc(db, TK_TicketDao.TABLE)
        setZoneCodeIdDesc(db, MD_Schedule_ExecDao.TABLE)
        //
        db.execSQL(
            """
                ALTER TABLE ${TkTicketCacheDao.TABLE}
                        ADD COLUMN open_zone_code int;
            """.trimIndent()
        )
        //
        db.execSQL(
            """
                ALTER TABLE ${TkTicketCacheDao.TABLE}
                        ADD COLUMN open_zone_desc text COLLATE nocase;
            """.trimIndent()
        )
        //
        db.endTransaction()
    }

    private fun setZoneCodeIdDesc(db: SQLiteDatabase, table: String) {
        db.execSQL(
            """
                ALTER TABLE ${table}
                        ADD COLUMN zone_code int;
            """.trimIndent()
        )

        db.execSQL(
            """
                ALTER TABLE ${table}
                        ADD COLUMN zone_id text COLLATE nocase;
            """.trimIndent()
        )

        db.execSQL(
            """
                ALTER TABLE ${table}
                        ADD COLUMN zone_desc text COLLATE nocase;
            """.trimIndent()
        )
    }
}


