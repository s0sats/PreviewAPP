package com.namoadigital.prj001.dao.trip

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.DaoWithReturn
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.trip.FsTripActionDownload
import com.namoadigital.prj001.model.trip.FsTripDestinationAction
import com.namoadigital.prj001.sql.TripDownloadPDFSql001
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class FsTripDestinationActionDao(
    val context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI),
    DaoWithReturn<FsTripDestinationAction> {
    private val toFsTripDestinationAction: Mapper<Cursor, FsTripDestinationAction>
    private val toContentValuesMapper: Mapper<FsTripDestinationAction, ContentValues>

    init {
        toFsTripDestinationAction = CursorToFsTripDestinationAction()
        toContentValuesMapper = FsTripDestinationActionToContentValuesMapper()
    }

    override fun addUpdate(fsTripDestinationAction: FsTripDestinationAction?): DaoObjReturn {
        return addUpdate(fsTripDestinationAction, null)
    }

    override fun addUpdate(
        items: MutableList<FsTripDestinationAction>?,
        status: Boolean
    ): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            //
            db.beginTransaction()

            if (status) {
                db.delete(TABLE, null, null)
            }
            //
            items?.forEach { fsTripDestinationAction ->
                curAction = DaoObjReturn.UPDATE
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(fsTripDestinationAction)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(
                    TABLE,
                    toContentValuesMapper.map(fsTripDestinationAction),
                    sbWhere.toString(),
                    null
                ).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(
                        TABLE,
                        null,
                        toContentValuesMapper.map(fsTripDestinationAction)
                    )
                }
            }
            //
            db.setTransactionSuccessful()
        } catch (e: SQLiteException) {
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            //Gera arquivo de exception usando dados da exception e do obj de retorno
            ToolBox_Inf.registerException(
                javaClass.name,
                Exception(
                    """
                ${e.message}
                ${daoObjReturn.errorMsg}
                """.trimIndent()
                )
            )
        } catch (e: Exception) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            db.endTransaction()
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = addUpdateRet
        }
        //
        closeDB()
        //
        return daoObjReturn
    }

    override fun addUpdate(sQuery: String?) {
        openDB()
        try {
            db.execSQL(sQuery)
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        }
        closeDB()
    }

    override fun remove(sQuery: String?) {
        remove(sQuery, null)
    }

    fun remove(sQuery: String?, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        val curAction = DaoObjReturn.DELETE
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }

        if (dbInstance == null) {
            db.beginTransaction()
        }

        try {
            db.execSQL(sQuery)
            if (dbInstance == null) {
                db.setTransactionSuccessful()
            }
        } catch (e: java.lang.Exception) {
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            if (dbInstance == null) {
                db.endTransaction()
            }
            daoObjReturn.action = curAction
        }
        if (dbInstance == null) {
            closeDB()
        }
        return daoObjReturn
    }

    override fun getByString(sQuery: String?) = getByString(sQuery, null)

    fun getByString(sQuery: String?, dbInstance: SQLiteDatabase?): FsTripDestinationAction? {
        var fsTripDestinationAction: FsTripDestinationAction? = null
        if (dbInstance == null) {
            openDB()
        } else {
            this.db = dbInstance
        }
        lateinit var cursor: Cursor
        try {
            cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                fsTripDestinationAction = toFsTripDestinationAction.map(cursor)
            }
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            cursor.close()
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        //
        return fsTripDestinationAction
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var hmAux: HMAux? = null
        openDB()

        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                hmAux = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }

        closeDB()

        return hmAux
    }

    override fun query(sQuery: String?): MutableList<FsTripDestinationAction> {
        var items = mutableListOf<FsTripDestinationAction>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux: FsTripDestinationAction = toFsTripDestinationAction.map(cursor)
                items.add(uAux)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        //
        closeDB()
        return items
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val items: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                items.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return items
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(fsTripDestinationAction: FsTripDestinationAction?): StringBuilder {
        fsTripDestinationAction?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                            $CUSTOMER_CODE = '${it.customerCode}'  
                        AND $TRIP_PREFIX = '${it.tripPrefix}'
                        AND $TRIP_CODE = '${it.tripCode}'
                        AND $DESTINATION_SEQ = '${it.destinationSeq}'
                        AND $ACTION_SEQ = '${it.actionSeq}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    fun addUpdate(
        fsTripDestinationAction: FsTripDestinationAction?,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }
        //
        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(fsTripDestinationAction)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(
                TABLE,
                toContentValuesMapper.map(fsTripDestinationAction),
                sbWhere.toString(),
                null
            ).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(
                    TABLE,
                    null,
                    toContentValuesMapper.map(fsTripDestinationAction)
                )
            }
        } catch (e: SQLiteException) {
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            //Gera arquivo de exception usando dados da exception e do obj de retorno
            ToolBox_Inf.registerException(
                javaClass.name,
                Exception(
                    """
                ${e.message}
                ${daoObjReturn.errorMsg}
                """.trimIndent()
                )
            )
        } catch (e: Exception) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = addUpdateRet
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        //
        return daoObjReturn
    }

    fun getExtract(
        tripPrefix: Int,
        tripCode: Int
    ): List<FsTripDestinationAction> {
        val value = query(
            """
            SELECT * 
            FROM $TABLE
            WHERE $TRIP_PREFIX = '$tripPrefix'
            AND $TRIP_CODE = '$tripCode'
            AND $DATE_START IS NOT NULL
            AND $DATE_END IS NOT NULL
            ORDER BY $DATE_START ASC
        """.trimIndent()
        )

        return value.ifEmpty { emptyList() }
    }
    fun getAction(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
    ): FsTripDestinationAction? {
        return getByString(
            """
            SELECT * 
            FROM $TABLE
            WHERE $CUSTOMER_CODE = '$customerCode'
            AND $TRIP_PREFIX = '$tripPrefix'
            AND $TRIP_CODE = '$tripCode'
            AND $DESTINATION_SEQ = '$destinationSeq'
            AND $CUSTOM_FORM_TYPE = '$customFormType'
            AND $CUSTOM_FORM_CODE = '$customFormCode'
            AND $CUSTOM_FORM_VERSION = '$customFormVersion'
            AND $CUSTOM_FORM_DATA = '$customFormData'
        """.trimIndent()
        )
    }

    fun getPdfDownloadList(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int
    ): List<FsTripActionDownload> {
        val actions = query_HM(
            TripDownloadPDFSql001(
                customerCode,
                tripPrefix,
                tripCode
            ).toSqlQuery()
        )
        //
        val list = mutableListOf<FsTripActionDownload>()
        actions.forEach {
            list.add(
                FsTripActionDownload(
                    it[CUSTOMER_CODE]?.toLong(),
                    it[TRIP_PREFIX]?.toInt(),
                    it[TRIP_CODE]?.toInt(),
                    it[DESTINATION_SEQ]?.toInt(),
                    it[ACTION_SEQ]?.toInt(),
                    it[ACT_PDF_URL],
                    it[ACT_PDF_LOCAL],
                    it[TripDownloadPDFSql001.FILE_LOCAL_NAME]?:"",
                )
            )
        }
        return list
    }

    fun getListDestinationAction(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): List<FsTripDestinationAction> {
        return query(
            """
            SELECT d.*
              FROM $TABLE d 
             WHERE d.$CUSTOMER_CODE = $customerCode 
               AND d.$TRIP_PREFIX =  $tripPrefix
               AND d.$TRIP_CODE =  $tripCode
               AND d.$DESTINATION_SEQ =  '$destinationSeq'
        """
        )
    }


    class CursorToFsTripDestinationAction : Mapper<Cursor, FsTripDestinationAction> {
        @SuppressLint("Range")
        override fun map(from: Cursor?): FsTripDestinationAction? {
            from?.let { cursor ->
                with(cursor) {
                    return FsTripDestinationAction(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        tripPrefix = getInt(getColumnIndex(TRIP_PREFIX)),
                        tripCode = getInt(getColumnIndex(TRIP_CODE)),
                        destinationSeq = getInt(getColumnIndex(DESTINATION_SEQ)),
                        actionSeq = getInt(getColumnIndex(ACTION_SEQ)),
                        siteCode = getInt(getColumnIndex(SITE_CODE)),
                        siteDesc = getString(getColumnIndex(SITE_DESC)),
                        regionCode = getIntOrNull(getColumnIndex(REGION_CODE)),
                        regionDesc = getStringOrNull(getColumnIndex(REGION_DESC)),
                        actType = getString(getColumnIndex(ACT_TYPE)),
                        actDesc = getStringOrNull(getColumnIndex(ACT_DESC)),
                        productCode = getInt(getColumnIndex(PRODUCT_CODE)),
                        productDesc = getString(getColumnIndex(PRODUCT_DESC)),
                        serialCode = getInt(getColumnIndex(SERIAL_CODE)),
                        serialId = getString(getColumnIndex(SERIAL_ID)),
                        serialInf1 = getStringOrNull(getColumnIndex(SERIAL_INF1)),
                        brandDesc = getStringOrNull(getColumnIndex(BRAND_DESC)),
                        modelDesc = getStringOrNull(getColumnIndex(MODEL_DESC)),
                        dateStart = getString(getColumnIndex(DATE_START)),
                        dateEnd = getString(getColumnIndex(DATE_END)),
                        processType = getStringOrNull(getColumnIndex(PROCESS_TYPE)),
                        ticketPrefix = getIntOrNull(getColumnIndex(TICKET_PREFIX)),
                        ticketCode = getIntOrNull(getColumnIndex(TICKET_CODE)),
                        ticketId = getStringOrNull(getColumnIndex(TICKET_ID)),
                        kanbanData = getStringOrNull(getColumnIndex(KANBAN_DATA)),
                        actPDFLocal = getStringOrNull(getColumnIndex(ACT_PDF_LOCAL)),
                        actPDFName = getStringOrNull(getColumnIndex(ACT_PDF_NAME)),
                        actPDFUrl = getStringOrNull(getColumnIndex(ACT_PDF_URL)),
                        customFormType = getIntOrNull(getColumnIndex(CUSTOM_FORM_TYPE)),
                        customFormCode = getIntOrNull(getColumnIndex(CUSTOM_FORM_CODE)),
                        customFormVersion = getIntOrNull(getColumnIndex(CUSTOM_FORM_VERSION)),
                        customFormData = getIntOrNull(getColumnIndex(CUSTOM_FORM_DATA)),
                    )
                }
            }

            return null
        }
    }

    class FsTripDestinationActionToContentValuesMapper :
        Mapper<FsTripDestinationAction, ContentValues> {
        override fun map(from: FsTripDestinationAction?): ContentValues {
            val contentValues = ContentValues()

            from?.let { position ->
                with(contentValues) {
                    if (position.customerCode > -1) put(CUSTOMER_CODE, position.customerCode)
                    if (position.tripPrefix > -1) put(TRIP_PREFIX, position.tripPrefix)
                    if (position.tripCode > -1) put(TRIP_CODE, position.tripCode)
                    if (position.destinationSeq > -1) put(
                        DESTINATION_SEQ,
                        position.destinationSeq
                    )
                    if (position.actionSeq > -1) put(ACTION_SEQ, position.actionSeq)
                    if (position.siteCode > -1) put(SITE_CODE, position.siteCode)
                    if (position.siteDesc != null) put(SITE_DESC, position.siteDesc)
                    put(REGION_CODE, position.regionCode)
                    put(REGION_DESC, position.regionDesc)
                    if (position.actType != null) put(ACT_TYPE, position.actType)
                    put(ACT_DESC, position.actDesc)
                    put(ACT_PDF_LOCAL, position.actPDFLocal)
                    put(ACT_PDF_NAME, position.actPDFName)
                    put(ACT_PDF_URL, position.actPDFUrl)
                    if (position.productCode > -1) put(PRODUCT_CODE, position.productCode)
                    if (position.productDesc != null) put(PRODUCT_DESC, position.productDesc)
                    if (position.serialCode > -1) put(SERIAL_CODE, position.serialCode)
                    if (position.serialId != null) put(SERIAL_ID, position.serialId)
                    put(SERIAL_INF1, position.serialInf1)
                    put(BRAND_DESC, position.brandDesc)
                    put(MODEL_DESC, position.modelDesc)
                    if (position.dateStart != null) put(DATE_START, position.dateStart)
                    if (position.dateEnd != null) put(DATE_END, position.dateEnd)
                    put(PROCESS_TYPE, position.processType)
                    put(TICKET_PREFIX, position.ticketPrefix)
                    put(TICKET_CODE, position.ticketCode)
                    put(TICKET_ID, position.ticketId)
                    put(KANBAN_DATA, position.kanbanData)
                    put(CUSTOM_FORM_TYPE, position.customFormType)
                    put(CUSTOM_FORM_CODE, position.customFormCode)
                    put(CUSTOM_FORM_VERSION, position.customFormVersion)
                    put(CUSTOM_FORM_DATA, position.customFormData)
                }
            }

            return contentValues
        }

    }


    companion object {

        const val TABLE = "fs_trip_destination_action"
        const val CUSTOMER_CODE = "customer_code"
        const val TRIP_PREFIX = "trip_prefix"
        const val TRIP_CODE = "trip_code"
        const val DESTINATION_SEQ = "destination_seq"
        const val ACTION_SEQ = "action_seq"
        const val SITE_CODE = "site_code"
        const val SITE_DESC = "site_desc"
        const val REGION_CODE = "region_code"
        const val REGION_DESC = "region_desc"
        const val ACT_TYPE = "act_type"
        const val ACT_DESC = "act_desc"
        const val ACT_PDF_LOCAL = "act_pdf_local"
        const val ACT_PDF_NAME = "act_pdf_name"
        const val ACT_PDF_URL = "act_pdf_url"
        const val PRODUCT_CODE = "product_code"
        const val PRODUCT_DESC = "product_desc"
        const val SERIAL_CODE = "serial_code"
        const val SERIAL_ID = "serial_id"
        const val SERIAL_INF1 = "serial_inf1"
        const val BRAND_DESC = "brand_desc"
        const val MODEL_DESC = "model_desc"
        const val DATE_START = "date_start"
        const val DATE_END = "date_end"
        const val PROCESS_TYPE = "process_type"
        const val TICKET_PREFIX = "ticket_prefix"
        const val TICKET_CODE = "ticket_code"
        const val TICKET_ID = "ticket_id"
        const val KANBAN_DATA = "kanban_data"
        const val CUSTOM_FORM_TYPE = "custom_form_type"
        const val CUSTOM_FORM_CODE = "custom_form_code"
        const val CUSTOM_FORM_VERSION = "custom_form_version"
        const val CUSTOM_FORM_DATA = "custom_form_data"

        fun instance(context: Context) = FsTripDestinationActionDao(context)

    }
}