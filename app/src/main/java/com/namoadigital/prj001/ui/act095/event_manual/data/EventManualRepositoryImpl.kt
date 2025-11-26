package com.namoadigital.prj001.ui.act095.event_manual.data

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse.ApiCollection
import com.namoadigital.prj001.core.util.TokenManager
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.event.EventManualDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.extensions.coroutines.flowSafe
import com.namoadigital.prj001.extensions.coroutines.namoaCatch
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.getUserCode
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.model.GE_File
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.model.event.remote.EventManualSetRequestItem
import com.namoadigital.prj001.model.event.remote.EventManualSetResponseItem
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import com.namoadigital.prj001.util.Constant.USER_TIMELINE_MANUAL_EVENT_SET
import com.namoadigital.prj001.util.ConstantBaseApp.PARAM_USER_TIMELINE
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOn
import java.sql.SQLDataException
import java.util.UUID
import javax.inject.Inject


class EventManualRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val typeDao: FSEventTypeDao,
    private val eventDao: EventManualDao,
    private val fileDao: GE_FileDao,
) : EventManualRepository {

    companion object {
        val TEMPORARY_SEQ = 100000
    }

    override fun hasAccessToEventManual(): Boolean {
        return ToolBox_Inf.parameterExists(appContext, PARAM_USER_TIMELINE)
    }


    override suspend fun getListEventType(): Flow<IResult<List<FSEventType>>> {
        return flowSafe(tag = "EventManualRepositoryImpl.getListEventType") {
            emit(loading())

            val list = typeDao.getListEventType(appContext.getCustomerCode())

            emit(success(list))
        }
    }

    override fun geEventTypeByCode(code: Int): FSEventType? {
        return typeDao.getEventTypeByCode(
            customerCode = appContext.getCustomerCode(),
            eventTypeCode = code
        )
    }

    override suspend fun saveEvent(eventManualData: EventManualData): Flow<IResult<EventManual>> =
        flowSafe(
            tag = "EventManualRepositoryImpl.saveEvent"
        ) {

            val eventManual = eventManualData.toEntity()
            var event = eventManual

            if (eventManual.daySeq == null) {
                val lastSeq = eventDao.getLastSeq(
                    userCode = eventManual.user,
                    eventDay = eventManual.eventDay
                )

                event = eventManual.copy(
                    appId = UUID.randomUUID().toString(),
                    daySeq = TEMPORARY_SEQ + (lastSeq + 1)
                )
            }

            val result = eventDao.saveEvent(event.copy(isUpdateRequired = true))
            if (result.hasError()) {
                emit(failed(SQLDataException(EventManualKey.ErrorSaveEventMsg.key)))
                return@flowSafe
            }

            emit(success(eventManual))
        }

    override suspend fun sendEvents(): Flow<IResult<Unit>> = flowSafe(
        tag = "EventManualRepositoryImpl.sendEvents"
    ) {
        val userCode = appContext.getUserCode().toInt()
        val pendingEvents = preparePendingEvents(userCode)

        if (pendingEvents.isEmpty()) {
            emit(success(Unit))
            return@flowSafe
        }

        processEventPhotos(pendingEvents)

        if (!ToolBox_Con.isOnline(appContext)) {
            emit(failed(NetworkConnectionException(EventManualKey.SavingEventOfflineMsg.key)))
            return@flowSafe
        }

        val domainEvents = pendingEvents.map { it.toDomain() }
        emitAll(handleOnlineSave(domainEvents))
    }


    private fun processEventPhotos(events: List<EventManual>) {
        val changedPhotos =
            events.mapNotNull { it.photo.takeIf { photo -> photo.isChanged && photo.local != null } }

        changedPhotos.forEach { photo ->
            val fileModel = GE_File().apply {
                file_code = photo.local!!.replace(Act011_Main.JPG_EXTENSION, "")
                file_path = photo.local
                file_status = GE_File.OPENED
                file_date = getCurrentDateApi(true)
            }

            fileDao.addUpdate(fileModel)
        }

        if (changedPhotos.isNotEmpty()) {
            ToolBox_Inf.scheduleUploadImgWork(appContext)
        }
    }


    private fun preparePendingEvents(eventUser: Int): MutableList<EventManual> {
        return eventDao.getAllEventsPending(userCode = eventUser).map {
            it.copy(daySeq = it.daySeq?.takeIf { seq -> seq < TEMPORARY_SEQ })
        }.toMutableList()
    }


    private fun handleOnlineSave(
        pendingEvents: List<EventManualSetRequestItem>,
    ): Flow<IResult<Unit>> = flowSafe(tag = "EventManualRepositoryImpl.handleOnlineSave") {

        val tokenManager = TokenManager<List<EventManualSetRequestItem>>(appContext)
        val token = tokenManager.getToken(pendingEvents)

        val requestModel = ApiRequest(
            token = token,
            parameters = pendingEvents
        ).apply {
            session_app = appContext.getUserSessionAPP()
        }

        appContext.connectWS<ApiResponse<ApiCollection<EventManualSetResponseItem>>>(
            url = USER_TIMELINE_MANUAL_EVENT_SET,
            model = requestModel,
        ) { collect ->

            collect.results(
                success = { response ->
                    emit(loading(message = EventManualKey.ProcessingEventMsg.key))

                    val remoteEvents = response.data?.list.orEmpty()
                    val localEventsMap = pendingEvents.associateBy { it.appId }

                    for (remote in remoteEvents) {
                        val localEvent =
                            localEventsMap[remote?.appId]?.toEntity()
                        val daySeq = remote?.eventDaySeq

                        if (localEvent != null && daySeq != null) {
                            val result = eventDao.updateEventFromServer(
                                appId = localEvent.appId,
                                eventDaySeq = daySeq,
                                eventDay = localEvent.eventDay,
                                eventUser = localEvent.user,
                            )

                            if (result.hasError()) {
                                emit(failed(SQLDataException(result.errorMsg)))
                                return@results
                            }
                        }
                    }

                    tokenManager.deleteToken()
                    emit(success(Unit))
                },
                failed = { throwable ->
                    emit(failed(throwable))
                }
            )
        }
    }


    override suspend fun getListEvents(date: Int): Flow<IResult<List<EventManual>>> = flowSafe(
        tag = "EventManualRepositoryImpl.getListEvents"
    ) {

        emit(loading())

        val local = eventDao.getAllEvents(
            userCode = appContext.getUserCode(),
            date = date,
        )

        emit(success(local))


    }.namoaCatch("EventManualRepositoryImpl.getListEvents").flowOn(Dispatchers.IO)

    override suspend fun getEventByDate(date: Int): Flow<IResult<List<EventManual>>> =
        flowSafe(tag = "EventManualRepositoryImpl.getEventByDate") {

            emit(loading())

            val event = eventDao.getEventByDate(date)

            emit(success(event))

        }.namoaCatch("EventManualRepositoryImpl.getEventByDate").flowOn(Dispatchers.IO)


    override fun getEventActive(): EventManual? {
        return eventDao.getEventActive()
    }

    override fun getEventPendency(): List<EventManual> {
        return eventDao.getAllEventsPending(appContext.getUserCode().toInt())
    }

    override fun getEventConflict(
        currentSeq: Int,
        startDate: String,
        endDate: String?
    ): EventConflict? {
        return eventDao.getEventConflict(
            currentSeq = currentSeq,
            userCode = appContext.getUserCode().toInt(),
            newStart = startDate,
            newEnd = endDate
        )
    }

    override fun getHistoryEventsByDay(eventDay: Int): Flow<IResult<List<EventManual>>> {
        return flowSafe(tag = "EventManualRepositoryImpl.getHistoryEventsByDay") {
            emit(loading())
            val result = eventDao.getHistoryEventsByDay(
                userCode = appContext.getUserCode().toInt(),
                eventDay = eventDay
            )
            result.map { map ->
                var typeByCode = geEventTypeByCode(map.typeCode)
                map.eventFieldConfig =
                    EventManualData.EventFieldConfig().apply { setFieldConfig(typeByCode) }
                map
            }



            emit(success(result))
        }
    }

}