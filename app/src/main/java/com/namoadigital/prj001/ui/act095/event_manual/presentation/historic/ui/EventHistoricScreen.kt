package com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.ui

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.EventHistoryViewModel
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventHistoryScreen(
    viewModel: EventHistoryViewModel,
    callbackTranslateMap: (TranslateMap) -> Unit,
    onEdit: (EventManual) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val translateMap = state.translate
    val events = state.listHistoric
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current as Activity?

    LaunchedEffect(translateMap) {
        callbackTranslateMap(translateMap)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = NamoaTheme.colors.primary,
                    titleContentColor = NamoaTheme.colors.surface,
                ),
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = { context?.finish() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = NamoaTheme.colors.surface
                        )
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = translateMap.textOf(key = EventManualKey.EventHistoryTitle)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .animateContentSize()
                .padding(NamoaTheme.spacing.medium)
        ) {

            OutlinedTextField(
                enabled = events.isNotEmpty(),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(translateMap.textOf(key = EventManualKey.SearchEventHistoryHint)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = NamoaTheme.spacing.mediumSmall)
            )

            val filteredEvents = events.filter {
                it.description.contains(searchQuery, ignoreCase = true) ||
                        context?.formatDate(FormatDateType.OnlyDate(it.dateStart))
                            ?.contains(searchQuery, ignoreCase = true) == true
            }

            if (!state.isLoading) {
                listEvents(filteredEvents, translateMap, onEdit)
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun listEvents(
    filteredEvents: List<EventManual>,
    translateMap: TranslateMap,
    onEdit: (EventManual) -> Unit
) {
    if (filteredEvents.isEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = NamoaTheme.spacing.medium),
            text = translateMap.textOf(key = EventManualKey.EventHistoryEmpty),
            style = NamoaTheme.typography.bodyMedium,
            color = NamoaTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = NamoaTheme.shapes.small),
            verticalArrangement = Arrangement.spacedBy(NamoaTheme.spacing.extraSmall),
        ) {
            items(
                key = { it.daySeq!! },
                items = filteredEvents,
            ) { event ->
                EventCard(event, translateMap = translateMap, onEdit = onEdit)
            }
        }
    }
}


@Composable
fun EventCard(
    item: EventManual,
    translateMap: TranslateMap,
    onEdit: (EventManual) -> Unit,
) {

    val context = LocalContext.current
    val start = context.formatDate(FormatDateType.DateAndHour(item.dateStart))
    val end = context.formatDate(FormatDateType.DateAndHour(item.dateEnd!!))
    val currentDay = SimpleDateFormat("yyyyMMdd").format(Date()).toInt()
    val isCurrentDay = currentDay == item.eventDay

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(NamoaTheme.spacing.small),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.m3_namoa_neutral95)),
        elevation = CardDefaults.cardElevation(defaultElevation = NamoaTheme.spacing.extraExtraSmall)
    ) {

        Column(
            modifier = Modifier
                .clickable(enabled = isCurrentDay) {
                    onEdit(item)
                }
                .padding(NamoaTheme.spacing.medium)
        ) {
            Column {
                Text(
                    text = "$start - $end",
                    style = NamoaTheme.typography.titleSmall.copy(
                        color = NamoaTheme.colors.onSurfaceVariant
                    )
                )
            }


            Spacer(modifier = Modifier.height(NamoaTheme.spacing.large))

            // Nome do evento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.description,
                    style = NamoaTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                if (item.photo.hasPhoto()) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))

            if (!item.cost.isNullOrBlank()) {
                Text(
                    modifier = Modifier.padding(top = NamoaTheme.spacing.extraExtraSmall),
                    text = "${translateMap.textOf(key = EventManualKey.CostLbl)} ${item.cost}",
                    style = NamoaTheme.typography.bodyMedium,
                    color = NamoaTheme.colors.onSurfaceVariant,
                )
            }

            if (!item.comments.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(NamoaTheme.spacing.small))
                Text(
                    text = item.comments,
                    style = NamoaTheme.typography.bodyMedium,
                    color = NamoaTheme.colors.onSurfaceVariant,
                )
            }

            if (!item.eventSiteDesc.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(NamoaTheme.spacing.small))
                Text(
                    text = item.eventSiteDesc,
                    style = NamoaTheme.typography.bodyMedium,
                    color = NamoaTheme.colors.onSurfaceVariant,
                )
            }

            if (isCurrentDay) {
                Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))

                Button(
                    onClick = { onEdit(item) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = translateMap.textOf(EventManualKey.EditBtn))
                }
            }
        }
    }
}

