package com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.namoa_digital.namoa_library.R.color
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.translate
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormBackupMachineList
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormField
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.TitleSection
import com.namoadigital.prj001.ui.act011.finish_os.ui.component.TitleSwitch
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_BACKUP_MACHINE_SWITCH_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_BACKUP_MACHINE_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_SELECT_BACKUP_MACHINE_EMPTY_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_SELECT_BACKUP_MACHINE_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_SELECT_BACKUP_SERIAL_HELP_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_SELECT_BACKUP_SERIAL_HINT
import com.namoadigital.prj001.util.ToolBox_Con

typealias OnBarcodeMkEditText = (mkEditTextNM: MKEditTextNM) -> Unit

@Composable
fun BackupMachineSerialComponent(
    modifier: Modifier = Modifier,
    translateMap: TranslateMap,
    backupMachine: FinishFormField.BackupMachine?,
    isReadOnly: Boolean = false,
    backupMachineList: FinishFormBackupMachineList,
    onSelectBackupMachine: (BaseSerialSearchItem.BackupMachineSerialItem) -> Unit,
    onBarcodeMkEditText: OnBarcodeMkEditText,
    onBackupMachineClear: () -> Unit,
    onBackupMachineSearch: (String?, Boolean) -> Unit,
    onSerial: (FinishFormField.BackupMachine?) -> Unit,
) {
    //
    val requiredColor =
        colorResource(id = color.m3_namoa_error_form_fields)
    //
    var showMachineBackupListDialog by remember {
        mutableStateOf(false)
    }
    //
    var backupMachineSwitchState by remember {
        mutableStateOf(backupMachine?.hasBackupMachine ?: false)
    }
    var backupMachineListState by remember {
        mutableStateOf(backupMachineList)
    }
    val serialSearchMode = backupMachine?.serialId?.isBlank() ?: true

    var serialIdSearchField by remember {
        mutableStateOf("")
    }

    var rowColor by remember {
        mutableStateOf(Color.Transparent)
    }

    LaunchedEffect(backupMachineList) {
        showMachineBackupListDialog = backupMachineList.backupList != null
        backupMachineListState = backupMachineList
    }

    LaunchedEffect(backupMachineSwitchState, backupMachine) {

        rowColor = if (!isReadOnly
            && backupMachineSwitchState
            && (backupMachine == null || !(backupMachine.isValid))
        ) {
            requiredColor
        } else {
            Color.Transparent
        }

        onSerial(
            backupMachine?.copy(
                hasBackupMachine = backupMachineSwitchState
            )
        )
    }
//    if (backupSerial != null) {
    Column(modifier = modifier) {
        TitleSection(text = translateMap.translate(DIALOG_FINALIZE_BACKUP_MACHINE_TTL))

        Box(
            modifier = Modifier
                .padding(top = NamoaTheme.spacing.extraExtraSmall)
                .border(width = 1.dp, color = rowColor, shape = RoundedCornerShape(8.dp))
        ) {

            TitleSwitch(
                title = translateMap.translate(DIALOG_FINALIZE_BACKUP_MACHINE_SWITCH_TTL),
                isRequiredOption = backupMachineSwitchState,
                isEnabled = !isReadOnly,
                initialSwitchState = backupMachineSwitchState,
                onSwitchChecked = { check ->
                    backupMachineSwitchState = check
                    //
                    serialIdSearchField = ""

                    onSerial(
                        backupMachine?.copy(
                            hasBackupMachine = check,
                            productCode = null,
                            productId = null,
                            productDesc = null,
                            serialCode = null,
                            serialId = null,
                        )
                    )
                    if (!check) {
                        onBackupMachineClear()
                    }
                    //
                }) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = NamoaTheme.spacing.mediumSmall,
                            bottom = NamoaTheme.spacing.none
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.0f, true)
                            .align(Alignment.Top)
                    ) {
                        AndroidView(
                            modifier = modifier.padding(bottom = NamoaTheme.spacing.extraExtraSmall),
                            factory = { context ->
                                //
                                val textInputLayout = TextInputLayout(
                                    context,
                                    null,
                                    R.style.NamoaTheme3_TextInputLayout_Outlined
                                ).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    setErrorTextAppearance(R.style.TextInputLayoutHelperTextColorRequired)
                                    isHintEnabled = true
                                    isErrorEnabled = false
                                    isHelperTextEnabled = true
                                    helperText = backupMachine?.productDesc
                                    hint = translateMap.translate(DIALOG_SELECT_BACKUP_SERIAL_HINT)
                                    boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
                                    val cornerRadiusPx = TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        6f,
                                        context.resources.displayMetrics
                                    )
                                    setBoxCornerRadii(
                                        cornerRadiusPx,
                                        cornerRadiusPx,
                                        cornerRadiusPx,
                                        cornerRadiusPx
                                    )
                                }
                                //
                                val mkEditTextNM = MKEditTextNM(context)
                                mkEditTextNM.id = View.generateViewId()
                                onBarcodeMkEditText(mkEditTextNM)
                                mkEditTextNM.apply {
                                    isEnabled = serialSearchMode
                                    val density = context.resources.displayMetrics.density
                                    setPadding(
                                        (16 * density).toInt(),
                                        (16 * density).toInt(),
                                        (16 * density).toInt(),
                                        (16 * density).toInt()
                                    )
                                    background = null
                                    setTextColor(
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                context,
                                                R.color.m3_namoa_onSurface
                                            )
                                        )
                                    )


                                    backupMachine?.let {
                                        setText(it.serialId)
                                    }
                                    addTextChangedListener {
                                        serialIdSearchField = it.toString()
                                    }

                                    setDelegateTextBySpecialist {
                                        text?.let {
                                            onBackupMachineSearch(it.toString(), true)
                                        }
                                    }
                                }
                                textInputLayout.addView(mkEditTextNM)

                                textInputLayout
                            },
                            update = { textInputLayout ->
                                //
                                val view = textInputLayout.editText as MKEditTextNM
                                //
                                if (backupMachine?.isValid == true
                                    && backupMachineSwitchState
                                ) {
                                    view.setText(backupMachine.serialId)
                                    textInputLayout.helperText = backupMachine.productDesc
                                } else {
                                    if (serialIdSearchField.isBlank()) {
                                        view.setText(serialIdSearchField)
                                    }
                                    //
                                    textInputLayout.isErrorEnabled = true
                                    textInputLayout.error =
                                        translateMap.translate(DIALOG_SELECT_BACKUP_SERIAL_HELP_LBL)
                                    //
                                }
                                //
                                view.apply {
                                    isEnabled = serialSearchMode
                                    view.setmBARCODE(serialSearchMode)
                                }
                            }
                        )
                    }
                    //
                    Spacer(modifier = Modifier.width(NamoaTheme.spacing.mediumLarge))
                    //
                    Column {

                        Spacer(modifier = Modifier.height(NamoaTheme.spacing.mediumSmall))

                        IconButton(
                            modifier = Modifier
                                .size(40.dp)
                                .border(1.dp, Color.Gray, CircleShape)
                                .clip(CircleShape),
                            enabled = !(serialSearchMode && serialIdSearchField.isNullOrBlank()) && !isReadOnly,
                            onClick = {
                                if (serialSearchMode) {
                                    onBackupMachineSearch(serialIdSearchField, false)
                                } else {
                                    serialIdSearchField = ""
                                    onSerial(
                                        backupMachine?.copy(
                                            hasBackupMachine = backupMachine.hasBackupMachine
                                                ?: false,
                                            productCode = null,
                                            productId = null,
                                            productDesc = null,
                                            serialCode = null,
                                            serialId = null,
                                        )
                                    )
                                    onBackupMachineClear()
                                }
                            },
                        ) {
                            if (serialSearchMode) {
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    modifier = Modifier.size(18.dp),
                                    contentDescription = null,
                                )
                            }
                        }
                    }

                }
            }
        }
    }

    if (showMachineBackupListDialog) {
        BackupMachineListDialog(
            backupMachineListState,
            translateMap,
            onItemSelect = { value ->
                onSelectBackupMachine(value)
            },
            onDialogDismiss = {
                showMachineBackupListDialog = false
                backupMachineListState.backupList = null
            }
        )
    }
}

@Composable
fun BackupMachineListDialog(
    backupMachineList: FinishFormBackupMachineList,
    translateMap: TranslateMap,
    onItemSelect: (value: BaseSerialSearchItem.BackupMachineSerialItem) -> Unit,
    onDialogDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDialogDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            decorFitsSystemWindows = true,
        )
    ) {
        // Conteúdo do diálogo
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 16.dp,
                ),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(
                        text = translateMap.translate(DIALOG_SELECT_BACKUP_MACHINE_TTL),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                    if (!backupMachineList.fromRemote) {
                        Icon(
                            imageVector = Icons.Filled.WifiOff,
                            contentDescription = null,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(
                    targetState = backupMachineList.backupList == null,
                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec = tween(1000)
                        ) { width ->
                            width
                        } + fadeIn(animationSpec = tween(1000)) togetherWith
                                slideOutHorizontally(tween(1000)) { width -> width } +
                                fadeOut(tween(1000))
                    },
                    label = ""
                ) { isLoading ->
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = NamoaTheme.colors.primary
                        )
                    } else {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (!backupMachineList.backupList.isNullOrEmpty()) {
                                // Exibe a lista usando LazyColumn
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    items(backupMachineList.backupList!!.size) { index ->
                                        BackupMachineItem(
                                            backupMachineList.backupList!![index]
                                        ) {
                                            onItemSelect(backupMachineList.backupList!![index])
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    text = translateMap.translate(
                                        DIALOG_SELECT_BACKUP_MACHINE_EMPTY_LBL
                                    ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Black
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                TextButton(onClick = { onDialogDismiss() }) {
                                    Text(
                                        text = translateMap.translate("sys_alert_btn_cancel"),
                                        style = NamoaTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackupMachineItem(
    value: BaseSerialSearchItem.BackupMachineSerialItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.extraSmall,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Text(
                text = value.serialId,
                style = MaterialTheme.typography.bodyLarge,
                color = NamoaTheme.colors.onSurface
            )
            Text(
                text = value.productDesc,
                color = NamoaTheme.colors.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
            value.siteDesc?.let {
                val textColor =
                    if (value.siteCode.toString() == ToolBox_Con.getPreference_Site_Code(
                            LocalContext.current
                        )
                    ) {
                        NamoaTheme.colors.onSurfaceVariant
                    } else {
                        NamoaTheme.colors.error
                    }

                Text(
                    text = value.siteDesc,
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
