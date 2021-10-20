package com.namoadigital.prj001.ui.act086.frg_historic

import com.namoadigital.prj001.model.Act086HistoricAlert
import com.namoadigital.prj001.model.GeOsDeviceItemHist
import java.util.ArrayList

interface Act086HistoricFrgContract {

    interface IView{

    }

    interface IPresenter{
        fun getAlertList(itemHist: ArrayList<GeOsDeviceItemHist>, measureValueSufix: String?, restrictionDecimal: Int?) :MutableList<Act086HistoricAlert>
        fun getFormattedLastMeasureInfo(lastFixed: GeOsDeviceItemHist, measureValueSufix: String?,restrictionDecimal: Int?): String
    }

}