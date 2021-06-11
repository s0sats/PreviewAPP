package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class SqlAct084_003(
        private val customerCode: Long,
        private val doneTab: Int
) : Specification {
    private var statusFilter = getStatusFilter()

    private fun getStatusFilter(): String {
        return  when (doneTab) {
            1 ->    """    and a.${GE_Custom_Form_ApDao.AP_STATUS} = '${ConstantBaseApp.SYS_STATUS_DONE}'"""
            else -> """    and a.${GE_Custom_Form_ApDao.AP_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_ERROR}','${ConstantBaseApp.SYS_STATUS_CANCELLED}') """
        }
    }

    override fun toSqlQuery(): String {
        var s = """ SELECT
                     a.*
                    FROM 
                     ${GE_Custom_Form_ApDao.TABLE} a
                    WHERE
                     a.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = $customerCode
                     $statusFilter                                        
              """.replace("'null'","null")
        return s
    }
}