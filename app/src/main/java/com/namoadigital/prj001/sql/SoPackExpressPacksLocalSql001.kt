package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SoPackExpressPacksLocalDao
import com.namoadigital.prj001.database.Specification

class SoPackExpressPacksLocalSql001(
    private val customerCode: Long,
    private val site_code: Long,
    private val operation_code: Long,
    private val product_code: Long,
    private val express_code: String,
    private val express_tmp: Long,
    private val pack_code: Int,
    private val pack_seq: Int
    ) : Specification {

    override fun toSqlQuery(): String {
        var s = """
          SELECT
            s.*
          FROM
            ${SoPackExpressPacksLocalDao.TABLE} s
          WHERE
            ${SoPackExpressPacksLocalDao.CUSTOMER_CODE} = '$customerCode'
            AND ${SoPackExpressPacksLocalDao.SITE_CODE} = '$site_code'  
            AND ${SoPackExpressPacksLocalDao.OPERATION_CODE} = '$operation_code'
            AND ${SoPackExpressPacksLocalDao.PRODUCT_CODE} = '$product_code'
            AND ${SoPackExpressPacksLocalDao.EXPRESS_CODE} = '$express_code'
            AND ${SoPackExpressPacksLocalDao.EXPRESS_TMP} = '$express_tmp'
            AND ${SoPackExpressPacksLocalDao.PACK_CODE} = '$pack_code'
            AND ${SoPackExpressPacksLocalDao.PACK_SEQ} = '$pack_seq' ;                                         
       """.trimIndent()
        //
        return s
    }
}