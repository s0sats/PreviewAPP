package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.database.Specification

/**
 * Criado query que selecionara os seriais que possuem estrutura
 */

class Sql_WS_Sync_Datapackage_Serial_001(
         private val customerCode: Long
        ):Specification {
    override fun toSqlQuery(): String {
        var s = """ SELECT
                     s.${MD_Product_SerialDao.CUSTOMER_CODE},
                     s.${MD_Product_SerialDao.PRODUCT_CODE},
                     s.${MD_Product_SerialDao.SERIAL_CODE},
                     CASE WHEN s.${MD_Product_SerialDao.SYNC_BIG_FILE} = 1 then 0 else s.${MD_Product_SerialDao.SCN_ITEM_CHECK} end ${MD_Product_SerialDao.SCN_ITEM_CHECK}
                    FROM
                     ${MD_Product_SerialDao.TABLE} s
                    WHERE                    
                     s.${MD_Product_SerialDao.CUSTOMER_CODE} = $customerCode
                     AND s.${MD_Product_SerialDao.HAS_ITEM_CHECK} = 1                                               
                """
        return s.trimIndent()
    }
}