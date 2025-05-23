package com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.translate
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormField
import com.namoadigital.prj001.ui.act011.finish_os.di.model.NewServiceChoose
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop.NO_STOPPED
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.DateTimePicker
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.RadioGroup
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.RadioGroupItem
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.RadioGroupOptions
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.TitleSection
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.TitleSwitch
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZED_OS_MACHINE_STOPPED_SWITCH_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZED_OS_OPTION_STOPPED_BY_MAINTENANCE
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZED_OS_OPTION_STOPPED_BY_THIRD_PARTY
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_AFTER_TITLE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_NOT_FINALIZED_DATE_INCORRECT_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_NOT_FINALIZED_PARTIAL_EXECUTION_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_NOT_FINALIZE_DECIDE_PLANNING_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_NOT_FINALIZE_INFO_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.MKDATETIME_DATE_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.MKDATETIME_HOUR_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation
import com.namoadigital.prj001.util.ConstantBaseApp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


@Composable
fun AfterFinishOSComponent(
    modifier: Modifier = Modifier,
    translateMap: TranslateMap,
    translateLib: TranslateMap,
    showResponsibleOptions: Boolean = false,
    isReadOnly: Boolean = false,
    machineStateFinal: FinishFormField.MachineOSFinal,
    newServiceState: FinishFormField.HasNewService,
    componentError: FinishValidation.ComponentError?,
    verticalScrollState: ScrollState,
    onMachineStopped: (ResponsibleStop?) -> Unit,
    onScheduleFinish: (NewServiceChoose?) -> Unit,
) {

    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .animateContentSize { _, _ ->
                if (verticalScrollState.value <= verticalScrollState.maxValue) {
                    scope.launch {
                        verticalScrollState.animateScrollTo(
                            value = verticalScrollState.maxValue + 400,
                            animationSpec = spring(
                                dampingRatio = 3.5f,
                                stiffness = 50f,
                            )
                        )
                    }
                }
            },
    ) {

        TitleSection(text = translateMap.translate(DIALOG_FINALIZE_OS_AFTER_TITLE_LBL))

        MachineFinalSwitch(
            modifier = Modifier.fillMaxWidth(),
            translateMap = translateMap,
            isReadOnly = isReadOnly,
            machineStateFinal = machineStateFinal,
            showResponsibleOptions = showResponsibleOptions,
            onOptionSelected = {
                onMachineStopped(it)
            }
        )

        ScheduleFinishSwitch(
            modifier = Modifier
                .padding(top = NamoaTheme.spacing.mediumSmall)
                .fillMaxWidth(),
            translateMap = translateMap,
            translateLib = translateLib,
            newServiceState = newServiceState,
            machineError = componentError,
            isReadOnly = isReadOnly,
            onOptionSelected = {
                onScheduleFinish(it)
            }
        )

    }

}

@Composable
fun MachineFinalSwitch(
    modifier: Modifier = Modifier,
    translateMap: TranslateMap,
    showResponsibleOptions: Boolean = false,
    machineStateFinal: FinishFormField.MachineOSFinal? = null,
    isReadOnly: Boolean = false,
    onOptionSelected: (ResponsibleStop?) -> Unit
) {

    var optionSelected by remember {
        mutableStateOf(if (isReadOnly) machineStateFinal?.option else ResponsibleStop.NO_STOPPED)
    }

    var switchState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(optionSelected) {
        optionSelected?.let(onOptionSelected)
    }

    TitleSwitch(
        title = translateMap.translate(DIALOG_FINALIZED_OS_MACHINE_STOPPED_SWITCH_TTL),
        isRequiredOption = if (showResponsibleOptions) switchState else false,
        isEnabled = !isReadOnly,
        initialSwitchState = if (isReadOnly) machineStateFinal?.option != NO_STOPPED else false,
        onSwitchChecked = { isChecked ->
            switchState = isChecked

            when {
                !isChecked -> {
                    optionSelected = null
                    onOptionSelected(NO_STOPPED)
                }

                showResponsibleOptions -> {
                    onOptionSelected(null)
                }

                else -> {
                    onOptionSelected(ResponsibleStop.STOPPED)
                }
            }
        }
    ) {
        if (showResponsibleOptions) {
            RadioGroup(
                modifier = modifier.padding(start = NamoaTheme.spacing.medium),
                isEnabled = !isReadOnly,
                radioGroupOptions = RadioGroupOptions(
                    list = listOf(
                        RadioGroupItem(
                            id = 1,
                            isSelected = optionSelected == ResponsibleStop.MAINTENANCE,
                            value = ResponsibleStop.MAINTENANCE,
                            text = translateMap.translate(
                                DIALOG_FINALIZED_OS_OPTION_STOPPED_BY_MAINTENANCE
                            ),
                        ),
                        RadioGroupItem(
                            id = 2,
                            isSelected = optionSelected == ResponsibleStop.THIRD_PARTY,
                            value = ResponsibleStop.THIRD_PARTY,
                            text = translateMap.translate(
                                DIALOG_FINALIZED_OS_OPTION_STOPPED_BY_THIRD_PARTY
                            ),
                        )
                    )
                ),
                onOptionSelected = { itemChecked ->
                    optionSelected = itemChecked.value as ResponsibleStop
                }
            )
        }
    }

}


@Composable
fun ScheduleFinishSwitch(
    modifier: Modifier = Modifier,
    translateMap: TranslateMap,
    translateLib: TranslateMap,
    machineError: FinishValidation.ComponentError?,
    isReadOnly: Boolean = false,
    onOptionSelected: (NewServiceChoose?) -> Unit,
    newServiceState: FinishFormField.HasNewService
) {

    val timeSelected by remember {
        mutableStateOf(getNextDay())
    }

    var dateError by remember {
        mutableStateOf(false)
    }

    var optionSelected by remember {
        mutableStateOf<NewServiceChoose?>(NewServiceChoose.FINALIZED)
    }

    var switchState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(optionSelected) {
        onOptionSelected(optionSelected)
    }

    LaunchedEffect(machineError) {
        dateError = machineError is FinishValidation.Component.ScheduleReturnForm.DateIncorrectOS
    }

    TitleSwitch(
        modifier = modifier,
        title = translateMap.translate(DIALOG_NOT_FINALIZE_INFO_LBL),
        isRequiredOption = switchState,
        initialSwitchState = newServiceState.option != NewServiceChoose.FINALIZED,
        isEnabled = !isReadOnly,
        onSwitchChecked = { switch ->
            switchState = switch
            optionSelected = if (!switch) {
                NewServiceChoose.FINALIZED
            } else {
                null
            }
        }
    ) {
        RadioGroup(
            modifier = modifier.padding(start = NamoaTheme.spacing.medium),
            isEnabled = !isReadOnly,
            radioGroupOptions = RadioGroupOptions(listOf(
                RadioGroupItem(
                    id = 1,
                    isSelected = newServiceState.option == NewServiceChoose.PLANNING,
                    value = NewServiceChoose.PLANNING,
                    text = translateMap.translate(DIALOG_NOT_FINALIZE_DECIDE_PLANNING_LBL),
                ),
                RadioGroupItem(
                    id = 2,
                    isSelected = newServiceState.option is NewServiceChoose.RETURN,
                    value = NewServiceChoose.RETURN(getNextDay()),
                    text = translateMap.translate(DIALOG_NOT_FINALIZED_PARTIAL_EXECUTION_LBL),
                    content = listOf {
                        DateTimePicker(
                            modifier = Modifier.fillMaxWidth(),
                            initialDate = timeSelected,
                            isDateEnabled = !isReadOnly,
                            isTimeEnabled = !isReadOnly,
                            dateHint = translateLib.translate(MKDATETIME_DATE_TTL),
                            timeHint = translateLib.translate(MKDATETIME_HOUR_TTL),
                            isError = dateError,
                            errorText = translateMap.translate(
                                DIALOG_NOT_FINALIZED_DATE_INCORRECT_LBL
                            ),
                            onDateTimeSelected = {
                                optionSelected = NewServiceChoose.RETURN(it.fullTimeStampGMT)
                            }
                        )
                    }

                ),
            )),
            onOptionSelected = {
                optionSelected = it.value as NewServiceChoose
            }
        )
    }

}


private fun getNextDay(): String {
    val calendar = Calendar.getInstance()
    val deviceGmt = ToolBox.getDeviceGMT(true)
    calendar.add(Calendar.DAY_OF_MONTH, 1)

    // Defina a hora, minuto, segundo e milissegundo para as 08:00:00.000
    calendar[Calendar.HOUR_OF_DAY] = 8
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    TimeZone.setDefault(TimeZone.getTimeZone(deviceGmt))

    // Formate a data e hora em uma String
    val format = SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT, Locale.getDefault())
    return format.format(calendar.time)
}