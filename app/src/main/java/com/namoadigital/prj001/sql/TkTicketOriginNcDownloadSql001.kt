package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TkTicketOriginNcDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

/**
 * BARRIONUEVO 05-08-2021
 * Separa as imagens em registros para controle de servico de download.
 * 1 a 4 - Imagens do Dots
 * 5 - Imagem do PhotoFF
 * 6 - Imagem do PictureFF
 */
class TkTicketOriginNcDownloadSql001 (
    private val customer_code: Long
) : Specification {

    override fun toSqlQuery(): String {
        val query = """
            SELECT 
            customer_code,
            ticket_prefix,
            ticket_code,
            page,
            custom_form_order,
            ${TICKET_ORIGIN_FILE_NAME_ID},
            ${FILE_NAME_LOCAL},
            ${FILE_NAME_URL}
            FROM(       SELECT 
                              o.*,
                              1 ${TICKET_ORIGIN_FILE_NAME_ID},
                              '${ConstantBaseApp.TK_TICKET_NC_PREX_IMG}_' || o.customer_code || '_' || o.ticket_prefix || '_' || o.ticket_code || '_' || o.page || '_' || o.custom_form_order || '_1' ${FILE_NAME_LOCAL},
                              o.${TkTicketOriginNcDao.DATA_PHOTO1_URL}  ${FILE_NAME_URL}
                         FROM ${TkTicketOriginNcDao.TABLE} o
                        WHERE o.${TkTicketOriginNcDao.CUSTOMER_CODE} = $customer_code
                          AND o.${TkTicketOriginNcDao.DATA_PHOTO1_URL} not null
                          AND o.${TkTicketOriginNcDao.DATA_PHOTO1_URL_LOCAL} is null
                   UNION
                        SELECT 
                              o.*,
                              2 ${TICKET_ORIGIN_FILE_NAME_ID},
                              '${ConstantBaseApp.TK_TICKET_NC_PREX_IMG}_' || o.customer_code || '_' || o.ticket_prefix || '_' || o.ticket_code || '_' || o.page || '_' || o.custom_form_order || '_2' ${FILE_NAME_LOCAL},
                              o.${TkTicketOriginNcDao.DATA_PHOTO2_URL}  ${FILE_NAME_URL}
                         FROM ${TkTicketOriginNcDao.TABLE} o
                        WHERE o.${TkTicketOriginNcDao.CUSTOMER_CODE} = $customer_code
                          AND o.${TkTicketOriginNcDao.DATA_PHOTO2_URL} not null
                          AND o.${TkTicketOriginNcDao.DATA_PHOTO2_URL_LOCAL} is null
                   UNION        
                        SELECT 
                              o.*,
                              3 ${TICKET_ORIGIN_FILE_NAME_ID},
                              '${ConstantBaseApp.TK_TICKET_NC_PREX_IMG}_' || o.customer_code || '_' || o.ticket_prefix || '_' || o.ticket_code || '_' || o.page || '_' || o.custom_form_order || '_3' ${FILE_NAME_LOCAL},
                              o.${TkTicketOriginNcDao.DATA_PHOTO3_URL}  ${FILE_NAME_URL}
                         FROM ${TkTicketOriginNcDao.TABLE} o
                        WHERE o.${TkTicketOriginNcDao.CUSTOMER_CODE} = $customer_code
                          AND o.${TkTicketOriginNcDao.DATA_PHOTO3_URL} not null
                          AND o.${TkTicketOriginNcDao.DATA_PHOTO3_URL_LOCAL} is null
                   UNION
                        SELECT 
                              o.*,
                              4 ${TICKET_ORIGIN_FILE_NAME_ID},
                              '${ConstantBaseApp.TK_TICKET_NC_PREX_IMG}_' || o.customer_code || '_' || o.ticket_prefix || '_' || o.ticket_code || '_' || o.page || '_' || o.custom_form_order || '_4' ${FILE_NAME_LOCAL},
                              o.${TkTicketOriginNcDao.DATA_PHOTO4_URL}  ${FILE_NAME_URL}
                         FROM ${TkTicketOriginNcDao.TABLE} o
                        WHERE o.${TkTicketOriginNcDao.CUSTOMER_CODE} = $customer_code
                          AND o.${TkTicketOriginNcDao.DATA_PHOTO4_URL} not null
                          AND o.${TkTicketOriginNcDao.DATA_PHOTO4_URL_LOCAL} is null   
                   UNION                 
                        SELECT 
                              o.*,
                              5 ${TICKET_ORIGIN_FILE_NAME_ID},
                              '${ConstantBaseApp.TK_TICKET_NC_PREX_IMG}_' || o.customer_code || '_' || o.ticket_prefix || '_' || o.ticket_code || '_' || o.page || '_' || o.custom_form_order || '_5' ${FILE_NAME_LOCAL},
                              o.${TkTicketOriginNcDao.DATA_VALUE_TXT} ${FILE_NAME_URL}
                         FROM ${TkTicketOriginNcDao.TABLE} o
                        WHERE o.${TkTicketOriginNcDao.CUSTOMER_CODE} = $customer_code
                          AND o.${TkTicketOriginNcDao.CUSTOM_FORM_DATA_TYPE} = 'PHOTO'                               
                          AND o.${TkTicketOriginNcDao.DATA_VALUE_TXT} not null
                          AND o.${TkTicketOriginNcDao.DATA_VALUE_LOCAL} is null
                   UNION         
                        SELECT 
                              o.*,
                              6 ${TICKET_ORIGIN_FILE_NAME_ID},
                              '${ConstantBaseApp.TK_TICKET_NC_PREX_IMG}_' || o.customer_code || '_' || o.ticket_prefix || '_' || o.ticket_code || '_' || o.page || '_' || o.custom_form_order || '_6' ${FILE_NAME_LOCAL},
                              o.${TkTicketOriginNcDao.PICTURE_URL} ${FILE_NAME_URL}
                         FROM ${TkTicketOriginNcDao.TABLE} o
                        WHERE o.${TkTicketOriginNcDao.CUSTOMER_CODE} = $customer_code
                          AND o.${TkTicketOriginNcDao.CUSTOM_FORM_DATA_TYPE} = 'PICTURE'                               
                          AND o.${TkTicketOriginNcDao.PICTURE_URL} not null
                          AND o.${TkTicketOriginNcDao.PICTURE_URL_LOCAL} is null
            )
      """.trimIndent()
        return query
    }
    companion object{
        @kotlin.jvm.JvmField
        var FILE_NAME_URL = "FILE_NAME_URL"
        @kotlin.jvm.JvmField
        var FILE_NAME_LOCAL = "FILE_NAME_LOCAL"
        @kotlin.jvm.JvmField
        var TICKET_ORIGIN_FILE_NAME_ID = "TICKET_ORIGIN_FILE_NAME_ID"
    }
}