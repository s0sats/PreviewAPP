package com.namoadigital.prj001.migrations

import android.database.Cursor
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
        if (!isFieldExist(db, GE_Custom_FormDao.TABLE, GE_Custom_FormDao.JUSTIFY_GROUP_CODE)) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_FormDao.TABLE}] ADD [${GE_Custom_FormDao.JUSTIFY_GROUP_CODE}] int;""".trimIndent())
        }
        if (!isFieldExist(db, GE_Custom_FormDao.TABLE, GE_Custom_Form_DataDao.FINALIZED_SERVICE)) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_Form_DataDao.TABLE}] ADD [${GE_Custom_Form_DataDao.FINALIZED_SERVICE}] int;""".trimIndent())
        }
        if (!isFieldExist(db, TK_TicketDao.TABLE, TK_TicketDao.KANBAN)) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.KANBAN}] int not null default 0;""".trimIndent())
            db.execSQL(""" UPDATE [${TK_TicketDao.TABLE}] SET [${TK_TicketDao.SYNC_REQUIRED}] = 1;""".trimIndent())
        }

    }

}

val MigrationV5 = object : MigrationSQLite(5, 6) {

    override fun migrate(db: SQLiteDatabase) {
        db.execSQL(
            """
               create table if not exists [sm_priority] (
                   [customer_code] int not null, 
                   [priority_code] int not null, 
                   [priority_desc] text not null collate nocase, 
                   [priority_weight] int not null, 
                   [priority_default] int not null, 
                   [priority_color] text not null collate nocase, 
                   constraint [pk_sm_priority] 
                   primary key(customer_code, priority_code)
               ); 
            """.trimIndent()
        )
        //
        if (!isFieldExist(db, GE_Custom_FormDao.TABLE, GE_Custom_FormDao.BLOCK_SPONTANEOUS_IN_TICKET)) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_FormDao.TABLE}] ADD [${GE_Custom_FormDao.BLOCK_SPONTANEOUS_IN_TICKET}] int not null DEFAULT 0;""".trimIndent())
        }
        //
    }

}

val MigrationV6 = object : MigrationSQLite(6, 7) {

    override fun migrate(db: SQLiteDatabase) {
        //
        if (!isFieldExist(db, MeMeasureTpDao.TABLE, MeMeasureTpDao.WITHOUT_MEASURE)) {
            db.execSQL(""" ALTER TABLE [${MeMeasureTpDao.TABLE}] ADD [${MeMeasureTpDao.WITHOUT_MEASURE}] int not null DEFAULT 0;""".trimIndent())
        }
        //
    }

}
val MigrationV7 = object : MigrationSQLite(7, 8) {

    override fun migrate(db: SQLiteDatabase) {
        //
        if (!isFieldExist(db, SM_SODao.TABLE, SM_SODao.DEADLINE_MANUAL)) {
            db.execSQL(""" ALTER TABLE [${SM_SODao.TABLE}] ADD [${SM_SODao.DEADLINE_MANUAL}] int not null DEFAULT 0;""".trimIndent())
        }
        //
        if (!isFieldExist(db, SM_SODao.TABLE, SM_SODao.HAS_CLIENT_DEADLINE)) {
            db.execSQL(""" ALTER TABLE [${SM_SODao.TABLE}] ADD [${SM_SODao.HAS_CLIENT_DEADLINE}] int not null DEFAULT 0;""".trimIndent())
            db.execSQL(""" UPDATE [${SM_SODao.TABLE}] SET [${SM_SODao.SYNC_REQUIRED}] = 1;""".trimIndent())
        }
        //
        if (!isFieldExist(db, SO_Pack_ExpressDao.TABLE, SO_Pack_ExpressDao.PIPELINE_DESC)) {
            db.execSQL(""" ALTER TABLE [${SO_Pack_ExpressDao.TABLE}] ADD [${SO_Pack_ExpressDao.PIPELINE_DESC}]  text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(db, SO_Pack_Express_LocalDao.TABLE, SO_Pack_Express_LocalDao.PIPELINE_DESC)) {
            db.execSQL(""" ALTER TABLE [${SO_Pack_Express_LocalDao.TABLE}] ADD [${SO_Pack_Express_LocalDao.PIPELINE_DESC}] text collate nocase;""".trimIndent())
        }
    }

}


val MigrationV8 = object : MigrationSQLite(8, 9) {
    override fun migrate(db: SQLiteDatabase) {
        db.execSQL(
            """
            create table [md_product_serial_tp_device_item_hist_mat]
            (
                    [customer_code]   int  not null,
                    [product_code]    int  not null,
                    [serial_code]     int  not null,
                    [device_tp_code]  int  not null,
                    [item_check_code] int  not null,
                    [item_check_seq]  int  not null,
                    [seq]             int  not null,
                    [material_code]   int  not null,
                    [un]              text not null collate nocase,
                    [qty]             real  not null,
                    [qty_planned]     real  not null,
                    [material_action] int  not null,
                    constraint [pk_md_product_serial_tp_device_item_hist_mat] primary key (
                    [customer_code],
                    [product_code],
                    [serial_code],
                    [device_tp_code],
                    [item_check_seq],
                    [item_check_code],
                    [seq],
                    [material_code]
                )
            );
        """.trimIndent()
        )
        //
        if (!isFieldExist(db, MD_Product_Serial_Tp_Device_Item_HistDao.TABLE, MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO1)) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO1}]  text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(db, MD_Product_Serial_Tp_Device_Item_HistDao.TABLE, MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO2)) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO2}]  text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(db, MD_Product_Serial_Tp_Device_Item_HistDao.TABLE, MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO3)) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO3}]  text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(db, MD_Product_Serial_Tp_Device_Item_HistDao.TABLE, MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO4)) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO4}]  text collate nocase;""".trimIndent())
            db.execSQL(""" UPDATE [${MD_Product_SerialDao.TABLE}] SET [${MD_Product_SerialDao.SCN_ITEM_CHECK}] = 0 WHERE has_item_check = 1;""".trimIndent())
        }
        //
        if (!isFieldExist(db, GE_Custom_FormDao.TABLE, GE_Custom_FormDao.NC_RECOGNIZE_EMAIL_IN_COMMENT)) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_FormDao.TABLE}] ADD [${GE_Custom_FormDao.NC_RECOGNIZE_EMAIL_IN_COMMENT}] int not null DEFAULT 0;""".trimIndent())
        }
        //
        if (!isFieldExist(db, GE_Custom_Form_LocalDao.TABLE, GE_Custom_Form_LocalDao.NC_RECOGNIZE_EMAIL_IN_COMMENT)) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_Form_LocalDao.TABLE}] ADD [${GE_Custom_Form_LocalDao.NC_RECOGNIZE_EMAIL_IN_COMMENT}] int not null DEFAULT 0;""".trimIndent())
        }
        //
        if (!isFieldExist(db, MD_SiteDao.TABLE, MD_SiteDao.EMAIL_NC)) {
            db.execSQL(""" ALTER TABLE [${MD_SiteDao.TABLE}] ADD [${MD_SiteDao.EMAIL_NC}] text collate nocase;""".trimIndent())
        }
        //
    }

}

val migrationV9: MigrationSQLite = object : MigrationSQLite(9, 10) {
    override fun migrate(db: SQLiteDatabase) {
        //
        if (!isFieldExist(db, MD_All_ProductDao.TABLE, MD_All_ProductDao.HAS_GROUP)) {
            db.execSQL(""" ALTER TABLE [${MD_All_ProductDao.TABLE}] ADD [${MD_All_ProductDao.HAS_GROUP}]   int not null default 0;""".trimIndent())
        }
        //
        if (!isFieldExist(db, MD_ProductDao.TABLE, MD_ProductDao.HAS_GROUP)) {
            db.execSQL(""" ALTER TABLE [${MD_ProductDao.TABLE}] ADD [${MD_ProductDao.HAS_GROUP}]   int not null default 0;""".trimIndent())
        }
        //
        db.execSQL(""" 
            update ${MD_All_ProductDao.TABLE} 
            set ${MD_All_ProductDao.HAS_GROUP}  =  ( select 
                                     case when count(1) = 0 
                                          then 0 
                                          else 1 
                                     end
                                from  ${MD_All_Product_Group_ProductDao.TABLE} gp
                               where ${MD_All_ProductDao.TABLE}.${MD_All_ProductDao.PRODUCT_CODE} = gp.${MD_All_Product_Group_ProductDao.PRODUCT_CODE} 
            )
        """.trimIndent())
        //
        db.execSQL(""" 
            update ${MD_ProductDao.TABLE} 
            set ${MD_ProductDao.HAS_GROUP}  =  ( select 
                                     case when count(1) = 0 
                                          then 0 
                                          else 1 
                                     end
                                from  ${MD_Product_Group_ProductDao.TABLE} gp
                               where ${MD_ProductDao.TABLE}.${MD_ProductDao.PRODUCT_CODE} = gp.${MD_Product_Group_ProductDao.PRODUCT_CODE} 
            )
        """.trimIndent()
        )
        //
    }
}

val migrationV10: MigrationSQLite = object : MigrationSQLite(10, 11) {
    override fun migrate(db: SQLiteDatabase) {
        //
        //region MD_Product_Serial_Tp_Device_ItemDao
        if (!isFieldExist(
                db,
                MD_Product_Serial_Tp_Device_ItemDao.TABLE,
                MD_Product_Serial_Tp_Device_ItemDao.PARTITIONED_EXECUTION
            )
        ) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_ItemDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_ItemDao.PARTITIONED_EXECUTION}] int not null default 0;""".trimIndent())
        }
        //endregion
        //
        //region TK_TicketDao
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.HAS_OPEN_SO_PARTITION
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.HAS_OPEN_SO_PARTITION}] int not null default 0;""".trimIndent())
            db.execSQL(""" UPDATE [${TK_TicketDao.TABLE}] SET [${TK_TicketDao.SYNC_REQUIRED}] = 1;""".trimIndent())
        }
        //endregion
        //
        //region TK_Ticket_FormDao
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.CUSTOM_FORM_DATA_PARTITION
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.CUSTOM_FORM_DATA_PARTITION}] int;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.ORDER_TYPE_CODE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.ORDER_TYPE_CODE}] int;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.ORDER_TYPE_DESC
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.ORDER_TYPE_DESC}] text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.PROCESS_TYPE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.PROCESS_TYPE}] text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.MEASURE_TP_CODE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.MEASURE_TP_CODE}] int;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.MEASURE_TP_DESC
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.MEASURE_TP_DESC}] text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.MEASURE_VALUE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.MEASURE_VALUE}] real;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.MEASURE_CYCLE_VALUE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.MEASURE_CYCLE_VALUE}] real;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.VALUE_SUFIX
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.VALUE_SUFIX}] text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.START_DATE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.START_DATE}] text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.PARTITION_MIN_DATE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.PARTITION_MIN_DATE}] text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                GeOsDeviceItemDao.TABLE,
                GeOsDeviceItemDao.PARTITIONED_EXECUTION
            )
        ) {
            db.execSQL(""" ALTER TABLE [${GeOsDeviceItemDao.TABLE}] ADD [${GeOsDeviceItemDao.PARTITIONED_EXECUTION}] int not null default 0;""".trimIndent())
        }
        //endregion
        //
        if (!isFieldExist(
                db,
                GE_Custom_Form_DataDao.TABLE,
                GE_Custom_Form_DataDao.CUSTOM_FORM_DATA_PARTITION
            )
        ) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_Form_DataDao.TABLE}] ADD [${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA_PARTITION}] int;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                GE_Custom_Form_DataDao.TABLE,
                GE_Custom_Form_DataDao.KANBAN_RESCHEDULE_DATE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_Form_DataDao.TABLE}] ADD [${GE_Custom_Form_DataDao.KANBAN_RESCHEDULE_DATE}]  text collate nocase;""".trimIndent())
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


