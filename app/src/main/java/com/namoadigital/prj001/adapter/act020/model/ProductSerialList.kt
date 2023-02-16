package com.namoadigital.prj001.adapter.act020.model

import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking

data class ProductSerialList(
    val currentSite: Boolean = true,
    val classColor: String,
    val serial: String,
    val product_desc: String,
    val local_control: Int,
    val siteAndZone: Pair<String, String>,
    val serialDescription: Triple<String, String, String>,
    val trackings: List<MD_Product_Serial_Tracking>,
){

    private fun trackingList() = trackings.map { m -> m.tracking }

    companion object {
        const val SEPARATOR = " | "
    }

    fun getSerialDescription() : String{
        val mlist = mutableListOf<String>()

        serialDescription.toList().forEach {
            if(it.isNotEmpty()) mlist.add(it)
        }

        return if(mlist.isEmpty()) "" else mlist.joinToString(SEPARATOR)
    }

    fun getSiteAndZone() : String{
        val mlist = mutableListOf<String>()

        siteAndZone.toList().forEach {
            if(it.isNotEmpty()) mlist.add(it)
        }

        return if(mlist.isEmpty()) "" else mlist.joinToString(SEPARATOR)
    }

    fun getTracking() : String{
        val mlist = mutableListOf<String>()

        trackingList().forEach {
            if(it.isNotEmpty()) mlist.add(it)
        }

        return if(mlist.isEmpty()) "" else mlist.joinToString(SEPARATOR)
    }


    fun getAllFieldForFilter() : String {
        var trackingFilter = ""
        trackings.forEach {
            if(it.tracking.isNotEmpty()){
                trackingFilter += "${it.tracking}|"
            }
        }
        return "$serial|$product_desc|" +
                "${siteAndZone.first}|${siteAndZone.second}|" +
                "${serialDescription.first}|${serialDescription.second}|${serialDescription.third}|" +
                trackingFilter
    }
}

fun MD_Product_Serial.toAdapterList(site_preference: Int = -1) : ProductSerialList = ProductSerialList(
    currentSite = (site_code ?: -1) == site_preference,
    classColor = class_color ?: "",
    serial = serial_id ?: "",
    product_desc = product_desc ?: "",
    local_control = local_control ?: 0,
    siteAndZone = if (site_desc == zone_desc) Pair(site_desc ?: "", "") else Pair(
        site_desc ?: "",
        zone_desc ?: ""
    ),
    serialDescription = Triple(brand_desc ?: "", model_desc ?: "", color_desc ?: ""),
    trackings = tracking_list ?: emptyList()
)