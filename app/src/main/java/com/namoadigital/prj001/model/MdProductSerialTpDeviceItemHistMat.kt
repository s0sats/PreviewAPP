package com.namoadigital.prj001.model

data class MdProductSerialTpDeviceItemHistMat(
    val customer_code: Long,
    val product_code: Long,
    val serial_code: Long,
    val item_check_seq: Int,
    val seq: Int,
    val material_code: Int,
    val un: String,
    val qty: Double,
    val qty_planned: Double,
    val material_action: Int
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
