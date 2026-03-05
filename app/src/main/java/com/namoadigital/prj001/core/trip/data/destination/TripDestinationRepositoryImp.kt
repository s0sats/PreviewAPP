package com.namoadigital.prj001.core.trip.data.destination

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.DB_TRANSACTION_ERROR_LBL
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.trip.base.BaseTripRepository
import com.namoadigital.prj001.core.trip.domain.model.OdometerArrivedDestination
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.core.util.TripTokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.extensions.coroutines.flowCatch
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.getUserCode
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripDestinationEditEnv
import com.namoadigital.prj001.model.trip.TripDestinationEditRec
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeRec
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.receiver.trip.WBR_SelectDestination
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.sql.transaction.trip.TransactionWsTripDestinationStatusChange
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toExtract
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailables
import com.namoadigital.prj001.ui.act094.destination.domain.select_destination.SelectDestinationEnv
import com.namoadigital.prj001.ui.act094.destination.domain.select_destination.SelectDestinationRec
import com.namoadigital.prj001.ui.act094.destination.local.preference.DestinationFilterPreference
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class TripDestinationRepositoryImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: FsTripDestinationDao,
    private val tripDao: FSTripDao,
    private val tripPositionDao : FsTripPositionDao,
) : TripDestinationRepository, BaseTripRepository(context) {

    private val preference: DestinationFilterPreference =
        DestinationFilterPreference.instance(context)

    override suspend fun getListExternalAddress(): List<DestinationAvailables> {
        return dao.getExternalAddressList(
            context.getUserCode(),
            ToolBox.getDeviceGMT(false)
        )
    }

    override suspend fun getListSiteAddress(): List<DestinationAvailables> {
        return dao.getSiteAddressList(context.getUserCode().toInt())
    }

    override fun execServiceSelectDestination(bundle: Bundle) {
        Intent(context, WBR_SelectDestination::class.java).apply {
            putExtras(bundle)
            context.sendBroadcast(this)
        }
    }

    override fun execServiceOvernightDestination(
        trip: FSTrip?,
        destinationType: String,
        currentLat: Double?,
        currentLon: Double?,
    ): Flow<IResult<Unit>> {
        return flow {
            trip?.let { trip ->
                /*
                    BARRIONUEVO 30-06-2024
                    Feito por causa do prazo. o certo era concentrar no metodo que
                    esta TripRepository gerando mta mudanca
                 */
                val isOnline = ToolBox_Con.isOnline(context) && !trip.hasUpdateRequired
                emit(loading(isOnline))

                saveOfflineOvernight(
                    trip,
                    currentLat,
                    currentLon,
                    null
                ).results(
                    success = {
                        if (isOnline) {
                            val gson = GsonBuilder().serializeNulls().create()

                            val params = SelectDestinationEnv(
                                tripPrefix = trip.tripPrefix,
                                tripCode = trip.tripCode,
                                scn = trip.scn,
                                destinationType,
                                siteCode = null,
                                destinationTicketPrefix = null,
                                destinationTicketCode = null,
                                currentLat = currentLat,
                                currentLon = currentLon,
                            )
//
                            val manager = TripTokenManager().create<SelectDestinationEnv>(context = context)
                            val token = manager.getToken(params)
                            val modelEnv = ApiRequest(
                                token = token,
                                parameters = params
                            ).apply {
                                session_app = context.getUserSessionAPP()
                            }
//
                            context.connectWS<ApiResponse<SelectDestinationRec>>(
                                url = Constant.WS_SELECT_DESTINATION,
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
                                            //
                                            DatabaseTransactionManager(context).executeTransaction { db ->
                                                tripDao.updateScn(
                                                    data.tripPrefix,
                                                    data.tripCode,
                                                    data.scn,
                                                    db,
                                                    0
                                                )
                                            }.success {
                                                ToolBox.sendBCStatus(
                                                    context,
                                                    "CLOSE_ACT",
                                                    genericTranslate["generic_process_finalized_msg"],
                                                    HMAux(),
                                                    gson.toJson(data),
                                                    "0"
                                                )
                                            }
                                            .failed {
                                                context.sendBCStatus(
                                                    WsTypeStatus.CUSTOM_ERROR(
                                                        networkTranslate[DB_TRANSACTION_ERROR_LBL]
                                                    )
                                                )
                                            }
                                            //
                                        }
                                    },
                                    failed = { throwable ->
                                        emit(handleNetworkError(throwable, context))
                                    }
                                )
                            }
                        } else {
                            emit(success(Unit))
                        }
                    },
                    failed = {
                        emit(failed(it))
                    }
                )
            }
        }.flowCatch(this::class.java.name).flowOn(Dispatchers.IO)


    }

    private suspend fun saveOfflineOvernight(
        trip: FSTrip,
        currentLat: Double?,
        currentLon: Double?,
        throwable: Throwable? = null
    ):IResult<Unit> {
        val nextDestinationSeq = getNextDestinationSeq(trip.tripPrefix, trip.tripCode)
            ?: return failed(IOException("SAVE_ERROR"))

        val selectDestinationRec = SelectDestinationRec(
            tripPrefix = trip.tripPrefix,
            tripCode = trip.tripCode,
            scn = trip.scn,
            destinationSeq = nextDestinationSeq,
            destinationStatus = DestinationStatus.ARRIVED.toDescription(),
            tripStatus = TripStatus.OVER_NIGHT.toDescription(),
            lat = currentLat,
            lon = currentLon,
            arrivedDate = getCurrentDateApi(true),
            arrivedLat = currentLat,
            arrivedLon = currentLon,
            distanceMin = trip.positionDistanceMin ?: 0.1
        )
        //
        val saveOverNightDestination = saveOverNightDestination(
            trip.customerCode,
            selectDestinationRec,
            isOnline = false
        )
        if (saveOverNightDestination) {
            return handleNetworkError(throwable, context)
        } else {
            return failed(IOException("SAVE_ERROR"))
        }
    }

    override fun saveFilterPreference(filter: AvailableDestinationFilter) {
        preference.write(filter)
    }

    override fun getDestinationFilterPreference(): AvailableDestinationFilter {
        return preference.read()
    }

    override fun getExtract(trip: FSTrip?): List<Extract<FsTripDestination>> {
        trip?.let { trip ->
            return dao.getExtract(trip.tripPrefix, trip.tripCode).map { it.toExtract() }
        }

        return emptyList()
    }


    override fun getListOdometerArrived(): List<OdometerArrivedDestination> {
        tripDao?.getTrip()?.let { trip ->
            return dao.getListOdometer(trip.tripPrefix, trip.tripCode)
        }
        return emptyList()
    }


    override fun getDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): FsTripDestination? {
        return dao.getDestination(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq,
        )
    }

    override fun getDestinationByStatus(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        status: DestinationStatus
    ): FsTripDestination? {
        return dao.getDestinationByStatus(customerCode, tripPrefix, tripCode, status.name)
    }

    override fun getLastDestinationStatus(
        tripPrefix: Int,
        tripCode: Int,
    ): DestinationStatus {
        return dao.getLastDestinationStatus(
            tripPrefix,
            tripCode
        ) ?: DestinationStatus.NULL
    }

    override fun getCoordinatesDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
    ): Coordinates? {
        val destination = getDestination(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq,
        )
        return destination?.coordinates
    }

    override fun saveDestination(
        customerCode: Long,
        remoteDestination: SelectDestinationRec,
        destination: SelectionDestinationAvailable,
        isOnlineFLow: Boolean
    ): Boolean {
        val fsTripDestination = FsTripDestination(
            customerCode = customerCode,
            tripPrefix = remoteDestination.tripPrefix,
            tripCode = remoteDestination.tripCode,
            destinationSeq = remoteDestination.destinationSeq,
            destinationType = destination.destinationType,
            destinationSiteCode = destination.siteCode,
            destinationSiteDesc = destination.siteDesc,
            destinationRegionCode = destination.regionCode,
            destinationRegionDesc = destination.regionDesc,
            ticketPrefix = destination.ticketPrefix,
            ticketCode = destination.ticketCode,
            ticketId = "",
            destinationStatus = remoteDestination.destinationStatus,
            latitude = remoteDestination.lat,
            longitude = remoteDestination.lon,
            countryId = destination.countryId,
            state = destination.state,
            city = destination.city,
            district = destination.district,
            street = destination.street,
            streetnumber = destination.streetnumber,
            complement = destination.complement,
            zipCode = destination.zipCode,
            plusCode = destination.plusCode,
            contactName = destination.contactName,
            contactPhone = destination.contactPhone,
            siteMainUser = destination.siteMainUser,
            minDate = destination.minDate,
            serialCnt = destination.serialCnt ?: 0,
        )
        //
        return tripDao.addTripDestination(
            fsTripDestination = fsTripDestination,
            scn = remoteDestination.scn,
            tripStatus = remoteDestination.tripStatus,
            isUpdateRequired = 1
        )
        //
    }

    override fun saveOverNightDestination(
        customerCode: Long,
        remoteDestination: SelectDestinationRec,
        isOnline: Boolean
    ): Boolean {


        val fsTripDestination = FsTripDestination(
            customerCode = customerCode,
            tripPrefix = remoteDestination.tripPrefix,
            tripCode = remoteDestination.tripCode,
            destinationSeq = remoteDestination.destinationSeq,
            destinationType = FsTripDestination.OVER_NIGHT_DESTINATION_TYPE,
            ticketId = null,
            destinationStatus = remoteDestination.destinationStatus,
            latitude = remoteDestination.lat,
            longitude = remoteDestination.lon,
            arrivedDate = remoteDestination.arrivedDate,
            arrivedLat = remoteDestination.lat,
            arrivedLon = remoteDestination.lon,
            arrivedType = TripDestinationStatusChangeEnv.TYPE_MANUAL,
            serialCnt = 0,
        )
        //
        return tripDao.addTripDestination(
            fsTripDestination = fsTripDestination,
            scn = remoteDestination.scn,
            tripStatus = remoteDestination.tripStatus,
            isUpdateRequired =  1
        )
        //
    }

    override suspend fun setTripDestinationStatusChange(
        destinationSeq: Int,
        destinationStatus: String,
        tripStatus: String
    ): Flow<IResult<Unit>> {
        return flow {
            tripDao.getTrip()?.let { trip ->
                val isOnlineMode = ToolBox_Con.isOnline(context) && !trip.hasUpdateRequired
                emit(loading(isOnlineMode))

                var nextDestination: FsTripDestination? = null
                if (TripStatus.valueOf(tripStatus) == TripStatus.TRANSIT) {
                    nextDestination = getDestinationByStatus(
                        context.getCustomerCode(),
                        trip.tripPrefix,
                        trip.tripCode,
                        DestinationStatus.PENDING
                    )
                }
                //
                offlineSaveTripDestinationStatusChange(
                    trip,
                    tripStatus,
                    destinationSeq,
                    destinationStatus,
                    nextDestination,
                    tripDao,
                    null
                ).results(
                    success = {
                        if (isOnlineMode) {
                            TripDestinationStatusChangeEnv(
                                tripPrefix = trip.tripPrefix,
                                tripCode = trip.tripCode,
                                destinationSeq = destinationSeq,
                                scn = trip.scn,
                                destinationStatus = destinationStatus,
                            ).let { request ->
                                val manager =
                                    TripTokenManager().create<TripDestinationStatusChangeEnv>(
                                        context
                                    )
                                val token = manager.getToken(request)

                                val modelEnv = ApiRequest(
                                    token = token,
                                    parameters = request
                                ).apply {
                                    session_app = context.getUserSessionAPP()
                                }
                                //
                                context.connectWS<ApiResponse<TripDestinationStatusChangeRec>>(
                                    url = Constant.WS_DESTINATION_CHANGE_STATUS,
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
                                                //
                                                DatabaseTransactionManager(context).executeTransaction {
                                                    tripDao.updateScn(
                                                        statusChanged.tripPrefix,
                                                        statusChanged.tripCode,
                                                        statusChanged.scn,
                                                        it,
                                                        0,
                                                    )
                                                    tripPositionDao.setLastPositionAsReference()
                                                }.results(
                                                    success = {
                                                        ToolBox.sendBCStatus(
                                                            context,
                                                            "CLOSE_ACT",
                                                            genericTranslate["generic_process_finalized_msg"],
                                                            HMAux(),
                                                            "",
                                                            "0"
                                                        )
                                                    },
                                                    failed = { throwable ->
                                                        ToolBox.sendBCStatus(
                                                            context,
                                                            "CUSTOM_ERROR",
                                                            genericTranslate["msg_no_data_returned"],
                                                            "",
                                                            ""
                                                        )
                                                    }
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
                                            emit(handleNetworkError(throwable, context))
                                        }
                                    )
                                }
                            }
                        } else {
                            emit(success(Unit))
                        }
                    },
                    failed = {
                        emit(failed(it))
                    }
                )
            }
        }.flowCatch(this::class.java.name).flowOn(Dispatchers.IO)
    }

    private fun offlineSaveTripDestinationStatusChange(
        trip: FSTrip,
        tripStatus: String,
        destinationSeq: Int,
        destinationStatus: String,
        nextDestination: FsTripDestination?,
        tripDao: FSTripDao,
        throwable: Throwable? = null,
    ):IResult<Unit> {
        return TripDestinationStatusChangeRec(
            tripPrefix = trip.tripPrefix,
            tripCode = trip.tripCode,
            tripStatus = tripStatus,
            scn = trip.scn,
            destinationSeq = destinationSeq,
            destinationStatus = destinationStatus,
            nextDestinationSeq = nextDestination?.destinationSeq,
            nextDestinationStatus = if (nextDestination != null) DestinationStatus.TRANSIT.toDescription() else null,
            date = getCurrentDateApi(true),
        ).let { modelRec ->
            TransactionWsTripDestinationStatusChange(
                context = context,
                fsTripDao = tripDao,
                fsTripDestinationDao = dao
            ).let { transaction ->
                if (transaction.save(modelRec, updateRequired = true)) {
                    handleNetworkError(throwable, context)
                    success(Unit)
                } else {
                    failed(IOException("SAVE_ERROR"))
                }
            }
        }
    }

    override fun saveDestinationDate(
        dateStart: String,
        dateEnd: String,
        destinationSeq: Int
    ): Flow<IResult<Unit>> {
        return flow {
            tripDao.getTrip()?.let { trip ->
                val isTripOnline = (ToolBox_Con.isOnline(context) && !trip.hasUpdateRequired)

                    saveDestinationOffline(trip, destinationSeq, dateStart, dateEnd).results(
                        success = {
                            if (isTripOnline) {
                                emit(loading())
                                TripDestinationEditEnv(
                                    tripPrefix = trip.tripPrefix,
                                    tripCode = trip.tripCode,
                                    scn = trip.scn,
                                    destinationSeq = destinationSeq,
                                    arrivedDate = dateStart,
                                    departedDate = dateEnd
                                ).let { request ->
                                    val manager =
                                        TripTokenManager().create<TripDestinationEditEnv>(context)
                                    val token = manager.getToken(request)
                                    val modelEnv = ApiRequest(
                                        token = token,
                                        parameters = request
                                    ).apply {
                                        session_app = context.getUserSessionAPP()
                                    }


                                    context.connectWS<ApiResponse<TripDestinationEditRec>>(
                                        url = Constant.WS_TRIP_DESTINATION_EDIT,
                                        model = modelEnv
                                    ) {
                                        it.results(
                                            success = { response ->
                                                manager.deleteToken()
                                                //
                                                context.sendBCStatus(
                                                    WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                                                        message = genericTranslate["generic_processing_data"],
                                                        required = "0"
                                                    )
                                                )
                                                //
                                                response.data?.let { data ->
                                                    DatabaseTransactionManager(context).executeTransaction { db ->
                                                        tripDao.updateScn(
                                                            data.tripPrefix,
                                                            data.tripCode,
                                                            data.scn,
                                                            db,
                                                            0
                                                        )
                                                    }.success {
                                                        context.sendBCStatus(
                                                            WsTypeStatus.CLOSE_ACT(
                                                                response = "ok"
                                                            )
                                                        )
                                                    }.failed {
                                                        context.sendBCStatus(
                                                            WsTypeStatus.CUSTOM_ERROR(
                                                                networkTranslate[DB_TRANSACTION_ERROR_LBL]
                                                            )
                                                        )
                                                    }
                                                }
                                            },
                                            failed = { throwable ->
                                                emit(success(Unit))
                                            }
                                        )
                                    }
                                }
                            } else {
                                emit(success(Unit))
                            }
                        },
                        failed = {
                            emit(failed(it))
                        }
                    )
            }
        }.flowCatch(this::class.java.name).flowOn(Dispatchers.IO)
    }

    private suspend fun saveDestinationOffline(
        trip: FSTrip,
        destinationSeq: Int,
        dateStart: String,
        dateEnd: String,
    ):IResult<Unit> {
        return DatabaseTransactionManager(context).executeTransaction { db ->
            dao.updateArrivedAndDeparted(
                trip.tripPrefix,
                trip.tripCode,
                destinationSeq,
                dateStart,
                dateEnd,
                db
            )
            tripDao.updateRequired(
                tripPrefix = trip.tripPrefix,
                tripCode = trip.tripCode,
                updateRequired = 1,
                db = db
            )
        }
    }

    override fun getLastDestinationDeparted(
        prefix: Int,
        code: Int,
    ): FsTripDestination? = dao.getLastDestinationDeparted(
        customerCode = context.getCustomerCode(),
        prefix = prefix,
        code = code
    )

    override fun getLastDestinationArrived(
        prefix: Int,
        code: Int,
    ): FsTripDestination? = dao.getLastDestinationArrived(
        customerCode = context.getCustomerCode(),
        prefix = prefix,
        code = code
    )

    override fun getLastDestination(
        prefix: Int,
        code: Int,
    ): FsTripDestination? {
        return dao.getLastDestination(
            customerCode = context.getCustomerCode(),
            prefix = prefix,
            code = code,
        )
    }

    override fun getPreviousValidDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType
    ): FsTripDestination? {
        return dao.previousDestination(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq,
            type,
        )
    }

    override fun getListDestinations(prefix: Int, code: Int) : List<FsTripDestination> {
        return dao.listAllDestinations(
            context.getCustomerCode(),
            prefix,
            code
        ) ?: emptyList()
    }

    override fun getNextValidDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType,

    ): FsTripDestination? {
        return dao.nextDestination(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq,
            type,
        )
    }

    override fun getNextDestinationSeq(tripPrefix: Int, tripCode: Int): Int? {
        return dao.getNextDestinationSeq(tripPrefix, tripCode)
    }

    override fun getCurrentDestination(): FsTripDestination? {
        return dao.getOnSiteDestination()
    }
}