package com.namoadigital.prj001.ui.act005.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.core.trip.domain.usecase.GetDestinationByStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventRestrictionDateUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SaveDestinationDateUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SaveFleetDataUseCase.SaveFleetParams
import com.namoadigital.prj001.core.trip.domain.usecase.SaveOriginUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.TripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SelectDestinationUseCase
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.trip.DestinationCounter
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestination.Companion.OVER_NIGHT_DESTINATION_TYPE
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripStatusChangeEnv
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.model.trip.toAlertType
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.service.location.FsTripLocationService
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.di.usecase.event.GetEventTypeUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.event.TripEventUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.extract.ListExtractUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.origin.GetFirstDateOnTripUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.ExecEditUserUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.InputParams
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.OutputParams
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.TripUsersUseCase
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.FSSaveEvent
import com.namoadigital.prj001.ui.act005.trip.util.TripState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class TripViewModel @Inject constructor(
    private val useCase: TripUseCase,
    private val destinationUseCase: DestinationUseCase,
    private val userUseCase: TripUsersUseCase,
    private val eventUseCase: TripEventUseCase,
    private val extractUseCase: ListExtractUseCase,
    private val validateOrigin: GetFirstDateOnTripUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TripState())
    val state = _state.asStateFlow()

    private var startTime: Date? = null
    val beforeTime = MutableStateFlow("")
    private var timerCount: Job? = null


    init {
        getCurrentTrip()
    }


    fun getCurrentTrip() {
        //
        val trip = useCase.trip(Unit)
        val fsTripDestination = trip?.let {
            val destinationStatus = when (it.tripStatus.toTripStatus()) {
                TripStatus.PENDING -> {
                    DestinationStatus.PENDING
                }

                TripStatus.TRANSIT -> {
                    DestinationStatus.TRANSIT
                }

                TripStatus.OVER_NIGHT -> {
                    DestinationStatus.PENDING
                }

                TripStatus.ON_SITE -> {
                    DestinationStatus.ARRIVED
                }

                else -> {
                    null
                }
            }
            //
            destinationStatus?.let {
                destinationUseCase.destinationByStatus?.invoke(
                    GetDestinationByStatusUseCase.GetDestinationParams(
                        trip.customerCode,
                        trip.tripPrefix,
                        trip.tripCode,
                        destinationStatus
                    )
                )
            }
        }
        val counter: DestinationCounter? = fsTripDestination?.let { tripDestination ->
            getDestinationCounter(
                tripDestination
            ) ?: DestinationCounter(
                "-",
                "-",
                "-",
                "-",
                tripDestination.serialCnt.toString()
            )
        }
        val event = useCase.getEvent(Unit)
        //
        _state.update {
            it.copy(
                trip = trip,
                destination = fsTripDestination,
                counter = counter,
                event = event
            )
        }

        initTimeEvent()

    }


    private fun initTimeEvent() {
        if (_state.value.event == null) {
            stopCountTimer()
            return
        }
        startCountTimer(_state.value.event?.eventStart!!)

    }

    private fun startCountTimer(data: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ", Locale.getDefault())
        startTime = formatter.parse(data)

        timerCount?.cancel()
        timerCount = viewModelScope.launch {
            while (true) {
                beforeTime.value = calculatePastTime()
                delay(TimeUnit.MINUTES.toMillis(1))
            }
        }
    }

    private fun stopCountTimer() {
        timerCount?.cancel()
    }

    private fun calculatePastTime(): String {
        val duration = Calendar.getInstance().time.time - (startTime?.time ?: 0)

        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours)

        return if (hours > 0) {
            String.format(Locale.getDefault(), "%dh %02d min", hours, minutes)
        } else {
            String.format(Locale.getDefault(), "%d min", minutes)
        }
    }


    fun getListEventType(): List<FSEventType> {
        return eventUseCase.listEventTypes(Unit)
    }

    fun getListEventType(customerCode: Long, tripEventTypeCode: Int): FSEventType? {
        return eventUseCase.getEventType(
            GetEventTypeUseCase.InputParams(
                customerCode,
                tripEventTypeCode
            )
        )
    }

    private fun getDestinationCounter(fsTripDestination: FsTripDestination): DestinationCounter? {
        return destinationUseCase.getDestinationCounter?.invoke(fsTripDestination)
    }

    fun addDestinationOverNight() {
        state.value.trip?.let {
            destinationUseCase.execSelectDestination?.invoke(
                SelectDestinationUseCase.SelectDestinationParam(
                    it.tripPrefix,
                    it.tripCode,
                    it.scn,
                    OVER_NIGHT_DESTINATION_TYPE,
                    currentLat = FsTripLocationService.LatLog.value.latitude,
                    currentLon = FsTripLocationService.LatLog.value.longitude
                )
            )
        }
    }

    fun saveFleetData(
        fleetPlate: String? = null,
        odometer: Long?,
        path: String? = null,
        changePhoto: Int,
        target: TripTarget,
        destinationSeq: Int? = null,
        deletePhoto: Boolean = false,
    ) {
        useCase.saveFleet(
            SaveFleetParams(fleetPlate, odometer, path, changePhoto, target, destinationSeq, deletePhoto)
        )

    }


    fun startTrip() {
        //t
        val tripStatus = state.value.destination?.let {
            TripStatus.TRANSIT
        } ?: TripStatus.TRANSFER
        //
        state.value.trip?.let {
            if (!it.isRequireDestinationFleetData
                || TripStatus.CANCELLED == tripStatus
                || TripStatus.TRANSIT == tripStatus
            ) {
                setTripStatus(tripStatus)
            }
        }
        //
    }

    fun setTripStatus(tripStatus: TripStatus, doneDate: String? = null) {
        state.value.trip?.let {
            val modelRequest = TripStatusChangeEnv(
                tripPrefix = it.tripPrefix,
                tripCode = it.tripCode,
                scn = it.scn,
                tripStatus = tripStatus.toDescription(),
                doneDate = doneDate
            )
            //
            useCase.statusChange(
                modelRequest
            )
        }

    }

    fun getListSites(): List<HMAux> {
        return useCase.listSites(Unit)
    }

    /**
     *  BARRIONUEVO - 10-04-2024
     *  Adicionada as HOFs(activateWsProgressDialog e locationNotFound) para tratativa
     *  de edicao de origin, ajuste rapido devido ao tempo do projeto
     */
    fun saveOriginSet(
        date: String,
        siteCode: Int? = null,
        siteDesc: String? = null,
        originType: OriginType,
        activateWsProgressDialog: () -> Unit,
        locationNotFound: () -> Unit,
    ) {
        when (originType) {
            OriginType.GPS -> {
                viewModelScope.launch {
                    FsTripLocationService.LatLog.value
                        .let {
                            if (!it.isDefault) {
                                activateWsProgressDialog()
                                useCase.saveOrigin(
                                    SaveOriginUseCase.SaveOriginParam(
                                        date = date,
                                        type = originType,
                                        coordinates = it
                                    )
                                )
                            } else {
                                locationNotFound()
                            }
                        }
                }
            }

            OriginType.SITE -> {
                activateWsProgressDialog()
                useCase.saveOrigin(
                    SaveOriginUseCase.SaveOriginParam(
                        date = date,
                        type = originType,
                        siteCode = siteCode,
                        siteDesc = siteDesc
                    )
                )
            }

            OriginType.EDIT -> {
                activateWsProgressDialog()
                useCase.saveOrigin(
                    SaveOriginUseCase.SaveOriginParam(
                        date = date,
                        type = originType,
                    )
                )
            }
        }
    }

    fun getUsersAvailable() {
        userUseCase.getUsers(Unit)
    }

    fun editUser(user: TripUserEdit, userAction: UserAction) {
        userUseCase.edit(ExecEditUserUseCase.ExecEditUserParams(user, userAction))
    }

    fun updateEvent(event: FSSaveEvent) {
        eventUseCase.save(event)
    }

    fun setDestinationStatus(
        destinationStatus: DestinationStatus,
        destination: FsTripDestination?
    ) {
        val modelEnv = state.value.trip?.let { trip ->
            destination?.let {
                TripDestinationStatusChangeEnv(
                    it.tripPrefix,
                    it.tripCode,
                    it.destinationSeq,
                    trip.scn,
                    destinationStatus.toDescription(),
                )
            }
        }
        //
        modelEnv?.let {
            destinationUseCase.setDestinationStatusUseCase?.invoke(it)
        }
        //
    }

    private fun checkFleetData(trip: FSTrip): Boolean {
        return !trip.isRequiredFleetData
                ||
                (trip.fleetStartOdometer != null
                        && trip.fleetStartPhoto != null)
    }

    private fun checkDestinationData(trip: FSTrip, destination: FsTripDestination?): Boolean {
        return !trip.isRequireDestinationFleetData
                ||
                (destination?.destinationStatus == DestinationStatus.PENDING.toDescription())
    }

    fun checkStartTrip(): Boolean {
        val trip = state.value.trip
        trip?.let {
            val destination = state.value.destination
            if (checkFleetData(it)
                && checkDestinationData(it, destination)
            ) {
                return true
            }
        }
        return false
    }

    fun getListExtract(): List<Extract<*>> {
        return extractUseCase(Unit)
    }

    fun getDestinationSeq() = _state.value.destination?.destinationSeq

    fun getListOdometerArrived() = destinationUseCase.listOdometerArrived?.invoke(Unit)
    fun getTicketInfo(ticketPrefix: Int?, ticketCode: Int?): TK_Ticket? {
        return null
    }

    fun saveDestinationDate(
        dateStart: String,
        dateEnd: String,
        destinationSeq: Int
    ) {

        destinationUseCase.saveDestinationDateUseCase?.invoke(
            SaveDestinationDateUseCase.SaveDestinationDateParams(
                dateStart,
                dateEnd,
                destinationSeq
            )
        )

    }

    fun getDestinationThresholds(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType = GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.BOTH
    ): Pair<FsTripDestination?, FsTripDestination?> {
        val seq = if (destinationSeq == -1) null else destinationSeq
        val destinationForThresholdValidation =
            destinationUseCase.getDestinationForThresholdValidationUseCase?.invoke(
                GetDestinationForThresholdValidationUseCase.InputParam(
                    customerCode,
                    tripPrefix,
                    tripCode,
                    seq,
                    type
                )
            )
        destinationForThresholdValidation?.let {
            return Pair(
                destinationForThresholdValidation.previousDestination,
                destinationForThresholdValidation.nextDestination
            )
        }
        //
        return Pair(null, null)
    }

    fun getEventError(
        startDateInMilis: Long,
        endDateInMilis: Long?,
        event: FSTripEvent?,
        waiting: Boolean
    ): GetEventRestrictionDateUseCase.OutputParams {

        return eventUseCase.getEventRestrictionDate(
            GetEventRestrictionDateUseCase.InputParams(
                startDateInMilis,
                endDateInMilis,
                event,
                waiting
            )
        )
    }

    fun getAlertTypeFromPreference(pref:CurrentTripPref, tripStatus: TripStatus): Int? {
        val model = pref.read()

        return when (model.last_alert_type.toAlertType()) {
            TripPositionAlertType.ARRIVED -> {
                if(tripStatus == TripStatus.TRANSIT) {
                    TripBaseFragment.TRIP_WARNING_ARRIVED_TO_SITE
                }else{
                    TripBaseFragment.TRIP_WARNING_REMOVE
                }
            }
            TripPositionAlertType.DEPARTED -> {
                if(tripStatus == TripStatus.ON_SITE) {
                    TripBaseFragment.TRIP_WARNING_DEPARTED_FROM_SITE
                }else if(tripStatus == TripStatus.PENDING) {
                    TripBaseFragment.TRIP_WARNING_DEPARTED_FROM_ORIGIN
                }else{
                    TripBaseFragment.TRIP_WARNING_REMOVE
                }
            }
            TripPositionAlertType.WAITING_DESTINATION -> {
                if(tripStatus == TripStatus.WAITING_DESTINATION &&
                    model.waiting_destination_date != null) {
                    TripBaseFragment.TRIP_WARNING_WAITING_DESTINATION
                }else{
                    TripBaseFragment.TRIP_WARNING_REMOVE
                }
            }
            TripPositionAlertType.PENDING -> {
                if(tripStatus == TripStatus.PENDING) {
                    TripBaseFragment.TRIP_WARNING_DEPARTED_FROM_ORIGIN
                }else{
                    TripBaseFragment.TRIP_WARNING_REMOVE
                }
            }
            TripPositionAlertType.NULL -> {
                TripBaseFragment.TRIP_WARNING_REMOVE
            }
        }
    }

    fun checkUserIntersection(userCode: Int, userSeq: Int?, startDateInMillis: Long, endDateInMillis: Long?): OutputParams {
        return userUseCase.intersection(
            InputParams(userCode, userSeq, startDateInMillis, endDateInMillis)
        )
    }

    fun validateOriginDate(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
    ):String?{
        return validateOrigin(
            GetFirstDateOnTripUseCase.InputParam(
            customerCode,
            tripPrefix,
            tripCode,
            )
        ).dateError
    }




    companion object {
        const val FS_PREFIX = "fs_"
        const val TEMP_PREFIX = "temp_"
        const val JPG_EXTENSION = ".jpg"
    }

}