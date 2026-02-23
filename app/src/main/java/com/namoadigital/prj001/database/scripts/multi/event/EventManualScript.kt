package com.namoadigital.prj001.database.scripts.multi.event

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.dao.event.EventManualDao

val eventManualTable = DatabaseTable(
    name = EventManualDao.TABLE_NAME,
    primaryKey = listOf(
        EventManualDao.EVENT_USER,
        EventManualDao.EVENT_DAY,
        EventManualDao.EVENT_DAY_SEQ,
    ),
    columns = listOf(
        DatabaseTable.Column(
            name = EventManualDao.EVENT_USER,
            type = ColumnType.INT
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_DAY,
            type = ColumnType.INT
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_DAY_SEQ,
            type = ColumnType.INT,
        ),
        DatabaseTable.Column(
            name = EventManualDao.APP_ID,
            type = ColumnType.TEXT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_TYPE_CODE,
            type = ColumnType.INT
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_DESCRIPTION,
            type = ColumnType.TEXT,
            collation = CollationType.NOCASE
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_COST,
            type = ColumnType.TEXT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_COMMENTS,
            type = ColumnType.TEXT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.PHOTO_URL,
            type = ColumnType.TEXT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.PHOTO_NAME,
            type = ColumnType.TEXT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.PHOTO_LOCAL,
            type = ColumnType.TEXT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_START,
            type = ColumnType.TEXT
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_END,
            type = ColumnType.TEXT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.CHANGED_PHOTO,
            type = ColumnType.INT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_STATUS,
            type = ColumnType.TEXT
        ),
        DatabaseTable.Column(
            name = EventManualDao.UPDATE_REQUIRED,
            type = ColumnType.INT
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_SITE_CODE,
            type = ColumnType.INT,
            isNullable = true
        ),
        DatabaseTable.Column(
            name = EventManualDao.EVENT_SITE_DESC,
            type = ColumnType.TEXT,
            isNullable = true,
        ),
    )
)