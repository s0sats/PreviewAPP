package com.namoadigital.prj001.database.scripts.multi.custom_form

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.DatabaseTable.Column
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.BUTTON_COMMENT
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.BUTTON_NC
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.BUTTON_PHOTO
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CONDITIONAL_NC
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao.CONDITIONAL_SEQ
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.AUTOMATIC
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.COMMENT
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOMER_CODE
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_CODE
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_MASK
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_SIZE
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_LOCAL_LINK
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_TYPE
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_VERSION
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.DEVICE_TP_CODE
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.PAGE
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.REQUIRED
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.REQUIRE_PHOTO_ON_NC
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao.TABLE

val geCustomFormFieldsLocalTable = DatabaseTable(
    name = TABLE,
    columns = listOf(
        Column(name = CUSTOMER_CODE, type = ColumnType.INT, isNullable = false),
        Column(name = CUSTOM_FORM_TYPE, type = ColumnType.INT, isNullable = false),
        Column(name = CUSTOM_FORM_CODE, type = ColumnType.INT, isNullable = false),
        Column(name = CUSTOM_FORM_VERSION, type = ColumnType.INT, isNullable = false),
        Column(name = CUSTOM_FORM_DATA, type = ColumnType.INT, isNullable = false),
        Column(name = CUSTOM_FORM_SEQ, type = ColumnType.INT, isNullable = false),
        Column(
            name = CUSTOM_FORM_DATA_TYPE,
            type = ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(name = CUSTOM_FORM_DATA_SIZE, type = ColumnType.INT, isNullable = true),
        Column(
            name = CUSTOM_FORM_DATA_MASK,
            type = ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            name = CUSTOM_FORM_DATA_CONTENT,
            type = ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            name = CUSTOM_FORM_LOCAL_LINK,
            type = ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(name = CUSTOM_FORM_ORDER, type = ColumnType.INT, isNullable = false),
        Column(name = PAGE, type = ColumnType.INT, isNullable = false),
        Column(name = REQUIRED, type = ColumnType.INT, isNullable = false),
        Column(name = DEVICE_TP_CODE, type = ColumnType.INT, isNullable = true),
        Column(
            name = AUTOMATIC,
            type = ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            name = COMMENT,
            type = ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            name = REQUIRE_PHOTO_ON_NC,
            type = ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            name = CUSTOM_FORM_FIELD_DESC,
            type = ColumnType.TEXT,
            isNullable = false,
            defaultValue = "''",
            collation = CollationType.NOCASE
        ),
        Column(
            name = BUTTON_NC,
            type = ColumnType.INT,
            isNullable = false,
            defaultValue = "1"
        ),
        Column(
            name = BUTTON_PHOTO,
            type = ColumnType.INT,
            isNullable = false,
            defaultValue = "1"
        ),
        Column(
            name = BUTTON_COMMENT,
            type = ColumnType.INT,
            isNullable = false,
            defaultValue = "1"
        ),
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
        CUSTOM_FORM_DATA,
        CUSTOM_FORM_SEQ
    )
).generateCreateTableScript()