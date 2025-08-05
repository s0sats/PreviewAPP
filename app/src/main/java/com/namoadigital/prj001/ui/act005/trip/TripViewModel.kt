package com.namoadigital.prj001.ui.act005.trip

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.core.trip.domain.usecase.GetDestinationByStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventRestrictionDateUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.LocationNotFound
import com.namoadigital.prj001.core.trip.domain.usecase.SaveDestinationDateUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SaveFleetDataUseCase.SaveFleetParams
import com.namoadigital.prj001.core.trip.domain.usecase.SaveOriginUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SetDestinationStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.TripStatusChangeUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.TripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SaveOverNightDestinationUseCase
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.trip.DestinationCounter
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.TripStatus
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
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment.Companion.WS_TRIP_OVER_NIGHT
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripWsProgress
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.SaveDestinationEdit
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.SaveOriginEdit
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.FSSaveEvent
import com.namoadigital.prj001.ui.act005.trip.util.ProgressState
import com.namoadigital.prj001.ui.act005.trip.util.TripState
import com.namoadigital.prj001.util.NetworkConnectionException
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
    savedState: SavedStateHandle = SavedStateHandle(),
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
    private val isExtractFrag: Boolean by lazy {
        ((savedState.get<Boolean?>("isExtract") != null)
                && (savedState.get<Boolean?>("isExtract") == true))
    }

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
        val listExtract = if (isExtractFrag) extractUseCase(Unit) else null
        //
        initTimeEvent()
        _state.update {
            TripState(
                trip = trip,
                destination = fsTripDestination,
                counter = counter,
                event = event,
                listExtract = listExtract,
            )
        }

    }


    private fun initTimeEvent() {
        val event = useCase.getEvent(Unit)
        if (event == null) {
            stopCountTimer()
            return
        }
        startCountTimer(event.eventStart!!)

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

    fun addDestinationOverNight(tripOverNightTtl: String?, tripOverNightMsg: String?) {

        viewModelScope.launch {
            state.value.trip?.let {
                destinationUseCase.saveOverNightDestinationUseCase?.invoke(
                    SaveOverNightDestinationUseCase.Input(
                        latitude = FsTripLocationService.LatLog.value.latitude,
                        longitude = FsTripLocationService.LatLog.value.longitude
                    )
                )?.results(
                    success = {
                        _state.loadingState { ProgressState.Offline }
                        _state.update { tripState ->
                            tripState.copy(
                                updateTripScreen = true
                            )
                        }
                    },
                    error = { errorMsg, exceptionError ->
                        exceptionError?.let {
                            _state.loadingState {
                                ProgressState.Error(exceptionError, errorMsg = errorMsg)
                            }
                            if(exceptionError is NetworkConnectionException){
                                _state.update { tripState ->
                                    tripState.copy(
                                        updateTripScreen = true
                                    )
                                }
                            }
                        }
                    },
                    loading = { isOnlineMode, _ ->
                        if (isOnlineMode) {
                            _state.loadingState {
                                ProgressState.Online(
                                    process = WS_TRIP_OVER_NIGHT,
                                    title = tripOverNightTtl?: TripTranslate.PROGRESS_TRIP_OVER_NIGHT_TTL,
                                    message = tripOverNightMsg ?: TripTranslate.PROGRESS_TRIP_OVER_NIGHT_MSG,
                                )
                            }
                        } else {
                            _state.loadingState {
                                ProgressState.Offline
                            }
                        }
                    },
                    failed = {
                        _state.loadingState { ProgressState.Offline }
                    }
                )
            }
        }

    }

    fun saveFleetData(
        odometer: Long?,
        changePhoto: Int,
        target: TripTarget,
        progressTranslate: TripWsProgress,
        fleetPlate: String? = null,
        path: String? = null,
        destinationSeq: Int? = null,
        deletePhoto: Boolean = false,
    ) {
        viewModelScope.launch {
            useCase.saveFleet(
                SaveFleetParams(
                    fleetPlate,
                    odometer,
                    path,
                    changePhoto,
                    target,
                    destinationSeq,
                    deletePhoto
                )
            ).results(
                success = {
                    if (target == TripTarget.END) {
                        setTripStatus(
                            TripStatus.DONE,
                            tripWsProgress = TripWsProgress(
                                process = progressTranslate.process,
                                title = progressTranslate.title,
                                message = progressTranslate.message
                            )
                        )
                        return@results
                    }
                    _state.loadingState { ProgressState.Offline }
                    getCurrentTrip()
                },
                loading = { _, _ ->
                    _state.loadingState {
                        ProgressState.Online(
                            process = progressTranslate.process,
                            title = progressTranslate.title,
                            message = progressTranslate.message
                        )
                    }
                },
                failed = { throwable ->
                    _state.loadingState { ProgressState.Error(throwable) }
                },
                error = { errorMsg, exceptionError ->
                    exceptionError?.let {
                        _state.loadingState {
                            ProgressState.Error(
                                exceptionError,
                                closeDialog = true,
                                errorMsg = errorMsg
                            )
                        }

                        if(exceptionError is NetworkConnectionException){
                            getCurrentTrip()
                        }
                    }
                }
            )

        }

    }


    /*    fun startTrip() {
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
        }*/

    fun setTripStatus(
        tripStatus: TripStatus,
        tripWsProgress: TripWsProgress
    ) {
        viewModelScope.launch {
            state.value.trip?.let {
                useCase.statusChange(
                    TripStatusChangeUseCase.Input(
                        tripStatus,
                    )
                ).results(
                    success = {
                        _state.loadingState { ProgressState.Offline }
                        getCurrentTrip()
                        _state.update { tripState ->
                            tripState.copy(
                                updateTripScreen = true
                            )
                        }
                    },
                    failed = { throwable ->
                        _state.loadingState {
                            ProgressState.Error(throwable)
                        }
                    },
                    error = { errorMsg, exceptionError ->
                        exceptionError?.let {
                            _state.loadingState {
                                ProgressState.Error(exceptionError, errorMsg = errorMsg)
                            }

                            if(exceptionError is NetworkConnectionException){
                                _state.update { tripState ->
                                    tripState.copy(
                                        updateTripScreen = true
                                    )
                                }
                            }
                        }
                    },
                    loading = { isOnline, _ ->
                        if (isOnline) {
                            _state.loadingState {
                                ProgressState.Online(
                                    process = tripWsProgress.process,
                                    title = tripWsProgress.title,
                                    message = tripWsProgress.message
                                )
                            }
                        } else {
                            _state.loadingState { ProgressState.Offline }
                        }
                    }
                )
            }
        }
    }

    fun getListSites(): List<HMAux> {
        return useCase.listSites(Unit)
    }

    fun saveOriginSet(
        date: String,
        siteCode: Int? = null,
        siteDesc: String? = null,
        originType: OriginType,
        progressTranslate: TripWsProgress,
        progressTranslateFleet: TripWsProgress,
        chainOriginAndFleet: SaveOriginEdit.FLEET? = null,
        locationNotFound: () -> Unit,
    ) {
        viewModelScope.launch {
            useCase.saveOrigin(
                SaveOriginUseCase.SaveOriginParam(
                    date = date,
                    type = originType,
                    siteCode = siteCode,
                    siteDesc = siteDesc
                )
            ).results(
                success = {
                    chainOriginAndFleet?.let {
                        saveFleetData(
                            fleetPlate = chainOriginAndFleet.fleet,
                            odometer = chainOriginAndFleet.odometer.toLong(),
                            path = chainOriginAndFleet.photoUpdate.path,
                            changePhoto = chainOriginAndFleet.photoUpdate.isNew,
                            deletePhoto = chainOriginAndFleet.photoUpdate.deletePhoto,
                            target = TripTarget.START,
                            progressTranslate = progressTranslateFleet
                        )
                        return@results
                    }
                    _state.loadingState { ProgressState.Offline }
                    getCurrentTrip()
                },
                loading = { isOnlineMode, _ ->
                    if (isOnlineMode) {
                        _state.loadingState {
                            ProgressState.Online(
                                process = progressTranslate.process,
                                title = progressTranslate.title,
                                message = progressTranslate.message
                            )
                        }

                        return@results
                    }

                    _state.loadingState { ProgressState.Offline }
                },
                error = { errorMsg, exceptionError ->
                    exceptionError?.let {
                        _state.loadingState {
                            ProgressState.Error(
                                exceptionError,
                                closeDialog = true,
                                errorMsg = errorMsg
                            )
                        }
                        if(exceptionError is NetworkConnectionException){
                            getCurrentTrip()
                        }
                    }
                },
                failed = { throwable ->
                    if (throwable is LocationNotFound) {
                        _state.loadingState { ProgressState.Error(throwable) }
                        locationNotFound()
                        return@results
                    }
                    _state.loadingState { ProgressState.Error(throwable) }
                }
            )
        }
    }

    fun getUsersAvailable() {
        userUseCase.getUsers(Unit)
    }

    fun editUser(
        user: TripUserEdit,
        userAction: UserAction,
        tripWsProgress: TripWsProgress
    ) {
        viewModelScope.launch {
            userUseCase.edit(
                ExecEditUserUseCase.ExecEditUserParams(user, userAction)
            ).results(
                success = {
                    _state.loadingState { ProgressState.Offline }
                    getCurrentTrip()
                },
                loading = { _, _ ->
                    _state.loadingState {
                        ProgressState.Online(
                            process = tripWsProgress.process,
                            title = tripWsProgress.title,
                            message = tripWsProgress.message
                        )
                    }
                },
                failed = { throwable ->
                    _state.loadingState {
                        ProgressState.Error(throwable)
                    }
                    //
                },
                error = { errorMsg, exceptionError ->
                    exceptionError?.let {
                        _state.loadingState {
                            ProgressState.Error(exceptionError, errorMsg = errorMsg)
                        }
                        if(exceptionError is NetworkConnectionException){
                            getCurrentTrip()
                        }
                    }
                }
            )
        }
    }

    fun updateEvent(
        event: FSSaveEvent,
        tripWsProgress: TripWsProgress
    ) {
        viewModelScope.launch {
            eventUseCase.save(event)
                .results(
                    success = {
                        _state.loadingState { ProgressState.Offline }
                        getCurrentTrip()
                    },
                    loading = { _, _ ->
                        _state.loadingState {
                            ProgressState.Online(
                                process = tripWsProgress.process,
                                title = tripWsProgress.title,
                                message = tripWsProgress.message
                            )
                        }

                    },
                    error = { errorMsg, exceptionError ->
                        exceptionError?.let {
                            _state.loadingState {
                                ProgressState.Error(
                                    exceptionError,
                                    closeDialog = true,
                                    errorMsg = errorMsg
                                )
                            }
                        }
                    },
                    failed = { throwable ->
                        _state.loadingState { ProgressState.Error(throwable) }
                    }
                )
        }
    }

    fun setDestinationStatus(
        destinationStatus: DestinationStatus,
        destination: FsTripDestination?,
        tripWsProgress: TripWsProgress
    ) {
        var isOnlineMode = false
        viewModelScope.launch {
            destination?.let {
                destinationUseCase.setDestinationStatusUseCase?.invoke(
                    SetDestinationStatusUseCase.Params(
                        it.destinationSeq,
                        destinationStatus,
                    )
                )?.results(
                    success = { _ ->
                        _state.loadingState { ProgressState.Offline }
                        _state.update { tripState ->
                            tripState.copy(
                                updateTripScreen = true
                            )
                        }
                    },
                    loading = { _, _ ->
                        _state.loadingState {
                            ProgressState.Online(
                                process = tripWsProgress.process,
                                title = tripWsProgress.title,
                                message = tripWsProgress.message
                            )
                        }
                    },
                    failed = { throwable ->
                        _state.loadingState {
                            ProgressState.Error(throwable)
                        }

                    },
                    error = { errorMsg, exceptionError ->
                        exceptionError?.let {
                            _state.loadingState {
                                ProgressState.Error(exceptionError, errorMsg = errorMsg)
                            }
                            if(exceptionError is NetworkConnectionException){
                                _state.update { tripState ->
                                    tripState.copy(
                                        updateTripScreen = true
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
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


    fun getDestinationSeq() = _state.value.destination?.destinationSeq

    fun getTicketInfo(ticketPrefix: Int?, ticketCode: Int?): TK_Ticket? {
        return null
    }

    fun saveDestinationDate(
        dateStart: String,
        dateEnd: String,
        destinationSeq: Int,
        chainDestinationAndFleet: SaveDestinationEdit.ODOMETER? = null,
        progressTranslate: TripWsProgress,
        progressTranslateFleet: TripWsProgress? = null,
    ) {

        viewModelScope.launch {
            destinationUseCase.saveDestinationDateUseCase?.invoke(
                SaveDestinationDateUseCase.SaveDestinationDateParams(
                    dateStart,
                    dateEnd,
                    destinationSeq
                )
            )?.results(
                success = {
                    chainDestinationAndFleet?.let { chainDestinationAndFleet ->
                        saveFleetData(
                            odometer = chainDestinationAndFleet.odometer,
                            path = chainDestinationAndFleet.photoUpdate.path,
                            changePhoto = chainDestinationAndFleet.photoUpdate.isNew,
                            destinationSeq = chainDestinationAndFleet.destinationSeq,
                            target = TripTarget.DESTINATION,
                            deletePhoto = chainDestinationAndFleet.photoUpdate.deletePhoto,
                            progressTranslate = TripWsProgress(
                                process = progressTranslateFleet!!.process,
                                title = progressTranslateFleet.title,
                                message = progressTranslateFleet.message,
                            )
                        )
                        return@results
                    }
                    _state.loadingState { ProgressState.Offline }
                    getCurrentTrip()

                },
                loading = { isOnline, _ ->
                    _state.loadingState {

                        ProgressState.Online(
                            process = progressTranslate.process,
                            title = progressTranslate.title,
                            message = progressTranslate.message
                        )
                    }
                },
                failed = { throwable ->
                    _state.loadingState { ProgressState.Error(throwable) }
                },
                error = { errorMsg, exceptionError ->
                    exceptionError?.let {
                        _state.loadingState {
                            ProgressState.Error(
                                exceptionError,
                                closeDialog = true,
                                errorMsg = errorMsg
                            )
                        }

                        if(exceptionError is NetworkConnectionException){
                            getCurrentTrip()
                        }
                    }

                }
            )
        }
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

    fun getAlertTypeFromPreference(pref: CurrentTripPref, tripStatus: TripStatus): Int? {
        val model = pref.read()

        return when (model.last_alert_type.toAlertType()) {
            TripPositionAlertType.ARRIVED -> {
                if (tripStatus == TripStatus.TRANSIT) {
                    TripBaseFragment.TRIP_WARNING_ARRIVED_TO_SITE
                } else {
                    TripBaseFragment.TRIP_WARNING_REMOVE
                }
            }

            TripPositionAlertType.DEPARTED -> {
                if (tripStatus == TripStatus.ON_SITE) {
                    TripBaseFragment.TRIP_WARNING_DEPARTED_FROM_SITE
                } else if (tripStatus == TripStatus.PENDING) {
                    TripBaseFragment.TRIP_WARNING_DEPARTED_FROM_ORIGIN
                } else {
                    TripBaseFragment.TRIP_WARNING_REMOVE
                }
            }

            TripPositionAlertType.WAITING_DESTINATION -> {
                if (tripStatus == TripStatus.WAITING_DESTINATION &&
                    model.waiting_destination_date != null
                ) {
                    TripBaseFragment.TRIP_WARNING_WAITING_DESTINATION
                } else {
                    TripBaseFragment.TRIP_WARNING_REMOVE
                }
            }

            TripPositionAlertType.PENDING -> {
                if (tripStatus == TripStatus.PENDING) {
                    TripBaseFragment.TRIP_WARNING_DEPARTED_FROM_ORIGIN
                } else {
                    TripBaseFragment.TRIP_WARNING_REMOVE
                }
            }

            TripPositionAlertType.NULL -> {
                TripBaseFragment.TRIP_WARNING_REMOVE
            }
        }
    }

    fun checkUserIntersection(
        userCode: Int,
        userSeq: Int?,
        startDateInMillis: Long,
        endDateInMillis: Long?
    ): OutputParams {
        return userUseCase.intersection(
            InputParams(userCode, userSeq, startDateInMillis, endDateInMillis)
        )
    }

    fun validateOriginDate(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
    ): String? {
        return validateOrigin(
            GetFirstDateOnTripUseCase.InputParam(
                customerCode,
                tripPrefix,
                tripCode,
            )
        ).dateError
    }

    fun removeLoadingState() {
        _state.loadingState { ProgressState.Hide(true) }
    }

    fun getOverNightDestination(fsTrip: FSTrip): FsTripDestination? {
        return destinationUseCase.destinationByStatus?.invoke(
            GetDestinationByStatusUseCase.GetDestinationParams(
                fsTrip.customerCode,
                fsTrip.tripPrefix,
                fsTrip.tripCode,
                DestinationStatus.ARRIVED
            )
        )
    }

    private inline fun <T> MutableStateFlow<T>.loadingState(block: (T) -> ProgressState) {
        _state.update {
            it.copy(
                progressState = block(this.value)
            )
        }
    }

    fun hasTripWithUpdateRequired(): Boolean {
        return useCase.hasTripWithUpdateRequired(Unit)
    }


    companion object {
        const val FS_PREFIX = "fs_"
        const val TEMP_PREFIX = "temp_"
        const val JPG_EXTENSION = ".jpg"
    }

}