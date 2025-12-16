package com.namoadigital.prj001.database.scripts.multi.ge_os

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.DatabaseTable.Column
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.MdItemCheckDao

val geOsDeviceItemTable = with(GeOsDeviceItemDao) {
    DatabaseTable(
        name = TABLE,
        columns = mutableListOf(
            Column(name = CUSTOMER_CODE, type = ColumnType.INT),
            Column(name = CUSTOM_FORM_TYPE, type = ColumnType.INT),
            Column(name = CUSTOM_FORM_CODE, type = ColumnType.INT),
            Column(name = CUSTOM_FORM_VERSION, type = ColumnType.INT),
            Column(name = CUSTOM_FORM_DATA, type = ColumnType.INT),
            Column(name = PRODUCT_CODE, type = ColumnType.INT),
            Column(name = SERIAL_CODE, type = ColumnType.INT),
            Column(name = DEVICE_TP_CODE, type = ColumnType.INT),
            Column(name = ITEM_CHECK_CODE, type = ColumnType.INT),
            Column(name = ITEM_CHECK_SEQ, type = ColumnType.INT),
            Column(
                name = ITEM_CHECK_ID,
                type = ColumnType.TEXT,
                collation = CollationType.NOCASE
            ),
            Column(
                name = ITEM_CHECK_DESC,
                type = ColumnType.TEXT,
                collation = CollationType.NOCASE
            ),
            Column(
                name = ITEM_CHECK_DESC_ALT_VG,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = ITEM_CHECK_GROUP_CODE,
                type = ColumnType.INT,
                isNullable = true
            ),
            Column(
                name = APPLY_MATERIAL,
                type = ColumnType.TEXT,
                collation = CollationType.NOCASE
            ),
            Column(
                name = VERIFICATION_INSTRUCTION,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = REQUIRE_JUSTIFY_PROBLEM,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            Column(name = CRITICAL_ITEM, type = ColumnType.INT, defaultValue = "0"),
            Column(name = CHANGE_ADJUST, type = ColumnType.INT, defaultValue = "0"),
            Column(name = ORDER_SEQ, type = ColumnType.INT),
            Column(name = STRUCTURE, type = ColumnType.INT),
            Column(
                name = ALREADY_OK_HIDE,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            Column(
                name = REQUIRE_PHOTO_FIXED,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            Column(
                name = REQUIRE_PHOTO_ALERT,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            Column(
                name = REQUIRE_PHOTO_ALREADY_OK,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            Column(
                name = REQUIRE_PHOTO_NOT_VERIFIED,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            Column(name = VG_CODE, type = ColumnType.INT, isNullable = true),
            Column(
                name = MANUAL_DESC,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = NEXT_CYCLE_MEASURE,
                type = ColumnType.REAL,
                isNullable = true
            ),
            Column(
                name = NEXT_CYCLE_MEASURE_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = NEXT_CYCLE_LIMIT_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = VALUE_SUFIX,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = RESTRICTION_DECIMAL,
                type = ColumnType.INT,
                isNullable = true
            ),
            Column(
                name = ITEM_CHECK_STATUS,
                type = ColumnType.TEXT,
                collation = CollationType.NOCASE
            ),
            Column(
                name = TARGET_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = EXEC_TYPE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = EXEC_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = EXEC_COMMENT,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = EXEC_PHOTO1,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = EXEC_PHOTO2,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = EXEC_PHOTO3,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = EXEC_PHOTO4,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = STATUS_ANSWER,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(name = HAS_EXPIRED_CYCLE, type = ColumnType.INT),
            Column(
                name = HIDE_DAYS_IN_ALERT,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            Column(
                name = PARTITIONED_EXECUTION,
                type = ColumnType.INT,
                defaultValue = "0"
            ),
            Column(name = TICKET_PREFIX, type = ColumnType.INT, isNullable = true),
            Column(name = TICKET_CODE, type = ColumnType.INT, isNullable = true),
            Column(name = VG_ACTION, type = ColumnType.INT, defaultValue = "0"),
            Column(name = IS_VISIBLE, type = ColumnType.INT, defaultValue = "0"),
            Column(
                name = COLOR_ITEM,
                type = ColumnType.TEXT,
                collation = CollationType.NOCASE
            ),
            Column(
                name = STATUS_MODIFICATION_TYPE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = AUTOMATIC_SELECTION_STATE,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = MEASURE_ACTIVE,
                type = ColumnType.INT,
                isNullable = true,
            ),
            Column(
                name = MEASURE_REQUIRE_ID,
                type = ColumnType.INT,
                isNullable = true,
            ),
            Column(
                name = MEASURE_UN,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = MEASURE_MIN,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            Column(
                name = MEASURE_MAX,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            Column(
                name = MEASURE_ALERT_MIN,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            Column(
                name = MEASURE_ALERT_MAX,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            Column(
                name = LAST_MEASURE_VALUE,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            Column(
                name = LAST_MEASURE_ID,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = LAST_MEASURE_UN,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = LAST_MEASURE_DATE,
                type = ColumnType.TEXT,
                isNullable = true,
            ),
            Column(
                name = LAST_MEASURE_ALERT,
                type = ColumnType.INT,
                isNullable = true,
            ),
            Column(
                name = MEASURE_START_VALUE,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            Column(
                name = MEASURE_END_VALUE,
                type = ColumnType.REAL,
                isNullable = true,
            ),
            Column(
                name = MEASURE_START_ID,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = MEASURE_END_ID,
                type = ColumnType.TEXT,
                isNullable = true,
                collation = CollationType.NOCASE
            ),
            Column(
                name = MdItemCheckDao.LABEL_FIXED,
                type = ColumnType.INT,
                isNullable = false,
                defaultValue = "1"
            ),
            Column(
                name = MdItemCheckDao.LABEL_ALREADY_OK,
                type = ColumnType.INT,
                isNullable = false,
                defaultValue = "2"
            ),
        ),
        primaryKey = listOf(
            CUSTOMER_CODE,
            CUSTOM_FORM_TYPE,
            CUSTOM_FORM_CODE,
            CUSTOM_FORM_VERSION,
            CUSTOM_FORM_DATA,
            PRODUCT_CODE,
            SERIAL_CODE,
            DEVICE_TP_CODE,
            ITEM_CHECK_CODE,
            ITEM_CHECK_SEQ
        )
    )
}

val GeOsDeviceItemScript = geOsDeviceItemTable.generateCreateTableScript()