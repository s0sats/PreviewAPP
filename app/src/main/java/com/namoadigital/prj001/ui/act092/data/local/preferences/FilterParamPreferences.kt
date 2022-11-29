package com.namoadigital.prj001.ui.act092.data.local.preferences

import android.content.SharedPreferences
import com.namoadigital.prj001.core.data.local.preferences.ModelPreferences
import com.namoadigital.prj001.ui.act092.model.SerialModel

class FilterParamPreferences constructor(
    private val preferences: SharedPreferences
) : ModelPreferences<SerialModel> {
    override fun write(model: SerialModel) {
        preferences.edit()
            .putString(SERIAL_MODEL_ORIGIN_FLOW, model.originFlow)
            .putInt(SERIAL_MODEL_TAG_OPER_CODE, model.tagOperCode ?: -1)
            .putInt(SERIAL_MODEL_PRODUCT_CODE, model.productCode ?: -1)
            .putString(SERIAL_MODEL_PRODUCT_ID, model.productId)
            .putString(SERIAL_MODEL_PRODUCT_DESC, model.productDesc)
            .putLong(SERIAL_MODEL_SERIAL_CODE, model.serialCode ?: -1L)
            .putString(SERIAL_MODEL_SERIAL_ID, model.serialId)
            .putString(SERIAL_MODEL_TICKET_ID, model.ticketId)
            .putString(SERIAL_MODEL_CALENDAR_DATE, model.calendarDate)
            .putString(SERIAL_MODEL_LAST_SELECT_PK, model.lastSelectedPk)
            .putString(SERIAL_MODEL_LAST_SELECT_ACTION_TYPE, model.lastSelectActionType)
            .putString(SERIAL_MODEL_CLASS_COLOR, model.classColor)
            .apply()
    }

    override fun read(): SerialModel {
        with(preferences) {
            return SerialModel(
                originFlow = getString(SERIAL_MODEL_ORIGIN_FLOW, ""),
                tagOperCode = getInt(SERIAL_MODEL_TAG_OPER_CODE, -1),
                productCode = getInt(SERIAL_MODEL_PRODUCT_CODE, -1),
                productId = getString(SERIAL_MODEL_PRODUCT_ID, ""),
                productDesc = getString(SERIAL_MODEL_PRODUCT_DESC, ""),
                serialCode = getLong(SERIAL_MODEL_SERIAL_CODE, -1L),
                serialId = getString(SERIAL_MODEL_SERIAL_ID, ""),
                ticketId = getString(SERIAL_MODEL_TICKET_ID, ""),
                calendarDate = getString(SERIAL_MODEL_CALENDAR_DATE, ""),
                lastSelectedPk = getString(SERIAL_MODEL_LAST_SELECT_PK, ""),
                lastSelectActionType = getString(SERIAL_MODEL_LAST_SELECT_ACTION_TYPE, ""),
                classColor = getString(SERIAL_MODEL_CLASS_COLOR, "") ?: ""
            )
        }
    }

    companion object {
        const val SERIAL_MODEL_ORIGIN_FLOW = "act092_serial_model_origin_flow"
        const val SERIAL_MODEL_TAG_OPER_CODE = "act092_serial_model_tag_oper_code"
        const val SERIAL_MODEL_PRODUCT_CODE = "act092_serial_model_product_code"
        const val SERIAL_MODEL_PRODUCT_ID = "act092_serial_model_product_id"
        const val SERIAL_MODEL_PRODUCT_DESC = "act092_serial_model_product_desc"
        const val SERIAL_MODEL_SERIAL_CODE = "act092_serial_model_serial_code"
        const val SERIAL_MODEL_SERIAL_ID = "act092_serial_model_serial_id"
        const val SERIAL_MODEL_TICKET_ID = "act092_serial_model_ticket_id"
        const val SERIAL_MODEL_CALENDAR_DATE = "act092_serial_model_calendar_date"
        const val SERIAL_MODEL_LAST_SELECT_PK = "act092_serial_model_last_select_pk"
        const val SERIAL_MODEL_LAST_SELECT_ACTION_TYPE =
            "act092_serial_model_last_select_action_type"
        const val SERIAL_MODEL_CLASS_COLOR = "act092_serial_model_class_color"
    }
}