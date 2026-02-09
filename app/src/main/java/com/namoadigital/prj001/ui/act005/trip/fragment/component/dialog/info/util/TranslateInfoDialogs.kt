package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.trip.domain.model.enums.TimelineBlockTranslate
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.DialogEventTrip.Companion.DIALOG_EVENT_RETRY_IMAGE_LBL
import com.namoadigital.prj001.util.ToolBox_Inf

object TranslateInfoDialogs {

    const val EXTRACT_DIALOG_INFO_RESOURCE = "extract_dialog_info_resource"


    //ORIGIN
    const val DIALOG_ORIGIN_TITLE_LBL = "dialog_origin_title_lbl"
    const val DIALOG_START_TRIP_TITLE_LBL = "dialog_start_trip_title_lbl"
    const val DIALOG_TYPE_GPS_LBL = "dialog_type_gps_lbl"
    const val DIALOG_DATE_START_LBL = "dialog_date_start_lbl"
    const val DIALOG_DATE_END_LBL = "dialog_date_end_lbl"
    const val DIALOG_ORIGIN_START_LBL = "dialog_origin_start_lbl"


    //DESTINATION
    const val DIALOG_DESTINATION_TITLE_LBL = "dialog_destination_title_lbl"
    const val DIALOG_DATE_ARRIVAL_LBL = "dialog_date_arrival_lbl"
    const val DIALOG_DATE_DEPARTED_LBL = "dialog_date_departed_lbl"
    const val DIALOG_LOCAL_LBL = "dialog_local_lbl"
    const val DIALOG_DESTINATION_OVER_NIGHT_LBL = "dialog_destination_over_night_lbl"

    //GENERIC
    const val DIALOG_DATE_HINT = "dialog_date_hint"
    const val DIALOG_HOUR_HINT = "dialog_hour_hint"
    const val DIALOG_FLEET_INFO_LBL = "dialog_fleet_info_lbl"
    const val DIALOG_FLEET_PLATE_HINT = "dialog_fleet_plate_hint"
    const val DIALOG_ODOMETER_HINT = "dialog_odometer_hint"
    const val DIALOG_ODOMETER_PHOTO_BTN = "dialog_odometer_photo_btn"
    const val DIALOG_SAVE_BTN = "dialog_save_btn"
    const val DIALOG_ERROR_FLEET_PLATE_LBL = "dialog_error_fleet_plate_lbl"
    const val DIALOG_ERROR_ODOMETER_LBL = "dialog_error_odometer_lbl"
    const val DIALOG_DATE_START_EXCEEDED_TRIP_LBL = "dialog_date_start_exceeded_trip_lbl"
    const val DIALOG_DATE_START_EXCEEDED_END_DATE_DESTINATION_LBL =
        "dialog_date_start_exceeded_end_date_destination_lbl"
    const val DIALOG_DATE_END_EXCEEDED_START_DATE_DESTINATION_LBL =
        "dialog_date_end_exceeded_start_date_destination_lbl"
    const val DIALOG_VALUE_SHOULD_BE_HIGHER_THAN_LBL = "dialog_value_should_be_higher_than_lbl"
    const val DIALOG_VALUE_SHOULD_BE_LOWER_THAN_LBL = "dialog_value_should_be_lower_than_lbl"
    const val DIALOG_VALUE_SHOULD_BE_HIGHER_THAN_DATE_LBL =
        "dialog_value_should_be_higher_than_date_lbl"
    const val DIALOG_VALUE_SHOULD_BE_LOWER_THAN_DATE_LBL =
        "dialog_value_should_be_lower_than_date_lbl"
    const val DIALOG_RETRY_IMAGE_LBL = "dialog_retry_image_lbl"
    const val DIALOG_ERROR_FUTURE_DATE = "dialog_error_future_date"

    const val PROCESS_DIALOG_START_DATE_TITLE = "process_dialog_start_date_title"
    const val PROCESS_DIALOG_START_DATE_MSG = "process_dialog_start_date_msg"

    //END TRIP
    const val DIALOG_ERROR_DATE_END_TRIP_LBL = "dialog_error_date_end_trip_lbl"
    const val DIALOG_ERROR_DATE_END_TRIP_FUTURE_LBL = "dialog_error_date_end_trip_lbl"


    private fun Context.getResource(): String = ToolBox_Inf.getResourceCode(
        this,
        TripBaseFragment.MODULE_CODE,
        EXTRACT_DIALOG_INFO_RESOURCE
    )

    fun   loadTranslation(context: Context): HMAux {
        listOf(
            DIALOG_ORIGIN_TITLE_LBL,
            DIALOG_TYPE_GPS_LBL,
            DIALOG_DATE_START_LBL,
            DIALOG_ORIGIN_START_LBL,
            DIALOG_DESTINATION_TITLE_LBL,
            DIALOG_DATE_ARRIVAL_LBL,
            DIALOG_DATE_DEPARTED_LBL,
            DIALOG_LOCAL_LBL,
            DIALOG_DATE_HINT,
            DIALOG_HOUR_HINT,
            DIALOG_FLEET_INFO_LBL,
            DIALOG_FLEET_PLATE_HINT,
            DIALOG_ODOMETER_HINT,
            DIALOG_ODOMETER_PHOTO_BTN,
            DIALOG_SAVE_BTN,
            DIALOG_ERROR_FLEET_PLATE_LBL,
            DIALOG_ERROR_ODOMETER_LBL,
            DIALOG_DESTINATION_OVER_NIGHT_LBL,
            DIALOG_EVENT_RETRY_IMAGE_LBL,
            DIALOG_DATE_START_EXCEEDED_END_DATE_DESTINATION_LBL,
            DIALOG_DATE_END_EXCEEDED_START_DATE_DESTINATION_LBL,
            DIALOG_VALUE_SHOULD_BE_HIGHER_THAN_LBL,
            DIALOG_VALUE_SHOULD_BE_LOWER_THAN_LBL,
            DIALOG_VALUE_SHOULD_BE_HIGHER_THAN_DATE_LBL,
            DIALOG_VALUE_SHOULD_BE_LOWER_THAN_DATE_LBL,
            DIALOG_ERROR_FUTURE_DATE,
            DIALOG_RETRY_IMAGE_LBL,
            DIALOG_START_TRIP_TITLE_LBL,
            DIALOG_DATE_END_LBL,
            DIALOG_ERROR_DATE_END_TRIP_LBL,
            DIALOG_ERROR_DATE_END_TRIP_FUTURE_LBL,
        ).let { list ->
            val newList = mutableListOf<String>()
            newList.addAll(list)
            newList.addAll(TimelineBlockTranslate.entries.map { it.key })
            return TranslateResource(
                context,
                TripBaseFragment.MODULE_CODE,
                context.getResource()
            ).setLanguage(newList)
        }

    }
}