package com.namoadigital.prj001.ui.act093.model

import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking
import com.namoadigital.prj001.ui.act093.model.InfoSerialModel.Companion.SEPARATOR

data class InfoSerialModel(
    val iconColor: String? = null,
    val serialId: String? = null,
    val productDesc: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val color: String? = null,
    val tracklist: String? = null,
    val last_measure_value: String? = null,
    val last_measure_date: String? = null,
    val last_cycle_value: String? = null,
    val value_suffix: String? = null,
) {

    companion object {
        const val SEPARATOR = " | "
    }

    fun convertForBrandModelColor(): String? {
        val list = mutableListOf<String>()

        if (brand?.isNotEmpty() == true) list.add(brand)
        if (model?.isNotEmpty() == true) list.add(model)
        if (color?.isNotEmpty() == true) list.add(color)

        return if (list.isEmpty()) null else list.joinToString(SEPARATOR)
    }


    val formatMeasureValue: String? = when {
        !last_measure_value.isNullOrEmpty() -> {
            if (!value_suffix.isNullOrEmpty()) {
                "$last_measure_value $value_suffix"
            } else {
                "$last_measure_value"
            }
        }
        else -> null
    }

    val formatCycleValue: String? = when {
        !last_cycle_value.isNullOrEmpty() -> {
            if (!value_suffix.isNullOrEmpty()) last_cycle_value + value_suffix
            else null
        }
        else -> null
    }


}

fun MD_Product_Serial.toInfoSerialModel() = InfoSerialModel(
    serialId = serial_id,
    productDesc = product_desc,
    brand = brand_desc,
    model = model_desc,
    color = color_desc,
    tracklist = tracking_list.toInfoSerial(),
    last_measure_value = last_measure_value.toInt().toString(),
    last_cycle_value = last_cycle_value.toInt().toString()
)

private fun List<MD_Product_Serial_Tracking>.toInfoSerial(): String? {
    val list = mutableListOf<String>()
    map { m -> m.tracking }
        .forEach {
            if (it.isNotEmpty()) list.add(it)
        }

    return if (list.isEmpty()) null else list.joinToString(SEPARATOR)
}
