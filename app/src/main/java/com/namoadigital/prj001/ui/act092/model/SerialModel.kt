package com.namoadigital.prj001.ui.act092.model

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MyActions

data class SerialModel(
    val originFlow: String? = null,
    val customerCode: Int? = null,
    var tagOperCode: Int? = null,
    var siteCode: String? = null,
    var siteCodeBack: String? = null,
    var zoneCodeBack: Int = 0,
    var productCode: Int? = null,
    var productId: String? = null,
    var productDesc: String? = null,
    var serialCode: Long? = null,
    var serialId: String? = null,
    var ticketId: String? = null,
    var calendarDate: String? = null,
    var userFocus: Int = 1, //Toggle ativado: User Focus = 1, desativado = 0
    val multStepsLbl: String? = null,
    val hmAux: HMAux? = null,
    val lastSelectedPk: String? = null,
    val lastSelectActionType: String? = null,
    val classColor: String? = null,
    val mainUserFocus: Boolean = false,
    val editFilter: String? = null,
    val otherSerialIsFiltered: Boolean = false,
) {

    fun getLastSelectedPk(actionType: String) =
        if (lastSelectActionType == actionType) {
            lastSelectedPk
        } else {
            null
        }


}