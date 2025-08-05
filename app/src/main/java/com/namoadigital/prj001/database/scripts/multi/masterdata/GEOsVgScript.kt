package com.namoadigital.prj001.database.scripts.multi.masterdata

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.DatabaseTable.Column
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.CUSTOMER_CODE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.CUSTOM_FORM_CODE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.CUSTOM_FORM_DATA
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.CUSTOM_FORM_TYPE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.CUSTOM_FORM_VERSION
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.EXEC_ONLY_PREVENTIVE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.HAS_EXPIRED
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.IS_ACTIVE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.MANUAL_DATE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.NEXT_CYCLE_LIMIT_DATE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.NEXT_CYCLE_MEASURE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.NEXT_CYCLE_MEASURE_DATE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.PARTITIONED_EXECUTION
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.PARTITIONED_TICKET_CODE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.PARTITIONED_TICKET_PREFIX
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.PARTITIONED_USER
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.PRODUCT_CODE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.RESTRICTION_DECIMAL
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.SERIAL_CODE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.TABLE_NAME
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.TARGET_DATE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.VALUE_SUFFIX
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.VG_CODE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.VG_DESC
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.VG_ID
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.VG_STATUS
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.LAST_EXECUTION_DATE
import com.namoadigital.prj001.dao.GeOsVgDao.Companion.LAST_EXECUTION_MEASURE


val GEOsVgScript = DatabaseTable(
    name = TABLE_NAME,
    columns = listOf(
        Column(CUSTOMER_CODE, ColumnType.REAL, isNullable = false),

        Column(CUSTOM_FORM_TYPE, ColumnType.INT, isNullable = false),
        Column(CUSTOM_FORM_CODE, ColumnType.INT, isNullable = false),
        Column(CUSTOM_FORM_VERSION, ColumnType.INT, isNullable = false),
        Column(CUSTOM_FORM_DATA, ColumnType.INT, isNullable = false),

        Column(PRODUCT_CODE, ColumnType.INT, isNullable = false),
        Column(SERIAL_CODE, ColumnType.INT, isNullable = false),
        Column(VG_CODE, ColumnType.INT, isNullable = false),

        Column(VG_ID, ColumnType.TEXT, isNullable = false, defaultValue = "''", collation = CollationType.NOCASE),
        Column(VG_DESC, ColumnType.TEXT, isNullable = false, defaultValue = "''", collation = CollationType.NOCASE),
        Column(VG_STATUS, ColumnType.TEXT, isNullable = false, defaultValue = "''", collation = CollationType.NOCASE),

        Column(VALUE_SUFFIX, ColumnType.TEXT, isNullable = true),
        Column(RESTRICTION_DECIMAL, ColumnType.TEXT, isNullable = true),

        Column(NEXT_CYCLE_MEASURE, ColumnType.REAL, isNullable = true),
        Column(NEXT_CYCLE_MEASURE_DATE, ColumnType.TEXT, isNullable = true),
        Column(NEXT_CYCLE_LIMIT_DATE, ColumnType.TEXT, isNullable = true),
        Column(LAST_EXECUTION_MEASURE, ColumnType.REAL, isNullable = true),
        Column(LAST_EXECUTION_DATE, ColumnType.TEXT, isNullable = true),
        Column(TARGET_DATE, ColumnType.TEXT, isNullable = true),
        Column(MANUAL_DATE, ColumnType.TEXT, isNullable = true),

        Column(PARTITIONED_TICKET_PREFIX, ColumnType.INT, isNullable = true),
        Column(PARTITIONED_TICKET_CODE, ColumnType.INT, isNullable = true),
        Column(PARTITIONED_USER, ColumnType.TEXT, isNullable = true, collation = CollationType.NOCASE),
        Column(PARTITIONED_EXECUTION, ColumnType.INT, isNullable = false, defaultValue = "0"),

        Column(IS_ACTIVE, ColumnType.INT, isNullable = false, defaultValue = "0"),
        Column(HAS_EXPIRED, ColumnType.INT, isNullable = false, defaultValue = "0"),
        Column(EXEC_ONLY_PREVENTIVE, ColumnType.INT, isNullable = false, defaultValue = "0"),
    ),
    primaryKey = listOf(
        CUSTOMER_CODE,
        CUSTOM_FORM_TYPE,
        CUSTOM_FORM_CODE,
        CUSTOM_FORM_VERSION,
        CUSTOM_FORM_DATA,
        PRODUCT_CODE,
        SERIAL_CODE,
        VG_CODE
    )
).generateCreateTableScript()
