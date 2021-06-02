package com.namoadigital.prj001.model

import android.content.Context
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.Serializable
import java.text.SimpleDateFormat

class MyActionFilterParam(
        var tagFilterCode: Int? = null,
        var tagFilterDesc: String? = null,
        var productCode: Int? = null,
        var productId: String? = null,
        var productDesc: String? = null,
        var serialId: String? = null,
        var clientId: String? = null,
        var contractId: String? = null,
        var ticketId: String? = null,
        var calendarDate: String? = null
) : Serializable{
    companion object{
        const val  MY_ACTION_FILTER_PARAM = "MY_ACTION_FILTER_PARAM"
    }

    var paramTextFilter: String? = null
    var paramItemSelectedTab: Int? = null
    var paramItemSelectedType: String? = null
    var paramItemSelectedPk: String? = null
    var paramNcFilter: Boolean? = null

    fun setSelectedItemParams(textFilter: String? = null, selectedTab: Int, selectedType: String, selectedPk: String,ncFilterOn: Boolean? = null){
        paramTextFilter = textFilter
        paramItemSelectedTab = selectedTab
        paramItemSelectedType = selectedType
        paramItemSelectedPk = selectedPk
        paramNcFilter = ncFilterOn
    }

    fun getFilledFilters(context: Context): List<String>{
        val filters = mutableListOf<String>()
//        tagFilterDesc?.let {
//            filters.add(it)
//        }
        productDesc?.let {
            filters.add(it)
        }
        serialId?.let {
            filters.add(it)
        }
        if(!clientId.isNullOrEmpty()) {
            filters.add(clientId!!)
        }
        if(!contractId.isNullOrEmpty()) {
            filters.add(contractId!!)
        }
        if(!ticketId.isNullOrEmpty()) {
            filters.add(ticketId!!)
        }
        calendarDate?.let {
            filters.add(getCalendarDateFormatted(context,it))
        }

        return filters
    }

    private fun getCalendarDateFormatted(context: Context,calendarDate: String): String {
        val dateFormatIn = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatOut: SimpleDateFormat
        var format = ToolBox_Inf.nlsDateFormat(context)
        return try {
            if (format == null || format.equals("", ignoreCase = true)) {
                format = "dd-MM-yyyy"
            }
            dateFormatOut = SimpleDateFormat(format)
            //
            dateFormatOut.format(dateFormatIn.parse(calendarDate))
        } catch (e: Exception) {
            ToolBox_Inf.registerException(MyActionFilterParam::class.java.name, e)
            "01-01-1900"
        }
    }
}