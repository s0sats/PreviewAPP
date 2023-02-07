package com.namoadigital.prj001.sql

import com.namoa_digital.namoa_library.ctls.SearchableSpinner
import com.namoadigital.prj001.dao.MdJustifyItemDao
import com.namoadigital.prj001.database.Specification

class MdJustifyItemSqlSS (
private val customerCode: Long,
private val justifyGroupCode: Int
) : Specification {
    override fun toSqlQuery(): String {
        var query = """SELECT
                       justify_item_code ${SearchableSpinner.CODE},
                       justify_item_id ${SearchableSpinner.ID}, 
                       justify_item_desc ${SearchableSpinner.DESCRIPTION},
                       required_comment 
                       FROM
                            ${MdJustifyItemDao.TABLE}                   
                       WHERE
                             ${MdJustifyItemDao.CUSTOMER_CODE} = '$customerCode'  
                            AND ${MdJustifyItemDao.JUSTIFY_GROUP_CODE} = '$justifyGroupCode'
                       ORDER BY justify_item_desc;                                
                    """.trimMargin()
        return query
    }
}