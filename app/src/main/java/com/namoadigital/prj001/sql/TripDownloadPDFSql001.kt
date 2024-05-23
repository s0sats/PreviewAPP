package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FsTripDestinationActionDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class TripDownloadPDFSql001(
    val customerCode: Long,
    val tripPrefix: Int,
    val tripCode: Int,
): Specification {
    override fun toSqlQuery(): String {
        val query = """
                SELECT action.${FsTripDestinationActionDao.CUSTOMER_CODE},
                       action.${FsTripDestinationActionDao.TRIP_PREFIX},
                       action.${FsTripDestinationActionDao.TRIP_CODE},
                       action.${FsTripDestinationActionDao.DESTINATION_SEQ},
                       action.${FsTripDestinationActionDao.ACTION_SEQ},
                       action.${FsTripDestinationActionDao.ACT_PDF_URL},
                       action.${FsTripDestinationActionDao.ACT_PDF_LOCAL},
                       '${ConstantBaseApp.N_FORM_PDF_PREFIX}'||action.${FsTripDestinationActionDao.ACT_PDF_NAME} $FILE_LOCAL_NAME  
                FROM ${FsTripDestinationActionDao.TABLE} action        
                WHERE action.${FsTripDestinationActionDao.CUSTOMER_CODE} = $customerCode
                  AND action.${FsTripDestinationActionDao.TRIP_PREFIX} = $tripPrefix  
                  AND action.${FsTripDestinationActionDao.TRIP_CODE} = $tripCode     
                  AND action.${FsTripDestinationActionDao.ACT_PDF_NAME} is not null    
                  AND (action.${FsTripDestinationActionDao.ACT_PDF_LOCAL} is null OR '${ConstantBaseApp.N_FORM_PDF_PREFIX}'||action.${FsTripDestinationActionDao.ACT_PDF_NAME} != action.${FsTripDestinationActionDao.ACT_PDF_LOCAL})  
                  AND action.${FsTripDestinationActionDao.ACT_PDF_URL} is not null    
      """.trimIndent()
        return query
    }

    companion object{
        const val FILE_LOCAL_NAME = "FILE_NAME"
    }
}