@file:OptIn(ExperimentalMaterial3Api::class)

package com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.extensions.callCameraAct
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.DateTimePicker
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.DateTimeSelected
import com.namoadigital.prj001.ui.act095.event_manual.composable.EventFormField
import com.namoadigital.prj001.ui.act095.event_manual.composable.PhotoSection
import com.namoadigital.prj001.ui.act095.event_manual.composable.controller.PhotoController
import com.namoadigital.prj001.ui.act095.event_manual.composable.controller.rememberPhotoController
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualDialogState
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import com.namoadigital.prj001.ui.act095.event_manual.util.ValidateEventManualField
import com.namoadigital.prj001.ui.act095.event_manual.util.validateEventManualFields

@Composable
fun EventManualScreen(
    state: EventManualData,
    startDateError: EventManualDialogState.ErrorDate? = null,
    endDateError: EventManualDialogState.ErrorDate? = null,
    isEditMode: Boolean = false,
    isHistoric: Boolean = false,
    translateMap: TranslateMap,
    onDelete: () -> Unit,
    onSave: (EventManualData) -> Unit,
    onDateSelected: (startDate: String, endDate: String?) -> Unit,
    onClose: () -> Unit,
) {
    val context = LocalContext.current
    val photoController = rememberPhotoController(state)

    var current by remember { mutableStateOf(state) }
    val waitAllowed = current.eventFieldConfig.waitAllowed
    var hasEndEvent by remember { mutableStateOf(current.endDate != null) }

    var costError by remember { mutableStateOf(false) }
    var commentError by remember { mutableStateOf(false) }
    var photoError by remember { mutableStateOf(false) }

    var showSaveDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val currentTime = getCurrentDateApi()

    if (showSaveDialog) {
        ConfirmSaveOrFinalizeDialog(
            translateMap = translateMap,
            hasEndEvent = hasEndEvent,
            waitAllowed = waitAllowed,
            onDismiss = { showSaveDialog = false },
            onConfirm = {
                var updatePhoto: EventManualData.PhotoData? = null
                if (photoController.hasBitmap()) {
                    updatePhoto = photoController.saveImageToOrigin() ?: current.photo
                } else {
                    photoController.clearAllPhoto()
                    updatePhoto = null
                }
                showSaveDialog = false
                onSave(current)
            }
        )
    }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            translateMap = translateMap,
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(NamoaTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(NamoaTheme.spacing.mediumSmall)
    ) {
        HeaderSection(
            title = current.title,
            onClose = {
                photoController.clearOrphanTempFiles()
                onClose()
            }
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            // Campo: Custo
            if (!current.eventFieldConfig.isCostHidden()) {
                EventFormField(
                    modifier = Modifier.fillMaxWidth(),
                    hint = translateMap.textOf(EventManualKey.CostLbl),
                    errorMessage = translateMap.textOf(EventManualKey.ErrorRequiredFieldMsg),
                    isRequired = current.eventFieldConfig.isCostRequired(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    icon = Icons.Default.Wallet,
                    value = current.cost,
                    onValueChange = {
                        costError = false
                        current = current.copy(cost = it)
                    },
                    isError = costError
                )
            }

            // Campo: Comentário
            if (!current.eventFieldConfig.isCommentHidden()) {
                EventFormField(
                    modifier = Modifier
                        .padding(top = NamoaTheme.spacing.medium)
                        .fillMaxWidth(),
                    hint = translateMap.textOf(EventManualKey.CommentLbl),
                    isRequired = current.eventFieldConfig.isCommentRequired(),
                    errorMessage = translateMap.textOf(EventManualKey.ErrorRequiredFieldMsg),
                    icon = Icons.AutoMirrored.Filled.Notes,
                    value = current.comment,
                    onValueChange = {
                        commentError = false
                        current = current.copy(comment = it)
                    },
                    isError = commentError
                )
            }

            // Campo: Foto
            if (!state.eventFieldConfig.isPhotoHidden()) {
                // Obtém o nome do arquivo temporário para câmera
                val requiredPhoto = state.eventFieldConfig.isPhotoRequired()

                Row(
                    modifier = Modifier.padding(top = NamoaTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        translateMap.textOf(EventManualKey.PhotoLbl),
                        style = NamoaTheme.typography.bodyMedium
                    )
                    if (requiredPhoto) {
                        Text(" *", color = Color.Red, style = NamoaTheme.typography.bodyMedium)
                    }
                }

                PhotoSection(
                    translateMap = translateMap,
                    photoBitmap = photoController.photoBitmap,
                    isLoading = photoController.isLoading,
                    downloadError = photoController.hasDownloadError(),
                    onAddPhoto = {
                        photoError = false
                        val tempName = photoController.createNewPhotoTemp()
                        context.callCameraAct(1, tempName!!)
                    },
                    onPreviewPhoto = {
                        val previewPath = photoController.bestPreviewName() ?: "photo.jpg"
                        context.callCameraAct(1, previewPath)
                    },
                    onRemove = {
                        photoController.removePhotoBitmap()
                        if (hasEndEvent && requiredPhoto) photoError = true
                    },
                    onRetryDownload = {
                        photoController.retryDownload(state.photo)
                    },
                    isError = photoError
                )
            }
            // Data de início
            DateSection(
                modifier = Modifier.padding(top = NamoaTheme.spacing.mediumLarge),
                translateMap = translateMap,
                isEditMode = isEditMode,
                label = translateMap.textOf(EventManualKey.StartDateLbl),
                dateData = current.startDate ?: currentTime,
                dateError = startDateError?.let {
                    "${translateMap.textOf(it.key)} ${it.date}"
                },
                onDateSelected = {
                    val startDate = it.fullTimeStampGMT
                    current = current.copy(startDate = startDate)
                    onDateSelected(startDate, current.endDate)
                }
            )

            Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))

            if (waitAllowed && !isHistoric) {
                EndEventSwitch(
                    label = translateMap.textOf(EventManualKey.ShowEndDateSwitchLbl),
                    checked = hasEndEvent,
                    onCheckedChange = { checked ->
                        hasEndEvent = checked

                        if (!checked) {
                            costError = false
                            commentError = false
                            photoError = false
                            onDateSelected(current.startDate!!, null)
                        }
                    }
                )
            }

            if (hasEndEvent && waitAllowed) {
                DateSection(
                    modifier = Modifier
                        .animateContentSize()
                        .padding(top = NamoaTheme.spacing.mediumLarge),
                    translateMap = translateMap,
                    label = translateMap.textOf(EventManualKey.EndDateLbl),
                    dateData = current.endDate ?: currentTime,
                    dateError = endDateError?.let {
                        "${translateMap.textOf(it.key)} ${it.date}"
                    },
                    onDateSelected = {
                        val endDate = it.fullTimeStampGMT
                        current = current.copy(endDate = endDate)
                        onDateSelected(current.startDate!!, endDate)
                    }
                )

                Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))
            }


            EventActionsRow(
                isEditMode = isEditMode,
                switchToEnd = hasEndEvent || !waitAllowed,
                translateMap = translateMap,
                onDelete = { showDeleteDialog = true },
                onSave = {
                    validateAndSaveEvent(
                        hasEndEvent = hasEndEvent,
                        waitAllowed = waitAllowed,
                        current = current,
                        startDateError = startDateError,
                        endDateError = endDateError,
                        photoController = photoController,
                        currentTime = currentTime,
                        onSave = {
                            showSaveDialog = true
                            current = it
                        },
                        onFieldError = {
                            costError = it.cost
                            commentError = it.comment
                            photoError = it.photo
                        }
                    )
                },
                isSaveEnabled = startDateError == null
                        && endDateError == null
            )
        }
    }
}

@Composable
private fun HeaderSection(title: String, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NamoaTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = NamoaTheme.typography.titleLarge,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = NamoaTheme.spacing.small),
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fechar",
                tint = NamoaTheme.colors.onSurface
            )
        }
    }
}

@Composable
fun ConfirmSaveOrFinalizeDialog(
    translateMap: TranslateMap,
    hasEndEvent: Boolean,
    waitAllowed: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    val isFinalized = hasEndEvent || !waitAllowed

    AlertDialog(
        icon = {
            Icon(
                imageVector = if (isFinalized) Icons.Default.CheckCircle else Icons.Default.Save,
                contentDescription = null,
                tint = if (isFinalized) Color(0xFF4CAF50) else NamoaTheme.colors.primary
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    if (isFinalized)
                        translateMap.textOf(EventManualKey.FinalizeButtonLbl)
                    else
                        translateMap.textOf(EventManualKey.SaveButtonLbl)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(translateMap.textOf(EventManualKey.CancelButtonLbl))
            }
        },
        title = {
            Text(
                if (isFinalized)
                    translateMap.textOf(EventManualKey.FinalizeButtonLbl)
                else
                    translateMap.textOf(EventManualKey.SaveButtonLbl)
            )
        },
        text = {
            Text(
                if (isFinalized)
                    translateMap.textOf(EventManualKey.ConfirmFinalizeMsg)
                else
                    translateMap.textOf(EventManualKey.ConfirmSaveMsg)
            )
        }
    )
}

@Composable
fun ConfirmDeleteDialog(
    translateMap: TranslateMap,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFFF9800)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(translateMap.textOf(EventManualKey.DeleteButtonLbl))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(translateMap.textOf(EventManualKey.CancelButtonLbl))
            }
        },
        title = { Text(translateMap.textOf(EventManualKey.DeleteButtonLbl)) },
        text = { Text(translateMap.textOf(EventManualKey.ConfirmDeleteMsg)) }
    )
}

@Composable
fun DateSection(
    modifier: Modifier = Modifier,
    dateError: String?,
    isEditMode: Boolean = false,
    label: String,
    dateData: String,
    translateMap: TranslateMap,
    onDateSelected: (DateTimeSelected) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(label, style = NamoaTheme.typography.bodyMedium)
        DateTimePicker(
            modifier = Modifier.fillMaxWidth(),
            initialDate = dateData,
            dateHint = translateMap.textOf(EventManualKey.DateHintLbl),
            timeHint = translateMap.textOf(EventManualKey.TimeHintLbl),
            onDateTimeSelected = { onDateSelected(it) },
            isError = dateError != null,
            isDateEnabled = !isEditMode,
            errorText = dateError ?: ""
        )
    }
}

@Composable
fun EventActionsRow(
    isEditMode: Boolean,
    switchToEnd: Boolean,
    translateMap: TranslateMap,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    isSaveEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = NamoaTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(NamoaTheme.spacing.small)
    ) {
        if (isEditMode) {
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = onDelete
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(NamoaTheme.spacing.extraSmall))
                Text(translateMap.textOf(EventManualKey.DeleteButtonLbl))
            }
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = onSave,
            enabled = isSaveEnabled,
            shape = RoundedCornerShape(NamoaTheme.spacing.mediumSmall),
            colors = ButtonDefaults.buttonColors(containerColor = NamoaTheme.colors.primary)
        ) {
            Text(
                text = if (switchToEnd)
                    translateMap.textOf(EventManualKey.FinalizeButtonLbl)
                else
                    translateMap.textOf(EventManualKey.SaveButtonLbl),
                textAlign = TextAlign.Center,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EndEventSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = NamoaTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

private fun validateAndSaveEvent(
    hasEndEvent: Boolean,
    waitAllowed: Boolean,
    current: EventManualData,
    startDateError: EventManualDialogState.ErrorDate?,
    endDateError: EventManualDialogState.ErrorDate?,
    photoController: PhotoController,
    currentTime: String,
    onSave: (EventManualData) -> Unit,
    onFieldError: (FieldErrors) -> Unit
) {
    var errors = mutableMapOf<ValidateEventManualField, Boolean>()
    var fieldErrors = FieldErrors()

    if (startDateError != null) {
        errors[ValidateEventManualField.DATE] = true
    }

    if (hasEndEvent || !waitAllowed) {
        errors = validateEventManualFields(
            data = current,
            endDateError = endDateError,
            hasPhoto = photoController.hasValidPhoto()
        )

        fieldErrors = FieldErrors(
            cost = errors[ValidateEventManualField.COST] == true,
            comment = errors[ValidateEventManualField.COMMENT] == true,
            photo = errors[ValidateEventManualField.PHOTO] == true
        )

        if (current.eventFieldConfig.isPhotoRequired() && photoController.hasDownloadError()) {
            errors[ValidateEventManualField.PHOTO] = true
            fieldErrors = fieldErrors.copy(photo = true)
        }

        if (current.eventFieldConfig.isPhotoRequired() && photoController.isLoading) {
            errors[ValidateEventManualField.PHOTO] = true
            fieldErrors = fieldErrors.copy(photo = true)
        }
    }

    onFieldError(fieldErrors)

    if (errors.containsValue(true)) {
        return
    }

    val updatedPhoto = photoController.saveImageToOrigin()
    val finalEndDate = when {
        !waitAllowed -> current.startDate ?: currentTime
        hasEndEvent -> current.endDate ?: currentTime
        else -> null
    }

    onSave(
        current.copy(
            photo = updatedPhoto,
            status = if (!waitAllowed || hasEndEvent) EventStatus.DONE else EventStatus.WAITING,
            endDate = finalEndDate
        )
    )
}

private data class FieldErrors(
    val cost: Boolean = false,
    val comment: Boolean = false,
    val photo: Boolean = false
)
