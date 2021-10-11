package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.Constant

/**
 * Created by DANIEL.LUCHE on 08/10/2021
 * Query que seleciona os device item da os relacionada ao form
 * LUCHE - 11/10/2021
 * O requisito escrito a tinta invisivel dizia que os itens não planejados e sem reposta não devem
 * ser enviados. Add clausula ${GeOsDeviceItemDao.EXEC_TYPE} IS NOT NULL
 */
class Sql_WS_Save_Device_Item_001(
    private val s_customer_code: Long
    ) : Specification {

    override fun toSqlQuery(): String {
        var sb = """
                    SELECT
                        i.*
                    FROM
                        ${GeOsDeviceItemDao.TABLE} i,
                        ${GE_Custom_Form_DataDao.TABLE} d
                    WHERE
                        d.${GE_Custom_Form_DataDao.CUSTOMER_CODE} = i.${GeOsDeviceItemDao.CUSTOMER_CODE}
                        AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE} = i.${GeOsDeviceItemDao.CUSTOM_FORM_TYPE}
                        AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE} = i.${GeOsDeviceItemDao.CUSTOM_FORM_CODE}
                        AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION} = i.${GeOsDeviceItemDao.CUSTOM_FORM_VERSION}
                        AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA} = i.${GeOsDeviceItemDao.CUSTOM_FORM_DATA}
                        
                        AND d.${GE_Custom_Form_DataDao.CUSTOMER_CODE} = '$s_customer_code'
                        AND d.${GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS} = '${Constant.SYS_STATUS_WAITING_SYNC}' 
                        AND d.${GE_Custom_Form_DataDao.TOKEN} != ''
                        AND i.${GeOsDeviceItemDao.EXEC_TYPE} IS NOT NULL
                    ORDER BY
                        i.${GeOsDeviceItemDao.CUSTOMER_CODE},
                        i.${GeOsDeviceItemDao.CUSTOM_FORM_TYPE},
                        i.${GeOsDeviceItemDao.CUSTOM_FORM_CODE},
                        i.${GeOsDeviceItemDao.CUSTOM_FORM_VERSION},
                        i.${GeOsDeviceItemDao.CUSTOM_FORM_DATA}
                        """
        return sb
    }
}