package com.namoadigital.prj001.ui.act095.event_manual.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.EventManualDialogViewModel
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualDialogEvent
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey

@Composable
fun EventTypeListScreen(
    onSelected: (FSEventType) -> Unit,
    onClose: () -> Unit
) {

    val viewModel = hiltViewModel<EventManualDialogViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = state.listEventType
    val translateMap = state.translate

    var searchText by remember { mutableStateOf("") }

    val eventsFiltered = events.filter {
        it.eventTypeDesc.contains(searchText, ignoreCase = true)
    }

    val isLoading = state.isLoading

    LaunchedEffect(Unit) {
        viewModel.onEvent(EventManualDialogEvent.GetListDialogEventType)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(NamoaTheme.spacing.medium),
        verticalArrangement = if (isLoading) Arrangement.Center else Arrangement.Top,
        horizontalAlignment = if (isLoading) Alignment.CenterHorizontally else Alignment.Start
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = translateMap.textOf(EventManualKey.EventTypeTitleLbl),
                style = NamoaTheme.typography.titleLarge
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fechar")
            }
        }

        Spacer(modifier = Modifier.height(NamoaTheme.spacing.small))

        // Campo de pesquisa
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text(text = translateMap.textOf(EventManualKey.EventTypeSearchHintLbl)) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Pesquisar")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))

        if (isLoading) {
            CircularLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = NamoaTheme.spacing.medium)
            )
        } else {
            ListEventsTypeComponent(
                eventsFiltered = eventsFiltered,
                translateMap = translateMap,
                hasFormInProcess = state.hasFormInProcess,
                onSelected = onSelected,
                events = events
            )
        }
    }


}

@Composable
private fun ListEventsTypeComponent(
    eventsFiltered: List<FSEventType>,
    translateMap: TranslateMap,
    hasFormInProcess: Boolean,
    onSelected: (FSEventType) -> Unit,
    events: List<FSEventType>
) {
    if (eventsFiltered.isEmpty()) {
        Text(
            modifier = Modifier
                .padding(top = NamoaTheme.spacing.medium)
                .fillMaxWidth(),
            text = translateMap.textOf(EventManualKey.TypeEventEmpty),
            style = NamoaTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(eventsFiltered) { event ->

            val isItemEnabled = !(hasFormInProcess && event.isWaitAllowed)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (isItemEnabled) 1f else 0.55f),
                shape = NamoaTheme.shapes.small,
                colors = CardDefaults.cardColors(
                    containerColor = if (isItemEnabled)
                        Color.White
                    else
                        colorResource(id = R.color.namoa_color_gray_7)
                ),
                enabled = isItemEnabled,
                onClick = { onSelected(event) }
            ) {
                Column(
                    modifier = Modifier.padding(
                        vertical = NamoaTheme.spacing.mediumSmall,
                        horizontal = NamoaTheme.spacing.small
                    )
                ) {
                    Text(
                        text = event.eventTypeDesc,
                        style = NamoaTheme.typography.bodyMedium,
                        color = if (isItemEnabled)
                            NamoaTheme.colors.onSurface
                        else
                            NamoaTheme.colors.onSurfaceVariant
                    )
                }
            }

            if (events.last() != event) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = NamoaTheme.spacing.medium)
                )
            }
        }
    }
}


