package com.namoadigital.prj001.database.scripts.multi.masterdata

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.dao.md.MDItemCheckLabelIconDao

val MDItemCheckLabelIconTable = DatabaseTable(
    name = MDItemCheckLabelIconDao.TABLE_NAME,
    primaryKey = listOf(
        MDItemCheckLabelIconDao.LABEL_ICON_ID,
    ),
    columns = listOf(
        DatabaseTable.Column(
            name = MDItemCheckLabelIconDao.LABEL_ICON_ID,
            type = ColumnType.TEXT,
            collation = CollationType.NOCASE
        ),
        DatabaseTable.Column(
            name = MDItemCheckLabelIconDao.LABEL_ICON,
            type = ColumnType.TEXT,
            collation = CollationType.NOCASE
        ),
        DatabaseTable.Column(
            name = MDItemCheckLabelIconDao.LABEL_ICON_DESC,
            type = ColumnType.TEXT,
            collation = CollationType.NOCASE
        ),
    )
)