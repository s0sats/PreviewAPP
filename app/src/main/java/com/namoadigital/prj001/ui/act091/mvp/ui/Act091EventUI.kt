package com.namoadigital.prj001.ui.act091.mvp.ui

import android.content.DialogInterface
import com.google.gson.Gson
import com.namoadigital.prj001.model.SoPackExpressPacksLocal

sealed class Act091EventUI {
    data class OpenBottomSheet(val soPackExpressPacksLocal: SoPackExpressPacksLocal, val updatePackageServices: Boolean) : Act091EventUI(){
        val localJson = Gson().toJson(soPackExpressPacksLocal)
    }

    data class CheckSizeList(val size: Int) : Act091EventUI()

    data class ShowAlertDialogOk(val title: String, val msg: String, val positiveButton: (DialogInterface) -> Unit) : Act091EventUI()
}
