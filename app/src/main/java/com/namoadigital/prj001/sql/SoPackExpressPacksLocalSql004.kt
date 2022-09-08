package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SoPackExpressPacksLocalDao
import com.namoadigital.prj001.database.Specification

class SoPackExpressPacksLocalSql004(
    private val customerCode: Long,
    private val site_code: Long,
    private val operation_code: Long,
    private val product_code: Long,
    private val express_code: String,
    private val express_tmp: Long
) : Specification {

    override fun toSqlQuery(): String {
        var s = """
          SELECT
            s.${SoPackExpressPacksLocalDao.CUSTOMER_CODE},
            s.${SoPackExpressPacksLocalDao.SITE_CODE},
            s.${SoPackExpressPacksLocalDao.OPERATION_CODE},
            s.${SoPackExpressPacksLocalDao.PRODUCT_CODE},
            s.${SoPackExpressPacksLocalDao.EXPRESS_CODE},
            s.${SoPackExpressPacksLocalDao.EXPRESS_TMP},
            s.${SoPackExpressPacksLocalDao.PACK_CODE},
            s.${SoPackExpressPacksLocalDao.PACK_SEQ},
            s.${SoPackExpressPacksLocalDao.PACK_SERVICE_DESC},
            s.${SoPackExpressPacksLocalDao.PACK_SERVICE_DESC_FULL},
            s.${SoPackExpressPacksLocalDao.PRICE_LIST_CODE},
            s.${SoPackExpressPacksLocalDao.MANUAL_PRICE},
            s.${SoPackExpressPacksLocalDao.PRICE},
            SUM(s.${SoPackExpressPacksLocalDao.QTY}) ${SoPackExpressPacksLocalDao.QTY},
            s.${SoPackExpressPacksLocalDao.TYPE_PS},
            s.${SoPackExpressPacksLocalDao.SERVICE_CODE},
            s.${SoPackExpressPacksLocalDao.COMMENTS}
          FROM
            ${SoPackExpressPacksLocalDao.TABLE} s
          WHERE
            ${SoPackExpressPacksLocalDao.CUSTOMER_CODE} = '$customerCode'
            AND ${SoPackExpressPacksLocalDao.SITE_CODE} = '$site_code'  
            AND ${SoPackExpressPacksLocalDao.OPERATION_CODE} = '$operation_code'
            AND ${SoPackExpressPacksLocalDao.PRODUCT_CODE} = '$product_code'
            AND ${SoPackExpressPacksLocalDao.EXPRESS_CODE} = '$express_code'
            AND ${SoPackExpressPacksLocalDao.EXPRESS_TMP} = '$express_tmp'
          GROUP BY ${SoPackExpressPacksLocalDao.PRICE_LIST_CODE}, ${SoPackExpressPacksLocalDao.PACK_CODE}, s.${SoPackExpressPacksLocalDao.SERVICE_CODE}
          ORDER BY rowid
            ;                                         
       """.trimIndent()
        //
        return s
    }
}