package com.namoadigital.prj001.ui.act093.model

import android.content.Context
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking
import com.namoadigital.prj001.ui.act093.model.InfoSerialModel.Companion.SEPARATOR
import com.namoadigital.prj001.util.ToolBox_Inf

data class InfoSerialModel(
    val iconColor: String? = null,
    val serialId: String? = null,
    val productDesc: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val color: String? = null,
    val tracklist: String? = null,
    val infoAdd: String? = null,
    val last_measure_value: Int? = null,
    val last_measure_date: String? = null,
    val last_cycle_value: Int? = null,
    val last_cycle_date: String? = null,
    val value_suffix: String? = null,
    val lastUpdateSerial: String? = null,
) {

    companion object {
        const val SEPARATOR = " | "


        fun Int?.formatMeasureValue(value_suffix: String?): String? = when {
            this != null -> {
                if (!value_suffix.isNullOrEmpty()) {
                    "$this $value_suffix"
                } else {
                    "$this"
                }
            }
            else -> null
        }

        fun String?.formatMeasureValue(value_suffix: String?): String? = when {
            this != null -> {
                if (!value_suffix.isNullOrEmpty()) {
                    "$this $value_suffix"
                } else {
                    "$this"
                }
            }
            else -> null
        }

        fun Int?.formatCycleValue(value_suffix: String?): String? = when {
            this != null -> {
                if (!value_suffix.isNullOrEmpty()) "$this  $value_suffix"
                else "$this"
            }
            else -> null
        }

        fun MutableList<String?>.formatInfoAdd(): String? {
            val list = mutableListOf<String>()
            forEach {
                if (!it.isNullOrEmpty()) list.add(it)
            }

            return if (list.isEmpty()) null else list.joinToString(SEPARATOR)
        }
    }

    fun convertForBrandModelColor(): String? {
        val list = mutableListOf<String>()

        if (brand?.isNotEmpty() == true) list.add(brand)
        if (model?.isNotEmpty() == true) list.add(model)
        if (color?.isNotEmpty() == true) list.add(color)

        return if (list.isEmpty()) null else list.joinToString(SEPARATOR)
    }

}

fun MD_Product_Serial.toInfoSerialModel(context: Context) = InfoSerialModel(
    serialId = serial_id,
    productDesc = product_desc,
    brand = brand_desc,
    model = model_desc,
    color = color_desc,
    tracklist = tracking_list?.toInfoSerial(),
    last_measure_value = last_measure_value?.toInt(),
    last_cycle_value = last_cycle_value?.toInt(),
    lastUpdateSerial = ToolBox_Inf.millisecondsToString(
        ToolBox_Inf.dateToMilliseconds(log_date),
        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
    )
)

private fun List<MD_Product_Serial_Tracking>.toInfoSerial(): String? {
    val list = mutableListOf<String>()
    map { m -> m.tracking }
        .forEach {
            if (it.isNotEmpty()) list.add(it)
        }

    return if (list.isEmpty()) null else list.joinToString(SEPARATOR)
}
