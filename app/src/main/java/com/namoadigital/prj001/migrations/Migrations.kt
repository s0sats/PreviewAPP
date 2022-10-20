package com.namoadigital.prj001.migrations

import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.dao.*
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
              ALTER TABLE [me_measure_tp_temp] RENAME TO [me_measure_tp]
            """.trimIndent()
        )
        //
    }
}

//val MigrationV2 = object : MigrationSQLite(2, 3) {
//
//    override fun migrate(db: SQLiteDatabase) {
//        db.execSQL(
//            """
//              CREATE TABLE IF NOT EXISTS [ge_namoa_table_test](
//                  [customer_code] int not null,
//                  [user_code] int not null,
//                CONSTRAINT [pk_ge_namoa_table_test]
//                PRIMARY KEY ([customer_code])
//              );
//            """.trimIndent()
//        )
//    }
//}


