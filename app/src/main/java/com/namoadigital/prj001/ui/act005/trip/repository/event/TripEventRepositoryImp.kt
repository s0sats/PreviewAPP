package com.namoadigital.prj001.ui.act005.trip.repository.event

import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.DB_TRANSACTION_ERROR_LBL
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.core.trip.base.BaseTripRepository
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventRestrictionDateUseCase
import com.namoadigital.prj001.core.util.TokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.extensions.coroutines.flowCatch
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.putApiRequest
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.model.GE_File
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripEventEnv
import com.namoadigital.prj001.model.trip.FSTripEventRec
import com.namoadigital.prj001.receiver.trip.WBR_Event
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.ui.act005.trip.TripViewModel
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toExtract
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TripEventRepositoryImp constructor(
    private val context: Context,
    private val dao: FSEventTypeDao,
    private val eventDao: FSTripEventDao,
    private val tripDao: FSTripDao,
    private val fileDao: GE_FileDao
) : TripEventRepository, BaseTripRepository(context) {

    override fun getListEventType(): List<FSEventType> {
        return dao.getListEventType(context.getCustomerCode())
    }

    override fun getEventType(customerCode: Long, eventTypeCode: Int): FSEventType? {
        return dao.getByString(
            """
                SELECT * 
                  FROM ${FSEventTypeDao.TABLE}
                 WHERE ${FSEventTypeDao.CUSTOMER_CODE} = $customerCode 
                   AND ${FSEventTypeDao.EVENT_TYPE_CODE} = $eventTypeCode
            """.trimIndent()
        )
    }

    override fun saveEvent(
        type: FSEventType,
        cost: Double?,
        comment: String?,
        photoPath: String?,
        changePhoto: Int,
        seq: Int?,
        eventStatus: EventStatus,
        dateStart: String?,
        dateEnd: String?
    ): Flow<IResult<Unit>> {
        return flow {
            tripDao.getTrip()?.let { trip ->
                val isOnlineMode = ToolBox_Con.isOnline(context) && !trip.hasUpdateRequired

                photoPath?.let { imagePath ->
                    GE_File().apply {
                        file_code = imagePath.replace(TripViewModel.JPG_EXTENSION, "")
                        file_path = imagePath
                        file_status = GE_File.OPENED
                        file_date = getCurrentDateApi(true)
                    }.let { fileModel ->
                        fileDao.addUpdate(fileModel)
                    }
                    ToolBox_Inf.scheduleUploadImgWork(context)
                }
                val request = FSTripEventEnv(
                    tripPrefix = trip.tripPrefix,
                    tripCode = trip.tripCode,
                    scn = trip.scn,
                    eventSeq = seq,
                    eventTypeCode = type.eventTypeCode,
                    eventTypeDesc = type.eventTypeDesc,
                    eventCost = cost,
                    eventComments = comment,
                    eventPhoto = photoPath,
                    eventStart = dateStart,
                    eventEnd = dateEnd,
                    changedPhoto = changePhoto,
                    eventStatus = eventStatus.name
                )

                if (!isOnlineMode) {
                    handleOfflineEvent(trip, request)
                } else {
                    emit(loading(true))

                    val manager = TokenManager<FSTripEventEnv>(context)
                    val token = manager.getToken(request)

                    val model = ApiRequest(
                        token = token,
                        parameters = request
                    ).apply {
                        session_app = context.getUserSessionAPP()
                    }


                    context.connectWS<ApiResponse<FSTripEventRec>>(
                        url = Constant.WS_TRIP_EVENT,
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
                                response.data?.let { data ->
                                    DatabaseTransactionManager(context).executeTransaction { db ->
                                        tripDao.updateScn(
                                            data.tripPrefix,
                                            data.tripCode,
                                            data.scn,
                                            db
                                        )

                                        val getEvent = eventDao.getEventFull(
                                            data.tripPrefix,
                                            data.tripCode,
                                            data.eventSeq,
                                            db
                                        )
                                        getEvent?.let { tripEvent ->
                                            tripEvent.apply {
                                                this.cost = request.eventCost
                                                this.comment = request.eventComments
                                                this.eventSeq = data.eventSeq
                                                this.eventStart = request.eventStart
                                                this.eventEnd = request.eventEnd
                                                this.eventStatus = request.eventStatus
                                                if (request.eventPhoto.isNullOrEmpty()) {
                                                    photoName = null
                                                    photoUrl = null
                                                    photoLocal = null
                                                } else {
                                                    this.photoLocal = request.eventPhoto
                                                }
                                                this.eventTypeCode = request.eventTypeCode
                                                this.eventTypeDesc = request.eventTypeDesc
                                                this.eventPhotoChanged = 0
                                            }
                                            eventDao.update(tripEvent, db)

                                        } ?: eventDao.update(
                                            FSTripEvent(
                                                customerCode = context.getCustomerCode(),
                                                tripPrefix = data.tripPrefix,
                                                tripCode = data.tripCode,
                                                eventTypeCode = request.eventTypeCode,
                                                eventSeq = data.eventSeq,
                                                eventTypeDesc = request.eventTypeDesc,
                                                eventStatus = request.eventStatus,
                                                eventTimeAlert = null,
                                                eventAllowedTime = null,
                                                eventTime = null,
                                                cost = request.eventCost,
                                                comment = request.eventComments,
                                                photoLocal = request.eventPhoto,
                                                photoName = null,
                                                photoUrl = null,
                                                eventStart = request.eventStart ?: "",
                                                eventEnd = request.eventEnd ?: "",
                                                eventPhotoChanged = 0
                                            ), db
                                        )
                                    }.success {
                                        context.sendBCStatus(WsTypeStatus.CLOSE_ACT(response = "ok"))
                                    }.failed {
                                        context.sendBCStatus(
                                            WsTypeStatus.CUSTOM_ERROR(
                                                networkTranslate[DB_TRANSACTION_ERROR_LBL]
                                            )
                                        )
                                    }

                                } ?: run {
                                    context.sendBCStatus(WsTypeStatus.ERROR(networkTranslate[DB_TRANSACTION_ERROR_LBL]))
                                }
                            },
                            failed = {
                                handleOfflineEvent(trip, request, it)
                            }
                        )
                    }
                }
            }
        }.flowCatch(this::class.java.name).flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<IResult<Unit>>.handleOfflineEvent(
        trip: FSTrip,
        eventRequest: FSTripEventEnv,
        networkError: Throwable? = null
    ) {

        DatabaseTransactionManager(context).executeTransaction { db ->
            val lastEventSeq = eventDao.getLastEvent(trip.tripPrefix, trip.tripCode)?.eventSeq ?: 0
            val newEvent = FSTripEventRec(
                tripPrefix = trip.tripPrefix,
                tripCode = trip.tripCode,
                scn = trip.scn,
                eventSeq = lastEventSeq + 1
            )

            val existingEvent =
                eventDao.getEvent(trip.tripPrefix, trip.tripCode, eventRequest.eventSeq ?: -1)

            if (existingEvent != null) {
                existingEvent.apply {
                    cost = eventRequest.eventCost
                    comment = eventRequest.eventComments
                    eventSeq = eventRequest.eventSeq!!
                    eventStart = eventRequest.eventStart
                    eventEnd = eventRequest.eventEnd
                    eventStatus = eventRequest.eventStatus
                    if (eventRequest.eventPhoto.isNullOrEmpty()) {
                        photoName = null
                        photoUrl = null
                        photoLocal = null
                    } else {
                        photoLocal = eventRequest.eventPhoto
                    }
                    eventTypeCode = eventRequest.eventTypeCode
                    eventTypeDesc = eventRequest.eventTypeDesc
                    eventPhotoChanged =
                        if (this.eventPhotoChanged == 1) 1 else eventRequest.changedPhoto
                }
                eventDao.update(existingEvent, db)
            } else {
                eventDao.update(
                    FSTripEvent(
                        customerCode = context.getCustomerCode(),
                        tripPrefix = eventRequest.tripPrefix,
                        tripCode = eventRequest.tripCode,
                        eventTypeCode = eventRequest.eventTypeCode,
                        eventSeq = newEvent.eventSeq,
                        eventTypeDesc = eventRequest.eventTypeDesc,
                        eventStatus = eventRequest.eventStatus,
                        eventTimeAlert = null,
                        eventAllowedTime = null,
                        eventTime = null,
                        cost = eventRequest.eventCost,
                        comment = eventRequest.eventComments,
                        photoLocal = eventRequest.eventPhoto,
                        photoName = null,
                        photoUrl = null,
                        eventStart = eventRequest.eventStart ?: "",
                        eventEnd = eventRequest.eventEnd ?: "",
                        eventPhotoChanged = eventRequest.changedPhoto
                    ), db
                )
            }
            tripDao.updateRequired(trip.tripPrefix, trip.tripCode, 1, db)
        }.results(
            success = {
                val result = handleNetworkError(networkError, context)
                emit(result)
            },
            failed = {
                emit(failed(it))
            }
        )
    }

    private fun sendToWebService(eventRequest: FSTripEventEnv) {
        context.sendToWebServiceReceiver<WBR_Event> {
            Bundle().putApiRequest(eventRequest)
        }
    }

    override fun getExtract(trip: FSTrip?): List<Extract<FSTripEvent>> {
        trip?.let { trip ->
            return eventDao.getExtract(trip.tripPrefix, trip.tripCode).map {
                it.apply {
                    waitAllowed = dao.getEventType(eventTypeCode)?.isWaitAllowed ?: false
                }.toExtract()
            }
        }

        return emptyList()
    }


    override fun checkRestrictionDate(
        startDateInMilis: Long,
        endDateInMilis: Long?,
        event: FSTripEvent?,
        waiting: Boolean
    ): GetEventRestrictionDateUseCase.OutputParams {
        var tripPrefix: Int? = null
        var tripCode: Int? = null
        var eventSeq: Int? = null
        event?.let {
            tripPrefix = it.tripPrefix
            tripCode = it.tripCode
            eventSeq = it.eventSeq
        } ?: run {
            tripDao.getTrip()?.let {
                tripPrefix = it.tripPrefix
                tripCode = it.tripCode
                eventSeq = null
            }
        }

        if (tripPrefix != null
            && tripCode != null
        ) {

            val eventsList = eventDao.getEventListDateValidation(
                tripPrefix!!,
                tripCode!!,
                eventSeq
            )

            eventsList.forEach { validationEvent ->
                val dateStart = ToolBox_Inf.dateToMilliseconds(validationEvent.event.eventStart)
                val dateEnd: Long? = validationEvent.event.eventEnd?.let { eventEnd ->
                    ToolBox_Inf.dateToMilliseconds(eventEnd)
                }
                //
                dateEnd?.let { end ->
                    var startDateError: Boolean = startDateInMilis in dateStart..end

                    endDateInMilis?.let { endDateInMilis ->
                        val endDateError: Boolean = endDateInMilis in dateStart..end
                        if (startDateError || endDateError) {
                            return GetEventRestrictionDateUseCase.OutputParams(
                                validationEvent.event,
                                startDateError = startDateError,
                                endDateError = endDateError
                            )
                        }
                        //
                        if ((dateStart in startDateInMilis..endDateInMilis)
                            || (end in startDateInMilis..endDateInMilis)
                        ) {
                            return GetEventRestrictionDateUseCase.OutputParams(
                                validationEvent.event,
                                startDateError = true,
                                endDateError = true
                            )
                        }
                    } ?: run {
                        if (startDateError) {
                            return GetEventRestrictionDateUseCase.OutputParams(
                                event = validationEvent.event,
                                startDateError = true,
                                endDateError = false
                            )
                        }
                    }
                } ?: run {
                    if (startDateInMilis == dateStart) {
                        return GetEventRestrictionDateUseCase.OutputParams(
                            validationEvent.event,
                            startDateError = true,
                            endDateError = false
                        )
                    }
                }
            }

        }
        return GetEventRestrictionDateUseCase.OutputParams(
            null,
            startDateError = false,
            endDateError = false
        )
    }

    override fun getFirstEventOnTrip(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int
    ): FSTripEvent? {
        val extract = eventDao.getExtract(
            tripPrefix,
            tripCode
        )
        return if (extract.isNotEmpty()) {
            extract[0]
        } else {
            null
        }
    }

}