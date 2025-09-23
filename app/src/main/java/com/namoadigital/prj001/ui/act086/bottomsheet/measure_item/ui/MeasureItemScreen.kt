package com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.extensions.isValidDecimalInput
import com.namoadigital.prj001.extensions.toStringConsiderDecimal
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.MeasureItemViewModel
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureBottomSheetContext
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemArguments
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemEvent
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemKey
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemState.CommonData
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemState.ValidationError
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureLabels

interface MeasurementActions {
    fun onNavigateReadOnly()
    fun onSaveMeasurement(
        newMeasure: Double?,
        newID: String? = null,
        state: MeasureItemArguments.State
    )

    fun onCancel()
}

@Composable
fun BoxScope.MeasureLoading() {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth().align(Alignment.Center)
    )
}

@Composable
fun DefaultDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = 32.dp,
    height: Dp = 4.dp,
    shape: Shape = NamoaTheme.shapes.extraSmall,
    color: Color = NamoaTheme.colors.onSurfaceVariant.copy(alpha = 0.4f)
) {
    Box(
        modifier = modifier.padding(vertical = NamoaTheme.spacing.small).fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.width(width).height(height).background(color = color, shape = shape)
        )
    }
}


@Composable
fun MeasurementBottomSheetContent(
    bottomSheetContext: MeasureBottomSheetContext,
    actions: MeasurementActions,
) {
    val viewModel = hiltViewModel<MeasureItemViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(MeasureItemEvent.Setup(bottomSheetContext.arguments))
    }

    Column(
        modifier = Modifier.background(
            NamoaTheme.colors.background,
            shape = NamoaTheme.shapes.large
        ).animateContentSize()
    ) {
        DefaultDragHandle()
        Box(
            modifier = Modifier.fillMaxWidth().padding(NamoaTheme.spacing.medium)
        ) {
            if (state.isLoading) {
                MeasureLoading()
            } else {
                MeasurementContent(
                    currentData = state.commonData!!,
                    labels = bottomSheetContext.labels,
                    translateKeys = state.translate,
                    actions = actions,
                    onMeasureValueChange = { viewModel.onEvent(MeasureItemEvent.OnMeasureChanged(it)) },
                    onIdValueChange = { viewModel.onEvent(MeasureItemEvent.OnIdChanged(it)) },
                    onAttemptSave = { viewModel.onEvent(MeasureItemEvent.AttemptSave(it)) }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ValidationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isError: Boolean,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    unit: String? = null,
    supportingText: @Composable (() -> Unit)? = null
) {


    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(placeholder) },
            isError = isError,
            trailingIcon = {
                unit?.let {
                    Text(
                        text = unit,
                        style = NamoaTheme.typography.bodyLarge.copy(color = NamoaTheme.colors.onSurfaceVariant)
                    )
                }
            },
            supportingText = supportingText,
            shape = RoundedCornerShape(NamoaTheme.spacing.mediumSmall),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        )
    }
}

@Composable
private fun MeasurementContent(
    currentData: CommonData,
    labels: MeasureLabels,
    translateKeys: TranslateMap,
    actions: MeasurementActions,
    onMeasureValueChange: (String) -> Unit,
    onIdValueChange: (String) -> Unit,
    onAttemptSave: (() -> Unit) -> Unit
) {

    val validationError = currentData.validationError
    val measureError = validationError.any { it is ValidationError.Measure }
    val idError = validationError.any { it is ValidationError.Id }
    val validationErrorMeasure = validationError.firstOrNull { it is ValidationError.Measure }
    val measureErrorMessage = when (validationErrorMeasure) {
        is ValidationError.Measure.Min -> translateKeys.textOf(validationErrorMeasure.errorMessage) + " ${validationErrorMeasure.minValue.toStringConsiderDecimal()}"
        is ValidationError.Measure.Max -> translateKeys.textOf(validationErrorMeasure.errorMessage) + " ${validationErrorMeasure.maxValue.toStringConsiderDecimal()}"
        is ValidationError.Measure.Required -> translateKeys.textOf(validationErrorMeasure.errorMessage)
        else -> null
    }

    val validationErrorId = validationError.firstOrNull { it is ValidationError.Id }
    val idErrorMessage = if (validationErrorId != null) {
        translateKeys.textOf(validationErrorId.errorMessage)
    } else {
        null
    }



    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        // 🔹 Título
        Text(
            text = translateKeys.textOf(MeasureItemKey.BottomSheetTitleLbl.key),
            style = NamoaTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold, color = NamoaTheme.colors.primary
            )
        )

        // 🔹 Subtítulo
        Text(
            text = translateKeys.textOf(labels.subtitle.key),
            style = NamoaTheme.typography.bodyMedium.copy(
                color = NamoaTheme.colors.onSurfaceVariant
            ),
            modifier = Modifier.padding(
                top = NamoaTheme.spacing.extraSmall, bottom = NamoaTheme.spacing.large
            )
        )

        // 🔹 Campo Medida
        ValidationTextField(
            value = currentData.measure ?: "",
            onValueChange = { if (it.isValidDecimalInput()) onMeasureValueChange(it) },
            placeholder = translateKeys.textOf(MeasureItemKey.MeasureLbl.key),
            isError = measureError,
            modifier = Modifier.widthIn(max = NamoaTheme.spacing.extraColossal + 45.dp),
            keyboardType = KeyboardType.Number,
            unit = currentData.unit,
        )

        if (measureError && !measureErrorMessage.isNullOrEmpty()) {
            Row(
                modifier = Modifier.padding(top = NamoaTheme.spacing.extraSmall),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ErrorOutline,
                    contentDescription = null,
                    tint = NamoaTheme.colors.error
                )
                Spacer(modifier = Modifier.width(NamoaTheme.spacing.small))
                Text(
                    text = measureErrorMessage,
                    style = NamoaTheme.typography.bodySmall.copy(color = NamoaTheme.colors.error)
                )
            }
        }

        Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))

        // 🔹 Campo ID (ou somente leitura)
        if (currentData.measureID?.isEditableId == true) {
            ValidationTextField(
                value = currentData.measureID.idEditable ?: "",
                onValueChange = { onIdValueChange(it) },
                placeholder = translateKeys.textOf(MeasureItemKey.IdEditableLbl.key),
                isError = idError,
                modifier = Modifier.padding(horizontal = NamoaTheme.spacing.massive)
                    .align(Alignment.CenterHorizontally),
            )

            if (idError && !idErrorMessage.isNullOrEmpty()) {
                Row(
                    modifier = Modifier.padding(top = NamoaTheme.spacing.extraSmall),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ErrorOutline,
                        contentDescription = null,
                        tint = NamoaTheme.colors.error
                    )
                    Spacer(modifier = Modifier.width(NamoaTheme.spacing.small))
                    Text(
                        text = idErrorMessage,
                        style = NamoaTheme.typography.bodySmall.copy(color = NamoaTheme.colors.error)
                    )
                }
            }

        } else {
            currentData.measureID?.id?.let { id ->
                Text(
                    text = "${translateKeys.textOf(MeasureItemKey.IdLbl.key)} $id",
                    style = NamoaTheme.typography.bodyLarge.copy(
                        color = NamoaTheme.colors.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = NamoaTheme.spacing.small)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(modifier = Modifier.height(NamoaTheme.spacing.large))

        // 🔹 Botões
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = { actions.onCancel() }) {
                Text(
                    translateKeys.textOf(MeasureItemKey.CancelButtonLbl.key),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(NamoaTheme.spacing.small))


            Button(
                onClick = {

                    when (currentData.currentState) {
                        MeasureItemArguments.State.INITIAL -> {
                            val isEditable = currentData.measureID?.isEditableId == true
                            val idEditableEmpty = currentData.measureID?.idEditable.isNullOrEmpty()
                            val measureEmpty = currentData.measure.isNullOrEmpty()

                            val shouldNavigateReadOnly =
                                (isEditable && idEditableEmpty && measureEmpty) || measureEmpty && !isEditable

                            if (shouldNavigateReadOnly) {
                                actions.onNavigateReadOnly()
                            } else {
                                onAttemptSave {
                                    actions.onSaveMeasurement(
                                        currentData.measure?.toDoubleOrNull(),
                                        currentData.measureID?.idEditable,
                                        currentData.currentState
                                    )
                                }
                            }
                        }

                        else -> onAttemptSave {
                            actions.onSaveMeasurement(
                                currentData.measure?.toDoubleOrNull(),
                                currentData.measureID?.idEditable,
                                currentData.currentState
                            )
                        }
                    }


                }, shape = RoundedCornerShape(NamoaTheme.spacing.mediumLarge)
            ) {
                Text(
                    translateKeys.textOf(labels.buttonNext.key), fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
