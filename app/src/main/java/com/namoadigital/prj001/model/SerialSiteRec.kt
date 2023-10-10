package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class SiteSerialInvRec(
    @SerializedName("site_serial_inv")
    var serialSiteInventory: List<SerialSiteInventory>?,
) : Main_Header_Rec()

data class SerialSiteInventory(

    @SerializedName("product_code") var productCode: Int,
    @SerializedName("serial_code") var serialCode: Int,
    @SerializedName("serial_id") var serialId: String,
    @SerializedName("class_code") var classCode: Int?,
    @SerializedName("class_id") var classId: String?,
    @SerializedName("class_color") var classColor: String?,
    @SerializedName("add_inf1") var addInf1: String?,
    @SerializedName("brand_code") var brandCode: String?,
    @SerializedName("brand_desc") var brandDesc: String?,
    @SerializedName("model_code") var modelCode: String?,
    @SerializedName("model_desc") var modelDesc: String?,
    @SerializedName("color_code") var colorCode: String?,
    @SerializedName("color_desc") var colorDesc: String?,
    @SerializedName("zone_code") var zoneCode: Int,
    @SerializedName("zone_desc") var zoneDesc: String,
    @SerializedName("measure_value") var measureValue: String?,
    @SerializedName("measure_date") var measureDate: String?,
    @SerializedName("preventive_cycle") var preventiveCycle: String?,
    @SerializedName("preventive_date") var preventiveDate: String?,
    @SerializedName("suggested_date") var suggestedDate: String?,
    @SerializedName("suggested_cycle") var suggestedCycle: String?,
    @SerializedName("has_item_check") var hasItemCheck: Int?,
    @SerializedName("tot_alert") var totAlert: Int?,
    @SerializedName("tot_exp_critical") var totExpCritical: Int?,
    @SerializedName("tot_exp") var totExp: Int?,
    @SerializedName("cnt_tkt") var cntTkt: Int?,
    @SerializedName("value_sufix") var valueSufix: String?

) : MyActionsBase() {


    fun getAllFieldForFilter(): String {
        return listOf(
            serialId,
            brandDesc,
            modelDesc,
            colorDesc,
            addInf1,
            measureValue,
            preventiveCycle,
            preventiveDate,
            suggestedCycle,
            suggestedDate,
        ).joinToString("|")
    }

    companion object {


        sealed class OnClickType {

            data class OnSerialClick(val model: SerialSiteInventory, val position: Int) :
                OnClickType()

            data class OnStatusClick(val model: SerialSiteInventory, val position: Int) :
                OnClickType()

        }

    }
}