package com.namoadigital.prj001.ui.act086.bottomsheet.measure_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.namoa_digital.namoa_library.compose.theme.NamoaApplicationTheme
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureBottomSheetContext
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemArguments
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.ui.MeasurementActions
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.ui.MeasurementBottomSheetContent

interface MeasureItemBottomSheetActions {
    fun onNavigateReadOnly()
    fun onSaveMeasurement(
        newMeasure: Double?,
        newID: String? = null,
        state: MeasureItemArguments.State
    )
}

class MeasureItemBottomSheet constructor(
    private val measureItemBottomSheet: MeasureBottomSheetContext?,
    private val actions: MeasureItemBottomSheetActions,
) : BottomSheetDialogFragment() {

    override fun onPause() {
        super.onPause()
        dismissAllowingStateLoss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            NamoaApplicationTheme {
                MeasurementBottomSheetContent(
                    bottomSheetContext = measureItemBottomSheet!!,
                    actions = object : MeasurementActions {
                        override fun onNavigateReadOnly() {
                            dismissAllowingStateLoss()
                            actions.onNavigateReadOnly()
                        }

                        override fun onSaveMeasurement(
                            newMeasure: Double?,
                            newID: String?,
                            state: MeasureItemArguments.State
                        ) {
                            actions.onSaveMeasurement(newMeasure, newID, state)
                            dismissAllowingStateLoss()
                        }

                        override fun onCancel() {
                            dismissAllowingStateLoss()
                        }
                    }
                )
            }
        }
    }

}