package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.GeOsDeviceItem

class GeOsDeviceItem_Sql_006 (
    private val customerCode: String,
    private val customFormType: String,
    private val customFormCode: String,
    private val customFormVersion: String,
    private val customFormData: String,
    private val productCode: String,
    private val serialCode: String

) : Specification {
    constructor(customerCode: Long,
                customFormType: Int,
                customFormCode: Int,
                customFormVersion: Int,
                customFormData: Int,
                productCode: Long,
                serialCode: Long

    ):
            this(
                customerCode.toString(),
                customFormType.toString(),
                customFormCode.toString(),
                customFormVersion.toString(),
                customFormData.toString(),
                productCode.toString(),
                serialCode.toString()

            )
    override fun toSqlQuery(): String {
        val s = """
                    SELECT *
                    FROM ${GeOsDeviceItemDao.TABLE}
                    WHERE  ${GeOsDeviceItemDao.CUSTOMER_CODE} = '$customerCode'
                       AND ${GeOsDeviceItemDao.CUSTOM_FORM_TYPE} = '$customFormType'
                       AND ${GeOsDeviceItemDao.CUSTOM_FORM_CODE} = '$customFormCode'
                       AND ${GeOsDeviceItemDao.CUSTOM_FORM_VERSION} = '$customFormVersion'
                       AND ${GeOsDeviceItemDao.CUSTOM_FORM_DATA} = '$customFormData'                   
                       AND ${GeOsDeviceItemDao.PRODUCT_CODE} = '$productCode'                   
                       AND ${GeOsDeviceItemDao.SERIAL_CODE} = '$serialCode'                                         
                       AND ${GeOsDeviceItemDao.EXEC_TYPE} is null
                       AND ${GeOsDeviceItemDao.ITEM_CHECK_STATUS} != '${GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL}'                                 
                    """.trimIndent()
        return s
    }
}