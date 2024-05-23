package com.namoadigital.prj001.ui.act005.trip.repository.event

import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventRestrictionDateUseCase
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.putApiRequest
import com.namoadigital.prj001.model.GE_File
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripEventEnv
import com.namoadigital.prj001.receiver.trip.WBR_Event
import com.namoadigital.prj001.ui.act005.trip.TripViewModel
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toExtract
import com.namoadigital.prj001.util.ToolBox_Inf

class TripEventRepositoryImp constructor(
    private val context: Context,
    private val dao: FSEventTypeDao,
    private val eventDao: FSTripEventDao,
    private val tripDao: FSTripDao,
    private val fileDao: GE_FileDao
) : TripEventRepository {
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
        changePhoto: Int?,
        seq: Int?,
        eventStatus: EventStatus,
        dateStart: String?,
        dateEnd: String?
    ) {
        photoPath?.let { imagePath ->
            GE_File().apply {
                file_code = imagePath.replace(TripViewModel.JPG_EXTENSION, "")
                file_path = imagePath
                file_status = GE_File.OPENED
                file_date = getCurrentDateApi()
            }.let { fileModel ->
                fileDao.addUpdate(fileModel)
            }
            ToolBox_Inf.scheduleUploadImgWork(context)
        }

        tripDao.getTrip()?.let { trip ->

            FSTripEventEnv(
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
                changedPhoto = if(photoPath == null) null else changePhoto,
                eventStatus = eventStatus.name
            ).let { model ->

                context.sendToWebServiceReceiver<WBR_Event> {
                    Bundle().putApiRequest(model)
                }

            }

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
        var tripPrefix : Int? = null
        var tripCode : Int? = null
        var eventSeq : Int? = null
        event?.let {
            tripPrefix = it.tripPrefix
            tripCode = it.tripCode
            eventSeq = it.eventSeq
        }?:run{
            tripDao.getTrip()?.let {
                tripPrefix = it.tripPrefix
                tripCode = it.tripCode
                eventSeq = null
            }
        }

        if(tripPrefix != null
            && tripCode != null){

            val eventsList = eventDao.getEventListDateValidation(
                tripPrefix!!,
                tripCode!!,
                eventSeq
            )

            eventsList.forEach { validationEvent->
                val dateStart = ToolBox_Inf.dateToMilliseconds(validationEvent.event.eventStart)
                val dateEnd:Long? = validationEvent.event.eventEnd?.let{eventEnd->
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
        return  GetEventRestrictionDateUseCase.OutputParams(
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
        return if(extract.isNotEmpty()){
            extract[0]
        }else{
            null
        }
    }

}