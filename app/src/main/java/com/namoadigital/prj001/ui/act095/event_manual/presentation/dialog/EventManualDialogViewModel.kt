package com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog

import androidx.lifecycle.viewModelScope
import com.namoadigital.prj001.core.domain.usecase.form_local.HasFormInProcessUseCase
import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.di.EventTranslate
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.core.viewmodel.BaseViewModel
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.EventManualUseCases
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.SaveEventManualUseCase
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.ValidateDateEventUseCase
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualDialogEvent
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualDialogState
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.ui.EventUiState
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.NetworkConnectionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EventManualDialogViewModel @Inject constructor(
    @EventTranslate translateBuild: TranslateBuild,
    private val useCases: EventManualUseCases,
    private val validate: ValidateDateEventUseCase,
    private val hasFormInProcess: HasFormInProcessUseCase
) : BaseViewModel<EventManualDialogState, EventManualDialogEvent>(
    initialState = EventManualDialogState(screensLoading = true, isLoading = true),
    translateBuild = translateBuild,
    applyTranslation = { state, translate -> state.copy(translate = translate) }
) {


    init {
        viewModelScope.launch {
            val translate = loadTranslation()
            updateState {
                it.copy(
                    translate = translate,
                )
            }
        }

        hasFormInProcess()
    }


    override fun onEvent(event: EventManualDialogEvent) {
        when (event) {
            is EventManualDialogEvent.SaveDialogEvent -> saveEvent(
                data = event.data,
                isEditMode = event.isEditMode
            )

            is EventManualDialogEvent.GetListDialogEventType -> getListEventType()

            is EventManualDialogEvent.UpdateDialogEventData -> {
                _uiState.update {
                    it.copy(
                        eventData = event.data,
                        screensLoading = false
                    )
                }
            }

            is EventManualDialogEvent.ValidateDate -> validateDate(
                startDate = event.startDate,
                endDate = event.endDate,
                withWaiting = event.withWaiting
            )

            is EventManualDialogEvent.UpdateDialogEventState -> {

                if (event.uiState is EventUiState.ShowEventDetails) {
                    updateState {
                        it.copy(
                            eventData = event.uiState.eventData,
                        )
                    }
                }

                updateState {
                    it.copy(
                        eventUiState = event.uiState,
                        screensLoading = false
                    )
                }
            }
        }
    }

    private fun validateDate(
        startDate: String,
        endDate: String?,
        withWaiting: Boolean
    ) {
        viewModelScope.launch {

            val sdf =
                SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT, Locale.getDefault())
            val now = Date()

            val startParsed = runCatching { sdf.parse(startDate) }.getOrNull()
            val endParsed = runCatching { endDate?.let { sdf.parse(it) } }.getOrNull()

            if (startParsed != null && startParsed.after(now)) {
                updateState {
                    it.copy(
                        startDateError = EventManualDialogState.ErrorDate(EventManualKey.ErrorInvalidFutureStartDateMsg),
                        endDateError = null
                    )
                }
                return@launch
            }

            if (endParsed != null && endParsed.after(now)) {
                updateState {
                    it.copy(
                        endDateError = EventManualDialogState.ErrorDate(EventManualKey.ErrorInvalidFutureEndDateMsg),
                        startDateError = null
                    )
                }
                return@launch
            }

            if (startParsed != null && endParsed != null && endParsed.before(startParsed)) {
                updateState {
                    it.copy(
                        startDateError = EventManualDialogState.ErrorDate(EventManualKey.ErrorStartEndConflictDateMsg),
                        endDateError = EventManualDialogState.ErrorDate(EventManualKey.ErrorStartEndConflictDateMsg)
                    )
                }
                return@launch
            }

            val output = if(withWaiting) validate(
                input = ValidateDateEventUseCase.Input(
                    currentSeq = _uiState.value.eventData?.primaryData?.eventDaySeq,
                    startDate = startDate,
                    endDate = endDate,
                    eventDay = _uiState.value.eventData?.primaryData?.eventDay
                )
            ) else null

            updateState {
                when (output?.fieldWithError) {
                    ValidateDateEventUseCase.FieldErrorType.START_DATE -> it.copy(
                        startDateError = EventManualDialogState.ErrorDate(
                            output.type.key, output.date
                        ),
                        endDateError = null
                    )

                    ValidateDateEventUseCase.FieldErrorType.END_DATE -> it.copy(
                        startDateError = null,
                        endDateError = EventManualDialogState.ErrorDate(
                            output.type.key, output.date
                        )
                    )

                    ValidateDateEventUseCase.FieldErrorType.BOTH -> it.copy(
                        startDateError = EventManualDialogState.ErrorDate(
                            output.type.key, output.date
                        ),
                        endDateError = EventManualDialogState.ErrorDate(
                            output.type.key, output.date
                        )
                    )

                    else -> it.copy(
                        startDateError = null,
                        endDateError = null
                    )
                }
            }
        }
    }


    private fun getListEventType() {
        viewModelScope.launch {
            useCases.listEventType(Unit).suspendResults(
                success = { list ->
                    updateState {
                        it.copy(
                            listEventType = list,
                            isLoading = false,
                        )
                    }
                },
                loading = { _, _ ->
                    updateState {
                        it.copy(
                            isLoading = true,
                            screensLoading = false
                        )
                    }
                },
            )
        }
    }

    private fun saveEvent(
        data: EventManualData,
        isEditMode: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.save(
                input = SaveEventManualUseCase.Params(
                    data = data,
                    isEditMode = isEditMode
                )
            ).collect { collect ->

                collect.watchStatus(
                    success = { event ->
                        updateState {
                            it.copy(
                                showProgress = true,
                            )
                        }
                    },
                    failed = { throwable ->

                        updateState {
                            it.copy(
                                showProgress = false,
                                errorMessage = throwable.takeIf { takeIf -> takeIf is NetworkConnectionException }
                                    ?.let { msg ->
                                        it.translate.textOf(msg.message!!)
                                    } ?: throwable.message
                            )
                        }
                    }
                )

            }
        }
    }


    private fun hasFormInProcess(){
        viewModelScope.launch {
            val hasForm = hasFormInProcess(Unit)
            updateState {
                it.copy(
                    hasFormInProcess = hasForm.isNotEmpty()
                )
            }
        }
    }


}