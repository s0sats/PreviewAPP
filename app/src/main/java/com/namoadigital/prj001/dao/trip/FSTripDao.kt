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
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.DaoWithReturn
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.extensions.isValidPDF
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.sql.FsTripDestinationActionSqlDelete
import com.namoadigital.prj001.sql.FsTripDestinationSqlDelete
import com.namoadigital.prj001.sql.FsTripEventSqlDelete
import com.namoadigital.prj001.sql.FsTripPositionSqlPreviosTripDelete
import com.namoadigital.prj001.sql.FsTripSqlDelete
import com.namoadigital.prj001.sql.FsTripUserSqlDelete
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflictType
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File
import javax.inject.Inject

class FSTripDao @Inject constructor(
    val context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<FSTrip> {

    constructor(
        context: Context
    ) : this(
        context = context,
        mDB_NAME = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
        mDB_VERSION = Constant.DB_VERSION_CUSTOM
    )

    companion object {
        const val TABLE = "fs_trip"
        const val CUSTOMER_CODE = "customer_code"
        const val TRIP_PREFIX = "trip_prefix"
        const val TRIP_CODE = "trip_code"
        const val TRIP_USER_CODE = "trip_user_code"
        const val TRIP_USER_NAME = "trip_user_name"
        const val TRIP_STATUS = "trip_status"
        const val FLEET_LICENCE_PLATE = "fleet_licence_plate"
        const val FLEET_START_ODOMETER = "fleet_start_odometer"
        const val FLEET_START_PHOTO = "fleet_start_photo"
        const val FLEET_START_PHOTO_LOCAL = "fleet_start_photo_local"
        const val FLEET_START_PHOTO_NAME = "fleet_start_photo_name"
        const val FLEET_START_PHOTO_CHANGED = "fleet_start_photo_changed"
        const val FLEET_END_ODOMETER = "fleet_end_odometer"
        const val FLEET_END_PHOTO = "fleet_end_photo"
        const val FLEET_END_PHOTO_LOCAL = "fleet_end_photo_local"
        const val FLEET_END_PHOTO_NAME = "fleet_end_photo_name"
        const val FLEET_END_PHOTO_CHANGED = "fleet_end_photo_changed"
        const val ORIGIN_TYPE = "origin_type"
        const val ORIGIN_SITE_CODE = "origin_site_code"
        const val ORIGIN_SITE_DESC = "origin_site_desc"
        const val ORIGIN_LAT = "origin_lat"
        const val ORIGIN_LON = "origin_lon"
        const val POSITION_LAT = "position_lat"
        const val POSITION_LON = "position_lon"
        const val POSITION_DISTANCE_MIN = "position_distance_min"
        const val REQUIRE_DESTINATION_FLEET_DATA = "require_destination_fleet_data"
        const val POSITION_DATE = "position_date"
        const val SCN = "scn"
        const val SYNC_REQUIRED = "sync_required"
        const val REQUIRE_FLEET_DATA = "require_fleet_data"
        const val DISTANCE_REF_MINUTES = "distance_ref_minutes"
        const val DISTANCE_REF_MINUTES_TRANS = "distance_ref_minutes_trans"
        const val ORIGIN_DATE = "origin_date"
        const val START_DATE = "start_date"
        const val DONE_DATE = "done_date"
        const val UPDATE_REQUIRED = "update_required"
    }

    private val toCursorToFSTripEventTypeMapperMapper: Mapper<Cursor, FSTrip>
    private val toContentValuesMapper: Mapper<FSTrip, ContentValues>

    init {
        toCursorToFSTripEventTypeMapperMapper = CursorToFSTripMapper()
        toContentValuesMapper = FSTripToContentValuesMapper()
    }

    override fun addUpdate(fsTrip: FSTrip?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(fsTrip)
            //Tenta update e armazena retorno
            addUpdateRet =
                db.update(TABLE, toContentValuesMapper.map(fsTrip), sbWhere.toString(), null)
                    .toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(fsTrip))
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

    fun syncTripFull(fsTrip: FSTrip): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()
        db.beginTransaction()
        try {
            //
            fsTrip.setPk()
            val fsTripUserDao = FSTripUserDao(context)
            val fsTripDestinationDao = FsTripDestinationDao(context)
            val fsTripEventDao = FSTripEventDao(context)
            val fsTripDestinationActionDao = FsTripDestinationActionDao(context)
            val fsTripPositionDao = FsTripPositionDao(context)
            //
            handlePdfChanges(fsTrip, fsTripDestinationActionDao)
            //
            if (remove(
                    FsTripSqlDelete(
                        fsTrip.customerCode,
                        fsTrip.tripPrefix,
                        fsTrip.tripCode
                    ).toSqlQuery(), db
                ).hasError()
            ) {
                throw Exception("Remove trip error")
            }
            if (fsTripUserDao.remove(
                    FsTripUserSqlDelete(
                        fsTrip.customerCode,
                        fsTrip.tripPrefix,
                        fsTrip.tripCode
                    ).toSqlQuery(), db
                ).hasError()
            ) {
                throw Exception("Remove trip error")
            }
            if (fsTripEventDao.remove(
                    FsTripEventSqlDelete(
                        fsTrip.customerCode,
                        fsTrip.tripPrefix,
                        fsTrip.tripCode
                    ).toSqlQuery(), db
                ).hasError()
            ) {
                throw Exception("Remove trip error")
            }
            if (fsTripDestinationDao.remove(
                    FsTripDestinationSqlDelete(
                        fsTrip.customerCode,
                        fsTrip.tripPrefix,
                        fsTrip.tripCode
                    ).toSqlQuery(), db
                ).hasError()
            ) {
                throw Exception("Remove trip error")
            }
            if (fsTripDestinationActionDao.remove(
                    FsTripDestinationActionSqlDelete(
                        fsTrip.customerCode,
                        fsTrip.tripPrefix,
                        fsTrip.tripCode
                    ).toSqlQuery(), db
                ).hasError()
            ) {
                throw Exception("Remove trip error")
            }
            if (fsTripPositionDao.remove(
                    FsTripPositionSqlPreviosTripDelete(
                        fsTrip.customerCode,
                        fsTrip.tripPrefix,
                        fsTrip.tripCode
                    ).toSqlQuery(), db
                ).hasError()
            ) {
                throw Exception("Remove trip error")
            }
            fsTrip.fleetStartPhotoLocal = checkFileLocalExists(fsTrip.fleetStartPhotoName)
            //
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(fsTrip)
            //Tenta update e armazena retorno
            addUpdateRet =
                db.update(TABLE, toContentValuesMapper.map(fsTrip), sbWhere.toString(), null)
                    .toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(fsTrip))
            }

            fsTrip.let {
                //FSTripUserDao
                it.users?.forEach { user ->
                    fsTripUserDao.addUpdate(user, db)
                }
                //FsTripDestinationDao
                it.destinations?.forEach { destination ->
                    destination.arrivedFleetPhotoLocal =
                        checkFileLocalExists(destination.arrivedFleetPhotoName)
                    fsTripDestinationDao.addUpdate(destination, db)
                }
                //FSTripEventDao
                it.events?.forEach { event ->
                    event.eventPhotoChanged = 0
                    event.photoLocal = checkFileLocalExists(event.photoName)
                    fsTripEventDao.addUpdate(event, db)
                }
            }
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

    private fun checkFileLocalExists(photoName: String?): String? {
        if (!photoName.isNullOrBlank()) {
            val fileLocal = File(ConstantBaseApp.CACHE_PATH_PHOTO, photoName)
            if (fileLocal.exists()
                && fileLocal.isFile
            ) {
                return photoName
            }
        }
        return null
    }

    private fun handlePdfChanges(
        fsTrip: FSTrip,
        fsTripDestinationActionDao: FsTripDestinationActionDao
    ) {

        fsTrip.destinations?.forEach { remoteDestination ->
            remoteDestination.actions?.forEach { remoteAction ->
                val file = remoteAction.actPDFName?.let { remoteName ->
                    File(
                        ConstantBaseApp.CACHE_PATH,
                        ConstantBaseApp.N_FORM_PDF_PREFIX + remoteName
                    )
                }
                file?.let { f ->
                    Log.d("TRIP_FILE", "-------handlePdfChanges------")
                    Log.d("TRIP_FILE", "f.path(): " + f.path)
                    Log.d("TRIP_FILE", "f.exists(): " + f.exists())
                    Log.d("TRIP_FILE", "isValidPDF(): " + isValidPDF(f))
                    if (f.exists()
                        && isValidPDF(f)
                    ) {
                        remoteAction.actPDFLocal =
                            ConstantBaseApp.N_FORM_PDF_PREFIX + remoteAction.actPDFName
                    } else {
                        val action = fsTripDestinationActionDao.getAction(
                            remoteAction.customerCode,
                            remoteAction.tripPrefix,
                            remoteAction.tripCode,
                            remoteAction.destinationSeq,
                            remoteAction.customFormType ?: -1,
                            remoteAction.customFormCode ?: -1,
                            remoteAction.customFormVersion ?: -1,
                            remoteAction.customFormData ?: -1,
                        )
                        //
                        action?.let { action ->
                            action.actPDFLocal?.let { localFile ->
                                Log.d(
                                    "TRIP_FILE",
                                    "remoteAction.actPDFName != action.actPDFName " + (remoteAction.actPDFName != action.actPDFName)
                                )
                                if (localFile.contains("TEMP", true)
                                    && remoteAction.actPDFName != action.actPDFName
                                ) {
                                    val local = File(ConstantBaseApp.CACHE_PATH, localFile)
                                    if (!isValidPDF(local)) {
                                        ToolBox_Inf.deleteFileListExceptionSafe(
                                            Constant.CACHE_PATH,
                                            action.actPDFName
                                        )
                                    } else {
                                        remoteAction.actPDFName?.let {
                                            local.renameTo(File(ConstantBaseApp.CACHE_PATH, it))
                                            remoteAction.actPDFLocal = remoteAction.actPDFName
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(fsTrip: FSTrip?): StringBuilder {
        fsTrip?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        $CUSTOMER_CODE = '${fsTrip.customerCode}'  
                        AND $TRIP_PREFIX = '${fsTrip.tripPrefix}'
                        AND $TRIP_CODE = '${fsTrip.tripCode}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(fsTrips: MutableList<FSTrip>?, status: Boolean): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE

            db.beginTransaction()

            if (status) {
                db.delete(TABLE, null, null)
            }

            fsTrips?.forEach { fsEventType ->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(fsEventType)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(
                    TABLE,
                    toContentValuesMapper.map(fsEventType),
                    sbWhere.toString(),
                    null
                ).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(fsEventType))
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
            ToolBox_Inf.registerException(javaClass.name, e)
            daoObjReturn.setError(true)
        } finally {
        }

        if (dbInstance == null) {
            closeDB()
        }

        return daoObjReturn
    }

    override fun addUpdate(sQuery: String?) {
        addUpdate(sQuery, null)
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

    override fun getByString(sQuery: String): FSTrip? {
        var fsTrip: FSTrip? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                fsTrip = toCursorToFSTripEventTypeMapperMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return fsTrip
    }

    override fun getByStringHM(sQuery: String): HMAux? {
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

    override fun query(sQuery: String?): MutableList<FSTrip> {
        val fsTrips = mutableListOf<FSTrip>()
        openDB()

        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux = toCursorToFSTripEventTypeMapperMapper.map(cursor)
                fsTrips.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return fsTrips
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val fsEventTypes: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                fsEventTypes.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return fsEventTypes
    }

    fun getOrigin(): Coordinates? {
        val trip = getTrip()
        trip?.let {
            return it.originCoordinates
        }
        return null
    }


    private class FSTripToContentValuesMapper :
        Mapper<FSTrip, ContentValues> {
        override fun map(data: FSTrip?): ContentValues {
            val contentValues = ContentValues()
            //
            data?.let {
                with(contentValues) {
                    if (data.customerCode > -1) put(CUSTOMER_CODE, data.customerCode)
                    if (data.tripPrefix > -1) put(TRIP_PREFIX, data.tripPrefix)
                    if (data.tripCode > -1) put(TRIP_CODE, data.tripCode)
                    if (data.tripUserCode > -1) put(TRIP_USER_CODE, data.tripUserCode)
                    put(TRIP_USER_NAME, data.tripUserName)
                    put(TRIP_STATUS, data.tripStatus)
                    put(FLEET_LICENCE_PLATE, data.fleetLicencePlate)
                    put(FLEET_START_ODOMETER, data.fleetStartOdometer)
                    put(FLEET_START_PHOTO, data.fleetStartPhoto)
                    put(FLEET_START_PHOTO_NAME, data.fleetStartPhotoName)
                    if (data.fleetStartPhotoChanged > -1) put(
                        FLEET_START_PHOTO_CHANGED,
                        data.fleetStartPhotoChanged
                    )
                    put(FLEET_START_PHOTO_LOCAL, data.fleetStartPhotoLocal)
                    put(FLEET_END_ODOMETER, data.fleetEndOdometer)
                    put(FLEET_END_PHOTO, data.fleetEndPhoto)
                    put(FLEET_END_PHOTO_NAME, data.fleetEndPhotoName)
                    if (data.fleetEndPhotoChanged > -1) put(
                        FLEET_END_PHOTO_CHANGED,
                        data.fleetEndPhotoChanged
                    )
                    put(FLEET_END_PHOTO_LOCAL, data.fleetEndPhotoLocal)
                    put(ORIGIN_TYPE, data.originType)
                    put(ORIGIN_SITE_CODE, data.originSiteCode)
                    put(ORIGIN_SITE_DESC, data.originSiteDesc)
                    put(ORIGIN_LAT, data.originLat)
                    put(ORIGIN_LON, data.originLon)
                    put(POSITION_LAT, data.positionLat)
                    put(POSITION_LON, data.positionLon)
                    put(POSITION_DISTANCE_MIN, data.positionDistanceMin)
                    if (data.requireDestinationFleetData > -1) put(
                        REQUIRE_DESTINATION_FLEET_DATA,
                        data.requireDestinationFleetData
                    )
                    put(POSITION_DATE, data.positionDate)
                    if (data.scn > -1) put(SCN, data.scn)
                    if (data.syncRequired > -1) put(SYNC_REQUIRED, data.syncRequired)
                    if (data.distanceRefMinutes > -1) put(
                        DISTANCE_REF_MINUTES,
                        data.distanceRefMinutes
                    )
                    if (data.distanceRefMinutesTrans > -1) put(
                        DISTANCE_REF_MINUTES_TRANS,
                        data.distanceRefMinutesTrans
                    )
                    put(REQUIRE_FLEET_DATA, data.requireFleetData)
                    put(ORIGIN_DATE, data.originDate)
                    put(START_DATE, data.startDate)
                    put(DONE_DATE, data.doneDate)
                    if (data.updateRequired > -1) put(UPDATE_REQUIRED, data.updateRequired)

                }
            }
            //
            return contentValues
        }
    }

    private class CursorToFSTripMapper : Mapper<Cursor, FSTrip> {
        @SuppressLint("Range")
        override fun map(cursor: Cursor?): FSTrip? {
            cursor?.let {
                with(cursor) {
                    return FSTrip(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        tripPrefix = getInt(getColumnIndex(TRIP_PREFIX)),
                        tripCode = getInt(getColumnIndex(TRIP_CODE)),
                        tripUserCode = getInt(getColumnIndex(TRIP_USER_CODE)),
                        tripUserName = getString(getColumnIndex(TRIP_USER_NAME)),
                        tripStatus = getString(getColumnIndex(TRIP_STATUS)),
                        fleetLicencePlate = getString(getColumnIndex(FLEET_LICENCE_PLATE)),
                        fleetStartOdometer = getLongOrNull(getColumnIndex(FLEET_START_ODOMETER)),
                        fleetStartPhoto = getStringOrNull(getColumnIndex(FLEET_START_PHOTO)),
                        fleetStartPhotoName = getStringOrNull(getColumnIndex(FLEET_START_PHOTO_NAME)),
                        fleetStartPhotoChanged = getInt(getColumnIndex(FLEET_START_PHOTO_CHANGED)),
                        fleetStartPhotoLocal = getStringOrNull(
                            getColumnIndex(
                                FLEET_START_PHOTO_LOCAL
                            )
                        ),
                        fleetEndOdometer = getLongOrNull(getColumnIndex(FLEET_END_ODOMETER)),
                        fleetEndPhoto = getStringOrNull(getColumnIndex(FLEET_END_PHOTO)),
                        fleetEndPhotoName = getStringOrNull(getColumnIndex(FLEET_END_PHOTO_NAME)),
                        fleetEndPhotoChanged = getInt(getColumnIndex(FLEET_END_PHOTO_CHANGED)),
                        fleetEndPhotoLocal = getStringOrNull(getColumnIndex(FLEET_END_PHOTO_LOCAL)),
                        originType = getStringOrNull(getColumnIndex(ORIGIN_TYPE)),
                        originSiteCode = getIntOrNull(getColumnIndex(ORIGIN_SITE_CODE)),
                        originSiteDesc = getStringOrNull(getColumnIndex(ORIGIN_SITE_DESC)),
                        originLat = getDoubleOrNull(getColumnIndex(ORIGIN_LAT)),
                        originLon = getDoubleOrNull(getColumnIndex(ORIGIN_LON)),
                        positionLat = getDoubleOrNull(getColumnIndex(POSITION_LAT)),
                        positionLon = getDoubleOrNull(getColumnIndex(POSITION_LON)),
                        positionDistanceMin = getDoubleOrNull(getColumnIndex(POSITION_DISTANCE_MIN)),
                        requireDestinationFleetData = getInt(
                            getColumnIndex(
                                REQUIRE_DESTINATION_FLEET_DATA
                            )
                        ),
                        positionDate = getStringOrNull(getColumnIndex(POSITION_DATE)),
                        syncRequired = getInt(getColumnIndex(SYNC_REQUIRED)),
                        scn = getInt(getColumnIndex(SCN)),
                        distanceRefMinutes = getInt(getColumnIndex(DISTANCE_REF_MINUTES)),
                        distanceRefMinutesTrans = getInt(getColumnIndex(DISTANCE_REF_MINUTES_TRANS)),
                        requireFleetData = getInt(getColumnIndex(REQUIRE_FLEET_DATA)),
                        originDate = getStringOrNull(getColumnIndex(ORIGIN_DATE)),
                        startDate = getStringOrNull(getColumnIndex(START_DATE)),
                        doneDate = getStringOrNull(getColumnIndex(DONE_DATE)),
                        updateRequired = getInt(getColumnIndex(UPDATE_REQUIRED))
                    )
                }
            }
            //
            return null
        }
    }

    //-----------------------------------------------------------------------------------\\
    fun getTrip(): FSTrip? {
        val value = query(
            """
            SELECT * 
              FROM $TABLE 
             WHERE $TRIP_STATUS 
                   in (
                     '${TripStatus.PENDING.toDescription()}',
                     '${TripStatus.TRANSIT.toDescription()}',
                     '${TripStatus.TRANSFER.toDescription()}',
                     '${TripStatus.ON_SITE.toDescription()}',
                     '${TripStatus.OVER_NIGHT.toDescription()}',
                     '${TripStatus.WAITING_DESTINATION.toDescription()}' 
                    )
        """.trimIndent()
        )
        if (value.isNotEmpty()) return value[0]
        return null
    }

    fun updateScn(tripPrefix: Int, tripCode: Int, scn: Int) {
        addUpdate(
            """
            UPDATE $TABLE 
            SET $SCN = $scn
            WHERE $TRIP_PREFIX = $tripPrefix 
            AND $TRIP_CODE = $tripCode
           
        """.trimIndent()
        )
    }

    @Throws(Exception::class)
    fun updateScn(tripPrefix: Int, tripCode: Int, scn: Int, db: SQLiteDatabase) {
        db.execSQL(
            """
            UPDATE $TABLE 
            SET $SCN = $scn
            WHERE $TRIP_PREFIX = $tripPrefix 
            AND $TRIP_CODE = $tripCode 
        """.trimIndent()
        )
    }

    @Throws(SQLiteException::class)
    fun updateFleet(
        tripPrefix: Int,
        tripCode: Int,
        licensePlate: String?,
        odometer: Long?,
        target: TripTarget?,
        photoPath: String?,
        photoChanged: Int,
        db: SQLiteDatabase,
    ) {

        val imageQuery = if (target == TripTarget.START) {
            if (!photoPath.isNullOrBlank()) {
                "$FLEET_START_PHOTO_LOCAL = '$photoPath'"
            } else {
                "$FLEET_START_PHOTO_LOCAL = null"
            }
        } else {
            if (!photoPath.isNullOrBlank()) {
                "$FLEET_END_PHOTO_LOCAL = '$photoPath'"
            } else {
                "$FLEET_END_PHOTO_LOCAL = null"
            }
        }
        //
        val photoName = if (target == TripTarget.START) {
            if (!photoPath.isNullOrBlank()) {
                "$FLEET_START_PHOTO_NAME = '$photoPath'"
            } else {
                "$FLEET_START_PHOTO_NAME = null, $FLEET_START_PHOTO = null"
            }
        } else {
            if (!photoPath.isNullOrBlank()) {
                "$FLEET_END_PHOTO_NAME = '$photoPath'"
            } else {
                "$FLEET_END_PHOTO_NAME = null, $FLEET_END_PHOTO = null"
            }
        }
        //
        val odometerQuery =
            if (target == TripTarget.START) {
                "$FLEET_START_ODOMETER = $odometer"
            } else {
                "$FLEET_END_ODOMETER = $odometer"
            }

        val photoChanged = when (target) {
            TripTarget.START -> {
                "$FLEET_START_PHOTO_CHANGED = $photoChanged"
            }

            TripTarget.END -> {
                "$FLEET_END_PHOTO_CHANGED = $photoChanged"
            }

            else -> {
                ""
            }
        }


        addUpdate(
            """
                UPDATE $TABLE
                SET $FLEET_LICENCE_PLATE = '$licensePlate',
                $odometerQuery,
                $imageQuery,
                $photoName,
                $photoChanged
                WHERE $TRIP_PREFIX = $tripPrefix
                AND $TRIP_CODE = $tripCode
            """.trimIndent(),
            db
        ).let {
            if (it.hasError()) throw SQLiteException(it.errorMsg)
        }
    }

    fun updateStatus(
        tripPrefix: Int,
        tripCode: Int,
        tripScn: Int,
        status: String,
        dbInstance: SQLiteDatabase? = null,
        updateRequired: Int = 0,
        startDate: String? = null,
        doneDate: String? = null
    ): DaoObjReturn {

        var date = ""

        if (doneDate != null) {
            date = ", $DONE_DATE = '$doneDate' "
        }

        if (startDate != null) {
            date += ", $START_DATE = '$startDate' "
        }


        return queryForUpdate(
            """
                UPDATE $TABLE
                SET $TRIP_STATUS = '$status',
                $SCN = $tripScn,
                $UPDATE_REQUIRED = $updateRequired
                $date
                WHERE $TRIP_PREFIX = $tripPrefix
                AND $TRIP_CODE = $tripCode
            """.trimIndent(),
            dbInstance
        )
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


    fun addTripDestination(
        fsTripDestination: FsTripDestination,
        scn: Int,
        tripStatus: String,
        isOnline: Int
    ): Boolean {
        val trip = getTrip()
        val updateRequired = if (isOnline == 1 || (trip?.hasUpdateRequired == true)) {
            1
        } else {
            0
        }
        val destinationDao = FsTripDestinationDao(context)
        return trip?.let {
            var isSuccessfull = true
            openDB()
            try {
                db.beginTransaction()
                updateStatus(it.tripPrefix, it.tripCode, scn, tripStatus, db, updateRequired)
                val addUpdate = destinationDao.addUpdate(fsTripDestination, db)
                if (!addUpdate.hasError()) {
                    db.setTransactionSuccessful()
                } else {
                    isSuccessfull = false
                }
            } catch (e: Exception) {
                ToolBox.registerException(
                    javaClass.name,
                    e
                )
                isSuccessfull = false
            } finally {
                db.endTransaction()
            }
            closeDB()
            //
            isSuccessfull
        } ?: false

    }

    @Throws(SQLiteException::class)
    fun updateOrigin(
        tripPrefix: Int,
        tripCode: Int,
        originDate: String,
        originType: String,
        originSiteCode: Int?,
        siteDesc: String?,
        lat: Double?,
        lon: Double?,
        db: SQLiteDatabase
    ) {
        addUpdate(
            """
                UPDATE $TABLE
                SET $ORIGIN_DATE = '$originDate',
                 $ORIGIN_TYPE = '$originType',
                 $ORIGIN_SITE_CODE = $originSiteCode,
                 $ORIGIN_SITE_DESC = '${siteDesc ?: ""}',
                 $ORIGIN_LAT = '$lat',
                 $ORIGIN_LON = '$lon'
                WHERE $TRIP_PREFIX = $tripPrefix AND $TRIP_CODE = $tripCode
            """.trimIndent(),
            db
        ).also {
            if (it.hasError()) throw SQLiteException(it.errorMsg)
        }
    }

    @Throws(SQLiteException::class)
    fun updateOriginDate(
        tripPrefix: Int,
        tripCode: Int,
        originDate: String,
        db: SQLiteDatabase
    ) {
        addUpdate(
            """
                UPDATE $TABLE
                SET $ORIGIN_DATE = '$originDate'
                WHERE $TRIP_PREFIX = $tripPrefix AND $TRIP_CODE = $tripCode
            """.trimIndent(),
            db
        ).also {
            if (it.hasError()) throw SQLiteException(it.errorMsg)
        }
    }

    @Throws(SQLiteException::class)
    fun setSyncRequired() {
        val trip = getTrip()
        trip?.let {
            if (it.updateRequired == 0) {
                it.syncRequired = 1
                addUpdate(trip).also { daoObj ->
                    if (daoObj.hasError()) throw SQLiteException(daoObj.errorMsg)
                }
            }
        }
    }

    fun getTripByDestination(destinationSeq: Int): FSTrip? {
        val query = query(
            """
                SELECT t.* FROM $TABLE t
                       LEFT JOIN ${FsTripDestinationDao.TABLE} d
                       ON t.$TRIP_PREFIX = d.${FsTripDestinationDao.TRIP_PREFIX}
                       AND t.$TRIP_CODE = d.${FsTripDestinationDao.TRIP_CODE}
                WHERE d.${FsTripDestinationDao.DESTINATION_SEQ} = $destinationSeq
            """.trimIndent()
        )

        return if (query.isNotEmpty()) query[0] else null
    }

    fun hasTripUpdateRequired(): Boolean {
        val trip = getTrip()
        return trip != null && trip.hasUpdateRequired
    }

    fun getTripWithUpdateRequired(): FSTrip? {
        val query = query(
            """
                SELECT t.* FROM $TABLE t
                WHERE t.$UPDATE_REQUIRED = 1
            """.trimIndent()
        )

        return if (query.isEmpty()) null else query[0]
    }

    @Throws(SQLiteException::class)
    fun updateRequired(tripPrefix: Int, tripCode: Int, updateRequired: Int, db: SQLiteDatabase) {
        addUpdate(
            sQuery = """
                            UPDATE $TABLE
                            SET $UPDATE_REQUIRED = $updateRequired
                            WHERE $TRIP_PREFIX = $tripPrefix AND $TRIP_CODE = $tripCode
                        """,
            dbInstance = db
        ).let {
            if (it.hasError()) throw SQLiteException(it.errorMsg)
        }
    }

    fun getTripByPk(customerCode: Long, tripPrefix: Int, tripCode: Int): FSTrip? {
        val value = query(
            """
            SELECT * 
              FROM $TABLE 
             WHERE $CUSTOMER_CODE = $customerCode 
               AND $TRIP_PREFIX = $tripPrefix 
               AND $TRIP_CODE = $tripCode
        """.trimIndent()
        )
        if (value.isNotEmpty()) return value[0]
        return null
    }

    fun updateStartDate(
        tripPrefix: Int,
        tripCode: Int,
        startDate: String?,
        db: SQLiteDatabase? = null
    ) {
        addUpdate(
            sQuery = """
                UPDATE $TABLE
                SET $START_DATE = '$startDate'
                WHERE $TRIP_PREFIX = $tripPrefix AND $TRIP_CODE = $tripCode
            """.trimIndent(),
            dbInstance = db
        )
    }

    fun getConflict(startDate: String, endDate: String?): EventConflict? {

        // ------------------------------------------------
        // 1) Conflito de INÍCIO
        // ------------------------------------------------
        val startConflict = query(
                    """
            SELECT *
              FROM $TABLE
             WHERE $TRIP_STATUS != '${TripStatus.CANCELLED.toDescription()}'
               AND strftime('%s', $ORIGIN_DATE) <= strftime('%s', '$startDate')
               AND (
                    $DONE_DATE IS NULL
                    OR strftime('%s', $DONE_DATE) > strftime('%s', '$startDate')
               )
             ORDER BY strftime('%s', $ORIGIN_DATE) ASC
             LIMIT 1
            """.trimIndent()
        )

        if (startConflict.isNotEmpty()) {
            val trip = startConflict.first()
            return EventConflict(
                dateStart = trip.originDate,
                dateEnd = trip.doneDate,
                type = EventConflictType.START_OVERLAP,
                description = trip.tripId
            )
        }

        // ------------------------------------------------
        // 2) Conflito de TÉRMINO
        // ------------------------------------------------
        if (!endDate.isNullOrBlank()) {
            val endConflict = query(
                """
        SELECT *
          FROM $TABLE
             WHERE $TRIP_STATUS != '${TripStatus.CANCELLED.toDescription()}'
           AND strftime('%s', $ORIGIN_DATE) < strftime('%s', '$endDate')
           AND (
                $DONE_DATE IS NULL
                OR strftime('%s', $DONE_DATE) >= strftime('%s', '$endDate')
           )
         ORDER BY strftime('%s', $ORIGIN_DATE) ASC
         LIMIT 1
        """.trimIndent()
            )

            if (endConflict.isNotEmpty()) {
                val trip = endConflict.first()
                return EventConflict(
                    dateStart = trip.originDate,
                    dateEnd = trip.doneDate,
                    type = EventConflictType.END_OVERLAP,
                    description = trip.tripId
                )
            }
        }

        // ------------------------------------------------
        // 3) Intervalo engloba outro
        // ------------------------------------------------
        if (!endDate.isNullOrBlank()) {
            val rangeConflict = query(
                """
        SELECT *
          FROM $TABLE
             WHERE $TRIP_STATUS != '${TripStatus.CANCELLED.toDescription()}'
           AND strftime('%s', $ORIGIN_DATE) >= strftime('%s', '$startDate')
           AND strftime('%s', $ORIGIN_DATE) <= strftime('%s', '$endDate')
         ORDER BY strftime('%s', $ORIGIN_DATE) ASC
         LIMIT 1
        """.trimIndent()
            )

            if (rangeConflict.isNotEmpty()) {
                val trip = rangeConflict.first()
                return EventConflict(
                    dateStart = trip.originDate,
                    dateEnd = trip.doneDate,
                    type = EventConflictType.RANGE_OVERLAP,
                    description = trip.tripId
                )
            }
        }


        return null
    }


    fun getMaxDateSimple(
        tripPrefix: Int,
        tripCode: Int
    ): String? {
        val query = """
        SELECT MAX(all_dates.date_value) as max_date
        FROM (
            
            SELECT strftime('%Y-%m-%d %H:%M:%S', MAX(${FSTripEventDao.EVENT_END}), 'utc') || ' +00:00' as date_value 
            FROM ${FSTripEventDao.TABLE} 
            WHERE $TRIP_PREFIX = $tripPrefix AND $TRIP_CODE = $tripCode
            AND ${FSTripEventDao.EVENT_STATUS} != '${TripStatus.CANCELLED.toDescription()}'
           
            UNION ALL
            
            SELECT strftime('%Y-%m-%d %H:%M:%S', MAX(${FsTripDestinationDao.DEPARTED_DATE}), 'utc') || ' +00:00' as date_value 
            FROM ${FsTripDestinationDao.TABLE} 
            WHERE $TRIP_PREFIX = $tripPrefix AND $TRIP_CODE = $tripCode
            AND ${FsTripDestinationDao.DESTINATION_STATUS} != '${DestinationStatus.CANCELLED.toDescription()}'
           
            UNION ALL
            
            SELECT strftime('%Y-%m-%d %H:%M:%S', MAX(${FSTripUserDao.DATE_END}), 'utc') || ' +00:00' as date_value 
            FROM ${FSTripUserDao.TABLE} 
            WHERE $TRIP_PREFIX = $tripPrefix AND $TRIP_CODE = $tripCode
            AND ${FSTripUserDao.DATE_END} IS NOT NULL
           
        ) as all_dates
        WHERE date_value IS NOT NULL
    """.trimIndent()
        return queryObject<String?>(query) { cursor ->
            cursor.getStringOrNull(
                cursor.getColumnIndex(
                    "max_date"
                )
            )
        }.firstOrNull()
    }

    fun deleteTripsInDevice() {
        val query = """DELETE FROM $TABLE""".trimIndent()
        remove(query)
    }


}