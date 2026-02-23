package com.namoadigital.prj001.dao.trip

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.DaoWithReturn
import com.namoadigital.prj001.dao.trip.FSEventTypeDao.Companion.WAIT_ALLOWED
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.helper.EventValidation
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflictType
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class FSTripEventDao(
    val context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI), DaoWithReturn<FSTripEvent> {
    private val toFSTripEvent: Mapper<Cursor, FSTripEvent>
    private val toContentValuesMapper: Mapper<FSTripEvent, ContentValues>

    init {
        toFSTripEvent = CursorToFSTripEvent()
        toContentValuesMapper = FSTripEventToContentValuesMapper()
    }

    override fun addUpdate(fsEvent: FSTripEvent?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(fsEvent)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(
                TABLE,
                toContentValuesMapper.map(fsEvent),
                sbWhere.toString(),
                null
            ).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(
                    TABLE,
                    null,
                    toContentValuesMapper.map(fsEvent)
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
        closeDB()
        //
        return daoObjReturn
    }

    override fun addUpdate(items: MutableList<FSTripEvent>?, status: Boolean): DaoObjReturn {
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
            items?.forEach { fsEvent ->
                curAction = DaoObjReturn.UPDATE
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(fsEvent)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(
                    TABLE,
                    toContentValuesMapper.map(fsEvent),
                    sbWhere.toString(),
                    null
                ).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(
                        TABLE,
                        null,
                        toContentValuesMapper.map(fsEvent)
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

    fun getByString(sQuery: String?, dbInstance: SQLiteDatabase?): FSTripEvent? {
        var fsEvent: FSTripEvent? = null
        if (dbInstance == null) {
            openDB()
        } else {
            this.db = dbInstance
        }
        lateinit var cursor: Cursor
        try {
            cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                fsEvent = toFSTripEvent.map(cursor)
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
        return fsEvent
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var hmAux: HMAux? = null
        openDB()

        try {
            val cursor = db.rawQuery(sQuery!!, null)
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

    override fun query(sQuery: String?): MutableList<FSTripEvent> {
        var items = mutableListOf<FSTripEvent>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux: FSTripEvent = toFSTripEvent.map(cursor)
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

    private fun queryForFullUpdate(sQuery: String?): MutableList<FSTripEvent>? {
        var items = mutableListOf<FSTripEvent>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux: FSTripEvent = toFSTripEvent.map(cursor)
                items.add(uAux)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
            return null
        } finally {
        }
        //
        closeDB()
        return items
    }


    fun query(sQuery: String?, db: SQLiteDatabase): MutableList<FSTripEvent> {
        var items = mutableListOf<FSTripEvent>()
        val cursor = db.rawQuery(sQuery!!, null)
        while (cursor.moveToNext()) {
            val uAux: FSTripEvent = toFSTripEvent.map(cursor)
            items.add(uAux)
        }
        //
        cursor.close()
        return items
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val items: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
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
    private fun getWherePkClause(fsEvent: FSTripEvent?): StringBuilder {
        fsEvent?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                            $CUSTOMER_CODE = '${it.customerCode}'  
                        AND $TRIP_PREFIX = '${it.tripPrefix}'
                        AND $TRIP_CODE = '${it.tripCode}'
                        AND $EVENT_TYPE_CODE = '${it.eventTypeCode}'
                        AND $EVENT_SEQ = '${it.eventSeq}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    fun addUpdate(fsEvent: FSTripEvent, dbInstance: SQLiteDatabase?): DaoObjReturn {
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
            val sbWhere: StringBuilder = getWherePkClause(fsEvent)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(
                TABLE,
                toContentValuesMapper.map(fsEvent),
                sbWhere.toString(),
                null
            ).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(
                    TABLE,
                    null,
                    toContentValuesMapper.map(fsEvent)
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


    class CursorToFSTripEvent : Mapper<Cursor, FSTripEvent> {
        @SuppressLint("Range")
        override fun map(from: Cursor?): FSTripEvent? {
            from?.let { cursor ->
                with(cursor) {
                    return FSTripEvent(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        tripPrefix = getInt(getColumnIndex(TRIP_PREFIX)),
                        tripCode = getInt(getColumnIndex(TRIP_CODE)),
                        eventTypeCode = getInt(getColumnIndex(EVENT_TYPE_CODE)),
                        eventSeq = getInt(getColumnIndex(EVENT_SEQ)),
                        eventTypeDesc = getString(getColumnIndex(EVENT_TYPE_DESC)),
                        eventStatus = getString(getColumnIndex(EVENT_STATUS)),
                        eventTime = getString(getColumnIndex(EVENT_TIME)),
                        eventAllowedTime = getString(getColumnIndex(EVENT_ALLOWED_TIME)),
                        eventTimeAlert = getInt(getColumnIndex(EVENT_TIME_ALERT)),
                        cost = getDoubleOrNull(getColumnIndex(COST)),
                        comment = getStringOrNull(getColumnIndex(COMMENT)),
                        photoUrl = getStringOrNull(getColumnIndex(PHOTO_URL)),
                        photoLocal = getStringOrNull(getColumnIndex(PHOTO_LOCAL)),
                        photoName = getStringOrNull(getColumnIndex(PHOTO_NAME)),
                        eventPhotoChanged = getInt(getColumnIndex(PHOTO_CHANGED)),
                        eventStart = getString(getColumnIndex(EVENT_START)),
                        eventEnd = getStringOrNull(getColumnIndex(EVENT_END)),
                        destinationSeq = getIntOrNull(getColumnIndex(DESTINATION_SEQ)),
                    )
                }
            }

            return null
        }
    }

    class FSTripEventToContentValuesMapper : Mapper<FSTripEvent, ContentValues> {
        override fun map(from: FSTripEvent?): ContentValues {
            val contentValues = ContentValues()

            from?.let { position ->
                with(contentValues) {
                    if (position.customerCode > -1) put(CUSTOMER_CODE, position.customerCode)
                    if (position.tripPrefix > -1) put(TRIP_PREFIX, position.tripPrefix)
                    if (position.tripCode > -1) put(TRIP_CODE, position.tripCode)
                    if (position.eventTypeCode > -1) put(
                        EVENT_TYPE_CODE,
                        position.eventTypeCode
                    )
                    put(EVENT_SEQ, position.eventSeq)
                    put(EVENT_TYPE_DESC, position.eventTypeDesc)
                    put(COST, position.cost)
                    put(COMMENT, position.comment)
                    put(PHOTO_URL, position.photoUrl)
                    put(PHOTO_NAME, position.photoName)
                    put(PHOTO_LOCAL, position.photoLocal)
                    put(EVENT_START, position.eventStart)
                    put(EVENT_END, position.eventEnd)
                    put(EVENT_ALLOWED_TIME, position.allowedTime)
                    put(EVENT_TIME, position.eventTime)
                    put(EVENT_TIME_ALERT, position.timeAlert)
                    put(EVENT_STATUS, position.eventStatus)
                    put(PHOTO_CHANGED, position.eventPhotoChanged)
                    put(DESTINATION_SEQ, position.destinationSeq)
                }
            }

            return contentValues
        }

    }

    //--------------- [ QUERY ]  ---------------------- \\


    fun getEventFull(
        tripPrefix: Int,
        tripCode: Int,
    ): FSTripEvent? {
        val value = query(
            """
            SELECT * FROM $TABLE 
            WHERE $TRIP_PREFIX = '$tripPrefix'
             AND $TRIP_CODE = '$tripCode'
             AND $EVENT_STATUS = '${EventStatus.WAITING.name}'
        """.trimIndent()
        )

        return if (value.isNotEmpty()) value[0]
        else null
    }

    @Throws(SQLiteException::class)
    fun getEventFull(
        tripPrefix: Int,
        tripCode: Int,
        seq: Int,
        dbInstance: SQLiteDatabase
    ): FSTripEvent? {
        val value = query(
            """
            SELECT * FROM $TABLE 
            WHERE $TRIP_PREFIX = '$tripPrefix'
             AND $TRIP_CODE = '$tripCode'
             AND $EVENT_SEQ = '$seq'
        """.trimIndent(), dbInstance
        )

        return if (value.isNotEmpty()) value[0]
        else null
    }

    fun getEvent(
        tripPrefix: Int,
        tripCode: Int,
        seq: Int
    ): FSTripEvent? {
        val value = query(
            """
            SELECT * FROM $TABLE 
            WHERE $TRIP_PREFIX = '$tripPrefix'
             AND $TRIP_CODE = '$tripCode'
             AND $EVENT_SEQ = '$seq'
        """.trimIndent()
        )

        return if (value.isNotEmpty()) value[0]
        else null
    }

    @kotlin.jvm.Throws(SQLiteException::class)
    fun update(event: FSTripEvent, dbInstance: SQLiteDatabase?) {
        addUpdate(event, dbInstance).let {
            if (it.hasError()) throw SQLiteException(it.errorMsg)
        }
    }

    fun getEventListDateValidation(
        tripPrefix: Int,
        tripCode: Int,
        eventSeq: Int?,
    ): List<EventValidation> {

        val eventSeqFilter = eventSeq?.let {
            "AND $EVENT_SEQ != $it"
        } ?: ""


        val value = query_HM(
            """
            SELECT e.*, et.$WAIT_ALLOWED 
            FROM $TABLE e
            LEFT JOIN ${FSEventTypeDao.TABLE}  et
                   ON et.${FSEventTypeDao.CUSTOMER_CODE} = e.$CUSTOMER_CODE
                  AND et.${FSEventTypeDao.EVENT_TYPE_CODE} = e.$EVENT_TYPE_CODE
            WHERE $TRIP_PREFIX = '$tripPrefix' 
            AND $TRIP_CODE = '$tripCode'
            $eventSeqFilter
            AND ($EVENT_END IS NOT NULL AND $EVENT_END != '')
            AND event_status IN('${EventStatus.DONE.name}', '${EventStatus.WAITING.name}')
            ORDER BY $EVENT_START ASC
        """.trimIndent()
        )

        val eventValidation = value.map {

            val fsTripEvent = FSTripEvent(
                customerCode = it[CUSTOMER_CODE]!!.toLong(),
                tripPrefix = it[TRIP_PREFIX]!!.toInt(),
                tripCode = it[TRIP_CODE]!!.toInt(),
                eventSeq = it[EVENT_SEQ]!!.toInt(),
                eventTypeCode = it[EVENT_TYPE_CODE]!!.toInt(),
                eventTypeDesc = it[EVENT_TYPE_DESC]!!,
                eventStatus = it[EVENT_STATUS]!!,
                eventStart = it[EVENT_START]!!,
                eventEnd = it[EVENT_END]!!,
                eventTime = it[EVENT_TIME]!!,
                eventAllowedTime = it[EVENT_ALLOWED_TIME]!!,
                eventTimeAlert = it[EVENT_TIME_ALERT]?.toIntOrNull(),
                cost = it[COST]?.toDoubleOrNull(),
                comment = it[COMMENT]!!,
                photoLocal = it[PHOTO_LOCAL]!!,
                photoName = it[PHOTO_NAME]!!,
                photoUrl = it[PHOTO_URL]!!,
                eventPhotoChanged = it[PHOTO_CHANGED]?.toInt() ?: 0,
                destinationSeq = it[DESTINATION_SEQ]?.toInt(),
            )
            EventValidation(
                event = fsTripEvent,
                waitAllowed = it[WAIT_ALLOWED]?.toInt() == 1
            )
        }

        return if (eventValidation.isEmpty()) emptyList() else eventValidation
    }

    fun getExtract(
        tripPrefix: Int,
        tripCode: Int,
    ): List<FSTripEvent> {
        val value = query(
            """
            SELECT * FROM $TABLE
            WHERE $TRIP_PREFIX = '$tripPrefix' 
            AND $TRIP_CODE = '$tripCode'
            AND ($EVENT_END IS NOT NULL AND $EVENT_END != '')
            AND event_status IN('${EventStatus.DONE.name}', '${EventStatus.WAITING.name}')
            ORDER BY $EVENT_START ASC
        """.trimIndent()
        )

        return if (value.isEmpty()) emptyList() else value
    }

    fun listAllEvents(
        tripPrefix: Int,
        tripCode: Int,
    ): List<FSTripEvent>? {
        return queryForFullUpdate(
            """
            SELECT * FROM $TABLE
            WHERE $TRIP_PREFIX = '$tripPrefix' 
            AND $TRIP_CODE = '$tripCode'
            ORDER BY $EVENT_START ASC
        """.trimIndent()
        )
    }

    @Throws(Exception::class)
    fun getLastEvent(
        tripPrefix: Int,
        tripCode: Int
    ): FSTripEvent? {
        val value = query(
            """
            SELECT * FROM $TABLE
            WHERE $TRIP_PREFIX = '$tripPrefix' 
            AND $TRIP_CODE = '$tripCode'
            ORDER BY $EVENT_SEQ DESC
        """.trimIndent()
        )

        return if (value.isEmpty()) null else value[0]
    }

    fun getAllEventsByTrip(customerCode: Long, tripPrefix: Int, tripCode: Int): List<FSTripEvent> {
        return query(
            """
            SELECT * FROM $TABLE
            WHERE $CUSTOMER_CODE = '$customerCode' 
            AND $TRIP_PREFIX = '$tripPrefix' 
            AND $TRIP_CODE = '$tripCode'
        """.trimIndent()
        )
    }

    fun getEventConflict(
        tripPrefix: Int,
        tripCode: Int,
        newStart: String,
        newEnd: String?,
        eventSeq: Int,
        validateStartDateEquals: Boolean = false
    ): EventConflict? {

        // Cláusula para ignorar eventos de espera
        val waitAllowedFilter = """
            JOIN ${FSEventTypeDao.TABLE} fet
              ON fet.${FSEventTypeDao.EVENT_TYPE_CODE} = em.$EVENT_TYPE_CODE
             AND fet.${WAIT_ALLOWED} = 1
    """.trimIndent()

        val validationStart = if(validateStartDateEquals) "<" else "<="
        val validationEnd = if(validateStartDateEquals) ">" else ">="


        // Conflito de início
        val startOverlap = query(
            """
        SELECT * FROM $TABLE em
        $waitAllowedFilter
        WHERE em.$TRIP_PREFIX = '$tripPrefix'
        AND em.$TRIP_CODE = '$tripCode'
        AND em.$EVENT_STATUS != '${EventStatus.CANCELLED.name}'
        AND em.$EVENT_SEQ != '$eventSeq'
        AND strftime('%s', $EVENT_START) $validationStart strftime('%s', '$newStart')
        AND (em.$EVENT_END IS NULL OR strftime('%s', em.$EVENT_END) > strftime('%s', '$newStart'))
        LIMIT 1
    """.trimIndent()
        ).firstOrNull()

        if (startOverlap != null) {
            return EventConflict(
                dateStart = startOverlap.eventStart,
                dateEnd = startOverlap.eventEnd,
                type = EventConflictType.START_OVERLAP,
                description = startOverlap.eventTypeDesc
            )
        }

        // Conflito de término
        if (newEnd != null) {
            val endOverlap = query(
                """
            SELECT * FROM $TABLE em
            $waitAllowedFilter
            WHERE em.$TRIP_PREFIX = '$tripPrefix'
            AND em.$TRIP_CODE = '$tripCode'
            AND em.$EVENT_STATUS != '${EventStatus.CANCELLED.name}'
            AND em.$EVENT_SEQ != '$eventSeq'
            AND strftime('%s', em.$EVENT_START) < strftime('%s', '$newEnd')
            AND (em.$EVENT_END IS NULL OR strftime('%s', em.$EVENT_END) $validationEnd strftime('%s', '$newEnd'))
            LIMIT 1
        """.trimIndent()
            ).firstOrNull()

            if (endOverlap != null) {
                return EventConflict(
                    dateStart = endOverlap.eventStart,
                    dateEnd = endOverlap.eventEnd,
                    type = EventConflictType.END_OVERLAP,
                    description = endOverlap.eventTypeDesc
                )
            }

            // O evento atual engloba completamente outro evento
            val rangeOverlap = query(
                """
            SELECT * FROM $TABLE em
            $waitAllowedFilter
            WHERE em.$TRIP_PREFIX = '$tripPrefix'
            AND em.$TRIP_CODE = '$tripCode'
            AND em.$EVENT_STATUS != '${EventStatus.CANCELLED.name}'
            AND em.$EVENT_SEQ != '$eventSeq'
            AND strftime('%s', em.$EVENT_START) >= strftime('%s', '$newStart')
            AND strftime('%s', em.$EVENT_END) <= strftime('%s', '$newEnd')
            LIMIT 1
        """.trimIndent()
            ).firstOrNull()

            if (rangeOverlap != null) {
                return EventConflict(
                    dateStart = rangeOverlap.eventStart,
                    dateEnd = rangeOverlap.eventEnd,
                    type = EventConflictType.RANGE_OVERLAP,
                    description = rangeOverlap.eventTypeDesc
                )
            }
        }

        return null
    }


    fun hasEventByTrip(
        prefix: Int,
        code: Int
    ): FSTripEvent? {
        return query("""
            SELECT * FROM $TABLE
            WHERE $TRIP_PREFIX = '$prefix'
            AND $TRIP_CODE = '$code' 
            AND $EVENT_STATUS = '${EventStatus.WAITING.name}'
        """.trimIndent()).firstOrNull()
    }

    companion object {

        const val TABLE = "fs_trip_event"
        const val CUSTOMER_CODE = "customer_code"
        const val TRIP_PREFIX = "trip_prefix"
        const val TRIP_CODE = "trip_code"
        const val EVENT_TYPE_CODE = "event_type_code"
        const val EVENT_TYPE_DESC = "event_type_desc"
        const val EVENT_SEQ = "event_seq"
        const val EVENT_STATUS = "event_status"
        const val EVENT_TIME = "event_time"
        const val EVENT_ALLOWED_TIME = "event_allowed_time"
        const val EVENT_TIME_ALERT = "event_time_alert"
        const val COST = "cost"
        const val COMMENT = "comment"
        const val PHOTO_URL = "photo_url"
        const val PHOTO_NAME = "photo_name"
        const val PHOTO_LOCAL = "photo_local"
        const val PHOTO_CHANGED = "photo_changed"
        const val EVENT_START = "event_start"
        const val EVENT_END = "event_end"
        const val DESTINATION_SEQ = "destination_seq"

        fun instance(context: Context) = FSTripEventDao(context)

    }
}