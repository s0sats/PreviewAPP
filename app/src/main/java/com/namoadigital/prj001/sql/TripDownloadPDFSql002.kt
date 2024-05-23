package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.trip.FsTripDestinationActionDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class TripDownloadPDFSql002(
    private val customerCode: Long,
    private val tripPrefix: Int,
    private val tripCode: Int,
    private val destinationSeq: Int,
    private val actionSeq: Int,
    private val actionPdfLocal: String,

    ):Specification {
    override fun toSqlQuery(): String {
        val query = """
                UPDATE ${FsTripDestinationActionDao.TABLE}
                   set ${FsTripDestinationActionDao.ACT_PDF_LOCAL} = '$actionPdfLocal'
                WHERE ${FsTripDestinationActionDao.CUSTOMER_CODE} = $customerCode
                  AND ${FsTripDestinationActionDao.TRIP_PREFIX} = $tripPrefix  
                  AND ${FsTripDestinationActionDao.TRIP_CODE} = $tripCode     
                  AND ${FsTripDestinationActionDao.DESTINATION_SEQ} = $destinationSeq     
                  AND ${FsTripDestinationActionDao.ACTION_SEQ} = $actionSeq     
      """.trimIndent()
        return query
    }
}