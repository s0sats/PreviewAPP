package com.namoadigital.prj001.ui.act086.frg_historic

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.Act086HistoricModel
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
    ): MutableList<Act086HistoricModel> {

        val toAlertList = itemHist.map { hist ->
            //Convert para lista do adapter
            Act086HistoricModel(
                icon = hist.getIcon(),
                titleLbl = hist.getTitleFormated(hmAuxTrans) ?: "",
                date = hist.getDate(context),
                measureLbl = hmAuxTrans["last_measure_lbl"]!!,
                measure = getFormattedLastMeasureInfo(hist.exec_value, measureValueSufix, restrictionDecimal),
                materialLbl = hist.getMaterialLbl(hmAuxTrans) ?: "",
                material = hist.hasMaterialApplied(hmAuxTrans) ?: "",
                comment = hist.exec_comment,
                exec_type = hist.exec_type
            )
        }
        //
        if(toAlertList.isNotEmpty()){
            toAlertList.filter {
                it.exec_type == GeOsDeviceItem.EXEC_TYPE_ALERT
            }.forEachIndexed { index, s ->
                if(index == 0)
                    s.titleLbl = hmAuxTrans["has_problem_lbl"] ?: ""
            }
        }
        //
        return toAlertList.toMutableList()
    }


    override fun getFormattedLastMeasureInfo(
        lastFixed: Float,
        measureValueSufix: String?,
        restrictionDecimal: Int?
    ): String {
        return "${ToolBox_Inf.convertFloatToBigDecimalString(
            lastFixed,true
        )}${if(measureValueSufix != null) " ".plus(measureValueSufix) else ""}"
    }
}