package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class SqlAct092_003(
    private val context: Context,
    private val customerCode: Int,
    private var productCode: Int?,
    private var serialId: String?,
    val userFocus: Int
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private var statusFilter = ""

    init {
        setFiltersByOriginAndFocus()
    }

    private fun setFiltersByOriginAndFocus() {
        setSerialFilterConfg()
    }

    private fun setSerialFilterConfg() {
        getStatusFilter()
    }

    private fun getStatusFilter() {
        statusFilter = """   and    a.${GE_Custom_Form_ApDao.AP_STATUS} in ('${ConstantBaseApp.SYS_STATUS_EDIT}',
                                                                     '${ConstantBaseApp.SYS_STATUS_PENDING}',
                                                                     '${ConstantBaseApp.SYS_STATUS_PROCESS}',                                                                 
                                                                     '${ConstantBaseApp.SYS_STATUS_WAITING_ACTION}',
                                                                     '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                                                                     ) 
                                                                     """
    }

    override fun toSqlQuery(): String {
        val s = """ SELECT
                     a.*
                    FROM 
                     ${GE_Custom_Form_ApDao.TABLE} a
                    WHERE
                     a.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = $customerCode
                     $statusFilter                                         
                     and ($productCode is null or a.${GE_Custom_Form_ApDao.PRODUCT_CODE} = $productCode )
                     and ('$serialId' is null or a.${GE_Custom_Form_ApDao.SERIAL_ID} = '$serialId' ) 
              """.replace("'null'","null")
        return s
    }
}