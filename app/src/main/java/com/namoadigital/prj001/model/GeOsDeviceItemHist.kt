package com.namoadigital.prj001.model

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.Serializable

class GeOsDeviceItemHist(
    val customer_code: Long,
    val custom_form_type: Int,
    val custom_form_code: Int,
    val custom_form_version: Int,
    val custom_form_data: Int,
    val product_code: Int,
    val serial_code: Int,
    val device_tp_code: Int,
    val item_check_code: Int,
    val item_check_seq: Int,
    val seq: Int,
    val exec_type: String,
    val exec_value: Float,
    val exec_date: String,
    val exec_comment: String?,
    val exec_material: Int,
    val change_adjust: Int
) : Serializable {


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
}