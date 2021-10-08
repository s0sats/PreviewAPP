package com.namoadigital.prj001.ui.act011.frags

import com.namoadigital.prj001.model.AcessoryFormView

interface InspectionListFragmentInteraction {
    fun onInspectionSelected(
        inspection: AcessoryFormView,
        isNewItem: Boolean,
        position: Int,
        textFilter: String
    )
}
