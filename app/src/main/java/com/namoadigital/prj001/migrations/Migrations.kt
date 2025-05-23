package com.namoadigital.prj001.migrations

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.addMissingColumns
import com.namoadigital.prj001.core.database.updateColumn
import com.namoadigital.prj001.dao.GE_Custom_FormDao
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.MD_All_ProductDao
import com.namoadigital.prj001.dao.MD_All_Product_Group_ProductDao
import com.namoadigital.prj001.dao.MD_ProductDao
import com.namoadigital.prj001.dao.MD_Product_Group_ProductDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.MdItemCheckDao
import com.namoadigital.prj001.dao.MdOrderTypeDao
import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao
import com.namoadigital.prj001.dao.TK_Ticket_FormDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.database.MigrationSQLite
import com.namoadigital.prj001.database.scripts.multi.FS_TRIP_CREATE_SCRIPT
import com.namoadigital.prj001.database.scripts.multi.FS_TRIP_DESTINATION_ACTION_CREATE_SCRIPT
import com.namoadigital.prj001.database.scripts.multi.FS_TRIP_DESTINATION_CREATE_SCRIPT
import com.namoadigital.prj001.database.scripts.multi.FS_TRIP_EVENT_CREATE_SCRIPT
import com.namoadigital.prj001.database.scripts.multi.FS_TRIP_EVENT_TYPE_CREATE_SCRIPT
import com.namoadigital.prj001.database.scripts.multi.FS_TRIP_POSITION_CREATE_SCRIPT
import com.namoadigital.prj001.database.scripts.multi.FS_TRIP_USER_CREATE_SCRIPT
import com.namoadigital.prj001.database.scripts.multi.masterdata.GEOsVgScript
import com.namoadigital.prj001.database.scripts.multi.masterdata.product_serial.VGProductSerialScript
import com.namoadigital.prj001.database.scripts.multi.masterdata.MD_REGION_CREATE_SCRIPT
import com.namoadigital.prj001.database.scripts.multi.masterdata.mdVerificationGroupDatabaseTable
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_FORCED
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MANUAL
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MANUAL_ALERT
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_NORMAL
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor


val MigrationV1 = object : MigrationSQLite(1, 2) {
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
        if (!isFieldExist(db, "tk_ticket", "class_code")) {
            db.execSQL(""" ALTER TABLE [tk_ticket] ADD [class_code] int;""".trimIndent())
        }
        if (!isFieldExist(db, "tk_ticket", "class_id")) {
            db.execSQL(""" ALTER TABLE [tk_ticket] ADD [class_id] text collate nocase;""".trimIndent())
        }
        if (!isFieldExist(db, "tk_ticket", "class_color")) {
            db.execSQL(""" ALTER TABLE [tk_ticket] ADD [class_color] text collate nocase;""".trimIndent())
        }
        if (!isFieldExist(db, "tk_ticket", "class_available")) {
            db.execSQL(""" ALTER TABLE [tk_ticket] ADD [class_available] int;""".trimIndent())
        }

        if (!isFieldExist(db, "tk_ticket_cache", "class_code")) {
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
        if (!isFieldExist(
                db,
                GE_Custom_FormDao.TABLE,
                GE_Custom_FormDao.BLOCK_SPONTANEOUS_IN_TICKET
            )
        ) {
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
        if (!isFieldExist(
                db,
                SO_Pack_Express_LocalDao.TABLE,
                SO_Pack_Express_LocalDao.PIPELINE_DESC
            )
        ) {
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
        if (!isFieldExist(
                db,
                MD_Product_Serial_Tp_Device_Item_HistDao.TABLE,
                MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO1
            )
        ) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO1}]  text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                MD_Product_Serial_Tp_Device_Item_HistDao.TABLE,
                MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO2
            )
        ) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO2}]  text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                MD_Product_Serial_Tp_Device_Item_HistDao.TABLE,
                MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO3
            )
        ) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO3}]  text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                MD_Product_Serial_Tp_Device_Item_HistDao.TABLE,
                MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO4
            )
        ) {
            db.execSQL(""" ALTER TABLE [${MD_Product_Serial_Tp_Device_Item_HistDao.TABLE}] ADD [${MD_Product_Serial_Tp_Device_Item_HistDao.EXEC_PHOTO4}]  text collate nocase;""".trimIndent())
            db.execSQL(""" UPDATE [${MD_Product_SerialDao.TABLE}] SET [${MD_Product_SerialDao.SCN_ITEM_CHECK}] = 0 WHERE has_item_check = 1;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                GE_Custom_FormDao.TABLE,
                GE_Custom_FormDao.NC_RECOGNIZE_EMAIL_IN_COMMENT
            )
        ) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_FormDao.TABLE}] ADD [${GE_Custom_FormDao.NC_RECOGNIZE_EMAIL_IN_COMMENT}] int not null DEFAULT 0;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                GE_Custom_Form_LocalDao.TABLE,
                GE_Custom_Form_LocalDao.NC_RECOGNIZE_EMAIL_IN_COMMENT
            )
        ) {
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
        db.execSQL(
            """ 
            update ${MD_All_ProductDao.TABLE} 
            set ${MD_All_ProductDao.HAS_GROUP}  =  ( select 
                                     case when count(1) = 0 
                                          then 0 
                                          else 1 
                                     end
                                from  ${MD_All_Product_Group_ProductDao.TABLE} gp
                               where ${MD_All_ProductDao.TABLE}.${MD_All_ProductDao.PRODUCT_CODE} = gp.${MD_All_Product_Group_ProductDao.PRODUCT_CODE} 
            )
        """.trimIndent()
        )
        //
        db.execSQL(
            """ 
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
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.CUSTOM_FORM_VERSION_PARTITION
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.CUSTOM_FORM_VERSION_PARTITION}] int;""".trimIndent())
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
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.ORDER_TYPE_ID
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.ORDER_TYPE_ID}] text collate nocase;""".trimIndent())
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
                TK_Ticket_FormDao.DISPLAY_OPTION
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.DISPLAY_OPTION}] text collate nocase;""".trimIndent())
        }
        //
        if (!isFieldExist(
                db,
                TK_Ticket_FormDao.TABLE,
                TK_Ticket_FormDao.ITEM_CHECK_GROUP_CODE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_FormDao.TABLE}] ADD [${TK_Ticket_FormDao.ITEM_CHECK_GROUP_CODE}] int;""".trimIndent())
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
        if (!isFieldExist(
                db,
                GE_Custom_Form_DataDao.TABLE,
                GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION_PARTITION
            )
        ) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_Form_DataDao.TABLE}] ADD [${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION_PARTITION}] int;""".trimIndent())
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

val migrationV11: MigrationSQLite = object : MigrationSQLite(11, 12) {
    override fun migrate(db: SQLiteDatabase) {
        db.execSQL(FS_TRIP_CREATE_SCRIPT)
        db.execSQL(FS_TRIP_EVENT_TYPE_CREATE_SCRIPT)
        db.execSQL(FS_TRIP_USER_CREATE_SCRIPT)
        db.execSQL(FS_TRIP_EVENT_CREATE_SCRIPT)
        db.execSQL(FS_TRIP_POSITION_CREATE_SCRIPT)
        db.execSQL(FS_TRIP_DESTINATION_CREATE_SCRIPT)
        db.execSQL(FS_TRIP_DESTINATION_ACTION_CREATE_SCRIPT)
        if (!isFieldExist(
                db,
                GE_Custom_Form_DataDao.TABLE,
                GE_Custom_Form_DataDao.TRIP_PREFIX
            )
        ) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_Form_DataDao.TABLE}] ADD [${GE_Custom_Form_DataDao.TRIP_PREFIX}]  int;""".trimIndent())
        }
        if (!isFieldExist(
                db,
                GE_Custom_Form_DataDao.TABLE,
                GE_Custom_Form_DataDao.TRIP_CODE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_Form_DataDao.TABLE}] ADD [${GE_Custom_Form_DataDao.TRIP_CODE}]  int;""".trimIndent())
        }
        if (!isFieldExist(
                db,
                GE_Custom_Form_DataDao.TABLE,
                GE_Custom_Form_DataDao.DESTINATION_SEQ
            )
        ) {
            db.execSQL(""" ALTER TABLE [${GE_Custom_Form_DataDao.TABLE}] ADD [${GE_Custom_Form_DataDao.DESTINATION_SEQ}]  int;""".trimIndent())
        }
        if (!isFieldExist(
                db,
                TK_Ticket_CtrlDao.TABLE,
                TK_Ticket_CtrlDao.TRIP_PREFIX
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_CtrlDao.TABLE}] ADD [${TK_Ticket_CtrlDao.TRIP_PREFIX}]  int;""".trimIndent())
        }
        if (!isFieldExist(
                db,
                TK_Ticket_CtrlDao.TABLE,
                TK_Ticket_CtrlDao.TRIP_CODE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_CtrlDao.TABLE}] ADD [${TK_Ticket_CtrlDao.TRIP_CODE}]  int;""".trimIndent())
        }
        if (!isFieldExist(
                db,
                TK_Ticket_CtrlDao.TABLE,
                TK_Ticket_CtrlDao.DESTINATION_SEQ
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_Ticket_CtrlDao.TABLE}] ADD [${TK_Ticket_CtrlDao.DESTINATION_SEQ}]  int;""".trimIndent())
        }

        if (!isFieldExist(
                db,
                TkTicketCacheDao.TABLE,
                TkTicketCacheDao.KANBAN
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TkTicketCacheDao.TABLE}] ADD [${TkTicketCacheDao.KANBAN}]  int not null default 0;""".trimIndent())
        }

        if (!isFieldExist(
                db,
                TkTicketCacheDao.TABLE,
                TkTicketCacheDao.KANBAN_STAGE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TkTicketCacheDao.TABLE}] ADD [${TkTicketCacheDao.KANBAN_STAGE}]  text;""".trimIndent())
        }

        if (!isFieldExist(
                db,
                TkTicketCacheDao.TABLE,
                TkTicketCacheDao.ABLE_TO_DONE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TkTicketCacheDao.TABLE}] ADD [${TkTicketCacheDao.ABLE_TO_DONE}]  text;""".trimIndent())
        }
        if (!isFieldExist(
                db,
                TkTicketCacheDao.TABLE,
                TkTicketCacheDao.PREVENTIVE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TkTicketCacheDao.TABLE}] ADD [${TkTicketCacheDao.PREVENTIVE}]  int;""".trimIndent())
        }
        if (!isFieldExist(
                db,
                TkTicketCacheDao.TABLE,
                TkTicketCacheDao.IS_PRIORITY
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TkTicketCacheDao.TABLE}] ADD [${TkTicketCacheDao.IS_PRIORITY}]  int;""".trimIndent())
        }
        if (!isFieldExist(
                db,
                TkTicketCacheDao.TABLE,
                TkTicketCacheDao.ADDRESS
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TkTicketCacheDao.TABLE}] ADD [${TkTicketCacheDao.ADDRESS}]  int;""".trimIndent())
        }
        //Tk_Ticket
        var ticketChange = false
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.KANBAN_DATE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.KANBAN_DATE}]  text;""".trimIndent())
            ticketChange = true
        }
        //
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.KANBAN_CUSTOM_FORM_TYPE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.KANBAN_CUSTOM_FORM_TYPE}]  text;""".trimIndent())
            ticketChange = true
        }

        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.KANBAN_CUSTOM_FORM_CODE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.KANBAN_CUSTOM_FORM_CODE}]  text;""".trimIndent())
            ticketChange = true
        }
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.KANBAN_OPEN_CONTINUE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.KANBAN_OPEN_CONTINUE}]  text;""".trimIndent())
            ticketChange = true
        }
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.KANBAN_STAGE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.KANBAN_STAGE}]  text;""".trimIndent())
            ticketChange = true
        }
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.ABLE_TO_DONE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.ABLE_TO_DONE}]  int not null default 0;""".trimIndent())
            ticketChange = true
        }
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.PREVENTIVE
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.PREVENTIVE}]  int;""".trimIndent())
            ticketChange = true
        }
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.IS_PRIORITY
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.IS_PRIORITY}]  int;""".trimIndent())
            ticketChange = true
        }
        if (!isFieldExist(
                db,
                TK_TicketDao.TABLE,
                TK_TicketDao.HAS_ADDRESS
            )
        ) {
            db.execSQL(""" ALTER TABLE [${TK_TicketDao.TABLE}] ADD [${TK_TicketDao.HAS_ADDRESS}]  int not null default 0;""".trimIndent())
            ticketChange = true
        }
        if (ticketChange) {
            db.execSQL(""" UPDATE [${TK_TicketDao.TABLE}] SET [${TK_TicketDao.SYNC_REQUIRED}] = 1;""".trimIndent())
        }
    }
}


val MigrationV12 = object : MigrationSQLite(12, 13) {
    override fun migrate(db: SQLiteDatabase) {
        //Table Region
        db.execSQL(MD_REGION_CREATE_SCRIPT)

        listOf(
            MD_SiteDao.COUNTRY_CODE,
            MD_SiteDao.STATE,
            MD_SiteDao.CITY,
            MD_SiteDao.DISTRICT,
            MD_SiteDao.STREET,
            MD_SiteDao.NUM,
            MD_SiteDao.COMPLEMENT,
            MD_SiteDao.ZIP_CODE,
            MD_SiteDao.PLUS_CODE,
            MD_SiteDao.CONTACT_NAME,
            MD_SiteDao.CONTACT_PHONE,
            MD_SiteDao.LATITUDE,
            MD_SiteDao.LONGITUDE,
        ).forEach { column ->
            db.checkIfFieldExist(
                MD_SiteDao.TABLE,
                column
            ) {
                addColumn(
                    MD_SiteDao.TABLE,
                    column,
                    "text collate nocase"
                )
            }
        }

        db.checkIfFieldExist(
            MD_SiteDao.TABLE,
            MD_SiteDao.REGION_CODE
        ) {
            addColumn(
                MD_SiteDao.TABLE,
                MD_SiteDao.REGION_CODE,
                "int"
            )
        }

        //Table Event
        db.checkIfFieldExist(
            FSTripEventDao.TABLE,
            FSTripEventDao.PHOTO_CHANGED
        ) {
            addColumn(
                FSTripEventDao.TABLE,
                FSTripEventDao.PHOTO_CHANGED,
                "int not null default 0"
            )
        }

        //Table Destination
        db.checkIfFieldExist(
            FsTripDestinationDao.TABLE,
            FsTripDestinationDao.ARRIVED_FLEET_PHOTO_CHANGED
        ) {
            addColumn(
                FsTripDestinationDao.TABLE,
                FsTripDestinationDao.ARRIVED_FLEET_PHOTO_CHANGED,
                "int not null default 0"
            )
        }

        listOf(
            FSTripDao.FLEET_START_PHOTO_CHANGED,
            FSTripDao.FLEET_END_PHOTO_CHANGED,
            FSTripDao.UPDATE_REQUIRED
        ).forEach { column ->
            db.checkIfFieldExist(
                FSTripDao.TABLE,
                column
            ) {
                addColumn(
                    FSTripDao.TABLE,
                    column,
                    "int not null default 0"
                )
            }
        }

        listOf(
            TK_TicketDao.ADDRESS_COUNTRY_ID,
            TK_TicketDao.ADDRESS_PLUS_CODE,
            TK_TicketDao.CONTACT_NAME,
            TK_TicketDao.CONTACT_PHONE,
        ).forEach { column ->
            db.checkIfFieldExist(
                TK_TicketDao.TABLE,
                column
            ) {
                addColumn(
                    TK_TicketDao.TABLE,
                    column,
                    "text collate nocase"
                )
            }
        }
    }
}

val migrationV13 = object : MigrationSQLite(13, 14) {
    override fun migrate(db: SQLiteDatabase) {
        db.checkIfFieldExist(MD_ProductDao.TABLE, MD_ProductDao.IS_CLASS_REQUIRED) {
            addColumn(
                MD_ProductDao.TABLE,
                MD_ProductDao.IS_CLASS_REQUIRED,
                "int not null default 0"
            )
        }
    }
}

val MigrationV14 = object : MigrationSQLite(14, 15) {
    override fun migrate(db: SQLiteDatabase) {

        db.addMissingColumnsIfNecessary(tableName = MD_Product_SerialDao.TABLE) {
            mapOf(
                MD_Product_SerialDao.HORIMETER to "real",
                MD_Product_SerialDao.HORIMETER_DATE to "text COLLATE NOCASE",
                MD_Product_SerialDao.HORIMETER_SUPPLIER_UID to "text COLLATE NOCASE",
                MD_Product_SerialDao.HORIMETER_SUPPLIER_DESC to "text COLLATE NOCASE",
                MD_Product_SerialDao.MEASURE_BLOCK_INPUT_TIME to "int",
                MD_Product_SerialDao.MEASURE_ALERT_INPUT_TIME to "int",
                MD_Product_SerialDao.UNAVAILABILITY_REASON_OPTION to "int not null default 0",
            )
        }
    }
}

val migrationV15 = object : MigrationSQLite(15, 16) {
    override fun migrate(db: SQLiteDatabase) {
        db.addMissingColumnsIfNecessary(tableName = TK_TicketDao.TABLE) {
            mapOf(
                TK_TicketDao.OPEN_SERIAL_STOPPED to "text",
                TK_TicketDao.OPEN_DESIRED_DATE to "text"
            )
        }

        db.addMissingColumnsIfNecessary(tableName = TK_TicketDao.TABLE) {
            mapOf(
                TK_TicketDao.IS_SERIAL_STOPPED to "int not null default 0"
            )
        }

        db.addMissingColumnsIfNecessary(tableName = TK_TicketDao.TABLE) {
            mapOf(
                TK_TicketDao.STOPPED_DATE to "text",
                TK_TicketDao.DESIRED_DATE to "text"
            )
        }

        db.updateColumn(
            TK_TicketDao.TABLE,
            TK_TicketDao.SYNC_REQUIRED,
            "1"
        )

        db.addMissingColumnsIfNecessary(tableName = GE_Custom_Form_DataDao.TABLE) {
            mapOf(
                GE_Custom_Form_DataDao.INITIAL_IS_SERIAL_STOPPED to "int",
                GE_Custom_Form_DataDao.FINAL_IS_SERIAL_STOPPED to "int",
            )
        }

        db.addMissingColumnsIfNecessary(tableName = GE_Custom_Form_DataDao.TABLE) {
            mapOf(
                GE_Custom_Form_DataDao.FINAL_UNAVAILABILITY_REASON to "text",
                GE_Custom_Form_DataDao.INITIAL_UNAVAILABILITY_REASON to "text",
                GE_Custom_Form_DataDao.INITIAL_STOPPED_DATE to "text"
            )
        }
        //
        db.addMissingColumnsIfNecessary(tableName = GeOsDao.TABLE) {
            mapOf(
                GeOsDao.INITIAL_IS_SERIAL_STOPPED to "int",
                GeOsDao.FINAL_IS_SERIAL_STOPPED to "int",
            )
        }
        //
        db.addMissingColumnsIfNecessary(tableName = GeOsDao.TABLE) {
            mapOf(
                GeOsDao.FINAL_UNAVAILABILITY_REASON to "text",
                GeOsDao.INITIAL_UNAVAILABILITY_REASON to "text",
                GeOsDao.INITIAL_STOPPED_DATE to "text"
            )
        }

        db.addMissingColumnsIfNecessary(tableName = GeOsDao.TABLE) {
            mapOf(GeOsDao.ALLOW_FORM_IN_THE_PAST to "int not null default 0")
        }

        db.updateColumn(
            GeOsDao.TABLE,
            GeOsDao.ALLOW_FORM_IN_THE_PAST,
            "1"
        )
    }
}

val migrationV16 = object : MigrationSQLite(16, 17) {
    override fun migrate(db: SQLiteDatabase) {
        db.addMissingColumnsIfNecessary(tableName = SM_SODao.TABLE) {
            mapOf(
                SM_SODao.RESERVED_USER to "int",
                SM_SODao.RESERVED_USER_NICK to "text",
                SM_SODao.RESERVED_USER_NAME to "text",
                SM_SODao.RESERVED_DATE to "text"
            )
        }
    }

}

val migrationV17 = object : MigrationSQLite(17, 18) {
    override fun migrate(db: SQLiteDatabase) {

        db.addMissingColumns(
            tableName = GE_Custom_Form_FieldDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = GE_Custom_Form_FieldDao.BUTTON_NC,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "1"
                ),
                DatabaseTable.Column(
                    name = GE_Custom_Form_FieldDao.BUTTON_PHOTO,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "1"
                ),
                DatabaseTable.Column(
                    name = GE_Custom_Form_FieldDao.BUTTON_COMMENT,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "1"
                )
            )
        )

        db.addMissingColumns(
            tableName = GE_Custom_Form_Field_LocalDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = GE_Custom_Form_Field_LocalDao.BUTTON_NC,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "1"
                ),
                DatabaseTable.Column(
                    name = GE_Custom_Form_Field_LocalDao.BUTTON_PHOTO,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "1"
                ),
                DatabaseTable.Column(
                    name = GE_Custom_Form_Field_LocalDao.BUTTON_COMMENT,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "1"
                )
            )
        )
    }
}

val migrationV18 = object : MigrationSQLite(18, 19) {
    override fun migrate(db: SQLiteDatabase) {
        db.addMissingColumns(
            tableName = TK_TicketDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = TK_TicketDao.IS_TMP,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                )
            )
        )
        //
        db.updateColumn(
            TK_TicketDao.TABLE,
            TK_TicketDao.SYNC_REQUIRED,
            "1"
        )
    }

}

val migrationV19 = object : MigrationSQLite(19, 20) {
    override fun migrate(db: SQLiteDatabase) {
        db.addMissingColumns(
            tableName = MD_Product_Serial_Tp_Device_ItemDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.TICKET_PREFIX,
                    type = ColumnType.INT,
                    isNullable = true,
                ),
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.TICKET_CODE,
                    type = ColumnType.INT,
                    isNullable = true,
                ),

            )
        )
        //
        db.addMissingColumns(
            tableName = GeOsDeviceItemDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.TICKET_PREFIX,
                    type = ColumnType.INT,
                    isNullable = true,
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.TICKET_CODE,
                    type = ColumnType.INT,
                    isNullable = true,
                ),
            )
        )
        //
        db.execSQL(""" UPDATE [${MD_Product_SerialDao.TABLE}] SET [${MD_Product_SerialDao.SCN_ITEM_CHECK}] = 0 WHERE [${MD_Product_SerialDao.HAS_ITEM_CHECK}]  = 1;""".trimIndent())
    }
}

val migrationV20 = object : MigrationSQLite(20, 21) {
    override fun migrate(db: SQLiteDatabase) {
        //
        db.execSQL(VGProductSerialScript)
        //
        db.execSQL(GEOsVgScript)
        //
        db.addMissingColumns(
            tableName = MdOrderTypeDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = MdOrderTypeDao.FORCE_EXE_EXPIRED_VG,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                )
            )
        )
        //
        db.execSQL(mdVerificationGroupDatabaseTable)
        //
        db.addMissingColumns(
            tableName = MdItemCheckDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = MdItemCheckDao.ITEM_CHECK_DESC_ALT_VG,
                    type = ColumnType.TEXT,
                    isNullable = true,
                    collation = CollationType.NOCASE,
                )
            )
        )
        //
        db.addMissingColumns(
            tableName = GeOsDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = GeOsDao.FORCE_EXE_EXPIRED_VG,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                )
            )
        )
        //
        db.addMissingColumns(
            tableName = MD_Product_Serial_Tp_Device_ItemDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.ALREADY_OK_HIDE,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.REQUIRE_PHOTO_FIXED,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.REQUIRE_PHOTO_ALERT,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.REQUIRE_PHOTO_ALREADY_OK,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.REQUIRE_PHOTO_NOT_VERIFIED,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.VG_CODE,
                    type = ColumnType.INT,
                    isNullable = true
                ),
                DatabaseTable.Column(
                    name = MD_Product_Serial_Tp_Device_ItemDao.VG_ACTION,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
            )
        )
        //
        migrateGeOsDeviceItemTable(db)
        //
        db.execSQL(
            """ 
            UPDATE ${GeOsDeviceItemDao.TABLE} 
            SET ${GeOsDeviceItemDao.COLOR_ITEM}  = '${GeOsDeviceItemStatusColor.RED}',
                ${GeOsDeviceItemDao.IS_VISIBLE}  = 1
            WHERE ${GeOsDeviceItemDao.ITEM_CHECK_STATUS} =  '${ITEM_CHECK_STATUS_MANUAL_ALERT}'
        """.trimIndent()
        )
        //
        db.execSQL(
            """ 
            UPDATE ${GeOsDeviceItemDao.TABLE} 
            SET ${GeOsDeviceItemDao.COLOR_ITEM}  = '${GeOsDeviceItemStatusColor.YELLOW}',
                ${GeOsDeviceItemDao.IS_VISIBLE}  = 1
            WHERE ${GeOsDeviceItemDao.ITEM_CHECK_STATUS} not in  ('${ITEM_CHECK_STATUS_MANUAL_ALERT}','${ITEM_CHECK_STATUS_NORMAL}','${ITEM_CHECK_STATUS_FORCED}','${ITEM_CHECK_STATUS_MANUAL}') 
              AND ${GeOsDeviceItemDao.CRITICAL_ITEM} = 1  
        """.trimIndent()
        )
        //
        db.execSQL(
            """ 
            UPDATE ${GeOsDeviceItemDao.TABLE} 
            SET ${GeOsDeviceItemDao.COLOR_ITEM}  = '${GeOsDeviceItemStatusColor.BLUE}',
                ${GeOsDeviceItemDao.IS_VISIBLE}  = 1
            WHERE ${GeOsDeviceItemDao.ITEM_CHECK_STATUS} not in  ('${ITEM_CHECK_STATUS_MANUAL_ALERT}','${ITEM_CHECK_STATUS_NORMAL}','${ITEM_CHECK_STATUS_MANUAL}') 
              AND (${GeOsDeviceItemDao.CRITICAL_ITEM} = 0 
                    OR ${GeOsDeviceItemDao.ITEM_CHECK_STATUS} = "${ITEM_CHECK_STATUS_FORCED}"
                  )              
            
        """.trimIndent()
        )
        //
        db.execSQL(
            """ 
            UPDATE ${GeOsDeviceItemDao.TABLE} 
            SET ${GeOsDeviceItemDao.IS_VISIBLE}  = 1
            WHERE ${GeOsDeviceItemDao.PARTITIONED_EXECUTION} = 1   
                OR ${GeOsDeviceItemDao.ITEM_CHECK_STATUS} =  '${ITEM_CHECK_STATUS_MANUAL}'
        """.trimIndent()
        )
        //
        db.execSQL(""" UPDATE [${MD_Product_SerialDao.TABLE}] SET [${MD_Product_SerialDao.SCN_ITEM_CHECK}] = 0 WHERE [${MD_Product_SerialDao.HAS_ITEM_CHECK}]  = 1;""".trimIndent())
    }

    private fun migrateGeOsDeviceItemTable(db:SQLiteDatabase) {
        db.addMissingColumns(
            tableName = GeOsDeviceItemDao.TABLE,
            columnsToAdd = listOf(
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.ITEM_CHECK_DESC_ALT_VG,
                    type = ColumnType.TEXT,
                    isNullable = true,
                    collation = CollationType.NOCASE,
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.ALREADY_OK_HIDE,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.REQUIRE_PHOTO_FIXED,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.REQUIRE_PHOTO_ALERT,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.REQUIRE_PHOTO_ALREADY_OK,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.REQUIRE_PHOTO_NOT_VERIFIED,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.VG_CODE,
                    type = ColumnType.INT,
                    isNullable = true
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.VG_ACTION,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.IS_VISIBLE,
                    type = ColumnType.INT,
                    isNullable = false,
                    defaultValue = "0"
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.COLOR_ITEM,
                    type = ColumnType.TEXT,
                    isNullable = false,
                    collation = CollationType.NOCASE,
                    defaultValue = GeOsDeviceItemStatusColor.GRAY.toString(),
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.STATUS_MODIFICATION_TYPE,
                    type = ColumnType.TEXT,
                    isNullable = true,
                    collation = CollationType.NOCASE,
                ),
                DatabaseTable.Column(
                    name = GeOsDeviceItemDao.AUTOMATIC_SELECTION_STATE,
                    type = ColumnType.TEXT,
                    isNullable = true,
                    collation = CollationType.NOCASE,
                ),
            )
        )
    }
}



@Deprecated(message = "Use a função com objeto Column")
fun SQLiteDatabase.addMissingColumnsIfNecessary(
    tableName: String,
    columnsToAdd: () -> Map<String, String>
) {
    columnsToAdd().forEach { (column, type) ->
        this@addMissingColumnsIfNecessary.checkIfFieldExist(tableName, column) {
            addColumn(tableName, column, type)
        }
    }
}

@Deprecated(message = "Use a função com objeto Column")
private fun SQLiteDatabase.addColumn(tableName: String, columnName: String, columnType: String) {
    execSQL(
        """
        ALTER TABLE $tableName
        ADD COLUMN [$columnName] $columnType;
    """.trimIndent()
    )
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

inline fun SQLiteDatabase.checkIfFieldExist(
    tableName: String,
    fieldName: String,
    block: SQLiteDatabase.() -> Unit
) {
    if (!isFieldExist(this, tableName, fieldName)) {
        block()
    }
}
