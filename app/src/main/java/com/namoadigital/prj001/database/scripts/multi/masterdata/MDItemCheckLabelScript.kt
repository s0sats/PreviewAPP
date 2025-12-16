package com.namoadigital.prj001.database.scripts.multi.masterdata

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.dao.md.MDItemCheckLabelDao


val MDItemCheckLabelTable = DatabaseTable(
    name = MDItemCheckLabelDao.TABLE_NAME,
    primaryKey = listOf(
        MDItemCheckLabelDao.CUSTOMER_CODE,
        MDItemCheckLabelDao.LABEL_CODE,
    ),
    columns = listOf(
        DatabaseTable.Column(
            name = MDItemCheckLabelDao.CUSTOMER_CODE,
            type = ColumnType.INT
        ),
        DatabaseTable.Column(
            name = MDItemCheckLabelDao.LABEL_CODE,
            type = ColumnType.INT
        ),
        DatabaseTable.Column(
            name = MDItemCheckLabelDao.LABEL_TYPE,
            type = ColumnType.TEXT,
        ),
        DatabaseTable.Column(
            name = MDItemCheckLabelDao.LABEL_ID,
            type = ColumnType.TEXT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = MDItemCheckLabelDao.LABEL_DESC,
            type = ColumnType.TEXT
        ),
        DatabaseTable.Column(
            name = MDItemCheckLabelDao.LABEL_ICON_ID,
            type = ColumnType.TEXT,
            collation = CollationType.NOCASE
        ),
        DatabaseTable.Column(
            name = MDItemCheckLabelDao.ACTIVE,
            type = ColumnType.INT,
            defaultValue = "0"
        ),
    )
)