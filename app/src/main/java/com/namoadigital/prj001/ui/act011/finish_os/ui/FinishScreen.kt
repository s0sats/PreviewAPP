package com.namoadigital.prj001.ui.act011.finish_os.ui

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.service.WS_Product_Serial_Backup
import com.namoadigital.prj001.ui.act011.finish_os.FinishOSViewModel
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishOsData
import com.namoadigital.prj001.ui.act011.finish_os.di.model.NewServiceChoose
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop.NO_STOPPED
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.FinishAppBar
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.verticalScrollBar
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.AfterFinishOSComponent
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.BackupMachineSerialComponent
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.InfoOSComponent
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.MachineInitialComponent
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.OnBarcodeMkEditText
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_MSG
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_SO_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_BTN_CANCEL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_BTN_SAVE
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_EMPTY_VERIFY_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_REQUIRED_BY_TICKET_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishScreenArguments
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation
import kotlinx.coroutines.launch

@Composable
fun FinishOSScreen(
    arguments: FinishScreenArguments,
    translateMapLib: TranslateMap,
    onBarcodeMkEditText: OnBarcodeMkEditText,
    onBackupMachineWsProcess: ((String?) -> Unit)? = null,
    onDialogDismiss: (() -> Unit)? = null,
    onDialogClose: () -> Unit
) {
    val viewModel = hiltViewModel<FinishOSViewModel>()
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current

    val translateMap by remember {
        derivedStateOf { uiState.translateMap }
    }

    val verticalScrollState = rememberScrollState()

    LaunchedEffect(uiState.saveFinishOS) {
        if (uiState.saveFinishOS) {
            onDialogClose()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getFinishData(
            typeCode = arguments.typeCode,
            code = arguments.code,
            versionCode = arguments.versionCode,
            formData = arguments.formData
        )
    }

    if (!arguments.isReadOnly) {
        DialogFinishOS(
            translateMap,
            context,
            uiState,
            onDialogDismiss,
            verticalScrollState,
            translateMapLib,
            onBarcodeMkEditText,
            arguments,
            onBackupMachineWsProcess
        )
    } else {
        ContentScreen(
            verticalScrollState,
            null,
            uiState,
            translateMapLib,
            onBarcodeMkEditText,
            arguments,
            onBackupMachineWsProcess,
            onDialogDismiss
        )
    }
}

@Composable
private fun DialogFinishOS(
    translateMap: TranslateMap,
    context: Context,
    uiState: FinishState,
    onDialogDismiss: (() -> Unit)?,
    verticalScrollState: ScrollState,
    translateMapLib: TranslateMap,
    onBarcodeMkEditText: OnBarcodeMkEditText,
    arguments: FinishScreenArguments,
    onBackupMachineWsProcess: ((String?) -> Unit)?
) {

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        topBar = {
            FinishAppBar(
                title = translateMap.textOf(DIALOG_FINALIZE_FORM_SO_TTL),
                onBackPressed = {
                    ToolBox.alertMSG_YES_NO(
                        context,
                        uiState.translateMap.textOf(
                            DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_TTL
                        ),
                        uiState.translateMap.textOf(
                            DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_MSG
                        ),
                        { confirmDialog, _ ->
                            confirmDialog.dismiss()
                            onDialogDismiss?.invoke()
                        },
                        1,
                    )
                }
            )
        }
    ) { innerPadding ->

        ContentScreen(
            verticalScrollState,
            innerPadding,
            uiState,
            translateMapLib,
            onBarcodeMkEditText,
            arguments,
            onBackupMachineWsProcess,
            onDialogDismiss
        )
    }
}

@Composable
fun Modifier.contentModifier(
    innerPadding: PaddingValues? = null,
    verticalScrollState: ScrollState
) = if (innerPadding != null) {
    this
        .fillMaxSize()
        .verticalScroll(verticalScrollState)
        .verticalScrollBar(ratio = 2f, state = verticalScrollState)
        .padding(innerPadding)
        .padding(horizontal = NamoaTheme.spacing.medium)
} else {
    this
        .fillMaxSize()
        .animateContentSize()
        .padding(horizontal = NamoaTheme.spacing.medium)
}

@Composable
private fun ContentScreen(
    verticalScrollState: ScrollState,
    innerPadding: PaddingValues? = null,
    uiState: FinishState,
    translateMapLib: TranslateMap,
    onBarcodeMkEditText: OnBarcodeMkEditText,
    arguments: FinishScreenArguments,
    onBackupMachineWsProcess: ((String?) -> Unit)?,
    onDialogDismiss: (() -> Unit)?
) {

    Column(
        modifier = Modifier.contentModifier(innerPadding, verticalScrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedContent(
            modifier = Modifier.animateContentSize(),
            targetState = uiState.isLoading || uiState.data == null,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(1000)
                ) { width ->
                    width
                } + fadeIn(animationSpec = tween(500)) togetherWith
                        slideOutHorizontally(tween(1000)) { width -> -width } +
                        fadeOut(tween(500))
            },
            label = ""
        ) { isLoading ->
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = NamoaTheme.spacing.extraMassive),
                    color = NamoaTheme.colors.primary
                )
            } else {
                FinishScreen(
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxSize(),
                    translateLib = translateMapLib,
                    onBarcodeMkEditText = onBarcodeMkEditText,
                    isReadOnly = arguments.isReadOnly,
                    verticalScrollState = verticalScrollState,
                    onBackupMachineWsProcess = onBackupMachineWsProcess,
                    onCancel = { onDialogDismiss?.invoke() }
                )
            }
        }

    }
}

@Composable
fun ShowBallon(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = NamoaTheme.colors.error
) {
    Surface(
        modifier = modifier.animateContentSize(),
        shape = RoundedCornerShape(NamoaTheme.spacing.small),
        color = backgroundColor.copy(
            alpha = 0.34f
        )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = NamoaTheme.spacing.small
                ),
            style = NamoaTheme.typography.bodyMedium,
            text = text,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun FinishScreen(
    modifier: Modifier = Modifier,
    translateLib: TranslateMap,
    onBackupMachineWsProcess: ((String?) -> Unit)? = null,
    onBarcodeMkEditText: OnBarcodeMkEditText,
    verticalScrollState: ScrollState,
    isReadOnly: Boolean = false,
    onCancel: () -> Unit
) {

    val viewModel = hiltViewModel<FinishOSViewModel>()
    val state by viewModel.state.collectAsState()
    val uiState = state
    val scope = rememberCoroutineScope()
    val isValidForm = state.isValidForm
    val componentError = isValidForm.component

    var finishValid by remember {
        mutableStateOf(
            FinishValidation(
                partialExecutionOS = uiState.data?.infoOs?.partitionMinDate,
            )
        )
    }


    LaunchedEffect(state.backupMachineWSProgress) {
        if (state.backupMachineWSProgress) {
            onBackupMachineWsProcess?.invoke(WS_Product_Serial_Backup::class.java.name)
        } else {
            onBackupMachineWsProcess?.invoke(null)
        }
    }

    LaunchedEffect(finishValid) {
        viewModel.validateForm(
            finishValid.copy(
                validAfterMachineStopped = uiState.data?.showOptionsStopped ?: false
            ),
            editedField = finishValid.infoOs.editedField,
            isReadOnly = isReadOnly
        )
    }

    Column(
        modifier = modifier,
    ) {
        if (uiState.data!!.showBalloonVerify && !isReadOnly) {
            ShowBallon(
                modifier = Modifier.padding(NamoaTheme.spacing.medium),
                text = uiState.translateMap.textOf(DIALOG_FINALIZE_OS_EMPTY_VERIFY_LBL)
            )
        }
        if (uiState.data.requiredByTicketLeft > 0 && !isReadOnly) {
            ShowBallon(
                modifier = Modifier.padding(NamoaTheme.spacing.medium),
                text = "${uiState.translateMap.textOf(DIALOG_FINALIZE_OS_REQUIRED_BY_TICKET_LBL)}: ${uiState.data.requiredByTicketLeft} ",
            )
        }
        if (uiState.data.showInitialStateMachine) {
            MachineInitialComponent(
                modifier = Modifier.fillMaxWidth(),
                showOptionsWhenMachineStopped = uiState.data.showOptionsStopped,
                isVersionMachineStopped = uiState.data.machineOsInitial.isSerialStopped ?: false,
                initialDate = uiState.data.machineOsInitial.date ?: "",
                responsibleStop = uiState.data.machineOsInitial.responsibleStop ?: NO_STOPPED,
                translateMap = uiState.translateMap,
                componentError = if (componentError.containsKey(FinishValidation.Component.InitialMachine)) componentError[FinishValidation.Component.InitialMachine] else null,
                translateLib = translateLib,
                isReadOnly = isReadOnly,
                onOptionSelected = { machineStatus ->
                    finishValid = finishValid.copy(initialMachineStatus = machineStatus)
                }
            )

            Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))
        }
        if (uiState.data.showBkupMachine) {
            BackupMachineSerialComponent(
                modifier = Modifier.fillMaxWidth(),
                translateMap = uiState.translateMap,
                backupMachine = uiState.data.backupMachine,
                isReadOnly = isReadOnly,
                onBarcodeMkEditText = onBarcodeMkEditText,
                backupMachineList = uiState.data.backupMachineListState,
                onSelectBackupMachine = { backupMachine ->
                    scope.launch {
                        viewModel.setBackupSerial(
                            backupMachine
                        )
                    }
                },
                onBackupMachineSearch = { serialId, autoSelection ->
                    scope.launch {
                        viewModel.getBackupSerial(
                            serialId,
                            autoSelection
                        )
                    }
                },
                onBackupMachineClear = {
                    viewModel.clearBackupMachine()
                }
            ) { serial ->
                finishValid = finishValid.copy(
                    backupMachine = FinishValidation.BackupMachine(
                        hasBackupMachine = serial?.hasBackupMachine ?: false,
                        productCode = serial?.productCode,
                        productId = serial?.productId,
                        productDesc = serial?.productDesc,
                        serialCode = serial?.serialCode,
                        serialId = serial?.serialId,
                    )
                )
            }

            Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))
        }

        InfoOSComponent(
            modifier = Modifier.fillMaxWidth(),
            translateMap = uiState.translateMap,
            translateLib = translateLib,
            infoOs = uiState.data.infoOs,
            isReadOnly = isReadOnly,
            componentError = if (componentError.containsKey(FinishValidation.Component.InfoOS)) componentError[FinishValidation.Component.InfoOS] else null,
            onInitialDateTimeSelected = { start, end, editedField ->
                val updatedValid = finishValid.copy(infoOs = finishValid.infoOs.copy(
                    dateStart = start,
                    dateEnd = end,
                    editedField = editedField
                ))

                finishValid = updatedValid
            },
        )

        Spacer(modifier = Modifier.height(NamoaTheme.spacing.large))

        AfterFinishOSComponent(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(),
            translateMap = uiState.translateMap,
            translateLib = translateLib,
            isReadOnly = isReadOnly,
            machineStateFinal = uiState.data.machineOsFinal,
            newServiceState = uiState.data.hasNewService,
            showResponsibleOptions = uiState.data.showOptionsStopped,
            showFinalStateMachine = uiState.data.showFinalStateMachine,
            verticalScrollState = verticalScrollState,
            componentError = if (componentError.containsKey(FinishValidation.Component.ScheduleReturnForm)) componentError[FinishValidation.Component.ScheduleReturnForm] else null,
            onMachineStopped = { finishValid = finishValid.copy(finalMachineStopped = it) },
            onScheduleFinish = { finishValid = finishValid.copy(hasNewService = it) }
        )

        Spacer(modifier = Modifier.height(NamoaTheme.spacing.large))

        if (!isReadOnly) {
            val context = LocalContext.current

            ButtonFinishComponent(
                modifier = Modifier.fillMaxWidth(),
                translateMap = uiState.translateMap,
                isEnabled = state.isValidForm.isValid
                        && checkRequiredItems(uiState.data, finishValid)
                ,
                onDone = {
                    scope.launch {
                        finishValid = finishValid.copy(
                            backupMachine = FinishValidation.BackupMachine(
                                productCode = uiState.data.backupMachine?.productCode,
                                productId = uiState.data.backupMachine?.productId,
                                productDesc = uiState.data.backupMachine?.productDesc,
                                serialCode = uiState.data.backupMachine?.serialCode,
                                serialId = uiState.data.backupMachine?.serialId,
                            )
                        )
                        viewModel.saveFinish(finishValid)
                    }
                },
                onCancel = {
                    ToolBox.alertMSG_YES_NO(
                        context,
                        uiState.translateMap.textOf(
                            DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_TTL
                        ),
                        uiState.translateMap.textOf(
                            DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_MSG
                        ),
                        { dialog, _ ->
                            dialog.dismiss()
                            onCancel()
                        },
                        1,
                    )
                }
            )

            Spacer(modifier = Modifier.height(NamoaTheme.spacing.large))
        }
    }
}

@Composable
private fun checkRequiredItems(
    data: FinishOsData,
    finishValid: FinishValidation
): Boolean = (data.requiredByTicketLeft <= 0
        || (
            finishValid.hasNewService != null
            && (
                    finishValid.hasNewService is NewServiceChoose.PLANNING
                    || finishValid.hasNewService is NewServiceChoose.RETURN
                )
            )
        )


@Composable
fun ButtonFinishComponent(
    modifier: Modifier = Modifier,
    translateMap: TranslateMap,
    isEnabled: Boolean,
    onCancel: () -> Unit,
    onDone: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {

            val (cancel, save) = createRefs()


            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = NamoaTheme.spacing.extraSmall)
                    .constrainAs(cancel) {
                        start.linkTo(parent.start)
                        end.linkTo(save.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
                shape = RoundedCornerShape(NamoaTheme.spacing.mediumLarge),
                onClick = onCancel
            ) {
                Text(
                    text = translateMap.textOf(DIALOG_FINALIZE_OS_BTN_CANCEL),
                    maxLines = 1
                )
            }

            Button(
                modifier = Modifier
                    .padding(start = NamoaTheme.spacing.extraSmall)
                    .fillMaxWidth()
                    .constrainAs(save) {
                        start.linkTo(cancel.end)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
                shape = RoundedCornerShape(NamoaTheme.spacing.mediumLarge),
                onClick = onDone,
                enabled = isEnabled
            ) {
                Text(
                    text = translateMap.textOf(DIALOG_FINALIZE_OS_BTN_SAVE),
                    maxLines = 1
                )
            }
        }


    }
}
