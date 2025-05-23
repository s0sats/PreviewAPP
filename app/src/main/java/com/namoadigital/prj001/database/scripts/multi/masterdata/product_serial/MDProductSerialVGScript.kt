package com.namoadigital.prj001.database.scripts.multi.masterdata.product_serial

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.DatabaseTable.Column
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.CUSTOMER_CODE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.LAST_EXECUTION_DATE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.LAST_EXECUTION_MEASURE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.MANUAL_DATE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.NEXT_CYCLE_LIMIT_DATE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.NEXT_CYCLE_MEASURE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.NEXT_CYCLE_MEASURE_DATE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.PARTITIONED_EXECUTION
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.PARTITIONED_TICKET_CODE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.PARTITIONED_TICKET_PREFIX
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.PARTITIONED_USER
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.PRODUCT_CODE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.SERIAL_CODE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.TABLE_NAME
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.TARGET_DATE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.VG_CODE
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao.Companion.VG_STATUS

val VGProductSerialScript = DatabaseTable(
    name = TABLE_NAME,
    columns = listOf(
        Column(CUSTOMER_CODE, ColumnType.INT, isNullable = false),
        Column(PRODUCT_CODE, ColumnType.INT, isNullable = false),
        Column(SERIAL_CODE, ColumnType.INT, isNullable = false),
        Column(VG_CODE, ColumnType.INT, isNullable = false),
        Column(NEXT_CYCLE_MEASURE, ColumnType.REAL, isNullable = true),
        Column(NEXT_CYCLE_MEASURE_DATE, ColumnType.TEXT, isNullable = true),
        Column(NEXT_CYCLE_LIMIT_DATE, ColumnType.TEXT, isNullable = true),
        Column(LAST_EXECUTION_MEASURE, ColumnType.REAL, isNullable = true),
        Column(LAST_EXECUTION_DATE, ColumnType.TEXT, isNullable = true),
        Column(
            VG_STATUS,
            ColumnType.TEXT,
            isNullable = true,
            collation = CollationType.NOCASE
        ),
        Column(TARGET_DATE, ColumnType.TEXT, isNullable = true),
        Column(MANUAL_DATE, ColumnType.TEXT, isNullable = true),
        Column(
            PARTITIONED_TICKET_PREFIX,
            ColumnType.INT,
            isNullable = true,
        ),
        Column(
            PARTITIONED_TICKET_CODE,
            ColumnType.INT,
            isNullable = true,
        ),
        Column(
            PARTITIONED_USER,
            ColumnType.TEXT,
            isNullable = true,
            collation = CollationType.NOCASE
        ),
        Column(
            PARTITIONED_EXECUTION,
            ColumnType.INT,
            isNullable = false,
            defaultValue = "0"
        ),
    ),
    primaryKey = listOf(
        CUSTOMER_CODE,
        PRODUCT_CODE,
        SERIAL_CODE,
        VG_CODE
    )
).generateCreateTableScript()