package com.namoadigital.prj001.core.data.local.preferences.barcode_settings

import android.content.Context
import android.content.SharedPreferences
import com.namoadigital.prj001.core.data.domain.model.BarCodeFlow
import com.namoadigital.prj001.core.data.domain.model.BarCodeTypeFlow
import com.namoadigital.prj001.core.data.domain.preference.ModelPreferences

class BarCodeFlowPref constructor(
    private val pref: SharedPreferences
) : ModelPreferences<BarCodeFlow> {


    override fun write(model: BarCodeFlow) {
        with(pref) {
            edit()
                .putString(FLOW_BARCODE, model.flowBarcode.toString())
                .apply()
        }
    }

    override fun read(): BarCodeFlow {
        with(pref) {
            return BarCodeFlow(
                flowBarcode = BarCodeFlow.toType(pref.getString(FLOW_BARCODE, BarCodeTypeFlow.DEFAULT.name) ?: BarCodeTypeFlow.DEFAULT.name)
            )
        }
    }

    companion object {

        const val FLOW_BARCODE = "FLOW_BARCODE"

        fun instance(context: Context): BarCodeFlowPref {
            return BarCodeFlowPref(
                context.getSharedPreferences("barcode_settings", Context.MODE_PRIVATE)
            )
        }

    }

}