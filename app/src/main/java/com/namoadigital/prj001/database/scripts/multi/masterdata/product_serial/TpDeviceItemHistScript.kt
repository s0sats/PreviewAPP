package com.namoadigital.prj001.database.scripts.multi.masterdata.product_serial

import com.namoadigital.prj001.core.database.CollationType
import com.namoadigital.prj001.core.database.ColumnType
import com.namoadigital.prj001.core.database.DatabaseTable
import com.namoadigital.prj001.core.database.DatabaseTable.Column
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.CHANGE_ADJUST
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.CUSTOMER_CODE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.DEVICE_TP_CODE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_COMMENT
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_DATE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_MATERIAL
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_PHOTO1
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_PHOTO2
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_PHOTO3
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_PHOTO4
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_TYPE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.EXEC_VALUE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.ITEM_CHECK_CODE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.ITEM_CHECK_SEQ
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.MEASURE_FIN_ALERT
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.MEASURE_FIN_ID
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.MEASURE_FIN_VALUE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.MEASURE_INI_ALERT
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.MEASURE_INI_ID
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.MEASURE_INI_VALUE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.MEASURE_UN
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.PRODUCT_CODE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.SEQ
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.SERIAL_CODE
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao.Companion.TABLE


val MDProductSerialTpDeviceItemHistScript = DatabaseTable(
    name = TABLE,
    columns = listOf(
        Column(CUSTOMER_CODE, ColumnType.INT, isNullable = false),
        Column(PRODUCT_CODE, ColumnType.INT, isNullable = false),
        Column(SERIAL_CODE, ColumnType.INT, isNullable = false),
        Column(DEVICE_TP_CODE, ColumnType.INT, isNullable = false),
        Column(ITEM_CHECK_CODE, ColumnType.INT, isNullable = false),
        Column(ITEM_CHECK_SEQ, ColumnType.INT, isNullable = false),
        Column(SEQ, ColumnType.INT, isNullable = false),
        Column(
            name = EXEC_TYPE,
            type = ColumnType.TEXT,
            isNullable = false,
            collation = CollationType.NOCASE
        ),
        Column(EXEC_VALUE, ColumnType.REAL, isNullable = false),
        Column(
            name = EXEC_DATE,
            type = ColumnType.TEXT,
            isNullable = false,
            collation = CollationType.NOCASE
        ),
        Column(
            name = EXEC_COMMENT,
            type = ColumnType.TEXT,
            isNullable = true,
            collation = CollationType.NOCASE
        ),
        Column(
            EXEC_MATERIAL,
            ColumnType.INT,
            isNullable = false
        ),
        Column(
            name = CHANGE_ADJUST,
            type = ColumnType.INT,
            isNullable = false,
            defaultValue = "0"
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
            name = MEASURE_UN,
            type = ColumnType.TEXT,
            isNullable = true,
            collation = CollationType.NOCASE
        ),
        Column(
            name = MEASURE_INI_VALUE,
            type = ColumnType.REAL,
            isNullable = true
        ),
        Column(
            name = MEASURE_INI_ID,
            type = ColumnType.TEXT,
            isNullable = true,
            collation = CollationType.NOCASE
        ),
        Column(
            name = MEASURE_INI_ALERT,
            type = ColumnType.INT,
            isNullable = true
        ),
        Column(
            name = MEASURE_FIN_VALUE,
            type = ColumnType.REAL,
            isNullable = true
        ),
        Column(
            name = MEASURE_FIN_ID,
            type = ColumnType.TEXT,
            isNullable = true,
            collation = CollationType.NOCASE
        ),
        Column(
            name = MEASURE_FIN_ALERT,
            type = ColumnType.INT,
            isNullable = true
        ),
    ),
    primaryKey = listOf(
        CUSTOMER_CODE,
        PRODUCT_CODE,
        SERIAL_CODE,
        DEVICE_TP_CODE,
        ITEM_CHECK_CODE,
        ITEM_CHECK_SEQ,
        SEQ
    )
).generateCreateTableScript()