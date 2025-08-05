package com.namoadigital.prj001.database.scripts.multi.masterdata

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.DatabaseTable.Column
import com.namoadigital.prj001.dao.md.MDVerificationGroupDao
import com.namoadigital.prj001.dao.md.MDVerificationGroupDao.Companion.CUSTOMER_CODE
import com.namoadigital.prj001.dao.md.MDVerificationGroupDao.Companion.EXEC_ONLY_PREVENTIVE
import com.namoadigital.prj001.dao.md.MDVerificationGroupDao.Companion.VG_CODE
import com.namoadigital.prj001.dao.md.MDVerificationGroupDao.Companion.VG_DESC
import com.namoadigital.prj001.dao.md.MDVerificationGroupDao.Companion.VG_ID


val mdVerificationGroupDatabaseTable =
    DatabaseTable(
        name = MDVerificationGroupDao.TABLE,
        columns = listOf(
            Column(CUSTOMER_CODE, ColumnType.INT, isNullable = false),
            Column(VG_CODE, ColumnType.INT, isNullable = false),
            Column(VG_ID, ColumnType.INT, isNullable = false),
            Column(
                VG_DESC,
                ColumnType.TEXT,
                isNullable = false,
                collation = CollationType.NOCASE
            ),
            Column(
                EXEC_ONLY_PREVENTIVE,
                ColumnType.INT,
                isNullable = false,
                defaultValue = "0"
            ),
        ),
        primaryKey = listOf(
                        CUSTOMER_CODE,
                        VG_CODE,
                    )
    ).generateCreateTableScript()
