package com.namoadigital.prj001.ui.act083.data.local.preferences

import android.content.SharedPreferences
import com.namoadigital.prj001.core.data.local.preferences.ModelPreferences
import com.namoadigital.prj001.ui.act083.model.SaveActionFilterModel
import com.namoadigital.prj001.util.ConstantBaseApp

class MyActionsFilterParamPreferences constructor(
    private val preferences: SharedPreferences
) : ModelPreferences<SaveActionFilterModel> {
    override fun write(model: SaveActionFilterModel) {
        preferences.edit()
            .putString(ACTION_FILTER_ORIGIN_FLOW, model.originFlow)
            .putString(ACTION_FILTER_SITE_CODE_BACK, model.siteCodeBack)
            .putString(ACTION_FILTER_INITIAL_TEXT_FILTER, model.initialTextFilter)
            .putString(ACTION_FILTER_TAG_FILTER_DESC, model.tagFilterDesc)
            .putString(ACTION_FILTER_LAST_SELECT_ACTION_PK, model.lastSelectActionPk)
            .putString(ACTION_FILTER_LAST_SELECT_ACTION_TYPE, model.lastSelectedActionType)
            .putString(ACTION_FILTER_SERIAL_ID, model.serialId)
            .putString(ACTION_FILTER_TICKET_ID, model.ticketId)
            .putString(ACTION_FILTER_CALENDAR_DATE, model.calendarDate)
            .putString(ACTION_FILTER_PRODUCT_DESC, model.productDesc)
            .putString(ACTION_FILTER_PRODUCT_ID, model.productId)
            .putString(ACTION_FILTER_SITE_CODE, model.siteCode)
            .putLong(ACTION_FILTER_SERIAL_CODE, model.serialCode ?: -1L)
            .putInt(ACTION_FILTER_ZONE_CODE_BACK, model.zoneCodeBack ?: 0)
            .putInt(ACTION_FILTER_INITIAL_TAB_LOAD, model.initialTabToLoad)
            .putInt(ACTION_FILTER_TAG_FILTER, model.tagFilter ?: -1)
            .putInt(ACTION_FILTER_PRODUCT_CODE, model.productCode ?: -1)
            .putBoolean(ACTION_FILTER_MAIN_USER_FILTER_STATE, model.mainUserFilterState)
            .apply()
    }

    override fun read(): SaveActionFilterModel {
        with(preferences) {
            val serialCode = if (getLong(ACTION_FILTER_SERIAL_CODE, -1L) == -1L) null else getLong(
                ACTION_FILTER_SERIAL_CODE,
                -1L
            )
            val tagFilter = if (getInt(ACTION_FILTER_TAG_FILTER, -1) == -1) null else getInt(
                ACTION_FILTER_TAG_FILTER,
                -1
            )
            val productCode = if (getInt(ACTION_FILTER_PRODUCT_CODE, -1) == -1) null else getInt(
                ACTION_FILTER_PRODUCT_CODE,
                -1
            )

            return SaveActionFilterModel(
                originFlow = getString(ACTION_FILTER_ORIGIN_FLOW, null) ?: ConstantBaseApp.ACT005,
                siteCode = getString(ACTION_FILTER_SITE_CODE, null),
                initialTextFilter = getString(ACTION_FILTER_INITIAL_TEXT_FILTER, null),
                tagFilterDesc = getString(ACTION_FILTER_TAG_FILTER_DESC, null),
                productDesc = getString(ACTION_FILTER_PRODUCT_DESC, null),
                productId = getString(ACTION_FILTER_PRODUCT_ID, null),
                lastSelectActionPk = getString(ACTION_FILTER_LAST_SELECT_ACTION_PK, null),
                lastSelectedActionType = getString(ACTION_FILTER_LAST_SELECT_ACTION_TYPE, null),
                serialId = getString(ACTION_FILTER_SERIAL_ID, null),
                serialCode = serialCode,
                ticketId = getString(ACTION_FILTER_TICKET_ID, null),
                calendarDate = getString(ACTION_FILTER_CALENDAR_DATE, null),
                siteCodeBack = getString(ACTION_FILTER_SITE_CODE_BACK, null),
                zoneCodeBack = getInt(ACTION_FILTER_ZONE_CODE_BACK, 0),
                initialTabToLoad = getInt(ACTION_FILTER_INITIAL_TAB_LOAD, 1),
                tagFilter = tagFilter,
                productCode = productCode
            )
        }
    }

    companion object {
        const val ACTION_FILTER_ORIGIN_FLOW = "act083_action_filter_origin_flow"
        const val ACTION_FILTER_SITE_CODE_BACK = "act083_action_filter_serial_code_back"
        const val ACTION_FILTER_INITIAL_TEXT_FILTER = "act083_action_filter_initial_text_filter"
        const val ACTION_FILTER_LAST_SELECT_ACTION_PK = "act083_action_filter_last_select_action_pk"
        const val ACTION_FILTER_LAST_SELECT_ACTION_TYPE =
            "act083_action_filter_last_select_action_type"
        const val ACTION_FILTER_SERIAL_ID = "act083_action_filter_serial_id"
        const val ACTION_FILTER_SERIAL_CODE = "act083_action_filter_serial_code"
        const val ACTION_FILTER_TICKET_ID = "act083_action_filter_ticket_id"
        const val ACTION_FILTER_CALENDAR_DATE = "act083_action_filter_calendar_date"
        const val ACTION_FILTER_SITE_CODE = "act083_action_filter_site_code"
        const val ACTION_FILTER_ZONE_CODE_BACK = "act083_action_filter_zone_code_back"
        const val ACTION_FILTER_INITIAL_TAB_LOAD = "act083_action_filter_initial_tab_load"
        const val ACTION_FILTER_TAG_FILTER = "act083_action_filter_tag_filter"
        const val ACTION_FILTER_TAG_FILTER_DESC = "act083_action_filter_tag_filter_desc"
        const val ACTION_FILTER_PRODUCT_CODE = "act083_action_filter_product_code"
        const val ACTION_FILTER_PRODUCT_DESC = "act083_action_filter_product_desc"
        const val ACTION_FILTER_PRODUCT_ID = "act083_action_filter_product_id"
        const val ACTION_FILTER_MAIN_USER_FILTER_STATE =
            "act083_action_filter_main_user_filter_state"
    }
}