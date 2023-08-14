package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao.CUSTOMER_CODE
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao.SYS_DATE_END
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao.TABLE
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.Constant

class GE_Custom_Form_Data_Sql_Get_Last_Date(
    private val customer_code: Long
) : Specification {
    override fun toSqlQuery() =
        """SELECT MIN($SYS_DATE_END) $SYS_DATE_END
        FROM $TABLE
        WHERE $CUSTOM_FORM_STATUS = '${Constant.SYS_STATUS_WAITING_SYNC}' 
        AND $CUSTOMER_CODE = '$customer_code'
        """.trimIndent()

}