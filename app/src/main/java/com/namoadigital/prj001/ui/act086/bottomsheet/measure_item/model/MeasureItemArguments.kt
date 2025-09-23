package com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model

import com.namoadigital.prj001.extensions.parseDatePair
import com.namoadigital.prj001.extensions.toStringConsiderDecimal

data class MeasureItemArguments(
    val value: Double? = null,
    val unit: String? = null,
    val state: State,
    val measureAlert: MeasureAlert? = null,
    val measureID: MeasureID? = null,
    val isReadOnly: Boolean = false,
) {

    enum class State {
        INITIAL, EDIT_INITIAL, FINAL
    }

    data class MeasureAlert(
        val min: Double? = null,
        val max: Double? = null,
    )

    data class MeasureID(
        val id: String? = null,
        val isRequiredId: Boolean? = null,
        val idEditable: String? = null
    ) {
        val isEditableId = id == null && isRequiredId == true
    }
}


data class MeasureItemData(
    val value: Double? = null,
    val unit: String? = null,
    val lastAlert: Boolean? = null,
    val date: String? = null,
    val context: MeasureBottomSheetContext? = null,
) {
    val showLastMeasure = value != null && unit != null && date != null

    val formatted = "${value.toStringConsiderDecimal()}$unit ${date?.parseDatePair()?.first}"
}


data class MeasureLabels(
    val subtitle: MeasureItemKey,
    val buttonNext: MeasureItemKey,
)

sealed class MeasureBottomSheetContext(
    open val arguments: MeasureItemArguments,
    open val labels: MeasureLabels
) {

    data class Initial(
        override val arguments: MeasureItemArguments,
    ) : MeasureBottomSheetContext(
        arguments = arguments,
        labels = MeasureLabels(
            subtitle = MeasureItemKey.SubTitleBeforeMaintenanceLbl,
            buttonNext = MeasureItemKey.NextButtonLbl,
        )
    )

    data class EditInitial(
        override val arguments: MeasureItemArguments
    ) : MeasureBottomSheetContext(
        arguments = arguments,
        labels = MeasureLabels(
            subtitle = MeasureItemKey.SubTitleBeforeMaintenanceLbl,
            buttonNext = MeasureItemKey.SaveButtonLbl,
        )
    )

    data class Final(
        override val arguments: MeasureItemArguments,
    ) : MeasureBottomSheetContext(
        arguments = arguments,
        labels = MeasureLabels(
            subtitle = MeasureItemKey.SubTitleAfterMaintenanceLbl,
            buttonNext = MeasureItemKey.SaveButtonLbl,
        )
    )

}


