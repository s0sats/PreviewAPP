package com.namoadigital.prj001.ui.act011.frags

import com.namoadigital.prj001.model.AcessoryFormView
import com.namoadigital.prj001.model.InspectionCell

interface InspectionListFragmentInteraction {
    fun onInspectionSelected(
        acessoryFormView: AcessoryFormView,
        isNewItem: Boolean,
        position: Int,
        searchFilterValue: String,
        chkStatus: Boolean,
        itemCodeAndSeqPk: String
    )
    //
    fun onNotVerifyAction(itemPk: String): InspectionCell
    fun onRefreshTabCounter(tabIndex: Int)
    fun getObjectView(position: Int): AcessoryFormView
}
