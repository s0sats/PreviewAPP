package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification

/**
 * BARRIONUEVO 19-10-2021
 * Responde todos os itens planejados com nao verificado e adiciona a justificativa no campo comenta
 * rio.
 */
class GeOsDeviceItem_Sql_004(
    private val customerCode: String,
    private val customFormType: String,
    private val customFormCode: String,
    private val customFormVersion: String,
    private val customFormData: String,
    private val productCode: String,
    private val serialCode: String,
    private val execDate: String

) : Specification {
    constructor(customerCode: Long,
                customFormType: Int,
                customFormCode: Int,
                customFormVersion: Int,
                customFormData: Int,
                productCode: Long,
                serialCode: Long,
                execDate: String

    ):
            this(
                customerCode.toString(),
                customFormType.toString(),
                customFormCode.toString(),
                customFormVersion.toString(),
                customFormData.toString(),
                productCode.toString(),
                serialCode.toString(),
                execDate

            )
    override fun toSqlQuery(): String {
        val s = """
                     UPDATE ${GeOsDeviceItemDao.TABLE}
                       SET ${GeOsDeviceItemDao.EXEC_DATE} = '${execDate}'
                    WHERE
                       ${GeOsDeviceItemDao.CUSTOMER_CODE} = '$customerCode'
                       AND ${GeOsDeviceItemDao.CUSTOM_FORM_TYPE} = '$customFormType'
                       AND ${GeOsDeviceItemDao.CUSTOM_FORM_CODE} = '$customFormCode'
                       AND ${GeOsDeviceItemDao.CUSTOM_FORM_VERSION} = '$customFormVersion'
                       AND ${GeOsDeviceItemDao.CUSTOM_FORM_DATA} = '$customFormData'                   
                       AND ${GeOsDeviceItemDao.PRODUCT_CODE} = '$productCode'                   
                       AND ${GeOsDeviceItemDao.SERIAL_CODE} = '$serialCode'
                       AND (${GeOsDeviceItemDao.EXEC_TYPE} = '' OR ${GeOsDeviceItemDao.EXEC_TYPE} is null)
                                                            
                    """.trimIndent()
        return s
    }
}