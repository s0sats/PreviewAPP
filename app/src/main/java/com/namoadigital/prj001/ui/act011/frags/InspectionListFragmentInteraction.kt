package com.namoadigital.prj001.ui.act011.frags

import com.namoadigital.prj001.model.AcessoryFormView
import com.namoadigital.prj001.model.InspectionCellActions

interface InspectionListFragmentInteraction {
    fun onInspectionSelected(
        inspection: AcessoryFormView,
        action: InspectionCellActions,
        position: Int,
        textFilter: String
    )
}
