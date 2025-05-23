package com.namoadigital.prj001.model.masterdata.product_serial.verification_group

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.MD_Product_Serial

data class MDProductSerialVg(
    @SerializedName("customer_code") var customerCode: Long = -1L,
    @SerializedName("product_code") var productCode: Int = -1,
    @SerializedName("serial_code") var serialCode: Int = -1,
    @SerializedName("vg_code") var vgCode: Int = -1,
    @SerializedName("next_cycle_measure") val nextCycleMeasure: Float? = null,
    @SerializedName("next_cycle_measure_date") val nextCycleMeasureDate: String? = null,
    @SerializedName("next_cycle_limit_date") val nextCycleLimitDate: String? = null,
    @SerializedName("last_execution_measure") val lastExecutionMeasure: Float? = null,
    @SerializedName("last_execution_date") val lastExecutionDate: String? = null,
    @SerializedName("vg_status") var vgStatus: String? = null,
    @SerializedName("target_date") val targetDate: String? = null,
    @SerializedName("manual_date") val manualDate: String? = null,
    @SerializedName("partitioned_ticket_prefix") val partitionedTicketPrefix: Int? = null,
    @SerializedName("partitioned_ticket_code") val partitionedTicketCode: Int? = null,
    @SerializedName("partitioned_user") val partitionedUser: String? = null,
    @SerializedName("partitioned_execution") val partitionedExecution: Int = 0,
) {
    fun updatePk(mdProductSerial: MD_Product_Serial) {
        this.customerCode = mdProductSerial.customer_code
        this.productCode = mdProductSerial.product_code.toInt()
        this.serialCode = mdProductSerial.serial_code.toInt()
    }

    companion object {
        @JvmStatic
        fun getSerialVGFromList(
            productSerial: MD_Product_Serial,
            mdProductSerialVgs: ArrayList<MDProductSerialVg>
        ): List<MDProductSerialVg> {
            return mdProductSerialVgs.filter {
                it.customerCode == productSerial.customer_code &&
                        it.productCode == productSerial.product_code.toInt() &&
                        it.serialCode == productSerial.serial_code.toInt()
            }
        }
    }


}