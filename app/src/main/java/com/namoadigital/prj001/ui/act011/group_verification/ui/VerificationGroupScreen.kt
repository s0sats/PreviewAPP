package com.namoadigital.prj001.ui.act011.group_verification.ui

import RowNamoaBadge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.namoa_digital.namoa_library.compose.components.ApplicationSnackBar
import com.namoa_digital.namoa_library.compose.components.lifecycle.LifeCycleEffect
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.translate
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.SECTION_ITEM_WITHOUT_GROUP_LBL
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.SECTION_SELECT_VERIFICATION_GROUP_LBL
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupViewModel
import com.namoadigital.prj001.ui.act011.group_verification.composable.components.VerificationGroupCard
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroupEvent
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroupState

@Composable
fun VerificationGroupScreen(
    geOs: GeOs,
    productCode: Long,
    serialCode: Long,
    isReadOnly: Boolean,
    isContinuousForm: Boolean,
    translateMap: TranslateMap,
    onProgressDialog: () -> Boolean?,
    onErrorGetGroupList: () -> Unit
) {

    val viewModel = hiltViewModel<VerificationGroupViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val listGroups = state.value.listGroups
    val stateLoading = state.value.stateLoading
    val isError = state.value.error
    val updateScreens = state.value.updateScreens
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(updateScreens) {
        if (!updateScreens) return@LaunchedEffect
        viewModel.onEvent(VerificationGroupEvent.OnUpdateScreens(onProgressDialog()))
    }

    LifeCycleEffect(
        onResume = {
            viewModel.onEvent(
                VerificationGroupEvent.OnHandleListVerificationGroup(
                    hasProcessVg = geOs.getProcessVg(),
                    formPKs = VerificationGroupState.FormPK(
                        customerCode = geOs.customer_code,
                        customFormType = geOs.custom_form_type,
                        customFormCode = geOs.custom_form_code,
                        customFormVersion = geOs.custom_form_version,
                        customFormData = geOs.custom_form_data,
                        productCode = productCode,
                        serialCode = serialCode,
                    ),
                    isReadOnly = isReadOnly
                )
            )
        }
    )

    LaunchedEffect(Unit) {
        viewModel.snackbarMessages.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (isError != null) {
        onErrorGetGroupList()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    when {
                        stateLoading.isLoading -> Modifier.blur(NamoaTheme.spacing.medium)
                        else -> Modifier
                    }
                )
                .padding(horizontal = NamoaTheme.spacing.medium),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(NamoaTheme.spacing.mediumSmall)
            ) {
                item {
                    Text(
                        translateMap.translate(key = SECTION_SELECT_VERIFICATION_GROUP_LBL),
                        style = NamoaTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))
                }

                items(listGroups.filter { it.vgCode != null }, key = { it.vgCode!! }) { group ->
                    VerificationGroupCard(
                        group = group,
                        translateMap = translateMap,
                        onSwitchChange = {
                            if(stateLoading.isLoading) return@VerificationGroupCard
                            viewModel.onEvent(
                                VerificationGroupEvent.onGroupSwitchChange(
                                    isContinuousForm,
                                    group.vgCode!!,
                                    it
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))
                }

                item {
                    Spacer(modifier = Modifier.height(NamoaTheme.spacing.medium))
                    Text(
                        translateMap.translate(key = SECTION_ITEM_WITHOUT_GROUP_LBL),
                        style = NamoaTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(NamoaTheme.spacing.small))
                    RowNamoaBadge(
                        balls = state.value.listGroups.filter { it.vgCode == null }
                            .flatMap { it.alerts }
                    )
                    Spacer(modifier = Modifier.height(NamoaTheme.spacing.small))
                }
            }
        }

        if (stateLoading.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.4f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = translateMap.translate(key = stateLoading.message),
                    modifier = Modifier.paddingFromBaseline(top = NamoaTheme.spacing.mediumLarge),
                    textAlign = TextAlign.Center
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(NamoaTheme.spacing.medium),
            snackbar = { data -> ApplicationSnackBar(snackBarData = data) }
        )
    }
}