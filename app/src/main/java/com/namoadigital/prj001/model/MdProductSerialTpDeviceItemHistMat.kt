package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class MdProductSerialTpDeviceItemHistMat(
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("product_code") val product_code: Long,
    @SerializedName("serial_code") val serial_code: Long,
    @SerializedName("device_tp_code") val device_tp_code: Int,
    @SerializedName("item_check_seq") val item_check_seq: Int,
    @SerializedName("item_check_code") val item_check_code: Int,
    @SerializedName("seq") val seq: Int,
    @SerializedName("material_code") val material_code: Int,
    @SerializedName("un") val un: String,
    @SerializedName("qty") val qty: Double,
    @SerializedName("qty_planned") val qty_planned: Double,
    @SerializedName("material_action") val material_action: Int
) {

    companion object {
        @JvmStatic
        fun getSerialDeviceTpItemHistMaterialFromList(
            serial: MD_Product_Serial,
            deviceItemHistMatList: List<MdProductSerialTpDeviceItemHistMat>
        ): ArrayList<MdProductSerialTpDeviceItemHistMat> {
            return deviceItemHistMatList.filter {
                it.customer_code == serial.customer_code
                        && it.product_code == serial.product_code
                        && it.serial_code == serial.serial_code
            } as ArrayList
        }
    }

}
