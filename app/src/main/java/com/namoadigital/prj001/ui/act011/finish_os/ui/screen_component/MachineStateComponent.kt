package com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.DateTimePicker
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.RadioGroup
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.RadioGroupItem
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.RadioGroupOptions
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.TitleSection
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.TitleSwitch
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_INITIAL_MACHINE_DATE_EMPTY_ERROR_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_INITIAL_MACHINE_DATE_EXCEED_FORM_START_ERROR_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_MAINTENANCE_OPT
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_NO_STOPPED_OPT
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_RESPONSIBLE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_STOPPED_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_SWITCH_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_THIRD_PARTY_ERROR_OPT
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.MKDATETIME_DATE_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.MKDATETIME_HOUR_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation


sealed class MachinesStatus {
    data object NO_STOPPED : MachinesStatus()

    data class STOPPED_FOR(
        val date: String?,
        val responsibleStop: ResponsibleStop? = null,
    ) : MachinesStatus()
}


@Composable
fun MachineInitialComponent(
    modifier: Modifier = Modifier,
    isVersionMachineStopped: Boolean,
    translateMap: TranslateMap,
    translateLib: TranslateMap,
    initialDate: String,
    responsibleStop: ResponsibleStop,
    componentError: FinishValidation.ComponentError?,
    showOptionsWhenMachineStopped: Boolean,
    isReadOnly: Boolean,
    onOptionSelected: (MachinesStatus?) -> Unit
) {

    Column(
        modifier = modifier,
    ) {

        TitleSection(
            modifier = Modifier
                .fillMaxWidth(),
            text = translateMap.textOf(INITIAL_SERIAL_STATE_TTL)
        )

        if (isVersionMachineStopped) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = NamoaTheme.spacing.small),
                text = translateMap.textOf(INITIAL_SERIAL_STATE_STOPPED_TTL),
                style = NamoaTheme.typography.bodyLarge,
                color = NamoaTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis

            )
            OptionWithoutSwitch(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = NamoaTheme.spacing.mediumSmall),
                initialDate = initialDate,
                responsibleStop = responsibleStop,
                translateMap = translateMap,
                translateLib = translateLib,
                machineError = componentError,
                onOptionSelected = onOptionSelected,
                isReadOnly = isReadOnly,
                showOptionsWhenMachineStopped = showOptionsWhenMachineStopped
            )
        } else {
            OptionWithSwitch(
                modifier = Modifier.fillMaxWidth(),
                initialDate = initialDate,
                translateMap = translateMap,
                translateLib = translateLib,
                responsibleStop = responsibleStop,
                onOptionSelected = onOptionSelected,
                machineError = componentError,
                isReadOnly = isReadOnly,
                showOptionsWhenMachineStopped = showOptionsWhenMachineStopped
            )
        }
    }

}


@Composable
fun OptionDateComponent(
    modifier: Modifier = Modifier,
    translateLib: TranslateMap,
    translateMap: TranslateMap,
    initialDate: String,
    isEnabled: Boolean,
    machineError: FinishValidation.ComponentError?,
    onDateTimeSelected: (String?) -> Unit
) {

    var dateStopped by remember {
        mutableStateOf(initialDate.ifEmpty { null })
    }

    var dateError by remember {
        mutableStateOf(false)
    }

    var dateTextError by remember {
        mutableStateOf("")
    }

    LaunchedEffect(dateStopped) {
        onDateTimeSelected(dateStopped)
    }

    LaunchedEffect(machineError) {

        when (machineError) {
            is FinishValidation.Component.InitialMachine.DateExceededFormStartDate -> {
                dateError = true
                dateTextError =
                    translateMap.textOf(
                        DIALOG_FINALIZE_INITIAL_MACHINE_DATE_EXCEED_FORM_START_ERROR_LBL
                    )
            }

            is FinishValidation.Component.InitialMachine.DateEmpty -> {
                dateError = true
                dateTextError =
                    translateMap.textOf(DIALOG_FINALIZE_INITIAL_MACHINE_DATE_EMPTY_ERROR_LBL)
            }

            else -> {
                dateError = false
                dateTextError = ""
            }
        }
    }


    DateTimePicker(
        modifier = modifier.padding(bottom = NamoaTheme.spacing.mediumSmall),
        initialDate = initialDate,
        dateHint = translateLib.textOf(MKDATETIME_DATE_TTL),
        timeHint = translateLib.textOf(MKDATETIME_HOUR_TTL),
        isError = dateError,
        errorText = dateTextError,
        isDateEnabled = isEnabled,
        isTimeEnabled = isEnabled,
        onDateTimeSelected = {
            dateStopped = it.fullTimeStampGMT
        }
    )
}

@Composable
fun OptionWithSwitch(
    modifier: Modifier = Modifier,
    initialDate: String,
    responsibleStop: ResponsibleStop,
    translateMap: TranslateMap,
    translateLib: TranslateMap,
    machineError: FinishValidation.ComponentError?,
    showOptionsWhenMachineStopped: Boolean,
    isReadOnly: Boolean,
    onOptionSelected: (MachinesStatus?) -> Unit
) {

    var dateStopped by remember {
        mutableStateOf(initialDate.ifEmpty { null })
    }

    var responsibleStopFromStopped by remember {
        mutableStateOf<ResponsibleStop?>(responsibleStop)
    }

    val checkInitialState: () -> MachinesStatus = {
        if (ResponsibleStop.isMachineStopped(responsibleStop)) {
            MachinesStatus.STOPPED_FOR(
                date = initialDate,
                responsibleStop = responsibleStop
            )
        } else {
            MachinesStatus.NO_STOPPED
        }
    }


    var optionSelected by remember {
        mutableStateOf(checkInitialState())
    }

    var isRequiredOption by remember {
        mutableStateOf(ResponsibleStop.isMachineStopped(responsibleStop))
    }

    LaunchedEffect(optionSelected, dateStopped, responsibleStopFromStopped) {
        when (optionSelected) {
            is MachinesStatus.STOPPED_FOR -> {
                onOptionSelected(
                    MachinesStatus.STOPPED_FOR(
                        dateStopped,
                        responsibleStopFromStopped
                    )
                )
            }

            else -> onOptionSelected(MachinesStatus.NO_STOPPED)

        }
    }

    TitleSwitch(
        modifier = modifier,
        title = translateMap.textOf(INITIAL_SERIAL_STATE_SWITCH_LBL),
        initialSwitchState = ResponsibleStop.isMachineStopped(responsibleStop),
        isRequiredOption = isRequiredOption,
        isEnabled = !isReadOnly,
        onSwitchChecked = { isChecked ->
            isRequiredOption = isChecked
            optionSelected = if (!isChecked) MachinesStatus.NO_STOPPED
            else MachinesStatus.STOPPED_FOR(dateStopped, responsibleStopFromStopped)
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(horizontal = NamoaTheme.spacing.mediumLarge)
                    .animateContentSize()
                    .fillMaxWidth()
            ) {

                Text(
                    text = translateMap.textOf(INITIAL_SERIAL_STATE_DATE_LBL),
                    style = NamoaTheme.typography.bodyMedium,
                    color = NamoaTheme.colors.onSurface
                )

                OptionDateComponent(
                    modifier = Modifier.fillMaxWidth(),
                    translateLib = translateLib,
                    translateMap = translateMap,
                    initialDate = initialDate,
                    machineError = machineError,
                    isEnabled = !isReadOnly,
                    onDateTimeSelected = { dateStopped = it }
                )

                if (showOptionsWhenMachineStopped) {
                    Text(
                        text = "${translateMap.textOf(INITIAL_SERIAL_STATE_RESPONSIBLE_LBL)}:",
                        style = NamoaTheme.typography.bodyMedium,
                        color = NamoaTheme.colors.onSurface,
                    )

                    RadioGroup(
                        modifier = Modifier
                            .fillMaxWidth(),
                        isEnabled = !isReadOnly,
                        radioGroupOptions = RadioGroupOptions(
                            list = listOf(
                                RadioGroupItem(
                                    id = 1,
                                    value = ResponsibleStop.MAINTENANCE,
                                    text = translateMap.textOf(
                                        INITIAL_SERIAL_STATE_MAINTENANCE_OPT
                                    ),
                                    isSelected = responsibleStopFromStopped == ResponsibleStop.MAINTENANCE
                                ),
                                RadioGroupItem(
                                    id = 2,
                                    value = ResponsibleStop.THIRD_PARTY,
                                    text = translateMap.textOf(
                                        INITIAL_SERIAL_STATE_THIRD_PARTY_ERROR_OPT
                                    ),
                                    isSelected = responsibleStopFromStopped == ResponsibleStop.THIRD_PARTY
                                )
                            )
                        ),
                        onOptionSelected = {
                            responsibleStopFromStopped = it.value as ResponsibleStop
                        }
                    )
                } else {
                    responsibleStopFromStopped = ResponsibleStop.STOPPED
                }
            }
        }
    )

}


@Composable
private fun OptionWithoutSwitch(
    modifier: Modifier = Modifier,
    initialDate: String,
    responsibleStop: ResponsibleStop,
    translateMap: TranslateMap,
    translateLib: TranslateMap,
    machineError: FinishValidation.ComponentError?,
    showOptionsWhenMachineStopped: Boolean,
    isReadOnly: Boolean,
    onOptionSelected: (MachinesStatus?) -> Unit
) {

    var dateStopped by remember {
        mutableStateOf(initialDate.ifEmpty { null })
    }

    var responsibleStopFromStopped by remember {
        mutableStateOf<ResponsibleStop?>(responsibleStop)
    }

    var optionSelected by remember {
        mutableStateOf<MachinesStatus?>(
            if (!dateStopped.isNullOrEmpty() && responsibleStop != ResponsibleStop.NO_STOPPED) {
                MachinesStatus.STOPPED_FOR(
                    date = initialDate,
                    responsibleStop = responsibleStop
                )
            } else {
                MachinesStatus.NO_STOPPED
            }
        )
    }

    LaunchedEffect(optionSelected, dateStopped, responsibleStopFromStopped) {
        when (optionSelected) {
            is MachinesStatus.STOPPED_FOR -> {
                onOptionSelected(
                    MachinesStatus.STOPPED_FOR(
                        dateStopped,
                        responsibleStopFromStopped
                    )
                )
            }

            else -> onOptionSelected(MachinesStatus.NO_STOPPED)

        }
    }

    RadioGroup(
        isEnabled = !isReadOnly,
        modifier = modifier.padding(top = NamoaTheme.spacing.medium),
        radioGroupOptions = RadioGroupOptions(
            list = listOf(
                RadioGroupItem(
                    id = 0,
                    value = MachinesStatus.NO_STOPPED,
                    text = translateMap.textOf(INITIAL_SERIAL_STATE_NO_STOPPED_OPT),
                    isSelected = responsibleStop == ResponsibleStop.NO_STOPPED
                ),
                RadioGroupItem(
                    id = 1,
                    value = MachinesStatus.STOPPED_FOR(initialDate, responsibleStop),
                    text = translateMap.textOf(INITIAL_SERIAL_STATE_DATE_LBL),
                    isSelected = ResponsibleStop.isMachineStopped(responsibleStop),
                    content = listOf(
                        {
                            OptionDateComponent(
                                modifier = Modifier.fillMaxWidth(),
                                translateLib = translateLib,
                                translateMap = translateMap,
                                initialDate = initialDate,
                                machineError = machineError,
                                isEnabled = !isReadOnly,
                                onDateTimeSelected = { dateStopped = it }
                            )
                        },
                        {
                            if (showOptionsWhenMachineStopped) {
                                Text(
                                    modifier = Modifier.padding(
                                        top = NamoaTheme.spacing.medium,
                                        start = NamoaTheme.spacing.large
                                    ),
                                    text = "${
                                        translateMap.textOf(
                                            INITIAL_SERIAL_STATE_RESPONSIBLE_LBL
                                        )
                                    }:",
                                    style = NamoaTheme.typography.bodyMedium,
                                    color = NamoaTheme.colors.onSurface,
                                    overflow = TextOverflow.Ellipsis
                                )
                                RadioGroup(
                                    isEnabled = !isReadOnly,
                                    modifier = Modifier
                                        .padding(start = NamoaTheme.spacing.mediumLarge)
                                        .fillMaxWidth(),
                                    radioGroupOptions = RadioGroupOptions(
                                        list = listOf(
                                            RadioGroupItem(
                                                id = 1,
                                                value = ResponsibleStop.MAINTENANCE,
                                                text = translateMap.textOf(
                                                    INITIAL_SERIAL_STATE_MAINTENANCE_OPT
                                                ),
                                                isSelected = responsibleStop == ResponsibleStop.MAINTENANCE
                                            ),
                                            RadioGroupItem(
                                                id = 2,
                                                value = ResponsibleStop.THIRD_PARTY,
                                                text = translateMap.textOf(
                                                    INITIAL_SERIAL_STATE_THIRD_PARTY_ERROR_OPT
                                                ),
                                                isSelected = responsibleStop == ResponsibleStop.THIRD_PARTY
                                            )
                                        )
                                    ),
                                    onOptionSelected = {
                                        responsibleStopFromStopped = it.value as ResponsibleStop
                                    }
                                )
                            } else {
                                responsibleStopFromStopped = ResponsibleStop.STOPPED
                            }
                        }
                    )
                )
            )
        ),
        onOptionSelected = {
            optionSelected = it.value as MachinesStatus
        }
    )
}

