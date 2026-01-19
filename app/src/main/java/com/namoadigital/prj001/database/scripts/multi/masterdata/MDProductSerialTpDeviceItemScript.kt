package com.namoadigital.prj001.database.scripts.multi.masterdata

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor

private val mdProductSerialTpDeviceItemTable = with(MD_Product_Serial_Tp_Device_ItemDao) {
    DatabaseTable(
        name = TABLE,
        columns = mutableListOf(
            DatabaseTable.Column(name = CUSTOMER_CODE, type = ColumnType.INT),
            DatabaseTable.Column(name = PRODUCT_CODE, type = ColumnType.INT),
            DatabaseTable.Column(name = SERIAL_CODE, type = ColumnType.INT),
            DatabaseTable.Column(name = DEVICE_TP_CODE, type = ColumnType.INT),
            DatabaseTable.Column(name = ITEM_CHECK_CODE, type = ColumnType.INT),
            DatabaseTable.Column(name = ITEM_CHECK_SEQ, type = ColumnType.INT),
            DatabaseTable.Column(
                name = APPLY_MATERIAL,
                type = ColumnType.TEXT,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = VERIFICATION_INSTRUCTION,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = REQUIRE_JUSTIFY_PROBLEM,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            DatabaseTable.Column(name = CRITICAL_ITEM, type = ColumnType.INT, defaultValue = "0"),
            DatabaseTable.Column(name = CHANGE_ADJUST, type = ColumnType.INT, defaultValue = "0"),
            DatabaseTable.Column(name = ORDER_SEQ, type = ColumnType.INT),
            DatabaseTable.Column(name = STRUCTURE, type = ColumnType.INT),
            DatabaseTable.Column(name = ALREADY_OK_HIDE, type = ColumnType.INT, defaultValue = "0"),
            DatabaseTable.Column(
                name = REQUIRE_PHOTO_FIXED,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            DatabaseTable.Column(
                name = REQUIRE_PHOTO_ALERT,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            DatabaseTable.Column(
                name = REQUIRE_PHOTO_ALREADY_OK,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            DatabaseTable.Column(
                name = REQUIRE_PHOTO_NOT_VERIFIED,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            DatabaseTable.Column(name = VG_CODE, type = ColumnType.INT, isNullable = true),
            DatabaseTable.Column(
                name = MANUAL_DESC,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = NEXT_CYCLE_MEASURE,
                type = ColumnType.REAL,
                isNullable = true
            ),
            DatabaseTable.Column(
                name = NEXT_CYCLE_MEASURE_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = NEXT_CYCLE_LIMIT_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = ITEM_CHECK_STATUS,
                type = ColumnType.TEXT,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = TARGET_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = PARTITIONED_EXECUTION,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            DatabaseTable.Column(name = TICKET_PREFIX, type = ColumnType.INT, isNullable = true),
            DatabaseTable.Column(name = TICKET_CODE, type = ColumnType.INT, isNullable = true),
            DatabaseTable.Column(name = VG_ACTION, type = ColumnType.INT, defaultValue = "0"),
            DatabaseTable.Column(
                name = MEASURE_ACTIVE,
                type = ColumnType.INT,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = MEASURE_REQUIRE_ID,
                type = ColumnType.INT,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = MEASURE_UN,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = MEASURE_MIN,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = MEASURE_MAX,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = MEASURE_ALERT_MIN,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = MEASURE_ALERT_MAX,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = LAST_MEASURE_VALUE,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = LAST_MEASURE_ID,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = LAST_MEASURE_UN,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            DatabaseTable.Column(
                name = LAST_MEASURE_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = LAST_MEASURE_ALERT,
                type = ColumnType.INT,
                isNullable = true,
            ),
            DatabaseTable.Column(
                name = ITEM_CHECK_STATUS_COLOR,
                type = ColumnType.TEXT,
                collation = CollationType.NOCASE,
                isNullable = false,
                defaultValue = GeOsDeviceItemStatusColor.GRAY.toString()
            )
        ),
        primaryKey = listOf(
            CUSTOMER_CODE,
            PRODUCT_CODE,
            SERIAL_CODE,
            DEVICE_TP_CODE,
            ITEM_CHECK_CODE,
            ITEM_CHECK_SEQ
        )
    )
}

// Agora você pode gerar o script SQL usando o método da classe:
val mdProductSerialTpDeviceItemTableScript =
    mdProductSerialTpDeviceItemTable.generateCreateTableScript()
