package com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.databinding.Act086VerificationFrgBinding
import com.namoadigital.prj001.extensions.toStringConsiderDecimal
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.MeasureItemViewModel
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemArguments
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface MeasurementStateActions {
    fun openBottomSheet(state: MeasureItemArguments.State)
}

data class MeasurementState(
    val initialValue: Double? = null,
    val initialId: String? = null,
    val afterValue: Double? = null,
    val afterId: String? = null,
    val unit: String? = null,
    val measureProgress: MeasureItemArguments.MeasureAlert? = null,
    val measureProgressAlert: MeasureItemArguments.MeasureAlert? = null,
    val optionSelected: Boolean = false,
    val isReadOnly: Boolean = false
) {
    fun hasInitialMeasurement() = initialValue != null
    fun hasAfterMeasurement() = afterValue != null
    fun hasInitialId() = initialId != null
    fun hasAfterId() = afterId != null

    fun withInitialMeasurement(value: Double?, id: String?): MeasurementState =
        copy(initialValue = value, initialId = id)

    fun withAfterMeasurement(value: Double?, id: String?): MeasurementState =
        copy(afterValue = value, afterId = id)

    fun withOptionSelected(selected: Boolean): MeasurementState =
        copy(optionSelected = selected)


    fun determineColor(
        value: Double?,
    ): ColorByMeasure? {
        if (value == null) return null

        val max = measureProgressAlert?.max
        val min = measureProgressAlert?.min

        return when {
            max != null && value >= max -> ColorByMeasure.RED
            min != null && value <= min -> ColorByMeasure.RED
            else -> ColorByMeasure.BLUE
        }
    }

    sealed class ColorByMeasure(val color: Int) {
        object RED : ColorByMeasure(R.color.m3_namoa_error)
        object BLUE : ColorByMeasure(R.color.m3_namoa_titleLarge)
    }
}

data class MeasurementDataForCallback(
    val initialValue: Double?,
    val initialId: String?,
    val initialValueColor: MeasurementState.ColorByMeasure?,
    val afterValue: Double?,
    val afterId: String?,
    val afterValueColor: MeasurementState.ColorByMeasure?
)

class MeasurementStateHolder constructor(
    initialState: MeasurementState,
    scope: CoroutineScope,
    private val onMeasurementChanged: (MeasurementDataForCallback) -> Unit
) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<MeasurementState> = _state.asStateFlow()

    init {
        _state.onEach { currentState ->

            val initialColor = currentState.determineColor(currentState.initialValue)
            val afterColor = currentState.afterValue?.let { currentState.determineColor(it) }

            if (!currentState.isReadOnly) {
                onMeasurementChanged(
                    MeasurementDataForCallback(
                        initialValue = currentState.initialValue,
                        initialId = currentState.initialId,
                        initialValueColor = initialColor,
                        afterValue = currentState.afterValue,
                        afterId = currentState.afterId,
                        afterValueColor = afterColor
                    )
                )
            }

        }.launchIn(scope)
    }

    fun updateState(newState: MeasurementState) {
        _state.value = newState
    }

    fun getCurrentState() = _state.value

}


class MeasurementBinder(
    private val binding: Act086VerificationFrgBinding,
    private val context: Context?,
    private val actions: MeasurementStateActions
) {
    private val translateKeys: TranslateMap by lazy {
        TranslateBuild(context!!)
            .resource(MeasureItemViewModel.MEASURE_ITEM_RESOURCE)
            .listVars { MeasureItemKey.entries.map { it.key } }
            .build()
    }

    private fun calculateProgress(value: Double?, min: Double?, max: Double?): Int {
        if (value == null || min == null || max == null) return 0
        val result = (((value - min) / (max - min)) * 100).toInt()
        return result
    }

    @SuppressLint("SetTextI18n")
    fun bind(state: MeasurementState) = with(binding) {

        if (state.isReadOnly && state.initialValue == null) {
            act086DataMeasure.isVisible = false
            return
        }

        // --- Cores ---
        val colorGray = ResourcesCompat.getColor(
            context?.resources!!,
            R.color.m3_namoa_onSurfaceVariant,
            null
        )
        val colorRequired = ResourcesCompat.getColor(
            context.resources,
            R.color.namoa_color_highlight_required_item,
            null
        )
        val colorRed = ResourcesCompat.getColor(
            context.resources,
            R.color.m3_namoa_error,
            null
        )
        val colorBlue = ResourcesCompat.getColor(
            context.resources,
            R.color.m3_namoa_primary,
            null
        )

        // --- Visibilidade principal ---
        act086DataMeasure.isVisible = true
        tvTitle.text = translateKeys.textOf(MeasureItemKey.MeasureDataTitleLbl.key)
        llInitial.isVisible = true
        llAfter.isInvisible = !state.optionSelected

        llProgressInitial.isVisible = state.hasInitialMeasurement()
        llProgressFinal.isVisible = state.hasAfterMeasurement()

        // --- Icone ---
        ivBarChart.setColorFilter(
            when {
                state.isReadOnly -> colorGray
                state.hasInitialMeasurement() -> colorGray
                else -> colorRequired
            }
        )

        // --- Botão inicial ---
        tvBeforeAction.apply {
            isVisible = !state.hasInitialMeasurement()
            val color = if (state.isReadOnly) colorGray else colorRequired
            setTextColor(ColorStateList.valueOf(color))
            isEnabled = !state.isReadOnly
        }

        // --- Botão final ---
        tvAfterAction.apply {
            isVisible = !state.hasAfterMeasurement()
            val color = if (state.isReadOnly || !state.optionSelected) colorGray else colorRequired
            setTextColor(ColorStateList.valueOf(color))
            isEnabled = !state.isReadOnly && state.optionSelected
        }

        // --- Labels ---
        tvInitialLabel.text = translateKeys.textOf(MeasureItemKey.InitialLbl.key)
        tvAfterLabel.text = translateKeys.textOf(MeasureItemKey.AfterLbl.key)

        // --- Edição inicial ---
        ivEditInitial.isVisible = state.hasInitialMeasurement() && !state.isReadOnly
        tvInitialId.isVisible = state.hasInitialId()
        state.initialId?.let {
            tvInitialId.text = "${translateKeys.textOf(MeasureItemKey.IdLbl.key)} $it"
        }

        // --- Edição final ---
        tvAfterAction.isEnabled = state.optionSelected && state.hasInitialMeasurement()
        ivEditFinal.isVisible = state.hasAfterMeasurement() && !state.isReadOnly
        tvAfterValue.isVisible = state.hasAfterMeasurement()
        tvAfterId.isVisible = state.hasAfterId()
        state.afterId?.let {
            tvAfterId.text = "${translateKeys.textOf(MeasureItemKey.IdLbl.key)} $it"
        }

        // --- Valores iniciais ---
        state.initialValue?.let { measure ->
            progressInitial.apply {
                progress = calculateProgress(
                    measure,
                    state.measureProgress?.min,
                    state.measureProgress?.max
                )
                progressTintList = ColorStateList.valueOf(
                    when (state.determineColor(measure)) {
                        MeasurementState.ColorByMeasure.RED -> colorRed
                        MeasurementState.ColorByMeasure.BLUE -> colorBlue
                        null -> colorGray
                    }
                )
            }
            tvInitialValue.apply {
                text = "${measure.toStringConsiderDecimal()}${state.unit}"
                setTextColor(
                    ColorStateList.valueOf(
                        when (state.determineColor(measure)) {
                            MeasurementState.ColorByMeasure.RED -> colorRed
                            MeasurementState.ColorByMeasure.BLUE -> colorBlue
                            null -> colorGray
                        }
                    )
                )
            }
        }

        // --- Valores finais ---
        state.afterValue?.let { measure ->
            progressAfter.apply {
                progress = calculateProgress(
                    measure,
                    state.measureProgress?.min,
                    state.measureProgress?.max
                )
                progressTintList = ColorStateList.valueOf(
                    when (state.determineColor(measure)) {
                        MeasurementState.ColorByMeasure.RED -> colorRed
                        MeasurementState.ColorByMeasure.BLUE -> colorBlue
                        null -> colorGray
                    }
                )
            }
            tvAfterValue.apply {
                text = "${measure.toStringConsiderDecimal()}${state.unit}"
                setTextColor(
                    ColorStateList.valueOf(
                        when (state.determineColor(measure)) {
                            MeasurementState.ColorByMeasure.RED -> colorRed
                            MeasurementState.ColorByMeasure.BLUE -> colorBlue
                            null -> colorGray
                        }
                    )
                )
            }
        }

        // --- Listeners ---
        if (!state.isReadOnly) {
            val initialClickListener = View.OnClickListener {
                actions.openBottomSheet(MeasureItemArguments.State.EDIT_INITIAL)
            }

            llInitial.setOnClickListener(initialClickListener)
            tvBeforeAction.apply {
                text = translateKeys.textOf(MeasureItemKey.PerformMeasurementBtn.key)
                setOnClickListener(initialClickListener)
            }

            val finalClickListener = View.OnClickListener {
                actions.openBottomSheet(MeasureItemArguments.State.FINAL)
            }

            llAfter.setOnClickListener(finalClickListener)
            tvAfterAction.apply {
                text = translateKeys.textOf(MeasureItemKey.PerformMeasurementBtn.key)
                setOnClickListener(finalClickListener)
            }
        }
    }

}
