package com.namoadigital.prj001.ui.act086.bottomsheet.measure_item

import androidx.lifecycle.viewModelScope
import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.viewmodel.BaseViewModel
import com.namoadigital.prj001.extensions.toStringConsiderDecimal
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemArguments
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemEvent
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemKey
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeasureItemViewModel @Inject constructor(
    translateBuild: TranslateBuild,
) : BaseViewModel<MeasureItemState, MeasureItemEvent>(
    initialState = MeasureItemState(),
    translateBuild = translateBuild,
    applyTranslation = { state, translate -> state.copy(translate = translate) }
) {

    companion object {
        const val MEASURE_ITEM_RESOURCE = "measure_item_resource"
    }

    override fun onEvent(event: MeasureItemEvent) {
        when (event) {
            is MeasureItemEvent.Setup -> setup(event.data)
            is MeasureItemEvent.OnMeasureChanged -> {
                val commonData = state.value.commonData ?: return
                updateState {
                    it.copy(
                        commonData = commonData.copy(
                            measure = event.value,
                            validationError = if (it.commonData?.validationError?.any { err -> err is MeasureItemState.ValidationError.Measure } == true) {
                                emptyList()
                            } else {
                                it.commonData?.validationError ?: emptyList()
                            }
                        )
                    )
                }
            }

            is MeasureItemEvent.OnIdChanged -> {
                val commonData = state.value.commonData ?: return
                updateState {
                    it.copy(
                        commonData = commonData.copy(
                            measureID = commonData.measureID?.copy(idEditable = event.value),
                            validationError = if (it.commonData?.validationError?.any { err -> err is MeasureItemState.ValidationError.Id } == true) {
                                emptyList()
                            } else {
                                it.commonData?.validationError ?: emptyList()
                            }
                        )
                    )
                }
            }

            is MeasureItemEvent.AttemptSave -> {
                validateAndProceed(event.onValidate)
            }
        }
    }

    private fun setup(arguments: MeasureItemArguments) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    commonData = MeasureItemState.CommonData(
                        measure = if (arguments.value == null) "" else arguments.value.toStringConsiderDecimal(),
                        unit = arguments.unit ?: "",
                        measureAlert = arguments.measureAlert,
                        measureID = arguments.measureID,
                        currentState = arguments.state,
                        validationError = emptyList(),
                    ),
                    isLoading = false
                )
            }
        }
    }

    private fun performValidation(): List<MeasureItemState.ValidationError> {
        val commonData = state.value.commonData ?: return emptyList()

        val numericValue = commonData.measure?.toDoubleOrNull()
        val idEditable = commonData.measureID?.idEditable
        val idIsEditable = commonData.measureID?.isEditableId == true
        val min = commonData.measureAlert?.min
        val max = commonData.measureAlert?.max

        val measureHasValue = numericValue != null
        val idHasValue = idIsEditable && !idEditable.isNullOrBlank()

        val errors = mutableListOf<MeasureItemState.ValidationError>()

        // Regras de obrigatoriedade condicional
        when {
            measureHasValue && !idHasValue && idIsEditable ->
                errors.add(MeasureItemState.ValidationError.Id.Required)

            !measureHasValue && idHasValue ->
                errors.add(MeasureItemState.ValidationError.Measure.Required)
        }

        // Regras de faixa
        numericValue?.let { value ->
            min?.takeIf { value < it }
                ?.let { errors.add(MeasureItemState.ValidationError.Measure.Min(it)) }
            max?.takeIf { value > it }
                ?.let { errors.add(MeasureItemState.ValidationError.Measure.Max(it)) }
        }

        // Validações adicionais para estados diferentes de INITIAL
        if (commonData.currentState != MeasureItemArguments.State.INITIAL) {
            if (!measureHasValue) {
                errors.add(MeasureItemState.ValidationError.Measure.Required)
            }
            if (measureHasValue && idIsEditable && !idHasValue) {
                errors.add(MeasureItemState.ValidationError.Id.Required)
            }
        }

        return errors
    }


    private fun validateAndProceed(
        onValidate: (() -> Unit)? = null
    ) {
        val errors = performValidation()
        updateState {
            it.copy(
                commonData = it.commonData?.copy(validationError = errors)
            )
        }

        if (errors.isEmpty()) {
            onValidate?.invoke()
        }
    }


    override suspend fun loadTranslation(): TranslateMap {
        return translateBuild
            .resource(MEASURE_ITEM_RESOURCE)
            .listVars { MeasureItemKey.entries.map { it.key } }
            .build()
    }

}