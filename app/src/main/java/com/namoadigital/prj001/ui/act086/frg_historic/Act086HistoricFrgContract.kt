package com.namoadigital.prj001.ui.act086.frg_historic

import com.namoadigital.prj001.model.Act086HistoricModel
import com.namoadigital.prj001.model.GeOsDeviceItemHist
import java.util.ArrayList

interface Act086HistoricFrgContract {

    interface IView{

    }

    interface IPresenter{
        fun getAlertList(itemHist: ArrayList<GeOsDeviceItemHist>, measureValueSufix: String?, restrictionDecimal: Int?) :MutableList<Act086HistoricModel>
        fun getFormattedLastMeasureInfo(
            lastFixed: Float, measureValueSufix: String?,
            restrictionDecimal: Int?): String
    }

}