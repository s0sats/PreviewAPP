package com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model

import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.viewmodel.TranslateState

data class MeasureItemState(
    val isLoading: Boolean = true,
    override val translate: TranslateMap = emptyMap(),
    val commonData: CommonData? = null,
) : TranslateState {
    data class CommonData(
        val measure: String?,
        val unit: String? = null,
        val currentState: MeasureItemArguments.State,
        val measureAlert: MeasureItemArguments.MeasureAlert? = null,
        val measureID: MeasureItemArguments.MeasureID? = null,
        val validationError: List<ValidationError> = emptyList()
    )


    sealed class ValidationError(
        open val errorMessage: String
    ) {
        sealed class Measure(override val errorMessage: String) : ValidationError(errorMessage) {
            data class Min(val minValue: Double) : Measure(MeasureItemKey.MeasureMinLbl.key)
            data class Max(val maxValue: Double) : Measure(MeasureItemKey.MeasureMaxLbl.key)
            object Required : Measure(MeasureItemKey.MeasureRequiredLbl.key)
        }

        sealed class Id(override val errorMessage: String) : ValidationError(errorMessage) {
            object Required : Id(MeasureItemKey.IdRequiredLbl.key)
        }

    }
}
