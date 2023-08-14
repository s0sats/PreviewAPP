package com.namoadigital.prj001.ui.act092.data.local.preferences

import android.content.SharedPreferences
import com.namoadigital.prj001.core.data.domain.preference.ModelPreferences
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
            .putString(SERIAL_MODEL_TEXT_FILTER, model.editFilter ?: "")
            .putBoolean(SERIAL_MODEL_MAIN_USER_FILTER, model.mainUserFocus)
            .putBoolean(SERIAL_MODEL_OTHER_ACTION_FILTER, model.otherSerialIsFiltered)
            .apply()
    }

    override fun read(): SerialModel {
        with(preferences) {

            val serialCode = if (getLong(SERIAL_MODEL_SERIAL_CODE, -1L) == -1L) null else getLong(
                SERIAL_MODEL_SERIAL_CODE,
                -1L
            )
            val tagOperCode = if (getInt(SERIAL_MODEL_TAG_OPER_CODE, -1) == -1) null else getInt(
                SERIAL_MODEL_TAG_OPER_CODE,
                -1
            )
            val productCode = if (getInt(SERIAL_MODEL_PRODUCT_CODE, -1) == -1) null else getInt(
                SERIAL_MODEL_PRODUCT_CODE,
                -1
            )

            val editFilter = if (getString(SERIAL_MODEL_TEXT_FILTER, "") == "") null else
                getString(SERIAL_MODEL_TEXT_FILTER, "")

            return SerialModel(
                originFlow = getString(SERIAL_MODEL_ORIGIN_FLOW, null),
                tagOperCode = tagOperCode,
                productCode = productCode,
                productId = getString(SERIAL_MODEL_PRODUCT_ID, null),
                productDesc = getString(SERIAL_MODEL_PRODUCT_DESC, null),
                serialCode = serialCode,
                serialId = getString(SERIAL_MODEL_SERIAL_ID, null),
                ticketId = getString(SERIAL_MODEL_TICKET_ID, null),
                calendarDate = getString(SERIAL_MODEL_CALENDAR_DATE, null),
                lastSelectedPk = getString(SERIAL_MODEL_LAST_SELECT_PK, null),
                lastSelectActionType = getString(SERIAL_MODEL_LAST_SELECT_ACTION_TYPE, null),
                classColor = getString(SERIAL_MODEL_CLASS_COLOR, null),
                mainUserFocus = getBoolean(SERIAL_MODEL_MAIN_USER_FILTER, false),
                editFilter = editFilter,
                otherSerialIsFiltered = getBoolean(SERIAL_MODEL_OTHER_ACTION_FILTER, false),
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
        const val SERIAL_MODEL_MAIN_USER_FILTER = "act092_serial_model_main_user_filter"
        const val SERIAL_MODEL_TEXT_FILTER = "act092_serial_model_text_filter"
        const val SERIAL_MODEL_OTHER_ACTION_FILTER = "act092_serial_model_other_action_filter"
    }
}