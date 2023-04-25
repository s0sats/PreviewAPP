package com.namoadigital.prj001.migrations

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.dao.MdJustifyItemDao
import com.namoadigital.prj001.database.MigrationSQLite

val MigrationV1 = object : MigrationSQLite(1, 2){
    override fun migrate(db: SQLiteDatabase) {
        //
        db.execSQL(
            """
               CREATE TABLE IF NOT EXISTS [me_measure_tp_temp] (
                    [customer_code] int not null, 
                    [measure_tp_code] int not null,
                    [measure_tp_id] text not null collate nocase,
                    [measure_tp_desc] text not null collate nocase,
                    [value_sufix] text collate nocase, 
                    [restriction_type] text collate nocase, 
                    [restriction_min] real,
                    [restriction_max] real, 
                    [restriction_decimal] int,
                    [value_cycle_size] real,
                    [cycle_tolerance] int,  
               constraint [pk_me_measure_tp] 
               primary key(
                       customer_code,
                       measure_tp_code
                )); 
            """.trimIndent()
        )
        //
        db.execSQL(
            """
               INSERT INTO [me_measure_tp_temp] 
                   SELECT 
                        customer_code, 
                        measure_tp_code,
                        measure_tp_id,
                        measure_tp_desc,
                        value_sufix, 
                        restriction_type, 
                        restriction_min,
                        restriction_max, 
                        restriction_decimal,
                        value_cycle_size,
                        cycle_tolerance  
                    FROM [me_measure_tp]  
            """.trimIndent()
        )
        //
        db.execSQL(
            """
              DROP TABLE [me_measure_tp]
            """.trimIndent()
        )
        //
        db.execSQL(
            """
              ALTER TABLE [me_measure_tp_temp] RENAME TO [me_measure_tp]
            """.trimIndent()
        )
        //
    }
}

val MigrationV2 = object : MigrationSQLite(2, 3) {

    override fun migrate(db: SQLiteDatabase) {
        if(!isFieldExist(db,"tk_ticket", "class_code")){
            db.execSQL(""" ALTER TABLE [tk_ticket] ADD [class_code] int;""".trimIndent())
        }
        if(!isFieldExist(db,"tk_ticket", "class_id")){
            db.execSQL(""" ALTER TABLE [tk_ticket] ADD [class_id] text collate nocase;""".trimIndent())
        }
        if(!isFieldExist(db,"tk_ticket", "class_color")){
            db.execSQL(""" ALTER TABLE [tk_ticket] ADD [class_color] text collate nocase;""".trimIndent())
        }
        if(!isFieldExist(db,"tk_ticket", "class_available")){
            db.execSQL(""" ALTER TABLE [tk_ticket] ADD [class_available] int;""".trimIndent())
        }

        if(!isFieldExist(db,"tk_ticket_cache", "class_code")){
            db.execSQL(""" ALTER TABLE [tk_ticket_cache] ADD [class_code] int;""".trimIndent())
        }
        if (!isFieldExist(db, "tk_ticket_cache", "class_id")) {
            db.execSQL(""" ALTER TABLE [tk_ticket_cache] ADD [class_id] text collate nocase;""".trimIndent())
        }
        if (!isFieldExist(db, "tk_ticket_cache", "class_color")) {
            db.execSQL(""" ALTER TABLE [tk_ticket_cache] ADD [class_color] text collate nocase;""".trimIndent())
        }
        if (!isFieldExist(db, "tk_ticket_cache", "class_available")) {
            db.execSQL(""" ALTER TABLE [tk_ticket_cache] ADD [class_available] int;""".trimIndent())
        }
    }
}

val MigrationV3 = object : MigrationSQLite(3, 4) {

    override fun migrate(db: SQLiteDatabase) {
        if (!isFieldExist(db, "md_product_serials", "last_cycle_date")) {
            db.execSQL(""" ALTER TABLE [md_product_serials] ADD [last_cycle_date] text collate nocase;""".trimIndent())
        }
    }
}

val MigrationV4 = object : MigrationSQLite(4, 5) {

    override fun migrate(db: SQLiteDatabase) {
        if (!isFieldExist(db, MdJustifyItemDao.TABLE, MdJustifyItemDao.JUSTIFY_GROUP_CODE)) {
            db.execSQL(""" ALTER TABLE [${MdJustifyItemDao.TABLE}] ADD [${MdJustifyItemDao.JUSTIFY_GROUP_CODE}] text collate nocase;""".trimIndent())
        }
    }

}

fun isFieldExist(db: SQLiteDatabase, tableName: String, fieldName: String): Boolean {

    val res: Cursor = db.rawQuery("PRAGMA table_info($tableName)", null)
    res.moveToFirst()
    do {
        val currentColumn: String = res.getString(1)
        if (currentColumn == fieldName) {
            res.close()
            return true
        }
    } while (res.moveToNext())
    return false
}


