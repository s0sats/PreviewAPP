package com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act095.event_manual.composable.CircularLoading
import com.namoadigital.prj001.ui.act095.event_manual.composable.EventTypeListScreen
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.EventManualDialogViewModel
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualDialogEvent
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey

sealed class EventUiState {
    object Loading : EventUiState()
    object ShowTypeList : EventUiState()
    data class ShowEventDetails(val eventData: EventManualData, val isEditMode: Boolean) :
        EventUiState()
}

@Composable
fun EventUIRoute(
    eventToEdit: EventManual? = null,
    onShowProgress: (String, String) -> Unit,
    onClose: () -> Unit,
) {
    val viewModel: EventManualDialogViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val translateMap = state.translate
    val uiState = state.eventUiState



    LaunchedEffect(Unit) {
        viewModel.onEvent(
            EventManualDialogEvent.UpdateDialogEventState(
                if (eventToEdit != null) {
                    EventUiState.ShowEventDetails(
                        eventData = eventToEdit.toScreenData(),
                        isEditMode = true
                    )
                } else {
                    EventUiState.ShowTypeList
                }
            )
        )
    }


    LaunchedEffect(state) {
        state.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        if (state.showProgress) {
            state.showProgress = false
            onShowProgress(
                translateMap.textOf(EventManualKey.SavingEventTitle),
                translateMap.textOf(EventManualKey.SavingEventMessage)
            )
        }

    }


    Card(
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = NamoaTheme.spacing.extraSmall
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {

        when (val screen = uiState) {
            EventUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularLoading()
                }
            }

            EventUiState.ShowTypeList -> EventTypeListScreen(
                onSelected = { eventType ->
                    val newEvent = EventManualData(
                        title = eventType.eventTypeDesc,
                        primaryData = EventManualData.PrimaryData(typeCode = eventType.eventTypeCode),
                        eventFieldConfig = EventManualData.EventFieldConfig().apply {
                            setFieldConfig(eventType)
                        }
                    )
                    viewModel.onEvent(
                        EventManualDialogEvent.UpdateDialogEventState(
                            EventUiState.ShowEventDetails(newEvent, false)
                        )
                    )
                },
                onClose = onClose
            )

            is EventUiState.ShowEventDetails -> EventManualScreen(
                state = screen.eventData!!,
                isEditMode = screen.isEditMode,
                isHistoric = screen.eventData.status == EventStatus.DONE,
                translateMap = translateMap,
                onClose = onClose,
                endDateError = state.endDateError,
                startDateError = state.startDateError,
                onDelete = {
                    viewModel.onEvent(
                        EventManualDialogEvent.SaveDialogEvent(
                            screen.eventData.copy(status = EventStatus.CANCELLED),
                            isEditMode = true
                        )
                    )
                },
                onDateSelected = { startDate, endDate ->
                    val waitAllowed = screen.eventData.eventFieldConfig.waitAllowed
                    viewModel.onEvent(
                        EventManualDialogEvent.ValidateDate(
                            startDate,
                            endDate,
                            screen.eventData.eventFieldConfig.waitAllowed
                        )
                    )
                },
                onSave = { data ->
                    viewModel.onEvent(
                        EventManualDialogEvent.SaveDialogEvent(
                            data = data,
                            isEditMode = screen.isEditMode,
                        )
                    )
                }
            )
        }
    }
}