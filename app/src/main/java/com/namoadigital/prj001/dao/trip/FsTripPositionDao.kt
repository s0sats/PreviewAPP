package com.namoadigital.prj001.dao.trip

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.DaoWithReturn
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.FsTripPosition
import com.namoadigital.prj001.sql.trip.PositionUpdateRequired
import com.namoadigital.prj001.sql.trip.PositionGetListUpdateRequired
import com.namoadigital.prj001.sql.trip.PositionGetMax
import com.namoadigital.prj001.sql.trip.PositionUpdateToken
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import javax.inject.Inject

class FsTripPositionDao @Inject constructor(
    val context: Context
) : BaseDao(
    context,
    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    Constant.DB_VERSION_CUSTOM,
    Constant.DB_MODE_MULTI
), DaoWithReturn<FsTripPosition> {
    private val toFsTripPosition: Mapper<Cursor, FsTripPosition>
    private val toContentValuesMapper: Mapper<FsTripPosition, ContentValues>

    init {
        toFsTripPosition = CursorToFsTripPosition()
        toContentValuesMapper = FsTripPositionToContentValuesMapper()
    }

    override fun addUpdate(fsTripPosition: FsTripPosition?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(fsTripPosition)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(
                TABLE,
                toContentValuesMapper.map(fsTripPosition),
                sbWhere.toString(),
                null
            ).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(
                    TABLE,
                    null,
                    toContentValuesMapper.map(fsTripPosition)
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

    override fun addUpdate(items: MutableList<FsTripPosition>?, status: Boolean): DaoObjReturn {
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
            items?.forEach { fsTripPosition ->
                curAction = DaoObjReturn.UPDATE
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(fsTripPosition)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(
                    TABLE,
                    toContentValuesMapper.map(fsTripPosition),
                    sbWhere.toString(),
                    null
                ).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(
                        TABLE,
                        null,
                        toContentValuesMapper.map(fsTripPosition)
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

    fun getByString(sQuery: String?, dbInstance: SQLiteDatabase?): FsTripPosition? {
        var fsTripPosition: FsTripPosition? = null
        if (dbInstance == null) {
            openDB()
        } else {
            this.db = dbInstance
        }
        lateinit var cursor: Cursor
        try {
            cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                fsTripPosition = toFsTripPosition.map(cursor)
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
        return fsTripPosition
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

    override fun query(sQuery: String?): MutableList<FsTripPosition> {
        var items = mutableListOf<FsTripPosition>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux: FsTripPosition = toFsTripPosition.map(cursor)
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
    private fun getWherePkClause(fsTripPosition: FsTripPosition?): StringBuilder {
        fsTripPosition?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        $CUSTOMER_CODE = '${it.customerCode}'  
                        AND $TRIP_PREFIX = '${it.tripPrefix}'
                        AND $TRIP_CODE = '${it.tripCode}'
                        AND $TRIP_POSITION_SEQ = '${it.tripPositionSeq}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }


    class CursorToFsTripPosition : Mapper<Cursor, FsTripPosition> {
        @SuppressLint("Range")
        override fun map(from: Cursor?): FsTripPosition? {
            from?.let { cursor ->
                with(cursor) {
                    return FsTripPosition(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        tripPrefix = getInt(getColumnIndex(TRIP_PREFIX)),
                        tripCode = getInt(getColumnIndex(TRIP_CODE)),
                        tripPositionSeq = getInt(getColumnIndex(TRIP_POSITION_SEQ)),
                        tripDestinationSeq = getIntOrNull(getColumnIndex(TRIP_DESTINATION_SEQ)),
                        tripPositionLat = getDoubleOrNull(getColumnIndex(TRIP_POSITION_LAT)),
                        tripPositionLon = getDoubleOrNull(getColumnIndex(TRIP_POSITION_LON)),
                        tripPositionDate = getStringOrNull(getColumnIndex(TRIP_POSITION_DATE)),
                        tripPositionAlertType = getStringOrNull(
                            getColumnIndex(
                                TRIP_POSITION_ALERT_TYPE
                            )
                        ),
                        updateRequired = getInt(getColumnIndex(UPDATE_REQUIRED)),
                        tripPositionSpeed = getDoubleOrNull(getColumnIndex(TRIP_POSITION_SPEED)),
                        tripPositionDistance = getDoubleOrNull(getColumnIndex(TRIP_POSITION_DISTANCE)),
                        isRef = getInt(getColumnIndex(IS_REF)),
                        token = getStringOrNull(getColumnIndex(TOKEN))
                    )
                }
            }

            return null
        }
    }

    class FsTripPositionToContentValuesMapper : Mapper<FsTripPosition, ContentValues> {
        override fun map(from: FsTripPosition?): ContentValues {
            val contentValues = ContentValues()

            from?.let { position ->
                with(contentValues) {
                    if (position.customerCode > -1) put(CUSTOMER_CODE, position.customerCode)
                    if (position.tripPrefix > -1) put(TRIP_PREFIX, position.tripPrefix)
                    if (position.tripCode > -1) put(TRIP_CODE, position.tripCode)
                    if (position.tripPositionSeq > -1) put(
                        TRIP_POSITION_SEQ,
                        position.tripPositionSeq
                    )
                    put(TRIP_DESTINATION_SEQ, position.tripDestinationSeq)
                    put(TRIP_POSITION_LAT, position.tripPositionLat)
                    put(TRIP_POSITION_LON, position.tripPositionLon)
                    put(TRIP_POSITION_DATE, position.tripPositionDate)
                    put(TRIP_POSITION_ALERT_TYPE, position.tripPositionAlertType)
                    if (position.updateRequired > -1) {
                        put(UPDATE_REQUIRED, position.updateRequired)
                    }
                    put(TRIP_POSITION_DISTANCE, position.tripPositionDistance)
                    put(TOKEN, position.token)
                    if (position.isRef > -1) {
                        put(IS_REF, position.isRef)
                    }
                    put(TRIP_POSITION_SPEED, position.tripPositionSpeed)
                }
            }

            return contentValues
        }

    }


    companion object {

        const val TABLE = "fs_trip_position"
        const val CUSTOMER_CODE = "customer_code"
        const val TRIP_PREFIX = "trip_prefix"
        const val TRIP_CODE = "trip_code"
        const val TRIP_POSITION_SEQ = "trip_position_seq"
        const val TRIP_DESTINATION_SEQ = "trip_destination_seq"
        const val TRIP_POSITION_LAT = "trip_position_lat"
        const val TRIP_POSITION_LON = "trip_position_lon"
        const val TRIP_POSITION_DATE = "trip_position_date"
        const val TRIP_POSITION_ALERT_TYPE = "trip_position_alert_type"
        const val UPDATE_REQUIRED = "update_required"
        const val TRIP_POSITION_SPEED = "trip_position_speed"
        const val TRIP_POSITION_DISTANCE = "trip_position_distance"
        const val IS_REF = "is_ref"
        const val TOKEN = "token"

    }


    //--------------------------------------------------------------------------------------------\\

    fun delete() = query(
        """DELETE FROM $TABLE
            WHERE $UPDATE_REQUIRED = 0
        """.trimMargin()
    )

    fun getPositionSeq(): Int {
        val value = getByStringHM(PositionGetMax().toSqlQuery())
        return value?.get(TRIP_POSITION_SEQ)?.toInt() ?: 0
    }

    fun getAllUpdateRequired(): List<FsTripPosition> {
        return query(PositionGetListUpdateRequired.toSqlQuery())
    }

    fun getAllUpdateRequiredTokenNull(): List<FsTripPosition> {
        return query(PositionGetListUpdateRequired.toSqlQueryWithoutToken())
    }

    fun getAllUpdateRequiredWithToken(): List<FsTripPosition> {
        return query(PositionGetListUpdateRequired.toSqlQueryWithToken())
    }

    fun updateRequired(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        seq: Int,
        updateRequired: Int,
    ) {
        addUpdate(
            PositionUpdateRequired(
                customerCode = customerCode,
                tripPrefix = tripPrefix,
                tripCode = tripCode,
                tripPositionSeq = seq,
                updateRequired = updateRequired
            ).toSqlQuery()
        )
    }

    fun updateToken(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        seq: Int,
        newToken: String
    ) {
        addUpdate(
            PositionUpdateToken(
                customerCode = customerCode,
                tripPrefix = tripPrefix,
                tripCode = tripCode,
                tripPositionSeq = seq,
                token = newToken
            ).toSqlQuery()
        )
    }

    fun getLastAlertType(): String? {
        val value = getByStringHM(
            """
             SELECT $TRIP_POSITION_ALERT_TYPE
             FROM $TABLE
             WHERE $UPDATE_REQUIRED = 0
             ORDER BY $TRIP_POSITION_SEQ DESC LIMIT 1
        """.trimIndent()
        )
        return value?.get(TRIP_POSITION_ALERT_TYPE)
    }

    fun getLastDateSave(): String? {
        val value = getByStringHM(
            """
            SELECT $TRIP_POSITION_DATE
            FROM $TABLE
            WHERE $UPDATE_REQUIRED = 0
            ORDER BY $TRIP_POSITION_SEQ DESC LIMIT 1
        """.trimIndent()
        )
        return value?.get(TRIP_POSITION_DATE)
    }

    fun setLastPositionAsReference() {
        val lastPosition = getByString(
            """SELECT * FROM $TABLE ORDER BY rowid DESC LIMIT 1;""".trimIndent()
        )

        lastPosition?.let {
            it.isRef = 1
            addUpdate(it)
        }
    }

//    fun getLastPosition(): Coordinates? {
//        val value = getByStringHM("""
//            SELECT $TRIP_POSITION_LAT, $TRIP_POSITION_LON, $TRIP_POSITION_DATE
//            FROM $TABLE
//            WHERE $UPDATE_REQUIRED = 0
//            ORDER BY $TRIP_POSITION_SEQ DESC LIMIT 1
//        """.trimIndent())
//        return if(value?.get(TRIP_POSITION_LAT).isNullOrEmpty() &&
//            value?.get(TRIP_POSITION_LON).isNullOrEmpty() &&
//            value?.get(TRIP_POSITION_DATE).isNullOrEmpty()) null
//        else Coordinates(value?.get(TRIP_POSITION_LAT)!!.toDouble(), value[TRIP_POSITION_LON]!!.toDouble(), value[TRIP_POSITION_DATE])
//    }
}