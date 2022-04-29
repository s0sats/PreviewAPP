package com.namoadigital.prj001.migrations

import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.MigrationSQLite

val MigrationV1 = object : MigrationSQLite(1, 2){
    override fun migrate(db: SQLiteDatabase) {
        //
        db.execSQL("""
            ALTER TABLE ${GE_Custom_FormDao.TABLE}
                    ADD COLUMN so_optional_justify_problem int not null default 0
        """.trimIndent()
        )

        db.execSQL("""
            ALTER TABLE ${GE_Custom_Form_LocalDao.TABLE}
                    ADD COLUMN so_optional_justify_problem int not null default 0;
        """.trimIndent()
        )

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

        db.execSQL(
            """
                ALTER TABLE ${TkTicketCacheDao.TABLE}
                        ADD COLUMN open_zone_desc text COLLATE nocase;
            """.trimIndent()
        )
        //
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


