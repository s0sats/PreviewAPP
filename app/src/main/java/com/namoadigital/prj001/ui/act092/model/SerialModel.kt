package com.namoadigital.prj001.ui.act092.model

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MyActions

data class SerialModel(
    val originFlow: String? = null,
    val customerCode: Int? = null,
    var tagOperCode: Int? = null,
    var siteCode: String? = null,
    var productCode: Int? = null,
    var productId: String? = null,
    var productDesc: String? = null,
    var serialCode: Long? = null,
    var serialId: String? = null,
    var ticketId: String? = null,
    var calendarDate: String? = null,
    var userFocus: Int = 1,
    val multStepsLbl: String? = null,
    val hmAux: HMAux? = null,
    val lastSelectedPk: String? = null,
    val lastSelectActionType: String? = null,
    val classColor: String = ""
) {

    fun getLastSelectedPk(actionType: String = MyActions.MY_ACTION_TYPE_FORM) =
        if (lastSelectedPk == actionType) {
            lastSelectedPk
        } else {
            null
        }

}