package com.namoadigital.prj001.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.util.ToolBox_Inf

class MD_Product_Serial_Tp_Device_Item_Hist(
    @SerializedName("seq") val seq: Int,
    @SerializedName("exec_type") val exec_type: String,
    @SerializedName("exec_value") val exec_value: Double,
    @SerializedName("exec_date") val exec_date: String,
    @SerializedName("exec_comment") val exec_comment: String?,
    @SerializedName("exec_photo1") val exec_photo1: String?,
    @SerializedName("exec_photo2") val exec_photo2: String?,
    @SerializedName("exec_photo3") val exec_photo3: String?,
    @SerializedName("exec_photo4") val exec_photo4: String?,
    @SerializedName("exec_material") val exec_material: Int,
    @SerializedName("change_adjust") val change_adjust: Int,
) {
    @SerializedName("customer_code")
    var customer_code: Long = -1
        private set
    @SerializedName("product_code")
    var product_code: Long = -1
        private set
    @SerializedName("serial_code")
    var serial_code: Long = -1
        private set
    @SerializedName("device_tp_code")
    var device_tp_code: Int = -1
        private set
    @SerializedName("item_check_code")
    var item_check_code: Int = -1
        private set
    @SerializedName("item_check_seq")
    var item_check_seq: Int = -1
        private set

    constructor(
        customer_code: Long,
        product_code: Long,
        serial_code: Long,
        device_tp_code: Int,
        item_check_code: Int,
        item_check_seq: Int,
        seq: Int,
        exec_type: String,
        exec_value: Double,
        exec_date: String,
        exec_comment: String?,
        exec_photo1: String?,
        exec_photo2: String?,
        exec_photo3: String?,
        exec_photo4: String?,
        exec_material: Int,
        change_adjust: Int
    ) : this(
        seq,
        exec_type,
        exec_value,
        exec_date,
        exec_comment,
        exec_photo1,
        exec_photo2,
        exec_photo3,
        exec_photo4,
        exec_material,
        change_adjust
    ){
        this.customer_code = customer_code
        this.product_code = product_code
        this.serial_code = serial_code
        this.device_tp_code = device_tp_code
        this.item_check_code = item_check_code
        this.item_check_seq = item_check_seq
    }

    fun setPk(item: MD_Product_Serial_Tp_Device_Item){
        this.customer_code = item.customer_code
        this.product_code = item.product_code
        this.serial_code = item.serial_code
        this.device_tp_code = item.device_tp_code
        this.item_check_code = item.item_check_code
        this.item_check_seq = item.item_check_seq
    }

    fun getIcon() = when(exec_type){
        GeOsDeviceItem.EXEC_TYPE_FIXED -> Pair(R.drawable.ic_build_black_24dp, R.color.namoa_os_form_done_action_blue)

        GeOsDeviceItem.EXEC_TYPE_ADJUST -> Pair(R.drawable.ic_build_black_24dp, R.color.namoa_color_gray_8)

        else -> Pair(R.drawable.ic_outline_report_problem_24_black, R.color.namoa_os_form_problem_red)
    }

    fun getTitleFormated(hmAux: HMAux) = when(exec_type) {
        GeOsDeviceItem.EXEC_TYPE_FIXED -> if (change_adjust == 1) {
            hmAux["change_lbl"]
        } else {
            hmAux["fixed_lbl"]
        }

        GeOsDeviceItem.EXEC_TYPE_ALERT -> hmAux["still_with_problem_lbl"]

        GeOsDeviceItem.EXEC_TYPE_ADJUST -> hmAux["adjust_lbl"]

        else -> ""
    }

    fun getDate(context: Context) = ToolBox_Inf.millisecondsToString(
        ToolBox_Inf.dateToMilliseconds(
            exec_date
        ),
        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
    )

    fun getMaterialLbl(hmAux: HMAux) =
        when(exec_type){
            GeOsDeviceItem.EXEC_TYPE_ALERT -> hmAux["material_requested_lbl"]
            else -> hmAux["material_applied_lbl"]
        }

    fun hasMaterialApplied(hmAux: HMAux) = when(exec_material){
        1 -> hmAux["YES"]
        else -> hmAux["NO"]
    }

    companion object{
        @JvmStatic
        fun getSerialDeviceTpItemHistFromList(serial: MD_Product_Serial, deviceItemHistList: ArrayList<MD_Product_Serial_Tp_Device_Item_Hist>) : ArrayList<MD_Product_Serial_Tp_Device_Item_Hist> {
            return deviceItemHistList.filter {
                it.customer_code == serial.customer_code
                        && it.product_code == serial.product_code
                        && it.serial_code == serial.serial_code
            } as ArrayList
        }
    }
}