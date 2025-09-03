package com.namoadigital.prj001.dao.trip

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.trip.domain.model.OdometerArrivedDestination
import com.namoadigital.prj001.core.trip.domain.model.OdometerArrivedDestination.Companion.toOdometerList
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.DaoWithReturn
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.md.MDRegionDao
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.model.trip.toDestinationStatus
import com.namoadigital.prj001.sql.trip.DestinationGetMax
import com.namoadigital.prj001.sql.trip.FsTripDestinationSqlGetDestinationByStatus
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailables
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import javax.inject.Inject

class FsTripDestinationDao @Inject constructor(
    val context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI),
    DaoWithReturn<FsTripDestination> {
    private val toFsTripDestination: Mapper<Cursor, FsTripDestination>
    private val toContentValuesMapper: Mapper<FsTripDestination, ContentValues>

    constructor(
        context: Context
    ) : this(
        context = context,
        mDB_NAME = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
        mDB_VERSION = Constant.DB_VERSION_CUSTOM
    )

    init {
        toFsTripDestination = CursorToFsTripDestination()
        toContentValuesMapper = FsTripDestinationToContentValuesMapper()
    }

    override fun addUpdate(fsTripDestination: FsTripDestination?): DaoObjReturn {
        return addUpdate(fsTripDestination, null)
    }

    override fun addUpdate(items: MutableList<FsTripDestination>?, status: Boolean): DaoObjReturn {
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
            items?.forEach { FsTripDestination ->
                curAction = DaoObjReturn.UPDATE
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(FsTripDestination)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(
                    TABLE,
                    toContentValuesMapper.map(FsTripDestination),
                    sbWhere.toString(),
                    null
                ).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(
                        TABLE,
                        null,
                        toContentValuesMapper.map(FsTripDestination)
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

    fun addUpdate(sQuery: String?, dbInstance: SQLiteDatabase? = null): DaoObjReturn {

        val daoObjReturn = DaoObjReturn()
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }
        try {
            db.execSQL(sQuery)
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e);
            daoObjReturn.setError(true)
        } finally {
        }

        if (dbInstance == null) {
            closeDB()
        }

        return daoObjReturn
    }


    fun queryForUpdate(sQuery: String?, dbInstance: SQLiteDatabase?): DaoObjReturn {

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
        if (dbInstance == null) {
            db.beginTransaction()
        }
        //
        try {
            db.execSQL(sQuery)
            //
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
            daoObjReturn.actionReturn = addUpdateRet
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        return daoObjReturn
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
            ToolBox_Inf.registerException(javaClass.name, e);
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

    fun getByString(sQuery: String?, dbInstance: SQLiteDatabase?): FsTripDestination? {
        var FsTripDestination: FsTripDestination? = null
        if (dbInstance == null) {
            openDB()
        } else {
            this.db = dbInstance
        }
        lateinit var cursor: Cursor
        try {
            cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                FsTripDestination = toFsTripDestination.map(cursor)
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
        return FsTripDestination
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

    override fun query(sQuery: String?): MutableList<FsTripDestination> {
        var items = mutableListOf<FsTripDestination>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux: FsTripDestination = toFsTripDestination.map(cursor)
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

    private fun queryForFullUpdate(sQuery: String?): MutableList<FsTripDestination>? {
        var items = mutableListOf<FsTripDestination>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux: FsTripDestination = toFsTripDestination.map(cursor)
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
    private fun getWherePkClause(FsTripDestination: FsTripDestination?): StringBuilder {
        FsTripDestination?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        $CUSTOMER_CODE = '${it.customerCode}'  
                        AND $TRIP_PREFIX = '${it.tripPrefix}'
                        AND $TRIP_CODE = '${it.tripCode}'
                        AND $DESTINATION_SEQ = '${it.destinationSeq}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    fun getDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
    ): FsTripDestination? {
        val value = query(
            """
            SELECT * 
              FROM $TABLE 
             WHERE $CUSTOMER_CODE = $customerCode 
               AND $TRIP_PREFIX =  $tripPrefix
               AND $TRIP_CODE =  $tripCode
               AND $DESTINATION_SEQ =  $destinationSeq
        """.trimIndent()
        )
        if (value.isNotEmpty()) return value[0]
        return null
    }


    fun getLastDestinationStatus(
        tripPrefix: Int,
        tripCode: Int
    ): DestinationStatus? {
        val value = query(
            """
            SELECT * FROM $TABLE d
            WHERE d.$TRIP_PREFIX = '$tripPrefix'
            AND d.$TRIP_CODE = '$tripCode'
            ORDER BY d.$DESTINATION_SEQ DESC
            LIMIT 1
        """.trimIndent()
        )
        return if (value.isNotEmpty()) value[0].destinationStatus?.toDestinationStatus() else null
    }

    @Throws(Exception::class)
    fun getNextDestinationSeq(
        tripPrefix: Int,
        tripCode: Int
    ): Int? {
        val value = getByStringHM(
            DestinationGetMax(
                tripPrefix,
                tripCode
            ).toSqlQuery()
        )
        value?.let {
            return if (value.hasConsistentValue(DESTINATION_SEQ)) {
                value[DESTINATION_SEQ]?.toInt()
            } else {
                null
            }
        }
        return null
    }

    fun listAllDestinations(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
    ): List<FsTripDestination>? {

        return queryForFullUpdate(
            """
            SELECT * 
              FROM $TABLE 
             WHERE $CUSTOMER_CODE = $customerCode 
               AND $TRIP_PREFIX =  $tripPrefix
               AND $TRIP_CODE =  $tripCode
        """.trimIndent()
        )
    }


    fun getOnSiteDestination(): FsTripDestination? {
        val dao = FSTripDao(context)
        val trip = dao.getTrip()
        trip?.let {
            if (it.tripStatus == TripStatus.ON_SITE.toDescription()) {
                val value = query(
                    FsTripDestinationSqlGetDestinationByStatus(
                        it.customerCode,
                        it.tripPrefix,
                        it.tripCode,
                        DestinationStatus.ARRIVED.toDescription(),
                    ).toSqlQuery()
                )
                if (value.isNotEmpty()) return value[0]
            }
        }

        return null
    }

    fun getDestinationByStatus(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        status: String,
    ): FsTripDestination? {
        val value = query(
            FsTripDestinationSqlGetDestinationByStatus(
                customerCode,
                tripPrefix,
                tripCode,
                status,
            ).toSqlQuery()
        )
        if (value.isNotEmpty()) return value[0]
        return null
    }

    fun updateStatus(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        status: String,
        dbInstance: SQLiteDatabase? = null
    ): DaoObjReturn {
        return queryForUpdate(
            """
                UPDATE $TABLE
                SET $DESTINATION_STATUS = '$status'
                WHERE $TRIP_PREFIX = $tripPrefix
                AND $TRIP_CODE = $tripCode
                AND $DESTINATION_SEQ = $destinationSeq
            """.trimIndent(),
            dbInstance
        )
    }

    fun updateDestinationArrivedStatus(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        status: String,
        date: String,
        dbInstance: SQLiteDatabase? = null
    ): DaoObjReturn {
        return queryForUpdate(
            """
                UPDATE $TABLE
                SET $DESTINATION_STATUS = '$status',
                    $ARRIVED_DATE = '$date',
                    $ARRIVED_TYPE = 'MANUAL'
                WHERE $TRIP_PREFIX = $tripPrefix
                AND $TRIP_CODE = $tripCode
                AND $DESTINATION_SEQ = $destinationSeq
            """.trimIndent(),
            dbInstance
        )
    }

    fun updateDestinationDepartedStatus(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        status: String,
        date: String,
        dbInstance: SQLiteDatabase? = null
    ): DaoObjReturn {
        return queryForUpdate(
            """
                UPDATE $TABLE
                SET $DESTINATION_STATUS = '$status',
                    $DEPARTED_DATE = '$date',
                    $DEPARTED_TYPE = 'MANUAL'
                WHERE $TRIP_PREFIX = $tripPrefix
                AND $TRIP_CODE = $tripCode
                AND $DESTINATION_SEQ = $destinationSeq
            """.trimIndent(),
            dbInstance
        )
    }

    fun getExtract(
        tripPrefix: Int,
        tripCode: Int
    ): List<FsTripDestination> {
        val value = query(
            """
            SELECT * 
            FROM $TABLE
            WHERE $TRIP_PREFIX = '$tripPrefix'
            AND $TRIP_CODE = '$tripCode'
            AND $ARRIVED_DATE IS NOT NULL
            ORDER BY $ARRIVED_DATE ASC
        """.trimIndent()
        )

        return if (value.isEmpty()) emptyList() else value
    }

    fun getListOdometer(
        tripPrefix: Int,
        tripCode: Int,
    ): List<OdometerArrivedDestination> {
        val value = getExtract(tripPrefix, tripCode).map { it.toOdometerList() }

        return value.ifEmpty { emptyList() }
    }


    fun addUpdate(
        fsTripDestination: FsTripDestination?,
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

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(fsTripDestination)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(
                TABLE,
                toContentValuesMapper.map(fsTripDestination),
                sbWhere.toString(),
                null
            ).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(
                    TABLE,
                    null,
                    toContentValuesMapper.map(fsTripDestination)
                )
            }
            //
            fsTripDestination?.let {
                var actionSeq = 0
                it.actions?.forEach { action ->
                    //Nao existe seq para action.
                    action.actionSeq = actionSeq++
                    val fsTripDestinationActionDao = FsTripDestinationActionDao(context)
                    fsTripDestinationActionDao.addUpdate(action, db)
                }
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

    @Throws(SQLiteException::class)
    fun updateArrivedFleet(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        odometer: Long?,
        photoPath: String?,
        photoChanged: Int,
        db: SQLiteDatabase,
    ) {
        val imageQuery = if (!photoPath.isNullOrBlank()) {
            "$ARRIVED_FLEET_PHOTO_LOCAL = '$photoPath'"
        } else {
            "$ARRIVED_FLEET_PHOTO_LOCAL = null, $ARRIVED_FLEET_PHOTO = null"

        }
        //
        val photoName = if (!photoPath.isNullOrBlank()) {
            "$ARRIVED_FLEET_PHOTO_NAME = '$photoPath'"
        } else {
            "$ARRIVED_FLEET_PHOTO_NAME = null"
        }
        //
        val odometerQuery = odometer?.let {
            "$ARRIVED_FLEET_ODOMETER = $odometer"
        } ?: "$ARRIVED_FLEET_ODOMETER = null"
        //
        addUpdate(
            """
                UPDATE $TABLE
                SET
                $odometerQuery,
                $photoName,
                $imageQuery,
                $ARRIVED_FLEET_PHOTO_CHANGED = $photoChanged
                WHERE $TRIP_PREFIX = '$tripPrefix' 
                AND $TRIP_CODE = '$tripCode'
                AND $DESTINATION_SEQ = '$destinationSeq'
            """.trimIndent(),
            db
        ).let {
            if (it.hasError()) throw SQLiteException(it.errorMsg)
        }
    }

    @Throws(SQLiteException::class)
    fun updateArrivedAndDeparted(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        arrivedDate: String,
        departedDate: String,
        db: SQLiteDatabase?
    ) {

        addUpdate(
            """
                UPDATE $TABLE
                SET $ARRIVED_DATE = '$arrivedDate',
                $DEPARTED_DATE = '$departedDate'
                WHERE $TRIP_PREFIX = '$tripPrefix' 
                AND $TRIP_CODE = '$tripCode'
                AND $DESTINATION_SEQ = '$destinationSeq'
            """.trimIndent(),
            db
        ).let {
            if (it.hasError()) throw SQLiteException(it.errorMsg)
        }

    }

    fun previousDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType
    ): FsTripDestination? {
        val destinationFilter = destinationSeq?.let {
            "AND $DESTINATION_SEQ < '$destinationSeq'"
        } ?: ""

        val odometerFilter = when (type) {
            GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.ODOMETER_PREVIOUS,
            GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.ODOMETER_BOTH ->
                "AND $ARRIVED_FLEET_ODOMETER is not null"

            else -> ""
        }

        return getByString(
            """
               SELECT *
                 FROM $TABLE
                WHERE $CUSTOMER_CODE = '$customerCode' 
                  AND $TRIP_PREFIX = '$tripPrefix' 
                  AND $TRIP_CODE = '$tripCode'
                  $destinationFilter
                  AND $DESTINATION_STATUS != '${DestinationStatus.CANCELLED}' 
                  AND $DESTINATION_TYPE != '${FsTripDestination.OVER_NIGHT_DESTINATION_TYPE}' 
                  $odometerFilter 
                  ORDER BY $DESTINATION_SEQ DESC
                  LIMIT 1
            """.trimIndent()
        )
    }

    fun nextDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType
    ): FsTripDestination? {
        val destinationFilter = destinationSeq?.let {
            "AND $DESTINATION_SEQ > '$destinationSeq'"
        } ?: ""

        val odometerFilter = when (type) {
            GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.ODOMETER_NEXT,
            GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.ODOMETER_BOTH ->
                "AND $ARRIVED_FLEET_ODOMETER is not null"

            else -> ""
        }

        return getByString(
            """
               SELECT *
                 FROM $TABLE
                WHERE $CUSTOMER_CODE = '$customerCode' 
                  AND $TRIP_PREFIX = '$tripPrefix' 
                  AND $TRIP_CODE = '$tripCode'
                  $destinationFilter
                  and $DESTINATION_STATUS not in ('${DestinationStatus.CANCELLED}','${DestinationStatus.PENDING}')
                  AND $DESTINATION_TYPE != '${FsTripDestination.OVER_NIGHT_DESTINATION_TYPE}'
                  $odometerFilter
                  order by $DESTINATION_SEQ asc
                  limit 1
            """.trimIndent()
        )
    }

    fun getExternalAddressList(userCode: String, deviceGMT: String): List<DestinationAvailables> {
        query_HM(
            """
            SELECT 
             'TICKET' as destinationType,
             s.${MD_SiteDao.SITE_CODE},
             s.${MD_SiteDao.SITE_DESC},
             t.${TK_TicketDao.TICKET_PREFIX},
             t.${TK_TicketDao.TICKET_CODE},
             t.${TK_TicketDao.OPEN_SERIAL_ID},
             t.${TK_TicketDao.HAS_ADDRESS},
             t.${TK_TicketDao.ADDRESS_COUNTRY},
             t.${TK_TicketDao.ADDRESS_STATE},
             t.${TK_TicketDao.ADDRESS_CITY},
             t.${TK_TicketDao.ADDRESS_DISTRICT},
             t.${TK_TicketDao.ADDRESS_STREET},
             t.${TK_TicketDao.ADDRESS_NUM},
             t.${TK_TicketDao.ADDRESS_COMPLEMENT},
             t.${TK_TicketDao.ADDRESS_ZIPCODE},
             t.${TK_TicketDao.ADDRESS_LAT},
             t.${TK_TicketDao.ADDRESS_LNG},
             t.${TK_TicketDao.ADDRESS_PLUS_CODE},
             t.${TK_TicketDao.CONTACT_NAME},
             t.${TK_TicketDao.CONTACT_PHONE},
             r.${MDRegionDao.REGION_CODE},
             r.${MDRegionDao.REGION_DESC},
             t.${TK_TicketDao.KANBAN_DATE},
              CASE WHEN t.${TK_TicketDao.IS_PRIORITY} = 1 THEN 1 ELSE 0 END as priorityCnt,
              CASE WHEN strftime('%Y-%m-%d', t.${TK_TicketDao.KANBAN_DATE}, '$deviceGMT') = strftime('%Y-%m-%d', 'now', '$deviceGMT') THEN 1 ELSE 0 END as todayCnt,
              CASE WHEN strftime('%Y-%m-%d', t.${TK_TicketDao.KANBAN_DATE}, '$deviceGMT') < strftime('%Y-%m-%d', 'now', '$deviceGMT') THEN 1 ELSE 0 END as lateCnt,
              CASE WHEN strftime('%Y-%m-%d', t.${TK_TicketDao.KANBAN_DATE}, '$deviceGMT', '+7 days') > strftime('%Y-%m-%d', 'now', '$deviceGMT', '+7 days') THEN 1 ELSE 0 END as nextCnt
              
             FROM ${TK_TicketDao.TABLE} t
                LEFT JOIN ${MD_SiteDao.TABLE} s ON t.${TK_TicketDao.OPEN_SITE_CODE} = s.${MD_SiteDao.SITE_CODE}
                LEFT JOIN ${MDRegionDao.TABLE_NAME} r ON s.${MD_SiteDao.REGION_CODE} = r.${MDRegionDao.REGION_CODE}
                LEFT JOIN $TABLE d ON t.${TK_TicketDao.TICKET_PREFIX} = d.$TICKET_PREFIX
                AND t.${TK_TicketDao.TICKET_CODE} = d.$TICKET_CODE
                AND d.$DESTINATION_STATUS = '${DestinationStatus.ARRIVED.toDescription()}'
            WHERE t.${TK_TicketDao.KANBAN} = 1
            AND d.$TICKET_PREFIX IS NULL
            AND t.${TK_TicketDao.HAS_ADDRESS} = 1
            AND t.${TK_TicketDao.MAIN_USER} = $userCode
            AND t.${TK_TicketDao.TICKET_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_PENDING}', '${ConstantBaseApp.SYS_STATUS_PROCESS}')
            AND t.${TK_TicketDao.KANBAN_STAGE} IN ('${TK_TicketDao.KANBAN_STAGE_EXECUTION}', '${TK_TicketDao.KANBAN_STAGE_RELEASE_FOR_EXECUTION}')
            
        """.trimIndent()
        ).let { query ->

            return query.map { hmAux ->
                DestinationAvailables(
                    destinationType = hmAux["destinationType"],
                    siteCode = hmAux[MD_SiteDao.SITE_CODE].takeIf { !it.isNullOrEmpty() }?.toInt(),
                    siteDesc = hmAux[MD_SiteDao.SITE_DESC],
                    ticketPrefix = hmAux[TK_TicketDao.TICKET_PREFIX].takeIf { !it.isNullOrEmpty() }
                        ?.toInt(),
                    ticketCode = hmAux[TK_TicketDao.TICKET_CODE].takeIf { !it.isNullOrEmpty() }
                        ?.toInt(),
                    serialId = hmAux[TK_TicketDao.OPEN_SERIAL_ID],
                    address = hmAux[TK_TicketDao.HAS_ADDRESS].takeIf { !it.isNullOrEmpty() }
                        ?.toInt(),
                    countryId = hmAux[TK_TicketDao.ADDRESS_COUNTRY_ID],
                    state = hmAux[TK_TicketDao.ADDRESS_STATE],
                    city = hmAux[TK_TicketDao.ADDRESS_CITY],
                    district = hmAux[TK_TicketDao.ADDRESS_DISTRICT],
                    street = hmAux[TK_TicketDao.ADDRESS_STREET],
                    streetnumber = hmAux[TK_TicketDao.ADDRESS_NUM],
                    complement = hmAux[TK_TicketDao.ADDRESS_COMPLEMENT],
                    zipcode = hmAux[TK_TicketDao.ADDRESS_ZIPCODE],
                    plusCode = hmAux[TK_TicketDao.ADDRESS_PLUS_CODE],
                    lat = hmAux[TK_TicketDao.ADDRESS_LAT].takeIf { !it.isNullOrEmpty() }
                        ?.toDouble(),
                    lon = hmAux[TK_TicketDao.ADDRESS_LNG].takeIf { !it.isNullOrEmpty() }
                        ?.toDouble(),
                    contactName = hmAux[TK_TicketDao.CONTACT_NAME],
                    contactPhone = hmAux[TK_TicketDao.CONTACT_PHONE],
                    regionCode = hmAux[MDRegionDao.REGION_CODE].takeIf { !it.isNullOrEmpty() }
                        ?.toInt(),
                    regionDesc = hmAux[MDRegionDao.REGION_DESC],
                    minDate = hmAux[TK_TicketDao.KANBAN_DATE],
                    priorityCnt = hmAux["priorityCnt"].takeIf { !it.isNullOrEmpty() }?.toInt(),
                    todayCnt = hmAux["todayCnt"].takeIf { !it.isNullOrEmpty() }?.toInt(),
                    lateCnt = hmAux["lateCnt"].takeIf { !it.isNullOrEmpty() }?.toInt(),
                    nextCnt = hmAux["nextCnt"].takeIf { !it.isNullOrEmpty() }?.toInt(),
                )
            }

        }
    }

    fun getSiteAddressList(userCode: Int): List<DestinationAvailables> {

        query_HM(
            """
            SELECT 
                'SITE' as destinationType,
                s.${MD_SiteDao.SITE_CODE},
                s.${MD_SiteDao.SITE_DESC},
                s.${MD_SiteDao.COUNTRY_CODE},
                s.${MD_SiteDao.STATE},
                s.${MD_SiteDao.CITY},
                s.${MD_SiteDao.DISTRICT},
                s.${MD_SiteDao.STREET},
                s.${MD_SiteDao.NUM},
                s.${MD_SiteDao.COMPLEMENT},
                s.${MD_SiteDao.ZIP_CODE},
                s.${MD_SiteDao.PLUS_CODE},       
                s.${MD_SiteDao.CONTACT_NAME},
                s.${MD_SiteDao.CONTACT_PHONE},
                s.${MD_SiteDao.LATITUDE},
                s.${MD_SiteDao.LONGITUDE},
                r.${MDRegionDao.REGION_CODE},
                r.${MDRegionDao.REGION_DESC},
                min(t.${TK_TicketDao.KANBAN_DATE}) as minDate
                FROM ${MD_SiteDao.TABLE} s
                      LEFT JOIN ${MDRegionDao.TABLE_NAME} r
                      ON s.${MD_SiteDao.REGION_CODE} = r.${MDRegionDao.REGION_CODE}
                      LEFT JOIN ${TK_TicketDao.TABLE} t
                      ON s.${MD_SiteDao.SITE_CODE} = t.${TK_TicketDao.OPEN_SITE_CODE}
                      AND t.${TK_TicketDao.HAS_ADDRESS} = 0
                      AND t.${TK_TicketDao.KANBAN} = 1
                      AND t.${TK_TicketDao.MAIN_USER} = $userCode
                      AND t.${TK_TicketDao.TICKET_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_PENDING}', '${ConstantBaseApp.SYS_STATUS_PROCESS}')
                      AND t.${TK_TicketDao.KANBAN_STAGE} IN ('${TK_TicketDao.KANBAN_STAGE_EXECUTION}', '${TK_TicketDao.KANBAN_STAGE_RELEASE_FOR_EXECUTION}')
                      LEFT JOIN $TABLE d
                      ON s.${MD_SiteDao.SITE_CODE} = d.${DESTINATION_SITE_CODE}
                      AND d.${DESTINATION_STATUS} = '${DestinationStatus.ARRIVED.toDescription()}'
                      AND d.${DESTINATION_TYPE} = 'SITE'
                WHERE d.$DESTINATION_SITE_CODE IS NULL
                GROUP BY  s.${MD_SiteDao.SITE_CODE}
        """.trimIndent()
        ).let { query ->

            return query.map { hmAux ->
                DestinationAvailables(
                    destinationType = hmAux["destinationType"],
                    siteCode = hmAux[MD_SiteDao.SITE_CODE].takeIf { !it.isNullOrEmpty() }?.toInt(),
                    siteDesc = hmAux[MD_SiteDao.SITE_DESC],
                    address = 1,
                    countryId = hmAux[MD_SiteDao.COUNTRY_CODE],
                    state = hmAux[MD_SiteDao.STATE],
                    city = hmAux[MD_SiteDao.CITY],
                    district = hmAux[MD_SiteDao.DISTRICT],
                    street = hmAux[MD_SiteDao.STREET],
                    streetnumber = hmAux[MD_SiteDao.NUM],
                    complement = hmAux[MD_SiteDao.COMPLEMENT],
                    zipcode = hmAux[MD_SiteDao.ZIP_CODE],
                    plusCode = hmAux[MD_SiteDao.PLUS_CODE],
                    contactName = hmAux[MD_SiteDao.CONTACT_NAME],
                    contactPhone = hmAux[MD_SiteDao.CONTACT_PHONE],
                    lat = hmAux[MD_SiteDao.LATITUDE].takeIf { !it.isNullOrEmpty() }?.toDouble(),
                    lon = hmAux[MD_SiteDao.LONGITUDE].takeIf { !it.isNullOrEmpty() }?.toDouble(),
                    regionCode = hmAux[MDRegionDao.REGION_CODE].takeIf { !it.isNullOrEmpty() }
                        ?.toInt(),
                    regionDesc = hmAux[MDRegionDao.REGION_DESC],
                    minDate = hmAux["minDate"]?.ifEmpty { null }
                )
            }
        }
    }

    class CursorToFsTripDestination : Mapper<Cursor, FsTripDestination> {
        @SuppressLint("Range")
        override fun map(from: Cursor?): FsTripDestination? {
            from?.let { cursor ->
                with(cursor) {
                    return FsTripDestination(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        tripPrefix = getInt(getColumnIndex(TRIP_PREFIX)),
                        tripCode = getInt(getColumnIndex(TRIP_CODE)),
                        destinationSeq = getInt(getColumnIndex(DESTINATION_SEQ)),
                        destinationType = getStringOrNull(getColumnIndex(DESTINATION_TYPE)),
                        destinationSiteCode = getIntOrNull(getColumnIndex(DESTINATION_SITE_CODE)),
                        destinationSiteDesc = getStringOrNull(getColumnIndex(DESTINATION_SITE_DESC)),
                        destinationRegionCode = getIntOrNull(getColumnIndex(DESTINATION_REGION_CODE)),
                        destinationRegionDesc = getStringOrNull(
                            getColumnIndex(
                                DESTINATION_REGION_DESC
                            )
                        ),
                        ticketPrefix = getIntOrNull(getColumnIndex(TICKET_PREFIX)),
                        ticketCode = getIntOrNull(getColumnIndex(TICKET_CODE)),
                        ticketId = getStringOrNull(getColumnIndex(TICKET_ID)),
                        destinationStatus = getStringOrNull(getColumnIndex(DESTINATION_STATUS)),
                        latitude = getDoubleOrNull(getColumnIndex(LATITUDE)),
                        longitude = getDoubleOrNull(getColumnIndex(LONGITUDE)),
                        arrivedDate = getStringOrNull(getColumnIndex(ARRIVED_DATE)),
                        arrivedLat = getDoubleOrNull(getColumnIndex(ARRIVED_LAT)),
                        arrivedLon = getDoubleOrNull(getColumnIndex(ARRIVED_LON)),
                        arrivedType = getStringOrNull(getColumnIndex(ARRIVED_TYPE)),
                        arrivedFleetOdometer = getLongOrNull(getColumnIndex(ARRIVED_FLEET_ODOMETER)),
                        arrivedFleetPhoto = getStringOrNull(getColumnIndex(ARRIVED_FLEET_PHOTO)),
                        arrivedFleetPhotoLocal = getStringOrNull(
                            getColumnIndex(
                                ARRIVED_FLEET_PHOTO_LOCAL
                            )
                        ),
                        arrivedFleetPhotoName = getStringOrNull(
                            getColumnIndex(
                                ARRIVED_FLEET_PHOTO_NAME
                            )
                        ),
                        arrivedFleetPhotoChanged = getInt(getColumnIndex(ARRIVED_FLEET_PHOTO_CHANGED)),
                        departedDate = getStringOrNull(getColumnIndex(DEPARTED_DATE)),
                        departedLat = getDoubleOrNull(getColumnIndex(DEPARTED_LAT)),
                        departedLon = getDoubleOrNull(getColumnIndex(DEPARTED_LON)),
                        departedType = getStringOrNull(getColumnIndex(DEPARTED_TYPE)),
                        countryId = getStringOrNull(getColumnIndex(COUNTRY_ID)),
                        state = getStringOrNull(getColumnIndex(STATE)),
                        city = getStringOrNull(getColumnIndex(CITY)),
                        district = getStringOrNull(getColumnIndex(DISTRICT)),
                        street = getStringOrNull(getColumnIndex(STREET)),
                        streetnumber = getStringOrNull(getColumnIndex(NUM)),
                        complement = getStringOrNull(getColumnIndex(COMPLEMENT)),
                        zipCode = getStringOrNull(getColumnIndex(ZIP_CODE)),
                        plusCode = getStringOrNull(getColumnIndex(PLUS_CODE)),
                        contactName = getStringOrNull(getColumnIndex(CONTACT_NAME)),
                        contactPhone = getStringOrNull(getColumnIndex(CONTACT_PHONE)),
                        siteMainUser = getIntOrNull(getColumnIndex(SITE_MAIN_USER)),
                        minDate = getStringOrNull(getColumnIndex(MIN_DATE)),
                        serialCnt = getInt(getColumnIndex(SERIAL_CNT)),
                    )
                }
            }

            return null
        }
    }

    class FsTripDestinationToContentValuesMapper : Mapper<FsTripDestination, ContentValues> {
        override fun map(from: FsTripDestination?): ContentValues {
            val contentValues = ContentValues()

            from?.let { destination ->
                with(contentValues) {
                    if (destination.customerCode > -1) put(CUSTOMER_CODE, destination.customerCode)
                    if (destination.tripPrefix > -1) put(TRIP_PREFIX, destination.tripPrefix)
                    if (destination.tripCode > -1) put(TRIP_CODE, destination.tripCode)
                    if (destination.destinationSeq > -1) put(
                        DESTINATION_SEQ,
                        destination.destinationSeq
                    )
                    put(DESTINATION_TYPE, destination.destinationType)
                    put(DESTINATION_SITE_CODE, destination.destinationSiteCode)
                    put(DESTINATION_SITE_DESC, destination.destinationSiteDesc)
                    put(DESTINATION_REGION_CODE, destination.destinationRegionCode)
                    put(DESTINATION_REGION_DESC, destination.destinationRegionDesc)
                    put(TICKET_PREFIX, destination.ticketPrefix)
                    put(TICKET_CODE, destination.ticketCode)
                    put(TICKET_ID, destination.ticketId)
                    put(DESTINATION_STATUS, destination.destinationStatus)
                    put(LATITUDE, destination.latitude)
                    put(LONGITUDE, destination.longitude)
                    put(ARRIVED_DATE, destination.arrivedDate)
                    put(ARRIVED_LAT, destination.arrivedLat)
                    put(ARRIVED_LON, destination.arrivedLon)
                    put(ARRIVED_TYPE, destination.arrivedType)
                    put(ARRIVED_FLEET_ODOMETER, destination.arrivedFleetOdometer)
                    put(ARRIVED_FLEET_PHOTO, destination.arrivedFleetPhoto)
                    put(ARRIVED_FLEET_PHOTO_LOCAL, destination.arrivedFleetPhotoLocal)
                    put(ARRIVED_FLEET_PHOTO_NAME, destination.arrivedFleetPhotoName)
                    put(ARRIVED_FLEET_PHOTO_CHANGED, destination.arrivedFleetPhotoChanged)
                    put(DEPARTED_DATE, destination.departedDate)
                    put(DEPARTED_LAT, destination.departedLat)
                    put(DEPARTED_LON, destination.departedLon)
                    put(DEPARTED_TYPE, destination.departedType)
                    put(COUNTRY_ID, destination.countryId)
                    put(STATE, destination.state)
                    put(CITY, destination.city)
                    put(DISTRICT, destination.district)
                    put(STREET, destination.street)
                    put(NUM, destination.streetnumber)
                    put(COMPLEMENT, destination.complement)
                    put(ZIP_CODE, destination.zipCode)
                    put(PLUS_CODE, destination.plusCode)
                    put(CONTACT_NAME, destination.contactName)
                    put(CONTACT_PHONE, destination.contactPhone)
                    put(SITE_MAIN_USER, destination.siteMainUser)
                    put(MIN_DATE, destination.minDate)
                    if (destination.serialCnt > -1) put(SERIAL_CNT, destination.serialCnt)
                }
            }

            return contentValues
        }

    }


    companion object {

        const val TABLE = "fs_trip_destination"
        const val CUSTOMER_CODE = "customer_code"
        const val TRIP_PREFIX = "trip_prefix"
        const val TRIP_CODE = "trip_code"
        const val DESTINATION_SEQ = "destination_seq"
        const val DESTINATION_TYPE = "destination_type"
        const val DESTINATION_SITE_CODE = "destination_site_code"
        const val DESTINATION_SITE_DESC = "destination_site_desc"
        const val DESTINATION_REGION_CODE = "destination_region_code"
        const val DESTINATION_REGION_DESC = "destination_region_desc"
        const val TICKET_PREFIX = "ticket_prefix"
        const val TICKET_CODE = "ticket_code"
        const val TICKET_ID = "ticket_id"
        const val DESTINATION_STATUS = "destination_status"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ARRIVED_DATE = "arrived_date"
        const val ARRIVED_LAT = "arrived_lat"
        const val ARRIVED_LON = "arrived_lon"
        const val ARRIVED_TYPE = "arrived_type"
        const val ARRIVED_FLEET_ODOMETER = "arrived_fleet_odometer"
        const val ARRIVED_FLEET_PHOTO = "arrived_fleet_photo"
        const val ARRIVED_FLEET_PHOTO_LOCAL = "arrived_fleet_photo_local"
        const val ARRIVED_FLEET_PHOTO_NAME = "arrived_fleet_photo_name"
        const val ARRIVED_FLEET_PHOTO_CHANGED = "arrived_fleet_photo_changed"
        const val DEPARTED_DATE = "departed_date"
        const val DEPARTED_LAT = "departed_lat"
        const val DEPARTED_LON = "departed_lon"
        const val DEPARTED_TYPE = "departed_type"
        const val COUNTRY_ID = "country_id"
        const val STATE = "state"
        const val CITY = "city"
        const val DISTRICT = "district"
        const val STREET = "street"
        const val NUM = "num"
        const val COMPLEMENT = "complement"
        const val ZIP_CODE = "zip_code"
        const val PLUS_CODE = "plus_code"
        const val CONTACT_NAME = "contact_name"
        const val CONTACT_PHONE = "contact_phone"
        const val SITE_MAIN_USER = "site_main_user"
        const val MIN_DATE = "min_date"
        const val SERIAL_CNT = "serial_cnt"

        fun instance(context: Context) = FsTripDestinationDao(context)

    }
}