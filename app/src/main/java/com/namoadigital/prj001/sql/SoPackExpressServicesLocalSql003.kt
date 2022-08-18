package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SoPackExpressServicesLocalDao
import com.namoadigital.prj001.database.Specification

class SoPackExpressServicesLocalSql003(
    private val customerCode: Long,
    private val site_code: Long,
    private val operation_code: Long,
    private val product_code: Long,
    private val express_code: String,
    private val express_tmp: Long,
    private val price_list_code: Int,
    private val pack_code: Int,
    private val pack_seq: Int,
    private val type_ps: String,
    private val service_seq: Int
) : Specification {

    override fun toSqlQuery(): String {
        var s = """
          SELECT
           IFNULL(max(express_tmp),0) + 1 ${SoPackExpressPacksLocalSql003.NEXT_TMP}
          FROM
            ${SoPackExpressServicesLocalDao.TABLE} s
          WHERE
            ${SoPackExpressServicesLocalDao.CUSTOMER_CODE} = '$customerCode'
            AND ${SoPackExpressServicesLocalDao.SITE_CODE} = '$site_code'  
            AND ${SoPackExpressServicesLocalDao.OPERATION_CODE} = '$operation_code'
            AND ${SoPackExpressServicesLocalDao.PRODUCT_CODE} = '$product_code'
            AND ${SoPackExpressServicesLocalDao.EXPRESS_CODE} = '$express_code'
            AND ${SoPackExpressServicesLocalDao.EXPRESS_TMP} = '$express_tmp'
            AND ${SoPackExpressServicesLocalDao.PRICE_LIST_CODE} = '$price_list_code'
            AND ${SoPackExpressServicesLocalDao.PACK_CODE} = '$pack_code'
            AND ${SoPackExpressServicesLocalDao.PACK_SEQ} = '$pack_seq'                                        
            AND ${SoPackExpressServicesLocalDao.TYPE_PS} = '$type_ps'                                        
            AND ${SoPackExpressServicesLocalDao.SERVICE_SEQ} = '$service_seq';                                        
       """.trimIndent()
        //
        return s
    }
    companion object{
        val NEXT_TMP = "next_tmp"
    }
}