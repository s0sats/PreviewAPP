package com.namoadigital.prj001.ui.act083.model

import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.util.ConstantBaseApp

data class SaveActionFilterModel(
    var originFlow: String = ConstantBaseApp.ACT005,
    val siteCodeBack: String? = null,
    val initialTextFilter: String? = null,
    val tagFilterDesc: String? = null,
    val productDesc: String? = null,
    val productId: String? = null,
    val lastSelectActionPk: String? = null,
    val lastSelectedActionType: String? = null,
    val serialId: String? = null,
    val serialCode: Long? = null,
    val ticketId: String? = null,
    val calendarDate: String? = null,
    val siteCode: String? = null,
    val zoneCodeBack: Int? = null,
    val initialTabToLoad: Int = 1,
    val tagFilter: Int? = null,
    val productCode: Int? = null,
    var mainUserFilterState: Boolean = false
) {

    companion object {

        fun SaveActionFilterModel.toMyActionFilter() = MyActionFilterParam(
            tagFilterCode = tagFilter,
            tagFilterDesc = tagFilterDesc,
            productCode = productCode,
            productId = productId,
            productDesc = productDesc,
            serialCode = serialCode,
            serialId = serialId,
            ticketId = ticketId,
            calendarDate = calendarDate
        ).apply {
            mainUserFilterState = this@toMyActionFilter.mainUserFilterState
            paramItemSelectedPk = this@toMyActionFilter.lastSelectActionPk
            paramItemSelectedTab = this@toMyActionFilter.initialTabToLoad
            paramItemSelectedType = this@toMyActionFilter.lastSelectedActionType
            originFlow = this@toMyActionFilter.originFlow
            paramTextFilter = this@toMyActionFilter.initialTextFilter
        }

    }

}
