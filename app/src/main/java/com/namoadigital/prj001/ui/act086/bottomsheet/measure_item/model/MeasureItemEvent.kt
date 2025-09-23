package com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model

sealed class MeasureItemEvent {
    data class Setup(val data: MeasureItemArguments) : MeasureItemEvent()
    data class OnMeasureChanged(val value: String) : MeasureItemEvent()
    data class OnIdChanged(val value: String, val validateID: Boolean = false) : MeasureItemEvent()
    data class AttemptSave(val onValidate: () -> Unit) : MeasureItemEvent()
}
