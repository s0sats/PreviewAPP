package com.namoadigital.prj001.ui.act086.frg_historic

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.Act086HistoricAlert
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceItemHist
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.ArrayList

class Act086HistoricFrgPresenter(
    private val context: Context,
    private val mView: Act086HistoricFrgContract.IView,
    private val hmAuxTrans: HMAux,
): Act086HistoricFrgContract.IPresenter {

    override fun getAlertList(
        itemHist: ArrayList<GeOsDeviceItemHist>,
        measureValueSufix: String?,
        restrictionDecimal: Int?
    ): MutableList<Act086HistoricAlert> {
        val toAlertList = itemHist.filter { hist ->
            hist.exec_type.equals(GeOsDeviceItem.EXEC_TYPE_ALERT, true)
        }.map { hist ->
            //Convert para lista do adapter.
            Act086HistoricAlert(
                alertLbl = hmAuxTrans["still_with_problem_lbl"]!!,
                date = ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(
                        hist.exec_date
                    ),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                measureLbl = hmAuxTrans["last_measure_lbl"]!!,
                measure = getFormattedLastMeasureInfo(hist, measureValueSufix,restrictionDecimal),
                materialLbl = hmAuxTrans["material_requested_lbl"]!!,
                material = if (hist.exec_material == 1) {
                    hmAuxTrans["YES"]!!
                } else {
                    hmAuxTrans["NO"]!!
                },
                comment = hist.exec_comment
            )
        }
        //
        if (toAlertList.isNotEmpty()) {
            //Seta label esta com problema apenas no primeiro item.
            toAlertList[0].alertLbl = hmAuxTrans["has_problem_lbl"]!!
        }
        //
        return toAlertList.toMutableList()
    }

    override fun getFormattedLastMeasureInfo(
        lastFixed: GeOsDeviceItemHist,
        measureValueSufix: String?,
        restrictionDecimal: Int?
    ): String {
        return "${ToolBox_Inf.convertFloatToBigDecimalString(
            lastFixed.exec_value,
            restrictionDecimal ?: 4,
            true
        )} ${measureValueSufix?:""}"
    }
}