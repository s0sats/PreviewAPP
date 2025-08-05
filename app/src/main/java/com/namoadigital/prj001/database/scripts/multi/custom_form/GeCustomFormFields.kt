package com.namoadigital.prj001.database.scripts.multi.custom_form

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.DatabaseTable.Column
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.AUTOMATIC
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.BUTTON_COMMENT
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.BUTTON_NC
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.BUTTON_PHOTO
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.COMMENT
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CONDITIONAL_NC
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CONDITIONAL_SEQ
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOMER_CODE
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_CODE
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_CONTENT
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_MASK
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_SIZE
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_DATA_TYPE
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_LOCAL_LINK
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_ORDER
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_SEQ
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_TYPE
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CUSTOM_FORM_VERSION
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.DEVICE_TP_CODE
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.PAGE
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.REQUIRED
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.REQUIRE_PHOTO_ON_NC
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.TABLE

val geCustomFormFieldsDatabaseTable = DatabaseTable(
    name = TABLE, columns = listOf(
        Column(CUSTOMER_CODE, ColumnType.INT, isNullable = false),
        Column(CUSTOM_FORM_TYPE, ColumnType.INT, isNullable = false),
        Column(CUSTOM_FORM_CODE, ColumnType.INT, isNullable = false),
        Column(CUSTOM_FORM_VERSION, ColumnType.INT, isNullable = false),
        Column(CUSTOM_FORM_SEQ, ColumnType.INT, isNullable = false),
        Column(
            CUSTOM_FORM_DATA_TYPE,
            ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(CUSTOM_FORM_DATA_SIZE, ColumnType.INT, isNullable = true),
        Column(
            CUSTOM_FORM_DATA_MASK,
            ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            CUSTOM_FORM_DATA_CONTENT,
            ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            CUSTOM_FORM_LOCAL_LINK,
            ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(CUSTOM_FORM_ORDER, ColumnType.INT, isNullable = false),
        Column(PAGE, ColumnType.INT, isNullable = false),
        Column(REQUIRED, ColumnType.INT, isNullable = false),
        Column(DEVICE_TP_CODE, ColumnType.INT, isNullable = true),
        Column(
            AUTOMATIC,
            ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            COMMENT,
            ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            REQUIRE_PHOTO_ON_NC,
            ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        //
        Column(BUTTON_NC, ColumnType.INT, isNullable = false, defaultValue = "1"),
        Column(BUTTON_PHOTO, ColumnType.INT, isNullable = false, defaultValue = "1"),
        Column(BUTTON_COMMENT, ColumnType.INT, isNullable = false, defaultValue = "1"),
        Column(
            name = CONDITIONAL_SEQ,
            type = ColumnType.INT,
            isNullable = true,
        ),
        Column(
            name = CONDITIONAL_NC,
            type = ColumnType.INT,
            isNullable = true,
        ),
    ),
    primaryKey = listOf(
        CUSTOMER_CODE,
        CUSTOM_FORM_TYPE,
        CUSTOM_FORM_CODE,
        CUSTOM_FORM_VERSION,
        CUSTOM_FORM_SEQ
    )
).generateCreateTableScript()