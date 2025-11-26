package com.namoadigital.prj001.core.trip.data.trip

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.DB_TRANSACTION_ERROR_LBL
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.NETWORK_GENERIC_ERROR
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.core.trip.base.BaseTripRepository
import com.namoadigital.prj001.core.util.TokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.extensions.coroutines.flowCatch
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.putApiRequest
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.model.GE_File
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripFullUpdateEnv
import com.namoadigital.prj001.model.trip.FSTripOriginEnv
import com.namoadigital.prj001.model.trip.FSTripOriginRec
import com.namoadigital.prj001.model.trip.FSTripStartEnv
import com.namoadigital.prj001.model.trip.FSTripStartRec
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeRec
import com.namoadigital.prj001.model.trip.TripFleetSetEnv
import com.namoadigital.prj001.model.trip.TripFleetSetRec
import com.namoadigital.prj001.model.trip.TripNewEnv
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripStatusChangeEnv
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.model.trip.mapping.toTripUpdate
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.model.trip.toTripTarget
import com.namoadigital.prj001.receiver.trip.WBRGetTripFull
import com.namoadigital.prj001.receiver.trip.WBRSendTripFullUpdate
import com.namoadigital.prj001.receiver.trip.WBR_CreateTrip
import com.namoadigital.prj001.sql.MD_Site_Sql_004
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.sql.transaction.trip.TransactionWsTripDestinationStatusChange
import com.namoadigital.prj001.ui.act005.trip.TripViewModel.Companion.JPG_EXTENSION
import com.namoadigital.prj001.ui.act005.trip.di.model.OriginSites
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toOriginExtract
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toStartTripExtract
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.IOException
import javax.inject.Inject

class TripRepositoryImp @Inject constructor(
    private var context: Context,
    private var dao: FSTripDao,
    private val fileDao: GE_FileDao? = null,
    private val siteDao: MD_SiteDao? = null,
    private val eventDao: FSTripEventDao? = null,
    private val userDao: FSTripUserDao? = null,
    private val destinationDao: FsTripDestinationDao? = null
) : TripRepository, BaseTripRepository(context) {


    constructor(context: Context) : this(
        context,
        FSTripDao(context),
        GE_FileDao(context),
        MD_SiteDao(context),
        FSTripEventDao(context),
        FSTripUserDao(context),
        FsTripDestinationDao(context)
    )

    override fun getTrip(): FSTrip? {
        return dao.getTrip()
    }

    override fun getOriginCoordinates(): Coordinates? {
        return getTrip()?.originCoordinates
    }

    override fun getTripStatus(): TripStatus {
        return getTrip()?.tripStatus?.toTripStatus() ?: TripStatus.NULL
    }

    override fun setTripStatus(
        tripStatus: TripStatus,
        nextTripStatus: String,
        destinationSeq: Int?,
        destinationStatus: String?,
        nextDestinationSeq: Int?,
        nextDestinationStatus: String?,
        endDate: String?,
    ): Flow<IResult<Unit>> {
        return flow {
            val trip = getTrip()
            trip?.let { trip ->
                val tripOnline = isTripOnline(trip)
                emit(loading(tripOnline))
                var doneDate: String? = null
                if (tripStatus == TripStatus.DONE) {
                    doneDate = endDate
                }

                var startDate: String? = null
                if (tripStatus == TripStatus.START) {
                    startDate = getCurrentDateApi(true)
                }

                if (tripOnline) {
                    val modelRequest =
                        TripStatusChangeEnv(
                            tripPrefix = trip.tripPrefix,
                            tripCode = trip.tripCode,
                            scn = trip.scn,
                            tripStatus = tripStatus.toDescription(),
                            startDate = startDate,
                            doneDate = doneDate
                        )
                    val manager = TokenManager<TripStatusChangeEnv>(context)
                    val token = manager.getToken(modelRequest)

                    val modelEnv = ApiRequest(
                        token = token,
                        parameters = modelRequest
                    ).apply {
                        session_app = context.getUserSessionAPP()
                    }
                    //
                    context.connectWS<ApiResponse<TripDestinationStatusChangeRec>>(
                        url = Constant.WS_TRIP_CHANGE_STATUS,
                        model = modelEnv
                    ) {
                        it.results(
                            success = { response ->
                                manager.deleteToken()
                                context.sendBCStatus(
                                    WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                                        message = genericTranslate["generic_processing_data"],
                                        required = "0"
                                    )
                                )
                                response.data?.let { statusChanged ->
                                    val transaction = TransactionWsTripDestinationStatusChange(
                                        context = context,
                                        FSTripDao(context),
                                        FsTripDestinationDao(context)
                                    )
                                    //
                                    modelEnv.parameters?.let {
                                        if (transaction.save(
                                                statusChanged,
                                                startDate = startDate,
                                            )
                                        ) {
                                            ToolBox.sendBCStatus(
                                                context,
                                                "CLOSE_ACT",
                                                genericTranslate["generic_process_finalized_msg"],
                                                HMAux(),
                                                "",
                                                "0"
                                            )
                                        } else {
                                            ToolBox.sendBCStatus(
                                                context,
                                                "CUSTOM_ERROR",
                                                genericTranslate["msg_no_data_returned"],
                                                "",
                                                ""
                                            )
                                        }
                                    } ?: ToolBox.sendBCStatus(
                                        context,
                                        "CUSTOM_ERROR",
                                        genericTranslate["msg_no_data_returned"],
                                        "",
                                        ""
                                    )
                                } ?: run {
                                    if (response.status?.code == 406) {
                                        ToolBox.sendBCStatus(
                                            context,
                                            "CUSTOM_ERROR",
                                            response.error_msg,
                                            "",
                                            ""
                                        )
                                    }
                                }
                            },
                            failed = { throwable ->
                                saveOfflineTripStatusChange(
                                    trip,
                                    tripStatus,
                                    startDate,
                                    doneDate,
                                    destinationSeq,
                                    destinationStatus,
                                    nextDestinationSeq,
                                    nextDestinationStatus,
                                    nextTripStatus,
                                    throwable
                                )
                            }
                        )
                    }
                } else {
                    saveOfflineTripStatusChange(
                        trip,
                        tripStatus,
                        startDate,
                        doneDate,
                        destinationSeq,
                        destinationStatus,
                        nextDestinationSeq,
                        nextDestinationStatus,
                        nextTripStatus
                    )
                }
            }
        }.flowCatch(this::class.java.name).flowOn(Dispatchers.IO)

    }

    private suspend fun FlowCollector<IResult<Unit>>.saveOfflineTripStatusChange(
        it: FSTrip,
        tripStatus: TripStatus,
        startDate: String?,
        doneDate: String?,
        destinationSeq: Int?,
        destinationStatus: String?,
        nextDestinationSeq: Int?,
        nextDestinationStatus: String?,
        nextTripStatus: String,
        throwable: Throwable? = null
    ) {
        val transaction = TransactionWsTripDestinationStatusChange(
            context = context,
            FSTripDao(context),
            FsTripDestinationDao(context),
            userDao = FSTripUserDao(context)
        )
        //
        it.updateRequired = 1

        if (tripStatus == TripStatus.DONE) {
            it.doneDate = doneDate
        }

        if (tripStatus == TripStatus.START) {
            it.startDate = startDate
        }
        //
        val currentDestination =
            getDestinationForTripChange(destinationSeq, it, destinationStatus)
        //
        val nextDestination =
            getDestinationForTripChange(nextDestinationSeq, it, nextDestinationStatus)
        //
        val saveReturn = transaction.save(
            TripDestinationStatusChangeRec(
                tripPrefix = it.tripPrefix,
                tripCode = it.tripCode,
                scn = it.scn,
                tripStatus = nextTripStatus,
                destinationSeq = currentDestination?.destinationSeq,
                destinationStatus = currentDestination?.destinationStatus,
                date = it.doneDate,
                nextDestinationSeq = nextDestination?.destinationSeq,
                nextDestinationStatus = nextDestination?.destinationStatus
            ),
            startDate = it.startDate,
            updateRequired = true,
        )

        if (saveReturn) {
            emit(handleNetworkError(throwable, context))
        } else {
            emit(failed(IOException("SAVE_ERROR")))
        }

    }

    private fun getDestinationForTripChange(
        destinationSeq: Int?,
        it: FSTrip,
        destinationStatus: String?
    ): FsTripDestination? {
        return destinationSeq?.let { seq ->
            destinationDao?.let { destinationDao ->
                val destination = destinationDao.getDestination(
                    customerCode = context.getCustomerCode(),
                    tripPrefix = it.tripPrefix,
                    tripCode = it.tripCode,
                    destinationSeq = seq
                )
                destination?.copy(
                    destinationStatus = destinationStatus
                )
            }
        }
    }

    override fun isTripOnline(it: FSTrip) = (ToolBox_Con.isOnline(context)
            && !it.hasUpdateRequired)

    override fun getPositionDistanceMin(): Double {
        return getTrip()?.positionDistanceMin ?: 0.0
    }

    override fun getPreference(): CurrentTripPrefModel {
        return CurrentTripPrefModel()
    }

    override fun setPreference(
        customerCode: Long?,
        tripPrefix: Int?,
        tripCode: Int?,
        tripScn: Int?
    ) {
        /*        val currentPref = pref.read()

                customerCode?.let{
                    currentPref.customer_code = it
                }
                tripPrefix?.let{
                    currentPref.trip_prefix = it
                }
                tripCode?.let {
                    currentPref.trip_code = it
                }
                tripScn?.let {
                    currentPref.trip_scn = it
                }
                pref.write(currentPref)*/
    }

    override fun execCreateTrip(input: Coordinates?) {
        val modelRequest = TripNewEnv(
            input?.latitude,
            input?.longitude
        )
        context.sendToWebServiceReceiver<WBR_CreateTrip> {
            Bundle().putApiRequest(modelRequest)
        }
    }

    override fun execSyncTrip() {
        context.sendToWebServiceReceiver<WBRGetTripFull>()
    }

    override fun execSaveFleetData(
        licensePlate: String?,
        odometer: Long?,
        path: String?,
        changePhoto: Int,
        target: String,
        destinationSeq: Int?,
        deletePhoto: Boolean,
    ): Flow<IResult<Unit>> = flow {
        dao.getTrip()?.let { trip ->
            val isOnlineMode = ToolBox_Con.isOnline(context) && !trip.hasUpdateRequired

            path?.let { imagePath ->
                GE_File().apply {
                    file_code = imagePath.replace(JPG_EXTENSION, "")
                    file_path = imagePath
                    file_status = GE_File.OPENED
                    file_date = getCurrentDateApi(true)
                }.let { fileModel ->
                    fileDao?.addUpdate(fileModel)
                }
                ToolBox_Inf.scheduleUploadImgWork(context)
            }

            val model = TripFleetSetEnv(
                tripPrefix = trip.tripPrefix,
                tripCode = trip.tripCode,
                destinationSeq = destinationSeq,
                scn = trip.scn,
                target = target,
                licensePlate = licensePlate,
                odometer = odometer,
                imageKey = path,
                imageChanged = changePhoto
            ).also {
                it.deletePhoto = deletePhoto
            }

            val imageKey = if (!model.deletePhoto) {
                model.imageKey
            } else {
                ""
            }

            if (!isOnlineMode) {
                saveOffline(
                    model = model,
                    trip = trip,
                    destinationSeq = destinationSeq,
                    odometer = odometer,
                    imageKey = imageKey,
                    licensePlate = licensePlate,
                    deletePhoto = deletePhoto
                )
                return@flow
            }



            emit(loading())

            val manager = TokenManager<TripFleetSetEnv>(context)
            val token = manager.getToken(model)
            val modelEnv = ApiRequest(
                token = token,
                parameters = model.copy(
                    imageKey = if (deletePhoto) null else model.imageKey,
                    imageChanged = if (deletePhoto) 1 else model.imageChanged
                )
            ).apply {
                session_app = context.getUserSessionAPP()
            }


            context.connectWS<ApiResponse<TripFleetSetRec>>(
                url = Constant.WS_TRIP_FLEET_SET,
                model = modelEnv
            ) {

                it.results(
                    success = { response ->
                        manager.deleteToken()
                        context.sendBCStatus(
                            WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                                message = genericTranslate["generic_processing_data"],
                                required = "0"
                            )
                        )
                        response.data?.let { data ->
                            DatabaseTransactionManager(context).executeTransaction { db ->
                                dao.updateScn(data.tripPrefix, data.tripCode, data.scn, db)
                                val image = if (!model.deletePhoto) {
                                    model.imageKey
                                } else {
                                    ""
                                }
                                when (model.target.toTripTarget()) {
                                    TripTarget.DESTINATION -> {
                                        destinationDao?.updateArrivedFleet(
                                            tripPrefix = data.tripPrefix,
                                            tripCode = data.tripCode,
                                            destinationSeq = model.destinationSeq,
                                            odometer = model.odometer,
                                            photoPath = image,
                                            photoChanged = 0,
                                            db = db
                                        )
                                    }

                                    else -> {
                                        dao.updateFleet(
                                            data.tripPrefix,
                                            data.tripCode,
                                            model.licensePlate,
                                            model.odometer,
                                            model.target.toTripTarget(),
                                            image,
                                            0,
                                            db
                                        )
                                    }
                                }
                            }.success {
                                if (deletePhoto) {
                                    try {
                                        val file =
                                            File("${ConstantBase.CACHE_PATH_PHOTO}/${model.imageKey}")
                                        if (file.exists()) {
                                            file.delete()
                                        }
                                    } catch (exception: Exception) {
                                        ToolBox.registerException(
                                            javaClass.name,
                                            exception
                                        )
                                        exception.printStackTrace()
                                    }
                                }
                                context.sendBCStatus(WsTypeStatus.CLOSE_ACT(response = "ok"))
                            }.failed {
                                context.sendBCStatus(WsTypeStatus.CUSTOM_ERROR(networkTranslate[DB_TRANSACTION_ERROR_LBL]))
                            }
                        } ?: run {
                            context.sendBCStatus(WsTypeStatus.CUSTOM_ERROR(networkTranslate[NETWORK_GENERIC_ERROR]))
                        }
                    },
                    failed = { throwable ->
                        saveOffline(
                            model = model,
                            trip = trip,
                            destinationSeq = destinationSeq,
                            odometer = odometer,
                            imageKey = imageKey,
                            licensePlate = licensePlate,
                            deletePhoto = deletePhoto,
                            networkError = throwable
                        )
                    }
                )

            }

        }
    }.flowCatch(this::class.java.name).flowOn(Dispatchers.IO)

    private suspend fun FlowCollector<IResult<Unit>>.saveOffline(
        model: TripFleetSetEnv,
        trip: FSTrip,
        destinationSeq: Int?,
        odometer: Long?,
        imageKey: String?,
        licensePlate: String?,
        deletePhoto: Boolean,
        networkError: Throwable? = null
    ) {
        DatabaseTransactionManager(context).executeTransaction { db ->
            when (model.target.toTripTarget()) {
                TripTarget.DESTINATION -> {
                    destinationDao?.let {
                        //
                        val destination = it.getDestination(
                            trip.customerCode,
                            trip.tripPrefix,
                            trip.tripCode,
                            destinationSeq!!
                        )
                        //
                        it.updateArrivedFleet(
                            tripPrefix = trip.tripPrefix,
                            tripCode = trip.tripCode,
                            destinationSeq = destinationSeq,
                            odometer = odometer,
                            photoPath = imageKey,
                            photoChanged = if (destination?.arrivedFleetPhotoChanged == 1) 1 else model.photoChanged,
                            db = db
                        )
                    }

                }

                else -> {
                    val photoChanged = if (model.target.toTripTarget() == TripTarget.START) {
                        if (trip.fleetStartPhotoChanged == 1) 1 else model.photoChanged
                    } else {
                        if (trip.fleetEndPhotoChanged == 1) 1 else model.photoChanged
                    }

                    dao.updateFleet(
                        tripPrefix = model.tripPrefix,
                        tripCode = model.tripCode,
                        licensePlate = licensePlate,
                        odometer = odometer,
                        target = model.target.toTripTarget(),
                        photoPath = imageKey,
                        photoChanged = photoChanged,
                        db = db
                    )
                }
            }
            dao.updateRequired(
                tripPrefix = model.tripPrefix,
                tripCode = model.tripCode,
                updateRequired = 1,
                db = db
            )
        }.results(
            success = {
                if (deletePhoto) {
                    try {
                        ToolBox_Inf.deleteLocalImage(imageKey)
                    } catch (exception: Exception) {
                        ToolBox.registerException(
                            javaClass.name,
                            exception
                        )
                    }
                }

                val result = handleNetworkError(networkError, context)
                emit(result)
            },
            failed = {
                emit(failed(it))
            }
        )
    }

    override fun getListSites(): List<OriginSites> {
        return siteDao?.query(
            MD_Site_Sql_004(
                context.getCustomerCode(),
                true
            ).toSqlQuery()
        )?.map {
            OriginSites(
                it.site_id,
                it.site_code.toLong(),
                it.site_desc
            )
        } ?: emptyList()
    }

    override suspend fun execOriginSet(
        date: String,
        originType: OriginType,
        coordinates: Coordinates?,
        siteCode: Int?,
        siteDesc: String?
    ): Flow<IResult<Unit>> {
        return flow {
            dao.getTrip()?.let { trip ->
                val isOnlineMode = ToolBox_Con.isOnline(context) && !trip.hasUpdateRequired
                emit(loading(isOnlineMode))
                val origin = if (originType == OriginType.EDIT) null else originType
                val envModel = FSTripOriginEnv(
                    tripPrefix = trip.tripPrefix,
                    tripCode = trip.tripCode,
                    scn = trip.scn,
                    originType = origin?.name,
                    lat = coordinates?.latitude,
                    lon = coordinates?.longitude,
                    originSiteCode = siteCode,
                    siteDesc = siteDesc,
                    originDate = date
                )

                if (!isOnlineMode) {
                    saveOriginOffline(envModel)
                    return@flow
                }

                emit(loading())

                val manager = TokenManager<FSTripOriginEnv>(context)
                val token = manager.getToken(envModel)

                val model = ApiRequest(
                    token = token,
                    parameters = envModel
                ).apply {
                    session_app = context.getUserSessionAPP()
                }

                context.connectWS<ApiResponse<FSTripOriginRec>>(
                    url = Constant.WS_TRIP_ORIGIN_SET,
                    model = model
                ) {
                    it.results(
                        success = { response ->
                            manager.deleteToken()
                            context.sendBCStatus(
                                WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                                    message = genericTranslate["generic_processing_data"],
                                    required = "0"
                                )
                            )
                            if (envModel.originType == null) {
                                DatabaseTransactionManager(context).executeTransaction {
                                    response.data?.let { data ->
                                        dao.updateScn(data.tripPrefix, data.tripCode, data.scn, it)
                                        dao.updateOriginDate(
                                            data.tripPrefix,
                                            data.tripCode,
                                            envModel.originDate,
                                            it
                                        )
                                    } ?: run {
                                        context.sendBCStatus(WsTypeStatus.ERROR(networkTranslate[DB_TRANSACTION_ERROR_LBL]))
                                    }
                                }.success {
                                    context.sendBCStatus(WsTypeStatus.CLOSE_ACT(""))
                                }.failed {
                                    context.sendBCStatus(WsTypeStatus.CUSTOM_ERROR(networkTranslate[DB_TRANSACTION_ERROR_LBL]))
                                }
                                return@results
                            }

                            DatabaseTransactionManager(context).executeTransaction {
                                response.data?.let { data ->
                                    dao.updateScn(data.tripPrefix, data.tripCode, data.scn, it)
                                    dao.updateOrigin(
                                        data.tripPrefix,
                                        data.tripCode,
                                        envModel.originDate,
                                        envModel.originType,
                                        envModel.originSiteCode,
                                        envModel.siteDesc,
                                        envModel.lat,
                                        envModel.lon,
                                        it
                                    )
                                } ?: run {
                                    context.sendBCStatus(WsTypeStatus.ERROR(networkTranslate[DB_TRANSACTION_ERROR_LBL]))
                                }
                            }.success {
                                context.sendBCStatus(WsTypeStatus.CLOSE_ACT("ok"))
                            }.failed {
                                context.sendBCStatus(WsTypeStatus.CUSTOM_ERROR(networkTranslate[DB_TRANSACTION_ERROR_LBL]))
                            }
                        },
                        failed = { throwable ->
                            saveOriginOffline(envModel, throwable)
                        }
                    )
                }
            }
        }.flowCatch(this::class.java.name).flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<IResult<Unit>>.saveOriginOffline(
        envModel: FSTripOriginEnv,
        throwable: Throwable? = null
    ) {
        if (envModel.originType == null) {
            DatabaseTransactionManager(context).executeTransaction { db ->
                dao.updateOriginDate(
                    tripPrefix = envModel.tripPrefix,
                    tripCode = envModel.tripCode,
                    originDate = envModel.originDate,
                    db = db
                )
                dao.updateRequired(
                    tripPrefix = envModel.tripPrefix,
                    tripCode = envModel.tripCode,
                    updateRequired = 1,
                    db = db
                )
            }.results(
                success = {
                    val result = handleNetworkError(throwable, context)
                    emit(result)
                },
                failed = {
                    emit(failed(it))
                }
            )
            return
        }

        DatabaseTransactionManager(context).executeTransaction { db ->
            dao.updateOrigin(
                tripPrefix = envModel.tripPrefix,
                tripCode = envModel.tripCode,
                originDate = envModel.originDate,
                originType = envModel.originType,
                originSiteCode = envModel.originSiteCode,
                siteDesc = envModel.siteDesc,
                lat = envModel.lat,
                lon = envModel.lon,
                db = db
            )
            dao.updateRequired(
                tripPrefix = envModel.tripPrefix,
                tripCode = envModel.tripCode,
                updateRequired = 1,
                db = db
            )
        }.results(
            success = {
                val result = handleNetworkError(throwable, context)
                emit(result)
            },
            failed = {
                emit(failed(it))
            }
        )
    }


    override fun getEvent(): FSTripEvent? {
        val trip = dao.getTrip()
        return eventDao?.getEventFull(
            trip?.tripPrefix ?: -1,
            trip?.tripCode ?: -1
        )
    }

    override fun getExtract(trip: FSTrip?): List<Extract<FSTrip>>? {
        trip?.let { trip ->
            val list = mutableListOf<Extract<FSTrip>>()
            if (trip.tripStatus.toTripStatus() != TripStatus.PENDING) {
                list.add(trip.toOriginExtract())
                trip.startDate?.let {
                    list.add(trip.toStartTripExtract())
                }
                return list
            }
        }
        return null
    }

    override fun getTripByDestinationSeq(destinationSeq: Int): FSTrip? {
        return dao.getTripByDestination(destinationSeq)
    }

    override fun getTripFullUpdateEnv(trip: FSTrip): FSTripFullUpdateEnv? {
        userDao?.let {
            val listAllUsers = it.getAllUsersForFullUpdate(trip.tripPrefix, trip.tripCode)
            listAllUsers?.let { list ->
                trip.users?.addAll(list)
            } ?: run {
                trip.users = null
            }
        } ?: run {
            trip.users = null
        }
        if (trip.users == null) {
            return null
        }
        //
        eventDao?.let {
            val listAllUsers = it.listAllEvents(trip.tripPrefix, trip.tripCode)
            listAllUsers?.let { list ->
                trip.events?.addAll(list)
            } ?: run {
                trip.events = null
            }
        } ?: run {
            trip.events = null
        }
        //
        if (trip.events == null) {
            return null
        }
        //
        destinationDao?.let {
            val listAllDestination = it.listAllDestinations(
                customerCode = context.getCustomerCode(),
                tripPrefix = trip.tripPrefix,
                tripCode = trip.tripCode
            )
            //
            listAllDestination?.let { list ->
                trip.destinations?.addAll(list)
            } ?: run {
                trip.destinations = null
            }
        } ?: run {
            trip.destinations = null
        }
        //
        if (trip.destinations == null) {
            return null
        }
        //
        return trip.toTripUpdate()
    }

    override fun getTripUpdateRequired(): Boolean {
        return dao.hasTripUpdateRequired()
    }

    override fun sendTripFullUpdate(): Boolean {
        dao.getTripWithUpdateRequired()?.let { trip ->
            //
            ToolBox_Inf.scheduleUploadImgWork(context)
            val tripEnv = getTripFullUpdateEnv(trip)
            //
            tripEnv?.let {
                context.sendToWebServiceReceiver<WBRSendTripFullUpdate> {
                    Bundle().putApiRequest(tripEnv)
                }
            } ?: run {
                return false
            }
        } ?: run {
            return false
        }
        return true
    }

    override fun existsTripWithUpdateRequired(): Boolean {
        return dao.getTripWithUpdateRequired() != null
    }

    override fun saveStartDateSet(date: String): Flow<IResult<Unit>> {
        return flow {
            dao.getTrip()?.let { trip ->
                val isOnlineMode = ToolBox_Con.isOnline(context) && !trip.hasUpdateRequired

                emit(loading(isOnlineMode))

                val envModel = FSTripStartEnv(
                    tripPrefix = trip.tripPrefix,
                    tripCode = trip.tripCode,
                    scn = trip.scn,
                    startDate = date
                )

                if (!isOnlineMode) {
                    DatabaseTransactionManager(context).executeTransaction {
                        dao.updateStartDate(
                            trip.tripPrefix,
                            trip.tripCode,
                            date,
                            it
                        )

                        dao.updateRequired(
                            tripPrefix = trip.tripPrefix,
                            tripCode = trip.tripCode,
                            updateRequired = 1,
                            it,
                        )
                    }
                    emit(success(Unit))
                    return@flow
                }


                emit(loading())

                val manager = TokenManager<FSTripStartEnv>(context)
                val token = manager.getToken(envModel)

                val model = ApiRequest(
                    token = token,
                    parameters = envModel
                ).apply {
                    session_app = context.getUserSessionAPP()
                }

                context.connectWS<ApiResponse<FSTripStartRec>>(
                    url = Constant.WS_TRIP_START_SET,
                    model = model
                ) {
                    it.results(
                        success = { response ->
                            manager.deleteToken()
                            context.sendBCStatus(
                                WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                                    message = genericTranslate["generic_processing_data"],
                                    required = "0"
                                )
                            )

                            DatabaseTransactionManager(context).executeTransaction { db ->
                                response.data?.let { data ->
                                    dao.updateScn(data.tripPrefix, data.tripCode, data.scn, db)
                                    dao.updateStartDate(
                                        data.tripPrefix,
                                        data.tripCode,
                                        envModel.startDate,
                                        db
                                    )
                                }
                            }.success {
                                context.sendBCStatus(WsTypeStatus.CLOSE_ACT(""))
                            }.failed {
                                context.sendBCStatus(WsTypeStatus.CUSTOM_ERROR(networkTranslate[DB_TRANSACTION_ERROR_LBL]))
                            }
                        },
                        failed = {
                            emit(failed(it))
                        }

                    )
                }
            }
        }.flowOn(Dispatchers.IO)
    }


    override fun getUsersCurrentTrip(
        tripPrefix: Int,
        tripCode: Int,
    ): List<FSTripUser> {
        return userDao?.getListUserByTrip(
            tripPrefix = tripPrefix,
            tripCode = tripCode
        ) ?: emptyList()
    }
}
