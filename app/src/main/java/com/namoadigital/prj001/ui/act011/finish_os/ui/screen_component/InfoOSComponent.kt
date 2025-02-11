package com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.translate
import com.namoadigital.prj001.design.compose.ApplicationTheme
import com.namoadigital.prj001.extensions.date.convertDateToFullTimeStampGMT
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormField
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.DateTimePicker
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.TitleSection
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_OS_DATE_EXCEED_MACHINE_DATE_STOPPED_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_SO_INFO_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_FORM_ELAPSED_TIME_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_FORM_END_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_FORM_INVALID_START_DATE_MSG
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_FORM_START_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_INFO_END_DATE_EXCEEDED_START_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_END_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_LAST_MEASURE_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.FORM_OS_INFO_END_DATE_FUTURE_ERROR_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.MKDATETIME_DATE_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.MKDATETIME_HOUR_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation
import com.namoadigital.prj001.util.ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT

@Composable
fun InfoOSComponent(
    modifier: Modifier = Modifier,
    translateMap: TranslateMap,
    translateLib: TranslateMap,
    infoOs: FinishFormField.ExpectedTimeOS?,
    componentError: FinishValidation.ComponentError? = null,
    isReadOnly: Boolean,
    onInitialDateTimeSelected: (String?) -> Unit,
    onFinalDateTimeSelected: (String?) -> Unit,
) {

    val defaultText = "--:--"

    val isEnableEditDate by remember {
        mutableStateOf(infoOs?.isEditDate ?: false)
    }

    var initialDate by remember {
        mutableStateOf(infoOs?.dateStart ?: "")
    }

    var finalDate by remember {
        mutableStateOf(infoOs?.dateEnd ?: getCurrentDateApi())
    }

    var timeExecution by remember {
        mutableStateOf(
            FinishFormField.ExpectedTimeOS.elapsedTime(finalDate, initialDate) ?: defaultText
        )
    }

    var initialDateError by remember {
        mutableStateOf(Pair(false, ""))
    }

    var finalDateError by remember {
        mutableStateOf(Pair(false, ""))
    }

    val isEnabledEditDate = if (!isReadOnly) isEnableEditDate else false


    LaunchedEffect(componentError) {
        when (componentError) {
            is FinishValidation.Component.InfoOS.DateExceededMachineDateStopped -> {
                initialDateError = Pair(
                    true,
                    translateMap.translate(
                        DIALOG_FINALIZE_FORM_OS_DATE_EXCEED_MACHINE_DATE_STOPPED_LBL
                    )
                )
                finalDateError = Pair(false, "")
            }

            is FinishValidation.Component.InfoOS.InvalidBothDate -> {
                initialDateError = Pair(
                    true,
                    translateMap.translate(DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_END_DATE_LBL)
                )
                finalDateError = Pair(
                    true,
                    translateMap.translate(DIALOG_FINALIZE_OS_INFO_END_DATE_EXCEEDED_START_DATE_LBL)
                )
                timeExecution = defaultText
            }

            is FinishValidation.Component.InfoOS.InvalidStartDate -> {
                initialDateError = Pair(
                    true,
                    translateMap.translate(DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_END_DATE_LBL)
                )
                finalDateError = Pair(false, "")
            }

            is FinishValidation.Component.InfoOS.DateExceededLastMeasureDate -> {
                initialDateError = Pair(
                    true,
                    translateMap.translate(
                        DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_LAST_MEASURE_DATE_LBL
                    )
                )
                finalDateError = Pair(false, "")
            }

            is FinishValidation.Component.InfoOS.InvalidEndDate -> {
                initialDateError = Pair(false, "")
                finalDateError = Pair(
                    true,
                    translateMap.translate(FORM_OS_INFO_END_DATE_FUTURE_ERROR_LBL)
                )
            }

            is FinishValidation.Component.InfoOS.PartialExecutionOS -> {
                initialDateError = Pair(
                    true,
                    "${translateMap.translate(DIALOG_FINALIZE_OS_FORM_INVALID_START_DATE_MSG)} ${
                        infoOs?.partitionMinDate?.convertDateToFullTimeStampGMT(
                            inputFormat = FULL_TIMESTAMP_TZ_FORMAT_GMT,
                            outputFormat = "dd/MM/yyyy HH:mm"
                        )
                    }"
                )
                finalDateError = Pair(false, "")
            }

            else -> {
                initialDateError = Pair(false, "")
                finalDateError = Pair(false, "")
            }
        }
    }

    LaunchedEffect(initialDate, finalDate) {
        timeExecution =
            FinishFormField.ExpectedTimeOS.elapsedTime(finalDate, initialDate) ?: defaultText
    }

    Column(
        modifier = modifier,
    ) {
        TitleSection(text = translateMap.translate(DIALOG_FINALIZE_FORM_SO_INFO_LBL))
        Column(modifier = Modifier.padding(start = ApplicationTheme.spacing.small)) {
            Text(
                modifier = Modifier.padding(start = ApplicationTheme.spacing.small),
                text = translateMap.translate(DIALOG_FINALIZE_OS_FORM_START_DATE_LBL),
                style = ApplicationTheme.typography.bodyLarge,
                color = ApplicationTheme.colors.onSurface
            )
            DateTimePicker(
                modifier = Modifier.fillMaxWidth(),
                isDateEnabled = infoOs?.partitionMinDate != null,
                isTimeEnabled = isEnabledEditDate,
                initialDate = initialDate,
                isError = initialDateError.first,
                errorText = initialDateError.second,
                dateHint = translateLib.translate(MKDATETIME_DATE_TTL),
                timeHint = translateLib.translate(MKDATETIME_HOUR_TTL),
                onDateTimeSelected = {
                    initialDate = it.fullTimeStampGMT
                    onInitialDateTimeSelected(it.fullTimeStampGMT)
                }
            )

            Spacer(modifier = Modifier.height(ApplicationTheme.spacing.medium))

            Text(
                modifier = Modifier.padding(start = ApplicationTheme.spacing.small),
                text = translateMap.translate(DIALOG_FINALIZE_OS_FORM_END_DATE_LBL),
                style = ApplicationTheme.typography.bodyLarge,
                color = ApplicationTheme.colors.onSurface
            )
            DateTimePicker(
                modifier = Modifier.fillMaxWidth(),
                isDateEnabled = isEnabledEditDate,
                isTimeEnabled = isEnabledEditDate,
                isError = finalDateError.first,
                errorText = finalDateError.second,
                initialDate = finalDate,
                dateHint = translateLib.translate(MKDATETIME_DATE_TTL),
                timeHint = translateLib.translate(MKDATETIME_HOUR_TTL),
                onDateTimeSelected = {
                    finalDate = it.fullTimeStampGMT
                    onFinalDateTimeSelected(it.fullTimeStampGMT)
                }
            )

            Spacer(modifier = Modifier.height(ApplicationTheme.spacing.extraSmall))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ApplicationTheme.spacing.mediumSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = ApplicationTheme.spacing.small),
                    text = translateMap.translate(DIALOG_FINALIZE_OS_FORM_ELAPSED_TIME_LBL),
                    style = ApplicationTheme.typography.bodyLarge,
                    color = ApplicationTheme.colors.onSurface
                )

                Text(
                    modifier = Modifier.padding(end = ApplicationTheme.spacing.small),
                    text = timeExecution,
                    style = ApplicationTheme.typography.bodyLarge,
                    color = ApplicationTheme.colors.onSurface
                )
            }
        }
    }

}